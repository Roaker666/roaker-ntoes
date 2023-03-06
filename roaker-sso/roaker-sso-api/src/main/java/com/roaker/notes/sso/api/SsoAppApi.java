package com.roaker.notes.sso.api;

import com.roaker.notes.sso.api.constants.SsoConstants;
import com.roaker.notes.sso.api.fallback.SsoAppApiFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author lei.rao
 * @since 1.0
 */
@FeignClient(name = SsoConstants.SSO_NAME, fallbackFactory = SsoAppApiFallbackFactory.class, dismiss404 = true)
public interface SsoAppApi {
}
