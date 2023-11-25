package com.roaker.notes.uc.dal.dataobject.notify;

import com.alibaba.excel.annotation.ExcelProperty;
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
    @ExcelProperty({"AR", "ar标题"})
    private String arTitle;
    @ExcelProperty({"AR", "ar内容"})
    private String arContent;
    @ExcelProperty({"AR", "ar重定向地址"})
    private String arRedirect;
    @ExcelProperty({"AR", "ar类型"})
    private Integer arType;
    @ExcelProperty({"AR", "arBanner"})
    private String arBanner;
    @ExcelProperty({"AR", "ar标签"})
    private String arRedirectLabel;
}
