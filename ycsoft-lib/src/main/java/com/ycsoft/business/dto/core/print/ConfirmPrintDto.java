package com.ycsoft.business.dto.core.print;

import java.io.Serializable;
import java.util.List;

public class ConfirmPrintDto implements Serializable {
	
	//针对居民用户 分终端打印缴费项
	public class ConfirmPrint {
		private String user_name;
		private String user_type;
		private String user_type_text;
		private String terminal_type;
		private String terminal_type_text;
		private String printitem_name;
		private int fee;					//单位：分
		
		public String getUser_type() {
			return user_type;
		}
		public void setUser_type(String user_type) {
			this.user_type = user_type;
		}
		public String getTerminal_type() {
			return terminal_type;
		}
		public void setTerminal_type(String terminal_type) {
			this.terminal_type = terminal_type;
		}
		public String getPrintitem_name() {
			return printitem_name;
		}
		public void setPrintitem_name(String printitem_name) {
			this.printitem_name = printitem_name;
		}
		public int getFee() {
			return fee;
		}
		public void setFee(int fee) {
			this.fee = fee;
		}
		public String getUser_type_text() {
			return user_type_text;
		}
		public void setUser_type_text(String user_type_text) {
			this.user_type_text = user_type_text;
		}
		public String getTerminal_type_text() {
			return terminal_type_text;
		}
		public void setTerminal_type_text(String terminal_type_text) {
			this.terminal_type_text = terminal_type_text;
		}
		public String getUser_name() {
			return user_name;
		}
		public void setUser_name(String user_name) {
			this.user_name = user_name;
		}
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 3705046637309328919L;
	
	private List<ConfirmPrint> data;
	
	private String printitem_name;		//针对 非居民用户
	private int sum_fee;				//单位：分
	private int count;					//打印项数目
	
	public int getSum_fee() {
		return sum_fee;
	}
	public void setSum_fee(int sum_fee) {
		this.sum_fee = sum_fee;
	}
	public List<ConfirmPrint> getData() {
		return data;
	}
	public void setData(List<ConfirmPrint> data) {
		this.data = data;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getPrintitem_name() {
		return printitem_name;
	}
	public void setPrintitem_name(String printitem_name) {
		this.printitem_name = printitem_name;
	}
}
