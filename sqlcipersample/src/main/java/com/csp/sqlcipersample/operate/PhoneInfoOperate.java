package com.csp.sqlcipersample.operate;

import android.content.Context;

import com.csp.sqlcipersample.base.BaseSqlciper;
import com.csp.sqlcipersample.config.TableFields;
import com.csp.sqlcipersample.tblbean.TblPhoneInfo;

/**
 * Description: tbl_test 表操作类
 * <p>Create Date: 2017/7/18
 * <p>Modify Date: 无
 *
 * @author csp
 * @version 1.0.0
 * @since common-database-operate 1.0.0
 */
public class PhoneInfoOperate extends BaseSqlciper<TblPhoneInfo> {
	public PhoneInfoOperate(Context context) {
		super(context, TableFields.TBL_PHONE_INFO, TblPhoneInfo.class);
	}
}
