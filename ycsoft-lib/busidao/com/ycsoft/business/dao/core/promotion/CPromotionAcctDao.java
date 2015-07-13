/**
 * CPromotionAcctDao.java	2010/07/26
 */

package com.ycsoft.business.dao.core.promotion;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.promotion.CPromotionAcct;
import com.ycsoft.business.dto.core.prod.CPromotionAcctDto;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * CPromotionAcctDao -> C_PROMOTION_ACCT table's operator
 */
@Component
public class CPromotionAcctDao extends BaseEntityDao<CPromotionAcct> {

	/**
	 *
	 */
	private static final long serialVersionUID = 2404075789055200606L;

	/**
	 * default empty constructor
	 */
	public CPromotionAcctDao() {}

	/**
	 * 查找促销的账户信息
	 * @param promotionSn
	 * @return
	 * @throws Exception
	 */
	public List<CPromotionAcct> queryBySn(String promotionSn) throws Exception{
		String sql = "select * from c_promotion_acct where promotion_sn=?";
		return this.findList(sql, promotionSn);
	}
	
	public List<CPromotionAcctDto> queryPromotionProdBySn(String promotionSn,String promotionId) throws Exception {
		String sql = "select distinct cpa.*,a.acctitem_name prod_name,ppt.tariff_name,ppa.necessary,ppa.promotion_id" +
				" from c_promotion_acct cpa,p_promotion_acct ppa,vew_acctitem a,p_prod_tariff ppt" +
			" where cpa.acctitem_id=ppa.acctitem_id" +
			" and cpa.acctitem_id=a.acctitem_id" +
			" and ppa.tariff_id=ppt.tariff_id(+)"+
			" and cpa.promotion_sn=?" +
			" and ppa.promotion_id=?";
		return this.createQuery(CPromotionAcctDto.class, sql, promotionSn,promotionId).list();
	}

	public void removeBySn(String promotionSn) throws Exception {
		String sql = "delete c_promotion_acct where promotion_sn=?";
		executeUpdate(sql, promotionSn);

	}
	public void removePromotionAcctWithHis(String promotionSn, Integer doneCode)throws JDBCException {
		String sql1 = "insert into c_promotion_acct_his (select ? donecode, c.* from c_promotion_acct c where c.promotion_sn=?)";
		executeUpdate(sql1, doneCode,promotionSn);
		
		String sql = "delete c_promotion_acct where promotion_sn=? ";
		executeUpdate(sql, promotionSn);
	}
}
