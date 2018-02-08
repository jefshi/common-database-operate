package com.csp.sqlitesample.util;

import java.util.Collection;
import java.util.List;

/**
 * Description: 判定数据是否为空
 * <p>Create Date: 2017/7/18
 * <p>Modify Date: 无
 *
 * @author csp
 * @version 1.0.0
 * @since JavaLibrary 1.0.0
 */
@SuppressWarnings("unused")
public class EmptyUtil {
	/**
	 * 字符串是否为空
	 *
	 * @param str 字符串
	 * @return true: 是
	 */
	public static boolean isEmpty(String str) {
		return str == null || str.trim().isEmpty();
	}

	/**
	 * 集合是否为空
	 *
	 * @param list 集合
	 * @return true: 是
	 */
	public static boolean isEmpty(Collection list) {
		return list == null || list.isEmpty();
	}

	/**
	 * 数组是否为空
	 *
	 * @param array 集合
	 * @return true: 是
	 */
	public static boolean isEmpty(Object[] array) {
		return array == null || array.length == 0;
	}
}
