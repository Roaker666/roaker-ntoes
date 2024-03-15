package com.roaker.notes.starters.sms.core.client.impl.debug;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.digest.HmacAlgorithm;
import cn.hutool.http.HttpUtil;
import com.roaker.notes.commons.core.KeyValue;
import com.roaker.notes.commons.utils.JacksonUtils;
import com.roaker.notes.commons.utils.RoakerMapUtils;
import com.roaker.notes.starters.sms.core.client.SmsCommonResult;
import com.roaker.notes.starters.sms.core.property.SmsChannelProperties;
import com.roaker.notes.starters.sms.core.property.SmsTemplateAuditStatusEnum;
import com.roaker.notes.starters.sms.core.client.dto.SmsReceiveRespDTO;
import com.roaker.notes.starters.sms.core.client.dto.SmsSendRespDTO;
import com.roaker.notes.starters.sms.core.client.dto.SmsTemplateRespDTO;
import com.roaker.notes.starters.sms.core.client.impl.AbstractSmsClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基于钉钉 WebHook 实现的调试的短信客户端实现类
 * 考虑到省钱，我们使用钉钉 WebHook 模拟发送短信，方便调试。
 *  *
 * @author lei.rao
 * @since 1.0
 */
public class DebugDingTalkSmsClient extends AbstractSmsClient {

    public DebugDingTalkSmsClient(SmsChannelProperties properties) {
        super(properties, new DebugDingTalkCodeMapping());
        Assert.notEmpty(properties.getApiKey(), "apiKey 不能为空");
        Assert.notEmpty(properties.getApiSecret(), "apiSecret 不能为空");
    }

    @Override
    protected void doInit() {

    }

    @Override
    protected SmsCommonResult<SmsSendRespDTO> doSendSms(String sendLogId, String mobile, String apiTemplateId, List<KeyValue<String, Object>> templateParams) throws Throwable {
        // 构建请求
        String url = buildUrl("robot/send");
        Map<String, Object> params = new HashMap<>();
        params.put("msgtype", "text");
        String content = String.format("【模拟短信】\n手机号：%s\n短信日志编号：%s\n模板参数：%s",
                mobile, sendLogId, RoakerMapUtils.convertMap(templateParams));
        params.put("text", MapUtil.builder().put("content", content).build());
        // 执行请求
        String responseText = HttpUtil.post(url, JacksonUtils.toJSON(params));
        // 解析结果
        Map<?, ?> responseObj = JacksonUtils.from(responseText, Map.class);
        return SmsCommonResult.build(MapUtil.getStr(responseObj, "errcode"), MapUtil.getStr(responseObj, "errorMsg"),
                null, new SmsSendRespDTO().setSerialNo(StrUtil.uuid()), codeMapping);
    }


    /**
     * 构建请求地址
     *
     * 参见 https://developers.dingtalk.com/document/app/custom-robot-access/title-nfv-794-g71 文档
     *
     * @param path 请求路径
     * @return 请求地址
     */
    @SuppressWarnings("SameParameterValue")
    private String buildUrl(String path) {
        // 生成 timestamp
        long timestamp = System.currentTimeMillis();
        // 生成 sign
        String secret = properties.getApiSecret();
        String stringToSign = timestamp + "\n" + secret;
        byte[] signData = DigestUtil.hmac(HmacAlgorithm.HmacSHA256, StrUtil.bytes(secret)).digest(stringToSign);
        String sign = Base64.encode(signData);
        // 构建最终 URL
        return String.format("https://oapi.dingtalk.com/%s?access_token=%s&timestamp=%d&sign=%s",
                path, properties.getApiKey(), timestamp, sign);
    }


    @Override
    protected List<SmsReceiveRespDTO> doParseSmsReceiveStatus(String text) throws Throwable {
        throw new UnsupportedOperationException("模拟短信客户端，暂时无需解析回调");
    }

    @Override
    protected SmsCommonResult<SmsTemplateRespDTO> doGetSmsTemplate(String apiTemplateId) throws Throwable {
        SmsTemplateRespDTO data = new SmsTemplateRespDTO().setId(apiTemplateId).setContent("")
                .setAuditStatus(SmsTemplateAuditStatusEnum.SUCCESS.getStatus()).setAuditReason("");
        return SmsCommonResult.build("0", "success", null, data, codeMapping);
    }
}
