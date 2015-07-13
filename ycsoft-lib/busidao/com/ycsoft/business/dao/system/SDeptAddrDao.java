/**
 * SDeptAddrDao.java	2013/05/20
 */
 
package com.ycsoft.business.dao.system ; 

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.system.SDeptAddr;
import com.ycsoft.beans.system.SDeptBusicode;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * SDeptAddrDao -> S_DEPT_ADDR table's operator
 */
@Component
public class SDeptAddrDao extends BaseEntityDao<SDeptAddr> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5390727182980171353L;

	/**
	 * default empty constructor
	 */
	public SDeptAddrDao() {}
	
	public List<SDeptAddr> getAddrByDept(String deptId) throws JDBCException {
		String sql = "select * from s_dept_addr t where t.dept_id = ? ";
		return createQuery(SDeptAddr.class,sql, deptId).list();
	}

	public void removeByDeptId (String [] addrIds, String dept_id) throws Exception {
		String	sql = "delete from s_dept_addr where dept_id='"+dept_id+"' and " +getSqlGenerator().setWhereInArray("addr_id", addrIds);
		executeUpdate(sql);
	}

	public List<SDeptAddr> queryDeptAddressByCountyId(String county_id) throws Exception{
		Map<String, Serializable> params = new HashMap<String, Serializable>();
		params.put("county_id", county_id);
		return this.findByMap(params);
	}
}
