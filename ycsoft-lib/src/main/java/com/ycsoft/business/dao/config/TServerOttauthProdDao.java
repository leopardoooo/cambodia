package com.ycsoft.business.dao.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.ott.TServerOttauthProd;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;

@Component
public class TServerOttauthProdDao extends BaseEntityDao<TServerOttauthProd>  {
	
	
	public List<TServerOttauthProd> queryNeedSyncDto() throws JDBCException{
		String sql="select * from t_server_ottauth_prod where need_sync='T' ";
		
		return this.createQuery(sql).list();
	}
	
	public Map<String,TServerOttauthProd> queryAllMap() throws JDBCException{
		 Map<String,TServerOttauthProd> map=new HashMap<>();
		 for(TServerOttauthProd ott:this.findAll()){
			 map.put(ott.getId(), ott);
		 }
		 return map;
	}
	
	public void updateSync(String prodId) throws JDBCException{
		String sql="update t_server_ottauth_prod set need_sync='F' ,sync_date=sysdate where id=? ";
		this.executeUpdate(sql, prodId);
	}
}
