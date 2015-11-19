package com.ycsoft.sysmanager.web.action.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ycsoft.beans.config.TProvince;
import com.ycsoft.beans.ott.TServerOttauthProd;
import com.ycsoft.beans.prod.PSpkg;
import com.ycsoft.beans.prod.PSpkgOpenbusifee;
import com.ycsoft.beans.prod.PSpkgOpenuser;
import com.ycsoft.beans.system.SAgent;
import com.ycsoft.beans.system.SDataTranslation;
import com.ycsoft.business.service.externalImpl.IOttServiceExternal;
import com.ycsoft.commons.abstracts.BaseAction;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.sysmanager.component.config.ConfigComponent;

@Controller
public class ConfigAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4463745557033215523L;
	
	@Autowired
	private ConfigComponent configComponent;
	
	private SAgent agent;
	private PSpkg spkg;
	private PSpkgOpenuser spkgUser;
	private PSpkgOpenbusifee spkgBusiFee;
	private TServerOttauthProd ottAuth;
	private IOttServiceExternal ottService;
	private String query;
	private String sp_id;
	private String id;
	private String status;
	private String type;
	
	public void setOttService(IOttServiceExternal ottService) {
		this.ottService = ottService;
	}
	
	public String queryAllOttAuth() throws Exception {
		getRoot().setRecords(configComponent.queryAllOttAuth());
		return JSON_RECORDS;
	}
	
	public String queryOttAuth() throws Exception {
		getRoot().setPage(configComponent.queryOttAuth(query, start, limit));
		return JSON_PAGE;
	}
	public String saveOttAuth() throws Exception {
		configComponent.saveOttAuth(ottAuth, type);
		if(ottAuth.getNeed_sync().equals(SystemConstants.BOOLEAN_TRUE)){
			ottService.saveSyncProd();
		}
		return JSON_SUCCESS;
	}
	
	public String querySpkg() throws Exception {
		getRoot().setPage(configComponent.querySpkg(query, start, limit));
		return JSON_PAGE;
	}
	
	public String querySpkgInfoBySpkgId() throws Exception {
		getRoot().setSimpleObj(configComponent.querySpkgInfoBySpkgId(sp_id));
		return JSON_SIMPLEOBJ;
	}
	
	public String saveSpkg() throws Exception {
		configComponent.saveSpkg(spkg);
		return JSON_SUCCESS;
	}
	
	public String saveSpkgUser() throws Exception {
		configComponent.saveSpkgUser(spkgUser);
		return JSON_SUCCESS;
	}
	
	public String saveSpkgBusiFee() throws Exception {
		configComponent.saveSpkgBusiFee(spkgBusiFee);
		return JSON_SUCCESS;
	}
	
	public String updateSpkgStatus() throws Exception {
		configComponent.updateSpkgStatus(sp_id, status);
		return JSON_SUCCESS;
	}
	
	public String updateSpkgUserStatus() throws Exception {
		configComponent.updateSpkgUserStatus(id, status);
		return JSON_SUCCESS;
	}
	
	public String updateSpkgBusiFeeStatus() throws Exception {
		configComponent.updateSpkgBusiFeeStatus(id, status);
		return JSON_SUCCESS;
	}
	
	public String deleteSpkgUser() throws Exception {
		configComponent.deleteSpkgUser(id);
		return JSON_SUCCESS;
	}
	
	public String deleteSpkgBusiFee() throws Exception {
		configComponent.deleteSpkgBusiFee(id);
		return JSON_SUCCESS;
	}
	
	public String queryDeviceFee() throws Exception {
		String deviceModel = request.getParameter("deviceModel");
		String buyMode = request.getParameter("buyMode");
		getRoot().setRecords(configComponent.queryDeviceFee(deviceModel, buyMode));
		return JSON_RECORDS;
	}
	
	public String queryBulkUserBusiFee() throws Exception {
		getRoot().setRecords(configComponent.queryBulkUserBusiFee());
		return JSON_RECORDS;
	}
	
	public String queryProvince() throws Exception {
		getRoot().setRecords(configComponent.queryProvince());
		return JSON_RECORDS;
	}
	
	public String saveProvince() throws Exception {
		String str = request.getParameter("provinces");
		List<TProvince> provinceList = new Gson().fromJson(str, new TypeToken<List<TProvince>>(){}.getType());
		configComponent.saveProvince(provinceList);
		return JSON_SUCCESS;
	}
	
	public String queryAllAgent() throws Exception {
		getRoot().setRecords(configComponent.queryAllAgent());
		return JSON_RECORDS;
	}
	
	public String queryAgent() throws Exception {
		getRoot().setPage(configComponent.queryAgent(query, start, limit));
		return JSON_PAGE;
	}
	
	public String saveAgent() throws Exception {
		configComponent.saveAgent(agent);
		return JSON_SUCCESS;
	}
	
	public String queryDataTranslation() throws Exception {
		getRoot().setPage(configComponent.queryDataTranslation(query, start, limit));
		return JSON_PAGE;
	}
	
	public String saveDataTranslation() throws Exception {
		String str = request.getParameter("dataTranslations");
		List<SDataTranslation> dataTransList = new Gson().fromJson(str, new TypeToken<List<SDataTranslation>>(){}.getType());
		configComponent.saveDataTranslation(dataTransList);
		return JSON_SUCCESS;
	}
	
	public String deleteDataTranslation() throws Exception {
		String[] ids = request.getParameterValues("dataIds");
		configComponent.deleteDataTranslation(ids);
		return JSON_SUCCESS;
	}
	
	public SAgent getAgent() {
		return agent;
	}

	public void setAgent(SAgent agent) {
		this.agent = agent;
	}
	
	public void setQuery(String query) {
		this.query = query;
	}
	
	public void setSp_id(String sp_id) {
		this.sp_id = sp_id;
	}

	public void setSpkg(PSpkg spkg) {
		this.spkg = spkg;
	}
	
	public PSpkgOpenuser getSpkgUser() {
		return spkgUser;
	}

	public void setSpkgUser(PSpkgOpenuser spkgUser) {
		this.spkgUser = spkgUser;
	}

	public PSpkgOpenbusifee getSpkgBusiFee() {
		return spkgBusiFee;
	}

	public void setSpkgBusiFee(PSpkgOpenbusifee spkgBusiFee) {
		this.spkgBusiFee = spkgBusiFee;
	}

	public PSpkg getSpkg() {
		return spkg;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public TServerOttauthProd getOttAuth() {
		return ottAuth;
	}
	public void setOttAuth(TServerOttauthProd ottAuth) {
		this.ottAuth = ottAuth;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
