package com.ycsoft.boss.remoting.ott;

public class Result {
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
		return "0".equals(this.err);
	}
	
	public boolean isUndefinedError(){
		return status.equals(OttClient.UNDEFINED_ERROR_STATUS);
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
