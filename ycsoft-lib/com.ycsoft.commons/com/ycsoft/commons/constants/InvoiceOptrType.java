package com.ycsoft.commons.constants;

public enum InvoiceOptrType {
	/**领用**/
	RECEIVE,
	/**取消领用**/
	CANCEL_RECEIVE,
	/**调拨*/
	TRANS,
	/**结账*/
	CHECK,
	/**核销**/
	CLOSE,
	/**取消结账*/
	CANCELCHECK,
	/**取消核销*/
	CANCELCLOSE,
	/**作废*/
	EDITSTATUS,
	/**营业操作,如冲正,业务回退等涉及到发票的**/
	BUSI,
	/**退库*/
	REFUND,
	/**定额发票下发*/
	QUOTA_TRANS,
	/**定额发票挂失*/
	QUOTA_LOSS,
	/**定额发票取消挂失*/
	QUOTA_CANCEL_LOSS,
	/**定额发票调账*/
	QUOTA_ADJUST;
}
