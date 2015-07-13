package com.ycsoft.report.component.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.system.SDataRightType;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.beans.system.SRole;
import com.ycsoft.beans.system.SSubSystem;
import com.ycsoft.business.dao.system.SDataRightTypeDao;
import com.ycsoft.business.dao.system.SOptrDao;
import com.ycsoft.business.dao.system.SOptrRoleDao;
import com.ycsoft.business.dao.system.SResourceDao;
import com.ycsoft.business.dao.system.SRoleDao;
import com.ycsoft.business.dao.system.SSubSystemDao;
import com.ycsoft.commons.abstracts.BaseComponent;
import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.commons.helper.MD5;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.report.commons.ReportConstants;
import com.ycsoft.report.commons.SystemConfig;
import com.ycsoft.report.dao.config.QueryRepDao;
import com.ycsoft.report.dao.keycon.QueryKeyValueDao;
import com.ycsoft.report.dto.RepDataRight;
import com.ycsoft.report.dto.RepResourceDto;
import com.ycsoft.report.query.datarole.BaseDataControl;
import com.ycsoft.report.query.datarole.DataRoleInit;
import com.ycsoft.report.query.datarole.FuncType;
import com.ycsoft.report.query.key.Impl.QueryKeyValue;
import com.ycsoft.sysmanager.dto.system.SResourceDto;
import com.ycsoft.sysmanager.dto.system.SRoleDto;

/**
 *
 * 首页显示的管理器
 * @author hh
 * @date Dec 29, 2009 4:02:49 PM
 */
@Component
public class IndexComponent extends BaseComponent{


	private SOptrDao sOptrDao ;
	private SResourceDao sResourceDao;
	private SRoleDao sRoleDao;
	private SSubSystemDao sSubSystemDao;
	private SOptrRoleDao sOptrRoleDao;
	private SDataRightTypeDao sDataRightTypeDao;
	private QueryKeyValueDao queryKeyValueDao;
	private DataRoleInit dataControl;
	private QueryRepDao queryRepDao;

	
	public SOptr queryOptrByloginname(String loginname) throws ReportException{
		try {
			return queryRepDao.querySOptrByloginname(loginname);
		} catch (JDBCException e) {
			throw new ReportException(e,e.getSQL());
		}
	}
	
	public void setQueryRepDao(QueryRepDao queryRepDao) {
		this.queryRepDao = queryRepDao;
	}
	/**
	 * 报表数据权限初始化
	 * @param optr
	 * @param session
	 * @throws ReportException 
	 */
	public void initDataRole(SOptr optr,HttpSession session) throws ReportException{
		session.setAttribute(ReportConstants.SESSION_DATA_ROLE, dataControl.setDataRole(optr));
	}
	/**
	 * 获取数据权限限制内容
	 */
	protected String queryReportDataRightCon(String optrId,String dataRightType)throws ReportException {
		try {
			List<SRole> roleList = sRoleDao.queryByOptrId(optrId,dataRightType);
			for (SRole role : roleList) {
				if (StringHelper.isNotEmpty(role.getData_right_level()))
					return null;
				else if (StringHelper.isNotEmpty(role.getRule_str())) {
					return role.getRule_str().replaceAll("\"", "'");
				}
			}
			return null;
		} catch (JDBCException e) {
			throw new ReportException(e);
		}
	}
	
	public Map<String,RepDataRight> queryRepRoleDataMap(SOptr optr)throws Exception{
		Map<String,RepDataRight> map=new HashMap<String,RepDataRight>();
		
		for(String datarighttype:SystemConfig.getDataRightTypeList()){
			String sql=queryReportDataRightCon(optr.getOptr_id(),datarighttype);
			if(StringHelper.isNotEmpty(sql)){
				RepDataRight o=new RepDataRight();
				o.setKey(datarighttype);
				o.setSql(sql);
				SDataRightType dataRight = sDataRightTypeDao.findByKey(datarighttype);
				
				String datavaluesql="select "+dataRight.getResult_column()+","+dataRight.getSelect_column()
				+" from "+dataRight.getTable_name()+" where "+sql;
				
				String datavalue="";
				List<QueryKeyValue> list=queryKeyValueDao.findList(ReportConstants.DATABASE_SYSTEM, datavaluesql);
				for(int i=0;i<list.size();i++){
					QueryKeyValue value=list.get(i);
					datavalue=datavalue+(i==0?"":"','")+ value.getId();
				}
				o.setValue(datavalue);
				map.put(datarighttype, o);
				 
				
			}
		}
		
		return map;
	}
	
	/**
	 * 查询所有子系统定义信息
	 * @return
	 * @throws Exception
	 */
	public List<SSubSystem> queryAllSubSystem(SOptr optr) throws Exception {
		return sSubSystemDao.queryAllSubSystem(optr);
	}
	public List<SRoleDto> getSubSystemByOptrId(String optrId) throws Exception {
		List<SRoleDto> list = sOptrRoleDao.queryOptrRole(optrId);
		if (list.size()>0) {
			for (int i = list.size() - 1; i >= 0; i--) {
				boolean ck = false;
				if (StringHelper.isEmpty(list.get(i).getSub_system_id())) {
					ck = true;
				}
				if (ck) {
					list.remove(i);
				}
			}
		}else{
			throw new Exception("操作员的配置存在问题，角色中不存在子系统!");
		}
		return list;
	}
	
	/**
	 * 修改操作员密码
	 */
	public boolean updateOptrData(String optrId,String password,String subSystemId) throws Exception{
		SOptr soptr = new SOptr();
		soptr.setOptr_id(optrId);
		int sues = -1;
		if (StringHelper.isNotEmpty(password)) {
			soptr.setPassword(MD5.EncodePassword(password));
		}
		if (StringHelper.isNotEmpty(subSystemId)) {
			soptr.setLogin_sys_id(subSystemId);
		}
		sues = sOptrDao.update(soptr)[0];
		if (sues>=0 || sues == -2){
			return true;
		}
		return false ;
	}
	/**
	 *  检查指定的操作员是否存在
	 * @param optr
	 */
	public SOptr checkOptrExists(SOptr optr)throws Exception{
		if (null == optr) {
			return null;
		}
		SOptr _o = sOptrDao.isExists(optr.getLogin_name(), optr.getPassword());
		if (null == _o) {
			return null;
		}
		return _o;
	}

	/**
	 * 根据子系统id 返回对应的url，找不到返回空字符
	 * @param sysId
	 * @return
	 */
	public SSubSystem querySubSystem(String sysId) throws JDBCException {
		return sSubSystemDao.findByKey(sysId);
	}
	
	/**
	 * 取一个操作员的报表权限
	 * @param optr
	 * @return
	 * @throws ReportException
	 */
	public Integer queryRepRole(SOptr optr) throws ReportException{
		try {
			Integer rep_role = null;
			SRole srole = sRoleDao.queryRepByOptrId( optr.getOptr_id());
			if(optr.getLogin_name().equals("admin"))
				rep_role=0;
			else if(srole!=null)
				rep_role=SystemConfig.getRepDataLevel().get(srole.getData_right_level()).getItem_idx();
			else
				rep_role=4;
			return rep_role;
		} catch (JDBCException e) {
			throw new ReportException(e);
		}
	}
	/**
	 * 获取一个操作员对应system_key_map
	 * @param optr
	 * @return
	 */
	public Map<String, String> queryRepOptrSystemKeyMap(SOptr optr){
		Map<String, String> systemmap =  new HashMap<String, String>();
		systemmap.put("#webareaid#", optr.getArea_id());
		systemmap.put("#webcountyid#", optr.getCounty_id());
		systemmap.put("#webdeptid#", optr.getDept_id());
		systemmap.put("#weboptrid#", optr.getOptr_id());
		return systemmap;
	}
	
	/**
	 * 加载报表专用的资源菜单
	 * @param optr
	 * @param sub_system_id
	 * @return
	 * @throws Exception
	 */
	public List<RepResourceDto> loadRepTabResource(SOptr optr,String sub_system_id)throws Exception{
		List<RepResourceDto> list=queryRepDao.queryRepResources(optr,sub_system_id);
		if(BaseDataControl.getRole().hasFunc(FuncType.EDITREP))
			for(RepResourceDto dto:list)
				dto.setRes_name(dto.getRes_name()+"_"+dto.getSort_num());
		return list;
	}
	
	/**
	 * 加载资源菜单
	 * @throws Exception
	 */
	public List<SResourceDto> loadTabResource(SOptr optr,String sub_system_id)throws Exception{
		List<SResourceDto> list=sResourceDao.queryResourcesByOptr(optr,sub_system_id);
		if(BaseDataControl.getRole().hasFunc(FuncType.EDITREP))
			for(SResourceDto dto:list)
				dto.setRes_name(dto.getRes_name()+"_"+dto.getSort_num());
		return list;
	}
	
	public List<RepResourceDto> loadTabResourceID(SOptr optr,String sub_system_id)throws Exception{
		List<RepResourceDto> list=queryRepDao.queryRepResources(optr,sub_system_id);
		for(RepResourceDto dto:list)
			dto.setRes_name(dto.getRes_name()+'('+dto.getRes_id()+')');
		return list ;
	}
	
	public List<SResourceDto> queryResourcesByResType(String optrId,String subSystemId,String resType) throws Exception {
		return queryRepDao.queryResourcesByResType(optrId, subSystemId, resType);
	}

	public SOptrDao getSOptrDao() {
		return sOptrDao;
	}


	public void setSOptrDao(SOptrDao optrDao) {
		sOptrDao = optrDao;
	}

	public SResourceDao getSResourceDao() {
		return sResourceDao;
	}

	public void setSResourceDao(SResourceDao resourceDao) {
		sResourceDao = resourceDao;
	}
	public SRoleDao getSRoleDao() {
		return sRoleDao;
	}
	public void setSRoleDao(SRoleDao roleDao) {
		sRoleDao = roleDao;
	}

	public void setSSubSystemDao(SSubSystemDao subSystemDao) {
		sSubSystemDao = subSystemDao;
	}

	public void setQueryKeyValueDao(QueryKeyValueDao queryKeyValueDao) {
		this.queryKeyValueDao = queryKeyValueDao;
	}

	public void setSDataRightTypeDao(SDataRightTypeDao dataRightTypeDao) {
		sDataRightTypeDao = dataRightTypeDao;
	}

	public SOptrRoleDao getSOptrRoleDao() {
		return sOptrRoleDao;
	}

	public void setSOptrRoleDao(SOptrRoleDao optrRoleDao) {
		sOptrRoleDao = optrRoleDao;
	}

	public DataRoleInit getDataControl() {
		return dataControl;
	}

	public void setDataControl(DataRoleInit dataControl) {
		this.dataControl = dataControl;
	}

}
