package com.ycsoft.report.commons.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RepTreeNode {

	private String pid ;
	private String text ;
	private String id ;
	private boolean leaf;
	private Boolean checked;
	private String cls;
	private String is_leaf;//在表中是否是叶子节点
	private String attr;
	private Map<String,String> others = new HashMap<String,String>();
	private List<RepTreeNode> children = new ArrayList<RepTreeNode>();

	private String iconCls;

	public String getIconCls() {
		return iconCls;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
	public List<RepTreeNode> getChildren() {
		return children;
	}
	public void setChildren(List<RepTreeNode> children) {
		this.children = children;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public boolean isLeaf() {
		return leaf;
	}
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}
	public String getCls() {
		return cls;
	}
	public void setCls(String cls) {
		this.cls = cls;
	}
	public Map<String, String> getOthers() {
		return others;
	}
	public void setOthers(Map<String, String> others) {
		this.others = others;
	}
	public String getIs_leaf() {
		return is_leaf;
	}
	public void setIs_leaf(String is_leaf) {
		this.is_leaf = is_leaf;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public Boolean getChecked() {
		return checked;
	}
	public void setChecked(Boolean checked) {
		this.checked = checked;
	}
	public String getAttr() {
		return attr;
	}
	public void setAttr(String attr) {
		this.attr = attr;
	}

}
