package com.jumian.wechat.utils.json;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.apache.commons.lang3.StringUtils;
//import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonMapper {

    private static JsonMapper defaultMapper = getDefault();

    private ObjectMapper mapper;

    public JsonMapper() {
        this(null);
    }

    public JsonMapper(Include include) {
        mapper = new ObjectMapper();
        // 设置输出时包含属性的风格
        if (include != null) {
            mapper.setSerializationInclusion(include);
        }
        // 设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        // 序列化失败处理
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        mapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, false);
    }

    public JsonMapper enumAndStringConvert(boolean enabled) {
        if (enabled) {
            mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
            mapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
        } else {
            mapper.disable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
            mapper.disable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
        }
        return this;

    }

    public JsonMapper dateAndTimestampConvert(boolean enabled) {
        if (enabled) {
            mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
            mapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
        } else {
            mapper.disable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
            mapper.disable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
        }
        return this;

    }

    /**
     * 创建只输出非Null且非Empty(如List.isEmpty)的属性到Json字符串的Mapper,建议在外部接口中使用.
     */
    public static JsonMapper nonEmptyMapper() {
        return new JsonMapper(Include.NON_EMPTY);
    }

    public static JsonMapper nonNullMapper() {
        return new JsonMapper(Include.NON_NULL);
    }

    /**
     * 创建只输出初始值被改变的属性到Json字符串的Mapper, 最节约的存储方式，建议在内部接口中使用。
     */
    public static JsonMapper nonDefaultMapper() {
        return new JsonMapper(Include.NON_DEFAULT);
    }

    /**
     * Object可以是POJO，也可以是Collection或数组。 如果对象为Null, 返回"null". 如果集合为空集合, 返回"[]".
     */
    public String toJson(Object object) {

        try {
            return mapper.writeValueAsString(object);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 反序列化POJO或简单Collection如List<String>.
     *
     * 如果JSON字符串为Null或"null"字符串, 返回Null. 如果JSON字符串为"[]", 返回空集合.
     *
     * 如需反序列化复杂Collection如List<MyBean>, 请使用fromJson(String,JavaType)
     *
     * @see #toObject(String, JavaType)
     */
    public <T> T toObject(String jsonString, Class<T> clazz) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        }

        try {
            return mapper.readValue(jsonString, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 反序列化POJO或简单Collection如List<String>.
     *
     * 如果JSON字符串为Null或"null"字符串, 返回Null. 如果JSON字符串为"[]", 返回空集合.
     *
     * 如需反序列化复杂Collection如List<MyBean>, 请使用fromJson(String,JavaType)
     *
     * @see #toObject(String, JavaType)
     */
    public <T, P> T toObject(String jsonString, Class<T> clazz, Class<P> generic) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        }

        try {
            JavaType javaType = mapper.getTypeFactory().constructParametrizedType(clazz, clazz,
                    generic);
            return mapper.readValue(jsonString, javaType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> List<T> toList(String jsonString, Class<T> elementType) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        }

        JavaType javaType = mapper.getTypeFactory().constructParametrizedType(ArrayList.class, ArrayList.class,
                elementType);
        return toObject(jsonString, javaType);
    }

    /**
     * 反序列化复杂Collection如List<Bean>, 先使用函数createCollectionType构造类型,然后调用本函数.
     *
     * @see #toObject(Class, Class...)
     */
    @SuppressWarnings("unchecked")
    public <T> T toObject(String jsonString, JavaType javaType) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        }

        try {
            return (T) mapper.readValue(jsonString, javaType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 当JSON里只含有Bean的部分属性时，更新一个已存在Bean，只覆盖该部分的属性.
     */
    @SuppressWarnings("unchecked")
    public <T> T update(String jsonString, T object) {
        try {
            return (T) mapper.readerForUpdating(object).readValue(jsonString);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 输出JSONP格式数据.
     */
    public String toJsonP(String functionName, Object object) {
        return toJson(new JSONPObject(functionName, object));
    }

    /**
     * 取出Mapper做进一步的设置或使用其他序列化API.
     */
    public ObjectMapper getMapper() {
        return mapper;
    }

    public static synchronized JsonMapper getDefault() {

        if (defaultMapper == null) {
            defaultMapper = new JsonMapper();
            defaultMapper.dateAndTimestampConvert(false);
        }
        return defaultMapper;

    }

    /**
     * 获取json一个更节点的值，如果出现多个key，返回最后一个
     *
     * @param json
     * @param key
     *
     * @return
     */
    public String getBykey(String json, String key) {
        String val = null;
        try {
            JsonNode rootNode = mapper.readTree(json);
            val = rootNode.get(key).toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return val;
    }

}
