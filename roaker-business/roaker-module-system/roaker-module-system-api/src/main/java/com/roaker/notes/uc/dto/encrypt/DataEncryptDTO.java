package com.roaker.notes.uc.dto.encrypt;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author lei.rao
 * @since 1.0
 */
@Data
public class DataEncryptDTO implements Serializable {
    private List<DataItemLine> dataList;
    private String sysCode;
}
