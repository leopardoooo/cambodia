package com.ycsoft.business.dto.core.prod;

import java.util.List;
import java.util.Map;

import com.ycsoft.beans.prod.PProd;
import com.ycsoft.beans.prod.PProdTariffDisct;
/**
 * 订购修改的初始化数据
 * @author new
 */
public class OrderProdEdit extends OrderProd {
	
	/**
	 * 用户栏目描述
	 */
	private String userDesc;
	//可用产品
	private List<PProd> prodList;
	//可用资费Map<prod_id,List<资费折扣定义>>
	private Map<String,List<PProdTariffDisct>> tariffMap;
	//原始订单订购金额，
	private Integer old_order_fee;
	//原始转移支付金额
	private Integer old_transfer_fee;

	public Integer getOld_order_fee() {
		return old_order_fee;
	}
	public void setOld_order_fee(Integer old_order_fee) {
		this.old_order_fee = old_order_fee;
	}
	public Integer getOld_transfer_fee() {
		return old_transfer_fee;
	}
	public void setOld_transfer_fee(Integer old_transfer_fee) {
		this.old_transfer_fee = old_transfer_fee;
	}
	public String getUserDesc() {
		return userDesc;
	}
	public void setUserDesc(String userDesc) {
		this.userDesc = userDesc;
	}
	public List<PProd> getProdList() {
		return prodList;
	}
	public void setProdList(List<PProd> prodList) {
		this.prodList = prodList;
	}
	public Map<String, List<PProdTariffDisct>> getTariffMap() {
		return tariffMap;
	}
	public void setTariffMap(Map<String, List<PProdTariffDisct>> tariffMap) {
		this.tariffMap = tariffMap;
	}
	
	
	
}
