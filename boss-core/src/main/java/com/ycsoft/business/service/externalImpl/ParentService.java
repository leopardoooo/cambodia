/**
 * 
 */
package com.ycsoft.business.service.externalImpl;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import com.ycsoft.business.commons.abstracts.BaseService;
import com.ycsoft.business.commons.pojo.BusiParameter;

/**
 * @author liujiaqi
 *
 */
public class ParentService implements BeanFactoryAware{

	private BeanFactory beanFactory;
	
	public void setBeanFactory(BeanFactory arg0) throws BeansException {
		this.beanFactory = arg0;
	}

	protected BaseService getBean(Class beanClass,BusiParameter p){
		BaseService service= (BaseService)beanFactory.getBean(beanClass);
		service.setParam(p);
		return service;
	}

}
