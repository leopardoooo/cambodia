package com.ycsoft.business.dto.core.user;

public class UserGroup {
	private Integer groupId;
	private Integer groupMembers;
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	public Integer getGroupMembers() {
		return groupMembers;
	}
	public void setGroupMembers(Integer groupMembers) {
		this.groupMembers = groupMembers;
	}
	public UserGroup(Integer groupId, Integer groupMembers) {
		super();
		this.groupId = groupId;
		this.groupMembers = groupMembers;
	}
	

}
