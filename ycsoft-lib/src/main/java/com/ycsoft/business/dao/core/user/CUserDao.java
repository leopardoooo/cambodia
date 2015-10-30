/**
 * CUserDao.java	2010/02/25
 */

package com.ycsoft.business.dao.core.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.beans.core.user.CUserStb;
import com.ycsoft.beans.core.user.UserResExpDate;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.business.dto.core.bill.UserBillDto;
import com.ycsoft.business.dto.core.user.ChangedUser;
import com.ycsoft.business.dto.core.user.UserDto;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;


/**
 * CUserDao -> C_USER table's operator
 */
@Component
public class CUserDao extends BaseEntityDao<CUser> {

	/**
	 *
	 */
	private static final long serialVersionUID = -4250480955592139362L;

	/**
	 * default empty constructor
	 */
	public CUserDao() {}
	
	public List<CUser> queryUserByDoneCode(Integer done_code)
			throws JDBCException {
		String sql = "SELECT u.* FROM c_done_code_detail d,c_user u WHERE u.user_id=d.user_id AND d.done_code=?";
		return createQuery(sql, done_code).list();
	}
	
	/**
	 * 欠费达一年及以上的用户
	 * @param countyId
	 * @param ownLongDays 
	 * @return
	 * @throws Exception
	 */
	public List<CUser> queryOwnFeeUser(String countyId, String ownLongDays) throws Exception {
		/*String sql = "select distinct u.* from busi.c_prod cp,busi.c_user u"
			+ " where cp.user_id=u.user_id and u.status='ACTIVE' and cp.county_id=u.county_id and cp.county='9005'"
			+ " and cp.prod_id in (select pp.prod_id from busi.p_prod pp where pp.is_base = 'T' and pp.serv_id in ('DTV', 'ATV'))"
			+ " and cp.package_sn is null and cp.status = 'OWESTOP' and cp.status_date < sysdate - 365"
			+ " and not exists"
			+ " (select 1 from busi.c_prod cp1"
			+ " where cp.user_id = cp1.user_id"
			+ " and cp.county_id = cp1.county_id"
			+ " and cp1.prod_id in (select pp.prod_id from busi.p_prod pp where pp.is_base = 'T' and pp.serv_id in ('DTV', 'ATV'))"
			+ " and cp1.prod_sn <> cp.prod_sn"
			+ " and (cp1.status <> 'OWESTOP' or (cp1.status = 'OWESTOP' and cp1.status_date >= sysdate - 365)))";*/
		//当一个数字或者模拟用户的基本包状态是欠费停，且状态变更日期+一年<=当天时，
		//如果不存在相同用户下其他基本包，产品状态是(非欠费停  或者  产品状态欠费停状态变更日期+一年>当天)时，则 该用户为长期欠费用户
		String sql = "select distinct u.* from c_prod cp,c_user u"
			+ " where cp.user_id=u.user_id and u.status=? and cp.county_id=u.county_id and cp.county_id=?"
			+ " and cp.prod_id in (select pp.prod_id from p_prod pp where pp.is_base=? and pp.serv_id in (?,?))"
			+ " and cp.package_sn is null and cp.status=? and cp.status_date < sysdate - ?"
			+ " and not exists"
			+ " (select 1 from c_prod cp1"
			+ " where cp.user_id = cp1.user_id"
			+ " and cp.county_id = cp1.county_id"
			+ " and cp1.prod_id in (select pp.prod_id from p_prod pp where pp.is_base=? and pp.serv_id in (?,?))"
			+ " and cp1.prod_sn <> cp.prod_sn"
			+ " and (cp1.status <> ? or (cp1.status = ? and cp1.status_date >= sysdate - ?))) and rownum <= 1000";
		return this.createQuery(sql, StatusConstants.ACTIVE, countyId,
				SystemConstants.BOOLEAN_TRUE, SystemConstants.PROD_SERV_ID_ATV,
				SystemConstants.PROD_SERV_ID_DTV, StatusConstants.OWESTOP,ownLongDays,
				SystemConstants.BOOLEAN_TRUE, SystemConstants.PROD_SERV_ID_ATV,
				SystemConstants.PROD_SERV_ID_DTV, StatusConstants.OWESTOP,
				StatusConstants.OWESTOP,ownLongDays).list();
	}

	/**
	 * @param custId
	 * @param county_id
	 * @return
	 */
	public List<CUser> queryUserFullInfo(Pager<Map<String ,Object>> p, String county_id) throws Exception{
		return null;
	}

	/**
	 * 用户转户
	 * @param userId 用户ＩＤ
	 * @param targetCustId　目标客户
	 * @throws JDBCException
	 */
	public void transferUser( String[] userId, String targetCustId) throws JDBCException{
		String sql = "update c_user cu set cu.cust_id = '"+ targetCustId + "' where cu.user_id = ?";
		this.executeBatch(sql, userId);
	}
	/**
	 * 根据客户ID查询用户
	 * @param custId
	 * @return
	 */
	public List<CUser> queryUserByCustId(String custId) throws JDBCException {
		if (custId == null) return new ArrayList<CUser>();
		
		String sql = "select * from c_user where cust_id=?";
		return createQuery(sql, custId).list();
	}
	/**
	 * 查询有加挂杂费的用户
	 * @param custId
	 * @return
	 * @throws JDBCException
	 */
	public List<CUser> queryUserByIpAddressFee(String custId)  throws JDBCException {
		String sql = "select * from c_user  where str5 in (select fee_id from t_busi_fee) and str6<>'0' and cust_id=? ";
		return createQuery(sql, custId).list();
	}
	/**
	 * 查询正常和施工中的用户清单
	 * @param custId
	 * @return
	 * @throws JDBCException
	 */
	public List<CUser> queryCanSelectUserByCustId(String custId) throws JDBCException {
		if (custId == null) return new ArrayList<CUser>();
		
		String sql = "select * from c_user where cust_id=? and status in (?,?)";
		return createQuery(sql, custId,StatusConstants.ACTIVE,StatusConstants.INSTALL).list();
	}
	
	/**
	 * 根据客户ID查询正常用户,类型
	 * @param custId 客户id
	 * @param userType 用户类型 SystemConstants.USER_TYPE_*
	 * @return
	 */
	public List<CUser> queryUserByCustId(String custId,String userType) throws JDBCException {
		if (custId == null) return new ArrayList<CUser>();
		
		String sql = "select * from c_user where cust_id=? and user_type=? and status=?";
		return createQuery(sql, custId,userType,StatusConstants.ACTIVE).list();
	}	
	
	/**
	 * 根据客户编号查询用户
	 * @param custNo
	 * @return
	 * @throws JDBCException
	 */
	public List<CUser> queryUserByCustNo(String custNo) throws JDBCException {
		String sql = "select u.* from c_user u,c_cust c where u.cust_id=c.cust_id and c.cust_no=?";
		return createQuery(sql, custNo).list();
	}
	
	/**
	 * 根据机顶盒号查询用户
	 *
	 * @param stbId
	 * @return
	 */
	public List<CUser> queryUserByStbId(String stbId)
			throws JDBCException {
		String sql = "select * from c_user where stb_id=?";
		return createQuery(sql, stbId).list();
	}

	/**
	 * 根据智能卡号查询用户
	 *
	 * @return
	 */
	public List<CUser> queryUserByCardId(String cardId) throws JDBCException {
		String sql = "select * from c_user where card_id=?";
		return createQuery(sql, cardId).list();
	}

	/**
	 * 根据modem mac查询用户
	 * @return
	 */
	public List<CUser> queryUserByModemId(String modemMac) throws JDBCException {
		String sql = "select * from c_user where modem_mac=?";
		return createQuery(sql, modemMac).list();
	}

	/**
	 * 更新设备
	 * @param userId
	 * @param stbId
	 * @param cardId
	 * @param modemMac
	 * @throws JDBCException
	 */
	public void updateDevice(String userId,String stbId,String cardId,String modemMac) throws JDBCException {
		CUser user = new CUser();
		user.setUser_id(userId);
		user.setStb_id(stbId);
		user.setCard_id(cardId);
		user.setModem_mac(modemMac);
		update(user);
	}

	
	/**
	 * 用户是客户的第几个用户
	 * @param user_id
	 * @return
	 * @throws JDBCException 
	 */
	public Integer queryUserSequence(String custId, String userId) throws JDBCException {
		String sql = "SELECT se FROM (SELECT u.*,ROWNUM se FROM c_user u WHERE u.cust_id=? ORDER BY u.open_time ) t WHERE t.user_id=? ";
		String seq = findUnique(sql, custId, userId);
		return seq==null?0:Integer.parseInt(seq);
	}

	public List<CUserStb> queryUserStbByCustId(String custId) throws JDBCException {
		String sql ="select a.*,d.buy_mode,d.buy_time,nvl(b.real_pay,0) buy_fee,definition_type stb_definition_type,d.change_reason,s.device_model "+
					"  from c_user a,  c_fee b,  c_fee_device c,c_cust_device d,r_stb s,r_stb_model sm "+
					" where a.stb_id = c.device_code(+) and a.stb_id=s.stb_id(+) and s.device_model=sm.device_model(+)"+
					"   and c.fee_sn = b.fee_sn(+) "+
					"   and a.stb_id=d.device_code" +
					"   and a.cust_id=? and d.cust_id= ? ";
		return createQuery(CUserStb.class, sql, custId,custId).list();
	}
	public CUserStb queryUserStbByUserId(String userId) throws JDBCException {
		String sql ="select a.*,d.buy_mode,d.buy_time,nvl(b.real_pay,0) buy_fee,definition_type stb_definition_type,d.change_reason,s.device_model "+
					"  from c_user a,  c_fee b,  c_fee_device c,c_cust_device d,r_stb s,r_stb_model sm "+
					" where a.stb_id = c.device_code(+) and a.stb_id=s.stb_id(+) and s.device_model=sm.device_model(+)"+
					"   and c.fee_sn = b.fee_sn(+) "+
					"   and a.stb_id=d.device_code" +
					"   and a.user_id=?";
		return createQuery(CUserStb.class, sql, userId).first();
	}

	//模拟剪线
	public void saveAtvCustLine(String userId, String countyId) throws Exception {
		String sql = "update c_user t set t.status=? where t.user_id=? and t.county_id=?";
		this.executeUpdate(sql, StatusConstants.REQSTOP, userId, countyId);
	}

	public List<ChangedUser> queryChangedUserInfo(String countyId) throws JDBCException {
		String sql = StringHelper.append("select t1.card_id,t2.cust_name,t2.address,t3.mobile,to_char(t1.open_time,'yyyy-mm-dd') change_time,",
			" case when t3.cert_type='SFZ' then t3.cert_num else '' end id_num,", 
			" t1.area_id,t1.county_id from c_user t1,c_cust t2,c_cust_linkman t3",
			" where t1.cust_id=t2.cust_id and t1.cust_id=t3.cust_id and t1.county_id=? and t1.card_id is not null order by t1.open_time");
		return createQuery(ChangedUser.class, sql, countyId).list();
	}

	/**
	 * 查询开始日期到今天之前的销户用户
	 * @param beginDate
	 * @param countyId
	 * @return
	 * @throws JDBCException 
	 */
	public List<ChangedUser> queryDeletedUsers(String beginDate,String endDate, String countyId) throws JDBCException {
		String sql = StringHelper.append("select t1.user_id, c.user_id, c.card_id,to_char(c.done_date,'yyyy-mm-dd hh24:mi:ss') change_time,'DELETE' data_type from c_user_his c where c.card_id is not null",
				" and c.county_id=? and c.done_date > to_date(?,'yyyy-mm-dd') and c.done_date < to_date(?,'yyyy-mm-dd')");
		return createQuery(ChangedUser.class, sql, countyId,beginDate,endDate).list();
	}

	/**
	 * 查询开始日期到今天之前的修改用户
	 * @param beginDate
	 * @param countyId
	 * @return
	 * @throws JDBCException 
	 */
	public List<ChangedUser> queryModifiedUsers(String beginDate,String endDate,
			String countyId) throws JDBCException {
		String sql = StringHelper.append("select * from (select * from( " ,
		" select t1.user_id, 'UPDATE' data_type, t1.card_id,t2.cust_name,t2.address,t3.mobile,t4.old_value old_card_id,to_char(t4.change_time,'yyyy-mm-dd hh24:mi:ss') change_time, ",
		" case when t3.cert_type = 'SFZ' then t3.cert_num else '' end id_num,t1.area_id,t1.county_id",
		" from c_user t1, c_cust t2, c_cust_linkman t3,c_user_prop_change t4 where t1.cust_id = t2.cust_id",
		" and t1.cust_id = t3.cust_id and t1.card_id is not null and t1.user_id=t4.user_id",
		" and t4.column_name='card_id'  and t1.county_id = :countyId and t4.change_time > to_date(:beginDate,'yyyy-mm-dd') ",
		" and t4.change_time < to_date(:endDate,'yyyy-mm-dd')",
		" union",
		" select t1.user_id, 'UPDATE' data_type,  t1.card_id,t2.cust_name,t2.address,t3.mobile,'' old_card_id,to_char(t4.change_time,'yyyy-mm-dd hh24:mi:ss') change_time, ",
		" case when t3.cert_type = 'SFZ' then t3.cert_num else ''end id_num,t1.area_id,t1.county_id",
		" from c_user t1, c_cust t2, c_cust_linkman t3,c_cust_prop_change t4 where t1.cust_id = t2.cust_id",
		" and t1.cust_id = t3.cust_id and t1.card_id is not null and t1.cust_id=t4.cust_id",
		" and (t4.column_name='address' or t4.column_name='cust_name' or t4.column_name='mobile')",
		" and t1.county_id = :countyId and t4.change_time > to_date(:beginDate,'yyyy-mm-dd') and t4.change_time < to_date(:endDate,'yyyy-mm-dd')" ,
		" union " ,
		" select c.user_id, 'DELETE' data_type,c.card_id,'' cust_name, '' address ,'' mobile, '' old_card_id," ,
		" to_char(c.done_date,'yyyy-mm-dd hh24:mi:ss') change_time,'' id_num,'' area_id,'' county_id " ,
		" from c_user_his c where c.card_id is not null",
		" and c.county_id=:countyId and c.done_date > to_date(:beginDate,'yyyy-mm-dd') and c.done_date < to_date(:endDate,'yyyy-mm-dd')",
		" union ",
		" select t1.user_id, 'ADD' data_type, t1.card_id,t2.cust_name,t2.address,t3.mobile,'' old_card_id,to_char(t1.open_time,'yyyy-mm-dd hh24:mi:ss') change_time,",
		" case when t3.cert_type='SFZ' then t3.cert_num else '' end id_num,", 
		" t1.area_id,t1.county_id from c_user t1,c_cust t2,c_cust_linkman t3",
		" where t1.cust_id=t2.cust_id and t1.cust_id=t3.cust_id and t1.card_id is not null and t1.county_id=:countyId" ,
		" and t1.open_time>to_date(:beginDate,'yyyy-mm-dd') and t1.open_time<to_date(:endDate,'yyyy-mm-dd')",
		") a order by a.change_time) where rownum < 10000");
		
		Map<String,Serializable> params = new HashMap<String, Serializable>();
		params.put("countyId", countyId);
		params.put("beginDate", beginDate);
		params.put("endDate", endDate);
		
		return createNameQuery(ChangedUser.class, sql, params).list();
	}

	/**
	 * 查询开始日期到今天之前的新增用户
	 * @param beginDate
	 * @param countyId
	 * @return
	 * @throws JDBCException 
	 * @throws JDBCException 
	 */
	public List<ChangedUser> queryAddedUsers(String beginDate,String endDate, String countyId) throws JDBCException {
		String sql = StringHelper.append("select t1.user_id, 'ADD' data_type, t1.card_id,t2.cust_name,t2.address,t3.mobile,to_char(t1.open_time,'yyyy-mm-dd hh24:mi:ss') change_time,",
				" case when t3.cert_type='SFZ' then t3.cert_num else '' end id_num,", 
				" t1.area_id,t1.county_id from c_user t1,c_cust t2,c_cust_linkman t3",
				" where t1.cust_id=t2.cust_id and t1.cust_id=t3.cust_id and t1.card_id is not null and t1.county_id=?" ,
				" and t1.open_time>to_date(?,'yyyy-mm-dd') and t1.open_time<to_date(?,'yyyy-mm-dd') order by t1.open_time");
		return createQuery(ChangedUser.class, sql, countyId,beginDate,endDate).list();
	}

	/**
	 * 查询用户消费清单,包括VOD点播记录和电视营业厅付费记录
	 * @param deviceId
	 * @param returnTvRecordCount
	 * @param returnVodRecordCount
	 * @param countyId 
	 * @return
	 * @throws JDBCException 
	 */
	public List<UserBillDto> queryUserBill(String deviceId,
			Integer returnTvRecordCount, Integer returnVodRecordCount, String countyId) throws JDBCException {
		//最多返回各1万条
		if(returnTvRecordCount == -1){
			returnTvRecordCount = 10000;
		}
		if(returnVodRecordCount == -1){
			returnVodRecordCount = 10000;
		}
		
		String sql = StringHelper.append(" select * from ( select * from (select c.prog_name prod_name , 'VOD' prod_type ,c.fee,to_char(fee_time,'yyyy-mm-dd hh24:mi:ss') fee_time",
				" from c_acct_pre_fee c, c_user u where  c.user_id = u.user_id and c.is_valid = 'T' and c.process_flag = 2 ",
          		" and c.status = 'T' AND c.ticket_sn is not null and u.card_id =? and u.county_id=? order by c.done_code desc) a  where rownum <= ?",
          		" union ",
          		" select * from ",
          		" (select t3.prod_name,'TV' prod_type,t.fee ,to_char(t.order_date,'yyyy-mm-dd hh24:mi:ss') fee_time from tv_order t,c_user t2,p_prod t3",
          		"  where t.user_id=t2.user_id and t.prod_id=t3.prod_id and t2.card_id=? and t2.county_id=?  order by t.order_date desc)",
          		" where rownum <= ? ) order by fee_time desc");
		
		return createQuery(UserBillDto.class, sql, deviceId,countyId,returnVodRecordCount,
					deviceId,countyId,returnTvRecordCount).list();
	}
	
	public List<CUser> queryUserByUserIds(String[] userIds) throws JDBCException {
		String sql = "SELECT * FROM c_user where "+getSqlGenerator().setWhereInArray("user_id",userIds)+"";
		return createQuery(sql).list();
	}

	public void batchLogoffUser(Integer doneCode,String remark,List<String> userIds,String isReclaimDevice,String deviceStatus, SOptr optr) throws Exception {
		for(String userId :userIds){
			if (StringHelper.isNotEmpty(userId))
			this.getJdbcTemplate().execute("call proc_del_user('"+userId+"', '"+isReclaimDevice+"', '"+deviceStatus+"','"+doneCode+"'," +
					"'"+optr.getOptr_id()+"',"+optr.getCounty_id()+",'"+optr.getArea_id()+"','"+optr.getDept_id()+"','"+remark+"')");
		}
		
	}

	public List<CUser> queryUserByCustIds(String[] custIds) throws JDBCException {
		String sql = "SELECT * FROM c_user where "+getSqlGenerator().setWhereInArray("cust_id",custIds)+"";
		return createQuery(sql).list();
	}
	
	public Pager<UserDto> queryUserInfoToCallCenter(Pager<Map<String ,Object>> p, String countyId) throws Exception{
		String cond = "", brandCond = "";
		boolean flag = false;
		Iterator<String> it = p.getParams().keySet().iterator();
		while(it.hasNext()){
			String key = String.valueOf(it.next());
			String value  = p.getParams().get(key).toString();
			if("USER_ID".equals(key)){
				cond += " and u.user_id='"+value+"'";
			}
			if("CUST_NO".equals(key)){
				cond += " and u.cust_id = (select cust_id from c_cust where cust_no='"+value+"' and county_id='"+countyId+"')";
			}
			if("CUST_ID".equals(key)){
				cond += " and u.cust_id='"+value+"'";
			}
			if("CARD_ID".equals(key)){
				cond += " and u.card_id='"+value+"'";
			}
			if("STB_ID".equals(key)){
				cond += " and u.stb_id='"+value+"'";
			}
			if("MODEM_MAC".equals(key)){
				cond += " and u.modem_mac='"+value+"'";
			}
			if("BROAD_NAME".equals(key)){
				flag = true;
				brandCond = " and b.login_name='" + value + "'";
			}
		}
		String sql = "";
		if(flag){
			sql = "select u.*,'' terminal_type,'' serv_type,b.login_name,b.check_type,b.max_connection"
				+ " from c_user u,c_user_broadband b where u.user_id=b.user_id and u.county_id=?" + cond + brandCond;
		}else{
			sql = "select u.*,a.terminal_type,a.serv_type,'' login_name,'' check_type,0 max_connection"
				+ " from c_user u,c_user_atv a where u.user_id=a.user_id and u.county_id='"+countyId+"'" + cond
				+ " union all "
				+ "select u.*,'' terminal_type,'' serv_type,b.login_name,b.check_type,b.max_connection"
				+ " from c_user u,c_user_broadband b where u.user_id=b.user_id and u.county_id='"+countyId+"'" + cond
				+ " union all "
				+ "select u.*,d.terminal_type,d.serv_type,'' login_name,'' check_type,0 max_connection"
				+ " from c_user u,c_user_dtv d where u.user_id=d.user_id and u.county_id=?" + cond;
		}
		return this.createQuery(UserDto.class, sql, countyId).setStart(
				p.getStart()).setLimit(p.getLimit()).page();
	}
	
	public void updateProdInclude(String oldUserId,String oldCustId,String newUserId,String newCustId) throws Exception {
		String sql = "update c_prod_include set user_id=?,cust_id=? where user_id=? and cust_id=?";
		this.executeUpdate(sql, newUserId, newCustId, oldUserId, oldCustId);
	}
	
	public void updateAcctId(String userId, String acctId) throws Exception {
		String sql = "update c_user set acct_id=? where user_id=?";
		this.executeUpdate(sql, acctId, userId);
	}
	public String queryMinUserId(String custId) throws Exception {
		final String sql = "select min(user_id) user_id from c_user where cust_id =?";
		return this.createQuery(sql, custId).list().get(0).getUser_id();
	}

	public Integer queryUserCount(String custId) throws Exception {
		String sql = "select count(1) from c_user where cust_id =?";
		return Integer.parseInt(findUnique(sql, custId));
	}
	public void callChangeCust(String userId, String toCustId,Integer doneCode ,String busiCode,SOptr optr){
		this.getJdbcTemplate().execute("call proc_user_change_cust('"+userId+"','"+toCustId+"',"+doneCode+",'"+busiCode+"','"+optr.getOptr_id()+"','"+optr.getDept_id()+"')");
	}
	
	/**
	 * 查找客户名下不同用户类型的用户数
	 * 状态为：正常或者install的用户
	 */
	
	public Map<String,Integer> queryUserCountGroupByType(String custId) throws Exception {
		Map<String,Integer> userCountMap = new HashMap<>();
		final String sql = "select user_type,count(1) count from c_user "
				+ "where cust_id =? and status in ('ACTIVE','INSTALL') "
				+ "group by user_type";
		List<Object[]> userTypeList= this.createSQLQuery(sql, custId).list();
		if (CollectionHelper.isNotEmpty(userTypeList)){
			for (Object[] obj:userTypeList){
				userCountMap.put(obj[0].toString(), Integer.parseInt(obj[1].toString()));
			}
		}
		return userCountMap;
	}
	
	/**
	 * 查找一个用户下所有有效资源的到期日(产品状态是开通状态的)
	 */
	public List<UserResExpDate> queryUserProdResExpDate(String userId) throws Exception{
		String sql =
				StringHelper.append("select c.external_res_id res_id,exp_date ",
						" from c_prod_order a,p_prod_static_res b,t_server_res c ,t_prod_status_openstop op ",
				        " where a.status=op.status_id and  op.open_or_stop=1 ",
				        " and  b.res_id= c.boss_res_id and a.prod_id=b.prod_id ",
				        " and a.user_id=? and a.exp_date>=trunc(sysdate) ");
		return this.createQuery(UserResExpDate.class, sql, userId).list();
	}
	
	public boolean validAccount(String name) throws Exception {
		String sql = "select count(1) from c_user t where t.login_name=?";
		return this.count(sql, name) > 0;
	}
	
	public List<String> queryLoginNameByUserType(String custId, String userType) throws Exception {
		String sql = "select t.login_name from c_user t where t.cust_id=? and t.user_type=? and t.login_name is not null"
				+ " union all"
				+ " select t.login_name from c_user_his t where t.cust_id=? and t.user_type=? and t.login_name is not null";
		if(!userType.equals(SystemConstants.USER_TYPE_BAND)){
			sql = "select t.login_name from c_user t where t.cust_id=? and t.user_type in (?,?) and t.login_name is not null"
					+ " union all"
					+ " select t.login_name from c_user_his t where t.cust_id=? and t.user_type in (?,?) and t.login_name is not null";
			return this.findUniques(sql, custId, SystemConstants.USER_TYPE_OTT, SystemConstants.USER_TYPE_OTT_MOBILE, 
					custId, SystemConstants.USER_TYPE_OTT, SystemConstants.USER_TYPE_OTT_MOBILE);
		}else{
			return this.findUniques(sql, custId, userType, custId, userType);
		}
		
	}
	
	/**
	 * 通过账户查用户
	 * @param loginName
	 * @return
	 * @throws JDBCException 
	 */
	public CUser queryUserByLoginName(String loginName) throws JDBCException{
		String sql="select * from c_user where login_name=? ";
		return this.createQuery(sql, loginName).first();
	}
	
	public int countLikeLoginName(String loginNamePrefix) throws Exception {
		String sql = "select count(1) from c_user where login_name like ?";
		return this.count(sql, loginNamePrefix+"%");
	}

	public int updateUserNameByDeviceCode(CUser user,String custId) throws JDBCException {
		String sql = "update c_user set user_name=? where cust_id=? and ( stb_id=? or card_id=? or modem_mac=? )";
		String deviceCode = user.getStb_id();
		int executeUpdate = executeUpdate(sql, user.getUser_name(),custId,deviceCode,deviceCode,deviceCode);
		return executeUpdate;
	}
	
	public List<CUser> queryTaskUser(String taskId) throws JDBCException {
		String sql = "select * from c_user a,w_task_user b where a.user_id = b.user_id and b.task_id =?";
		return this.createQuery(sql, taskId).list();
	}
	
	public CUser queryUserByDeviceCode(String DeviceCode) throws JDBCException{
		String sql="select * from c_user where  stb_id=? or card_id=? or modem_mac=? ";
		return this.createQuery(sql, DeviceCode,DeviceCode,DeviceCode).first();
	}


}
