package com.csp.database.operate.sql;

import com.csp.database.operate.util.EmptyUtil;

import java.util.List;

/**
 * Description: sql generate
 * <p>Create Date: 2017/04/24
 * <p>Modify Date: 2018/0/23
 *
 * @author csp
 * @version 1.0.1
 * @since common-database-operate 1.0.0
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class SqlGenerate {
    private String mTableName;

    public SqlGenerate() {
    }

    public SqlGenerate(String tableName) {
        mTableName = tableName;
    }

    public void setTableName(String tableName) {
        mTableName = tableName;
    }

    /**
     * CREATE TABLE SQL
     *
     * @param fields       所有字段
     * @param fieldsType   所有字段类型
     * @param uniqueFields 构成主键的字段值
     * @return sql
     */
    public String createTable(String[] fields, String[] fieldsType, String[] uniqueFields) {
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE IF NOT EXISTS ")
                .append(mTableName).append(" (");

        String template = " '%s' %s,";
        for (int i = 0; i < fields.length; i++)
            builder.append(String.format(template, fields[i], fieldsType[i]));

        if (uniqueFields.length != 0) {
            builder.append(" UNIQUE (");
            for (String pkField : uniqueFields)
                builder.append(" '").append(pkField).append("',");
        }

        builder.deleteCharAt(builder.length() - 1)
                .append(uniqueFields.length == 0 ? " )" : " ))");
        return builder.toString();
    }

    /**
     * DROP TABLE SQL
     *
     * @return sql
     */
    public String dropTable() {
        return "DROP TABLE IF EXISTS " + mTableName;
    }

    /**
     * INSERT SQL
     *
     * @param fields       字段名
     * @param recordValues 多条记录的字段值
     * @return sql
     */
    public String insert(String[] fields, List<String[]> recordValues) {
        StringBuilder builder = new StringBuilder();
        builder.append("INSERT INTO ").append(mTableName).append(" ( ");

        int _idIndex = -1;
        for (String key : fields) {
            if ("_id".equals(key)) {
                _idIndex++;
                continue;
            }

            builder.append(key).append(", ");
        }
        builder.deleteCharAt(builder.length() - 2);

        builder.append(") VALUES ");
        for (String[] recordValue : recordValues) {
            builder.append("( ");
            for (int i = 0; i < recordValue.length; i++) {
                if (i == _idIndex)
                    continue;

                if (recordValue[i] == null)
                    builder.append("NULL, ");
                else
                    builder.append("'").append(recordValue[i]).append("', ");
            }
            builder.deleteCharAt(builder.length() - 2).append("), ");
        }
        builder.deleteCharAt(builder.length() - 2);
        return builder.toString();
    }

    /**
     * DELETE SQL
     *
     * @param whereKeys   列名
     * @param whereValues value
     * @return sql
     */
    public String delete(String[] whereKeys, String[] whereValues) {
        StringBuilder builder = new StringBuilder();
        builder.append("DELETE FROM ").append(mTableName);

        // WHERE
        where(builder, whereKeys, whereValues);
        return builder.toString();
    }

    /**
     * UPDATE SQL
     *
     * @param field       列名
     * @param fieldValue  value
     * @param whereKeys   where 列名
     * @param whereValues where value
     * @return sql
     */
    public String update(String[] field, String[] fieldValue, String[] whereKeys, String[] whereValues) {
        StringBuilder builder = new StringBuilder();
        builder.append("UPDATE ").append(mTableName);

        // SET
        builder.append(" SET ");
        for (int i = 0; i < field.length; i++) {
            builder.append(field[i]);
            if (fieldValue[i] == null) {
                builder.append(" = NULL, ");
            } else {
                builder.append(" = '").append(fieldValue[i]).append("', ");
            }
        }
        builder.deleteCharAt(builder.length() - 2);

        // WHERE
        where(builder, whereKeys, whereValues);
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
        builder.append("SELECT ");
        for (String selectKey : selectKeys) {
            builder.append(selectKey).append(", ");
        }
        builder.deleteCharAt(builder.length() - 2)
                .append(" FROM ").append(mTableName);

        // WHERE 语句
        where(builder, whereKeys, whereValues);

        // ORDER BY 语句
        if (!isEmpty(orderKeys)) {
            builder.append(" ORDER BY ");
            for (String key : orderKeys) {
                builder.append(key).append(", ");
            }
            builder.deleteCharAt(builder.length() - 2);
        }
        return builder.toString();
    }

    /**
     * WHERE SQL
     *
     * @param builder     sql
     * @param whereKeys   字段名
     * @param whereValues 字段值
     */
    private void where(StringBuilder builder, String[] whereKeys, String[] whereValues) {
        if (isEmpty(whereKeys))
            return;

        builder.append(" WHERE ");
        for (int i = 0; i < whereKeys.length; i++) {
            builder.append(whereKeys[i]);
            if (whereValues[i] == null) {
                builder.append(" ISNULL AND ");
            } else {
                builder.append("='").append(whereValues[i]).append("' AND ");
            }
        }
        builder.delete(builder.length() - 4, builder.length());
    }

    /**
     * @return true: 数组为空
     */
    private boolean isEmpty(Object[] array) {
        return EmptyUtil.isEmpty(array);
    }
}
