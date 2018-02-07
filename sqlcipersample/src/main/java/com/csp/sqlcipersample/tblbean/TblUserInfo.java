package com.csp.sqlcipersample.tblbean;

import com.csp.database.operate.bean.TableField;
import com.csp.sqlcipersample.base.BaseTableBean;
import com.csp.sqlcipersample.config.TableFields;

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
