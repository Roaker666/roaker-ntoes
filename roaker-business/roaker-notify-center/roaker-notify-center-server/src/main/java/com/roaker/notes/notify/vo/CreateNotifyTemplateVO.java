package com.roaker.notes.notify.vo;

import com.roaker.notes.enums.SceneEnum;
import com.roaker.notes.notify.dal.dataobject.NotifyARDO;
import com.roaker.notes.notify.dal.dataobject.NotifyEmailDO;
import com.roaker.notes.notify.dal.dataobject.NotifyPNDO;
import com.roaker.notes.notify.dal.dataobject.NotifySmsDO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author lei.rao
 * @since 1.0
 */
@Data
public class CreateNotifyTemplateVO implements Serializable {
    private Long uid;
    private String templateCode;
    private String templateName;
    private String scene;
    private NotifyPNDO notifyPn;
    private NotifyARDO notifyAr;
    private NotifySmsDO notifySms;
    private NotifyEmailDO notifyEmail;
    private Integer recipientType;
    private String remark;
    private List<Integer> enableChannelStatus;
}
