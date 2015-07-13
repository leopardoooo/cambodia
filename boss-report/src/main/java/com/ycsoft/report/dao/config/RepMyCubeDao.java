package com.ycsoft.report.dao.config;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.report.bean.RepMyCube;
import com.ycsoft.report.commons.ReportConstants;

@Component
public class RepMyCubeDao extends BaseEntityDao<RepMyCube> {
	
	/**
	 * 更新警戒配置
	 * @param template_id
	 * @param warn_json
	 * @throws ReportException
	 */
	public void updateWarnJson(String template_id,String warn_json) throws ReportException{
		String sql="update rep_my_cube set warn_json=? where template_id=?";
		try {
			this.executeUpdate(sql, warn_json,template_id);
		} catch (JDBCException e) {
			throw new ReportException(e,e.getSQL());
		}
	}
	/**
	 * 根据模板id获取个人cube模板
	 * @param template_id
	 * @return
	 * @throws ReportException
	 */
	public RepMyCube queryMyCubeByTemplateId(String template_id) throws ReportException{
		String sql="select * from rep_my_cube where template_id=?";
		try {
			return this.createQuery(sql, template_id).first();
		} catch (JDBCException e) {
			throw new ReportException(e,e.getSQL());
		}
	}
	/**
	 * 设置模板首选
	 * @param rep_id
	 * @param optr_id
	 * @param name
	 * @throws ReportException
	 */
	public void updateMyCubeDufaultShow(String rep_id,String optr_id,String name) throws ReportException{
		String sql = "update rep_my_cube set default_show=(case when name=? then 'true' else 'false' end) where rep_id=? and optr_id =? ";
		try {
			this.executeUpdate(sql,name, rep_id,optr_id);
		} catch (JDBCException e) {
			throw new ReportException(e,e.getSQL());
		}
	}
	/**
	 * 更新模板备注
	 * @param rep_id
	 * @param optr_id
	 * @param name
	 * @param remark
	 * @throws ReportException
	 */
	public void updateMyCubeRemark(String rep_id,String optr_id,String name,String remark) throws ReportException{
		String sql = "update rep_my_cube set remark=? where rep_id=? and optr_id =? and name=?";
		try {
			if(this.executeUpdate(sql,remark, rep_id,optr_id,name)<0)
				throw new ReportException("系统模板不能修改备注或模板已删");
		} catch (JDBCException e) {
			throw new ReportException(e,e.getSQL());
		}
	}
	/**
	 * 根据名称删名称
	 * @param rep_id
	 * @param optr_id
	 * @param name
	 * @throws ReportException
	 */
	public void deleteMyCubeByName(String rep_id,String optr_id,String name) throws ReportException{
		String sql = "delete from rep_my_cube where rep_id=? and optr_id =? and name=?";
		try {
			if(this.executeUpdate(sql, rep_id,optr_id,name)<0)
				throw new ReportException("系统模板不能删或模板已删");
		} catch (JDBCException e) {
			throw new ReportException(e,e.getSQL());
		}
	}
	/**
	 * 判断名称是否存在
	 * @param rep_id
	 * @param optr_id
	 * @param name
	 * @return
	 * @throws ReportException
	 */
	public boolean checkMyCubeName(String rep_id,String optr_id,String name) throws ReportException{
		String sql = "select * from rep_my_cube where rep_id=? and optr_id =? and name=?";
		try {
			if(this.createQuery(sql, rep_id,optr_id,name).list().size()>0)
				return true;
			else 
				return false;
		} catch (JDBCException e) {
			throw new ReportException(e,e.getSQL());
		}
	}
	/**
	 * 查询我的模板
	 * @param rep_id
	 * @param optr_id
	 * @return
	 * @throws ReportException
	 */
	public List<RepMyCube> queryMyCube(String rep_id, String optr_id)
			throws ReportException {
		String sql = "select * from rep_my_cube where rep_id=? and optr_id =? order by default_show desc";
		try {
			return this.createQuery(sql, rep_id, optr_id).list();
		} catch (JDBCException e) {
			throw new ReportException(e, sql);
		}
	}
	
	/**
	 * 查询我的有效模板
	 * @param rep_id
	 * @param optr_id
	 * @return
	 * @throws ReportException
	 */
	public List<RepMyCube> queryMyValidCube(String rep_id, String optr_id)
			throws ReportException {
		String sql = "select * from rep_my_cube where rep_id=? and optr_id =? and status=? order by default_show desc";
		try {
			return this.createQuery(sql, rep_id, optr_id,ReportConstants.VALID_T).list();
		} catch (JDBCException e) {
			throw new ReportException(e, sql);
		}
	}
	/**
	 * 查询我的默认首选模板
	 * @param rep_id
	 * @param optr_id
	 * @return
	 * @throws ReportException
	 */
	public RepMyCube queryDefaultShowCube(String rep_id, String optr_id) throws ReportException{
		String sql = "select * from rep_my_cube where rep_id=? and optr_id =? and status=? and default_show='true'";
		try {
			return this.createQuery(sql, rep_id, optr_id,ReportConstants.VALID_T).first();
		} catch (JDBCException e) {
			throw new ReportException(e, sql);
		}
	}
	
	/**
	 * 
	 * @param rep_id
	 * @param optr_id
	 * @param name
	 * @param status
	 * @throws ReportException
	 */
	public void updateStatus(String rep_id,String optr_id,String name,boolean status) throws ReportException{
		String sql="update rep_my_cube set status=? where rep_id=? and optr_id=? and name=?";
		try {
			this.executeUpdate(sql,(status?ReportConstants.VALID_T:ReportConstants.VALID_F), rep_id,optr_id,name);
		} catch (JDBCException e) {
			throw new ReportException(e, sql);
		}
	}
	/**
	 * 根据rep_id查询模板配置
	 * @param rep_id
	 * @return
	 * @throws ReportException 
	 */
	public List<RepMyCube> queryMyCubeByRepid(String rep_id) throws ReportException{
		String sql="select * from rep_my_cube where rep_id=?";
		try {
			return this.createQuery(sql, rep_id).list();
		} catch (JDBCException e) {
			throw new ReportException(e, sql);
		}
	}
	
	/**
	 * 保存我的模板
	 * @param mycube
	 * @throws ReportException
	 */
	public void saveRepMyCube(RepMyCube mycube) throws ReportException{
//		String sql=" insert into rep_my_cube(rep_id,optr_id,name,remark,default_show,STATUS,template_id,cube_json) "
//				+" values('"+mycube.getRep_id()+"','"+mycube.getOptr_id()+"','"+mycube.getName()+"','"+mycube.getRemark()
//				+"','"+mycube.getDefault_show()
//				+"','"+mycube.getStatus()
//				+"','"+mycube.getTemplate_id()+
//				"','"+mycube.getCube_json()+"')";
//		try {
//			this.executeUpdate(sql);
//		} catch (JDBCException e) {
//			throw new ReportException(e,sql);
//		}
		try {
			this.save(mycube);
		} catch (JDBCException e) {
			throw new ReportException(e,e.getSQL());
		}
	}
}
