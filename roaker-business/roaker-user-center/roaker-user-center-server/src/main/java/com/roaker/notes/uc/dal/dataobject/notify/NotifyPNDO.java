package com.roaker.notes.uc.dal.dataobject.notify;

import com.alibaba.excel.annotation.ExcelProperty;
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
    @ExcelProperty(value = {"PN","PN标题"})
    private String pnTitle;
    @ExcelProperty(value = {"PN","PN内容"})
    private String pnContent;
    @ExcelProperty(value = {"PN","PN重定向地址"})
    private String pnRedirect;
    @ExcelProperty(value = {"PN","PNIcon"})
    private String pnIcon;
    @ExcelProperty(value = {"PN","PNBanner"})
    private String pnBanner;
}
