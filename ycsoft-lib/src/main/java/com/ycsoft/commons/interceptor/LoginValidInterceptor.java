package com.ycsoft.commons.interceptor;

import static com.ycsoft.commons.constants.Environment.CONTEXT_PATH;
import static com.ycsoft.commons.constants.Environment.SSO_TOKEN_PARAM_KEY;
import static com.ycsoft.commons.constants.Environment.TOGGLE_SYSTEM_PROCESS_METHOD;
import static com.ycsoft.commons.constants.Environment.USER_IN_SESSION_NAME;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.commons.abstracts.BaseAction;
import com.ycsoft.commons.action.SsoUnit;
import com.ycsoft.commons.helper.JsonHelper;
import com.ycsoft.commons.helper.WebHelper;
import com.ycsoft.commons.pojo.ExceptionData;
import com.ycsoft.commons.pojo.Root;

/**
 * <p>验证请求是否已经登陆成功。</p>
 * 简单的验证当前的请求<code>Session</code>中是否存在操作员信息
 *
 * @author <a href='mailTo:huanghui2004@hotmail.com'>hh</a>
 */
public class LoginValidInterceptor extends AbstractInterceptor {


	/**
	 *
	 */
	private static final long serialVersionUID = -7594309987971245981L;

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {

		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession(true);
		String ssoSid = WebHelper.getCookieValue(request, SSO_TOKEN_PARAM_KEY);
		String actionName = invocation.getProxy().getActionName();
		
		if(actionName.startsWith(TOGGLE_SYSTEM_PROCESS_METHOD)
			|| actionName.startsWith("gologin")){
		}else{
			Object action = invocation.getAction();
			if( action instanceof BaseAction){
				Object user = session.getAttribute( USER_IN_SESSION_NAME);
				if (null == user) {
					String jsonOptr = SsoUnit.checkSso(ssoSid, session.getId(),
							request.getContextPath());
					session.setAttribute(USER_IN_SESSION_NAME, jsonOptr);
					session.setAttribute(SSO_TOKEN_PARAM_KEY, ssoSid);
					session.setAttribute(CONTEXT_PATH, request.getContextPath());
				}
				
				if(null == user) {
					ExceptionData msg = new ExceptionData();
					msg.setType(ExceptionData.LOGIN_EXCEPTION);
					msg.setTitle("错误");
					msg.setContent("操作员信息丢失，请重新登录！");
					
					BaseAction ba = (BaseAction)action;
					Root root = ba.getRoot();
					root.setException(msg);
					root.setSuccess(false);
					return "json";
				}else{
					BaseAction ba = (BaseAction)invocation.getAction();
					SOptr optr = JsonHelper.toObject(user.toString(), SOptr.class);
					ba.setOptr(optr);
				}
			}
		}
		// invoke next interceptor or action
		
		return invocation.invoke();
	}

}
