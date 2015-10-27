/**
 * PSpkgOpenbusifeeDao.java	2015/09/05
 */
 
package com.ycsoft.business.dao.prod; 

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.prod.PSpkgOpenbusifee;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * PSpkgOpenbusifeeDao -> P_SPKG_OPENBUSIFEE table's operator
 */
@Component
public class PSpkgOpenbusifeeDao extends BaseEntityDao<PSpkgOpenbusifee> {
	
	/**
	 * default empty constructor
	 */
	public PSpkgOpenbusifeeDao() {}
	
	public List<PSpkgOpenbusifee> querySpkgOpenFee(String spkgSn) throws Exception {
		String sql = "select sbf.*,b.fee_name from p_spkg s, p_spkg_openbusifee sbf,t_busi_fee b"
				+ " where s.sp_id=sbf.sp_id and sbf.fee_id=b.fee_id"
				+ " and s.spkg_sn=? and b.fee_type=? and sbf.status=? and s.status=?";
		return this.createQuery(sql, spkgSn, SystemConstants.FEE_TYPE_BUSI, StatusConstants.IDLE, StatusConstants.CONFIRM).list();
	}
	
	public List<PSpkgOpenbusifee> querySpkgOpenFeeBySpId(String spId) throws Exception {
		String sql = "select sbf.*,b.fee_name from p_spkg_openbusifee sbf,t_busi_fee b"
				+ " where sbf.fee_id=b.fee_id and sbf.sp_id=?"
				+ " order by sbf.status, sbf.fee_id";
		return this.createQuery(sql, spId).list();
	}

}
