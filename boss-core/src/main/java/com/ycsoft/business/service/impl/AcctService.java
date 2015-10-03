/**
 *
 */
package com.ycsoft.business.service.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.ycsoft.beans.config.TBusiFee;
import com.ycsoft.beans.config.TCountyAcct;
import com.ycsoft.beans.config.TRuleDefine;
import com.ycsoft.beans.core.acct.CAcct;
import com.ycsoft.beans.core.acct.CAcctAcctitem;
import com.ycsoft.beans.core.acct.CAcctAcctitemAdjust;
import com.ycsoft.beans.core.acct.CAcctAcctitemChange;
import com.ycsoft.beans.core.acct.CAcctAcctitemInactive;
import com.ycsoft.beans.core.acct.CAcctAcctitemOrder;
import com.ycsoft.beans.core.acct.CAcctAcctitemThreshold;
import com.ycsoft.beans.core.acct.CAcctAcctitemThresholdProp;
import com.ycsoft.beans.core.acct.CAcctBank;
import com.ycsoft.beans.core.acct.CAcctPreFee;
import com.ycsoft.beans.core.acct.CGeneralAcct;
import com.ycsoft.beans.core.acct.CGeneralContract;
import com.ycsoft.beans.core.acct.CGeneralContractDetail;
import com.ycsoft.beans.core.acct.CGeneralContractPay;
import com.ycsoft.beans.core.acct.CGeneralCredential;
import com.ycsoft.beans.core.bank.CBankAgree;
import com.ycsoft.beans.core.bank.CBankPay;
import com.ycsoft.beans.core.bank.CBankRefundtodisk;
import com.ycsoft.beans.core.bank.CBankReturn;
import com.ycsoft.beans.core.bank.CBankReturnPayerror;
import com.ycsoft.beans.core.bill.BBill;
import com.ycsoft.beans.core.bill.BBillWriteoff;
import com.ycsoft.beans.core.common.CDoneCode;
import com.ycsoft.beans.core.cust.CCust;
import com.ycsoft.beans.core.fee.CFee;
import com.ycsoft.beans.core.prod.CProd;
import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.beans.invoice.RInvoice;
import com.ycsoft.beans.prod.PProdTariff;
import com.ycsoft.beans.prod.PPromFee;
import com.ycsoft.beans.system.SDept;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.business.commons.pojo.BusiParameter;
import com.ycsoft.business.component.config.ExpressionUtil;
import com.ycsoft.business.component.core.PrintComponent;
import com.ycsoft.business.dto.config.TemplateConfigDto;
import com.ycsoft.business.dto.core.acct.AcctAcctitemActiveDto;
import com.ycsoft.business.dto.core.acct.AcctAcctitemInvalidDto;
import com.ycsoft.business.dto.core.acct.AcctAcctitemThresholdDto;
import com.ycsoft.business.dto.core.acct.AcctDto;
import com.ycsoft.business.dto.core.acct.AcctitemDto;
import com.ycsoft.business.dto.core.acct.BankReturnDto;
import com.ycsoft.business.dto.core.acct.GeneralContractDto;
import com.ycsoft.business.dto.core.acct.PayDto;
import com.ycsoft.business.dto.core.acct.QueryAcctitemThresholdDto;
import com.ycsoft.business.dto.core.acct.UnitPayDto;
import com.ycsoft.business.dto.core.cust.CustFullInfoDto;
import com.ycsoft.business.dto.core.fee.CFeePayDto;
import com.ycsoft.business.dto.core.print.ConfirmPrintDto;
import com.ycsoft.business.dto.core.print.ConfirmPrintDto.ConfirmPrint;
import com.ycsoft.business.dto.core.prod.CProdDto;
import com.ycsoft.business.dto.core.prod.PromFeeProdDto;
import com.ycsoft.business.service.IAcctService;
import com.ycsoft.commons.constants.BusiCmdConstants;
import com.ycsoft.commons.constants.BusiCodeConstants;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ServicesException;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.commons.helper.DateHelper;
import com.ycsoft.commons.helper.JsonHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;

/**
 * @author liujiaqi
 */
@Service
public class AcctService extends BaseBusiService implements IAcctService {
	private ExpressionUtil expressionUtil;
	private PrintComponent printComponent;
	
	public void savePrePay(BusiParameter p,List<PayDto> payList) throws Exception {
		this.setParam(p);
		savePrePay(payList);
	}
	public void savePrePay(List<PayDto> payList) throws Exception {
		//获取业务流水
		if(null != payList && payList.size() > 0){
			Integer doneCode = doneCodeComponent.gDoneCode();
			String busiInfo = "";
			List<ConfirmPrint> confirmPrintList = new ArrayList<ConfirmPrint>();
			ConfirmPrintDto confirmPrintDto = new ConfirmPrintDto();
			int fees = 0;
			String busiCode = this.getBusiParam().getBusiCode();
			
			if(StringHelper.isNotEmpty(payList.get(0).getCust_id()) || 
					StringHelper.isEmpty(payList.get(0).getAcct_id())){
				//单位批量缴费或者文件批量缴费
				
				List<String> custIdList = new ArrayList<String>();
				//保存缴费信息
				for (PayDto payFee:payList){
					CProd prod = userProdComponent.queryByProdSn(payFee.getProd_sn());
					
					if (StringHelper.isEmpty(payFee.getAcct_id())){
						//文件批量缴费
						payFee.setCust_id(prod.getCust_id());
						payFee.setAcct_id(prod.getAcct_id());
						payFee.setAcctitem_id(prod.getProd_id());
						payFee.setUser_id(prod.getUser_id());
						payFee.setTariff_id(prod.getTariff_id());
						payFee.setInvalid_date(DateHelper.dateToStr(prod.getInvalid_date()));
					}
					
					//批量收费功能
					if(busiCode.equals(BusiCodeConstants.BATCH_FILE_ACCT_PAY)){
						CustFullInfoDto custFullDto = new CustFullInfoDto();
						CCust ccust = new CCust();
						ccust.setCust_id(prod.getCust_id());
						custFullDto.setCust(ccust);
						this.getBusiParam().setCustFullInfo(custFullDto);
						
						//只有赠送金额，修改支付方式为赠送
						if(payFee.getFee() == 0 && payFee.getPresent_fee() > 0 ){
							CFeePayDto payDto = new CFeePayDto();
							payDto.setPay_type(SystemConstants.PAY_TYPE_PRESENT);
							this.getBusiParam().setPay(payDto);
							
							payFee.setFee(payFee.getPresent_fee());
							payFee.setPresent_fee(0);
						}
					}
					
					//保存缴费记录
					if (payFee.getFee()>0 || payFee.getPresent_fee() > 0){
						
						payFee.setBegin_date(payFee.getInvalid_date());
						this.saveAcctPay(doneCode, payFee);
						
						//产品原来的订购方式不是订购
						if(prod !=null){
							//修改公用账目使用类型
							changeNoneToAll(doneCode, prod);
						}
						
						//保存缴费信息
						if (StringHelper.isNotEmpty(payFee.getInvoice_id())) {
							//文件批量
							CFeePayDto payDto = new CFeePayDto();
							payDto.setPay_type(SystemConstants.PAY_TYPE_CASH);
							payDto.setPayer(payFee.getCust_name());
							feeComponent.savePayFee(payDto, payFee.getCust_id(),doneCode);
							
							//使用发票
							feeComponent.saveManualInvoice(doneCode, payFee
									.getInvoice_code(), payFee.getInvoice_id(), payFee
									.getInvoice_book_id());
							invoiceComponent.useInvoice(payFee.getInvoice_code(),payFee.getInvoice_id(), 
									SystemConstants.INVOICE_MODE_MANUAL, payFee.getFee());
							
						}else if (getBusiParam().getPay()!=null){
						}
						fees += payFee.getFee();	//确认单打印 总金额项
					}
					
					//清除客户临时信用度
					acctComponent.clearTempThreshold(payFee.getCust_id());
					//生成销账任务
					jobComponent.createCustWriteOffJob(doneCode, payFee.getCust_id(), SystemConstants.BOOLEAN_TRUE);
					
					//防止同个客户多次生成计算到期日任务
					if(!custIdList.contains(payFee.getCust_id())){
						//生成计算到期日任务
						jobComponent.createInvalidCalJob(doneCode, payFee.getCust_id());
						jobComponent.createAcctModeCalJob(doneCode, payFee.getCust_id());
						
						custIdList.add(payFee.getCust_id());
					}
					//批量文件收费，为每个客户添加受理记录
					if(busiCode.equals(BusiCodeConstants.BATCH_FILE_ACCT_PAY)){
						saveAllPublic(doneCode,this.getBusiParam());
						doneCode = doneCodeComponent.gDoneCode();
					}
					
					this.acctivateBandProd(doneCode, prod);
					
				}
				custIdList = null;
				
				//防止拦截器再次调用
//				getBusiParam().setPay(null);
				
				//确认单打印 取打印项目名
				String printItemId = acctComponent.queryPrintItemId(payList.get(0).getAcctitem_id());
				confirmPrintDto.setPrintitem_name(MemoryDict.getDictName(
						DictKey.PRINTITEM_NAME, printItemId));
				confirmPrintDto.setSum_fee(fees);
				
			}else{
				//单客户缴费
				//获取客户信息
				String custId = getBusiParam().getCust().getCust_id();
				
				List<String> userIds = new ArrayList<String>();//用户编号
				boolean includePub = false;//是否包含公用账目缴费
				
				//保存缴费信息
				for (PayDto pay:payList){
					CProd prod = userProdComponent.queryByProdSn(pay.getProd_sn());
					
					//产品原来的订购方式不是订购
					if(prod !=null){
						//修改公用账目使用类型
						changeNoneToAll(doneCode, prod);
						
						if(prod.getProd_type().equals(SystemConstants.PROD_TYPE_CUSTPKG)){
							List<CProd> childrenProd = userProdComponent.queryProdByPackageProd(custId, prod.getProd_id());
							for(CProd cp : childrenProd){
								if(!userIds.contains(cp.getUser_id())){
									userIds.add(cp.getUser_id());
								}
							}
						}
					}
					
					
					
					//保存缴费记录
					if (pay.getFee()>0){
						
						//缴费项目包含公用账目
						if(pay.getAcctitem_id().equals(SystemConstants.ACCTITEM_PUBLIC_ID)){
							includePub = true;
						}
						if(StringHelper.isNotEmpty(pay.getUser_id())){
							userIds.add(pay.getUser_id());
							
							
						}
						
						pay.setBegin_date(pay.getInvalid_date());
						this.saveAcctPay(doneCode, pay);
						
						//单客户缴费
						if(busiCode.equals(BusiCodeConstants.ACCT_PAY)){
							ConfirmPrint confirmPrint = confirmPrintDto.new ConfirmPrint();
							if(StringHelper.isNotEmpty(pay.getUser_id())){
								//确认单打印
								CUser userDto = userComponent.queryUserById(pay.getUser_id());
								confirmPrint.setUser_name(userDto.getUser_name());
								confirmPrint.setUser_type(userDto.getUser_type());
								confirmPrint.setUser_type_text(userDto.getUser_type_text());
								confirmPrint.setTerminal_type(userDto.getTerminal_type());
								confirmPrint.setTerminal_type_text(userDto.getTerminal_type_text());
							}else{
								confirmPrint.setTerminal_type("");
							}
							String printItemId = acctComponent.queryPrintItemId(pay.getAcctitem_id());
							//确认单打印 取打印项目名
							confirmPrint.setPrintitem_name(MemoryDict.getDictName(
									DictKey.PRINTITEM_NAME, printItemId));
							confirmPrint.setFee(pay.getFee());
							confirmPrintList.add(confirmPrint);
							doneCodeComponent.saveDoneCodeInfo(doneCode, getBusiParam().getCust().getCust_id(),pay.getUser_id(), new HashMap());
						}
						
						
						fees += pay.getFee();	//确认单打印 总金额项
					}
					
					this.acctivateBandProd(doneCode, prod);
				}
				List<CUser> userList = new ArrayList<CUser>();
				//如果缴费项目包括公用账目，则遍历所有用户
				if(includePub){
					userList = userComponent.queryUserByCustId(custId);
				}else{
					if(userIds.size() > 0){
						userList = userComponent.queryUserByUserIds(userIds);
					}
				}
				
				for(CUser user : userList){
					recoverUserStatus(user, doneCode);
				}
				
				//清除客户临时信用度
				acctComponent.clearTempThreshold(custId);
				//生成销账任务
				jobComponent.createCustWriteOffJob(doneCode, custId, SystemConstants.BOOLEAN_TRUE);
				jobComponent.createAcctModeCalJob(doneCode, custId);
				//生成计算到期日任务
				jobComponent.createInvalidCalJob(doneCode, custId);
				
				//单客户缴费
				if(busiCode.equals(BusiCodeConstants.ACCT_PAY)){
					//确认单打印
					Collections.sort(confirmPrintList, new Comparator<ConfirmPrint>(){
						public int compare(ConfirmPrint o1, ConfirmPrint o2) {
							if( StringHelper.isEmpty(o1.getTerminal_type()) )
								return -1;
							if( StringHelper.isNotEmpty(o2.getTerminal_type()) )
								return o2.getTerminal_type().compareTo(o1.getTerminal_type());
							else
								return 1;
						}
						
					});
					int count = confirmPrintList.size();
					for(int i=0;i<count;i++){
						String terminalType = confirmPrintList.get(i).getTerminal_type();
						if(StringHelper.isNotEmpty(terminalType)){
							for(int j=i+1;j<count;j++){		//设置后面相同终端下的产品对象中终端类型为空
								if(terminalType.equals(confirmPrintList.get(j).getTerminal_type())){
									confirmPrintList.get(j).setTerminal_type_text("");
								}
							}
						}
					}
					
					confirmPrintDto.setData(confirmPrintList);
					confirmPrintDto.setCount(count);
				}
				
			}
			
			//批量文件收费，为每个客户添加受理记录
			if(!busiCode.equals(BusiCodeConstants.BATCH_FILE_ACCT_PAY)){
				//批量缴费
				if(busiCode.equals(BusiCodeConstants.BATCH_ACCT_PAY)){
					String printItemId = acctComponent.queryPrintItemId(payList.get(0).getAcctitem_id());
					//确认单打印 取打印项目名
					confirmPrintDto.setPrintitem_name(MemoryDict.getDictName(
							DictKey.PRINTITEM_NAME, printItemId));
				}
				confirmPrintDto.setSum_fee(fees);
				busiInfo = JsonHelper.fromObject(confirmPrintDto);
				getBusiParam().setBusiConfirmParam("info", busiInfo);
//				saveAllPublic(doneCode,getBusiParam(),busiInfo);
				saveAllPublic(doneCode,getBusiParam());
			}
		}
	}
	
	
	public List<PayDto> calcBatchPayFees(List<PayDto> payList, Date newInvalidDate,String custId) throws Exception{
		if(CollectionHelper.isEmpty(payList)){
			return payList;
		}
		List<CProdDto> allProds = userProdComponent.queryProdByCustId(custId);
		Map<String, CProdDto> prodMappedByProdSn = CollectionHelper.converToMapSingle(allProds, "prod_sn");
		
		for(PayDto dto:payList){
			CProdDto cProdDto = prodMappedByProdSn.get(dto.getProd_sn());
			long feeByInvalidDatePro = userProdComponent.getFeeByInvalidDatePro(cProdDto.getProd_sn(), newInvalidDate);
			dto.setFee((int)feeByInvalidDatePro);
		}
		return payList;
	}
	
	private void acctivateBandProd(Integer doneCode, CProd prod) throws Exception {
		if(prod != null){
			if(StringHelper.isNotEmpty(prod.getUser_id())){
				CUser userDto = userComponent.queryUserById(prod.getUser_id());
				if(userDto != null && userDto.getUser_type().equals(SystemConstants.USER_TYPE_BAND)){
					jobComponent.createBusiCmdJob(doneCode,
							BusiCmdConstants.ACCTIVATE_PROD, userDto.getCust_id(), userDto.getUser_id(), 
							null, null, userDto.getModem_mac(), prod.getProd_sn(),prod.getProd_id());
				}
			}else{
				if(prod.getProd_type().equals(SystemConstants.PROD_TYPE_CUSTPKG)){
					List<CProd> childProdList = userProdComponent.queryByPkgSn(prod.getProd_sn());
					for(CProd childProd : childProdList){
						CUser userDto = userComponent.queryUserById(childProd.getUser_id());
						if(userDto != null && userDto.getUser_type().equals(SystemConstants.USER_TYPE_BAND)){
							jobComponent.createBusiCmdJob(doneCode,
									BusiCmdConstants.ACCTIVATE_PROD, userDto.getCust_id(), userDto.getUser_id(), 
									null, null, userDto.getModem_mac(), childProd.getProd_sn(),childProd.getProd_id());
						}
					}
				}
			}
		}
	}
	
	public void saveSinglePay(String acctId,String[] acctitemId, Integer fee[],CBankPay bankPay) throws Exception {
		// 获取客户信息
		CCust cust = getBusiParam().getCust();
		// 获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		String busiCode = getBusiParam().getBusiCode();
		// 保存缴费信息
		
		for (int i = 0 ;i<acctitemId.length;i++){
			PayDto pay = new PayDto();
			pay.setAcct_id(acctId);
			pay.setAcctitem_id(acctitemId[i]);
			pay.setFee(fee[i]);
			
			this.saveAcctPay(doneCode, pay);
		}
		
		List<CUser> userList = userComponent.queryUserByCustId(cust.getCust_id());
		for(CUser user : userList){
			recoverUserStatus(user, doneCode);
		}
		
		if(bankPay != null){
			bankPay.setDone_code(doneCode);
			feeComponent.saveBankPay(bankPay);
		}
		//清除客户临时信用度
		acctComponent.clearTempThreshold(cust.getCust_id());
		//生成销账任务
		jobComponent.createCustWriteOffJob(doneCode, cust.getCust_id(), SystemConstants.BOOLEAN_TRUE);
		jobComponent.createAcctModeCalJob(doneCode, cust.getCust_id());
		//生成计算到期日任务
		jobComponent.createInvalidCalJob(doneCode, cust.getCust_id());
		
//		printComponent.saveDoc(feeComponent.queryAutoMergeFees(doneCode,cust.getCust_id()), cust
//			.getCust_id(), doneCode, busiCode);

		saveAllPublic(doneCode,getBusiParam());
	}
	
	public int saveBankPk(String bankTransSn, String acctId,String custId,
			String acctitemId, Date beginDate,Date endDate, int fee,String userId,String prodSn) throws Exception {
		Integer doneCode = doneCodeComponent.gDoneCode();
		String busiCode = getBusiParam().getBusiCode();
		getBusiParam().setPay(new CFeePayDto());
		getBusiParam().getPay().setPay_type(SystemConstants.PAY_TYPE_BANK_DEDU);
		getBusiParam().getPay().setReceipt_id(bankTransSn);
		//获取客户信息
		CCust cust = custComponent.queryCustById(custId);
		if (null == cust)
			throw new ServicesException("客户已经销户，退款至银行");
		
		CUser userdto = userComponent.queryUserById(userId);
		if (userdto==null)
			throw new ServicesException("用户已经销户，退款至银行");
		if (userdto.getStatus().equals(StatusConstants.REQSTOP)){
			//TODO 这里需要判断是否有欠费账单
			throw new ServicesException("用户已经报停，退款至银行");
		}
		
		CustFullInfoDto custFullInfo = new CustFullInfoDto();
		custFullInfo.setCust(cust);
		getBusiParam().setCustFullInfo(custFullInfo);
		

		//保存缴费信息
		PayDto pay= new PayDto();
		if (fee>0){
			CProd cprod = userProdComponent.queryByProdSn(prodSn);
			if (null == cprod)
				throw new ServicesException("对应产品"+prodSn+"项未找到,产品被退订，或者用户被销户");
			pay.setCust_id(custId);
			pay.setAcct_id(acctId);
			pay.setUser_id(userId);
			pay.setProd_sn(prodSn);
			pay.setAcctitem_id(acctitemId);
			pay.setFee(fee);
			pay.setBegin_date(DateHelper.dateToStr(cprod.getInvalid_date()));
			pay.setInvalid_date(DateHelper.dateToStr(endDate));
			this.saveAcctPay(doneCode, pay);
		}
		//更新银行回盘处理成功信息
		CBankReturn cbr2 = new CBankReturn();
		cbr2.setBank_trans_sn(bankTransSn);
		cbr2.setPay_status("SUCCESS");
		cbr2.setPay_done_code(doneCode); // 保存缴费自动注入;
		acctComponent.updateBankReturn(cbr2);
		
		recoverUserStatus(userdto, doneCode);
		//清除客户临时信用度
		acctComponent.clearTempThreshold(custId);
		//生成销账任务
		jobComponent.createCustWriteOffJob(doneCode, custId, SystemConstants.BOOLEAN_TRUE);
		
		jobComponent.createAcctModeCalJob(doneCode, custId);
		
		//生成计算到期日任务
		jobComponent.createInvalidCalJob(doneCode, custId);
		
		printComponent.saveDoc(feeComponent.queryAutoMergeFees(doneCode,custId), 
				custId, doneCode, busiCode);

		saveAllPublic(doneCode,getBusiParam());
		return doneCode;
	}
	/**
	 * 支付平台网账目充值
	 * @param payList
	 * @param bankPay
	 * @throws Exception
	 */
	public void saveBankProdPay(List<PayDto> payList,CBankPay bankPay) throws Exception{
		Integer doneCode = doneCodeComponent.gDoneCode();
		String busiCode = getBusiParam().getBusiCode();
		//获取客户信息
		String custId = getBusiParam().getCust().getCust_id();
		
		//保存缴费信息
		for (PayDto pay:payList){
			CProd prod = userProdComponent.queryByProdSn(pay.getProd_sn());
			
			//产品原来的订购方式不是订购
			if(prod ==null){
				throw new ServicesException("产品"+pay.getProd_sn()+"不存在");
			}
			//修改公用账目使用类型
			changeNoneToAll(doneCode, prod);
			pay.setUser_id(prod.getUser_id());
			pay.setAcct_id(prod.getAcct_id());
			pay.setAcctitem_id(prod.getProd_id());
			//保存缴费记录
			if (pay.getFee()>0){
				pay.setBegin_date(DateHelper.dateToStr(prod.getInvalid_date()));
				this.saveAcctPay(doneCode, pay);
			}
		}
		
		List<CUser> userList = userComponent.queryUserByCustId(custId);
		for(CUser user : userList){
			recoverUserStatus(user, doneCode);
		}
		
		bankPay.setDone_code(doneCode);
		feeComponent.saveBankPay(bankPay);
		
		//清除客户临时信用度
		acctComponent.clearTempThreshold(custId);
		//生成销账任务
		jobComponent.createCustWriteOffJob(doneCode, custId, SystemConstants.BOOLEAN_TRUE);
		
		jobComponent.createAcctModeCalJob(doneCode, custId);
		
		//生成计算到期日任务
		jobComponent.createInvalidCalJob(doneCode, custId);
		
		printComponent.saveDoc(feeComponent.queryAutoMergeFees(doneCode,custId), 
				custId, doneCode, busiCode);

		saveAllPublic(doneCode,getBusiParam());
	}
	
	/**
	 * 协议缴费
	 */
	public void savePay(String prodSn, int fee, int months,Date beginDate,Date invalidDate) throws Exception {
		//获取客户信息
		CCust cust = getBusiParam().getCust();
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		//查找产品信息
		CProd prod = userProdComponent.queryByProdSn(prodSn);
		if (fee>0){
			
			//保存缴费信息
			PayDto pay = new PayDto();
			pay.setUser_id(prod.getUser_id());
			pay.setAcct_id(prod.getAcct_id());
			pay.setAcctitem_id(prod.getProd_id());
			pay.setFee(fee);
			pay.setTariff_id(prod.getTariff_id());
			pay.setProd_sn(prodSn);
			pay.setBegin_date(DateHelper.dateToStr(prod.getInvalid_date()));
			
			this.saveAcctPay(doneCode, pay);
			//计算到期日并更新
			invalidDate = userProdComponent.getDate(beginDate, months, 0);
			userProdComponent.updateInvalidDate(doneCode, prodSn, invalidDate);
			List<CFee> feeList = feeComponent.queryByDoneCode(doneCode);
			if(feeList != null && feeList.size() > 0){
				//更新缴费的到期日
				feeComponent.updateInvalidDate(feeList.get(0).getFee_sn(),prod.getProd_sn(),invalidDate);
			}
			//修改帐目欠费
			acctComponent.changeAcctItemOwefee(false, prod.getAcct_id(), prod.getProd_id(), fee);
			
			//生成账单信息
			int billFee=fee/months;
			for (int i=0;i<months;i++){
				String billingCycle = DateHelper.format(beginDate, DateHelper.FORMAT_YM);
				if (i == months -1)
					billFee = fee - (months -1)*billFee;
				billComponent.createBill(prod, doneCode, billingCycle, billFee,billFee, SystemConstants.BILL_COME_FROM_MANUAL);
				beginDate = DateHelper.getNextMonth(beginDate);
			}
			//生成销帐任务
			jobComponent.createCustWriteOffJob(doneCode, cust.getCust_id(),SystemConstants.BOOLEAN_TRUE);
		} else {
			//修改到期日,并修改产品状态为正常
			userProdComponent.updateInvalidDateStatus(doneCode, prodSn, invalidDate,BusiCodeConstants.ACCT_PAY_ZERO);
			jobComponent.createCreditExecJob(doneCode, cust.getCust_id());
		}
		
		//如果产品状态隔离
		if(null != prod && StatusConstants.ISOLATED.equals(prod.getStatus())){
			CUser user = userComponent.queryUserById(prod.getUser_id());
			recoverUserStatus(user, doneCode);
		}

		saveAllPublic(doneCode,getBusiParam());
	}
	
	public List<CAcctAcctitemInactive> queryPromAcctItemInactive(Integer doneCode, String custId, boolean fromHistory) throws Exception{
		return acctComponent.queryPromAcctItemInactive(custId,doneCode,fromHistory);
	}
	
	public void cancelPromFee(String promFeeSn, String reason) throws Exception{
		Integer doneCode = doneCodeComponent.gDoneCode();
		getBusiParam().setRemark(reason);
		feeComponent.cancelPromFee(doneCode,promFeeSn,getBusiParam().getCustFullInfo().getCust().getCust_id());
		saveAllPublic(doneCode, getBusiParam());
	}
	
	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.IAcctService#savePromPayFee(java.lang.String, int, java.util.List)
	 */
	public void savePromPayFee(String promFeeId, int promFee,
			List<PromFeeProdDto> prodList, String preOpenTime) throws Exception {
		/*
		
		
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
//		String busiCode = getBusiParam().getBusiCode();
		CCust cust = getBusiParam().getCust();
		String custId = getBusiParam().getCust().getCust_id();
		
		//保存促销缴费,记录产品促销信息
		CPromFee cPromFee= feeComponent.savePromPay(doneCode,custId,promFeeId,promFee,prodList);
		PPromFee promFeeInfo = userComponent.queryPromFeeSimpleInfo(promFeeId);
		getBusiParam().getBusiConfirmParamInfo().put("prom_fee", promFeeInfo);
		
		//根据user_id,prod_id 查找并更新对应的prod_sn,rent,tariff_id,prod_id,billing_cycle,should_pay等信息。
		//宽带产品自动匹配装入prod_id,返回的map格式Map<user_id,Map<prod_id,PromFeeProdDto>>
		Map<String, Map<String, PromFeeProdDto>> promMap=userProdComponent.queryPromFeeProdSn(custId, getOptr().getCounty_id(), prodList);
		//绑定产品清单
		List<PromFeeProdDto> bindList=new ArrayList<PromFeeProdDto>();
		
		//获取产品用户特殊资源
		List<PProdUserRes> userResList = prodComponent.queryUserResByCountyId();
		
		
		//生效日期
		String feeDate = StringHelper.isNotEmpty(preOpenTime) ? preOpenTime : DateHelper.getDate("-");
		Date preOpenDate = StringHelper.isNotEmpty(preOpenTime) ? DateHelper.strToDate(preOpenTime) : null;
		
		//标记用户十分已经判断过有产品(只要有产品就有基本包)
		Map<String, Boolean> userProdIsEmptyMap = new HashMap<String, Boolean>();
		//产品账目充值
		Map<String, PProd> prodMap = new HashMap<String, PProd>();
		boolean hasBaseProd = false;//标记：如果当前套餐是否包含包含基本产品
		for(PromFeeProdDto promProd : prodList){
			String prod_id = promProd.getProd_id();
			PProd prod = prodComponent.queryProdById(prod_id);
			prodMap.put(prod_id, prod);
			if(prod.getIs_base().equalsIgnoreCase(SystemConstants.BOOLEAN_TRUE)){
				hasBaseProd = true;
			}
		}
		List<UserDto> related_user_list = new ArrayList<UserDto>();
		List<String> rulids = new ArrayList<String>();
		for(PromFeeProdDto promProd : prodList){
			String prodSn = promProd.getProd_sn();;
			//如果产品未订购
			if(StringHelper.isEmpty(promProd.getProd_sn())){
				//用户还没有订购此产品
				PProd prod = prodMap.get(promProd.getProd_id());
				PProdTariff newTariff = prodComponent.queryTariffById(promProd.getTariff_id());
				//装入资费金额
				promProd.setRent(newTariff.getRent());
				promProd.setBilling_cycle(newTariff.getBilling_cycle());
				
				UserDto user = userComponent.queryUserById(promProd.getUser_id());
				
				//用户下十分以后产品
				String user_id = user.getUser_id();
				if(userProdIsEmptyMap.get(user_id) == null && !hasBaseProd){//只判断一次,判断过必定不为空 
					List<CProdDto> userProds = userProdComponent.queryByUserId(user_id);
					Boolean userHaveProd = CollectionHelper.isNotEmpty(userProds);
					if(!userHaveProd){//外层已经判断了套餐里没有基本产品,如用户本身也没有已经订购基本产品,则跑出异常
						String cardId = user.getCard_id();
						String baseInfo = user.getTerminal_type_text() ;
						if(StringHelper.isNotEmpty(cardId)){
							baseInfo += "-" + StringHelper.delStartChar(cardId, cardId.length() -4);
						}
						throw new ServicesException( baseInfo + " 必须先订基础节目才能参加套餐缴费!");
					}
					userProdIsEmptyMap.put(user_id, userHaveProd);
				}
				
				//判断该产品这个用户能否订购
				if(SystemConstants.PROD_SERV_ID_ATV.equals(prod.getServ_id())){
					//模拟产品
					if(!user.getUser_type().equals(SystemConstants.USER_TYPE_ATV))
						throw new ServicesException("非模拟用户:"+prod.getProd_name()+"不适用"+promProd.getUser_name());
				}else if(SystemConstants.PROD_SERV_ID_BAND.equals(prod.getServ_id())){
					//宽带产品
					if(!user.getUser_type().equals(SystemConstants.USER_TYPE_BAND))
						throw new ServicesException("非宽带用户:"+prod.getProd_name()+"不适用"+promProd.getUser_name());
				}else if(SystemConstants.PROD_SERV_ID_ITV.equals(prod.getServ_id())){
					//双向互动
					if(!user.getUser_type().equals(SystemConstants.USER_TYPE_DTV)||!user.getServ_type().equals(SystemConstants.DTV_SERV_TYPE_DOUBLE))
						throw new ServicesException("非双向互动用户:"+prod.getProd_name()+"不适用"+promProd.getUser_name());
				}else if(SystemConstants.PROD_SERV_ID_DTV.equals(prod.getServ_id())){
					if(!user.getUser_type().equals(SystemConstants.USER_TYPE_DTV))
						throw new ServicesException("非数字用户:"+prod.getProd_name()+"不适用"+promProd.getUser_name());
				}else if(SystemConstants.PROD_SERV_ID_RENT.equals(prod.getServ_id())){
					
				}else{
					throw new ServicesException("套餐缴费未识别该产品服务类型"+prod.getServ_id());
				}
					
				
				if (prod!=null && null!=newTariff){
					String stopByInvalidDate = prodComponent.stopByInvaliddate(prod, newTariff);
					
					if (prod.getProd_type().equals(SystemConstants.PROD_TYPE_BASE)){
						prodSn = userProdComponent.addProd(doneCode,custId, user.getAcct_id(), user_id,null,null,
							prod.getProd_id(), prod.getProd_type(), SystemConstants.PROD_ORDER_TYPE_PRESENT,
							feeDate,null,user.getStop_type(), newTariff,  null,stopByInvalidDate,prod.getIs_base(),preOpenDate,prod.getIs_bank_pay());
						//如果存在动态资源处理
						if(promProd.getResList()!=null&&promProd.getResList().size()>0){
							userProdComponent.savePromFeeProdDynRes(prodSn, promProd.getResList());
						}
					} else {
						List<PPackageProd> childList = prodComponent.queryPackageProd(prod.getProd_id(),newTariff.getTariff_id());
						prodSn = userProdComponent.addPackage(doneCode,custId, user.getAcct_id(), user_id, prod.getProd_id(), prod.getProd_type(), SystemConstants.PROD_ORDER_TYPE_PRESENT
								,feeDate,null,  user.getStop_type(),newTariff, childList, null,stopByInvalidDate,prod.getIs_base(),preOpenDate,SystemConstants.BOOLEAN_FALSE);
					}
					
					
					expressionUtil.setCcust(cust);
					expressionUtil.setCuser(user);
					for (PProdUserRes userRes:userResList){
						if (userRes.getProd_id().equals(prod.getProd_id())){
							if (StringHelper.isEmpty(userRes.getRule_id_text()) 
									|| expressionUtil.parseBoolean(userRes.getRule_id_text())){
								String[] res = userRes.getRes_id().split(",");
								for (String resId:res){
									userProdComponent.addUserProdres(prodSn, resId);
								}
							}
						}
					}
					
					//创建产品账目
					acctComponent.createAcctItem(user.getAcct_id(), prod.getProd_id());
					
					//插入预开通记录
					if(preOpenDate != null){
						//生成预开通JOB等待执行
						jobComponent.createPreAuthCmdJob(doneCode,prodSn,preOpenDate,user.getArea_id(),user.getCounty_id());
					}else{
						jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.ACCTIVATE_PROD, cust.getCust_id(),
								user_id, user.getStb_id(), user.getCard_id(), user.getModem_mac(), prodSn,prod.getProd_id());
					}
					
				} 
				//处理多个规则给同一个用户使用的情况下，多个规则包含了同样的产品
				for(PromFeeProdDto prom : prodList){
					if(prom.getUser_id().equals(promProd.getUser_id()) && prom.getProd_id().equals(promProd.getProd_id()) ){
						prom.setProd_sn(prodSn);
						prom.setTariff_id(promProd.getTariff_id());
						prom.setRent(promProd.getRent());
						prom.setBilling_cycle(promProd.getBilling_cycle());
						prom.setShould_pay(prom.getRent()*prom.getMonths()/prom.getBilling_cycle());
					}
				}
			}
			
			CProd prod = userProdComponent.queryByProdSn(prodSn);
			if(prod==null)
				throw new ServicesException("系统错误：产品未成功订购.");
			//实缴大于（资费*月数/周期）情况下，插入额外账单，如果是包多月，则直接使用实缴插入额外账单
			int more_fee=0;
			if(promProd.getBilling_cycle()>1&&promProd.getRent()>0){//包多月情况
				more_fee=promProd.getReal_pay();
			}else {//非包多月
				PProdTariff tariff = prodComponent.queryTariffById(prod.getTariff_id());
				//包月计费模式要额外生成账单
				if(tariff.getBilling_type().equals(SystemConstants.BILLING_TYPE_MONTH)){
					more_fee=(int) (promProd.getReal_pay()- promProd.getRent()*promProd.getMonths()*1.0/promProd.getBilling_cycle());
				}
			}
			if(more_fee>0){
				if (promProd.getMonths()==0 ){
					String billingCycle = DateHelper.format(
							DateHelper.getNextMonthByNum(prod.getInvalid_date().after(new Date())?prod.getInvalid_date():new Date(),0)
							, DateHelper.FORMAT_YM);
					billComponent.createPromFeeBill(prod, doneCode, billingCycle, more_fee, more_fee);
					acctComponent.changeAcctItemOwefee(false, prod.getAcct_id(), prod.getProd_id(), more_fee);	
				}else{
				
				//账单要求填serv_id
				PProd pprod = prodComponent.queryProdById(promProd.getProd_id());
				prod.setServ_id(pprod.getServ_id());
				
				int allbill_fee=more_fee;
				//实缴每月额外资费
				int bill_rent=0;
				//第一月账单计算
				int first_bill_rent=0;
				if(promProd.getBilling_cycle()>1&&promProd.getRent()>0){
					bill_rent=(int)(promProd.getReal_pay()*1.0/promProd.getMonths());
					first_bill_rent=bill_rent+promProd.getReal_pay()-bill_rent*promProd.getMonths();
				}else{
					bill_rent=(int) (promProd.getReal_pay()*1.0/promProd.getMonths()- promProd.getRent()*1.0/promProd.getBilling_cycle());
					CAcctAcctitem acctitem=acctComponent.queryAcctItemByAcctitemId(prod.getAcct_id(), prod.getProd_id());
					int owe_fee =acctitem.getOwe_fee()-acctitem.getActive_balance();
					if(owe_fee>0){//使用欠费计算第一个账单基数
						first_bill_rent=(1+ (int)(owe_fee*promProd.getBilling_cycle()*1.0/promProd.getRent()+0.5))*bill_rent;
					}
				}
				
				int bill_month_index=0;
				while(allbill_fee>0){			
					int bill_fee=bill_month_index==0?first_bill_rent:bill_rent;
					if(bill_fee>=allbill_fee){
						bill_fee=allbill_fee;
						allbill_fee=0;
					}else{
						allbill_fee=allbill_fee-bill_fee;
					}
					//按到期日来判断开始账单的账期
					String billingCycle = DateHelper.format(
							DateHelper.getNextMonthByNum(prod.getInvalid_date().after(new Date())?prod.getInvalid_date():new Date(),bill_month_index)
							, DateHelper.FORMAT_YM);
					billComponent.createPromFeeBill(prod, doneCode, billingCycle, bill_fee, bill_fee);
					bill_month_index++;
				}
				//账户欠费金额修改
				acctComponent.changeAcctItemOwefee(false, prod.getAcct_id(), prod.getProd_id(), more_fee);	
				}
			}
			
			//绑定产品
			if(StringHelper.isNotEmpty(promProd.getBind_prod_id())){
				bindList.add(promProd);
			}else{//非绑定到期日产品
				//包月资费产品处理缴费
				if(promProd.getRent() > 0&&promProd.getBilling_cycle()==1){
					//如果实缴金额大于0，记录c_fee信息
					if(promProd.getReal_pay() > 0){
						PayDto pay = new PayDto();
						pay.setProd_sn(prod.getProd_sn());
						pay.setTariff_id(prod.getTariff_id());
						pay.setCust_id(custId);
						pay.setUser_id(prod.getUser_id());
						pay.setAcct_id(prod.getAcct_id());
						pay.setAcctitem_id(prod.getProd_id());
						pay.setFee(promProd.getReal_pay());
						if (promProd.getMonths()>0 )
							pay.setPresent_fee(promProd.getShould_pay() - promProd.getReal_pay());
						
						pay.setBegin_date(DateHelper.dateToStr(prod.getInvalid_date()));
						
						this.saveAcctPay(doneCode, pay);
					}else{
						//记录赠送冻结记录
						//增加冻结余额
						CAcctAcctitemInactive inactiveItem =new CAcctAcctitemInactive();
						inactiveItem.setPromotion_sn(cPromFee.getProm_fee_sn());
						inactiveItem.setCust_id(custId);
						inactiveItem.setAcct_id(prod.getAcct_id());
						inactiveItem.setAcctitem_id(prod.getProd_id());
						inactiveItem.setCycle(1);
						inactiveItem.setActive_amount(promProd.getShould_pay());
						inactiveItem.setInit_amount(promProd.getShould_pay());
						inactiveItem.setBalance(promProd.getShould_pay());
						inactiveItem.setCounty_id(prod.getCounty_id());
						inactiveItem.setArea_id(prod.getArea_id());
						inactiveItem.setDone_code(doneCode);
						acctComponent.addAcctItemInactive(inactiveItem);
						//修改产品到期日
						userProdComponent.updateInvalidDate(doneCode, promProd.getProd_sn(), 0, promProd.getShould_pay(),
								acctComponent.queryAcctItemByAcctitemId(prod.getAcct_id(), prod.getProd_id()));
					}
				
				}else {//0资费和包多月资费,直接修改到期日,有实缴则插入缴费
					//计算到期日
					Date invalidDate = userProdComponent.getDate(prod.getInvalid_date().after(DateHelper.now()) ? prod.getInvalid_date() : DateHelper.now(), promProd.getMonths(), 0);
					//保存缴费信息
					if (promProd.getReal_pay()>0){
						PayDto pay = new PayDto();
						pay.setUser_id(prod.getUser_id());
						pay.setAcct_id(prod.getAcct_id());
						pay.setAcctitem_id(prod.getProd_id());
						pay.setFee(promProd.getReal_pay());
						pay.setTariff_id(prod.getTariff_id());
						pay.setProd_sn(prodSn);
						pay.setBegin_date(DateHelper.dateToStr(prod.getInvalid_date()));
						pay.setInvalid_date(DateHelper.dateToStr(invalidDate));
						this.saveAcctPay(doneCode, pay);
					}
					//更新到期日
					userProdComponent.updateInvalidDate(doneCode, prodSn, invalidDate);
			}
			
		  }
			UserDto user = userComponent.queryUserById(promProd.getUser_id());
			if(!rulids.contains(user.getUser_id())){
				related_user_list.add(user);
				rulids.add(user.getUser_id());
			}
			//如果是宽带产品，则发激活产品指令
			if(SystemConstants.PROD_SERV_ID_BAND.equals(promProd.getServ_id())){
				jobComponent.createBusiCmdJob(doneCode, BusiCmdConstants.ACCTIVATE_PROD, user.getCust_id(),
						user.getUser_id(), user.getStb_id(),user.getCard_id(),user.getModem_mac(), promProd.getProd_sn(),promProd.getProd_id());
			}
			recoverUserStatus(user, doneCode);
		}
		
		//绑定产品的到期日处理,排除重复绑定
		 Map<String,PromFeeProdDto> bindMap=new HashMap<String,PromFeeProdDto>();
		 for(PromFeeProdDto promProd:  bindList){
			 if(promMap.containsKey(promProd.getUser_id())
					 &&promMap.get(promProd.getUser_id()).containsKey(promProd.getBind_prod_id())){
				 PromFeeProdDto bindpromprod=promMap.get(promProd.getUser_id()).get(promProd.getBind_prod_id());
				 CProd bindprod = userProdComponent.queryByProdSn(bindpromprod.getProd_sn());
				 promProd.setBind_invalid_date(bindprod.getInvalid_date());
				 bindMap.put(promProd.getProd_sn(), promProd);
			 }
		 }
		 //按照到期日赠送
		 for(PromFeeProdDto promProd: bindMap.values()){
			 CProd prod = userProdComponent.queryByProdSn(promProd.getProd_sn());
			 if(promProd.getBind_invalid_date().after(new Date())
					 &&prod.getInvalid_date().before(promProd.getBind_invalid_date())){
				 if(promProd.getRent()>0&&promProd.getBilling_cycle()==1){
					    //计算两个到期日之间的需要的金额
					    int should_pay=
					    	(int) (DateHelper.getDiffDays
						(prod.getInvalid_date().after(new Date())?prod.getInvalid_date():new Date()
						, promProd.getBind_invalid_date())* promProd.getRent()*12/(promProd.getBilling_cycle()*365.0));
					    
					    //记录赠送冻结记录
						//增加冻结余额
						CAcctAcctitemInactive inactiveItem =new CAcctAcctitemInactive();
						inactiveItem.setPromotion_sn(cPromFee.getProm_fee_sn());
						inactiveItem.setCust_id(custId);
						inactiveItem.setAcct_id(prod.getAcct_id());
						inactiveItem.setAcctitem_id(prod.getProd_id());
						inactiveItem.setCycle(1);
						inactiveItem.setActive_amount(should_pay);
						inactiveItem.setInit_amount(should_pay);
						inactiveItem.setBalance(should_pay);
						inactiveItem.setCounty_id(prod.getCounty_id());
						inactiveItem.setArea_id(prod.getArea_id());
						inactiveItem.setDone_code(doneCode);
						acctComponent.addAcctItemInactive(inactiveItem);
						
						//修改产品到期日
						userProdComponent.updateInvalidDate(doneCode, promProd.getProd_sn(), 0, should_pay,
								acctComponent.queryAcctItemByAcctitemId(prod.getAcct_id(), prod.getProd_id()));
				 }else{
					 userProdComponent.updateInvalidDate(doneCode, promProd.getProd_sn(), promProd.getBind_invalid_date());
				 }
				 
			 }
		 }
		 
		 //保存套餐缴费明细
		feeComponent.savePromPayProds(cPromFee, prodList);
		
		jobComponent.createCreditExecJob(doneCode, cust.getCust_id());
		//生成计算到期日任务
		jobComponent.createInvalidCalJob(doneCode, custId);
		
		List<DoneInfoDto> details = new ArrayList<DoneInfoDto>();
		int start = 0;
		int limit  = 20;
		Pager<DoneInfoDto> donePager = doneCodeComponent.getPromFeeDate(doneCode,getOptr().getCounty_id(),start,limit);
		int counts = 1;
		while(donePager.getRecords() !=null && !donePager.getRecords().isEmpty()){
			details.addAll(donePager.getRecords());
			start += limit * counts;
			counts += 1;
			donePager = doneCodeComponent.getPromFeeDate(doneCode,getOptr().getCounty_id(),start,limit);
		}
		
		String[] users = CollectionHelper.converValueToArray(details, "user_id");
		List<CUser> userList = userComponent.queryAllUserByUserIds(users);
		for(DoneInfoDto dto:details){
			
			for(CUser user:userList){
				if(dto.getUser_id().equals(user.getUser_id())){
					String userName = "";
					if(StringHelper.isNotEmpty(user.getCard_id())){
						userName = user.getUser_type_text()+"-"+ user.getUser_name()+"("+user.getCard_id().substring( user.getCard_id().length()-4, user.getCard_id().length())+")";
					}else{
						userName = user.getUser_type_text()+"-"+user.getUser_name();
					}
					dto.setUser_name(userName);
				}
				
			}
		}
		
		String prod_remark = promFeeInfo.getRemark();
		prod_remark = StringHelper.isEmpty(prod_remark) ? "": prod_remark.replaceAll("\n", "</br>").replaceAll("\r", "").replaceAll("\"", "") ;
		
		List<Map<String, Object>> pdlist = new ArrayList<Map<String,Object>>();
		Map<String,Object> prodTariffMap = new HashMap<String, Object>();
		prodTariffMap.put("prod_name", promFeeInfo.getProm_fee_name());
		prodTariffMap.put("tariff_name", "" +(promFee/100) );
		prodTariffMap.put("prod_desc", prod_remark);
		pdlist.add(prodTariffMap);
		
		for(CUser user:userList){
			if(user.getUser_type().equals(SystemConstants.USER_TYPE_DTV)){
				CUserDtv userDtv = new CUserDtv();
				BeanUtils.copyProperties(user, userDtv);
				getBusiParam().addUser(userDtv);
			}else if(user.getUser_type().equals(SystemConstants.USER_TYPE_BAND)){
				CUserBroadband userBand = new CUserBroadband();
				BeanUtils.copyProperties(user, userBand);
				getBusiParam().addUser(userBand);
			}else if(user.getUser_type().equals(SystemConstants.USER_TYPE_ATV)){
				CUserAtv userAtv = new CUserAtv();
				BeanUtils.copyProperties(user, userAtv);
				getBusiParam().addUser(userAtv);
			}
		}
		
//		saveOrderProdDoneInfo(doneCode, pdList );
		getBusiParam().setBusiConfirmParam("prod_tariff_list", pdlist);
		getBusiParam().setBusiConfirmParam("related_user_list", related_user_list);
		
		doneCodeComponent.saveDoneCodeInfo(doneCode, custId, related_user_list.get(0).getUser_id(), getBusiParam().getBusiConfirmParamInfo());
		
		getBusiParam().getBusiConfirmParamInfo().put("details", details);
		saveAllPublic(doneCode,getBusiParam());
		*/
	}

	
	
	/**
	 * 批量修改到期日
	 * @param payList
	 * @param newExpDate
	 * @throws Exception
	 */
	public void saveBatchEditExpDate(List<PayDto> payList,String newExpDate) throws Exception{
		if(null != payList && payList.size() > 0){
			//获取业务流水
			Integer doneCode = doneCodeComponent.gDoneCode();
			
			if(StringHelper.isNotEmpty(payList.get(0).getCust_id())){
				for(PayDto pay : payList){
					String prodSn = pay.getProd_sn();
					Date invalidDate = DateHelper.strToDate(newExpDate);
					
					//查找产品信息
//					CProd prod = userProdComponent.queryByProdSn(prodSn);
					
					//修改到期日
					userProdComponent.updateInvalidDate(doneCode, prodSn, invalidDate);
				}
				
				List<String> custIds = CollectionHelper.converValueToList(payList, "cust_id");
				List<String> userIds = CollectionHelper.converValueToList(payList, "user_id");
				for (String custId:custIds){
					jobComponent.createCreditExecJob(doneCode, custId);
				}

				getBusiParam().setDoneCode(doneCode);
				doneCodeComponent.saveBatchDoneCode(doneCode, getBusiParam().getBusiCode(), getBusiParam().getRemark(), custIds, userIds);
			}else{
				//获取客户信息
				CCust cust = getBusiParam().getCust();
//				Map<String,CUser> map = CollectionHelper.converToMapSingle(getBusiParam().getSelectedUsers(), "user_id");
				
				for(PayDto pay : payList){
					String prodSn = pay.getProd_sn();
					Date invalidDate = DateHelper.strToDate(pay.getNext_invalid_date());
					
					//查找产品信息
//					CProd prod = userProdComponent.queryByProdSn(prodSn);
					//修改到期日
					userProdComponent.updateInvalidDate(doneCode, prodSn, invalidDate);
					
					
				}
				jobComponent.createCreditExecJob(doneCode, cust.getCust_id());
				saveAllPublic(doneCode,getBusiParam());
			}
		}
	}

	public void savePay(String prodId,String tariffId, int fee, 
			Date beginDate, Date invalidDate) throws Exception {
		//获取客户信息
		CUser user = getBusiParam().getSelectedUsers().get(0);
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		//保存缴费信息
		PayDto pay = new PayDto();
		pay.setUser_id(user.getUser_id());
		pay.setAcct_id(user.getAcct_id());
		pay.setAcctitem_id(prodId);
		pay.setFee(fee);
		pay.setTariff_id(tariffId);
		pay.setInvalid_date(DateHelper.dateToStr(invalidDate));
		this.saveAcctPay(doneCode, pay);
		//保存模拟产品账单，产品sn、资费编号为空，账期去当前账期
		String billingCycle = DateHelper.format(new Date(), DateHelper.FORMAT_YM);
		CProd prod = new CProd();
		BeanUtils.copyProperties(user, prod);
		prod.setProd_id(prodId);
		prod.setTariff_id(tariffId);
		BBill bill = billComponent.createBill(prod, doneCode, billingCycle, fee,0, SystemConstants.BILL_COME_FROM_MANUAL);
		//生成销账记录
		billComponent.createWriteOff(bill,getBusiParam().getCust().getAddr_id());
		//保存业务流水
		saveAllPublic(doneCode,getBusiParam());
	}

	public void saveTrans(String sourceAcctId, String sourceAcctItemId,
			String orderAcctId, String orderAcctItemId, int fee) throws Exception{
		//获取客户信息
		CCust cust = getBusiParam().getCust();
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		String busiCode = getBusiParam().getBusiCode();
		acctTrans(cust.getCust_id(), doneCode, busiCode,sourceAcctId, sourceAcctItemId,
				orderAcctId, orderAcctItemId, fee);
		//创建销帐任务
		jobComponent.createCustWriteOffJob(doneCode, cust.getCust_id(),SystemConstants.BOOLEAN_TRUE);
		jobComponent.createAcctModeCalJob(doneCode, cust.getCust_id());
		//生成计算到期日任务
		jobComponent.createInvalidCalJob(doneCode, cust.getCust_id());
		
		saveAllPublic(doneCode,getBusiParam());
	}

	/**
	 * 退款
	 * @param acctId
	 * @param acctItemId
	 * @param userId
	 * @param fee
	 * @throws Exception
	 * 20130401 by wang 记录套餐信息
	 */
	public void saveRefund(String acctId, String acctItemId,String prodSn,String userId, int fee,String invalidDate,String promFeeSn)
			throws Exception {
		String custId = getBusiParam().getCust().getCust_id();
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		String tariffId = "";
		
		if(StringHelper.isNotEmpty(promFeeSn)){
			userProdComponent.savePromProdRefund(promFeeSn, prodSn.split(","), doneCode,fee);
		}
		
		/*if(StringHelper.isNotEmpty(prodSn)){
			CProd prod = userProdComponent.queryByProdSn(prodSn);
			if(null!= prod){
				PProdTariff tariff = prodComponent.queryTariffById(prod.getTariff_id());
				tariffId = tariff.getTariff_id();
				//如果是包多月的资费
				if(tariff.getBilling_cycle() > 1 && SystemConstants.BILLING_TYPE_MONTH.equals(tariff.getBilling_type())
						&& tariff.getRent() > 0){
					
					//作废到期日当月后的账单
					billComponent.cancelBill(prod.getProd_sn(),DateHelper.formatYM(prod.getInvalid_date()));
					
					//需要作废的账单月份数
					int months = (fee * tariff.getBilling_cycle()) / tariff.getRent();
					//如果不能整除，费用不足那一月账单也作废
					if((fee * tariff.getBilling_cycle()) % tariff.getRent() >0){
						months = months + 1;
					}
					
					//查询最后一个月份的账单
					BBill bill = billComponent.queryLatsBillByProdSn(prod.getProd_sn());
					if(null != bill){
						String billCycleId = bill.getBilling_cycle_id();
						Calendar c = Calendar.getInstance();
						c.set(Calendar.YEAR, Integer.valueOf(billCycleId.substring(0, 4)));
						c.set(Calendar.MONTH, Integer.valueOf(billCycleId.substring(4, 6))-1);
						
						//得到作废账单起始月份
						c.add(Calendar.MONTH, -months +1);
						
						billCycleId = DateHelper.format(c.getTime(), "yyyyMM");
						
						//作废账单
						billComponent.cancelBill(prodSn,billCycleId);
						
						List<CProdPropChange> propChangeList = new ArrayList<CProdPropChange>();
						//修改到期日
						c.setTime(prod.getInvalid_date());
						c.add(Calendar.MONTH, -months);
						Date newInvalidDate = c.getTime();
						
						propChangeList.add(new CProdPropChange("invalid_date",
								DateHelper.dateToStr(prod.getInvalid_date()),DateHelper.dateToStr(newInvalidDate)));
						
//						userProdComponent.updateInvalidDate(doneCode, prodSn, newInvalidDate);
						//修改下次出账日期
						c.setTime(prod.getNext_bill_date());
						c.add(Calendar.MONTH, -months);
						Date newBillDate = c.getTime();
						
						propChangeList.add(new CProdPropChange("next_bill_date",
								DateHelper.dateToStr(prod.getNext_bill_date()),DateHelper.dateToStr(newBillDate)));
						
						
						userProdComponent.editProd(doneCode, prodSn, propChangeList);
					}
				}
			}
		}*/
		
		
		//保存退款记录
		PayDto pay = new PayDto();
		pay.setUser_id(userId);
		pay.setAcct_id(acctId);
		pay.setAcctitem_id(acctItemId);
		pay.setFee(fee*-1);
		pay.setProd_sn(prodSn);
		pay.setTariff_id(tariffId);
		pay.setInvalid_date(invalidDate);
		pay.setBegin_date(invalidDate);
		this.saveAcctPay(doneCode, pay);
		
		
		
		
		
		//生成销账任务
		jobComponent.createCustWriteOffJob(doneCode, custId, SystemConstants.BOOLEAN_TRUE);
		jobComponent.createAcctModeCalJob(doneCode, custId);
		//生成计算到期日任务
		jobComponent.createInvalidCalJob(doneCode, custId);
		saveAllPublic(doneCode,getBusiParam());
	}
	
	/* 
	 * 定额赠送退款
	 * @see com.ycsoft.business.service.IAcctService#dezsRefund(java.lang.String, java.lang.String, java.lang.String, int)
	 */
	public void dezsRefund(String acctId, String acctItemId, String feeType,
			int fee) throws Exception {
		Integer doneCode = doneCodeComponent.gDoneCode();
		String busiCode = this.getBusiParam().getBusiCode();
		CCust cust = getBusiParam().getCust();
		String custId = cust.getCust_id();
		
		acctComponent.changeAcctItemBanlance(doneCode, busiCode,
				custId, acctId, acctItemId,
				SystemConstants.ACCT_CHANGE_REFUND , feeType, -fee, null);
		
		TCountyAcct countyAcct = acctComponent.queryAcctConfig(cust.getCust_colony(),cust.getCounty_id());
		
		//更新定额账户的使用
		if(null != countyAcct){
			acctComponent.updateCoutyAcct(countyAcct, doneCode, -fee);
		}
		
		
		
		CProd prod = userProdComponent.queryByAcctItem(acctId, acctItemId);
		if(null != prod){
			CAcctAcctitem acctItem = acctComponent.queryAcctItemByAcctitemId(acctId, acctItemId);
			userProdComponent.updateInvalidDate(doneCode, prod.getProd_sn(), 0, fee*-1, acctItem);
		}else{
			//生成计算到期日任务
			jobComponent.createInvalidCalJob(doneCode, custId);
		}
		
		saveAllPublic(doneCode,getBusiParam());
	}

	
	public void saveRefundInvlid(String acctId, String acctItemId,
			String feeType, int fee) throws Exception {
		String custId = getBusiParam().getCust().getCust_id();
		String addrId = getBusiParam().getCust().getAddr_id();
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		String busiCode = this.getBusiParam().getBusiCode();
		//保存退款记录
		PayDto pay = new PayDto();
		pay.setAcct_id(acctId);
		pay.setAcctitem_id(acctItemId);
		pay.setFee(fee*-1);
		String payType = SystemConstants.PAY_TYPE_CASH;
		if (this.getBusiParam().getPay()!= null && this.getBusiParam().getPay().getPay_type() !=null)
			payType = this.getBusiParam().getPay().getPay_type();
		feeComponent.saveAcctFee(custId, addrId,pay, doneCode, busiCode, payType);
		//更新账目作废记录金额
		acctComponent.createAcctitemInvalid(custId, acctId, acctItemId, doneCode, busiCode, feeType, fee*-1);
		
		//生成计算到期日任务
		jobComponent.createInvalidCalJob(doneCode, custId);
		
		saveAllPublic(doneCode,getBusiParam());
	}
	
	public void saveAcctFree(String acctId, String acctItemId, String prodSn,
			int fee, String feeType) throws Exception {
		CProd prod = userProdComponent.queryByProdSn(prodSn);
		CUser user = userComponent.queryUserById(prod.getUser_id());
		String ofl = "";
		if(user.getUser_type().equals(SystemConstants.USER_TYPE_BAND)){
			ofl = acctComponent.queryTemplateConfig(TemplateConfigDto.Config.BAND_OWE_FEE_NUMBER.toString());
		}else{
			ofl = acctComponent.queryTemplateConfig(TemplateConfigDto.Config.OWE_FEE_NUMBER.toString());
		}
		if(StringHelper.isNotEmpty(ofl)){
			int ownFeeNumber = Integer.parseInt(ofl);	//最新配置 欠费天数
			int rent = prodComponent.queryTariffById(prod.getTariff_id()).getRent();
			if(fee * -1 > ((float)rent/30)*ownFeeNumber)	//抹零金额不能大于 产品每天资费*配置天数
				throw new ServicesException("当前基本包欠费天数为: "+ownFeeNumber);
		}
		this.saveAdjust(acctId, acctItemId, prodSn, fee, feeType, SystemConstants.ADJUST_REASON_OWNFEE);
	}

	public void saveAdjust(String acctId, String acctItemId, String prodSn,
			int fee, String feeType, String reason) throws Exception {
		//获取客户信息
		CCust cust = getBusiParam().getCust();
		
		CProd prod = userProdComponent.queryByProdSn(prodSn);
		if(StringHelper.isNotEmpty(feeType) && feeType.equals(SystemConstants.ACCT_FEETYPE_ADJUST_EASY)){
			String day =  acctComponent.getAdjustDay();
			if(StringHelper.isEmpty(day)){
				throw new ServicesException("模板没有配置小额减免天数，保存失败");
			}
			PProdTariff tariff = userProdComponent.queryProdTariffById(prod.getTariff_id());
			List<CAcctAcctitemAdjust> adjustList = acctComponent.queryAdjustFee(acctId, acctItemId,feeType);
			int adjustFee =0;
			for(CAcctAcctitemAdjust dto:adjustList){
				adjustFee+=dto.getAjust_fee();
			}
			double realFee = (float)(tariff.getRent()/(float)(tariff.getBilling_cycle()*30))*Integer.parseInt(day);
			double canFee = adjustFee+realFee;
			DecimalFormat df = new DecimalFormat("###,###,###.##");
			double rate = Double.valueOf(df.format((double)canFee/100));
			if(canFee + fee < 0 ){
				throw new ServicesException("不能超过本月调账剩余额度["+rate+"]，保存失败");
			}
		}
		
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		if (StringHelper.isEmpty(feeType))
			feeType = SystemConstants.ACCT_FEETYPE_ADJUST;
		acctComponent.addAdjust(doneCode, acctId, acctItemId, fee,feeType, reason,getBusiParam().getRemark());
		CAcctAcctitem acctitem = acctComponent.queryAcctItemByAcctitemId(acctId, acctItemId);
		

		
		
		if (acctitem!=null){//账目还存在
			//创建账单
			String billingCycle = DateHelper.format(new Date(), DateHelper.FORMAT_YM);
			if (prod == null){
				prod = new CProd();
				BeanUtils.copyProperties(cust, prod);
				prod.setAcct_id(acctId);
				prod.setProd_id(acctItemId);
			}
			String comeFrom = SystemConstants.BILL_COME_FROM_ADJUST;
			if(SystemConstants.ACCT_FEETYPE_ADJUST_KT.equals(feeType))
				comeFrom = SystemConstants.BILL_COME_FROM_ADJUST_KT;
			billComponent.createBill(prod, doneCode, billingCycle, fee,fee, comeFrom);
			
			//创建销帐任务
			jobComponent.createCustWriteOffJob(doneCode, cust.getCust_id(),SystemConstants.BOOLEAN_TRUE);
			
			jobComponent.createAcctModeCalJob(doneCode, cust.getCust_id());
			//到期日更新更新
			acctComponent.changeAcctItemOwefee(false, acctId, acctItemId, fee);
			//修改产品对应的到期日
			CAcctAcctitem acctItem = acctComponent.queryAcctItemByAcctitemId(acctId, acctItemId);
			userProdComponent.updateInvalidDate(doneCode,  prodSn,0, fee*-1, acctItem);
			
			
			//生成计算到期日任务
			jobComponent.createInvalidCalJob(doneCode ,cust.getCust_id());
			
			
		} else {
			//账目不存在
			throw new ServicesException("账目已经被删除，无法调账");
		}
		
		saveAllPublic(doneCode,getBusiParam());
	}

	public int saveAcctUnfreeze(CAcctAcctitemInactive unfreezeJob)throws Exception{
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		String custId = unfreezeJob.getCust_id();
		String acctId = unfreezeJob.getAcct_id();
		String acctItemId = unfreezeJob.getAcctitem_id();
		String feeType = SystemConstants.ACCT_FEETYPE_PRESENT;
		String sn = StringHelper.isEmpty(unfreezeJob.getFee_sn())?unfreezeJob.getPromotion_sn():unfreezeJob.getFee_sn();
		int fee = unfreezeJob.getActive_amount()>=unfreezeJob.getBalance()?unfreezeJob.getBalance():unfreezeJob.getActive_amount();
		int cycle =unfreezeJob.getCycle();

		//修改账目余额
		acctComponent.changeAcctItemBanlance(doneCode, BusiCodeConstants.ACCT_UNFREEZE, 
				custId, acctId, acctItemId, SystemConstants.ACCT_CHANGE_UNFREEZE, feeType, fee, unfreezeJob.getDone_code());
		//修改冻结资金余额,并更新下次返回日期
		acctComponent.changeAcctitemInactive(unfreezeJob.getBalance(), acctId, acctItemId, sn, fee, cycle);
		
		jobComponent.createCustWriteOffJob(doneCode, custId, SystemConstants.BOOLEAN_TRUE);
		
		//生成计算到期日任务
		jobComponent.createInvalidCalJob(doneCode, custId);
		
		//保存流水
		saveDoneCode(doneCode, BusiCodeConstants.ACCT_UNFREEZE, custId);
		return fee;
	}

	/**
	 * 删除指定账户或账目
	 * @param acctId
	 * @param acctItemId
	 * @throws Exception
	 */
	public void saveDelAcctItem(String acctId, String acctItemId,Integer doneCode)
			throws Exception {
		String busiCode = this.getBusiParam().getBusiCode();
		CDoneCode cDoneCode = doneCodeComponent.queryByKey(doneCode);
		if(cDoneCode != null){
			busiCode = cDoneCode.getBusi_code();
		}
		
		CAcct acct = acctComponent.queryByAcctId(acctId);
		if (StringHelper.isEmpty(acctItemId)){
			acctComponent.removeAcctWithHis(acct, doneCode,busiCode);
		} else {
			acctComponent.removeAcctItemWithoutHis(acct.getCust_id(),acctId, acctItemId, doneCode,busiCode);
		}
		
		//信控任务
		jobComponent.createCreditCalJob(doneCode, acct.getCust_id(), null, SystemConstants.BOOLEAN_TRUE);
		
	}
	
	/**
	 * 补入非公用账目充值的账目异动数据
	 * @param changeList
	 * @param doneCode
	 */
	public void saveAdjustSpecAcctPay(List<CAcctAcctitemChange> changeList,Integer doneCode) throws Exception{
		acctComponent.saveAdjustSpecAcctPay(changeList, doneCode);
	}
	
	

	public void saveCancelBill(String[] billSns) throws Exception {
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		String busiCode = this.getBusiParam().getBusiCode();
		CCust cust = this.getBusiParam().getCust();
		
		//普通包多月和套餐缴费的包多月判断到期日
		Map<String,PProdTariff> tariffMap=new HashMap<String,PProdTariff>();//资费缓存
		Map<String,Integer> prodSnInvalidMap=new HashMap<String,Integer>();//产品SN到期日月数判断
		
		for(String billSn : billSns){
			//查找账单信息以及账单对应的账目信息
			BBill bill = billComponent.queryBillBySn(billSn);
			if(bill.getCome_from().equals(SystemConstants.BILL_COME_FROM_MUCH)){
				//包多月账单
				Integer months=prodSnInvalidMap.get(bill.getProd_sn());
				if(months==null){
					prodSnInvalidMap.put(bill.getProd_sn(), 1);
				}else{
					prodSnInvalidMap.put(bill.getProd_sn(), months+1);
				}
			}
			if(bill.getCome_from().equals(SystemConstants.BILL_COME_FROM_PROM)){
				//套餐缴费中包多月或0资费的账单
				if(bill.getTariff_id()!=null){
					PProdTariff tariff=tariffMap.get(bill.getTariff_id());
					if(tariff==null){
						tariff=prodComponent.queryTariffById(bill.getTariff_id());
						tariffMap.put(bill.getTariff_id(), tariff);
					}
					if(tariff!=null&&(tariff.getBilling_cycle()>1||tariff.getRent()==0)){
						Integer months=prodSnInvalidMap.get(bill.getProd_sn());
						if(months==null){
							prodSnInvalidMap.put(bill.getProd_sn(), 1);
						}else{
							prodSnInvalidMap.put(bill.getProd_sn(), months+1);
						}
					}
				}
			}
			CAcctAcctitem acctItem = this.acctComponent.queryAcctItemByAcctitemId(bill.getAcct_id(), bill.getAcctitem_id());
			//查找账单的销账记录
			List<BBillWriteoff> writeOffList = this.billComponent.queryWriteOffByBill(billSn);
			
			for ( BBillWriteoff writeOff :writeOffList){
				if (acctItem != null){
					//更新账目余额
					this.acctComponent.changeAcctItemBanlance(doneCode, busiCode, 
							bill.getCust_id(), bill.getAcct_id(), bill.getAcctitem_id(), SystemConstants.ACCT_CHANGE_UNWRITEOFF,
							writeOff.getFee_type(), writeOff.getFee(), null);
					if(0<writeOffList.size() && bill.getStatus().equals("1") && busiCode.equals(BusiCodeConstants.CANCEL_B_BILL)){
						//还原欠费
						this.acctComponent.changeAcctItemOwefee(false, bill.getAcct_id(), bill.getAcctitem_id(), writeOff.getFee());
					}
				} else {
					//插入新的作废金额
					this.acctComponent.createAcctitemInvalid(bill.getCust_id(), bill.getAcct_id(), bill.getAcctitem_id()
							, doneCode, busiCode
							, writeOff.getFee_type(),writeOff.getFee());
				}
				//删除销账记录
				this.billComponent.cancelWriteOff(writeOff, doneCode);
			}
			//已出账已销账
			if(0<writeOffList.size() && bill.getStatus().equals("1") && busiCode.equals(BusiCodeConstants.CANCEL_B_BILL)){
				//更新欠费金额
				this.acctComponent.changeAcctItemOwefee(false, bill.getAcct_id(), bill.getAcctitem_id(), bill.getFinal_bill_fee()*-1);
			}
			
			//已出账未销账
			if(0==writeOffList.size() && bill.getStatus().equals("1") && busiCode.equals(BusiCodeConstants.CANCEL_B_BILL)){
				//更新欠费金额
				this.acctComponent.changeAcctItemOwefee(false, bill.getAcct_id(), bill.getAcctitem_id(), bill.getOwe_fee()*-1);
			}
			
			//实时账单
			if(0==writeOffList.size() && bill.getStatus().equals("0") && busiCode.equals(BusiCodeConstants.CANCEL_B_BILL)){
				//更新欠费金额
				this.acctComponent.changeAcctItemOwefee(true, bill.getAcct_id(), bill.getAcctitem_id(), 0);
				//清空月租
				this.billComponent.deleteRentfee(billSn,bill.getProd_sn(),bill.getAcct_id(), bill.getAcctitem_id());
				//费用作废
				this.billComponent.updateFeeStatus(billSn);
			}
			
			//包多月未出帐
			if (0 == writeOffList.size() && bill.getStatus().equals("0") 
					&& bill.getFee_flag().equals(SystemConstants.BILL_FEE_FLAG_DY)
					&& busiCode.equals(BusiCodeConstants.CANCEL_B_BILL)) {
				//更新欠费金额
				this.acctComponent.changeAcctItemOwefee(true, bill.getAcct_id(), bill.getAcctitem_id(), 0);
			}

			//更新账单状态为作废状态
			this.billComponent.cancelBill(billSn);
		
		}
		
		//普通包多月或者套餐缴费中的包多月的到期日判断
		if(prodSnInvalidMap.size()>0){
			Iterator<String> it= prodSnInvalidMap.keySet().iterator();
			while(it.hasNext()){
				String prodSn=it.next();
				CProd cprod= userProdComponent.queryByProdSn(prodSn);
				int months=prodSnInvalidMap.get(prodSn);
				if(cprod!=null){
					Date invalidDate=DateHelper.getNextMonthByNum(cprod.getInvalid_date(),months*-1 );
					if(invalidDate.before(DateHelper.today())){
						invalidDate=DateHelper.today();
					}
					userProdComponent.updateInvalidDate(doneCode, prodSn,invalidDate );
				}
			}
		}
		
		//创建销帐任务
		jobComponent.createCustWriteOffJob(doneCode, cust.getCust_id(),SystemConstants.BOOLEAN_TRUE);
		jobComponent.createAcctModeCalJob(doneCode, cust.getCust_id());
		
		//生成计算到期日任务
		jobComponent.createInvalidCalJob(doneCode ,cust.getCust_id());
		
		saveAllPublic(doneCode,getBusiParam());
	}
	

	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.IAcctService#queryAcctByCustId(java.lang.String)
	 */
	public List<AcctDto> queryAcctByCustId(String custId) throws Exception {
		CCust cust = custComponent.queryCustById(custId);
		return acctComponent.queryAcctsEsayByCustId(cust);
	}
	/**
	 * 查询公用账目余额
	 * @param custId
	 * @return
	 * @throws Exception 
	 */
	public List<AcctitemDto> queryPublicAcctItemByCustId(String custId) throws Exception{
		CAcct acct= acctComponent.queryCustAcctByCustId(custId);
		if(acct != null){
			List<AcctitemDto> list = new ArrayList<AcctitemDto>();
			AcctitemDto  t = acctComponent.queryAcctItemDtoByAcctitemId(acct.getAcct_id(), SystemConstants.ACCTITEM_PUBLIC_ID);
			list.add(t);
			return list;
		}
		return null;
	}
	
	public List<AcctitemDto> queryAcctitemToCallCenter(Map<String,Object> params) throws Exception{
		return acctComponent.queryAcctitemToCallCenter(params);
	}
	
	/**
	 * 查询产品的账户信息
	 * @param custIds
	 * @return
	 * @throws Exception
	 */
	public List<UnitPayDto> queryBaseProdAcctItems(String[] custIds,String prodId)
			throws Exception {
		return acctComponent.queryBaseProdAcctItems(custIds,prodId);
	}
	
	/**
	 * 单位缴费，查询可以缴费的产品
	 * @param custIds
	 * @return
	 */
	public List<CProd> querySelectableProds(String[] custIds) throws Exception {
		return acctComponent.querySelectableProds(custIds);
	}

	/**
	 * @see com.ycsoft.business.component.core.AcctComponent#queryAcctitemActiveByAcctitemId(java.lang.String)
	 */
	public List<AcctAcctitemActiveDto> queryActive(String acctId,
			String acctitemId) throws Exception {
		List<AcctAcctitemActiveDto> actives = acctComponent.queryActiveById(
				acctId, acctitemId);
		for (int i = actives.size() - 1; i >= 0; i--) {
			AcctAcctitemActiveDto active = actives.get(i);
			if (active.getBalance().compareTo(0) == 0) {
				actives.remove(i);
			}
		}
		return actives;
	}
	

	/**
	 * @see com.ycsoft.business.component.core.AcctComponent#queryInactive(java.lang.String)
	 */
	public List<CAcctAcctitemInactive> queryAcctitemInactive(
			String acctId,String acctitemId) throws JDBCException {
		return acctComponent.queryInactive(acctId,acctitemId);
	}

	/**
	 * @see com.ycsoft.business.component.core.AcctComponent#queryOrder(java.lang.String)
	 */
	public List<CAcctAcctitemOrder> queryAcctitemOrder(
			String acctId,String acctitemId) throws JDBCException {
		return acctComponent.queryOrder(acctId,acctitemId);
	}

	/**
	 * @see com.ycsoft.business.component.core.AcctComponent#queryThreshold(java.lang.String)
	 */
	public List<CAcctAcctitemThreshold> queryAcctitemThreshold(
			String acctId,String acctitemId) throws Exception {
		return acctComponent.queryThreshold(acctId,acctitemId);
	}
	
	public void updateThreshold(List<CAcctAcctitemThresholdProp> thresholdPropList) throws Exception {
		Integer doneCode = doneCodeComponent.gDoneCode();
		acctComponent.updateThreshold(thresholdPropList,doneCode);
		saveAllPublic(doneCode, getBusiParam());
		
		String custId = getBusiParam().getCust().getCust_id();
		//信控任务
		jobComponent.createCreditExecJob(doneCode, custId);
	}

	public List<AcctAcctitemThresholdDto> queryThresholdByAcctId(String custId,String[] acctIds) throws Exception {
		Map<String, CUser> userMap = CollectionHelper.converToMapSingle(
				userComponent.queryUserByCustId(custId), "user_id");
		List<AcctAcctitemThresholdDto> list = acctComponent.queryThresholdByAcctId(acctIds);
		
		for (AcctAcctitemThresholdDto dto : list) {
			// 设置用户信息
			if (StringHelper.isNotEmpty(dto.getUser_id())) {
				CUser user = userMap.get(dto.getUser_id());
				if (user != null)
					PropertyUtils.copyProperties(dto, user);
			}
		}
		
		return list;
	}
	
	public List<AcctAcctitemThresholdDto> queryThresholdByCustId(
			QueryAcctitemThresholdDto dto, String custId, String[] acctIds)
			throws Exception {
		return acctComponent.queryThresholdByCustId(dto, custId, acctIds);
	}

	/**
	 * @see com.ycsoft.business.component.core.AcctComponent#queryAcctitemChange(java.lang.String)
	 */
	public Pager<CAcctAcctitemChange> queryAcctitemChange(
			String acctId,String acctitemId,Integer start, Integer limit) throws JDBCException {
		return acctComponent.queryAcctitemChange(acctId,acctitemId,start,limit);
	}


	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.IAcctService#saveSignBank(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void saveSignBank(CBankAgree cBankAgree) throws Exception {
		Integer doneCode = doneCodeComponent.gDoneCode();
		
		CCust cust = getBusiParam().getCust();
		String acctId = acctComponent.queryCustAcctByCustId(cust.getCust_id()).getAcct_id();
		acctComponent.saveSignBank(doneCode,acctId,cust,cBankAgree);
		
		//生成计算信用度任务
		jobComponent.createCreditCalJob(doneCode, cust.getCust_id(), null,SystemConstants.BOOLEAN_TRUE);
		
		saveAllPublic(doneCode,getBusiParam());
	}

	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.IAcctService#deleteSignBack(java.lang.String, com.ycsoft.beans.core.bank.CBankAgree)
	 */
	public void saveRemoveSignBank(String bankPayType,Date time) throws Exception {
		Integer doneCode = doneCodeComponent.gDoneCode();
		
		String custId = getBusiParam().getCust().getCust_id();
		String acctId = acctComponent.queryCustAcctByCustId(getBusiParam().getCust().getCust_id()).getAcct_id();
		//return null;
		acctComponent.removeSignBank(doneCode ,acctId, custId,bankPayType,time);
		
		//生成计算信用度任务
		jobComponent.createCreditCalJob(doneCode, custId, null, SystemConstants.BOOLEAN_TRUE);
		saveAllPublic(doneCode,getBusiParam());
	}

	
	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.IAcctService#querySignBank()
	 */
	public CAcctBank querySignBank(String bankPayType) throws Exception {
		String custId = getBusiParam().getCust().getCust_id();
		String acctId = acctComponent.queryCustAcctByCustId(custId).getAcct_id();
		return acctComponent.querySignBankByAcctId(acctId,bankPayType);
	}	 


	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.IAcctService#queryAcctitemAdjust(java.lang.String, java.lang.String)
	 */
	public List<CAcctAcctitemAdjust> queryAcctitemAdjust(String acctId,
			String acctItemId) throws JDBCException {
		return acctComponent.queryAdjust(acctId, acctItemId);
	}
	
	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.IAcctService#queryCompanyAcct()
	 */
	public List<CGeneralAcct> queryCompanyAcct() throws Exception {
		return acctComponent.queryCompanyAcct();
	}
	
	public void editAdjustReason(Integer doneCode,String reason) throws Exception {
		acctComponent.editAdjustReason(doneCode,reason);
	}

	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.IAcctService#editCompanyAcct(java.util.List)
	 */
	public void editCompanyAcct(List<CGeneralAcct> generalAcctList) throws Exception {
		Integer doneCode = doneCodeComponent.gDoneCode();
		
		
		acctComponent.updateGeneralAcct(generalAcctList);
		
		for(CGeneralAcct generalAcct : generalAcctList){
			if(generalAcct.getChangeBalance() != 0){
				//记录非公司账户费用
				feeComponent.saveGeneralAcctFee(generalAcct.getG_acct_id(),generalAcct.getChangeBalance(), doneCode, generalAcct.getCounty_id());
			}
		}
		
		saveAllPublic(doneCode,getBusiParam());
	}
	
	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.IAcctService#addCredential(java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	public void addCredential(String contractId, Integer credentialStartNo,
			Integer credentialEndNo, Integer credentialAmount) throws Exception {
		Integer doneCode = doneCodeComponent.gDoneCode();
		
		
		//更新合同总金额
		CGeneralContract contract = acctComponent.queryGeneralContractById(contractId);
		int change = (credentialEndNo-credentialStartNo + 1) * credentialAmount;
		contract.setTotal_amount(contract.getTotal_amount() + change);
		acctComponent.updateGeneralContract(contract);
		
		//保存合同凭据
		List<CGeneralCredential> generalCredentialList = new ArrayList<CGeneralCredential>();
		if(null != credentialEndNo){
			for(int i=credentialStartNo;i<=credentialEndNo;i++){
				CGeneralCredential generalCredential = new CGeneralCredential();
				generalCredential.setContract_id(contract.getContract_id());
				generalCredential.setCredential_no(i+"");
				generalCredential.setAmount(credentialAmount);
				generalCredential.setBalance(credentialAmount);
				generalCredentialList.add(generalCredential);
			}
		}
		acctComponent.saveGeneralCredentialList(generalCredentialList);
		
		saveAllPublic(doneCode,getBusiParam());
	}
	
	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.IAcctService#queryCredential(java.lang.String, java.lang.Integer, java.lang.Integer)
	 */
	public Pager<CGeneralCredential> queryCredential(String contractId,
			Integer start, Integer limit) throws JDBCException {
		return acctComponent.queryCredential(contractId,start,limit);
	}
	
	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.IAcctService#queryPayInfo(java.lang.String, java.lang.Integer, java.lang.Integer)
	 */
	public Pager<CGeneralContractPay> queryPayInfo(String contractId,
			Integer start, Integer limit) throws JDBCException {
		return acctComponent.queryPayInfo(contractId,start,limit);
	}

	/**
	 * 预扣费接口
	 */
	public String vodPreFee(String transId, String userId, String prodId,
			String progId, String progName, Date requestTime, int price,
			String detailParams) throws Exception {
		Integer doneCode = doneCodeComponent.gDoneCode();
		String busiCode = this.getBusiParam().getBusiCode();
		
		//处理结果，三部分组成，价格和需要充值的金额，账目余额，分号隔开
		String result = "";
		
		//是否需要处理预扣费,false需要处理
		boolean needDeal = false;
		
		//判断是否是24小时内重复请求
		CAcctPreFee oldacctPreFee = acctComponent.queryByProgId(userId, prodId, progId);
		if(null != oldacctPreFee){
			long time = (requestTime.getTime() - oldacctPreFee.getFee_time().getTime())/(1000 * 3600);
			//如果请求时间小于24小时
			if(time <= SystemConstants.VOD_TIME_FORFREE){
				result = 0 + ";";
				needDeal = true;
			}
		}
		
		if(!needDeal){
			//价格
			int fee=0; int needFee=0;
			//根据prodId，userId获取用户产品信息（基本产品）
			CProd cProd = userProdComponent.queryByProdId(userId, prodId);
			//根据资费编号获取资费信息
			PProdTariff tariff = userProdComponent.queryProdTariffById(cProd.getTariff_id());
			if (StringHelper.isNotEmpty(tariff.getUse_fee_rule()) || StringHelper.isNotEmpty(tariff.getRule_id())){
				//根据规则计算fee
				TRuleDefine rule = expressionUtil.queryByRuleId(tariff.getRule_id());
				String preBillingRule = null;//根据rule_id获取
				if(null != rule){
					preBillingRule = rule.getPre_billing_rule();
				}
				if (StringHelper.isEmpty(preBillingRule)){
					fee=0;
				} else if (SystemConstants.PRE_FEE_PRICE.equals(preBillingRule)){
					fee = price;
				} else {
					//根据规则计算
				}
				
			} else {
				fee=0;
			}
			
			//如果价格大于0
			//根据prodSn获取账目信息
			CAcctAcctitem acctitem = null;
			List<AcctitemDto> acctitemList = acctComponent.queryAcctItemByUserId(userId);
			for(AcctitemDto dto : acctitemList){
				if(dto.getAcctitem_id().equals(cProd.getProd_id())){
					acctitem = dto;
					break;
				}
			}
			//获取累计预扣款总额 
			/**
			 * 不用获取，直接通过本月费用获取
			 */
			int totalPreFee =0;
//			List<CAcctPreFee> acctPreFeeList = acctComponent.queryByUserIdandProdId(userId, prodId);
//			if(null !=acctPreFeeList && acctPreFeeList.size()>0){
//				for(CAcctPreFee preFee : acctPreFeeList){
//					totalPreFee = totalPreFee + preFee.getFee();
//				}
//			}
			//获取账目余额
			int acctBalance = acctitem.getActive_balance() - acctitem.getOrder_balance() -
			acctitem.getReal_fee() - totalPreFee ;
			//获取需要转账金额
			int needTransFee = acctitem.getActive_balance() - acctitem.getOrder_balance() -
				acctitem.getReal_fee() - totalPreFee - fee;
			if (fee>0){
				if (needTransFee < 0){
					//获取公用账目信息
					CAcct acct = acctComponent.queryCustAcctByCustId(cProd.getCust_id());
					CAcctAcctitem publicAcctItem= acctComponent.queryAcctItemByAcctitemId(acct.getAcct_id(), SystemConstants.ACCTITEM_PUBLIC_ID);
					int canTransFee = publicAcctItem.getCan_trans_balance();
					acctBalance = acctBalance +canTransFee;
					
					//可转余额小于需要转的余额
					if (canTransFee<-needTransFee){
						needFee = (canTransFee + needTransFee);
						result = fee + ";" + (canTransFee + needTransFee)+";"+acctBalance;
						fee = canTransFee + needTransFee;
					} else {
						needFee = (canTransFee + needTransFee);
						result = fee + ";0;"+acctBalance;
						//做账户转账
						acctComponent.trans(cProd.getCust_id(), doneCode, busiCode,
								publicAcctItem.getAcct_id(), publicAcctItem.getAcctitem_id(), 
								acctitem.getAcct_id(), acctitem.getAcctitem_id() ,-needTransFee);
						//生成账务模式判断任务
						jobComponent.createAcctModeCalJob(doneCode, cProd.getCust_id());
						//生成计算到期日任务
						jobComponent.createInvalidCalJob(doneCode, cProd.getCust_id());
					}
				}else{
					needFee = 0;
					result = fee +";0;"+acctBalance;
				}
				//记录预扣费信息 如果费用大于0 同时 需要充值金额等于0
				if(fee > 0 && needFee==0){
					CAcctPreFee acctPreFee = new CAcctPreFee(doneCode,cProd.getCust_id(),userId,acctitem.getAcct_id(),
							acctitem.getAcctitem_id(),prodId,fee,requestTime,cProd.getArea_id(),cProd.getCounty_id(),
							progName,transId,detailParams,new Date(),SystemConstants.BOOLEAN_TRUE,progId);
					//设置为有效扣费
					acctPreFee.setIs_valid("T");
					acctComponent.saveCAcctPreFee(acctPreFee);
					//更新账单
					BBill bill = billComponent.queryLatsBillByProdSn(cProd.getProd_sn());
					if(null == bill){
						BBill bbill = billComponent.createVodBill(cProd, doneCode, DateHelper.format(new Date(), DateHelper.FORMAT_YM), fee, fee, "1");
						//出账
						billComponent.confirmBill(bbill.getProd_sn(), doneCode);
					}else{
						billComponent.updateBill(cProd.getProd_sn(),bill.getOwe_fee()+fee);
						//出账
						billComponent.confirmBill(cProd.getProd_sn(), doneCode);
					}
					//更新欠费字段
					acctComponent.changeAcctItemOwefee(true, acctitem.getAcct_id(), acctitem.getAcctitem_id(), fee);
					//插入实时销账任务
					jobComponent.createCustWriteOffJob(doneCode, cProd.getCust_id(), SystemConstants.BOOLEAN_TRUE);
					//生成账务模式判断任务
					jobComponent.createAcctModeCalJob(doneCode, cProd.getCust_id());
					//生成计算到期日任务
					jobComponent.createInvalidCalJob(doneCode, cProd.getCust_id());
				}
			}else{
				result = fee + ";0;"+acctBalance;
			}
		}
		
		saveAllPublic(doneCode,this.getBusiParam());
		
		return result;
	}
	
	/**
	 * 取消预扣费接口
	 */
	public void cancelVodPreFee(String transId,String userId) throws Exception{
		Integer doneCode = doneCodeComponent.gDoneCode();
		String busiCode = this.getBusiParam().getBusiCode();
		if(StringHelper.isEmpty(busiCode)){
			busiCode = BusiCodeConstants.CANCEL_VOD_PRE_FEE;
			this.getBusiParam().setBusiCode(busiCode);
		}
		
		//取消预扣款
		CAcctPreFee acctPreFee = acctComponent.queryByTransId(transId, userId);
		acctPreFee.setStatus(SystemConstants.BOOLEAN_FALSE);
		acctPreFee.setCancel_time(new Date());
		acctComponent.updateAcctPreFee(acctPreFee);
		
		//取消转账
		acctComponent.cancelTrans(acctPreFee.getDone_code(), busiCode);
		
		saveAllPublic(doneCode,this.getBusiParam());
	}
	
	/**
	 * 任务处理，取消预扣费接口
	 * @param acctPreFee
	 * @throws Exception
	 */
	public void cancelVodPreFee(CAcctPreFee acctPreFee) throws Exception{
		Integer doneCode = doneCodeComponent.gDoneCode();
		String busiCode = this.getBusiParam().getBusiCode();
		if(StringHelper.isEmpty(busiCode)){
			busiCode = BusiCodeConstants.CANCEL_VOD_PRE_FEE;
			this.getBusiParam().setBusiCode(busiCode);
		}
		
		acctPreFee.setStatus(SystemConstants.BOOLEAN_FALSE);
		acctPreFee.setCancel_time(new Date());
		acctComponent.updateAcctPreFee(acctPreFee);
		
		//取消转账
		acctComponent.cancelTrans(acctPreFee.getDone_code(), busiCode);
		
		saveAllPublic(doneCode,this.getBusiParam());
	}
	
	public List<CAcctPreFee> queryVodPreFees(String cardId) throws Exception{
		return acctComponent.queryVodPreFees(cardId);
	}
	

	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.IAcctService#queryCompanyWithOutAcct()
	 */
	public List<SDept> queryCompanyWithOutAcct() throws Exception {
		return acctComponent.queryCompanyWithOutAcct();
	}

	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.IAcctService#saveGeneralAcct(com.ycsoft.beans.core.acct.CGeneralAcct)
	 */
	public void saveGeneralAcct(CGeneralAcct generalAcct) throws Exception {
		Integer doneCode = doneCodeComponent.gDoneCode();
		
		
		generalAcct.setType(SystemConstants.General_ACCT_TYPE_COMPANY);
		//保存分公司账户
		acctComponent.saveGeneralAcct(generalAcct);
		
		//记录非公司账户费用
		feeComponent.saveGeneralAcctFee(generalAcct.getG_acct_id(),generalAcct.getBalance(), doneCode,generalAcct.getCounty_id());
		
		saveAllPublic(doneCode,getBusiParam());
	}
	
	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.IAcctService#saveGeneralContract(com.ycsoft.beans.core.acct.CGeneralContract)
	 */
	public void saveGeneralContract(CGeneralContract generalContract, CFeePayDto pay,
			SOptr optr, Integer credentialStartNo, Integer credentialEndNo,
			Integer credentialAmount, Integer presentAmount) throws Exception {
		Integer doneCode = doneCodeComponent.gDoneCode();
		
		String payType = pay.getPay_type();
		
		//验证合同号不重复
		String contractNo = generalContract.getContract_no();
		CGeneralContract contract = acctComponent.queryByContractNo(contractNo);
		if(null != contract){
			throw new ServicesException("合同号"+contractNo+"已经存在，不能重复");
		}
		
		String busiCode = getBusiParam().getBusiCode();
		//非营业收费 无cust，默认cust_id=111
		String custId = "111";
		pay.setFee(generalContract.getNominal_amount());
		CFeePayDto feePay = feeComponent.savePayFeeNew(pay, custId, doneCode);
		RInvoice invoice = invoiceComponent.queryInvoiceById(pay.getInvoice_id()).get(0);
		CFee fee = feeComponent.saveFeeUnitpre(payType, generalContract
				.getFee_id(), generalContract.getFee_type(), generalContract
				.getNominal_amount(), doneCode, doneCode, busiCode,
				SystemConstants.BOOLEAN_FALSE,generalContract.getAddr_district(), feePay.getPay_sn(), custId, invoice);
		String[] feeSns = {fee.getFee_sn()};
		invoiceComponent.useInvoice(invoice.getInvoice_code(), invoice.getInvoice_id(), invoice.getInvoice_mode(), generalContract.getNominal_amount());
		printComponent.saveDoc(feeComponent.queryAutoMergeFees(feeSns), null,
				doneCode, getBusiParam().getBusiCode());
		
		
		int total = 0;
		if(null != credentialEndNo && credentialEndNo > 0){
			total = (credentialEndNo-credentialStartNo + 1) * credentialAmount;
			generalContract.setTotal_amount(total);
		}
		
		generalContract.setFee_sn(fee.getFee_sn());
		//如果是分期付款
		if(SystemConstants.PAY_TYPE_DELEYPAY.equals(payType)){
			generalContract.setPayed_amount(0);
		}else{
			generalContract.setPayed_amount(generalContract.getNominal_amount());
		}
		acctComponent.saveGeneralContract(generalContract);
		
		
		//保存合同凭据
		if(total != 0){
			presentAmount = total - generalContract.getNominal_amount();
			if(presentAmount < 0){
				presentAmount = 0;
			}
			DecimalFormat df = new DecimalFormat("#.00");
			double rate = Double.valueOf(df.format((double)presentAmount/(double)total));
			int percent = (int) (rate * 100);
			
			//保存合同凭据明细
			CGeneralContractDetail contractDetail = new CGeneralContractDetail();
			contractDetail.setContract_id(generalContract.getContract_id());
			contractDetail.setCredential_amount(credentialAmount);
			contractDetail.setPercent(percent);
			contractDetail.setCredential_start_no(credentialStartNo);
			contractDetail.setCredential_end_no(credentialEndNo);
			acctComponent.saveGeneralContractDetail(contractDetail);
			
			
			//如果支付方式不等于分期付款
			if(!SystemConstants.PAY_TYPE_DELEYPAY.equals(payType)){
				List<CGeneralCredential> generalCredentialList = new ArrayList<CGeneralCredential>();
				if(null != credentialEndNo && credentialEndNo > 0){
					for(int i=credentialStartNo;i<=credentialEndNo;i++){
						CGeneralCredential generalCredential = new CGeneralCredential();
						generalCredential.setContract_id(generalContract.getContract_id());
						generalCredential.setCredential_no(contractNo+StringHelper.leftWithZero(i+"", 5));
						generalCredential.setAmount(credentialAmount);
						generalCredential.setBalance(credentialAmount);
						generalCredential.setPercent(percent);
						generalCredentialList.add(generalCredential);
					}
				}
				
				acctComponent.saveGeneralCredentialList(generalCredentialList);
			}
		}
		
		saveAllPublic(doneCode,getBusiParam());
	}
	
	/**
	 * 
	 */
	public void savePayUnBusiFee(String contractId, int fee) throws Exception{
		Integer doneCode = doneCodeComponent.gDoneCode();
		String busiCode = getBusiParam().getBusiCode();
		//保存合同付款信息
		acctComponent.saveGeneralContractPay(doneCode,contractId,fee);
		
		//更新合同已付金额
		CGeneralContract generalContract = acctComponent.queryGeneralContractById(contractId);
		generalContract.setPayed_amount(generalContract.getPayed_amount() + fee);
		acctComponent.updateGeneralContract(generalContract);
		
		CFee cFee = feeComponent.queryBySn(generalContract.getFee_sn());
		//费用不需要打印
		feeComponent.saveFeeUnitpre(SystemConstants.PAY_TYPE_CASH, cFee
				.getFee_id(), cFee.getFee_type(), fee, doneCode, doneCode,
				busiCode, SystemConstants.BOOLEAN_TRUE,null);
		
		String contractNo = generalContract.getContract_no();
		
		//获得凭据明细
		CGeneralContractDetail generalContractDetail = acctComponent.queryGeneralContractDetailById(contractId);
		if(null != generalContractDetail){
			
			List<CGeneralCredential> generalCredentialList = new ArrayList<CGeneralCredential>();
			
			//查询上次支付的最后一张凭据
			CGeneralCredential lastGeneralCredential = acctComponent.queryLastCredential(contractId);
			
			int percent = generalContractDetail.getPercent();
			//凭据起始和截止号
			Integer startNo = generalContractDetail.getCredential_start_no();
			Integer endNo = generalContractDetail.getCredential_end_no();
			
			//每张凭据金额
			int amount = generalContractDetail.getCredential_amount();
			//实际每张凭据现金
			int cash = amount - amount * generalContractDetail.getPercent() / 100;
			
			//需要插入的凭据张数
			int num = 0;
			
			//尚未插入凭据（即未支付）
			if(null == lastGeneralCredential){
				//本次付款能插入凭据张数
				num = fee / cash;//整除的张数
				num = num + (fee % cash == 0 ? 0 : 1);//如果fee不能能被cash整除，数量再加一，最后一张凭据金额由下次付款时补充
				//如果比截止号大，取截止
				num = (num+startNo-1) > endNo ? endNo : (num+startNo-1);
				
			}else{
				//根据最后一张凭据号，得到本次付款的第一张凭据号
				String lastNoStr = lastGeneralCredential.getCredential_no();
				int lastNo = Integer.valueOf(lastNoStr.substring(contractNo.length(), lastNoStr.length()));
				startNo = lastNo + 1;
				
				//如果最后一张凭据金额小于配置金额，补齐
				if(lastGeneralCredential.getAmount() < amount){
					//需要补齐的现金
					int diffAmount = amount - lastGeneralCredential.getAmount();
					int diffCash = diffAmount - diffAmount * percent / 100;
					
					//更新凭据金额和剩余金额
					lastGeneralCredential.setAmount(amount);
					lastGeneralCredential.setBalance(diffAmount + lastGeneralCredential.getBalance());
					
					acctComponent.updateGeneralCredential(lastGeneralCredential);
					
					//实际剩余金额
					fee = fee - diffCash;
				}
				
				num = fee / cash;//整除的张数
				num = num + (fee % cash == 0 ? 0 : 1);//如果fee不能能被cash整除，数量再加一，最后一张凭据金额由下次付款时补充
				
				//如果比截止号大，取截止
				num = (num+startNo-1) > endNo ? (endNo-num-startNo) : (num+startNo-1);
			}
			
			for(int i=startNo;i<=num;i++){
				if(i != num){
					CGeneralCredential generalCredential = new CGeneralCredential();
					generalCredential.setContract_id(generalContract.getContract_id());
					generalCredential.setCredential_no(contractNo+StringHelper.leftWithZero(i+"", 5));
					generalCredential.setAmount(amount);
					generalCredential.setBalance(amount);
					generalCredential.setPercent(percent);
					generalCredentialList.add(generalCredential);
					
					fee = fee -cash;
				}else{
					CGeneralCredential generalCredential = new CGeneralCredential();
					generalCredential.setContract_id(generalContract.getContract_id());
					generalCredential.setCredential_no(contractNo+StringHelper.leftWithZero(i+"", 5));
					//最后一笔,按剩余现金折算
					int lastAmount = 0;
					if(percent == 0){
						lastAmount = fee;//没赠送的时候
					}else{
						lastAmount = (fee * 100 / percent ) > amount ? amount : (fee * 100 / percent );
					}
					generalCredential.setAmount(lastAmount );
					generalCredential.setBalance(lastAmount);
					generalCredential.setPercent(percent);
					generalCredentialList.add(generalCredential);
				}
			}
			
			acctComponent.saveGeneralCredentialList(generalCredentialList);
		}
		
		
		saveAllPublic(doneCode,getBusiParam());
//		saveDoneCode(doneCode, getBusiParam().getBusiCode(), null);
	}

	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.IAcctService#queryGeneralContracts()
	 */
	public Pager<GeneralContractDto> queryGeneralContracts(Integer start, Integer limit, String query) throws Exception {
		return acctComponent.queryGeneralContracts(start,limit,query);
	}
	
	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.IAcctService#editGeneralContract(java.lang.String, java.lang.String, java.lang.Integer)
	 */
	public void editGeneralContract(CGeneralContract generalContract) throws Exception {
		Integer doneCode = doneCodeComponent.gDoneCode();
		
		
		//更新合同
		acctComponent.updateGeneralContract(generalContract);
		
//		saveAllPublic(doneCode,getBusiParam());
		saveDoneCode(doneCode, getBusiParam().getBusiCode(), null);
	}
	
	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.IAcctService#saveRemoveContract(java.lang.String)
	 */
	public void saveRemoveContract(String contractId) throws Exception {
		Integer doneCode = doneCodeComponent.gDoneCode();
		
		
		CGeneralContract contract = acctComponent.queryGeneralContractById(contractId);
		acctComponent.removeContractWithHis(contract, doneCode);
		
		CFee fee = feeComponent.queryBySn(contract.getFee_sn());
		/*List<CFee> feeList = feeComponent.queryContractPay(contract.getContract_id());
		feeList.add(fee);
		for(CFee cFee : feeList){
			cancelFee(doneCode, null, cFee);
		}*/
		if(fee != null){
			feeComponent.saveCancelFee(fee, doneCode);
			feeComponent.saveCancelPayFee(fee.getPay_sn(), doneCode);
		}
		
		saveAllPublic(doneCode,getBusiParam());
//		saveDoneCode(doneCode, getBusiParam().getBusiCode(), null);
	}
	
	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.IAcctService#saveRefundUnBusiFee(com.ycsoft.beans.core.acct.CGeneralContract)
	 */
	public void saveRefundUnBusiFee(CGeneralContract generalContract) throws Exception {
		Integer doneCode = doneCodeComponent.gDoneCode();
		
		String busiCode = getBusiParam().getBusiCode();
		CFee fee = feeComponent.saveFeeUnitpre(SystemConstants.PAY_TYPE_CASH,
				generalContract.getFee_id(), generalContract.getFee_type(),
				generalContract.getNominal_amount(), doneCode, doneCode,
				busiCode, SystemConstants.BOOLEAN_FALSE,null);
		String[] feeSns = {fee.getFee_sn()};
		printComponent.saveDoc(feeComponent.queryAutoMergeFees(feeSns), null,
				doneCode, getBusiParam().getBusiCode());
		
		if(null != generalContract.getTotal_amount()){
			generalContract.setTotal_amount(-generalContract.getTotal_amount());
		}
		generalContract.setPayed_amount(-generalContract.getPayed_amount());
		generalContract.setFee_sn(fee.getFee_sn());
		acctComponent.saveGeneralContract(generalContract);
		
		//删除预收款凭据
		acctComponent.removeGeneralCredentialByContractId(generalContract.getContract_id());
		
		saveAllPublic(doneCode,getBusiParam());
//		saveDoneCode(doneCode, getBusiParam().getBusiCode(), null);
	}
	
	/**
	 * 作废冻结金额
	 */
	public void clearInactiveAmount(String sn,String acctId, String acctItemId,int fee,int preFee) throws Exception {
		Integer doneCode = doneCodeComponent.gDoneCode();
		String custId = getBusiParam().getCust().getCust_id();
		String busiCode = getBusiParam().getBusiCode();
		acctComponent.clearInactiveAmount(doneCode,sn, acctId, acctItemId,fee);
		//记录作废金额异动
		acctComponent.saveAcctitemChange(doneCode,
				busiCode, custId, acctId, 
				acctItemId, SystemConstants.ACCT_CHANGE_INVALID,
				SystemConstants.ACCT_FEETYPE_PRESENT, fee*-1,preFee, doneCode);
		
		//创建销帐任务
		jobComponent.createCustWriteOffJob(doneCode, custId, SystemConstants.BOOLEAN_TRUE);
		
		jobComponent.createAcctModeCalJob(doneCode, custId);
		//修改产品对应的到期日
		CAcctAcctitem acctItem = acctComponent.queryAcctItemByAcctitemId(acctId, acctItemId);
		CProd prod = userProdComponent.queryByAcctItem(acctId, acctItemId);
		userProdComponent.updateInvalidDate(doneCode,  prod.getProd_sn(),0, fee*-1, acctItem);
		
		//生成计算到期日任务
		jobComponent.createInvalidCalJob(doneCode ,custId);
		
		saveAllPublic(doneCode, getBusiParam());
	}
	
	
	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.IAcctService#queryAllByAcctitemId(com.ycsoft.business.commons.pojo.BusiParameter, java.lang.String, java.lang.String)
	 */
	public List<CAcctAcctitemOrder> queryAllByAcctitemId(String acctId, String acctItemId) throws Exception {
		return acctComponent.queryAllAcctitemOrder(acctId, acctItemId);
	}
	
	public List<CAcctAcctitemThresholdProp> queryAcctitemThresholdProp(
			String acctId, String acctItemId) throws Exception {
		return acctComponent.queryAcctitemThresholdProp(acctId, acctItemId);
	}
	
	
	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.IAcctService#queryAllAcctitemOrder(java.lang.String, java.lang.String)
	 */
	public List<CAcctAcctitemOrder> queryAllAcctitemOrder(String acctId,
			String acctItemId) throws Exception {
		return acctComponent.queryAllAcctitemOrder(acctId, acctItemId);
	}
	
	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.IAcctService#queryUnBusiFee()
	 */
	public List<TBusiFee> queryUnBusiFee() throws Exception {
		return feeComponent.queryUnBusiFee();
	}

	
	public CBankPay queryBankPay(String banklogid) throws Exception {
		return feeComponent.queryBankPayByLogId(banklogid);
	}

	public void saveBankPay(CBankPay bankPay) throws JDBCException {
		feeComponent.saveBankPay(bankPay);
		feeComponent.updateBankPayByDoneCode(bankPay.getDone_code());
	}
	
	public CAcctAcctitem queryAcctItemByAcctitemId(String acctId,String acctItemId) throws JDBCException {
		return acctComponent.queryAcctItemByAcctitemId(acctId, acctItemId);
	}
	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.IAcctService#queryAcctitemInvalidByCustId(java.lang.String)
	 */
	public List<AcctAcctitemInvalidDto> queryAcctitemInvalidByCustId(String custId)
	throws Exception {
		return acctComponent.queryAcctitemInvalidByCustId(custId);
	}

	/**
	 * @param expressionUtil the expressionUtil to set
	 */
	public void setExpressionUtil(ExpressionUtil expressionUtil) {
		this.expressionUtil = expressionUtil;
	}

	/**
	 * @param printComponent the printComponent to set
	 */
	public void setPrintComponent(PrintComponent printComponent) {
		this.printComponent = printComponent;
	}

	/**
	 * 银行回盘处理
	 * 因为事物控制问题，分离了处理缴费和1301充值成功更新回盘标记的代码
	 * @return
	 */
	public void runBankReturn(BankReturnDto r,String errorinfo) throws Exception {
				// 扣款回盘
				if ("1301".equals(r.getBusi_type())
						&& "0000".equals(r.getIs_success())) {
					if(errorinfo!=null){
						// 缴费失败更新回盘记录状态
						CBankReturn cbr2 = new CBankReturn();
						cbr2.setBank_trans_sn(r.getBank_trans_sn());
						cbr2.setPay_status("FAILURE");
						cbr2.setPay_failure_reason(errorinfo);
						acctComponent.updateBankReturn(cbr2);

						// 插入回盘错误记录
						CBankReturnPayerror dest = new CBankReturnPayerror();
						BeanUtils.copyProperties(r,dest);
						dest.setPay_status("FAILURE");
						dest.setPay_failure_reason(errorinfo);
						dest.setBank_fee_name(r.getBank_fee_name());
						dest.setCreate_time(null);
						acctComponent.saveBankReturnPayerror(dest);
					}
				} else if("9301".equals(r.getBusi_type())
						&& "0000".equals(r.getIs_success())){
					CBankRefundtodisk cbf = acctComponent.findBankRefundtodisk(r.getTrans_sn());
					if(cbf == null){
						throw new IllegalArgumentException("交易流水号【"+ r.getTrans_sn() + "】,没有退款记录!");
					}
					String error_sn = cbf.getBank_trans_sn();
					// 退款成功
					if("0000".equals(r.getIs_success())){
						// 更新状态
						CBankReturn cbr2 = new CBankReturn();
						cbr2.setBank_trans_sn(r.getBank_trans_sn());
						cbr2.setPay_status("SUCCESS");
						acctComponent.updateBankReturn(cbr2);
						
						// 更新错误记录
						CBankReturnPayerror dest = new CBankReturnPayerror();
						dest.setBank_trans_sn(error_sn);
						dest.setRefund_bank_sn(r.getBank_trans_sn());
						dest.setRefund_status("SUCCESS");
						acctComponent.updateBankReturnPayerror(dest); // UPDATE
					}else{ // 退款失败
						// 更新状态
						CBankReturn cbr2 = new CBankReturn();
						cbr2.setBank_trans_sn(r.getBank_trans_sn());
						cbr2.setPay_status("INVALID");
						acctComponent.updateBankReturn(cbr2);
						
						// 更新错误记录
						CBankReturnPayerror dest = new CBankReturnPayerror();
						dest.setBank_trans_sn(error_sn);
						dest.setRefund_bank_sn(r.getBank_trans_sn());
						dest.setRefund_status("FAILURE");
						acctComponent.updateBankReturnPayerror(dest); // UPDATE
					}
				}else {
					CBankReturn cbr2 = new CBankReturn();
					cbr2.setBank_trans_sn(r.getBank_trans_sn());
					cbr2.setPay_status("INVALID");
					acctComponent.updateBankReturn(cbr2);
				}
	}
	public List<PPromFee> queryIsPromFee(String userId, String prodId) throws Exception {
		return userComponent.queryIsPromFee(userId,prodId);
	}
	
	public void cancelFree(String acctId, String acctItemId, String prodSn,
			int fee) throws Exception {
		//获取客户信息
		CCust cust = getBusiParam().getCust();
		String busiCode = this.getBusiParam().getBusiCode();
		//获取业务流水
		Integer doneCode = doneCodeComponent.gDoneCode();
		CAcctAcctitem acctitem = acctComponent.queryAcctItemByAcctitemId(acctId, acctItemId);
		
		if (acctitem!=null){//账目还存在
			
			acctComponent.changeAcctItemBanlance(doneCode, busiCode, cust.getCust_id(),
					acctitem.getAcct_id(), acctitem.getAcctitem_id(),
					SystemConstants.ACCT_CHANGE_INVALID, SystemConstants.ACCT_FEETYPE_PRESENT, fee*-1, null);
			
			//插入新的作废金额
			this.acctComponent.createAcctitemInvalid(acctitem,cust.getCust_id(), doneCode, busiCode,SystemConstants.ACCT_FEETYPE_PRESENT,fee);
			
			//修改产品对应的到期日
			CAcctAcctitem acctItem = acctComponent.queryAcctItemByAcctitemId(acctId, acctItemId);
			userProdComponent.updateInvalidDate(doneCode,  prodSn,0, fee*-1, acctItem);
			
			//生成计算到期日任务
			jobComponent.createInvalidCalJob(doneCode ,cust.getCust_id());
			
			
		} else {
			//账目不存在
			throw new ServicesException("账目已经被删除，无法作废");
		}
		
		saveAllPublic(doneCode,getBusiParam());
	}

}
