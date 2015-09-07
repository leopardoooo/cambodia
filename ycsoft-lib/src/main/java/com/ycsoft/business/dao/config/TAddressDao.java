/**
 * TAddressDao.java	2010/03/11
 */

package com.ycsoft.business.dao.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TAddress;
import com.ycsoft.beans.core.cust.CCust;
import com.ycsoft.business.dto.config.TAddressDto;
import com.ycsoft.business.dto.config.TAddressSysDto;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;
import com.ycsoft.sysmanager.dto.tree.TreeDto;


/**
 * TAddressDao -> T_ADDRESS table's operator
 */
@Component
public class TAddressDao extends BaseEntityDao<TAddress> {

	/**
	 *
	 */
	private static final long serialVersionUID = 6774749258895833188L;

	/**
	 * default empty constructor
	 */
	public TAddressDao() {}
	
	public List<TAddressDto> queryActiveAddrByName(String q,String pId, String countyId, String deptId, String dataRight)
		throws Exception {
	String str = "";
	if(!countyId.equals(SystemConstants.COUNTY_ALL)){
		str = " and t.county_id='"+countyId+"' ";
	}
	String filter="";
	if(StringHelper.isNotEmpty(q)){
		filter=" and  (t.addr_name like '%"+q+"%' or t2.full_sepll like '%"+q+"%' or t2.seq_sepll like '%"+q+"%') ";
	}
//	String sql = "select distinct a.*,level  from t_address a "
//			+ " start with a.addr_id in (select addr_id from t_address t,t_spell t2 "
//			+ " where t.addr_id = t2.data_id(+)"+str
//			+ " and t.is_leaf='T' and t.status='ACTIVE' "
//			+ (StringHelper.isNotEmpty(deptId) ? 
//						" and t.addr_pid in (select sda.addr_id from s_dept_addr sda where sda.dept_id = '" + deptId + "') "
//						: " ")
//			+ filter+")"
//			+ " and a.status='ACTIVE' and "+dataRight
//			+ " connect by prior a.addr_pid=a.addr_id order by level desc";
//	String sql = "select  a.*, level from t_address a start with a.addr_PID=10 "
//			+ "connect by prior a.addr_id = a.addr_Pid "
//			+ "order by level asc";
	String sql = " select  a.* from t_address a where a.addr_PID=? ";
	
	return createQuery(TAddressDto.class, sql,pId).list();
	}
	
	public List<TAddressDto> queryActiveAddrByName(String q, String countyId, String dataRight)
			throws Exception {
		String str = "";
		if(!countyId.equals(SystemConstants.COUNTY_ALL)){
			str = " and t.county_id='"+countyId+"' ";
		}
		String filter="";
		if(StringHelper.isNotEmpty(q)){
			filter=" and  (t.addr_name like '%"+q+"%' or t2.full_sepll like '%"+q+"%' or t2.seq_sepll like '%"+q+"%') ";
		}
		String sql = "select distinct a.*,level  from t_address a "
				+ " start with a.addr_id in (select addr_id from t_address t,t_spell t2 "
				+ " where t.addr_id = t2.data_id(+)"+str
				+ " and t.is_leaf='T' and t.status='ACTIVE' "+ filter+")"
				+ " and a.status='ACTIVE' and "+dataRight
				+ " connect by prior a.addr_pid=a.addr_id order by level desc";
		
		return createQuery(TAddressDto.class, sql).list();
		}

	public List<TAddressDto> queryAddrByName(String q, String countyId, String dataRight)
			throws Exception {
		String str = "";
		if(!countyId.equals(SystemConstants.COUNTY_ALL)){
			str = " and t.county_id='"+countyId+"' ";
		}
		String filter="";
		if(StringHelper.isNotEmpty(q)){
			filter=" and  (t.addr_name like '%"+q+"%' or t2.full_sepll like '%"+q+"%' or t2.seq_sepll like '%"+q+"%') ";
		}
		String sql = "select distinct a.*,level  from t_address a "
				+ " start with a.addr_id in (select addr_id from t_address t,t_spell t2 "
				+ " where t.addr_id = t2.data_id(+)"+str
				+ " and t.is_leaf='T' "+ filter+")"
				+ " and "+dataRight
				+ " connect by prior a.addr_pid=a.addr_id order by level desc";

		return createQuery(TAddressDto.class, sql).list();
	}

	public List<TAddressDto> getAddrByName(String q, String countyId)
		throws Exception {
	String sql = "select distinct a.addr_id,a.addr_pid,a.addr_name,a.addr_full_name,a.tree_level, level,'T' is_leaf  from t_address a "
			+ " start with a.addr_id in (select addr_id from t_address t,t_spell t2 "
			+ " where t.addr_id = t2.data_id(+) and t.county_id=? "
			+ " and t.status='ACTIVE' and  (t.addr_name like '%"+q+"%' or t2.full_sepll like '%"+q+"%' or t2.seq_sepll like '%"+q+"%')) "
			+ " and a.status='ACTIVE' connect by prior a.addr_pid=a.addr_id order by level desc";
	return createQuery(TAddressDto.class, sql,countyId).list();
	}
	
	
	/**
	 *	在指定的父节点下，查找地址
	 * @param pAddrid
	 * @param addrid
	 * @return
	 * @throws JDBCException
	 */
	public List<TAddress> queryAddrByPid(String addrid, String... pAddrid)
			throws JDBCException {
		String sql = "SELECT * FROM (SELECT * FROM t_address "
				+ "  start with addr_id in (:pAddrid) "
				+ " connect by prior addr_id = addr_pid)";
		if (StringHelper.isNotEmpty(addrid)) {
			sql = StringHelper.append(sql, "WHERE addr_id='", addrid, "'");
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pAddrid", Arrays.asList(pAddrid));

		return createNameQuery(sql, params).list();
	}
	
	/**
	 * 在指定的父节点下，查找地址
	 * @param addrPid
	 * @return
	 * @throws JDBCException
	 */
	public List<TAddress> getAddrByPid(String addrPid) throws JDBCException {
		String sql = "SELECT * FROM t_address "
				+ "  start with addr_pid =? "
				+ " connect by prior addr_id = addr_pid";
		return createQuery(sql,addrPid).list();
	}

	
	/**
	 *	获取下一个地址编号
	 *	规则
	 *	2级地址2位 最小加1
	 *  3级地址5位 最小加1
	 *	
	 */
	public String getAddrId(String pid) throws JDBCException {
		String add_id = findUnique(
				"SELECT  nvl(max(addr_id)+1,0) from t_address where addr_pid=? and addr_id<>'"
						+ pid + "999'  and addr_id<>'99'", pid);
		return add_id;
	}

	/**
	 * 	查询指定addrid数组的地址信息
	 * @param addrid
	 * @return
	 * @throws JDBCException
	 */
	
	public List<TAddress> queryAddrByaddr(String[] addrid) throws JDBCException {
		String sql = "SELECT * FROM t_address where status='ACTIVE' and "+getSqlGenerator().setWhereInArray("addr_id",addrid)+"";
		return createQuery(sql).list();
	}
	
	public List<CCust> getCustByAddrId(String addrId) throws JDBCException{
		String sql = "select * from c_cust c where c.addr_id=?";
		return createQuery(CCust.class, sql, addrId).list();
	}
	public List<TAddress> queryAddrByCountyId(String countyId) throws JDBCException {
		String sql = "SELECT * FROM t_address where status='ACTIVE' and tree_level = ? and county_id = ? ";
		return createQuery(sql,SystemConstants.ADDRESS_LEVEL_DISTRICT,countyId).list();
	}
	
	public List<TAddressDto> queryAddrByaddrPid(String addrid) throws JDBCException {
		String sql = "select count(cust_id) num, t1.addr_name,t1.addr_id from c_cust t, t_address t1 " +
				" where t.addr_id in (select t.addr_id from t_address t where t.status='ACTIVE' and t.addr_pid = ? ) " +
				" and t.addr_id = t1.addr_id and t1.status='ACTIVE' group by t1.addr_name,t1.addr_id";
		return createQuery(TAddressDto.class,sql,addrid).list();
	}
	
	
	/**组装满足条件需要变更地址的客户
	 * @param newAddr 区域的信息
	 * @param oldAddr 小区与其区域的信息
	 * @param countyId
	 * @return
	 * @throws JDBCException
	 */
	public List<CCust> getCustByAddrId(TAddressDto newAddr,TAddressDto oldAddr,String countyId)
		throws JDBCException {
		String sql = "select regexp_replace(t.address, '^' ||?, ?) address,t.address old_address,t.cust_id,t.county_id,t.area_id " +
				" from c_cust t where t.county_id = ?  and t.addr_id = ? " +
				" and t.address like '"+oldAddr.getAddr_p_name()+"%' and t.address not like '"+oldAddr.getAddr_name()+"%'  ";
		return createQuery(CCust.class, sql, oldAddr.getAddr_p_name(),newAddr.getAddr_name(),countyId,oldAddr.getAddr_id()).list();
	}
	
	/**组装满足条件需要变更邮件地址的客户
	 * @param newAddr 区域的信息
	 * @param oldAddr 小区与其区域的信息
	 * @param countyId
	 * @return
	 * @throws JDBCException
	 */
	public List<CCust> getCustLinkmanByAddrId(TAddressDto newAddr,TAddressDto oldAddr,String countyId)
		throws JDBCException {
		String sql = "select regexp_replace(t.mail_address, '^' ||?, ?) address,t.mail_address old_address,t1.cust_id,t1.county_id,t1.area_id " +
			    " from c_cust_linkman t,c_cust t1 " +
			    " where t.cust_id = t1.cust_id and t1.county_id = ? and t1.addr_id =?  " +
			    " and t.mail_address like '"+oldAddr.getAddr_p_name()+"%'and t.mail_address not like '"+oldAddr.getAddr_name()+"%'";
	return createQuery(CCust.class, sql, oldAddr.getAddr_p_name(),newAddr.getAddr_name(),countyId,oldAddr.getAddr_id()).list();
	}
	
	/**组装满足条件需要变更地址的客户
	 * @param newAddr 小区与其区域的信息
	 * @param oldAddr 小区与其区域的信息
	 * @param countyId
	 * @return
	 * @throws JDBCException
	 */
	public List<CCust> getoneCustByAddrPId(TAddressDto newAddr,TAddressDto oldAddr,String countyId)
		throws JDBCException {
		String sql = "select regexp_replace(t.address, '^' ||?||?,?||?) address,t.address old_address,t.cust_id,t.county_id,t.area_id " +
				" from c_cust t where t.county_id = ? and t.addr_id =? and t.address like ?||?||'%' ";
		return createQuery(CCust.class, sql, oldAddr.getAddr_p_name(),oldAddr.getAddr_name(),newAddr.getAddr_p_name(),
				newAddr.getAddr_name(),countyId,oldAddr.getAddr_id(),oldAddr.getAddr_p_name(),oldAddr.getAddr_name()).list();
	}
	
	public List<CCust> gettwoCustByAddrPId(TAddressDto newAddr,TAddressDto oldAddr,String countyId)
		throws JDBCException {
		String sql = "select regexp_replace(t.address, '^' ||?,?||?) address,t.address old_address,t.cust_id,t.county_id,t.area_id " +
				" from c_cust t where t.county_id = ? and t.addr_id =? and t.address like ?||'%' and t.address not like ?||'%' ";
		return createQuery(CCust.class, sql,oldAddr.getAddr_name(),newAddr.getAddr_p_name(),
				newAddr.getAddr_name(),countyId,oldAddr.getAddr_id(),oldAddr.getAddr_name(),oldAddr.getAddr_p_name()).list();
	}
	/**组装满足条件需要变更邮件地址的客户
	 * @param newAddr  小区与其区域的信息
	 * @param oldAddr  小区与其区域的信息
	 * @param countyId
	 * @return
	 * @throws JDBCException
	 */
	public List<CCust> getoneCustLinkmanByAddrPId(TAddressDto newAddr,TAddressDto oldAddr,String countyId)
		throws JDBCException {
		String sql = " select  regexp_replace(t.mail_address, '^' ||?||?,?||?) address,t.mail_address old_address,t1.cust_id ,t1.county_id,t1.area_id" +
				" from c_cust_linkman t, c_cust t1 where  t.cust_id=t1.cust_id and t1.county_id = ?  and t1.addr_id = ? " +
				" and t.mail_address  like ?||?||'%' ";
		return createQuery(CCust.class, sql,oldAddr.getAddr_p_name(),oldAddr.getAddr_name(),newAddr.getAddr_p_name(),
				newAddr.getAddr_name(),countyId,oldAddr.getAddr_id(),oldAddr.getAddr_p_name(),oldAddr.getAddr_name()).list();
	}
	
	public List<CCust> gettwoCustLinkmanByAddrPId(TAddressDto newAddr,TAddressDto oldAddr,String countyId)
		throws JDBCException {
		String sql = " select regexp_replace(t.mail_address, '^' ||?,?||?) address,t.mail_address old_address,t1.cust_id,t1.county_id,t1.area_id " +
				" from c_cust_linkman t, c_cust t1 where  t.cust_id=t1.cust_id and t1.county_id = ?  and t1.addr_id = ? " +
				" and t.mail_address like ?||'%' and t.mail_address not like ?||'%' ";
		return createQuery(CCust.class, sql,oldAddr.getAddr_name(),newAddr.getAddr_p_name(),newAddr.getAddr_name()
			,countyId,oldAddr.getAddr_id(),oldAddr.getAddr_name(),oldAddr.getAddr_p_name()).list();
	}
	
	public void updateAddr (String [] addrid,String addrPid,String countyId) throws Exception {
		String	sql = "update t_address set addr_pid = '"+addrPid+"' where addr_id = ?  and county_id = "+countyId+" ";
		executeBatch(sql, addrid);
	}
	
	public Pager<CCust> queryCustAddrByCustIds(String[] custIds, String countyId,Integer start,Integer limit) throws Exception {
		String sql = "SELECT cc.*,cc.address old_address,cca.t1,cca.t2,cca.t3,cca.t4,cca.t5,cca.note FROM c_cust cc,c_cust_addr cca " +
				" where  cc.cust_id= cca.cust_id and cc.county_id = ?  and ("+getSqlGenerator().setWhereInArray("cc.cust_id",custIds)+")";
		return createQuery(CCust.class, sql,countyId).setStart(start).setLimit(limit).page();
	}
	
	public TAddressDto getAddressByAddrId(String addrId) throws JDBCException {
		String sql = " select t1.addr_name,t2.addr_name addr_p_name,t1.addr_id from t_address t1,t_address t2 where t1.addr_id =? and t1.addr_pid= t2.addr_id ";
		return createQuery(TAddressDto.class,sql,addrId).first();
	}
	
	public List<CCust> getCustAllByAddrId(String[] addrId, String countyId) throws Exception {
		String sql = "select * from c_cust where "+getSqlGenerator().setWhereInArray("addr_id",addrId)+" and county_id= ? ";
		return createQuery(CCust.class, sql,countyId).list();
	}
	
	public List<TAddressDto> queryAddrDistrict(String countyId) throws JDBCException {
		String sql = "select * from t_address t where t.tree_level=2 and t.status='ACTIVE' ";
		if(!countyId.equals(SystemConstants.COUNTY_ALL)){
			sql = sql + " and t.county_id='"+countyId+"' ";
		}
		return createQuery(TAddressDto.class, sql).list();
	}

	public List<TAddressDto> queryAddrCommunity(String addrPid) throws JDBCException {
		String sql = "select * from t_address t where t.status='ACTIVE' and t.addr_pid = ?";
		return createQuery(TAddressDto.class, sql,addrPid).list();
	}
	public List<TAddressDto> queryDeptAddr(String[] deptId) throws JDBCException {
		String sql = "select sd.*,ta.addr_name from s_dept_addr  sd,T_ADDRESS ta " +
				"WHERE sd.addr_id=ta.addr_id and "+getSqlGenerator().setWhereInArray("sd.DEPT_ID",deptId)+"  ";
		return createQuery(TAddressDto.class, sql).list();
	}

	public List<TreeDto> getAddrByCountyId(String countyId) throws Exception{
		String con = "";
		if(!countyId.equals(SystemConstants.COUNTY_ALL)){
			con = " and t.county_id='"+countyId+"' ";
		}
		String sql = " select t.addr_id id,t.addr_pid pid,t.addr_name text , t.tree_level  attr from t_address t " +
				"where t.tree_level in ('1','2') and t.status= ? "+con+"  start with t.addr_pid = '-1'  " +
				"connect by prior t.addr_id = t.addr_pid ";
		  return createQuery(TreeDto.class,sql,StatusConstants.ACTIVE).list();
	}
	
	public List<TreeDto> getAddrByDeptd(String deptId) throws Exception{
		String sql = " select t.addr_id id from s_dept_addr t where t.dept_id=? ";
		  return createQuery(TreeDto.class,sql,deptId).list();
	}

	/**
	 * 查询可绑定到部门的address.包括已经能够被本部门绑定的.
	 * @param countyId
	 * @param deptId
	 * @return
	 * @throws Exception
	 */
	public List<TAddress> queryBindableAddr(String countyId, String deptId) throws Exception {
		String sql = "select t.* from t_address t  where t.county_id = ? and t.tree_level = '2' " ;
		return createQuery(sql, countyId).list();
	}

	public List<TAddressDto> queryAddrByName(String name,String[] addrPid) throws Exception {
		String src = "";
		if(addrPid != null){
			src = " and "+getSqlGenerator().setWhereInArray("t.addr_pid",addrPid);
		}
		String sql = " select t.*,level from t_address t start with "
				+ " t.addr_name like '%"+name+"%' and t.status = ? "+src
						+ "connect by prior t.addr_id = t.addr_Pid order by level asc ";
		return createQuery(TAddressDto.class,sql, StatusConstants.ACTIVE).list();
	}
	
	public List<TAddressDto> queryAddrByAllowPids(String levelId,String[] addrPid) throws JDBCException {
		String src = "";
		if(addrPid != null && addrPid.length>0){
			src = " and "+getSqlGenerator().setWhereInArray("addr_pid",addrPid);
		}
		String sql = "SELECT * FROM t_address where status='ACTIVE' and tree_level = ? "+src;
		return createQuery(TAddressDto.class,sql,levelId).list();
	}
	
	public List<TAddressDto> queryAddrByAllowIds(String levelId,String[] addrIds) throws JDBCException {
		String src = "";
		if(addrIds != null && addrIds.length>0){
			src = " and "+getSqlGenerator().setWhereInArray("addr_id",addrIds);
		}
		String sql = "SELECT * FROM t_address where status='ACTIVE' and tree_level = ? "+src;
		return createQuery(TAddressDto.class,sql,levelId).list();
	}
	
	
	public List<TAddressDto> queryAddrByaddrPids(String name,String[] addrPids) throws JDBCException {
		String src = "";
		if(addrPids != null && addrPids.length>0){
			src = " and "+getSqlGenerator().setWhereInArray("addr_pid",addrPids);
		}
		String sql = "SELECT * FROM t_address  where  lower(addr_name) like '%"+name+"%' and status='ACTIVE'  "+src;
		return createQuery(TAddressDto.class,sql).list();
	}
	
	public List<TAddressDto> queryAddrByaddrIds(String name,String[] addrIds) throws JDBCException {
		String src = "";
		if(addrIds != null&& addrIds.length>0){
			src = " and "+getSqlGenerator().setWhereInArray("addr_id",addrIds);
		}
		String sql = "SELECT * FROM t_address where  lower(addr_name) like '%"+name+"%' and status='ACTIVE'  "+src;
		return createQuery(TAddressDto.class,sql).list();
	}
	public List<TAddressDto> queryAddrByIds(String[] addrIds) throws JDBCException {
		String src = "";
		if(addrIds != null && addrIds.length>0){
			src = " and "+getSqlGenerator().setWhereInArray("addr_id",addrIds);
		}
		String sql = "SELECT * FROM t_address where status='ACTIVE' and tree_level = '1' "+ src;
		return createQuery(TAddressDto.class,sql).list();
	}
	
	public List<TAddressDto> queryAddrById(String addrId) throws JDBCException {
		String sql = "SELECT * FROM t_address where status='ACTIVE'  and addr_pid = ? order by sort_num ";
		return createQuery(TAddressDto.class,sql,addrId).list();
	}
	
	
	public List<TAddressSysDto> queryAllAddrByIds(String[] addrIds) throws JDBCException {
		String src = "";
		if(addrIds != null && addrIds.length>0){
			src = " and "+getSqlGenerator().setWhereInArray("addr_id",addrIds);
		}
		String sql = "SELECT * FROM t_address where  tree_level = '1' "+ src;
		return createQuery(TAddressSysDto.class,sql).list();
	}
	
	public List<TAddressSysDto> queryAllAddrById(String addrId) throws JDBCException {
		String sql = "SELECT * FROM t_address where addr_pid = ?  order by sort_num ";
		return createQuery(TAddressSysDto.class,sql,addrId).list();
	}
	public List<TAddressSysDto> queryAddrSysTreeByLvOneAndName(String[] lvOneAddrIds,String name) throws JDBCException{
		String sql=StringHelper.append("select d.* from ",
			" (select distinct c.* ",
			" from t_address c ",
			" start with addr_id in ",
			" (select a.addr_id ",
			" from t_address a ,(",
			" select t.addr_id from t_address t ",
			" where ",getSqlGenerator().setWhereInArray("addr_pid",lvOneAddrIds),
			" and  lower( t.addr_name) not like  '%'||?||'%' ",
			" ) b  ",
			" where a.addr_pid =b. addr_id ",
			" and lower( a.addr_name) like '%'||?||'%'",
			" union all ",
			" select t.addr_id from t_address t ",
			" where  ",getSqlGenerator().setWhereInArray("addr_pid",lvOneAddrIds),
			" and  lower( t.addr_name) like '%'||?||'%' ",
			") connect by prior c.addr_pid = c.addr_id)d ",
			" where d.tree_level<>0 order by d.tree_level,d.sort_num");
		return createQuery(TAddressSysDto.class,sql,name,name,name).list();
	}
	
	
	public List<TAddressDto> queryAddrTreeByLvOneAndName(String[] lvOneAddrIds,String name) throws JDBCException{
		String sql=StringHelper.append("select d.* from ",
			" (select distinct c.* ",
			" from t_address c ",
			" start with addr_id in ",
			" (select a.addr_id ",
			" from t_address a ,(",
			" select t.addr_id from t_address t ",
			" where ",getSqlGenerator().setWhereInArray("addr_pid",lvOneAddrIds),
			" and  lower( t.addr_name) not like  '%'||?||'%' ",
			" ) b  ",
			" where a.addr_pid =b. addr_id ",
			" and lower( a.addr_name) like '%'||?||'%'",
			" union all ",
			" select t.addr_id from t_address t ",
			" where  ",getSqlGenerator().setWhereInArray("addr_pid",lvOneAddrIds),
			" and  lower( t.addr_name) like '%'||?||'%' ",
			") connect by prior c.addr_pid = c.addr_id)d ",
			" where d.tree_level<>0 order by d.tree_level,d.sort_num");
		return createQuery(TAddressDto.class,sql,name,name,name).list();
	}
	
	public TAddress querySortNumByNextId(String addrPId,float sortNum) throws JDBCException {
		String sql = "select * from ( select * from t_address t where t.addr_pid=? and t.sort_num> ?  "
				+ " order by t.sort_num ) where  rownum<2 ";
		return createQuery(sql,addrPId,sortNum).first();
	}
	
	public String queryMaxSortNumByPid(String addrPId) throws JDBCException {
		String sql = " select  decode(max(t.sort_num),null,0,max(t.sort_num)) sort_num from t_address t where t.addr_pid=? ";
		return this.findUnique(sql,addrPId);
	}
	
	
}
