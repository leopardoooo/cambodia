/**
 * SCountyDao.java	2010/03/07
 */

package com.ycsoft.business.dao.system;


import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.system.SOptrResource;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.sysmanager.dto.tree.TreeDto;


/**
 * SCountyDao -> S_COUNTY table's operator
 */
@Component
public class SOptrResourceDao extends BaseEntityDao<SOptrResource> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * default empty constructor
	 */
	public SOptrResourceDao() {}

	public List<TreeDto> getResourceByOptr(String optrId) throws Exception{
		String sql = " select  id ,pid,text,attr from ( select 'S-'||sub_system_id id,'-1' pid,sub_system_name text,'false' attr from  s_sub_system " +
				"union all select distinct s.res_id id,'S-'||s.sub_system_id pid,s.res_name text ,'true' attr from s_resource s, s_optr_role o, s_role_resource rr " +
				"where s.res_status =?  and o.role_id = rr.role_id   and rr.res_id = s.res_id   and res_type = ?   " +
				"and o.optr_id = ?   and s.res_id not in (select res_id from s_optr_resource where optr_id=?) " +
				"union all select distinct s.res_id id,'S-'||s.sub_system_id pid,s.res_name text,case when sor.more_or_less = '0'  then  'false' ELSE 'true' end attr   from s_resource s,s_optr_resource sor  " +
				"where s.res_id=sor.res_id  and sor.optr_id = ?  ) t  start with t.pid = '-1'  connect by prior t.id = t.pid ";
		  return createQuery(TreeDto.class,sql,StatusConstants.ACTIVE,SystemConstants.ROLE_TYPE_MENU,optrId,optrId,optrId).list();
	}
	
	public List<TreeDto> getResourceByCounty(String optrId) throws Exception{
		String sql = " select  id ,pid,text,attr from ( select 'S-'||sub_system_id id,'-1' pid,sub_system_name text,'false' attr from  s_sub_system " +
				"union all select distinct s.res_id id,'S-'||s.sub_system_id pid,s.res_name text ,'true' attr from s_resource s, s_optr_role o, s_role_resource rr " +
				"where s.res_status =?  and o.role_id = rr.role_id   and rr.res_id = s.res_id   and res_type = ?   " +
				"and o.optr_id = ?   and s.res_id not in (select res_id from s_optr_resource where optr_id=?) " +
				"union all select distinct s.res_id id,'S-'||s.sub_system_id pid,s.res_name text,'true'  attr   from s_resource s,s_optr_resource sor  " +
				"where s.res_id=sor.res_id  and sor.optr_id = ? and sor.more_or_less ='1' ) t  start with t.pid = '-1'  connect by prior t.id = t.pid ";
		  return createQuery(TreeDto.class,sql,StatusConstants.ACTIVE,SystemConstants.ROLE_TYPE_MENU,optrId,optrId,optrId).list();
	}
	
	public List<SOptrResource> getResourceByRole(String optrId) throws Exception{
		String sql = " select  s.res_id  from s_resource s, s_optr_role o, s_role_resource rr where s.res_status =?   and o.role_id = rr.role_id  " +
				" and rr.res_id = s.res_id   and res_type = ?   and o.optr_id = ?   ";
		return createQuery(SOptrResource.class,sql,StatusConstants.ACTIVE,SystemConstants.ROLE_TYPE_MENU,optrId).list();
	}
	public void delete (String optrId) throws Exception {
		String	sql = "delete s_optr_resource where optr_id = ? ";
		executeUpdate(sql, optrId);
	}

	public List<SOptrResource> queryByOptr(String optrId) throws Exception{
		String sql = " select optr.optr_name,res.res_name,t.* from busi.s_optr_resource t,s_optr optr,s_resource res  where optr.optr_id = t.optr_id and res.res_id = t.res_id and t.optr_id = ?";
		return createQuery(sql, optrId).list();
	}
	
	/**
	 * 复制操作员资源
	 * @param sourceOptrId
	 * @param newOptrId
	 * @throws Exception
	 */
	public List<SOptrResource> copyOptrResource(String sourceOptrId,String newOptrId) throws Exception {
		//more_or_less 禁用的资源也一起复制：复制的权限中有可能包含禁用的资源
		String sql = "insert into s_optr_resource(optr_id, res_id, more_or_less) " +
				" select ?,res_id,more_or_less from s_optr_resource where optr_id=?";
		this.executeUpdate(sql, newOptrId, sourceOptrId);
		
		sql = "select o.*,r.res_name from s_optr_resource o,s_resource r where o.res_id=r.res_id and o.optr_id=?";
		return this.createQuery(sql, newOptrId).list();
	}
}
