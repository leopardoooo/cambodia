/**
 * TNonresCustApprovalDao.java	2012/10/11
 */
 
package com.ycsoft.business.dao.config; 

import java.util.List;
import org.springframework.stereotype.Component;
import com.ycsoft.beans.config.TNonresCustApproval;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.Pager;


/**
 * TNonresCustApprovalDao -> T_NONRES_CUST_APPROVAL table's operator
 */
@Component
public class TNonresCustApprovalDao extends BaseEntityDao<TNonresCustApproval> {
	

	/**
	 * default empty constructor
	 */
	public TNonresCustApprovalDao() {}
	
	public Pager<TNonresCustApproval> queryNonresCustApp(String status,String query,Integer start,Integer limit) throws Exception {
		String sql = "select * from T_NONRES_CUST_APPROVAL where 1=1";
		if(StringHelper.isNotEmpty(query)){
			sql += " and (app_id='"+query+"' or app_name like '%"+query+"%')";
		}
		if(StringHelper.isNotEmpty(status)){
			sql += " and status='"+status+"'";
		}
		return this.createQuery(sql).setStart(start).setLimit(limit).page();
	}
	
	public void updateStatus(String appCode,String status) throws Exception {
		String sql = "update T_NONRES_CUST_APPROVAL set status=? where app_code=?";
		this.executeUpdate(sql, status, appCode);
	}
	
	public void updateNonresCustApp(TNonresCustApproval nca) throws Exception {
		String sql = "update T_NONRES_CUST_APPROVAL set app_code=?,app_name=?,status=?,remark=? where app_id=?";
		this.executeUpdate(sql, nca.getApp_code(), nca.getApp_name(), nca
				.getStatus(), nca.getRemark(), nca.getApp_id());
	}
	
	public TNonresCustApproval queryByAppCode(String appCode) throws Exception {
		String sql = "select * from T_NONRES_CUST_APPROVAL where app_code=?";
		return this.createQuery(sql, appCode).first();
	}
	
	public void deleteByAppId(String appId) throws Exception {
		String sql = "delete from T_NONRES_CUST_APPROVAL where app_id=?";
		this.executeUpdate(sql, appId);
	}
	
}
