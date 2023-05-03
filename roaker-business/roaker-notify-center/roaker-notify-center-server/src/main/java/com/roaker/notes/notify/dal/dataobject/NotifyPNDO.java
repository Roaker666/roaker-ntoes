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
public class NotifyPNDO implements Serializable {
    private String pnTitle;
    private String pnContent;
    private String pnRedirect;
    private String pnIcon;
    private String pnBanner;
}
