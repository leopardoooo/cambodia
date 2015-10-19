/**
 *
 */
package com.ycsoft.business.component.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.cust.CCust;
import com.ycsoft.beans.core.fee.CFee;
import com.ycsoft.beans.core.print.CInvoicePropChange;
import com.ycsoft.beans.core.prod.CProd;
import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.beans.invoice.RInvoice;
import com.ycsoft.beans.invoice.RInvoiceDetail;
import com.ycsoft.beans.invoice.RInvoiceFeelist;
import com.ycsoft.beans.invoice.RInvoiceOptr;
import com.ycsoft.business.commons.abstracts.BaseBusiComponent;
import com.ycsoft.business.dao.core.cust.CCustDao;
import com.ycsoft.business.dao.core.fee.CFeeDao;
import com.ycsoft.business.dao.core.print.CDocDao;
import com.ycsoft.business.dao.core.print.CInvoiceDao;
import com.ycsoft.business.dao.core.print.CInvoicePropChangeDao;
import com.ycsoft.business.dao.core.user.CUserDao;
import com.ycsoft.business.dao.resource.invoice.RInvoiceDao;
import com.ycsoft.business.dao.resource.invoice.RInvoiceDetailDao;
import com.ycsoft.business.dao.resource.invoice.RInvoiceFeelistDao;
import com.ycsoft.business.dao.resource.invoice.RInvoiceOptrDao;
import com.ycsoft.business.dao.system.SDeptDao;
import com.ycsoft.business.dto.config.TemplateConfigDto;
import com.ycsoft.business.dto.core.acct.PayDto;
import com.ycsoft.business.dto.core.fee.FeeDto;
import com.ycsoft.business.dto.core.print.CInvoiceDto;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.constants.InvoiceOptrType;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ComponentException;
import com.ycsoft.commons.exception.ErrorCode;
import com.ycsoft.commons.exception.ServicesException;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.commons.helper.DateHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.sysmanager.dto.resource.invoice.InvoiceDetailDto;
import com.ycsoft.sysmanager.dto.resource.invoice.InvoiceDto;

/**
 * @author liujiaqi
 *
 */
@Component
public class InvoiceComponent extends BaseBusiComponent {

	private CInvoiceDao cInvoiceDao;
	private CFeeDao cFeeDao;
	private RInvoiceDao rInvoiceDao;
	private RInvoiceOptrDao rInvoiceOptrDao;
	private RInvoiceDetailDao rInvoiceDetailDao;
	private RInvoiceFeelistDao rInvoiceFeelistDao;
	private CCustDao cCustDao;
	private CUserDao cUserDao;
	private CInvoicePropChangeDao cInvoicePropChangeDao;
	private CDocDao cDocDao;
	private SDeptDao sDeptDao;
	
	/**
	 * 根据发票id查询发票详细信息
	 * @param invoiceId
	 * @return
	 * @throws Exception
	 */
	public InvoiceDto queryInvoiceByInvoiceId(String invoiceId,String invoiceCode) throws Exception {
		boolean flag = rInvoiceDao.isExistsInvoice(invoiceId, invoiceCode);
		if(!flag){
			throw new ComponentException(ErrorCode.InvoiceNotExists);
		}
		
		InvoiceDto invoice = rInvoiceDao.queryInvoiceByInvoiceId(invoiceId,invoiceCode);
		List<InvoiceDetailDto> detail = rInvoiceDao.queryDetail(invoiceId,invoice.getInvoice_book_id(),invoice.getInvoice_code());
		invoice.setInvoiceDetailList(detail);
		return invoice;
	}
	
	/**
	 * 根据发票ID 和发票 CODE 修改发票状态.
	 * @param invoiceId
	 * @param invoiceCode
	 */
	public void editInvoiceStatus(String invoiceId, String invoiceCode,String newStatus) throws Exception{
		rInvoiceDao.updateStatusByIdAndCode(invoiceId, invoiceCode,newStatus);
	}
	
	/**
	 * 根据发票号查询发票信息
	 * @param invoiceId
	 * @return
	 * @throws Exception
	 */
	public List<RInvoice> queryInvoiceById(String invoiceId) throws Exception {
		return rInvoiceDao.queryInvoiceByCountyId(invoiceId,getOptr().getCounty_id());
	}
	

	/**
	 * 使用发票
	 *
	 * @param invoiceCode
	 * @param invoiceId
	 * @param invoiceMode
	 *            开具方式
	 */
	public void useInvoice(String invoiceCode, String invoiceId,
			String invoiceMode,int amount) throws JDBCException, ComponentException {
		rInvoiceDao.useInvoice(invoiceCode, invoiceId, invoiceMode,amount);
	}
	
	public void updateInvoiceInfo(String invoiceCode, String invoiceId, String invoiceMode, int amount) throws Exception {
		rInvoiceDao.updateInvoiceInfo(invoiceCode, invoiceId, invoiceMode,amount, getOptr().getOptr_id());
	}
	
	public void updateDocType(String docSn, String docType) throws JDBCException {
		cDocDao.updateDocType(docSn, docType);
	}
	
	/**
	 * 更换发票，记录异动
	 * @param doneCode
	 * @param oldInvoiceId
	 * @param oldInvoiceCode
	 * @param newInvoiceId
	 * @param newInvoiceCode
	 * @throws Exception
	 */
	public void saveInvoicePropChange(Integer doneCode, String oldInvoiceId,
			String oldInvoiceCode, String newInvoiceId, String newInvoiceCode)
			throws Exception {
		RInvoice oldInvoice = rInvoiceDao.queryInvoice(oldInvoiceCode, oldInvoiceId);
		RInvoice newInvoice = rInvoiceDao.queryInvoice(newInvoiceCode, newInvoiceId);
		
		CInvoicePropChange prop = new CInvoicePropChange();
		prop.setDone_code(doneCode);
		prop.setOld_invoice_id(oldInvoice.getInvoice_id());
		prop.setOld_invoice_code(oldInvoice.getInvoice_code());
		prop.setOld_invoice_book_id(oldInvoice.getInvoice_book_id());
		prop.setOld_invoice_type(oldInvoice.getInvoice_type());
		prop.setNew_invoice_id(newInvoice.getInvoice_id());
		prop.setNew_invoice_code(newInvoice.getInvoice_code());
		prop.setNew_invoice_book_id(newInvoice.getInvoice_book_id());
		prop.setNew_invoice_type(newInvoice.getInvoice_type());
		setBaseInfo(prop);
		cInvoicePropChangeDao.save(prop);
	}
	
	/**
	 * 取消使用发票
	 * @param invoiceCode
	 * @param invoiceId
	 * @throws JDBCException
	 * @throws ComponentException
	 */
	public void cancelUseInvoice(CInvoiceDto oldInvoice) throws JDBCException, ComponentException {
		rInvoiceDao.cancelUseInvoice(oldInvoice.getStatus(),oldInvoice.getInvoice_code(),oldInvoice.getInvoice_id());
	}
	
	/**
	 * 使用发票
	 * @param fees
	 */
	public void useInvoice(List<FeeDto> fees) throws JDBCException {
		for (FeeDto fee:fees)
			rInvoiceDao.useInvoice(fee.getNew_invoice_code(),
					fee.getNew_invoice_id(), SystemConstants.INVOICE_MODE_MANUAL, fee
							.getReal_pay());
		
	}

	/**
	 * 取消使用发票
	 * @param fees
	 */
	public void cancelUseInvoice(List<FeeDto> fees) throws JDBCException {
		for (FeeDto fee:fees)
			rInvoiceDao.cancelUseInvoice(fee.getReal_pay(),fee.getInvoice_code(),fee.getInvoice_id());
	}

	/**
	 * 作废发票,并且清除c_fee里发票相关内容.
	 * @param doneCode
	 * @param invoiceId
	 * @param invoiceCode
	 * @throws Exception
	 */
	public void invalidInvoiceAndClearFeeInfo(Integer doneCode,String invoiceId, String invoiceCode) throws Exception {
		cInvoiceDao.invalidInvoiceAndClearFeeInfo(invoiceId, invoiceCode);
		
		RInvoiceOptr invoiceOptr = new RInvoiceOptr();
		invoiceOptr.setDone_code(doneCode);
		invoiceOptr.setOptr_type(InvoiceOptrType.EDITSTATUS.toString());
		invoiceOptr.setInvoice_count(1);
		invoiceOptr.setOptr_id(getOptr().getOptr_id());
		invoiceOptr.setDept_id(getOptr().getDept_id());
		invoiceOptr.setCounty_id(getOptr().getCounty_id());
		invoiceOptr.setCreate_time(DateHelper.now());
		rInvoiceOptrDao.save(invoiceOptr);
		
		RInvoiceDetail invoiceDetail = new RInvoiceDetail();
		invoiceDetail.setDone_code(doneCode);
		invoiceDetail.setInvoice_code(invoiceCode);
		invoiceDetail.setInvoice_id(invoiceId);
		rInvoiceDetailDao.save(invoiceDetail);
	}
	
	/**
	 * 作废发票
	 * @param invoiceCode
	 * @param invoiceId
	 * @throws JDBCException
	 * @throws ComponentException
	 */
	public void invalidInvoice(Integer doneCode,String invoiceId, String invoiceCode) throws Exception {
		cInvoiceDao.invalidInvoice(invoiceId, invoiceCode);
		
		RInvoiceOptr invoiceOptr = new RInvoiceOptr();
		invoiceOptr.setDone_code(doneCode);
		invoiceOptr.setOptr_type(InvoiceOptrType.EDITSTATUS.toString());
		invoiceOptr.setInvoice_count(1);
		invoiceOptr.setOptr_id(getOptr().getOptr_id());
		invoiceOptr.setDept_id(getOptr().getDept_id());
		invoiceOptr.setCounty_id(getOptr().getCounty_id());
		invoiceOptr.setCreate_time(DateHelper.now());
		rInvoiceOptrDao.save(invoiceOptr);
		
		RInvoiceDetail invoiceDetail = new RInvoiceDetail();
		invoiceDetail.setDone_code(doneCode);
		invoiceDetail.setInvoice_code(invoiceCode);
		invoiceDetail.setInvoice_id(invoiceId);
		rInvoiceDetailDao.save(invoiceDetail);
	
		//定额发票不涉及前台操作
		rInvoiceDao.saveEditStatus(doneCode,StatusConstants.INVALID,"");
	}

	
	
	/**
	 * @param invoiceId
	 * @param invoiceMode
	 * @return
	 * @throws JDBCException
	 * @throws ComponentException
	 */
	public List<RInvoice> checkInvoice(String invoiceId,String docType,String invoiceMode) throws Exception {
		String scopeInvoice = queryTemplateConfig(TemplateConfigDto.Config.SCOPE_INVOICE.toString());
		String[] depts = null;
		if (SystemConstants.SYS_LEVEL_OPTR.equals(scopeInvoice) || SystemConstants.SYS_LEVEL_DEPT.equals(scopeInvoice)) {
			depts = getOptr().getDept_id().split(",");
		}else if (SystemConstants.SYS_LEVEL_COUNTY.equals(scopeInvoice)) {
			depts = CollectionHelper.converValueToArray(sDeptDao.queryByCountyId(getOptr().getCounty_id()), "dept_id");
		}
		
		List<String> errorMsgList = new ArrayList<String>();
		String errorMeg = "";
		List<RInvoice> invoices = new ArrayList<RInvoice>();
		//查找在当前库的发票
		if (SystemConstants.INVOICE_MODE_MANUAL.equals(invoiceMode)){
			//手票不限制发票类型
			List<RInvoice> invoiceList = new ArrayList<RInvoice>();
			invoiceList = rInvoiceDao.queryInvoiceByDepot(invoiceId,depts);
			for(RInvoice invoice : invoiceList){
				//收费清单不能开手工票
				if(!invoice.getInvoice_type().equals(SystemConstants.DOC_TYPE_FEELIST)){
					invoices.add(invoice);
				}
			}
		}else if (SystemConstants.INVOICE_MODE_AUTO.equals(invoiceMode)){
			if (StringHelper.isEmpty(docType)){
				errorMeg = "请确定机打票的发票类型";
				errorMsgList.add(errorMeg);
			}else{
				invoices = rInvoiceDao.queryInvoiceByDepot(invoiceId,depts);
			}
		}else{
			errorMeg = "没有指定出票方式";
			errorMsgList.add(errorMeg);
		}
		//符合条件发票记录
		//有可能发票号相同，发票本号不同的发票修改
		List<RInvoice> list = new ArrayList<RInvoice>();
		
		if(invoices!=null){
			if (invoices.size()==0){
				throw new ServicesException(ErrorCode.ReceiptNotExists);
			}else{
				for (int i=invoices.size()-1;i>=0;i--){
					RInvoice invoice = invoices.get(i);
					if(SystemConstants.SYS_LEVEL_OPTR.equals(scopeInvoice)){
						/*if(!getOptr().getOptr_id().equals(invoice.getOptr_id())){
							errorMeg = "发票["+ invoice.getInvoice_id() +"]未领用";
							errorMsgList.add(errorMeg);
							continue;
						}*/
						if(StringHelper.isEmpty(invoice.getOptr_id())){
							/*errorMeg = "发票["+ invoice.getInvoice_id() +"]未领用";
							errorMsgList.add(errorMeg);
							continue;*/
							throw new ServicesException(ErrorCode.ReceiptNotRecipients, invoice.getInvoice_id());
						}
					}
					if(!docType.equals(invoice.getInvoice_type())){
						errorMeg = "发票类型不符,需要【" + MemoryDict.getDictName(DictKey.INVOICE_TYPE, docType) + "】 ";
						errorMsgList.add(errorMeg);
						continue;
					}
					if(!invoice.getFinance_status().equals(SystemConstants.INVOICE_STATUS_IDLE)){
						/*errorMeg = "发票已结账";
						errorMsgList.add(errorMeg);
						continue;*/
						throw new ServicesException(ErrorCode.ReceiptAlreadyCheckout);
					}else if (!invoice.getStatus().equals(SystemConstants.INVOICE_STATUS_IDLE)
							&&!SystemConstants.INVOICE_MODE_MANUAL.equals(invoice.getInvoice_mode())){
						//打印的发票状态必须为空闲
						/*errorMeg = "发票["+ invoice.getInvoice_id() +"]已使用";
						errorMsgList.add(errorMeg);
						continue;*/
						throw new ServicesException(ErrorCode.ReceiptIsUsed, invoice.getInvoice_id());
					}else if (SystemConstants.INVOICE_MODE_AUTO.equals(invoiceMode)){
						//如果机打票，不允许原先被手工开票过
						if (SystemConstants.INVOICE_MODE_MANUAL.equals(invoice.getInvoice_mode())){
							errorMeg = "发票已经用于手工票，不能进行打印";
							errorMsgList.add(errorMeg);
							continue;
						}else{
							list.add(invoice);
						}
					}else if (SystemConstants.INVOICE_MODE_MANUAL.equals(invoice.getInvoice_mode())){
						//开手工票，开票人必须是同一个操作员
						List<CFee> invices = cFeeDao.queryFeeByInvoice(invoice.getInvoice_code(),invoice.getInvoice_id());
						if (invices.size()>0&&!invices.get(0).getOptr_id().equals(getOptr().getOptr_id())){
							errorMeg = "已被操作员 "+invices.get(0).getOptr_name()+" 开手工票";
							errorMsgList.add(errorMeg);
							continue;
						}else{
							list.add(invoice);
						}
					}else{
						errorMeg = null;
						list.add(invoice);
					}
				}
			}
		}
		
		if(!errorMsgList.isEmpty() && list.size()==0){
			errorMeg = errorMsgList.size()>1 ? "发票不可用":errorMsgList.toArray()[0].toString();
			throw new ComponentException(errorMeg);
		}
		return list;
	}
	
	/**
	 * @param feeList
	 */
	public void queryValidFeeList(List<PayDto> feeList) throws Exception{
		String scopeInvoice = queryTemplateConfig(TemplateConfigDto.Config.SCOPE_INVOICE.toString());
		String depotId = "";
		if (SystemConstants.SYS_LEVEL_OPTR.equals(scopeInvoice)) {
			depotId = getOptr().getOptr_id();
		}else if (SystemConstants.SYS_LEVEL_DEPT.equals(scopeInvoice)) {
			depotId = getOptr().getDept_id();
		}else if (SystemConstants.SYS_LEVEL_COUNTY.equals(scopeInvoice)) {
			depotId = getOptr().getCounty_id();
		}
		
		//查找在当前库的发票
		List<RInvoice> invoices = rInvoiceDao.queryInvoiceByIdAndCode(feeList,depotId);
		Map<String,RInvoice> map = CollectionHelper.converToMapSingle(invoices, "invoice_id","invoice_code");
		
		String optrCounty = getOptr().getCounty_id();
		for(PayDto pay : feeList){
			
			CCust cust = cCustDao.queryCustByCustNo(pay.getCust_no());
			if(cust == null){
				throw new ComponentException("客户编号: "+pay.getCust_no()+" 对应客户不存在");
			}
			if(!cust.getCounty_id().equals(optrCounty)){
				throw new ComponentException("客户名: "
						+ pay.getCust_name() + " 所在县市 " + cust.getCounty_name()
						+ " 和操作员县市不一致，请检查数据或切换县市!");
			}
			
			CUser user = cUserDao.findByKey(pay.getUser_id());
			if(user == null){
				throw new ComponentException("用户ID: "+pay.getUser_id()+" 对应用户不存在");
			}
			if(!user.getCust_id().equals(cust.getCust_id())){
				throw new ComponentException("用户对应客户ID和文件中客户ID: "+cust.getCust_id()+" 不相同");
			}
			CProd prod = cProdDao.findByKey(pay.getProd_sn());
			if(prod == null){
				throw new ComponentException("产品ID: "+pay.getProd_sn()+" 对应产品不存在");
			}else{
				pay.setInvalid_date(DateHelper.dateToStr(prod.getInvalid_date()));
			}
			if(!prod.getUser_id().equals(user.getUser_id())){
				throw new ComponentException("用户订购产品ID和文件中产品ID: "+pay.getProd_sn()+" 不相同");
			}
			
			int fee = pay.getFee().intValue();
			if(fee == 0 && pay.getPresent_fee().intValue() == 0){
				throw new ComponentException("上传的文件中,请填入缴费金额,客户名为: "+pay.getCust_name());
			}
			if(fee == 0)	continue;	//缴费金额为0，不用填发票
			if( StringHelper.isEmpty(pay.getInvoice_id()) || StringHelper.isEmpty(pay.getInvoice_code()) ){
				throw new ComponentException("上传的文件中,存在数据发票为空");
			}
			String key = pay.getInvoice_id() + "_" + pay.getInvoice_code();
			RInvoice invoice = map.get(key);
			if(null == invoice){
				throw new ComponentException("上传的文件中，发票号"+pay.getInvoice_id()+",发票代码"+pay.getInvoice_code()+"的数据，它的发票号不能用或不存在");
			}else{
				pay.setInvoice_code(invoice.getInvoice_code());
			}
		}
	}

	/**
	 * 查询发票
	 * @param invoiceCode
	 * @param invoiceId
	 * @return
	 * @throws Exception
	 */
	public RInvoice queryById(String oldInvoiceId,String oldInvoiceCode) throws JDBCException {
		return rInvoiceDao.queryInvoice(oldInvoiceCode, oldInvoiceId);
	}
	
	public void changeFeelistInvoice(Integer doneCode,String custId,String feelistId, String feelistCode,
			String feelistBookId, String invoiceId, String invoiceCode, String remark) throws Exception {
		RInvoice invoice = rInvoiceDao.queryInvoice(feelistCode, feelistId);
		
		RInvoiceFeelist feelist = new RInvoiceFeelist();
		feelist.setDone_code(doneCode);
		feelist.setFeelist_id(feelistId);
		feelist.setFeelist_code(feelistCode);
		feelist.setFeelist_book_id(feelistBookId);
		feelist.setFeelist_type(invoice.getInvoice_type());
		feelist.setInvoice_id(invoiceId);
		feelist.setInvoice_code(invoiceCode);
		feelist.setAmount(invoice.getAmount());
		feelist.setCust_id(custId);
		feelist.setStatus(StatusConstants.ACTIVE);
		feelist.setStatus_time(DateHelper.now());
		feelist.setOptr_id(getOptr().getOptr_id());
		feelist.setCounty_id(invoice.getDepot_id());
		feelist.setRemark(remark);
		
		rInvoiceFeelistDao.save(feelist);
	}
	
	public void invalidFeeListInvoice(Integer feeDoneCode) throws Exception {
		rInvoiceFeelistDao.updateStatus(feeDoneCode,StatusConstants.INVALID);
	}
	
	/**
	 * 修改发票出票方式
	 * @param feeSn
	 * @param invoiceMode
	 */
	public void editInvoiceMode(String feeSn, String invoiceMode, CInvoiceDto oldInvoice,CInvoiceDto newInvoice,Integer realPay, Integer doneCode) throws Exception {
		CFee fee = new CFee();
		fee.setFee_sn(feeSn);
		if (invoiceMode.equals(SystemConstants.INVOICE_MODE_MANUAL)){//手开
			fee.setIs_doc(SystemConstants.BOOLEAN_TRUE);
			fee.setInvoice_mode(invoiceMode);
			String mId = "xs"+doneCode;//xs + donecode
			fee.setInvoice_id(mId);
			fee.setInvoice_code(mId);
			fee.setInvoice_book_id(mId);
			fee.setInvoice_fee(0);
			fee.setInvoice_mode(invoiceMode);
//			rInvoiceDao.useInvoice(newInvoice.getInvoice_code(),
//					newInvoice.getInvoice_id(), SystemConstants.INVOICE_MODE_MANUAL, realPay);
			
			if(oldInvoice!=null){
				List<CFee> cfList = new ArrayList<CFee>();
				List<CFee> feeList = cFeeDao.queryByInvoiceId(feeSn,oldInvoice,getOptr().getCounty_id());
				for(CFee dto: feeList){
					CFee cf = new CFee();
					cf.setFee_sn(dto.getFee_sn());
					cf.setIs_doc(SystemConstants.BOOLEAN_FALSE);
					cf.setInvoice_mode("");
					cf.setInvoice_id("");
					cf.setInvoice_code("");
					cf.setInvoice_book_id("");
					cfList.add(cf);
				}
				cFeeDao.update(cfList.toArray(new CFee[cfList.size()]));
				cancelUseInvoice(oldInvoice);
				cInvoiceDao.invalidInvoice(oldInvoice.getInvoice_id(), oldInvoice.getInvoice_code());
			}
			
		}else{//机打或定额
			CFee oldFee = cFeeDao.findByKey(feeSn);
			if(StringHelper.isNotEmpty(oldFee.getInvoice_id())){
				rInvoiceDao.cancelUseInvoice(realPay,oldFee.getInvoice_code(),oldFee.getInvoice_id());
			}
			fee.setInvoice_mode("");
			fee.setInvoice_id("");
			fee.setInvoice_code("");
			fee.setInvoice_book_id("");
			
			if(SystemConstants.INVOICE_MODE_QUOTA.equals(invoiceMode)){//定额
				fee.setInvoice_fee(oldFee.getReal_pay());
				fee.setIs_doc(SystemConstants.BOOLEAN_TRUE);
			}else{
				fee.setInvoice_fee(0);
				fee.setIs_doc(SystemConstants.BOOLEAN_FALSE);
			}
			
			//旧发票为机打
			if(oldInvoice!=null && SystemConstants.INVOICE_MODE_AUTO.equals(oldInvoice.getInvoice_mode())){
				List<CFee> cfList = new ArrayList<CFee>();
				List<CFee> feeList = cFeeDao.queryByInvoiceId(feeSn,oldInvoice,getOptr().getCounty_id());
				for(CFee dto: feeList){
					CFee cf = new CFee();
					cf.setFee_sn(dto.getFee_sn());
					cf.setIs_doc(SystemConstants.BOOLEAN_FALSE);
					cf.setInvoice_mode("");
					cf.setInvoice_id("");
					cf.setInvoice_code("");
					cf.setInvoice_book_id("");
					cfList.add(cf);
				}
				cFeeDao.update(cfList.toArray(new CFee[cfList.size()]));
				cancelUseInvoice(oldInvoice);
				cInvoiceDao.invalidInvoice(oldInvoice.getInvoice_id(), oldInvoice.getInvoice_code());
			}
		}
		
		cFeeDao.update(fee);
	}
	
	/**
	 * @param feeSn
	 * @param acctDate
	 */
	public void editAcctDate(String feeSn, String acctDate) throws JDBCException {
		CFee fee = cFeeDao.findByKey(feeSn);
		fee.setAcct_date(DateHelper.strToDate(acctDate));
		cFeeDao.update(fee);
	}
	
	/**
	 * 直接录入新的手工发票入库.
	 * @param newInvoice
	 */
	public void saveManuallyEditMInvoice(RInvoice newRin,RInvoice oldRin,String feeSN, boolean isNewInvoice) throws Exception{
		if(isNewInvoice){
			rInvoiceDao.save(newRin);
		}
		if(oldRin!=null){
			//TODO 不要处理这个如果有需要以后再改
//			editInvoiceStatus(oldRin.getInvoice_id(),oldRin.getInvoice_code(),SystemConstants.INVOICE_STATUS_INVALID);
		}
		String invoiceCode = newRin.getInvoice_code();
		CFee fee = cFeeDao.findByKey(feeSN);
		fee.setInvoice_book_id(invoiceCode);
		fee.setInvoice_code(invoiceCode);
		fee.setInvoice_id(newRin.getInvoice_id());
		cFeeDao.update(fee);
	}
	
	/**
	 * @param invoiceDao
	 *            the rInvoiceDao to set
	 */
	public void setRInvoiceDao(RInvoiceDao invoiceDao) {
		rInvoiceDao = invoiceDao;
	}


	/**
	 * @param invoiceOptrDao the rInvoiceOptrDao to set
	 */
	public void setRInvoiceOptrDao(RInvoiceOptrDao invoiceOptrDao) {
		rInvoiceOptrDao = invoiceOptrDao;
	}


	/**
	 * @param invoiceDetailDao the rInvoiceDetailDao to set
	 */
	public void setRInvoiceDetailDao(RInvoiceDetailDao invoiceDetailDao) {
		rInvoiceDetailDao = invoiceDetailDao;
	}


	/**
	 * @param invoiceDao the cInvoiceDao to set
	 */
	public void setCInvoiceDao(CInvoiceDao invoiceDao) {
		cInvoiceDao = invoiceDao;
	}

	/**
	 * @param feeDao the cFeeDao to set
	 */
	public void setCFeeDao(CFeeDao feeDao) {
		cFeeDao = feeDao;
	}

	public void setRInvoiceFeelistDao(RInvoiceFeelistDao invoiceFeelistDao) {
		rInvoiceFeelistDao = invoiceFeelistDao;
	}

	public void setCCustDao(CCustDao custDao) {
		cCustDao = custDao;
	}

	public void setCUserDao(CUserDao userDao) {
		cUserDao = userDao;
	}

	public void setCInvoicePropChangeDao(CInvoicePropChangeDao invoicePropChangeDao) {
		cInvoicePropChangeDao = invoicePropChangeDao;
	}

	public void setCDocDao(CDocDao docDao) {
		cDocDao = docDao;
	}

	public void setSDeptDao(SDeptDao deptDao) {
		sDeptDao = deptDao;
	}

	public boolean checkInvoiceOptr(List<String> invoiceIdList,String optrId) throws Exception{
		if(invoiceIdList == null || invoiceIdList.size()==0){
			return false;
		}
		
		for(String str : invoiceIdList){
			RInvoice r = rInvoiceDao.queryInvoice(SystemConstants.BASE_INVOICE_CODE,str);
			if(r == null ||StringHelper.isEmpty(r.getOptr_id()) || !r.getOptr_id().equals(optrId)){
				return false;
			}
		}
		return true;
	}
}
