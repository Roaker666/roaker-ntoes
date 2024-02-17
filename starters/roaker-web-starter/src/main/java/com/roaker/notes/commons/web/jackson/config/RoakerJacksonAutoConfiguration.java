package com.roaker.notes.commons.web.jackson.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.ClassKey;
import com.roaker.notes.commons.utils.JacksonUtils;
import com.roaker.notes.commons.web.jackson.core.databind.LocalDateTimeDeserializer;
import com.roaker.notes.commons.web.jackson.core.databind.LocalDateTimeSerializer;
import com.roaker.notes.commons.web.jackson.core.databind.NumberSerializer;
import com.roaker.notes.dynamic.enums.CommonEnum;
import com.roaker.notes.dynamic.enums.CommonEnumDeserializer;
import com.roaker.notes.enums.CommonStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.LocalDateTime;

@Configuration
@Slf4j
public class RoakerJacksonAutoConfiguration {

    @Bean
    public BeanPostProcessor objectMapperBeanPostProcessor() {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                if (!(bean instanceof ObjectMapper)) {
                    return bean;
                }
                ObjectMapper objectMapper = (ObjectMapper) bean;
                SimpleModule simpleModule = new SimpleModule();
                /*
                 * 1. 新增Long类型序列化规则，数值超过2^53-1，在JS会出现精度丢失问题，因此Long自动序列化为字符串类型
                 * 2. 新增LocalDateTime序列化、反序列化规则
                 */
                simpleModule
                        .addSerializer(Long.class, NumberSerializer.INSTANCE)
                        .addSerializer(Long.TYPE, NumberSerializer.INSTANCE)
                        .addSerializer(LocalDateTime.class, LocalDateTimeSerializer.INSTANCE)
                        .addDeserializer(LocalDateTime.class, LocalDateTimeDeserializer.INSTANCE)
                        .addDeserializer(CommonEnum.class, new CommonEnumDeserializer());
                //自定义查找规则
                simpleModule.setDeserializers(new SimpleDeserializers() {
                    @Override
                    public JsonDeserializer<?> findEnumDeserializer(Class<?> type, DeserializationConfig config,
                                                                    BeanDescription beanDesc) throws JsonMappingException {
                        var enumDeserializer = super.findEnumDeserializer(type, config, beanDesc);
                        if (enumDeserializer != null) {
                            return enumDeserializer;
                        }
                        //遍历枚举实现的接口, 查找反序列化器
                        for (var typeInterface : type.getInterfaces()) {
                            enumDeserializer = this._classMappings.get(new ClassKey(typeInterface));
                            if (enumDeserializer != null) {
                                return enumDeserializer;
                            }
                        }
                        return null;
                    }
                });
                simpleModule.addDeserializer(CommonEnum.class, new CommonEnumDeserializer());

                simpleModule.addSerializer(CommonEnum.class, new JsonSerializer<>() {
                    @Override
                    public void serialize(CommonEnum value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                        gen.writeStartObject();
                        gen.writeNumberField("type", value.getCode());
                        gen.writeStringField("name", value.getName());
                        gen.writeEndObject();
                    }

                    @Override
                    public void serializeWithType(CommonEnum value, JsonGenerator gen, SerializerProvider serializers,
                                                  TypeSerializer typeSer) throws IOException {
                        var typeIdDef = typeSer.writeTypePrefix(gen, typeSer.typeId(value, JsonToken.VALUE_STRING));
                        serialize(value, gen, serializers);
                        typeSer.writeTypeSuffix(gen, typeIdDef);
                    }
                });
                objectMapper.registerModules(simpleModule);

                JacksonUtils.init(objectMapper);
                log.info("初始化 jackson 自动配置");
                return bean;
            }
        };
    }

}
