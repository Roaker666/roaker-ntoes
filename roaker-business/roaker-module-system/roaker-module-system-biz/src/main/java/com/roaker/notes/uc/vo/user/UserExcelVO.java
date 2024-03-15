package com.roaker.notes.uc.vo.user;

import com.alibaba.excel.annotation.ExcelProperty;
import com.roaker.notes.commons.constants.DictTypeConstants;
import com.roaker.notes.commons.excel.annoatations.DictFormat;
import com.roaker.notes.commons.excel.convert.DictConvert;
import com.roaker.notes.enums.CommonStatusEnum;
import com.roaker.notes.enums.UserSexEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户 Excel 导出 VO
 */
@Data
public class UserExcelVO {

    @ExcelProperty("用户编号")
    private Long id;

    @ExcelProperty("用户名称")
    private String username;

    @ExcelProperty("用户昵称")
    private String nickname;

    @ExcelProperty("用户邮箱")
    private String email;

    @ExcelProperty("手机号码")
    private String mobile;

    @ExcelProperty(value = "用户性别", converter = DictConvert.class)
    @DictFormat(UserSexEnum.class)
    private String sex;

    @ExcelProperty(value = "帐号状态", converter = DictConvert.class)
    @DictFormat(CommonStatusEnum.class)
    private String status;

    @ExcelProperty("最后登录IP")
    private String loginIp;

    @ExcelProperty("最后登录时间")
    private LocalDateTime loginDate;

    @ExcelProperty("部门名称")
    private String deptName;

    @ExcelProperty("部门负责人")
    private String deptLeaderNickname;

}
