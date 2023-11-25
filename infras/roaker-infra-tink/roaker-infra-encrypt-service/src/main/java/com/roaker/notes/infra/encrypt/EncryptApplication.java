package com.roaker.notes.infra.encrypt;

import com.roaker.notes.commons.db.util.MyBatisUtils;
import com.roaker.notes.commons.web.annotation.RoakerCloudApplication;
import io.microservices.canvas.extractor.ServiceModelExtractor;
import io.microservices.canvas.extractor.ServiceModelHolder;
import io.microservices.canvas.extractor.spring.SpringContextModelBuilder;
import io.microservices.canvas.extractor.spring.annotations.ServiceDescription;
import io.microservices.canvas.springmvc.MicroserviceCanvasWebConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

/**
 * @author lei.rao
 * @since 1.0
 */
@RoakerCloudApplication
@Import({MicroserviceCanvasWebConfiguration.class})
@ServiceDescription(description = "Encrypt Management", capabilities = "Encrypt Management")
@RequiredArgsConstructor
public class EncryptApplication {
    private final ApplicationContext applicationContext;
    public static void main(String[] args) {
        SpringApplication.run(EncryptApplication.class, args);
    }

    @Bean
    public MyBatisUtils springContextModelBuilder() {
        applicationContext.getBeansOfType(ServiceModelExtractor.class);
        applicationContext.getBeansOfType(ServiceModelHolder.class);
        applicationContext.getBean(SpringContextModelBuilder.class).buildModel();
        return new MyBatisUtils();
    }
}
