package com.ycsoft.business.dto.core.prod;

import java.io.Serializable;
import java.util.List;

import com.ycsoft.beans.prod.PPackageProd;
import com.ycsoft.beans.prod.PProd;

public class PackageGroupUser implements Serializable  {
	
	//套餐内容组ID
	private String package_group_id;
    //内容组选中的终端用户id数组
    private List<String>  userSelectList;
    //套餐内容组定义
    private PPackageProd pPackageProd;
	//套餐内容组中产品内容清单
    private List<PProd>  prodList;

    
    public List<PProd> getProdList() {
		return prodList;
	}
	public void setProdList(List<PProd> prodList) {
		this.prodList = prodList;
	}

	public String getPackage_group_id() {
		return package_group_id;
	}
	public void setPackage_group_id(String package_group_id) {
		this.package_group_id = package_group_id;
	}
	public List<String> getUserSelectList() {
		return userSelectList;
	}
	public void setUserSelectList(List<String> userSelectList) {
		this.userSelectList = userSelectList;
	}
	
	public PPackageProd getpPackageProd() {
		return pPackageProd;
	}
	public void setpPackageProd(PPackageProd pPackageProd) {
		this.pPackageProd = pPackageProd;
	}
    
}
