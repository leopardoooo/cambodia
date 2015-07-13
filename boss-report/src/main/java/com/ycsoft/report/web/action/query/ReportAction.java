package com.ycsoft.report.web.action.query;

import org.springframework.stereotype.Controller;

import com.ycsoft.commons.abstracts.BaseAction;
import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.commons.helper.JsonHelper;
import com.ycsoft.report.bean.RepTask;
import com.ycsoft.report.component.query.QueryComponent;
import com.ycsoft.report.pojo.Parameter;
import com.ycsoft.report.query.datarole.BaseDataControl;
import com.ycsoft.report.query.datarole.FuncType;

/**
 * 初始化查询
 * 
 */
@Controller
public class ReportAction extends BaseAction {

	private QueryComponent queryComponent;
	private Parameter parameter;
	protected String rep_id;
	protected String query_id;
	protected String client_ip;
	private String reptask;
	private String task_id;
	private String query;
	
	/**
	 * 保存任务配置
	 * 配置一个任务配置权限（未实现，目前用报表编辑权限来实现）
	 * rep_id,task_name,task_type,task_execday,remark
	 */
	public String saveRepTask()throws Exception{
		//TODO 应该配置一个独立的配置任务权限
		if (BaseDataControl.getRole().hasFunc(FuncType.EDITREP)){
			RepTask task=JsonHelper.toObject(reptask, RepTask.class);
			queryComponent.saveTask(task,parameter, optr);
		}else{
			throw new ReportException("权限不足!");
		}
		return JSON_SUCCESS;
	}
	
	/**
	 * 删除一个任务配置
	 * @return
	 * @throws Exception
	 */
	public String deleteRepTask()throws Exception{
		queryComponent.deleteRepTask(Integer.valueOf(task_id));
		return JSON_SUCCESS;
	}
	
	/**
	 * 查询任务列表
	 * 具有编辑报表权限的人，可以下载删除所有任务列表，否则只能下载删除自己配置的报表
	 * 
	 */
	public String queryTasks()throws Exception{
		getRoot().setPage(queryComponent.queryRepTask(query, start, limit,optr));
		return JSON;
	}
	
	public String getReptask() {
		return reptask;
	}

	public void setReptask(String reptask) {
		this.reptask = reptask;
	}

	public String getTask_id() {
		return task_id;
	}

	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}

	/**
	 * 查询按钮提交初始化报表结果集
	 * 如果Session中上一个查询初始化还未完成，则提示操作员等待初始化完成(配置一个拦截器，处理这种情况)
	 * @return
	 * @throws Exception
	 */
	public String initQuery() throws Exception {
		//如果Session中上一个查询初始化还未完成，则提示操作员等待初始化完成(配置一个拦截器，处理这种情况)
		
		getRoot().setSimpleObj(queryComponent.initQuery(rep_id,getParameter(), optr,client_ip));
		return JSON_SIMPLEOBJ;
	}
	public QueryComponent getQueryComponent() {
		return queryComponent;
	}

	public void setQueryComponent(QueryComponent queryComponent) {
		this.queryComponent = queryComponent;
	}

	public String getRep_id() {
		return rep_id;
	}

	public void setRep_id(String rep_id) {
		this.rep_id = rep_id;
	}

	public String getQuery_id() {
		return query_id;
	}

	public void setQuery_id(String query_id) {
		this.query_id = query_id;
	}

	public Parameter getParameter() {
		return  parameter = parameter == null
		? new Parameter()
		: parameter;
	}

	public void setParameter(Parameter parameter) {
		this.parameter = parameter;
	}

	public String getClient_ip() {
		return client_ip;
	}
	public void setClient_ip(String client_ip) {
		this.client_ip = client_ip;
	}

	public void setQuery(String query) {
		this.query = query;
	}

}
