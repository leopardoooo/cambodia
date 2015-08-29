package com.ycsoft.business.commons.abstracts;

import com.ycsoft.beans.system.SOptr;
import com.ycsoft.business.commons.pojo.BusiParameter;
import com.ycsoft.business.commons.pojo.IParameter;
import com.ycsoft.business.commons.pojo.Parameter;
import com.ycsoft.business.component.core.DoneCodeComponent;
import com.ycsoft.business.dto.config.ExtAttrFormDto;
import com.ycsoft.commons.exception.ErrorCode;
import com.ycsoft.commons.exception.ServicesException;

/**
 *
 * <b>Service 基类定义</b>
 * <ul>
 * 	<li>提供系统通用的功能</li>
 * 	<li>用于以后更容易扩展。</li>
 * <ul>
 * @author hh
 * @date Dec 30, 2009 10:10:56 AM
 */
abstract public class BaseService implements IBaseService {

	private static ThreadLocal<Parameter> param = new ThreadLocal<Parameter>();

	@Override
	public void finalize() {
		param.remove();
	}
	
	protected DoneCodeComponent doneCodeComponent;

	public static SOptr getOptr() throws ServicesException{
		Parameter parameter = param.get();
		if (parameter==null)
			throw new ServicesException(ErrorCode.SystemNoOptr);
		return parameter.getOptr();
	}
	
	public void setParam( IParameter p){
		param.set((Parameter)p);
	}
	/**
	 * 获取参数对象，将该参数强制转换为<code>BusiParameter</code>
	 * 一般单个业务应当使用此函数
	 * @see BusiParameter
	 */
	protected BusiParameter getBusiParam(){
		if(null == param)
			return null;
		return (BusiParameter)param.get();
	}

	/**
	 * 设置扩展表必要的信息
	 * @param extTable
	 * @param pkColumn
	 * @param pkValue
	 */
	protected void setExtAttrInfo(String extTable , String pkColumn ,String pkValue){
		ExtAttrFormDto dto = getBusiParam().getExtAttrForm();
		if( dto == null)
			dto = new ExtAttrFormDto();
		dto.setExtendTable(extTable);
		dto.setPkColumn(pkColumn);
		dto.setPkValue(pkValue);

		getBusiParam().setExtAttrForm(dto);
	}

	public IParameter getParam() {
		return param.get();
	}

	public void setDoneCodeComponent(DoneCodeComponent doneCodeComponent) {
		this.doneCodeComponent = doneCodeComponent;
	}



}
