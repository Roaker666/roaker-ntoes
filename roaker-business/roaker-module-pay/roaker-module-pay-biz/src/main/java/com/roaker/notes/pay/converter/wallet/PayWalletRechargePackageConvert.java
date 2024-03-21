package com.roaker.notes.pay.converter.wallet;

import com.roaker.notes.commons.db.PageResult;
import com.roaker.notes.pay.dal.dataobject.wallet.PayWalletRechargePackageDO;
import com.roaker.notes.pay.vo.wallet.rechargepackage.WalletRechargePackageCreateReqVO;
import com.roaker.notes.pay.vo.wallet.rechargepackage.WalletRechargePackageRespVO;
import com.roaker.notes.pay.vo.wallet.rechargepackage.WalletRechargePackageUpdateReqVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface PayWalletRechargePackageConvert {

    PayWalletRechargePackageConvert INSTANCE = Mappers.getMapper(PayWalletRechargePackageConvert.class);


    @Mapping(target = "status", expression = "java(com.roaker.notes.dynamic.enums.CommonEnum.of(bean.getStatus(), com.roaker.notes.enums.CommonStatusEnum.class))")
    PayWalletRechargePackageDO convert(WalletRechargePackageCreateReqVO bean);
    @Mapping(target = "status", expression = "java(com.roaker.notes.dynamic.enums.CommonEnum.of(bean.getStatus(), com.roaker.notes.enums.CommonStatusEnum.class))")
    PayWalletRechargePackageDO convert(WalletRechargePackageUpdateReqVO bean);

    @Mapping(target = "status", expression = "java(bean.getStatus().getCode())")
    WalletRechargePackageRespVO convert(PayWalletRechargePackageDO bean);

    @Mapping(target = "status", expression = "java(bean.getStatus().getCode())")
    List<WalletRechargePackageRespVO> convertList(List<PayWalletRechargePackageDO> list);
    PageResult<WalletRechargePackageRespVO> convertPage(PageResult<PayWalletRechargePackageDO> page);

}
