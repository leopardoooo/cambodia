/**
 * PProdTariffDisct.java	2010/07/06
 */

package com.ycsoft.beans.prod;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.beans.base.OptrBase;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;

/**
 * PProdTariffDisct -> P_PROD_TARIFF_DISCT mapping
 */
@POJO(tn = "P_PROD_TARIFF_DISCT", sn = "SEQ_DISCT_ID", pk = "DISCT_ID")
public class PProdTariffDisct extends OptrBase implements Serializable {

	// PProdTariffDisct all properties

	/**
	 *
	 */
	private static final long serialVersionUID = 2929576243950773257L;
	private String disct_id;
	private String disct_name;
	private String tariff_id;
	private Integer final_rent;
	private Integer disct_rent;
	private Integer min_pay;
	private String status;
	private Date create_time;
	private String rule_id;
	private Date eff_date;
	private Date exp_date;
	private String refund;
	private String trans;

	private String refund_text;
	private String trans_text;
	private String status_text;
	private String rule_name;
	private String rule_id_text;
	private String disct_name_all;
	public String getRule_name() {
		return rule_name;
	}

	public void setRule_name(String rule_name) {
		this.rule_name = rule_name;
	}

	public String getStatus_text() {
		return status_text;
	}

	public void setStatus_text(String status_text) {
		this.status_text = status_text;
	}

	/**
	 * @return the rule_id
	 */
	public String getRule_id() {
		return rule_id;
	}

	/**
	 * @param rule_id
	 *            the rule_id to set
	 */
	public void setRule_id(String rule_id) {
		this.rule_id = rule_id;
	}

	/**
	 * default empty constructor
	 */
	public PProdTariffDisct() {
	}

	// disct_id getter and setter
	public String getDisct_id() {
		return disct_id;
	}

	public void setDisct_id(String disct_id) {
		this.disct_id = disct_id;
	}

	// disct_name getter and setter
	public String getDisct_name() {
		return disct_name;
	}

	public void setDisct_name(String disct_name) {
		this.disct_name = disct_name;
	}

	// tariff_id getter and setter
	public String getTariff_id() {
		return tariff_id;
	}

	public void setTariff_id(String tariff_id) {
		this.tariff_id = tariff_id;
	}

	// final_rent getter and setter
	public Integer getFinal_rent() {
		return final_rent;
	}

	public void setFinal_rent(Integer final_rent) {
		this.final_rent = final_rent;
	}

	// disct_rent getter and setter
	public Integer getDisct_rent() {
		return disct_rent;
	}

	public void setDisct_rent(Integer disct_rent) {
		this.disct_rent = disct_rent;
	}

	// min_pay getter and setter
	public Integer getMin_pay() {
		return min_pay;
	}

	public void setMin_pay(Integer min_pay) {
		this.min_pay = min_pay;
	}

	// status getter and setter
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
		status_text= MemoryDict.getDictName(DictKey.STATUS, status);
	}

	// create_time getter and setter
	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	/**
	 * @return the rule_id_text
	 */
	public String getRule_id_text() {
		return rule_id_text;
	}

	/**
	 * @param rule_id_text the rule_id_text to set
	 */
	public void setRule_id_text(String rule_id_text) {
		this.rule_id_text = rule_id_text;
	}

	public String getDisct_name_all() {
		return disct_name_all;
	}

	public void setDisct_name_all(String disctNameAll) {
		disct_name_all = disctNameAll;
	}

	public Date getEff_date() {
		return eff_date;
	}

	public void setEff_date(Date effDate) {
		eff_date = effDate;
	}

	public Date getExp_date() {
		return exp_date;
	}

	public void setExp_date(Date expDate) {
		exp_date = expDate;
	}

	public String getRefund() {
		return refund;
	}

	public void setRefund(String refund) {
		this.refund = refund;
		refund_text = MemoryDict.getDictName(DictKey.BOOLEAN, this.refund);
	}

	public String getTrans() {
		return trans;
	}

	public void setTrans(String trans) {
		this.trans = trans;
		trans_text = MemoryDict.getDictName(DictKey.BOOLEAN, this.trans);
	}

	public String getRefund_text() {
		return refund_text;
	}

	public void setRefund_text(String refundText) {
		refund_text = refundText;
	}

	public String getTrans_text() {
		return trans_text;
	}

	public void setTrans_text(String transText) {
		trans_text = transText;
	}

}