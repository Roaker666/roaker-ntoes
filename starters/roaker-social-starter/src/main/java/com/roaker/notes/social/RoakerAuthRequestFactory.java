package com.roaker.notes.social;

import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.ReflectUtil;
import com.roaker.notes.social.core.enums.AuthExtendSource;
import com.roaker.notes.social.core.request.AuthWeChatMiniAppRequest;
import com.xingyuv.jushauth.cache.AuthStateCache;
import com.xingyuv.jushauth.config.AuthConfig;
import com.xingyuv.jushauth.config.AuthSource;
import com.xingyuv.jushauth.request.AuthRequest;
import com.xingyuv.justauth.AuthRequestFactory;
import com.xingyuv.justauth.autoconfigure.JustAuthProperties;

import java.lang.reflect.Method;

/**
 * @author lei.rao
 * @since 1.0
 */
public class RoakerAuthRequestFactory extends AuthRequestFactory {

    protected JustAuthProperties properties;
    protected AuthStateCache authStateCache;

    /**
     * 由于父类 configureHttpConfig 方法是 private 修饰，所以获取后，进行反射调用
     */
    private final Method configureHttpConfigMethod = ReflectUtil.getMethod(AuthRequestFactory.class,
            "configureHttpConfig", String.class, AuthConfig.class, JustAuthProperties.JustAuthHttpConfig.class);

    public RoakerAuthRequestFactory(JustAuthProperties properties, AuthStateCache authStateCache) {
        super(properties, authStateCache);
        this.properties = properties;
        this.authStateCache = authStateCache;
    }

    /**
     * 返回 AuthRequest 对象
     *
     * @param source {@link AuthSource}
     * @return {@link AuthRequest}
     */
    @Override
    public AuthRequest get(String source) {
        // 先尝试获取自定义扩展的
        AuthRequest authRequest = getExtendRequest(source);
        // 找不到，使用默认拓展
        if (authRequest == null) {
            authRequest = super.get(source);
        }
        return authRequest;
    }

    protected AuthRequest getExtendRequest(String source) {
        AuthExtendSource authExtendSource;
        try {
            authExtendSource = EnumUtil.fromString(AuthExtendSource.class, source.toUpperCase());
        } catch (IllegalArgumentException e) {
            // 无自定义匹配
            return null;
        }

        // 拓展配置和默认配置齐平，properties 放在一起
        AuthConfig config = properties.getType().get(authExtendSource.name());
        // 找不到对应关系，直接返回空
        if (config == null) {
            return null;
        }
        // 反射调用，配置 http config
        ReflectUtil.invoke(this, configureHttpConfigMethod, authExtendSource.name(), config, properties.getHttpConfig());

        // 获得拓展的 Request
        // noinspection SwitchStatementWithTooFewBranches
        switch (authExtendSource) {
            case WECHAT_MINI_APP:
                return new AuthWeChatMiniAppRequest(config, authStateCache);
            default:
                return null;
        }
    }
}
