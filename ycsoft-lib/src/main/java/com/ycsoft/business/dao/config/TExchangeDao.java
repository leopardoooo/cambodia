package com.ycsoft.business.dao.config;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TExchange;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;

@Component
public class TExchangeDao extends BaseEntityDao<TExchange> {
	/**
	 * 提取生效的汇率
	 * @return
	 * @throws JDBCException 
	 */
	public Integer getExchange() throws JDBCException{
		String sql="select * from t_exchange where status=? and eff_date <=sysdate order by eff_date desc ";
		TExchange e= this.createQuery(sql, StatusConstants.ACTIVE).first();
		if(e==null){
			return 0;
		}else{
			return e.getExchange();
		}
	}
}
