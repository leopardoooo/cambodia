package com.ycsoft.web.commons.interceptor;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.business.commons.pojo.Parameter;
import com.ycsoft.commons.constants.Environment;
import com.ycsoft.commons.exception.ServicesException;
import com.ycsoft.commons.helper.JsonHelper;
import com.ycsoft.commons.helper.LoggerHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.web.commons.abstracts.BaseBusiAction;

/**
 * 该拦截器用于解析前台所提交的Json参数字符串。
 * 将对应的参数字符串转换为Java Object。
 * 并将解析的Object注入目标Action。
 *
 * @author hh
 * @date Mar 9, 2010 6:23:48 PM
 */
public class JsonParamInterceptor extends AbstractInterceptor{

	/**
	 *
	 */
	private static final long serialVersionUID = 1653745357462662878L;
	protected transient Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		if(!(invocation.getAction() instanceof BaseBusiAction)){
			return invocation.invoke();
		}

		BaseBusiAction bba = (BaseBusiAction)invocation.getAction();
		//获取客户等通用信息
		Object ps = request.getParameter( Environment.REQUEST_JSON_PARATEMER_STRING );
		if( null != ps ){
			process( bba , ps.toString() );
		}

		//将参数设置到request中，为Service的拦截器所使用
		request.setAttribute( Environment.BUSIPARAM_INTERCEPT_KEY , bba.getParameter());
		return invocation.invoke();
	}

	/**
	 * 对请求参数进行解析。并注入Action
	 * @param target 目标Action
	 * @param json 请求参数JSON字符串
	 */
	private void process(BaseBusiAction target , String json)throws Exception{
		Parameter p = JsonHelper.toObject(json, target.getParameter().getClass());
		//if("1040".equals(p.getBusiCode())){
			//cfee缴费业务记录日志，用于串数据分析
			//LoggerHelper.info("CFEE", JsonParamInterceptor.class,json);
		//}
		
		target.setParameter( p );

	}
}
