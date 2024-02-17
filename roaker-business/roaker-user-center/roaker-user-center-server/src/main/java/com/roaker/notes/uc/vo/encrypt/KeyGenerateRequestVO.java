package com.roaker.notes.uc.vo.encrypt;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lei.rao
 * @since 1.0
 */
@Data
public class KeyGenerateRequestVO implements Serializable {
    private String sysCode;
    private Integer dataType;
    private Integer keyType;
    private String randomKey;
}
