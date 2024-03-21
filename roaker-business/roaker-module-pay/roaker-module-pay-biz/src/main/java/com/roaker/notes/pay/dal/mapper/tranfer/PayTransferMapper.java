package com.roaker.notes.pay.dal.mapper.tranfer;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.roaker.notes.commons.db.PageResult;
import com.roaker.notes.commons.db.core.mapper.BaseMapperX;
import com.roaker.notes.commons.db.core.query.LambdaQueryWrapperX;
import com.roaker.notes.dynamic.enums.CommonEnum;
import com.roaker.notes.pay.api.enums.transfer.PayTransferStatusEnum;
import com.roaker.notes.pay.core.enums.transfer.PayTransferTypeEnum;
import com.roaker.notes.pay.dal.dataobject.tranfer.PayTransferDO;
import com.roaker.notes.pay.vo.tranfer.PayTransferPageReqVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PayTransferMapper extends BaseMapperX<PayTransferDO> {

    default int updateByIdAndStatus(Long id, List<Integer> status, PayTransferDO updateObj) {
        return update(updateObj, new LambdaQueryWrapper<PayTransferDO>()
                .eq(PayTransferDO::getId, id).in(PayTransferDO::getPayTransferStatus, PayTransferStatusEnum.getListByCodeList(status)));
    }

    default PayTransferDO selectByAppIdAndMerchantTransferId(Long appId, String merchantTransferId) {
        return selectOne(PayTransferDO::getAppId, appId,
                PayTransferDO::getMerchantTransferId, merchantTransferId);
    }

    default PayTransferDO selectByNo(String no) {
        return selectOne(PayTransferDO::getNo, no);
    }

    default PageResult<PayTransferDO> selectPage(PayTransferPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<PayTransferDO>()
                .eqIfPresent(PayTransferDO::getNo, reqVO.getNo())
                .eqIfPresent(PayTransferDO::getAppId, reqVO.getAppId())
                .eqIfPresent(PayTransferDO::getChannelCode, reqVO.getChannelCode())
                .eqIfPresent(PayTransferDO::getMerchantTransferId, reqVO.getMerchantTransferId())
                .eqIfPresent(PayTransferDO::getPayType, CommonEnum.of(reqVO.getType(), PayTransferTypeEnum.class))
                .eqIfPresent(PayTransferDO::getPayTransferStatus, CommonEnum.of(reqVO.getStatus(), PayTransferStatusEnum.class))
                .likeIfPresent(PayTransferDO::getUserName, reqVO.getUserName())
                .eqIfPresent(PayTransferDO::getChannelTransferNo, reqVO.getChannelTransferNo())
                .betweenIfPresent(PayTransferDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(PayTransferDO::getId));
    }

    default List<PayTransferDO> selectListByStatus(Integer status) {
        return selectList(PayTransferDO::getPayTransferStatus, CommonEnum.of(status, PayTransferStatusEnum.class));
    }
}




