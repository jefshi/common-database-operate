package com.csp.sqlcipersample.database.operate;

import android.content.Context;

import com.csp.sqlcipersample.database.config.TableFields;
import com.csp.sqlcipersample.database.tblbean.TblPhoneInfo;


/**
 * Description: 表 phoneInfo 操作类
 * <p>Create Date: 2018/02/06
 * <p>Modify Date: nothing
 *
 * @author csp
 * @version 1.0.0
 * @since common-database-operate 1.0.0
 */
public class PhoneInfoOperate extends BaseOperate<TblPhoneInfo> {
	public PhoneInfoOperate(Context context) {
		super(context, TableFields.TBL_PHONE_INFO, TblPhoneInfo.class);
	}
}
