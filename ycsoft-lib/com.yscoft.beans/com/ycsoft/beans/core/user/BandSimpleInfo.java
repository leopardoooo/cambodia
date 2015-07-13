package com.ycsoft.beans.core.user;

public class BandSimpleInfo {
	private String check_type;
	private String login_name;
	private String login_password;
	private String bind_type;
	private Integer max_connection;
	private Integer max_user_num;
	private String service_name;
	private String old_password="";
	public void setCheck_type(String check_type) {
		this.check_type = check_type;
	}
	public void setLogin_name(String login_name) {
		this.login_name = login_name;
	}
	
	public Integer getMax_user_num() {
		return max_user_num;
	}
	public void setMax_user_num(Integer maxUserNum) {
		max_user_num = maxUserNum;
	}
	public void setBind_type(String bind_type) {
		this.bind_type = bind_type;
	}
	public void setMax_connection(Integer max_connection) {
		this.max_connection = max_connection;
	}
	public void setService_name(String service_name) {
		this.service_name = service_name;
	}
	public String getOld_password() {
		return old_password;
	}
	public void setOld_password(String old_password) {
		this.old_password = old_password;
	}
	public String getCheck_type() {
		return check_type;
	}
	public String getLogin_name() {
		return login_name;
	}
	public String getBind_type() {
		return bind_type;
	}
	public Integer getMax_connection() {
		return max_connection;
	}
	public String getService_name() {
		return service_name;
	}
	public String getLogin_password() {
		return login_password;
	}
	public void setLogin_password(String login_password) {
		this.login_password = login_password;
	}
	
	
}
