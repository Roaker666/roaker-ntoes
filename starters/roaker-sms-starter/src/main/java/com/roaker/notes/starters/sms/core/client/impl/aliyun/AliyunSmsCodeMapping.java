package com.roaker.notes.starters.sms.core.client.impl.aliyun;

import com.roaker.notes.exception.ErrorCode;
import com.roaker.notes.exception.enums.GlobalErrorCodeConstants;
import com.roaker.notes.starters.sms.core.client.SmsCodeMapping;
import com.roaker.notes.starters.sms.core.property.SmsFramewordErrorCodeConstants;

/**
 *  阿里云的 SmsCodeMapping 实现类
 *  <a href="https://help.aliyun.com/document_detail/101346.htm">参见文档</a>
 * @author lei.rao
 * @since 1.0
 */
public class AliyunSmsCodeMapping implements SmsCodeMapping {
    @Override
    public ErrorCode apply(String apiCode) {
        switch (apiCode) {
            case "OK": return GlobalErrorCodeConstants.SUCCESS;
            case "isv.ACCOUNT_NOT_EXISTS":
            case "isv.ACCOUNT_ABNORMAL":
            case "MissingAccessKeyId": return SmsFramewordErrorCodeConstants.SMS_ACCOUNT_INVALID;
            case "isp.RAM_PERMISSION_DENY": return SmsFramewordErrorCodeConstants.SMS_PERMISSION_DENY;
            case "isv.INVALID_JSON_PARAM":
            case "isv.INVALID_PARAMETERS": return SmsFramewordErrorCodeConstants.SMS_API_PARAM_ERROR;
            case "isv.BUSINESS_LIMIT_CONTROL": return SmsFramewordErrorCodeConstants.SMS_SEND_BUSINESS_LIMIT_CONTROL;
            case "isv.DAY_LIMIT_CONTROL": return SmsFramewordErrorCodeConstants.SMS_SEND_DAY_LIMIT_CONTROL;
            case "isv.SMS_CONTENT_ILLEGAL": return SmsFramewordErrorCodeConstants.SMS_SEND_CONTENT_INVALID;
            case "isv.SMS_TEMPLATE_ILLEGAL": return SmsFramewordErrorCodeConstants.SMS_TEMPLATE_INVALID;
            case "isv.SMS_SIGNATURE_ILLEGAL":
            case "isv.SIGN_NAME_ILLEGAL":
            case "isv.SMS_SIGN_ILLEGAL": return SmsFramewordErrorCodeConstants.SMS_SIGN_INVALID;
            case "isv.AMOUNT_NOT_ENOUGH":
            case "isv.OUT_OF_SERVICE": return SmsFramewordErrorCodeConstants.SMS_ACCOUNT_MONEY_NOT_ENOUGH;
            case "isv.MOBILE_NUMBER_ILLEGAL": return SmsFramewordErrorCodeConstants.SMS_MOBILE_INVALID;
            case "isv.TEMPLATE_MISSING_PARAMETERS": return SmsFramewordErrorCodeConstants.SMS_TEMPLATE_PARAM_ERROR;
            default: return SmsFramewordErrorCodeConstants.SMS_UNKNOWN;
        }
    }
}
