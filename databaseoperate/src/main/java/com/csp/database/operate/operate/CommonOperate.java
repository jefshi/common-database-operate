package com.csp.database.operate.operate;

import android.database.Cursor;

import com.csp.database.operate.sql.SqlGenerate;
import com.csp.database.operate.bean.TableField;
import com.csp.database.operate.bean.TableBeanInterface;
import com.csp.database.operate.openhelper.SqlOpenHelperInterface;
import com.csp.database.operate.util.EmptyUtil;
import com.csp.database.operate.util.LogCat;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Description: sql operate common, implement add, delete, update, select
 * <p>Create Date: 2017/04/24
 * <p>Modify Date: 2018/02/24
 *
 * @author csp
 * @version 1.0.2
 * @since common-database-operate 1.0.0
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class CommonOperate<T extends TableBeanInterface> implements SqlOperateInterface<T> {
    protected final TableField tableField;
    protected final SqlOpenHelperInterface openHelper;
    protected final SqlGenerate sqlGenerate;
    protected final Class<T> tblBeanClass;

    public CommonOperate(SqlOpenHelperInterface openHelper, TableField tableField, Class<T> tblBeanClass) {
        this.openHelper = openHelper;
        this.tableField = tableField;
        this.tblBeanClass = tblBeanClass;
        sqlGenerate = new SqlGenerate(tableField.getTableName());
    }

    @Override
    public String getTableName() {
        return tableField.getTableName();
    }

    @Override
    public String[] getFields() {
        return tableField.getFields();
    }

    @Override
    public String[] getFieldsType() {
        return tableField.getFieldsType();
    }

    @Override
    public String[] getUniqueFields() {
        return tableField.getUniqueFields();
    }

    /**
     * 检查表字段并补充 "_id" 字段值
     * {@link TableBeanInterface#toFieldsValue()}
     *
     * @param datum 一条记录
     */
    protected String[] getFieldsValue(T datum) {
        String[] value = datum.toFieldsValue();
        int mustLen = tableField.getFields().length;

        if (value.length == mustLen)
            return value;
        else if (value.length != mustLen - 1)
            throw new Error("fields values num is too more or too less, please check {@link TableBeanInterface#toFieldsValue()}");
        else {
            String[] result = new String[value.length + 1];
            System.arraycopy(value, 0, result, 1, value.length);
            result[0] = datum.get_id() + "";
            return result;
        }
    }

    /**
     * 检查表唯一约束字段
     * {@link TableBeanInterface#toUniqueFieldsValue()}
     *
     * @param datum 一条记录
     */
    protected String[] getUniqueFieldsValue(T datum) {
        String[] value = datum.toUniqueFieldsValue();
        int mustLen = tableField.getUniqueFields().length;

        if (value.length == mustLen)
            return value;
        else
            throw new Error("unique fields values num is too more or too less, please check {@link TableBeanInterface#toUniqueFieldsValue()}");
    }

    @Override
    public boolean addData(T datum) {
        if (datum == null)
            return false;

        List<T> list = new ArrayList<>();
        list.add(datum);
        return addData(list);
    }

    @Override
    public boolean addData(final List<T> data) {
        if (isEmpty(data))
            return false;

        List<String[]> values = new ArrayList<>();
        for (T datum : data) {
            values.add(getFieldsValue(datum));
        }
        String[] key = getFields();
        String sql = sqlGenerate.insert(key, values);
        return execSQL(sql);
    }

    @Override
    public boolean delData(String[] whereKey, String[] whereValue) {
        return execSQL(sqlGenerate.delete(whereKey, whereValue));
    }

    @Override
    public boolean delData(T datum) {
        return delData(getFields(), getUniqueFieldsValue(datum));
    }

    @Override
    public boolean upData(T datum) {
        if (datum == null)
            return false;

        List<T> list = new ArrayList<>();
        list.add(datum);
        return upData(list);
    }

    @Override
    public boolean upData(List<T> data) {
        if (isEmpty(data))
            return true;

        openHelper.beginTransaction();
        try {
            String[] key = getFields();
            String[] value;
            String[] whereKey = tableField.getFields();
            String[] whereValue;
            String sql;
            for (T datum : data) {
                value = getFieldsValue(datum);
                whereValue = getUniqueFieldsValue(datum);
                sql = sqlGenerate.update(key, value, whereKey, whereValue);
                openHelper.execSQL(sql);
            }

            openHelper.setTransactionSuccessful();
        } catch (Exception e) {
            printStackTrace(e);
            return false;
        } finally {
            openHelper.endTransaction();
        }
        return true;
    }

    @Override
    public List<T> getData(String[] whereKey, String[] whereValue, String[] orderKey) {
        String[] selectKeys = getFields();
        String sql = sqlGenerate.query(selectKeys, whereKey, whereValue, orderKey);
        return querySQL(sql, whereValue);
    }

    @Override
    public T getDatum(String[] whereKey, String[] whereValue, String[] orderKey) {
        String[] selectKeys = getFields();
        String sql = sqlGenerate.query(selectKeys, whereKey, whereValue, orderKey);
        List<T> list = querySQL(sql + " LIMIT 1", whereValue);
        return isEmpty(list) ? null : list.get(0);
    }

    @Override
    public List<T> querySQL(String sql, String[] value) {
        List<T> list = new ArrayList<>();
        try {
            Cursor cursor = openHelper.querySQL(sql, value);
            List<T> data = parseData(cursor);
            list.addAll(data);
        } catch (Exception e) {
            printStackTrace(e);
        }
        return list;
    }

    /**
     * 将游标结果解析成表对象数据集合
     *
     * @param cursor 游标
     * @return 表对象数据集合
     */
    private List<T> parseData(Cursor cursor) throws ReflectiveOperationException {
        List<T> list = new ArrayList<>();
        if (cursor != null) {
            int columnCount = cursor.getColumnCount();
            String[] fieldsType = tableField.getFieldsType();
            T tableBean;
            String methodName;
            Class paramType;
            Method method;
            try {
                while (cursor.moveToNext()) {
                    tableBean = tblBeanClass.newInstance();
                    for (int i = 0; i < columnCount; i++) {
                        methodName = getMethodName(cursor.getColumnName(i));
                        paramType = getFieldType(fieldsType[i]);
                        method = tblBeanClass.getMethod(methodName, paramType);
                        method.invoke(tableBean, getFieldValue(cursor, i, paramType));
                    }
                    list.add(tableBean);
                }
            } finally {
                cursor.close();
            }
        }
        return list;
    }

    /**
     * 获取 set 方法名
     *
     * @param filedName 成员名
     * @return set 方法名
     */
    private String getMethodName(String filedName) {
        return "set" + filedName.substring(0, 1).toUpperCase() + filedName.substring(1);
    }

    /**
     * 获取字段的类型
     *
     * @param type 字段的类型（String）
     * @return 字段的类型（Class）
     */
    private Class getFieldType(String type) {
        if (type.contains(TableField.CFieldType.INTEGER))
            return long.class;
        else
            return String.class;
    }

    /**
     * 获取表的某个字段的值
     *
     * @param cursor 游标
     * @param index  该字段的索引
     * @param type   表字段类型
     * @return 字段的值
     */
    private Object getFieldValue(Cursor cursor, int index, Class type) {
        if (type.equals(String.class))
            return cursor.getString(index);
        if (type.equals(long.class))
            return cursor.getLong(index);
        return null;
    }

    @Override
    public boolean execSQL(String sql) {
        try {
            openHelper.execSQL(sql);
        } catch (Exception e) {
            printStackTrace(e);
        }
        return true;
    }

    @Override
    public boolean execSQLByTransaction(String[] sqls) {
        openHelper.beginTransaction();
        try {
            for (String sql : sqls) {
                openHelper.execSQL(sql);
            }
            openHelper.setTransactionSuccessful();
        } catch (Exception e) {
            printStackTrace(e);
            return false;
        } finally {
            openHelper.endTransaction();
        }
        return true;
    }

    @Override
    public boolean execSQLByTransaction(List<String> sqls) {
        return execSQLByTransaction((String[]) sqls.toArray());
    }

    /**
     * 字符串是否为空
     *
     * @return true: yes
     */
    protected static boolean isEmpty(String string) {
        return EmptyUtil.isEmpty(string);
    }

    /**
     * 集合是否为空
     *
     * @return true: yes
     */
    protected static boolean isEmpty(Collection collection) {
        return EmptyUtil.isEmpty(collection);
    }

    /**
     * 数组是否为空
     *
     * @return true: yes
     */
    protected static boolean isEmpty(Object[] array) {
        return EmptyUtil.isEmpty(array);
    }

    /**
     * 打印异常日志
     */
    protected void printStackTrace(Exception exception) {
        LogCat.printStackTrace(exception);
    }
}
