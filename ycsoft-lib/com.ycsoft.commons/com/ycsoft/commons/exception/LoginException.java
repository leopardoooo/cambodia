package com.ycsoft.commons.exception;

public class LoginException extends Exception {
	/**
	 *
	 */
	private static final long serialVersionUID = -6700963888396375555L;

	public LoginException( String msg ){
		super(msg);
	}

	public LoginException( Exception e){
		super(e);
	}

	public LoginException( String msg ,Exception e){
		super( msg , e ) ;
	}
}
