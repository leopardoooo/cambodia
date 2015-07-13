/**
 * SRoleDao.java	2010/03/07
 */

package com.ycsoft.business.dao.system;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TRuleDefine;
import com.ycsoft.beans.system.SItemvalue;
import com.ycsoft.beans.system.SRole;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.constants.SequenceConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;


/**
 * SRoleDao -> S_ROLE table's operator
 */
@Component
public class SRoleDao extends BaseEntityDao<SRole> {

	/**
	 *
	 */
	private static final long serialVersionUID = 1150564563246876876L;

	/**
	 * default empty constructor
	 */
	public SRoleDao() {}
	//查询所有角色信息
	public Pager<SRole> queryAll(Integer start , Integer limit ,List<SItemvalue> list ,String[] ruleArry,String keyword,String countyId,String countyDataRight,String roleDataRight)throws Exception{
		String itms = "";
		boolean key = false;
		String sql = " select t.* from (  select  t1.*,'' rule_name  from s_role t1 where t1.rule_id is null " +
				" union select t1.*,t2.rule_name from s_role t1 ,t_rule_define t2 where  t1.rule_id=t2.rule_id  ) t where 1=1 ";				
			if(!countyId.equals(SystemConstants.COUNTY_ALL)){
				sql = sql + "  and t.role_id in (select s.role_id from s_role_county s where "+countyDataRight+") ";
				String addData ="";
				if(roleDataRight.trim().equals("1=1")){
					addData = " 1=1 ";
				}else{
					addData =" t."+roleDataRight.trim();
				}
				sql = StringHelper.append(sql, " and ", addData);
			}
			if(StringHelper.isNotEmpty(keyword)){
				itms += " and ( t.role_name like '%"+keyword+"%' or t.role_desc like '%"+keyword+"%' ";
				key = true;
				if(ruleArry!=null){
					itms += " or t.rule_id in ("+getSqlGenerator().in(ruleArry)+") "; 
				}
			    if(list.size()>0){
			    	for(SItemvalue dto :list){
			    		if(dto.getItem_key().equals(DictKey.DATA_TYPE.toString())){
			    			itms += " or t.data_right_type = '"+dto.getItem_value()+"' ";
			    		}
			    		if(dto.getItem_key().equals(DictKey.SYS_LEVEL.toString())){
			    			itms += " or t.data_right_level = '"+dto.getItem_value()+"' ";
			    		}
			    		if(dto.getItem_key().equals(DictKey.SUB_SYSTEM.toString())){
			    			itms += " or t.sub_system_id = '"+dto.getItem_value()+"' ";
			    		}
			    		if(dto.getItem_key().equals(DictKey.ROLE_TYPE.toString())){
			    			itms += " or t.role_type= '"+dto.getItem_value()+"' ";
			    		}
			    	}
			    	
			    }
				if (key) {
					itms += " ) ";
				} else {
					itms = "";
				}
			}
		sql = sql + itms;
		
	return createQuery(SRole.class , sql).setLimit(limit)
				.setStart(start).page();
	}
	
	public List<TRuleDefine> getRule(String itemName) throws JDBCException{
		String sql = "select * from VEW_RULE  where rule_type = ? and rule_id in (select  distinct t.rule_id from s_role t where t.rule_id is not null) and rule_name like '%"+itemName+"%'";
		return createQuery(TRuleDefine.class,sql,SystemConstants.RULE_TYPE_DATA).list();
	}
	
	//查询所有角色信息
	public Pager<SRole> queryByCountyId(Integer start , Integer limit ,String keyword,String pid,String county_id)throws Exception{
		String cond="";
		String sql =" select t1.*,t2.rule_name from s_role t1,T_RULE_DEFINE t2  where county_id='"+county_id+"' and t1. ";
		if(!"".equals(keyword)&& keyword != null){
			cond= "and t1.role_name like '%"+keyword+"%'";
		}
		 if(!"".equals(pid)&& pid != null){
			   cond =" and (t1.role_id='"+pid+"' or t1.county_id='"+pid+"' or t1.area_id ='"+pid+"')";
		   }
		sql=sql+cond;
	return createQuery(SRole.class , sql).setLimit(limit)
				.setStart(start).page();
	}

	//判断角色名称是否存在
	public boolean isRoleToken(String role_name, String county_id) throws Exception{
		String sql = "select 1 from  s_role where role_name =? and county_id=? ";
		return findUnique( sql , role_name,county_id ) == null ? false : true ;
	}

	//获取角色seq
	public String getRoleID() throws Exception{
		return findSequence(SequenceConstants.SEQ_S_ROLE).toString();
	}
	/**
	 * 查找操作员对应的所有所有角色
	 * @param optrId
	 * @param dataRightType 
	 * @return
	 * @throws Exception
	 */
	public List<SRole> queryByOptrId(String optrId, String dataRightType,String countyId) throws JDBCException{
//		String sql ="select * from s_role a,s_optr_role b,t_rule_define c where a.rule_id=c.rule_id(+) and" +
//				" a.role_id=b.role_id and b.optr_id=? and data_right_type=?";
		String sql = "select a.*,'' rule_str from s_role a, s_optr_role b where a.role_id = b.role_id"
			+" and a.rule_id is null and b.optr_id=? and data_right_type=?"
			+" union all"
			+" select a.*,c.rule_str from s_role a, s_optr_role b, t_rule_define c,t_rule_define_county rc"
			+" where a.role_id = b.role_id and a.rule_id=c.rule_id and b.optr_id=? and data_right_type=?"
			+" and c.rule_id=rc.rule_id and c.eff_date < sysdate"
			+" and (c.exp_date is null or c.exp_date > sysdate) and rc.county_id in (?,?)";
		return this.createQuery(sql, optrId, dataRightType, optrId,
				dataRightType, SystemConstants.COUNTY_ALL,countyId).list();
	}
	
	/**
	 * 查找操作员对应的所有所有角色,报表使用
	 * @param optrId
	 * @param dataRightType 
	 * @return
	 * @throws Exception
	 */
	public List<SRole> queryByOptrId(String optrId, String dataRightType) throws JDBCException{
		String sql ="select * from s_role a,s_optr_role b,t_rule_define c where a.rule_id=c.rule_id(+) and" +
				" a.role_id=b.role_id and b.optr_id=? and data_right_type=?";
		return this.createQuery(sql, optrId,dataRightType).list();
	}
	
	/**
	 * 查找可以分配的角色
	 * @param subSystemId
	 * @param dataType
	 * @param dataRight
	 * @return
	 * @throws JDBCException
	 */
	public List<SRole> queryRoleToUse(String subSystemId, String dataType,
			String countyId,String dataRight)  throws JDBCException{
		String sql = "select * from s_role s,s_role_county t " +
				" where  t.role_id=s.role_id and t.county_id=? and (s.sub_system_id= ? or s.data_right_type= ? )" ;
		String addData ="";
		if(dataRight.trim().equals("1=1")){
			addData = " 1=1 ";
		}else{
			addData =" s."+dataRight.trim();
		}
		sql = StringHelper.append(sql, " and ", addData);
		
		return this.createQuery(sql,countyId,subSystemId,dataType).list();
	}
	/**
	 * 根据操作员获得报表权限
	 * @param optrId
	 * @return
	 * @throws JDBCException
	 */
	public SRole queryRepByOptrId(String optrId)throws JDBCException{
		String sql ="select * from s_role a,s_optr_role b where a.role_id=b.role_id " +
		" and b.optr_id=? and a.data_right_type='REPORT'";
		return this.findEntity(sql, optrId);
	}
	/**
	 * 查询子系统角色
	 */
	public List<SRole> queryRoleBySystemId(String subSystemId ) throws JDBCException{
		String sql = "select * from s_role t where t.sub_system_id=? ";
		return this.createQuery(sql, subSystemId).list();
	}
	public List<SRole> getRoleByCfg(SRole role ) throws JDBCException{
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
		String sql = "select * from s_role  where 1=1 "+str+" ";
		return this.createQuery(sql).list();
	}
	
	/**
	 * 查找可以分配的角色
	 * @param subSystemId
	 * @param dataType
	 * @param dataRight
	 * @return
	 * @throws JDBCException
	 */
	public List<SRole> queryRoleForAssign(String subSystemId, String dataType,
			String dataRight,String roleType)  throws JDBCException{
		String sql = "select * from s_role where role_type=? and (sub_system_id=? or data_right_type=? ) and "+dataRight;
		return this.createQuery(sql, roleType,subSystemId,dataType).list();
	}
	
	public Pager<SRole> queryAll(Integer start, Integer limit, String keyword) throws Exception {
		String sql =" select * from (  select  t1.*,'' rule_name  from s_role t1 where t1.rule_id is null union select t1.*,t2.rule_name from s_role t1 ,t_rule_define t2 where  t1.rule_id=t2.rule_id  ) t  where 1=1 ";
		if(!"".equals(keyword)&& keyword != null){
			sql=sql+ "and t.role_name like '%"+keyword+"%'";
		}
		return createQuery(SRole.class , sql).setLimit(limit).setStart(start).page();
	}
	
}
