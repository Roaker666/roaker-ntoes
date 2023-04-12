package com.roaker.notes.starters.sms.core.client;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.lang.Assert;
import com.roaker.notes.exception.ErrorCode;
import com.roaker.notes.starters.sms.core.property.SmsFramewordErrorCodeConstants;
import com.roaker.notes.vo.CommonResult;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 *  短信的 CommonResult 拓展类
 *  考虑到不同的平台，返回的 code 和 msg 是不同的，所以统一额外返回 {@link #apiCode} 和 {@link #apiMsg} 字段
 *  另外，一些短信平台（例如说阿里云、腾讯云）会返回一个请求编号，用于排查请求失败的问题，我们设置到 {@link #apiRequestId} 字段
 * @author lei.rao
 * @since 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class SmsCommonResult<T> extends CommonResult<T> {
    private String apiCode;
    private String apiMsg;
    private String apiRequestId;
    private SmsCommonResult(){}

    public static <T> SmsCommonResult<T> build(String apiCode, String apiMsg, String apiRequestId,
                                               T data, SmsCodeMapping codeMapping) {
        Assert.notNull(codeMapping, "参数 codeMapping 不能为空");
        SmsCommonResult<T> result = new SmsCommonResult<T>().setApiCode(apiCode).setApiMsg(apiMsg).setApiRequestId(apiRequestId);
        result.setData(data);
        // 翻译错误码
        if (codeMapping != null) {
            ErrorCode errorCode = codeMapping.apply(apiCode);
            if (errorCode == null) {
                errorCode = SmsFramewordErrorCodeConstants.SMS_UNKNOWN;
            }
            result.setCode(errorCode.getCode()).setMsg(errorCode.getMsg());
        }
        return result;
    }

    public static <T> SmsCommonResult<T> error(Throwable ex) {
        SmsCommonResult<T> result = new SmsCommonResult<>();
        result.setCode(SmsFramewordErrorCodeConstants.EXCEPTION.getCode());
        result.setMsg(ExceptionUtil.getRootCauseMessage(ex));
        return result;
    }
}
