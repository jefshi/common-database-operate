package com.csp.database.operate.bean;

import com.csp.database.operate.interfaces.TableBeanInterface;

/**
 * Description: table field info
 * <p>Create Date: 2017/4/24
 * <p>Modify Date: 无
 *
 * @author csp
 * @version 1.0.0
 * @since common-database-operate 1.0.0
 */
public final class TableField {
	private String tableName;

	/*
	 * TODO 所有字段以及类型集合，建表默认追加：
	 * 1. _id  integer primary key autoincrement
	 */
	private String[] fields;
	private String[] fieldsType;

	// 所有构成主键的字段集合，默认"_id"为主键之一
	private String[] pkFields;

	public TableField(String tableName, String[] fields, String[] fieldType, String[] pkFields) {
		this.tableName = tableName;
		this.fields = fields;
		this.fieldsType = fieldType;
		this.pkFields = pkFields;
	}

	public String getTableName() {
		return tableName;
	}

	public String[] getFields() {
		return fields;
	}

	public String[] getFieldsType() {
		return fieldsType;
	}

	public String[] getPkFields() {
		return pkFields;
	}

	/**
	 * 打印表记录内容
	 *
	 * @param tableField 表结构信息
	 * @param tbl        表记录对象
	 * @return 表记录对象.toString()
	 */
	public static String toString(TableField tableField, TableBeanInterface tbl) {
		String str = "";
		String[] fields = tableField.getFields();
		String[] fieldValues = tbl.toFieldsValue();
		for (int i = 0; i < fields.length; i++) {
			str += ", " + fields[i] + "='" + fieldValues[i] + '\'';
		}
		return '{' + str.substring(2) + '}';
	}
}