/**
 * CCustDao.java	2010/02/08
 */

package com.ycsoft.business.dao.core.cust;

import static com.ycsoft.commons.helper.StringHelper.append;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.cust.CCust;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.business.dto.core.cust.CustGeneralInfo;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;


/**
 * CCustDao -> C_CUST table's operator
 */
@Component
public class CCustDao extends BaseEntityDao<CCust> {

	/**
	 *
	 */
	private static final long serialVersionUID = 7887394978274473475L;

	/**
	 * default empty constructor
	 */
	public CCustDao() {}
	
	/**
	 * 客户锁，并发锁定
	 * @param cust_id
	 * @return
	 * @throws JDBCException
	 */
	public CCust lockCust(String cust_id) throws JDBCException{
		String sql="select * from c_cust where cust_id=? for update ";
		return this.createQuery(sql, cust_id).first();
	}

	/**
	 * 按条件搜索客户
	 * @param p
	 * @param countyId
	 * @return
	 * @throws Exception
	 */
	public Pager<CCust> searchCust(Pager<Map<String ,Object>> p, String dataType,String countyId)throws Exception{
		String sql = null ;
		Iterator<?> it=p.getParams().keySet().iterator();
		String key = it.next().toString();
		String value = p.getParams().get(key).toString();
		
		Pager<CCust> resultPager =null;
		if(key.equals("cust_no")){
			sql = append("  SELECT t1.* FROM c_cust t1 " ,
					" where t1.county_id = ? and t1.cust_no =? ",
					dataType.trim().equals("1=1")?"":" and t1."+dataType.trim(),
					" order by cust_no"		);
			resultPager = createQuery(sql, countyId,value).setStart(p.getStart()).setLimit(p.getLimit()).page();	
					
		}else if(key.equals("cust_name")){
			if(StringHelper.isNotEmpty(value)){
				value=value.toLowerCase();
				value=value.replaceAll(" ", "");
			}
			sql = append("SELECT t1.* FROM c_cust t1",
					" where lower( replace(t1.cust_name,' '))  like ? ",
					dataType.trim().equals("1=1")?"":" and t1."+dataType.trim(),
					" order by cust_no");
			resultPager = createQuery(sql, "%"+value+"%").setStart(p.getStart()).setLimit(p.getLimit()).page();	
		}else if(key.equals("device_id")){
			String deviceId = value.replace(":","").replace("：", "");
			sql = append(
					" SELECT t1.cust_id,t1. cust_name,t1. cust_no,t1. old_cust_no,t1. addr_id,t1. address,t1. status,t1. password,t1. cust_type,t1. cust_level,t1. cust_class,t1. cust_colony,t1. net_type,t1. is_black,t1. open_time,t1. area_id,t1. county_id,t1. remark,t1. str1,t1. str2,t1. str3,t1. str4,t1. str5,t1. str6,t1. str7,t1. str8,t1. str9,t1. str10,t1. cust_count,t1. app_code,t1. cust_class_date,t1. optr_id,t1. dept_id,t1. spkg_sn",
					" FROM c_cust t1,c_cust_device t2",
					" where t1.cust_id = t2.cust_id",
					" and t2.device_code = ? ",
					dataType.trim().equals("1=1")?"":" and t1."+dataType.trim(),
					" union all",
					" SELECT t1.cust_id,t1. cust_name,t1. cust_no,t1. old_cust_no,t1. addr_id,t1. address,t1. status,t1. password,t1. cust_type,t1. cust_level,t1. cust_class,t1. cust_colony,t1. net_type,t1. is_black,t1. open_time,t1. area_id,t1. county_id,t1. remark,t1. str1,t1. str2,t1. str3,t1. str4,t1. str5,t1. str6,t1. str7,t1. str8,t1. str9,t1. str10,t1. cust_count,t1. app_code,t1. cust_class_date,t1. optr_id,t1. dept_id,t1. spkg_sn",
					" FROM c_cust t1,c_user t2",
					" where t1.cust_id=t2.cust_id and t2.login_name=?",
					dataType.trim().equals("1=1")?"":" and t1."+dataType.trim(),
					" order by cust_no"
					
			);
			resultPager = createQuery(sql,deviceId,deviceId).setStart(p.getStart()).setLimit(p.getLimit()).page();	
		}else if(key.equals("addr_name")){
			sql = append("SELECT t1.* FROM c_cust t1 ",
					" where  t1.county_id=? ",
					" and  t1.address like '%'||?||'%' ",
					dataType.trim().equals("1=1")?"":" and t1."+dataType.trim(),
					" order by cust_no"		);	
			
			resultPager = createQuery(sql, countyId,value).setStart(p.getStart()).setLimit(p.getLimit()).page();	
			
		}else if(key.equals("tel")){
			sql = append("SELECT t1.* FROM c_cust t1, c_cust_linkman t2",
					" where t1.cust_id = t2.cust_id and t1.county_id=? ",
					" and (t2.tel=? or t2.mobile=? )",
					dataType.trim().equals("1=1")?"":" and t1."+dataType.trim(),
					" order by cust_no"		);
			resultPager = createQuery(sql, countyId,value,value).setStart(p.getStart()).setLimit(p.getLimit()).page();	
		}else{
			sql = append("SELECT t1.* FROM c_cust t1 where t1.county_id=? ",
					"and ", getSqlGenerator().and( p.getParams()),
					 dataType.trim().equals("1=1")?"":" and t1."+dataType.trim(),
					" order by cust_no"		 
					);
			resultPager = createQuery(sql, countyId).setStart(p.getStart()).setLimit(p.getLimit()).page();
		}
		//销户客户查询
		/*if(resultPager.getRecords() == null || resultPager.getRecords().size() == 0){
			
			if(key.equals("addr_name")){
				sql = append("SELECT t1.* FROM c_cust_his t1 ",
						" where  t1.county_id=? ",
						" and  t1.address like '%'||?||'%' ",
						dataType.trim().equals("1=1")?"":" and t1."+dataType.trim(),
						" order by cust_no"		);	
				resultPager = createQuery(sql, countyId,value).setStart(p.getStart()).setLimit(p.getLimit()).page();	
				
			}else if(key.equals("tel")){
				sql = append("SELECT t1.* FROM c_cust_his t1, c_cust_linkman_his t2",
						" where t1.cust_id = t2.cust_id and t1.county_id=? ",
						" and (t2.tel=? or t2.mobile=? )",
						dataType.trim().equals("1=1")?"":" and t1."+dataType.trim(),
						" order by cust_no"		);
				resultPager = createQuery(sql, countyId,value,value).setStart(p.getStart()).setLimit(p.getLimit()).page();	
			}else if(key.equals("cust_no")){
				sql = append("  SELECT t1.* FROM c_cust_his t1 " ,
						" where t1.county_id = ? and t1.cust_no =? ",
						dataType.trim().equals("1=1")?"":" and t1."+dataType.trim(),
						" order by cust_no"		);
				resultPager = createQuery(sql, countyId,value).setStart(p.getStart()).setLimit(p.getLimit()).page();	
						
			}else if(key.equals("cust_name")){
				sql = append("SELECT t1.* FROM c_cust_his t1",
						" where t1.county_id=? ",
						"  and(  t1.cust_name like '%'||?||'%' ",
						"  or  lower( replace(t1.cust_name,' '))  like '%'||?||'%' )",
						dataType.trim().equals("1=1")?"":" and t1."+dataType.trim(),
						" order by cust_no"		);
				resultPager = createQuery(sql, countyId,value,value).setStart(p.getStart()).setLimit(p.getLimit()).page();	
			}
			
			if(resultPager.getRecords() != null && resultPager.getRecords().size() > 0){
				for(CCust his: resultPager.getRecords()){
					his.setStatus(StatusConstants.INVALID);
				}
			}
		}*/	
		return resultPager;
	}
	
	
	
	/**
	 * 按条件搜索客户
	 * @param p
	 * @param countyId
	 * @return
	 * @throws Exception
	 */
	public Pager<CCust> searchTransportableCust(String custNameLike, String dataType,String countyId,Integer start,Integer limit)throws Exception{
		String sql = null ;
		sql = append("SELECT t1.* FROM c_cust t1 where t1.county_id=? and t1.status = ? ",
				" and cust_name like '%"+custNameLike+"%'");
		String addData ="";
		if(dataType.trim().equals("1=1")){
			addData = " 1=1 ";
		}else{
			addData =" t1."+dataType.trim();
		}
		sql = StringHelper.append(sql, " and ", addData," order by cust_no");
		return createQuery(sql, countyId,StatusConstants.ACTIVE).setStart(start).setLimit(limit).page();
	}
	
	
	/**
	 * 多条件查询客户
	 * @param cust
	 * @param start
	 * @param limit
	 * @param countyId 
	 * @return
	 * @throws JDBCException 
	 */
	@SuppressWarnings("unchecked")
	public Pager<CCust> complexSearchCust(CCust cust, Integer start,
			Integer limit, String dataType, String countyId) throws Exception {
		String sql = " SELECT t1.* from c_cust t1 where t1.county_id =? " ;
		Pager<CCust> resultPager = null;
		if(StringHelper.isNotEmpty(cust.getCust_name())){
			sql = append(sql," and t1.cust_name like '" + cust.getCust_name() + "%'");
		}
		if(StringHelper.isNotEmpty(cust.getAddr_id())){
			sql = append(sql," and t1.addr_id = '"+cust.getAddr_id() + "'");
		}
		if(StringHelper.isNotEmpty(cust.getAddress())){
			sql = append(sql," and t1.address like '%" + cust.getAddress() + "%'");
		}
		if(StringHelper.isNotEmpty(cust.getCust_type())){
			sql = append(sql," and t1.cust_type = '" + cust.getCust_type() + "'");
		}
		if(StringHelper.isNotEmpty(cust.getStatus())){
			sql = append(sql," and t1.status = '" + cust.getStatus() + "'");
		}
		if(StringHelper.isNotEmpty(cust.getLogin_name())){
			sql = append(sql, " and t1.cust_id in (select cust_id from c_user where login_name='"+cust.getLogin_name()+"')");
		}
		if(StringHelper.isNotEmpty(cust.getNet_type())){
			sql = append(sql, " and exists (select 1 from c_cust_linkman l where t1.cust_id=l.cust_id and t1.county_id=l.county_id",
					" and l.cert_num='"+cust.getNet_type()+"')");
		}
		sql += " and "+dataType;
		resultPager = createQuery(sql, countyId).setStart(start).setLimit(limit).page();
		//查找已销户的客户
		/*if (resultPager.getRecords()== null ||resultPager.getRecords().size()==0){
			sql = " SELECT t1.* from c_cust_his t1 where t1.county_id =? " ;
			if(StringHelper.isNotEmpty(cust.getCust_name())){
				sql = append(sql," and t1.cust_name like '" + cust.getCust_name() + "%'");
			}
			if(StringHelper.isNotEmpty(cust.getAddr_id())){
				sql = append(sql," and t1.addr_id = '"+cust.getAddr_id() + "'");
			}
			if(StringHelper.isNotEmpty(cust.getAddress())){
				sql = append(sql," and t1.address like '%" + cust.getAddress() + "%'");
			}
			if(StringHelper.isNotEmpty(cust.getCust_type())){
				sql = append(sql," and t1.cust_type = '" + cust.getCust_type() + "'");
			}
			if(StringHelper.isNotEmpty(cust.getStatus())){
				sql = append(sql," and t1.status = '" + cust.getStatus() + "'");
			}
			if(StringHelper.isNotEmpty(cust.getLogin_name())){
				sql = append(sql, " and t1.cust_id in (select cust_id from c_user_his where login_name='"+cust.getLogin_name()+"')");
			}
			sql += " and "+dataType;
			resultPager = createQuery(sql, countyId).setStart(start).setLimit(limit).page();
		}*/
		
		return resultPager;
	}

	/**
	 * 根据名称查找单位客户信息
	 * @param unitName
	 * @param countyId
	 * @return
	 * @throws Exception
	 */
	public List<CCust> queryUnitByNameAndAddr(String unitName,String addr,String countyId,String dataType) throws Exception{
		List<CCust> custList = null;
		String sql = "select * from c_cust t1" +
				" where cust_name like '%"+unitName+"%'" +
				" and cust_type = ? and county_id=?";
		if(StringHelper.isNotEmpty(addr)){
			sql+=" and address like '%"+addr+"%'";
		}
		
		if(dataType==null||dataType.trim().equals("")||dataType.trim().equals("1=1")){
			sql+= " and 1=1 ";
		}else{
			sql+=" and t1."+dataType.trim();
		}
		
		custList = this.createQuery(sql,
				SystemConstants.CUST_TYPE_UNIT, countyId).list();
		return custList;
	}
	
	/**
	 * 根据名称查找模拟大客户信息
	 * @param unitName
	 * @param countyId
	 * @return
	 * @throws Exception
	 */
	public List<CCust> queryMnCustByNameAndAddr(String mnCustName,String addr,String countyId) throws Exception{
		List<CCust> custList = null;
		String sql = "select * from c_cust " +
				" where cust_name like '%"+mnCustName+"%'" +
				" and cust_colony in (?,?) and county_id=?";
		if(StringHelper.isNotEmpty(addr)){
			sql+=" and address like '%"+addr+"%'";
		}
		custList = this.createQuery(sql,
				SystemConstants.CUST_COLONY_MNDKH,SystemConstants.CUST_COLONY_XYKH, countyId).list();
		return custList;
	}
	
	/**
	 *根据地市编号查找所有单位 
	 */
	public List<CCust> getUnitAll(String countyId) throws Exception{
		List<CCust> custList = null;
		String sql = "select * from c_cust " +
				" where  cust_type = ?" +
				" and county_id=?";
		custList = this.createQuery(CCust.class, sql,
				SystemConstants.CUST_TYPE_UNIT, countyId).list();
		return custList;
	}
	
	
	
	/**
	 * 根据居民客户id查找对应的单位信息
	 * @param ResidentCustId
	 * @param countyId
	 * @return
	 * @throws Exception
	 */
	public List<CCust> queryUnitByResident(String residentCustId,
			String countyId) throws Exception {
		List<CCust> custList = null;
		String sql = "select a.*,b.create_time from c_cust a,c_cust_unit_to_resident b "
				+ " where a.cust_id=b.unit_cust_id "
				+ " and b.resident_cust_id=? and a.county_id=?";

		custList = this.createQuery(CCust.class, sql, residentCustId, countyId)
				.list();
		return custList;
	}
	
	/**
	 * 根据居民客户id查找对应的模拟大客户信息
	 * @param ResidentCustId
	 * @param countyId
	 * @return
	 * @throws Exception
	 */
	public List<CCust> queryMnCustByResident(String residentCustId,
			String countyId) throws Exception {
		List<CCust> custList = null;
		String sql = "select a.*,b.create_time from c_cust a,c_cust_mn_to_resident b "
				+ " where a.cust_id=b.mn_cust_id "
				+ " and b.cust_id=? and a.county_id=?";

		custList = this.createQuery(CCust.class, sql, residentCustId, countyId)
				.list();
		return custList;
	}

	public CCust queryCustFullInfo(String searchType, String searchValue, String dataRight) throws JDBCException {
		String sql = "";
		if ("CUST_NO".equals(searchType)){
			sql = "SELECT * FROM c_cust t WHERE t.cust_no=? and "+dataRight;
		}else if("CARD_ID".equals(searchType)){
			sql = "SELECT * FROM c_cust t,c_user u WHERE t.cust_id=u.cust_id AND u.card_id=? and "+dataRight;
		}
		if(StringHelper.isEmpty(sql))
			return null;
		return createQuery(sql, searchValue).first();
	}

	/**
	 * 根据客户ID查询客户
	 * @param custId
	 * @return
	 * @throws JDBCException
	 */
	public CCust searchById(String custId, String dataType) throws JDBCException{
		String sql = "select b.busi_optr_id,b.serv_optr_id,b.net_type as add_net_type,t1.*,d.addr_name||b.addr_name addr_id_text,c.t1,c.t2,c.t3,c.t4,c.t5,c.note "
			+ " from c_cust t1 ,t_address b ,c_cust_addr c,t_address d "
			+ " where t1.cust_id=? and t1.addr_id=b.addr_id and t1.cust_id=c.cust_id and d.addr_id=b.addr_pid and ";
		String addData ="";
		if(dataType.trim().equals("1=1")){
			addData = " 1=1 ";
		}else{
			addData =" t1."+dataType.trim();
		}
		sql = StringHelper.append(sql,addData);
		CCust cust = createQuery(sql, custId).first();
		if(cust != null){
			return cust;
		}else{
			sql = " select b.net_type as add_net_type,t1.cust_id, cust_name, cust_no, old_cust_no, t1.addr_id, address, 'INVALID' status,"
			+ " PASSWORD, cust_type, cust_level, cust_class, cust_colony, t1.net_type, is_black, open_time,"
			+ " t1.area_id, t1.county_id, t1.remark, str1, str2, str3, str4, str5, str6, str7, str8, str9, str10 ,b.addr_name addr_id_text,c.t1,c.t2,c.t3,c.t4,c.t5,c.note "
			+ " from c_cust_his t1 ,t_address b ,c_cust_addr_his c "
			+ " where t1.cust_id=? and t1.addr_id=b.addr_id and t1.cust_id=c.cust_id and "+addData;
			return createQuery(sql, custId).first();
		}
	}
	
	/**
	 * 查询指定单位custId下的所有客户
	 * @param custId
	 * @return
	 * @throws JDBCException
	 */
	public List<CCust> queryUnitMember(String custId) throws JDBCException {
		String sql = "SELECT * FROM c_cust c ,c_cust_unit_to_resident r WHERE "
				+ " c.cust_id=r.resident_cust_id AND r.unit_cust_id=?";
		return createQuery(CCust.class, sql, custId).list();
	}

	/**
	 * 查询客户的完整信息
	 * 包含客户基本信息、地址信息、非居民信息
	 * @param countyId
	 * @return
	 * @throws Exception
	 */
	public List<CCust> queryCustFullInfoToCallCenter(String cust_no, String cust_name, String address, String card_id, String telOrMobile, String modem_mac, String stb_id, String band_login_name, String county_id)throws Exception{
		String sql = "SELECT distinct t.* FROM c_cust t, c_user t1, c_cust_linkman t2, c_user_broadband t3 WHERE t.cust_id=t2.cust_id "
					+" AND t.cust_id = t1.cust_id(+)  and t1.user_id=t3.user_id(+) AND t.county_id = ?";
		List<String> wheres = new ArrayList<String>();
		List<Object> params = new ArrayList<Object>();
		params.add(county_id);
		if(StringHelper.isNotEmpty(cust_no)){
			wheres.add(" t.cust_no = ? ");
			params.add(cust_no);
		}
		if(StringHelper.isNotEmpty(card_id)){
			wheres.add(" t1.card_id = ? ");
			params.add(card_id);
		}
		if(StringHelper.isNotEmpty(modem_mac)){
			wheres.add(" t1.modem_mac = ? ");
			params.add(modem_mac);
		}
		if(StringHelper.isNotEmpty(stb_id)){
			wheres.add(" t1.stb_id = ? ");
			params.add(stb_id);
		}
		
		if(StringHelper.isNotEmpty(band_login_name)){
			wheres.add(" t3.login_name = ? ");
			params.add(band_login_name);
		}
		
		if(StringHelper.isNotEmpty(cust_name)){
			wheres.add(" t.cust_name like ? ");
			params.add("%" + cust_name + "%");
		}
		
		if(StringHelper.isNotEmpty(address)){
			wheres.add(" t.address like ? ");
			params.add("%" + address + "%");
		}
		
		if(StringHelper.isNotEmpty(telOrMobile)){
			wheres.add(" (t2.tel like ? or t2.mobile like ?) ");
			params.add("%" + telOrMobile + "%");
			params.add("%" + telOrMobile + "%");
		}
		
		StringBuffer tempWhere = new StringBuffer();
		for (String w : wheres) {
			tempWhere.append(" and ");
			tempWhere.append(w);
		}
		
		if(tempWhere.length() == 0 ){
			throw new IllegalArgumentException("not found sql condition");
		}
		sql = "select * from ("+sql+tempWhere+") where ROWNUM <= 20 ";
		return createQuery(sql, params.toArray()).list();
	}
	
	public CustGeneralInfo SearchCustGeneralInfo(String custId, String countyId) throws JDBCException{
		String sql = "select c.cust_no,c.cust_id,c.cust_name,"
		       +" count(u.user_id) totalUserAmount,"
		       +" sum(decode(u.status,'ACTIVE',1,0)) activeUserAmount,"
//		       +" sum(decode(u.status,'REQSTOP',1,0)) stopUserAmount,"
		       +" sum(decode(u.user_type,'ATV',1,0)) atvUserAmount,"
		       +" sum(decode(u.user_type,'DTV',1,0)) dtvUserAmount,"
		       +" sum(decode(u.user_type,'BAND',1,0)) bandUserAmount,"
		       
		       +" count(p.prod_id) totalProdAmount,"
		       +" sum(decode(p.status,'ACTIVE',1,0)) activeProdAmount,"
//		       +" sum(decode(p.status,'OWESTOP',1,0)) oweStopProdAmount,"
		       +" sum(decode(p.status,'LINKSTOP',1,0)) oweUnStopProdAmount,"
		       
		       +" sum(aa.active_balance - aa.owe_fee - aa.real_bill) totalBalance,"
		       +" sum(aa.owe_fee) totalOwn,"
		       
		       +" nvl(b.balance,0),"
		       +" nvl(b.his_balance,0)"
		       
		       +" from c_cust c,c_user u,c_prod p,c_acct a,c_acct_acctitem aa,c_cust_bonuspoint b"
		       +" where c.cust_id=u.cust_id and c.cust_id=p.cust_id and u.user_id=p.user_id"
		       +" and c.cust_id=a.cust_id and a.user_id=u.user_id and a.acct_id=aa.acct_id"
		       +" and c.cust_id=b.cust_id(+) and c.cust_id=? and c.county_id=?"
		       +" group by c.cust_no,c.cust_id,c.cust_name,b.balance,b.his_balance";
		return this.createQuery(CustGeneralInfo.class, sql, custId, countyId).first();
	}

	/**
	 * 根据客户地址，模糊查找没加入单位的居民客户
	 * @param query
	 * @return
	 * @throws JDBCException
	 */
	public List<CCust> searchResidentCust(CCust cust, String dataType,String countyId) throws JDBCException {
		String sql = StringHelper.append("select * from c_cust c ",
				"where county_id=? and c.cust_type =?  and ",dataType,
				" and c.cust_id not in (select resident_cust_id from c_cust_unit_to_resident) ");
		if(StringHelper.isNotEmpty(cust.getAddress())){
			sql += " and c.address like '%"+cust.getAddress()+"%'";
		}
		if(StringHelper.isNotEmpty(cust.getCust_colony())){
			sql += " and c.cust_colony = '"+cust.getCust_colony()+"'";
		}
		if(StringHelper.isNotEmpty(cust.getCust_class())){
			sql += " and c.cust_class = '"+cust.getCust_class()+"'";
		}
		sql += " and rownum<=500 ";
		return createQuery(CCust.class,sql,countyId,SystemConstants.CUST_TYPE_RESIDENT).list();
	}

	public List<CCust> queryCustByUnit(String unitId, String[] custIds,
			String countyId) throws JDBCException {
		String sql = "select a.*,b.create_time from c_cust a,c_cust_unit_to_resident b "
				+ " where a.cust_id=b.unit_cust_id and b.unit_cust_id=?"
				+ " and b.resident_cust_id in ("+getSqlGenerator().in(custIds)+") and a.county_id=?";

		return createQuery(CCust.class, sql, unitId, countyId)
		.list();
	}
	
	/**
	 * 验证客户密码
	 * @param custId
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public CCust validCustByPassword(String custId,String password) throws Exception{
		String sql = "select * from c_cust c where c.cust_id =? and c.password = ?";
		return createQuery(CCust.class, sql, custId,password).first();
	}
	
	/**
	 * 根据数组客户编号，查询多个客户信息
	 * @param custIds
	 * @return
	 * @throws JDBCException
	 */
	public List<CCust> queryCustByCustIds(String[] custIds) throws JDBCException {
		String sql = "SELECT * FROM c_cust where "+getSqlGenerator().setWhereInArray("cust_id",custIds)+"";
		return createQuery(sql).list();
	}
	
	public List<CCust> queryCustByCustNos(String[] custNos) throws JDBCException {
		String sql = "SELECT * FROM c_cust where "+getSqlGenerator().setWhereInArray("cust_no",custNos)+"";
		return createQuery(sql).list();
	}
	
	public CCust queryCustByCustNo(String custNo) throws JDBCException {
		String sql = "SELECT * FROM c_cust where cust_no=?";
		return this.createQuery(sql, custNo).first();
	}
	
	public Pager<CCust> queryCustAddrByCustIds(String[] custIds, String countyId,Integer start,Integer limit) throws Exception {
		String sql = "SELECT cc.*,cc.address old_address,cca.t1,cca.t2,cca.t3,cca.t4,cca.t5,cca.note FROM c_cust cc,c_cust_addr cca " +
				" where "+getSqlGenerator().setWhereInArray("cc.cust_id",custIds)+" and cc.cust_id= cca.cust_id and cc.county_id = ? ";
		return createQuery(CCust.class, sql,countyId).setStart(start).setLimit(limit).page();
	}
	
	public Pager<CCust> queryCustAddress(CCust cust, String countyId,Integer start,Integer limit) throws Exception {
		String addStr = "";
		String filter = " and ( ";
		if (StringHelper.isNotEmpty(cust.getT1())) {
			addStr += " cca.t1 like '%" + cust.getT1()+ "%' and ";
		}
		if (StringHelper.isNotEmpty(cust.getT2())) {
			addStr += " cca.t2 like '%" + cust.getT2()+ "%' and ";
		}
		if (StringHelper.isNotEmpty(cust.getT3())) {
			addStr += " cca.t3 like '%" + cust.getT3()+ "%' and ";
		}
		if (StringHelper.isNotEmpty(cust.getT4())) {
			addStr += " cca.t4 like '%" + cust.getT4()+ "%' and ";
		}
		if (StringHelper.isNotEmpty(cust.getT5())) {
			addStr += " cca.t5 like '%" + cust.getT5()+ "%' and ";
		}
		if (StringHelper.isNotEmpty(cust.getNote())) {
			addStr += " cca.note like '%" + cust.getNote()+ "%' and ";
		}
		if (StringHelper.isNotEmpty(cust.getAddr_id())) {
			addStr += " cc.addr_id = '"+ cust.getAddr_id() +"' and ";
		}
		if (StringHelper.isNotEmpty(cust.getAddress())) {
			addStr += " cc.address like '%" + cust.getAddress()+ "%' and ";
		}
		
		if (StringHelper.isNotEmpty(addStr)) {
			filter +=  StringHelper.delEndChar( addStr,4 )+")";
		}else{
			filter = "";
		}
		String sql = "select ta1.addr_name||ta.addr_name addr_id_text,cca.t1,cca.t2,cca.t3,cca.t4,cca.t5,cca.note, cc.*,cc.address old_address" +
				" from c_cust cc, c_cust_addr cca, t_address ta  ,t_address ta1" +
				" where cc.cust_id = cca.cust_id   and cc.addr_id = ta.addr_id and ta.addr_pid = ta1.addr_id and cc.county_id= ? "  + filter
				+ "  order by cc.address ";
		return createQuery(CCust.class, sql, countyId).setStart(start).setLimit(limit).page();
	}
	
	public List<CCust> queryAddressAll(String addrId, String countyId) throws Exception {
		String sql = "select * from c_cust where addr_id = ? and county_id= ? ";
		return createQuery(CCust.class, sql,addrId,countyId).list();
	}

	public int queryImportanceCustNum(String countyId,String dataRight) throws Exception {
		String sql = " select count(distinct cc.cust_id) from c_cust cc " +
				" where cc.cust_id in (select cust_id from " +
				" (select t.*,d.cust_id from c_acct_acctitem t ,(select  cust_id,acct_id from c_acct  " +
				" where acct_id  in (select t1.acct_id from c_acct t1, c_cust t2 where  t2.county_id = ? and t2.str1 = ? and t1.cust_id= t2.cust_id  )) d " +
				" where t.acct_id = d.acct_id ) a where 1=1 and " + dataRight+")";		
		return count(sql,countyId, SystemConstants.BOOLEAN_TRUE);
	}
	public Pager<CCust> queryImportanceCust(Integer start,
			Integer limit, String dataRight, String countyId) throws Exception {
		String sql = " select * from c_cust cc " +
				" where cc.cust_id in (select cust_id from " +
				" (select t.*,d.cust_id from c_acct_acctitem t ,(select  cust_id,acct_id from c_acct  " +
				" where acct_id  in (select t1.acct_id from c_acct t1, c_cust t2 " +
				" where t2.county_id = ? and t2.str1 = ? and t1.cust_id= t2.cust_id )) d where t.acct_id = d.acct_id ) a " +
				" where 1=1 and " + dataRight+")";
		return createQuery(CCust.class, sql,countyId,SystemConstants.BOOLEAN_TRUE ).setStart(start).setLimit(limit).page();
	}
	
	public void updateCustAddr(String oldAddrId, String newAddrId,String countyId)  throws Exception {
		String sql=" update c_cust set addr_id = ? where addr_id = ? and county_id = ? ";
		this.executeUpdate(sql, newAddrId,oldAddrId,countyId);
	}

	public void batchLogoffCust(Integer doneCode,String remark,List<String> custIds, String isReclaimDevice, String deviceStatus, SOptr optr) throws Exception {
		for(String custId :custIds){
			if (StringHelper.isNotEmpty(custId))
			this.getJdbcTemplate().execute("call proc_del_cust('"+custId+"','"+isReclaimDevice+"', '"+deviceStatus+"','"+doneCode+"'," +
					"'"+optr.getOptr_id()+"',"+optr.getCounty_id()+",'"+optr.getArea_id()+"','"+optr.getDept_id()+"','"+remark+"')");
		}
	}
	
	public CCust queryBySpkgSn(String spkgSn) throws Exception {
		String sql = "select * from c_cust where spkg_sn=?";
		return this.createQuery(sql, spkgSn).first();
	}
	
	public void clearSpkgSn(String spkgSn) throws Exception {
		String sql = "update c_cust set spkg_sn=null where spkg_sn=?";
		this.executeUpdate(sql, spkgSn);
	}
	
}
