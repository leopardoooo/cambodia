package com.ycsoft.business.dto.core.prod;

import java.util.ArrayList;
import java.util.HashMap;
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
	 *  如果是单产品订购情况，返用户类型+设备编号 (DTT_12312312,OTT_122)
?	 * 其他情况，返回空字符
	 */
	private String userDesc;
	//可用产品
	private List<PProd> prodList;
	//可用资费Map<prod_id,List<资费折扣定义>>
	private Map<String,List<PProdTariffDisct>> tariffMap;
	/**上期订购记录Map<prod_id,订购记录>
	 * 情况1 升级：上期订购记录是选中的要升级的订
	 * 情况2非升级： a.非宽带单产品的上期订购记录是 exp_date>=今天，相同user_id,相同prod_id(含套餐的子产 exp_date的订购记录
	 *           b.宽带单产品的上期订购记录exp_date>=今天，相同user_id,prod_id in(宽带单产品，含套餐的子产) exp_date的订购记
	 *           c.套餐的上期订购记录是   exp_date>=今天，相同cust_id,prod_id=套餐的exp_date的订购记录
	**/
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
	public OrderProdPanel() {
		prodList = new ArrayList<>();
		tariffMap = new HashMap<>();
		lastOrderMap = new HashMap<>();
		
	}
	
	
	
	
}
