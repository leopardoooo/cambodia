package com.ycsoft.business.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ycsoft.beans.config.TBusiFee;
import com.ycsoft.beans.core.acct.CAcctAcctitemChange;
import com.ycsoft.beans.core.acct.CGeneralContractPay;
import com.ycsoft.beans.core.bank.CBankGotodisk;
import com.ycsoft.beans.core.bank.CBankReturn;
import com.ycsoft.beans.core.bank.CBankReturnPayerror;
import com.ycsoft.beans.core.common.CDoneCodeDetail;
import com.ycsoft.beans.core.common.CDoneCodeUnpay;
import com.ycsoft.beans.core.cust.CCust;
import com.ycsoft.beans.core.fee.CFee;
import com.ycsoft.beans.core.fee.CFeeAcct;
import com.ycsoft.beans.core.fee.CFeeBusi;
import com.ycsoft.beans.core.fee.CFeeDevice;
import com.ycsoft.beans.core.fee.CFeePay;
import com.ycsoft.beans.core.fee.CFeePropChange;
import com.ycsoft.beans.core.fee.CFeeUnitpre;
import com.ycsoft.beans.core.job.JCustWriteoff;
import com.ycsoft.beans.core.prod.CProdMobileBill;
import com.ycsoft.beans.core.prod.CProdOrder;
import com.ycsoft.beans.core.prod.CProdOrderFee;
import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.beans.invoice.RInvoice;
import com.ycsoft.beans.prod.PProd;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.business.commons.pojo.BusiParameter;
import com.ycsoft.business.component.core.OrderComponent;
import com.ycsoft.business.dao.config.TExchangeDao;
import com.ycsoft.business.dao.core.bank.CBankGotodiskDao;
import com.ycsoft.business.dao.core.bank.CBankReturnDao;
import com.ycsoft.business.dao.core.bank.CBankReturnPayerrorDao;
import com.ycsoft.business.dao.core.prod.CProdOrderDao;
import com.ycsoft.business.dao.core.prod.CProdOrderFeeDao;
import com.ycsoft.business.dao.prod.PProdDao;
import com.ycsoft.business.dto.core.acct.PayDto;
import com.ycsoft.business.dto.core.fee.CFeePayDto;
import com.ycsoft.business.dto.core.fee.FeeBusiFormDto;
import com.ycsoft.business.dto.core.fee.FeeDto;
import com.ycsoft.business.dto.core.fee.MergeFeeDto;
import com.ycsoft.business.dto.core.print.CInvoiceDto;
import com.ycsoft.business.service.IPayService;
import com.ycsoft.commons.constants.BusiCmdConstants;
import com.ycsoft.commons.constants.BusiCodeConstants;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ErrorCode;
import com.ycsoft.commons.exception.ServicesException;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.commons.helper.DateHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.commons.store.MemoryPrintData;
import com.ycsoft.daos.core.JDBCException;

/**
 * 业务保存接口的实现类
 *
 * @author hh
 */
@Service
public class PayService extends BaseBusiService implements IPayService {
	private CBankReturnDao cBankReturnDao;
	private CBankReturnPayerrorDao cBankReturnPayerrorDao;
	private CBankGotodiskDao cBankGotodiskDao;
	@Autowired
	private CProdOrderDao cProdOrderDao;
	@Autowired
	private PProdDao pProdDao;
	@Autowired
	private TExchangeDao tExchangeDao;
	@Autowired
	private OrderComponent orderComponent;
	@Autowired
	private CProdOrderFeeDao cProdOrderFeeDao;
	/**
	 * 查询汇率
	 * @return
	 * @throws ServicesException
	 */
	public Integer queryExchage() throws Exception{
		Integer exchange= tExchangeDao.getExchange();
		if(exchange==null||exchange<=0){
			throw new ServicesException(ErrorCode.ExchangeConfigError);
		}
		return exchange;
	}
	/**
	 * 查询未支付总额
	 * @param cust_id
	 * @return
	 * @throws Exception
	 */
	public Map<String,Integer> queryUnPaySum(String cust_id) throws Exception{
		
		return feeComponent.queryUnPaySum(cust_id,this.getOptr().getOptr_id());
	}
	/**
	 * 查询未支付的费用明细
	 * 显示 费用编号 fee_sn,业务名称busi_name,费用名称fee_text,数量(当count不为空，显示count否则显示begin_date(yyyymmdd)+“-”+prod_invalid_date),
	 * 操作员 optr_name,操作时间create_time,金额 real_pay,订单号 prod_sn,X按钮(当prod_sn不为空时显示)
	 * @param cust_id
	 * @return
	 */
	public List<FeeDto> queryUnPayDetail(String cust_id)throws Exception{
		return feeComponent.queryUnPay(cust_id,this.getOptr().getOptr_id());
	}
	
	/**
	 * 检查未支付退订数据
	 * @param fee
	 * @throws ServicesException
	 */
	private void checkCanclUpPayFeeParam(CFee fee,String cust_id) throws Exception{
		if(fee==null){
			throw new ServicesException(ErrorCode.ParamIsNull);
		}
		if(!fee.getCust_id().equals(cust_id)){
			throw new ServicesException(ErrorCode.CustDataException);
		}
		
		if(!StatusConstants.UNPAY.equals(fee.getStatus())|| 
			!SystemConstants.PAY_TYPE_UNPAY.equals(fee.getPay_type())){
			throw new ServicesException(ErrorCode.UnPayFeeHasPay);
		}
		if(fee.getFee_type().equals(SystemConstants.FEE_TYPE_ACCT)
				&&StringHelper.isEmpty(fee.getAcctitem_id())){
			throw new ServicesException(ErrorCode.ParamIsNull);
		}
		if(!fee.getFee_type().equals(SystemConstants.FEE_TYPE_ACCT)
				&&StringHelper.isEmpty(fee.getFee_id())){
			throw new ServicesException(ErrorCode.ParamIsNull);
		}
		
	}
	/**
	 * 取消未支付的费用项目
	 * @param feeSn
	 * @throws Exception
	 */
	public String saveCancelUnPayFee(String fee_sn,String fee_type,boolean onlyShowInfo) throws Exception{
		
		if(StringHelper.isEmpty(fee_sn)||StringHelper.isEmpty(fee_type)){
			throw new ServicesException(ErrorCode.ParamIsNull);
		}
		String cust_id=this.getBusiParam().getCust().getCust_id();
		doneCodeComponent.lockCust(cust_id);
		Integer doneCode=doneCodeComponent.gDoneCode();
		
		String info="";//取消费用的业务提示
		
		if(fee_type.equals(SystemConstants.FEE_TYPE_ACCT)){
			CFeeAcct fee=feeComponent.queryAcctFeeByFeeSn(fee_sn);
			this.checkCanclUpPayFeeParam(fee, cust_id);

			if(!fee.getAcctitem_id().equals(SystemConstants.ACCTITEM_PUBLIC_ID)){
				if(fee.getReal_pay()>0){
					//产品订购，业务回退
					if(!onlyShowInfo){
						this.cancelUnPayProdOrderPay(fee,doneCode);
					}
					info=SystemConstants.UNPAY_CANCEL_PROMPT_FEEANDBUSI;
				}else{
					//产品退订，退款转到账户，业务正常
					if(!onlyShowInfo){
						this.cancelUnPayProdUnsubscribePay(fee,doneCode);
					}
					info=SystemConstants.UNPAY_CANCEL_PROMPT_ONLYACCT;
				}
			}else{
				if(fee.getReal_pay()>0){
					//公用现金充值，业务回退
					if(!onlyShowInfo){
						this.cancelUnPayAcctPay(fee,doneCode);
					}
					info=SystemConstants.UNPAY_CANCEL_PROMPT_FEEANDBUSI;
				}else{
					//公用现金退款，业务回退
					if(!onlyShowInfo){
						this.cancelUnPayAcctRefund(fee,doneCode);
					}
					info=SystemConstants.UNPAY_CANCEL_PROMPT_FEEANDBUSI;
				}
			}
		}else if(fee_type.equals(SystemConstants.FEE_TYPE_BUSI)){
			CFeeBusi fee=feeComponent.queryFeeBusi(fee_sn);
			this.checkCanclUpPayFeeParam(fee, cust_id);
			//取消杂费，业务正常
			if(!onlyShowInfo){
				this.cancelUnPayBusiFee(fee, doneCode);
			}
			info=SystemConstants.UNPAY_CANCEL_PROMPT_ONLYFEE;
			
		}else if(fee_type.equals(SystemConstants.FEE_TYPE_DEVICE)){
			CFeeDevice fee=feeComponent.queryFeeDevice(fee_sn);
			this.checkCanclUpPayFeeParam(fee, cust_id);
			
			if(SystemConstants.DEVICE_TYPE_FITTING.equals(fee.getDevice_type())
				&&fee.getCreate_done_code().equals(fee.getBusi_done_code()) //非费用修改
				&&fee.getReal_pay()>0){
				//配件费取消,业务回退
				if(!onlyShowInfo){
					this.cancelUnPayFittingFee(fee, doneCode);
				}
				info=SystemConstants.UNPAY_CANCEL_PROMPT_FEEANDBUSI;
			}else{
				//取消费用，业务正常
				if(!onlyShowInfo){
					this.cancelUnPayDeviceFeePay(fee, doneCode);
				}
				info=SystemConstants.UNPAY_CANCEL_PROMPT_ONLYFEE;
			}
			
		}else {
			throw new ServicesException(ErrorCode.UnPayFeeTypeCanNotCancel);
		}
		
		//取消费用提示描述
		String infoDesc=MemoryDict.getDictName(DictKey.UNPAY_CANCEL_PROMPT, info);
		if(infoDesc==null){
			infoDesc=info;
		}
		
		this.saveAllPublic(doneCode, this.getBusiParam());
	    return infoDesc;
	}
	/**
	 * 取消公用充值
	 * @param fee_sn
	 * @throws Exception 
	 */
	private void cancelUnPayAcctPay(CFee fee,Integer doneCode) throws Exception{
		
		if(!fee.getAcctitem_id().equals(SystemConstants.ACCTITEM_PUBLIC_ID)){
			throw new ServicesException(ErrorCode.UnPayAcctIsNotPublic);
		}
		//从账户扣回可退部分余额
		acctComponent.saveAcctDebitFee(fee.getCust_id(), fee.getAcct_id(), fee.getAcctitem_id(),
				SystemConstants.ACCT_CHANGE_UNCFEE, fee.getReal_pay()*-1, this.getBusiParam().getBusiCode(), doneCode, true,null);
		//作废缴费信息
		feeComponent.saveCancelFeeUnPay(fee, doneCode);
		//作废业务
		doneCodeComponent.cancelDoneCode(fee.getCreate_done_code());
	}
	/**
	 * 取消公用退款
	 * @param fee_sn
	 */
	private void cancelUnPayAcctRefund(CFee fee,Integer doneCode)throws Exception{

		if(!fee.getAcctitem_id().equals(SystemConstants.ACCTITEM_PUBLIC_ID)){
			throw new ServicesException(ErrorCode.UnPayAcctIsNotPublic);
		}
		//查找上次异动记录，并按异动记录资金明细恢复金额
		List<CAcctAcctitemChange> changeList=acctComponent.queryAcctitemChangeByBusiInfo(fee.getAcct_id(), fee.getAcctitem_id(),fee.getBusi_code(),fee.getCreate_done_code());
		int totalFee=0;
		for(CAcctAcctitemChange change:changeList){
			totalFee=totalFee+change.getChange_fee();
			if(!fee.getCust_id().equals(change.getCust_id())){
				throw new ServicesException(ErrorCode.CustDataException);
			}
		}
		if(totalFee!=fee.getReal_pay()){
			throw new ServicesException(ErrorCode.UnPayAcctRefundFeeAndChangeIsDiffer);
		}
		//恢复账户金额
		for(CAcctAcctitemChange change:changeList){
			acctComponent.saveAcctAddFee(change.getCust_id(), fee.getAcct_id(), fee.getAcctitem_id(),
					SystemConstants.ACCT_CHANGE_UNCFEE,change.getChange_fee()*-1, change.getFee_type(), this.getBusiParam().getBusiCode(), doneCode,null);
		}
		//作废缴费信息
		feeComponent.saveCancelFeeUnPay(fee, doneCode);
		//作废业务
		doneCodeComponent.cancelDoneCode(fee.getCreate_done_code());
	}
	/**
	 * 取消未支付订单，业务回退
	 * @param fee_sn
	 * @throws Exception 
	 */
	private void cancelUnPayProdOrderPay(CFeeAcct fee,Integer doneCode) throws Exception{
		
		String order_sn=fee.getProd_sn();
		
		CProdOrder order=cProdOrderDao.findByKey(order_sn);
		//检查套餐类要按订购顺序取消，同一个用户的宽带类单产品要按订购顺序取消，用户一个用户的非宽带单产品按相同产品订购顺序取消。
		//目的是保证c_fee_acct中pre_invalid_date和begin_date准确
		this.checkUnPayOrderCancel(order,fee);
		//恢复被覆盖转移的订单
		orderComponent.recoverTransCancelOrder(order.getDone_code(),order.getCust_id(),doneCode);
		//删除c_prod_order_fee
		cProdOrderFeeDao.deleteOrderFeeByOrderSn(order_sn);
		//移除订单到历史表
		orderComponent.saveCancelProdOrder(order, doneCode);

		//作废缴费信息
		feeComponent.saveCancelFeeUnPay(fee, doneCode);
		//作废业务
		doneCodeComponent.cancelDoneCode(fee.getCreate_done_code());
	}
	/**
	 * 检查是否按顺序取消
	 * 套餐类要按订购顺序取消，同一个用户的宽带类单产品要按订购顺序取消，用户一个用户的非宽带单产品按相同产品订购顺序取消。
	 * 目的是保证c_fee_acct.pro_invalid_date begin_date准确
	 * @param order
	 * @throws Exception 
	 */
	private void checkUnPayOrderCancel(CProdOrder order,CFee cfee) throws Exception{
		if(order==null||cfee==null){
			throw new ServicesException(ErrorCode.ParamIsNull);
		}
		String fee_sn=cfee.getFee_sn();
		String cust_id=cfee.getCust_id();
		if(StringHelper.isEmpty(cust_id)||StringHelper.isEmpty(fee_sn)){
			throw new ServicesException(ErrorCode.ParamIsNull);
		}
		if(!cust_id.equals(order.getCust_id())){
			throw new ServicesException(ErrorCode.CustDataException);
		}
		if(cfee.getReal_pay()<0){
			throw new ServicesException(ErrorCode.UnPayOrderCancelUnsubscribe);
		}
		
		if(!order.getDone_code().equals(cfee.getCreate_done_code())||!order.getProd_id().equals(cfee.getAcctitem_id())){
			throw new ServicesException(ErrorCode.CFeeAndProdOrderIsNotOne);
		}
		
		PProd prod=pProdDao.findByKey(order.getProd_id());
		//碰撞检测
		if(!prod.getProd_type().equals(SystemConstants.PROD_TYPE_BASE)){	
			//套餐处理
			for(CProdOrder checkOrder: cProdOrderDao.queryNotExpPackageOrder(cust_id)){
				if(order.getExp_date().before(checkOrder.getExp_date())){
					throw new ServicesException(ErrorCode.UnPayOrderCancelBefor,checkOrder.getOrder_sn());
				}
			}
			//跟单产品在套餐后续订碰撞
			List<CProdOrder>  orderAfterPakList=cProdOrderDao.querySingleProdOrderAfterPak(order.getOrder_sn());
			if(orderAfterPakList!=null&&orderAfterPakList.size()>0){
				throw new ServicesException(ErrorCode.UnPayOrderCancelBefor,orderAfterPakList.get(0).getOrder_sn());
			}
			
		}else{
			//单产品直接碰撞处理
			for(CProdOrder checkOrder: cProdOrderDao.queryProdOrderDtoByUserId(order.getUser_id())){
				if(prod.getServ_id().equals(SystemConstants.PROD_SERV_ID_BAND)||order.getProd_id().equals(checkOrder.getProd_id())){
					if(order.getExp_date().before(checkOrder.getExp_date())){
						throw new ServicesException(ErrorCode.UnPayOrderCancelBefor,checkOrder.getOrder_sn());
					}
				}
			}
		}
	}
	/**
	 * 取消订单退订金额：  退款转给账户，业务正常
	 */
	private void cancelUnPayProdUnsubscribePay(CFeeAcct fee,Integer doneCode) throws Exception{
		
		String order_sn=fee.getProd_sn();
		String fee_sn=fee.getFee_sn();
		if(StringHelper.isEmpty(order_sn)||StringHelper.isEmpty(fee_sn)){
			throw new ServicesException(ErrorCode.ParamIsNull);
		}
		PProd prod=prodComponent.queryById(fee.getAcctitem_id());
		//提取资金转出到缴费的 订单金额明细
		List<CProdOrderFee> orderFees=cProdOrderFeeDao.queryByOutPutInfo(order_sn, SystemConstants.ORDER_FEE_TYPE_CFEE, fee_sn);
		for(CProdOrderFee orderFee:orderFees){
			orderFee.setOutput_type(SystemConstants.ORDER_FEE_TYPE_ACCT);//更新转出类型为账户
			orderFee.setOutput_sn(null);
			orderFee.setProd_name(prod.getProd_name());
		}
		//原来转给缴费的金额现在转给公用账目
		acctComponent.saveCancelFeeToAcct(orderFees, fee.getCust_id(), doneCode, this.getBusiParam().getBusiCode());
		//更新资金明细
		cProdOrderFeeDao.update(orderFees.toArray(new CProdOrderFee[orderFees.size()]));
		//作废缴费
		feeComponent.saveCancelFeeUnPay(fee, doneCode);
	}
	
	/**
	 * 只取消设备费用,业务正常
	 * @throws Exception 
	 */
	private void cancelUnPayDeviceFeePay(CFeeDevice feeDevice,Integer doneCode) throws Exception{
		feeComponent.saveCancelFeeUnPay(feeDevice, doneCode);
	}
	
	/**
	 * 取消配件费，回退业务
	 * @throws Exception 
	 */
	private void cancelUnPayFittingFee(CFeeDevice feeDevice,Integer doneCode) throws Exception{
		//回退配件库存
		deviceComponent.updateDeviceNum(feeDevice);
		//作废缴费
		feeComponent.saveCancelFeeUnPay(feeDevice, doneCode);
		//作废业务
		doneCodeComponent.cancelDoneCode(feeDevice.getCreate_done_code());
		
	}
	
	/**
	 * 取消营业费的收费，业务正常
	 * @param fee_sn
	 * @throws Exception 
	 */
	private void cancelUnPayBusiFee(CFeeBusi feeBusi,Integer doneCode) throws Exception{
		feeComponent.saveCancelFeeUnPay(feeBusi, doneCode);
	}
	
	
	/**
	 * 保存支付信息
	 * @throws Exception 
	 */
	public void savePay(CFeePayDto pay) throws Exception{
		String cust_id=pay.getCust_id();
		//用户锁
		doneCodeComponent.lockCust(cust_id);
		
		//查询未支付业务
		List<CDoneCodeUnpay> upPayDoneCodes=doneCodeComponent.queryUnPayList(cust_id);
		//数据验证
		this.checkCFeePayParam(pay, upPayDoneCodes,cust_id);
		
		Integer done_code=doneCodeComponent.gDoneCode();
		//保存支付记录
		feeComponent.savePayFeeNew(pay,cust_id,done_code);
		
		//更新缴费信息状态、支付方式和发票相关信息
		feeComponent.updateCFeeToPay(upPayDoneCodes, pay);
		
		//更新订单费用的费用类型(新订购记录才有需要处理)
		orderComponent.updateOrderFeeTypeByPayType(upPayDoneCodes, pay.getPay_type());
		
		//更新订单状体并给订单发授权
		updateOrder(cust_id,done_code);
		
		//删除未支付业务信息
		doneCodeComponent.deleteDoneCodeUnPay(upPayDoneCodes);
	
		//保存业务流水
		this.saveAllPublic(done_code, this.getBusiParam());
	}
	/**
	 * 检查支付参数
	 * @param cust_id
	 * @throws Exception 
	 */
	private void checkCFeePayParam(CFeePayDto pay, List<CDoneCodeUnpay> upPayDoneCodes,String cust_id) throws Exception{
		//参数不能为空
		if(StringHelper.isEmpty(cust_id)||pay.getExchange()==null
				||pay.getUsd()==null||pay.getKhr()==null||this.getBusiParam().getCust()==null
				||StringHelper.isEmpty(pay.getPay_type())
				){
			throw new ServicesException("参数不能为空");
		}
		//串数据判断
		if(!cust_id.equals(this.getBusiParam().getCust().getCust_id())){
			throw new ServicesException("客户不一致");
		}
		
		//验证汇率是否一致
		//List list=MemoryDict.getDicts(DictKey.EXCHANGE,DictKey.ex);
		Integer exchange=tExchangeDao.getExchange();
		if(exchange==null||exchange<=0||!exchange.equals(pay.getExchange())){
			throw new ServicesException("汇率未正确配置或汇率不一致");
		}

		//验证支付金额和待支付金额是否一致
		int payFee=pay.getUsd()+Math.round(pay.getKhr()*1.0f/exchange.intValue());
		int needPayFee=feeComponent.queryUnPaySum(cust_id,this.getOptr().getOptr_id()).get("FEE").intValue();
		if(upPayDoneCodes==null||upPayDoneCodes.size()==0||payFee!=needPayFee){
			throw new ServicesException("待支付金额已失效，请重新打开待支付界面");
		}
		//四舍五入部分
		pay.setCos((needPayFee-pay.getUsd())*exchange.intValue()-Math.round(pay.getKhr()*1.0f/exchange.intValue()));
	}
	/**
	 * 保存支付信息
	 * @param busiParam
	 * @param doneCode
	 * @return
	 * @throws Exception
	 */
	
	/**
	 * 更新产品订单的支付属性并发加授权
	 * @param unPayDoneCodes
	 * @param cust_id
	 * @param done_code
	 * @throws Exception
	 */
	private void updateOrder(String cust_id,Integer done_code) throws Exception{
		
		Set<Integer> doneCodeSet=new HashSet<Integer>();
		Map<String,CUser> userMap=userComponent.queryUserMap(cust_id);
		
		Map<CUser,List<CProdOrder>> atvMap=new HashMap<>();
		//查询待支付的订单(含套餐和套餐子产品)
		for(CProdOrder order:cProdOrderDao.queryUnPayOrder(cust_id)){ 
			//有订单的未支付业务流水号
			doneCodeSet.add(order.getDone_code());
			//订单状态是正常的才要授权
			if(StringHelper.isNotEmpty(order.getUser_id())
					&&order.getStatus().equals(StatusConstants.ACTIVE)){
				CUser user=userMap.get(order.getUser_id());
				if(user==null){
					throw new ServicesException(ErrorCode.OrderDateException,order.getOrder_sn());
				}
				List<CProdOrder> list=atvMap.get(user);
				if(list==null){
					list=new ArrayList<>();
					atvMap.put(user, list);
				}
				list.add(order);
			}
		}
		//更改订单支付属性
		Iterator<Integer> it= doneCodeSet.iterator();
		while(it.hasNext()){
			cProdOrderDao.updateOrderToPay(it.next(), cust_id);
		}
		//发授权
		for(CUser user:atvMap.keySet()){
			authComponent.sendAuth(user, atvMap.get(user),  BusiCmdConstants.ACCTIVATE_PROD, done_code);
		}
		
	}
	
	/**
	 * 根据用户类型查询一次性费用信息
	 * @param feeType 预收费
	 * @return
	 * @throws Exception
	 */
	public List<TBusiFee> queryUnitpreBusiFee() throws Exception {
		return feeComponent.queryUnitpreBusiFee();
	}

	/**
	 * 预付款
	 * @param dto
	 * @param optr
	 * @throws Exception
	 */
	public void saveFeeUnitpre(CFeeUnitpre dto,SOptr optr) throws Exception {
		//获取业务流水
//		Integer doneCode = doneCodeComponent.gDoneCode();
//		String busiCode = getBusiParam().getBusiCode();
//		CFee fee = feeComponent.saveFeeUnitpre(dto,optr,null,doneCode,busiCode);
//		String[] feeSns = {fee.getFee_sn()};
//		printComponent.saveDoc(feeComponent.queryAutoMergeFees(feeSns), null,
//				doneCode, getBusiParam().getBusiCode());
//
//		doneCodeComponent.saveDoneCode(doneCode,getBusiParam());
	}

	/**
	 * 查询同一地区类的所有预付款
	 * @param countyId
	 * @return
	 * @throws Exception
	 */
	public List<CFeeUnitpre> queryFeeUnitpre(String countyId) throws Exception {
		return feeComponent.queryFeeUnitpre(countyId);
	}


	public List<RInvoice> checkInvoice(String invoiceId,String docType,String invoiceMode)
			throws Exception {
		return invoiceComponent.checkInvoice(invoiceId, docType, invoiceMode);
	}
	
	public List<CFee> queryFeeByInvoice(String invoiceCode, String invoiceId,String custId)
			throws JDBCException {
		return feeComponent.queryFeeByInvoice(invoiceCode, invoiceId,custId);
	}

	/**
	 * 保存支付信息
	 */
	public void savePay(CFeePayDto pay, String[] feeSn) throws Exception {
		Integer doneCode = doneCodeComponent.gDoneCode();

		feeComponent.savePayFee(pay, feeSn, getBusiParam().getCust()
				.getCust_id(), doneCode);
		if (pay.getInvoice_mode().equals(SystemConstants.INVOICE_MODE_MANUAL)){
			feeComponent.saveManualInvoice(feeSn, pay
					.getInvoice_code(), pay.getInvoice_id(), pay
					.getInvoice_book_id());
			invoiceComponent.useInvoice(pay.getInvoice_code(), pay
					.getInvoice_id(), SystemConstants.INVOICE_MODE_MANUAL, 0);
		}
		//更新账目余额
//		payAcctFee(doneCode, feeSn, pay.getPay_type());
		
		saveAllPublic(doneCode,getBusiParam());
	}

	/**
	 * 冲正
	 */
	public void saveCancelFee(String feeSn) throws Exception {
		Integer doneCode = doneCodeComponent.gDoneCode();
		String busiCode = getBusiParam().getBusiCode();
		String custId = getBusiParam().getCust().getCust_id();
		// 查找费用信息
		CFee fee = feeComponent.queryBySn(feeSn);
		
		//如果是协议缴费，必须等销帐完成才能冲正
		if(BusiCodeConstants.ACCT_PAY_ZERO.equals(fee.getBusi_code())){
			JCustWriteoff jCustWriteOff = jobComponent.queryWriteOff(fee.getCreate_done_code());
			if(null != jCustWriteOff){
				throw new ServicesException("销账完成才能冲正");
			}
		}
		
		CFeePay feePay = feeComponent.queryCFeePaybyFeeSn(fee.getFee_sn());
//		if(StringHelper.isNotEmpty(feePay.getReceipt_id())){
//			//冲正流水对应的费用
//			List<CFee> feeList = feeComponent.queryByDoneCode(fee.getCreate_done_code());
//			for (CFee unitpreFee:feeList){
//				if (unitpreFee.getStatus().equals(StatusConstants.PAY))
//					cancelFee(doneCode, busiCode, unitpreFee);
//				else {
//					if (unitpreFee.getFee_type().equals(SystemConstants.FEE_TYPE_ACCT)){
//						cancelFee(doneCode, busiCode, unitpreFee);
//					}
//					feeComponent.removeFee(unitpreFee.getFee_sn());
//				}
//			}
//		}else 
		if(BusiCodeConstants.Unit_ACCT_PAY.equals(fee.getBusi_code())){//如果是单位批量缴费中的某一笔
			List<CFee> feeList = feeComponent.queryByDoneCode(fee.getCreate_done_code());
			for (CFee cFee : feeList) {
				cancelFee(doneCode, busiCode, cFee);
			}
		}else if(BusiCodeConstants.PROM_ACCT_PAY.equals(fee.getBusi_code())){//如果是套餐缴费中的某一笔
			//回退套餐缴费
			cancelPromPay(doneCode,fee.getCreate_done_code(),busiCode,custId);
			
		}else{
			cancelFee(doneCode, busiCode, fee);
		}
		//如果是银行批扣，需要把钱退回到客户的银行账户
		if (feePay.getPay_type().equals(SystemConstants.PAY_TYPE_BANK_DEDU)){
			CBankReturn bankRetrun = cBankReturnDao.findByKey(feePay.getReceipt_id());
			
			bankRetrun.setPay_status(StatusConstants.CANCEL);
			bankRetrun.setPay_failure_reason("缴费记录被冲正");
			cBankReturnDao.update(bankRetrun);
			
			CBankReturnPayerror bankError = new CBankReturnPayerror();
			BeanUtils.copyProperties(bankRetrun, bankError);
			bankError.setCreate_time(null);
			bankError.setCancel_done_code(""+doneCode);
			//查找扣款时的费用名称
			CBankGotodisk kf= cBankGotodiskDao.findByKey(bankRetrun.getTrans_sn());
			bankError.setBank_fee_name(kf.getBank_fee_name());
			cBankReturnPayerrorDao.save(bankError);
			
		}
		
		
		//创建返销帐任务
		jobComponent.createCustWriteOffJob(doneCode, custId, SystemConstants.BOOLEAN_FALSE);
		jobComponent.createAcctModeCalJob(doneCode, custId);

		saveAllPublic(doneCode,getBusiParam());
	}



	/**
	 * 修改费用
	 */
	public void editFee(int busiDoneCode, List<FeeBusiFormDto> feeList) throws Exception {
		Integer doneCode = doneCodeComponent.gDoneCode();
		String busiCode = getBusiParam().getBusiCode();
		//查找流水详细信息以及用户详细信息
		List<CDoneCodeDetail> detailList = doneCodeComponent.queryDetail(busiDoneCode);
		CCust cust = getBusiParam().getCust();
		List<CUser> userList = userComponent.queryUserByCustId(cust.getCust_id());
		for (int i=userList.size()-1;i>=0;i--){
			CUser user = userList.get(i);
			boolean flag=false;
			for (CDoneCodeDetail detail:detailList){
				if (user.getUser_id().equals(detail.getUser_id())){
					flag = true;
					break;
				}
			}
			
			if (!flag)
				userList.remove(i);
		}
		
		for (FeeBusiFormDto feeDto:feeList){
			if (feeDto.getFee_type().equals(SystemConstants.FEE_TYPE_BUSI)){
				String payType = SystemConstants.PAY_TYPE_CASH;
				if(null != getBusiParam().getPay()){
					payType = getBusiParam().getPay().getPay_type();
				}
				
				feeComponent.saveBusiFee(cust.getCust_id(), StringHelper.isEmpty(feeDto.getAddr_id())?getBusiParam().getCust().getAddr_id():feeDto.getAddr_id(),
						feeDto.getFee_id(), 1,payType, feeDto.getReal_pay(),
						doneCode, busiDoneCode, busiCode, userList,null);
			} else {
				//根据donecode和fee_id查找设备信息
				List<CFeeDevice> devices = feeComponent.queryDeviceByDoneCode(busiDoneCode);
				CFeeDevice device = null;
				if (devices.size()>0)
					device = devices.get(0);
				else
					device = new CFeeDevice();
				String payType = SystemConstants.PAY_TYPE_CASH;
				if (this.getBusiParam().getPay()!= null && this.getBusiParam().getPay().getPay_type() !=null)
					payType = this.getBusiParam().getPay().getPay_type();
				feeComponent.saveDeviceFee(cust.getCust_id(),cust.getAddr_id(), feeDto.getFee_id(), device.getFee_std_id(),payType,
						device.getDevice_type(), device.getDevice_id(),
						device.getDevice_code(), device.getPair_card_id(),
						device.getPair_card_code(), device.getPair_modem_id(),
						device.getPair_modem_code(),device.getDevice_model(), feeDto.getReal_pay(),
						doneCode,busiDoneCode, busiCode, feeDto.getBuy_num());
			}
		}

		saveAllPublic(doneCode,getBusiParam());
	}


	public void changeFeelistInvoice(String custId, String feelistId, String feelistCode,
			String feelistBookId, String invoiceId, String invoiceCode, String remark) throws Exception {
		Integer doneCode = doneCodeComponent.gDoneCode();
		invoiceComponent.changeFeelistInvoice(doneCode, getBusiParam()
				.getCust().getCust_id(), feelistId, feelistCode, feelistBookId,
				invoiceId, invoiceCode, remark);
		saveAllPublic(doneCode,getBusiParam());
	}
	
	public void invalidInvoice(String invoice_id, String invoice_code,
			String invoice_book_id)throws Exception{
		Integer doneCode = doneCodeComponent.gDoneCode();
		invoiceComponent.invalidInvoiceAndClearFeeInfo(doneCode, invoice_id, invoice_code);
	}
	
	public void invalidFeeListInvoice(Integer feeDoneCode) throws Exception {
		Integer doneCode = doneCodeComponent.gDoneCode();
		invoiceComponent.invalidFeeListInvoice(feeDoneCode);
		saveAllPublic(doneCode,getBusiParam());
	}


	/**
	 * 保存费用并合并打印项
	 * @throws Exception
	 */
	public void savePayAndMerge(CFeePayDto pay,String[] feeSn) throws Exception{
		savePay(pay, feeSn);
//		if (pay.getInvoice_mode().equals(SystemConstants.INVOICE_MODE_AUTO))
//			printComponent.saveDoc(feeComponent.queryAutoMergeFees(feeSn),
//				getBusiParam().getCust().getCust_id(), getBusiParam()
//						.getDoneCode(), getBusiParam().getBusiCode());
	}

	public CFee queryFeeInfo(String feeSn) throws Exception {
		return feeComponent.queryBySn(feeSn);
	}


	/**
	 * 查询客户下所有没有支付的费用项
	 */
	public List<FeeDto> queryUnPayFees(String custId)
			throws Exception {
		return feeComponent.queryUnPayFees(custId);
	}
	/**
	 * 查询指定客户下未合并的费用项
	 * @param custId 客户编号
	 */
	public List<MergeFeeDto> queryUnMergeFees(String custId)throws Exception {
		return feeComponent.queryUnMergeFees(custId);
	}

	public Map<String, Object> queryFeeView(String custId)throws Exception{
		return feeComponent.queryFeeView(custId);
	}

	public void saveDepositUnPay(String feeSn) throws Exception {
		Integer doneCode = doneCodeComponent.gDoneCode();
		String busiCode = getBusiParam().getBusiCode();
		//查找费用信息
		CFee fee = feeComponent.queryBySn(feeSn);
		if (fee.getFee_type().equals(SystemConstants.FEE_TYPE_BUSI)) {
			feeComponent.updateFeeDepositStatus(feeSn);
			CFeeBusi feeBusi = feeComponent.queryFeeBusiBySn(feeSn);
			String newfeesn = feeComponent.saveBusiFee(fee.getCust_id(),getBusiParam().getCust().getAddr_id(), fee.getFee_id(), fee
					.getReal_pay()
					* -1, doneCode, fee.getBusi_done_code(), busiCode, feeBusi);
			feeComponent.updateFeePayStatus(newfeesn);
		}

		saveAllPublic(doneCode,getBusiParam());
	}
	
	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.IPayService#editInvoiceMode(java.lang.String, java.lang.String)
	 */
	public void editInvoiceMode(String feeSn, String invoiceMode,
			CInvoiceDto oldInvoice,CInvoiceDto newInvoice,Integer realPay)
			throws Exception {
		Integer doneCode = doneCodeComponent.gDoneCode();
		
		invoiceComponent.editInvoiceMode(feeSn,invoiceMode, oldInvoice, newInvoice,realPay,doneCode);
		
		String optrId = getOptr().getOptr_id();
		//重载操作员未打印的费用
		List<String> feeSnList = feeComponent.queryUnPrintFeeByOptr(optrId);
		MemoryPrintData.reloadOptrFee(optrId, feeSnList);
		
		saveAllPublic(doneCode,getBusiParam());
	}
	
	/**
	 * 定额发票调账
	 */
	public void editInvoiceFee(String feeSn, int newInvoiceFee, String remark) throws Exception{
		Integer doneCode = doneCodeComponent.gDoneCode();
		
		feeComponent.editInvoiceFee(feeSn,newInvoiceFee);
		
		if(StringHelper.isNotEmpty(remark)){
			getBusiParam().setRemark(remark);
		}
		
		saveAllPublic(doneCode,getBusiParam());
	}
	
	/**
	 * 限制暂时去掉了.
	 * 当一个合同款是预收款合同时，修改账务日期时，要验证该支付记录对应的金额是否被使用，被使用则不允许修改.
	 * @param leftAmount
	 * @param list
	 * @return
	 * @throws Exception
	 */
	private boolean shouldEditAcctDate(Integer leftAmount,List<CGeneralContractPay> list) throws Exception{
		Integer allAmount = 0;
		for(CGeneralContractPay cp:list){
			allAmount += cp.getFee();
		}
		return allAmount >= leftAmount;
	}
	
	/**
	 * 如果合同收款修改账目日期,查看是否有分期付款信息,然后每个单笔的也要进行修改.
	 */
	public void editAcctDate(String feeSn, String newAcctDate, String oldAcctDate, String contractId, String leftAmountStr) throws Exception {
		
		if(StringHelper.isNotEmpty(contractId)){//合同款分期付款
			CGeneralContractPay contractFeeInfo = new CGeneralContractPay();
			contractFeeInfo.setFee_sn(feeSn);
			List<CGeneralContractPay> payInfo = acctComponent.queryPayInfo(contractId,null,null).getRecords();//有fee_sn
			payInfo.add(contractFeeInfo);
			String[] feeSns = CollectionHelper.converValueToArray(payInfo, "fee_sn");
			
			List<CFee> rawFeeList = feeComponent.queryFeeListBySns(feeSns);//原始值,还需要处理
			/*
			Map<String, CFee> feeMapedBySn = CollectionHelper.converToMapSingle(rawFeeList, "fee_sn");
			
			payInfo.remove(payInfo.size()-1);
			List<CFee> feeList = new ArrayList<CFee>();
			
			//预收款合同时
			Integer leftAmount = Integer.parseInt(leftAmountStr);
			
			if(rawFeeList.get(rawFeeList.size()-1).getFee_type().equals("UNITPRE")){
				int length = payInfo.size();
				for(int index = 0;index<length;index++){
					CGeneralContractPay cf = payInfo.get(index);
					if(shouldEditAcctDate(leftAmount,payInfo.subList(index, length))){
						feeList.add(feeMapedBySn.get(cf.getFee_sn()));
					}
				}
				feeList.add(feeMapedBySn.get(feeSn));
			}else{
				feeList = rawFeeList;
			}
			*/
			batchEditAcctDate(rawFeeList, newAcctDate);
			
		}else{
			Integer doneCode = doneCodeComponent.gDoneCode();
			
			invoiceComponent.editAcctDate(feeSn,newAcctDate);
			
			List<CFeePropChange> propList = new ArrayList<CFeePropChange>();
			CFeePropChange prop = new CFeePropChange();
			prop.setFee_sn(feeSn);
			prop.setColumn_name("acct_date");
			prop.setOld_value(oldAcctDate);
			prop.setNew_value(newAcctDate);
			prop.setDone_code(doneCode);
			propList.add(prop);
			
			feeComponent.saveFeePropChange(propList);
			
			
			saveAllPublic(doneCode,getBusiParam());
		}
	}
	
	/**
	 * 批量修改账目日期
	 * @param feeSns
	 * @param acctDate
	 * @throws Exception
	 */
	public void batchEditAcctDate(List<CFee> feeList, String acctDate) throws Exception {
		List<CFeePropChange> propList = new ArrayList<CFeePropChange>();
		for(CFee fee : feeList){
			Integer doneCode = doneCodeComponent.gDoneCode();
			doneCodeComponent.saveDoneCode(doneCode,BusiCodeConstants.EDIT_ACCT_DATE, "", fee.getDept_id(), 
					fee.getCounty_id(), fee.getArea_id(),fee.getCust_id(), null);
			CFeePropChange prop = new CFeePropChange();
			prop.setFee_sn(fee.getFee_sn());
			prop.setColumn_name("acct_date");
			prop.setOld_value(DateHelper.dateToStr(fee.getAcct_date()));
			prop.setNew_value(acctDate);
			prop.setDone_code(doneCode);
			propList.add(prop);
			
			fee.setAcct_date(DateHelper.strToDate(acctDate));
		}
		feeComponent.updateFees(feeList);
		feeComponent.saveFeePropChange(propList);
		
	}
	
	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.IPayService#queryValidFeeList(java.util.List)
	 */
	public void queryValidFeeList(List<PayDto> feeList) throws Exception {
		if(null != feeList && feeList.size() > 0){
			feeList.remove(0);
			if(feeList.size() > 1000){
				throw new ServicesException("表格数据请不要超过1000行");
			}else if(feeList.size() == 0 ){
				throw new ServicesException("表格数据是空的");
			}
		}else{
			throw new ServicesException("表格数据是空的");
		}
		
		invoiceComponent.queryValidFeeList(feeList);
		
	}
	
	/**
	 * 移动结帐
	 * @param payList
	 */
	public int saveCheckMobileBill(List<PayDto> payList) throws Exception{
		List<CFee> mobileFee = new ArrayList<CFee>();
		List<CProdMobileBill> mobileBillList = new ArrayList<CProdMobileBill>();
		List<CFeePropChange> propList = new ArrayList<CFeePropChange>();
		CFeePropChange prop = null;
		
		for (PayDto payDto : payList) {
			if(payDto.getFee() > 0){
				//查找设置费用信息
				List<CFee> feeList = feeComponent.queryByDoneCode(payDto.getDone_code());
				for (CFee fee : feeList) {
					if(fee.getUser_id().equals(payDto.getUser_id()) && fee.getAcctitem_id().equals(payDto.getAcctitem_id())
							&& fee.getReal_pay().equals(payDto.getFee()) && fee.getStatus().equals(SystemConstants.PAY_TYPE_UNPAY)){
						Integer doneCode = doneCodeComponent.gDoneCode();
						
						//记录异动
						prop = new CFeePropChange();
						prop.setFee_sn(fee.getFee_sn());
						prop.setColumn_name("pay_type");
						prop.setOld_value(fee.getPay_type());
						prop.setNew_value(SystemConstants.PAY_TYPE_CASH);
						prop.setDone_code(doneCode);
						propList.add(prop);
						
						prop = new CFeePropChange();
						prop.setFee_sn(fee.getFee_sn());
						prop.setColumn_name("status");
						prop.setOld_value(fee.getStatus());
						prop.setNew_value(StatusConstants.PAY);
						prop.setDone_code(doneCode);
						propList.add(prop);
						
						
						fee.setStatus(StatusConstants.PAY);
						fee.setPay_type(SystemConstants.PAY_TYPE_CASH);
						fee.setIs_doc(SystemConstants.BOOLEAN_TRUE);
						fee.setInvoice_id(payDto.getInvoice_id());
						fee.setInvoice_book_id(payDto.getInvoice_book_id());
						fee.setInvoice_mode(SystemConstants.INVOICE_MODE_MANUAL);
						fee.setInvoice_code(payDto.getInvoice_code());
						mobileFee.add(fee);
						
						//更新发票金额
						invoiceComponent.useInvoice(payDto.getInvoice_code(), payDto.getInvoice_id(), SystemConstants.INVOICE_MODE_MANUAL, payDto.getFee());
						
						//保存结账明细
						CProdMobileBill mobileBill = new CProdMobileBill();
						BeanUtils.copyProperties(payDto, mobileBill);
						mobileBill.setCreate_done_code(doneCode);
						mobileBillList.add(mobileBill);
						
						//修改账目余额,未结账资金转成现金
						acctComponent.changeAcctItemBanlance(doneCode, BusiCodeConstants.CHECK_MOBILE_BILL, 
								fee.getCust_id(), fee.getAcct_id(), fee.getAcctitem_id(), SystemConstants.ACCT_CHANGE_CHECKMOBILE, SystemConstants.ACCT_FEETYPE_UNPAY, -fee.getReal_pay(), null);
						acctComponent.changeAcctItemBanlance(doneCode, BusiCodeConstants.CHECK_MOBILE_BILL, 
								fee.getCust_id(), fee.getAcct_id(), fee.getAcctitem_id(), SystemConstants.ACCT_CHANGE_CHECKMOBILE, SystemConstants.ACCT_FEETYPE_CASH, fee.getReal_pay(), null);
						
						//销账任务
						jobComponent.createCustWriteOffJob(doneCode, fee.getCust_id(), SystemConstants.BOOLEAN_TRUE);
						
						//保存流水
						saveDoneCode(doneCode, BusiCodeConstants.CHECK_MOBILE_BILL, fee.getCust_id());
					}
				}
			}
		}
		//更新费用信息
		if(mobileFee.size() > 0){
			feeComponent.updateFees(mobileFee);
			feeComponent.saveFeePropChange(propList);
			feeComponent.saveMoblieBill(mobileBillList);
		}
		
		return mobileFee.size();
	}

	/* (non-Javadoc)
	 * 根据银行交易流水号查询C_Fee费用信息
	 * @see com.ycsoft.business.service.IPayService#queryFeeByBankTransCode(java.lang.String, java.lang.String)
	 */
	public List<CFee> queryFeeByBankTransCode(String startTransCode,String endTransCode,String countyId) throws JDBCException {
		// TODO Auto-generated method stub
		return feeComponent.queryFeeByBankTransCode(startTransCode,endTransCode,countyId);
	}
	
	public CFee editBusiOptr(String feeSn,String newBusiOptrId,String oldBusiOptrId) throws Exception {
		CFee fee = feeComponent.editBusiOptr(feeSn, newBusiOptrId);
		Integer doneCode = doneCodeComponent.gDoneCode();
		BusiParameter p = getBusiParam();
		p.setRemark("旧业务员：" + MemoryDict.getDictName(DictKey.OPTR, oldBusiOptrId)
				+ "   新业务员：" + (fee.getBusi_optr_name()==null?"":fee.getBusi_optr_name()));
		saveAllPublic(doneCode,p);
		return fee;
	}

	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.IPayService#editRemark(int, java.lang.String)
	 */
	public void editRemark(int doneCode, String remark) throws Exception {
		doneCodeComponent.editRemark(doneCode, remark);
		Integer d = doneCodeComponent.gDoneCode();
		BusiParameter p = getBusiParam();
		if(null != p)
			p.setRemark(String.valueOf(doneCode));
		saveAllPublic(d, p);
	}


	/**
	 * 标记打印
	 */
	public void savePrintStatus(String fee_sn) throws Exception {
		CFee fee = feeComponent.queryBySn(fee_sn);
		if(BusiCodeConstants.PROM_ACCT_PAY.equals(fee.getBusi_code())){
			feeComponent.updateIsDocByDoneCode(fee.getCreate_done_code(),SystemConstants.BOOLEAN_NO);
		}else{
			feeComponent.savePrintStatus(fee_sn);
		}
		
		String optrId = getOptr().getOptr_id();
		//重载操作员未打印的费用
		List<String> feeSnList = feeComponent.queryUnPrintFeeByOptr(optrId);
		MemoryPrintData.reloadOptrFee(optrId, feeSnList);
		
		Integer doneCode = doneCodeComponent.gDoneCode();
		BusiParameter p = getBusiParam();
		saveAllPublic(doneCode,p);
		
	}

	/**
	 * 取消标记
	 */
	public void saveCancelPrintStatus(String fee_sn) throws Exception {
		CFee fee = feeComponent.queryBySn(fee_sn);
		if(BusiCodeConstants.PROM_ACCT_PAY.equals(fee.getBusi_code())){
			feeComponent.updateIsDocByDoneCode(fee.getCreate_done_code(),SystemConstants.BOOLEAN_FALSE);
		}else{
			feeComponent.saveCancelPrintStatus(fee_sn);
		}
		
		String optrId = getOptr().getOptr_id();
		//重载操作员未打印的费用
		List<String> feeSnList = feeComponent.queryUnPrintFeeByOptr(optrId);
		MemoryPrintData.reloadOptrFee(optrId, feeSnList);
		
		Integer doneCode = doneCodeComponent.gDoneCode();
		BusiParameter p = getBusiParam();
		saveAllPublic(doneCode,p);
	}
	
	public void setCBankReturnDao(CBankReturnDao cBankReturnDao) {
		this.cBankReturnDao = cBankReturnDao;
	}

	public void setCBankReturnPayerrorDao(
			CBankReturnPayerrorDao cBankReturnPayerrorDao) {
		this.cBankReturnPayerrorDao = cBankReturnPayerrorDao;
	}

	public void setCBankGotodiskDao(CBankGotodiskDao cBankGotodiskDao) {
		this.cBankGotodiskDao = cBankGotodiskDao;
	}

}
