package com.ycsoft.sysmanager.component.config;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TExchange;
import com.ycsoft.business.dao.config.TExchangeDao;
import com.ycsoft.commons.abstracts.BaseComponent;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.exception.ComponentException;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.daos.core.Pager;

@Component
public class ExchangeComponent extends BaseComponent {
	private TExchangeDao tExchangeDao;



	public Pager<TExchange> queryExchangeRate(TExchange exchange, Integer start, Integer limit) throws Exception{
		return tExchangeDao.query(exchange,start, limit);
	}

	public void saveInvalid(TExchange exchange) throws Exception{
		tExchangeDao.invalidExchange(exchange);
	}

	public void saveOrUpdate(TExchange exchange, String optrId, Integer oldExchange, Date oldEffDate) throws Exception{
		TExchange queryCopy = new TExchange();
		queryCopy.setExchange(oldExchange);
		queryCopy.setEff_date(oldEffDate);
		
		exchange.setStatus(StatusConstants.ACTIVE);
		List<TExchange> findByEntity = null;
		if(null !=oldExchange || oldEffDate != null){
			findByEntity = tExchangeDao.findByEntity(queryCopy);
		}
		
		try {
			if(CollectionHelper.isEmpty(findByEntity)){
				exchange.setOptr_id(optrId);
				exchange.setCreate_time(new java.util.Date());
				tExchangeDao.save(exchange);
			}else{
				tExchangeDao.updateExchange(exchange, oldExchange, oldEffDate);
			}
		} catch (Exception e) {
			throw new ComponentException(e.getMessage());
		}
		
	}
	
	
	public void setTExchangeDao(TExchangeDao tExchangeDao) {
		this.tExchangeDao = tExchangeDao;
	}
	
}
