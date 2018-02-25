package com.csp.database.operate.operate;

import android.database.Cursor;

import com.csp.database.operate.bean.TableField;
import com.csp.database.operate.openhelper.SqlOpenHelperInterface;
import com.csp.database.operate.reflective.GetSetReflective;
import com.csp.database.operate.sql.SqlGenerate;
import com.csp.database.operate.util.EmptyUtil;
import com.csp.database.operate.util.LogCat;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Description: sql operate common, implement add, delete, update, select
 * <p>Create Date: 2017/04/24
 * <p>Modify Date: 2018/02/25
 *
 * @author csp
 * @version 1.0.3
 * @since common-database-operate 1.0.0
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class CommonOperate<T> implements SqlOperateInterface<T> {
    protected final TableField mTableField;
    protected final SqlOpenHelperInterface mOpenHelper;
    protected final SqlGenerate mSqlGenerate;
    protected final Class<T> mTblBeanClass;
    protected final GetSetReflective<T> mReflective;

    public CommonOperate(SqlOpenHelperInterface openHelper, TableField tableField, Class<T> tblBeanClass) {
        mOpenHelper = openHelper;
        mTableField = tableField;
        mTblBeanClass = tblBeanClass;
        mSqlGenerate = new SqlGenerate(tableField.getTableName());
        mReflective = new GetSetReflective<>(tblBeanClass);
    }

    @Override
    public String getTableName() {
        return mTableField.getTableName();
    }

    @Override
    public String[] getFields() {
        return mTableField.getFields();
    }

    @Override
    public String[] getFieldsType() {
        return mTableField.getFieldsType();
    }

    @Override
    public String[] getUniqueFields() {
        return mTableField.getUniqueFields();
    }

    /**
     * 通过反射获取表记录对象的值，允许不存在字段"_id"
     *
     * @param datum 一条记录
     * @param field 字段
     * @return 字段值
     */
    private Object getValueAllowNotExist_id(T datum, String field)
            throws InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        Object value = null;
        try {
            value = mReflective.getValue(datum, field);
        } catch (NoSuchFieldException e) {
            if (!TableField.Constant.INHERENT_FIELD_ID.equals(field))
                throw e;
        }
        return value;
    }

    /**
     * 通过反射获取设置表记录对象的值，允许不存在字段"_id"
     *
     * @param datum 一条记录
     * @param field 字段
     * @param value 字段值
     */
    private void setValueAllowNotExist_id(T datum, String field, Object value)
            throws InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        try {
            mReflective.setValue(datum, field, value);
        } catch (NoSuchFieldException e) {
            if (!TableField.Constant.INHERENT_FIELD_ID.equals(field))
                throw e;
        }
    }

    /**
     * 将对象转换成字符串数组，且顺序与相关表的字段顺序一致
     * //     * 检查表字段并补充 "_id" 字段值
     *
     * @param datum 一条记录
     */
    protected String[] getFieldsValue(T datum)
            throws IllegalAccessException, NoSuchFieldException, InvocationTargetException {
        String[] fields = mTableField.getFields();
        String[] values = new String[fields.length];
        for (int i = 0; i < values.length; i++) {
            Object value = getValueAllowNotExist_id(datum, fields[i]);
            if (value == null)
                continue;

            values[i] = String.valueOf(value);
        }
        return values;
    }

    /**
     * 将对象中构成主键的成员转换成字符串数组，且顺序与相关表中构成主键的字段顺序一致
     * 检查表唯一约束字段
     *
     * @param datum 一条记录
     */
    protected String[] getUniqueFieldsValue(T datum)
            throws IllegalAccessException, NoSuchFieldException, InvocationTargetException {
        String[] fields = mTableField.getUniqueFields();
        String[] values = new String[mTableField.getUniqueFields().length];
        for (int i = 0; i < values.length; i++) {
            Object value = mReflective.getValue(datum, fields[i]);
            if (value == null)
                continue;

            values[i] = String.valueOf(value);
        }
        return values;
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
        try {
            for (T datum : data) {
                values.add(getFieldsValue(datum));
            }
        } catch (Exception e) {
            printStackTrace(e);
            return false;
        }

        String[] key = getFields();
        String sql = mSqlGenerate.insert(key, values);
        return execSQL(sql);
    }

    @Override
    public boolean delData(String[] whereKey, String[] whereValue) {
        return execSQL(mSqlGenerate.delete(whereKey, whereValue));
    }

    @Override
    public boolean delData(T datum) {
        String[] value;
        try {
            value = getUniqueFieldsValue(datum);
        } catch (Exception e) {
            printStackTrace(e);
            return false;
        }
        return delData(getUniqueFields(), value);
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

        mOpenHelper.beginTransaction();
        try {
            String[] key = getFields();
            String[] value;
            String[] whereKey = mTableField.getUniqueFields();
            String[] whereValue;
            String sql;
            for (T datum : data) {
                value = getFieldsValue(datum);
                whereValue = getUniqueFieldsValue(datum);
                sql = mSqlGenerate.update(key, value, whereKey, whereValue);
                mOpenHelper.execSQL(sql);
            }

            mOpenHelper.setTransactionSuccessful();
        } catch (Exception e) {
            printStackTrace(e);
            return false;
        } finally {
            mOpenHelper.endTransaction();
        }
        return true;
    }

    @Override
    public List<T> getData(String[] whereKey, String[] whereValue, String[] orderKey) {
        String[] selectKeys = getFields();
        String sql = mSqlGenerate.query(selectKeys, whereKey, whereValue, orderKey);
        return querySQL(sql, whereValue);
    }

    @Override
    public T getDatum(String[] whereKey, String[] whereValue, String[] orderKey) {
        String[] selectKeys = getFields();
        String sql = mSqlGenerate.query(selectKeys, whereKey, whereValue, orderKey);
        List<T> list = querySQL(sql + " LIMIT 1", whereValue);
        return isEmpty(list) ? null : list.get(0);
    }

    @Override
    public List<T> querySQL(String sql, String[] value) {
        List<T> list = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = mOpenHelper.querySQL(sql, value);
            List<T> data = parseData(cursor);
            list.addAll(data);
        } catch (Exception e) {
            printStackTrace(e);
        } finally {
            if (cursor != null)
                cursor.close();
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
            String[] fieldsType = mTableField.getFieldsType();
            T datum;
            while (cursor.moveToNext()) {
                datum = mTblBeanClass.newInstance();
                for (int i = 0; i < columnCount; i++) {
                    String field = cursor.getColumnName(i);
                    Object value = getFieldValue(cursor, i, fieldsType[i]);
                    setValueAllowNotExist_id(datum, field, value);
                }
                list.add(datum);
            }
        }
        return list;
    }

    /**
     * 获取表的某个字段的值
     *
     * @param cursor 游标
     * @param index  该字段的索引
     * @param type   表字段类型
     * @return 字段的值
     */
    private Object getFieldValue(Cursor cursor, int index, String type) {
        if (type.contains(TableField.Constant.TYPE_INTEGER))
            return cursor.getLong(index);
        else
            return cursor.getString(index);
    }

    @Override
    public boolean execSQL(String sql) {
        try {
            mOpenHelper.execSQL(sql);
        } catch (Exception e) {
            printStackTrace(e);
        }
        return true;
    }

    @Override
    public boolean execSQLByTransaction(String[] sqls) {
        mOpenHelper.beginTransaction();
        try {
            for (String sql : sqls) {
                mOpenHelper.execSQL(sql);
            }
            mOpenHelper.setTransactionSuccessful();
        } catch (Exception e) {
            printStackTrace(e);
            return false;
        } finally {
            mOpenHelper.endTransaction();
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
