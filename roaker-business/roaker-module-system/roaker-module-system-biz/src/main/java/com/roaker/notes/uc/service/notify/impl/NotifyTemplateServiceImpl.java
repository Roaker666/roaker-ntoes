package com.roaker.notes.uc.service.notify.impl;

import com.roaker.notes.dynamic.enums.CommonEnum;
import com.roaker.notes.commons.constants.ErrorCodeConstants;
import com.roaker.notes.commons.db.PageResult;
import com.roaker.notes.commons.db.core.query.LambdaQueryWrapperX;
import com.roaker.notes.enums.CommonStatusEnum;
import com.roaker.notes.enums.NotifyChannelEnum;
import com.roaker.notes.enums.NotifyRecipientTypeEnum;
import com.roaker.notes.enums.SceneEnum;
import com.roaker.notes.uc.dal.dataobject.notify.NotifySceneChannelDO;
import com.roaker.notes.uc.dal.dataobject.notify.NotifyTemplateDO;
import com.roaker.notes.uc.dal.mapper.notify.NotifySceneChannelMapper;
import com.roaker.notes.uc.dal.mapper.notify.NotifyTemplateMapper;
import com.roaker.notes.uc.dto.notify.NotifyTemplateDto;
import com.roaker.notes.uc.service.notify.NotifyTemplateService;
import com.roaker.notes.uc.vo.notify.CreateNotifyTemplateVO;
import com.roaker.notes.uc.vo.notify.ExportNotifyTemplateReqVO;
import com.roaker.notes.uc.vo.notify.QueryNotifyTemplatePageReqVO;
import com.roaker.notes.uc.vo.notify.UpdateNotifyTemplateVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.roaker.notes.exception.util.ServiceExceptionUtil.exception;

/**
 * @author lei.rao
 * @since 1.0
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class NotifyTemplateServiceImpl implements NotifyTemplateService {
    private final NotifyTemplateMapper notifyTemplateMapper;
    private final NotifySceneChannelMapper notifySceneChannelMapper;

    @Override
    public String createNotifyTemplate(CreateNotifyTemplateVO createReqVo) {
        boolean exists = notifyTemplateMapper.exists(new LambdaQueryWrapperX<NotifyTemplateDO>()
                .eq(NotifyTemplateDO::getTemplateCode, createReqVo.getTemplateCode()));
        if (exists) {
            throw exception(ErrorCodeConstants.MSG_TEMPLATE_EXISTED);
        }
        NotifyTemplateDO templateDO = NotifyTemplateDO.builder()
                .templateCode(createReqVo.getTemplateCode())
                .templateName(createReqVo.getTemplateName())
                .recipientType(CommonEnum.of(createReqVo.getRecipientType(), NotifyRecipientTypeEnum.class))
                .notifyAr(createReqVo.getNotifyAr())
                .notifyPn(createReqVo.getNotifyPn())
                .notifyEmail(createReqVo.getNotifyEmail())
                .notifySms(createReqVo.getNotifySms())
                .scene(CommonEnum.of(createReqVo.getScene(), SceneEnum.class))
                .status(CommonStatusEnum.ENABLE)
                .remark(createReqVo.getRemark())
                .build();
        templateDO.setCreator(String.valueOf(createReqVo.getUid()));
        templateDO.setModifier(String.valueOf(createReqVo.getUid()));
        notifyTemplateMapper.insert(templateDO);
        List<NotifySceneChannelDO> channelDOList = createReqVo.getEnableChannelStatus().stream()
                .map(channel -> NotifySceneChannelDO.builder()
                        .scene(CommonEnum.of(createReqVo.getScene(), SceneEnum.class))
                        .status(CommonStatusEnum.ENABLE)
                        .notifyChannel(CommonEnum.of(channel, NotifyChannelEnum.class))
                        .build())
                .collect(Collectors.toList());
        notifySceneChannelMapper.insertBatch(channelDOList);
        return createReqVo.getTemplateCode();
    }

    @Override
    public String updateNotifyTemplate(UpdateNotifyTemplateVO updateReqVo) {
        boolean exists = notifyTemplateMapper.exists(new LambdaQueryWrapperX<NotifyTemplateDO>()
                .eq(NotifyTemplateDO::getTemplateCode, updateReqVo.getTemplateCode()));
        if (!exists) {
            throw exception(ErrorCodeConstants.MSG_TEMPLATE_NOT_EXISTS);
        }
        notifyTemplateMapper.update(updateReqVo);
        return updateReqVo.getTemplateCode();
    }

    @Override
    public Boolean deleteNotifyTemplate(String templateCode) {
        return notifyTemplateMapper.delete(new LambdaQueryWrapperX<NotifyTemplateDO>().eq(NotifyTemplateDO::getTemplateCode, templateCode)) > 0;
    }

    @Override
    public NotifyTemplateDto getNotifyTemplate(String templateCode) {
        NotifyTemplateDO notifyTemplateDO = notifyTemplateMapper.selectOne(new LambdaQueryWrapperX<NotifyTemplateDO>()
                .eq(NotifyTemplateDO::getTemplateCode, templateCode));
        if (notifyTemplateDO == null) {
            throw exception(ErrorCodeConstants.MSG_TEMPLATE_NOT_EXISTS);
        }
        List<NotifySceneChannelDO> channelDOList = notifySceneChannelMapper.queryList(templateCode, notifyTemplateDO.getScene());

        return NotifyTemplateDto.from(notifyTemplateDO, channelDOList);
    }

    @Override
    public PageResult<NotifyTemplateDto> queryNotifyTemplatePage(QueryNotifyTemplatePageReqVO queryReqVo) {
        List<NotifyTemplateDto> result = new ArrayList<>();
        PageResult<NotifyTemplateDO> templateDOPageResult = notifyTemplateMapper.selectPage(queryReqVo);
        if (CollectionUtils.isNotEmpty(templateDOPageResult.getList())) {
            result = templateDOPageResult.getList().stream()
                    .map(notifyTemplateDO -> {
                        List<NotifySceneChannelDO> channelDOList = notifySceneChannelMapper.queryList(notifyTemplateDO.getTemplateCode(), notifyTemplateDO.getScene());
                        return NotifyTemplateDto.from(notifyTemplateDO, channelDOList);
                    })
                    .collect(Collectors.toList());
        }
        return new PageResult<>(result, templateDOPageResult.getTotal());
    }

    @Override
    public List<NotifyTemplateDto> queryNotifyTemplateList(ExportNotifyTemplateReqVO exportReqVo) {
        List<NotifyTemplateDto> result = new ArrayList<>();
        List<NotifyTemplateDO> templateDOList = notifyTemplateMapper.selectList(exportReqVo);
        if (CollectionUtils.isNotEmpty(templateDOList)) {
            result = templateDOList.stream()
                    .map(notifyTemplateDO -> {
                        List<NotifySceneChannelDO> channelDOList = notifySceneChannelMapper.queryList(notifyTemplateDO.getTemplateCode(), notifyTemplateDO.getScene());
                        return NotifyTemplateDto.from(notifyTemplateDO, channelDOList);
                    })
                    .collect(Collectors.toList());
        }
        return result;
    }
}
