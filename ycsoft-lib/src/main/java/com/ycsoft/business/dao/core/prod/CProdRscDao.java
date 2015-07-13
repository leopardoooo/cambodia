/**
 * CUserProdRscDao.java	2010/06/25
 */

package com.ycsoft.business.dao.core.prod;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.prod.CProdRsc;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * CProdRscDao -> C_PROD_RSC table's operator
 */
@Component
public class CProdRscDao extends BaseEntityDao<CProdRsc> {
	/**
	 *
	 */
	private static final long serialVersionUID = -3466872043417464144L;

	/**
	 * default empty constructor
	 */
	public CProdRscDao() {}

	public void removeByProdSn(String prodSn) throws Exception{
		String sql ="delete C_PROD_RSC where prod_sn=?";
		executeUpdate(sql, prodSn);
	}
	
	public void removeByProdRes(String prodSn,String[] resIds) throws Exception{
		if (resIds == null || resIds.length==0)
			return;
		String con ="";
		for (String resId:resIds){
			con += ",'"+resId+"'";
		}
		if (con.length()>0){
			con = con.substring(1);
		}
		String sql ="delete C_PROD_RSC where prod_sn=? and res_id in ("+con+")";
		executeUpdate(sql, prodSn);
	}

	public List<String> queryUserProdRes(String prodSn)throws Exception {
		String sql ="select res_id from c_prod_rsc where prod_sn=?";
		return this.findUniques(sql, prodSn);
	}


}
