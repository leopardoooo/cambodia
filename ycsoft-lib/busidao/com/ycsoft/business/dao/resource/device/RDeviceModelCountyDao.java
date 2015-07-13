/**
 * RDeviceModelCountyDao.java	2012/10/18
 */
 
package com.ycsoft.business.dao.resource.device; 

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.device.RDeviceModelCounty;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.sysmanager.dto.tree.TreeDto;


/**
 * RDeviceModelCountyDao -> R_DEVICE_MODEL_COUNTY table's operator
 */
@Component
public class RDeviceModelCountyDao extends BaseEntityDao<RDeviceModelCounty> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8554598878198543296L;

	/**
	 * default empty constructor
	 */
	public RDeviceModelCountyDao() {}

	public List<RDeviceModelCounty> getModelCounty(String dataRight) throws Exception{
		String sql = StringHelper.append("select * from R_DEVICE_MODEL_COUNTY  ");
	    return createQuery(RDeviceModelCounty.class,sql).list();
	}
	public List<RDeviceModelCounty> queryCountyByDataRight(String dataRight) throws Exception{
		String sql = "select * from s_county s where "+dataRight;
		return createQuery(sql).list();
	}
	
	public List<TreeDto> getModelCounty() throws Exception{
		String sql = StringHelper.append("select * from ( select device_type id,'-1' pid ,type_name text  from  r_device_type where manage_detail='T' " +
				"union select device_type||'_'||device_model id,device_type pid, model_name from vew_device_model  ) t  " +
				"start with t.pid = '-1' connect by prior t.id = t.pid  order by level   ");
		return createQuery(TreeDto.class,sql).list();
	}
	public List<TreeDto> getModelCountyById(String countyId) throws Exception{
		String sql = StringHelper.append("select device_type||'_'||device_model id,device_type pid from R_DEVICE_MODEL_COUNTY where county_id =?  ");
	    return createQuery(TreeDto.class,sql,countyId).list();
	}
	public void deleteById (String countyId) throws Exception {
		String	sql = "delete R_DEVICE_MODEL_COUNTY where  county_id = ?  ";
		executeUpdate(sql, countyId);
	}
}
