/**
 * CDoneCodeDao.java	2010/03/16
 */

package com.ycsoft.business.dao.core.common;

import static com.ycsoft.commons.helper.StringHelper.append;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.common.CDoneCode;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.business.dto.core.cust.DoneCodeDto;
import com.ycsoft.business.dto.core.cust.DoneInfoDto;
import com.ycsoft.business.dto.core.fee.QueryFeeInfo;
import com.ycsoft.business.dto.print.BusiDocPrintItemDto;
import com.ycsoft.commons.constants.BusiCodeConstants;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;


/**
 * CDoneCodeDao -> C_DONE_CODE table's operator
 */
@Component
public class CDoneCodeDao extends BaseEntityDao<CDoneCode> {

	private static final long serialVersionUID = 142913821221838946L;

	/**
	 * default empty constructor
	 */
	public CDoneCodeDao() {}
	
	public void saveCancel(int doneCode,String busiCode) throws Exception{
		String sql ="insert into c_done_code_cancel (done_code,cancel_date,flag,busi_code) " +
				" values (?,sysdate,?,?)";
		this.executeUpdate(sql, doneCode,SystemConstants.BOOLEAN_FALSE,busiCode);
	}
	
	public void updateCancelFlag(int doneCode) throws Exception{
		String sql ="update c_done_code set flag=? where done_code=?";
		this.executeUpdate(sql,SystemConstants.BOOLEAN_TRUE, doneCode);
	}
	
	public void updateRemark(int doneCode,String remark) throws Exception{
		String sql ="update c_done_code set remark=? where done_code=?";
		this.executeUpdate(sql,remark, doneCode);
	}

	/**
	 * 根据用户ID，获取用户受理记录
	 * @param custId
	 * @param countyId
	 */
	public List<CDoneCode> queryUserDoneCode(String userId,String countyId)throws Exception{
		List<CDoneCode> doneCodeList = null;
		String sql = "select distinct a.done_code,a.busi_code,done_date,a.optr_id,a.remark " +
				" from c_done_code a, c_done_code_detail b" +
				" where a.done_code=b.done_code " +
				" and b.user_id= ? and a.county_id=? ";
		doneCodeList =  this.createQuery( sql,userId,countyId).list();
		return doneCodeList;
	}

	/**
	 * 查询在当前donecode之前可以回退的流水
	 * @param custId
	 * @param countyId
	 */
	public List<CDoneCode> queryAfterDoneCode(Integer doneCode,String custId,String countyId)throws Exception{
		List<CDoneCode> doneCodeList = null;
		String sql = " select distinct a.done_code,a.busi_code,done_date,a.optr_id,a.remark" +
				" from c_done_code a ,c_done_code_detail b,t_busi_code c " +
				" where a.done_code=b.done_code and a.busi_code = c.busi_code " +
				" and b.cust_id=? and status=? and a.county_id=? " +
				" and c.ignore=? and busi_type='1' " +
				" and done_date > (select done_date from c_done_code where done_code=?)";
		doneCodeList =  this.createQuery(sql,custId,StatusConstants.ACTIVE,countyId,SystemConstants.BOOLEAN_FALSE,doneCode).list();
		return doneCodeList;
	}
	/**
	 * 根据客户ID，获取客户受理记录
	 * @param custId
	 * @param countyId
	 */
	public Pager<DoneCodeDto> queryCustDoneCode(String custId, QueryFeeInfo queryFeeInfo,String countyId,Integer start,Integer limit)throws Exception{
		
		String sql = append("select distinct a.done_code,b.user_id, ",
				" a.remark,a.busi_code,a.done_date,a.optr_id,a.status,t3.cancel,t3.ignore,t3.busi_fee,a.dept_id,(select sum(real_pay) from c_fee f where f.create_done_code =a.done_code) real_pay ",
				" from c_done_code a, c_done_code_detail b,t_busi_code t3  ",
				" where t3.busi_type(+)='1' and a.done_code = b.done_code",
				" and a.busi_code = t3.busi_code(+) ",
				" and b.cust_id = ? ",
				" and a.county_id = ?");
		
		if(queryFeeInfo != null){
			if(StringHelper.isNotEmpty(queryFeeInfo.getStatus())){
				sql += " and a.status='" + queryFeeInfo.getStatus() + "'";
			}
			if(StringHelper.isNotEmpty(queryFeeInfo.getBusi_name())){
				sql += " and t3.busi_name like '%" + queryFeeInfo.getBusi_name() + "%'";
			}
			if(StringHelper.isNotEmpty(queryFeeInfo.getOptr_name())){
				sql += " and a.optr_id in (select optr_id from s_optr where county_id='"+countyId+"' and optr_name like '%"+queryFeeInfo.getOptr_name()+"%')";
			}
		}
		sql += " order by a.done_code desc";
		return this.createQuery(DoneCodeDto.class, sql, custId, countyId)
				.setStart(start).setLimit(limit).page();
	}

	/**
	 * 根据done_code查询作废信息
	 * @param doneCode
	 * @param custId
	 * @return
	 * @throws Exception
	 */
	public List<DoneCodeDto> queryCfeeByDoneCode(String[] doneCode,String custId)throws Exception{
		if (doneCode==null || doneCode.length==0)
			return new ArrayList<DoneCodeDto>();
		String sql = "select t.create_done_code done_code ,t.reverse_done_code,t.real_pay,t.fee_id from c_fee t where  t.cust_id =? " +
				" and ("+getSqlGenerator().setWhereInArray("t.create_done_code",doneCode)+") ";
		return createQuery(DoneCodeDto.class, sql,custId).list();
	}
	
	/**
	 * @param doneCode
	 */
	public void delete(Integer doneCode) throws Exception{
		String sql ="update c_done_code set status =? where done_code=?";
		executeUpdate(sql, StatusConstants.INVALID,doneCode);
	}

	/**
	 * 获取用户在几个月内临时授权的次数
	 * @param userId
	 * @param months
	 * @param county_id
	 * @return
	 */
	public int queryOpenTempTimes(String userId, int months,
			String countyId) throws Exception{
		String sql = "select count(1) " +
		" from c_done_code a, c_done_code_detail b" +
		" where a.done_code=b.done_code " +
		" and a.status=? and a.county_id=? " +
		" and a.busi_code=? and b.user_id= ? " +
		" and a.done_date>= add_months(sysdate,?) ";
		return Integer.parseInt(findUnique(sql, StatusConstants.ACTIVE,countyId,BusiCodeConstants.USER_OPEN_TEMP,userId,months*-1).toString());
	}
	
	/**
	 *  查询数据库时间 
	 * @throws Exception 
	 * @throws JDBCException 
	 */
	public String queryDataBaseTime() throws JDBCException, Exception{
		String sql="select to_char(sysdate,'yyyy-mm-dd hh24:mi:ss') from dual";
		String dateStr = this.findUnique(sql);
		return dateStr;
	}
	
	public Pager<DoneInfoDto> getUserOpenDate(Integer cDoneCode, Integer start, Integer limit) throws Exception {
		String sql = "select T.STB_ID,T.CARD_ID,T.MODEM_MAC,t.user_type from C_USER T,c_done_code T1,c_done_code_detail T2 " +
				" WHERE T1.DONE_CODE =? AND T1.DONE_CODE=T2.DONE_CODE AND T2.USER_ID=T.USER_ID";
		return this.createQuery(DoneInfoDto.class, sql, cDoneCode)
				.setStart(start).setLimit(limit).page();
	}
	
	public Pager<DoneInfoDto> getOrderProdDate(Integer doneCode, String custId, Integer start, Integer limit) throws Exception {
		/*String sql = "select pp.prod_name,ppt.tariff_name,cp.invalid_date,cu.stb_id,cu.card_id,cu.user_type ,cp.status" +
				" from c_prod cp,p_prod pp,c_user cu,p_prod_tariff ppt " +
				" where cp.done_code=? and cp.county_id=? and cu.county_id=?" +
				" and ppt.tariff_id(+)=cp.tariff_id and cp.prod_id=pp.prod_id(+) and cp.user_id = cu.user_id" +
				" union select pp.prod_name,ppt.tariff_name,cp.invalid_date,cu.stb_id,cu.card_id,cu.user_type ,cp.status " +
				" from c_prod_his cp,p_prod pp,c_user cu,p_prod_tariff ppt  where cp.order_done_code=? " +
				"  and cp.county_id=? and cu.county_id=?"+
				" and ppt.tariff_id(+)=cp.tariff_id and cp.prod_id=pp.prod_id(+) and cp.user_id = cu.user_id ";
		
		return this.createQuery(DoneInfoDto.class, sql, cDoneCode,countyId,countyId,cDoneCode,countyId,countyId)
				.setStart(start).setLimit(limit).page();*/
		String sql = "select '订购Order' remark,t.order_sn,t.done_code,t.prod_id,t.order_time,t.order_fee,t.active_fee,t.order_months,t.eff_date,t.exp_date,"
				+ " p.prod_name,pf.tariff_name"
				+ " from c_prod_order t,p_prod p,p_prod_tariff  pf"
				+ " where t.package_sn is null and t.prod_id=p.prod_id and t.tariff_id=pf.tariff_id "
				+ " and t.done_code=? and t.cust_id=?"
				+ " union all"
				+ " select '订购Order' remark,t.order_sn,t.done_code,t.prod_id,t.order_time,t.order_fee,t.active_fee,t.order_months,t.eff_date,t.exp_date,"
				+ " p.prod_name,pf.tariff_name"
				+ " from c_prod_order_his t ,p_prod p,p_prod_tariff  pf"
				+ " where t.package_sn is null and t.prod_id=p.prod_id and t.tariff_id=pf.tariff_id "
				+ " and t.done_code=? and t.cust_id=?"
				+ " union all"
				+ " select '退订Unsubscribe' remark,t.order_sn,t.delete_done_code done_code,t.prod_id,t.order_time,t.order_fee,t.active_fee,t.order_months,t.eff_date,t.exp_date,"
				+ " p.prod_name,pf.tariff_name"
				+ " from c_prod_order_his t ,p_prod p,p_prod_tariff  pf"
				+ " where t.package_sn is null and t.prod_id=p.prod_id and t.tariff_id=pf.tariff_id  "
				+ " and t.delete_done_code=? and t.cust_id=?"
				+ " order by remark";
		return this.createQuery(DoneInfoDto.class, sql, doneCode, custId, doneCode, custId, doneCode, custId).setStart(start).setLimit(limit).page();
	}
	
	public Pager<DoneInfoDto> getDeviceBuyDate(Integer cDoneCode, String countyId, Integer start, Integer limit) throws Exception {
		String sql = "select t.device_type,t.device_code,t.pair_card_code,t.pair_modem_code,t.status,t.buy_mode " +
				" from c_cust_device T,c_done_code_detail cd WHERE cd.done_code=t.done_code and cd.DONE_CODE =?  and t.cust_id=cd.cust_id and t.county_id =?  and cd.county_id = ? " +
				" union select t.device_type,t.device_code,t.pair_card_code,t.pair_modem_code,t.status,t.buy_mode " +
				" from c_cust_device_his T,c_done_code_detail cd WHERE cd.done_code=t.buy_DONE_CODE and cd.DONE_CODE =?  and t.cust_id=cd.cust_id and t.county_id =? and cd.county_id = ?";
		return this.createQuery(DoneInfoDto.class, sql, cDoneCode,countyId,countyId,cDoneCode,countyId,countyId)
				.setStart(start).setLimit(limit).page();
	}
	
	public Pager<DoneInfoDto> getDeviceChangeDate(Integer cDoneCode, String countyId, Integer start, Integer limit) throws Exception {
		String sql = "select cu.column_name,cu.old_value,cu.new_value, tt.comments column_name_text,c.user_type  " +
				" from c_user_prop_change cu, t_tab_define tt,c_user c where cu.done_code =? and cu.county_id=? " +
				" and c.county_id=? and c.user_id = cu.user_id " +
				" and cu.column_name=tt.column_name and tt.status =? and tt.table_name in ('CUSER','CUSERBROADBAND','CUSERATV','CUSERDTV')";
		return this.createQuery(DoneInfoDto.class, sql, cDoneCode,countyId,countyId,StatusConstants.ACTIVE)
				.setStart(start).setLimit(limit).page();
	}
	
	public Pager<DoneInfoDto> getPromotionDate(Integer cDoneCode, String countyId, Integer start, Integer limit) throws Exception {
		String sql = "select c.status,p.promotion_name from c_promotion c,p_promotion p,c_done_code_detail t " +
				" where c.promotion_id=p.promotion_id and c.user_id=t.user_id  and t.done_code=c.done_code and t.done_code = ? and c.county_id= ? and t.county_id = ? " +
				" union select c.status,p.promotion_name from c_promotion_his c,p_promotion p,c_done_code_detail t " +
				" where c.promotion_id=p.promotion_id and c.user_id=t.user_id  and t.done_code=c.done_code and t.done_code = ? and c.county_id= ? and t.county_id = ? ";
		return this.createQuery(DoneInfoDto.class, sql, cDoneCode,countyId,countyId,cDoneCode,countyId,countyId)
				.setStart(start).setLimit(limit).page();
	}
	
	public Pager<DoneInfoDto> getPromFeeDate(Integer cDoneCode, String countyId, Integer start, Integer limit) throws Exception {
		String sql = "select  cpfp.user_id,cpfp.months,cpfp.real_pay,cpfp.should_pay,cpfp.bind_invalid_date,pp.prod_name ,sum(cr.refund_pay) refund_pay,pp.remark " +
				" from c_prom_fee cpf,c_prom_fee_prod cpfp,p_prod pp,c_prom_prod_Refund cr,p_prom_fee pp " +
				" where pp.prod_id = cpfp.prod_id and cpf.county_id = ? and cpf.done_code =?  and cpf.prom_fee_sn = cpfp.prom_fee_sn  " +
				" and cpfp.prom_fee_sn = cr.prom_fee_sn(+) and cpfp.prod_sn = cr.prod_sn(+) and cpf.prom_fee_id=pp.prom_fee_id " +
				" group by cpfp.user_id,cpfp.months,cpfp.real_pay,cpfp.should_pay,cpfp.bind_invalid_date,pp.prod_name,pp.remark " +
				" order by cpfp.user_id";
		return this.createQuery(DoneInfoDto.class, sql, countyId,cDoneCode)
				.setStart(start).setLimit(limit).page();
	}
	
	public Pager<DoneInfoDto> getBandUpgradeDate(Integer doneCode, String countyId, Integer start, Integer limit) throws Exception {
		String sql = "select t.*,p1.prod_name old_prod_name,p2.prod_name prod_name,pt1.tariff_name old_tariff_name,pt2.tariff_name tariff_name" +
			" from C_BAND_UPGRADE_RECORD t,p_prod p1,p_prod p2,p_prod_tariff pt1,p_prod_tariff pt2" +
			" where t.old_prod_id=p1.prod_id and t.new_prod_id=p2.prod_id" +
			" and t.old_tariff_id=pt1.tariff_id and t.new_tariff_id=pt2.tariff_id" +
			" and t.done_code=? and t.county_id=?";
		return this.createQuery(DoneInfoDto.class, sql, doneCode, countyId).setStart(start).setLimit(limit).page();
	}
	
	public List<DoneCodeDto> queryOnelineUserBusi(String optrId)throws Exception{
		String sql = "select optr_id , busi_code, count(1) done_num, sum(decode(status,'ACTIVE',1,0)) active_num ," +
				"sum(decode(status,'INVALID',1,0)) INVALID_num , max(done_date) last_done_date from busi.c_done_code  " +
				"where  optr_id=? and done_date >= trunc(sysdate) and done_date < trunc(sysdate) + 1 " +
				"group by busi_code,optr_id order by last_done_date desc";
		return this.createQuery(DoneCodeDto.class, sql,optrId).list();
	}
	
	/**
	 * 查询客户未打印的业务单据按业务分类
	 * @param countyId
	 * @param custId
	 */
	public List<BusiDocPrintItemDto> queryDocPrintBusiCodeByCust(SOptr optr ,String custId,String docSn)throws Exception{
		  String sql = " SELECT distinct t.busi_code,T.doneCode done_code ,si.BUSI_CODE_SPAN,si.GROUP_COLUMN , si.condition, "
						 +" (case when si.GROUP_COLUMN='CUST' then '' when si.GROUP_COLUMN is null then '' else t.user_id end )user_id"
						 +" FROM (SELECT T2.INFO, T.BUSI_CODE, t2.done_code doneCode,t2.last_print, t.done_date,cdcd.user_id  "
						 +" FROM C_DONE_CODE T,  C_DONE_CODE_INFO T2 ,busi.c_done_code_detail cdcd "
						 +" WHERE T.COUNTY_ID =? "
						 +" AND T2.COUNTY_ID =? "
						 +" AND T2.CUST_ID =? "
						 + ( StringHelper.isEmpty(docSn)? " and t.status = '" + StatusConstants.ACTIVE + "' " : "" )
						 +" and t.done_code =cdcd.done_code and t2.done_code=cdcd.done_code "
//						 +" AND T.DONE_CODE = T2.DONE_CODE and t.optr_id = ? ) T, "
						 +" AND T.DONE_CODE = T2.DONE_CODE ) T, "//不限制操作员
						 +" (SELECT T2.TEMPLATE_FILENAME, T2.BUSI_CODE, t2.height, t2.width "
						 +"  FROM T_TEMPLATE_COUNTY T, T_BUSI_DOC_TEMPLATEFILE_DETAIL T2 "
						 +"  WHERE T.TEMPLATE_TYPE = 'DOC' "
						 +" AND T.COUNTY_ID =? "
						 +"  AND T.TEMPLATE_ID = T2.TEMLATE_ID "
						 +" AND T2.DOC_TYPE = 'DOC') T1,busi.t_busi_doc_templatefile_span si "
						 +"  WHERE T.BUSI_CODE = T1.BUSI_CODE  and  si.busi_code = t.busi_code   ";
						//重打
						 if(!StringHelper.isEmpty(docSn)){
							 sql +=" and exists ( ";
							 sql+="	 select 1 from  busi.c_doc_item cdi where cdi.doc_sn = '"+docSn+"' and cdi.docitem_sn=t.doneCode ) ";
						 }else{
						//第一次打印 ，获取未打印信息
							 sql +=" and t.last_print is null and T.DONE_DATE BETWEEN TRUNC(SYSDATE) AND TRUNC(SYSDATE+1)";
						 }
						 sql+=" group by t.busi_code, T.doneCode, si.busi_code_span, si.GROUP_COLUMN,t.user_id,si.condition";
						 sql+=" ORDER BY doneCode";
		String county_id = optr.getCounty_id();
//		return createQuery(BusiDocPrintItemDto.class, sql, county_id,county_id,custId,optr.getOptr_id(),county_id).list();
		return createQuery(BusiDocPrintItemDto.class, sql, county_id,county_id,custId,county_id).list();
	}	
	
	public String queryProtocolInfo(String done_code)throws Exception{
		final String sql = "SELECT t1.protocol_desc "
						 +" FROM EXT_C_DONE_CODE T, P_PROTOCOL T1 "
						 +" WHERE T.DONE_CODE = ? "
						 +"  AND T.ATTRIBUTE_ID = ? "
						 +"  AND T.ATTRIBUTE_VALUE = T1.PROTOCOL_ID";
		return findUnique(sql, done_code, SystemConstants.EXT_ATTRID_PROTOCOL);
	}
	
	
	/**
	 * 查询客户未打印的业务单据 增加排序
	 * @param countyId
	 * @param custId
	 */
	public List<BusiDocPrintItemDto> queryDocPrintContentByCust(String busiCode,SOptr optr ,String custId,String userId,String condition,String docSn)throws Exception{
		String condSql = "";
		//重打
		if(!StringHelper.isEmpty(docSn)){
			condSql =" and exists (select 1 from  busi.c_doc_item cdi where cdi.doc_sn = '"+docSn+"' and cdi.docitem_sn=t.done_code ) ";
		}else{
			//第一次打印
			condSql =" and t.last_print is null and T.DONE_DATE BETWEEN TRUNC(SYSDATE) AND TRUNC(SYSDATE+1)";
		}
		String optr_id = optr.getOptr_id();
		String county_id = optr.getCounty_id();
		if(StringHelper.isEmpty(userId)){
			  String sql = "SELECT t.info,t.INFO1,t.INFO2,t.INFO3,t.INFO4,t.INFO5,t.INFO6,t.INFO7,t.INFO8,t.INFO9,t.INFO10, t.busi_code, t.done_code, t1.template_filename,t1.height, t1.width,si.busi_code_idx "
							  +" FROM (SELECT T2.INFO,t2.INFO1,t2.INFO2,t2.INFO3,t2.INFO4,t2.INFO5,t2.INFO6,t2.INFO7,t2.INFO8,t2.INFO9,t2.INFO10, T.BUSI_CODE, t2.done_code,t2.last_print, t.done_date "
							  +" FROM C_DONE_CODE T,  C_DONE_CODE_INFO T2"
							  +" WHERE T.COUNTY_ID = ?"
							  +" AND T2.COUNTY_ID = ?"
							  +" AND T2.CUST_ID = ?"
							  +" and t.busi_code=?"
//							  +" and t.optr_id =? "
							  +" AND T.DONE_CODE = T2.DONE_CODE) T,"
							  +" (SELECT T2.TEMPLATE_FILENAME, T2.BUSI_CODE, t2.height, t2.width"
							  +" FROM T_TEMPLATE_COUNTY T, T_BUSI_DOC_TEMPLATEFILE_DETAIL T2"
							  +" WHERE T.TEMPLATE_TYPE = 'DOC'"
							  +" AND T.COUNTY_ID = ?"
							  +" AND T.TEMPLATE_ID = T2.TEMLATE_ID"
							  +" AND T2.DOC_TYPE = 'DOC') T1,busi.t_busi_doc_templatefile_span si "
							  +" WHERE T.BUSI_CODE = T1.BUSI_CODE  and si.busi_code = t.busi_code  " ;
							  if(!StringHelper.isEmpty(condition)){
									sql+=" and "+condition+" ";
								}
							  sql += condSql;
							  sql+=" ORDER BY  T.done_date ";
			
//							  return createQuery(BusiDocPrintItemDto.class, sql, county_id,county_id,custId,busiCode,optr_id,county_id).list() ;
							  return createQuery(BusiDocPrintItemDto.class, sql, county_id,county_id,custId,busiCode,county_id).list() ;
		}else{
			 String sql = " SELECT t.info,t.INFO1,t.INFO2,t.INFO3,t.INFO4,t.INFO5,t.INFO6,t.INFO7,t.INFO8,t.INFO9,t.INFO10, t.busi_code, t.done_code, t1.template_filename,t1.height, t1.width,si.busi_code_idx "
				+" FROM (SELECT T2.INFO,t2.INFO1,t2.INFO2,t2.INFO3,t2.INFO4,t2.INFO5,t2.INFO6,t2.INFO7,t2.INFO8,t2.INFO9,t2.INFO10, T.BUSI_CODE, t2.done_code,t2.last_print, t.done_date "
				+" FROM C_DONE_CODE T,  C_DONE_CODE_INFO T2,c_done_code_detail cdcd"
				+" WHERE T.COUNTY_ID = ?"
				+" AND T2.COUNTY_ID = ?"
				+" AND T2.CUST_ID = ?"
				+" and cdcd.user_id=?"
				+" and T2.user_id=?"
				+" and t.busi_code=?"
//				+" and t.optr_id =? "
				+" and t.done_code=cdcd.done_code and  T2.done_code=cdcd.done_code "
				+" AND T.DONE_CODE = T2.DONE_CODE) T,"
				+" (SELECT T2.TEMPLATE_FILENAME, T2.BUSI_CODE, t2.height, t2.width"
				+" FROM T_TEMPLATE_COUNTY T, T_BUSI_DOC_TEMPLATEFILE_DETAIL T2"
				+" WHERE T.TEMPLATE_TYPE = 'DOC'"
				+" AND T.COUNTY_ID = ?"
				+" AND T.TEMPLATE_ID = T2.TEMLATE_ID"
				+" AND T2.DOC_TYPE = 'DOC') T1,busi.t_busi_doc_templatefile_span si "
				+" WHERE T.BUSI_CODE = T1.BUSI_CODE  and si.busi_code = t.busi_code " ;
				if(!StringHelper.isEmpty(condition)){
					sql+=" and "+condition+" ";
				}
				sql += condSql;
				sql+=" ORDER BY  T.done_date ";
				return createQuery(BusiDocPrintItemDto.class, sql, county_id,county_id,custId,userId,userId,busiCode,county_id).list() ;
//				return createQuery(BusiDocPrintItemDto.class, sql, county_id,county_id,custId,userId,userId,busiCode,optr_id,county_id).list() ;
		}
		
	}
	
	public List<CDoneCode> queryCardCaDoneCode(String cardId ,String countyId)throws Exception{
		List<CDoneCode> doneCodeList = null;
		String str = "%CARD:"+cardId;
		String sql = "select * from c_done_code  t where t.busi_code='1722'  and t.remark like '"+str+"' and t.county_id=?  order by done_date asc ";
		doneCodeList =  this.createQuery( sql,countyId).list();
		return doneCodeList;
	}
}
