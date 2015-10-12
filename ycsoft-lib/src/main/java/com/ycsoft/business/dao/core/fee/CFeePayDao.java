/**
 * CFeePayDao.java	2010/04/08
 */

package com.ycsoft.business.dao.core.fee;

import static com.ycsoft.commons.constants.StatusConstants.PAY;
import static com.ycsoft.commons.constants.StatusConstants.UNPAY;
import static com.ycsoft.commons.constants.SystemConstants.BOOLEAN_FALSE;
import static com.ycsoft.commons.helper.StringHelper.append;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.fee.CFeePay;
import com.ycsoft.business.dto.core.fee.FeeDto;
import com.ycsoft.business.dto.core.fee.FeePayDto;
import com.ycsoft.business.dto.core.fee.MergeFeeDto;
import com.ycsoft.business.dto.core.fee.QueryFeeInfo;
import com.ycsoft.commons.constants.BusiCodeConstants;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;

/**
 * CFeePayDao -> C_FEE_PAY table's operator
 */
@Component
public class CFeePayDao extends BaseEntityDao<CFeePay> {

	/**
	 *
	 */
	private static final long serialVersionUID = 6069118318279568989L;


	/**
	 * default empty constructor
	 */
	public CFeePayDao() {
	}

	/**
	 * 查询客户下的费用项
	 */
	public List<FeeDto> queryPayFees(String custId, String countyId,String feeStatus)
			throws Exception {
		String sql = null;
		sql = append(
				" SELECT t.fee_type ,t.fee_sn,decode(t2.fee_name,null,t3.acctitem_name,t2.fee_name) fee_text," +
				" t.real_pay,t.optr_id,t.create_time,t.busi_code,t.optr_id",
				" FROM c_fee t,t_busi_fee t2,vew_acctitem t3 ",
				" WHERE  t.cust_id=? and t.county_id= ? and t.status=? and t.real_pay<>0",
				"  and t.fee_id=t2.fee_id(+) " ,
				"  and t.acctitem_id = t3.acctitem_id(+)");

		return createQuery(FeeDto.class, sql,custId, countyId, feeStatus).list();
	}


	/**
	 * 查询指定客户下未合并的费用项,不包括预收费和押金
	 * @param custId
	 * @param countyId
	 * @return
	 * @throws Exception
	 */
	public List<MergeFeeDto> queryUnMergeFees(String custId, String countyId)
			throws Exception {
		String sql = null;
		sql = append(
					" SELECT t.fee_type,t.fee_sn,decode(t2.fee_name,null,t3.acctitem_name,t2.fee_name) fee_text, t.real_pay,",
					" t.create_time, t4.printitem_name,t4.printitem_id ",
					" FROM c_fee t, t_busi_fee t2,vew_acctitem t3,t_printitem t4 ",
					" WHERE t.cust_id=:custId and t.county_id=:countyId ",
					" and t.status=:status and t.is_doc=:isDoc and t2.fee_type IN ('BUSI','DEVICE') AND t2.deposit='F'" ,
					" and t.fee_id = t2.fee_id(+) and t.acctitem_id=t3.acctitem_id(+) ",
					" and t2.printitem_id = t4.printitem_id ");

		Map<String,Serializable> params = new HashMap<String, Serializable>();
		params.put("custId", custId);
		params.put("countyId", countyId);
		params.put("status", PAY);
		params.put("isDoc", BOOLEAN_FALSE);

 		return createNameQuery(MergeFeeDto.class, sql, params).list();
	}

	/**
	 * 获得指定donecode 的feesn
	 * @param doneCode
	 */
	public String[] queryFeeSn(Integer doneCode ,String countyId )throws Exception{
		String sql = " SELECT t1.fee_sn FROM c_fee t1 " +
				" WHERE t1.create_done_code=? and t1.county_id=?";
		return findUniques(sql, doneCode,countyId).toArray(new String[]{});
	}
	/**
	 * 查询自动合并的费用项，并将结果及封装至Map中。不包括预收费和押金
	 * @throws Exception
	 */
	public List<Map<String,Object>> queryFeeByDoneCode(Integer doneCode,String countyId)throws Exception{
		String sql = append(" SELECT T1.FEE_SN, T1.REAL_PAY AMOUNT, T2.PRINTITEM_ID,t3.doc_type",
				"   FROM C_FEE T1, T_BUSI_FEE T2,t_invoice_printitem t3,t_template_county t4 ,t_pay_type t5",
				"  WHERE T1.FEE_ID = T2.FEE_ID AND t3.printitem_id=t2.printitem_id AND t4.county_id=? " +
				"    AND t4.template_type=?",
				"    AND T1.COUNTY_ID = ?  ",
				"    AND T1.create_done_code = ? ",
				"    AND T1.STATUS = ? AND t1.pay_type=t5.pay_type and t5.is_print='T'",
				" UNION ALL ",
				" SELECT T1.FEE_SN, T1.REAL_PAY AMOUNT, T2.PRINTITEM_ID,t3.doc_type",
				"   FROM C_FEE T1, VEW_ACCTITEM T2,t_invoice_printitem t3,t_template_county t4 ,t_pay_type t5 ",
				"  WHERE t3.printitem_id=t2.printitem_id AND t4.county_id=? " +
				"    AND t4.template_type=? AND T1.COUNTY_ID = ?  ",
				"    AND T1.create_done_code = ?  ",
				"    AND T1.STATUS = ?  AND t1.pay_type=t5.pay_type and t5.is_print='T'",
				"    AND T1.ACCTITEM_ID = T2.ACCTITEM_ID  ");

		return findToList(sql, countyId, 
				SystemConstants.TEMPLATE_TYPE_INVOICE,countyId, doneCode,
				StatusConstants.PAY,countyId,
				SystemConstants.TEMPLATE_TYPE_INVOICE,  countyId,
				doneCode, StatusConstants.PAY);
	}

	/**
	 *  查询自动合并的费用项，并将结果及封装至Map中。不包括预收费和押金
	 * @param feeSn
	 * @param county_id
	 * @return
	 */
	public List<Map<String, Object>> queryFeeByFeeSn(String[] feeSn,String optrId,
			String countyId)throws Exception {
		String sql = append(" SELECT T1.FEE_SN, T1.REAL_PAY AMOUNT, T2.PRINTITEM_ID,t3.doc_type",
				"   FROM C_FEE T1, T_BUSI_FEE T2,t_invoice_printitem t3,t_template_county t4 ,t_pay_type t5",
				"  WHERE T1.FEE_ID = T2.FEE_ID AND t3.printitem_id=t2.printitem_id AND t4.county_id=? " +
				"    AND t4.template_type=?  AND t4.template_id=t3.template_id",
				"    AND T1.COUNTY_ID = ?  ",
				"    AND T1.fee_sn in (" ,getSqlGenerator().in(feeSn), ")  ",
				"    AND T1.STATUS = ? AND t1.pay_type=t5.pay_type and t5.is_print='T'",
				"    AND T2.FEE_TYPE IN (?, ?, ?, ?,?)",
				"    AND T2.DEPOSIT = 'F' AND T1.optr_id=?",
				" UNION ALL ",
				" SELECT T1.FEE_SN, T1.REAL_PAY AMOUNT, T2.PRINTITEM_ID,t3.doc_type",
				"   FROM C_FEE T1, VEW_ACCTITEM T2,t_invoice_printitem t3,t_template_county t4 ,t_pay_type t5 ",
				"  WHERE t3.printitem_id=t2.printitem_id AND t4.county_id=? " +
				"     AND t4.template_id=t3.template_id AND t4.template_type=? AND T1.COUNTY_ID = ?  ",
				"    AND T1.fee_sn in (" ,getSqlGenerator().in(feeSn), ")  ",
				"    AND T1.STATUS = ?  AND t1.pay_type=t5.pay_type and t5.is_print='T'",
				"    AND T1.ACCTITEM_ID = T2.ACCTITEM_ID  ",
				"    AND T1.FEE_TYPE IN (?)  AND T1.optr_id=?");

		return findToList(sql, countyId, 
				SystemConstants.TEMPLATE_TYPE_INVOICE, countyId,
				StatusConstants.PAY, SystemConstants.FEE_TYPE_BUSI,
				SystemConstants.FEE_TYPE_DEVICE,SystemConstants.FEE_TYPE_UNITPRE,
				SystemConstants.FEE_TYPE_UNBUSI,SystemConstants.FEE_TYPE_VALUABLE,optrId,countyId,
				SystemConstants.TEMPLATE_TYPE_INVOICE, countyId,
				 StatusConstants.PAY, SystemConstants.FEE_TYPE_ACCT,optrId);
	}
	
	public List<Map<String, Object>> queryFeeByFeeSn(String[] feeSn,String countyId)throws Exception {
		String sql = append(" SELECT T1.FEE_SN, T1.REAL_PAY AMOUNT, T2.PRINTITEM_ID,t3.doc_type",
				"   FROM C_FEE T1, T_BUSI_FEE T2,t_invoice_printitem t3,t_template_county t4 ,t_pay_type t5",
				"  WHERE T1.FEE_ID = T2.FEE_ID AND t3.printitem_id=t2.printitem_id AND t4.county_id=? " +
				"    AND t4.template_type=?  AND t4.template_id=t3.template_id",
				"    AND T1.COUNTY_ID = ?  ",
				"    AND T1.fee_sn in (" ,getSqlGenerator().in(feeSn), ")  ",
				"    AND T1.STATUS = ? AND t1.pay_type=t5.pay_type and t5.is_print='T'",
				"    AND T2.FEE_TYPE IN (?, ?, ?, ?,?)",
				"    AND T2.DEPOSIT = 'F' ",
				" UNION ALL ",
				" SELECT T1.FEE_SN, T1.REAL_PAY AMOUNT, T2.PRINTITEM_ID,t3.doc_type",
				"   FROM C_FEE T1, VEW_ACCTITEM T2,t_invoice_printitem t3,t_template_county t4 ,t_pay_type t5 ",
				"  WHERE t3.printitem_id=t2.printitem_id AND t4.county_id=? " +
				"     AND t4.template_id=t3.template_id AND t4.template_type=? AND T1.COUNTY_ID = ?  ",
				"    AND T1.fee_sn in (" ,getSqlGenerator().in(feeSn), ")  ",
				"    AND T1.STATUS = ?  AND t1.pay_type=t5.pay_type and t5.is_print='T'",
				"    AND T1.ACCTITEM_ID = T2.ACCTITEM_ID  ",
				"    AND T1.FEE_TYPE IN (?) ");

		return findToList(sql, countyId, 
				SystemConstants.TEMPLATE_TYPE_INVOICE, countyId,
				StatusConstants.PAY, SystemConstants.FEE_TYPE_BUSI,
				SystemConstants.FEE_TYPE_DEVICE,SystemConstants.FEE_TYPE_UNITPRE,
				SystemConstants.FEE_TYPE_UNBUSI,SystemConstants.FEE_TYPE_VALUABLE,countyId,
				SystemConstants.TEMPLATE_TYPE_INVOICE, countyId,
				 StatusConstants.PAY, SystemConstants.FEE_TYPE_ACCT);
	}
	
	public List<Map<String, Object>> queryFeeByFeeSn(String custId,String optrId,
			String countyId)throws Exception {
		String sql = append(" SELECT T1.FEE_SN, T1.REAL_PAY AMOUNT, T2.PRINTITEM_ID,t3.doc_type,t1.acct_id,'' prom_fee_sn",
				"   FROM C_FEE T1, T_BUSI_FEE T2,t_invoice_printitem t3,t_template_county t4,t_pay_type t5,c_fee_pay fp ",
				"  WHERE T1.FEE_ID = T2.FEE_ID AND t3.printitem_id=t2.printitem_id  AND t4.template_id=t3.template_id AND t4.county_id= ?" +
				"    AND t4.template_type= ?",
				"    AND T1.COUNTY_ID =  ?  ",
				"    AND T1.cust_id=? ",
				"    AND T1.STATUS = ? AND t1.pay_type=t5.pay_type and t5.is_print='T'",
				"    AND T1.is_doc= ? ",
				"    AND fp.pay_sn=t1.pay_sn and  fp.optr_id= ? ",
				" UNION ALL ",
				" SELECT T1.FEE_SN, T1.REAL_PAY AMOUNT, T2.PRINTITEM_ID,t3.doc_type,t1.acct_id,'' prom_fee_sn",
				"   FROM C_FEE T1, VEW_ACCTITEM T2,t_invoice_printitem t3,t_template_county t4,t_pay_type t5 ,c_fee_pay fp ",
				"  WHERE t3.printitem_id=t2.printitem_id AND t4.county_id= ? " +
				"     AND t4.template_id=t3.template_id AND t4.template_type= ? AND T1.COUNTY_ID = ?  ",
				"    AND T1.cust_id= ?  ",
				"    AND T1.STATUS = ? AND t1.pay_type=t5.pay_type and t5.is_print='T' ",
				"    AND T1.is_doc= ? ",
				"    AND T1.ACCTITEM_ID = T2.ACCTITEM_ID  ",
				"    AND T1.FEE_TYPE=? and fp.pay_sn=t1.pay_sn AND fp.optr_id= ?"
				);
		
		return findToList(sql, 
				countyId, SystemConstants.TEMPLATE_TYPE_INVOICE, countyId,custId,
				StatusConstants.PAY, SystemConstants.BOOLEAN_FALSE,optrId,
				countyId,SystemConstants.TEMPLATE_TYPE_INVOICE, countyId,custId,
				 StatusConstants.PAY, SystemConstants.BOOLEAN_FALSE, SystemConstants.FEE_TYPE_ACCT,optrId
				);
	}
	
	public List<Map<String, Object>> queryYHZZFeeByCustId(String custId,String countyId)throws Exception {
		String sql = " SELECT T1.FEE_SN, T1.REAL_PAY AMOUNT, T2.PRINTITEM_ID,t3.doc_type,t1.acct_id,t5.pay_type_name"
           +" FROM C_FEE T1, VEW_ACCTITEM T2,t_invoice_printitem t3,t_template_county t4,t_pay_type t5" 
           +" WHERE t3.printitem_id=t2.printitem_id AND t4.county_id=?"
           +" AND t4.template_id=t3.template_id AND t4.template_type=? AND T1.COUNTY_ID=?"
           +" AND T1.cust_id=?"
           +" AND T1.STATUS = ? AND t1.pay_type=t5.pay_type and t5.is_print='T'"
           +" AND T1.is_doc= ?"
           +" AND T1.ACCTITEM_ID = T2.ACCTITEM_ID" 
           +" AND T1.FEE_TYPE=?"
           +" and t1.pay_type=?";
		
		return findToList(sql, countyId, SystemConstants.TEMPLATE_TYPE_INVOICE,
				countyId, custId, StatusConstants.PAY,
				SystemConstants.BOOLEAN_FALSE, SystemConstants.FEE_TYPE_ACCT,
				SystemConstants.PAY_TYPE_BANK_DEDU);
	}


	/**
	 * 查询客户下的 未付费 数量和总额
	 * @param custId
	 * @param county_id
	 * @return
	 */
	public Map<String, Object> queryFeeView(String custId, String county_id) throws Exception{
		String sql =  " SELECT count(*) feeCount , nvl(sum(t.real_pay),0) feeTotal  " +
			     "FROM c_fee t " +
			     "WHERE t.real_pay<>0 AND t.cust_id=? and t.county_id=? and t.status=? ";
		List<Map<String, Object>> lst =  findToList(sql, custId,county_id,UNPAY);
		if(null != lst && lst.size() > 0){
			return lst.get(0);
		}
		return null;
	}

	/**
	 *
	 * @param custId
	 * @param county_id
	 * @return
	 */
	public Pager<FeeDto> queryAcctPayFee(String custId, QueryFeeInfo queryFeeInfo, String countyId,Integer start,Integer limit) throws JDBCException {
		String str = "",deviceCodeJoin="";
		
		if(queryFeeInfo != null){
			if(StringHelper.isNotEmpty(queryFeeInfo.getCreate_time1())){
				str += " and t.create_time >= to_date('"+queryFeeInfo.getCreate_time1()+"','yyyy-MM-dd hh24:mi:ss')";
			}
			if(StringHelper.isNotEmpty(queryFeeInfo.getCreate_time2())){
				str += " and t.create_time <= to_date('"+queryFeeInfo.getCreate_time2()+"','yyyy-MM-dd hh24:mi:ss')";
			}
			if(StringHelper.isNotEmpty(queryFeeInfo.getStatus())){
				str += " and t.status='" +queryFeeInfo.getStatus() +"'";
			}
			if(StringHelper.isNotEmpty(queryFeeInfo.getOptr_name())){
				str += " and t.optr_id in (select optr_id from s_optr where county_id='"+countyId+"' and optr_name like '%"+queryFeeInfo.getOptr_name()+"%')";
			}
			if(StringHelper.isNotEmpty(queryFeeInfo.getInvoice_id())){
				str += " and t.invoice_id='" +queryFeeInfo.getInvoice_id() +"'";
			}
			String deviceCode = queryFeeInfo.getDevice_code();
		
			if(StringHelper.isNotEmpty(deviceCode)){
				deviceCodeJoin=" join c_user u on t.user_id=u.user_id and t.county_id=u.county_id and u.card_id like '%"+deviceCode+"%' ";
			}
		}
		String sql =StringHelper.append( 
				" SELECT t.*,t2.acctitem_name fee_text,t2.acctitem_name,ca.acct_type,t3.begin_date, ",
				"  t3.prod_invalid_date,r.finance_status,r.invoice_type doc_type,t3.prod_sn",
				"  FROM c_fee t  ",
				"  join vew_acctitem t2 on  t.acctitem_id = t2.acctitem_id  ",
				"  left join c_acct ca on ca.acct_id=t.acct_id and ca.county_id=t.county_id ",
				"  join c_fee_acct t3 on t.fee_sn=t3.fee_sn and t.county_id=t3.county_id ",
				"  left join r_invoice r on  t.invoice_id=r.invoice_id and t.invoice_code=r.invoice_code ",
				deviceCodeJoin,
				"  WHERE   t.cust_id=? and t.county_id=? ",
				str,
				"  order by t.create_time desc ");
		
		return createQuery(FeeDto.class, sql, custId, countyId).setStart(start).setLimit(limit).page();
	}
	
	
	public String queryMaxDoneCode(String custId, String countyId) throws JDBCException{
		String sql = " select decode( max(create_done_code),null,-1, max(create_done_code)) from (SELECT t.*,t2.acctitem_name fee_text,t2.acctitem_name,ca.acct_type,t3.begin_date,"
			+ " t3.prod_invalid_date,r.finance_status,r.invoice_type doc_type"
			+ " FROM c_fee t,vew_acctitem t2,c_acct ca,c_fee_acct t3,r_invoice r"
			+ " WHERE ca.acct_id(+)=t.acct_id and t.fee_sn=t3.fee_sn"
			+ " and t.invoice_id=r.invoice_id(+) and t.invoice_code=r.invoice_code(+)"
			+ " and t.cust_id=? and t.county_id=? and t3.county_id=? and t.acctitem_id = t2.acctitem_id " 
			+ " Union "
			+ " SELECT t.*,t2.prom_fee_name fee_text,'' acctitem_name,'' acct_type,null begin_date,"
			+ " null prod_invalid_date,r.finance_status,r.invoice_type doc_type"
			+ " FROM c_fee t,p_prom_fee t2,r_invoice r"
			+ " where t.fee_type = ? and t.fee_id=t2.prom_fee_id"
			+ " and t.invoice_id=r.invoice_id(+) and t.invoice_code=r.invoice_code(+)"
			+ " and t.cust_id=? and t.county_id= ? " 
			+ " union "
			+ " select null fee_sn,t.fee_type,t.busi_done_code,t.create_done_code,t.reverse_done_code,"
			+ " t.busi_code,null cust_id,null user_id,null acct_id,null acctitem_id,null fee_id,null count,"
			+ " null status,sum(t.should_pay) should_pay,sum(t.real_pay) real_pay,t.pay_type,"
			+ " cd.done_date create_time,t.acct_date,null disct_type,null disct_info,null promotion_sn,"
			+ " null auto_promotion,t.is_doc,t.invoice_mode,t.invoice_id,t.invoice_book_id,t.invoice_code,"
			+ " t.optr_id,t.dept_id,t.area_id,t.county_id,null busi_optr_id,null invoice_fee,null addr_id,null pay_sn,"
			+ " t2.acctitem_name fee_text,t2.acctitem_name,'UNIT' acct_type,null begin_date,"
			+ " null prod_invalid_date,null finance_status,null doc_type"
			+ " from c_fee t,vew_acctitem t2,c_done_code cd,c_done_code_detail cdc"
			+ " where t.create_done_code = cd.done_code and cd.done_code = cdc.done_code"
			+ " and t.acctitem_id = t2.acctitem_id and cd.busi_code=?"
			+ " and cdc.cust_id = ? and t.county_id=?"
			+ " and cdc.county_id=? and cd.county_id=? " 
			+ " group by t.fee_type,t.busi_done_code,t.create_done_code,t.reverse_done_code,"
			+ " t.busi_code,t.should_pay,t.pay_type,cd.done_date,t.acct_date,t.is_doc,"
			+ " t.invoice_mode,t.invoice_id,t.invoice_book_id,t.invoice_code,t.optr_id,t.dept_id,t.area_id,t.county_id,t2.acctitem_name"
			+" ) where status <> ? and pay_type <> ? order by create_time desc,fee_sn desc";
		return this.findUnique(sql, custId, countyId, countyId,
				SystemConstants.FEE_TYPE_PROMACCT, custId, countyId,
				BusiCodeConstants.Unit_ACCT_PAY, custId, countyId, countyId,
				countyId,StatusConstants.INVALID,SystemConstants.BUSI_BUY_MODE_PRESENT);
		
	} 
	
	public List<FeeDto> queryUnitPayFee(String custId, String countyId) throws JDBCException {
		String sql = StringHelper.append(
				" select t.create_done_code,t.fee_type,t.busi_code, sum(t.real_pay) real_pay, t2.acctitem_name fee_text," +
				" t.optr_id ,t.status,t.pay_type,t.dept_id,cd.done_date create_time,'UNIT' acct_type" +
				" from c_fee t,vew_acctitem t2,c_done_code cd ,c_done_code_detail cdc where t.busi_done_code = cd.done_code" +
				" and cd.done_code=cdc.done_code and cd.busi_code=? and cdc.cust_id=? and t.county_id = ?" +
				" and t.acctitem_id = t2.acctitem_id group by create_done_code,acctitem_name,fee_type,t.busi_code," +
				" t.optr_id,t.status,t.pay_type,t.dept_id,cd.done_date"
			);
		return createQuery(FeeDto.class, sql, BusiCodeConstants.Unit_ACCT_PAY,custId, countyId).list();
	}
	
	public Pager<FeeDto> queryAcctPayFeeHis(String custId, QueryFeeInfo queryFeeInfo, String countyId,Integer start,Integer limit) throws JDBCException {
		String sql = "SELECT t.*,t2.acctitem_name fee_text,t2.acctitem_name,ca.acct_type,r.finance_status,r.invoice_type doc_type"+
				" FROM c_fee t,vew_acctitem t2,c_acct_his ca,r_invoice r"+
				" WHERE ca.acct_id=t.acct_id and t.cust_id=? and t.county_id=?"+
				" and t.invoice_id=r.invoice_id(+) and t.invoice_code=r.invoice_code(+)"+
				" and t.acctitem_id = t2.acctitem_id";
		if(queryFeeInfo != null){
			if(StringHelper.isNotEmpty(queryFeeInfo.getCreate_time1())){
				sql += " and t.create_time >= to_date('"+queryFeeInfo.getCreate_time1()+"','yyyy-MM-dd hh24:mi:ss')";
			}
			if(StringHelper.isNotEmpty(queryFeeInfo.getCreate_time2())){
				sql += " and t.create_time <= to_date('"+queryFeeInfo.getCreate_time2()+"','yyyy-MM-dd hh24:mi:ss')";
			}
			if(StringHelper.isNotEmpty(queryFeeInfo.getStatus())){
				sql += " and t.status='" +queryFeeInfo.getStatus() +"'";
			}
			if(StringHelper.isNotEmpty(queryFeeInfo.getOptr_name())){
				sql += " and t.optr_id in (select optr_id from s_optr where county_id='"+countyId+"' and optr_name like '%"+queryFeeInfo.getOptr_name()+"%')";
			}
			if(StringHelper.isNotEmpty(queryFeeInfo.getInvoice_id())){
				sql += " and t.invoice_id='" +queryFeeInfo.getInvoice_id() +"'";
			}
			String deviceCode = queryFeeInfo.getDevice_code();
			if(StringHelper.isNotEmpty(deviceCode)){
				sql = "SELECT t.*,t2.acctitem_name fee_text,t2.acctitem_name,ca.acct_type,r.finance_status,r.invoice_type doc_type"+
					" FROM c_fee t,vew_acctitem t2,c_acct_his ca,r_invoice r,c_user u"+
					" WHERE ca.acct_id=t.acct_id and t.cust_id=? and t.county_id=?"+
					" and t.invoice_id=r.invoice_id(+) and t.invoice_code=r.invoice_code(+)"+
					" and t.acctitem_id = t2.acctitem_id and t.user_id=u.user_id and u.card_id like '%"+deviceCode+"%'";
			}
		}
		sql += " order by t.fee_sn desc";
		return createQuery(FeeDto.class, sql, custId, countyId).setStart(start).setLimit(limit).page();
	}
	
	private String[] getSqlCondition(String beginOptrateDate,
			String endOptrateDate, String beginAcctDate, String endAcctDate,
			String optrId, String feeType, String deptId, String custNo,
			String beginInvoice, String endInvoice, String countyId, String dataRight){
		String financeSql = "", sql = "";
		//dataRight指修改帐务日期的权限等级
		//营业厅级别：只可修改为轧帐的账务日期
		if(dataRight.equals(SystemConstants.SYS_LEVEL_DEPT)){
			financeSql = " and i.finance_status = '"+StatusConstants.IDLE+"'"
					+ " and not exists (select 1 from t_grip_data gd where"
					+ " regexp_replace(gd.grip_key, '[^0-9]+', '') = t.dept_id"
					+ " and regexp_replace(gd.grip_key, '[^A-Z]+', '')='DEPT')";
		}else if(dataRight.equals(SystemConstants.SYS_LEVEL_COUNTY)){
			//分公司级别：已核销发票不能修改账务日期以外，其他情况均可修改账务日期
			financeSql = " and i.finance_status in ('"+StatusConstants.IDLE+"','"+StatusConstants.CHECKED+"')";
		}else if(dataRight.equals(SystemConstants.SYS_LEVEL_ALL)){
			//所有
		}
//		String financeSql  = " and exists ("+
//				" select i.invoice_id from r_invoice i where"+
//				" i.invoice_id=t.invoice_id and i.invoice_book_id=t.invoice_book_id"+ finance_status + ")";
		
		/*if(StringHelper.isEmpty(countyId)){
			sql +=" and t.dept_id='"+deptId+"'";
		}else{
			sql +=" and t.county_id='"+countyId+"'";
		}*/
		if(StringHelper.isNotEmpty(deptId)){
			sql +=" and t.dept_id='"+deptId+"'";
		}else{
			sql +=" and t.county_id='"+countyId+"'";
		}
		
		if(StringHelper.isEmpty(endInvoice)){
			endInvoice = beginInvoice;
		}
		if(StringHelper.isEmpty(endAcctDate)){
			endAcctDate = beginAcctDate;
		}
		if(StringHelper.isNotEmpty(custNo)){
			sql += " and t.cust_id in (select cust_id from c_cust where cust_no='"+custNo+"' union"+
               " select cust_id from c_cust_his where cust_no='"+custNo+"')";
		}
		if(StringHelper.isNotEmpty(beginInvoice)){
			sql += " and t.invoice_id between '"+beginInvoice+"' and '"+endInvoice+"'";
		}
		if(StringHelper.isNotEmpty(optrId)){
			sql += " and t.optr_id='"+optrId+"'";
		}
		if(StringHelper.isNotEmpty(beginAcctDate)){
			sql += " and to_char(t.acct_date,'yyyy-mm-dd') between '"+beginAcctDate+"' and '"+endAcctDate+"'";
		}
		
		return new String[]{financeSql,sql};
	}
	
	//查询批量修改预存费
	public List<FeeDto> queryBatchAcctPayFee(String beginOptrateDate,
			String endOptrateDate, String beginAcctDate, String endAcctDate,
			String optrId, String feeType, String deptId, String custNo,
			String beginInvoice, String endInvoice, String countyId, String dataRight)
			throws JDBCException {
		String[] sqlConditions = this.getSqlCondition(beginOptrateDate, endOptrateDate, beginAcctDate,
				endAcctDate, optrId, feeType, deptId, custNo, beginInvoice,
				endInvoice, countyId, dataRight);
		
		List<String> params = new ArrayList<String>();
		//营业收费
		String acctSql = "SELECT t.*,t2.acctitem_name fee_text,ca.acct_type,t3.begin_date,t3.prod_invalid_date,"+
				" u.user_name,u.user_type user_type_text,u.card_id device_code"+
				" FROM c_fee t,vew_acctitem t2,c_acct ca,c_fee_acct t3,c_user u,r_invoice i"+
				" WHERE ca.acct_id=t.acct_id and t.fee_sn=t3.fee_sn"+
				" and t.acctitem_id = t2.acctitem_id and t.user_id=u.user_id(+)"+
				" and i.invoice_id(+)=t.invoice_id and i.invoice_code(+)=t.invoice_code"+
				" and to_char(t.create_time,'yyyy-mm-dd') between ? and ?"+
				" "+sqlConditions[0]+""+
				" "+sqlConditions[1]+""+
				" union "+
				"SELECT t.*,t2.acctitem_name fee_text,ca.acct_type,t3.begin_date,t3.prod_invalid_date,"+
				" u.user_name,u.user_type user_type_text,u.card_id device_code"+
				" FROM c_fee t,vew_acctitem t2,c_acct_his ca,c_fee_acct t3,c_user_his u,r_invoice i"+
				" WHERE ca.acct_id=t.acct_id and t.fee_sn=t3.fee_sn"+
				" and t.acctitem_id = t2.acctitem_id and t.user_id=u.user_id(+)"+
				" and i.invoice_id(+)=t.invoice_id and i.invoice_code(+)=t.invoice_code"+
				" and to_char(t.create_time,'yyyy-mm-dd') between ? and ?"+
				" "+sqlConditions[0]+""+
				" "+sqlConditions[1]+" " ;
		
		String unitPreOtherCondition = " ";
		
		if(StringHelper.isNotEmpty(beginAcctDate) && StringHelper.isNotEmpty(endAcctDate)){
			unitPreOtherCondition = " and to_char(t.create_time,'yyyy-mm-dd') between '" + beginAcctDate +"' and '" + endAcctDate + "' " ;
		}else if(StringHelper.isNotEmpty(endAcctDate) && StringHelper.isEmpty(beginAcctDate)){
			unitPreOtherCondition = " and t.create_time < to_date('" + endAcctDate + "','yyyy-mm-dd')";
		}else if(StringHelper.isNotEmpty(beginAcctDate) && StringHelper.isEmpty(endAcctDate)){
			unitPreOtherCondition = " and t.create_time > to_date('" + beginAcctDate + "','yyyy-mm-dd')";
		}
		
		if(StringHelper.isNotEmpty(optrId)){
			unitPreOtherCondition += " and t.optr_id = '" + optrId + "' ";
		}
		
		if(StringHelper.isNotEmpty(deptId)){
			unitPreOtherCondition += " and t.dept_id = '" + deptId + "' ";
		}
		
		
		//合同收费
		String unitpreSql = " select t.* , cgc.contract_name fee_text,'' acct_type,null begin_date,null prod_invalid_date," +
				" cgc.cust_name user_name,'' user_type_text,'' device_code " +
				" from busi.c_general_contract_pay cp, busi.c_fee t, busi.c_general_contract cgc "+
				" where cp.contract_id = cgc.contract_id and cp.done_code = t.create_done_code " +
				" and to_char(t.create_time,'yyyy-mm-dd') between ? and ? and cgc.county_id = '" + countyId + "' " +
				unitPreOtherCondition
				
				+ " union all " +//合同款主记录
				" select t.* , cgc.contract_name fee_text,'' acct_type,null begin_date,null prod_invalid_date," +
				" cgc.cust_name user_name,'' user_type_text,'' device_code " +
				" from busi.c_fee t,busi.c_general_contract cgc "+
				" where cgc.fee_sn = t.fee_sn  " +
				" and to_char(t.create_time,'yyyy-mm-dd') between ? and ? and cgc.county_id = '" + countyId + "' " +
				unitPreOtherCondition ;
		
		String sql = "select t.* from (";
		
		//ACCT'],['杂费','BUSI'],['非营业收费','UNITPRE']]
		
		params.add(beginOptrateDate);
		params.add(endOptrateDate);
		params.add(beginOptrateDate);
		params.add(endOptrateDate);
		
		if(feeType.equals("ALL")){
			sql += acctSql + " union all " + unitpreSql ;
			params.add(beginOptrateDate);
			params.add(endOptrateDate);
			params.add(beginOptrateDate);
			params.add(endOptrateDate);
		}else if(feeType.equals("ACCT")){
			sql += acctSql;
		}else if(feeType.equals("UNITPRE")){
			sql += unitpreSql;
		}
		
		
		sql = StringHelper.append(sql,") t order by t.create_time desc");
		
		return createQuery(FeeDto.class, sql, params.toArray()).list();
	}

	//查询批量修改杂费(业务费)
	public List<FeeDto> queryBatchBusiPayFee(String beginOptrateDate,
			String endOptrateDate, String beginAcctDate, String endAcctDate,
			String optrId, String feeType, String deptId, String custNo,
			String beginInvoice, String endInvoice, String countyId, String dataRight) throws JDBCException {
		String[] sqlConditions = this.getSqlCondition(beginOptrateDate, endOptrateDate, beginAcctDate,
				endAcctDate, optrId, feeType, deptId, custNo, beginInvoice,
				endInvoice, countyId, dataRight);
		String sql = 
			" select t.* from (SELECT t.fee_sn, t.fee_type, busi_done_code, create_done_code, reverse_done_code, busi_code, cust_id, user_id, is_doc, " +
			"t.status, t.fee_id, count, should_pay, real_pay, disct_type, disct_info, promotion_sn, pay_type, t.invoice_mode," +
			" t.invoice_code,t.invoice_book_id, t.invoice_id, t.create_time, acct_date, t.area_id, t.county_id, t.optr_id, t.dept_id, t.acct_id, t.acctitem_id, t.auto_promotion," +
			"t2.fee_name fee_text,t2.deposit,d.device_id,d.device_code,d.device_type"+
			" FROM c_fee t,t_busi_fee t2,c_fee_device d,r_invoice i"+
			" WHERE t.fee_id = t2.fee_id and t.fee_sn=d.fee_sn "+
			" and to_char(t.create_time,'yyyy-mm-dd') between ? and ? "+
			" and i.invoice_id(+)=t.invoice_id and i.invoice_code(+)=t.invoice_code"+
			" "+sqlConditions[0]+""+
			" "+sqlConditions[1]+""+
			" UNION SELECT t.fee_sn, t.fee_type, busi_done_code, create_done_code, reverse_done_code, busi_code, cust_id, user_id, is_doc, " +
			"t.status, t.fee_id, count, should_pay, real_pay, disct_type, disct_info, promotion_sn, pay_type, t.invoice_mode," +
			" t.invoice_code,t.invoice_book_id, t.invoice_id, t.create_time, acct_date, t.area_id, t.county_id, t.optr_id, t.dept_id, t.acct_id, acctitem_id, t.auto_promotion," +
			"t2.fee_name fee_text,t2.deposit,'','',''"+
			" FROM c_fee t,t_busi_fee t2,c_fee_busi d,r_invoice i"+
			" WHERE t.fee_id = t2.fee_id and t.fee_sn=d.fee_sn"+
			" and to_char(t.create_time,'yyyy-mm-dd') between ? and ? "+
			" and i.invoice_id(+)=t.invoice_id and i.invoice_code(+)=t.invoice_code"+
			" "+sqlConditions[0]+""+
			" "+sqlConditions[1]+""+
			") t order by t.create_time desc";
		return createQuery(FeeDto.class, sql, beginOptrateDate, endOptrateDate,
				beginOptrateDate, endOptrateDate).list();
	}
	
	/**
	 * 
	 * @param custId
	 * @param countyId 
	 * @return
	 * @throws JDBCException 
	 */
	public Pager<FeeDto> queryBusiPayFee(String custId, QueryFeeInfo queryFeeInfo, 
			String countyId, Integer start, Integer limit) throws JDBCException {
		String sql = null, str = "", deviceCodeStr = "";
		if(queryFeeInfo != null){
			if(StringHelper.isNotEmpty(queryFeeInfo.getCreate_time1())){
				str += " and t.create_time >= to_date('"+queryFeeInfo.getCreate_time1()+"','yyyy-MM-dd hh24:mi:ss')";
			}
			if(StringHelper.isNotEmpty(queryFeeInfo.getCreate_time2())){
				str += " and t.create_time <= to_date('"+queryFeeInfo.getCreate_time2()+"','yyyy-MM-dd hh24:mi:ss')";
			}
			if(StringHelper.isNotEmpty(queryFeeInfo.getStatus())){
				str += " and t.status='" +queryFeeInfo.getStatus() +"'";
			}
			if(StringHelper.isNotEmpty(queryFeeInfo.getOptr_name())){
				str += " and t.optr_id in (select optr_id from s_optr where county_id='"+countyId+"' and optr_name like '%"+queryFeeInfo.getOptr_name()+"%')";
			}
			if(StringHelper.isNotEmpty(queryFeeInfo.getInvoice_id())){
				str += " and t.invoice_id='" +queryFeeInfo.getInvoice_id() + "'";
			}
			if(StringHelper.isNotEmpty(queryFeeInfo.getDevice_type())){
				deviceCodeStr += " and d.device_type='" +queryFeeInfo.getDevice_type() + "'";
			}
			if(StringHelper.isNotEmpty(queryFeeInfo.getDevice_code())){
				deviceCodeStr += " and d.device_code like '%" +queryFeeInfo.getDevice_code() + "%' ";
			}
		}
		sql = " select t.*,'Y' is_busi_fee from (SELECT t.fee_sn, t.fee_type, busi_done_code, create_done_code, reverse_done_code, busi_code, cust_id, user_id, is_doc, "
				+ "t.status, t.fee_id, count, should_pay, real_pay, disct_type, disct_info, promotion_sn, pay_type, t.invoice_mode,"
				+ " t.invoice_code,t.invoice_book_id, t.invoice_id, t.create_time, acct_date, t.area_id, t.county_id, t.optr_id, dept_id, acct_id, acctitem_id, auto_promotion,"
				+ "t2.fee_name fee_text,t2.deposit,d.device_id,d.device_code,d.device_type,r.finance_status,t.busi_optr_id,r.invoice_type doc_type,d.buy_num,d.device_model, v.model_name device_model_name,null count_text"
				+ " from c_fee t join t_busi_fee t2 on t.fee_id = t2.fee_id "
				+ " join c_fee_device d on t.fee_sn=d.fee_sn"
				+ " left join r_invoice r on t.invoice_id=r.invoice_id and t.invoice_code=r.invoice_code"
				+ " left join vew_device_typemodel v on d.device_type||'_'||d.device_model=v.device_type_model"
				+ " WHERE t.cust_id = ? and t.county_id= ? " + str + "" + deviceCodeStr;
		
		if(StringHelper.isEmpty(deviceCodeStr)){
			sql += " UNION SELECT t.fee_sn, t.fee_type, busi_done_code, create_done_code, reverse_done_code, busi_code, cust_id, user_id, is_doc, " 
				+ "t.status, t.fee_id, count, should_pay, real_pay, disct_type, disct_info, promotion_sn, pay_type, t.invoice_mode," 
				+ " t.invoice_code,t.invoice_book_id, t.invoice_id, t.create_time, acct_date, t.area_id, t.county_id, t.optr_id, dept_id, acct_id, acctitem_id, auto_promotion,"
				+ "t2.fee_name fee_text,t2.deposit,'','','',r.finance_status,t.busi_optr_id,r.invoice_type doc_type,null buy_num, null device_model, null device_model_name,"
				+ " case when t.disct_info is not null and t.fee_type='BUSI' then t.disct_info end count_text"
				+ " FROM c_fee t,t_busi_fee t2,c_fee_busi d,r_invoice r"
				+ " WHERE t.cust_id = ? and t.county_id= ? "
				+ " and t.invoice_id=r.invoice_id(+) and t.invoice_code=r.invoice_code(+)"+ str
				+ " and t.fee_id = t2.fee_id and t.fee_sn=d.fee_sn) t order by t.create_time desc";
			return createQuery(FeeDto.class, sql, custId, countyId, custId, countyId).setStart(start).setLimit(limit).page();
		}else{
			sql += " ) t order by t.create_time desc";
			return createQuery(FeeDto.class, sql, custId, countyId).setStart(start).setLimit(limit).page();
		}
	}
	
	
	public Pager<FeePayDto> queryFeePay(String custId, QueryFeeInfo queryFeeInfo, 
			Integer start, Integer limit) throws JDBCException {
		String sql = null, str = "";
		if(queryFeeInfo != null){
			if(StringHelper.isNotEmpty(queryFeeInfo.getCreate_time1())){
				str += " and t.create_time >= to_date('"+queryFeeInfo.getCreate_time1()+"','yyyy-MM-dd hh24:mi:ss')";
			}
			if(StringHelper.isNotEmpty(queryFeeInfo.getCreate_time2())){
				str += " and t.create_time <= to_date('"+queryFeeInfo.getCreate_time2()+"','yyyy-MM-dd hh24:mi:ss')";
			}
			if(StringHelper.isNotEmpty(queryFeeInfo.getStatus())){
				str += " and t.is_valid='" +queryFeeInfo.getStatus() +"'";
			}
			if(StringHelper.isNotEmpty(queryFeeInfo.getOptr_name())){
				str += " and t.optr_id in (select optr_id from s_optr where  optr_name like '%"+queryFeeInfo.getOptr_name()+"%')";
			}
			if(StringHelper.isNotEmpty(queryFeeInfo.getInvoice_id())){
				str += " and t.receipt_id='" +queryFeeInfo.getInvoice_id() + "'";
			}			
		}
		sql = "select t.* from c_fee_pay t where  t.cust_id= ? "+str +" order by t.create_time desc";
		return createQuery(FeePayDto.class, sql, custId).setStart(start).setLimit(limit).page();
	}
	
	/**
	 * 根据feeSn查询
	 * @param feeSn
	 * @return
	 * @throws Exception
	 */
	public CFeePay queryByFeeSn(String feeSn) throws Exception{
		String sql = "select * from c_fee_pay a,c_fee b,c_fee_pay_detail c where a.pay_sn=c.pay_sn "
				+ " and b.fee_sn=c.fee_sn and b.fee_sn=?";
		return createQuery(CFeePay.class, sql, feeSn).first();
	}
	
	/**
	 * 查询单位批量缴费需要打印的记录
	 * @param custIds
	 * @param optrId
	 * @param countyId
	 * @return
	 * @throws JDBCException 
	 */
	public List<Map<String, Object>> queryUnitFeeByFeeSn(String unitCustId,
			String optrId, String countyId) throws JDBCException {
		/*String sql = append(
				" SELECT T1.FEE_SN, T1.REAL_PAY AMOUNT, T2.PRINTITEM_ID,t3.doc_type,t1.acct_id",
				"   FROM C_FEE T1, VEW_ACCTITEM T2,t_invoice_printitem t3,t_template_county t4,t_pay_type t5  ",
				"  WHERE t1.busi_code = ?  and t3.printitem_id=t2.printitem_id AND t4.county_id=? " +
				"     AND t4.template_id=t3.template_id AND t4.template_type=? AND T1.COUNTY_ID = ?  ",
				"    AND T1.cust_id in (" ,getSqlGenerator().in(custIds), ")  ",
				"    AND T1.STATUS = ? AND t1.pay_type=t5.pay_type and t5.is_print='T' ",
				"    AND T1.is_doc= ? ",
				"    AND T1.ACCTITEM_ID = T2.ACCTITEM_ID  ",
				"    AND T1.FEE_TYPE = ? AND T1.optr_id=?");*/
		String sql = "SELECT T1.FEE_SN, T1.REAL_PAY AMOUNT, T2.PRINTITEM_ID,t3.doc_type,t1.acct_id"+
				" FROM C_FEE T1, VEW_ACCTITEM T2,t_invoice_printitem t3,t_template_county t4,t_pay_type t5,c_cust_unit_to_resident r"+
				" WHERE t1.busi_code = ? and t3.printitem_id=t2.printitem_id AND t4.county_id=?"+
				" AND t4.template_id=t3.template_id AND t4.template_type=? AND T1.COUNTY_ID = ?"+
				" AND T1.STATUS = ? AND t1.pay_type=t5.pay_type and t5.is_print='T' AND T1.is_doc= ?"+
				" AND T1.ACCTITEM_ID = T2.ACCTITEM_ID AND T1.FEE_TYPE = ? AND T1.optr_id=?" +
				" and T1.cust_id=r.resident_cust_id AND r.unit_cust_id=?";

		return findToList(sql, BusiCodeConstants.Unit_ACCT_PAY, countyId,
				SystemConstants.TEMPLATE_TYPE_INVOICE, countyId,
				StatusConstants.PAY, SystemConstants.BOOLEAN_FALSE,
				SystemConstants.FEE_TYPE_ACCT, optrId, unitCustId);
	} 

	public List<FeeDto> queryFeePayDetail(String paySn)throws Exception {
		String sql = null;
		sql = append(
				" select t.real_pay,decode(t1.fee_name,null,t2.acctitem_name,t1.fee_name) fee_text,t.invoice_id,t.create_done_code " +
				" from c_fee t, t_busi_Fee t1, vew_acctitem t2",				
				" where t.fee_id = t1.fee_id(+) and t.acctitem_id = t2.acctitem_id(+) and t.pay_sn  = ? ");
		return createQuery(FeeDto.class, sql,paySn).list();
	}
	
}
