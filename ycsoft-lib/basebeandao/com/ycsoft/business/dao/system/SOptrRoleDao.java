/**
 * SOptrRoleDao.java	2010/04/21
 */

package com.ycsoft.business.dao.system;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.ycsoft.beans.system.SOptrRole;
import com.ycsoft.beans.system.SRole;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.sysmanager.dto.system.SRoleDto;


/**
 * SOptrRoleDao -> S_OPTR_ROLE table's operator
 */
@Component
public class SOptrRoleDao extends BaseEntityDao<SOptrRole> {

	/**
	 *
	 */
	private static final long serialVersionUID = -38525601776727018L;

	/**
	 * 根据角色编号获取操作员信息
	 */
	public List<SOptrRole> getOptrRole(String role_id,String optr_id) throws Exception{
		String cond=" and 1=2";
		String sql="select t1.*,t2.optr_name,t3.role_name from s_optr_role t1,s_optr t2,s_role t3 where t1.optr_id = t2.optr_id and t3.role_id = t1.role_id";
		if(StringUtils.isNotEmpty(role_id) && StringUtils.isEmpty(optr_id)){
			cond = " and t1.role_id = '"+role_id+"'";
		}
		if(StringUtils.isNotEmpty(optr_id) && StringUtils.isEmpty(role_id)){
			cond = " and t1.optr_id = '"+optr_id+"'";
		}
		if(StringUtils.isNotEmpty(role_id) && StringUtils.isNotEmpty(optr_id) ){
			cond = " and t1.optr_id = '"+optr_id+"' and t1.role_id = '"+role_id+"'";
		}
		sql = sql +cond;
		return createQuery(SOptrRole.class, sql).list();
	}
	/**
	 * 保存角色编号与操作员关系(单个Role_id 对应多个Optr_id)
	 */
	public boolean saveOptrRoleByOptrs (String [] optr_id, String role_id) throws Exception {

		String	sql = "insert into s_optr_role(optr_id, role_id) values (?, '"+role_id+"')";
		int[] sues = executeBatch(sql, optr_id);
		if(sues.length>0){
			return true;
		}
		return false;
	}
	/**
	 * 保存角色编号与操作员关系(单个Optr_id 对应多个Role_id)
	 */
	public boolean saveOptrRoleByRoles (String [] role_id, String optr_id) throws Exception {

		String	sql = "insert into s_optr_role(optr_id, role_id) values ('"+optr_id+"',?)";
		int[] sues = executeBatch(sql, role_id);
		if(sues.length>0){
			return true;
		}
		return false;
	}
	/**
	 * (根据Role_id) 删除该optr_id的角色与操作员关系
	 */
	public boolean deteleOptrRole(String roleId,String optrId) throws Exception {

		String sql = "delete s_optr_role t where t.role_id = ? and t.optr_id=?";
		int sues = executeUpdate(sql, roleId,optrId);
		if(sues>0){
			return true;
		}
		return false;
	}
	/**
	 * (根据Role_id) 删除所有角色与操作员关系
	 */
	public void deteleOptrAllByRole(String [] optrs,SRole role) throws Exception {
		String str = "";
		if(role.getRole_type().equals(SystemConstants.ROLE_TYPE_MENU)&&StringHelper.isNotEmpty(role.getSub_system_id())){
			str = "and role_type='"+SystemConstants.ROLE_TYPE_MENU+"' and sub_system_id is not null ";
		}
		if(role.getRole_type().equals(SystemConstants.ROLE_TYPE_DATA)&&StringHelper.isNotEmpty(role.getData_right_type())){
			if(StringHelper.isNotEmpty(role.getRule_id())){
				str = "and role_type='"+SystemConstants.ROLE_TYPE_DATA+"' and data_right_type = '"+role.getData_right_type()+"' and rule_id is not null ";
			}
			if(StringHelper.isNotEmpty(role.getData_right_level())){
				str = "and role_type='"+SystemConstants.ROLE_TYPE_DATA+"' and data_right_type = '"+role.getData_right_type()+"' and data_right_level is not null ";
			}
		}
		String sql = "delete s_optr_role t where t.optr_id = ? "+str+"";
		executeBatch(sql, optrs);
	}
	
	
	/**
	 * (根据Role_id) 删除对应地市的角色与操作员关系
	 */
	public void deteleOptrAllByRoleCounty(String [] optrs,SRole role) throws Exception {
		String str = "";
		
		//如果是菜单类型权限
		if(role.getRole_type().equals(SystemConstants.ROLE_TYPE_MENU)&&StringHelper.isNotEmpty(role.getSub_system_id())){
			str = " and t.role_id in(select role_id from s_role where role_type='"+SystemConstants.ROLE_TYPE_MENU+"' and sub_system_id ='"+role.getSub_system_id()+"' )";
		}else if(role.getRole_type().equals(SystemConstants.ROLE_TYPE_DATA)&&StringHelper.isNotEmpty(role.getData_right_type())){
			if(StringHelper.isNotEmpty(role.getRule_id())){
				str = " and t.role_id in(select role_id from s_role where role_type='"+SystemConstants.ROLE_TYPE_DATA+"' and data_right_type = '"+role.getData_right_type()+"' and rule_id is not null )";
			}
			if(StringHelper.isNotEmpty(role.getData_right_level())){
				str = " and t.role_id in(select role_id from s_role where role_type='"+SystemConstants.ROLE_TYPE_DATA+"' and data_right_type = '"+role.getData_right_type()+"' and data_right_level is not null) ";
			}
		}
		String sql = StringHelper.append("delete s_optr_role t where t.optr_id = ? ",str);
		
		executeBatch(sql, optrs);
	}
	/**
	 * (根据Optr_id) 删除角色与操作员关系
	 */
	public boolean deteleOptrRoleByOptr(String optr_id) throws Exception {

		String sql = "delete s_optr_role t where t.optr_id = ?";
		int sues = executeUpdate(sql, optr_id);
		if(sues>0){
			return true;
		}
		return false;
	}

	/**
	 * (根据role_id) 删除角色与操作员关系
	 */
	public boolean deteleOptrRoleByRole(String role_id,String countyDataRight) throws Exception {
		
		String sql = StringHelper.append("delete s_optr_role t where t.role_id = ? and t.optr_id in (select optr_id from s_optr where ",
				countyDataRight," )");
		
		int sues = executeUpdate(sql, role_id);
		
		if(sues>0){
			return true;
		}
		return false;
	}
	
	public List<SRoleDto> queryOptrRole(String optrId) throws Exception {
		String sql = " select * from s_optr_role t1,s_role t2 where  t1.role_id=t2.role_id and t1.optr_id = ? ";
		return createQuery(SRoleDto.class, sql, optrId).list();
	}
	
	/**
	 * 复制操作员权限
	 * @param sourceOptrId
	 * @param newOptrId
	 * @throws Exception
	 */
	public List<SOptrRole> copyOptrRole(String sourceOptrId,String newOptrId) throws Exception {
		String newOptrSql = "delete s_optr_role t where t.optr_id = ? ";
		executeUpdate(newOptrSql, newOptrId);
		
		String sql = "insert into s_optr_role(optr_id, role_id) " +
				" select ?,role_id from s_optr_role where optr_id=?";
		this.executeUpdate(sql, newOptrId, sourceOptrId);
		
		sql = "select o.*,r.role_name from s_optr_role o,s_role r where o.role_id=r.role_id and o.optr_id=?";
		return this.createQuery(sql, newOptrId).list();
	}
	
	/**
	 * default empty constructor
	 */
	public SOptrRoleDao() {}

}
