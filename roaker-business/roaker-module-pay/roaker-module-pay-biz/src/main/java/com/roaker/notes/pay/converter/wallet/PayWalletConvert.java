package com.roaker.notes.pay.converter.wallet;

import com.roaker.notes.commons.db.PageResult;
import com.roaker.notes.commons.utils.RoakerMapUtils;
import com.roaker.notes.pay.dal.dataobject.wallet.PayWalletDO;
import com.roaker.notes.pay.vo.wallet.wallet.AppPayWalletRespVO;
import com.roaker.notes.pay.vo.wallet.wallet.PayWalletRespVO;
import com.roaker.notes.uc.vo.user.AdminUserRespDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Map;

@Mapper
public interface PayWalletConvert {

    PayWalletConvert INSTANCE = Mappers.getMapper(PayWalletConvert.class);

    AppPayWalletRespVO convert(PayWalletDO bean);
    @Mapping(target = "userType", expression = "java(bean.getUserType().getCode())")
    PayWalletRespVO convert03(PayWalletDO bean);
    @Mapping(target = "userType", expression = "java(bean.getUserType().getCode())")
    PayWalletRespVO convert02(String nickname, String avatar, PayWalletDO bean);
    PageResult<PayWalletRespVO> convertPage(PageResult<PayWalletDO> page);
    default PageResult<PayWalletRespVO> convertPage(PageResult<PayWalletDO> page, Map<String, AdminUserRespDTO> userMap) {
        PageResult<PayWalletRespVO> pageResult = convertPage(page);
        pageResult.getList().forEach(wallet -> RoakerMapUtils.findAndThen(userMap, wallet.getUserId(),
                user -> wallet.setNickname(user.getNickname()).setAvatar(user.getAvatar())));
        return pageResult;
    }
}
