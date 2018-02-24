package com.csp.sqlcipersample.database.tblbean;

import com.csp.database.operate.bean.TableBeanInterface;

/**
 * Description: 表 phoneInfo 对象
 * <p>Create Date: 2018/02/06
 * <p>Modify Date: nothing
 *
 * @author csp
 * @version 1.0.0
 * @since common-database-operate 1.0.0
 */
public class TblUserInfo implements TableBeanInterface {
    private long _id;
    private String userId;
    private String status;

    @Override
    public long get_id() {
        return _id;
    }

    @Override
    public void set_id(long _id) {
        this._id = _id;
    }

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
    public String[] toFieldsValue() {
        return new String[]{
               get_id() + "", getUserId(), getStatus()
        };
    }

    @Override
    public String[] toUniqueFieldsValue() {
        return new String[]{
                getUserId()
        };
    }

    @Override
    public String toString() {
        return "TblUserInfo{" +
                "_id=" + _id +
                ", userId='" + userId + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
