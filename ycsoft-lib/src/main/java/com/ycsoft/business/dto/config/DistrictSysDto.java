package com.ycsoft.business.dto.config;

import com.ycsoft.beans.config.TDistrict;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.tree.AddrTree;
import com.ycsoft.commons.tree.AddrTreeNode;

/**
 * <p> 扩展地址实体类 </p>
 * @author hh
 * @date Dec 29, 2009 3:52:34 PM
 */
public class DistrictSysDto extends TDistrict implements AddrTree {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4951164597206771788L;
	
	public void transform(AddrTreeNode node){
		node.setId( getDistrict_id());
		node.setPid( getParent_id());
		node.setText( getDistrict_name());
		node.setLeaf(true);
		node.setCls("file");
		if(getDistrict_level() == 0){
//			node.setHideObarAdd(true);
			node.setExpanded(true);
		}
		if(getStatus().equals(StatusConstants.ACTIVE)){
			node.setHideObarStatusactive(true);
		}else{
			node.setHideObarAdd(true);
			node.setHideObarEdit(true);
			node.setHideObarLeveladd(true);
			node.setHideObarStatusinvalid(true);
		}
//		node.setIs_leaf( getIs_leaf());
		node.getOthers().put("district_level", String.valueOf(getDistrict_level()));
		node.getOthers().put("province_id", getProvince_id());
		node.getOthers().put("remark", getRemark());
		node.getOthers().put("district_desc", getDistrict_desc());
		node.getOthers().put("status", getStatus());
	}


	
}
