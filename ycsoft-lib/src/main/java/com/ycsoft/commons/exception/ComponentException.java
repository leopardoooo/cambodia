package com.ycsoft.commons.exception;



/**
 * 所有的业务层如果需要捕捉异常，
 * 则必须向上抛出BusinessException类型异常。
 * @author <a href='mailTo:huanghui2004@hotmail.com'>hh</a>
 */
public class ComponentException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = 5888503597834560573L;

	public ComponentException(String msg){
		super(msg);
	}

	public ComponentException(Exception e){
		super(e);
	}

	public ComponentException( String msg ,Exception e){
		super( msg , e );
	}

}