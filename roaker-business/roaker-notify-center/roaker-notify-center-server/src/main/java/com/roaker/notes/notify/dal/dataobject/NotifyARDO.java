package com.roaker.notes.notify.dal.dataobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author lei.rao
 * @since 1.0
 */
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class NotifyARDO implements Serializable {
    private String arTitle;
    private String arContent;
    private String arRedirect;
    private String arFolder;
    private String arBanner;
    private String arRedirectLabel;
}
