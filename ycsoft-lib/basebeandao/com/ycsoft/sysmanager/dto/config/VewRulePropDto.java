package com.ycsoft.sysmanager.dto.config;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.commons.tree.Tree;
import com.ycsoft.commons.tree.TreeNode;

public class VewRulePropDto implements Tree {
	private String model_name;
	private String model_desc;
	private String prop_id;
	private String prop_name;
	private String data_type;
	private String param_name;

	private String data_type_text;

	public void transform(TreeNode node) {
		node.setId( getProp_id());
		node.setPid( getModel_name());
		node.setText( getProp_name());
		node.setLeaf(true);
		node.setCls("file");
		node.getOthers().put("data_type", getData_type());
		node.getOthers().put("param_name", getParam_name());
	}

	public String getModel_name() {
		return model_name;
	}
	public void setModel_name(String model_name) {
		this.model_name = model_name;
	}
	public String getModel_desc() {
		return model_desc;
	}
	public void setModel_desc(String model_desc) {
		this.model_desc = model_desc;
	}
	public String getProp_id() {
		return prop_id;
	}
	public void setProp_id(String prop_id) {
		this.prop_id = prop_id;
	}
	public String getProp_name() {
		return prop_name;
	}
	public void setProp_name(String prop_name) {
		this.prop_name = prop_name;
	}
	public String getData_type() {
		return data_type;
	}
	public void setData_type(String data_type) {
		data_type_text = MemoryDict.getDictName(DictKey.DATA_TYPE, data_type);
		this.data_type = data_type;
	}
	public String getParam_name() {
		return param_name;
	}
	public void setParam_name(String param_name) {
		this.param_name = param_name;
	}
	public String getData_type_text() {
		return data_type_text;
	}
}
