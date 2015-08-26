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
import com.ycsoft.commons.constants.SystemConstants;

/**
 * 单个客户业务的参数封装。 扩展<code>Parameter</code>基类
 *
 * @author hh
 * @data Mar 17, 2010 10:55:02 AM
 */
public class BusiParameter extends Parameter {
	private static final long serialVersionUID = -6773990683472720374L;

	private CustFullInfoDto custFullInfo = new CustFullInfoDto();
//	private List<CUserAtv> selectedAtvs = new ArrayList<CUserAtv>();
//	private List<CUserDtv> selectedDtvs = new ArrayList<CUserDtv>();
//	private List<CUserBroadband> selectedBands = new ArrayList<CUserBroadband>();
	
	private List<CUser> selectedUsers = new ArrayList<CUser>();
	
	private Map<String, Object> busiConfirmParamInfo = new HashMap<String, Object>();
	
	//服务渠道
	private String service_channel=SystemConstants.SERVICE_CHANNEL_YYT;
	
	
	public String getService_channel() {
		return service_channel;
	}

	public void setService_channel(String service_channel) {
		this.service_channel = service_channel;
	}

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

	public CCust getCust(){
		return getCustFullInfo().getCust();
	}

	public List<String> getSelectedUserIds(){
		List<String> ids = new ArrayList<String>();
		for (CUser user :selectedUsers){
			ids.add(user.getUser_id());
		}
		
		return ids;
	}

	
	public void addUser(CUser user){
		this.getSelectedUsers().add(user);
	}
	
	public void resetUser(){
		this.selectedUsers.clear();
	}

	public List<CUser> getSelectedUsers() {
		return selectedUsers;
	}

	public void setSelectedUsers(List<CUser> selectedUsers) {
		this.selectedUsers = selectedUsers;
	}
	
	

}
