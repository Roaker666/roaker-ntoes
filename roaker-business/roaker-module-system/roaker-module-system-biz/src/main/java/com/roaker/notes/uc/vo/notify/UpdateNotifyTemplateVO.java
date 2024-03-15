package com.roaker.notes.uc.vo.notify;

import com.roaker.notes.uc.dal.dataobject.notify.NotifyARDO;
import com.roaker.notes.uc.dal.dataobject.notify.NotifyEmailDO;
import com.roaker.notes.uc.dal.dataobject.notify.NotifyPNDO;
import com.roaker.notes.uc.dal.dataobject.notify.NotifySmsDO;
import lombok.Data;

import java.util.List;

/**
 * @author lei.rao
 * @since 1.0
 */
@Data
public class UpdateNotifyTemplateVO {
    private String uid;
    private String templateCode;
    private String templateName;
    private NotifyPNDO notifyPn;
    private NotifyARDO notifyAr;
    private NotifySmsDO notifySms;
    private NotifyEmailDO notifyEmail;
    private Integer recipientType;
    private Integer status;
    private String remark;
    private List<Integer> enableChannelStatus;
}
