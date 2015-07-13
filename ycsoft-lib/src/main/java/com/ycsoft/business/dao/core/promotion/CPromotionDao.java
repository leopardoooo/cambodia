/**
 * CPromotionDao.java	2010/07/26
 */

package com.ycsoft.business.dao.core.promotion;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.prod.CProd;
import com.ycsoft.beans.core.promotion.CPromotion;
import com.ycsoft.beans.core.promotion.CPromotionChange;
import com.ycsoft.business.dto.core.prod.CPromotionDto;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * CPromotionDao -> C_PROMOTION table's operator
 */
@Component
public class CPromotionDao extends BaseEntityDao<CPromotion> {

	/**
	 *
	 */
	private static final long serialVersionUID = 7391025584102704218L;

	/**
	 * default empty constructor
	 */
	public CPromotionDao() {}

	/**
	 * @param doneCode
	 * @return
	 */
	public CPromotion queryByDoneCode(Integer doneCode) throws Exception{
		String sql ="select * from c_promotion  where done_code=?";
		return createQuery(sql, doneCode).first();
	}

	/**
	 * 根据用户Id和产品Id查询产品
	 * @param userId
	 * @param prodId
	 * @return
	 * @throws JDBCException
	 */
	public CProd queryProdByUserId(String userId,String prodId) throws JDBCException{
		String sql = "select * from c_prod c where c.user_id=? and c.prod_id=?";
		return createQuery(CProd.class, sql, userId,prodId).first();

	}

	/**
	 * 查询用户促销信息
	 * @param userId
	 * @param countyId
	 * @return
	 * @throws JDBCException
	 */
	public List<CPromotionDto> queryUserPromotion(String userId, String countyId) throws JDBCException{
		String sql = "select c.*,p.promotion_name,p.total_acct_fee,p.repetition_times,p.total_acct_count," +
						"(case when p.total_acct_count>0 and p.total_acct_count<>(select count(1)" +
						" from p_promotion_acct pa where pa.promotion_id=p.promotion_id)" +
						" then 'F' else 'T' end) is_necessary" +
				" from c_promotion c,p_promotion p" +
				" where c.promotion_id=p.promotion_id and c.user_id=? and c.county_id=?";
		return createQuery(CPromotionDto.class,sql, userId, countyId).list();
	}
	
	/**
	 * 可回退的促销.
	 * @param userId
	 * @param countyId
	 * @param prodId 
	 * @return
	 * @throws JDBCException
	 */
	public List<CPromotionDto> queryPromotionCanCancel(String userId, String countyId) throws JDBCException{
		String sql = "select c.promotion_sn,c.promotion_id, p.promotion_name,p.promotion_name,acct.acctitem_name cust_id,acct.acctitem_id user_id,c.create_time,c.status " +
						"from c_promotion c, p_promotion p,busi.c_promotion_acct  pa,busi.vew_acctitem acct " +
						" where c.promotion_id = p.promotion_id and c.user_id = ? and acct.acctitem_id =pa.acctitem_id" +
						" and pa.promotion_sn = c.promotion_sn and c.county_id = ?";
		return createQuery(CPromotionDto.class,sql, userId, countyId).list();
	}
	
	/**
	 * 查询用户下产品作为促销的触发条件而引起的促销(可回退).
	 * @param userId
	 * @param countyId
	 * @param countyId 
	 * @return
	 * @throws Exception
	 */
	public List<CPromotionDto> queryPromotionCanCancelAsCondition(String userId, String prodId, String countyId) throws Exception{
		String sql = "select t.promotion_id,r.rule_id promotion_sn,replace(r.rule_str,' ','') is_necessary,p.promotion_name,pa.acctitem_id user_id,acct.acctitem_name cust_id " +
				"from busi.c_promotion t,busi.p_promotion p,busi.t_rule_define r,busi.c_promotion_acct  pa,busi.vew_acctitem acct " +
				"where t.user_id =? and t.promotion_id = p.promotion_id and r.rule_str like '%" + prodId +"%' " +
				" and acct.acctitem_id  = pa.acctitem_id and t.create_time between trunc(sysdate, 'month')  and last_day(trunc(sysdate, 'month')) + 1 " +
				" and pa.promotion_sn = t.promotion_sn and p.rule_id = r.rule_id and t.status = 'ACTIVE' and t.county_id = ? ";
		return createQuery(CPromotionDto.class,sql, userId, countyId).list();
	}

	public void removePromotionWithHis(String promotionSn, Integer doneCode)throws JDBCException {
		String sql1 = "insert into c_promotion_his (select ? donecode, c.* from c_promotion c where c.promotion_sn=?)";
		executeUpdate(sql1, doneCode,promotionSn);
		
		String sql = "delete c_promotion where promotion_sn=? ";
		executeUpdate(sql, promotionSn);
	}
	
	/**
	 * 更换促销取得原始促销信息
	 * @throws JDBCException 
	 */
	public CPromotionChange queryPromotionChangeHis(String promotionSn) 
	throws JDBCException{
		String sql="select * from c_promotion_change where new_promotion_sn=? ";
		return this.createQuery(CPromotionChange.class, sql, promotionSn).first();
	}
	
	/**
	 * 保存变更促销异动
	 * @param change
	 * @throws JDBCException
	 */
	public void savePromotionChange(CPromotionChange change) throws JDBCException{
		String sql="insert into c_promotion_change(change_done_code,new_promotion_sn,old_promotion_sn,first_promotion_sn) values (?,?,?,?)";
		this.executeUpdate(sql, change.getChange_done_code(),change.getNew_promotion_sn(),change.getOld_promotion_sn(),change.getFirst_promotion_sn());
	}
}
