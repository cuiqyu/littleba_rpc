package com.limpid.utils;

import com.google.common.collect.Lists;
import com.limpid.exception.BusinessException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.List;

/**
 * 异常断言工具
 *
 * @auther cuiqiongyu
 * @create 2020/9/1 10:56
 */
public class ExceptionAssertUtil {

    public static void isTrue(boolean expression) {
        isTrue(expression, "this expression must be true");
    }

    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new BusinessException(message);
        }
    }

    public static void isNull(Object object, String message) {
        if (object != null) {
            throw new BusinessException(message);
        }
    }

    public static void isNull(Object object) {
        isNull(object, "the object argument must be null");
    }

    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new BusinessException(message);
        }
    }

    public static void notNull(Object object) {
        notNull(object, "this argument is required; it must not be null");
    }

    public static void notEmpty(String str, String message) {
        if (StringUtils.isEmpty(str)) {
            throw new BusinessException(message);
        }
    }

    public static void notEmpty(Collection<?> collection) {
        notEmpty(collection, "this collection must not be empty: it must contain at least 1 element");
    }

    public static void notEmpty(Collection<?> collection, String message) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new BusinessException(message);
        }
    }

    public static void allNotNullEmpty(String message, Object obj, Object... objects) {
        List<Object> objectList = Lists.newArrayList(objects);
        objectList.add(obj);
        for (Object object : objectList) {
            if (object instanceof Collection) {
                notEmpty((Collection<?>) object, message);
            } else {
                notNull(object, message);
            }
        }
    }

}
