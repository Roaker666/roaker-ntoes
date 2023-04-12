package com.roaker.notes.infra.encrypt.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author lei.rao
 * @since 1.0
 */
@Data
public class DataDecryptDTO implements Serializable {
    private List<DataItemLine> encryptList;

    private String sysCode;
}
