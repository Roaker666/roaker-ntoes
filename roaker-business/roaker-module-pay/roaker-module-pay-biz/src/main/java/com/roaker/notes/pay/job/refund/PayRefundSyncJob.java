package com.roaker.notes.pay.job.refund;

import cn.hutool.core.util.StrUtil;
import com.roaker.notes.pay.service.refund.PayRefundService;
import com.roaker.notes.quartz.core.handler.JobHandler;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;


/**
 * 退款订单的同步 Job
 * 由于退款订单的状态，是由支付渠道异步通知进行同步，考虑到异步通知可能会失败（小概率），所以需要定时进行同步。
 *
 * @author 芋道源码
 */
@Component
public class PayRefundSyncJob implements JobHandler {

    @Resource
    private PayRefundService refundService;

    @Override
    public String execute(String param) {
        int count = refundService.syncRefund();
        return StrUtil.format("同步退款订单 {} 个", count);
    }

}
