/**
 * RStbModelDao.java	2015/08/24
 */

package com.ycsoft.business.dao.resource.device;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.device.RDeviceModel;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;

@Component
public class RDeviceModelDao extends BaseEntityDao<RDeviceModel> {



	/**
	 * 
	 */
	private static final long serialVersionUID = 1297914146071734397L;

	/**
	 * default empty constructor
	 */
	public RDeviceModelDao() {}

	public List<RDeviceModel> queryDeviceModel() throws JDBCException{
		String sql = "SELECT * FROM r_device_model";
		return this.createQuery(RDeviceModel.class, sql).list();
	}

	public RDeviceModel findDevice(String device_type, String device_model) throws JDBCException{
		String sql = "SELECT * FROM r_device_model where device_type=? and device_model = ?";
		return this.createQuery(RDeviceModel.class, sql,device_type,device_model).first();
	}

	public void saveMateral(String device_type, String device_model,
			String model_name) throws JDBCException{
		String sql = "insert into r_device_model(device_type,device_model,model_name) values(?,?,?)";
		this.executeUpdate(sql, device_type,device_model,model_name);
		
	}

	public void updateMateral(String device_type, String device_model,
			String model_name) throws JDBCException{
		String sql = "update r_device_model set model_name=?  where device_type =? and device_model = ? ";
		this.executeUpdate(sql,model_name, device_type,device_model);
		
	}
	
	public RDeviceModel lockModel(String deviceModel) throws JDBCException{
		String sql="select * from r_device_model where device_model=? for update ";
		return this.createQuery(sql, deviceModel).first();
	}
	
}
