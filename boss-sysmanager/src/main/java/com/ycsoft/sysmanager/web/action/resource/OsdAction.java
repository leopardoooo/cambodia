package com.ycsoft.sysmanager.web.action.resource;

import java.util.Date;

import org.springframework.stereotype.Controller;

import com.ycsoft.beans.config.TOsdPhrase;
import com.ycsoft.beans.config.TOsdStop;
import com.ycsoft.beans.core.job.JCaCommand;
import com.ycsoft.business.component.config.MemoryComponent;
import com.ycsoft.commons.abstracts.BaseAction;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.core.Pager;
import com.ycsoft.sysmanager.component.resource.OsdComponent;

@Controller
public class OsdAction extends BaseAction {
	/**
	 *
	 */
	private static final long serialVersionUID = -1227977321692753554L;

	private OsdComponent osdComponent;
	private TOsdPhrase phrase;
	private String transnum;
	private TOsdStop stop;
	private MemoryComponent memoryComponent;
	
	/**
	 * 停止所有OSD.
	 * @return
	 * @throws Exception
	 */
	public String stopAll() throws Exception {
		stop.setOp_time(new Date());
		stop.setOptr_id(getOptr().getOptr_id());
		stop.setStatus(StatusConstants.ACTIVE);
		osdComponent.stopAll(stop);
		return JSON_SUCCESS;
	}
	
	/**
	 * 取消停止所有OSD.
	 * @return
	 * @throws Exception
	 */
	public String queryLatestStopAllDate() throws Exception {
		TOsdStop stop = osdComponent.queryLatestStop();
		getRoot().setSimpleObj(stop);
		return JSON_SIMPLEOBJ;
	}
	
	/**
	 * 取消停止所有OSD.
	 * @return
	 * @throws Exception
	 */
	public String cancelStopAll() throws Exception {
		osdComponent.cancelStopAll();
		return JSON_SUCCESS;
	}
	
	/**
	 * 新增和保存词组.
	 * @return
	 * @throws Exception
	 */
	public String souPhrase() throws Exception {
		osdComponent.saveOrUpdatePhrase(phrase);
		memoryComponent.addDictSignal("");
		return JSON_SUCCESS;
	}
	
	/**
	 * 删除词组.
	 * @return
	 * @throws Exception
	 */
	public String removePhrase() throws Exception {
		osdComponent.removePhrase(phrase);
		memoryComponent.addDictSignal("");
		return JSON_SUCCESS;
	}
	
	/**
	 * 手工非法OSD.
	 * @return
	 * @throws Exception
	 */
	public String invalidOsd() throws Exception {
		osdComponent.invalidOsd(transnum);
		return JSON_SUCCESS;
	}
	
	/** 
	 * 查询所有合法的osd词组.
	 * @return
	 * @throws Exception
	 */
	public String queryOsdPhrase() throws Exception{
		start = null == start ? 0 : start;
		limit = null == limit ? 20 : limit;
		Pager<TOsdPhrase> page = osdComponent.queryOsdPhrase(start,limit);
		getRoot().setPage(page);
		return JSON_PAGE;
	}

	/** 
	 * 查询今日已发送的OSD.
	 * @return
	 * @throws Exception
	 */
	public String querySended() throws Exception{
		start = null == start ? 0 : start;
		limit = null == limit ? 20 : limit;
		Pager<JCaCommand> page = osdComponent.querySended(start,limit);
		getRoot().setPage(page);
		return JSON_PAGE;
	}

	/** 
	 * 查询还在待发送队列的OSD.
	 * @return
	 * @throws Exception
	 */
	public String queryQueued() throws Exception{
		start = null == start ? 0 : start;
		limit = null == limit ? 20 : limit;
		Pager<JCaCommand> page = osdComponent.queryQueued(start,limit);
		getRoot().setPage(page);
		return JSON_PAGE;
	}
	
	public String queryErrorData() throws Exception{
		start = null == start ? 0 : start;
		limit = null == limit ? 20 : limit;
		getRoot().setPage(osdComponent.queryErrorData(start,limit));
		return JSON_PAGE;
	}
	
	
	/**
	 * @return the phrase
	 */
	public TOsdPhrase getPhrase() {
		return phrase;
	}

	/**
	 * @param phrase the phrase to set
	 */
	public void setPhrase(TOsdPhrase phrase) {
		this.phrase = phrase;
	}

	/**
	 * @param osdComponent the osdComponent to set
	 */
	public void setOsdComponent(OsdComponent osdComponent) {
		this.osdComponent = osdComponent;
	}

	/**
	 * @return the transnum
	 */
	public String getTransnum() {
		return transnum;
	}

	/**
	 * @param transnum the transnum to set
	 */
	public void setTransnum(String transnum) {
		this.transnum = transnum;
	}

	/**
	 * @return the stop
	 */
	public TOsdStop getStop() {
		return stop;
	}

	/**
	 * @param stop the stop to set
	 */
	public void setStop(TOsdStop stop) {
		this.stop = stop;
	}

	/**
	 * @param memoryComponent the memoryComponent to set
	 */
	public void setMemoryComponent(MemoryComponent memoryComponent) {
		this.memoryComponent = memoryComponent;
	}
}
