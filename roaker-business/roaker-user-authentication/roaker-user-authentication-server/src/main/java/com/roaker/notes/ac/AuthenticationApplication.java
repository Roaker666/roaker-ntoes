package com.roaker.notes.ac;

import com.roaker.notes.commons.web.annotation.RoakerCloudApplication;
import org.springframework.boot.SpringApplication;

@RoakerCloudApplication
public class AuthenticationApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthenticationApplication.class,args);
    }
}
