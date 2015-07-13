/**
 * JBusiCmdSendosdDao.java	2012/03/01
 */
 
package com.ycsoft.business.dao.core.job; 

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.job.JBusiCmdSendosd;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.Pager;


/**
 * JBusiCmdSendosdDao -> J_BUSI_CMD_SENDOSD table's operator
 */
@Component
public class JBusiCmdSendosdDao extends BaseEntityDao<JBusiCmdSendosd> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4526445661861117799L;

	/**
	 * default empty constructor
	 */
	public JBusiCmdSendosdDao() {}

	public List<JBusiCmdSendosd> queryAllOsdCmd() throws Exception{
		String sql ="select * from J_BUSI_CMD_SENDOSD t where t.send_time<sysdate  order by job_id ";
		return createQuery(sql).list();
	}
	
	public void saveAllOsdHis(int jobId)throws Exception{
		String sql = StringHelper.append(
				"insert into J_BUSI_CMD_SENDOSD_HIS ",
				" (job_id, cas_id, supplier_id, send_time, optr_id, message,county_id,area_id,done_code,done_date,ca_type) ",
				" select job_id, cas_id, supplier_id, send_time, optr_id, message,county_id,area_id,done_code,done_date,ca_type " ,
				" from J_BUSI_CMD_SENDOSD where job_id = ?");
		executeUpdate(sql, jobId);

		sql = "delete J_BUSI_CMD_SENDOSD  where job_id =  ?";
		executeUpdate(sql, jobId);
	}
	
	public Pager<JBusiCmdSendosd> query(Integer start , Integer limit  ,String query,String countyId)throws Exception{
		String sql = "select distinct j.done_code, j.message,t.server_name,min(j.done_date) done_date,j.optr_id,count(1) num,j.county_id,min(j.send_time) next_time " +
				" ,j.ca_type from j_busi_cmd_sendosd j, t_server t where j.cas_id=t.server_id ";
		if(!countyId.equals(SystemConstants.COUNTY_ALL)){
			sql += " and exists (select 1 from t_server_county c where c.server_id = j.cas_id and c.county_id= '"+countyId+"' )";
		}
		if(!"".equals(query)&& query != null){
			sql=sql+ " and j.message like '%"+query+"%'";
		}	
		sql += " group by j.done_code,j.message,t.server_name,j.optr_id,j.county_id,j.ca_type  order by j.done_code desc";
		return createQuery(JBusiCmdSendosd.class,sql ).setLimit(limit).setStart(start).page();
	}
	
	public void delete(Integer doneCode)throws Exception{
		String sql = StringHelper.append(
				"insert into J_BUSI_CMD_SENDOSD_HIS ",
				" (job_id, cas_id, supplier_id, send_time, optr_id, message,county_id,area_id,done_code,done_date,ca_type) ",
				" select job_id, cas_id, supplier_id, send_time, optr_id, message,county_id,area_id,done_code,done_date,ca_type " ,
				" from J_BUSI_CMD_SENDOSD where done_code = ?");
		executeUpdate(sql, doneCode);

		sql = "delete J_BUSI_CMD_SENDOSD  where done_code =  ? ";
		executeUpdate(sql, doneCode);
	}
	
	public String findServerNameById(String serverId) throws Exception {
		String sql = "select server_name from t_server where server_id=?";
		return this.findUnique(sql, serverId);
	}
}
