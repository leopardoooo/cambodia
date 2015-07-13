package com.ycsoft.report.web.commons;

import static com.ycsoft.commons.constants.Environment.USER_IN_SESSION_NAME;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.beans.system.SRole;
import com.ycsoft.business.dao.system.SRoleDao;
import com.ycsoft.commons.constants.Environment;
import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.commons.helper.JsonHelper;
import com.ycsoft.commons.helper.LoggerHelper;
import com.ycsoft.report.commons.ReportConstants;
import com.ycsoft.report.commons.SystemConfig;
import com.ycsoft.report.dto.RepDataRight;
import com.ycsoft.report.pojo.Parameter;
import com.ycsoft.report.query.datarole.BaseDataControl;
import com.ycsoft.report.query.datarole.DataRole;
import com.ycsoft.report.query.datarole.DataRoleInit;
import com.ycsoft.report.web.action.query.ReportAction;

/**
 * 报表拦截器,报表权限初始化，自定义json解析,报表执行控制
 */
public class ReportInterceptor extends AbstractInterceptor {

	private static Map<String, String> repexecmap = new HashMap<String, String>();
	private static final String[] execkeys = { "query/Report!initQuery",
			"query/Show!downloadExp" ,"query/Show!cube"};
	
	private SRoleDao sRoleDao;

	/**
	 * 获得客户端的IP
	 * 
	 * @param request
	 * @return
	 */
	public String getClientIP(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {

		String key = null;
		try {
			if (!(invocation.getAction() instanceof ReportAction)) {
				return invocation.invoke();
			}

			HttpServletRequest request = ServletActionContext.getRequest();
			ReportAction reportaction = (ReportAction) invocation.getAction();

			key = this.ExceControl(request, reportaction, invocation.getProxy()
					.getActionName());
			this.AnalysisJsonParam(request, reportaction);
			this.RoleInit(request, reportaction);

			return invocation.invoke();
		} catch (Exception e) {
			throw e;
		} finally {
			if (key != null)
				repexecmap.remove(key);
		}
	}

	/**
	 * 控制(一个操作员ID+IP) 只能执行一个报表初始化，或数据导出压缩,或cube变换
	 * proxyactionname访问的action服务
	 * @param request
	 * @param reportaction
	 * @param proxyactionname
	 * @return
	 * @throws Exception
	 */
	public String ExceControl(HttpServletRequest request,
			ReportAction reportaction, String proxyactionname) throws Exception {
		String key = null;
		// 客户端IP
		String ip = getClientIP(request);
		if(proxyactionname==null) return key;
		for (String execkey : execkeys) {
			// 符合需要控制的action调用
			if (execkey.equals(proxyactionname)||proxyactionname.indexOf(execkey)>-1) {
				key = reportaction.getOptr().getOptr_id() + "_" + ip;
				reportaction.setClient_ip(ip);
				LoggerHelper.debug(this.getClass(), key+"=>"+proxyactionname);
				if (repexecmap.containsKey(key)){
					
					throw new ReportException("请等待您上一个" + repexecmap.get(key)
							+ "后台执行完毕");
				}else{
					repexecmap.put(key, execkey.equals(execkeys[0]) ? "报表查询"
							: ( execkey.equals(execkeys[1])?"数据导出":"cube导航"));
				}

			}
		}
		return key;
	}

	/**
	 * 解析前台所提交的Json参数字符串。 将对应的参数字符串转换为Java Object。 并将解析的Object注入目标Action。
	 */
	public void AnalysisJsonParam(HttpServletRequest request,
			ReportAction repaction) throws Exception {
		// 获取客户等通用信息
		Object ps = request
				.getParameter(Environment.REQUEST_JSON_PARATEMER_STRING);
		if (null != ps) {
			LoggerHelper.debug(this.getClass()," 解析JSON=> "+ ps.toString());
			Parameter p = JsonHelper.toObject(ps.toString(), repaction
					.getParameter().getClass());
			repaction.setParameter(p);
		}
	}

	/**
	 * 报表权限初始化
	 * 
	 * @param request
	 * @param repaction
	 * @throws Exception
	 */
	public void RoleInit(HttpServletRequest request, ReportAction repaction)
			throws Exception {
		/**
		 * 权限装入从session中提取并装入thread
		 */
		DataRole role=(DataRole) request.getSession().getAttribute( ReportConstants.SESSION_DATA_ROLE);
		if(role!=null){
			BaseDataControl.setRole(role);
		}
	}

	public SRoleDao getSRoleDao() {
		return sRoleDao;
	}

	public void setSRoleDao(SRoleDao roleDao) {
		sRoleDao = roleDao;
	}
}
