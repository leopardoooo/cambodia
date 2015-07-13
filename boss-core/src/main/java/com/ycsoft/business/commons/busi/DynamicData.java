/**
 *
 */
package com.ycsoft.business.commons.busi;

import com.ycsoft.beans.core.cust.CCust;
import com.ycsoft.beans.core.fee.CFee;
import com.ycsoft.business.dto.core.user.UserDto;

/**
 * @author liujiaqi
 *
 */
public class DynamicData {
	private String busicode;

	private String userid;
	private UserDto user;

	private String custid;
	private CCust cust;

	private String feeSn;
	private CFee fee;


	/**
	 * @return the userid
	 */
	public String getUserid() {
		return userid;
	}
	/**
	 * @param userid the userid to set
	 */
	public void setUserid(String userid) {
		this.userid = userid;
	}
	/**
	 * @return the user
	 */
	public UserDto getUser() {
		return user;
	}
	/**
	 * @param user the user to set
	 */
	public void setUser(UserDto user) {
		this.user = user;
	}
	/**
	 * @return the busicode
	 */
	public String getBusicode() {
		return busicode;
	}
	/**
	 * @param busicode the busicode to set
	 */
	public void setBusicode(String busicode) {
		this.busicode = busicode;
	}
	/**
	 * @return the custid
	 */
	public String getCustid() {
		return custid;
	}
	/**
	 * @param custid the custid to set
	 */
	public void setCustid(String custid) {
		this.custid = custid;
	}
	/**
	 * @return the cust
	 */
	public CCust getCust() {
		return cust;
	}
	/**
	 * @param cust the cust to set
	 */
	public void setCust(CCust cust) {
		this.cust = cust;
	}
	/**
	 * @return the feeSn
	 */
	public String getFeeSn() {
		return feeSn;
	}
	/**
	 * @param feeSn the feeSn to set
	 */
	public void setFeeSn(String feeSn) {
		this.feeSn = feeSn;
	}
	/**
	 * @return the fee
	 */
	public CFee getFee() {
		return fee;
	}
	/**
	 * @param fee the fee to set
	 */
	public void setFee(CFee fee) {
		this.fee = fee;
	}

}
