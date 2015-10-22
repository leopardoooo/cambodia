/**
 * TDistrictDao.java	2015/08/24
 */
 
package com.ycsoft.business.dao.config; 

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TDistrict;
import com.ycsoft.business.dto.config.DistrictSysDto;
import com.ycsoft.business.dto.config.TAddressSysDto;
import com.ycsoft.business.dto.config.TDistrictDto;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * TDistrictDao -> T_DISTRICT table's operator
 */
@Component
public class TDistrictDao extends BaseEntityDao<TDistrict> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1462781961678449250L;


	/**
	 * default empty constructor
	 */
	public TDistrictDao() {}
	
	
	public List<TDistrict> queryDistrictListById(String Id) throws JDBCException {
		String sql = "  select level lv,t.* from t_district t start with t.district_id= ? "
				+ " connect by prior t.parent_id=t.district_id order by t.district_level desc" ;
		return createQuery(sql, Id).list();
	}
	
	public List<TDistrict> queryByPidStatus(String Id,String status) throws JDBCException {
		String sql = " select t.* from  t_district t where t.parent_id = ? and t.status <> ?";
		return createQuery(sql, Id,status).list();
	}
	
	public List<TDistrictDto> queryDistrictListByPid(String pId) throws JDBCException {
		String sql = "  select t.*,level from t_district t start with t.district_id = ? "
				+ " connect by prior t.district_id = t.parent_id order by level asc " ;
		return createQuery(TDistrictDto.class,sql, pId).list();
	}
	public List<DistrictSysDto> queryByPid(String pId)  throws JDBCException {
		String sql = "select t.* from  t_district t where t.parent_id = ? ";
		return createQuery(DistrictSysDto.class,sql, pId).list();
	}

	public List<DistrictSysDto> queryAllAddrByName(String name)  throws JDBCException {
		name = name.toLowerCase();
		String sql = "select d.* from (select distinct c.*  from t_district c "
				+ "start with c.district_id in (select a.district_id from t_district a ,(select t.district_id from t_district t "
				+ "where   lower( t.district_name) not like  '%'||?||'%' ) b  "
				+ "where a.parent_id =b. district_id and lower( a.district_name) like '%'||?||'%' "
				+ "union all select t.district_id from t_district t "
				+ "where  lower( t.district_name) like '%'||?||'%' )"
				+ " connect by prior c.parent_id = c.district_id)d order by d.district_level  ";
		return createQuery(DistrictSysDto.class,sql, name,name,name).list();
	}
	public List<DistrictSysDto> queryBaseByPid()  throws JDBCException {
		String sql = "select t.* from  t_district t where t.district_level = '0' ";
		return createQuery(DistrictSysDto.class,sql).list();
	}
	public List<DistrictSysDto> queryAllDistrictTree()  throws JDBCException {
		String sql = "select t.*,level from t_district t where t.district_level in ('0','1') start with t.district_id = ? "
				+ "connect by prior t.district_id = t.parent_id order by level asc ";
		return createQuery(DistrictSysDto.class,sql,SystemConstants.DISTRICT_ID).list();
	}

}
