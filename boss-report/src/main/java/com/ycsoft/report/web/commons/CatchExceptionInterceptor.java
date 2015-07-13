/**
 * action异常拦截器
 */
package com.ycsoft.report.web.commons;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.ycsoft.commons.abstracts.BaseAction;
import com.ycsoft.commons.exception.ExceptionMessage;
import com.ycsoft.commons.helper.LoggerHelper;
import com.ycsoft.commons.pojo.ExceptionData;
import com.ycsoft.report.commons.SystemConfig;

/**
 * @author danjp
 *
 */
public class CatchExceptionInterceptor extends AbstractInterceptor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see com.opensymphony.xwork2.interceptor.AbstractInterceptor#intercept(com.opensymphony.xwork2.ActionInvocation)
	 */
	public String intercept(ActionInvocation invocation) throws Exception {
		String result = "json";
		try {
			result = invocation.invoke();
		} catch (Exception e) {
			LoggerHelper.error(invocation.getAction().getClass(), e.getMessage(),e);
			
			ExceptionData msg = ExceptionMessage.getMsgFromException(e);
			BaseAction ba = (BaseAction)invocation.getAction();
			//无修改报表权限用户，无法看到异常详细信息
			if(!ba.getOptr().getLogin_name().equals("admin")&&!SystemConfig.getOptrConfigRepMap().containsKey(ba.getOptr().getOptr_id()))
				msg.setDetail("");
			ba.getRoot().setException(msg);
			ba.getRoot().setSuccess(false);
			//e.printStackTrace();
		}
		return result;
	}
}
