/**
 * SDeptDao.java	2010/03/07
 */

package com.ycsoft.business.dao.system;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.depot.RDepotDefine;
import com.ycsoft.beans.system.SDept;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.sysmanager.dto.system.SDeptDto;


/**
 * SDeptDao -> S_DEPT table's operator
 */
@Component
public class SDeptDao extends BaseEntityDao<SDept> {

	/**
	 *
	 */
	private static final long serialVersionUID = 4771453198394274415L;

	/**
	 * default empty constructor
	 */
	public SDeptDao() {}

	/**
	 * 查询地区的所有部门
	 * @param countyId
	 * @return
	 */
	public List<SDept> queryByCountyId(String countyId) throws JDBCException {
		String sql = "select * from s_dept where county_id=?  and status = ?";
		return createQuery(sql, countyId,StatusConstants.ACTIVE).list();
	}

	/**
	 * 地区的所有仓库
	 * @return
	 * @throws Exception
	 */
	public List<RDepotDefine> queryDepot(String countyId) throws Exception {
		String sql = "select dd.* from r_depot_define dd " +
				"where dd.county_id=?";
		return createQuery(RDepotDefine.class, sql,countyId).list();
	}
	
	/**
	 * 根据操作员ID查询部门
	 * @param countyId
	 * @return
	 * @throws JDBCException
	 */
	public List<SDeptDto> queryDeptByCountyId(String countyId) throws JDBCException{
		String sql = StringHelper.append("select level, s.*",
				" from ( select dept_id,dept_name,dept_type,county_id,area_id,d.create_time,creator,dept_order_num,status, dept_pid,d.agent_id, a.name agent_name",
				" from s_dept d, s_agent a where d.agent_id=a.id(+) ) s",
				" where s.status = ? start with dept_id = ? ",
				" connect by prior s.dept_id = s.dept_pid  order by level,s.dept_type desc");
		return createQuery(SDeptDto.class, sql, StatusConstants.ACTIVE,countyId).list();
	}
	
	public List<SDept> queryFgsByDeptId(String deptId) throws Exception {
		String sql = StringHelper.append("select * from s_dept t where t.dept_type = 'FGS'",
				" start with dept_id = ? connect by prior dept_id = dept_pid",
				" order by level"
		);
		return this.createQuery(sql, deptId).list();
	}
	
	/**
	 * 根据部门ID查询操作员
	 * @param deptId
	 * @return
	 * @throws Exception
	 */
	public List<SOptr> queryOptrByDeptId(String deptId) throws Exception{
		String sql = "select * from s_optr s where s.dept_id=? and s.status=?";
		return createQuery(SOptr.class, sql, deptId,StatusConstants.ACTIVE).list();
	}
	
	/**
	 * 查找部门
	 * @return
	 * @throws JDBCException 
	 */
	public List<SDeptDto> queryYYT(String countyId) throws JDBCException {
		String sql = "select level , s.*  from s_dept s "
			+ " where s.status = ? start with s.dept_id=?  connect by prior s.dept_id = s.dept_pid  order by level,s.dept_type desc";
		return createQuery(SDeptDto.class,sql, StatusConstants.ACTIVE, countyId).list();
	}

	/**
	 * 查找部门
	 * @return
	 * @throws JDBCException 
	 */
	public List<SDeptDto> queryAllYYT(String[] countyIds) throws JDBCException {
		String sql = "select level , s.*  from s_dept s "
			+ " where s.status = ? start with s.dept_id in ( " +
					" select dept_id from  (select  a.* ,RANK ()over(partition by  '' order by a.lv ) lvl "
			+" from (select level lv,  sc.* from s_dept sc "
			+" start with sc.dept_id='"+SystemConstants.COUNTY_ALL+"' "
			+" connect by prior sc.dept_id = sc.dept_pid) a "
			+" where "+getSqlGenerator().setWhereInArray("a.dept_id",countyIds)+
			" ) b where b.lvl=1 "
		+") connect by prior s.dept_id = s.dept_pid  order by level,s.dept_type desc";
		return createQuery(SDeptDto.class,sql, StatusConstants.ACTIVE).list();
	}
	
	public List<SDept> queryAllDept() throws Exception{
		String sql = "  select * from vew_depot ";
		return createQuery(SDept.class,sql).list();
	}
	
	public List<SDept> queryChildDept(String deptId) throws Exception{
		String sql = "  select dept_id,( case when level=2 then '|-' when level=3 then '|----' when level=4 then '|-----'end) ||dept_name dept_name "
				+ "from (select dept_id,dept_name,dept_pid from busi.s_dept t where t.status='ACTIVE') "
				+ "start with dept_id=? connect by prior dept_id = dept_pid  ";
		return createQuery(SDept.class,sql,deptId).list();
	}

}
