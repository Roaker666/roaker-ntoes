package com.roaker.notes.commons.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author lei.rao
 * @since 1.0
 */
@Slf4j
public class JacksonUtils {
    private static ObjectMapper mapper;

    /**
     * 序列化级别，默认只序列化不为空的字段
     */
    protected static final JsonInclude.Include DEFAULT_PROPERTY_INCLUSION = JsonInclude.Include.NON_NULL;

    /**
     * 是否缩进JSON格式
     */
    protected static final boolean IS_ENABLE_INDENT_OUTPUT = false;

    static {
        try {
            //初始化
            mapper = new ObjectMapper();
            //配置序列化级别
            mapper.setSerializationInclusion(DEFAULT_PROPERTY_INCLUSION);
            //配置JSON缩进支持
            mapper.configure(SerializationFeature.INDENT_OUTPUT, IS_ENABLE_INDENT_OUTPUT);
            //开启浮点数精确
            mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
            //配置普通属性
            config(mapper);
        } catch (Exception e) {
            log.error("jackson config error", e);
        }
    }


    /**
     * 初始化 objectMapper 属性
     * <p>
     * 通过这样的方式，使用 Spring 创建的 ObjectMapper Bean
     *
     * @param objectMapper ObjectMapper 对象
     */
    public static void init(ObjectMapper objectMapper) {
        JacksonUtils.mapper = objectMapper;
        //配置序列化级别
        mapper.setSerializationInclusion(DEFAULT_PROPERTY_INCLUSION);
        //配置JSON缩进支持
        mapper.configure(SerializationFeature.INDENT_OUTPUT, IS_ENABLE_INDENT_OUTPUT);
        //开启浮点数精确
        mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
        //配置普通属性
        config(mapper);
    }

    protected static void config(ObjectMapper objectMapper) {
        //序列化BigDecimal时之间输出原始数字还是科学计数, 默认false, 即是否以toPlainString()科学计数方式来输出
        objectMapper.enable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN);
        //允许将JSON空字符串强制转换为null对象值
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);

        //允许单个数值当做数组处理
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

        //禁止重复键, 抛出异常
        objectMapper.enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);
        //禁止使用int代表Enum的order()來反序列化Enum, 抛出异常
        objectMapper.enable(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS);
        //有属性不能映射的时候不报错
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        //使用null表示集合类型字段是时不抛异常
        objectMapper.disable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES);
        //对象为空时不抛异常
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        //允许在JSON中使用c/c++风格注释
        objectMapper.enable(JsonParser.Feature.ALLOW_COMMENTS);
        //允许未知字段
        objectMapper.enable(JsonGenerator.Feature.IGNORE_UNKNOWN);
        //在JSON中允许未引用的字段名
        objectMapper.enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES);
        //时间格式
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        //识别单引号
        objectMapper.enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);
        //识别Java8时间
        objectMapper.registerModule(new ParameterNamesModule());
        objectMapper.registerModule(new Jdk8Module());
        objectMapper.registerModule(new JavaTimeModule());
    }

    public static ObjectMapper getObjectMapper() {
        return mapper;
    }

    /**
     * JSON反序列化
     */
    public static <V> V from(URL url, Class<V> type) {
        try {
            return mapper.readValue(url, type);
        } catch (IOException e) {
            log.error("jackson from error, url: {}, type: {}", url.getPath(), type, e);

            throw new IllegalArgumentException(String.format("jackson from error, url: [%s], type: [%s]", url.getPath(), type.getName()));
        }
    }

    /**
     * JSON反序列化
     */
    public static <V> V from(URL url, TypeReference<V> type) {
        try {
            return mapper.readValue(url, type);
        } catch (IOException e) {
            log.error("jackson from error, url: {}, type: {}", url.getPath(), type, e);
            throw new IllegalArgumentException(String.format("jackson from error, url: [%s], type: [%s]", url.getPath(), type.toString()));
        }
    }

    /**
     * JSON反序列化（List）
     */
    public static <V> List<V> fromList(URL url, Class<V> type) {
        try {
            CollectionType collectionType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, type);
            return mapper.readValue(url, collectionType);
        } catch (IOException e) {
            log.error("jackson from error, url: {}, type: {}", url.getPath(), type, e);
            throw new IllegalArgumentException(String.format("jackson from error, url: [%s], type: [%s]", url.getPath(), type.getName()));
        }
    }

    /**
     * JSON反序列化
     */
    public static <V> V from(InputStream inputStream, Class<V> type) {
        try {
            return mapper.readValue(inputStream, type);
        } catch (IOException e) {
            log.error("jackson from error, type: {}", type, e);
            throw new IllegalArgumentException(String.format("jackson from error, type: [%s]", type.getName()));
        }
    }

    /**
     * JSON反序列化
     */
    public static <V> V from(InputStream inputStream, TypeReference<V> type) {
        try {
            return mapper.readValue(inputStream, type);
        } catch (IOException e) {
            log.error("jackson from error, type: {}", type, e);
            throw new IllegalArgumentException(String.format("jackson from error, type: [%s]", type.toString()));
        }
    }

    /**
     * JSON反序列化（List）
     */
    public static <V> List<V> fromList(InputStream inputStream, Class<V> type) {
        try {
            CollectionType collectionType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, type);
            return mapper.readValue(inputStream, collectionType);
        } catch (IOException e) {
            log.error("jackson from error, type: {}", type, e);
            throw new IllegalArgumentException(String.format("jackson from error, type: [%s]", type.getName()));
        }
    }

    /**
     * JSON反序列化
     */
    public static <V> V from(File file, Class<V> type) {
        try {
            return mapper.readValue(file, type);
        } catch (IOException e) {
            log.error("jackson from error, file path: {}, type: {}", file.getPath(), type, e);
            throw new IllegalArgumentException(String.format("jackson from error, file path: [%s], type: [%s]", file.getPath(), type.getName()));
        }
    }

    /**
     * JSON反序列化
     */
    public static <V> V from(File file, TypeReference<V> type) {
        try {
            return mapper.readValue(file, type);
        } catch (IOException e) {
            log.error("jackson from error, file path: {}, type: {}", file.getPath(), type, e);
            throw new IllegalArgumentException(String.format("jackson from error, file path: [%s], type: [%s]", file.getPath(), type.toString()));
        }
    }

    /**
     * JSON反序列化（List）
     */
    public static <V> List<V> fromList(File file, Class<V> type) {
        try {
            CollectionType collectionType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, type);
            return mapper.readValue(file, collectionType);
        } catch (IOException e) {
            log.error("jackson from error, file path: {}, type: {}", file.getPath(), type, e);
            throw new IllegalArgumentException(String.format("jackson from error, file path: [%s], type: [%s]", file.getPath(), type.getName()));
        }
    }

    /**
     * JSON反序列化
     */
    public static <V> V fromJSON(String json, Class<V> type) {
        return from(json, (Type) type);
    }

    /**
     * JSON反序列化
     */
    public static <V> V from(String json, TypeReference<V> type) {
        return from(json, type.getType());
    }

    /**
     * JSON反序列化
     */
    public static <V> V from(String json, Type type) {
        if (json == null) {
            return null;
        }
        try {
            JavaType javaType = mapper.getTypeFactory().constructType(type);
            return mapper.readValue(json, javaType);
        } catch (IOException e) {
            log.error("jackson from error, json: {}, type: {}", json, type, e);
            throw new IllegalArgumentException(String.format("jackson from error, file path: [%s], type: [%s]", json, type.getTypeName()));
        }
    }

    /**
     * JSON反序列化（List）
     */
    public static <V> List<V> fromList(String json, Class<V> type) {
        if (json == null) {
            return null;
        }
        try {
            CollectionType collectionType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, type);
            return mapper.readValue(json, collectionType);
        } catch (IOException e) {
            log.error("jackson from error, json: {}, type: {}", json, type, e);
            throw new IllegalArgumentException(String.format("jackson from error, file path: [%s], type: [%s]", json, type.getName()));
        }
    }

    /**
     * JSON反序列化（Map）
     */
    public static Map<String, Object> fromMap(String json) {
        if (json == null) {
            return null;
        }
        try {
            MapType mapType = mapper.getTypeFactory().constructMapType(HashMap.class, String.class, Object.class);
            return mapper.readValue(json, mapType);
        } catch (IOException e) {
            log.error("jackson from error, json: {}", json, e);
            throw new IllegalArgumentException(String.format("jackson from error, json: [%s]", json));
        }
    }

    /**
     * 序列化为JSON
     */
    public static <V> String to(List<V> list) {
        try {
            return mapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            log.error("jackson to error, data: {}", list, e);
            throw new IllegalArgumentException(String.format("jackson to error, data: [%s]", list.toString()));
        }
    }

    /**
     * 序列化为JSON
     */
    public static <V> String toJSON(V v) {
        try {
            return mapper.writeValueAsString(v);
        } catch (JsonProcessingException e) {
            log.error("jackson to error, data: {}", v, e);
            throw new IllegalArgumentException(String.format("jackson to error, data: [%s]", v.toString()));
        }
    }

    /**
     * 从json串中获取某个字段
     *
     * @return String，默认为 null
     */
    public static String getAsString(String json, String key) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        try {
            JsonNode jsonNode = getAsJsonObject(json, key);
            if (null == jsonNode) {
                return null;
            }
            return getAsString(jsonNode);
        } catch (Exception e) {
            log.error("jackson get string error, json: {}, key: {}", json, key, e);
            throw new IllegalArgumentException(String.format("jackson get string error, json: [%s], key: [%s]", json, key));
        }
    }

    private static String getAsString(JsonNode jsonNode) {
        return jsonNode.isTextual() ? jsonNode.textValue() : jsonNode.toString();
    }

    /**
     * 从json串中获取某个字段
     *
     * @return object, 默认为 null
     */
    public static <V> V getAsObject(String json, String key, Class<V> type) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        try {
            JsonNode jsonNode = getAsJsonObject(json, key);
            if (null == jsonNode) {
                return null;
            }
            JavaType javaType = mapper.getTypeFactory().constructType(type);
            return from(getAsString(jsonNode), javaType);
        } catch (Exception e) {
            log.error("jackson get list error, json: {}, key: {}, type: {}", json, key, type, e);
            throw new IllegalArgumentException(String.format("jackson get list error, json: [%s], key: [%s], type: [%s]", json, key, type.getName()));
        }
    }

    /**
     * 从json串中获取某个字段
     *
     * @return JsonNode, 默认为 null
     */
    public static JsonNode getAsJsonObject(String json, String key) {
        try {
            JsonNode node = mapper.readTree(json);
            if (null == node) {
                return null;
            }
            return node.get(key);
        } catch (IOException e) {
            log.error("jackson get object from json error, json: {}, key: {}", json, key, e);
            throw new IllegalArgumentException(String.format("jackson get object from json error, json: [%s], key: [%s]", json, key));
        }
    }

    /**
     * 向json中添加属性
     *
     * @return json
     */
    public static <V> String add(String json, String key, V value) {
        try {
            JsonNode node = mapper.readTree(json);
            add(node, key, value);
            return node.toString();
        } catch (IOException e) {
            log.error("jackson add error, json: {}, key: {}, value: {}", json, key, value, e);
            throw new IllegalArgumentException(String.format("jackson add error, json: [%s], key: {}, value: [%s]", json, key, value.toString()));
        }
    }

    /**
     * 向json中添加属性
     */
    private static <V> void add(JsonNode jsonNode, String key, V value) {
        if (value instanceof String) {
            ((ObjectNode) jsonNode).put(key, (String) value);
        } else if (value instanceof Short) {
            ((ObjectNode) jsonNode).put(key, (Short) value);
        } else if (value instanceof Integer) {
            ((ObjectNode) jsonNode).put(key, (Integer) value);
        } else if (value instanceof Long) {
            ((ObjectNode) jsonNode).put(key, (Long) value);
        } else if (value instanceof Float) {
            ((ObjectNode) jsonNode).put(key, (Float) value);
        } else if (value instanceof Double) {
            ((ObjectNode) jsonNode).put(key, (Double) value);
        } else if (value instanceof BigDecimal) {
            ((ObjectNode) jsonNode).put(key, (BigDecimal) value);
        } else if (value instanceof BigInteger) {
            ((ObjectNode) jsonNode).put(key, (BigInteger) value);
        } else if (value instanceof Boolean) {
            ((ObjectNode) jsonNode).put(key, (Boolean) value);
        } else if (value instanceof byte[]) {
            ((ObjectNode) jsonNode).put(key, (byte[]) value);
        } else {
            ((ObjectNode) jsonNode).put(key, toJSON(value));
        }
    }

    /**
     * 除去json中的某个属性
     *
     * @return json
     */
    public static String remove(String json, String key) {
        try {
            JsonNode node = mapper.readTree(json);
            ((ObjectNode) node).remove(key);
            return node.toString();
        } catch (IOException e) {
            log.error("jackson remove error, json: {}, key: {}", json, key, e);
            throw new IllegalArgumentException(String.format("jackson remove error, json: [%s], key: [%s]", json, key));
        }
    }

    /**
     * 修改json中的属性
     */
    public static <V> String update(String json, String key, V value) {
        try {
            JsonNode node = mapper.readTree(json);
            ((ObjectNode) node).remove(key);
            add(node, key, value);
            return node.toString();
        } catch (IOException e) {
            log.error("jackson update error, json: {}, key: {}, value: {}", json, key, value, e);
            throw new IllegalArgumentException(String.format("jackson update error, json: [%s], key: [%s], value: [%s]", json, key, value.toString()));
        }
    }


    public static <T> T getPOJOFromObject(Object obj, Class<T> classType) {
        String objJson = JacksonUtils.toJSON(obj);
        return from(objJson, classType);
    }

    /**
     * 格式化Json(美化)
     *
     * @return json
     */
    public static String format(String json) {
        try {
            JsonNode node = mapper.readTree(json);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
        } catch (IOException e) {
            log.error("jackson format json error, json: {}", json, e);
            throw new IllegalArgumentException(String.format("jackson format json error, json: [%s]", json));
        }
    }

    /**
     * 判断字符串是否是json
     *
     * @return json
     */
    public static boolean isJson(String json) {
        try {
            mapper.readTree(json);
            return true;
        } catch (Exception e) {
            log.error("jackson check json error, json: {}", json, e);
            return false;
        }
    }


    /**
     * 递归取多节点数据
     *
     * @param node
     * @param attrs
     * @param isRequired
     * @param message
     * @return
     */
    public static String getJsonNodeValue(JsonNode node, String attrs, boolean isRequired, String message) {
        int index = attrs.indexOf('.');
        if (index == -1) {
            if (node != null && node.get(attrs) != null) {
                return node.get(attrs).textValue();
            }
            if ((node == null || node.get(attrs) == null) && isRequired) {
                log.error(message);
                throw new IllegalArgumentException("invalid param");
            }
            return "";
        } else {
            String s1 = attrs.substring(0, index);
            String s2 = attrs.substring(index + 1);
            return getJsonNodeValue(node.get(s1), s2, isRequired, message);
        }
    }

    /**
     * 递归取多节点数据
     *
     * @param node
     * @param attrs
     * @return
     */
    public static Object getJsonNodeValue(JsonNode node, String attrs) {
        int index = attrs.indexOf('.');
        if (index == -1) {
            if (node != null && node.get(attrs) != null) {
                JsonNode resultNode = node.get(attrs);
                if (resultNode.isDouble() || resultNode.isNumber()) {
                    return resultNode.asDouble();
                } else {
                    return resultNode.textValue();
                }
            }
            return null;
        } else {
            String s1 = attrs.substring(0, index);
            String s2 = attrs.substring(index + 1);
            JsonNode childNode = node.get(s1);
            if (childNode == null) {
                return null;
            }
            return getJsonNodeValue(node.get(s1), s2);
        }
    }

    /**
     * 递归取多节点数据
     *
     * @param node
     * @param attrs
     * @param isRequired
     * @param message
     * @return
     */
    public static BigDecimal getJsonNodeNumber(JsonNode node, String attrs, boolean isRequired, String message) {
        int index = attrs.indexOf('.');
        if (index == -1) {
            if (node != null && node.get(attrs) != null) {
                if (!node.get(attrs).isInt()
                        && !node.get(attrs).isDouble() && !node.get(attrs).isNumber()) {
                    log.error("number is invalid");
                    throw new IllegalArgumentException("invalid param");
                }
                BigDecimal value;
                try {
                    value = node.get(attrs).decimalValue();
                } catch (Exception e) {
                    log.error("number is invalid", e);
                    throw new IllegalArgumentException("invalid param");
                }
                return value;
            }
            if ((node == null || node.get(attrs) == null) && isRequired) {
                log.error(message);
                throw new IllegalArgumentException("invalid param");
            }
            return BigDecimal.ZERO;
        } else {
            String s1 = attrs.substring(0, index);
            String s2 = attrs.substring(index + 1);
            return getJsonNodeNumber(node.get(s1), s2, isRequired, message);
        }
    }

    /**
     * @param node
     * @param message
     * @return
     */
    public static JsonNode requireNonNull(JsonNode node, String message) {
        if (node == null) {
            log.error(message);
            throw new IllegalArgumentException("invalid param");
        }
        return node;
    }


    /**
     * 递归取多节点数据
     *
     * @param node
     * @param attrs
     * @param isRequired
     * @param message
     * @return
     */
    public static JsonNode getJsonNode(JsonNode node, String attrs, boolean isRequired, String message) {
        int index = attrs.indexOf('.');
        if (index == -1) {
            if (node != null && node.get(attrs) != null) {
                return node.get(attrs);
            }
            if ((node == null || node.get(attrs) == null) && isRequired) {
                log.error(message);
                throw new IllegalArgumentException("invalid param");
            }
            return null;
        } else {
            String s1 = attrs.substring(0, index);
            String s2 = attrs.substring(index + 1);
            return getJsonNode(node.get(s1), s2, isRequired, message);
        }
    }


    /**
     * 递归取多节点数据
     *
     * @param node
     * @param attrs
     * @return
     */
    public static JsonNode getJsonNode(JsonNode node, String attrs) {
        int index = attrs.indexOf('.');
        if (index == -1) {
            if (node != null && node.get(attrs) != null) {
                return node.get(attrs);
            }
            return null;
        } else {
            String s1 = attrs.substring(0, index);
            String s2 = attrs.substring(index + 1);
            JsonNode childNode = node.get(s1);
            if (childNode == null) {
                return null;
            }
            return getJsonNode(childNode, s2);
        }
    }


    /**
     * 递归取多节点数据
     *
     * @param str
     * @param attrs
     * @param isRequired
     * @param message
     * @return
     */
    public static String getJsonNodeValue(String str, String attrs, boolean isRequired, String message) {
        int index = attrs.indexOf('.');

        String s1 = attrs.substring(0, index);
        JsonNode node = getAsJsonObject(str, s1);

        if (null == node) {
            if (isRequired) {
                log.error(message);
                throw new IllegalArgumentException("invalid param");
            } else {
                return "";
            }
        }
        String s2 = attrs.substring(index + 1);
        return getJsonNodeValue(node, s2, isRequired, message);
    }


    public static void updateNestedJsonNode(JsonNode node, String parentKey, String nestedKey, String value) {
        if (node.has(parentKey)) {
            JsonNode nestedNode = node.get(parentKey);
            ((ObjectNode) nestedNode).put(nestedKey, value);
        }
        // Add key if not exists
        else {
            // Create nested node
            ObjectNode nestedNode = mapper.createObjectNode();
            nestedNode.put(nestedKey, value);
            // Add to parent
            ((ObjectNode) node).set(parentKey, nestedNode);
        }
    }

    public static String getNestedText(JsonNode node, String parentKey, String nestedKey) {
        if (node.has(parentKey)) {
            return node.get(parentKey).get(nestedKey).textValue();
        }
        return null;
    }

    public static Optional<JsonNode> getChildNode(JsonNode jsonNode, String key) {
        String[] keys = key.split("\\.");

        if (keys.length == 0) {
            throw new IllegalArgumentException(
                    String.format("Invalid key = %s, valid key is a period separated string values", key)
            );
        }

        if (keys.length > 1) {
            String head = keys[0];
            String tails = String.join(".", ArrayUtils.subarray(keys, 1, keys.length));
            return Optional.ofNullable(jsonNode.get(head))
                    .flatMap(nextNode -> getChildNode(nextNode, tails));
        }

        String head = keys[0];

        JsonNode value = jsonNode.get(head);
        return Optional.ofNullable(value).filter(__ -> !__.isNull());
    }

    public static String getChildNodeTextValue(JsonNode jsonNode, String key) {
        return getChildNode(jsonNode, key)
                .map(JsonNode::asText)
                .orElse(null);
    }

    public static BigDecimal getChildNodeBigDecimalValue(JsonNode jsonNode, String key) {
        return getChildNode(jsonNode, key)
                .map(JsonNode::asText)
                .map(BigDecimal::new)
                .orElse(null);
    }
}