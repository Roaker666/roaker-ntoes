package com.roaker.notes.pay.converter.wallet;

import com.roaker.notes.commons.db.PageResult;
import com.roaker.notes.pay.dal.dataobject.wallet.PayWalletTransactionDO;
import com.roaker.notes.pay.service.wallet.bo.WalletTransactionCreateReqBO;
import com.roaker.notes.pay.vo.wallet.transaction.PayWalletTransactionRespVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PayWalletTransactionConvert {

    PayWalletTransactionConvert INSTANCE = Mappers.getMapper(PayWalletTransactionConvert.class);

    PageResult<PayWalletTransactionRespVO> convertPage2(PageResult<PayWalletTransactionDO> page);

    @Mapping(target = "bizType", expression = "java(com.roaker.notes.dynamic.enums.CommonEnum.of(bean.getBizType(), com.roaker.notes.pay.api.enums.wallet.PayWalletBizTypeEnum.class))")
    PayWalletTransactionDO convert(WalletTransactionCreateReqBO bean);

}
