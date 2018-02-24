package com.csp.database.operate.util;

import java.util.Collection;

/**
 * Description: judge object is empty
 * <p>Create Date: 2017/07/18
 * <p>Modify Date: nothing
 *
 * @author csp
 * @version 1.0.0
 * @since JavaLibrary 1.0.0
 */
@SuppressWarnings("unused")
public class EmptyUtil {
    /**
     * @return true: 字符串为空
     */
    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * @return true: 集合为空
     */
    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * @return true: 数组为空
     */
    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }
}
