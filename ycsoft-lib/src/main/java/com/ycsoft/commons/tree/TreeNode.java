package com.ycsoft.commons.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreeNode {

	private String pid ;
	private String text ;
	private String id ;
	private boolean leaf;
	private Boolean checked;
	private String cls;
	private String is_leaf;//在表中是否是叶子节点
	private String attr;
	private String attr_src;
	private String attr_src_id;
	private String other_id;
	private String other_name;
	private boolean expanded = false;
	private boolean singleClickExpand = true;
	private Map<String,String> others = new HashMap<String,String>();
	private List<TreeNode> children = new ArrayList<TreeNode>();


	public List<TreeNode> getChildren() {
		return children;
	}
	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}


	public String getOther_id() {
		return other_id;
	}
	public void setOther_id(String other_id) {
		this.other_id = other_id;
	}
	public String getOther_name() {
		return other_name;
	}
	public void setOther_name(String other_name) {
		this.other_name = other_name;
	}
	
	public boolean isExpanded() {
		return expanded;
	}
	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
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
	public boolean isSingleClickExpand() {
		return singleClickExpand;
	}
	public void setSingleClickExpand(boolean singleClickExpand) {
		this.singleClickExpand = singleClickExpand;
	}
	public String getAttr_src() {
		return attr_src;
	}
	public void setAttr_src(String attr_src) {
		this.attr_src = attr_src;
	}
	public String getAttr_src_id() {
		return attr_src_id;
	}
	public void setAttr_src_id(String attr_src_id) {
		this.attr_src_id = attr_src_id;
	}
}
