package com.roaker.notes.pay.controller.admin.wallet;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.roaker.notes.commons.db.PageResult;
import com.roaker.notes.commons.utils.RoakerCollectionUtils;
import com.roaker.notes.commons.utils.RoakerMapUtils;
import com.roaker.notes.pay.converter.wallet.PayWalletConvert;
import com.roaker.notes.pay.dal.dataobject.wallet.PayWalletDO;
import com.roaker.notes.pay.service.wallet.PayWalletService;
import com.roaker.notes.pay.vo.wallet.wallet.PayWalletPageReqVO;
import com.roaker.notes.pay.vo.wallet.wallet.PayWalletRespVO;
import com.roaker.notes.pay.vo.wallet.wallet.PayWalletUserReqVO;
import com.roaker.notes.uc.api.user.AdminUserApi;
import com.roaker.notes.uc.vo.user.AdminUserRespDTO;
import com.roaker.notes.vo.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static com.roaker.notes.commons.utils.RoakerCollectionUtils.*;
import static com.roaker.notes.enums.UserTypeEnum.MEMBER;
import static com.roaker.notes.vo.CommonResult.success;

@Tag(name = "管理后台 - 用户钱包")
@RestController
@RequestMapping("/pay/wallet")
@Validated
@Slf4j
public class PayWalletController {

    @Resource
    private PayWalletService payWalletService;
    @Resource
    private AdminUserApi adminUserApi;

    @GetMapping("/get")
    @PreAuthorize("@ss.hasPermission('pay:wallet:query')")
    @Operation(summary = "获得用户钱包明细")
    public CommonResult<PayWalletRespVO> getWallet(PayWalletUserReqVO reqVO) {
        PayWalletDO wallet = payWalletService.getOrCreateWallet(reqVO.getUserId(), MEMBER.getCode());
        // TODO：如果为空，返回给前端只要 null 就可以了
        AdminUserRespDTO user = adminUserApi.getUser(reqVO.getUserId());
        String nickname = user == null ? "" : user.getNickname();
        String avatar = user == null ? "" : user.getAvatar();
        return success(PayWalletConvert.INSTANCE.convert02(nickname, avatar, wallet));
    }

    @GetMapping("/page")
    @Operation(summary = "获得会员钱包分页")
    @PreAuthorize("@ss.hasPermission('pay:wallet:query')")
    public CommonResult<PageResult<PayWalletRespVO>> getWalletPage(@Valid PayWalletPageReqVO pageVO) {
        if (StrUtil.isNotEmpty(pageVO.getNickname())) {
            List<AdminUserRespDTO> users = adminUserApi.getUserListByNickname(pageVO.getNickname());
            pageVO.setUserIds(convertList(users, AdminUserRespDTO::getUid));
        }
        // TODO @jason：管理员也可以先查询下。。
        // 暂时支持查询 userType 会员类型。管理员类型还不知道使用场景
        PageResult<PayWalletDO> pageResult = payWalletService.getWalletPage(MEMBER.getCode(),pageVO);
        if (CollectionUtil.isEmpty(pageResult.getList())) {
            return success(new PageResult<>(pageResult.getTotal()));
        }
        List<AdminUserRespDTO> users = adminUserApi.getUserList(convertList(pageResult.getList(), PayWalletDO::getUserId));
        Map<String, AdminUserRespDTO> userMap = RoakerCollectionUtils.convertMap(users, AdminUserRespDTO::getUid);
        return success(PayWalletConvert.INSTANCE.convertPage(pageResult, userMap));
    }

}
