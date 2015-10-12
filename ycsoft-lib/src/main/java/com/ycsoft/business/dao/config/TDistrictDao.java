/**
 * TDistrictDao.java	2015/08/24
 */
 
package com.ycsoft.business.dao.config; 

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TDistrict;
import com.ycsoft.business.dto.config.TDistrictDto;
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
	
	public List<TDistrictDto> queryDistrictListByPid(String pId) throws JDBCException {
		String sql = "  select t.*,level from t_district t start with t.district_id = ? "
				+ " connect by prior t.district_id = t.parent_id order by level asc " ;
		return createQuery(TDistrictDto.class,sql, pId).list();
	}

}
