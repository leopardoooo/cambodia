package com.ycsoft.report.dto;

import com.ycsoft.beans.system.SResource;
import com.ycsoft.report.commons.tree.RepTree;
import com.ycsoft.report.commons.tree.RepTreeNode;
import com.ycsoft.report.commons.tree.TreeLink;


public class RepResourceDto extends SResource implements RepTree,TreeLink {

	public void transform(RepTreeNode node) {
		node.setId(getRes_id());
		node.setPid(getRes_pid());
		node.setText(getRes_name());
		node.setLeaf(true);
		node.getOthers().put("url", getUrl());
		node.getOthers().put("handler", getHandler());
		node.setIconCls(this.getIconcls());
	}

	public String getId() {
		return super.getRes_id();
	}

	public String getPid() {
		return super.getRes_pid();
	}

}
