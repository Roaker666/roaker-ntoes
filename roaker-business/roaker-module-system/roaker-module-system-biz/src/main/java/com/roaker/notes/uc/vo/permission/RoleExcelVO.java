package com.roaker.notes.uc.vo.permission;

import com.alibaba.excel.annotation.ExcelProperty;
import com.roaker.notes.commons.excel.annoatations.DictFormat;
import com.roaker.notes.commons.excel.convert.DictConvert;
import com.roaker.notes.enums.CommonStatusEnum;
import lombok.Data;

/**
 * 角色 Excel 导出响应 VO
 */
@Data
public class RoleExcelVO {

    @ExcelProperty("角色序号")
    private Long roleId;

    @ExcelProperty("角色名称")
    private String roleName;

    @ExcelProperty("角色标志")
    private String code;

    @ExcelProperty("角色排序")
    private Integer sort;

    @ExcelProperty(value = "角色状态", converter = DictConvert.class)
    @DictFormat(CommonStatusEnum.class)
    private String status;

}
