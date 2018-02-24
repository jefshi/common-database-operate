package com.csp.database.operate.bean;

/**
 * Description: table field info
 * <p>Create Date: 2017/04/24
 * <p>Modify Date: 2018/02/23
 *
 * @author csp
 * @version 1.0.2
 * @since common-database-operate 1.0.0
 */
@SuppressWarnings({"unused"})
public final class TableField {
    /**
     * table field type
     */
    public interface CFieldType {
        String TEXT = "text";
        String INTEGER = "integer";
    }

    /**
     * 字段默认追加：_id  integer primary key autoincrement
     */
    private String mTableName;
    private String[] mFields;
    private String[] mFieldsType;
    private String[] mUniqueFields;

    public TableField(String tableName, String[] fields, String[] fieldsType, String[] uniqueFields) {
        mTableName = tableName;
        mUniqueFields = uniqueFields;

        initFields(fields, fieldsType);
    }

    private void initFields(String[] fields, String[] fieldsType) {
        int fieldsNum = fields.length + 1;

        boolean contain = false;
        for (String field : fields) {
            if (field.contains("_id")) {
                contain = true;
                break;
            }
        }

        if (contain) {
            mFields = fields;
            mFieldsType = fieldsType;
        } else {
            mFields = new String[fieldsNum];
            System.arraycopy(fields, 0, mFields, 1, fields.length);
            mFields[0] = "_id";

            mFieldsType = new String[fieldsNum];
            System.arraycopy(fieldsType, 0, mFieldsType, 1, fieldsType.length);
            mFieldsType[0] = "integer primary key autoincrement";
        }
    }

    public String getTableName() {
        return mTableName;
    }

    public String[] getFields() {
        return mFields;
    }

    public String[] getFieldsType() {
        return mFieldsType;
    }

    public String[] getUniqueFields() {
        return mUniqueFields;
    }
}