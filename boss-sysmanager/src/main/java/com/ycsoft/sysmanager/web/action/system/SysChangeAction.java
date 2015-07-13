package com.ycsoft.sysmanager.web.action.system;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;

import com.ycsoft.beans.system.SSysChange;
import com.ycsoft.commons.abstracts.BaseAction;
import com.ycsoft.commons.constants.SysChangeType;
import com.ycsoft.daos.core.Pager;
import com.ycsoft.sysmanager.component.system.SysChangeComponent;

@Controller
public class SysChangeAction extends BaseAction {
	private SysChangeComponent sysChangeComponent;

	private SysChangeType changeType;// 异动对象
	private String id;// 内容键值
	private String id_desc;// 内容键值描述
	
	private Date beginDate;
	private Date endDate;

	/**
	 * 列出变更类型.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String listChangeTypes() throws Exception {
		SysChangeType[] types = SysChangeType.values();
		List<Map<String, Object>> records = new ArrayList<Map<String,Object>>();
		for (SysChangeType type : types) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", type.name());
			map.put("desc", type.getShowName());
			records.add(map);
		}
		getRoot().setRecords(records);
		return JSON_RECORDS;
	}
	
	/**
	 * 根据键值说明模糊查询键值.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String queryKey() throws Exception {
		getRoot().setRecords(sysChangeComponent.queryKey(changeType,this.id_desc));
		return JSON_RECORDS;
	}

	public String query() throws Exception {
		
		Pager<SSysChange> pager = sysChangeComponent.queryChangeInfo(beginDate,endDate,changeType, id,start,limit);
		pager.setStart(start);
		pager.setLimit(limit);
		getRoot().setPage(pager);
		
		return JSON_PAGE;
	}

	public void setSysChangeComponent(SysChangeComponent sysChangeComponent) {
		this.sysChangeComponent = sysChangeComponent;
	}

	public SysChangeType getChangeType() {
		return changeType;
	}

	public void setChangeType(SysChangeType changeType) {
		this.changeType = changeType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId_desc() {
		return id_desc;
	}

	public void setId_desc(String idDesc) {
		id_desc = idDesc;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
