/**
 * CAcctAcctitemThresholdDao.java	2010/07/12
 */

package com.ycsoft.business.dao.core.acct;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.acct.CAcctAcctitemThreshold;
import com.ycsoft.business.dto.core.acct.AcctAcctitemThresholdDto;
import com.ycsoft.business.dto.core.acct.QueryAcctitemThresholdDto;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * CAcctAcctitemThresholdDao -> C_ACCT_ACCTITEM_THRESHOLD table's operator
 */
@Component
public class CAcctAcctitemThresholdDao extends BaseEntityDao<CAcctAcctitemThreshold> {

	/**
	 *
	 */
	private static final long serialVersionUID = 5818623061995818994L;



	/**
	 * default empty constructor
	 */
	public CAcctAcctitemThresholdDao() {}

	/**
	 * 修改账目的停开机的临时阈值
	 * @param acct_id
	 * @param acctitem_id
	 * @param tempThreshold
	 */
	public void updateTempThreshold(String acctId, String acctitemId,int tempThreshold)  throws JDBCException{
		String sql = "update c_acct_acctitem_threshold set THRESHOLD= ?,TEMP_THRESHOLD=? - BASE_THRESHOLD  where " +
				" ACCT_ID =? and ACCTITEM_ID=? and task_code=?";
		executeUpdate(sql, tempThreshold,tempThreshold,acctId,acctitemId,SystemConstants.TASK_STOP);
	}

	public void clearTempThreshold(String custId,String countyId)  throws JDBCException{
		String sql = "update c_acct_acctitem_threshold set THRESHOLD= BASE_THRESHOLD,TEMP_THRESHOLD=0 " +
				" where acct_id in (select acct_id from c_acct where cust_id=?) and  TEMP_THRESHOLD<>0" +
				" and county_id=? and task_code=?";
		executeUpdate(sql,custId,countyId,SystemConstants.TASK_STOP);
	}

	/**
	 * 删除
	 * @param acctId
	 * @throws Exception
	 */

	public void removeByAcctId(String acctId) throws Exception{
		String sql = "delete c_acct_acctitem_threshold where acct_id=?";
		executeUpdate(sql, acctId);
	}

	/**
	 * 查询账目下阈值明细
	 * @param acctitemId 账目id
	 * @return
	 * @throws JDBCException
	 */
	public CAcctAcctitemThreshold queryByAcctitemIdAndCode(String acctId,String acctitemId,String taskCode) throws JDBCException {
		String sql = "select * from c_acct_acctitem_threshold t where t.acct_id=? and  t.acctitem_id=? and t.task_code=?";
		return this.createQuery(sql, acctId,acctitemId,taskCode).first();
	}


	/**
	 * 查询账目下阈值明细
	 * @param acctitemId 账目id
	 * @return
	 * @throws JDBCException
	 */
	public List<CAcctAcctitemThreshold> queryByAcctitemId(String acctId,String acctitemId) throws JDBCException {
		String sql = "select * from c_acct_acctitem_threshold t where t.acct_id=? and  t.acctitem_id=?";
		return createQuery(sql,acctId, acctitemId).list();
	}

	public void removeByAcctItemId(String acctId, String acctItemId)throws JDBCException {
		String sql = "delete c_acct_acctitem_threshold where acct_id=? and acctitem_id=?";
		executeUpdate(sql, acctId,acctItemId);
		
	}
	
	public void updateThreshold(CAcctAcctitemThreshold threshold)
			throws JDBCException {
		String sql = "update c_acct_acctitem_threshold set temp_threshold=?,threshold=base_threshold+?" +
				" where acct_id=? and acctitem_id=? and task_code=?";
		executeUpdate(sql, threshold.getTemp_threshold(),threshold.getTemp_threshold(), threshold
				.getAcct_id(), threshold.getAcctitem_id(), threshold
				.getTask_code());
	}

	public List<AcctAcctitemThresholdDto> queryThresholdByAcctId(
			String[] acctIds, String countyId) throws JDBCException {
		String sql = "select t.*,a.user_id,p.prod_name acctitem_name" +
				" from c_acct a,c_acct_acctitem_threshold t,p_prod p" +
				" where a.acct_id=t.acct_id and t.acctitem_id=p.prod_id" +
				" and a.acct_id in ("+sqlGenerator.in(acctIds)+") and a.county_id=?" +
				" order by t.task_code";
		return createQuery(AcctAcctitemThresholdDto.class, sql, countyId).list();
	}
	
	public List<AcctAcctitemThresholdDto> queryThresholdByCustId(
			QueryAcctitemThresholdDto dto, String custId, String[] acctIds,
			String countyId) throws JDBCException {
		String sql = "select t.*,p.prod_name acctitem_name,u.*"
				+ " from c_acct a,c_acct_acctitem_threshold t,p_prod p,c_user u"
				+ " where a.acct_id=t.acct_id and t.acctitem_id=p.prod_id and a.user_id=u.user_id"
				+ " and a.acct_id in (" + sqlGenerator.in(acctIds) + ")"
				+ " and a.cust_id=? and a.county_id=?";
		
		if(dto != null){
			String acctItemId = dto.getAcctitem_id();
			String taskCode = dto.getTask_code();
			String userStopType = dto.getUser_stop_type();
			String userStatus = dto.getUser_status();
			String userClass = dto.getUser_class();
			
			if(StringHelper.isNotEmpty(acctItemId)){
				sql += " and t.acctitem_id='"+acctItemId+"'";
			}
			
			if(StringHelper.isNotEmpty(taskCode)){
				sql += " and t.task_code='"+taskCode+"'";
			}
			
			if(StringHelper.isNotEmpty(userStopType)){
				sql += " and u.stop_type='"+userStopType+"'";
			}
			
			if(StringHelper.isNotEmpty(userStatus)){
				sql += " and u.status='"+userStatus+"'";
			}
			
			if(StringHelper.isNotEmpty(userClass)){
				sql += " and u.user_class='"+userClass+"'";
			}
		}
		sql += " order by t.task_code";
		return createQuery(AcctAcctitemThresholdDto.class, sql, custId,
				countyId).list();
	}

}
