package com.roaker.notes.pay.service.wallet;

import com.roaker.notes.commons.db.PageParam;
import com.roaker.notes.commons.db.PageResult;
import com.roaker.notes.pay.dal.dataobject.wallet.PayWalletRechargeDO;
import com.roaker.notes.pay.vo.wallet.recharge.AppPayWalletRechargeCreateReqVO;

/**
 * 钱包充值 Service 接口
 *
 */
public interface PayWalletRechargeService {

    /**
     * 创建钱包充值记录（发起充值）
     *
     * @param userId      用户 id
     * @param userType    用户类型
     * @param createReqVO 钱包充值请求 VO
     * @param channelId 渠道ID
     * @param userIp  用户Ip
     * @return 钱包充值记录
     */
    PayWalletRechargeDO createWalletRecharge(String userId,
                                             Integer userType,
                                             String userIp,
                                             Long channelId,
                                             AppPayWalletRechargeCreateReqVO createReqVO);

    /**
     * 获得钱包充值记录分页
     *
     * @param userId 用户编号
     * @param userType 用户类型
     * @param pageReqVO 分页请求
     * @param payStatus 是否支付
     * @return 钱包充值记录分页
     */
    PageResult<PayWalletRechargeDO> getWalletRechargePackagePage(String userId,
                                                                 Integer userType,
                                                                 PageParam pageReqVO,
                                                                 Boolean payStatus);

    /**
     * 更新钱包充值成功
     *
     * @param id         钱包充值记录 id
     * @param payOrderId 支付订单 id
     */
    void updateWalletRechargerPaid(Long id, Long payOrderId);

    /**
     * 发起钱包充值退款
     *
     * @param id     钱包充值编号
     * @param userIp 用户 ip 地址
     * @param channelId 渠道ID
     * @param reason 退款原因
     */
    void refundWalletRecharge(Long id, String userIp, Long channelId, String reason);

    /**
     * 更新钱包充值记录为已退款
     *
     * @param id          钱包充值 id
     * @param payRefundId 退款单id
     */
    void updateWalletRechargeRefunded(Long id, Long payRefundId);

}
