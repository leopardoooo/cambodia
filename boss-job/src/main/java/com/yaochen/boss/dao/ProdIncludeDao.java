package com.yaochen.boss.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.cust.CCust;
import com.ycsoft.beans.core.prod.CProdInclude;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;

@Component
public class ProdIncludeDao extends BaseEntityDao<CCust> {

	public ProdIncludeDao(){}
	
	
	//设置产品之间的关系
	public void saveProdInclude(String userId,List<CProdInclude> includeList) throws Exception{
		for (CProdInclude include :includeList){
			String sql = "insert into c_prod_include_lxr (cust_id,user_id,prod_sn,include_prod_sn) " +
					" values (?,?,?,?)";
			executeUpdate(sql,include.getCust_id(),include.getUser_id(),include.getProd_sn(),include.getInclude_prod_sn());
		}
	} 
}
