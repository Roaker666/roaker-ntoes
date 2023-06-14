package com.roaker.notes.commons.db.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusPropertiesCustomizer;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.incrementer.H2KeyGenerator;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.gitee.sunchenbin.mybatis.actable.manager.handler.StartUpHandler;
import com.gitee.sunchenbin.mybatis.actable.manager.handler.StartUpHandlerImpl;
import com.google.common.collect.ImmutableList;
import com.roaker.notes.commons.db.properties.MybatisPlusAutoFillProperties;
import com.roaker.notes.seq.SeqClient;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.annotation.Order;

/**
 * @author lei.rao
 * @since 1.0
 */

@Configuration
@EnableConfigurationProperties(MybatisPlusAutoFillProperties.class)
@ComponentScan(
        basePackages = {"com.gitee.sunchenbin.mybatis.actable.manager", "com.roaker.notes"},
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = StartUpHandlerImpl.class)})
@MapperScan(basePackages = {
        "com.gitee.sunchenbin.mybatis.actable.dao",
        "com.roaker.notes.infra.encrypt.dal.mapper",
        "com.roaker.notes.uc.dal.mapper",
})
public class MybatisPlusAutoConfigure {

    /**
     * 分页插件，自动识别数据库类型
     */
    @Bean
    public MybatisPlusInterceptor paginationInterceptor() {
        MybatisPlusInterceptor mpInterceptor = new MybatisPlusInterceptor();
        mpInterceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return mpInterceptor;
    }

    @Bean
    @ConditionalOnProperty(prefix = "mybatis-plus.global-config.db-config", name = "id-type", havingValue = "INPUT")
    public MybatisPlusPropertiesCustomizer plusPropertiesCustomizer(SeqClient seqClient) {
        return plusProperties ->
                plusProperties.getGlobalConfig()
                        .setIdentifierGenerator(seqIdGenerator(seqClient))
                        .getDbConfig()
                        .setKeyGenerators(ImmutableList.of(new H2KeyGenerator()));
    }

    @Bean
    @Order(-1)
    public StartUpHandler startUpHandler() {
        return new CreateTableStartUpHandlerImpl();
    }

    @Bean
    public SeqIdGenerator seqIdGenerator(SeqClient seqClient) {
        return new SeqIdGenerator(seqClient);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "roaker.mybatis-plus.auto-fill", name = "enabled", havingValue = "true", matchIfMissing = true)
    public MetaObjectHandler metaObjectHandler(MybatisPlusAutoFillProperties mybatisPlusAutoFillProperties) {
        return new BasicMetaObjectHandler(mybatisPlusAutoFillProperties);
    }
}
