package com.ycsoft.business.dao.core.acct;

/**
 * CAcctPreFeeDao.java	2011/03/25
 */

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.acct.CAcctPreFee;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * CAcctPreFeeDao -> C_ACCT_PRE_FEE table's operator
 */
@Component
public class CAcctPreFeeDao extends BaseEntityDao<CAcctPreFee> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8434358209902754874L;

	/**
	 * default empty constructor
	 */
	public CAcctPreFeeDao() {}
	
	/**
	 * 通过用户编号和产品编号查询预扣费记录
	 * @param userId
	 * @param prodId
	 * @return
	 * @throws JDBCException 
	 */
	public List<CAcctPreFee> queryByUserIdandProdId(String userId,String prodId) throws JDBCException{
		String sql = "select * from c_acct_pre_fee c where c.user_id=? and c.prod_id=? and "
			+ " c.status='T' and c.process_flag=1";
		return createQuery(CAcctPreFee.class,sql, userId, prodId).list();
	}
	
	/**
	 * 根据交易流水查询预扣费记录
	 * @param transId
	 * @param userId
	 * @return
	 * @throws JDBCException
	 */
	public CAcctPreFee queryByTransId(String transId,String userId) throws JDBCException{
		String sql = "select * from c_acct_pre_fee c where c.trans_id=? and c.user_id=? and c.process_flag=1 ";
		return createQuery(CAcctPreFee.class,sql, transId, userId).first();
	}

	/**
	 * 根据用户编号，产品编号，影片编号查询已处理的预扣费记录
	 * @param userId
	 * @param prodId
	 * @param progId
	 * @return
	 * @throws JDBCException 
	 */
	public CAcctPreFee queryByProgId(String userId, String prodId, String progId) throws JDBCException {
		String sql = "select * from c_acct_pre_fee c where c.user_id=? and c.prod_id=? and "
			+ " c.prog_id = ? and c.process_flag=2 and c.is_valid='T' and c.original_sn is not null order by c.fee_time desc";
		return createQuery(CAcctPreFee.class, sql, userId,prodId,progId).first();
	}
	
	
	
	/**
	 * 根据卡号，查询VOD点播消费记录
	 * @param cardId
	 * @return
	 * @throws Exception
	 */
	public List<CAcctPreFee> queryVodPreFees(String cardId) throws Exception{
		String sql = StringHelper.append("select * from (select c.* from c_acct_pre_fee c, c_user u",
				"  where u.card_id = ?  and c.user_id = u.user_id",
				" and c.is_valid='T' and c.process_flag=2  and c.status='T' AND c.ticket_sn is not null order by c.done_code desc) a",
				" where rownum <=50");
		return createQuery(CAcctPreFee.class, sql, cardId).list();
	}

}
