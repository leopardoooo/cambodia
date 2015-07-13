package com.ycsoft.sysmanager.web.action.config;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ycsoft.beans.config.TCountyAcct;
import com.ycsoft.commons.abstracts.BaseAction;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.sysmanager.component.config.AcctConfigComponent;
import com.ycsoft.sysmanager.dto.config.ExtendTableDto;

@Controller
public class AcctConfigAction extends BaseAction  {
	private AcctConfigComponent acctConfigComponent;
	
	private String countyId;
	private String colony;
	
	private String acctListStr;
	private String[] acctIds;
	private String acctId;
	
	public String queryAcctConfig() throws JDBCException {
		getRoot().setPage(acctConfigComponent.queryAcctConfig(countyId,colony, start, limit));
		return JSON_PAGE;
	}
	
	public String queryAcctConfigForAdd() throws JDBCException{
		getRoot().setRecords(acctConfigComponent.queryAcctConfigForAdd(countyId,colony));
		return JSON_RECORDS;
	}
	
	public String queryAllCounty() throws JDBCException{
		getRoot().setRecords(acctConfigComponent.findViewDict(DictKey.COUNTY.toString()));
		return JSON_RECORDS;
	}
	
	public String queryAllColony() throws JDBCException{
		getRoot().setRecords(acctConfigComponent.findViewDict(DictKey.CUST_COLONY.toString()));
		return JSON_RECORDS;
	}
	
	/**
	 * 保存配置
	 * @return
	 * @throws JDBCException
	 */
	public String saveAcctConfig() throws JDBCException{
		List<TCountyAcct> acctList = new ArrayList<TCountyAcct>();
		if(StringHelper.isNotEmpty(acctListStr)){
			Type type = new TypeToken<List<TCountyAcct>>(){}.getType();
			Gson gson = new Gson();
			acctList = gson.fromJson(acctListStr,type);
		}
		acctConfigComponent.saveAcctConfig(acctList,optr);
		return JSON;
	}
	
	/**
	 * 删除配置
	 * @return
	 * @throws JDBCException
	 */
	public String deleteAcctConfig() throws JDBCException{
		acctConfigComponent.deleteAcctConfig(acctIds, optr.getOptr_id());
		return JSON;
	}
	
	/**
	 * 查询账户明细
	 * @return
	 * @throws JDBCException
	 */
	public String queryAcctDetail() throws JDBCException{
		getRoot().setPage(acctConfigComponent.queryAcctDetail(acctId,start,limit));
		return JSON_PAGE;
	}

	public void setAcctConfigComponent(AcctConfigComponent acctConfigComponent) {
		this.acctConfigComponent = acctConfigComponent;
	}


	public void setCountyId(String countyId) {
		this.countyId = countyId;
	}

	public void setColony(String colony) {
		this.colony = colony;
	}

	public void setAcctListStr(String acctListStr) {
		this.acctListStr = acctListStr;
	}

	public void setAcctIds(String[] acctIds) {
		this.acctIds = acctIds;
	}

	public void setAcctId(String acctId) {
		this.acctId = acctId;
	}
	
	
	
}
