package com.ycsoft.business.dto.config;

import com.ycsoft.beans.config.TAddress;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.tree.AddrTree;
import com.ycsoft.commons.tree.AddrTreeNode;

/**
 * <p> 扩展地址实体类 </p>
 * @author hh
 * @date Dec 29, 2009 3:52:34 PM
 */
public class TAddressDto extends TAddress implements AddrTree {

	/**
	 *
	 */
	private static final long serialVersionUID = -8492116133830244486L;

	private Integer num;
	private String addr_p_name;
	
	public void transform(AddrTreeNode node){
		node.setId( getAddr_id());
		node.setPid( getAddr_pid());
		node.setText( getAddr_name());
		node.setLeaf(true);
		node.setCls("file");
		if(getTree_level()!=1){
			node.setHideObarLeveladd(true);
		}
		if(getStatus().equals(StatusConstants.ACTIVE)){
			node.setHideObarStatusactive(true);
		}else{
			node.setHideObarStatusinvalid(true);
		}
		node.setIs_leaf( getIs_leaf());
		node.getOthers().put("fullName", getAddr_full_name());
		node.getOthers().put("tree_level", String.valueOf(getTree_level()));
		node.getOthers().put("area_id", getArea_id());
		node.getOthers().put("county_id", getCounty_id());
		node.getOthers().put("disableObarEdit", getBusi_optr_id());
		node.getOthers().put("busi_optr_name", getBusi_optr_name());
		node.getOthers().put("serv_optr_id", getServ_optr_id());
		node.getOthers().put("serv_optr_name", getServ_optr_name());
		node.getOthers().put("status", getStatus());
		if(null != getCapacity()){
			node.getOthers().put("capacity", getCapacity().toString());
		}
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
