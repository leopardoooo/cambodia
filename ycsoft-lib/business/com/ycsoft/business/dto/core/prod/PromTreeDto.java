package com.ycsoft.business.dto.core.prod;

import java.util.List;

import com.ycsoft.commons.tree.TreeNode;

public class PromTreeDto  {
	
	private List<PromFeeProdDto> userList ;
	private List<TreeNode> promTree;
	public List<PromFeeProdDto> getUserList() {
		return userList;
	}
	public void setUserList(List<PromFeeProdDto> userList) {
		this.userList = userList;
	}
	public List<TreeNode> getPromTree() {
		return promTree;
	}
	public void setPromTree(List<TreeNode> promTree) {
		this.promTree = promTree;
	}
	
}
