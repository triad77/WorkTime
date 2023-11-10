package com.kbfng.worktime;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Token Not Found")
public class CustomRuntimeException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public CustomRuntimeException(String message) {
        super(message);
    }
}
