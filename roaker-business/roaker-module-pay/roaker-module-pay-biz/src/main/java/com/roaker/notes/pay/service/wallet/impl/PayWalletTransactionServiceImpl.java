package com.roaker.notes.pay.service.wallet.impl;

import com.roaker.notes.commons.db.PageResult;
import com.roaker.notes.pay.api.enums.wallet.PayWalletBizTypeEnum;
import com.roaker.notes.pay.converter.wallet.PayWalletTransactionConvert;
import com.roaker.notes.pay.dal.dataobject.wallet.PayWalletDO;
import com.roaker.notes.pay.dal.dataobject.wallet.PayWalletTransactionDO;
import com.roaker.notes.pay.dal.mapper.wallet.PayWalletTransactionMapper;
import com.roaker.notes.pay.service.wallet.PayWalletService;
import com.roaker.notes.pay.service.wallet.PayWalletTransactionService;
import com.roaker.notes.pay.service.wallet.bo.WalletTransactionCreateReqBO;
import com.roaker.notes.pay.vo.wallet.transaction.AppPayWalletTransactionPageReqVO;
import com.roaker.notes.pay.vo.wallet.transaction.AppPayWalletTransactionSummaryRespVO;
import com.roaker.notes.pay.vo.wallet.transaction.PayWalletTransactionPageReqVO;
import com.roaker.notes.uc.api.seq.SeqApi;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

import static com.roaker.notes.pay.vo.wallet.transaction.AppPayWalletTransactionPageReqVO.TYPE_EXPENSE;
import static com.roaker.notes.pay.vo.wallet.transaction.AppPayWalletTransactionPageReqVO.TYPE_INCOME;

/**
 * 钱包流水 Service 实现类
 *
 */
@Service
@Slf4j
@Validated
public class PayWalletTransactionServiceImpl implements PayWalletTransactionService {

    /**
     * 钱包流水的 no 前缀
     */
    private static final String WALLET_NO_PREFIX = "W";

    @Resource
    private PayWalletService payWalletService;
    @Resource
    private PayWalletTransactionMapper payWalletTransactionMapper;
    @Resource
    private SeqApi seqApi;

    @Override
    public PageResult<PayWalletTransactionDO> getWalletTransactionPage(String userId, Integer userType,
                                                                       AppPayWalletTransactionPageReqVO pageVO) {
        PayWalletDO wallet = payWalletService.getOrCreateWallet(userId, userType);
        return payWalletTransactionMapper.selectPage(wallet.getId(), pageVO.getType(), pageVO, pageVO.getCreateTime());
    }

    @Override
    public PageResult<PayWalletTransactionDO> getWalletTransactionPage(PayWalletTransactionPageReqVO pageVO) {
        return payWalletTransactionMapper.selectPage(pageVO.getWalletId(), null, pageVO, null);
    }

    @Override
    public PayWalletTransactionDO createWalletTransaction(WalletTransactionCreateReqBO bo) {
        PayWalletTransactionDO transaction = PayWalletTransactionConvert.INSTANCE.convert(bo)
                .setNo(seqApi.getSegmentId(WALLET_NO_PREFIX));
        payWalletTransactionMapper.insert(transaction);
        return transaction;
    }

    @Override
    public PayWalletTransactionDO getWalletTransactionByNo(String no) {
        return payWalletTransactionMapper.selectByNo(no);
    }

    @Override
    public PayWalletTransactionDO getWalletTransaction(String bizId, PayWalletBizTypeEnum type) {
        return payWalletTransactionMapper.selectByBiz(bizId, type.getCode());
    }

    @Override
    public AppPayWalletTransactionSummaryRespVO getWalletTransactionSummary(String userId, Integer userType, LocalDateTime[] createTime) {
        PayWalletDO wallet = payWalletService.getOrCreateWallet(userId, userType);
        return new AppPayWalletTransactionSummaryRespVO()
                .setTotalExpense(payWalletTransactionMapper.selectPriceSum(wallet.getId(), TYPE_EXPENSE, createTime))
                .setTotalIncome(payWalletTransactionMapper.selectPriceSum(wallet.getId(), TYPE_INCOME, createTime));
    }

}
