package com.ycsoft.sysmanager.dto.resource.invoice;

import com.ycsoft.commons.tree.Tree;
import com.ycsoft.commons.tree.TreeNode;

public class InvoiceTreeDto implements Tree {
	private String id;
	private String pid;
	private String text;
	private String is_leaf;

	public void transform(TreeNode node) {
		node.setId(getId());
		node.setPid(getPid());
		node.setText(getText());
		node.setLeaf(true);
		node.setCls("file");
		node.setIs_leaf(getIs_leaf());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getIs_leaf() {
		return is_leaf;
	}

	public void setIs_leaf(String is_leaf) {
		this.is_leaf = is_leaf;
	}

}
