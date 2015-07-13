/**
 *
 */
package com.ycsoft.business.dto.core.prod;

import java.util.List;

import com.ycsoft.beans.prod.PProdTariff;
import com.ycsoft.beans.prod.PProdTariffDisct;

/**
 * @author YC-SOFT
 *
 */
public class ProdTariffDto extends PProdTariff {

	private static final long serialVersionUID = -3139351719650518144L;
	private List<Object> ruleList;
	private String for_area_id;
	private List<PProdTariffDisct> disctList;
	
	private String rule_name;
	
	private List<String> countyList;
	
	public List<Object> getRuleList() {
		return ruleList;
	}
	public void setRuleList(List<Object> ruleList) {
		this.ruleList = ruleList;
	}
	public List<PProdTariffDisct> getDisctList() {
		return disctList;
	}
	public void setDisctList(List<PProdTariffDisct> disctList) {
		this.disctList = disctList;
	}
	public String getFor_area_id() {
		return for_area_id;
	}
	public void setFor_area_id(String for_area_id) {
		this.for_area_id = for_area_id;
	}
	/**
	 * @return the countyList
	 */
	public List<String> getCountyList() {
		return countyList;
	}
	/**
	 * @param countyList the countyList to set
	 */
	public void setCountyList(List<String> countyList) {
		this.countyList = countyList;
	}
	public String getRule_name() {
		return rule_name;
	}
	public void setRule_name(String rule_name) {
		this.rule_name = rule_name;
	}
	
}
