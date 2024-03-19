package com.roaker.notes.pay.dal.mapper.notify;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.roaker.notes.commons.db.PageResult;
import com.roaker.notes.commons.db.core.mapper.BaseMapperX;
import com.roaker.notes.commons.db.core.query.LambdaQueryWrapperX;
import com.roaker.notes.dynamic.enums.CommonEnum;
import com.roaker.notes.pay.api.enums.notify.PayNotifyStatusEnum;
import com.roaker.notes.pay.api.enums.notify.PayNotifyTypeEnum;
import com.roaker.notes.pay.dal.dataobject.notify.PayNotifyTaskDO;
import com.roaker.notes.pay.vo.notify.PayNotifyTaskPageReqVO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface PayNotifyTaskMapper extends BaseMapperX<PayNotifyTaskDO> {

    /**
     * 获得需要通知的 PayNotifyTaskDO 记录。需要满足如下条件：
     * 1. status 非成功
     * 2. nextNotifyTime 小于当前时间
     *
     * @return PayTransactionNotifyTaskDO 数组
     */
    default List<PayNotifyTaskDO> selectListByNotify() {
        return selectList(new LambdaQueryWrapper<PayNotifyTaskDO>()
                .in(PayNotifyTaskDO::getPayNotifyStatus, PayNotifyStatusEnum.WAITING.getCode(),
                        PayNotifyStatusEnum.REQUEST_SUCCESS.getCode(), PayNotifyStatusEnum.REQUEST_FAILURE.getCode())
                .le(PayNotifyTaskDO::getNextNotifyTime, LocalDateTime.now()));
    }

    default PageResult<PayNotifyTaskDO> selectPage(PayNotifyTaskPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<PayNotifyTaskDO>()
                .eqIfPresent(PayNotifyTaskDO::getAppId, reqVO.getAppId())
                .eqIfPresent(PayNotifyTaskDO::getPayType, CommonEnum.of(reqVO.getPayType(), PayNotifyTypeEnum.class))
                .eqIfPresent(PayNotifyTaskDO::getDataId, reqVO.getDataId())
                .eqIfPresent(PayNotifyTaskDO::getPayNotifyStatus, CommonEnum.of(reqVO.getPayNotifyStatus(), PayNotifyStatusEnum.class))
                .eqIfPresent(PayNotifyTaskDO::getMerchantOrderId, reqVO.getMerchantOrderId())
                .betweenIfPresent(PayNotifyTaskDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(PayNotifyTaskDO::getId));
    }

}
