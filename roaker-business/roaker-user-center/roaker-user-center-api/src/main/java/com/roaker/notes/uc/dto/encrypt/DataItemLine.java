package com.roaker.notes.uc.dto.encrypt;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lei.rao
 * @since 1.0
 */
@Data
public class DataItemLine implements Serializable {
    private String data;

    private Integer dataType;

    private Long keyId;

    private String randomKey;
}
