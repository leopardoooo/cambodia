package com.ycsoft.commons.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * 报表内部逻辑错误
 * @author lxr
 *
 */
public class ReportException extends Exception {
	/**
	 *
	 */
	private static final long serialVersionUID = -761870458775302435L;

	private String sql="";
	
	private boolean prompt_sign=false;//错误显示级别 true表示只在前台提示
	
	public ReportException( String msg ){
		super(msg);
		prompt_sign=true;
	}

	public ReportException( String msg,String sql ){
		super(msg);
		this.sql=sql;
		prompt_sign=true;
	}

	public ReportException( String msg,Exception e,String sql ){
		super(msg,e);
		this.sql=sql;
	}

	public ReportException(Exception e){
		super(e.getMessage(),e);
	}

	public ReportException(Exception e,String sql){
		super(e.getMessage(),e);
		this.sql=sql;
	}

	public ReportException( String msg ,Exception e){
		super( msg , e ) ;
	}

	/**
	 * 重定义错误打印堆栈
	 * 输出底层错误堆栈
	 */
	@Override
	public void printStackTrace(){
		this.printStackTrace(System.err);
	}

	/**
	 * 重定义错误打印堆栈
	 * 输出底层错误堆栈
	 */
	@Override
	public void printStackTrace(PrintStream s){
		Throwable e=getCause();
		String message=this.getClass().getName()+":"+getMessage();
		if(e!=null)
			message=message+"\n"+e.getClass().getName()+":"+e.getMessage();
		while(e!=null&&e.getCause()!=null){
			e=e.getCause();
			message=message+"\n"+e.getClass().getName()+":"+e.getMessage();
		}
		if(e==null)
			e=this;
		synchronized (s) {
			s.println(message);
			if(sql!=null&&!sql.trim().equals("")){
				s.println();
				s.println(sql);
			}
            StackTraceElement[] trace = e.getStackTrace();
            for (StackTraceElement element : trace)
				s.println("\tat " + element);
        }
	}
	
	/**
	 * 重定义错误打印堆栈
	 * 输出底层错误堆栈
	 */
	@Override
	public void printStackTrace(PrintWriter s){
		Throwable e=getCause();
		String message=this.getClass().getName()+":"+getMessage();
		if(e!=null)
			message=message+"\n"+e.getClass().getName()+":"+e.getMessage();
		while(e!=null&&e.getCause()!=null){
			e=e.getCause();
			message=message+"\n"+e.getClass().getName()+":"+e.getMessage();
		}
		if(e==null)
			e=this;
		synchronized (s) {
			s.println(message);
			if(sql!=null&&!sql.trim().equals("")){
				s.println();
				s.println(sql);
			}
				
            StackTraceElement[] trace = e.getStackTrace();
            for (StackTraceElement element : trace)
				s.println("\tat " + element);
        }
	}

	public String getSql() {
		return sql;
	}

	public boolean isPrompt_sign() {
		return prompt_sign;
	}
}
