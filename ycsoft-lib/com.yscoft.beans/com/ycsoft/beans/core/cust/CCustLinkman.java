/**
 * CCustLinkman.java	2010/02/08
 */

package com.ycsoft.beans.core.cust;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.beans.base.BusiBase;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;

/**
 * CCustLinkman -> C_CUST_LINKMAN mapping
 */
@POJO(tn = "C_CUST_LINKMAN", sn = "", pk = "CUST_ID")
public class CCustLinkman extends BusiBase implements Serializable {

	// CCustLinkman all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 6788639796879619097L;
	private String cust_id;
	private String linkman_name;
	private String sex;
	private Date birthday;
	private String tel;
	private String mobile;
	private String email;
	private String mail_address;
	private String postcode;
	private String cert_type;
	private String cert_num;

	private String sex_text;
	private String cert_type_text;

	public String getSex_text() {
		return sex_text;
	}

	public void setSex_text(String sex_text) {
		this.sex_text = sex_text;
	}

	/**
	 * default empty constructor
	 */
	public CCustLinkman() {
	}

	// cust_id getter and setter
	public String getCust_id() {
		return cust_id;
	}

	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}

	// linkman_name getter and setter
	public String getLinkman_name() {
		return linkman_name;
	}

	public void setLinkman_name(String linkman_name) {
		this.linkman_name = linkman_name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
		sex_text = MemoryDict.getDictName(DictKey.SEX, this.sex);
	}

	// birthday getter and setter
	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	// tel getter and setter
	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	// mobile getter and setter
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	// email getter and setter
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the mail_address
	 */
	public String getMail_address() {
		return mail_address;
	}

	/**
	 * @param mail_address the mail_address to set
	 */
	public void setMail_address(String mail_address) {
		this.mail_address = mail_address;
	}

	// postcode getter and setter
	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getCert_type() {
		return cert_type;
	}

	public void setCert_type(String cert_type) {
		this.cert_type = cert_type;
		cert_type_text = MemoryDict.getDictName(DictKey.CERT_TYPE, cert_type);
	}

	public String getCert_num() {
		return cert_num;
	}

	public void setCert_num(String cert_num) {
		this.cert_num = cert_num;
	}

	public String getCert_type_text() {
		return cert_type_text;
	}

}