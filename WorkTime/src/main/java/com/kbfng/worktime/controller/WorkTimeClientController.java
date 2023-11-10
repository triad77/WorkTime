package com.kbfng.worktime.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.kbfng.worktime.CustomRuntimeException;
import com.kbfng.worktime.dto.EmployeeDto;
import com.kbfng.worktime.dto.WorkTimeDto;
import com.kbfng.worktime.service.WorkTimeClientService;
import com.kbfng.worktime.util.SessionManager;
import com.kbfng.worktime.util.SseEmitters;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WorkTimeClientController {
	
	// Server Sent Event 처리객체
	private final SseEmitters sseEmitters;
	private final SessionManager sessionManager;
	private final WorkTimeClientService workTimeService;
	private final WorkTimeClientService workTimeClientService;
	
	@Value("${worktime.host.url}")
	private String workTimeHostUrl;

	@GetMapping("/")
    public String root() {
        return "redirect:/workTimeTest";
    }
	
	@GetMapping("/workTimeTest")
    public String workTimeTest(Model model) {
		
		model.addAttribute("empSelCtnt", workTimeClientService.getNotRegEmployee());
		
        return "/workTimeTest";
    }
	
	@GetMapping("/workTimeForm")
    public String workTimeForm(Model model, String empNo) {
		
		if(StringUtils.isNotBlank(empNo)) {
			model.addAttribute("empNo", empNo);
		}
		
        return "/workTimeForm";
    }
	
	@GetMapping("/registSuccess")
    public String registSuccess() {
    	return "/registSuccess.html";
    }

	/**
	 * 출근시간등록용도의 QR코드 이미지 생성 메소드
	 * @param model
	 * @param empNo:String - 등록직원번호
	 * @param sessionId:String - 서버에서 클라이언트를 식별하기 위한 SSID. 클라이언트에서 생성한다 
	 * @return html name:String
	 * @throws WriterException
	 * @throws IOException
	 */
	@GetMapping("/sWorkTimeReg")
	public String sWorkTimeReg(Model model, String empNo, String sessionId) throws WriterException, IOException {
		
		String qrUrl = workTimeHostUrl + "/registTime?sessionId=" + sessionId;
		String img = getQRCodeImage(qrUrl, 200, 200);
		model.addAttribute("img", img);
		model.addAttribute("sessionId", sessionId);
		
		sessionManager.put(sessionId, empNo);

		return "/qrcode";
	}
	
	/**
	 * QR코드 이미시 생성 메소드
	 * @param text:String - QR코드에 담을 데이터(ex:url)
	 * @param width:int - 생성할 QR코드 이미지 너비
	 * @param height:int - 생성할 QR코드 이미지 높이
	 * @return QR코드 이미지 인코딩
	 * @throws WriterException
	 * @throws IOException
	 */
	private String getQRCodeImage(String text, int width, int height) throws WriterException, IOException {
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

		ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();

		MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);

		return Base64.getEncoder().encodeToString(pngOutputStream.toByteArray());
	}
	
	/**
	 * 모바일에서 QR코드 스캔한 후 요청되는 URL을 받는 메소드 
	 * @param sessionId:String - 클라이언트 식별용 SSID. 해당 SSID에 매핑되는 클라이언트에 종료이벤트 송신용도로 사용
	 * @param empNo:String - 직원번호
	 * @return
	 * @throws IOException
	 */
	@GetMapping("/registTime")
	public String registTime(@RequestParam String sessionId) throws IOException {

		SseEmitter emitter = sseEmitters.findSSEById(sessionId);
		if(emitter == null) {
			throw new CustomRuntimeException("세션이 만료되었거나 잘못된 접근입니다");
		}
		
		String empNo = sessionManager.get(sessionId); 
		if(StringUtils.isBlank(empNo)) {
			throw new CustomRuntimeException("세션이 만료된 직원번호입니다");
		}
		
		EmployeeDto empDto = workTimeClientService.getEmployee(empNo);
		
		if(workTimeClientService.isRegistedEmp(empNo, LocalDate.now())) {
			throw new CustomRuntimeException("이미 등록된 직원번호입니다");
		}
		
		WorkTimeDto workTimeDto = WorkTimeDto.builder()
				.scanIp(sseEmitters.findIpById(sessionId))
				.emp(empDto)
				.regDate(LocalDateTime.now())
				.build();
		
		workTimeService.regist(workTimeDto);
		
		sseEmitters.completeSSEById(sessionId);
		
		log.info("@@@ complete OK {}", sessionId);  
		
		return "/registSuccess";
	}
}