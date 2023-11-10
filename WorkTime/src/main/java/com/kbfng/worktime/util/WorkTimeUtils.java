package com.kbfng.worktime.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.kbfng.worktime.entity.EnumModel;
import com.kbfng.worktime.entity.EnumValue;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WorkTimeUtils {

	public static String getLocation(HttpServletRequest request) {
		
		String ip = request.getHeader("X-Forwarded-For");
		
		if (ip == null) {
			ip = request.getHeader("Proxy-Client-IP");
			log.info(">>>> Proxy-Client-IP : " + ip);
		}
		
		if (ip == null) {
			ip = request.getHeader("WL-Proxy-Client-IP");
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

		log.info(">>>> Result : IP Address : " + ip);

		return ip;

	}
	
	public static List<EnumValue> getStatList(Class<? extends EnumModel> e) {
		return Arrays.stream(e.getEnumConstants())
				.map(EnumValue::new)
				.collect(Collectors.toList());
	}

	public static List<EnumValue> getListForPlodrCorp(Class<? extends EnumModel> enumModel) {
		return Arrays.stream(enumModel.getEnumConstants())
				.filter(corp -> Integer.parseInt(corp.getCode()) <= 10)
				.map(EnumValue::new)
				.collect(Collectors.toList());
	}
	
	public static List<EnumValue> getListForBelngCorp(Class<? extends EnumModel> enumModel) {
		return Arrays.stream(enumModel.getEnumConstants())
				.filter(corp -> Integer.parseInt(corp.getCode()) > 10)
				.map(EnumValue::new)
				.collect(Collectors.toList());
	}
}
