/**
 * BTaskInfoDao.java	2011/05/27
 */
 
package com.ycsoft.business.dao.core.bill; 

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.bill.BTaskInfo;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * BTaskInfoDao -> B_TASK_INFO table's operator
 */
@Component
public class BTaskInfoDao extends BaseEntityDao<BTaskInfo> {
	
	/**
	 * default empty constructor
	 */
	public BTaskInfoDao() {}

	public void saveTaskInfo(BTaskInfo task) throws JDBCException {
		String sql = "insert into b_task_info(task_code,county_id,area_id,task_info,mail_title)" +
				" values (?,?,?,?,?)";
		this.executeUpdate(sql, task.getTask_code(), task.getCounty_id(), 
				task.getArea_id(), task.getTask_info(), task.getMail_title());
	}
	

	public void saveOrUpdateTaskInfo(BTaskInfo task) throws JDBCException {
		Map<String, Serializable> param = new HashMap<String, Serializable>();
		param.put("task_code", task.getTask_code());
		param.put("county_id", task.getCounty_id());
		List<BTaskInfo> list = this.findByMap(param);
		if(CollectionHelper.isNotEmpty(list)){//如果有，先删掉
			deleteTask(task);
		}
		saveTaskInfo(task);
	}
	
	/**
	 * 根据 county_id 和task_code 删除任务.
	 * @param task
	 * @throws JDBCException
	 */
	public void deleteTask(BTaskInfo task) throws JDBCException{
		executeUpdate("delete from b_task_info where county_id = ? and task_code = ?", task.getCounty_id(),task.getTask_code());
	}
	
	public List<BTaskInfo> queryTaskInfoByCountyId(String countyId) throws JDBCException {
		if(countyId.equals(SystemConstants.COUNTY_ALL)){
			return this.findAll();
		}else{
			String sql = "select * from b_task_info where county_id=?";
			return this.createQuery(BTaskInfo.class, sql, countyId).list();
		}
	}
	
	public Map<String, BTaskInfo> queryTaskInfo() throws Exception {
		List<BTaskInfo> all = findAll();
		return CollectionHelper.converToMapSingle(all, "county_id");
	}

}
