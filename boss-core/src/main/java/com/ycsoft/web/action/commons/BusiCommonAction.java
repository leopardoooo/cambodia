package com.ycsoft.web.action.commons;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.ycsoft.business.component.config.BusiConfigComponent;
import com.ycsoft.business.service.IQueryCfgService;
import com.ycsoft.commons.exception.ActionException;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.web.commons.abstracts.BaseBusiAction;

/**
 * 系统颗心模块的通用处理器， 包含如：杂费查询、打印初始化等等。
 *
 * @author hh
 * @date Feb 8, 2010 7:10:35 PM
 */
public class BusiCommonAction extends BaseBusiAction {

	/**
	 *
	 */
	private static final long serialVersionUID = 5959768227655717134L;
	private IQueryCfgService queryCfgService;

	private String acctDate;
	private String optrId;
	private String addrIds;

	/**
	 * 查询所有子系统定义
	 * @return
	 * @throws Exception
	 */
	public String queryAllSubSystem() throws Exception {
		getRoot().setRecords(queryCfgService.queryAllSubSystem(optr));
		return JSON_RECORDS;
	}

	/**
	 * 获取扩展配置表信息
	 * @return
	 * @throws Exception
	 */
	public String extAttrForm()throws Exception{
		String tabName = request.getParameter("tabName");
		String groupId = request.getParameter("group");
		if(StringHelper.isEmpty(tabName)){
			throw new ActionException("获取扩展信息时，表名不能为空!");
		}
		getRoot().setRecords(queryCfgService.extAttrForm(groupId,tabName));
		return JSON_RECORDS;
	}

	/**
	 * 显示扩展信息
	 * @return
	 * @throws Exception
	 */
	public String extAttrView()throws Exception{
		String tabName = request.getParameter("tabName");
		String pkValue = request.getParameter("pkValue");
		String groupId = request.getParameter("group");
		if(StringHelper.isEmpty(tabName) || StringHelper.isEmpty(pkValue)){
			throw new ActionException("获取扩展信息时，表名或主键不能为空!");
		}
		getRoot().setRecords(queryCfgService.extAttrView(groupId,tabName,pkValue));
		return JSON_RECORDS;
	}


	/**
	 * 轧帐日期
	 * @return
	 * @throws Exception
	 */
	public String acctDate() throws Exception{
		getRoot().setSimpleObj(queryCfgService.queryAcctDate());
		return JSON_SIMPLEOBJ;
	}
	
	/**
	 * 终端数扎帐
	 * @return
	 * @throws Exception
	 */
	public String checkDeviceCount() throws Exception{
		String deptId= request.getParameter("deptId");
		queryCfgService.checkDeviceCount(acctDate,optrId,deptId);
		return JSON;
	}
	
	public String queryRecordByDeptId() throws Exception {
		getRoot().setRecords(queryCfgService.queryRecordByDeptId());
		return JSON_RECORDS;
	}
	
	/**
	 * 用户数扎帐
	 * @return
	 * @throws Exception
	 */
	public String checkUserCount() throws Exception{
		queryCfgService.checkUserCount(acctDate,addrIds);
		return JSON;
	}

	/**
	 * 轧帐模式
	 * @return
	 * @throws Exception
	 */
	public String queryGripAccountMode() throws Exception{
		getRoot().setSimpleObj(queryCfgService.queryGripAccountMode());
		return JSON_SIMPLEOBJ;
	}

	/**
	 * 保存轧帐日期
	 * @return
	 * @throws Exception
	 */
	public String modifyAcctDate() throws Exception{
		Date date = new SimpleDateFormat("yyyy-MM-dd").parse(acctDate);
		queryCfgService.modifyAcctDate(date);
		return JSON;
	}

	public void setQueryCfgService(IQueryCfgService queryCfgService) {
		this.queryCfgService = queryCfgService;
	}

	/**
	 * @param acctDate the acctDate to set
	 */
	public void setAcctDate(String acctDate) {
		this.acctDate = acctDate;
	}

	public void setOptrId(String optrId) {
		this.optrId = optrId;
	}

	public void setAddrIds(String addrIds) {
		this.addrIds = addrIds;
	}

}
