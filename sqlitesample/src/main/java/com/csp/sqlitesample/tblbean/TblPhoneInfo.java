package com.csp.sqlitesample.tblbean;

import com.csp.database.operate.bean.TableField;
import com.csp.sqlitesample.base.BaseTableBean;
import com.csp.sqlitesample.config.TableFields;

public class TblPhoneInfo extends BaseTableBean {
    private String userId;
    private String phone;
//    private String createDate;
//    private String updateDate;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return TableField.toString(TableFields.TBL_USER_INFO, this);
    }

    @Override
    public String[] toFieldsValue() {
        return new String[]{
                getUserId(), getPhone(), getCreateDate() + "", getUpdateDate() + ""
        };
    }

    @Override
    public String[] toPKFieldsValue() {
        return new String[]{
                getUserId()
        };
    }
}
