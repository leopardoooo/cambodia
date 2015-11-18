package com.ycsoft.sysmanager.web.action.config;

import java.util.Date;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;

import com.ycsoft.beans.config.TServerRes;
import com.ycsoft.beans.prod.PRes;
import com.ycsoft.beans.prod.PResgroup;
import com.ycsoft.commons.abstracts.BaseAction;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.sysmanager.component.config.ResComponent;
import com.ycsoft.sysmanager.dto.prod.ResGroupDto;
@Controller
public class ResAction extends BaseAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7093257458035293363L;
	private String query;
	private String doneId;
	private String records;
	private ResGroupDto resGroupDto;
	private ResComponent resComponent;
	private PRes res;
	private TServerRes serverRes;
	private TServerRes oldServerRes;
	private String resId;
	private String resName;
	
	private String groupId;
	
	public String queryServerByServType() throws Exception {
		String servType = request.getParameter("serv_type");
		getRoot().setRecords(resComponent.queryServerByServType(servType));
		return JSON_RECORDS;
	}
	
	//查询所有t_server
	public String queryServerByCountyId() throws Exception {
		getRoot().setRecords(resComponent.queryServerByCountyId(optr.getCounty_id()));
		return JSON_RECORDS;
	}

	//查询所有pres
	public String queryPRes() throws Exception {
		getRoot().setRecords(resComponent.queryAllRes());
		return JSON_RECORDS;
	}
	
	//查询所有t_server_res
	public String queryServerRes() throws Exception{
		getRoot().setPage(resComponent.queryServerRes(resId,optr.getCounty_id(), query, start, limit));
		return JSON_PAGE;
	}
	
	//分页查询资源信息
	public String queryRes() throws Exception {
		String servId = request.getParameter("servId");
		getRoot().setPage(resComponent.queryRes(servId,query, optr.getCounty_id(), start, limit));
		return JSON_PAGE;
	}
	
	public String saveServerRes() throws Exception {
		serverRes.setArea_id(optr.getArea_id());
		serverRes.setCounty_id(optr.getCounty_id());
		resComponent.saveServerRes(serverRes,oldServerRes);
		return JSON;
	}
	
	public String deleteServerRes() throws Exception {
		resComponent.deleteServerRes(serverRes);
		return JSON;
	}
	
	public String updateRes() throws Exception {
		resComponent.updateRes(res);
		return JSON;
	}
	
	public String getResByResName() throws Exception {
		getRoot().setSimpleObj(resComponent.getResByResName(resName));
		return JSON_SIMPLEOBJ;
	}
	
	public String saveRes() throws Exception {
		res.setOptr_id(optr.getOptr_id());
		res.setCounty_id(optr.getCounty_id());
		res.setArea_id(optr.getArea_id());
		getRoot().setSimpleObj(resComponent.saveRes(res));
		return JSON;
	}
	public String deleteRes() throws Exception {
		resComponent.deleteRes(resId);
		return JSON;
	}
	
	public String activeRes() throws Exception{
		resComponent.activeRes(resId);
		return JSON;
	}

	//查询资源 资源组信息
	public String queryAllRes()throws Exception{
		getRoot().setPage(resComponent.queryAllRes(start, limit,query,optr.getCounty_id()));
		return JSON_PAGE;
	}
	
	public String queryResById() throws Exception{
		getRoot().setSimpleObj(resComponent.queryResById(doneId));
		return JSON_SIMPLEOBJ;
	}
	
	public String delteResGroup() throws Exception{
		resComponent.delteResGroup(groupId,optr);
		return JSON;
	}
	
	public String queryResByServId()throws Exception{
		getRoot().setRecords(resComponent.getResByServId(doneId,optr.getCounty_id()));
		return JSON_RECORDS;
		
	}
	public String queryResByGroupId()throws Exception{
		getRoot().setRecords(resComponent.queryResByGroupId(doneId));
		return JSON_RECORDS;
		
	}
	public String saveResGroup()throws Exception{
		PResgroup resdto = new PResgroup();
		BeanUtils.copyProperties(resGroupDto, resdto);
		resdto.setArea_id(optr.getArea_id());
		resdto.setCounty_id(optr.getCounty_id());
		resdto.setOptr_id(optr.getOptr_id());
		resdto.setCreate_time( new Date() );
		if(StringHelper.isNotEmpty(resdto.getGroup_id())){
			resComponent.updateRes(resdto,records,optr);
		}else{
			resComponent.saveRes(resdto,records,optr);
		}
		return JSON;
		
	}
	
	
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}

	public void setResComponent(ResComponent resComponent) {
		this.resComponent = resComponent;
	}

	public ResComponent getResComponent() {
		return resComponent;
	}




	public String getDoneId() {
		return doneId;
	}




	public void setDoneId(String doneId) {
		this.doneId = doneId;
	}

	public ResGroupDto getResGroupDto() {
		return resGroupDto;
	}

	public void setResGroupDto(ResGroupDto resGroupDto) {
		this.resGroupDto = resGroupDto;
	}

	public String getRecords() {
		return records;
	}

	public void setRecords(String records) {
		this.records = records;
	}
	public PRes getRes() {
		return res;
	}
	public void setResId(String resId) {
		this.resId = resId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public void setRes(PRes res) {
		this.res = res;
	}

	public TServerRes getServerRes() {
		return serverRes;
	}

	public void setServerRes(TServerRes serverRes) {
		this.serverRes = serverRes;
	}

	public TServerRes getOldServerRes() {
		return oldServerRes;
	}

	public void setOldServerRes(TServerRes oldServerRes) {
		this.oldServerRes = oldServerRes;
	}

	public void setResName(String resName) {
		this.resName = resName;
	}
	
}
