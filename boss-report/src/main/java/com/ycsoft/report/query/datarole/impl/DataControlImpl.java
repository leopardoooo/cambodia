package com.ycsoft.report.query.datarole.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ycsoft.beans.system.SOptr;
import com.ycsoft.beans.system.SRole;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.report.bean.RepKeyLevel;
import com.ycsoft.report.commons.ReportConstants;
import com.ycsoft.report.dao.config.QueryRepDao;
import com.ycsoft.report.dao.config.RepOptrConfigrepDao;
import com.ycsoft.report.dao.keycon.QueryKeyValueDao;
import com.ycsoft.report.dao.keycon.RepKeyLevelDao;
import com.ycsoft.report.query.datarole.BaseDataControl;
import com.ycsoft.report.query.datarole.DataRole;
import com.ycsoft.report.query.datarole.FuncType;
import com.ycsoft.report.query.datarole.RepLevelManage;
import com.ycsoft.report.query.key.Impl.QueryKeyValue;
/**
 * 
 * 
 *
 */
public class DataControlImpl extends BaseDataControl {


	/**
	 * 报表标准数据权限
	 * @param optr
	 * @return
	 * @throws JDBCException
	 * @throws ReportException
	 */
	private Integer initRepRole(SOptr optr) throws JDBCException, ReportException{
		Integer rep_role=null;
		if(optr.getLogin_name().equals("admin")){
			rep_role=0;
		}else{
			rep_role= repKeyLevelDao.queryRepRole(optr.getOptr_id());
		}
		if(rep_role==null)
			rep_role=4;
		return rep_role;
	}
	/**
	 * 自定义数据权限
	 * @param optr
	 * @return
	 * @throws ReportException
	 */
	private Map<String,List<QueryKeyValue>> initSDataRight(SOptr optr) throws ReportException{
		Map<String,List<QueryKeyValue>> map=new HashMap<String,List<QueryKeyValue>>();
		for(SRole vo: repKeyLevelDao.querySDatarightRole(optr.getOptr_id())){
			List<QueryKeyValue> list=repLevelManage.getDatarightValues(vo.getData_right_type(), vo.getRule_id());
			if(list!=null&&list.size()>0)
				map.put(vo.getData_right_type(), list);
		}
		return map;
	}
	/**
	 * 操作员的optr_id,dept_id,county_id,area_id对应4,3,2,1 权限初始化
	 * @param optr
	 * @return
	 */
	private Map<Integer,QueryKeyValue> initRepRoleValueMap(SOptr optr){
		Map<Integer,QueryKeyValue> map=new HashMap<Integer,QueryKeyValue>();
		QueryKeyValue area=new QueryKeyValue();
		area.setId(optr.getArea_id());
		area.setName(MemoryDict.getDictName(DictKey.AREA, area.getId()));
		map.put(1, area);
		
		QueryKeyValue county=new QueryKeyValue();
		county.setId(optr.getCounty_id());
		county.setName(MemoryDict.getDictName(DictKey.COUNTY, county.getId()));
		county.setPid(optr.getArea_id());
		map.put(2, county);
		
		QueryKeyValue dept=new QueryKeyValue();
		dept.setId(optr.getDept_id());
		dept.setName(MemoryDict.getDictName(DictKey.DEPT, dept.getId()));
		dept.setPid(optr.getCounty_id());
		map.put(3, dept);
		
		QueryKeyValue optrid=new QueryKeyValue();
		optrid.setId(optr.getOptr_id());
		optrid.setName(optr.getOptr_name());
		optrid.setPid(optr.getDept_id());
		map.put(4, optrid);
		
		return map;
	}
	
	public DataRole setDataRole(SOptr optr) throws ReportException {
		
		try {
			DataRoleImpl datarole=new DataRoleImpl();
			datarole.setReprole(this.initRepRole(optr));
			datarole.setDataright_map(this.initSDataRight(optr));
			datarole.setReprolemap(this.initRepRoleValueMap(optr));
			datarole.setFuncs(this.initFuncRoles(optr));
			setRole(datarole);
			return datarole;
		} catch (JDBCException e) {
			throw new ReportException(e,e.getSQL());
		}
	}
	
	/**
	 * rep_key_level：
	 * 1 配置的s_data_right_type 使用该值取值
	 * 2 配置了role_level,如果valuesql为空或者valuesql未配置#system#键值，
	 * 			则使用role_level对应操作员权限取值
	 * 3 配置了role_level,如果valuesql不为空，有未配置#system#键值，
	 * 			则role_level对应操作员权限取值替换键值，去数据库取值
	 */
	public List<QueryKeyValue> getControlValues(String key) throws ReportException {
		DataRole role=this.getRole();
		//有缓存直接提取
		List<QueryKeyValue> role_value=role.getKeyValueByCache(key);
		if(role_value!=null) return role_value;
		//无缓存查询提取
		RepKeyLevel level=repLevelManage.getKeyLevel(key);

		if(this.isDataRightTypeControl(key)){
			role_value= role.getDataRightValues(level.getS_data_right_type());
		}else{
			if(StringHelper.isEmpty(level.getValuesql())){
				role_value=new ArrayList<QueryKeyValue>();
				role_value.add(role.getReproleValue(level.getRole_level()));
			}else
				role_value= queryKeyValueDao.findList(ReportConstants.DATABASE_SYSTEM,
							level.getValuesql().replaceAll("#system#", role.getReproleValue(level.getRole_level()).getId()));
		}
		//装入权限缓存
		role.setKeyValueCache(key, role_value);
		return role_value;
	}

	public boolean isControl(String key) throws ReportException {
		if(this.getRole()==null)
			return false;
		RepKeyLevel level=repLevelManage.getKeyLevel(key);
		if(level==null)
			return false;
		//自定义权限存在则限制
		if(this.getRole().getDataRightValues(level.getS_data_right_type())!=null)
			return true;
		//广电级不限制
		if(this.getRole().getReprole()==0)
			return false;
		if(level.getRole_level()==null)
			throw new ReportException(key+":rep_key_levle is defined_error.");
		
		if(level.getRole_level()<=this.getRole().getReprole())
			return true;
		return false;
	}
	
	public boolean isDataRightTypeControl(String key) throws ReportException {
		if(this.getRole()==null)
			return false;
		RepKeyLevel level=repLevelManage.getKeyLevel(key);
		if(level==null)
			return false;
		if(StringHelper.isNotEmpty(level.getS_data_right_type())){
			if(level.getRole_level()==null)
				return true;
			//当前数据权限_配置值<键值数据权限_配置值
			//(即当前数据权限>键值的数据权限)
			if(this.getRole().getReprole()<level.getRole_level())
				return true;
		}
		return false;
	}
	
	/**
	 * 初始化编辑功能权限
	 * @param optr
	 * @return
	 * @throws ReportException 
	 */
	private FuncType[] initFuncRoles(SOptr optr) throws ReportException{
		try {
			if(optr.getLogin_name().equals("admin")){
				return FuncType.values();
			}
			
			//报表编辑权限
			if(repOptrConfigrepDao.queryEditOptr(optr.getOptr_id())){
				return FuncType.values();
			}
			List<FuncType> list=new ArrayList<FuncType>();
			//编辑备注和查看SQL权限
			List<QueryKeyValue> templist= queryRepDao.queryReportEditRole(optr.getOptr_id());
			if(templist!=null&&templist.size()>0){
				for(QueryKeyValue vo:templist){
					for(FuncType type: FuncType.values()){
						if(type.toString().equals(vo.getId()))
							list.add(type);
					}
				}
			}
			return list.size()==0?null:list.toArray(new FuncType[list.size()]);
		} catch (JDBCException e) {
			throw new ReportException(e,e.getSQL());
		}
	}
	
	private QueryRepDao queryRepDao;
	private RepOptrConfigrepDao repOptrConfigrepDao;
	private RepKeyLevelDao repKeyLevelDao;
	private RepLevelManage repLevelManage;
	private QueryKeyValueDao queryKeyValueDao;
	public RepLevelManage getRepLevelManage() {
		return repLevelManage;
	}
	public void setRepLevelManage(RepLevelManage repLevelManage) {
		this.repLevelManage = repLevelManage;
	}
	public RepKeyLevelDao getRepKeyLevelDao() {
		return repKeyLevelDao;
	}
	public void setRepKeyLevelDao(RepKeyLevelDao repKeyLevelDao) {
		this.repKeyLevelDao = repKeyLevelDao;
	}

	public QueryKeyValueDao getQueryKeyValueDao() {
		return queryKeyValueDao;
	}

	public void setQueryKeyValueDao(QueryKeyValueDao queryKeyValueDao) {
		this.queryKeyValueDao = queryKeyValueDao;
	}
	public RepOptrConfigrepDao getRepOptrConfigrepDao() {
		return repOptrConfigrepDao;
	}
	public void setRepOptrConfigrepDao(RepOptrConfigrepDao repOptrConfigrepDao) {
		this.repOptrConfigrepDao = repOptrConfigrepDao;
	}
	public QueryRepDao getQueryRepDao() {
		return queryRepDao;
	}
	public void setQueryRepDao(QueryRepDao queryRepDao) {
		this.queryRepDao = queryRepDao;
	}
	
}
