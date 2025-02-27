package com.roaker.notes.uc.vo.logger.loginlog;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.roaker.notes.commons.constants.DictTypeConstants;
import com.roaker.notes.commons.excel.annoatations.DictFormat;
import com.roaker.notes.commons.excel.convert.DictConvert;
import com.roaker.notes.enums.LoginLogTypeEnum;
import com.roaker.notes.enums.LoginResultEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 登录日志 Response VO")
@Data
@ExcelIgnoreUnannotated
public class LoginLogRespVO {

    @Schema(description = "日志编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @ExcelProperty("日志主键")
    private Long id;

    @Schema(description = "日志类型，参见 LoginLogTypeEnum 枚举类", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty(value = "日志类型", converter = DictConvert.class)
    @DictFormat(LoginLogTypeEnum.class)
    private String logType;

    @Schema(description = "用户编号", example = "666")
    private String userId;

    @Schema(description = "用户类型，参见 UserTypeEnum 枚举", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private Integer userType;

    @Schema(description = "链路追踪编号", example = "89aca178-a370-411c-ae02-3f0d672be4ab")
    private String traceId;

    @Schema(description = "用户账号", requiredMode = Schema.RequiredMode.REQUIRED, example = "Roaker")
    @ExcelProperty("用户账号")
    private String username;

    @Schema(description = "登录结果，参见 LoginResultEnum 枚举类", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty(value = "登录结果", converter = DictConvert.class)
    @DictFormat(LoginResultEnum.class)
    private String result;

    @Schema(description = "用户 IP", requiredMode = Schema.RequiredMode.REQUIRED, example = "127.0.0.1")
    @ExcelProperty("登录 IP")
    private String userIp;

    @Schema(description = "浏览器 UserAgent", example = "Mozilla/5.0")
    @ExcelProperty("浏览器 UA")
    private String userAgent;

    @Schema(description = "登录时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("登录时间")
    private LocalDateTime createTime;

}
