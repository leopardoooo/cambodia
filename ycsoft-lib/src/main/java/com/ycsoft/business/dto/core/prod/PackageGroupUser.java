package com.ycsoft.business.dto.core.prod;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.ycsoft.beans.prod.PPackageProd;
import com.ycsoft.beans.prod.PPackageProdDetail;
import com.ycsoft.beans.prod.PProd;

public class PackageGroupUser implements Serializable  {
	
	//套餐内容组ID
	private String package_group_id;
    //内容组选中的终端用户id数组
    private List<String>    userSelectList;
	//产品组选中的产品：Map<packege_prod_content_id,List<prod_id>>
    private Map<String,List<String>>    prodSelectMap;
   
    //套餐内容组定义
    private PPackageProd pPackageProd;
    //套餐内容产品组定义和prod_list对应的所有产品信息
    private Map<PPackageProdDetail,List<PProd>> prodcontentMap;
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
	public Map<String, List<String>> getProdSelectMap() {
		return prodSelectMap;
	}
	public void setProdSelectMap(Map<String, List<String>> prodSelectMap) {
		this.prodSelectMap = prodSelectMap;
	}
	public PPackageProd getpPackageProd() {
		return pPackageProd;
	}
	public void setpPackageProd(PPackageProd pPackageProd) {
		this.pPackageProd = pPackageProd;
	}
	public Map<PPackageProdDetail, List<PProd>> getProdcontentMap() {
		return prodcontentMap;
	}
	public void setProdcontentMap(
			Map<PPackageProdDetail, List<PProd>> prodcontentMap) {
		this.prodcontentMap = prodcontentMap;
	}
    
}
