package com.roaker.notes.pay.dal.mapper.wallet;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.roaker.notes.commons.db.PageResult;
import com.roaker.notes.commons.db.core.mapper.BaseMapperX;
import com.roaker.notes.commons.db.core.query.LambdaQueryWrapperX;
import com.roaker.notes.pay.dal.dataobject.wallet.PayWalletDO;
import com.roaker.notes.pay.vo.wallet.wallet.PayWalletPageReqVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PayWalletMapper extends BaseMapperX<PayWalletDO> {

    default PayWalletDO selectByUserIdAndType(String userId, Integer userType) {
        return selectOne(PayWalletDO::getUserId, userId,
                PayWalletDO::getUserType, userType);
    }

    default PageResult<PayWalletDO> selectPage(Integer userType, PayWalletPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<PayWalletDO>()
                .inIfPresent(PayWalletDO::getUserId, reqVO.getUserIds())
                .eqIfPresent(PayWalletDO::getUserType, userType)
                .betweenIfPresent(PayWalletDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(PayWalletDO::getId));
    }

    /**
     * 当消费退款时候， 更新钱包
     *
     * @param id 钱包 id
     * @param price 消费金额
     */
    default int updateWhenConsumptionRefund(Long id, Integer price){
        LambdaUpdateWrapper<PayWalletDO> lambdaUpdateWrapper = new LambdaUpdateWrapper<PayWalletDO>()
                .setSql(" balance = balance + " + price
                        + ", total_expense = total_expense - " + price)
                .eq(PayWalletDO::getId, id);
        return update(null, lambdaUpdateWrapper);
    }

    /**
     * 当消费时候， 更新钱包
     *
     * @param price 消费金额
     * @param id 钱包 id
     */
    default int updateWhenConsumption(Long id, Integer price){
        LambdaUpdateWrapper<PayWalletDO> lambdaUpdateWrapper = new LambdaUpdateWrapper<PayWalletDO>()
                .setSql(" balance = balance - " + price
                        + ", total_expense = total_expense + " + price)
                .eq(PayWalletDO::getId, id)
                .ge(PayWalletDO::getBalance, price); // cas 逻辑
        return update(null, lambdaUpdateWrapper);
    }

    /**
     * 当充值的时候，更新钱包
     *
     * @param id 钱包 id
     * @param price 钱包金额
     */
    default int updateWhenRecharge(Long id, Integer price){
        LambdaUpdateWrapper<PayWalletDO> lambdaUpdateWrapper = new LambdaUpdateWrapper<PayWalletDO>()
                .setSql(" balance = balance + " + price
                        + ", total_recharge = total_recharge + " + price)
                .eq(PayWalletDO::getId, id);
        return update(null, lambdaUpdateWrapper);
    }

    /**
     * 冻结钱包部分余额
     *
     * @param id 钱包 id
     * @param price 冻结金额
     */
    default int freezePrice(Long id, Integer price){
        LambdaUpdateWrapper<PayWalletDO> lambdaUpdateWrapper = new LambdaUpdateWrapper<PayWalletDO>()
                .setSql(" balance = balance - " + price
                        + ", freeze_price = freeze_price + " + price)
                .eq(PayWalletDO::getId, id)
                .ge(PayWalletDO::getBalance, price); // cas 逻辑
        return update(null, lambdaUpdateWrapper);
    }

    /**
     * 解冻钱包余额
     *
     * @param id 钱包 id
     * @param price 解冻金额
     */
    default int unFreezePrice(Long id, Integer price){
        LambdaUpdateWrapper<PayWalletDO> lambdaUpdateWrapper = new LambdaUpdateWrapper<PayWalletDO>()
                .setSql(" balance = balance + " + price
                        + ", freeze_price = freeze_price - " + price)
                .eq(PayWalletDO::getId, id)
                .ge(PayWalletDO::getFreezePrice, price); // cas 逻辑
        return update(null, lambdaUpdateWrapper);
    }

    /**
     * 当充值退款时, 更新钱包
     *
     * @param id 钱包 id
     * @param price 退款金额
     */
    default  int updateWhenRechargeRefund(Long id, Integer price){
        LambdaUpdateWrapper<PayWalletDO> lambdaUpdateWrapper = new LambdaUpdateWrapper<PayWalletDO>()
                .setSql(" freeze_price = freeze_price - " + price
                        + ", total_recharge = total_recharge - " + price)
                .eq(PayWalletDO::getId, id)
                .ge(PayWalletDO::getFreezePrice, price)
                .ge(PayWalletDO::getTotalRecharge, price);// cas 逻辑
        return update(null, lambdaUpdateWrapper);
    }


}




