package com.roaker.notes.pay.converter.wallet;

import com.roaker.notes.commons.db.PageResult;
import com.roaker.notes.pay.dal.dataobject.wallet.PayWalletRechargePackageDO;
import com.roaker.notes.pay.vo.wallet.rechargepackage.WalletRechargePackageCreateReqVO;
import com.roaker.notes.pay.vo.wallet.rechargepackage.WalletRechargePackageRespVO;
import com.roaker.notes.pay.vo.wallet.rechargepackage.WalletRechargePackageUpdateReqVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface PayWalletRechargePackageConvert {

    PayWalletRechargePackageConvert INSTANCE = Mappers.getMapper(PayWalletRechargePackageConvert.class);

    PayWalletRechargePackageDO convert(WalletRechargePackageCreateReqVO bean);

    PayWalletRechargePackageDO convert(WalletRechargePackageUpdateReqVO bean);

    WalletRechargePackageRespVO convert(PayWalletRechargePackageDO bean);

    List<WalletRechargePackageRespVO> convertList(List<PayWalletRechargePackageDO> list);

    PageResult<WalletRechargePackageRespVO> convertPage(PageResult<PayWalletRechargePackageDO> page);

}
