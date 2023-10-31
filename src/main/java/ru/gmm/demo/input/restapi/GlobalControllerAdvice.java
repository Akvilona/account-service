package ru.gmm.demo.input.restapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.gmm.demo.exception.Result;
import ru.gmm.demo.exception.ServiceException;

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
