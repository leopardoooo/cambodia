package com.ycsoft.business.commons.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ycsoft.beans.core.cust.CCust;
import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.beans.core.user.CUserAtv;
import com.ycsoft.beans.core.user.CUserBroadband;
import com.ycsoft.beans.core.user.CUserDtv;
import com.ycsoft.business.dto.core.cust.CustFullInfoDto;

/**
 * 单个客户业务的参数封装。 扩展<code>Parameter</code>基类
 *
 * @author hh
 * @data Mar 17, 2010 10:55:02 AM
 */
public class BusiParameter extends Parameter {
	private static final long serialVersionUID = -6773990683472720374L;

	private CustFullInfoDto custFullInfo = new CustFullInfoDto();
	private List<CUserAtv> selectedAtvs = new ArrayList<CUserAtv>();
	private List<CUserDtv> selectedDtvs = new ArrayList<CUserDtv>();
	private List<CUserBroadband> selectedBands = new ArrayList<CUserBroadband>();
	
	private Map<String, Object> busiConfirmParamInfo = new HashMap<String, Object>();
	
	public void setBusiConfirmParam(String name,Object value){
		this.busiConfirmParamInfo.put(name, value);
	}
	
	public Map<String, Object> getBusiConfirmParamInfo(){
		return this.busiConfirmParamInfo;
	}

	public CustFullInfoDto getCustFullInfo() {
		return custFullInfo;
	}

	public void setCustFullInfo(CustFullInfoDto custFullInfo) {
		this.custFullInfo = custFullInfo;
	}

	public List<CUserAtv> getSelectedAtvs() {
		return selectedAtvs;
	}

	public void setSelectedAtvs(List<CUserAtv> selectedAtvs) {
		this.selectedAtvs = selectedAtvs;
	}

	public List<CUserDtv> getSelectedDtvs() {
		return selectedDtvs;
	}

	public void setSelectedDtvs(List<CUserDtv> selectedDtvs) {
		this.selectedDtvs = selectedDtvs;
	}

	public List<CUserBroadband> getSelectedBands() {
		return selectedBands;
	}

	public void setSelectedBands(List<CUserBroadband> selectedBands) {
		this.selectedBands = selectedBands;
	}

	public CCust getCust(){
		return getCustFullInfo().getCust();
	}

	public List<String> getSelectedUserIds(){
		List<String> ids = new ArrayList<String>();
		for (CUser user :selectedAtvs){
			ids.add(user.getUser_id());
		}
		for (CUser user :selectedDtvs){
			ids.add(user.getUser_id());
		}
		for (CUser user :selectedBands){
			ids.add(user.getUser_id());
		}
		return ids;
	}

	public List<CUser> getSelectedUsers(){
		List<CUser> users = new ArrayList<CUser>();
		for (CUser user :selectedAtvs){
			users.add(user);
		}
		for (CUser user :selectedDtvs){
			users.add(user);
		}
		for (CUser user :selectedBands){
			users.add(user);
		}
		return users;
	}

	public void addUser(CUser user){
		if (user instanceof CUserAtv){
			getSelectedAtvs().add((CUserAtv)user);
		} else if (user instanceof CUserDtv){
			getSelectedDtvs().add((CUserDtv)user);
		} else if (user instanceof CUserBroadband){
			getSelectedBands().add((CUserBroadband)user);
		}
	}
	
	public void resetUser(){
		this.selectedAtvs.clear();
		this.selectedDtvs.clear();
		this.selectedBands.clear();
	}

}
