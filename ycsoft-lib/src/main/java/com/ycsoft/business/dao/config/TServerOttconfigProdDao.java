package com.ycsoft.business.dao.config;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.ott.TServerOttauthFee;
import com.ycsoft.beans.ott.TServerOttauthProd;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;

@Component
public class TServerOttconfigProdDao extends BaseEntityDao<TServerOttauthProd>  {
	
	
	public List<TServerOttauthProd> queryNeedSyncDto() throws JDBCException{
		String sql="select * from t_server_ottauth_prod where need_sync='T' ";
		
		return this.createQuery(sql).list();
	}
	
	public List<TServerOttauthFee> queryFee(String prodId) throws JDBCException{
		String sql="select * from t_server_ottauth_fee where prod_id=? ";
		return this.createQuery(TServerOttauthFee.class,sql,prodId).list();
	}

	public void updateSync(String prodId) throws JDBCException{
		String sql="update t_server_ottauth_prod set need_sync='F' ,sync_date=sysdate where id=? ";
		this.executeUpdate(sql, prodId);
	}
}
