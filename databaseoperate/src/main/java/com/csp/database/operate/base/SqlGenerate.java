package com.csp.database.operate.base;

import java.util.List;

/**
 * Description: sql generate
 * <p>Create Date: 2017/4/24
 * <p>Modify Date: 无
 * <p>Github: https://github.com/jefshi/common-database-operate.git
 *
 * @author csp
 * @version 1.0.0
 * @since common-database-operate 1.0.0
 */
public class SqlGenerate {
	private String tableName; // 表名

	public SqlGenerate() {
	}

	public SqlGenerate(String tableName) {
		this.tableName = tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * 获取创建表语句
	 *
	 * @param fields   所有字段
	 * @param pkFields 构成主键的字段值
	 * @return sql
	 */
	public String createTable(String[] fields, String[] pkFields) {
		StringBuilder builder = new StringBuilder();
		builder.append("create table if not exists ").append(tableName)
				.append(" ( '_id' integer not null,"); // primary key autoincrement

		String base = " '%s' text,";
		String pkBase = " '%s' text not null,";
		for (String field : fields) {
			if (contains(field, pkFields)) {
				builder.append(String.format(pkBase, field));
			} else {
				builder.append(String.format(base, field));
			}
		}

		builder.append(" primary key (");
		for (String pkField : pkFields) {
			builder.append(" '")
					.append(pkField)
					.append("',");
		}

		builder.deleteCharAt(builder.length() - 1)
				.append("));");
		return builder.toString();
	}

	/**
	 * 指定字段是否为 PK 字段
	 *
	 * @param field    指定字段
	 * @param pkFields 所有 PK 字段
	 * @return boolean true : 是, false : 否
	 */
	private boolean contains(String field, String[] pkFields) {
		if (pkFields == null || field == null) {
			return false;
		}

		for (String pkField : pkFields) {
			if (field.equals(pkField)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 拼INSERT语句
	 *
	 * @param keys      列名
	 * @param variables value
	 * @return sql
	 */
	public String insert(String[] keys, String[] variables) {
		StringBuilder builder = new StringBuilder();
		builder.append("INSERT INTO ").append(tableName).append("( _id,");

		for (String key : keys) {
			builder.append(key).append(",");
		}
		builder.deleteCharAt(builder.length() - 1);

		builder.append(") values (").append(System.currentTimeMillis()).append(",");
		for (String variable : variables) {
			if (variable != null) {
				builder.append("'")
						.append(variable)
						.append("',");
			} else {
				builder.append("NULL,");
			}
		}
		builder.deleteCharAt(builder.length() - 1)
				.append(")");
		return builder.toString();
	}

	/**
	 * 拼INSERT语句
	 *
	 * @param keys   列名
	 * @param values 多条记录的字段值
	 * @return sql
	 */
	public String insert(String[] keys, List<String[]> values) {
		StringBuilder builder = new StringBuilder();
		builder.append("INSERT INTO ").append(tableName).append("( _id,");
		for (String key : keys) {
			builder.append(key).append(",");
		}
		builder.deleteCharAt(builder.length() - 1);

		builder.append(") values ");
		for (String[] value : values) {
			builder.append("(").append(System.currentTimeMillis()).append(",");
			for (String variable : value) {
				if (variable != null) {
					builder.append("'").append(variable).append("',");
				} else {
					builder.append("NULL,");
				}
			}
			builder.deleteCharAt(builder.length() - 1).append("),");
		}
		builder.deleteCharAt(builder.length() - 1);
		return builder.toString();
	}

	/**
	 * 拼DELETE语句
	 *
	 * @param whereKey   列名
	 * @param whereValue value
	 * @return sql
	 */
	public String delete(String[] whereKey, String[] whereValue) {
		StringBuilder builder = new StringBuilder();
		builder.append("DELETE FROM ").append(tableName);

		// WHERE 语句
		if (whereKey != null && whereKey.length != 0) {
			String base = "%s='%s' and ";
			builder.append(" where ");
			for (int i = 0; i < whereKey.length; i++) {
				if (whereValue[i] == null) {
					builder.append(whereKey[i]).append(" ISNULL and ");
				} else {
					builder.append(String.format(base, whereKey[i], whereValue[i]));
				}
			}
			builder.delete(builder.length() - 4, builder.length());
		}
		return builder.toString();
	}

	/**
	 * 拼UPDATE语句
	 *
	 * @param keys        列名
	 * @param value       value
	 * @param whereKeys   where 列名
	 * @param whereValues where value
	 * @return sql
	 */
	public String update(String[] keys, String[] value, String[] whereKeys, String[] whereValues) {
		StringBuilder builder = new StringBuilder();
		builder.append("UPDATE ").append(tableName).append(" SET ");

		if (keys == null || keys.length == 0) {
			return "";
		}

		// SET 语句
		String base = "%s = '%s', ";
		for (int i = 0; i < keys.length; i++) {
			if (value[i] == null) {
				builder.append(keys[i]).append(" = null, ");
			} else {
				builder.append(String.format(base, keys[i], value[i]));
			}
		}
		builder.deleteCharAt(builder.length() - 2);

		// WHERE 语句
		if (whereKeys != null && whereKeys.length != 0) {
			builder.append(" where ");
			String whereBase = "%s='%s' and ";
			for (int i = 0; i < whereKeys.length; i++) {
				if (whereValues[i] == null) {
					builder.append(whereKeys[i]).append(" ISNULL and ");
				} else {
					builder.append(String.format(whereBase, whereKeys[i], whereValues[i]));
				}
			}
			builder.delete(builder.length() - 4, builder.length());
		}
		return builder.toString();
	}

	/**
	 * 拼query语句
	 *
	 * @param whereKeys   列名
	 * @param whereValues where value
	 * @param orderKeys   排序列名, 可以含"ASC", "DESC"标志
	 * @return sql
	 */
	public String query(String[] selectKeys, String[] whereKeys, String[] whereValues, String[] orderKeys) {
		StringBuilder builder = new StringBuilder();

		// SELECT 语句
		builder.append("select ");
		for (String selectKey : selectKeys) {
			builder.append(selectKey).append(",");
		}
		builder.deleteCharAt(builder.length() - 1);
		builder.append(" from ").append(tableName);

		// WHERE 语句
		if (whereKeys != null && whereKeys.length != 0) {
			builder.append(" where ");
			for (int i = 0; i < whereKeys.length; i++) {
				if (whereValues[i] == null) {
					builder.append(whereKeys[i]).append(" ISNULL and ");
				} else {
					builder.append(whereKeys[i]).append("=? and ");
				}
			}
			builder.delete(builder.length() - 4, builder.length());
		}

		// ORDER BY 语句
		if (orderKeys != null && orderKeys.length != 0) {
			builder.append(" ORDER BY ");
			for (String key : orderKeys) {
				builder.append(key).append(",");
			}
			builder.deleteCharAt(builder.length() - 1);
		}

		return builder.toString();
	}
}
