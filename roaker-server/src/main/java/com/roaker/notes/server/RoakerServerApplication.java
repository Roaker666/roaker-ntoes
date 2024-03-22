package com.roaker.notes.server;

import com.roaker.notes.commons.web.annotation.RoakerCloudApplication;
import com.sankuai.inf.leaf.plugin.annotation.EnableLeafServer;
import com.xingyuv.justauth.autoconfigure.JustAuthAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author lei.rao
 * @since 1.0
 */
@RoakerCloudApplication
@EnableLeafServer
@ComponentScan(
        basePackages = {"com.gitee.sunchenbin.mybatis.actable.manager", "com.roaker.notes"})
@MapperScan(basePackages = {
        "com.gitee.sunchenbin.mybatis.actable.dao",
        "com.roaker.notes.infra.encrypt.dal.mapper",
        "com.roaker.notes.uc.dal.mapper",
        "com.roaker.notes.pay.dal.mapper"
})
@SpringBootApplication(exclude = {JustAuthAutoConfiguration.class})
public class RoakerServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(RoakerServerApplication.class, args);
    }
}
