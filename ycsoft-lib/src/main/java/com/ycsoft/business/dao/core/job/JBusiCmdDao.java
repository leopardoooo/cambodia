/**
 * JBusiCmdDao.java	2010/06/08
 */

package com.ycsoft.business.dao.core.job;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TBusiCmdSupplier;
import com.ycsoft.beans.config.TServer;
import com.ycsoft.beans.config.TServerCounty;
import com.ycsoft.beans.core.common.CDoneCode;
import com.ycsoft.beans.core.job.JBusiCmd;
import com.ycsoft.beans.core.prod.CProd;
import com.ycsoft.beans.device.RCard;
import com.ycsoft.business.dto.core.user.UserRes;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;


/**
 * JBusiCmdDao -> J_BUSI_CMD table's operator
 */
@Component
public class JBusiCmdDao extends BaseEntityDao<JBusiCmd> {

	/**
	 *
	 */
	private static final long serialVersionUID = 756392088914189544L;


	/**
	 * default empty constructor
	 */
	public JBusiCmdDao() {}
	
	public List<TBusiCmdSupplier> queryOsdCmdSupplier(String[] caType,String supplierId) throws Exception {
		String sql = "select * from t_busi_cmd_supplier t where t.cmd_id in("+getSqlGenerator().in(caType)+") and t.supplier_id=?";
		return this.createQuery(TBusiCmdSupplier.class, sql,supplierId).list();
	}
	
	public List<TServer> queryForOsdServer(String countyId) throws Exception {
		String sql = StringHelper.append(
				"select distinct t.* from t_server t,t_server_county c",
				" where t.server_id=c.server_id and t.for_osd=?"
		);
		if(!SystemConstants.COUNTY_ALL.equals(countyId)){
			sql = StringHelper.append(
					sql," and c.county_id='",countyId,"'"
				);
		}
		return this.createQuery(TServer.class, sql ,SystemConstants.BOOLEAN_TRUE).list();
	}
	
	public List<TServerCounty> queryServerCounty(String countyId) throws Exception {
		String sql = "select * from t_server_county t where t.county_id=?";
		return this.createQuery(TServerCounty.class, sql ,countyId).list();
	}


	/**
	 * @param doneCode
	 * @param county_id
	 * @return
	 */
	public List<JBusiCmd> queryNewProdByDoneCode(String doneCode, String countyId) throws Exception{
		String sql = " select a.* from j_busi_cmd a,c_prod  b" +
				" where a.prod_sn = b.prod_sn and a.done_code=b.done_code " +
				" and a.done_code=? and b.done_code=? and a.county_id=? and b.county_id=?";
		return createQuery(sql, doneCode,doneCode,countyId,countyId).list();
	}
	
	public void saveBusiCmdHis(int jobId)throws Exception{
		String sql = StringHelper.append(
				"insert into j_busi_cmd_his ",
				" (job_id, done_code, busi_cmd_type, cust_id, user_id, stb_id, card_id, modem_mac, create_time, area_id, county_id, prod_sn,prod_id,detail_params,priority) ",
				" select job_id, done_code, busi_cmd_type, cust_id, user_id, stb_id, card_id, modem_mac, create_time, area_id, county_id, prod_sn,prod_id ,detail_params,priority" ,
				" from j_busi_cmd where job_id = ?");
		executeUpdate(sql, jobId);

		sql = "delete j_busi_cmd  where job_id =  ?";
		executeUpdate(sql, jobId);
	}
	
	public void saveBusiCmdHis(int minJobId,int maxJobId)throws Exception{
		String sql = StringHelper.append(
				"insert into j_busi_cmd_his ",
				" (job_id, done_code, busi_cmd_type, cust_id, user_id, stb_id, card_id, modem_mac, create_time, area_id, county_id, prod_sn,prod_id,detail_params,priority) ",
				" select job_id, done_code, busi_cmd_type, cust_id, user_id, stb_id, card_id, modem_mac, create_time, area_id, county_id, prod_sn,prod_id ,detail_params,priority" ,
				" from j_busi_cmd where job_id between ? and  ?");
		executeUpdate(sql, minJobId,maxJobId);

		sql = "delete j_busi_cmd  where job_id between ? and  ?";
		executeUpdate(sql, minJobId,maxJobId);
	}

	public List<String> queryDnyRes(String prodSn) throws Exception{
		String sql = "select res_id from c_prod_rsc where prod_sn=?";
		return findUniques(sql, prodSn);
	}
	
	/**
	 * 查询产品资源，包括静态资源动态资源
	 * @param prodSn
	 * @return
	 * @throws Exception
	 */
	public List<String> queryBaseProdRes(String prodSn) throws Exception{
		String sql = StringHelper.append("select distinct * from (select res_id from c_prod_rsc where prod_sn=? ",
				" union ",
				" select p.res_id from p_prod_static_res p,c_prod c where c.prod_id=p.prod_id and  c.prod_sn=? ",
				" union ",
				" select res_id from p_prod_county_res pr,c_prod cp where pr.prod_id=cp.prod_id and pr.county_id=cp.county_id and cp.prod_sn=? )");
		return findUniques(sql, prodSn,prodSn,prodSn);
	}
	
	public List<String> queryCountyRes(String prodId,String countyId)throws Exception{
		String sql = "select res_id from  p_prod_county_res p where p.prod_id=? and p.county_id = ?";
		return findUniques(sql,prodId,countyId);
	}

	/**
	 * 根据卡号获取卡型号
	 */
	public Map<String,RCard> queryCardModel (String[] cardIds) throws Exception{
		String sql = "Select * from r_card where card_id in("+getSqlGenerator().in(cardIds)+")";
		return CollectionHelper.converToMapSingle(this.createQuery(RCard.class,sql).list(),"card_id");
	}


	/**
	 * 查找该地区没有处理的授权指令
	 */
	public List<JBusiCmd> queryBusiCmd(String areaId) throws Exception{
		String sql ="select * from (select j.* from j_busi_cmd j order by priority,job_id) " +
				" where rownum<500 ";
		
		return createQuery(sql).list();
	}

	public List<UserRes> queryValidRes(String[] userIds)throws Exception{
		String idStr= "";
		for (String userId:userIds){
			if (StringHelper.isNotEmpty(userId))
				idStr +="'"+userId+"',";
		}
		
		if (idStr.length()>0){
			idStr = idStr.substring(0,idStr.length()-1);
			
			String sql = StringHelper.append(
					"select A.*,B.RES_NAME from (",
					"select DISTINCT CP.USER_ID, T_RES.RES_ID ",
					"  from c_prod CP, ",
					"       (SELECT PROD_ID, RES_ID ,'ALL' COUNTY_ID",
					"          FROM P_PROD_STATIC_RES ",
					"        UNION ALL ",
					"        SELECT PROD_ID, RES_ID,COUNTY_ID  ",
					"          FROM P_PROD_COUNTY_RES ) T_RES ",
					" where CP.prod_type = 'BASE' ",
					"   AND CP.STATUS IN (SELECT STATUS_ID FROM t_prod_status_openstop WHERE OPEN_OR_STOP=1) ",
					"   AND CP.USER_ID in("+idStr+")",
					"   AND CP.PROD_ID = T_RES.PROD_ID " +
					"   AND (T_RES.COUNTY_ID='ALL' OR T_RES.COUNTY_ID=CP.COUNTY_ID) ",
					"UNION ALL ",
					"select DISTINCT CP.USER_ID, CPR.RES_ID ",
					"  from c_prod CP,  C_PROD_RSC CPR ",
					" where CP.prod_type = 'BASE' ",
					"   AND CP.STATUS IN (SELECT STATUS_ID FROM t_prod_status_openstop WHERE OPEN_OR_STOP=1) ",
					"   AND CP.USER_ID in("+idStr+")",
					"   AND CP.PROD_SN = CPR.PROD_SN) A,P_RES B ",
					"  WHERE A.RES_ID = B.RES_ID ",
					"   ORDER BY USER_ID");
			return createQuery(UserRes.class,sql).list();
		} else {
			return new ArrayList<UserRes>();
		}
	}
	
	/**
	 * 查询用户排斥资源
	 * @param userIds
	 * @return
	 * @throws Exception
	 */
	public List<UserRes> queryRejectRes(String[] userIds)throws Exception{
		String idStr= "";
		for (String userId:userIds){
			if (StringHelper.isNotEmpty(userId))
				idStr +="'"+userId+"',";
		}
		
		if (idStr.length()>0){
			idStr = idStr.substring(0,idStr.length()-1);
			String sql ="select user_id,res_id from c_reject_res where USER_ID in("+idStr+")";
			return createQuery(UserRes.class,sql).list();
		}else {
			return new ArrayList<UserRes>();
		}
	}
	
	public List<CProd> queryValidDoubleProd(String[] userIds)throws Exception{
		String idStr= "";
		for (String userId:userIds){
			if (StringHelper.isNotEmpty(userId))
				idStr +="'"+userId+"',";
		}
		
		if (idStr.length()>0){
			idStr = idStr.substring(0,idStr.length()-1);
			String sql ="select c.user_id,c.prod_id from c_prod c,p_prod p where c.prod_id=p.prod_id and p.serv_id='ITV' " +
					"AND C.STATUS IN (SELECT STATUS_ID FROM t_prod_status_openstop WHERE OPEN_OR_STOP=1) and c.USER_ID in("+idStr+")";
			return createQuery(CProd.class,sql).list();
		}else {
			return new ArrayList<CProd>();
		}
	}
	
	public CDoneCode queryByDonecode(Integer donecode) throws JDBCException {
		String sql = "select * from c_done_code c where c.done_code=?";
		return createQuery(CDoneCode.class, sql, donecode).first();
	}
}
