package com.ycsoft.sysmanager.web.action.config;


import com.ycsoft.beans.config.TExchange;
import com.ycsoft.commons.abstracts.BaseAction;
import com.ycsoft.commons.helper.DateHelper;
import com.ycsoft.sysmanager.component.config.ExchangeComponent;

@SuppressWarnings("serial")
public class ExchangeAction  extends BaseAction {

	private Integer oldExchange;
	private String effDate;
	
	private Integer newExchange;
	private String newEffDate;
	
	private String status;
	
	private String exchangeId;
	
	private ExchangeComponent exchangeComponent;
	
	public String query() throws Exception{
		TExchange exchange = new TExchange();
		exchange.setEff_date(DateHelper.parseDate(effDate, DateHelper.FORMAT_YMD));
		exchange.setStatus(status);
		exchange.setExchange(oldExchange);
		getRoot().setPage(exchangeComponent.queryExchangeRate(exchange,start,limit));
		return JSON_PAGE;
	}
	
	public String saveOrUpdate() throws Exception {
		TExchange exchange = new TExchange();
		exchange.setExchange_id(exchangeId);
		exchange.setEff_date(DateHelper.parseDate(newEffDate, DateHelper.FORMAT_YMD));
		exchange.setExchange(newExchange);
		exchangeComponent.saveOrUpdate(exchange,getOptr().getOptr_id());
		
		return JSON_SUCCESS;
	}

	public String invalid() throws Exception {
	
		exchangeComponent.saveInvalid(exchangeId);
		return JSON_SUCCESS;
	}

	public void setExchangeComponent(ExchangeComponent exchangeComponent) {
		this.exchangeComponent = exchangeComponent;
	}

	public void setEffDate(String effDate) {
		this.effDate = effDate;
	}

	public void setOldExchange(Integer oldExchange) {
		this.oldExchange = oldExchange;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setNewExchange(Integer newExchange) {
		this.newExchange = newExchange;
	}

	public void setNewEffDate(String newEffDate) {
		this.newEffDate = newEffDate;
	}
	
	public void setExchangeId(String exchangeId) {
		this.exchangeId = exchangeId;
	}

}
