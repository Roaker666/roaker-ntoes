package com.roaker.notes.uc.controller.oauth2.admin;

import com.roaker.notes.uc.converter.oauth2.Oauth2ClientConvert;
import com.roaker.notes.uc.dal.dataobject.oauth2.Oauth2ClientDO;
import com.roaker.notes.uc.service.oauth2.Oauth2ClientService;
import com.roaker.notes.uc.controller.oauth2.admin.vo.client.Oauth2ClientCreateReqVO;
import com.roaker.notes.uc.controller.oauth2.admin.vo.client.Oauth2ClientPageReqVO;
import com.roaker.notes.uc.controller.oauth2.admin.vo.client.Oauth2ClientRespVO;
import com.roaker.notes.uc.controller.oauth2.admin.vo.client.Oauth2ClientUpdateReqVO;
import com.roaker.notes.commons.db.PageResult;
import com.roaker.notes.vo.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.roaker.notes.vo.CommonResult.success;

/**
 * @author lei.rao
 * @since 1.0
 */
@Tag(name = "管理后台 - Oauth2 客户端")
@RestController
@RequestMapping("/admin-ua/oauth2-client")
@Validated
public class Oauth2ClientController {
    @Resource
    private Oauth2ClientService oauth2ClientService;

    @PostMapping("/create")
    @Operation(summary = "创建 Oauth2 客户端")
    public CommonResult<Long> createOauth2Client(@Valid @RequestBody Oauth2ClientCreateReqVO createReqVO) {
        return success(oauth2ClientService.createOauth2Client(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新 Oauth2 客户端")
    public CommonResult<Boolean> updateOauth2Client(@Valid @RequestBody Oauth2ClientUpdateReqVO updateReqVO) {
        oauth2ClientService.updateOauth2Client(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除 Oauth2 客户端")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteOauth2Client(@RequestParam("id") Long id) {
        oauth2ClientService.deleteOauth2Client(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得 Oauth2 客户端")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<Oauth2ClientRespVO> getOauth2Client(@RequestParam("id") Long id) {
        Oauth2ClientDO oAuth2Client = oauth2ClientService.getOauth2Client(id);
        return success(Oauth2ClientConvert.INSTANCE.convert(oAuth2Client));
    }

    @GetMapping("/page")
    @Operation(summary = "获得Oauth2 客户端分页")
    public CommonResult<PageResult<Oauth2ClientRespVO>> getOauth2ClientPage(@Valid Oauth2ClientPageReqVO pageVO) {
        PageResult<Oauth2ClientDO> pageResult = oauth2ClientService.getOauth2ClientPage(pageVO);
        return success(Oauth2ClientConvert.INSTANCE.convertPage(pageResult));
    }

}
