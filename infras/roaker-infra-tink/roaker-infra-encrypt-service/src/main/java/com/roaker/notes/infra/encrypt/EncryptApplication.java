package com.roaker.notes.infra.encrypt;

import com.roaker.notes.commons.web.annotation.RoakerCloudApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;

/**
 * @author lei.rao
 * @since 1.0
 */
@RoakerCloudApplication
public class EncryptApplication {
    public static void main(String[] args) {
        SpringApplication.run(EncryptApplication.class, args);
    }
}
