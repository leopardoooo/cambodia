package com.ycsoft.business.commons.interceptor;

import java.lang.reflect.Method;

import org.springframework.aop.AfterReturningAdvice;

import com.ycsoft.business.component.config.ExtTableComponent;
import com.ycsoft.business.component.core.FeeComponent;
import com.ycsoft.business.component.resource.InvoiceComponent;
import com.ycsoft.business.component.task.TaskComponent;


/**
 * 业务保存之后的后续处理拦截器，如保存工单，业务单据等内容
 *
 * @author hh
 * @data Mar 17, 2010 3:40:21 PM
 */
public class AfterServiceInterceptor implements AfterReturningAdvice  {

	private TaskComponent taskComponent;
	private FeeComponent feeComponent;
	private ExtTableComponent extTableComponent;
	private InvoiceComponent invoiceComponent;
	/**
	 * 实现核心的处理函数
	 */
	public void afterReturning(@SuppressWarnings("unused")
	Object returnValue, @SuppressWarnings("unused")
	Method m, @SuppressWarnings("unused")
	Object[] params, Object target) throws Throwable {
//		debug(getClass(), "业务保存的后续处理拦截器!");
//		if(!(target instanceof BaseService)){
//			throw new Exception("该拦截器只适用于Service层...");
//		}
//		BaseService base = (BaseService)target;
//		BusiParameter param = (BusiParameter)base.getParam();
//		String custId = null;
//		CCust cust = param.getCustFullInfo().getCust();
//		if(cust != null){
//			//获取业务流水
//			custId = cust.getCust_id();
//		}
//		
//		//保存扩展信息
//		extTableComponent.saveOrUpdate(param.getExtAttrForm());
//		//保存业务扩展信息
//		if(null != param.getBusiExtAttr()){
//			extTableComponent.saveBusiAttr(param.getDoneCode(), param.getBusiExtAttr());
//		}
//		//保存业务工单
//		String newAddr= param.getTempVar().get(SystemConstants.EXTEND_ATTR_KEY_NEWADDR)==null?
//				null:param.getTempVar().get(SystemConstants.EXTEND_ATTR_KEY_NEWADDR).toString();
//		taskComponent.createTask(param.getTaskIds(), param.getDoneCode(), param.getCustFullInfo(),param.getSelectedUsers(), newAddr);
//
//		//保存业务单据
////		printComponent.saveDoc(param.getDoneCode(),param.getBusiCode(), param.getCust().getCust_id(),param.getDocTypes());
//
//		//检查支付信息是否为NULL，如果不为NULL则保存支付信息，并根据一定的规则保存合并记录。
//		CFeePayDto pay = param.getPay();
//		
//		String payType = SystemConstants.PAY_TYPE_CASH;
//		if(null != pay){
//			payType = pay.getPay_type();
//		}
//		//保存业务费用信息
//		if (param.getFees() !=null)
//			for (FeeBusiFormDto feeDto : param.getFees()) {
//				if(feeDto.getReal_pay() > 0){
//					feeComponent.saveBusiFee(custId, feeDto.getFee_id(), feeDto.getCount(),payType,feeDto
//							.getReal_pay(), param.getDoneCode(),param.getDoneCode(), param.getBusiCode(),
//							param.getSelectedUsers());
//				}
//			}
//
//
//		
//		if(null != pay){
//			//保存缴费信息
//			feeComponent.savePayFee(pay, param.getCust().getCust_id(),param.getDoneCode());
//			
//			//if (pay.getInvoice_mode().equals(SystemConstants.INVOICE_MODE_AUTO))
//			//	printComponent.saveDoc( feeComponent.queryAutoMergeFees(param.getDoneCode()),param.getCust().getCust_id(), param.getDoneCode(),param.getBusiCode());
//			if (SystemConstants.INVOICE_MODE_MANUAL.equals(pay.getInvoice_mode())){
//				feeComponent.saveManualInvoice(param.getDoneCode(), pay
//						.getInvoice_code(), pay.getInvoice_id(), pay
//						.getInvoice_book_id());
//				invoiceComponent.useInvoice(pay.getInvoice_code(),pay.getInvoice_id(), 
//						SystemConstants.INVOICE_MODE_MANUAL, pay.getFee());
//			}
//		}
	}

	public void setTaskComponent(TaskComponent taskComponent) {
		this.taskComponent = taskComponent;
	}

	public void setFeeComponent(FeeComponent feeComponent) {
		this.feeComponent = feeComponent;
	}

	public ExtTableComponent getExtTableComponent() {
		return extTableComponent;
	}

	public void setExtTableComponent(ExtTableComponent extTableComponent) {
		this.extTableComponent = extTableComponent;
	}

	public void setInvoiceComponent(InvoiceComponent invoiceComponent) {
		this.invoiceComponent = invoiceComponent;
	}
}
