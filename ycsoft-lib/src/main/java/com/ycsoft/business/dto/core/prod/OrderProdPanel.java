package com.ycsoft.business.dto.core.prod;

import java.util.List;
import java.util.Map;

import com.ycsoft.beans.core.prod.CProdOrder;
import com.ycsoft.beans.prod.PProd;
import com.ycsoft.beans.prod.PProdTariffDisct;
/**
 * 订购界面产品查询返回结果
 * @author new
 *
 */
public class OrderProdPanel {
	/**
	 * 用户栏目描述
	 *  如果是单产品订购情况，返回 用户类型+设备编号 (DTT_12312312,OTT_122)
	 * 如果是套餐续订或升级情况，返回 套餐的内容组用户选择描述( 例如： OTT一组=8，宽带组=2，OTT2组=4）
　　	 * 其他情况，返回空字符串
	 */
	private String userDesc;
	//可用产品组
	private List<PProd> prodList;
	//可用资费组 Map<prod_id,List<资费折扣定义>>
	private Map<String,List<PProdTariffDisct>> tariffMap;
	//上期订购记录Map<prod_id,订购记录>
	private Map<String,CProdOrder> lastOrderMap;
	
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
	public Map<String, CProdOrder> getLastOrderMap() {
		return lastOrderMap;
	}
	public void setLastOrderMap(Map<String, CProdOrder> lastOrderMap) {
		this.lastOrderMap = lastOrderMap;
	}
	
	
}
