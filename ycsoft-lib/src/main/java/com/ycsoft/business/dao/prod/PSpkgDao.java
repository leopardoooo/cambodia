/**
 * PSpkgDao.java	2015/09/05
 */
 
package com.ycsoft.business.dao.prod; 

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.prod.PSpkg;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * PSpkgDao -> P_SPKG table's operator
 */
@Component
public class PSpkgDao extends BaseEntityDao<PSpkg> {
	
	/**
	 * default empty constructor
	 */
	public PSpkgDao() {}
	
	public List<PSpkg> querySpkgBySn(String spkgSn) throws Exception {
		return this.createQuery("select * from p_spkg where spkg_sn=?", spkgSn).list();
	}

}
