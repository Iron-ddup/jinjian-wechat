package com.jumian.wechat.utils.json;




import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * JSON 序列化
 */
public final class JsonUtils {

    private static JsonMapper jsonMapper = JsonMapper.getDefault();

    public static String toJson(Object object) {
        if (object == null) {
            return "{}";
        }

        return jsonMapper.toJson(object);
    }

    /**
     * 不含值为null的属性
     *
     * @param object
     *
     * @return
     */
    public static String toJsonIgnoreNullField(Object object) {
        return JsonMapper.nonNullMapper().toJson(object);
    }

    public static <T> T toObject(String jsonString, Class<T> clazz) {
        return jsonMapper.toObject(jsonString, clazz);
    }

    public static <T, P> T toObject(String jsonString, Class<T> clazz, Class<P> generic) {
        return jsonMapper.toObject(jsonString, clazz, generic);
    }

    public static <T> List<T> toList(String jsonString, Class<T> clazz) {
        return jsonMapper.toList(jsonString, clazz);
    }

    /**
     * 获取json一个更节点的值，如果出现多个key，返回最后一个
     *
     * @param jsonString
     * @param key
     *
     * @return
     */
    public static String getNode(String jsonString, String key) {
        if (jsonString == null || "".equals(jsonString.trim())) {
            return null;
        }
        String val = jsonMapper.getBykey(jsonString, key);
        if (StringUtils.isEmpty(val)) {
            return null;
        }
        if ("null".equals(val.trim())) {
            val = null;
        } else if (val.indexOf("\"") == 0 && val.lastIndexOf("\"") + 1 == val.length()) {//首位都用双引号"
            val = val.substring(0, val.lastIndexOf("\""));//先去掉尾部的双引号
            val = val.replace("\"", "");//再去掉头部双引号
        }

        return val;
    }

    public static Map toMap(String jsonString) {
        if (jsonString == null || "".equals(jsonString.trim())) {
            return null;
        }
        return toObject(jsonString, LinkedHashMap.class);
    }
}