/**
 *
 */
package com.ycsoft.business.dto.core.print;

/**
 * 打印项的数据封装
 *
 * @author hh
 */
public class PrintItemDto {

	private String docitem_sn ;
	private Integer amount ;
	private String printitem_name;
	private String doc_type;
	private String card_id;

	/**
	 * @return the card_id
	 */
	public String getCard_id() {
		return card_id;
	}
	/**
	 * @param card_id the card_id to set
	 */
	public void setCard_id(String card_id) {
		this.card_id = card_id;
	}
	public String getDocitem_sn() {
		return docitem_sn;
	}
	public void setDocitem_sn(String docitem_sn) {
		this.docitem_sn = docitem_sn;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public String getPrintitem_name() {
		return printitem_name;
	}
	public void setPrintitem_name(String printitem_name) {
		this.printitem_name = printitem_name;
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
