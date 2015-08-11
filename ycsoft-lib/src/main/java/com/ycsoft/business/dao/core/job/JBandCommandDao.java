/**
 * JBandCommandDao.java	2011/05/09
 */
 
package com.ycsoft.business.dao.core.job; 

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.job.JBandCommand;
import com.ycsoft.beans.core.job.JVodCommand;
import com.ycsoft.business.dto.core.prod.JBandCommandDto;
import com.ycsoft.commons.constants.BusiCmdConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;


/**
 * JBandCommandDao -> J_BAND_COMMAND table's operator
 */
@Component
public class JBandCommandDao extends BaseEntityDao<JBandCommand> {

	private static final long serialVersionUID = 2340619908536493124L;
	/**
	 * default empty constructor
	 */
	public JBandCommandDao() {}

	public int queryCreateUserCmd(String userId) throws Exception{
		String sql="select count(1) from j_band_command where user_id=? and cmd_type=? and is_success = 'Y'";
		return Integer.parseInt(this.findUnique(sql, userId,BusiCmdConstants.CREAT_USER));
	}
	public Pager<JBandCommand> queryByCustId(String custId,Integer start,Integer limit) throws JDBCException {
		String sql = "select v.transnum,v.cmd_type,v.send_time,v.done_code,v.error_info,u.modem_mac," +
				" CASE when v.error_info = 'null' AND v.is_success ='Y' then 'Y' " +
				" when v.error_info is null and v.is_success = 'Y' and v.return_code = '1' then 'Y' " +
				" when v.error_info  is null AND v.is_success is null then 'F' else 'N' end is_success " +
				" from j_band_command v,(" +
					"select user_id,modem_mac from c_user where cust_id=?"+
					" union"+
					" select user_id,modem_mac from c_user_his where cust_id=?" +
				" ) u where  v.user_id=u.user_id and v.cust_id= ? order by v.transnum desc";
		return createQuery(sql, custId, custId, custId).setStart(start).setLimit(limit).page();
	}
	public Pager<JBandCommandDto> queryBandCommandByParam(Map<String,String> param,Integer start,Integer limit) throws JDBCException {
		StringBuffer sql  = new StringBuffer();	 
		 sql.append("select cub.login_name,tss.supplier_name,ts.server_name , v.cmd_type,v.create_time ,v.send_time,v.done_code,v.error_info,u.modem_mac, v.is_send ");
		 sql.append(" from j_band_command v,c_user u,t_server ts , t_server_supplier  tss ,busi.c_user_broadband cub");
		 sql.append(" where  v.user_id=u.user_id and v.supplyier_id= tss.supplier_id and v.server_id = ts.server_id  and u.user_id = cub.user_id ");
		Iterator<String> it= param.keySet().iterator();
		while(it.hasNext()){
			String key = String.valueOf(it.next());
			if(key.equals("USERID")){
				sql.append( " and u.user_id='"+param.get(key).toString()+"'");
			}else if(key.equals("BROADNAME")){
				sql.append( " and cub.login_name='"+param.get(key).toString()+"'");
			}else if(key.equals("MODEMMAC")){
				sql.append( " and u.modem_mac='"+param.get(key).toString()+"'");
			}
		}
		sql.append(" order by create_time desc ");
		return createQuery(JBandCommandDto.class,sql.toString()).setStart(start).setLimit(limit).page();
	}
	public void deleteUserBand(String userId, String cmdType) throws JDBCException {
		String sql = "delete from j_band_command j where j.user_id=? and j.cmd_type=?";
		executeUpdate(sql, userId,cmdType);
	}

	public List<JBandCommand> queryCmd() throws JDBCException{
		String sql = "select * from j_band_command where is_send='F' and rownum<500 order by transnum";
		return this.createQuery(sql).list();
	}
}
