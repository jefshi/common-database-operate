package com.csp.sqlitesample.database.base;

import com.csp.database.operate.interfaces.TableBeanInterface;

/**
 * Created by chenshp on 2018/2/6.
 */

public abstract class BaseTableBean implements TableBeanInterface {
	private long _id;
	private long createDate;
	private long updateDate;

	public long get_id() {
		return _id;
	}

	public void set_id(long _id) {
		this._id = _id;
	}

	public long getCreateDate() {
		return createDate;
	}

	@Override
	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}

	public long getUpdateDate() {
		return updateDate;
	}

	@Override
	public void setUpdateDate(long updateDate) {
		this.updateDate = updateDate;
	}

	@Override
	public String[] toFieldsValue() {
		return new String[0];
	}
}
