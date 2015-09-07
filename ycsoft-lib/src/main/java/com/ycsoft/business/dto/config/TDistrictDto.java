package com.ycsoft.business.dto.config;

import com.ycsoft.beans.config.TDistrict;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.tree.Tree;
import com.ycsoft.commons.tree.TreeNode;

/**
 * <p> 扩展地址实体类 </p>
 * @author hh
 * @date Dec 29, 2009 3:52:34 PM
 */
public class TDistrictDto extends TDistrict implements Tree {

	/**
	 *
	 */
	private static final long serialVersionUID = -8492116133830244486L;

	private Integer num;
	private String addr_p_name;
	private String addr_last_id;
	
	public void transform(TreeNode node){
		node.setId( getDistrict_id());
		node.setPid( getParent_id());
		node.setText( getDistrict_name());
		node.setLeaf(true);
		node.setCls("file");
		if(getDistrict_level()==4){
			node.setIs_leaf(SystemConstants.BOOLEAN_TRUE);
		}else{
			node.setIs_leaf(SystemConstants.BOOLEAN_FALSE);
		}
		node.getOthers().put("tree_level", String.valueOf(getDistrict_level()));
		node.getOthers().put("province_id", getProvince_id());
		node.getOthers().put("district_id", String.valueOf(getDistrict_id()));
		node.getOthers().put("status", getStatus());
	}

	
	public String getAddr_last_id() {
		return addr_last_id;
	}


	public void setAddr_last_id(String addr_last_id) {
		this.addr_last_id = addr_last_id;
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
