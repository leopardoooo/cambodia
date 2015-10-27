/**
 * PSpkgOpenuserDao.java	2015/09/05
 */
 
package com.ycsoft.business.dao.prod; 

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.prod.PSpkgOpenuser;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * PSpkgOpenuserDao -> P_SPKG_OPENUSER table's operator
 */
@Component
public class PSpkgOpenuserDao extends BaseEntityDao<PSpkgOpenuser> {
	
	/**
	 * default empty constructor
	 */
	public PSpkgOpenuserDao() {}
	
	public void updateOpenUserDoneCode(String spkgSn, Integer doneCode) throws Exception {
		String sql = "update P_SPKG_OPENUSER set use_done_code=?, status=?, status_date=sysdate where status=? and sp_id=(select sp_id from p_spkg where spkg_sn=?)";
		this.executeUpdate(sql, doneCode, StatusConstants.USE, StatusConstants.IDLE, spkgSn);
		
		sql = "update P_SPKG_OPENBUSIFEE set use_done_code=?, status=?, status_date=sysdate where status=? and sp_id=(select sp_id from p_spkg where spkg_sn=?)";
		this.executeUpdate(sql, doneCode, StatusConstants.USE, StatusConstants.IDLE, spkgSn);
	}
	
	public List<PSpkgOpenuser> querySpkgUser(String spkgSn) throws Exception {
		String sql = "select so.*, bm.buy_mode_name, f.fee_name"
				+ " from p_spkg s, p_spkg_openuser so, t_device_buy_mode bm, t_busi_fee f"
				+ " where s.sp_id=so.sp_id and so.buy_type=bm.buy_mode and so.fee_id=f.fee_id(+)"
				+ " and s.spkg_sn = ? and f.fee_type(+)=? and so.status=? and s.status=?";
		return this.createQuery(sql, spkgSn, SystemConstants.FEE_TYPE_DEVICE, StatusConstants.IDLE, StatusConstants.CONFIRM).list();
	}
	
	public List<PSpkgOpenuser> querySpkgUserBySpkgId(String spId) throws Exception {
		String sql = "select so.*, bm.buy_mode_name, f.fee_name"
				+ " from p_spkg_openuser so, t_device_buy_mode bm, t_busi_fee f"
				+ " where so.buy_type=bm.buy_mode and so.fee_id=f.fee_id(+)"
				+ " and so.sp_id = ?"
				+ " order by so.status, so.user_type";
		return this.createQuery(sql, spId).list();
	}

}
