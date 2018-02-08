package com.csp.sqlitesample;

/**
 * Description: table bean interface
 * <p>Create Date: 2017/4/24
 * <p>Modify Date: 无
 * <p>Github: https://github.com/jefshi/common-database-operate.git
 *
 * @author csp
 * @version 1.0.0
 * @since common-database-operate 1.0.0
 */
public interface TableBeanInterface {

	String[] toFieldsValue();

	String[] toPKFieldsValue();

	void setCreateDate(long createDate);

	void setUpdateDate(long updateDate);
}
