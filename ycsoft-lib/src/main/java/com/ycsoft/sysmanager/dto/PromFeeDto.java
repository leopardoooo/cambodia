package com.ycsoft.sysmanager.dto;

import java.util.List;

import com.ycsoft.beans.prod.PPromFee;
import com.ycsoft.beans.prod.PPromFeeCounty;
import com.ycsoft.beans.prod.PPromFeeDivision;
import com.ycsoft.beans.prod.PPromFeeProd;
import com.ycsoft.beans.prod.PPromFeeUser;

public class PromFeeDto {
	private String promFeeId;
	private String promFeeName;
	private PPromFee promFee;
	private List<PPromFeeCounty> county;
	private List<PPromFeeUser> users;
	private List<PPromFeeDivision> divisions;
	private List<PPromFeeProd> prods;
	
	public String getPromFeeId() {
		return promFeeId;
	}
	public void setPromFeeId(String promFeeId) {
		this.promFeeId = promFeeId;
	}
	public PPromFee getPromFee() {
		return promFee;
	}
	public void setPromFee(PPromFee promFee) {
		this.promFee = promFee;
	}
	public List<PPromFeeCounty> getCounty() {
		return county;
	}
	public void setCounty(List<PPromFeeCounty> county) {
		this.county = county;
	}
	public List<PPromFeeUser> getUsers() {
		return users;
	}
	public void setUsers(List<PPromFeeUser> users) {
		this.users = users;
	}
	public List<PPromFeeDivision> getDivisions() {
		return divisions;
	}
	public void setDivisions(List<PPromFeeDivision> divisions) {
		this.divisions = divisions;
	}
	public List<PPromFeeProd> getProds() {
		return prods;
	}
	public void setProds(List<PPromFeeProd> prods) {
		this.prods = prods;
	}
	public String getPromFeeName() {
		return promFeeName;
	}
	public void setPromFeeName(String promFeeName) {
		this.promFeeName = promFeeName;
	}
}
