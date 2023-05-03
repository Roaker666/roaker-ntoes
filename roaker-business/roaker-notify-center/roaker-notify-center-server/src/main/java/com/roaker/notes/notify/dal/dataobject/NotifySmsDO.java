package com.roaker.notes.notify.dal.dataobject;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class NotifySmsDO implements Serializable {
    private String smsContent;
    private String smsRecipient;
}
