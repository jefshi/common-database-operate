package com.csp.database.operate.interfaces;

/**
 * Description: table bean interface
 * <p>Create Date: 2017/4/24
 * <p>Modify Date: 无
 * <p>Github: https://github.com/jefshi/common-database-operate.git
 *
 * @author csp
 * @version 1.0.0
 * @since common-database-operate 1.0.0
 */
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
	String[] toPKFieldsValue();

	/**
	 * 设置记录添加时间
	 *
	 * @param createDate 插入时间
	 */
	void setCreateDate(long createDate);

	/**
	 * 设置记录更新时间
	 *
	 * @param updateDate 更新时间
	 */
	void setUpdateDate(long updateDate);
}
