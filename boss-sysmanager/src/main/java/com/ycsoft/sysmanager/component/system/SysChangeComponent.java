package com.ycsoft.sysmanager.component.system;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.ycsoft.beans.system.SItemvalue;
import com.ycsoft.beans.system.SSysChange;
import com.ycsoft.commons.abstracts.BaseComponent;
import com.ycsoft.commons.constants.SysChangeType;
import com.ycsoft.daos.core.Pager;

@Component
public class SysChangeComponent extends BaseComponent{
	
	/**
	 * 根据类型和关键字查询SSysChange的ID.支持模糊查询.
	 * @param changeType
	 * @param idDesc
	 * @return
	 */
	public List<SItemvalue> queryKey(SysChangeType changeType, String idDesc) throws Exception{
		Assert.notNull(changeType, "变更类型不能为空!");
		Assert.notNull(idDesc, "查询关键字不能为空!");
		
		return sSysChangeDao.queryChangeId(changeType,idDesc);
	}
	
	public Pager<SSysChange> queryChangeInfo(Date beginDate, Date endDate, SysChangeType changeType, String id, Integer start, Integer limit) throws Exception{
		return sSysChangeDao.queryChangeInfo(beginDate,endDate,changeType,id,start,limit);
	}
}
