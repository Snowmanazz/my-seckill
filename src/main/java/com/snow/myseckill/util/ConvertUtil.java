package com.snow.myseckill.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class ConvertUtil {
    public static <T> T objectToBean(Object o, Class<T> clazz) {
        if (o == null || clazz == null) {
            return null;
        }
        String value = String.valueOf(o);
        if (clazz == int.class || clazz == Integer.class) {
            return (T) Integer.valueOf(value);
        } else if (clazz == String.class) {
            return (T) value;
        } else if (clazz == long.class || clazz == Long.class) {
            return (T) Long.valueOf(value);
        } else if (clazz == double.class || clazz == Double.class) {
            return (T) Double.valueOf(value);
        } else {
            return JSON.parseObject(JSON.parse(JSON.toJSONString(o)).toString(), clazz);
        }
    }

    public static <T> String beanToString(T value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Number || value instanceof String) {
            return String.valueOf(value);
        } else {
            return JSON.toJSONString(value);
        }
    }

    public static <T> T stringToBean(String value, Class<T> clazz) {
        if (value == null || clazz == null) {
            return null;
        }
        if (clazz == int.class || clazz == Integer.class) {
            return (T) Integer.valueOf(value);
        } else if (clazz == String.class) {
            return (T) value;
        } else if (clazz == long.class || clazz == Long.class) {
            return (T) Long.valueOf(value);
        } else if (clazz == double.class || clazz == Double.class) {
            return (T) Double.valueOf(value);
        } else {
            return (T) JSON.parseObject(value, clazz);
        }
    }
}
