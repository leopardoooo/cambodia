package com.ycsoft.web.commons.abstracts;

import com.ycsoft.business.commons.pojo.BusiParameter;
import com.ycsoft.business.commons.pojo.Parameter;
import com.ycsoft.commons.abstracts.BaseAction;

/**
 * <p>业务<code>Action</code>的基类，
 * 包含操作员等大部分业务模块<code>Action</code>共有的信息。</p>
 *
 * @author hh
 */
public class BaseBusiAction extends BaseAction{

	/**
	 *
	 */
	private static final long serialVersionUID = -4938621617822110741L;
	// 封装好的参数Object
	private Parameter parameter;

	/**
	 * 构造函数初始化信息
	 */
	public BaseBusiAction(){
		parameter = new BusiParameter();
	}
	protected BusiParameter getBusiParam(){
		if(null == parameter)
			return null;
		return (BusiParameter)parameter;
	}
	public Parameter getParameter() {
		return parameter;
	}

	public void setParameter(Parameter parameter) {
		this.parameter = parameter;
	}
}
