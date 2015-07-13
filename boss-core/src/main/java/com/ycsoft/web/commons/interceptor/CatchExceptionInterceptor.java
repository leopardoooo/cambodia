/**
 * action异常拦截器
 */
package com.ycsoft.web.commons.interceptor;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.ycsoft.commons.abstracts.BaseAction;
import com.ycsoft.commons.exception.ExceptionMessage;
import com.ycsoft.commons.helper.LoggerHelper;
import com.ycsoft.commons.pojo.ExceptionData;
import com.ycsoft.commons.pojo.Root;

/**
 * @author danjp
 *
 */
public class CatchExceptionInterceptor extends AbstractInterceptor {

	private static final long serialVersionUID = 1L;

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		String result = "json";
		try {
			result = invocation.invoke();
		} catch (Exception e) {
			LoggerHelper.error(invocation.getAction().getClass(), e.getMessage());

			ExceptionData msg = ExceptionMessage.getMsgFromException(e);
			BaseAction ba = (BaseAction)invocation.getAction();
			Root root = ba.getRoot();
			root.setException(msg);
			root.setSuccess(false);
			e.printStackTrace();
		}
		return result;
	}
}
