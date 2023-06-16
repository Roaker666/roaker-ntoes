package com.roaker.notes.uc.vo.notify;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lei.rao
 * @since 1.0
 */
@Data
@AllArgsConstructor
public class NotifyResp implements Serializable {
    private String msgId;
}
