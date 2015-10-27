package com.ycsoft.business.dto.core.prod;

import java.io.Serializable;
import java.util.List;

import com.ycsoft.beans.core.user.CUser;
/**
 * 终端用户选择界面查询结果
 * @author new
 *
 */
public class PackageGroupPanel implements Serializable {
	//套餐内容组详细信息
	private List<PackageGroupUser> groupList;
	//客户下所有终端用户
	private List<CUser> userList;
	
	//套餐配置跟客户用户数是否刚好匹配，如果匹配则needShow=falase（表示不需要打开终端选择界面)
	private Boolean needShow;
	
	public List<PackageGroupUser> getGroupList() {
		return groupList;
	}
	public void setGroupList(List<PackageGroupUser> groupList) {
		this.groupList = groupList;
	}
	public List<CUser> getUserList() {
		return userList;
	}
	public void setUserList(List<CUser> userList) {
		this.userList = userList;
	}
	public Boolean getNeedShow() {
		return needShow;
	}
	public void setNeedShow(Boolean needShow) {
		this.needShow = needShow;
	}
	
}
