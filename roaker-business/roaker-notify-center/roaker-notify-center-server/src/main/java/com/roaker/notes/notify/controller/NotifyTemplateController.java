package com.roaker.notes.notify.controller;

import com.roaker.notes.commons.db.core.dataobject.PageResult;
import com.roaker.notes.commons.excel.utils.ExcelUtils;
import com.roaker.notes.notify.dto.NotifyTemplateDto;
import com.roaker.notes.notify.service.NotifyTemplateService;
import com.roaker.notes.notify.vo.CreateNotifyTemplateVO;
import com.roaker.notes.notify.vo.ExportNotifyTemplateReqVO;
import com.roaker.notes.notify.vo.QueryNotifyTemplatePageReqVO;
import com.roaker.notes.notify.vo.UpdateNotifyTemplateVO;
import com.roaker.notes.vo.CommonResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * @author lei.rao
 * @since 1.0
 */
@Tag(name = "消息中心 - 模板配置")
@RestController
@RequestMapping("/notify/template")
@Validated
@Slf4j
@RequiredArgsConstructor
public class NotifyTemplateController {

    private final NotifyTemplateService notifyTemplateService;

    @PostMapping("/create")
    public CommonResult<String> createTemplate(@RequestBody CreateNotifyTemplateVO createNotifyTemplateReq) {
        return CommonResult.success(notifyTemplateService.createNotifyTemplate(createNotifyTemplateReq));
    }

    @PostMapping("/update")
    public CommonResult<String> updateTemplate(@RequestBody UpdateNotifyTemplateVO updateNotifyTemplateReq) {
        return CommonResult.success(notifyTemplateService.updateNotifyTemplate(updateNotifyTemplateReq));
    }

    @PostMapping("/delete")
    public CommonResult<Boolean> deleteTemplate(String templateCode) {
        return CommonResult.success(notifyTemplateService.deleteNotifyTemplate(templateCode));
    }

    @GetMapping("/view")
    public CommonResult<NotifyTemplateDto> viewTemplate(String templateCode) {
        return CommonResult.success(notifyTemplateService.getNotifyTemplate(templateCode));
    }


    @GetMapping("/query")
    public CommonResult<PageResult<NotifyTemplateDto>> queryTemplateList(QueryNotifyTemplatePageReqVO queryReq) {
        return CommonResult.success(notifyTemplateService.queryNotifyTemplatePage(queryReq));
    }

    @GetMapping("/export")
    public void exportTemplateList(@RequestBody ExportNotifyTemplateReqVO exportNotifyTemplateReqVO, HttpServletResponse httpServletResponse) throws IOException {
        List<NotifyTemplateDto> notifyTemplateDtos =
                notifyTemplateService.queryNotifyTemplateList(exportNotifyTemplateReqVO);
        ExcelUtils.write(httpServletResponse, "notifyTempalte.xlxs", "notifyTemplate", NotifyTemplateDto.class, notifyTemplateDtos);
    }
}
