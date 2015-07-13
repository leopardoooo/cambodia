package com.ycsoft.business.commons.interceptor;
import static com.ycsoft.commons.helper.LoggerHelper.debug;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.springframework.aop.MethodBeforeAdvice;

import com.ycsoft.beans.system.SOptr;
import com.ycsoft.business.commons.abstracts.BaseService;
import com.ycsoft.business.commons.pojo.Parameter;
import com.ycsoft.commons.constants.Environment;
import com.ycsoft.commons.helper.JsonHelper;

/**
 * 使用拦截器设置 <code>Parameter</code> 等信息,
 * 仅拦截<code>Service</code>层的类。
 *
 * @author hh
 * @date Jan 4, 2010 4:09:49 PM
 */
public class SetterParameterInterceptor implements MethodBeforeAdvice {

	/**
	 * 从<code>HttpServletRequest</code>中获取操作员的信息,
	 * 并注入到<code>Service</code>目标类中
	 */
	public void before(@SuppressWarnings("unused")
	Method method, @SuppressWarnings("unused")
	Object[] params, Object scope) throws Throwable {
		if(!(scope instanceof BaseService)){
			throw new Exception("该拦截器只适用于Service层...");
		}
		try{
			HttpServletRequest request = ServletActionContext.getRequest();
			if (request != null) {
				//设置参数信息(web模式，在form提交时要求上传)，
				BaseService base = (BaseService)scope ;
				
				Object o = request.getAttribute( Environment.BUSIPARAM_INTERCEPT_KEY );
				Parameter param = (Parameter) o;
				if( null == o)
					param = new Parameter();
				
				//从session中获取操作员信息
				Object optr = request.getSession().getAttribute(Environment.USER_IN_SESSION_NAME);
				if(optr == null)
					throw new Exception("操作员信息无法获取，系统错误");
			
				//debug(getClass(), base.toString()+"设置Service操作员：" + optr.toString());
				param.setOptr(JsonHelper.toObject( optr.toString(), SOptr.class));
				
				if(param.getPay() != null){
					request.getSession().setAttribute(Environment.CURRENT_BUSI_OPTR_ID,param.getPay().getBusi_optr_id());
				}
				
				base.setParam(param);
			}
			
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
}
