/**
 * CFeeDao.java	2010/07/30
 */

package com.ycsoft.business.dao.core.fee;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.bill.BillDto;
import com.ycsoft.beans.core.fee.CFee;
import com.ycsoft.beans.core.fee.CFeeAcct;
import com.ycsoft.beans.core.fee.CFeeDevice;
import com.ycsoft.beans.core.prod.CProd;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.business.dto.core.fee.BBillPrintDto;
import com.ycsoft.business.dto.core.fee.BbillingcycleCfgDto;
import com.ycsoft.business.dto.core.fee.CFeePayDto;
import com.ycsoft.business.dto.core.fee.FeeDto;
import com.ycsoft.business.dto.core.print.CInvoiceDto;
import com.ycsoft.business.dto.print.PrintFeeitemDto;
import com.ycsoft.commons.constants.BusiCodeConstants;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.DateHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;


/**
 * CFeeDao -> C_FEE table's operator
 */
@Component
public class CFeeDao extends BaseEntityDao<CFee> {

	/**
	 *
	 */
	private static final long serialVersionUID = -1674544282525067358L;

	/**
	 * default empty constructor
	 */
	public CFeeDao() {}

	public FeeDto queryUnPayFeeDto(String feeSn) throws JDBCException{
		String sql="select cf.*,fa.prod_sn from c_fee cf left join c_fee_acct fa on fa.fee_sn=cf.fee_sn where cf.fee_sn=? ";
		return  this.createQuery(FeeDto.class, sql, feeSn).first();
	}
	/**
	 * 按支付编号查询缴费记录
	 * @param paySn
	 * @return
	 * @throws JDBCException 
	 */
	public List<FeeDto> queryPayFeeDto(String paySn) throws JDBCException{
		String sql="select cf.*,fa.prod_sn from c_fee cf left join c_fee_acct fa on fa.fee_sn=cf.fee_sn where cf.pay_sn=? ";
		return  this.createQuery(FeeDto.class, sql, paySn).list();
	}
	/**
	 * 恢复费用记录的未支付状态
	 * @param paySn
	 * @throws JDBCException
	 */
	public void updateCFeeToUnPayByPaySn(String paySn) throws JDBCException{
		String sql="update c_fee set status=? ,pay_type=?,is_doc=decode(is_doc,'N','N','F'), "
				+" invoice_id=null,invoice_book_id=null,invoice_code=null,pay_sn=null  "
				+" where pay_sn=? ";
		this.executeUpdate(sql, StatusConstants.UNPAY,SystemConstants.PAY_TYPE_UNPAY,paySn);
	}
	
	/**
	 * 更新缴费记录的未支付状态
	 * @param cust_id
	 * @param done_code
	 * @throws JDBCException 
	 */
	public void updateCFeeToPay(String feeSn,String busi_optr_id,CFeePayDto pay,String isDoc) throws JDBCException{
		String sql=StringHelper.append(
				"update c_fee set status=? ,pay_type=?,",
				" invoice_mode=?,invoice_id=?,invoice_book_id=?,invoice_code=?,",
				" pay_sn=?,acct_date=sysdate,busi_optr_id=?,",
				" is_doc=? ",
				" where fee_sn=? and status=? ");
		this.executeUpdate(sql, 
				StatusConstants.PAY,pay.getPay_type(),
				pay.getInvoice_mode(),pay.getInvoice_id(),pay.getInvoice_book_id(),pay.getInvoice_code(),
				pay.getPay_sn(),busi_optr_id,
				isDoc,feeSn,StatusConstants.UNPAY);
	}
	/**
	 * 查询待支付的总额
	 * @param cust_id
	 * @return
	 * @throws JDBCException
	 */
	public Map<String,Integer> queryUnPaySum(String cust_id,String optr_id) throws JDBCException{
		String sql="select nvl(sum(cf.real_pay),0) fee,count(1) cnt from c_fee cf,c_done_code_unpay un where cf.create_done_code=un.done_code and un.cust_id=? and cf.status=? ";
		List<Object[]> list=this.createSQLQuery(sql, cust_id,StatusConstants.UNPAY).list();
		Map<String,Integer> map=new HashMap<>();
		if(list==null||list.size()==0){
			map.put("FEE", 0);
			map.put("CNT", 0);
		}else{
			map.put("FEE", Integer.valueOf(list.get(0)[0].toString()));
			map.put("CNT", Integer.valueOf(list.get(0)[1].toString()));
		}
		return map;
	}
	/**
	 * 查询未支付的费用明细
	 * 显示 费用编号 fee_sn,业务名称busi_name,费用名称fee_text,数量(当count不为空，显示count否则显示begin_date(yyyymmdd)+“-”+prod_invalid_date),操作员 optr_name,操作时间create_time,金额 real_pay,
	 * @param cust_id
	 * @return
	 * @throws JDBCException
	 */
	public List<FeeDto> queryUnPay(String cust_id,String optr_id) throws JDBCException{
		String sql=StringHelper.append(
				"select cf.*,nvl(atm.acctitem_name,bf.fee_name) fee_text,fa.prod_invalid_date,fa.begin_date,fa.prod_sn,",
					" case when fa.begin_date is not null and fa.prod_invalid_date is not null ",
					"          then to_char(fa.begin_date,'yyyymmdd')||'-'||to_char(fa.prod_invalid_date,'yyyymmdd') ",
					"      when cf.fee_id is not null and cf.fee_type='DEVICE' then vdtm.model_name",
					"      when cf.disct_info is not null and cf.fee_type='BUSI' then cf.disct_info end count_text, cfb.buy_num, u.user_type ",
					" from c_fee cf join c_done_code_unpay un on cf.create_done_code=un.done_code",
					" left join c_user u on cf.user_id=u.user_id",
					" left join c_fee_acct fa on fa.fee_sn=cf.fee_sn ",              
					" left join c_fee_device cfb on cfb.fee_sn=cf.fee_sn",
					" left join t_busi_fee bf on bf.fee_id=cf.fee_id",
					" left join vew_device_typemodel vdtm on cfb.device_type||'_'||cfb.device_model=vdtm.device_type_model",
					" left join vew_acctitem atm on atm.acctitem_id=cf.acctitem_id",
				" where un.cust_id=? and cf.status=? ",
				" order by cf.create_time ");
		return this.createQuery(FeeDto.class, sql, cust_id, StatusConstants.UNPAY).list();
	}
	/**
	 * 根据业务流水号查询未支付费用信息
	 * @param doneCode
	 * @return
	 * @throws JDBCException
	 */
	public List<CFee> queryUnPayByDoneCode(Integer doneCode) throws JDBCException{
		String sql=" select * from c_fee  where create_done_code=? and status=? ";
		return this.createQuery(sql, doneCode,StatusConstants.UNPAY).list();
	}
	
	//客户受理单打印获取收费
	public List<PrintFeeitemDto> queryPrintFee(String custId,SOptr optr,String docSn)throws Exception{
		String countyId = optr.getCounty_id();
		String optr_id = optr.getOptr_id();
		 String sql =  "SELECT cuser.card_id,cuser.stb_id,cuser.modem_mac,t.create_done_code done_code ,d.disct_name," +
		 		" (select cpd.invalid_date from c_prod cpd where cpd.prod_id  = caai.acctitem_id and cpd.acct_id = cacct.acct_id ) as invalid_date " 
		 	+ ",t.busi_code,T.real_pay, T.ACCTITEM_ID," +
		 		"NVL(FEEID.PRINTITEM_NAME,ITEM.PRINTITEM_NAME)ACCTITEM_NAME ,t.invoice_id,"
		 	+"  nvl(t.count,0) count,NVL(FEEID.PRINTITEM_ID,ITEM.PRINTITEM_ID) PRINTITEM_ID,t.fee_id"
			+"  FROM C_FEE T,c_acct_acctitem caai, p_prod_tariff_disct d,c_fee_acct cfa,c_acct cacct,c_user cuser, "
			+"  ( SELECT TBF.FEE_ID,TP.PRINTITEM_NAME,TP.PRINTITEM_ID   "
			+"  FROM BUSI.T_BUSI_FEE TBF ,BUSI.T_PRINTITEM TP  "
			+"   WHERE TBF.PRINTITEM_ID=TP.PRINTITEM_ID "
			+"    )FEEID "
			+"   ,(SELECT VA.ACCTITEM_ID,TP.PRINTITEM_NAME,TP.PRINTITEM_ID   "
			+"   FROM BUSI.VEW_ACCTITEM VA    ,BUSI.T_PRINTITEM TP  "
			+"   WHERE VA.PRINTITEM_ID=TP.PRINTITEM_ID "
			+"   )ITEM    "
			+"   WHERE T.STATUS = 'PAY'   and d.disct_id(+) = cfa.disct_id and cfa.fee_sn = t.fee_sn " 
//			+ "   and t.optr_id = ? "
//			+" and cpd.prod_id = t.acctitem_id and cuser.user_id = cpd.user_id and cpd.acct_id = cpd.acct_id and cpd.cust_id = t.cust_id and t.user_id = cpd.user_id "
			+" and caai.acct_id = cacct.acct_id and cacct.acct_id = t.acct_id and caai.acct_id = t.acct_id and t.acctitem_id = caai.acctitem_id and cuser.user_id(+) = cacct.user_id  "
			+"   AND T.FEE_ID = FEEID.FEE_ID(+) AND T.ACCTITEM_ID = ITEM.ACCTITEM_ID(+)  "
			+"   AND t.CUST_ID =? AND T.COUNTY_ID =?  and t.pay_type='XJ'  ";
		 if(!StringHelper.isEmpty(docSn)){
			 sql+=" AND  exists (select 1 from c_doc_item ditem where ditem.docitem_sn=t.create_done_code and  ditem.doc_sn = '"+docSn+"'  )";
		 }else{
			 sql+=" AND T.CREATE_TIME BETWEEN TRUNC(SYSDATE) AND TRUNC(SYSDATE + 1)";
			 sql+=" AND not exists (select 1 from c_doc_item ditem where ditem.docitem_sn=to_char(t.create_done_code)    )";
		 }
			sql+=" ORDER BY T.CREATE_TIME";
			return createQuery(PrintFeeitemDto.class, sql, custId, countyId).list();
//			return createQuery(PrintFeeitemDto.class, sql,optr_id, custId, countyId).list();
	}
	
	/**
	 * 查询所有账期
	 * 从201101开始查询，到当前年月为止
	 * @return
	 * @throws JDBCException
	 */
	public Pager<BbillingcycleCfgDto> queryAllBillingCycleCfg(String query, Integer start, Integer limit) throws Exception {
		String currentDateStr = new SimpleDateFormat("yyyyMM").format(new Date());
		if(query == null || "".equals(query)){
			query = currentDateStr;
		}else{
			query += "12";
			DateFormat df = new SimpleDateFormat("yyyyMM");
			Date d = DateHelper.getNextMonth(df.parse(query));
			query = df.format(d);
			if(query.compareTo(currentDateStr)>0){//大于当前月，去当前月份
				query = currentDateStr;
			}
		}
		
		String sql = "select * from b_billingcycle_cfg t where"+
				" t.billing_cycle_id >= '201101' and t.billing_cycle_id < ? order by t.billing_cycle_id desc";
		return this.createQuery(BbillingcycleCfgDto.class, sql, query)
				.setStart(start).setLimit(limit).page();
	}
	
	/**
	 * 账单费用信息
	 * @param custId
	 * @param currentDate
	 * @return
	 * @throws JDBCException
	 */
	public List<BillDto> queryFeeInfoByCustId(String custId,String currentDate) throws JDBCException {
		String sql = "select p.prod_name acctitem_name,b.user_id,sum(b.final_bill_fee) final_bill_fee"+
				" from b_bill b, p_prod p"+
				" where b.cust_id = ?"+
				" and b.billing_cycle_id = ?"+
				" and b.prod_id = p.prod_id"+
				" and b.user_id is not null"+
				" group by b.user_id,p.prod_name"+
				" order by b.user_id";
		
		return this.createQuery(BillDto.class, sql, custId, currentDate).list();
	}
	
	public List<BillDto> queryPublicFeeInfoByCustId(String custId,String currentDate) throws JDBCException {
		String sql = "select p.acctitem_name,b.acctitem_id,sum(b.final_bill_fee) final_bill_fee"+
			" from b_bill b, t_public_acctitem p"+
			" where b.cust_id = ?"+
			" and b.billing_cycle_id = ?"+
			" and b.prod_id = p.acctitem_id"+
			" and b.user_id is null"+
			" group by b.acctitem_id,p.acctitem_name"+
			" order by b.acctitem_id";
		return this.createQuery(BillDto.class, sql, custId, currentDate).list();
	}
	
	public List<BillDto> queryPromInfoByCustId(String custId,String promFeeSn,String countyId) throws JDBCException {
		String sql = "select t2.user_id,t3.prom_fee_name acctitem_name,sum(t2.real_pay) final_bill_fee " +
				" from c_prom_fee t1, c_prom_fee_prod t2,p_prom_fee t3 " +
				" where t1.prom_fee_sn = t2.prom_fee_sn and t1.cust_id = ? and t3.prom_fee_id = t1.prom_fee_id  " +
				" and t1.county_id = ?  and t1.prom_fee_sn = ? group by t2.user_id,t3.prom_fee_name";
		
		return this.createQuery(BillDto.class, sql, custId,countyId,promFeeSn).list();
	}
	
	/**
	 * 查询客户某账期的数据
	 * @param custId
	 * @param billingCycleId
	 * @return
	 * @throws Exception
	 */
	public BBillPrintDto queryBillPrint(String custId,String billingCycleId) throws Exception {
		String sql = " select * from b_bill_print where cust_id=? and billing_cycle_id=?";
		return this.createQuery(BBillPrintDto.class, sql, custId, billingCycleId).first();
	}
	
	public BBillPrintDto queryPromPrint(String custId,String promFeeSn,String countyId) throws Exception {
		String sql = " select * from c_prom_fee where cust_id=? and prom_fee_sn=? and county_id = ? ";
		return this.createQuery(BBillPrintDto.class, sql, custId, promFeeSn,countyId).first();
	}
	
	/**
	 * 批量更改费用项的状态，及账务日期
	 * @param feeSn 要修改的费用项
	 * @param payType 支付类型
	 */
	public void updatePay(String[] feeSn,Date acctDate,String busiOptrId)
			throws Exception {
		String sql = "update c_fee t set acct_date=?,busi_optr_id=? "
				+ " where t.fee_sn in (" + getSqlGenerator().in(feeSn) + ") ";
		executeUpdate(sql, acctDate,busiOptrId);
	}

	/**
	 * 修改费用状态
	 * @param feeSn
	 * @param status
	 * @throws Exception
	 */
	public void updateStatus(String feeSn,String status)throws JDBCException {
		String sql = "update c_fee t set t.status=? " +
				" where t.fee_sn = ? ";
		executeUpdate(sql, status,feeSn );
	}
	
	/**
	 * @param feeSn
	 * @param status
	 * @param doneCode
	 * @throws JDBCException
	 */
	public void saveCancelFee(String feeSn,String status,Integer doneCode)throws JDBCException {
		String sql = "update c_fee t set t.status=? ,t.reverse_done_code = ?  " +
				" where t.fee_sn = ? ";
		executeUpdate(sql, status,doneCode,feeSn );
	}
	
	public List<String> queryDocFeeSn(List<String> docSnItems) throws JDBCException{
		String sql = "SELECT fee_sn FROM c_doc_fee t WHERE t.docitem_sn in ("+getSqlGenerator().in(docSnItems.toArray(new String[docSnItems.size()]))+")";
		return findUniques(sql);
	}
	/**
	 * 更新FeeBusi的is_doc为 T
	 * @throws Exception
	 */
	public void updateDocStatus(List<String> feeSnList, String invoiceId,
			String invoiceCode, String invoiceBookId) throws JDBCException {
		String sql = "update c_fee t set t.is_doc=?,invoice_id=?,invoice_code=?,invoice_book_id=?,invoice_mode=? where t.fee_sn in ("+getSqlGenerator().in(feeSnList.toArray(new String[feeSnList.size()]))+")";
		executeUpdate(sql,SystemConstants.BOOLEAN_TRUE,invoiceId,invoiceCode,invoiceBookId,SystemConstants.INVOICE_MODE_AUTO);
	}

	public List<FeeDto> queryByPromotionId(String custId,String userId,String promotionId) throws Exception{
		String sql ="select b.fee_sn,t.fee_name,b.fee_id,b.real_pay should_pay,a.disct_value real_pay " +
				" from p_promotion_fee a ,c_fee b ,t_busi_fee t" +
				" where b.cust_id=? and a.promotion_id=? and a.fee_id=t.fee_id and " +
				" a.fee_id = b.fee_id and (b.user_id is null or b.user_id = ?)";
		return createQuery(FeeDto.class,sql, custId,promotionId,userId).list();
	}

	public List<CFee> queryByDoneCode(Integer doneCode,String countyId) throws Exception{
		String sql = "select * from c_fee where create_done_code=? and county_id=? ";
		return createQuery(sql,doneCode,countyId).list();

	}
	
	public List<CFee> queryByBusiDoneCode(Integer busiDoneCode,String countyId) throws Exception{
		String sql = "select * from c_fee where busi_done_code=? and county_id=? ";
		return createQuery(sql,busiDoneCode,countyId).list();

	}
	
	public List<CFee> queryByInvoiceId(String feeSn,CInvoiceDto oldInvoice,String countyId) throws Exception{
		String sql = "select * from c_fee where fee_sn <> ? and invoice_id = ? and invoice_code = ? and county_id = ? ";
		return createQuery(sql,feeSn,oldInvoice.getInvoice_id(),oldInvoice.getInvoice_code(),countyId).list();

	}
	
	public List<CFee> querySumFeeByDoneCode(String custId,Integer doneCode, String countyId) throws Exception{
		String sql = "select fee_type,fee_id,fd.fee_std_id,null addr_id,sum(decode(status,'PAY',real_pay,'UNPAY',real_pay,0)) real_pay,sum(fd.buy_num) buy_num" +
				" from c_fee f,c_fee_device fd " +
				" where f.fee_sn=fd.fee_sn and f.cust_id=?" +
				" and f.busi_done_code=? and f.county_id=? and fd.county_id=?" +
				" and f.fee_type<> ?" +
				" group by fee_type,f.fee_id,fd.fee_std_id" +
				" union all " +
				"select fee_type,fee_id,null fee_std_id,max(f.addr_id) addr_id,sum(decode(status,'PAY',real_pay,'UNPAY',real_pay,0)) real_pay,1 buy_num" +
				" from c_fee f,c_fee_busi fd " +
				" where f.fee_sn=fd.fee_sn and f.cust_id=?" +
				" and f.busi_done_code=? and f.county_id=? and fd.county_id=?" +
				" and f.fee_type<> ?" +
				" group by fee_type,f.fee_id";
		return createQuery(sql, custId, doneCode, countyId, countyId,
				SystemConstants.FEE_TYPE_ACCT, custId, doneCode, countyId,
				countyId, SystemConstants.FEE_TYPE_ACCT).list();
	}
	

	public List<CFeeAcct> queryAcctFeeByDoneCode(Integer doneCode,String countyId) throws Exception{
		String sql = "select * from c_fee a,c_fee_acct b where a.fee_sn=b.fee_sn and " +
				" a.create_done_code=? and a.county_id=? and a.fee_type=? ";
		return createQuery(CFeeAcct.class,sql,doneCode,countyId,SystemConstants.FEE_TYPE_ACCT).list();

	}

	/**
	 * @param doneCode
	 * @param county_id
	 * @return
	 */
	public List<CFeeDevice> queryDeviceByDoneCode(Integer doneCode, String countyId) throws Exception{
		String sql = "select * from c_fee_device a,c_fee b " +
				" where a.fee_sn = b.fee_sn" +
				" and b.fee_id is not null " +
				" and b.create_done_code = ? and b.county_id=?";
		return createQuery(CFeeDevice.class,sql,doneCode,countyId).list();
	}
	
	public List<CFeeDevice> queryDeviceByDoneCodeAndFeeStdId(Integer doneCode, String feeId, String feeStdId) throws Exception{
		String sql = "select * from c_fee_device a,c_fee b " +
				" where a.fee_sn = b.fee_sn" +
				" and b.create_done_code = ?" +
				" and b.fee_id=? and a.fee_std_id=? ";
		return createQuery(CFeeDevice.class,sql,doneCode, feeId, feeStdId).list();
	}

	/**
	 * @param promotionSn
	 * @param county_id
	 */
	public void cancelDisct(String promotionSn, String countyId) throws Exception{
		String sql ="update c_fee set real_pay = should_pay," +
				" disct_info=null,disct_type=null,promotion_sn=null" +
				" where promotion_sn=? and county_id=? ";
		executeUpdate(sql, promotionSn,countyId);
	}

	/**
	 * 更新费用对应的发票
	 * @param docitemsn
	 * @param invoice_code
	 * @param invoice_id
	 */
	public void updateInvoiceByDocItem(String docitemsn, String invoiceCode,String invoiceBookId,
			String invoiceId,String invoiceMode) throws JDBCException {
		//非套餐缴费
		String sql = "update c_fee f set f.invoice_code=?,f.invoice_id=?,f.invoice_book_id=?,f.invoice_mode=?,is_doc=? " +
				" where f.fee_sn in(select t.fee_sn from c_doc_fee t where t.docitem_sn=?)";
		executeUpdate(sql, invoiceCode, invoiceId, invoiceBookId, invoiceMode,
				SystemConstants.BOOLEAN_TRUE, docitemsn);
		/**
		//套餐缴费
		sql = "update c_fee f set f.invoice_code=?,f.invoice_id=?,f.invoice_book_id=?,f.invoice_mode=?,is_doc=? " + 
	         	" where f.busi_code=? and f.create_done_code in ("+
	         	"      select cpf.done_code from c_prom_fee cpf,c_doc_fee t"+
	         	"      where t.fee_sn=cpf.prom_fee_sn and t.docitem_sn=?)";
		executeUpdate(sql, invoiceCode, invoiceId, invoiceBookId, invoiceMode,
				SystemConstants.BOOLEAN_TRUE, BusiCodeConstants.PROM_ACCT_PAY, docitemsn);
		**/
	}

	/**
	 * 保存发票号
	 * @param feeSn
	 * @param invoiceCode
	 * @param invoiceId
	 */
	public int updateInvoiceByFeeSn(String[] feeSn, String invoiceCode,
			String invoiceId,String invoiceBookId, String invoiceMode) throws JDBCException {
		String sql = "UPDATE c_fee t SET t.invoice_mode=?,t.invoice_code=?,t.invoice_id=?,t.invoice_book_id=?,t.is_doc=?"
				+ " WHERE t.fee_sn in ("+getSqlGenerator().in(feeSn)+") AND t.status=?";
		return executeUpdate(sql, invoiceMode, invoiceCode, invoiceId,invoiceBookId,SystemConstants.BOOLEAN_TRUE,
				StatusConstants.PAY);

	}
	
	public int updateInvoiceByDoneCode(Integer doneCode, String invoiceCode,
			String invoiceId,String invoiceBookId, String invoiceMode) throws JDBCException {
		String sql = "UPDATE c_fee t SET t.invoice_mode=?,t.invoice_code=?,t.invoice_id=?,t.invoice_book_id=?,t.is_doc=?"
				+ " WHERE t.create_done_code=? AND t.status=?";
		return executeUpdate(sql, invoiceMode, invoiceCode, invoiceId,invoiceBookId,SystemConstants.BOOLEAN_TRUE,
				doneCode,StatusConstants.PAY);

	}
	
	public void updateInvoiceByDoneCode(Integer doneCode,
			String invoiceMode) throws JDBCException {
		String sql = "UPDATE c_fee t SET t.invoice_mode=?,t.is_doc=? , t.invoice_fee=t.real_pay WHERE t.create_done_code=? AND t.status=?";
		executeUpdate(sql, invoiceMode,SystemConstants.BOOLEAN_TRUE,doneCode,StatusConstants.PAY);
	}

	public void updateIsDocByDoneCode(Integer doneCode,String stauts) throws JDBCException {
		String sql = "UPDATE c_fee t SET t.is_doc=?  WHERE t.create_done_code=? ";
		executeUpdate(sql,stauts,doneCode);
	}

	
	/**
	 * 查询费用项
	 * @param feeSns
	 * @return
	 */
	public List<CFee> queryByFeeSns(String[] feeSns) throws JDBCException {
		String sql = "select * from c_fee where fee_sn in ("
				+ sqlGenerator.in(feeSns) + ")";
		return createQuery(sql).list();
	}


	/**
	 * 查询账目费用项
	 * @param feeSns
	 * @return
	 */
	public List<CFeeAcct> queryAcctFeeByFeeSns(String[] feeSns) throws JDBCException {
		String sql = "select * from c_fee a,c_fee_acct b where a.fee_sn in ("
				+ sqlGenerator.in(feeSns) + ") and a.fee_sn=b.fee_sn and a.fee_type=?";
		return createQuery(CFeeAcct.class,sql,SystemConstants.FEE_TYPE_ACCT).list();
	}
	/**
	 * 查询账目费用
	 * @param feeSn
	 * @return
	 * @throws JDBCException 
	 */
	public CFeeAcct queryAcctFee(String feeSn) throws JDBCException{
		String sql = "select * from c_fee a,c_fee_acct b where a.fee_sn =? and a.fee_sn=b.fee_sn and a.fee_type=?";
		return createQuery(CFeeAcct.class,sql,feeSn,SystemConstants.FEE_TYPE_ACCT).first();
	}

	/**
	 * 统计所有费用
	 * @param feeSns
	 * @return
	 */
	public Integer sumFeeByFeeSns(String[] feeSns) throws JDBCException {
		String sql = "select sum(real_pay) from c_fee where fee_sn in ("+sqlGenerator.in(feeSns)+")";
		return Integer.parseInt(findUnique(sql));
	}


	//查找用户当天的累积计费记录
	public List<CFee> queryUserFee(String custId ,String userId) throws Exception{
		String sql ="select cust_id,user_id,acct_id,acctitem_id,sum(real_pay) real_pay" +
				" from c_fee " +
				" where fee_type=? " +
				" and status <> ? " +
				" and create_time>=to_date(to_char(sysdate,'yyyymmdd'),'yyyymmdd') " +
				" and (user_id = ? or (cust_id=? and user_id is null))" +
				" group by cust_id,user_id,acct_id,acctitem_id";
		return this.createQuery(CFee.class,sql, SystemConstants.FEE_TYPE_ACCT,StatusConstants.INVALID,userId,custId).list();

	}

	/**
	 * 根据发票号码和Id查询相应记录
	 * @param invoiceCode
	 * @param invoiceId
	 * @return
	 * @throws JDBCException 
	 * @throws Exception
	 */
	public List<CFee> queryFeeByInvoice(String invoiceCode, String invoiceId,String custId) throws JDBCException {
		String sql = StringHelper.append("select invoice_code,invoice_id,invoice_book_id,fee_sn,real_pay, p.acctitem_name fee_name from c_fee c,vew_acctitem p where c.acctitem_id=p.acctitem_id and c.invoice_code = :INVOICECODE and c.invoice_id = :INVOICEID and cust_id=:CUSTID" ,
				"  UNION ALL SELECT invoice_code,invoice_id,invoice_book_id,fee_sn,real_pay, f.fee_name FEE_NAME FROM C_FEE C, t_busi_fee f WHERE C.Fee_Id = f.fee_id AND C.INVOICE_CODE =  :INVOICECODE AND C.INVOICE_ID = :INVOICEID  and cust_id=:CUSTID ",
				" UNION ALL select invoice_code,invoice_id,invoice_book_id,'' fee_sn,sum(real_pay) real_pay, p.acctitem_name fee_name",
				" from c_fee c, vew_acctitem p where c.acctitem_id = p.acctitem_id and c.invoice_code =:INVOICECODE and c.invoice_id = :INVOICEID",
				" and c.busi_code=:UNITPAY group by   invoice_code,invoice_id,invoice_book_id,real_pay, p.acctitem_name");
		
		Map<String,String> params = new HashMap<String, String>();
		params.put("INVOICEID", invoiceId);
		params.put("INVOICECODE", invoiceCode);
		params.put("CUSTID", custId);
		params.put("UNITPAY", BusiCodeConstants.Unit_ACCT_PAY);
		return createNameQuery(sql, params).list();
	}
	
	public List<CFee> queryFeeByInvoice(String invoiceCode, String invoiceId) throws JDBCException {
		String sql = "select c.*,p.acctitem_name fee_name from c_fee c,vew_acctitem p where c.acctitem_id=p.acctitem_id and c.invoice_code = ? and c.invoice_id = ? " +
				"  UNION ALL SELECT C.*, f.fee_name FEE_NAME FROM C_FEE C, t_busi_fee f WHERE C.Fee_Id = f.fee_id AND C.INVOICE_CODE = ? AND C.INVOICE_ID = ?  ";
		return createQuery(sql, invoiceCode,invoiceId,invoiceCode,invoiceId).list();
	}

	public int queryBeforeAllFees(String custId,String userId, String acctItemId,
			Integer days) throws JDBCException {
		String sql = "SELECT SUM(real_pay) FROM c_fee WHERE status<> ? AND acctitem_id=? AND cust_id=? AND user_id=? AND create_time>SYSDATE-?";
		return count(sql, StatusConstants.INVALID, acctItemId,custId,userId,days);
	}

	/**
	 * 更新缴费记录表中的发票号信息
	 * @param oldInvoice
	 * @param newInvoice
	 * @throws JDBCException
	 */
	public void updateInvoice(String newInvoiceCode,String newInvoiceBookId, String newInvoiceId,
			String oldInvoiceCode,String oldInvoiceId) throws JDBCException {
		String sql = "update c_fee set invoice_id=?,invoice_code=?,invoice_book_id=? where invoice_id=? and invoice_code=?";
		executeUpdate(sql,newInvoiceId,newInvoiceCode,newInvoiceBookId,
				oldInvoiceId,oldInvoiceCode);
	}

	public List<String> queryUnPrintCustByOptr(String optrId, String countyId)throws JDBCException {
		String sql = "select ta.cust_no from ( " +
				" select c.cust_no FROM c_fee t,c_cust c,t_pay_type a " +
				" WHERE c.cust_id=t.cust_id AND t.status=? AND a.is_print=? AND t.pay_type=a.pay_type " +
				" AND t.invoice_id IS NULL AND t.optr_id= ? AND T.CREATE_TIME >SYSDATE-7  AND t.real_pay<>0  " +
				" and t.busi_code !=? AND t.county_id=? AND t.county_id=c.county_id  AND t.is_doc=? " +
				" union all select distinct cc.cust_no from c_cust_unit_to_resident ccu,c_cust cc " +
				" where ccu.resident_cust_id in " +
				" (SELECT t.cust_id FROM c_fee t,t_pay_type a  WHERE t.status=? AND a.is_print=? AND t.pay_type=a.pay_type " +
				" AND t.invoice_id IS NULL AND t.optr_id=?  AND T.CREATE_TIME >SYSDATE-7  AND t.real_pay<>0 " +
				" and t.busi_code =? AND t.county_id=?   AND t.is_doc=?) and ccu.unit_cust_id =cc.cust_id ) " +
				" ta GROUP BY ta.cust_no ";
		return findUniques(sql, StatusConstants.PAY,SystemConstants.BOOLEAN_TRUE,optrId,BusiCodeConstants.Unit_ACCT_PAY, 
				countyId, SystemConstants.BOOLEAN_FALSE, StatusConstants.PAY,SystemConstants.BOOLEAN_TRUE,optrId,
				BusiCodeConstants.Unit_ACCT_PAY, countyId, SystemConstants.BOOLEAN_FALSE);
	}
	
	public List<CFee> queryUnPrintFee() throws JDBCException{
		String sql = "select * from c_fee c,t_pay_type t where c.pay_type=t.pay_type and t.is_print=?"
				+ " AND C.status = ? AND C.invoice_id IS NULL AND C.CREATE_TIME > SYSDATE - 30 "
				+ " AND C.real_pay <> 0  AND C.is_doc = ?";
		return createQuery(CFee.class, sql, SystemConstants.BOOLEAN_TRUE,StatusConstants.PAY, SystemConstants.BOOLEAN_FALSE).list();
	}
	
	/**
	 * 查找操作员未打印的费用
	 * @param optrId
	 * @return
	 * @throws JDBCException
	 */
	public List<String> queryUnPrintFeeSns(String optrId) throws JDBCException{
		String sql = "select c.fee_sn from c_fee c,t_pay_type t where c.pay_type=t.pay_type and t.is_print=?"
				+ " AND C.status = ? AND C.invoice_id IS NULL AND C.CREATE_TIME > SYSDATE - 3 "
				+ " AND C.real_pay <> 0  AND C.is_doc = ? and c.optr_id=?";
		return findUniques(sql, SystemConstants.BOOLEAN_TRUE,StatusConstants.PAY, SystemConstants.BOOLEAN_FALSE,optrId);
	}

//	public List<String> queryUnPrintCustByOptr(String optrId,String countyId) throws JDBCException {
//		String sql = "SELECT c.cust_no FROM c_fee t,c_cust c,t_pay_type a " +
//				" WHERE c.cust_id=t.cust_id AND t.status='PAY' AND a.is_print='T' AND t.pay_type=a.pay_type" +
//				" AND t.invoice_id IS NULL AND t.optr_id=? " +
//				" AND T.CREATE_TIME >SYSDATE-7  AND t.real_pay<>0 " +
//				" AND t.county_id=? AND t.county_id=c.county_id  AND t.is_doc=? GROUP BY c.cust_no";
//		return findUniques(sql, optrId,countyId,SystemConstants.BOOLEAN_FALSE);
//	}

	public Integer queryFeeByDate(String userId,String acctItemId, String bDate, String eDate) throws JDBCException {
		String sql = "SELECT sum(t.real_pay) FROM c_fee t WHERE t.user_id=? AND t.acctitem_id=? " +
				" AND t.create_time BETWEEN to_date(?,'yyyymmdd') AND to_date(?,'yyyymmdd') AND t.status<>?";
		return count(sql,userId,acctItemId,bDate,eDate,StatusConstants.INVALID);
	}
	
	 /**
	  * 根据银行流水号和县市，查询CFee
	  * @param startTransCode
	  * @param endTransCode
	  * @param countyId
	  * @return
	  * @throws JDBCException
	  */
	public List<CFee> queryFeeByBankTransCode(String startTransCode,String endTransCode,String countyId) throws JDBCException {
		String sql = " select c.*,p.acctitem_name fee_name from busi.c_fee c,busi.vew_acctitem p ,busi.c_bank_pay cbp "+
					"where c.acctitem_id=p.acctitem_id and  c.create_done_code=cbp.done_code "+
					"and cbp.banklogid>=? and cbp.banklogid<=? and c.county_id=? order by c.acct_date";
		return createQuery(sql, startTransCode,endTransCode,countyId).list();
	}
	
	/**
	 * 客户购买设备费用
	 * @param custId
	 * @param deviceId
	 * @param countyId
	 * @return
	 * @throws Exception
	 */
	public CFee queryDeviceFeeByCustId(String custId,String deviceId,String countyId) throws Exception {
		String sql = "select f.* from c_fee f,c_fee_device fd"+
				" where f.fee_sn=fd.fee_sn"+
				" and f.cust_id=? and f.status=? and f.county_id=? and fd.device_id=? and fd.county_id=?"+
				" order by f.create_time desc";
		return this.createQuery(sql, custId,StatusConstants.PAY,countyId,deviceId,countyId).first();
	}

	public List<CFee> queryContractPay(Integer contractId) throws JDBCException {
		String sql = "select f.* from c_general_contract_pay c,c_fee f where c.contract_id=? and c.done_code=f.create_done_code";
		return createQuery(sql, contractId).list();
	}

	public List<CFee> queryDepositInCust(String custId)throws JDBCException{
		String sql = "SELECT T2.FEE_NAME, SUM(T1.REAL_PAY) REAL_PAY "
				+" FROM C_FEE T1, T_BUSI_FEE T2 WHERE T1.FEE_ID = T2.FEE_ID"
				+"  AND T2.DEPOSIT = 'T' AND t1.cust_id=? GROUP BY T2.FEE_NAME having SUM(T1.REAL_PAY) > 0";
		return createQuery(sql, custId).list();
	}

	public List<CProd> queryProdByDoneCode(Integer doneCode, String countyId) throws JDBCException {
		String sql = "SELECT p.* FROM c_fee f,c_fee_acct a,c_prod p where f.fee_sn=a.fee_sn and  p.prod_sn=a.prod_sn  " +
				"and  f.county_id =?  and p.county_id=? and a.county_id=? and f.create_done_code=?";
		return createQuery(CProd.class,sql, countyId,countyId,countyId,doneCode).list();
	}
	
	public List<CFeeAcct> queryUserUnPayOrderFee(String custId,String[] userIds) throws JDBCException {
		String sql = "select a.*,b.prod_sn from c_fee a,c_fee_acct b where a.fee_sn = b.fee_sn "+
			  "	and b.prod_sn in ( "+
			  "	select order_sn "+
			  "	  from c_prod_order "+
			  "	 where cust_id = ?  "+
			  "	   and user_id in ("+sqlGenerator.in(userIds)+") "+
			  "	   and package_sn is null "+
			  "	union "+
			  "	select package_sn order_sn "+
			  "	  from c_prod_order "+
			  "	 where cust_id = ? "+
			  "	   and user_id in ("+sqlGenerator.in(userIds)+") "+
			  "	   and package_sn is not null) and status='UNPAY'";	
		return createQuery(CFeeAcct.class,sql,custId,custId).list();
	} 
	/**
	 * 提取和工单相关的缴费记录
	 * a类：所有单产品订单（退订） 、 b类：在工单创建之后订购的套餐（套餐退订）
	 * @param custId
	 * @param userIds
	 * @return
	 * @throws JDBCException
	 */
	public List<CFeeAcct> queryTaskUserUnPayCFeeAcct(String custId,String[] userIds,Integer taskDoneCode) throws JDBCException {
		String sql = "select a.*,b.prod_sn from c_fee a,c_fee_acct b where a.fee_sn = b.fee_sn "+
			  "and a.status = 'UNPAY'	and b.prod_sn in ( "+
				  "	select order_sn "+
				  "	  from c_prod_order "+
				  "	 where cust_id = ?  "+
				  "	   and user_id in ("+sqlGenerator.in(userIds)+") "+
				  "	   and package_sn is null "+
				  "	union "+
				  "	select pak.order_sn "+
				  "	  from c_prod_order a,c_prod_order pak "+
				  "	 where a.cust_id = ? "+
				  "	   and a.user_id in ("+sqlGenerator.in(userIds)+") "+
				  "	   and a.package_sn=pak.order_sn and pak.done_code>? )";
		return createQuery(CFeeAcct.class,sql,custId,custId,taskDoneCode).list();
	}
	
	public CFeeAcct queryAcctFeeByOrderSn(String orderSn)throws JDBCException {
		String sql ="select a.* from c_fee a,c_fee_acct b "
				+ " where a.fee_sn=b.fee_sn and b.prod_sn = ?";
		
		return createQuery(CFeeAcct.class,sql,orderSn).list().get(0);
	}


}
