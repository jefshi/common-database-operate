package com.csp.sqlitesample.database.tblbean;

import com.csp.database.operate.annotations.TableFieldName;

/**
 * Description: 表 phoneInfo 对象
 * <p>Create Date: 2018/02/06
 * <p>Modify Date: nothing
 *
 * @author csp
 * @version 1.0.0
 * @since common-database-operate 1.0.0
 */
public class TblUserInfo {
    @TableFieldName("_id")
    private long _id;
    @TableFieldName("userId")
    private String userId;
    private String status;

    public long get_id() {
        return _id;
    }

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
    public String toString() {
        return "TblUserInfo{" +
                "_id=" + _id +
                ", userId='" + userId + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
