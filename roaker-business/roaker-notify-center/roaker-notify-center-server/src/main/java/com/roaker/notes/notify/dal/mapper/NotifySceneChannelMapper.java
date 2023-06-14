package com.roaker.notes.notify.dal.mapper;

import com.roaker.notes.commons.db.core.mapper.BaseMapperX;
import com.roaker.notes.commons.db.core.query.LambdaQueryWrapperX;
import com.roaker.notes.enums.SceneEnum;
import com.roaker.notes.notify.dal.dataobject.NotifySceneChannelDO;

import java.util.List;

/**
 * @author lei.rao
 * @since 1.0
 */
public interface NotifySceneChannelMapper extends BaseMapperX<NotifySceneChannelDO> {
    default List<NotifySceneChannelDO> queryList(String templateCode, SceneEnum sceneEnum) {
        return this.selectList(new LambdaQueryWrapperX<NotifySceneChannelDO>()
                .eq(NotifySceneChannelDO::getTemplateCode, templateCode)
                .eq(NotifySceneChannelDO::getScene, sceneEnum));
    }
}
