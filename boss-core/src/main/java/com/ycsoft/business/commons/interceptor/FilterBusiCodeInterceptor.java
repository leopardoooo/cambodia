package com.ycsoft.business.commons.interceptor;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.springframework.aop.MethodBeforeAdvice;

import com.ycsoft.beans.system.SItemvalue;
import com.ycsoft.business.commons.abstracts.BaseService;
import com.ycsoft.business.commons.pojo.Parameter;
import com.ycsoft.business.component.system.IndexComponent;
import com.ycsoft.business.service.impl.OttExternalService;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.exception.ServicesException;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.store.MemoryDict;

/**
 * 使用拦截器设置过滤某些小区不能操作的业务.
 * 仅拦截<code>Service</code>层的类。
 */
public class FilterBusiCodeInterceptor implements MethodBeforeAdvice {
	private IndexComponent indexComponent;

	/**
	 * 从<code>HttpServletRequest</code>中获取操作员的信息,
	 * 并注入到<code>Service</code>目标类中
	 */
	public void before(@SuppressWarnings("unused")
	Method method, @SuppressWarnings("unused")
	Object[] params, Object scope) throws Throwable {
		if(!(scope instanceof BaseService)){
			throw new Exception("该拦截器只适用于Service层...1");
		}
		if(scope instanceof  OttExternalService ){
			//接口不拦截
			return ;
		}
		
		HttpServletRequest request = null;
		try {
			request = ServletActionContext.getRequest();
		}catch (Exception e) {
		}
			
		if (request != null) {
			//设置参数信息(web模式，在form提交时要求上传)，
			BaseService base = (BaseService)scope ;
			Parameter param = (Parameter)base.getParam();
			String busiCode = param.getBusiCode();
			if(StringHelper.isEmpty(busiCode)){//ps.action 等不是业务请求的时候,直接返回
				return;
			}
			String dept_id = base.getOptr().getDept_id();
			String value = dept_id + "_" + busiCode;
			SItemvalue dictItem = MemoryDict.getDictItem(DictKey.DEPT_BUSICODE, value);
			if(dictItem != null){
				String [] array = dictItem.getItem_name().split("_");
				throw new ServicesException("当前部门 '" + array[0] +"' 无法进行 ['" + array[1] +"'] 业务!");
			}
		}
			
	}

	public void setIndexComponent(IndexComponent indexComponent) {
		this.indexComponent = indexComponent;
	}
}
