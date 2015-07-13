/**
 * JOdscntRecordDao.java	2012/09/25
 */
 
package com.ycsoft.business.dao.core.job; 

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.job.JOdscntRecord;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * JOdscntRecordDao -> J_ODSCNT_RECORD table's operator
 */
@Component
public class JOdscntRecordDao extends BaseEntityDao<JOdscntRecord> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6439647923475496234L;

	/**
	 * default empty constructor
	 */
	public JOdscntRecordDao() {}
	
	public List<JOdscntRecord> queryRecordByDeptId(String deptId) throws Exception {
		String sql = "select jor.optr_id,re.addr_id,jor.ods_date,jor.done_date,a.addr_name"
				+ " from ( select * from  j_odscnt_record where ods_type='ODS_USER' and dept_id=?"
				+ " ) jor,(select addr_id from busi.s_dept_addr where dept_id=?"
				+ " union"
				+ " select addr_id from busi.s_dept_addr where dept_id in ("
				+ " select dept_id from busi.s_dept where county_id=?"
				+ " )) re ,t_address a"
				+ " where jor.addr_id(+) = re.addr_id and re.addr_id=a.addr_id";
		return this.createQuery(sql, deptId, deptId, deptId).list();
	}

}
