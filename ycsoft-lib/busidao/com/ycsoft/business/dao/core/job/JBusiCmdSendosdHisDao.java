/**
 * JBusiCmdSendosdHisDao.java	2012/03/01
 */
 
package com.ycsoft.business.dao.core.job; 

import org.springframework.stereotype.Component;
import com.ycsoft.beans.core.job.JBusiCmdSendosd;
import com.ycsoft.beans.core.job.JBusiCmdSendosdHis;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.Pager;


/**
 * JBusiCmdSendosdHisDao -> J_BUSI_CMD_SENDOSD_HIS table's operator
 */
@Component
public class JBusiCmdSendosdHisDao extends BaseEntityDao<JBusiCmdSendosdHis> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7721461782580637050L;

	/**
	 * default empty constructor
	 */
	public JBusiCmdSendosdHisDao() {}
	
	public Pager<JBusiCmdSendosdHis> query(Integer start, Integer limit,
			String query, String countyId) throws Exception {
		String sql = "select distinct j.done_code, j.message,t.server_name,min(j.done_date) done_date,j.optr_id,count(1) num,j.county_id,min(j.send_time) next_time "
				+ " ,j.ca_type from j_busi_cmd_sendosd_his j, t_server t where j.cas_id=t.server_id ";
		if (!countyId.equals(SystemConstants.COUNTY_ALL)) {
			sql += " and exists (select 1 from t_server_county c where c.server_id = j.cas_id and c.county_id= '"
					+ countyId + "' )";
		}
		if (StringHelper.isNotEmpty(query)) {
			sql = sql + " and j.message like '%" + query + "%'";
		}
		sql += " group by j.done_code,j.message,t.server_name,j.optr_id,j.county_id,j.ca_type  order by j.done_code desc";
		return createQuery(JBusiCmdSendosdHis.class, sql).setLimit(limit)
				.setStart(start).page();
	}

}
