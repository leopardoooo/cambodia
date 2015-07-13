/**
 * TGripDataDao.java	2010/11/25
 */

package com.ycsoft.business.dao.config;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TGripData;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;

/**
 * TGripDataDao -> T_GRIP_DATA table's operator
 */
@Component
public class TGripDataDao extends BaseEntityDao<TGripData> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6941396855163299910L;

	/**
	 * default empty constructor
	 */
	public TGripDataDao() {
	}

	public TGripData queryByKey(String gripKey) throws JDBCException {
		return createQuery("select * from t_grip_data where grip_key=?",
				gripKey).first();
	}

	public void save(String gripKey, Date acctDate) throws JDBCException {
		executeUpdate("delete t_grip_data where grip_key=?", gripKey);
		executeUpdate(
				"insert into t_grip_data (grip_key,grip_date) values(?,?) ",
				gripKey, acctDate);
	}

}
