package com.roaker.notes.dynamic.enums;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

/**
 * @author lei.rao
 * @since 1.0
 */
public class CommonEnumDeserializer extends StdDeserializer<CommonEnum> implements ContextualDeserializer {
    public CommonEnumDeserializer() {
        super((JavaType) null);
    }

    public CommonEnumDeserializer(JavaType valueType) {
        super(valueType);
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext deserializationContext, BeanProperty beanProperty) throws JsonMappingException {
        return new CommonEnumDeserializer(beanProperty.getType());
    }

    @Override
    @SuppressWarnings("all")
    public CommonEnum deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        return CommonEnum.of(jsonParser.getIntValue(), (Class)_valueClass);
    }
}
