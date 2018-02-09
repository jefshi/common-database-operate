package com.csp.database.operate.base;

import android.database.Cursor;

import com.csp.database.operate.bean.TableField;
import com.csp.database.operate.interfaces.SqlOpenHelperInterface;
import com.csp.database.operate.interfaces.SqlOperateInterface;
import com.csp.database.operate.interfaces.TableBeanInterface;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Description: sql operate common, implement add, delete, update, select
 * <p>Create Date: 2017/04/24
 * <p>Modify Date: 2018/02/09
 *
 * @author csp
 * @version 1.0.0
 * @since common-database-operate 1.0.0
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class CommonOperate<T extends TableBeanInterface> implements SqlOperateInterface<T> {
    private final TableField tableField;
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
    public String[] getPkFields() {
        return tableField.getPkFields();
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
        long time = System.currentTimeMillis();
        for (T datum : data) {
            datum.setCreateDate(time);
            datum.setUpdateDate(time);
            values.add(datum.toFieldsValue());
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
        return delData(getFields(), datum.toPKFieldsValue());
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
            long time = System.currentTimeMillis();
            String sql;
            for (T object : data) {
                object.setUpdateDate(time);

                value = object.toFieldsValue();
                whereValue = object.toPKFieldsValue();
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
            T object;
            String methodName;
            Method method;
            try {
                while (cursor.moveToNext()) {
                    object = tblBeanClass.newInstance();
                    for (int i = 0; i < columnCount; i++) {
                        methodName = getSetMethodName(cursor.getColumnName(i));
                        Class type = getFieldType("");
                        method = tblBeanClass.getMethod(methodName, type);
                        method.invoke(object, cursor.getString(i));
                    }
                    list.add(object);
                }
            } finally {
                cursor.close();
            }
        }
        return list;
    }

    private Class getFieldType(String type) {
        switch (type) {
            case "integer":
                return Long.class;
            default:
                return String.class;
        }
    }

//    private <T> getParam(Class type) {
//
//    }

    /**
     * 获取 set 方法名
     *
     * @param filedName 成员名
     * @return set 方法名
     */
    private String getSetMethodName(String filedName) {
        return "set" + filedName.substring(0, 1).toUpperCase() + filedName.substring(1);
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
    protected boolean isEmpty(String string) {
        return string == null || string.trim().isEmpty();
    }

    /**
     * 集合是否为空
     *
     * @return true: yes
     */
    protected boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 数组是否为空
     *
     * @return true: yes
     */
    protected boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 打印异常日志
     */
    protected void printStackTrace(Exception exception) {
    }
}
