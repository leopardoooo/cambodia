/**
 * SCountyDao.java	2010/03/07
 */

package com.ycsoft.business.dao.system;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.system.SCounty;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.store.SysConfig;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.sysmanager.dto.system.SCountyDto;
import com.ycsoft.sysmanager.dto.system.SRoleDto;
import com.ycsoft.sysmanager.dto.tree.TreeDto;


/**
 * SCountyDao -> S_COUNTY table's operator
 */
@Component
public class SCountyDao extends BaseEntityDao<SCounty> {

	/**
	 *
	 */
	private static final long serialVersionUID = -3427504179706553556L;

	/**
	 * default empty constructor
	 */
	public SCountyDao() {}
	/**
	 * 查询地区和已选择的地区
	 * @param templateId
	 * @param countyId 
	 * @return
	 * @throws JDBCException
	 */
	public List<SCountyDto> queryCounties(String templateId,String countyId, String dataRight) throws JDBCException{
		String sql = "select s.*,decode(c.county_id,null,0,1) checked from s_county s, "
			+ " (select * from t_template_county t where t.template_id=?) c "
			+ " where c.county_id(+)= s.county_id ";
		if(!SystemConstants.COUNTY_ALL.equals(countyId)){
			sql = StringHelper.append(sql ," and s.",dataRight.trim());
		}
		return createQuery(SCountyDto.class, sql, templateId).list();
	}

	/**
	 * 根据模板ID删除本身地区选择记录
	 * @param templateId
	 * @throws JDBCException
	 */
	public void deleteByTplId(String templateId) throws JDBCException{
		String sql = "delete from t_template_county t where t.template_id=?";
		executeUpdate(sql, templateId);
	}

	/**
	 * 根据模板类型和地区ID删除同类型的记录
	 * @param templateType
	 * @param countyIds
	 * @throws JDBCException
	 */
	public void deleteByTplType(String templateType,String[] countyIds) throws JDBCException{
		if (countyIds.length>0){
			String sql = "delete from t_template_county t where t.template_type=? and t.county_id in ("+sqlGenerator.in(countyIds)+")";
			executeUpdate(sql, templateType);
		}
	}

	public List<SCounty> getCountyById(String county_id ) throws Exception{
		String sql = "select * from  s_county where county_id =? ";
		return createQuery(SCounty.class,sql,county_id).list();
	}

	public List<SCounty> getCountyByAreaId(String area_id ) throws Exception{
		String sql = "select * from  s_county where area_id ='"+area_id+"' ";
		return createQuery(SCounty.class,sql).list();
	}
	
	public List<TreeDto> getCountyTreeByDataRight(String dataRight) throws Exception{
		String sql = StringHelper.append("select * from ( select county_id id, area_id||'-1' pid, county_name text from s_county where " ,dataRight,
				" union select area_id||'-1' id, '-1' pid, area_name text from s_area where area_id in (select area_id from s_county where " ,dataRight,
				" )) t  start with t.pid = '-1' connect by prior t.id = t.pid  order by level ");
	    return createQuery(TreeDto.class,sql).list();
	}
	
	/**
	 * 查询部门树.
	 * @param dataRight
	 * @return
	 * @throws Exception
	 */
	public List<TreeDto> getDeptTreeByDataRight(String dataRight) throws Exception{
		String sql = StringHelper.append(
				" select *  from (select dept_id id, dept_pid  pid, dept_name text  from s_dept ) t ",
				" start with t.pid = '-1' connect by prior t.id = t.pid  order by level ");
	    return createQuery(TreeDto.class,sql).list();
	}
	
	public List<SCounty> queryCountyByDataRight(String dataRight) throws Exception{
		String sql = "select * from s_county s where "+dataRight;
		return createQuery(sql).list();
	}
	
//	public List<TreeDto> getCountyTree() throws Exception{
//			String sql = "  select * from ( select county_id id, area_id pid, county_name text from s_county  union select area_id id, '-1' pid, area_name text from s_area ) t  start with t.pid = '-1' connect by prior t.id = t.pid  order by level ";
//			  return createQuery(TreeDto.class,sql).list();
//	}
//	public List<TreeDto> getCountyTreeByAreaId(String areaId ) throws Exception{
//		String sql = "  select * from ( select county_id id, area_id pid, county_name text from s_county where area_id = ?  union select area_id id, '-1' pid, area_name text from s_area where area_id = ? ) t  start with t.pid = '-1' connect by prior t.id = t.pid  order by level ";
//		 return createQuery(TreeDto.class,sql,areaId,areaId).list();
//	}
//	
//	public List<TreeDto> getCountyTreeByCountyId(String areaId ) throws Exception{
//		String sql = "  select * from ( select county_id id, area_id pid, county_name text from s_county where county_id = ?  union select area_id id, '-1' pid, area_name text from s_area  where area_id in(select area_id from s_county where county_id= ?)) t  start with t.pid = '-1' connect by prior t.id = t.pid  order by level ";
//		 return createQuery(TreeDto.class,sql,areaId,areaId).list();
//	}
	public List<SCounty> querySwitchCounty(String dataRight)  throws Exception{
		String sql ="select * from s_county where "+dataRight;
		return createQuery(sql).list();
	}
	
	public List<String> querySwitchArea(String dataRight)  throws Exception{
		String sql ="select distinct area_id from s_county where "+dataRight;
		return findUniques(sql);
	}
	
	public List<TreeDto> getCountyTreeAndOptr(SRoleDto role,SOptr optr,String countyDataRight) throws Exception{
		String str = "";
		String sql = "";
		
		//如果是省公司
		if(optr.getCounty_id().equals(SystemConstants.COUNTY_ALL)){
			countyDataRight = SystemConstants.DEFAULT_DATA_RIGHT;
		}
		
		if(role.getRole_type().equals(SystemConstants.ROLE_TYPE_MENU)&&StringHelper.isNotEmpty(role.getSub_system_id())){
			str = "and t1.role_type='"+SystemConstants.ROLE_TYPE_MENU+"' and t1.sub_system_id = '"+role.getSub_system_id()+"'";
		}
		if(role.getRole_type().equals(SystemConstants.ROLE_TYPE_DATA)&&StringHelper.isNotEmpty(role.getData_right_type())){
			if(StringHelper.isNotEmpty(role.getRule_id())){
				str = "and t1.role_type='"+SystemConstants.ROLE_TYPE_DATA+"' and t1.data_right_type = '"+role.getData_right_type()+"' and t1.rule_id is not null ";
			}
			if(StringHelper.isNotEmpty(role.getData_right_level())){
				str = "and t1.role_type='"+SystemConstants.ROLE_TYPE_DATA+"' and t1.data_right_type = '"+role.getData_right_type()+"' and t1.data_right_level is not null ";
			}
		}
		if(StringHelper.isNotEmpty(str)){
		  sql = StringHelper.append("  select * from ( select county_id id, '-1' pid, county_name text from s_county where 1=1  and ",countyDataRight,
				" union select optr_id id ,county_id pid,optr_name text from s_optr where status = ?  and " ,countyDataRight,
				"and  optr_id not in( select distinct t2.optr_id from s_role t1,s_optr_role t2 where  t1.role_id =t2.role_id "+str+" ) " ,
				" union select o.optr_id id, o.county_id pid, o.optr_name||'('||t1.role_name||')' text ",
				" from s_optr o ,s_optr_role sor,s_role t1 where ",
				SystemConstants.DEFAULT_DATA_RIGHT.equals(countyDataRight) ? countyDataRight : "o."+countyDataRight.trim(),
				" and o.status = ? and  sor.optr_id=o.optr_id and t1.role_id=sor.role_id "+str+" "+
				" ) t  start with t.pid = '-1' connect by prior t.id = t.pid  order by level ");
		}
		  return createQuery(TreeDto.class,sql,StatusConstants.ACTIVE,StatusConstants.ACTIVE).list();
	}
	
	public List<TreeDto> queryAddTemplateOptr(String[] columnIds, String countyId, String countyDataRight) throws Exception {
		if(countyId.equals(SystemConstants.COUNTY_ALL)){
			countyDataRight = SystemConstants.DEFAULT_DATA_RIGHT;
		}
		String sql = "select * from (" +
			"select county_id id, '-1' pid, county_name text from s_county where 1=1 and "+countyDataRight+
			" union " +
			" select t.optr_id id ,t.county_id pid,t.optr_name text" +
			" from s_optr t,s_optr_role sr,s_role_resource srr" +
			" where t.optr_id=sr.optr_id and sr.role_id=srr.role_id and srr.res_id='16'" +
			" and not exists (" +
			" 	select 1 from s_optr_resource tt " +
			" 	where tt.optr_id=t.optr_id and tt.res_id='16' and tt.more_or_less='0'" +
			" ) and not exists (" +
			"     select 1 from t_template_column_optr co where co.optr_id=t.optr_id" +
			"     and co.column_id in ("+sqlGenerator.in(columnIds)+")" +
			" ) and t.status=? " +
			" union " +
			" select t.optr_id id, t.county_id pid, t.optr_name text" +
			" from s_optr t, s_optr_resource sr" +
	        " where t.optr_id = sr.optr_id" +
	        "   and sr.res_id = '16'" +
	        "   and sr.more_or_less='1'" +
	        "   and not exists" +
	        " (select 1 from t_template_column_optr co" +
	        "         where co.optr_id = t.optr_id" +
	        "          and co.column_id in ("+sqlGenerator.in(columnIds)+")" +
	        " )  and t.status = ?" +
			") t  start with t.pid = '-1' connect by prior t.id = t.pid  order by level";
		return createQuery(TreeDto.class,sql,StatusConstants.ACTIVE,StatusConstants.ACTIVE).list();
	}
	
	public List<TreeDto> queryEditTemplateOptr(String[] columnIds, String countyId, String countyDataRight) throws Exception {
		if(countyId.equals(SystemConstants.COUNTY_ALL)){
			countyDataRight = SystemConstants.DEFAULT_DATA_RIGHT;
		}
		String sql = "select * from (" +
			"select county_id id, '-1' pid, county_name text from s_county where 1=1  and "+countyDataRight+
			" union " +
			" select t.optr_id id ,t.county_id pid,t.optr_name text from s_optr t,s_optr_role sr,s_role_resource srr" +
			" where t.optr_id=sr.optr_id and sr.role_id=srr.role_id and srr.res_id='16'" +
			" and not exists (" +
			" 	select 1 from s_optr_resource tt " +
			" 	where tt.optr_id=t.optr_id and tt.res_id='16' and tt.more_or_less='0'" +
			" ) and t.status=?" +
			" union " +
			" select t.optr_id id, t.county_id pid, t.optr_name text" +
			" from s_optr t, s_optr_resource sr" +
	        " where t.optr_id = sr.optr_id" +
	        "   and sr.res_id = '16'" +
	        "   and sr.more_or_less='1'" +
	        " and t.status = ?" +
			" ) t  start with t.pid = '-1' connect by prior t.id = t.pid  order by level";
		return createQuery(TreeDto.class,sql,StatusConstants.ACTIVE,StatusConstants.ACTIVE).list();
	}
	
	public List<TreeDto> getCountyTreeOptrByCounty(String countyId ) throws Exception{
		String sql = "  select * from ( select county_id id, area_id pid, county_name text from s_county where area_id = ?  union select area_id id, '-1' pid, area_name text from s_area where area_id = ? ) t  start with t.pid = '-1' connect by prior t.id = t.pid  order by level ";
		 return createQuery(TreeDto.class,sql,countyId,countyId).list();
	}
	public List<SCounty> queryAllCounty() throws JDBCException{
		String sql = " select * from s_county ";
		return createQuery(sql).list();
	}
}
