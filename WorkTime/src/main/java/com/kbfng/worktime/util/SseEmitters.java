package com.kbfng.worktime.util;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.extern.slf4j.Slf4j;

@Component  
@Slf4j
public class SseEmitters { 
	
	@Autowired
	private SessionManager sessionManager;
	
	// SSE 이벤트처리를 위한 Map
	private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
	
	// Client IP 정보를 가진 Map
	private final Map<String, String> clientIps = new ConcurrentHashMap<>();
  
    public SseEmitter add(String sessionId, SseEmitter emitter, String clientIp) {  
        this.emitters.put(sessionId, emitter);
        this.clientIps.put(sessionId, clientIp);
        log.info("new emitter added: {}", emitter);  
        log.info("emitters size: {}", emitters.size());  
        return emitter;  
    }
    
    public SseEmitter findSSEById(String sessionId) {
    	return emitters.get(sessionId);
    }
    
    public void deleteSSEById(String sessionId) {
        emitters.remove(sessionId);
        sessionManager.remove(sessionId);
    }
    
    public void completeSSEById(String sessionId) {
    	sendToClient(emitters.get(sessionId), "COMPLETE", "COMPLETE_" + sessionId, "COMPLETE_" + sessionId + "_" + now());
    }
    
    public String findIpById(String sessionId) {
    	return clientIps.get(sessionId);
    }
    
    public void deleteIpById(String sessionId) {
    	clientIps.remove(sessionId);
    }
    
	/**
	 * client가 sseEmiter를 종료했는지 확인하기 위해 핑을 체크한다.
	 */
	public void pingCheck(String sessionId) {
		sendPingMessage(sessionId, emitters.get(sessionId));
	}
	
	/**
	 * 10초 간격으로 반복해서 PING을 보내는 함수
	 */
	private void sendPingMessage(String sessionId, SseEmitter emitter) {
		Timer timer = new Timer();

		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				
				// Redis에 sessionId 키를 조회하여 없으면
				if (!emitters.containsKey(sessionId)) {
					// 반복을 종료시키고
					timer.cancel();
					// 종료 메세지를 클라이언트로 보낸다.
					sendToClient(emitter, "FAIL", "FAIL_" + sessionId, "FAIL_" + sessionId + "_" + now());
					// Sse Emitter도 종료 시킨다.
					emitter.complete();
				} 
				// 키가 있다면
				else { 
					
					log.info("Ping Check : {}", sessionId);
					
					// 메세지를 만들어
					String eventKey = sessionId + "_" + System.currentTimeMillis();
					// 핑 메세지를 보낸후 성공 여부를 리턴받는다.
					if (!sendToClient(emitter, "PING", "PING_" + eventKey, now()))
						// 실패한경우 타이머를 종료 시킨다.
						timer.cancel();
				}
			}
		};
		timer.schedule(task, 0L, 10000);

	}
	
	/**
	 * SSE 통신에 데이터를 보내는 함수
	 */
	private boolean sendToClient(SseEmitter emitter, String name, String id, Object data) {
		try {
			// 클라이언트에서 메세지를 구분할 때 사용한다.
			// 파라미터 값으로 받은 데이터들로 이벤트를 만들어 데이터를 보낸다.
			emitter.send(SseEmitter.event().id(id).name(name)
					.data(data));
		} catch (IOException | IllegalStateException exception) {
			// 에러발생시
			emitter.completeWithError(exception);
			// 통로를 닫고 에러를 발생시킨다.
			log.info("연결이 종료되었습니다. 연결 및 데이터를 삭제합니다.");
			return false;
		}
		return true;
	}
	
	private String now() {
		return LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
	}
}