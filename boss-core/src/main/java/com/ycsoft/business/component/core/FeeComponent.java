package com.ycsoft.business.component.core;

import static com.ycsoft.commons.constants.StatusConstants.UNPAY;
import static com.ycsoft.commons.constants.SystemConstants.BOOLEAN_FALSE;
import static com.ycsoft.commons.constants.SystemConstants.BOOLEAN_NO;
import static com.ycsoft.commons.constants.SystemConstants.BOOLEAN_TRUE;
import static com.ycsoft.commons.constants.SystemConstants.FEE_TYPE_ACCT;
import static com.ycsoft.commons.constants.SystemConstants.FEE_TYPE_BUSI;
import static com.ycsoft.commons.constants.SystemConstants.FEE_TYPE_DEVICE;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TBusiFee;
import com.ycsoft.beans.config.TGripData;
import com.ycsoft.beans.config.TGripLog;
import com.ycsoft.beans.core.acct.CAcctAcctitemInactive;
import com.ycsoft.beans.core.acct.CGeneralAcct;
import com.ycsoft.beans.core.acct.CGeneralAcctDedail;
import com.ycsoft.beans.core.acct.CGeneralCredential;
import com.ycsoft.beans.core.bank.CBankPay;
import com.ycsoft.beans.core.bill.BillDto;
import com.ycsoft.beans.core.cust.CCust;
import com.ycsoft.beans.core.fee.CFee;
import com.ycsoft.beans.core.fee.CFeeAcct;
import com.ycsoft.beans.core.fee.CFeeBusi;
import com.ycsoft.beans.core.fee.CFeeDevice;
import com.ycsoft.beans.core.fee.CFeePay;
import com.ycsoft.beans.core.fee.CFeePayDetail;
import com.ycsoft.beans.core.fee.CFeePropChange;
import com.ycsoft.beans.core.fee.CFeeUnitpre;
import com.ycsoft.beans.core.fee.CFeeUnprint;
import com.ycsoft.beans.core.prod.CProd;
import com.ycsoft.beans.core.prod.CProdMobileBill;
import com.ycsoft.beans.core.prod.CProdOrder;
import com.ycsoft.beans.core.prod.CProdOrderDto;
import com.ycsoft.beans.core.prod.CProdOrderFee;
import com.ycsoft.beans.core.prod.CProdOrderFeeOut;
import com.ycsoft.beans.core.promotion.CPromFee;
import com.ycsoft.beans.core.promotion.CPromFeeProd;
import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.beans.core.voucher.CVoucher;
import com.ycsoft.beans.invoice.RInvoice;
import com.ycsoft.business.commons.abstracts.BaseBusiComponent;
import com.ycsoft.business.component.config.ExpressionUtil;
import com.ycsoft.business.dao.config.TBusiFeeDao;
import com.ycsoft.business.dao.config.TBusiFeeStdDao;
import com.ycsoft.business.dao.config.TGripDataDao;
import com.ycsoft.business.dao.config.TGripLogDao;
import com.ycsoft.business.dao.core.acct.CAcctAcctitemInactiveDao;
import com.ycsoft.business.dao.core.acct.CGeneralAcctDao;
import com.ycsoft.business.dao.core.acct.CGeneralAcctDedailDao;
import com.ycsoft.business.dao.core.acct.CGeneralCredentialDao;
import com.ycsoft.business.dao.core.bank.CBankPayDao;
import com.ycsoft.business.dao.core.common.CDoneCodeUnpayDao;
import com.ycsoft.business.dao.core.cust.CCustDao;
import com.ycsoft.business.dao.core.cust.CCustHisDao;
import com.ycsoft.business.dao.core.fee.CFeeAcctDao;
import com.ycsoft.business.dao.core.fee.CFeeBusiDao;
import com.ycsoft.business.dao.core.fee.CFeeDao;
import com.ycsoft.business.dao.core.fee.CFeeDeviceDao;
import com.ycsoft.business.dao.core.fee.CFeePayDao;
import com.ycsoft.business.dao.core.fee.CFeePayDetailDao;
import com.ycsoft.business.dao.core.fee.CFeePropChangeDao;
import com.ycsoft.business.dao.core.fee.CFeeUnitpreDao;
import com.ycsoft.business.dao.core.fee.CFeeUnprintDao;
import com.ycsoft.business.dao.core.print.CDocItemDao;
import com.ycsoft.business.dao.core.print.CInvoiceDao;
import com.ycsoft.business.dao.core.prod.CProdMobileBillDao;
import com.ycsoft.business.dao.core.promotion.CPromFeeDao;
import com.ycsoft.business.dao.core.promotion.CPromFeeProdDao;
import com.ycsoft.business.dao.core.user.CUserDao;
import com.ycsoft.business.dao.core.voucher.CVoucherDao;
import com.ycsoft.business.dto.config.TemplateConfigDto;
import com.ycsoft.business.dto.core.acct.AcctitemDto;
import com.ycsoft.business.dto.core.acct.PayDto;
import com.ycsoft.business.dto.core.fee.BBillPrintDto;
import com.ycsoft.business.dto.core.fee.BbillingcycleCfgDto;
import com.ycsoft.business.dto.core.fee.BusiFeeDto;
import com.ycsoft.business.dto.core.fee.CFeePayDto;
import com.ycsoft.business.dto.core.fee.FeeDto;
import com.ycsoft.business.dto.core.fee.FeePayDto;
import com.ycsoft.business.dto.core.fee.MergeFeeDto;
import com.ycsoft.business.dto.core.fee.MergeFeeFormDto;
import com.ycsoft.business.dto.core.fee.QueryFeeInfo;
import com.ycsoft.business.dto.core.print.InvoiceFromDto;
import com.ycsoft.business.dto.core.print.PrintItemDto;
import com.ycsoft.business.dto.core.prod.PromFeeProdDto;
import com.ycsoft.commons.constants.DataRight;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ComponentException;
import com.ycsoft.commons.exception.ErrorCode;
import com.ycsoft.commons.exception.ServicesException;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.commons.helper.DateHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.store.TemplateConfig.Template;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;
import com.ycsoft.daos.helper.BeanHelper;
/**
 * 处理营业费、设备费、账户缴费
 *
 * @author pyb
 *
 * Mar 18, 2010
 *
 */

@Component
public class FeeComponent extends BaseBusiComponent {

	private static Map<String, List<BusiFeeDto>> busifees = null;
	private TBusiFeeStdDao tBusiFeeStdDao;
	private CFeeBusiDao cFeeBusiDao;
	private CFeeDeviceDao cFeeDeviceDao;
	private CFeeAcctDao cFeeAcctDao;
	private CFeePayDao cFeePayDao;
	private CFeePayDetailDao cFeePayDetailDao;
	private CAcctAcctitemInactiveDao cAcctAcctitemInactiveDao;
	private CInvoiceDao cInvoiceDao;
	private CFeeDao cFeeDao;
	private CFeeUnitpreDao cFeeUnitpreDao;
	private TBusiFeeDao tBusiFeeDao;
	private CBankPayDao cBankPayDao;
	private TGripDataDao tGripDataDao;
	private TGripLogDao tGripLogDao;
	private CDocItemDao cDocItemDao;
	private CUserDao cUserDao;
	private CCustDao cCustDao;
	private CCustHisDao cCustHisDao;
	private CVoucherDao cVoucherDao;
	private CGeneralAcctDao cGeneralAcctDao;
	private CGeneralCredentialDao cGeneralCredentialDao;
	private CFeePropChangeDao cFeePropChangeDao;
	private CProdMobileBillDao cProdMobileBillDao;
	private CGeneralAcctDedailDao cGeneralAcctDedailDao;
	private CPromFeeDao cPromFeeDao;
	private CPromFeeProdDao cPromFeeProdDao;
	
	private ExpressionUtil expressionUtil;
	@Autowired
	private CDoneCodeUnpayDao cDoneCodeUnpayDao;
	@Autowired
	private CFeeUnprintDao cFeeUnprintDao;
	/**
	 * 查询营业员未打印的费用
	 * @param optrId
	 * @return
	 * @throws JDBCException 
	 */
	public List<CFeeUnprint> queryUnPrintByOptr(String optrId) throws JDBCException{
		return cFeeUnprintDao.queryByOptr(optrId);
	}
	
	/**
	 * 查询挂载IP费用的用户费用清单
	 * @param cust_id
	 * @return
	 * @throws Exception
	 */
	public Map<String,BusiFeeDto> queryUserIpAddresFee(String cust_id)throws Exception{
		Map<String,BusiFeeDto> map=new HashMap<String,BusiFeeDto>();
		for(CUser user:cUserDao.queryUserByIpAddressFee(cust_id)){
			int fee_count=0;
			try{
			   fee_count= Integer.valueOf(user.getStr6());
			}catch(Exception e){}
			if(fee_count<=0){
				continue;
			}
			BusiFeeDto busiFee= getBusiFee(user.getStr5());
			if(busiFee==null){
				throw new ComponentException(ErrorCode.TemplateNotConfigBuseFee,user.getStr5());
			}
			busiFee.setFee_count(fee_count);
			List<CProdOrder> orders= cProdOrderDao.queryNotExpAllOrderByUser(user.getUser_id());
			
			if(orders!=null&&orders.size()>0){
				//开始计费日期=上次到期日+1天
				busiFee.setLast_prod_exp(DateHelper.addDate(orders.get(orders.size()-1).getExp_date(),1));
			}else{
				busiFee.setLast_prod_exp(DateHelper.today());
			}
			map.put(user.getUser_id(), busiFee);	
		}
		return map;
	}
	
	/**
	 * 保存订单退款费用信息
	 * @param cancelList
	 * @param cust
	 * @param doneCode
	 * @param busi_code
	 * @throws InvocationTargetException 
	 * @throws Exception 
	 */
	public void saveCancelFee(List<CProdOrderDto> cancelList,List<CProdOrderFeeOut> outList,CCust cust,Integer doneCode,String busi_code) throws Exception{
		//按订单退款
		for(CProdOrderDto order:cancelList){
			if(order.getBalance_cfee()!=null&&order.getBalance_cfee()>0){
				PayDto pay=new PayDto();
				BeanHelper.copyProperties(pay, order);
				pay.setProd_sn(order.getOrder_sn());
				pay.setAcctitem_id(order.getProd_id());
				pay.setInvalid_date(DateHelper.dateToStr(DateHelper.today().before(order.getEff_date())? order.getEff_date():DateHelper.today()));
				pay.setBegin_date(DateHelper.dateToStr(order.getExp_date()));
				pay.setPresent_fee(0);
				pay.setFee(order.getBalance_cfee()*-1);
				CFeeAcct cfeeacct=this.saveAcctFee(cust.getCust_id(), cust.getAddr_id(), pay, doneCode, busi_code, StatusConstants.UNPAY);
				for(CProdOrderFeeOut out:outList){
					if(out.getOrder_sn().equals(order.getOrder_sn())
							&&out.getOutput_type().equals(SystemConstants.ORDER_FEE_TYPE_CFEE)){
						out.setOutput_sn(cfeeacct.getFee_sn());
					}
				}
			}
		}
	}
	/**
	 * 保存订单修改费用缴费信息
	 * @param order
	 * @param payFee
	 * @param cust
	 * @param doneCode
	 * @param busi_code
	 * @return
	 * @throws Exception
	 */
	public String saveOrderEdittoCFee(CProdOrder order,Integer payFee,CCust cust,Integer doneCode,String busi_code) throws Exception{
		PayDto pay=new PayDto();
		BeanHelper.copyProperties(pay, order);
		pay.setProd_sn(order.getOrder_sn());
		pay.setAcctitem_id(order.getProd_id());
		//pay.setInvalid_date(DateHelper.dateToStr(DateHelper.today().before(order.getEff_date())? order.getEff_date():DateHelper.today()));
		//pay.setBegin_date(DateHelper.dateToStr(order.getExp_date()));
		pay.setPresent_fee(0);
		pay.setFee(payFee);
		CFeeAcct cfeeacct=this.saveAcctFee(cust.getCust_id(), cust.getAddr_id(), pay, doneCode, busi_code, StatusConstants.UNPAY);
		return cfeeacct.getFee_sn();
	}
	
	/**
	 * 查询未支付详细信息
	 * @param cust_id
	 * @return
	 * @throws Exception
	 */
	public List<FeeDto> queryUnPay(String cust_id,String optr_id) throws Exception{
		return cFeeDao.queryUnPay(cust_id,optr_id);
	}
	
	
	/**
	 * 查询未支付总额
	 * @param cust_id
	 * @return
	 * @throws Exception
	 */
	public Map<String,Integer> queryUnPaySum(String cust_id,String optr_id) throws Exception{
		return cFeeDao.queryUnPaySum(cust_id,optr_id);
	}
	/**
	 * 查询未支付的费用清单
	 * @param feeSns
	 * @return
	 * @throws Exception
	 */
	public List<FeeDto> queryUnPayFeeDtoByFeeSn(String[] feeSns) throws Exception{
		List<FeeDto> list=new ArrayList<FeeDto>();
		for(String feeSn:feeSns){
			list.add(cFeeDao.queryUnPayFeeDto(feeSn));
		}
		return list;
	}
	/**
	 * 查询支付对应的缴费记录
	 * @param paySn
	 * @return
	 * @throws Exception
	 */
	public List<FeeDto> queryPayFeeDtoByPaySn(String paySn) throws Exception{
		return cFeeDao.queryPayFeeDto(paySn);
	}
	
	public CFeePay queryCFeePayByPaySn(String paySn) throws JDBCException{
		return cFeePayDao.findByKey(paySn);
	}
	/**
	 * 恢复缴费费用记录的未支付状态，支付信息去掉，发票去掉
	 * @param paySn
	 * @throws JDBCException
	 */
	public void updateCFeeToUnPay(String paySn) throws JDBCException{
		cFeeDao.updateCFeeToUnPayByPaySn(paySn);
	}
	/**
	 * 缴费记录更新支付信息,并记录未打印的信息
	 * 状态，发票，打印，支付方式，等等
	 * @param unpayList
	 * @param pay
	 * @throws JDBCException
	 * @throws ServicesException 
	 */
	public void updateCFeeToPay(List<FeeDto> feeList,CFeePayDto pay) throws Exception{
		String isDoc=SystemConstants.BOOLEAN_FALSE;
		if(pay.getFee()==0){//支付金额=0，则发票不需要打印
			isDoc=SystemConstants.BOOLEAN_NO;
		}
		if(StringHelper.isNotEmpty(pay.getInvoice_id())){
			//有发票号码则不需要打印
			isDoc=SystemConstants.BOOLEAN_TRUE;
		}
		
		for(FeeDto fee:feeList){
			if(isDoc.equals(SystemConstants.BOOLEAN_FALSE)){
				//记录未打印的费用信息，用于营业员未打印提示
				cFeeUnprintDao.insertByUnPayDoneCode(fee.getFee_sn(),this.getOptr().getOptr_id());
			}
			String busiOptrId = fee.getBusi_optr_id();
			if(StringHelper.isEmpty(busiOptrId)){
				CCust cust = cCustDao.findByKey(pay.getCust_id());
				busiOptrId = cust.getStr9();
			}
			cFeeDao.updateCFeeToPay(fee.getFee_sn(), busiOptrId, pay,isDoc);
		}
		
		/**
		for(CDoneCodeUnpay un:unpayList){
			if(isDoc.equals(SystemConstants.BOOLEAN_FALSE)){
				//记录未打印的费用信息，用于营业员未打印提示
				cFeeUnprintDao.insertByUnPayDoneCode(un);
			}
			cFeeDao.updateCFeeToPay(un, pay,isDoc);
		}**/
	}
	/**
	 * 取消未支付的缴费记录
	 * @param fee_sn
	 * @throws JDBCException
	 */
	public void deleteUnPayCFee(String fee_sn) throws JDBCException{
		cFeeDao.remove(fee_sn);
		cFeeBusiDao.remove(fee_sn);
		cFeeDeviceDao.remove(fee_sn);
		cFeeAcctDao.remove(fee_sn);
	}
	
	public CVoucher queryVoucherById(String voucherId) throws Exception {
		return cVoucherDao.queryVoucherById(voucherId, getOptr().getCounty_id());
	}
	
	public void saveFeePropChange(List<CFeePropChange> propList) throws Exception {
		for(CFeePropChange prop : propList){
			setBaseInfo(prop);
		}
		cFeePropChangeDao.save(propList.toArray(new CFeePropChange[propList.size()]));
	}
	
	public List<TBusiFee> queryBusiFeeByType(String feeType) throws Exception {
		return tBusiFeeDao.queryBusiFeeByFeeType(feeType);
	}
	
	/**
	 * 查询所有账期
	 * @return
	 * @throws JDBCException
	 */
	public Pager<BbillingcycleCfgDto> queryAllBillingCycleCfg(String query, Integer start, Integer limit) throws Exception {
		return cFeeDao.queryAllBillingCycleCfg(query, start, limit);
	}
	
	public Pager<CPromFee> queryPromFeeByCust(String custId, Integer start, Integer limit) throws Exception {
		return cPromFeeDao.queryPromFeeByCust(custId,getOptr().getCounty_id(), start, limit);
	}
	
	
	/**
	 * 客户套餐信息
	 * @param custId
	 * @return
	 * @throws Exception
	 */
	public List<CPromFee>  queryCustPromFee(String custId)throws Exception{
		List<CPromFee> promFeeList = cPromFeeDao.queryCustPromFee(custId, getOptr().getCounty_id());
		return promFeeList;
	}
	
	public BBillPrintDto queryBillPrint(String custId,String billingCycleId) throws Exception{
		BBillPrintDto billPrintDto = cFeeDao.queryBillPrint(custId, billingCycleId);
		List<BillDto> billList = cFeeDao.queryFeeInfoByCustId(custId, billingCycleId);
		if(billPrintDto == null){
			billPrintDto = new BBillPrintDto();
		}
		List<BillDto> billList2 = cFeeDao.queryPublicFeeInfoByCustId(custId, billingCycleId);
		List<BillDto> list = new ArrayList<BillDto>();
		list.addAll(billList);
		list.addAll(billList2);
		billPrintDto.setBillList(list);
		return billPrintDto;
	}
	
	public BBillPrintDto queryPromPrint(String custId,String promFeeSn) throws Exception{
		BBillPrintDto billPrintDto = cFeeDao.queryPromPrint(custId, promFeeSn,getOptr().getCounty_id());
		List<BillDto> billList = cFeeDao.queryPromInfoByCustId(custId, promFeeSn,getOptr().getCounty_id());
		if(billPrintDto == null){
			billPrintDto = new BBillPrintDto();
		}
		billPrintDto.setBillList(billList);
		return billPrintDto;
	}
	
	
	/**
	 * 查询当前操作员，上线后未打印发票的客户编号
	 * @return
	 */
	public List<String> queryUnPrintCustByOptr() throws Exception {
		return cFeeDao.queryUnPrintCustByOptr(getOptr().getOptr_id(),getOptr().getCounty_id());
	}
	
	public List<String> queryUnPrintFeeByOptr(String optrId) throws Exception{
		return cFeeDao.queryUnPrintFeeSns(optrId);
	}
	
	/**
	 * @param gripDataDao the tGripDataDao to set
	 */
	public void setTGripDataDao(TGripDataDao gripDataDao) {
		tGripDataDao = gripDataDao;
	}
	
	public void setTGripLogDao(TGripLogDao gripLogDao) {
		tGripLogDao = gripLogDao;
	}

	/**
	 * 根据用户类型查询一次性费用信息
	 * @param feeType 预收费
	 * @return
	 * @throws Exception
	 */
	public List<TBusiFee> queryUnitpreBusiFee() throws Exception {
		List<TBusiFee> list= tBusiFeeDao.queryBusiFeeByFeeType(SystemConstants.FEE_TYPE_UNITPRE);
		list.addAll(tBusiFeeDao.queryBusiFeeByFeeType(SystemConstants.FEE_TYPE_UNBUSI));
		return list;

	}
	
	private void setBusiOptrId(CFee fee, String custId) throws Exception {
		CCust cust = cCustDao.findByKey(custId);
		if(cust != null){
			fee.setBusi_optr_id(cust.getStr9());
		}
	}

	/**
	 * 预付款,充值卡
	 * @param dto
	 * @param optr
	 * @param custId
	 * @param done_code
	 * @param busiCode
	 * @throws Exception
	 */
	public CFee saveFeeUnitpre(String payType,String feeId,String feeType,Integer realPay,
			Integer createDoneCode,Integer busiDoneCode,String busiCode,String isDoc,String addrId) throws Exception {

		CFee fee = new CFee();
		fee.setFee_sn(gFeeSn());
		
		fee.setFee_id(feeId);
		fee.setFee_type(feeType);//预付款
		fee.setBusi_done_code(busiDoneCode);
		fee.setCreate_done_code(createDoneCode);
		fee.setBusi_code(busiCode);
		fee.setIs_doc(isDoc);//未打印
		fee.setStatus(StatusConstants.PAY);
		fee.setShould_pay(realPay);
		fee.setReal_pay(realPay);
		fee.setCounty_id(getOptr().getCounty_id());
		fee.setOptr_id(getOptr().getOptr_id());
		fee.setDept_id(getOptr().getDept_id());
		fee.setArea_id(getOptr().getArea_id());
		fee.setPay_type(payType);
		fee.setAddr_id(addrId);
		cFeeDao.save(fee);

		return fee;
	}
	
	public CFee saveFeeUnitpre(String payType,String feeId,String feeType,Integer realPay, Integer createDoneCode,Integer busiDoneCode,
			String busiCode,String isDoc,String addrId, String paySn, String custId,RInvoice invoice) throws Exception {
		CFee fee = new CFee();
		fee.setFee_sn(gFeeSn());
		
		fee.setFee_id(feeId);
		fee.setFee_type(feeType);//预付款
		fee.setBusi_done_code(busiDoneCode);
		fee.setCreate_done_code(createDoneCode);
		fee.setBusi_code(busiCode);
		fee.setIs_doc(isDoc);//未打印
		fee.setStatus(StatusConstants.PAY);
		fee.setShould_pay(realPay);
		fee.setReal_pay(realPay);
		fee.setCounty_id(getOptr().getCounty_id());
		fee.setOptr_id(getOptr().getOptr_id());
		fee.setDept_id(getOptr().getDept_id());
		fee.setArea_id(getOptr().getArea_id());
		fee.setPay_type(payType);
		fee.setAddr_id(addrId);
		
		fee.setPay_sn(paySn);
		fee.setCust_id(custId);
		fee.setInvoice_id(invoice.getInvoice_id());
		fee.setInvoice_code(invoice.getInvoice_code());
		fee.setInvoice_book_id(invoice.getInvoice_book_id());
		fee.setInvoice_mode(invoice.getInvoice_mode());
		setBusiOptrId(fee, custId);
		cFeeDao.save(fee);

		return fee;
	}
	
	public void cancelPromFee(Integer doneCode,String promFeeSn, String custId) throws Exception{
		CPromFee bean = cPromFeeDao.findByKey(promFeeSn);
		List<CAcctAcctitemInactive> list = cAcctAcctitemInactiveDao.queryByPromDoneCode(bean.getDone_code(),custId,false);
		bean.setStatus(StatusConstants.INVALID);
		bean.setCancel_done_code(doneCode);
		List<AcctitemDto> items = cAcctAcctitemDao.queryByCustId(custId, getOptr().getCounty_id());
		Map<String, AcctitemDto> map = CollectionHelper.converToMapSingle(items, "acct_id","acctitem_id");
		for(CAcctAcctitemInactive cai:list){
			AcctitemDto dto = map.get(cai.getAcct_id() + "_" + cai.getAcctitem_id());
			if(dto!=null){
				cAcctAcctitemDao.updateInActiveBanlance(dto.getAcct_id(),dto.getAcctitem_id(),getOptr().getCounty_id(),dto.getInactive_balance() - cai.getBalance());
				cAcctAcctitemInactiveDao.removeByPromDoneCodeWithHis(cai.getDone_code(), doneCode);
			}
		}
		cPromFeeDao.update(bean);
	}
	
	/**
	 * 保存促销缴费信息
	 * @param doneCode
	 * @param promFeeId
	 * @param promFee
	 */
	public CPromFee savePromPay(Integer doneCode,String custId ,String promFeeId, int promFee, List<PromFeeProdDto> prodList) throws Exception {
		CPromFee cPromFee = new CPromFee();
		cPromFee.setProm_fee_sn(gPromFeeSn());
		cPromFee.setProm_fee_id(promFeeId);
		cPromFee.setStatus(StatusConstants.ACTIVE);
		cPromFee.setCust_id(custId);
		cPromFee.setArea_id(getOptr().getArea_id());
		cPromFee.setCounty_id(getOptr().getCounty_id());
		cPromFee.setDone_code(doneCode);
		cPromFeeDao.save(cPromFee);
		return cPromFee;
	}
	/**
	 * 保存套餐缴费明细
	 * @param cPromFee
	 * @param prodList
	 * @throws Exception
	 */
	public void savePromPayProds(CPromFee cPromFee,List<PromFeeProdDto> prodList)throws Exception{
		List<CPromFeeProd> promProdList = new ArrayList<CPromFeeProd>();
		for(PromFeeProdDto dto : prodList){
			CPromFeeProd promProd = new CPromFeeProd();
			promProd.setProm_fee_sn(cPromFee.getProm_fee_sn());
			BeanUtils.copyProperties(dto, promProd);
			promProdList.add(promProd);
		}
		cPromFeeProdDao.save(promProdList.toArray(new CPromFeeProd[promProdList.size()]));
	}

	

	/**
	 * @return
	 */
	private String gPromFeeSn() throws JDBCException {
		return cPromFeeDao.findSequence().toString();
	}

	/**
	 * 查询同一地区类的所有预付款
	 * @param countyId
	 * @return
	 * @throws Exception
	 */
	public List<CFeeUnitpre> queryFeeUnitpre(String countyId) throws Exception {
		return cFeeUnitpreDao.queryFeeUnitpre(countyId);
	}

	/**
	 * @param busifees the busifees to set
	 */
	public static void setBusifees(Map<String, List<BusiFeeDto>> busifees) {
		FeeComponent.busifees = busifees;
	}
	/**
	 * 生成收费sn
	 * @return
	 * @throws Exception
	 */
	public String gFeeSn() throws Exception{
		return cFeeBusiDao.findSequence().toString();
	}
	/**
	 * 生成付款sn
	 * @return
	 * @throws Exception
	 */
	public String gPaySn() throws Exception{
		return cFeePayDao.findSequence().toString();
	}

	/**
	 * 保存服务费信息
	 * @param fees 费用项,必须包含的参数除了 "value_id" 字段,其它均需要设置
	 * @param doneCode 流水号
	 * @param busiCode 当前的业务代码
	 * @param custId 客户编号
	 * @param currentBusiUser 用户信息（只针对用户业务有效，其他情况传NULL）
	 */
	public String saveBusiFee(String custId,String addr_id,String feeId,int feeCount,String payType,
			int realPay ,Integer createDoneCode,Integer busiDoneCode,
			String busiCode, List<CUser> userList,String disctInfo) throws Exception{
		CFee fee = new CFee();
		fee.setCust_id(custId);
		fee.setFee_id(feeId);
		fee.setShould_pay(realPay);
		fee.setReal_pay(realPay);
		if(feeCount != 0)
		fee.setCount(feeCount);
		fee.setFee_type(SystemConstants.FEE_TYPE_BUSI);
		fee.setPay_type(payType);
		fee.setAddr_id(addr_id);
		fee.setDisct_info(disctInfo);
		setBaseCFee(fee, custId, createDoneCode, busiDoneCode, busiCode);
		setBusiOptrId(fee, custId);
		cFeeDao.save(fee);
		

		CFeeBusi feeBusi = new CFeeBusi();
		feeBusi.setFee_sn(fee.getFee_sn());
		feeBusi.setArea_id(getOptr().getArea_id());
		feeBusi.setCounty_id(getOptr().getCounty_id());
		setUserType(userList, feeBusi);
		feeBusi.setAddr_id(addr_id);
		cFeeBusiDao.save(feeBusi);

		return fee.getFee_sn();
	}

	/**
	 * 保存业务费用
	 * @param custId
	 * @param feeId
	 * @param realPay
	 * @param doneCode
	 * @param busiCode
	 * @param feeBusi
	 * @return
	 * @throws Exception
	 */
	public String saveBusiFee(String custId,String addrId,String feeId,
			int realPay ,Integer createDoneCode,Integer busiDoneCode,
			String busiCode, CFeeBusi feeBusi) throws Exception{
		CFee fee = new CFee();
		fee.setCust_id(custId);
		fee.setFee_id(feeId);
		fee.setShould_pay(realPay);
		fee.setReal_pay(realPay);
		fee.setFee_type(SystemConstants.FEE_TYPE_BUSI);
		fee.setAcct_id(addrId);
		setBaseCFee(fee, custId, createDoneCode,busiDoneCode, busiCode);
		setBusiOptrId(fee, custId);
		cFeeDao.save( fee);
		

		feeBusi.setFee_sn(fee.getFee_sn());
		feeBusi.setArea_id(getOptr().getArea_id());
		feeBusi.setCounty_id(getOptr().getCounty_id());

		cFeeBusiDao.save(feeBusi);

		return fee.getFee_sn();
	}

	/**
	 * 保存设备费用
	 * @param custId
	 * @param feeId
	 * @param deviceType
	 * @param deviceId
	 * @param pairCardId
	 * @param realPay
	 * @param doneCode
	 * @param busiCode
	 * @param buyNum 购买数量
	 * @return
	 * @throws Exception
	 */
	public String saveDeviceFee(String custId,String addrId, String feeId, String feeStdId,
			String payType, String deviceType, String deviceId,
			String deviceCode, String pairCardId, String pairCardCode,
			String pairModemId, String pairModemCode,String deviceModel, int realPay,
			Integer createDoneCode, Integer busiDoneCode, String busiCode, Integer buyNum)
			throws Exception {
		CFee fee = new CFee();
		fee.setCust_id(custId);
		fee.setFee_id(feeId);
		fee.setShould_pay(realPay);
		fee.setReal_pay(realPay);
		fee.setFee_type(SystemConstants.FEE_TYPE_DEVICE);
		fee.setPay_type(payType);
		fee.setAddr_id(addrId);
		setBaseCFee(fee, custId,createDoneCode, busiDoneCode, busiCode);
		setBusiOptrId(fee, custId);
		cFeeDao.save( fee);
		
		//保存设备信息
		CFeeDevice feeDevice = new CFeeDevice();
		feeDevice.setFee_sn(fee.getFee_sn());
		feeDevice.setDevice_type(deviceType);
		feeDevice.setDevice_id(deviceId);
		feeDevice.setFee_std_id(feeStdId);
		feeDevice.setDevice_code(deviceCode);
		feeDevice.setPair_card_id(pairCardId);
		feeDevice.setPair_card_code(pairCardCode);
		feeDevice.setPair_modem_id(pairModemId);
		feeDevice.setPair_modem_code(pairModemCode);
		feeDevice.setArea_id(getOptr().getArea_id());
		feeDevice.setCounty_id(getOptr().getCounty_id());
		feeDevice.setBuy_num(buyNum);
		feeDevice.setDevice_model(deviceModel);

		cFeeDeviceDao.save(feeDevice);
		return fee.getFee_sn();
	}
	
	/**
	 * 保存分公司账户费用
	 * @param realPay
	 * @param createDoneCode
	 * @param busiDoneCode
	 * @param busiCode
	 * @throws Exception
	 */
	public void saveGeneralAcctFee(String gAcctId,int changeFee ,Integer doneCode,String countyId) throws Exception{
		CGeneralAcctDedail detail = new CGeneralAcctDedail();
		detail.setG_acct_id(gAcctId);
		detail.setChange_fee(changeFee);
		detail.setDone_code(doneCode);
		detail.setCounty_id(countyId);
		cGeneralAcctDedailDao.save(detail);
	}

	/**
	 * 保存预存费用，
	 * fees需要封装的参数: cust_id、acctitem_id、acct_id、 user_id、 real_pay
	 * 如果是客户的公用账户则，user_id 可以为NULL
	 * @param fees 费用项
	 * @param custId 客户id
	 * @param doneCode 流水号
	 * @param busiCode 业务代码
	 */
	public CFeeAcct saveAcctFee(String custId,String addrId,PayDto pay,Integer doneCode, String busiCode,String payType) throws Exception{
		if (pay.getFee() != 0 || pay.getPresent_fee() != 0) {
			CFee fee = new CFee();
		
			fee.setCust_id(pay.getCust_id());
			fee.setUser_id(pay.getUser_id());
			fee.setAcct_id(pay.getAcct_id());
			fee.setAcctitem_id(pay.getAcctitem_id());
			fee.setShould_pay(pay.getFee()+pay.getPresent_fee());
			fee.setReal_pay(pay.getFee());
			fee.setFee_type(SystemConstants.FEE_TYPE_ACCT);
			fee.setPay_type(payType);
			fee.setAddr_id(addrId);
			setBaseCFee(fee, custId, doneCode, doneCode, busiCode);
			if(SystemConstants.PAY_TYPE_UNPAY.equals(payType)){
				fee.setStatus(payType);
			}
			setBusiOptrId(fee, custId);
			cFeeDao.save(fee);
			
			
			CFeeAcct feeAcct = new CFeeAcct();
			BeanUtils.copyProperties(fee,feeAcct);
			feeAcct.setDisct_id(pay.getDisct_id());
			feeAcct.setProd_sn(pay.getProd_sn());
			feeAcct.setTariff_id(pay.getTariff_id());
			if (StringHelper.isNotEmpty(pay.getBegin_date())){
				feeAcct.setBegin_date(DateHelper.strToDate(pay.getBegin_date()));
			}
			
			if (StringHelper.isNotEmpty(pay.getInvalid_date())){
				feeAcct.setProd_invalid_date(DateHelper.strToDate(pay.getInvalid_date()));
			}
			cFeeAcctDao.save(feeAcct);
			
			return feeAcct;
		}
		return null;
	}

	/**
	 * 保存费用优惠
	 * @param feeSn
	 * @param disctType
	 * @param disctInfo
	 * @param promotionSn
	 * @param disctFee
	 * @throws Exception
	 */
	public void saveDisctFee(String feeSn,String disctType,String disctInfo,String promotionSn,int disctFee) throws Exception{
		CFee fee = new CFee();
		fee.setFee_sn(feeSn);
		fee.setDisct_type(disctType);
		fee.setDisct_info(disctInfo);
		fee.setPromotion_sn(promotionSn);
		fee.setReal_pay(disctFee);

		cFeeDao.update(fee);

	}
	/**
	 * 取消费用
	 * 	对未付费的进行取消
	 * @throws Exception
	 */
	public void saveCancelFee(CFee fee,Integer doneCode) throws JDBCException{
		cFeeDao.saveCancelFee(fee.getFee_sn(), StatusConstants.INVALID,doneCode);
		if (StringHelper.isNotEmpty(fee.getInvoice_id()))
			cancelInvoice(fee);
	}
	
	public void saveCancelPayFee(String paySn, Integer doneCode) throws Exception {
		CFeePay pay = new CFeePay();
		pay.setPay_sn(paySn);
		pay.setReverse_done_code(doneCode);
		pay.setIs_valid(SystemConstants.BOOLEAN_FALSE);
		cFeePayDao.update(pay);
	}
	/**
	 * 取消费用
	 * 判断是否删除未支付业务
	 * @param fee_sn
	 * @param doneCode
	 */
	public void saveCancelFeeUnPay(CFee fee,Integer doneCode)throws Exception{
		cFeeDao.saveCancelFee(fee.getFee_sn(), StatusConstants.INVALID,doneCode);
		//检查费用创建流水号是否还存在未支付费用
		List<CFee> list=cFeeDao.queryUnPayByDoneCode(fee.getCreate_done_code());
		if(list==null||list.size()==0){
			//不存在未支付则删除未支付业务流水号
			cDoneCodeUnpayDao.remove(fee.getCreate_done_code());
		}
	}

	/**
	 * 取消费用的相关发票
	 * 手工票
	 *		取消当前业务的发票，从发票总额中减去当前业务的发票额，
	 *		如果减完后为总额为0，则修改发票状态为空闲
	 * 
	 * 机打票
	 *		取消当前业务的发票，并修改该发票的所有付费的发票状态为未打印
	 *		将该发票状态的改为失效
	 * @param fee
	 */
	private void cancelInvoice(CFee fee) throws JDBCException {
		if(fee.getInvoice_mode().equals(SystemConstants.INVOICE_MODE_MANUAL)){
			cInvoiceDao.cancelManualInvoice(fee);
		}else{
			cInvoiceDao.cancelAutoInvoice(fee);
		}
	}

	/**
	 * 修改为押金已退状态
	 * @param feeSn
	 * @throws Exception
	 */
	public void updateFeeDepositStatus(String feeSn) throws JDBCException{
		cFeeDao.updateStatus(feeSn, StatusConstants.DEPOSIT);
	}

	/**
	 * 修改为支付状态
	 * @param feeSn
	 * @throws Exception
	 */
	public void updateFeePayStatus(String feeSn) throws Exception{
		cFeeDao.updateStatus(feeSn, StatusConstants.PAY);
	}


	public void cancelDisct(String promotionSn) throws Exception{
		cFeeDao.cancelDisct(promotionSn,getOptr().getCounty_id());
	}

	/**
	 * 保存支付信息及明细
	 * @param pay 支付信息
	 * @param doneCode 业务流水号
	 * @throws Exception
	 */
	public void savePayFee(CFeePayDto pay, String custId,Integer doneCode) throws Exception {
		if (null == pay || doneCode == null)
			return;
		this.savePayFee(pay, cFeePayDao.queryFeeSn(doneCode, getOptr()
				.getCounty_id()),custId, doneCode);
	}
	
	/**
	 * 柬埔寨保存支付信息
	 * @param pay
	 * @param custId
	 * @param doneCode
	 * @return
	 * @throws Exception
	 */
	public CFeePayDto savePayFeeNew(CFeePayDto pay,String custId,Integer doneCode) throws Exception{
		setBaseInfo(pay);
		pay.setPay_sn(gPaySn());
		pay.setCreate_time(new Date());
		pay.setIs_valid(BOOLEAN_TRUE);
		pay.setDone_code(doneCode);
		pay.setCust_id(custId);
		if ("M".equals(pay.getInvoice_mode())) {
			pay.setInvoice_id("xs" + doneCode);
			pay.setInvoice_code("xs" + doneCode);
			pay.setInvoice_book_id("xs" + doneCode);
		}else{
			if(StringHelper.isNotEmpty(pay.getInvoice_id())){
				pay.setInvoice_book_id(pay.getInvoice_id());
				pay.setInvoice_code(pay.getInvoice_code());
			}
		}
		// 保存支付信息
		cFeePayDao.save(pay);
		return pay;
	}
	/**
	 * 保存支付信息及明细
	 * @param pay 必须设置的参数pay_type、receipt_id、payer、remark
	 * @param feeSn 费用feesn
	 * @param doneCode 业务流水
	 */
	public void savePayFee(CFeePayDto pay , String[] feeSn,String custId, Integer doneCode )throws Exception{
		if(null == pay || null == feeSn || 0 == feeSn.length) return ;
		//设置操作员信息
		setBaseInfo(pay);
		pay.setPay_sn(gPaySn());
		pay.setCreate_time(new Date());
		pay.setIs_valid(BOOLEAN_TRUE);
		pay.setDone_code(doneCode);
		pay.setCust_id(custId);
		if ("M".equals(pay.getInvoice_mode())) {
			pay.setInvoice_id("xs" + doneCode);
			pay.setInvoice_code("xs" + doneCode);
			pay.setInvoice_book_id("xs" + doneCode);
		}
		// 保存支付信息
		cFeePayDao.save(pay);

		if (pay.getPay_type().equals(SystemConstants.PAY_TYPE_UNITPRE)) {
			// 预缴费
			CGeneralCredential credential = cGeneralCredentialDao.queryCredentialById(pay.getReceipt_id(),getOptr().getCounty_id());
			if(null == credential){
				throw new ComponentException("凭据号有误，请重新输入");
			}
			
			if(credential.getBalance() < pay.getFee()){
				throw new ComponentException("凭据号对应余额不足，只剩"+credential.getBalance()/100);
			}
			
			cGeneralCredentialDao.updateCredentialByNo(pay.getFee(), credential.getCredential_no());
			
			// 预缴费
//			CFeeUnitpre feeUnitpre = cFeeUnitpreDao.queryByPrefeeNo(pay
//					.getReceipt_id());
//			if (feeUnitpre == null)
//				throw new ComponentException("错误的预缴费单号：" + pay.getReceipt_id());
//			Integer allFees = cFeeDao.sumFeeByFeeSns(feeSn);
//			if (feeUnitpre.getUsed() + allFees > feeUnitpre.getReal_pay()) {
//				throw new ComponentException("预缴费余额不足，余额："
//						+ (feeUnitpre.getReal_pay() - feeUnitpre.getUsed())
//						/ 100);
//			}
//			cFeeUnitpreDao.saveUnitPreFee(pay.getReceipt_id(), allFees);
			
		}else if (pay.getPay_type().equals(SystemConstants.PAY_TYPE_DJQ)) {
			// 代金券
			CVoucher voucher = cVoucherDao.findByKey(pay.getReceipt_id());
			if (voucher == null)
				throw new ComponentException("错误的代金券号：" + pay.getReceipt_id());
			if(voucher.getStatus().equals(StatusConstants.USE)){
				throw new ComponentException("该代金券已经使用");
			}
			if(voucher.getInvalid_time() != null && (new Date()).after(voucher.getInvalid_time())){
				throw new ComponentException("该代金券已经失效");
			}
			if(voucher.getStatus().equals(StatusConstants.INVALID)){
				throw new ComponentException("该代金券已经失效");
			}
			if(!voucher.getFor_county_id().equals(getOptr().getCounty_id())){
				throw new ComponentException("当前操作员不能使用非本地区的代金券");
			}
			if (pay.getFee() > voucher.getVoucher_value()) {
				throw new ComponentException("该代金券面额:"+ voucher.getVoucher_value()/100+"元，不足以缴纳当前费用:"+pay.getFee()/100+"元");
			}
			String queryVoucherRule = cVoucherDao.queryVoucherRule(voucher.getVoucher_type());
			if (StringHelper.isNotEmpty(queryVoucherRule)){
				CPromFee cpromfee = cPromFeeDao.queryPromFeeByDoneCode(doneCode,getOptr().getCounty_id());
				if (cpromfee != null)
					expressionUtil.setVariable("promfeeid",cpromfee.getProm_fee_id());
				else{
					List<CProd> prods = cFeeDao.queryProdByDoneCode(doneCode, getOptr().getCounty_id());
					if(prods.size()!=1)
						throw new ComponentException("只能为一个产品缴费");
					expressionUtil.setVariable("prodid",prods.get(0).getProd_id());
					expressionUtil.setCuserStb(cUserDao.queryUserStbByUserId(prods.get(0).getUser_id()));
				}
				try{
					if(!expressionUtil.parseBoolean(queryVoucherRule))
						throw new ComponentException("产品不能使用该代金券");
				}catch (ComponentException e) {
					throw e;
				}catch (Exception e) {
					throw new ComponentException("表达式解析错误，不能使用该代金券"+e.getMessage());
				}
			}
			voucher.setUsed_money(pay.getFee());
			voucher.setUnused_money(voucher.getVoucher_value()-pay.getFee());
			voucher.setStatus(StatusConstants.USE);
			voucher.setStatus_time(DateHelper.now());
			voucher.setUsed_time(DateHelper.now());
			voucher.setOptr_id(getOptr().getOptr_id());
			voucher.setCust_id(custId);
			cVoucherDao.update(voucher);
		}else if(SystemConstants.PAY_TYPE_PRESENT.equals(pay.getPay_type())){//如果赠送
			String needAcct = queryTemplateConfig(Template.GENERAL_ACCT_FLAG.toString());
			if(SystemConstants.BOOLEAN_TRUE.equals(needAcct)){
				updateGeneralAcct(pay.getFee());
			}
		}
		
		//费用明细
		CFeePayDetail[] details = new CFeePayDetail[feeSn.length];
		CFeePayDetail tPayDetail = null;
		List<CFeePropChange> feePropList = new ArrayList<CFeePropChange>();
		for (int i=0; i<  feeSn.length ; i++) {
			String fsn = feeSn[i];
			tPayDetail = new CFeePayDetail();
			tPayDetail.setPay_sn( pay.getPay_sn() );
			tPayDetail.setFee_sn( fsn );

			details[i] = tPayDetail;
			
			CFee fee = cFeeDao.findByKey(fsn);
			//支付时，修改账务日期
			if (pay.getAcct_date() != null && fee.getAcct_date() != null
					&& !DateHelper.dateToStr(fee.getAcct_date()).equals(
							DateHelper.dateToStr(pay.getAcct_date()))) {
				CFeePropChange prop = new CFeePropChange();
				prop.setFee_sn(fsn);
				prop.setColumn_name("acct_date");
				prop.setOld_value(DateHelper.dateToStr(fee.getAcct_date()));
				prop.setNew_value(DateHelper.dateToStr(pay.getAcct_date()));
				prop.setDone_code(doneCode);
				feePropList.add(prop);
			}
		}
		//保存明细
		cFeePayDetailDao.save( details );
		//修改费用信息
		CCust cust = cCustDao.findByKey(custId);
		//默认所有产生费用 使用客户发展人id
		cFeeDao.updatePay(feeSn, pay.getAcct_date(), cust.getStr9());
		
		if(feePropList.size() > 0){
			this.saveFeePropChange(feePropList);
		}
	}

	/**
	 * @param fee_sn
	 */
	public void removeFee(String feeSn) throws Exception{
		cFeeDao.remove(feeSn);
	}
	
	public void updateFees(List<CFee> feeList) throws Exception {
		cFeeDao.update(feeList.toArray(new CFee[feeList.size()]));
	}
	
	public void saveMoblieBill(List<CProdMobileBill> mobileBillList) throws JDBCException{
		cProdMobileBillDao.save(mobileBillList.toArray(new CProdMobileBill[mobileBillList.size()]));
	}
	
	/**
	 * 将对应参数设置到baseFee中
	 */
	private void setBaseCFee( CFee tFee,String custId ,Integer createDoneCode,Integer busiDoneCode, String busiCode )throws Exception{
		tFee.setFee_sn(gFeeSn());
		tFee.setCust_id( custId );
		//新保存的费用，都是未支付状态
		if(SystemConstants.PAY_TYPE_UNPAY.equals(tFee.getPay_type())){
			tFee.setStatus( StatusConstants.UNPAY );
		}
		
		//支付方式为银行代扣默认为不打印，其他支付方式为未打印
		if (tFee.getPay_type().equals(SystemConstants.PAY_TYPE_BANK_DEDU))
			tFee.setIs_doc( BOOLEAN_NO );
		else
			tFee.setIs_doc( BOOLEAN_FALSE );
		
		tFee.setBusi_done_code(busiDoneCode);
		tFee.setCreate_done_code( createDoneCode );
		tFee.setBusi_code(busiCode);
//		tFee.setCreate_time( new Date() );
		//操作员信息
		setBaseInfo( tFee );

//		if (tFee.getReal_pay()<0){
//			tFee.setPay_type(SystemConstants.PAY_TYPE_CASH);
//			tFee.setStatus(StatusConstants.PAY);
//		}
	}
	
	/**
	 * 查询客户预存费用
	 * @param custId
	 * @return
	 * 20130710 by wang  0001317 冲正做限制，按流水冲正，排除状态是失效的、支付方式是赠送的
	 */
	public Pager<FeeDto> queryAcctPayFee(String custId, QueryFeeInfo queryFeeInfo, Integer start, Integer limit) throws Exception{
		String dataRight = null;
		try {
			dataRight = queryDataRightCon(getOptr(), DataRight.ACCTDATE_EDIT.toString());
		} catch (Exception e) {
		}
		//允许冲正的最大create_done_code，排除状态失效的、支付方式赠送的；
		//String maxDoneCode = cFeePayDao.queryMaxDoneCode(custId,  getOptr().getCounty_id());
		Pager<FeeDto> feeList = cFeePayDao.queryAcctPayFee(custId, queryFeeInfo, getOptr().getCounty_id(),start,limit);
		if(dataRight != null){
			List<FeeDto> list = feeList.getRecords();
			for(FeeDto fee : list){
				fee.setAllow_done_code("F");
				fee.setData_right(dataRight);
			}
		}
		return feeList;
	}
	
	public List<FeeDto> queryUnitPayFee(String custId) throws Exception{
		String dataRight = null;
		try {
			dataRight = queryDataRightCon(getOptr(), DataRight.ACCTDATE_EDIT.toString());
		} catch (Exception e) {
		}
		List<FeeDto> feeList = cFeePayDao.queryUnitPayFee(custId, getOptr().getCounty_id());
		if(dataRight != null){
			for(FeeDto fee : feeList){
				fee.setData_right(dataRight);
			}
		}
		return feeList;
	}
	
	public Pager<FeeDto> queryAcctPayFeeHis(String custId, QueryFeeInfo queryFeeInfo,Integer start,Integer limit) throws Exception {
		String dataRight = null;
		try {
			dataRight = queryDataRightCon(getOptr(), DataRight.ACCTDATE_EDIT.toString());
		} catch (Exception e) {
		}
		Pager<FeeDto> feeList = cFeePayDao.queryAcctPayFeeHis(custId, queryFeeInfo, getOptr().getCounty_id(),start,limit);
		if(dataRight != null){
			List<FeeDto> list = feeList.getRecords();
			for(FeeDto fee : list){
				fee.setData_right(dataRight);
			}
		}
		return feeList;
	}
	
	/**
	 * 查询客户预存费用(批量)
	 * @param custId
	 * @return
	 */
	public List<FeeDto> queryBatchAcctPayFee(String beginOptrateDate,
			String endOptrateDate, String beginAcctDate, String endAcctDate,
			String optrId, String feeType, String deptId, String custNo,
			String beginInvoice, String endInvoice, String countyId)
			throws Exception {
		//没有权限，不能修改，抛出异常
		String dataRight = queryDataRightCon(getOptr(), DataRight.ACCTDATE_EDIT.toString());
		return cFeePayDao.queryBatchAcctPayFee(beginOptrateDate,
				endOptrateDate, beginAcctDate, endAcctDate,
				optrId, feeType, deptId,custNo,beginInvoice,endInvoice,countyId,dataRight);
	}
	
	/**
	 * 查询客户业务费用(批量)
	 * @param custId
	 * @return
	 */
	public List<FeeDto> queryBatchBusiPayFee(String beginOptrateDate,
			String endOptrateDate, String beginAcctDate, String endAcctDate,
			String optrId, String feeType, String deptId, String custNo,
			String beginInvoice, String endInvoice, String countyId)
			throws Exception {
		String dataRight = queryDataRightCon(getOptr(), DataRight.ACCTDATE_EDIT.toString());
		return cFeePayDao.queryBatchBusiPayFee(beginOptrateDate,
				endOptrateDate, beginAcctDate, endAcctDate,
				optrId, feeType, deptId,custNo,beginInvoice,endInvoice,countyId,dataRight);
	}
	
	/**
	 *  查询客户业务费用（受理费+设备销售）
	 * @param custId
	 * @return
	 */
	public Pager<FeeDto> queryBusiPayFee(String custId, QueryFeeInfo queryFeeInfo, Integer start, Integer limit) throws Exception {
		String dataRight = null;
		try {
			dataRight = queryDataRightCon(getOptr(), DataRight.ACCTDATE_EDIT.toString());
		} catch (Exception e) {
		}
		Pager<FeeDto> feePager = cFeePayDao.queryBusiPayFee(custId, queryFeeInfo, getOptr().getCounty_id(), start, limit);
		List<FeeDto> feeList = feePager.getRecords();
		if(dataRight != null){
			for(FeeDto fee : feeList){
				fee.setData_right(dataRight);
			}
		}
		return feePager;
	}
	
	public Pager<FeePayDto> queryFeePay(String custId, QueryFeeInfo queryFeeInfo, Integer start, Integer limit) throws Exception {
		String dataRight = null;
		try {
			dataRight = queryDataRightCon(getOptr(), DataRight.ACCTDATE_EDIT.toString());
		} catch (Exception e) {
		}
		Pager<FeePayDto> feePager = cFeePayDao.queryFeePay(custId, queryFeeInfo,start, limit);
		List<FeePayDto> feeList = feePager.getRecords();
		if(dataRight != null){
			for(FeePayDto fee : feeList){
				fee.setData_right(dataRight);
			}
		}
		return feePager;
	}
	
	/**
	 * 查询某客户下指定费用类型和状态的费用项，
	 * @param custId 客户编号
	 * @param feeType 费用类型 (服务费、设备费、预存费)
	 * @param status 费用状态 (未收费、已付费、作废、已退款)
	 */
	public List queryFees(String custId ,String feeType ,String status) throws Exception {
		List fees = null ;
		Map<String, Serializable> params = new HashMap<String,Serializable>();
		params.put("county_id" , getOptr().getCounty_id() );
		params.put("cust_id", custId );
		params.put("status", status  );
		if(FEE_TYPE_BUSI.equals( feeType)){
			fees = cFeeBusiDao.findByMap( params );
		}else if(FEE_TYPE_DEVICE.equals( feeType)){
			fees = cFeeDeviceDao.findByMap( params );
		}else if(FEE_TYPE_ACCT.equals( feeType)){
			fees = cFeeDao.findByMap( params );
		}
		return fees;
	}

	public List<CFee> queryByDoneCode(Integer doneCode) throws Exception{
		return cFeeDao.queryByDoneCode(doneCode, getOptr().getCounty_id());
	}
	
	public List<CFee> queryByBusiDoneCode(Integer busiDoneCode) throws Exception{
		return cFeeDao.queryByDoneCode(busiDoneCode, getOptr().getCounty_id());
	}
	
	/**
	 * 根据流水号查询业务费用，按费用进行汇总
	 * @param doneCode
	 * @return
	 * @throws Exception
	 */
	public List<CFee> querySumFeeByDoneCode(String custId,Integer doneCode) throws Exception{
		return cFeeDao.querySumFeeByDoneCode(custId,doneCode, getOptr().getCounty_id());
	}

	public List<CFeeAcct> queryAcctFeeByDoneCode(Integer doneCode) throws Exception{
		return cFeeDao.queryAcctFeeByDoneCode(doneCode, getOptr().getCounty_id());
	}


	public List<CFeeDevice> queryDeviceByDoneCode(Integer doneCode) throws Exception{
		List<CFeeDevice> list= cFeeDao.queryDeviceByDoneCode(doneCode, getOptr().getCounty_id());
		return list;
	}
	
	public List<CFeeDevice> queryDeviceByDoneCodeAndFeeStdId(Integer doneCode, String feeId, String feeStdId) throws Exception{
		return cFeeDao.queryDeviceByDoneCodeAndFeeStdId(doneCode, feeId, feeStdId);
	}
	/**
	 * 根据sn获取费用信息
	 * @param feeSn
	 * @return
	 * @throws Exception
	 */
	public CFee queryBySn(String feeSn) throws Exception {
		return cFeeDao.findByKey(feeSn);
	}
	
	/**
	 * 根据sn获取费用信息
	 * @param feeSn
	 * @return
	 * @throws Exception
	 */
	public List<CFee> queryFeeListBySns(String [] feeSns) throws Exception {
		return cFeeDao.queryByFeeSns(feeSns);
	}

	public List<CFeeAcct> queryAcctFeeByFeeSns(String[] feeSns) throws Exception {
		return cFeeDao.queryAcctFeeByFeeSns(feeSns);
	}

	public CFeeAcct queryAcctFeeByFeeSn(String feeSn) throws Exception {
		return cFeeDao.queryAcctFee(feeSn);
	}

	public CFeeBusi queryFeeBusiBySn(String feeSn) throws Exception {
		return cFeeBusiDao.findByKey(feeSn);
	}
	
	public CFeeBusi queryFeeBusi(String feeSn)throws Exception {
		return cFeeBusiDao.queryCFeeBusi(feeSn);
	}

	public CFeeDevice queryFeeDeviceBySn(String feeSn) throws Exception {
		return cFeeDeviceDao.findByKey(feeSn);
	}
	
	public CFeeDevice queryFeeDevice(String feeSn) throws Exception {
		return cFeeDeviceDao.queryCFeeDevice(feeSn);
	}

	/**
	 * 查询客户下没有支付的费用项，包含三个类型的费用项，busi、device、acct
	 * @param custId 客户编号
	 */
	public List<FeeDto> queryUnPayFees(String custId)throws Exception{
		return cFeePayDao.queryPayFees(custId, getOptr().getCounty_id(),UNPAY);
	}

	/**
	 * 查询指定客户下未合并的费用项
	 * @param custId 客户编号
	 */
	public List<MergeFeeDto> queryUnMergeFees(String custId) throws Exception {
		return cFeePayDao.queryUnMergeFees(custId, getOptr().getCounty_id());
	}

	/**
	 * 根据单据编号查询发票信息
	 * @param docSn 单据编号
	 * @return
	 * @throws Exception
	 */
	public List<InvoiceFromDto> queryInvoiceByDocSn(String docSn, String invoiceId, String invoiceCode)throws Exception {
		return cInvoiceDao.queryInvoiceByDocSn(docSn, invoiceId, invoiceCode);
	}
	
	/**
	 * 发票重打，查找需要作废的发票
	 * @param donecode
	 * @param docSn
	 * @return
	 * @throws JDBCException
	 */
	public List<InvoiceFromDto> queryOldInvoiceByDocSn(Integer donecode,String docSn) throws JDBCException{
		return cInvoiceDao.queryOldInvoiceByDocSn(donecode, docSn);
	}

	/**
	 * 根据收费编号查询所在发票的信息
	 * @param feeSn
	 * @return
	 * @throws Exception
	 */
	public InvoiceFromDto queryInvoiceByFeeSn(String feeSn)throws Exception {
		return cInvoiceDao.queryInvoiceByFeeSn(feeSn);
	}

	/**
	 * 通过流水号获取需要自动合并的费用项
	 * @param doneCode
	 * @return
	 * @throws Exception
	 */
	public List<MergeFeeFormDto> queryAutoMergeFees(Integer doneCode,String custId)throws Exception{
		return makeAutoMergeData(cFeePayDao.queryFeeByDoneCode(doneCode, getOptr().getCounty_id()),custId);
	}

	/**
	 * 通过流水号获取需要自动合并的费用项
	 * @param doneCode
	 * @return
	 * @throws Exception
	 */
	public List<MergeFeeFormDto> queryAutoMergeFees(String[] feeSn)throws Exception{
		List<Map<String,Object>> lst = cFeePayDao.queryFeeByFeeSn(feeSn, getOptr().getCounty_id());
		return makeAutoMergeData( lst,null );

	}
	
	public List<MergeFeeFormDto> queryAutoMergeFees(String custId)throws Exception{
		List<Map<String,Object>> lst = cFeePayDao.queryFeeByFeeSn(custId,getOptr().getOptr_id(), getOptr().getCounty_id());
		return makeAutoMergeData( lst ,custId);
	}
	
	public List<MergeFeeFormDto> queryYHZZAutoMergeFees(String custId) throws Exception {
		List<Map<String,Object>> lst = cFeePayDao.queryYHZZFeeByCustId(custId,getOptr().getCounty_id());
		return makeAutoMergeData( lst ,custId);
	}
	
	public List<MergeFeeFormDto> queryAutoMergeUnitFees(String unitCustId)throws Exception{
		List<Map<String,Object>> lst = cFeePayDao.queryUnitFeeByFeeSn(unitCustId,getOptr().getOptr_id(), getOptr().getCounty_id());
		return makeAutoMergeData( lst ,unitCustId);
	}

	public List<FeeDto> queryPromotionFee(String custId,String userId,String promotionId) throws Exception{
		return cFeeDao.queryByPromotionId(custId, userId, promotionId);
	}
	/**
	 * @param custId
	 * @return
	 */
	public Map<String, Object> queryFeeView(String custId)throws Exception {
		return cFeePayDao.queryFeeView(custId, getOptr().getCounty_id());
	}

	/**
	 * 返回账务日期
	 * @return
	 */
	public Date acctDate() throws Exception {
		String gripAccountKey = gripAccountKey();
		TGripData acctDate = tGripDataDao.queryByKey(gripAccountKey);
		if (acctDate == null) {
			return DateHelper.today();
		}
		return DateHelper.addDate(acctDate.getGrip_date(),1);
	}

	/**
	 * 设置账务日期
	 * @param acctDate
	 * @throws ComponentException
	 * @throws JDBCException
	 */
	public void saveAcctDate(Date acctDate) throws Exception{
		String gripAccountKey = gripAccountKey();
		TGripLog log = new TGripLog();
		log.setGrip_date(acctDate);
		log.setGrip_key(gripAccountKey);
		log.setOptr_id(getOptr().getOptr_id());
		tGripLogDao.save(log);
		tGripDataDao.save(gripAccountKey,acctDate);
	}

	/**
	 * 扎帐等级
	 * @return
	 */
	public String queryGripAccountMode() throws Exception {
		return  queryTemplateConfig(TemplateConfigDto.Config.GRIP_ACCOUNT.toString());
	}

	/**
	 * 获取gripAccount 的key
	 * @return
	 * @throws ComponentException
	 */
	private String gripAccountKey() throws Exception {
		String gripAccount = queryGripAccountMode();
		String key = gripAccount;
		if (gripAccount.equals(SystemConstants.SYS_LEVEL_COUNTY)) {
			key += getOptr().getCounty_id();
		} else if (gripAccount.equals(SystemConstants.SYS_LEVEL_DEPT)) {
			key += getOptr().getDept_id();
		} else if (gripAccount.equals(SystemConstants.SYS_LEVEL_OPTR)) {
			key += getOptr().getOptr_id();
		} else if (gripAccount.equals(SystemConstants.SYS_LEVEL_AREA)) {
			key += getOptr().getArea_id();	
		} else if (gripAccount.equals(SystemConstants.SYS_LEVEL_ALL)) {
			key += SystemConstants.AREA_ALL;
		} else {
			throw new ComponentException("错误的扎账配置参数" + gripAccount);
		}
		return key;
	}

	/**
	 * 保存手工发票
	 *
	 * @param feeSn
	 * @param invoiceCode
	 * @param invoiceId
	 */
	public void saveManualInvoice(String[] feeSn, String invoiceCode,
			String invoiceId, String invoiceBookId) throws JDBCException {
		cFeeDao.updateInvoiceByFeeSn(feeSn, invoiceCode, invoiceId,
				invoiceBookId, SystemConstants.INVOICE_MODE_MANUAL);
	}
	public void saveManualInvoice(Integer doneCode, String invoiceCode,
			String invoiceId, String invoiceBookId) throws JDBCException {
		cFeeDao.updateInvoiceByDoneCode(doneCode, invoiceCode, invoiceId,
				invoiceBookId, SystemConstants.INVOICE_MODE_MANUAL);
	}
	
	public void saveQuatoInvoice(Integer doneCode) throws JDBCException{
		cFeeDao.updateInvoiceByDoneCode(doneCode,SystemConstants.INVOICE_MODE_QUOTA);
	}

	private List<MergeFeeFormDto> makeAutoMergeData(
			List<Map<String, Object>> lst, String custId) throws Exception {
		String custType = null;
		if(StringHelper.isNotEmpty(custId)){
			CCust cust = cCustDao.findByKey(custId);
			if(cust == null){
				cust = cCustHisDao.findByKey(custId);
			}
			custType = cust.getCust_type();
		}
		// TODO 需要通过配置
		List<MergeFeeFormDto> target = new ArrayList<MergeFeeFormDto>();
		Map<String,MergeFeeFormDto> temp = new HashMap<String,MergeFeeFormDto>();
		Map<String,CUser> userMap = CollectionHelper.converToMapSingle(cUserDao.queryUserByCustId(custId), "acct_id");
		if (null != lst) {
			for (Map<String, Object> map : lst) {
				Object printitemId = map.get("printitem_id");
				Integer amount = new Integer(map.get("amount").toString());
				String promFeeSN = map.get("prom_fee_sn") != null ? map.get("prom_fee_sn").toString() : "";
				String docType = map.get("doc_type").toString();
				if (printitemId == null||amount.compareTo(0)==0)
					continue;
				
				Object acctId = map.get("acct_id");
				CUser user = new CUser();
				if (acctId!=null)
					user =userMap.get(acctId)==null?user:userMap.get(acctId);
				
				
				String sign = "";
				if (amount.compareTo(0) > 0)
					sign = "Positive";
				else
					sign = "Negative";
				String key = "";
				
				//单位客户缴费打印，套餐缴费打印时acctId为null
				if(custType!=null && custType.equals(SystemConstants.CUST_TYPE_RESIDENT) && null != acctId){
					key = printitemId.toString()+"_"+docType+"_"+sign+"_"+user.getCard_id();
				}else{
					key = printitemId.toString()+"_"+docType+"_"+sign+"_"+promFeeSN;
//					key = printitemId.toString()+"_"+docType+"_"+sign;
				}
				MergeFeeFormDto mff = temp.get(key);
					if(mff == null){
						mff = new MergeFeeFormDto();
						mff.setPrintitem_id(printitemId.toString());
						mff.setDoc_type(docType);
						mff.setAmount(amount);
						target.add(mff);
						temp.put(key, mff);
					}else
						mff.setAmount(mff.getAmount()+amount);
				
					
				mff.getFee_sns().add(map.get("fee_sn").toString());
				
			}
		}
		return target;
	}


	@SuppressWarnings("unused")
	private List<BusiFeeDto> busifee(String countyId, String busiCode) throws Exception {
		loadBusiFees();
		List<BusiFeeDto> fees = busifees.get(countyId + busiCode);
		if (fees == null)
			fees = new ArrayList<BusiFeeDto>();
		return fees;
	}

	public void setCFeeBusiDao(CFeeBusiDao feeBusiDao) {
		cFeeBusiDao = feeBusiDao;
	}
	private void loadBusiFees () throws ComponentException, JDBCException, IllegalAccessException, InvocationTargetException{
		if (busifees == null){
			synchronized (FeeComponent.class) {
				if (busifees == null){
					Map<String, List<BusiFeeDto>> tempbusifees = null;
					List<BusiFeeDto> fees = tBusiFeeStdDao.getAllFee();
					String keyName="keyname";
					try {
						tempbusifees = CollectionHelper.converToMap(fees, keyName);
					} catch (Exception e) {
						throw new ComponentException("加载费用项失败");
					}
					busifees = tempbusifees;
				}
			}
		}
	}
	
	/**
	 * @return
	 */
	public List<CFee> queryUnPrintFee() throws JDBCException {
		return cFeeDao.queryUnPrintFee();
	}


	/**
	 * 查找用户当天的累积计费记录
	 * @param custId
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<CFee> queryUserFee(String custId ,String userId) throws Exception{
		return cFeeDao.queryUserFee(custId,userId);
	}
	/**
	 * 返回当前地区的所有业务费用项
	 * @return
	 * @throws Exception
	 */
	public List<BusiFeeDto> getBusiFeeItems() throws Exception {
		return tBusiFeeStdDao.queryBusiFeeStd(queryTemplateId(SystemConstants.TEMPLATE_TYPE_FEE));
	}
	/**
	 * 返回当前地区的指定业务费用项目
	 * @param busiFeeId
	 * @return
	 */
	public BusiFeeDto getBusiFee(String busiFeeId) throws Exception{
		return tBusiFeeStdDao.queryIpBusiFeeStdByFeeId(queryTemplateId(SystemConstants.TEMPLATE_TYPE_FEE),busiFeeId);
	}
	
	public List<BusiFeeDto> getBusiFeeAndIpFeeItems() throws Exception {
		List<BusiFeeDto>  list = getBusiFeeItems();
		BusiFeeDto ipFee = tBusiFeeStdDao.queryIpBusiFeeStdByFeeId(queryTemplateId(SystemConstants.TEMPLATE_TYPE_FEE),SystemConstants.USER_IP_FEE_ID);
		if(ipFee != null){
			list.add(ipFee);
		}
		return list;
	}
	
	/**
	 * 返回当前地区的所有设备费用项
	 * @return
	 * @throws Exception
	 */
	public List<BusiFeeDto> getDeviceFeeItems() throws Exception {
		return tBusiFeeStdDao.queryDeviceFeeStd(queryTemplateId(SystemConstants.TEMPLATE_TYPE_FEE));
	}
	
	/**
	 * 修改收费记录中的发票号
	 * @param oldInvoiceId
	 * @param oldInvoiceCode
	 * @param newInvoiceId
	 * @param newInvoiceCode
	 */
	public void changeFeeInvoice(String newInvoiceCode,String newInvoiceBookId, String newInvoiceId,
			String oldInvoiceCode,String oldInvoiceId) throws JDBCException {
		cFeeDao.updateInvoice(newInvoiceCode, StringHelper.isEmpty(newInvoiceBookId)?newInvoiceCode:newInvoiceBookId, newInvoiceId, oldInvoiceCode, oldInvoiceId);
	}
	
	/**
	 * 修改收费记录中的发票号
	 * @param fees
	 */
	public void changeFeeInvoice(List<FeeDto> fees) throws JDBCException {
		List<CFee> feeList = new ArrayList<CFee>();
		for (FeeDto fee : fees) {
			CFee f = new CFee();
			f.setFee_sn(fee.getFee_sn());
			f.setInvoice_id(fee.getNew_invoice_id());
			f.setInvoice_code(fee.getNew_invoice_code());
			f.setInvoice_book_id(fee.getNew_invoice_book_id());
			feeList.add(f);
		}
		cFeeDao.update(feeList.toArray(new CFee[feeList.size()]));
	}
	
	/**
	 * 
	 * 根据发票号码和Id查询相应记录
	 * @param invoiceCode
	 * @param invoiceId
	 * @return
	 * @throws JDBCException
	 */
	public List<CFee> queryFeeByInvoice(String invoiceCode, String invoiceId,String custId)
			throws JDBCException {
		return cFeeDao.queryFeeByInvoice(invoiceCode, invoiceId,custId);
	}
	/**
	 * 
	 * 根据发票号码和Id查询相应记录
	 * @param invoiceCode
	 * @param invoiceId
	 * @return
	 * @throws JDBCException
	 */
	public List<CFee> queryFeeByInvoice(String invoiceCode, String invoiceId)
			throws JDBCException {
		return cFeeDao.queryFeeByInvoice(invoiceCode, invoiceId);
	}
	
	public List<PrintItemDto> queryPrintitemBySn(String docSn, String custType, String invoiceId, String invoiceCode) throws Exception{
		return cDocItemDao.queryBySn(docSn,custType, invoiceId, invoiceCode);
	}
	
	/**
	 * 还原代金券的状态
	 * @param fee_sn
	 */
	public void updateVoucher(String feeSn) throws Exception {
		CFeePay feePay = cFeePayDao.queryByFeeSn(feeSn);
		List<CFee> fees = cFeeDao.queryByFeeSns(new String [] {feeSn});
		Map<String, CFee> map = CollectionHelper.converToMapSingle(fees, "fee_sn");
		CFee fee = map.get(feeSn);
		if(fee == null){
			throw new ComponentException("回退业务时未能获取缴费记录！");
		}
		Integer real_pay = fee.getReal_pay();
		CVoucher voucher = cVoucherDao.findByKey(feePay.getReceipt_id());
		voucher.setUnused_money(voucher.getUnused_money() + real_pay );
		voucher.setUsed_money(voucher.getUsed_money() - real_pay );
		voucher.setStatus(StatusConstants.IDLE);
		voucher.setCust_id(null);
		cVoucherDao.update(voucher);
	}


	
	/**
	 * 更新分公司账户余额
	 * @param fee
	 */
	public void updateGeneralAcct(Integer fee) throws Exception{
		String needAcct = queryTemplateConfig(Template.GENERAL_ACCT_FLAG.toString());			
		if(SystemConstants.BOOLEAN_TRUE.equals(needAcct)){
			CGeneralAcct acct = cGeneralAcctDao.queryCompanyAcctByCountyId(getOptr().getCounty_id());
			
			if(fee >0 && acct == null){
				throw new ComponentException("没有配置分公司账户");
			}
			if(acct!=null){
				if(acct.getBalance() < fee){
					throw new ComponentException("分公司账户余额仅剩"+acct.getBalance()/100+",无法支付");
				}else{
					acct.setBalance(acct.getBalance()-fee);
					cGeneralAcctDao.update(acct);
				}
			}
		}
	}
	
	/**
	 * 更新凭据剩余金额
	 * @param fee_sn
	 */
	public void updateCredential(Integer fee , String receiptId) throws Exception {
		cGeneralCredentialDao.updateCredentialByNo(fee, receiptId);
	}
	
	/**
	 * @return
	 */
	public List<TBusiFee> queryUnBusiFee() throws Exception {
		return tBusiFeeDao.queryUnBusiFee();
	}

	
	/**
	 * 根据feeSn查找CFeePay
	 * @param feeSn
	 * @return
	 * @throws Exception
	 */
	public CFeePay queryCFeePaybyFeeSn(String feeSn) throws Exception{
		return cFeePayDao.queryByFeeSn(feeSn);
	}
	/**
	 * 
	 * 根据银行交易流水号查询CFee
	 * @param invoiceCode
	 * @param invoiceId
	 * @return
	 * @throws JDBCException
	 */
	public List<CFee> queryFeeByBankTransCode(String startTransCode,String endTransCode,String countyId)
			throws JDBCException {
		return cFeeDao.queryFeeByBankTransCode(startTransCode, endTransCode,countyId);
	}
	
	
	public void saveBankPay(CBankPay bankPay) throws JDBCException {
		cBankPayDao.save(bankPay);
	}

	/**
	 * 检查客户名下是否含有未退的保证金
	 * @return
	 * @throws Exception
	 */
	public List<CFee> hasDepositInCust(String custId)throws Exception{
		return cFeeDao.queryDepositInCust(custId);
	}
	
	public void setCFeeDao(CFeeDao feeDao) {
		cFeeDao = feeDao;
	}
	public void setCFeeDeviceDao(CFeeDeviceDao feeDeviceDao) {
		cFeeDeviceDao = feeDeviceDao;
	}

	public void setCFeePayDetailDao(CFeePayDetailDao feePayDetailDao) {
		cFeePayDetailDao = feePayDetailDao;
	}
	public void setCFeePayDao(CFeePayDao feePayDao) {
		cFeePayDao = feePayDao;
	}
	public void setCInvoiceDao(CInvoiceDao invoiceDao) {
		cInvoiceDao = invoiceDao;
	}
	/**
	 * @param feeUnitpreDao the cFeeUnitpreDao to set
	 */
	public void setCFeeUnitpreDao(CFeeUnitpreDao feeUnitpreDao) {
		cFeeUnitpreDao = feeUnitpreDao;
	}

	public void setTBusiFeeDao(TBusiFeeDao busiFeeDao) {
		tBusiFeeDao = busiFeeDao;
	}

	public void setCFeeAcctDao(CFeeAcctDao feeAcctDao) {
		cFeeAcctDao = feeAcctDao;
	}

	/**
	 * @param busiFeeStdDao the tBusiFeeStdDao to set
	 */
	public void setTBusiFeeStdDao(TBusiFeeStdDao busiFeeStdDao) {
		tBusiFeeStdDao = busiFeeStdDao;
	}

	/**
	 * @param bankPayDao the cBankPayDao to set
	 */
	public void setCBankPayDao(CBankPayDao bankPayDao) {
		cBankPayDao = bankPayDao;
	}

	/**
	 * @param banklogid
	 * @return
	 */
	public CBankPay queryBankPayByLogId(String banklogid) throws Exception {
		return cBankPayDao.queryBankPayByLogId(banklogid);
	}

	/**
	 * @param done_code
	 */
	public void updateBankPayByDoneCode(Integer doneCode) throws JDBCException {
		cBankPayDao.updateBankPayByDoneCode(doneCode);
	}

	/**
	 * @param fee_sn
	 * @param invalidDate
	 */
	public void updateInvalidDate(String feeSn,String prodSn, Date invalidDate) throws JDBCException {
		cFeeAcctDao.updateInvalidDate(feeSn,prodSn,invalidDate);
		
	}
	
	public CFee editBusiOptr(String feeSn,String busiOptrId) throws Exception {
		CFee fee = new CFee();
		fee.setFee_sn(feeSn);
		fee.setBusi_optr_id(busiOptrId);
		cFeeDao.update(fee);
		return fee;
	}
	
	/**
	 * 保存打印标记
	 * @param feeSn
	 * @throws JDBCException
	 */
	public void savePrintStatus(String feeSn) throws JDBCException {
		CFee fee = new CFee();
		fee.setFee_sn(feeSn);
		fee.setIs_doc(SystemConstants.BOOLEAN_NO);
		cFeeDao.update(fee);
	}
	
	/**
	 * 取消打印标记
	 * @param feeSn
	 * @throws JDBCException
	 */
	public void saveCancelPrintStatus(String feeSn) throws JDBCException {
		CFee fee = new CFee();
		fee.setFee_sn(feeSn);
		fee.setIs_doc(BOOLEAN_FALSE);
		cFeeDao.update(fee);
	}
	
	/**
	 * 根据流水修改打印标记
	 * @param doneCode
	 * @param status
	 * @throws JDBCException
	 */
	public void updateIsDocByDoneCode(Integer doneCode,String status) throws JDBCException{
		cFeeDao.updateIsDocByDoneCode(doneCode,status);
	}
	
	/**
	 * 修改定额发票金额
	 * @param feeSn
	 * @param newInvoiceFee
	 */
	public void editInvoiceFee(String feeSn, int newInvoiceFee) throws JDBCException {
		CFee fee = new CFee();
		fee.setFee_sn(feeSn);
		fee.setInvoice_fee(newInvoiceFee);
		cFeeDao.update(fee);
	}
	
	/**
	 * @param contractId
	 * @return
	 */
	public List<CFee> queryContractPay(Integer contractId) throws JDBCException {
		return cFeeDao.queryContractPay(contractId);
	}
	

	/**
	 * @param doneCode
	 * @return
	 */
	public List<CPromFeeProd> queryFeeProdByDoneCode(Integer doneCode) throws JDBCException {
		return cPromFeeProdDao.queryFeeProdByDoneCode(doneCode);
	}
	

	/**
	 * 根据流水查找促销冻结记录
	 * @param createDoneCode
	 * @return
	 */
	public List<CAcctAcctitemInactive> queryInactiveByDoneCode(
			Integer doneCode) throws JDBCException {
		return cPromFeeDao.queryInactiveByDoneCode(doneCode);
	}

	/**
	 * @param feeDoneCode
	 */
	public void savePromPayHis(Integer doneCode,Integer feeDoneCode) throws Exception {
//		cPromFeeDao.queryPromFeeByDoneCode(feeDoneCode);
		cPromFeeDao.removeWithHis(doneCode,feeDoneCode);
	}

	public List<FeeDto> queryFeePayDetail(String paySn) throws Exception {
		List<FeeDto> list = cFeePayDao.queryFeePayDetail(paySn);
		return list;
	}
	
	

	/**
	 * @param docItemDao the cDocItemDao to set
	 */
	public void setCDocItemDao(CDocItemDao docItemDao) {
		cDocItemDao = docItemDao;
	}

	/**
	 * @param userDao the cUserDao to set
	 */
	public void setCUserDao(CUserDao userDao) {
		cUserDao = userDao;
	}

	public void setCVoucherDao(CVoucherDao voucherDao) {
		cVoucherDao = voucherDao;
	}

	public void setCGeneralAcctDao(CGeneralAcctDao generalAcctDao) {
		cGeneralAcctDao = generalAcctDao;
	}

	public void setCGeneralCredentialDao(CGeneralCredentialDao generalCredentialDao) {
		cGeneralCredentialDao = generalCredentialDao;
	}
	public void setCFeePropChangeDao(CFeePropChangeDao feePropChangeDao) {
		cFeePropChangeDao = feePropChangeDao;
	}

	public void setCCustDao(CCustDao custDao) {
		cCustDao = custDao;
	}

	public void setCCustHisDao(CCustHisDao custHisDao) {
		cCustHisDao = custHisDao;
	}

	public void setCProdMobileBillDao(CProdMobileBillDao cProdMobileBillDao) {
		this.cProdMobileBillDao = cProdMobileBillDao;
	}

	public void setCGeneralAcctDedailDao(CGeneralAcctDedailDao cGeneralAcctDedailDao) {
		this.cGeneralAcctDedailDao = cGeneralAcctDedailDao;
	}

	public void setCPromFeeDao(CPromFeeDao cPromFeeDao) {
		this.cPromFeeDao = cPromFeeDao;
	}

	public void setCPromFeeProdDao(CPromFeeProdDao cPromFeeProdDao) {
		this.cPromFeeProdDao = cPromFeeProdDao;
	}

	/**
	 * @param expressionUtil the expressionUtil to set
	 */
	public void setExpressionUtil(ExpressionUtil expressionUtil) {
		this.expressionUtil = expressionUtil;
	}
	
	public void setCAcctAcctitemInactiveDao(
			CAcctAcctitemInactiveDao acctAcctitemInactiveDao) {
		cAcctAcctitemInactiveDao = acctAcctitemInactiveDao;
	}


}
