package com.roaker.notes.sso.api.fallback;

import com.roaker.notes.sso.api.SsoAppApi;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * @author lei.rao
 * @since 1.0
 */
public class SsoAppApiFallbackFactory implements FallbackFactory<SsoAppApi> {
    @Override
    public SsoAppApi create(Throwable cause) {
        return new SsoAppApi() {
        };
    }
}
