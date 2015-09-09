package com.ycsoft.sysmanager.component.config;

import java.util.List;
import org.springframework.stereotype.Component;
import com.ycsoft.beans.config.TExchange;
import com.ycsoft.business.dao.config.TExchangeDao;
import com.ycsoft.commons.abstracts.BaseComponent;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.exception.ComponentException;
import com.ycsoft.commons.exception.ErrorCode;
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
		
		if(StringHelper.isEmpty(exchange.getExchange_id())){
			List<TExchange>  list=tExchangeDao.queryByEffDate(exchange.getEff_date());
			if(list!=null&&list.size()>0){
				throw new ComponentException(ErrorCode.ExchangeConfigExits);
			}
			exchange.setStatus(StatusConstants.ACTIVE);
			exchange.setExchange_id(tExchangeDao.findSequence().toString());
			exchange.setOptr_id(optrId);
			exchange.setCreate_time(new java.util.Date());
			tExchangeDao.save(exchange);
		}else{
			TExchange tx=tExchangeDao.findByKey(exchange.getExchange_id());
			if(!tx.getStatus().equals(StatusConstants.ACTIVE)){
				throw new ComponentException(ErrorCode.ExchangeConfigINvalid);
			}
			tExchangeDao.update(exchange);
		}
	}
	
	public void setTExchangeDao(TExchangeDao tExchangeDao) {
		this.tExchangeDao = tExchangeDao;
	}
	
}
