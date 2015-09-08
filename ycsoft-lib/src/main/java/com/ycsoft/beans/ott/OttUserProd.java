package com.ycsoft.beans.ott;

import java.io.Serializable;

import com.ycsoft.commons.helper.DateHelper;

/**
 * re.put("id", order.getProd_id());
			re.put("name", order.getProd_name());
			re.put("continue_buy", "1");
			re.put("update", "0");
			re.put("begin_time", DateHelper.format(order.getEff_date(),DateHelper.FORMAT_TIME));
			re.put("end_time"
 * @author new
 *
 */
public class OttUserProd implements Serializable {
	private String id;
	private String name;
    private String continue_buy="1";
    private String update="0";
    private String begin_time;
    private String end_time;
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
	public String getContinue_buy() {
		return continue_buy;
	}
	public void setContinue_buy(String continue_buy) {
		this.continue_buy = continue_buy;
	}
	public String getUpdate() {
		return update;
	}
	public void setUpdate(String update) {
		this.update = update;
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
