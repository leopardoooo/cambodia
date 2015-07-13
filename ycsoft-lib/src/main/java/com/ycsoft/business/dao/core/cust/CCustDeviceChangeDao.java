
/**
 * CCustDeviceChangeDao.java	2010/08/13
 */


package com.ycsoft.business.dao.core.cust;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.cust.CCustDeviceChange;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * CCustDeviceChangeDao -> C_CUST_DEVICE_CHANGE table's operator
 */
@Component
public class CCustDeviceChangeDao extends BaseEntityDao<CCustDeviceChange> {

	/**
	 *
	 */
	private static final long serialVersionUID = 6019936719623609991L;

	/**
	 * default empty constructor
	 */
	public CCustDeviceChangeDao() {}

	/**
	 * @param doneCode
	 * @return
	 */
	public List<CCustDeviceChange> queryByDoneCode(Integer doneCode) throws Exception{
		String sql="select * from C_CUST_DEVICE_CHANGE where done_code=?";
		return this.createQuery(sql, doneCode).list();
	}

	/**
	 * @param doneCode
	 */
	public void removeByDoneCode(Integer doneCode) throws Exception{
		String sql = "delete c_cust_device_change where done_code=?";
		executeUpdate(sql, doneCode);
	}

}
