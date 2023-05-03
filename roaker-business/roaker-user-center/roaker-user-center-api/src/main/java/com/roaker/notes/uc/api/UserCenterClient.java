package com.roaker.notes.uc.api;

import com.roaker.notes.commons.constants.ApplicationNameConstants;
import com.roaker.notes.uc.dto.ShareUserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = ApplicationNameConstants.UC_NAME, fallbackFactory = UserCenterClientFallback.class, dismiss404 = true)
public interface UserCenterClient {
    @GetMapping("/uc-inner/getUser/{mobile}")
    ShareUserDTO getByMobile(@PathVariable String mobile);
}
