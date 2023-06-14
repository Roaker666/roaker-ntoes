package com.roaker.notes.notify.dal.mapper;

import com.roaker.notes.commons.db.core.dataobject.BaseDO;
import com.roaker.notes.commons.db.core.dataobject.PageResult;
import com.roaker.notes.commons.db.core.mapper.BaseMapperX;
import com.roaker.notes.commons.db.core.query.LambdaUpdateWrapperX;
import com.roaker.notes.dynamic.enums.CommonEnum;
import com.roaker.notes.enums.CommonStatusEnum;
import com.roaker.notes.enums.NotifyRecipientTypeEnum;
import com.roaker.notes.notify.dal.dataobject.NotifyTemplateDO;
import com.roaker.notes.notify.vo.ExportNotifyTemplateReqVO;
import com.roaker.notes.notify.vo.QueryNotifyTemplatePageReqVO;
import com.roaker.notes.notify.vo.UpdateNotifyTemplateVO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lei.rao
 * @since 1.0
 */
@Mapper
public interface NotifyTemplateMapper extends BaseMapperX<NotifyTemplateDO> {
    default int update(UpdateNotifyTemplateVO updateDO) {
        return this.update(null, new LambdaUpdateWrapperX<NotifyTemplateDO>()
                .eq(NotifyTemplateDO::getTemplateCode, updateDO.getTemplateCode())
                .setIfPresent(NotifyTemplateDO::getNotifyAr, updateDO.getNotifyAr())
                .setIfPresent(NotifyTemplateDO::getNotifyPn, updateDO.getNotifyPn())
                .setIfPresent(NotifyTemplateDO::getNotifyEmail, updateDO.getNotifyEmail())
                .setIfPresent(NotifyTemplateDO::getNotifySms, updateDO.getNotifySms())
                .setIfPresent(NotifyTemplateDO::getTemplateName, updateDO.getTemplateName())
                .setIfPresent(NotifyTemplateDO::getRecipientType, CommonEnum.of(updateDO.getRecipientType(), NotifyRecipientTypeEnum.class))
                .setIfPresent(NotifyTemplateDO::getRemark, updateDO.getRemark())
                .setIfPresent(NotifyTemplateDO::getStatus, CommonEnum.of(updateDO.getStatus(), CommonStatusEnum.class))
                .set(BaseDO::getUpdateTime, LocalDateTime.now())
                .set(NotifyTemplateDO::getModifier, updateDO.getUid()));
    }

    default PageResult<NotifyTemplateDO> selectPage(QueryNotifyTemplatePageReqVO queryReqVo) {
        return this.selectPage(queryReqVo, new LambdaUpdateWrapperX<NotifyTemplateDO>()
                .eqIfPresent(NotifyTemplateDO::getTemplateCode, queryReqVo.getTemplateCode())
                .likeIfPresent(NotifyTemplateDO::getTemplateName, queryReqVo.getTemplateName())
                .eqIfPresent(NotifyTemplateDO::getScene, queryReqVo.getScene())
                .eqIfPresent(NotifyTemplateDO::getRecipientType, queryReqVo.getRecipientType())
                .orderByDesc(BaseDO::getCreateTime));
    }

    default List<NotifyTemplateDO> selectList(ExportNotifyTemplateReqVO exportVo) {
        return this.selectList(new LambdaUpdateWrapperX<NotifyTemplateDO>()
                .eqIfPresent(NotifyTemplateDO::getTemplateCode, exportVo.getTemplateCode())
                .likeIfPresent(NotifyTemplateDO::getTemplateName, exportVo.getTemplateName())
                .eqIfPresent(NotifyTemplateDO::getScene, exportVo.getScene())
                .eqIfPresent(NotifyTemplateDO::getRecipientType, exportVo.getRecipientType())
                .orderByDesc(BaseDO::getCreateTime));
    }
}
