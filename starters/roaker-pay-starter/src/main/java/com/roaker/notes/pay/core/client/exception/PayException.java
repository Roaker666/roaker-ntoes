package com.roaker.notes.pay.core.client.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 支付系统异常 Exception
 *
 * @author lei.rao
 * @since 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PayException extends RuntimeException {

    public PayException(Throwable cause) {
        super(cause);
    }

}
