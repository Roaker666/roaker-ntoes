package com.roaker.notes.uc.service.notify;

import com.roaker.notes.commons.db.PageResult;
import com.roaker.notes.uc.dto.notify.NotifyTemplateDto;
import com.roaker.notes.uc.vo.notify.CreateNotifyTemplateVO;
import com.roaker.notes.uc.vo.notify.ExportNotifyTemplateReqVO;
import com.roaker.notes.uc.vo.notify.QueryNotifyTemplatePageReqVO;
import com.roaker.notes.uc.vo.notify.UpdateNotifyTemplateVO;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * @author lei.rao
 * @since 1.0
 */
public interface NotifyTemplateService {
    String createNotifyTemplate(CreateNotifyTemplateVO createReqVo);

    String updateNotifyTemplate(UpdateNotifyTemplateVO updateReqVo);

    Boolean deleteNotifyTemplate(String templateCode);

    @Cacheable(cacheNames = "roaker-cache", key = "#templateCode")
    NotifyTemplateDto getNotifyTemplate(String templateCode);

    PageResult<NotifyTemplateDto> queryNotifyTemplatePage(QueryNotifyTemplatePageReqVO queryReqVo);

    List<NotifyTemplateDto> queryNotifyTemplateList(ExportNotifyTemplateReqVO exportReqVo);
}
