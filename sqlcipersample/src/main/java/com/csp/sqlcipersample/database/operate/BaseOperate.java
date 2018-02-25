package com.csp.sqlcipersample.database.operate;

import android.content.Context;

import com.csp.database.operate.bean.TableField;
import com.csp.database.operate.operate.CommonOperate;
import com.csp.sqlcipersample.database.openhelper.SqlciperSampleHelper;

import java.util.List;

/**
 * Description: sql operate common, must specify the database type
 * <p>Create Date: 2017/04/24
 * <p>Modify Date: 2018/02/09
 *
 * @author csp
 * @version 1.0.1
 * @since common-database-operate 1.0.0
 */
@SuppressWarnings("unused")
public abstract class BaseOperate<T> extends CommonOperate<T> {

    public BaseOperate(Context context, TableField tableField, Class<T> tblBeanClass) {
        super(SqlciperSampleHelper.getInstance(context), tableField, tblBeanClass);
    }

    public List<T> getData() {
        return getData(null, null, null);
    }

    public List<T> getData(String[] whereKey, String[] whereValue) {
        return getData(whereKey, whereValue, null);
    }

    public T getDatum(String[] whereKey, String[] whereValue) {
        return getDatum(whereKey, whereValue, null);
    }
}