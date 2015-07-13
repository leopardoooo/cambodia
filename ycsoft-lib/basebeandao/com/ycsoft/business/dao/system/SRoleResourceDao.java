/**
 * SRoleResourceDao.java	2010/03/07
 */

package com.ycsoft.business.dao.system;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.system.SRole;
import com.ycsoft.beans.system.SRoleResource;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.sysmanager.dto.system.SRoleDto;


/**
 * SRoleResourceDao -> S_ROLE_RESOURCE table's operator
 */
@Component
public class SRoleResourceDao extends BaseEntityDao<SRoleResource> {
	/**
	 * default empty constructor
	 */
	public SRoleResourceDao() {}
	private static final long serialVersionUID = -3973815288622481833L;
	public List<SRoleResource> getRoleResource(String resource_id) throws Exception{
		String sql="";
		if(!"".equals(resource_id) && resource_id != null){
			sql = "select * from s_role_resource  where res_id = '"+resource_id+"'";
			return createQuery(SRoleResource.class, sql).list();
		}
		return null;
	}
	
	public List<SRoleResource> findRoleResource(String roleid) throws Exception{
		String sql="select r.role_name,res.res_name,rr.* from S_ROLE_RESOURCE rr,s_role r,s_resource res where r.role_id = rr.role_id and res.res_id = rr.res_id and rr.role_id = ?";
		if(StringHelper.isEmpty(roleid)){
			throw new IllegalArgumentException("role_id不能为空!");
		}
		return createQuery(sql, roleid).list();
	}
	
	public List<SRoleDto> getResBySystemId(String systemId) throws Exception{
		String sql = null;
		//如果是报表系统
		if(SystemConstants.SUB_SYSTEM_REPROT.equals(systemId)){
			sql = "select res_id,res_name || '(' || res_id || ')' res_name from s_resource  where res_type='"+SystemConstants.ROLE_TYPE_MENU+"' and  res_status= ?  and sub_system_id = ?  ";
		}else{
			sql = "select res_id ,res_name from s_resource  where res_type='"+SystemConstants.ROLE_TYPE_MENU+"' and  res_status= ?  and sub_system_id = ?  ";
		}
		return createQuery(SRoleDto.class,sql,StatusConstants.ACTIVE,systemId).list();
	}
	public List<SRoleDto> getResByoptrId(String systemId,String optrId) throws Exception{
		String sql = "select t.res_id ,t.res_name ";
		//如果是报表系统
		if(SystemConstants.SUB_SYSTEM_REPROT.equals(systemId)){
			sql = "select t.res_id,t.res_name || '(' || t.res_id || ')' res_name ";
		}
		sql += " from (select sr.res_id ,sr.res_name from s_role s ,s_role_resource srr,s_resource sr " +
		"where s.role_id in  (select s.role_id from s_optr_role s where s.optr_id = ?)   " +
		"and sr.sub_system_id = ?   and sr.res_type = ? " +
		"and srr.role_id = s.role_id and sr.res_id= srr.res_id and  sr.res_status= ? " +
		"and sr.res_id not in (select res_id from s_optr_resource where more_or_less ='0' and optr_id=? ) " +
		"union  select sr.res_id,sr.res_name  from s_resource sr,s_optr_resource sor " +
		" where sr.sub_system_id = ? and sr.res_type =? and sr.res_status= ? and sor.more_or_less ='1' " +
		" and sor.optr_id=? and sor.res_id=sr.res_id ) t ";

		return createQuery(SRoleDto.class,sql,optrId,systemId,SystemConstants.ROLE_TYPE_MENU,StatusConstants.ACTIVE,
				optrId,systemId,SystemConstants.ROLE_TYPE_MENU,StatusConstants.ACTIVE,optrId).list();
	}
	
	public List<SRoleDto> findRoleResource(String systemId, String roleId)
			throws Exception {
		String sql = null;
		if(SystemConstants.SUB_SYSTEM_REPROT.equals(systemId)){
			sql = "select s.res_id,s.res_name || '(' || s.res_id || ')' res_name from s_resource s,s_role_resource rr  "
				+ "where s.res_status = ? and rr.res_id=s.res_id and s.res_type='"+SystemConstants.ROLE_TYPE_MENU+"'  and s.sub_system_id= ?  and  rr.role_id = ? order by obj_type ";
		}else{
			sql = "select distinct s.* from s_resource s,s_role_resource rr  "
				+ "where s.res_status = ? and rr.res_id=s.res_id and s.res_type='"+SystemConstants.ROLE_TYPE_MENU+"'  and s.sub_system_id= ?  and  rr.role_id = ? order by obj_type ";
		}
		
		return createQuery(SRoleDto.class, sql, StatusConstants.ACTIVE,
				systemId, roleId).list();
	}
	public void addRoleRes (String [] resId, String roleId) throws Exception {
		String	sql = "insert into s_role_resource(res_id, role_id) values (?, '"+roleId+"')";
		 executeBatch(sql, resId);
	}

	public void deleteRoleRes (String roleId) throws Exception {
		String	sql = "delete s_role_resource where role_id = ? ";
		executeUpdate(sql, roleId);
	}
	public boolean removeRoleResource(String res_id) throws Exception{
		String sql = "delete s_role_resource t where t.res_id = ?";
		int sues = executeUpdate(sql, res_id);
		if(sues>0){
			return true;
		}
		return false;
	}

	public boolean saveRoleResource(String[] roleIds ,String res_id) throws Exception{
	   String sql = "insert into s_role_resource(role_id, res_id) values (?, '"+res_id+"')";
		executeBatch(sql, roleIds);
	    return true;
	  }
	
	public List<SRoleDto> getResBySystemId(String systemId,String resType) throws Exception{
		String sql = "select res_id ,res_name from s_resource  where res_type=? and  res_status= ?  and sub_system_id = ?  ";
		return createQuery(SRoleDto.class,sql,resType,StatusConstants.ACTIVE,systemId).list();
	}
	
	public List<SRoleDto> findRoleResource(String systemId, String roleId,String resType) throws Exception {
		String sql = StringHelper.append("select distinct s.* from s_resource s,s_role_resource rr",
				" where rr.res_id=s.res_id and s.res_status=? and s.sub_system_id=? and s.res_type=?",
				" and rr.role_id=? order by obj_type "
		);
		return createQuery(SRoleDto.class, sql, StatusConstants.ACTIVE,systemId, resType, roleId).list();
	}
}
