package com.roaker.notes.ac.controller.oauth2.admin;

import com.roaker.notes.ac.controller.oauth2.admin.vo.token.Oauth2AccessTokenPageReqVO;
import com.roaker.notes.ac.controller.oauth2.admin.vo.token.Oauth2AccessTokenRespVO;
import com.roaker.notes.ac.converter.oauth2.Oauth2TokenConvert;
import com.roaker.notes.ac.dal.dataobject.oauth2.Oauth2AccessTokenDO;
import com.roaker.notes.ac.service.auth.AdminAuthService;
import com.roaker.notes.ac.service.oauth2.Oauth2TokenService;
import com.roaker.notes.commons.db.core.dataobject.PageResult;
import com.roaker.notes.enums.LoginLogTypeEnum;
import com.roaker.notes.vo.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import static com.roaker.notes.vo.CommonResult.success;

/**
 * @author lei.rao
 * @since 1.0
 */
@Tag(name = "管理后台 - Oauth2.0 令牌")
@RestController
@RequestMapping("/admin-ua/oauth2-token")
public class Oauth2TokenController {
    @Resource
    private Oauth2TokenService oauth2TokenService;
    @Resource
    private AdminAuthService authService;

    @GetMapping("/page")
    @Operation(summary = "获得访问令牌分页", description = "只返回有效期内的")
    public CommonResult<PageResult<Oauth2AccessTokenRespVO>> getAccessTokenPage(@Valid Oauth2AccessTokenPageReqVO reqVO) {
        PageResult<Oauth2AccessTokenDO> pageResult = oauth2TokenService.getAccessTokenPage(reqVO);
        return success(Oauth2TokenConvert.INSTANCE.convert(pageResult));
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除访问令牌")
    @Parameter(name = "accessToken", description = "访问令牌", required = true, example = "tudou")
    public CommonResult<Boolean> deleteAccessToken(@RequestParam("accessToken") String accessToken) {
        authService.logout(accessToken, LoginLogTypeEnum.LOGOUT_DELETE.getCode());
        return success(true);
    }
}
