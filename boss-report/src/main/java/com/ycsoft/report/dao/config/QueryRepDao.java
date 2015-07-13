package com.ycsoft.report.dao.config;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.ycsoft.beans.system.SOptr;
import com.ycsoft.beans.system.SResource;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;
import com.ycsoft.report.bean.RepQueryLog;
import com.ycsoft.report.commons.ReportConstants;
import com.ycsoft.report.commons.tree.RepTreeBuilder;
import com.ycsoft.report.dto.RepResourceDto;
import com.ycsoft.report.query.datarole.BaseDataControl;
import com.ycsoft.report.query.datarole.FuncType;
import com.ycsoft.report.query.key.Impl.QueryKeyValue;
import com.ycsoft.sysmanager.dto.system.SResourceDto;

@Component
public class QueryRepDao extends BaseEntityDao<SResource> {
	
	private String driverclassname;
	
	/**
	 * 查询报表编辑备注和查看SQL功能
	 * @return 
	 * @throws ReportException 
	 */
	public List<QueryKeyValue> queryReportEditRole(String optr_id) throws ReportException{
		try {
			
			String sql=StringHelper.append("select distinct ru.rule_id id,",
				"'select '||ty.result_column||' id,'||ty.select_column||' name from '||ty.table_name||' where '||ru.rule_str name,",
				"ru.data_type pid",
				" from s_role r,t_rule_define ru,s_data_right_type ty ,s_optr_role b",
				" where r.role_type='DATA' and r.rule_id=ru.rule_id and r.data_right_type=ru.data_type and b.role_id=r.role_id and b.optr_id =? ",
				" and ty.data_right_type=r.data_right_type and ty.table_name is not null and ru.data_type =? ").toString();
		
			QueryKeyValue vo=this.createQuery(QueryKeyValue.class, sql, optr_id,ReportConstants.SDATARIGHTTYPE_ES).first();
			if(vo!=null){
				return this.createQuery(QueryKeyValue.class, vo.getName()).list();
			}
			return null;
		} catch (JDBCException e) {
			throw new ReportException(e,e.getSQL());
		}
	}
	
	public SOptr querySOptrByloginname(String loginname) throws JDBCException{
		String sql="select t.* from s_optr t where t.login_name=?  and t.status=? ";
		return this.createQuery(SOptr.class, sql, loginname,StatusConstants.ACTIVE).first();
		
	}
	/**
	 * 查询资源菜单根据操作员权限
	 * @return
	 * @throws Exception
	 */
	public List<RepResourceDto> queryRepResources(SOptr optr,String subSystemId)throws Exception{
		String sql="";
		if(StringUtils.isEmpty(subSystemId)){
			return null;
		}else{
			if("com.mysql.jdbc.Driver".equals(this.getDriverclassname())){
				if(!BaseDataControl.getRole().hasFunc(FuncType.EDITREP)){	
					sql ="select a.res_id,a.res_pid,a.res_name,a.url,a.sort_num,a.handler from s_resource a where a.res_id in ( "
						+" select distinct s.res_id from s_resource s,s_optr_role o,s_role_resource rr "
						+" where s.res_status = 'ACTIVE' and  o.role_id=rr.role_id and rr.res_id=s.res_id and s.res_type='MENU' "
						+" and s.sub_system_id='7' and o.optr_id=? ) "
						+" union all "
						+" select a.res_id,a.res_pid,a.res_name,a.url,a.sort_num,a.handler from s_resource a where a.res_id in ( "
						+" select distinct s.res_pid from s_resource s,s_optr_role o,s_role_resource rr "
						+" where s.res_status = 'ACTIVE' and  o.role_id=rr.role_id and rr.res_id=s.res_id and s.res_type='MENU' "
						+" and s.sub_system_id='7' and o.optr_id=? ) or a.res_pid='-1' "
						+" order by sort_num";
					return RepTreeBuilder.orderByTree(createQuery(RepResourceDto.class, sql,optr.getOptr_id(),optr.getOptr_id()).list(),"-1");
				}else{
					sql="select s.*,db.ICONCLS from s_resource s LEFT JOIN rep_define rd on rd.REP_ID=s.RES_ID "
					+" left join rep_database db on db.DATABASE=rd.DATABASE"
					+" where  s.res_status=? and(s.SUB_SYSTEM_ID=? or s.res_pid='-1')  order by s.SORT_NUM";
					return RepTreeBuilder.orderByTree(createQuery(RepResourceDto.class, sql,StatusConstants.ACTIVE, subSystemId).list(), "-1");
				}
			}else{		
				if("admin".equals(optr.getLogin_name())){
					//admin可以看到失效的报表
					//管理子系统的角色管理不能管理失效的报表
					//失效的报表建议都放到一个专用的目录
					sql ="select a.*,da.iconcls  from ("
						+" SELECT * FROM ( select distinct res_id,res_pid,decode(t.res_status,'ACTIVE','','(已失效)')||res_name res_name,url,sort_num,handler from s_resource t "
				 		+" start with res_id in ("+
				 		"select distinct s.res_id from s_resource s"
						+ " where  s.res_type='"+SystemConstants.ROLE_TYPE_MENU+"' and s.sub_system_id=? "
				 		+") connect by prior res_pid = res_id) " +
				 		"start with res_pid = -1 connect by prior res_id = res_pid order siblings by sort_num"
				 		+") a,rep_define rd,rep_database da where da.database(+)=rd.database and rd.rep_id(+)=a.res_id ";
				    return createQuery(RepResourceDto.class, sql, subSystemId).list();
				}else if(!BaseDataControl.getRole().hasFunc(FuncType.EDITREP)){	
					sql ="select a.*,da.iconcls  from ("
						+" SELECT b.*,level lv FROM ( select distinct res_id,res_pid,res_name,url,sort_num,handler from s_resource t "
					 		+" start with res_id in (select distinct s.res_id from s_resource s,s_optr_role o,s_role_resource rr"
					+ " where s.res_status = ? and  o.role_id=rr.role_id and rr.res_id=s.res_id and res_type=? "
					+ " and sub_system_id=? and o.optr_id=? and s.res_id not in (select res_id from s_optr_resource where more_or_less ='0' and optr_id=? ) " 
					+ " union select distinct s.res_id from s_resource s,s_optr_resource sor where s.res_id=sor.res_id and sor.optr_id = ? and sub_system_id = ? and sor.more_or_less ='1') connect by prior res_pid = res_id) b" +
					 		" start with res_pid = -1 connect by prior res_id = res_pid " 
					+") a,rep_define rd,rep_database da where da.database(+)=rd.database and rd.rep_id(+)=a.res_id  order by a.lv,a.sort_num";
					return createQuery(RepResourceDto.class, sql,StatusConstants.ACTIVE,SystemConstants.ROLE_TYPE_MENU, subSystemId, optr.getOptr_id(),optr.getOptr_id(),optr.getOptr_id(),subSystemId).list();
				}else {
					sql ="select a.*,da.iconcls  from ("
						+" SELECT * FROM ( select distinct res_id,res_pid,decode(t.res_status,'ACTIVE','','(已失效)')||res_name res_name,url,sort_num,handler from s_resource t "
				 		+" start with res_id in ("+
				 		"select distinct s.res_id from s_resource s"
						+ " where s.res_status = ? and s.res_type='"+SystemConstants.ROLE_TYPE_MENU+"' and s.sub_system_id=? "
				 		+") connect by prior res_pid = res_id) " +
				 		"start with res_pid = -1 connect by prior res_id = res_pid order siblings by sort_num"
				 		+") a,rep_define rd,rep_database da where da.database(+)=rd.database and rd.rep_id(+)=a.res_id ";
				    return createQuery(RepResourceDto.class, sql,StatusConstants.ACTIVE, subSystemId).list();
				}
			}
		}
	}
	
	public boolean queryResExist(String optr_id,String rep_id) {
		return false;
	}
	
	/**
	 * 查询报表系统的res_type为NODE的信息
	 * @param optrId
	 * @param subSystemId
	 * @return
	 * @throws Exception
	 */
	public List<SResourceDto> queryResourcesByResType(String optrId,String subSystemId,String resType)throws Exception{
		if("com.mysql.jdbc.Driver".equals(this.getDriverclassname())){
			String sql ="  select t.res_id,t.res_pid,t.res_name,t.url,t.sort_num from s_resource t where  t.sub_system_id=? and t.res_type=?"
	                  + " union  "
	                  +" select t.res_id,t.res_pid,t.res_name,t.url,t.sort_num from s_resource t where t.res_pid='-1' "
	                  +" order   by sort_num " ;
			return createQuery(SResourceDto.class, sql,subSystemId,resType).list();
		}else{
		
			String sql =" SELECT * FROM ( select distinct res_id,res_pid,res_name,url,sort_num from s_resource t "
		 		+" start with res_id in (SELECT distinct res_id from s_resource where t.sub_system_id=? and res_type=?) connect by prior res_pid = res_id "
				+" ) start with res_pid = -1 connect by prior res_id = res_pid order siblings by sort_num" ;
	
			return createQuery(SResourceDto.class, sql,subSystemId,resType).list();
		}
	}
	/**
	 * 查询所有报表
	 * 
	 * @param optr_id
	 * @param start
	 * @param limit
	 * @return
	 * @throws JDBCException
	 */
	public List<SResource> queryAllRep(String optr_id) throws JDBCException {
		String sql = StringHelper
				.append(
						" select res.*  from  s_optr_role r,rep_define d,s_role_resource r_res,s_resource res",
						" where  r.role_id=r_res.role_id and r_res.res_id=res.res_id and res.res_id=d.rep_id",
						" and r.optr_id=?",
						" union ",
						" select res.*  from  rep_define d,s_optr_resource o,s_resource res ",
						" where  res.res_id=o.res_id and res.res_id=d.rep_id and o.optr_id=?");
		return createQuery(sql).list();
	}

	/**
	 * 查询营业报表
	 * 
	 * @param optr_id
	 * @param start
	 * @param limit
	 * @return
	 * @throws JDBCException
	 */
	public Pager<SResource> queryBusiness(String optr_id, Integer start,
			Integer limit) throws JDBCException {
		String sql = StringHelper
				.append(
						" select res.res_id,res.res_name,s1.item_name res_type,s2.item_name  res_status,d.database url " ,
						"from  s_optr_role r,rep_define d,s_role_resource r_res,s_resource res ,",
						" s_itemvalue s1,s_itemvalue s2",
						" where  r.role_id=r_res.role_id and r_res.res_id=res.res_id and res.res_id=d.rep_id",
						" and  d.rep_info='business' and r.optr_id=? ",
						" and s1.item_key='DEFINE_TYPE' and s1.item_value=d.rep_type ",
						" and s2.item_key='DEFINE_INFO' and s2.item_value=d.rep_info ",
						" union ",
						" select res.res_id,res.res_name,s1.item_name res_type,s2.item_name  res_status,d.database url " ,
						"from  rep_define d,s_optr_resource o,s_resource res ,",
						" s_itemvalue s1,s_itemvalue s2",
						" where  res.res_id=o.res_id and res.res_id=d.rep_id and d.rep_info='business' and o.optr_id=?",
						" and s1.item_key='DEFINE_TYPE' and s1.item_value=d.rep_type ",
						" and s2.item_key='DEFINE_INFO' and s2.item_value=d.rep_info ",
						" order by res_name");
		return createQuery(sql,optr_id,optr_id).setStart(start).setLimit(limit).page();
	}

	/**
	 * 查询财务报表
	 * 
	 * @param optr_id
	 * @param start
	 * @param limit
	 * @return
	 * @throws JDBCException
	 */
	public Pager<SResource> queryFinance(String optr_id, Integer start,
			Integer limit) throws JDBCException {
		String sql = StringHelper
		.append(
				" select res.res_id,res.res_name,s1.item_name res_type,s2.item_name  res_status,d.database url " ,
				"from  s_optr_role r,rep_define d,s_role_resource r_res,s_resource res ,",
				" s_itemvalue s1,s_itemvalue s2",
				" where  r.role_id=r_res.role_id and r_res.res_id=res.res_id and res.res_id=d.rep_id",
				" and  d.rep_info='finance' and r.optr_id=? ",
				" and s1.item_key='DEFINE_TYPE' and s1.item_value=d.rep_type ",
				" and s2.item_key='DEFINE_INFO' and s2.item_value=d.rep_info ",
				" union ",
				" select res.res_id,res.res_name,s1.item_name res_type,s2.item_name  res_status,d.database url " ,
				"from  rep_define d,s_optr_resource o,s_resource res ,",
				" s_itemvalue s1,s_itemvalue s2",
				" where  res.res_id=o.res_id and res.res_id=d.rep_id and d.rep_info='finance' and o.optr_id=?",
				" and s1.item_key='DEFINE_TYPE' and s1.item_value=d.rep_type ",
				" and s2.item_key='DEFINE_INFO' and s2.item_value=d.rep_info ",
				" order by res_name");
		return createQuery(sql,optr_id,optr_id).setStart(start).setLimit(limit).page();
	}

	/**
	 * 查询我的报表
	 * 
	 * @param optr_id
	 * @param start
	 * @param limit
	 * @return
	 * @throws JDBCException
	 */
	public Pager<SResource> queryMyRep(String optr_id, Integer start,
			Integer limit) throws JDBCException {
		String sql = StringHelper
				.append(
						"select   t4.res_id,t4.res_name,s1.item_name res_type,s2.item_name  res_status,d.database url " ,
						"from  s_optr_role t1,rep_myreport t2,s_role_resource t3,s_resource t4 ",
						",rep_define d,s_itemvalue s1,s_itemvalue s2",
						" where t2.optr_id=t1.optr_id and t1.role_id=t3.role_id and t3.res_id=t2.rep_id ",
						" and t2.rep_id=t4.res_id and t4.res_status='ACTIVE' and t2.optr_id=?",
						" and d.rep_id=t2.rep_id and  s1.item_key='DEFINE_TYPE' and s1.item_value=d.rep_type ",
						" and s2.item_key='DEFINE_INFO' and s2.item_value=d.rep_info ",
						" union ",
						" select t4.res_id,t4.res_name,s1.item_name,s2.item_name ,d.database url from  rep_myreport t2,s_optr_resource t1,s_resource t4 ",
						",rep_define d,s_itemvalue s1,s_itemvalue s2",
						" where  t4.res_id=t2.rep_id and t4.res_status='ACTIVE' ",
						" and d.rep_id=t2.rep_id and  s1.item_key='DEFINE_TYPE' and s1.item_value=d.rep_type ",
						" and s2.item_key='DEFINE_INFO' and s2.item_value=d.rep_info ",
						" and t1.optr_id=t2.optr_id and t1.res_id=t2.rep_id and t2.optr_id=? order by res_name");
		return createQuery(sql, optr_id, optr_id).setStart(start).setLimit(
				limit).page();
	}
	/**
	 * 查询当天打开的报表
	 * @param optr_id
	 * @param start
	 * @param limit
	 * @return
	 * @throws JDBCException
	 */
	public Pager<RepQueryLog> queryDayOpen(String optr_id,Integer start,Integer limit) throws JDBCException{
		String sql=StringHelper
		.append(" select t.rep_id,s.res_name query_id,max(t.create_date) create_date"
				," from rep_query_log t,s_resource  s where  t.rep_id=s.res_id and  t.optr_id=?"
				," group by t.rep_id,s.res_name   order by create_date desc");
		return createQuery(RepQueryLog.class,sql,optr_id).setStart(start).setLimit(limit).page();
	}
	
	public String getDriverclassname() {
		return driverclassname;
	}
	public void setDriverclassname(String driverclassname) {
		this.driverclassname = driverclassname;
	}

}
