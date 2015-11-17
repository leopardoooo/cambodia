package com.ycsoft.business.dao.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.ott.TServerOttauthProd;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;
import com.ycsoft.daos.core.Query;

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
	
	public int countByFeeId(String id, String feeId) throws Exception {
		String sql = "select count(1) from t_server_ottauth_prod where fee_id=?";
		if(StringHelper.isNotEmpty(id)){
			sql += " and id<>'"+id+"'";
		}
		return this.count(sql, feeId);
	}
	
	public Pager<TServerOttauthProd> queryOttAuth(String query, Integer start, Integer limit) throws Exception {
		String sql = "select * from t_server_ottauth_prod where 1=1 ";
		Query q = null;
		if(StringHelper.isNotEmpty(query)){
			query = "%"+query+"%";
			sql += " and name like ? or fee_name like ?";
			q = this.createQuery(sql, query, query);
		}else{
			q = this.createQuery(sql);
		}
		return q.setStart(start).setLimit(limit).page();
	}
}
