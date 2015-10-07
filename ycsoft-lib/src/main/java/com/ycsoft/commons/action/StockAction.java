package com.ycsoft.commons.action;

import static com.ycsoft.commons.constants.Environment.CONTEXT_PATH;
import static com.ycsoft.commons.constants.Environment.SSO_TOKEN_PARAM_KEY;
import static com.ycsoft.commons.constants.Environment.USER_IN_SESSION_LANG;
import static com.ycsoft.commons.constants.Environment.USER_IN_SESSION_NAME;

import java.util.ArrayList;
import java.util.List;

import com.ycsoft.beans.system.SItemvalue;
import com.ycsoft.commons.abstracts.BaseAction;
import com.ycsoft.commons.constants.Environment;
import com.ycsoft.commons.helper.LoggerHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.helper.WebHelper;
import com.ycsoft.commons.store.MemoryDict;

/**
 * 
 * 定义系统一些常用的处理函数， 以下所定义的处理函数，对所有子系统都可用的
 * 
 * @author hh
 * @date Feb 8, 2010 7:10:35 PM
 */
public class StockAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2534855421124865468L;

	private String sub_system_id;
	private String url;
	
	private String changeDept;

	// combo 需要的参数名称
	private String[] comboParamNames;

	/**
	 * 将地址转向所传递的URL。
	 * @return
	 * @throws Exception
	 */
	@Override
	public String execute() throws Exception {
		return "to";
	}

	/**
	 * 切换子系统
	 * @throws Exception
	 */
	public String rego() throws Exception {
		String jsonOptr = null;
		//获取ssoid
		String ssoSid = WebHelper.getCookieValue(request, SSO_TOKEN_PARAM_KEY);
		if (StringHelper.isEmpty(ssoSid))
			ssoSid = request.getParameter(SSO_TOKEN_PARAM_KEY);

		if (StringHelper.isNotEmpty(ssoSid)) {
			jsonOptr = SsoUnit.checkSso(ssoSid, getSession().getId(), request
					.getContextPath());
		}
		
		if (StringHelper.isEmpty(jsonOptr)) {
			LoggerHelper.debug(getClass(), "在切换系统中检查到没有登录或session已经失效!"
					+ request.getRemoteHost());
			return "login";
		}
		
		if(changeDept == null){
			//操作员信息放入本地Session,如果是切换营业厅不需要设置信息
			getSession().setAttribute(USER_IN_SESSION_NAME, jsonOptr);
			getSession().setAttribute(SSO_TOKEN_PARAM_KEY, ssoSid);
			// 设置语言至当前session中
			Object lang = com.alibaba.fastjson.JSON.parseObject(jsonOptr).get("lang");
			getSession().setAttribute(USER_IN_SESSION_LANG, lang);
			getSession().setAttribute(CONTEXT_PATH, request.getContextPath());
		}

//		if(Environment.ROOT_PATH_BOSS_CORE.equals(request.getContextPath())){
//			url = "/views/index/index.jsp?" + SSO_TOKEN_PARAM_KEY + "=" + ssoSid;
//		}else{
			url = "/pages/index/index.jsp?" + SSO_TOKEN_PARAM_KEY + "=" + ssoSid;
//		}
		return "to";
	}

	public String gologin() throws Exception {
		String ssoSid = (String) request.getSession().getAttribute(
				Environment.SSO_TOKEN_PARAM_KEY);
		getSession().setAttribute(USER_IN_SESSION_NAME, null);
		SsoUnit.loginOut(ssoSid);
		url = SessionListener.getSsoLoginUrl(request);
		return "to";
	}
	
	/**
	 * 增加在线用户的操作记录
	 * @return
	 * @throws Exception
	 */
	public String addSession() throws Exception {
		String ssoSid = (String) request.getSession().getAttribute(
				Environment.SSO_TOKEN_PARAM_KEY);
		String resName= request.getParameter("resName");
		SsoUnit.addResoucreInfo(ssoSid , resName);
		getRoot().setSimpleObj(resName);
		return JSON_SIMPLEOBJ;
	}
	
	/**
	 * 查询系统参数
	 * 
	 * @return
	 * @throws Exception
	 */
	public String querySysParam() throws Exception {
		if (comboParamNames == null) {
			return JSON_RECORDS;
		}
		
		Object langObj = request.getSession().getAttribute(Environment.USER_IN_SESSION_LANG);
		if(langObj != null && !langObj.toString().equals(MemoryDict.getLang())){
			MemoryDict.setLang(langObj.toString());
		}
		
		List<List<SItemvalue>> lst = new ArrayList<List<SItemvalue>>();
		for (String element : comboParamNames) {
			lst.add(MemoryDict.getDicts(element));
		}
		getRoot().setRecords(lst);
		return JSON_RECORDS;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String[] getComboParamNames() {
		return comboParamNames;
	}

	public void setComboParamNames(String[] comboParamNames) {
		this.comboParamNames = comboParamNames;
	}

	/**
	 * @return the sub_system_id
	 */
	public String getSub_system_id() {
		return sub_system_id;
	}

	/**
	 * @param sub_system_id
	 *            the sub_system_id to set
	 */
	public void setSub_system_id(String sub_system_id) {
		this.sub_system_id = sub_system_id;
	}

	/**
	 * @return the changeDept
	 */
	public String getChangeDept() {
		return changeDept;
	}

	/**
	 * @param changeDept the changeDept to set
	 */
	public void setChangeDept(String changeDept) {
		this.changeDept = changeDept;
	}



}
