package com.kbfng.worktime.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
	
    @GetMapping("/index")
    public String index(Model model) {
    	
    	 // 현재 날짜 구하기
        LocalDate now = LocalDate.now();
        // 포맷 정의
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd ");
    	
    	model.addAttribute("localDateTime", LocalDateTime.now());
    	model.addAttribute("todayStr", now.format(formatter));
    	model.addAttribute("testYmd", "20230831");
    	return "/index";
    }
    
    @GetMapping("/test")
    public String test() {
    	return "/tmpTest.html";
    }
}
