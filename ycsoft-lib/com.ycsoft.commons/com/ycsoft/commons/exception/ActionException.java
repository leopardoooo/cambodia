package com.ycsoft.commons.exception;

/**
 * 控制器异常定义，所有控制器需要抛出的异常则抛出此类型的异常。
 * @author <a href='mailTo:huanghui2004@hotmail.com'>hh</a>
 */
public class ActionException extends ServicesException{

	/**
	 *
	 */
	private static final long serialVersionUID = -4625746430487252538L;
	private ServicesException servicesException;

	public ActionException( String msg ){
		super( msg );
	}

	public ActionException( String msg , ServicesException servicesException ){
		super( msg , servicesException ) ;
	}

	public ServicesException getServicesException() {
		return servicesException;
	}

	public void setServicesException(ServicesException servicesException) {
		this.servicesException = servicesException;
	}
}
