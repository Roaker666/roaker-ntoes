package com.roaker.notes.starters.sms.core.client.impl.tencent;

import com.roaker.notes.exception.ErrorCode;
import com.roaker.notes.exception.enums.GlobalErrorCodeConstants;
import com.roaker.notes.starters.sms.core.client.SmsCodeMapping;
import com.roaker.notes.starters.sms.core.property.SmsFramewordErrorCodeConstants;

/**
 * 腾讯云的 SmsCodeMapping 实现类
 * 参见 <a href="https://cloud.tencent.com/document/api/382/52075#.E5.85.AC.E5.85.B1.E9.94.99.E8.AF.AF.E7.A0.81">...</a>
 *
 * @author lei.rao
 * @since 1.0
 */
public class TencentSmsCodeMapping implements SmsCodeMapping {
    @Override
    public ErrorCode apply(String apiCode) {
        switch (apiCode) {
            case TencentSmsClient.API_SUCCESS_CODE: return GlobalErrorCodeConstants.SUCCESS;
            case "FailedOperation.ContainSensitiveWord": return SmsFramewordErrorCodeConstants.SMS_SEND_CONTENT_INVALID;
            case "FailedOperation.JsonParseFail":
            case "MissingParameter.EmptyPhoneNumberSet":
            case "LimitExceeded.PhoneNumberCountLimit":
            case "FailedOperation.FailResolvePacket": return GlobalErrorCodeConstants.BAD_REQUEST;
            case "FailedOperation.InsufficientBalanceInSmsPackage": return SmsFramewordErrorCodeConstants.SMS_ACCOUNT_MONEY_NOT_ENOUGH;
            case "FailedOperation.MarketingSendTimeConstraint": return SmsFramewordErrorCodeConstants.SMS_SEND_MARKET_LIMIT_CONTROL;
            case "FailedOperation.PhoneNumberInBlacklist": return SmsFramewordErrorCodeConstants.SMS_MOBILE_BLACK;
            case "FailedOperation.SignatureIncorrectOrUnapproved": return SmsFramewordErrorCodeConstants.SMS_SIGN_INVALID;
            case "FailedOperation.MissingTemplateToModify":
            case "FailedOperation.TemplateIncorrectOrUnapproved": return SmsFramewordErrorCodeConstants.SMS_TEMPLATE_INVALID;
            case "InvalidParameterValue.IncorrectPhoneNumber": return SmsFramewordErrorCodeConstants.SMS_MOBILE_INVALID;
            case "InvalidParameterValue.SdkAppIdNotExist": return SmsFramewordErrorCodeConstants.SMS_APP_ID_INVALID;
            case "InvalidParameterValue.TemplateParameterLengthLimit":
            case "InvalidParameterValue.TemplateParameterFormatError": return SmsFramewordErrorCodeConstants.SMS_TEMPLATE_PARAM_ERROR;
            case "LimitExceeded.PhoneNumberDailyLimit": return SmsFramewordErrorCodeConstants.SMS_SEND_DAY_LIMIT_CONTROL;
            case "LimitExceeded.PhoneNumberThirtySecondLimit":
            case "LimitExceeded.PhoneNumberOneHourLimit": return SmsFramewordErrorCodeConstants.SMS_SEND_BUSINESS_LIMIT_CONTROL;
            case "UnauthorizedOperation.RequestPermissionDeny":
            case "FailedOperation.ForbidAddMarketingTemplates":
            case "FailedOperation.NotEnterpriseCertification":
            case "UnauthorizedOperation.IndividualUserMarketingSmsPermissionDeny": return SmsFramewordErrorCodeConstants.SMS_PERMISSION_DENY;
            case "UnauthorizedOperation.RequestIpNotInWhitelist": return SmsFramewordErrorCodeConstants.SMS_IP_DENY;
            case "AuthFailure.SecretIdNotFound": return SmsFramewordErrorCodeConstants.SMS_ACCOUNT_INVALID;
        }
        return SmsFramewordErrorCodeConstants.SMS_UNKNOWN;
    }
}
