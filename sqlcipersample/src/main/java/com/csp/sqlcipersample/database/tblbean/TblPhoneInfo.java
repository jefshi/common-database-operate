package com.csp.sqlcipersample.database.tblbean;

/**
 * Description: 表 phoneInfo 对象
 * <p>Create Date: 2018/02/06
 * <p>Modify Date: nothing
 *
 * @author csp
 * @version 1.0.0
 * @since common-database-operate 1.0.0
 */
public class TblPhoneInfo {
    private long _id;
    private String userId;
    private String phone;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "TblPhoneInfo{" +
                "_id=" + _id +
                ", userId='" + userId + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
