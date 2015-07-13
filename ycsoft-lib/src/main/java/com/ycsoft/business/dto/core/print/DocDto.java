/**
 *
 */
package com.ycsoft.business.dto.core.print;

import java.util.Date;

import com.ycsoft.beans.core.print.CDoc;

/**
 * @author liujiaqi
 *
 */
public class DocDto  extends CDoc{

	/**
	 *
	 */
	private static final long serialVersionUID = -6847034870068254402L;
	private String doc_name;
	private String print_type;
	private String is_invoice;
	private String template_filename;

	private Date done_date ;




	public String getDoc_name() {
		return doc_name;
	}

	public void setDoc_name(String doc_name) {
		this.doc_name = doc_name;
	}

	public String getPrint_type() {
		return print_type;
	}

	public void setPrint_type(String print_type) {
		this.print_type = print_type;
	}

	public String getIs_invoice() {
		return is_invoice;
	}

	public void setIs_invoice(String is_invoice) {
		this.is_invoice = is_invoice;
	}

	public String getTemplate_filename() {
		return template_filename;
	}

	public void setTemplate_filename(String template_filename) {
		this.template_filename = template_filename;
	}



	public Date getDone_date() {
		return done_date;
	}

	public void setDone_date(Date done_date) {
		this.done_date = done_date;
	}

}
