package com.ycsoft.beans.core.prod;

//批量销户实体类
public class CancelUserDto {

	private String user_id;
	private String prod_name;
	private Integer active_fee;
	
	private String info;	//提交json串

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getProd_name() {
		return prod_name;
	}

	public void setProd_name(String prod_name) {
		this.prod_name = prod_name;
	}

	public Integer getActive_fee() {
		return active_fee;
	}

	public void setActive_fee(Integer active_fee) {
		this.active_fee = active_fee;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
	
}
