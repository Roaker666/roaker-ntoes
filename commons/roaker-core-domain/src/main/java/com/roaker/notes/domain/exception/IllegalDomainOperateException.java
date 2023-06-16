package com.roaker.notes.domain.exception;

/**
 * @author lei.rao
 * @since 1.0
 */
public class IllegalDomainOperateException extends RuntimeException {
    public IllegalDomainOperateException(String message) {
        super(message);
    }

    public IllegalDomainOperateException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalDomainOperateException(Throwable cause) {
        super(cause);
    }
}
