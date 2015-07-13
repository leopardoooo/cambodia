/**
 * SDeptDao.java	2010/03/07
 */

package com.ycsoft.business.dao.system;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.system.SDeptBusicode;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * SDeptDao -> S_DEPT_BUSICODE table's operator
 */
@Component
public class SdeptBusicodeDao extends BaseEntityDao<SDeptBusicode> {

	/**
	 *
	 */
	private static final long serialVersionUID = 4771453198394274415L;

	/**
	 * default empty constructor
	 */
	public SdeptBusicodeDao() {}

	public List<SDeptBusicode> queryDeptBusiCodes(String deptId) throws Exception{
		String sql = "select  t.*,d.busi_name from s_dept_busicode t,t_busi_code d where t.busi_code = d.busi_code and t.dept_id = ? ";
		return createQuery(sql, deptId).list();
	}

	public void deleteBySdeptId(String deptId) throws Exception{
		String sql  = "delete from s_dept_busicode where dept_id = ?";
		executeUpdate(sql, deptId);
	}

}
