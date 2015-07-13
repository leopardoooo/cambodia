/**
 * CProdHisDao.java	2010/07/19
 */

package com.ycsoft.business.dao.core.prod;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.prod.CProd;
import com.ycsoft.beans.core.prod.CProdHis;
import com.ycsoft.business.dto.core.prod.UserProdDto;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * CProdHisDao -> C_PROD_HIS table's operator
 */
@Component
public class CProdHisDao extends BaseEntityDao<CProdHis> {

	/**
	 *
	 */
	private static final long serialVersionUID = 6977914442191921047L;

	/**
	 * default empty constructor
	 */
	public CProdHisDao() {}
	
	public List<UserProdDto> queryUserProdHisToCallCenter(Map<String,Object> p,String countyId) throws Exception {
		String cond = "", brandCond = "";
		Iterator<String> it = p.keySet().iterator();
		boolean flag = false;
		while(it.hasNext()){
			String key = it.next();
			String value = p.get(key).toString();
			
			if(key.equals("USER_ID")){
				cond += " and u.user_id='"+value+"'";
			}
			if(key.equals("CARD_ID")){
				cond += " and u.card_id='"+value+"'";
			}
			if(key.equals("STB_ID")){
				cond += " and u.stb_id='"+value+"'";
			}
			if(key.equals("MODEM_MAC")){
				cond += " and u.modem_mac='"+value+"'";
			}
			if(key.equals("BROAD_NAME")){
				flag = true;
				brandCond = " and b.login_name='" + value + "'";
			}
		}
		String sql = "";
		if(flag){
			sql = "select p.*,pp.prod_name,u.user_type,u.stb_id,u.card_id,u.modem_mac," +
			"u.status user_status,'' terminal_type,'' serv_type, b.login_name,c.optr_id,c.done_date,c.remark" +
			" from c_user u,c_user_broadband b,c_prod_his p,p_prod pp,c_done_code c" +
			" where u.user_id=b.user_id and u.user_id=p.user_id and p.prod_id=pp.prod_id and p.done_code=c.done_code" +
			" and u.county_id=? and b.county_id=? and p.county_id=? and c.county_id=?" + cond + brandCond;
		}else{
			sql = "select p.*,pp.prod_name,u.user_type,u.stb_id,u.card_id,u.modem_mac," +
				"u.status user_status,a.terminal_type,a.serv_type,'' login_name,c.optr_id,c.done_date,c.remark" +
				" from c_user u,c_user_atv a,c_prod_his p,p_prod pp,c_done_code c" +
				" where u.user_id=a.user_id and u.user_id=p.user_id and p.prod_id=pp.prod_id and p.done_code=c.done_code" +
				" and u.county_id=? and p.county_id='"+countyId+"' and c.county_id=?" + cond +
				" union all " +
				"select p.*,pp.prod_name,u.user_type,u.stb_id,u.card_id,u.modem_mac," +
				"u.status user_status,d.terminal_type,d.serv_type,'' login_name,c.optr_id,c.done_date,c.remark" +
				" from c_user u,c_user_dtv d,c_prod_his p,p_prod pp,c_done_code c" +
				" where u.user_id=d.user_id and u.user_id=p.user_id and p.prod_id=pp.prod_id and p.done_code=c.done_code" +
				" and u.county_id=? and p.county_id='"+countyId+"' and c.county_id=?" + cond ;
		}
		return this.createQuery(UserProdDto.class, sql, countyId, countyId, countyId, countyId).list();
	}
	
	public List<CProdHis> queryByAcctItem(String acctId, String acctItemId,
			String countyId) throws JDBCException{
		String sql ="select * from c_prod_his where acct_id=? and prod_id=? and county_id=? order by prod_sn desc";

		return createQuery(sql,acctId,acctItemId,countyId).list();
	}

}
