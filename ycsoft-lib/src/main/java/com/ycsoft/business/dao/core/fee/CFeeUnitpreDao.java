/**
 * CFeeUnitpreDao.java	2010/10/11
 */

package com.ycsoft.business.dao.core.fee;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.fee.CFeeUnitpre;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * CFeeUnitpreDao -> C_FEE_UNITPRE table's operator
 */
@Component
public class CFeeUnitpreDao extends BaseEntityDao<CFeeUnitpre> {

	/**
	 *
	 */
	private static final long serialVersionUID = -851740802460933137L;

	/**
	 * default empty constructor
	 */
	public CFeeUnitpreDao() {}

	/**
	 * 查询同一地区类的所有预付款
	 * @param countyId
	 * @return
	 * @throws Exception
	 */
	public List<CFeeUnitpre> queryFeeUnitpre(String countyId) throws Exception {
		String sql = "select u.*,f.fee_id,f.real_pay,bf.fee_name from c_fee_unitpre u,c_fee f,t_busi_fee bf" +
				" where bf.fee_id=f.fee_id and u.fee_sn=f.fee_sn and f.county_id=? order by f.fee_sn desc";
		return createQuery(sql,countyId).list();
	}



	/**
	 * 查询单据信息
	 * @param  PrefeeNo
	 */
	public CFeeUnitpre queryByPrefeeNo(String prefeeNo) throws JDBCException {
		String sql = "SELECT * FROM c_fee f,c_fee_unitpre p WHERE f.fee_sn=p.fee_sn AND p.prefee_no=?";
		return createQuery(sql, prefeeNo).first();
	}

	/**
	 * 保存预付费费用
	 *
	 * @param prefeeNo
	 * @param fee
	 */
	public void saveUnitPreFee(String prefeeNo, Integer fee)
			throws JDBCException {
		String sql = "UPDATE c_fee_unitpre p SET p.used=p.used+? WHERE p.prefee_no=?";
		executeUpdate(sql, fee, prefeeNo);
	}

}
