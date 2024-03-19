package com.roaker.notes.pay.dal.mapper.app;

import com.roaker.notes.commons.db.PageResult;
import com.roaker.notes.commons.db.core.mapper.BaseMapperX;
import com.roaker.notes.commons.db.core.query.LambdaQueryWrapperX;
import com.roaker.notes.dynamic.enums.CommonEnum;
import com.roaker.notes.enums.CommonStatusEnum;
import com.roaker.notes.pay.dal.dataobject.app.PayAppDO;
import com.roaker.notes.pay.vo.app.PayAppPageReqVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PayAppMapper extends BaseMapperX<PayAppDO> {

    default PageResult<PayAppDO> selectPage(PayAppPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<PayAppDO>()
                .likeIfPresent(PayAppDO::getName, reqVO.getName())
                .eqIfPresent(PayAppDO::getStatus, CommonEnum.of(reqVO.getStatus(), CommonStatusEnum.class))
                .betweenIfPresent(PayAppDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(PayAppDO::getId));
    }

}
