package com.ycsoft.sysmanager.dto.config;

import java.io.Serializable;
import java.util.List;

import com.ycsoft.commons.tree.TreeNode;

public class TemplateTreeDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2129200560636339274L;
	private List<TreeNode> treeList;
	private String changeCountyIds;
	public List<TreeNode> getTreeList() {
		return treeList;
	}
	public void setTreeList(List<TreeNode> t) {
		this.treeList = t;
	}
	public String getChangeCountyIds() {
		return changeCountyIds;
	}
	public void setChangeCountyIds(String changeCountyIds) {
		this.changeCountyIds = changeCountyIds;
	}
}
