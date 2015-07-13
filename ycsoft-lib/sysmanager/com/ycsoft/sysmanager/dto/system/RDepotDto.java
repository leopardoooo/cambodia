package com.ycsoft.sysmanager.dto.system;

import com.ycsoft.beans.depot.RDepotDefine;
import com.ycsoft.commons.tree.Tree;
import com.ycsoft.commons.tree.TreeNode;

public class RDepotDto extends RDepotDefine implements Tree {
	/**
	 *
	 */
	private static final long serialVersionUID = -1748165260392854800L;
	private String district;
	private String is_leaf;

	public void transform(TreeNode node) {
		node.setId( getDepot_id());
		node.setPid( getDepot_pid());
		node.setText( getDepot_name());
		node.setLeaf(true);
		node.setCls("file");
		node.setIs_leaf(is_leaf);
		node.getOthers().put("county_id", getCounty_id());
		node.getOthers().put("area_id", getArea_id());
		node.getOthers().put("county_name", getCounty_name());
		node.getOthers().put("area_name", getArea_name());
	}

	public String getIs_leaf() {
		return is_leaf;
	}

	public void setIs_leaf(String is_leaf) {
		this.is_leaf = is_leaf;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}
}
