package com.roaker.notes.uc.controller;

import com.roaker.notes.uc.api.UserCenterClient;
import com.roaker.notes.uc.converter.UserCenterConvert;
import com.roaker.notes.uc.dto.ShareUserDTO;
import com.roaker.notes.uc.service.UserCenterService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "用户中心 —— user center API")
@Validated
@Slf4j
@RequiredArgsConstructor
public class UserCenterInnerController implements UserCenterClient {
    private final UserCenterService userCenterService;

    @Override
    public ShareUserDTO getByMobile(String mobile) {
        return UserCenterConvert.INSTANCE.convert(userCenterService.getByMobile(mobile));
    }
}
