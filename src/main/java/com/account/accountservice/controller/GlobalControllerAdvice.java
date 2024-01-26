package com.account.accountservice.controller;

import com.account.accountservice.exception.Result;
import com.account.accountservice.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<Result> serviceExceptionHandler(final ServiceException e) {
        return ResponseEntity
            .status(e.getHttpStatus())
            .body(Result.builder()
                .code(e.getCode())
                .description(e.getMessage())
                .build());
    }

}
