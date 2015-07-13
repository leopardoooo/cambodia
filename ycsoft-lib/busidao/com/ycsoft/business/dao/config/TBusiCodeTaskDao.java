/**
 * TBusiCodeTaskDao.java	2010/03/18
 */

package com.ycsoft.business.dao.config;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.task.TBusiCodeTask;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * TBusiCodeTaskDao -> T_BUSI_CODE_TASK table's operator
 */
@Component
public class TBusiCodeTaskDao extends BaseEntityDao<TBusiCodeTask> {

	/**
	 *
	 */
	private static final long serialVersionUID = -5412235664958156075L;
	/**
	 * default empty constructor
	 */
	public TBusiCodeTaskDao() {}

	/**
	 * 根据模板ID查询工单配置
	 * @param templateId
	 * @return
	 * @throws JDBCException
	 */
	public List<TBusiCodeTask> queryTaskTpls(String templateId) throws JDBCException{
		String sql = "  select t1.*,t2.detail_type_name,t3.busi_name,t4.template_name "
			 + " from t_busi_code_task t1 ,t_task_detail_type t2,t_busi_code t3,t_template t4 "
			 + " where t1.template_id=? and t1.template_id=t4.template_id and t1.busi_code=t3.busi_code and t1.detail_type_id=t2.detail_type_id";
		return createQuery(TBusiCodeTask.class, sql, templateId).list();
	}

	/**
	 * 根据模板ID删除记录
	 * @param templateId
	 * @throws JDBCException
	 */
	public void deleteByTplId(String templateId) throws JDBCException{
		String sql = "delete from t_busi_code_task t where t.template_id=?";
		executeUpdate(sql, templateId);
	}


	public Object isupdate(String template_id,String busi_code,String detail_type_id) throws Exception{
		String sql  = "  select * from t_busi_code_task  where template_id = ? and busi_code = ?  and detail_type_id = ?  ";
		Object request = findUnique( sql ,template_id,busi_code ,detail_type_id) == null ? false : true;
		 if( request.equals(true)){
			 request = "该模板业务费用组合已经存在";
		 }
		return request;
	}

	public void updateTemplateBusi(String template_id,String busi_code,
			String detail_type_id,String template_id_back,String busi_code_back,String detail_type_id_back) throws JDBCException{
		String sql = " UPDATE t_busi_code_task  SET   template_id = ? ,busi_code= ? , detail_type_id = ?  WHERE template_id = ? and busi_code= ? and detail_type_id = ?    ";
		executeUpdate(sql, template_id, busi_code,detail_type_id,template_id_back,busi_code_back,detail_type_id_back);
	}
	public void logoffTT(String template_id,String busi_code,String detail_type_id) throws JDBCException{
		String sql = " DELETE t_busi_code_task   WHERE   template_id = ?  and busi_code = ? and detail_type_id = ?  ";
		executeUpdate(sql,template_id,busi_code, detail_type_id);
	}
}
