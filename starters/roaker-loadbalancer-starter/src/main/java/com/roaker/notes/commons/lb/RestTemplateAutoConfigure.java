package com.roaker.notes.commons.lb;

import com.roaker.notes.commons.lb.config.RestTemplateProperties;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * @author lei.rao
 * @since 1.0
 */
@EnableConfigurationProperties(RestTemplateProperties.class)
public class RestTemplateAutoConfigure {

    @Bean
    public RestTemplate restTemplate(RestTemplateProperties restTemplateProperties) {
        ClientHttpRequestFactory factory = httpRequestFactory(restTemplateProperties);
        // 可以添加消息转换
        //restTemplate.setMessageConverters(...);
        // 可以增加拦截器
        //restTemplate.setInterceptors(...);
        return new RestTemplate(factory);
    }
    public ClientHttpRequestFactory httpRequestFactory(RestTemplateProperties restTemplateProperties) {
        return new OkHttp3ClientHttpRequestFactory(okHttpConfigClient(restTemplateProperties));
    }
    public OkHttpClient okHttpConfigClient(RestTemplateProperties restTemplateProperties) {
        return new OkHttpClient().newBuilder()
                .connectionPool(new ConnectionPool(restTemplateProperties.getMaxTotal(), restTemplateProperties.getKeepAliveDuration(), TimeUnit.MILLISECONDS))
                .connectTimeout(Duration.ofMillis(restTemplateProperties.getConnectTimeout()))
                .readTimeout(Duration.ofMillis(restTemplateProperties.getReadTimeout()))
                .writeTimeout(Duration.ofMillis(restTemplateProperties.getWriteTimeout()))
                .retryOnConnectionFailure(true)
                .hostnameVerifier(((hostname, session) -> true))
                //设置代理
                //.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 8888)))
                //.addInterceptor()
                .build();

    }


}
