/**
 * SAreaDao.java	2010/03/07
 */

package com.ycsoft.business.dao.system;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.system.SArea;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * SAreaDao -> S_AREA table's operator
 */
@Component
public class SAreaDao extends BaseEntityDao<SArea> {

	/**
	 *
	 */
	private static final long serialVersionUID = -3392678236405709132L;

	/**
	 * default empty constructor
	 */
	public SAreaDao() {}

	/**
	* @Description: 根据地区编号area_id查询对应信息
	* @param area_id
	* @return
	* @throws Exception
	* @return List<SArea>
	 */
	public List<SArea> getAreaById(String area_id ) throws Exception{
		String sql = "select * from  s_area where area_id ='"+area_id+"' ";
		return createQuery(SArea.class,sql).list();
	}
	
	/**
	* @Description:查询地区编号查询
	* @return
	* @throws Exception
	* @return List<SArea>
	 */
	public List<SArea> getAreaById(String[] areaIds) throws Exception{
		String sql = "select * from  s_area   ";
		return createQuery(SArea.class,sql).list();
	}

}
