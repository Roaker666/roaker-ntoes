package com.roaker.notes.uc;

import com.roaker.notes.commons.web.annotation.RoakerCloudApplication;
import com.sankuai.inf.leaf.plugin.annotation.EnableLeafServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author lei.rao
 * @since 1.0
 */
@RoakerCloudApplication
@EnableLeafServer
public class UserCenterApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserCenterApplication.class, args);
    }
}
