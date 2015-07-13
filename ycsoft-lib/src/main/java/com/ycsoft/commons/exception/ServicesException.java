package com.ycsoft.commons.exception;


/**
 * 所有服务层的类，如果需要捕捉异常。则必须向上抛出该类型的异常。
 * @author <a href='mailTo:huanghui2004@hotmail.com'>hh</a>
 */
public class ServicesException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = 2758171495592228753L;

	public ServicesException( String msg ){
		super(msg);
	}

	public ServicesException( Exception e){
		super(e);
	}

	public ServicesException( String msg ,Exception e){
		super( msg , e ) ;
	}
}