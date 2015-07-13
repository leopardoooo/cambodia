/**
 * JSendMsgDao.java	2010/11/21
 */
 
package com.ycsoft.business.dao.config; 


import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.JSendMsg;
import com.ycsoft.beans.system.SItemvalue;
import com.ycsoft.business.dto.config.TSendMsgDto;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.Pager;


/**
 * JSendMsgDao -> J_SEND_MSG table's operator
 */
@Component
public class JSendMsgDao extends BaseEntityDao<JSendMsg> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -775611226254707583L;

	/**
	 * default empty constructor
	 */
	public JSendMsgDao() {}

	public TSendMsgDto queryMsgByJob(String JobId)
			throws Exception {
		String sql = "select * from J_SEND_MSG  where job_id=?";
		return this.createQuery(TSendMsgDto.class,sql, JobId).first();
	}
	
	
	public Pager<JSendMsg> query(Integer start , Integer limit , List<SItemvalue> list ,String countyId,String taskId)throws Exception{
		String cond = "";
		String itms = "";
		boolean key = false;
		String sql = " select * from J_SEND_MSG  where 1=1 ";
		    if(StringUtils.isNotEmpty(countyId)){
		    		cond +=" and  county_id='"+countyId+"' ";
			 }
			if(StringUtils.isNotEmpty(taskId)){
		    		cond +=" and  task_code='"+taskId+"' ";
			 }
		    if(list.size()>0){
		    	itms += " and ( ";
		    	for(SItemvalue dto :list){
		    		if(dto.getItem_key().equals(DictKey.JOB_MESSAGE_TYPE.toString())){
		    			itms += "  msg_type = '"+dto.getItem_value()+"' or ";
		    			key = true;
		    		}
		    		if(dto.getItem_key().equals(DictKey.JOB_SEND_TYPE.toString())){
		    			itms += "  send_type like '%"+dto.getItem_value()+"%' or ";
		    			key = true;
		    		}
		    		if(dto.getItem_key().equals(DictKey.STATUS.toString())){
		    			itms += " status = '"+dto.getItem_value()+"' or ";
		    			key = true;
		    		}
		    	}
		    	if(key){
		    		itms = StringHelper.delEndChar( itms ,  3);
		    		itms +=" ) ";
		    	}else{
		    		itms +="";
		    	}
		    }
		    sql=sql+cond+itms;
		return createQuery(JSendMsg.class,sql ).setLimit(limit).setStart(start).page();
	}
	
	public int invalidMsg(String job) throws Exception {
		String sql ="update J_SEND_MSG set status=? where job_id=?";
		return executeUpdate(sql, StatusConstants.INVALID,job);
	}
}
