/**
 * CFee.java	2010/07/30
 */

package com.ycsoft.beans.core.fee;

import java.io.Serializable;
import java.util.Date;

import com.ycsoft.beans.base.BusiBase;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;

/**
 * CFee -> C_FEE mapping
 */
@POJO(
	tn="C_FEE",
	sn="SEQ_FEE_SN",
	pk="fee_sn")
public class CFee extends BusiBase implements Serializable {

	// CFee all properties

	/**
	 *
	 */
	private static final long serialVersionUID = -1810520144709051900L;
	private String fee_sn;
	private Integer busi_done_code;
	private Integer create_done_code;
	private Integer reverse_done_code;
	private String cust_id;
	private String user_id;
	private String is_doc;
	private String status;
	private String fee_type;
	private String acct_id;
	private String acctitem_id;
	private String fee_id;
	private Integer count;
	private Integer should_pay;
	private Integer real_pay;
	private String disct_type;
	private String disct_info;
	private String promotion_sn;
	private String pay_type;
	private String invoice_mode;
	private String invoice_code;
	private String invoice_id;
	private String invoice_book_id;
	private Date acct_date;
	private String auto_promotion;
	private String busi_optr_id;
	private Integer invoice_fee;

	private String is_doc_text;//打印状态
	private String status_text;
	private String pay_type_text;// 付款方式
	private String fee_type_text;// 费用类别
	private String fee_name;//费用名称
	private String busi_optr_name;
	private String invoice_mode_text;
	private Integer buy_num;
	private String addr_id;
	private String pay_sn;
	private String fee_std_id;
	
	private String acctitem_id_text;
	private String fee_id_text;
	
	
	public String getAcctitem_id_text() {
		return acctitem_id_text;
	}

	public String getFee_id_text() {
		return fee_id_text;
	}

	public String getFee_std_id() {
		return fee_std_id;
	}

	public void setFee_std_id(String fee_std_id) {
		this.fee_std_id = fee_std_id;
	}

	public String getPay_sn() {
		return pay_sn;
	}

	public void setPay_sn(String pay_sn) {
		this.pay_sn = pay_sn;
	}

	public String getAddr_id() {
		return addr_id;
	}

	public void setAddr_id(String addr_id) {
		this.addr_id = addr_id;
	}

	/**
	 * default empty constructor
	 */
	public CFee() {
	}

	// fee_sn getter and setter
	public String getFee_sn() {
		return fee_sn;
	}

	public void setFee_sn(String fee_sn) {
		this.fee_sn = fee_sn;
	}

	// busi_done_code getter and setter
	public Integer getBusi_done_code() {
		return busi_done_code;
	}

	public void setBusi_done_code(Integer busi_done_code) {
		this.busi_done_code = busi_done_code;
	}

	// create_done_code getter and setter
	public Integer getCreate_done_code() {
		return create_done_code;
	}

	public void setCreate_done_code(Integer create_done_code) {
		this.create_done_code = create_done_code;
	}

	// reverse_done_code getter and setter
	public Integer getReverse_done_code() {
		return reverse_done_code;
	}

	public void setReverse_done_code(Integer reverse_done_code) {
		this.reverse_done_code = reverse_done_code;
	}

	// cust_id getter and setter
	public String getCust_id() {
		return cust_id;
	}

	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}

	// user_id getter and setter
	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}



	// is_doc getter and setter
	public String getIs_doc() {
		return is_doc;
	}

	public void setIs_doc(String is_doc) {
		this.is_doc = is_doc;
		if(this.is_doc.equals("T")){
			this.is_doc_text = MemoryDict.getDictName(DictKey.STATUS, "PRINT");
		}else if(this.is_doc.equals("N")){
			this.is_doc_text = MemoryDict.getDictName(DictKey.STATUS, "DONOTPRINT");
		}else{
			this.is_doc_text = MemoryDict.getDictName(DictKey.STATUS, "NOTPRINT");
		}
	}

	public String getIs_doc_text() {
		return is_doc_text;
	}

	public void setIs_doc_text(String is_doc_text) {
		this.is_doc_text = is_doc_text;
	}

	// status getter and setter
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		status_text = MemoryDict.getDictName(DictKey.STATUS, status);
		this.status = status;
	}

	// fee_id getter and setter
	public String getFee_id() {
		return fee_id;
	}

	public void setFee_id(String fee_id) {
		this.fee_id = fee_id;
		this.fee_id_text=MemoryDict.getDictName(DictKey.BUSI_FEE, fee_id);
	}

	// count getter and setter
	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	// should_pay getter and setter
	public Integer getShould_pay() {
		return should_pay;
	}

	public void setShould_pay(Integer should_pay) {
		this.should_pay = should_pay;
	}

	// real_pay getter and setter
	public Integer getReal_pay() {
		return real_pay;
	}

	public void setReal_pay(Integer real_pay) {
		this.real_pay = real_pay;
	}

	// disct_type getter and setter
	public String getDisct_type() {
		return disct_type;
	}

	public void setDisct_type(String disct_type) {
		this.disct_type = disct_type;
	}

	// disct_info getter and setter
	public String getDisct_info() {
		return disct_info;
	}

	public void setDisct_info(String disct_info) {
		this.disct_info = disct_info;
	}

	// promotion_sn getter and setter
	public String getPromotion_sn() {
		return promotion_sn;
	}

	public void setPromotion_sn(String promotion_sn) {
		this.promotion_sn = promotion_sn;
	}

	// pay_type getter and setter
	public String getPay_type() {
		return pay_type;
	}

	public void setPay_type(String pay_type) {
		pay_type_text = MemoryDict.getDictName(DictKey.PAY_TYPE, pay_type);
		this.pay_type = pay_type;
	}

	// invoice_code getter and setter
	public String getInvoice_code() {
		return invoice_code;
	}

	public void setInvoice_code(String invoice_code) {
		this.invoice_code = invoice_code;
	}

	// invoice_id getter and setter
	public String getInvoice_id() {
		return invoice_id;
	}

	public void setInvoice_id(String invoice_id) {
		this.invoice_id = invoice_id;
	}

	/**
	 * @return the status_text
	 */
	public String getStatus_text() {
		return status_text;
	}

	public String getFee_type() {
		return fee_type;
	}

	public void setFee_type(String fee_type) {
		this.fee_type = fee_type;
		fee_type_text = MemoryDict.getDictName(DictKey.FEE_TYPE, fee_type);
	}

	/**
	 * @return the pay_type_text
	 */
	public String getPay_type_text() {
		return pay_type_text;
	}

	/**
	 * @return the fee_type_text
	 */
	public String getFee_type_text() {
		return fee_type_text;
	}

	public String getFee_name() {
		return fee_name;
	}

	public void setFee_name(String fee_name) {
		this.fee_name = fee_name;
	}
	/**
	 * @return the acct_date
	 */
	public Date getAcct_date() {
		return acct_date;
	}

	/**
	 * @param acct_date the acct_date to set
	 */
	public void setAcct_date(Date acct_date) {
		this.acct_date = acct_date;
	}

	/**
	 * @return the invoice_mode
	 */
	public String getInvoice_mode() {
		return invoice_mode;
	}

	/**
	 * @param invoice_mode the invoice_mode to set
	 */
	public void setInvoice_mode(String invoice_mode) {
		this.invoice_mode = invoice_mode;
		this.invoice_mode_text = MemoryDict.getDictName(DictKey.INVOICE_MODE, invoice_mode);
	}

	public String getAcct_id() {
		return acct_id;
	}

	public void setAcct_id(String acct_id) {
		this.acct_id = acct_id;
	}

	public String getAcctitem_id() {
		return acctitem_id;
	}

	public void setAcctitem_id(String acctitem_id) {
		this.acctitem_id = acctitem_id;
		this.acctitem_id_text=MemoryDict.getDictName(DictKey.ACCTITEM, acctitem_id);
	}

	public String getAuto_promotion() {
		return auto_promotion;
	}

	public void setAuto_promotion(String auto_promotion) {
		this.auto_promotion = auto_promotion;
	}

	/**
	 * @return the invoice_book_id
	 */
	public String getInvoice_book_id() {
		return invoice_book_id;
	}

	/**
	 * @param invoice_book_id the invoice_book_id to set
	 */
	public void setInvoice_book_id(String invoice_book_id) {
		this.invoice_book_id = invoice_book_id;
	}

	public String getBusi_optr_id() {
		return busi_optr_id;
	}

	public void setBusi_optr_id(String busi_optr_id) {
		this.busi_optr_id = busi_optr_id;
		
		if(StringHelper.isNotEmpty(this.busi_optr_id)){
			String[] optrIds = this.busi_optr_id.split(",");
			if(optrIds.length == 1){
				busi_optr_name = MemoryDict.getDictName(DictKey.OPTR, busi_optr_id);
			}else{
				busi_optr_name = "";
				for(String s : optrIds){
					busi_optr_name += MemoryDict.getDictName(DictKey.OPTR, s)+",";
				}
				busi_optr_name = busi_optr_name.substring(0, busi_optr_name.length()-1);
			}
		}
	}

	public String getBusi_optr_name() {
		return busi_optr_name;
	}

	public Integer getInvoice_fee() {
		return invoice_fee;
	}

	public void setInvoice_fee(Integer invoiceFee) {
		invoice_fee = invoiceFee;
	}

	public String getInvoice_mode_text() {
		return invoice_mode_text;
	}

	public Integer getBuy_num() {
		return buy_num;
	}

	public void setBuy_num(Integer buy_num) {
		this.buy_num = buy_num;
	}



}