/**
 * CDoneCode.java	2010/03/16
 */

package com.ycsoft.beans.core.common;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.daos.config.POJO;

/**
 * CDoneCodeInfo -> C_DONE_CODE_INFO mapping
 */
@POJO(tn = "C_DONE_CODE_INFO", sn = "", pk = "DONE_CODE")
public class CDoneCodeInfo extends CDoneCode implements Serializable {

	// CDoneCode all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -2336693094456857500L;
	private String info;
	private String ext;

	private Date last_print;
	private String cust_id;
	private String user_id;
	private String info1;
	private String info2;
	private String info3;
	private String info4;
	private String info5;
	private String info6;
	private String info7;
	private String info8;
	private String info9;
	private String info10;
	
	
	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getInfo1() {
		return info1;
	}

	public void setInfo1(String info1) {
		this.info1 = info1;
	}

	public String getInfo2() {
		return info2;
	}

	public void setInfo2(String info2) {
		this.info2 = info2;
	}

	public String getInfo3() {
		return info3;
	}

	public void setInfo3(String info3) {
		this.info3 = info3;
	}

	public String getInfo4() {
		return info4;
	}

	public void setInfo4(String info4) {
		this.info4 = info4;
	}

	public String getInfo5() {
		return info5;
	}

	public void setInfo5(String info5) {
		this.info5 = info5;
	}

	public String getInfo6() {
		return info6;
	}

	public void setInfo6(String info6) {
		this.info6 = info6;
	}

	public String getInfo7() {
		return info7;
	}

	public void setInfo7(String info7) {
		this.info7 = info7;
	}

	public String getInfo8() {
		return info8;
	}

	public void setInfo8(String info8) {
		this.info8 = info8;
	}

	public String getInfo9() {
		return info9;
	}

	public void setInfo9(String info9) {
		this.info9 = info9;
	}

	public String getInfo10() {
		return info10;
	}

	public void setInfo10(String info10) {
		this.info10 = info10;
	}

	public String getCust_id() {
		return cust_id;
	}

	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}

	/**
	 * @return the last_print
	 */
	public Date getLast_print() {
		return last_print;
	}

	/**
	 * @param last_print the last_print to set
	 */
	public void setLast_print(Date last_print) {
		this.last_print = last_print;
	}

	/**
	 * @return the info
	 */
	public String getInfo() {
		return info;
	}

	/**
	 * @param info
	 *            the info to set
	 */
	public void setInfo(String info) {
		this.info = info;
	}

	/**
	 * @return the ext
	 */
	public String getExt() {
		return ext;
	}

	/**
	 * @param ext
	 *            the ext to set
	 */
	public void setExt(String ext) {
		this.ext = ext;
	}
}