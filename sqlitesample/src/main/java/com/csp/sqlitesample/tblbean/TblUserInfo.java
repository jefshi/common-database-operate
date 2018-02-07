package com.csp.sqlitesample.tblbean;

import com.csp.sqlitesample.base.BaseTableBean;
import com.csp.database.operate.bean.TableField;
import com.csp.sqlitesample.config.TableFields;

public class TblUserInfo extends BaseTableBean {
    private String userId;
    private String status;
//    private String createDate;
//    private String updateDate;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return TableField.toString(TableFields.TBL_USER_INFO, this);
    }

    @Override
    public String[] toFieldsValue() {
        return new String[]{
               getUserId(), getStatus(), getCreateDate() + "", getUpdateDate() + ""
        };
    }

    @Override
    public String[] toPKFieldsValue() {
        return new String[]{
                getUserId()
        };
    }
}
