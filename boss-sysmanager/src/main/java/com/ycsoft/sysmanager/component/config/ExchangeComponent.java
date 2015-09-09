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
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.core.Pager;

@Component
public class ExchangeComponent extends BaseComponent {
	private TExchangeDao tExchangeDao;

	public Pager<TExchange> queryExchangeRate(TExchange exchange, Integer start, Integer limit) throws Exception{
		return tExchangeDao.query(exchange,start, limit);
	}

	public void saveInvalid(String exchangeId) throws Exception{
		tExchangeDao.invalidExchange(exchangeId);
	}

	public void saveOrUpdate(TExchange exchange, String optrId) throws Exception{
		exchange.setStatus(StatusConstants.ACTIVE);
		if(StringHelper.isEmpty(exchange.getExchange_id())){
			exchange.setExchange_id(tExchangeDao.findSequence().toString());
			exchange.setOptr_id(optrId);
			exchange.setCreate_time(new java.util.Date());
			tExchangeDao.save(exchange);
		}else{
			tExchangeDao.update(exchange);
		}
	}
	
	public void setTExchangeDao(TExchangeDao tExchangeDao) {
		this.tExchangeDao = tExchangeDao;
	}
	
}
