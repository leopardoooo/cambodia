package com.ycsoft.commons.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddrTreeNode {

	private String pid ;
	private String text ;
	private String id ;
	private boolean leaf;
	private Boolean checked;
	private String cls;
	private String iconCls;
	private String is_leaf;//在表中是否是叶子节点
	private String attr;
	private String attr_src;
	private String attr_src_id;
	private boolean is_refresh = true;
	private boolean hideObarEdit = false;
	private boolean hideObarStatusactive = false;
	private boolean hideObarStatusinvalid = false;
	private boolean hideObarLeveladd = false;
	private boolean hideObarAdd = false;
	
	private boolean expanded = false;
	private boolean singleClickExpand = true;
	private Map<String,String> others = new HashMap<String,String>();
	private List<AddrTreeNode> children = new ArrayList<AddrTreeNode>();

	
	


	public boolean isIs_refresh() {
		return is_refresh;
	}
	public void setIs_refresh(boolean is_refresh) {
		this.is_refresh = is_refresh;
	}
	public String getIconCls() {
		return iconCls;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
	public boolean isHideObarAdd() {
		return hideObarAdd;
	}
	public void setHideObarAdd(boolean hideObarAdd) {
		this.hideObarAdd = hideObarAdd;
	}
	public boolean isHideObarEdit() {
		return hideObarEdit;
	}
	public void setHideObarEdit(boolean hideObarEdit) {
		this.hideObarEdit = hideObarEdit;
	}

	public boolean isHideObarStatusactive() {
		return hideObarStatusactive;
	}
	public void setHideObarStatusactive(boolean hideObarStatusactive) {
		this.hideObarStatusactive = hideObarStatusactive;
	}
	public boolean isHideObarStatusinvalid() {
		return hideObarStatusinvalid;
	}
	public void setHideObarStatusinvalid(boolean hideObarStatusinvalid) {
		this.hideObarStatusinvalid = hideObarStatusinvalid;
	}
	public boolean isHideObarLeveladd() {
		return hideObarLeveladd;
	}
	public void setHideObarLeveladd(boolean hideObarLeveladd) {
		this.hideObarLeveladd = hideObarLeveladd;
	}
	public List<AddrTreeNode> getChildren() {
		return children;
	}
	public void setChildren(List<AddrTreeNode> children) {
		this.children = children;
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
