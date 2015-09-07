package com.ycsoft.business.dao.config;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TExchange;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;

@SuppressWarnings("serial")
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
	
	public void updateExchange(TExchange exchange,Integer oldExchange,Date effDate) throws Exception {
		String sql = "update t_exchange set status = ?,eff_date = ?,exchange = ?  where eff_date = ? and exchange = ?";

		executeUpdate(sql, StatusConstants.ACTIVE, exchange.getEff_date(), exchange.getExchange(),effDate,oldExchange);
	}

	/**
	 * 作废.
	 * @param exchange
	 * @throws JDBCException 
	 */
	public void invalidExchange(TExchange exchange) throws JDBCException {
		String sql = "update t_exchange set status = ? where eff_date = ? and exchange = ?";
		
		executeUpdate(sql, StatusConstants.INVALID,exchange.getEff_date(),exchange.getExchange());
	}

	
	public Pager<TExchange> query(TExchange query, Integer start,Integer limit) throws JDBCException {
		if(query == null){
			query = new TExchange();
		}
		Date eff_date = query.getEff_date();
		String status = query.getStatus();
		Integer exchange = query.getExchange();
		String sql = "SELECT T.* FROM t_exchange t WHERE 1=1 "
				+ ( StringHelper.isNotEmpty(status) ? " AND t.status = '" + status + "' " : " "  )
				+ ( exchange != null ? " AND t.exchange = " + exchange + " " : " "  )
				+ ( eff_date != null ? " AND t.eff_date >= ? " : " "  )
				;
		Object [] params = null;
		if(null != eff_date){
			params = new Object[1];
			params[0] = eff_date;
		}
		return createQuery(sql, params).setStart(start).setLimit(limit).page();
	}
}
