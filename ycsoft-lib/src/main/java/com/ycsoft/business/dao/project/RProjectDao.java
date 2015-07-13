/**
 * RProjectDao.java	2012/05/07
 */
 
package com.ycsoft.business.dao.project; 

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.project.RProject;
import com.ycsoft.business.dto.project.QueryProject;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.Pager;


/**
 * RProjectDao -> R_PROJECT table's operator
 */
@Component
public class RProjectDao extends BaseEntityDao<RProject> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1385040317372974253L;

	/**
	 * default empty constructor
	 */
	public RProjectDao() {}
	
	/**
	 * 检查项目编号是否存在
	 * @param projectNumber
	 * @return false 不存在，true 存在
	 * @throws Exception
	 */
	public boolean checkProjectNumber(String projectNumber) throws Exception {
		String sql = "select 1 from r_project where project_number = ?";
		return this.findUnique(sql, projectNumber) == null ? false : true;
	}
	
	/**
	 * 检查编号 项目顺序码在同一个小区是否存在
	 * @param projectNumber
	 * @return
	 * @throws Exception
	 */
	public List<RProject> checkProjectOrderCode(String projectId,
			String projectNumber, String[] addrIdArr) throws Exception {
		String sql = "select t.*,a.addr_name from r_project t,r_project_addr pa,t_address a" +
				" where t.project_id=pa.project_id and pa.addr_id=a.addr_id" +
				" and a.addr_id in (" +sqlGenerator.in(addrIdArr)+")"+
				" and substr(t.project_number,3,4) = substr(?,3,4)";
		return this.createQuery(sql, projectNumber).list();
	}
	
	public void isInvalid(String projectId,String isValid) throws Exception{
		String sql = "update r_project t set t.is_valid=? where t.project_id=?";
		this.executeUpdate(sql, isValid,projectId);
	}
	
	public Pager<RProject> queryProject(QueryProject qp,String countyId,Integer start,Integer limit) throws Exception {
		String sql = "select t.*,wmsys.wm_concat(addr.addr_name) addr_name" +
				" from r_project t,r_project_addr pa,t_address addr where pa.addr_id=addr.addr_id(+)" +
				" and t.project_id=pa.project_id(+)";
		if(qp != null){
			String selCountyId = qp.getCounty_id();
			String addrName = qp.getAddr_name();
			if(StringHelper.isNotEmpty(addrName)){
				sql += " and addr.addr_name like '%"+addrName+"%'";
				if(StringHelper.isNotEmpty(selCountyId)){
					sql += " and addr.county_id='"+selCountyId+"'";
				}
			}
			
			if(StringHelper.isNotEmpty(selCountyId)){
				countyId = selCountyId;
			}
			
			String  projectNumber = qp.getProject_number();
			if(StringHelper.isNotEmpty(projectNumber)){
				sql += " and t.project_number='"+projectNumber+"'";
			}
			
			String projectName = qp.getProject_name();
			if(StringHelper.isNotEmpty(projectName)){
				sql += " and t.project_name like '%"+projectName+"%'";
			}
			
			String startDate = qp.getStart_date();
			if(StringHelper.isNotEmpty(startDate)){
				sql += " and t.create_date >=to_date('"+startDate+"','yyyy-mm-dd')";
			}
			
			String endDate = qp.getEnd_date();
			if(StringHelper.isNotEmpty(endDate)){
				sql += " and t.create_date <=to_date('"+endDate+"','yyyy-mm-dd')";
			}
		}
		if(!countyId.equals(SystemConstants.COUNTY_ALL)){
			sql += " and t.county_id='"+countyId+"'";
		}
		sql += "group by t.project_id,t.project_number,t.project_name,t.project_type,t.pre_start_date,"
			+"t.start_date,t.pre_end_date,t.end_date,t.plan_num,t.real_num,t.plan_users_1,t.plan_income_1,"
			+"t.plan_users_2,t.plan_income_2,t.plan_users_3,t.plan_income_3,t.plan_users_4,t.plan_income_4,"
			+"t.plan_users_5,t.plan_income_5,t.optic_cable_length,t.electric_cable_length,t.optical_node_number,"
			+"t.project_plan_money,t.project_final_money,t.status,t.create_date,t.change_date,t.is_valid,"
			+"t.county_id,t.remark order by t.create_date,t.change_date desc";
		return this.createQuery(sql).setStart(start).setLimit(limit).page();
	}

}
