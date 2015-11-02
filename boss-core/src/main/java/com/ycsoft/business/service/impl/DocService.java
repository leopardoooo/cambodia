/**
 *
 */
package com.ycsoft.business.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;

import com.ycsoft.beans.config.TBusiDocTemplatefile;
import com.ycsoft.beans.config.TPrintitem;
import com.ycsoft.beans.core.common.CDoneCodeInfo;
import com.ycsoft.beans.core.print.CDoc;
import com.ycsoft.beans.invoice.RInvoice;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.business.cache.PrintContentConfiguration;
import com.ycsoft.business.commons.abstracts.BaseBusiComponent;
import com.ycsoft.business.commons.pojo.BusiParameter;
import com.ycsoft.business.component.config.BusiConfigComponent;
import com.ycsoft.business.component.core.PrintComponent;
import com.ycsoft.business.dto.config.TaskQueryWorkDto;
import com.ycsoft.business.dto.config.TemplateConfigDto;
import com.ycsoft.business.dto.core.fee.FeeDto;
import com.ycsoft.business.dto.core.fee.MergeFeeFormDto;
import com.ycsoft.business.dto.core.print.CInvoiceDto;
import com.ycsoft.business.dto.core.print.DocDto;
import com.ycsoft.business.dto.core.print.InvoiceFromDto;
import com.ycsoft.business.dto.core.print.PrintItemDto;
import com.ycsoft.business.dto.print.BusiDocPrintConfigDto;
import com.ycsoft.business.dto.print.BusiDocPrintItemDto;
import com.ycsoft.business.dto.print.PrintFeeitemDto;
import com.ycsoft.business.service.IDocService;
import com.ycsoft.commons.constants.BusiCodeConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ServicesException;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.commons.helper.DateHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.store.MemoryPrintData;
import com.ycsoft.sysmanager.dto.resource.invoice.InvoiceDto;

/**
 * @author liujiaqi
 *
 */
@Service
public class DocService extends BaseBusiService implements IDocService {
	protected BusiConfigComponent busiConfigComponent;
	private BaseBusiComponent baseBusiComponent;
	private PrintComponent printComponent;
	
	public List<CInvoiceDto> queryInvoiceByCustId(String custId) throws Exception{
		return printComponent.queryInvoiceByCustId(custId);
	}

	public List<TaskQueryWorkDto> queryTaskByCustId(String custId)throws Exception{
		return taskComponent.queryTaskByCustId(custId);
	}

	public List<CDoneCodeInfo> queryDocByCustId(String custId) throws Exception {
		return printComponent.queryDocByCustId(custId);
	}
	
	public List<CDoc> queryBusiConfirmDocByCustId(String custId) throws Exception {
		return printComponent.queryBusiConfirmDocByCustId(custId);
	}
	
	/**
	 * 
	 * @param custId
	 * @param doneCodes	业务doneCode
	 * @throws Exception
	 */
	public void saveDoc(String custId,String docSn, String[] doneCodes) throws Exception {
		Integer doneCode = doneCodeComponent.gDoneCode();//打印doneCode
//		String docSn = printComponent.gDocSn();
		printComponent.saveServiceDoc(custId,doneCode, docSn);//保存打印doneCode和打印项数据
		printComponent.saveDocItem(docSn, doneCodes);//保存业务doneCode和打印项数据
		
		saveAllPublic(doneCode,getBusiParam());
	}
	
	public void saveChangeInvoice(List<FeeDto> fees ) throws Exception {
		Integer doneCode = doneCodeComponent.gDoneCode();
		
		if(null != fees && fees.size() == 1 && StringHelper.isEmpty(fees.get(0).getFee_sn())){
			FeeDto fee = fees.get(0);
			//修改收费记录中的发票号
			feeComponent.changeFeeInvoice(fee.getNew_invoice_code(), fee.getNew_invoice_book_id(), fee.getNew_invoice_id(),
					fee.getInvoice_code(), fee.getInvoice_id());
		}else{
			feeComponent.changeFeeInvoice(fees);
			for(FeeDto feeDto : fees){
				invoiceComponent.saveInvoicePropChange(doneCode, feeDto
						.getInvoice_id(), feeDto.getInvoice_code(), feeDto
						.getNew_invoice_id(), feeDto.getNew_invoice_code());
			}
		}
		invoiceComponent.useInvoice(fees);
		invoiceComponent.cancelUseInvoice(fees);
		
		saveAllPublic(doneCode,getBusiParam());
	}

	public void saveChangeInvoice(CInvoiceDto oldInvoice,CInvoiceDto newInvoice,String docSn) throws Exception {
		Integer doneCode = doneCodeComponent.gDoneCode();
		
		//修改发票打印记录对应的发票号
		printComponent.changeInvoice(oldInvoice,newInvoice,docSn);
		
		//修改收费记录中的发票号
		feeComponent.changeFeeInvoice(newInvoice.getInvoice_code(), newInvoice.getInvoice_book_id(), newInvoice.getInvoice_id(),
				oldInvoice.getInvoice_code(), oldInvoice.getInvoice_id());
		
		//根据发票号查找原发票信息
		RInvoice invoice = invoiceComponent.queryById(oldInvoice.getInvoice_id(),oldInvoice.getInvoice_code());
		//修改新发票状态为使用状态、金额、出票方式为原发票对应的信息
		invoiceComponent.useInvoice(newInvoice.getInvoice_code(), newInvoice.getInvoice_id(), invoice.getInvoice_mode(),invoice.getAmount());
		//修改原发票的状态，金额为0，出 票方式为空
		invoiceComponent.cancelUseInvoice(oldInvoice);
		
		invoiceComponent.saveInvoicePropChange(doneCode, oldInvoice
				.getInvoice_id(), oldInvoice.getInvoice_code(), newInvoice
				.getInvoice_id(), newInvoice.getInvoice_code());
		
//		if(!oldInvoice.getDoc_type().equals(newInvoice.getDoc_type())){
//			invoiceComponent.updateDocType(docSn, newInvoice.getDoc_type());
//		}
		this.getBusiParam().setOperateObj(oldInvoice.getInvoice_id()+"=>"+newInvoice.getInvoice_id());
		saveAllPublic(doneCode,getBusiParam());
	}
	
	public void saveManuallyEditMInvoice(CInvoiceDto oldInvoice,CInvoiceDto newInvoice,String fee_sn) throws Exception {
		Integer doneCode = doneCodeComponent.gDoneCode();
		
		//根据发票号查找原发票信息
		if(StringHelper.isEmpty(newInvoice.getInvoice_id() ) || StringHelper.isEmpty(newInvoice.getInvoice_code() ) ){
			throw new ServicesException("传入的新发票号码或者代码为空!");
		}
		RInvoice oldRin = null;
		if(StringHelper.isNotEmpty(oldInvoice.getInvoice_id()) && StringHelper.isNotEmpty(oldInvoice.getInvoice_code())){
			oldRin = invoiceComponent.queryById(oldInvoice.getInvoice_id(), oldInvoice.getInvoice_code());
		}
		RInvoice newRin = invoiceComponent.queryById(newInvoice.getInvoice_id(), newInvoice.getInvoice_code());
		boolean isNewInvoice = false;
		//如果不存在,直接入库
		if(newRin == null ){
			newRin = new RInvoice();
			newRin.setInvoice_id(newInvoice.getInvoice_id());
			newRin.setInvoice_book_id(newInvoice.getInvoice_code());
			newRin.setInvoice_code(newInvoice.getInvoice_code());
			newRin.setInvoice_type("2");
			newRin.setDepot_id(getOptr().getDept_id());
			newRin.setStatus(SystemConstants.INVOICE_STATUS_USE);
			newRin.setAmount(0);
			newRin.setInvoice_mode("M");
			newRin.setFinance_status(SystemConstants.INVOICE_STATUS_IDLE);
			newRin.setCreate_time(new Date());
			newRin.setInvoice_amount(0);
			newRin.setIs_loss(SystemConstants.BOOLEAN_FALSE);
			isNewInvoice = true;
		}
		invoiceComponent.saveManuallyEditMInvoice(newRin,oldRin,fee_sn,isNewInvoice);
		saveAllPublic(doneCode,getBusiParam());
	}
	
	public Map<String, ?> queryPrintContent(String custId, CDoc doc, String suffix, String invoiceId, String invoiceCode)throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();

		//查询单据的配置
		TBusiDocTemplatefile busiDoc = busiConfigComponent.queryBusiDoc(doc.getDoc_type());
		map.put("printType", busiDoc.getPrint_type());
		map.put("docType", busiDoc.getDoc_type());
		//map.put("data", {});
		if(busiDoc.getPrint_type().equals("NOPRINT")&&StringHelper.isNotEmpty(busiDoc.getChange_doc_type())){
			map.put("docType", busiDoc.getChange_doc_type());
		}

			//获取数据,包括单据的模板内容及模板对应的数据
			String content = PrintContentConfiguration.getTemplate(suffix + busiDoc.getTemplate_filename());
			
			int balance = 0;//现金余额
			List<InvoiceFromDto> list = feeComponent.queryInvoiceByDocSn(doc.getDoc_sn(), invoiceId, invoiceCode);
			if(list != null && list.size() > 0){
				InvoiceFromDto ifdto = list.get(0);
				balance = ifdto.getBalance();
				if(balance == 0 ){
					balance = acctComponent.queryXJBalanceByCustId(custId);
				}
			}else{
				balance = acctComponent.queryXJBalanceByCustId(custId);
			}
			
			Map<String, Object> data = new HashMap<String, Object>();
	
			data.put("doc", doc);
			data.put("custid", custId);
			data.put("balance", balance);
			data.put("invoiceId", invoiceId);
			data.put("invoiceCode", invoiceCode);
			printComponent.fillData(data,busiDoc.getMethod_name());
	
			//设置数据，返回
			map.put("content", content);
			map.put("data", data);
		
		
		return map;
	}
	
	public List<PrintItemDto> queryPrintItemByDoc(String docSn, String custType, String invoiceId, String invoiceCode) throws Exception{
		return feeComponent.queryPrintitemBySn(docSn,custType, invoiceId, invoiceCode);
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.IDocService#queryPrintItemByDoneCode(java.lang.String)
	 */
	public List<PrintItemDto> queryPrintItemByDoneCode(String doneCode)
			throws Exception {
		String genInvioceManual = printComponent.queryTemplateConfig(TemplateConfigDto.Config.GEN_INVOICE_MANUAL.toString());
		if (SystemConstants.BOOLEAN_FALSE.equals(genInvioceManual)) {
			return printComponent.queryUnPrintItemByDoneCode(doneCode);
		} else {
			return null;
		}
	}

	/**
	 * 根据打印编号查询
	 * @param printitemId
	 * @return
	 * @throws Exception
	 */
	public List<TPrintitem> queryPrintItemById(String printitemId) throws Exception{
		List<TPrintitem> printitemList = new ArrayList<TPrintitem>();
		TPrintitem printitem = printComponent.queryPrintItemById(printitemId);
		if(null != printitem){
			printitemList.add(printitem);
		}
		
		return printitemList;
	}
	
	/**
	 * 修改打印项
	 * @param printitem
	 * @throws Exception
	 */
	public void editPrintitem(String printitemId,String printitemMame) throws Exception{
		TPrintitem printitem = new TPrintitem();
		printitem.setPrintitem_id(printitemId);
		printitem.setPrintitem_name(printitemMame);
		printComponent.editPrintitem(printitem);
	}
	
	public Map<String, ?> queryConfigPrintContent(String custId,String[] doneCode)throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		
		//查询单据的配置
		TBusiDocTemplatefile busiDoc = busiConfigComponent.queryBusiDoc(SystemConstants.DOC_TYPE_CONFIG);
		
		//获取数据,包括单据的模板内容及模板对应的数据
		String content = PrintContentConfiguration.getTemplate(busiDoc.getTemplate_filename());
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("custid", custId);
		data.put("doneCodes", doneCode);
		printComponent.fillData(data,busiDoc.getMethod_name());
		
		//设置数据，返回
		map.put("content", content);
		map.put("data", data);
		return map;
	}

	public Map<String, ?> queryPrintContent(String custId, String[] doneCode,CDoc doc) throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();

		//查询单据的配置
		TBusiDocTemplatefile busiDoc = busiConfigComponent.queryBusiDoc(doc.getDoc_type());

		//获取数据,包括单据的模板内容及模板对应的数据
		String content = PrintContentConfiguration.getTemplate(busiDoc.getTemplate_filename());

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("doc", doc);
		data.put("custid", custId);
		data.put("doneCodes", doneCode);
		printComponent.fillData(data,busiDoc.getMethod_name());

		//设置数据，返回
		map.put("content", content);
		map.put("data", data);
		return map;
	}

	public void savePrintItem(List<MergeFeeFormDto> lst) throws Exception{
		Integer doneCode = doneCodeComponent.gDoneCode();
		printComponent.saveDoc( lst, getBusiParam().getCust().getCust_id(), doneCode,getBusiParam().getBusiCode());
		saveAllPublic(doneCode,getBusiParam());
	}

	public List<CDoneCodeInfo> queryPrintConfig(String custId,String[] doneCodes)throws Exception{
		return printComponent.queryPrintConfig(custId,doneCodes);
	}
	
	public List<DocDto> queryUnPrintInvoice(String custId) throws Exception {
		Integer doneCode = doneCodeComponent.gDoneCode();
		BusiParameter param = getBusiParam();
		param.getCust().setCust_id(custId);
		printComponent.saveDoc(feeComponent.queryAutoMergeFees(custId), custId, doneCode,BusiCodeConstants.PRINT);
		String genInvioceManual = printComponent.queryTemplateConfig(TemplateConfigDto.Config.GEN_INVOICE_MANUAL.toString());
		if (SystemConstants.BOOLEAN_TRUE.equals(genInvioceManual)) {
			return printComponent.queryInvoiceType(doneCode,custId);
		} else{
			return printComponent.queryDocInvoice(doneCode);
		}
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.IDocService#saveDocItemManual(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public String saveDocItemManual(String docType, String doneCode,
			String custId, String[] docItems) throws Exception {
		//删除doneCode对应的doc
//		this.printComponent.deleteDoc(doneCode);
		//生成新的doc
		String docSn = this.printComponent.saveDoc(Integer.parseInt(doneCode),BusiCodeConstants.PRINT, custId, new String[]{docType});
		//更新items对应的doc
		printComponent.updateDocItem(docItems, docSn);
		return docSn;
	}

	public List<DocDto> queryYHZZPrintInvoice(String custId) throws Exception {
		Integer doneCode = doneCodeComponent.gDoneCode();
		BusiParameter param = getBusiParam();
		param.getCust().setCust_id(custId);
		
		printComponent.saveDoc(feeComponent.queryYHZZAutoMergeFees(custId), custId, doneCode,BusiCodeConstants.PRINT);
		return printComponent.queryDocInvoice(doneCode);
	}
	
	public List<DocDto> queryUnitUnPrintInvoice(String unitCustId)
		throws Exception {
		Integer doneCode = doneCodeComponent.gDoneCode();
		BusiParameter param = getBusiParam();
		param.getCust().setCust_id(unitCustId);
		
		printComponent.saveDoc(feeComponent.queryAutoMergeUnitFees(unitCustId), unitCustId, doneCode,BusiCodeConstants.PRINT);
		List<DocDto> docList = null;
		String genInvioceManual = printComponent.queryTemplateConfig(TemplateConfigDto.Config.GEN_INVOICE_MANUAL.toString());
		if (SystemConstants.BOOLEAN_TRUE.equals(genInvioceManual)) {
			docList = printComponent.queryInvoiceType(doneCode,unitCustId);
		} else{
			docList = printComponent.queryDocInvoice(doneCode);
		}
		return docList;
	}
	
	
	public void editInvoiceStatus(String invoiceId,String invoiceCode,String newStatus) throws Exception{
		invoiceComponent.editInvoiceStatus(invoiceId,invoiceCode,newStatus);
	}
	
	/**
	 * 根据发票id查询发票详细信息
	 * @param invoiceId
	 * @return
	 * @throws Exception
	 */
	public InvoiceDto queryInvoiceByInvoiceId(String invoiceId,String invoiceCode) 
			throws Exception{
			return invoiceComponent.queryInvoiceByInvoiceId(invoiceId,invoiceCode);
		}
	
	public List<RInvoice> queryInvoiceById(String invoiceId) throws Exception{
		return invoiceComponent.queryInvoiceById(invoiceId);
	}

	public boolean saveInvoice(String invoiceId, String invoiceCode,
			List<InvoiceFromDto> invoices) throws Exception {
		Integer doneCode = doneCodeComponent.gDoneCode();
		int balance = acctComponent.queryXJBalanceByCustId(getBusiParam().getCust().getCust_id());
		boolean oldFlag = false;
		String docSn = invoices.get(0).getDoc_sn();
		
		
		List<String> invoiceIdList = CollectionHelper.converValueToList(invoices, "invoice_id");
		if(invoiceIdList.contains(invoiceId)){
			oldFlag = true;
		}
		if(BusiCodeConstants.PRINT.equals(getBusiParam().getBusiCode())){
			//验证发票是否可用
			for (InvoiceFromDto i : invoices) {
				invoiceComponent.checkInvoice(i.getInvoice_id(), i.getDoc_type(), SystemConstants.INVOICE_MODE_AUTO);
			}
		}
		//验证能不能跳收据号
		boolean flay = invoiceComponent.checkInvoiceOptr(invoiceIdList,getOptr().getOptr_id());
		
		
		if(!oldFlag){
			for (InvoiceFromDto i : invoices) {
				printComponent.saveInvoice(doneCode, i, balance);
				invoiceComponent.updateInvoiceInfo(i.getInvoice_code(), i .getInvoice_id(), SystemConstants.INVOICE_MODE_AUTO, i .getAmount());
				
				//重载操作员未打印的费用
				//List<String> feeSnList = feeComponent.queryUnPrintFeeByOptr(optrId);
				//MemoryPrintData.reloadOptrFee(optrId, feeSnList);
				
			}
		}
		
		
		//有原发票 则取消
		if (!oldFlag && StringHelper.isNotEmpty(invoiceId)
				&& StringHelper.isNotEmpty(invoiceCode)) {
			//多张发票重打
			if(null != invoices && invoices.size() > 1){
				List<InvoiceFromDto> invoiceList = feeComponent.queryOldInvoiceByDocSn(doneCode, docSn);
				for(InvoiceFromDto dto : invoiceList){
					invoiceComponent.invalidInvoice(doneCode, dto.getInvoice_id(), dto.getInvoice_code());
				}
			}else{
				invoiceComponent.invalidInvoice(doneCode, invoiceId, invoiceCode);
			}
			
		}
		if(StringHelper.isNotEmpty(invoiceId)){
			this.getBusiParam().setOperateObj(invoiceId);
		}
		if(invoiceIdList!=null&&invoiceIdList.size()>0){
			String _ids="";
			for(String _id: invoiceIdList ){
				
				_ids+=" "+_id;
			}
			this.getBusiParam().setOperateObj(_ids);
		}
		saveAllPublic(doneCode,getBusiParam());
		return flay;
	}
	
	public CInvoiceDto queryReprintInvoice(String invoiceId, String invoiceCode) throws Exception {
		return printComponent.queryReprintInvoice(invoiceId,invoiceCode);
	}

	public List<DocDto> queryUnPrintUnitPre(String feeSn) throws Exception {
		Integer doneCode = doneCodeComponent.gDoneCode();
		printComponent.saveDoc(feeComponent.queryAutoMergeFees(new String[]{feeSn}), null, doneCode,BusiCodeConstants.PRINT);
		String genInvioceManual = printComponent.queryTemplateConfig(TemplateConfigDto.Config.GEN_INVOICE_MANUAL.toString());
		if (SystemConstants.BOOLEAN_TRUE.equals(genInvioceManual)) {
			return printComponent.queryInvoiceType(doneCode,"");
		} else{
			return printComponent.queryUnPrintUnitPre(feeSn);
		}
		
	}
	
	public List<DocDto> queryFeeSnAll(String[] feeSn) throws Exception {
		Integer doneCode = doneCodeComponent.gDoneCode();
		printComponent.saveDoc(feeComponent.queryAutoMergeFees(feeSn), null, doneCode,BusiCodeConstants.PRINT);
		return printComponent.queryDocInvoice(doneCode);
	}
	
	public void saveConfigPrint(String[] doneCode) throws Exception {
		printComponent.saveConfigPrint(doneCode);
	}
	
	/**
	 * 查询业务单据
	 * @param custId
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> queryServiceDoc(String custId) throws Exception {
		BusiDocPrintConfigDto cfg = printComponent.queryServiceDocFile();
		List<BusiDocPrintItemDto> itemsPrint = new ArrayList<BusiDocPrintItemDto>();
		String split=",";
		String doneCodes = "";
		//获得打印业务汇总
		List<BusiDocPrintItemDto> itemBusi = printComponent.queryServiceSubDocBusiCode(custId,"");
		List<BusiDocPrintItemDto> itemBusiF = new ArrayList<BusiDocPrintItemDto>();
		BusiDocPrintItemDto pitem = null;
		for(int i=0;i<itemBusi.size();i++){
			BusiDocPrintItemDto bp = itemBusi.get(i);
			if(StringHelper.isNotEmpty(bp.getCondition()) && "T".equals(bp.getBusi_code_span())){
				//拆分条件
				String[] condtionList = bp.getCondition().split(";");
				bp.setCondition(condtionList[0]);
				for(int j=0;j<condtionList.length-1;j++){
					pitem = new BusiDocPrintItemDto();
					BeanUtils.copyProperties(pitem,bp);
					bp.setCondition(condtionList[j+1]);
					pitem.setIndex(i+j+1);
					itemBusiF.add(pitem);
				}
				
			}  
		};
		for(BusiDocPrintItemDto bp : itemBusiF){
			itemBusi.add(bp.getIndex(), bp);
		}
		List<String> doneCodeList = new ArrayList<String>();
		for(BusiDocPrintItemDto bp : itemBusi){
			//如果不进行合并
			if("F".equals(bp.getBusi_code_span())){
				List<BusiDocPrintItemDto> items = printComponent.queryServiceSubDocFile(bp.getBusi_code(),custId,bp.getUser_id(),"","");
				
				for(BusiDocPrintItemDto item : items){
					if(doneCodeList.contains(item.getDone_code())){
						continue;
					}
					doneCodeList.add(item.getDone_code());
					item.setInfo(item.getInfo()+item.getInfo1()+item.getInfo2()+item.getInfo3()+item.getInfo4()+item.getInfo5()+item.getInfo6()+item.getInfo7()+item.getInfo8()+item.getInfo9()+item.getInfo10());
					itemsPrint.add(item);
					doneCodes += item.getDone_code() + split;
				}
			}
			//进行合并
			if("T".equals(bp.getBusi_code_span())){
				 List<BusiDocPrintItemDto> items= new ArrayList<BusiDocPrintItemDto>();
				 //按用户分组无条件
				 if("USER".equals(bp.getGroup_column())&& StringHelper.isEmpty(bp.getCondition())){
					items = printComponent.queryServiceSubDocFile(bp.getBusi_code(),custId,bp.getUser_id(),"","");
				 }else if("USER".equals(bp.getGroup_column())){//按用户分组有条件
					items = printComponent.queryServiceSubDocFile(bp.getBusi_code(),custId,bp.getUser_id(),bp.getCondition(),"");
				 }
				 //按客户分组无条件
				 if("CUST".equals(bp.getGroup_column())&& StringHelper.isEmpty(bp.getCondition())){
					items = printComponent.queryServiceSubDocFile(bp.getBusi_code(),custId,"","","");
				 }else if("CUST".equals(bp.getGroup_column())){ //按客户分组有条件
					 items = printComponent.queryServiceSubDocFile(bp.getBusi_code(),custId,"",bp.getCondition(),"");
				 }
				 //打印公共部分
				BusiDocPrintItemDto itemHeader= new BusiDocPrintItemDto();
				//合并打印项
				String info="[";
				int busi_count=0;
 				for(BusiDocPrintItemDto item : items){
 					if(doneCodeList.contains(item.getDone_code())){
						continue;
					}
					doneCodeList.add(item.getDone_code());
					item.setInfo(item.getInfo()+item.getInfo1()+item.getInfo2()+item.getInfo3()+item.getInfo4()+item.getInfo5()+item.getInfo6()+item.getInfo7()+item.getInfo8()+item.getInfo9()+item.getInfo10());
					info+=item.getInfo()+",";
					doneCodes += item.getDone_code()+",";
					busi_count++;
				}
				if(null!=items && items.size()>0){
					info = info.substring(0,info.length()-1);
					info+="]";
					//组装打印信息
					BeanUtils.copyProperties(itemHeader,items.get(0));
					String headInfo = itemHeader.getInfo();
					if(StringHelper.isNotEmpty(headInfo)){
						headInfo = headInfo.substring(0,headInfo.length()-1);
					}
					info = headInfo+split+getInfo("busi_count",busi_count+"")+split+"\"items\":"+info+"}";
					itemHeader.setInfo(info);
					itemsPrint.add(itemHeader);
				}
			}
		}
 
		
		
		
		
		
		//get tpl
		cfg.setTpl(getTemplateFile(cfg.getTemplate_filename()));
//		BusiDocPrintItemDto orderProdItem = null;
//		String orderProdInfo = "[";
		for (BusiDocPrintItemDto item : itemsPrint) {
			if(item.getBusi_code().equals("1015")){
//				orderProdItem = item;
//				orderProdInfo +=item.getInfo();
			}
			item.setProtocol_desc(printComponent.queryProtocolInfo(item.getDone_code()));
			item.setTpl(getTemplateFile(item.getTemplate_filename()));
		}
//		if(null!=orderProdItem){
//			orderProdItem.setInfo(orderProdInfo);
//		}
		
		//付费信息
		List<PrintFeeitemDto> fees = printComponent.queryFeeItems(custId,"");
		//总金额
		Integer total = 0;
		
		List<Map<String, Object>> feeList = new ArrayList<Map<String, Object>>();
		Map<String, List<PrintFeeitemDto>> mapByDevAndActItem = new HashMap<String, List<PrintFeeitemDto>>();
		
		for (PrintFeeitemDto dto : fees) {
			String stb_id = dto.getStb_id();
			stb_id = StringHelper.isEmpty(stb_id)?"":stb_id;
			String card_id = dto.getCard_id();
			card_id = StringHelper.isEmpty(card_id)?"":card_id;
			String modem_mac = dto.getModem_mac();
			modem_mac = StringHelper.isEmpty(modem_mac)?"":modem_mac;
			String acctitem_id = dto.getAcctitem_id();
			
			String key = "s_"+stb_id + "c_" + card_id + "m_" + modem_mac +"item_" + acctitem_id ;
			List<PrintFeeitemDto> list = mapByDevAndActItem.get(key);
			if(list==null){
				list = new ArrayList<PrintFeeitemDto>();
			}
			list.add(dto);
			mapByDevAndActItem.put(key, list);
			String doneCode = dto.getDone_code().toString();
			if(!doneCodeList.contains(doneCode)){
				doneCodeList.add(doneCode);
				doneCodes += doneCode+",";
			}
			total += dto.getReal_pay();
		}
		
		for(String key : mapByDevAndActItem.keySet()){
			List<PrintFeeitemDto> list = mapByDevAndActItem.get(key);
			if(CollectionHelper.isEmpty(list)){
				continue;
			}
			PrintFeeitemDto dto = list.get(0);
			String invalidDateStr = DateHelper.format(dto.getInvalid_date(), DateHelper.FORMAT_YMD);
			Map<String, Object> eachItem = new HashMap<String, Object>();
			eachItem.put("invalid_date", invalidDateStr);
			eachItem.put("list", list);
			feeList.add(eachItem);
		}
		
		
		//设置参数
		Map<String, Object> context = new HashMap<String, Object>();
		context.put("print", cfg);
		context.put("items", itemsPrint);
//		context.put("fees", fees);//fees
		context.put("fees", feeList);//fees
		context.put("feeTotal", total );
		context.put("today", new SimpleDateFormat("yyyy年MM月dd日 HH:mm").format(new Date()));
		context.put("loginName", this.getBusiParam().getOptr().getLogin_name());
		context.put("optrDeptName", this.getBusiParam().getOptr().getDept_name());
		String docSn = printComponent.gDocSn();
		context.put("docSn", docSn);
		context.put("id", DateHelper.format(new Date(),DateHelper.FORMAT_YMD_STR)+docSn);
 		if(doneCodes.length()>1)
			context.put("doneCodes", doneCodes.substring(0,doneCodes.length()-1));
		return context;
	}
	
	/**
	 * 查询 重打业务单据
	 * @param custId
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> queryServiceRepeatDoc(String custId,String docSn) throws Exception {
		BusiDocPrintConfigDto cfg = printComponent.queryServiceDocFile();
		CDoc doc = printComponent.queryDocByDocSn(docSn).get(0);
		List<BusiDocPrintItemDto> itemsPrint = new ArrayList<BusiDocPrintItemDto>();
		String split=",";
		//获得打印业务汇总
		List<BusiDocPrintItemDto> itemBusi = printComponent.queryServiceSubDocBusiCode(custId,docSn);
		List<BusiDocPrintItemDto> itemBusiF = new ArrayList<BusiDocPrintItemDto>();
		BusiDocPrintItemDto pitem = null;
		for(int i=0;i<itemBusi.size();i++){
			BusiDocPrintItemDto bp = itemBusi.get(i);
			if(StringHelper.isNotEmpty(bp.getCondition()) && "T".equals(bp.getBusi_code_span())){
				//拆分条件
				String[] condtionList = bp.getCondition().split(";");
				bp.setCondition(condtionList[0]);
				for(int j=0;j<condtionList.length-1;j++){
					pitem = new BusiDocPrintItemDto();
					BeanUtils.copyProperties(bp, pitem);
					bp.setCondition(condtionList[j+1]);
					pitem.setIndex(i+j+1);
					itemBusiF.add(pitem);
				}
				
			}  
		};
		List<String> doneCodeList = new ArrayList<String>();
		for(BusiDocPrintItemDto bp : itemBusiF){
			itemBusi.add(bp.getIndex(), bp);
		}
		
		for(BusiDocPrintItemDto bp : itemBusi){
			//如果不进行合并
			if("F".equals(bp.getBusi_code_span())){
				List<BusiDocPrintItemDto> items = printComponent.queryServiceSubDocFile(bp.getBusi_code(),custId,bp.getUser_id(),"",docSn);
				for(BusiDocPrintItemDto item : items){
					if(doneCodeList.contains(item.getDone_code())){
						continue;
					}
					doneCodeList.add(item.getDone_code());
					item.setInfo(item.getInfo()+item.getInfo1()+item.getInfo2()+item.getInfo3()+item.getInfo4()+item.getInfo5()+item.getInfo6()+item.getInfo7()+item.getInfo8()+item.getInfo9()+item.getInfo10());
					itemsPrint.add(item);
				}
			}
			//进行合并
			else if("T".equals(bp.getBusi_code_span())){
				 List<BusiDocPrintItemDto> items=null;
				 //按用户分组无条件
				 if("USER".equals(bp.getGroup_column())&& StringHelper.isEmpty(bp.getCondition())){
					items = printComponent.queryServiceSubDocFile(bp.getBusi_code(),custId,bp.getUser_id(),"",docSn);
				 }else if("USER".equals(bp.getGroup_column())){//按用户分组有条件
					items = printComponent.queryServiceSubDocFile(bp.getBusi_code(),custId,bp.getUser_id(),bp.getCondition(),docSn);
				 }
				 //按客户分组无条件
				 if("CUST".equals(bp.getGroup_column())&& StringHelper.isEmpty(bp.getCondition())){
					items = printComponent.queryServiceSubDocFile(bp.getBusi_code(),custId,"","",docSn);
				 }else if("CUST".equals(bp.getGroup_column())){ //按客户分组有条件
					 items = printComponent.queryServiceSubDocFile(bp.getBusi_code(),custId,"",bp.getCondition(),docSn);
				 }
				 //打印公共部分
				BusiDocPrintItemDto itemHeader= new BusiDocPrintItemDto();
				//合并打印项
				String info="[";
				int busi_count=0;
				for(BusiDocPrintItemDto item : items){
					if(doneCodeList.contains(item.getDone_code())){
						continue;
					}
					doneCodeList.add(item.getDone_code());
					item.setInfo(item.getInfo()+item.getInfo1()+item.getInfo2()+item.getInfo3()+item.getInfo4()+item.getInfo5()+item.getInfo6()+item.getInfo7()+item.getInfo8()+item.getInfo9()+item.getInfo10());
					info+=item.getInfo()+",";
					busi_count++;
				}
				if(null!=items && items.size()>0){
					info = info.substring(0,info.length()-1);
					info+="]";
					//组装打印信息
					BeanUtils.copyProperties( itemHeader,items.get(0));
					String headInfo = itemHeader.getInfo();
					if(StringHelper.isNotEmpty(headInfo) &&headInfo.length()>0){
						headInfo = headInfo.substring(0,headInfo.length()-1);						
					}else{
						headInfo = "";
					}
					info = headInfo+split+getInfo("busi_count",busi_count+"")+split+"\"items\":"+info+"}";
					itemHeader.setInfo(info);
					itemsPrint.add(itemHeader);
				}
			}
		}
 
		
		
		
		
		
		//get tpl
		cfg.setTpl(getTemplateFile(cfg.getTemplate_filename()));
//		BusiDocPrintItemDto orderProdItem = null;
//		String orderProdInfo = "[";
		for (BusiDocPrintItemDto item : itemsPrint) {
			if("1015".equals(item.getBusi_code())){
//				orderProdItem = item;
//				orderProdInfo +=item.getInfo();
			}
			item.setProtocol_desc(printComponent.queryProtocolInfo(item.getDone_code()));
			item.setTpl(getTemplateFile(item.getTemplate_filename()));
		}
//		if(null!=orderProdItem){
//			orderProdItem.setInfo(orderProdInfo);
//		}
		
		//付费信息
		List<PrintFeeitemDto> fees = printComponent.queryFeeItems(custId,docSn);
		//总金额
		Integer total = 0;
		List<Map<String, Object>> feeList = new ArrayList<Map<String, Object>>();
		Map<String, List<PrintFeeitemDto>> mapByDevAndActItem = new HashMap<String, List<PrintFeeitemDto>>();
		
		for (PrintFeeitemDto dto : fees) {
			String stb_id = dto.getStb_id();
			stb_id = StringHelper.isEmpty(stb_id)?"":stb_id;
			String card_id = dto.getCard_id();
			card_id = StringHelper.isEmpty(card_id)?"":card_id;
			String modem_mac = dto.getModem_mac();
			modem_mac = StringHelper.isEmpty(modem_mac)?"":modem_mac;
			String acctitem_id = dto.getAcctitem_id();
			
			String key = "s_"+stb_id + "c_" + card_id + "m_" + modem_mac +"item_" + acctitem_id ;
			List<PrintFeeitemDto> list = mapByDevAndActItem.get(key);
			if(list==null){
				list = new ArrayList<PrintFeeitemDto>();
			}
			list.add(dto);
			mapByDevAndActItem.put(key, list);
			total += dto.getReal_pay();
		}
		
		for(String key : mapByDevAndActItem.keySet()){
			List<PrintFeeitemDto> list = mapByDevAndActItem.get(key);
			if(CollectionHelper.isEmpty(list)){
				continue;
			}
			PrintFeeitemDto dto = list.get(0);
			String invalidDateStr = DateHelper.format(dto.getInvalid_date(), DateHelper.FORMAT_YMD);
			Map<String, Object> eachItem = new HashMap<String, Object>();
			eachItem.put("invalid_date", invalidDateStr);
			eachItem.put("list", list);
			feeList.add(eachItem);
		}
		
		
		//设置参数
		Map<String, Object> context = new HashMap<String, Object>();
		context.put("print", cfg);
		context.put("items", itemsPrint);
		context.put("fees", feeList);
		context.put("feeTotal", total );
		context.put("today", new SimpleDateFormat("yyyy年MM月dd日  HH:mm").format(doc.getCreate_time()));
		SOptr optr = userComponent.queryOptrById(doc.getOptr_id());
		context.put("loginName", optr.getLogin_name());
		context.put("optrDeptName", getOptr().getDept_name());
		context.put("id", DateHelper.format(doc.getCreate_time(),DateHelper.FORMAT_YMD)+docSn);
		
		return context;
	}
	
	private String getTemplateFile(String fileName)throws Exception{
		String context = PrintContentConfiguration.getTemplate(fileName);
		if(context == null){
			throw new IllegalArgumentException("[" + fileName + "]打印文件在内存中不存在!");
		}
		return context;
	}
	
	public String getInfo(String name , String value){
		return "\""+name+"\""+":"+"\""+value+"\"";
	}
	
	/**
	 * @param busiConfigComponent the busiConfigComponent to set
	 */
	public void setBusiConfigComponent(BusiConfigComponent busiConfigComponent) {
		this.busiConfigComponent = busiConfigComponent;
	}

	/**
	 * @param printComponent the printComponent to set
	 */
	public void setPrintComponent(PrintComponent printComponent) {
		this.printComponent = printComponent;
	}
}
