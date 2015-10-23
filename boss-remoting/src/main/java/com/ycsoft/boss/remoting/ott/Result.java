package com.ycsoft.boss.remoting.ott;

public class Result {
	public static String UNDEFINED_ERROR_STATUS="10000";
	public static String BOSS_ERROR_STATUS="-1";
	private String err;
	private String status;
	private String reason;
	public String getErr() {
		return err;
	}
	public void setErr(String err) {
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
	
	public boolean isSuccess(){
		return "0".equals(this.status);
	}
	
	public boolean isUndefinedError(){
		return UNDEFINED_ERROR_STATUS.equals(status);
	}
	
	public boolean isBossError(){
		return BOSS_ERROR_STATUS.equals(status);
	}
	
	public boolean isConnectionError(){
		reason = reason.toLowerCase();
		if (reason.indexOf("ioexception")>-1 || (
				reason.indexOf("connect")>-1 && reason.indexOf("timeout")>-1)){
			return true;
		} 
		
		return false;
		
	}
	
	

}
