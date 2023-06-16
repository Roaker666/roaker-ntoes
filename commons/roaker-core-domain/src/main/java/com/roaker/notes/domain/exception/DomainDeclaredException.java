package com.roaker.notes.domain.exception;

/**
 * @author lei.rao
 * @since 1.0
 */
public class DomainDeclaredException extends RuntimeException {
    public DomainDeclaredException() {
        super();
    }

    public DomainDeclaredException(String message) {
        super(message);
    }

    public DomainDeclaredException(String message, Throwable cause) {
        super(message, cause);
    }

    public DomainDeclaredException(Throwable cause) {
        super(cause);
    }
}
