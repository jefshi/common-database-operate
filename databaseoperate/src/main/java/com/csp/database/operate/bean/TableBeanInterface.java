package com.csp.database.operate.bean;

/**
 * Description: table bean interface
 * <p>Create Date: 2017/04/24
 * <p>Modify Date: nothing
 *
 * @author csp
 * @version 1.0.0
 * @since common-database-operate 1.0.0
 */
@SuppressWarnings({"unused"})
public interface TableBeanInterface {
	/**
	 * 将对象转换成字符串数组，且顺序与相关表的字段顺序一致
	 *
	 * @return String[] 对象值
	 */
	String[] toFieldsValue();

	/**
	 * 将对象中构成主键的成员转换成字符串数组，且顺序与相关表中构成主键的字段顺序一致
	 *
	 * @return String[] 对象主键值
	 */
	String[] toUniqueFieldsValue();

	/**
	 * 获取记录的 _id 字段
	 */
	long get_id();

	/**
	 * 设置记录的 _id 字段
	 */
	void set_id(long _id);
}
