/**
 * RDepotDefineDao.java	2010/06/24
 */

package com.ycsoft.business.dao.resource.device;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.ycsoft.beans.depot.RDepotDefine;
import com.ycsoft.beans.system.SDept;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;
import com.ycsoft.sysmanager.dto.system.RDepotDto;
import com.ycsoft.sysmanager.dto.tree.TreeDto;


/**
 * RDepotDefineDao -> R_DEPOT_DEFINE table's operator
 */
@Component
public class RDepotDefineDao extends BaseEntityDao<RDepotDefine> {

	/**
	 *
	 */
	private static final long serialVersionUID = 6035592247304161642L;

	
	public RDepotDefine findBydepotId(String depotId) throws JDBCException {
		String sql = "select * from vew_device_depot where depot_id=?";
		return createQuery(sql, depotId).first();
	}

	/**
	 * 查询设备当前仓库可以流转的上下级仓库
	 * @param depotId
	 * @throws JDBCException
	 */
	public List<RDepotDefine> queryDeviceTransDepot(String depotId)
			throws JDBCException {
		String sql = "SELECT a.* FROM vew_device_depot a ,vew_device_depot b "
				+ " WHERE b.depot_id=? AND a.depot_id=b.depot_pid "
				+ " UNION  SELECT * FROM vew_device_depot  WHERE depot_pid = ?";
		return createQuery(sql, depotId, depotId).list();
	}
	
	/**
	 * 营业厅不能调拨到其他营业厅
	 * @param depotId
	 * @return
	 * @throws JDBCException
	 */
	public List<RDepotDefine> queryYytDepotById(String depotId,String countyId) throws JDBCException {
		String sql = "select s.dept_id depot_id,s.dept_name depot_name from s_dept  t,s_dept s  " +
				" where t.dept_id=? and s.dept_type <> t.dept_type and s.county_id = ? and s.status = ? and s.dept_type in (?,?) ";
		return createQuery(sql, depotId,countyId,StatusConstants.ACTIVE,SystemConstants.DEPT_TYPE_CK,SystemConstants.DEPT_TYPE_FGS).list();
	}
	
	/**
	 * 仓库可以调拨到所有仓库、营业厅、分公司
	 * @param depotId
	 * @return
	 * @throws JDBCException
	 */
	public List<RDepotDefine> queryCkDepotById(String depotId,String countyId) throws JDBCException {
		String sql = "select s.dept_id depot_id,s.dept_name depot_name  from s_dept s  where s.dept_id <>?   and s.county_id = ?  and s.status = ?";
		return createQuery(sql, depotId,countyId,StatusConstants.ACTIVE).list();
	}	
	
	/**
	 * 查询发票当前仓库可以流转的上下级仓库
	 * @param depotId
	 * @throws JDBCException
	 */
	public List<RDepotDefine> queryInvoiceTransDepot(String depotId)
			throws JDBCException {
		String sql = "SELECT a.* FROM vew_invoice_depot a ,vew_device_depot b "
				+ " WHERE b.depot_id=? AND a.depot_id=b.depot_pid "
				+ " UNION  SELECT * FROM vew_invoice_depot  WHERE depot_pid = ?";
		return createQuery(RDepotDefine.class,sql, depotId, depotId).list();
	}
	
	public List<SDept> queryQuotaInvoiceTransDepot(String depotId)
			throws JDBCException {
		String sql = "SELECT a.* FROM s_dept a ,s_dept b"
	         +" WHERE b.dept_id=? AND a.dept_id=b.dept_pid" 
	         +" and (a.dept_type='FGS' OR a.dept_type='YYT') and a.status='ACTIVE'"
	         +" and (b.dept_type='FGS' OR b.dept_type='YYT') and b.status='ACTIVE'"
	         +" UNION "  
	         +" SELECT * FROM s_dept a  WHERE a.dept_pid=?"
	         +" and (a.dept_type='FGS' OR a.dept_type='YYT') and a.status='ACTIVE'"
	         +" union " 
	         +" select * from s_dept a  WHERE a.dept_id=?"
			 +" and (a.dept_type='FGS' OR a.dept_type='YYT') and a.status='ACTIVE'";
		return createQuery(SDept.class, sql, depotId, depotId, depotId).list();
	}
	
	/**
	 * 查选发票仓库的上级仓库
	 * @param depotId
	 * @return
	 * @throws JDBCException
	 */
	public String queryParentDepot(String depotId)throws JDBCException {
		String sql ="select depot_pid from vew_invoice_depot where depot_id=?";
		return this.findUnique(sql, depotId);
	}
	
	/**
	 * 查询当前仓库及以下子仓库
	 * @param depotId
	 * @return
	 * @throws JDBCException
	 */
	public List<RDepotDto> queryChildDepot(String[] depotIds) throws JDBCException {
		String sql = "select * from vew_device_depot start with depot_id in ("+this.sqlGenerator.in(depotIds)+") connect by prior depot_id=depot_pid";
		return this.createQuery(RDepotDto.class,sql).list();
	}
	
	/**
	 * 查询当前发票及以下子仓库
	 * @param depotId
	 * @return
	 * @throws JDBCException
	 */
	public List<RDepotDto> queryChildInvoiceDepot(String[] depotIds) throws JDBCException {
		String sql = "select * from vew_invoice_depot start with depot_id in ("+this.sqlGenerator.in(depotIds)+") connect by prior depot_id=depot_pid";
		return this.createQuery(RDepotDto.class,sql).list();
	}

}
