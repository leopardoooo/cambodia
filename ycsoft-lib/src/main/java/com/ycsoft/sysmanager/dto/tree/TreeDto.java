package com.ycsoft.sysmanager.dto.tree;

import com.ycsoft.commons.tree.Tree;
import com.ycsoft.commons.tree.TreeNode;


public class TreeDto extends TreeNode implements Tree {

	public void transform(TreeNode node) {
		node.setId(getId());
		node.setPid(getPid());
		node.setText(getText());
		node.setChecked(getChecked());
		node.setLeaf(true);
		node.getOthers().put("attr", getAttr());
	}


}
