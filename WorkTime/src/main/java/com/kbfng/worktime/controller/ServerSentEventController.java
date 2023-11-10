package com.kbfng.worktime.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.kbfng.worktime.util.WorkTimeUtils;
import com.kbfng.worktime.util.SseEmitters;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController 
@RequiredArgsConstructor
public class ServerSentEventController {

	// SSE 만료시간
	private static final Long DEFAULT_TIMEOUT = 60 * 1000L * 3;
	// SSE 처리객체
	private final SseEmitters sseEmitters;
	
	@GetMapping(value = "/sseConnect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)  
    public ResponseEntity<SseEmitter> connect(HttpServletRequest request, @RequestParam String sessionId) {
        
		log.info("@@@ sessionId: {}", sessionId);
		
		String clientIp = WorkTimeUtils.getLocation(request);
		
		log.info("@@@ clientIp: {}", clientIp);
		
		SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
		sseEmitters.add(sessionId, emitter, clientIp);
        
        emitter.onCompletion(() -> sseEmitters.deleteSSEById(sessionId));
        emitter.onTimeout(() -> sseEmitters.deleteSSEById(sessionId));
        emitter.onError((e) -> sseEmitters.deleteSSEById(sessionId));
        
        try {
        	// emitter는 생성 후 만료시간까지 아무런 데이터를 보내지 않으면
        	// 재연결 요청 시 503 Service Unavailable 에러가 발생할 수 있음
            emitter.send(SseEmitter.event()
                    .name("CONNECT")       // 해당 이벤트의 이름 지정  
                    .data("CONNECTED!"));  // 503에러를 방지하기 위해 더미데이터 송신
        } catch (IOException e) {
            throw new RuntimeException(e);  
        }
        return ResponseEntity.ok(emitter);  
    }  
	
	// Ping 체크
	@GetMapping("/ssePing")
	public ResponseEntity<Object> pingCheck(@RequestParam String sessionId) throws IOException {
		sseEmitters.pingCheck(sessionId);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	/**
	 * Client의 IP정보를 얻어오는 메소드
	 * 모바일에서 직원번호를 얻어오지 못하는 경우 IP대조로 사용자를 식별하기 위함
	 * @param request
	 * @return
	 */
	private String getIp(HttpServletRequest request) {
		 
        String ip = request.getHeader("X-Forwarded-For");
 
        log.info(">>>> X-FORWARDED-FOR : " + ip);
 
        if (ip == null) {
            ip = request.getHeader("Proxy-Client-IP");
            log.info(">>>> Proxy-Client-IP : " + ip);
        }
        if (ip == null) {
            ip = request.getHeader("WL-Proxy-Client-IP"); // 웹로직
            log.info(">>>> WL-Proxy-Client-IP : " + ip);
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_CLIENT_IP");
            log.info(">>>> HTTP_CLIENT_IP : " + ip);
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            log.info(">>>> HTTP_X_FORWARDED_FOR : " + ip);
        }
        if (ip == null) {
            ip = request.getRemoteAddr();
        }
        
        log.info(">>>> Result : IP Address : "+ip);
 
        return ip;
 
    }
}
