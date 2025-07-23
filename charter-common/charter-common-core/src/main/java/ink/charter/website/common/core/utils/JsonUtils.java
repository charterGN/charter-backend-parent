package ink.charter.website.common.core.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * JSON工具类
 * 基于Jackson提供JSON序列化和反序列化功能
 *
 * @author charter
 * @create 2025/07/19
 */
@Slf4j
public final class JsonUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        // 配置ObjectMapper
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        OBJECT_MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
    }

    private JsonUtils() {
        // 工具类，禁止实例化
    }

    /**
     * 对象转JSON字符串
     *
     * @param obj 对象
     * @return JSON字符串
     */
    public static String toJsonString(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("对象转JSON字符串失败", e);
            return null;
        }
    }

    /**
     * 对象转格式化的JSON字符串
     *
     * @param obj 对象
     * @return 格式化的JSON字符串
     */
    public static String toPrettyJsonString(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("对象转格式化JSON字符串失败", e);
            return null;
        }
    }

    /**
     * JSON字符串转对象
     *
     * @param jsonString JSON字符串
     * @param clazz      目标类型
     * @param <T>        泛型类型
     * @return 对象
     */
    public static <T> T parseObject(String jsonString, Class<T> clazz) {
        if (jsonString == null || jsonString.trim().isEmpty()) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(jsonString, clazz);
        } catch (JsonProcessingException e) {
            log.error("JSON字符串转对象失败，jsonString: {}, clazz: {}", jsonString, clazz.getName(), e);
            return null;
        }
    }

    /**
     * JSON字符串转对象（支持泛型）
     *
     * @param jsonString    JSON字符串
     * @param typeReference 类型引用
     * @param <T>           泛型类型
     * @return 对象
     */
    public static <T> T parseObject(String jsonString, TypeReference<T> typeReference) {
        if (jsonString == null || jsonString.trim().isEmpty()) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(jsonString, typeReference);
        } catch (JsonProcessingException e) {
            log.error("JSON字符串转对象失败，jsonString: {}, typeReference: {}", jsonString, typeReference.getType(), e);
            return null;
        }
    }

    /**
     * JSON字符串转List
     *
     * @param jsonString JSON字符串
     * @param clazz      元素类型
     * @param <T>        泛型类型
     * @return List对象
     */
    public static <T> List<T> parseList(String jsonString, Class<T> clazz) {
        if (jsonString == null || jsonString.trim().isEmpty()) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(jsonString, 
                    OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (JsonProcessingException e) {
            log.error("JSON字符串转List失败，jsonString: {}, clazz: {}", jsonString, clazz.getName(), e);
            return null;
        }
    }

    /**
     * JSON字符串转Map
     *
     * @param jsonString JSON字符串
     * @return Map对象
     */
    public static Map<String, Object> parseMap(String jsonString) {
        return parseObject(jsonString, new TypeReference<Map<String, Object>>() {});
    }

    /**
     * 对象转Map
     *
     * @param obj 对象
     * @return Map对象
     */
    public static Map<String, Object> objectToMap(Object obj) {
        if (obj == null) {
            return null;
        }
        return OBJECT_MAPPER.convertValue(obj, new TypeReference<Map<String, Object>>() {});
    }

    /**
     * Map转对象
     *
     * @param map   Map对象
     * @param clazz 目标类型
     * @param <T>   泛型类型
     * @return 对象
     */
    public static <T> T mapToObject(Map<String, Object> map, Class<T> clazz) {
        if (map == null) {
            return null;
        }
        return OBJECT_MAPPER.convertValue(map, clazz);
    }

    /**
     * 判断字符串是否为有效的JSON
     *
     * @param jsonString JSON字符串
     * @return 是否为有效JSON
     */
    public static boolean isValidJson(String jsonString) {
        if (jsonString == null || jsonString.trim().isEmpty()) {
            return false;
        }
        try {
            OBJECT_MAPPER.readTree(jsonString);
            return true;
        } catch (JsonProcessingException e) {
            return false;
        }
    }

    /**
     * 深度复制对象
     *
     * @param obj   源对象
     * @param clazz 目标类型
     * @param <T>   泛型类型
     * @return 复制后的对象
     */
    public static <T> T deepCopy(Object obj, Class<T> clazz) {
        if (obj == null) {
            return null;
        }
        try {
            String jsonString = OBJECT_MAPPER.writeValueAsString(obj);
            return OBJECT_MAPPER.readValue(jsonString, clazz);
        } catch (JsonProcessingException e) {
            log.error("深度复制对象失败", e);
            return null;
        }
    }

    /**
     * 获取ObjectMapper实例
     *
     * @return ObjectMapper实例
     */
    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }
}