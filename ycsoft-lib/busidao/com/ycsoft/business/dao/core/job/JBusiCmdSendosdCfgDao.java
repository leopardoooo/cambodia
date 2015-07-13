/**
 * JBusiCmdSendosdCfgDao.java	2012/08/07
 */
 
package com.ycsoft.business.dao.core.job; 

import org.springframework.stereotype.Component;
import com.ycsoft.beans.core.job.JBusiCmdSendosdCfg;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.Pager;


/**
 * JBusiCmdSendosdCfgDao -> J_BUSI_CMD_SENDOSD_CFG table's operator
 */
@Component
public class JBusiCmdSendosdCfgDao extends BaseEntityDao<JBusiCmdSendosdCfg> {
	
	/**
	 * default empty constructor
	 */
	public JBusiCmdSendosdCfgDao() {}
	
	public Pager<JBusiCmdSendosdCfg> query(Integer start, Integer limit,
			String query, String countyId) throws Exception {
		String sql = "select j.*,t.server_name from j_busi_cmd_sendosd_cfg j, t_server t where j.cas_id=t.server_id ";
		if (!countyId.equals(SystemConstants.COUNTY_ALL)) {
			sql += " and exists (select 1 from t_server_county c where c.server_id = j.cas_id and c.county_id= '"
					+ countyId + "' )";
		}
		if (StringHelper.isNotEmpty(query)) {
			sql = sql + " and j.message like '%" + query + "%'";
		}
		sql += " order by j.done_code desc";
		return createQuery(JBusiCmdSendosdCfg.class, sql).setLimit(limit)
				.setStart(start).page();
	}

}
