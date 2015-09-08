package com.ycsoft.business.dto.core.prod;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ycsoft.beans.prod.PPackageProd;
import com.ycsoft.beans.prod.PProd;

public class PackageGroupUser extends PPackageProd  implements Serializable  {
	
    //内容组选中的终端用户id数组
    private List<String>  userSelectList=new ArrayList<>();
	//套餐内容组中产品内容清单
    private List<PProd>  prodList=new ArrayList<>();

    
    public PackageGroupUser(PPackageProd pakProd){
    	this.setMax_user_cnt(pakProd.getMax_user_cnt());
    	this.setPackage_group_id(pakProd.getPackage_group_id());
    	this.setPackage_group_name(pakProd.getPackage_group_name());
    	this.setProd_list(pakProd.getProd_list());
    	this.setUser_type(pakProd.getUser_type());
    	this.setTerminal_type(pakProd.getTerminal_type());
    	this.setPackage_id(pakProd.getPackage_id());
    	this.setPrecent(pakProd.getPrecent());
    	this.setProd_type(pakProd.getProd_type());
    }
    
    public PackageGroupUser(){}
    
    public List<PProd> getProdList() {
		return prodList;
	}
	public void setProdList(List<PProd> prodList) {
		this.prodList = prodList;
	}

	public List<String> getUserSelectList() {
		return userSelectList;
	}
	public void setUserSelectList(List<String> userSelectList) {
		this.userSelectList = userSelectList;
	}
	
}
