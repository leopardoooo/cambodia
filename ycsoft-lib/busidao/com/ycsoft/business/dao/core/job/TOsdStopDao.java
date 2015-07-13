package com.ycsoft.business.dao.core.job;


import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TOsdStop;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * JCaCommandDao -> J_CA_COMMAND table's operator
 */
@Component
public class TOsdStopDao extends BaseEntityDao<TOsdStop> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3334829572153437436L;

	/**
	 * default empty constructor
	 */
	public TOsdStopDao() {}

	public void cancelStopAll() throws Exception {
		executeUpdate("update T_OSD_STOP set status = 'INVALID' where EFF_END_TIME >= trunc(sysdate) ");
	}

	public TOsdStop queryLatestStop() throws Exception {
		String sql = "select * from t_osd_stop t where t.status = 'ACTIVE' and t.eff_end_time >= trunc(sysdate) order by t.eff_end_time desc";
		return createQuery(sql).first();
	}
}
