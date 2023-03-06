package com.roaker.notes.infra;

import com.roaker.notes.commons.web.annotation.RoakerCloudApplication;
import com.sankuai.inf.leaf.plugin.annotation.EnableLeafServer;
import org.springframework.boot.SpringApplication;

/**
 * @author lei.rao
 * @since 1.0
 */
@RoakerCloudApplication
@EnableLeafServer
public class SeqApplication {
    public static void main(String[] args) {
        SpringApplication.run(SeqApplication.class, args);
    }
}
