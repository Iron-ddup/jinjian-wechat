package com.jumian.wechat.utils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class JsonUtils {
    /**
     * 对象转成json字符
     *
     * @param object
     * @return
     */
    public static String objectToJson(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * json字符串转成map
     *
     * @param json
     * @return
     */
    public static Map<String, Object> jsonToMap(String json) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        try {
            return new ObjectMapper().readValue(json, new TypeReference<Map<String, Object>>() {
            });
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * json字符串转成map
     *
     * @param json
     * @return
     */
    public static Map<String, String> jsonToMapString(String json) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        try {
            return new ObjectMapper().readValue(json, new TypeReference<Map<String, String>>() {
            });
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static LinkedHashMap<String, Object> jsonToLinkedHashMap(String json) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        try {
            return new ObjectMapper().readValue(json, new TypeReference<LinkedHashMap<String, Object>>() {
            });
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * json字符串转成bean
     *
     * @param json
     * @param cl
     * @return
     */
    public static <T> T jsonToBean(String json, Class<T> cl) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return objectMapper.readValue(json, cl);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * json字符串按驼峰转成bean
     *
     * @param json
     * @param cl
     * @return
     */
    public static <T> T jsonToCaseBean(String json, Class<T> cl) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return objectMapper.readValue(json, cl);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * json字符串转成List
     *
     * @param json
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List<Object> jsonToList(String json) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        try {
            return new ObjectMapper().readValue(json, List.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> List<T> jsonToList(String json, Class<T> cl) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        List<T> ret = new ArrayList<T>();
        List<Object> list = jsonToList(json);
        for (Object object : list) {
            ret.add(jsonToBean(objectToJson(object), cl));
        }
        return ret;
    }
}
