/**
 * CBankAgree.java	2010/11/01
 */

package com.ycsoft.beans.core.bank;

import java.io.Serializable;
import com.ycsoft.daos.config.POJO;

/**
 * CBankAgree -> C_BANK_AGREE mapping
 */
@POJO(tn = "C_BANK_AGREE", sn = "SEQ_AGREE_ID", pk = "AGREE_ID")
public class CBankAgree implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1186007189225323023L;
	// CBankAgree all properties

	private Integer agree_id;
	private String b_filename;
	private String b_ognb;
	private String b_feid;
	private String b_asnb;
	private String b_name;
	private String b_bkno;
	private String b_acno;
	private String b_sqno;
	private String b_state;
	private String b_wkdt;
	private String proc_status;
	private String proc_result;
	private String file_detail;
	private String proc_date;
	/**
	 * @return the proc_date
	 */
	public String getProc_date() {
		return proc_date;
	}

	/**
	 * @param proc_date the proc_date to set
	 */
	public void setProc_date(String proc_date) {
		this.proc_date = proc_date;
	}

	/**
	 * default empty constructor
	 */
	public CBankAgree() {
	}

	// agree_id getter and setter
	public Integer getAgree_id() {
		return this.agree_id;
	}

	public void setAgree_id(Integer agree_id) {
		this.agree_id = agree_id;
	}

	// b_filename getter and setter
	public String getB_filename() {
		return this.b_filename;
	}

	public void setB_filename(String b_filename) {
		this.b_filename = b_filename;
	}

	// b_ognb getter and setter
	public String getB_ognb() {
		return this.b_ognb;
	}

	public void setB_ognb(String b_ognb) {
		this.b_ognb = b_ognb;
	}

	// b_feid getter and setter
	public String getB_feid() {
		return this.b_feid;
	}

	public void setB_feid(String b_feid) {
		this.b_feid = b_feid;
	}

	// b_asnb getter and setter
	public String getB_asnb() {
		return this.b_asnb;
	}

	public void setB_asnb(String b_asnb) {
		this.b_asnb = b_asnb;
	}

	// b_name getter and setter
	public String getB_name() {
		return this.b_name;
	}

	public void setB_name(String b_name) {
		this.b_name = b_name;
	}

	// b_bkno getter and setter
	public String getB_bkno() {
		return this.b_bkno;
	}

	public void setB_bkno(String b_bkno) {
		this.b_bkno = b_bkno;
	}

	// b_acno getter and setter
	public String getB_acno() {
		return this.b_acno;
	}

	public void setB_acno(String b_acno) {
		this.b_acno = b_acno;
	}

	// b_sqno getter and setter
	public String getB_sqno() {
		return this.b_sqno;
	}

	public void setB_sqno(String b_sqno) {
		this.b_sqno = b_sqno;
	}

	// b_state getter and setter
	public String getB_state() {
		return this.b_state;
	}

	public void setB_state(String b_state) {
		this.b_state = b_state;
	}

	// b_wkdt getter and setter
	public String getB_wkdt() {
		return this.b_wkdt;
	}

	public void setB_wkdt(String b_wkdt) {
		this.b_wkdt = b_wkdt;
	}

	// proc_status getter and setter
	public String getProc_status() {
		return this.proc_status;
	}

	public void setProc_status(String proc_status) {
		this.proc_status = proc_status;
	}

	// proc_result getter and setter
	public String getProc_result() {
		return this.proc_result;
	}

	public void setProc_result(String proc_result) {
		this.proc_result = proc_result;
	}

	// file_detail getter and setter
	public String getFile_detail() {
		return this.file_detail;
	}

	public void setFile_detail(String file_detail) {
		this.file_detail = file_detail;
	}

}