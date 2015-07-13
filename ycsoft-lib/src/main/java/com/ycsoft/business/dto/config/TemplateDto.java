/**
 *
 */
package com.ycsoft.business.dto.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ycsoft.beans.config.TBusiCodeFee;
import com.ycsoft.beans.config.TBusiDoc;
import com.ycsoft.beans.config.TConfigTemplate;
import com.ycsoft.beans.config.TInvoicePrintitem;
import com.ycsoft.beans.config.TProdStatusRent;
import com.ycsoft.beans.task.TBusiCodeTask;

/**
 * @author liujiaqi
 *
 */
public class TemplateDto {

	private String template_id;
	private String template_type;
	private String template_name;

	private List<TBusiCodeFee> fee = new ArrayList<TBusiCodeFee>();
	private List<TBusiCodeTask> task = new ArrayList<TBusiCodeTask>();
	private List<TBusiDoc> doc = new ArrayList<TBusiDoc>();
	private List<TInvoicePrintitem> printItem = new ArrayList<TInvoicePrintitem>();
	private List<TProdStatusRent> prodRent = new ArrayList<TProdStatusRent>();

	private Map<String, TConfigTemplate> config = null;

	/**
	 * @return the config
	 */
	public Map<String, TConfigTemplate> getConfig() {
		return config;
	}

	/**
	 * @param config
	 *            the config to set
	 */
	public void setConfig(Map<String, TConfigTemplate> config) {
		this.config = config;
	}

	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}

	public void setTemplate_type(String template_type) {
		this.template_type = template_type;
	}

	public void setTemplate_name(String template_name) {
		this.template_name = template_name;
	}

	public void setFee(List<TBusiCodeFee> fee) {
		this.fee = fee;
	}

	public void setTask(List<TBusiCodeTask> task) {
		this.task = task;
	}

	public String getTemplate_id() {
		return template_id;
	}

	public String getTemplate_type() {
		return template_type;
	}

	public String getTemplate_name() {
		return template_name;
	}

	public List<TBusiCodeFee> getFee() {
		return fee;
	}

	public List<TBusiCodeTask> getTask() {
		return task;
	}

	public List<TBusiDoc> getDoc() {
		return doc;
	}

	public void setDoc(List<TBusiDoc> doc) {
		this.doc = doc;
	}

	public List<TProdStatusRent> getProdRent() {
		return prodRent;
	}

	public void setProdRent(List<TProdStatusRent> prodRent) {
		this.prodRent = prodRent;
	}

	public List<TInvoicePrintitem> getPrintItem() {
		return printItem;
	}

	public void setPrintItem(List<TInvoicePrintitem> printItem) {
		this.printItem = printItem;
	}
}
