package com.ycsoft.commons.exception;


/**
 * DaoException是自定义Dao函数所抛出的异常。
 * 与JDBCException异常不一样。
 * JDBCException 是后台Daos库所抛出的异常。
 * @author <a href='mailTo:huanghui2004@hotmail.com'>hh</a>
 */
public class DaoException extends Exception{


	/**
	 *
	 */
	private static final long serialVersionUID = -4167136038092897677L;

	public DaoException(String msg){
		super(msg);
	}

	public DaoException(Exception e){
		super(e);
	}

	public DaoException( String msg ,Exception e){
		super( msg , e );
	}
}
