package com.roaker.cloud.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lei.rao
 * @since 1.0
 */
@SpringBootApplication
@RequestMapping("/test")
@RestController
public class Test1Application {
    public static void main(String[] args) {
        SpringApplication.run(Test1Application.class, args);
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello1";
    }

    @GetMapping("/hello2")
    public String hello2() {
        return "hello2";
    }
}
