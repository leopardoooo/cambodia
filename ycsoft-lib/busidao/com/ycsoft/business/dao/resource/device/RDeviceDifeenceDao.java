/**
 * RDeviceOutputDao.java	2010/09/06
 */

package com.ycsoft.business.dao.resource.device;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.device.RDeviceDifeence;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * RDeviceDifeenceDao -> R_DEVICE_DIFEENCE table's operator
 */
@Component
public class RDeviceDifeenceDao extends BaseEntityDao<RDeviceDifeence> {

 
 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * default empty constructor
	 */
	public RDeviceDifeenceDao() {}
	
	public void saveDiffence(Integer doneCode,SOptr optr,String deviceIds,String remark) throws Exception{
		String sql=" insert into R_DEVICE_DIFEENCE "+
	    		   " select ?, rd.device_id,rd.depot_id,sysdate,?,? from r_device rd  where rd.device_id in ("+deviceIds+") ";
		this.executeUpdate(sql, doneCode, optr.getOptr_id(),remark);
	}
	public void removeDiffence(String [] deviceIds) throws Exception{
		String sql = "delete from R_DEVICE_DIFEENCE  WHERE device_id=?";
		executeBatch(sql, deviceIds);
	}
	
}
