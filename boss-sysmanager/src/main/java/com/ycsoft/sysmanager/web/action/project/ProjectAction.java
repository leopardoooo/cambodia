package com.ycsoft.sysmanager.web.action.project;

import org.springframework.stereotype.Controller;


import com.ycsoft.beans.project.RProject;
import com.ycsoft.business.dto.project.QueryProject;
import com.ycsoft.commons.abstracts.BaseAction;
import com.ycsoft.sysmanager.component.project.ProjectComponent;

@Controller
public class ProjectAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7167819119874002692L;
	private ProjectComponent projectComponent;
	
	private RProject project;
	private QueryProject queryProject;
	private String projectId;
	private String projectNumber;
	private String addrIds;
	private String isValid;
	private String addrPid;
	private String countyId;
	
	private Integer start;
	private Integer limit;
	
	public String queryProject() throws Exception{
		getRoot().setPage(projectComponent.queryProject(queryProject,optr.getCounty_id(),start,limit));
		return JSON_PAGE;
	}
	
	public String saveProject() throws Exception{
		getRoot().setSimpleObj( projectComponent.saveProject(project) );
		return JSON;
	}
	
	/**
	 * 项目小区关联
	 * @return
	 * @throws Exception
	 */
	public String saveLinkedProject() throws Exception {
		getRoot().setSimpleObj(projectComponent.saveLinkedProject(projectId, addrIds));
		return JSON;
	}
	
	/**
	 * 查询关联项目的小区
	 * @return
	 * @throws Exception
	 */
	public String queryAddrByProjectId() throws Exception{
		getRoot().setRecords(projectComponent.queryAddrByProjectId(projectId));
		return JSON_RECORDS;
	}
	
	/**
	 * 项目失效、生效
	 * @param isValid 失效 'F', 生效 'T'
	 * @return
	 * @throws Exception
	 */
	public String isInvalid() throws Exception{
		projectComponent.isInvalid(projectId, isValid);
		return JSON;
	}
	
	public String queryAddrDistrict() throws Exception {
		getRoot().setRecords(projectComponent.queryAddrDistrict(countyId));
		return JSON_RECORDS;
	}
	
	public String queryAddrCommunity() throws Exception {
		getRoot().setRecords(projectComponent.queryAddrCommunity(addrPid));
		return JSON_RECORDS;
	}
	
	public String queryByCountyId() throws Exception {
		getRoot().setRecords(projectComponent.queryByCountyId(optr.getCounty_id()));
		return JSON_RECORDS;
	}
	
	public String checkProjectNumber() throws Exception {
		getRoot().setSuccess(projectComponent.checkProjectNumber(projectNumber));
		return JSON_SUCCESS;
	}

	public QueryProject getQueryProject() {
		return queryProject;
	}

	public void setQueryProject(QueryProject queryProject) {
		this.queryProject = queryProject;
	}
	
	public void setProjectComponent(ProjectComponent projectComponent) {
		this.projectComponent = projectComponent;
	}

	public RProject getProject() {
		return project;
	}

	public void setProject(RProject project) {
		this.project = project;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setAddrIds(String addrIds) {
		this.addrIds = addrIds;
	}

	public void setAddrPid(String addrPid) {
		this.addrPid = addrPid;
	}

	public void setProjectNumber(String projectNumber) {
		this.projectNumber = projectNumber;
	}

	public void setCountyId(String countyId) {
		this.countyId = countyId;
	}
	

}
