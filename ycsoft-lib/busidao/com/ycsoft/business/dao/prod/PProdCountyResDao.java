/**
 * PProdCountyResDao.java	2010/09/21
 */

package com.ycsoft.business.dao.prod;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.prod.PProdCountyRes;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.sysmanager.dto.prod.ProdCountyResDto;


/**
 * PProdCountyResDao -> P_PROD_COUNTY_RES table's operator
 */
@Component
public class PProdCountyResDao extends BaseEntityDao<PProdCountyRes> {

	/**
	 *
	 */
	private static final long serialVersionUID = -4984221478547868088L;
	/**
	 * default empty constructor
	 */
	public PProdCountyResDao() {}

	public List<ProdCountyResDto> getProdCountyResByProdId(String prodId)throws Exception{
		String sql = " select t1.prod_id prod_id,t1.county_id county_id,t1.res_id res_id , t2.res_name from p_prod_county_res t1, p_res t2 where t1.res_id = t2.res_id   and t1.prod_id = ?  union all select  t4.prod_id prod_id,?  county_id,t4.res_id res_id,t3.res_name from p_prod_static_res t4, p_res t3 where t4.res_id = t3.res_id  and t4.prod_id  = ? ";
		return createQuery(ProdCountyResDto.class,sql,prodId,SystemConstants.COUNTY_ALL,prodId).list();
	}
	public void deleteByAll (List<Object[]> list) throws Exception {
		String	sql = "delete p_prod_county_res where prod_id = ? and county_id = ? and res_id = ? ";
		executeBatch(sql, list);
	}
}
