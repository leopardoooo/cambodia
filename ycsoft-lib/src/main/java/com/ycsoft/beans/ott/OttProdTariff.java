package com.ycsoft.beans.ott;

import java.io.Serializable;
/**
 * id	产品ID	
name	产品名称	
type	产品类型	0：周期性产品，如包月或者包年1：单片
product_fee_id	产品资费ID	
price	价格	
currency_type	货币类型	RMB：人民币USD：美元
unit	单位（周期性产品的时间周期单位）	year:年month:月day:天
amount	数量	
boss_data	BOSS扩展数据	产品订购时回传BOSS
begin_time	开始时间	2014-06-19 08:59:59
end_time	结束时间	2050-06-19 08:59:59
 * @author new
 *
 */
public class OttProdTariff implements Serializable {
	 private String id;//产品ID	
	 private String name;//	产品名称	
	 private String  type;//	产品类型	0：周期性产品，如包月或者包年1：单片
	 private String product_fee_id;//	产品资费ID	
	 private String  price;//	价格	
	 private String currency_type="USD";//	货币类型	RMB：人民币USD：美元
	 private String unit;//	单位（周期性产品的时间周期单位）	year:年month:月day:天
	 private String amount;//	数量	
	 private Object boss_data=new Object();//	BOSS扩展数据	产品订购时回传BOSS
	 private String begin_time="2014-06-19 08:59:59";//	开始时间	2014-06-19 08:59:59
	 private String  end_time="2050-06-19 08:59:59";//	结束时间	2050-06-19 08:59:59
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getProduct_fee_id() {
		return product_fee_id;
	}
	public void setProduct_fee_id(String product_fee_id) {
		this.product_fee_id = product_fee_id;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getCurrency_type() {
		return currency_type;
	}
	public void setCurrency_type(String currency_type) {
		this.currency_type = currency_type;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public Object getBoss_data() {
		return boss_data;
	}
	public void setBoss_data(Object boss_data) {
		this.boss_data = boss_data;
	}
	public String getBegin_time() {
		return begin_time;
	}
	public void setBegin_time(String begin_time) {
		this.begin_time = begin_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	 
	 
}
