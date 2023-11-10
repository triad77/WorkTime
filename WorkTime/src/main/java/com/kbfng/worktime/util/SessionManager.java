package com.kbfng.worktime.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class SessionManager {

	private Map<String, String> sessionMap = new ConcurrentHashMap<String, String>();
	
	public void put(String key, String value) {
		sessionMap.put(key, value);
	}
	
	public String get(String key) {
		return sessionMap.get(key);
	}
	
	public String remove(String key) {
		return sessionMap.remove(key);
	}
	
	public boolean containsKey(String key) {
		return sessionMap.containsKey(key);
	}
}
