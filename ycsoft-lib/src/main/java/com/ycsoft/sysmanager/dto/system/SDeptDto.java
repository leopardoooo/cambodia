package com.ycsoft.sysmanager.dto.system;

import com.ycsoft.beans.system.SDept;
import com.ycsoft.commons.tree.Tree;
import com.ycsoft.commons.tree.TreeNode;

/**
 *
 * @author eaglesnail
 *
 */
public class SDeptDto extends SDept implements Tree {
	/**
	 *
	 */
	private static final long serialVersionUID = 5818033123462387263L;

	private String address_name_src; 
	private String address_id_src; 
	
	public void transform(TreeNode node) {
		node.setId(getDept_id());
		node.setPid(getDept_pid());
		node.setAttr(getDept_type_text());
		node.setText(getDept_name());
		node.setAttr_src(getAddress_name_src());
		node.setAttr_src_id(getAddress_id_src());
		node.setOther_id(getAgent_id());
		node.setOther_name(getAgent_name());
		node.setLeaf(true);
		node.getOthers().put("dept_type",getDept_type());
		node.getOthers().put("countyId", getCounty_id());
		node.getOthers().put("areaId", getArea_id());
	}

	public void setAddress_name_src(String address_name_src) {
		this.address_name_src = address_name_src;
	}

	public String getAddress_name_src() {
		return address_name_src;
	}

	public String getAddress_id_src() {
		return address_id_src;
	}

	public void setAddress_id_src(String address_id_src) {
		this.address_id_src = address_id_src;
	}
}
