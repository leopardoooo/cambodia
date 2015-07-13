package com.ycsoft.business.dao.system;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.ycsoft.beans.system.SOptr;
import com.ycsoft.beans.system.SResource;
import com.ycsoft.commons.constants.SequenceConstants;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;
import com.ycsoft.sysmanager.dto.system.SResourceDto;

/**
 * 资源菜单数据库操作类
 *
 */
@Component
public class SResourceDao extends BaseEntityDao<SResource> {

	/**
	 *
	 */
	private static final long serialVersionUID = 356272872772817830L;

	/**
	 * 获取权限资源菜单
	 *
	 * @return
	 * @throws Exception
	 */
	public List<SResource> findResource(String optrId, String systemId)
			throws Exception {
		String sql = "select *  from (select distinct s.* from s_resource s,s_optr_role o,s_role_resource rr"
				+ " where s.res_status = ? and  o.role_id=rr.role_id and rr.res_id=s.res_id and res_type=? "
				+ " and sub_system_id=? and o.optr_id=? and s.res_id not in (select res_id from s_optr_resource where more_or_less ='0' and optr_id=? ) " 
				+ " union select distinct s.* from s_resource s,s_optr_resource sor where s.res_id=sor.res_id and sor.optr_id = ? and sub_system_id = ? and sor.more_or_less ='1') order by sort_num ";
		return createQuery(SResource.class, sql,StatusConstants.ACTIVE,SystemConstants.ROLE_TYPE_MENU, systemId, optrId,optrId,optrId,systemId).list();
	}

	/**
	 * 查询菜单资源信息
	 */
	public Pager<SResource> query(Integer start , Integer limit ,String keyword,String pid)throws Exception{
		String cond="";
		if( StringUtils.isNotEmpty(keyword)){
			cond= " and t1.res_name like '%"+keyword+"%'" ;
		}
		if (StringHelper.isNotEmpty(pid)){          //start with 是指从哪个节点开始递归查询
		cond =" and t1.res_id in (select res_id from s_resource start with res_id ='"+pid+"' connect by prior res_id = res_pid)";
		}
		String sql =" select t1.*,t2.res_name res_pid_text,t3.sub_system_name sub_system_text from s_resource t1," +
				"s_resource t2,s_sub_system t3 "
					+" where t2.res_status =? AND t1.res_pid=t2.res_id and t1.sub_system_id=t3.sub_system_id(+) ";
			   sql=sql+cond+" order by t1.sort_num desc ";
	return createQuery(SResource.class , sql,StatusConstants.ACTIVE).setLimit(limit)
				.setStart(start).page();
	}
	/**
	 * 判断资源ID是否存在
	 * @return
	 */
	public  boolean getResid(String res_id) throws Exception {
		String sql =" select count(*) from s_resource where res_id=?";
		Object count= findUnique(sql, res_id);
		if(count==null){
			return true;
		}
		return false;
	}
	/**
	 * 获取资源ID
	 */
	public String getResourceID() throws Exception{
		return findSequence(SequenceConstants.SEQ_S_RESOURCE).toString();
	}
	/**
	 * 查询资源菜单
	 */
	public List<SResourceDto> ResourcesTree(String login_name,String role_id)throws Exception{
		String sql ="";
		//获取角色对应资源id
		//String sql="select res_id from s_role_resource where role_id in ( '"+role_id+"')";
		//获取操作员对应角色id
		//sql=" select distinct * from s_role where  role_id in ( select role_id from s_optr_role where optr_id ='"+optr_id+"')";
		if(SystemConstants.SUPER_ADMIN.equals(login_name)){
			//超级管理员
			sql =" SELECT * FROM ( select distinct res_id,res_pid,res_name,url from s_resource t "
			 		+" start with res_id in (SELECT distinct res_id from s_resource) connect by prior res_pid = res_id "
					+" ) start with res_pid = -1 connect by prior res_id = res_pid order by level" ;
		}else{
			sql=" SELECT  * FROM ( "
				   +" select distinct res_id,res_pid,res_name,url from s_resource t  "
				   +"   start with res_id in (SELECT distinct res_id from s_resource  "
				   +"   where res_id in (select res_id from s_optr_resource where optr_id in ( select optr_id from s_optr where login_name='"+login_name+"'))) "
				   +"   connect by prior res_pid = res_id "
				   +" union "
				   +"   select distinct res_id,res_pid,res_name,url from s_resource t    "
				   +"   start with res_id in (SELECT distinct res_id from s_resource     "
				   +"   where res_id in (select res_id from s_role_resource where  role_id in  "
				   +"	( select role_id from s_optr_role where optr_id in ( select optr_id from s_optr where login_name='"+login_name+"'))))     "
				   +"  connect by prior res_pid = res_id "
				+" )start with res_pid = -1 connect by prior res_id = res_pid order by level" ;

		}
		return createQuery(SResourceDto.class, sql).list();


	}


	//
	public boolean isNotSuperAdmin(HttpSession session){
		  String userLoginName = session.getAttribute("userLoginName").toString();
		  return !SystemConstants.SUPER_ADMIN.equals(userLoginName);
	  }

	public List<SResourceDto> queryResources(String subSystemId, String resType) throws JDBCException {
		String sql="";
		String restype = "";
		if ("NODE".equals(resType)){
			restype = " and res_type = 'NODE' ";
		}
		if(StringUtils.isEmpty(subSystemId)){
			//暂时没有根据角色来查找
			sql =" SELECT * FROM ( select distinct res_id,res_pid,res_name,url,sort_num from s_resource t "
			 		+" start with res_id in (SELECT distinct res_id from s_resource where 1=1 "+restype+" ) connect by prior res_pid = res_id "
					+" ) start with res_pid = -1 connect by prior res_id = res_pid order siblings by sort_num" ;
		}else{
			//暂时没有根据角色来查找
			sql =" SELECT * FROM ( select distinct res_id,res_pid,res_name,url,sort_num from s_resource t "
			 		+" start with res_id in (SELECT distinct res_id from s_resource where t.sub_system_id  in ("+subSystemId+") "+restype+" ) connect by prior res_pid = res_id "
					+" ) start with res_pid = -1 connect by prior res_id = res_pid order siblings by sort_num" ;
		}

		return createQuery(SResourceDto.class, sql).list();
	}

	/**
	 * 查询报表系统的res_type为NODE的信息
	 * @param optrId
	 * @param subSystemId
	 * @return
	 * @throws Exception
	 */
	public List<SResourceDto> queryResourcesByResType(String optrId,String subSystemId,String resType)throws Exception{
		String sql =" SELECT * FROM ( select distinct res_id,res_pid,res_name,url,sort_num from s_resource t "
			 		+" start with res_id in (SELECT distinct res_id from s_resource where t.sub_system_id=? and res_type=?) connect by prior res_pid = res_id "
					+" ) start with res_pid = -1 connect by prior res_id = res_pid order siblings by sort_num" ;

		return createQuery(SResourceDto.class, sql,subSystemId,resType).list();
	}

	/**
	 * 查询资源菜单根据操作员权限
	 * @return
	 * @throws Exception
	 */
	public List<SResourceDto> queryResourcesByOptr(String optrId,String subSystemId)throws Exception{
		String sql="";
		if(StringUtils.isEmpty(subSystemId)){
			return null;
		}else{
			//暂时没有根据角色来查找		
			sql =" SELECT * FROM ( select distinct res_id,res_pid,res_name,url,sort_num from s_resource t "
			 		+" start with res_id in ("+
			 		"select distinct s.res_id from s_resource s,s_optr_role o,s_role_resource rr"
					+ " where s.res_status = ? and  o.role_id=rr.role_id and rr.res_id=s.res_id and res_type='"+SystemConstants.ROLE_TYPE_MENU+"'"
					+ " and sub_system_id=? and o.optr_id=? "
			 		+") connect by prior res_pid = res_id) " +
			 		"start with res_pid = -1 connect by prior res_id = res_pid order siblings by sort_num" ;
			return createQuery(SResourceDto.class, sql,sql,StatusConstants.ACTIVE, subSystemId, optrId).list();
		}
	}
	/**
	 * 查询资源菜单根据操作员权限
	 * @return
	 * @throws Exception
	 */
	public List<SResourceDto> queryResourcesByOptr(SOptr optr,String subSystemId)throws Exception{
		String sql="";
		if(StringUtils.isEmpty(subSystemId)){
			return null;
		}else{
			if(!optr.getLogin_name().equals("admin")){	
				sql =" SELECT * FROM ( select distinct res_id,res_pid,res_name,url,sort_num from s_resource t "
				 		+" start with res_id in (select distinct s.res_id from s_resource s,s_optr_role o,s_role_resource rr"
				+ " where s.res_status = ? and  o.role_id=rr.role_id and rr.res_id=s.res_id and res_type=? "
				+ " and sub_system_id=? and o.optr_id=? and s.res_id not in (select res_id from s_optr_resource where more_or_less ='0' and optr_id=? ) " 
				+ " union select distinct s.res_id from s_resource s,s_optr_resource sor where s.res_id=sor.res_id and sor.optr_id = ? and sub_system_id = ? and sor.more_or_less ='1') connect by prior res_pid = res_id) " +
				 		"start with res_pid = -1 connect by prior res_id = res_pid order siblings by sort_num" ;
				return createQuery(SResourceDto.class, sql,StatusConstants.ACTIVE,SystemConstants.ROLE_TYPE_MENU, subSystemId, optr.getOptr_id(),optr.getOptr_id(),optr.getOptr_id(),subSystemId).list();
			}else{
				sql =" SELECT * FROM ( select distinct res_id,res_pid,res_name,url,sort_num from s_resource t "
			 		+" start with res_id in ("+
			 		"select distinct s.res_id from s_resource s"
					+ " where s.res_status = ? and s.res_type='"+SystemConstants.ROLE_TYPE_MENU+"' and s.sub_system_id=? "
			 		+") connect by prior res_pid = res_id) " +
			 		"start with res_pid = -1 connect by prior res_id = res_pid order siblings by sort_num" ;
			    return createQuery(SResourceDto.class, sql,StatusConstants.ACTIVE, subSystemId).list();
			}
		}
	}

	public List<SResourceDto> queryResources() throws JDBCException {
		String sql=StringHelper.append("SELECT * FROM (",
					" select distinct res_id,res_pid,res_name,url,sort_num from s_resource t",
					" start with res_id in (SELECT distinct res_id from s_resource where ",
					" res_type in ('MENU','BUTTON','NODE') ",
					" ) connect by prior res_pid = res_id ",
					" ) start with res_pid = -1 connect by prior res_id = res_pid order siblings by sort_num"
			);
		return createQuery(SResourceDto.class, sql).list();
	}
	
	public List<SResource> queryByResIds(String[] resids)throws JDBCException{
		String sql = "select * from s_resource where" +getSqlGenerator().setWhereInArray("res_id", resids);
		return createQuery(sql).list();
	}

}
