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
		exchange.setEff_date(DateHelper.parseDate(newEffDate, DateHelper.FORMAT_YMD));
		exchange.setExchange(newExchange);
		exchangeComponent.saveOrUpdate(exchange,getOptr().getOptr_id(), oldExchange, DateHelper.parseDate(effDate, DateHelper.FORMAT_YMD));
		
		return JSON_SUCCESS;
	}

	public String invalid() throws Exception {
		TExchange exchange = new TExchange();
		exchange.setEff_date(DateHelper.parseDate(effDate, DateHelper.FORMAT_YMD));
		exchange.setExchange(oldExchange);
		exchangeComponent.saveInvalid(exchange);
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

}
