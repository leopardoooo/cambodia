/*
 * @(#) ResultBody.java 1.0.0 Aug 12, 2015 6:12:13 PM
 */
package com.ycsoft.web.action.external;

import com.ycsoft.commons.exception.ErrorCode;
import com.ycsoft.commons.exception.ServicesException;

/**
 * 移动端接口返回值统一定义
 * 
 * @author Killer
 */
public class ResultBody {

	/**
	 * 取值0：正常执行，1：异常返回
	 */
	private Err err;
	private String status; 
	private String reason = "";
	
	private Object msg = new Object();

	private ResultBody() {
		super();
	}
	
	/**
	 * 构造一个业务办理成功返回的数据结构
	 * @param msg 返回的数据体
	 */
	public static ResultBody createWithMsg(Object msg) {
		ResultBody rb = new ResultBody();
		rb.err = Err.SUCCESS;
		rb.status = rb.err.getStatus();
		rb.reason="成功";
		rb.msg = msg;
		
		return rb;
	}
	
	/**
	 * 构造一个业务办理失败返回的数据结构，
	 * @param reason 错误原因
	 */
	public static ResultBody createWithException(ServicesException ex) {
		ResultBody rb = new ResultBody();
		rb.err = Err.FAILURE;
		ErrorCode ec=ex.getErrorCode();
		rb.status = ec.getOttStatusCode();
		rb.reason = ec.getDesc();
		return rb;
	}
	/**
	 * @see #ResultBody(ServicesException)
	 * 返回一个业务失败的数据结构，并且附带一些消息体
	 */
	public static ResultBody createWithExceptionAndMsg(ServicesException ex, Object msg) {
		ResultBody rb = createWithException(ex);
		rb.msg = msg;
		return rb;
	}

	public String getErr() {
		return err.value();
	}

	public void setErr(Err err) {
		this.err = err;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Object getMsg() {
		return msg;
	}

	public void setMsg(Object msg) {
		this.msg = msg;
	}
	
	
	public enum Err{
		SUCCESS("0","20000"), FAILURE("1","20003");
		String value;
		String status;
		
		private Err(String intValue, String status){
			this.value = intValue;
			this.status = status;
		}
		public String value(){
			return this.value;
		}
		public String getStatus(){
			return this.status;
		}
	}
	
}
