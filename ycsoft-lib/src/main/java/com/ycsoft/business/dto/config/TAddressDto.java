package com.ycsoft.business.dto.config;

import com.ycsoft.beans.config.TAddress;
import com.ycsoft.commons.tree.Tree;
import com.ycsoft.commons.tree.TreeNode;

/**
 * <p> 扩展地址实体类 </p>
 * @author hh
 * @date Dec 29, 2009 3:52:34 PM
 */
public class TAddressDto extends TAddress implements Tree {

	/**
	 *
	 */
	private static final long serialVersionUID = -8492116133830244486L;

	private Integer num;
	private String addr_p_name;
	
	public void transform(TreeNode node){
		node.setId( getAddr_id());
		node.setPid( getAddr_pid());
		node.setText( getAddr_name());
		node.setLeaf(true);
		node.setCls("file");
		node.setIs_leaf( getIs_leaf());
		node.getOthers().put("fullName", getAddr_full_name());
		node.getOthers().put("tree_level", String.valueOf(getTree_level()));
		node.getOthers().put("area_id", getArea_id());
		node.getOthers().put("county_id", getCounty_id());
		node.getOthers().put("status", getStatus());
		node.getOthers().put("net_type", getNet_type());
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public String getAddr_p_name() {
		return addr_p_name;
	}

	public void setAddr_p_name(String addrPName) {
		addr_p_name = addrPName;
	}

}
