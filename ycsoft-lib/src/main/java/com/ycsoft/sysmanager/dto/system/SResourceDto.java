package com.ycsoft.sysmanager.dto.system;

import com.ycsoft.beans.system.SResource;
import com.ycsoft.commons.tree.Tree;
import com.ycsoft.commons.tree.TreeNode;

public class SResourceDto extends SResource implements Tree {

	/**
	 *
	 */
	private static final long serialVersionUID = 2232093631462905849L;

	public void transform(TreeNode node) {
		node.setId(getRes_id());
		node.setPid(getRes_pid());
		node.setText(getRes_name());
		node.setLeaf(true);
		node.getOthers().put("url", getUrl());
	}

}
