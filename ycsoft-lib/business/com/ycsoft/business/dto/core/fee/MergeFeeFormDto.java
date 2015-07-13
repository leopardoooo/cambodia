package com.ycsoft.business.dto.core.fee;

import java.util.ArrayList;
import java.util.List;

import com.ycsoft.beans.core.print.CDocItem;
import com.ycsoft.commons.pojo.DTO;


/**
 * 封装合并的费用项
 * @author hh
 */
public class MergeFeeFormDto implements DTO<CDocItem>{

	private String printitem_id ;
	private String doc_type;
	private Integer amount ;

	private List<String> fee_sns = new ArrayList<String>();;

	public String getPrintitem_id() {
		return printitem_id;
	}

	public void setPrintitem_id(String printitem_id) {
		this.printitem_id = printitem_id;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public List<String> getFee_sns() {
		return fee_sns;
	}

	public void setFee_sns(List<String> fee_sns) {
		this.fee_sns = fee_sns;
	}

	public CDocItem transform() {
		CDocItem t = new CDocItem();
		t.setAmount( amount);
		t.setPrintitem_id(printitem_id);
		return t;
	}

	/**
	 * @return the doc_type
	 */
	public String getDoc_type() {
		return doc_type;
	}

	/**
	 * @param doc_type the doc_type to set
	 */
	public void setDoc_type(String doc_type) {
		this.doc_type = doc_type;
	}


}
