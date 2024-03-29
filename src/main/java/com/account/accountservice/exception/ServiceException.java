
package com.account.accountservice.exception;

import lombok.Getter;

@Getter
public class ServiceException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private final String code;
    private final Integer httpStatus;

    public ServiceException(final ErrorCode errorCode, final Object... args) {
        super(errorCode.formatDescription(args));
        this.code = errorCode.getCode();
        this.httpStatus = errorCode.getHttpStatus();
    }

    public ServiceException(final ErrorCode errorCode) {
        super(errorCode.getDescription());
        this.code = errorCode.getCode();
        this.httpStatus = errorCode.getHttpStatus();
    }
}
