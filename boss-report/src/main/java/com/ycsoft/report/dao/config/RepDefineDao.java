/**
 * RepDefineDao.java	2010/06/21
 */
 
package com.ycsoft.report.dao.config; 

import org.springframework.stereotype.Component;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.report.bean.RepDefine;
import com.ycsoft.report.dto.RepDefineDto;


/**
 * RepDefineDao -> REP_DEFINE table's operator
 */
@Component
public class RepDefineDao extends BaseEntityDao<RepDefine> {
	
	/**
	 * default empty constructor
	 */
	public RepDefineDao() {}
	
	/**
	 * 获取一张报表的配置信息
	 * 
	 * @param rep_id
	 * @return
	 * @throws ReportException
	 * @throws JDBCException
	 */
	public RepDefine getRepDefine(String rep_id) throws ReportException, JDBCException{
		
		RepDefine rd=findByKey(rep_id);
		if(rd==null)
				throw new ReportException(rep_id+"_report is not exisit.");
		return rd;
	}
	
	public void updateRemark(String rep_id,String remark) throws JDBCException{
		String sql="update rep_define set remark=? where rep_id=?";
		this.executeUpdate(sql, remark,rep_id);
	}
	
	public void updateQuieeRaq(String rep_id,String quiee_raq) throws JDBCException{
		this.executeUpdate("update rep_define set quiee_raq=? where rep_id=?", quiee_raq,rep_id);
	}
	
	public void updateDetail(String rep_id,String detail_id) throws JDBCException{
		this.executeUpdate("update rep_define set detail_id=? where rep_id=?", detail_id,rep_id);
	}
	
}
