package com.ycsoft.business.dto.core.voucher;

import com.ycsoft.beans.core.voucher.CVoucherProcure;

public class VoucherProcureDto extends CVoucherProcure {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5346394437912373069L;
	private String start_voucher_id;
	private String end_voucher_id;
	public String getStart_voucher_id() {
		return start_voucher_id;
	}
	public void setStart_voucher_id(String start_voucher_id) {
		this.start_voucher_id = start_voucher_id;
	}
	public String getEnd_voucher_id() {
		return end_voucher_id;
	}
	public void setEnd_voucher_id(String end_voucher_id) {
		this.end_voucher_id = end_voucher_id;
	}
}
