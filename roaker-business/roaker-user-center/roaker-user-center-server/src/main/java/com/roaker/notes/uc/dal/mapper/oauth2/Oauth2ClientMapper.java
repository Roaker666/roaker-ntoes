package com.roaker.notes.uc.dal.mapper.oauth2;

import com.roaker.notes.uc.controller.oauth2.admin.vo.client.Oauth2ClientPageReqVO;
import com.roaker.notes.uc.dal.dataobject.oauth2.Oauth2ClientDO;
import com.roaker.notes.commons.db.core.dataobject.PageResult;
import com.roaker.notes.commons.db.core.mapper.BaseMapperX;
import com.roaker.notes.commons.db.core.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;


/**
 * Oauth2 客户端 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface Oauth2ClientMapper extends BaseMapperX<Oauth2ClientDO> {

    default PageResult<Oauth2ClientDO> selectPage(Oauth2ClientPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<Oauth2ClientDO>()
                .likeIfPresent(Oauth2ClientDO::getName, reqVO.getName())
                .eqIfPresent(Oauth2ClientDO::getStatus, reqVO.getStatus())
                .orderByDesc(Oauth2ClientDO::getId));
    }

    default Oauth2ClientDO selectByClientId(String clientId) {
        return selectOne(Oauth2ClientDO::getClientId, clientId);
    }

}
