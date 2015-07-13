/**
 * TTaskTemplatefileDao.java	2013/08/29
 */
 
package com.ycsoft.business.dao.task; 

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.task.TTaskTemplatefile;
import com.ycsoft.business.dto.config.TaskDetailTypeDto;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.daos.abstracts.BaseEntityDao;


/**
 * TTaskTemplatefileDao -> T_TASK_TEMPLATEFILE table's operator
 */
@Component
public class TTaskTemplatefileDao extends BaseEntityDao<TTaskTemplatefile> {
	
	/**
	 * default empty constructor
	 */
	public TTaskTemplatefileDao() {}
	/**
	 * 查询打印模板通过工单类型和地区
	 * 
	 * @param task_detail_type
	 * @param county_id
	 * @return
	 * @throws Exception
	 */
	public String findPrintXml(String task_detail_type, String template_id) throws Exception{
		final String sql = "SELECT t.template_filename FROM T_TASK_TEMPLATEFILE t WHERE "
						 +" t.temlate_id = ? AND t.task_detail_type=?";
		return findUnique(sql, template_id, task_detail_type);
	}
	
	public List<TTaskTemplatefile> queryTaskFileById(String templateId) throws Exception{
		String sql ="select * from T_TASK_TEMPLATEFILE where temlate_id = ? ";
		return createQuery(sql, templateId).list(); 
	}
}
