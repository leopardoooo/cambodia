package com.ycsoft.sysmanager.web.action.system;



import org.springframework.stereotype.Controller;

import com.ycsoft.beans.config.TNonresCustApproval;
import com.ycsoft.commons.abstracts.BaseAction;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.sysmanager.component.system.AddressComponent;

@Controller
public class CustColonyAction extends BaseAction{

	/**
	 *
	 */
	private static final long serialVersionUID = -4183471848212000275L;

	private AddressComponent addressComponent;
	
	private String query;
	private String years;
	private String cust_colony;
	private String cust_class;
	private Integer custNum;
	private Integer userNum;
	private String countys;
	private TNonresCustApproval nonresCustApp;
	private String appId;
	
	
	public String queryCustColony()throws Exception{ 
		getRoot().setPage( addressComponent.queryCustColony(start, limit,query,optr));
		return JSON_PAGE;  
	}

	public String getCustColony(){
		getRoot().setRecords(MemoryDict.getDicts(DictKey.CUST_COLONY));
		return JSON_RECORDS;
	}
	
	public String getCustClass(){
		getRoot().setRecords(MemoryDict.getDicts(DictKey.CUST_CLASS));
		return JSON_RECORDS;
	}
	
	public String saveCustColony() throws Exception{
		addressComponent.saveCustColony(years,cust_colony,cust_class,countys,custNum,userNum,optr);
		return JSON;
	}
	public String updateCustColony() throws Exception{
		addressComponent.updateCustColony(years,cust_colony,cust_class,countys,custNum,userNum);
		return JSON;
	}
	
	public String deleteCustColony() throws Exception{
		getRoot().setSuccess(addressComponent.deleteCustColony(years,cust_colony,cust_class,countys));
		return JSON;
	}
	
	public String queryNonresCustApp() throws Exception {
		getRoot().setPage(addressComponent.queryNonresCustApp(query, start, limit));
		return JSON_PAGE;
	}
	
	public String updateNonresCustApp() throws Exception {
		addressComponent.updateNonresCustApp(nonresCustApp);
		return JSON;
	}
	
	public String deleteNonresCustApp() throws Exception {
		addressComponent.deleteNonresCustApp(appId);
		return JSON;
	}
	
	public void setYears(String years) {
		this.years = years;
	}

	public void setCust_colony(String custColony) {
		cust_colony = custColony;
	}

	public void setCustNum(Integer custNum) {
		this.custNum = custNum;
	}

	public void setCountys(String countys) {
		this.countys = countys;
	}
	
	public void setAddressComponent(AddressComponent addressComponent) {
		this.addressComponent = addressComponent;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public TNonresCustApproval getNonresCustApp() {
		return nonresCustApp;
	}

	public void setNonresCustApp(TNonresCustApproval nonresCustApp) {
		this.nonresCustApp = nonresCustApp;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public void setCust_class(String cust_class) {
		this.cust_class = cust_class;
	}

	public void setUserNum(Integer userNum) {
		this.userNum = userNum;
	}

	
	
}
