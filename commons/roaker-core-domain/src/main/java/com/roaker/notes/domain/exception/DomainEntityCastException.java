package com.roaker.notes.domain.exception;

/**
 * @author lei.rao
 * @since 1.0
 */
public class DomainEntityCastException extends RuntimeException {
    private final static String errMsg = "domain entity cast error: cannot cast: %s to %s";
    public DomainEntityCastException(Class<?> source, Class<?> target) {
        super(String.format(errMsg, source.getName(), target.getName()));
    }

    public static DomainEntityCastException builder(Class<?> source, Class<?> target) {
        return new DomainEntityCastException(source, target);
    }
}