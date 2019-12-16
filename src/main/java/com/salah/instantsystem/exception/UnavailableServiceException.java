package com.salah.instantsystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class UnavailableServiceException extends RuntimeException {

    public UnavailableServiceException(String msg) {
        super(msg);
    }
}