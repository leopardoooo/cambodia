package com.ycsoft.business.component.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TBusiDocTemplatefile;
import com.ycsoft.beans.config.TInvoicePrintitem;
import com.ycsoft.beans.config.TPrintitem;
import com.ycsoft.beans.core.common.CDoneCodeInfo;
import com.ycsoft.beans.core.cust.CCust;
import com.ycsoft.beans.core.cust.CCustLinkman;
import com.ycsoft.beans.core.print.CDoc;
import com.ycsoft.beans.core.print.CDocFee;
import com.ycsoft.beans.core.print.CDocItem;
import com.ycsoft.beans.core.print.CInvoice;
import com.ycsoft.beans.core.print.CInvoiceItem;
import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.business.commons.abstracts.BaseBusiComponent;
import com.ycsoft.business.dao.config.TBusiDocTemplatefileDao;
import com.ycsoft.business.dao.config.TInvoicePrintitemDao;
import com.ycsoft.business.dao.config.TPrintitemDao;
import com.ycsoft.business.dao.core.bill.BBillWriteoffDao;
import com.ycsoft.business.dao.core.common.CDoneCodeInfoDao;
import com.ycsoft.business.dao.core.cust.CCustDao;
import com.ycsoft.business.dao.core.cust.CCustLinkmanDao;
import com.ycsoft.business.dao.core.fee.CFeeDao;
import com.ycsoft.business.dao.core.print.CDocDao;
import com.ycsoft.business.dao.core.print.CDocFeeDao;
import com.ycsoft.business.dao.core.print.CDocItemDao;
import com.ycsoft.business.dao.core.print.CInvoiceDao;
import com.ycsoft.business.dao.core.print.CInvoiceItemDao;
import com.ycsoft.business.dao.core.user.CUserDao;
import com.ycsoft.business.dto.core.cust.CustFullInfoDto;
import com.ycsoft.business.dto.core.fee.MergeFeeFormDto;
import com.ycsoft.business.dto.core.print.CInvoiceDto;
import com.ycsoft.business.dto.core.print.DocDto;
import com.ycsoft.business.dto.core.print.InvoiceFromDto;
import com.ycsoft.business.dto.core.print.PrintDate;
import com.ycsoft.business.dto.core.print.PrintItemDto;
import com.ycsoft.business.dto.print.BBillWriteoffDto;
import com.ycsoft.business.dto.print.BusiDocPrintConfigDto;
import com.ycsoft.business.dto.print.BusiDocPrintItemDto;
import com.ycsoft.business.dto.print.PrintFeeitemDto;
import com.ycsoft.commons.constants.BusiCodeConstants;
import com.ycsoft.commons.constants.DataRight;
import com.ycsoft.commons.constants.SequenceConstants;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ComponentException;
import com.ycsoft.commons.helper.BeanHelper;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.commons.helper.DateHelper;
import com.ycsoft.commons.helper.LoggerHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.store.TemplateConfig;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;

/**
 * 打印组件
 * @author PANYB
 *
 */
@Component
public class PrintComponent extends BaseBusiComponent  {
	private DynamicDataComponent dynamicDataComponent;

	private CCustDao cCustDao;
	private CustComponent custComponent;
	private CCustLinkmanDao cCustLinkmanDao;
	private CDocDao cDocDao ;
	private CUserDao cUserDao;
	private BBillWriteoffDao bBillWriteoffDao;
	private CDocItemDao cDocItemDao ;
	private CDocFeeDao cDocFeeDao ;
	private TInvoicePrintitemDao tInvoicePrintitemDao;
	private TBusiDocTemplatefileDao tBusiDocTemplatefileDao;
	private CFeeDao cFeeDao;
	private CInvoiceDao cInvoiceDao;
	private CInvoiceItemDao cInvoiceItemDao;
	private CDoneCodeInfoDao cDoneCodeInfoDao;
	private TPrintitemDao tPrintitemDao;

	
	public void saveServiceDoc(String custId,Integer doneCode,String docSn) throws Exception {
		CDoc doc = new CDoc();
		doc.setCust_id(custId);
		doc.setBusi_code(BusiCodeConstants.SERVICE_PRINT);
		doc.setDone_code(doneCode);
		doc.setDoc_sn(docSn);
		doc.setDoc_type(SystemConstants.DOC_TYPE_SERVICE);
		doc.setCreate_time(new Date());
		setBaseInfo(doc);
		cDocDao.save(doc);
	}
	
	public void saveDocItem(String docSn,String[] doneCodes) throws Exception {
		List<CDocItem> docItemList = new ArrayList<CDocItem>();
		Date lastPrint = new Date();
		for(String doneCode : doneCodes){
			CDocItem docItem = new CDocItem();
			docItem.setDoc_sn(docSn);
			docItem.setDocitem_sn(doneCode);
			docItemList.add(docItem);
			updateLastPrintByDoneCode(Integer.parseInt(doneCode), lastPrint);
		}
		cDocItemDao.save(docItemList.toArray(new CDocItem[docItemList.size()]));
	}
	
	public void updateLastPrintByDoneCode(Integer doneCode,Date lastPrint) throws Exception {
		cDoneCodeInfoDao.updateLastPrintByDoneCode(doneCode, lastPrint);
	}
	
	public BusiDocPrintConfigDto queryServiceDocFile()throws Exception{
		String county_id = getOptr().getCounty_id();
		String template_type = TemplateConfig.Template.DOC.toString();
		List<BusiDocPrintConfigDto> lst = tBusiDocTemplatefileDao.getTemplate(county_id, template_type);
		if(lst == null || lst.size() == 0){
			throw new IllegalArgumentException("请配置模板数据为["+ county_id+","+ template_type +"]");
		}
		if(lst.size() > 1){
			throw new IllegalArgumentException("不允许配置多个模板数据["+ county_id+","+ template_type +"]");
		}
		return lst.get(0);
	}
	
	public List<BusiDocPrintItemDto> queryServiceSubDocBusiCode(String custId,String docSn)throws Exception{
		return cDoneCodeDao.queryDocPrintBusiCodeByCust(getOptr(), custId,docSn);
	}
	public List<BusiDocPrintItemDto> queryServiceSubDocFile(String busiCode,String custId,String userId,String condition,String docSn)throws Exception{
		return cDoneCodeDao.queryDocPrintContentByCust(busiCode,getOptr(), custId,userId,condition,docSn);
	}
	
	public List<PrintFeeitemDto> queryFeeItems(String custId,String docSn)throws Exception{
		List<PrintFeeitemDto> printFee = cFeeDao.queryPrintFee(custId, getOptr(),docSn);
		return printFee;
	}
	
	private List<BBillWriteoffDto>  queryWriteoffInfoByDonecode(Integer doneCode) throws JDBCException{
		return bBillWriteoffDao.queryWriteoffInfo(doneCode);
	}
	
	/**
	 * 查询客户已打印业务单记录
	 * @param custId
	 * @return
	 */
	public List<CDoc> queryDocByDocSn(String docSn) throws Exception {
		return cDoneCodeInfoDao.queryDocByDocSn(docSn);
	}
	
	/**
	 * 客户是否全部是模拟客户
	 * @param custId
	 * @return
	 */
	private boolean isAtvCust(String custId) throws JDBCException{
//		boolean result = true;
//		List<CUser> users = cUserDao.queryUserByCustId(custId);
//		for(CUser user:users){
//			if (!SystemConstants.USER_TYPE_ATV.equals(user.getUser_type()) && !SystemConstants.USER_TYPE_BAND.equals(user.getUser_type())){
//				result=false;
//				break;
//			}
//		}
		return false;
	}
	
	/**
	 * @param done_code
	 * @return
	 */
	public String queryProtocolInfo(String done_code)throws Exception {
		return cDoneCodeDao.queryProtocolInfo(done_code);
	}
	/**
	 * 生成一个新的序列值
	 */
	public String gDocSn()throws Exception{
		return cDocDao.findSequence( SequenceConstants.SEQ_DOC_SN ).toString();
	}

	/**
	 * 生成一个新的DocItem项的序列值
	 */
	private String gDocItemSn()throws Exception{
		return cDocDao.findSequence( SequenceConstants.SEQ_DOC_ITEM_SN ).toString();
	}

	/**
	 * 修改发票号
	 * @param oldInvoiceId
	 * @param oldInvoiceCode
	 * @param newInvoiceId
	 * @param newInvoiceCode
	 * @throws Exception
	 */
	public void changeInvoice(CInvoiceDto oldInvoice,CInvoiceDto newInvoice,String docSn) throws Exception {
		cInvoiceDao.changeInvoice(oldInvoice, newInvoice,docSn);
		cInvoiceDao.changeInvoiceItem(oldInvoice,newInvoice,docSn);
	}
	/**
	 * 保存业务单据(不包括费用，用于模板配置的单据统一保存)
	 * @param doneCode   业务流水
	 * @param docTypes   单据类型数组
	 * @throws Exception
	 */
	public String saveDoc(Integer doneCode,String busiCode,String custId,String[] docTypes) throws Exception{
		if (docTypes != null && docTypes.length>0){
			CDoc[] d = new CDoc[docTypes.length];
			for(int i=0;i< docTypes.length ;i++){
				d[i] = new CDoc();
				d[i].setDoc_sn(gDocSn());
				d[i].setDone_code(doneCode);
				d[i].setBusi_code(busiCode);
				d[i].setDoc_type(docTypes[i]);
				d[i].setCust_id(custId);
				d[i].setCreate_time(DateHelper.now());
				setBaseInfo(d[i]);
			}
			cDocDao.save(d);
			return d[0].getDoc_sn();
		}
		return "";
	}

	/**
	 * 保存打印记录信息
	 *
	 * @param data 要合并的项，及每项所对应的费用明细
	 * @param doneCode
	 * @throws Exception
	 */
	public void saveDoc(List<MergeFeeFormDto> data ,String custId, Integer doneCode,String busiCode) throws Exception{
		if(null == data || data.size() == 0) return ;
		Map<String,List<MergeFeeFormDto>> docs = CollectionHelper.converToMap(data,"doc_type");

		//定义需要封装的参数值
		List<CDoc> docList = new ArrayList<CDoc>();
		List<CDocItem> itemList = new ArrayList<CDocItem>();
		List<CDocFee> feeList = new ArrayList<CDocFee>();
		List<String> feeSn = new ArrayList<String>();

		CDoc tcd = null;
		CDocFee tcdf = null ;
		CDocItem tcdi = null ;

		//封装参数
		for (String docType : docs.keySet()) {
			tcd = new CDoc( gDocSn(),custId, doneCode ,busiCode,docType );
			setBaseInfo(tcd);
			List<MergeFeeFormDto> _tMff = docs.get( tcd.getDoc_type());
			for (MergeFeeFormDto m : _tMff) {
				tcdi = m.transform();
				tcdi.setDoc_sn(tcd.getDoc_sn() );
				tcdi.setDocitem_sn( gDocItemSn());

				for (String _sn : m.getFee_sns()) {
					tcdf = new CDocFee(tcd.getDoc_sn(),tcdi.getDocitem_sn() , _sn);
					feeList.add( tcdf );
					feeSn.add( _sn );
				}
				itemList.add( tcdi );
			}
			docList.add( tcd );
		}

		//批量保存数据
		cDocDao.save(docList.toArray(new CDoc[docList.size()]));
		cDocItemDao.save(itemList.toArray(new CDocItem[itemList.size()]));
		cDocFeeDao.save(feeList.toArray(new CDocFee[feeList.size()]));

		// 更新费用状态为以生产单据
	//	cFeeDao.updateDocStatus(feeSn);

	}

	/**
	 * 保存发票打印项数据
	 * @param data
	 * @throws JDBCException
	 */
	public void saveInvioce(List<InvoiceFromDto> data) throws Exception {
		if (null == data || data.size() == 0)
			return;

		List<CInvoice> invoices = new ArrayList<CInvoice>();
		List<CInvoiceItem> invoiceitems = new ArrayList<CInvoiceItem>();

		for (InvoiceFromDto invoice : data) {
			List<String> docSnItems = invoice.getDocSnItems();
			if (docSnItems != null && docSnItems.size() > 0) {
				CInvoice cInvoice = new CInvoice();
				cInvoice.setDoc_sn(invoice.getDoc_sn());
				cInvoice.setInvoice_code(invoice.getInvoice_code());
				cInvoice.setInvoice_id(invoice.getInvoice_id());
				cInvoice.setInvoice_book_id(invoice.getInvoice_book_id());
				cInvoice.setStatus(StatusConstants.ACTIVE);
				cInvoice.setAmount(invoice.getAmount());
				cInvoice.setDocitem_data(invoice.getDocitem_data());
				setBaseInfo(cInvoice);
				invoices.add(cInvoice);

				for (String docitemsn : docSnItems) {
					CInvoiceItem cInvoiceItem = new CInvoiceItem();
					cInvoiceItem.setDocitem_sn(docitemsn);
					cInvoiceItem.setInvoice_code(cInvoice.getInvoice_code());
					cInvoiceItem.setInvoice_id(cInvoice.getInvoice_id());
					invoiceitems.add(cInvoiceItem);
				}
			}
		}
		cInvoiceDao.save(invoices.toArray(new CInvoice[invoices.size()]));
		cInvoiceItemDao.save(invoiceitems.toArray(new CInvoiceItem[invoiceitems
				.size()]));
	}

	/**
	 * 通过data中的printitem_id获取<code>TInvoicePrintitem</code>配置记录项
	 */
	private List<TInvoicePrintitem> getPrintitem(List<MergeFeeFormDto> data)throws Exception {
		//查询对应的数据printitem_id and doc_type
		String[] ids = new String[data.size()];
		for (int i = 0; i < data.size(); i++) {
			ids[i] = data.get(i).getPrintitem_id();
		}
		return tInvoicePrintitemDao.queryPrintitem(getOptr().getCounty_id(), ids);
	}

	/**
	 * 将合并的费用项，按<code>printitem_id</code>对应的<code>doc_type</code>进行分类，
	 * 如果<code>printitem_id</code>对应的<code>doc_type</code>相等，则放入一个组里
	 */
	private Map<String  , List<MergeFeeFormDto>> groupFee123(List<MergeFeeFormDto> data)throws Exception{

		//封装对照表
		Map<String, TInvoicePrintitem> temp = CollectionHelper.converToMapSingle(getPrintitem(data), "printitem_id");

		//定义局部变量
		Map<String, List<MergeFeeFormDto>> target = new HashMap<String, List<MergeFeeFormDto>>();
		List<MergeFeeFormDto> tList = null;
		String docType = null ;

		for (MergeFeeFormDto mff : data) {
			//从temp参照表中获取doc_type,
			//并判断target是否存在该doc_type为key的元素
			docType = temp.get(mff.getPrintitem_id()).getDoc_type();
			tList = target.get(docType);
			if(null == tList){
				tList = new ArrayList<MergeFeeFormDto>();
				target.put( docType , tList);
			}
			tList.add( mff );
		}
		return target;
	}
	
	/**
	 * @param unPrintInvoice
	 */
	private void fillTemplateFile(List<DocDto> unPrintInvoice) throws Exception {
		Map<String, TBusiDocTemplatefile> templateFilemap = CollectionHelper
				.converToMapSingle(tBusiDocTemplatefileDao.queryAll(getOptr()
						.getCounty_id()), "doc_type");
		for (DocDto doc : unPrintInvoice) {
			TBusiDocTemplatefile templateFile = templateFilemap.get(doc
					.getDoc_type());
			if (templateFile == null)
				throw new ComponentException("找不到对应的打印配置文件");
			doc.setTemplate_filename(templateFile.getTemplate_filename());
			doc.setPrint_type(templateFile.getPrint_type());
		}
	}

	/**
	 * 查询预付费打印项
	 * @param feeSn
	 * @return
	 */
	public List<DocDto> queryUnPrintUnitPre(String feeSn) throws Exception {
		List<DocDto> docs = cDocDao.queryUnPrintUnitPre(feeSn);
		fillTemplateFile(docs);
		return docs;
	}
	
	/**
	 * 查询客户下最后一次操作未打印的发票
	 * @param custId
	 * @return
	 */
	public List<DocDto> queryLastUnPrintInvoice(String custId) throws Exception {
		List<DocDto> docs = cDocDao.queryLastUnPrintInvoice(custId);
		fillTemplateFile(docs);
		return docs;
	}
	
	/**
	 * 查询确认单
	 * @param custId
	 * @return
	 * @throws JDBCException
	 */
	public List<CDoneCodeInfo> queryPrintConfig(String custId, String[] doneCodes)
			throws Exception {
		List<CDoneCodeInfo> docs = null;
		if (doneCodes == null || doneCodes.length == 0)
			docs = cDoneCodeInfoDao.queryUnPrintConfig(getOptr().getCounty_id(),custId);
		else
			docs = cDoneCodeInfoDao.queryPrintConfig(doneCodes);
//		fillTemplateFile(docs);
		return docs;
	}

	/**
	 * 查询客户下的已支付单据
	 * @param custId
	 * @return
	 * @throws JDBCException
	 */
	public Pager<DocDto> queryDoc(String custId,Integer start,Integer limit) throws JDBCException{
		return cDocDao.queryDoc(custId, start, limit);
	}

	/**
	 * 填充指定的数据
	 * @param methodName 按照逗号隔开的方法名，
	 * @return
	 */
	public void fillData(Map<String ,Object> map, String methodNames)throws Exception  {

		map.put("optr", getOptr());
		map.put("date", new PrintDate());
		Object custId= map.get("custid");
		CCust cust = null;
		if (custId!=null && StringHelper.isNotEmpty(custId.toString())){
			String dataRight = "";
			try {
				dataRight = this.queryDataRightCon(getOptr(), DataRight.QUERY_CUST.toString());
			} catch (Exception e) {
				dataRight = " 1=1 ";
			}
			//TEST FLAG
			CustFullInfoDto cfid = custComponent.searchCustInfoById(custId.toString());
			cust = cfid.getCust();
			map.put("cust", cust);
			map.put("linkman", cfid.getLinkman());
		}
		
		//读取备注信息
		CDoc doc = (CDoc) map.get("doc");
		List<String> remarks = cDocItemDao.queryRemarkBySn(doc.getDoc_sn());
		String remark = "";
		for (String r : remarks) {
			remark += r;
		}
		if(remark.length()>50)
			remark = remark.substring(0,50);
		map.put("remark",remark);
		

		//获取动态数据
		Class[] types = new Class[]{Map.class,CCust.class};
		if(StringHelper.isNotEmpty(methodNames)) {
			String[] mds = methodNames.split(",");
			for (String md : mds) {
				LoggerHelper.debug(PrintComponent.class, "invoke mothodName:"+md);
				BeanHelper.invoke(dynamicDataComponent, md, types, new Object[]{map,cust});
			}
		}
		
	}



	public void setCCustDao(CCustDao custDao) {
		cCustDao = custDao;
	}

	public void setCCustLinkmanDao(CCustLinkmanDao custLinkmanDao) {
		cCustLinkmanDao = custLinkmanDao;
	}

	public void setCDocDao(CDocDao docDao) {
		cDocDao = docDao;
	}
	public void setCDocItemDao(CDocItemDao docItemDao) {
		cDocItemDao = docItemDao;
	}
	public void setCDocFeeDao(CDocFeeDao docFeeDao) {
		cDocFeeDao = docFeeDao;
	}

	public void setTInvoicePrintitemDao(TInvoicePrintitemDao invoicePrintitemDao) {
		tInvoicePrintitemDao = invoicePrintitemDao;
	}
	/**
	 * @param dynamicDataComponent the dynamicDataComponent to set
	 */
	public void setDynamicDataComponent(DynamicDataComponent dynamicDataComponent) {
		this.dynamicDataComponent = dynamicDataComponent;
	}

	/**
	 * @param invoiceDao the cInvoiceDao to set
	 */
	public void setCInvoiceDao(CInvoiceDao invoiceDao) {
		cInvoiceDao = invoiceDao;
	}

	/**
	 * @param invoiceItemDao the cInvoiceItemDao to set
	 */
	public void setCInvoiceItemDao(CInvoiceItemDao invoiceItemDao) {
		cInvoiceItemDao = invoiceItemDao;
	}

	/**
	 * @param feeDao the cFeeDao to set
	 */
	public void setCFeeDao(CFeeDao feeDao) {
		cFeeDao = feeDao;
	}

	/**
	 * 查询客户的所有发票信息
	 * @param custId
	 * @param countyId
	 * @return
	 */
	public List<CInvoiceDto> queryInvoiceByCustId(String custId) throws Exception {
		return cInvoiceDao.queryInvoiceByCustId(custId, getOptr().getCounty_id());
	}

	/**
	 * 查询客户的单据 （不包括发票）
	 * @param custId
	 * @return
	 */
	public List<CDoneCodeInfo> queryDocByCustId(String custId) throws Exception {
		return cDoneCodeInfoDao.queryDocByCustId(custId,getOptr().getCounty_id());
	}
	
	public List<CDoc> queryBusiConfirmDocByCustId(String custId) throws Exception {
		List<CDoc> docs = cDoneCodeInfoDao.queryBusiConfirmDocByCustId(custId,getOptr().getCounty_id());
		Map<String, List<CDoc>> map = CollectionHelper.converToMap(docs, "doc_sn");
		List<CDoc> result = new ArrayList<CDoc>();
		for(String key :map.keySet()){
			List<CDoc> list = map.get(key);
			CDoc doc = list.get(0);
			String busiNames = "";
			for(CDoc d:list){
				busiNames += d.getBusi_name() + ",";
			}
			doc.setBusi_name(busiNames.substring(0, busiNames.length() -1));
			result.add(doc);
		}
		return result;
	}

	/**
	 * 根据发票查询单据
	 * @param oldInvoiceId
	 * @param oldInvoiceCode
	 * @return
	 */
	public CDoc queryDocByInvoice(String oldInvoiceId, String oldInvoiceCode) throws JDBCException {
		return cDocDao.queryByInvoice(oldInvoiceId,oldInvoiceCode);
	}
	
	/**
	 * 保存发票打印后信息
	 * @param invoice
	 * @throws JDBCException
	 */
	public void saveInvoice(Integer doneCode,InvoiceFromDto invoice,int balance) throws Exception {
		String docitemData = StringHelper.isEmpty(invoice.getDocitem_data())?"":invoice.getDocitem_data();
		docitemData += ","+balance;
		List<String> docSnItems = invoice.getDocSnItems();
		if (docSnItems != null && docSnItems.size() > 0) {
			CInvoice cInvoice = new CInvoice();
			cInvoice.setDone_code(doneCode);
			cInvoice.setDoc_sn(invoice.getDoc_sn());
			cInvoice.setInvoice_code(invoice.getInvoice_code());
			cInvoice.setInvoice_id(invoice.getInvoice_id());
			cInvoice.setInvoice_book_id(invoice.getInvoice_book_id());
			cInvoice.setStatus(StatusConstants.ACTIVE);
			cInvoice.setAmount(invoice.getAmount());
			cInvoice.setDocitem_data(docitemData);
			cInvoice.setPrint_date(DateHelper.now());
			setBaseInfo(cInvoice);

			cInvoiceDao.save(cInvoice);

			List<CInvoiceItem> invoiceitems = new ArrayList<CInvoiceItem>();
			for (String docitemsn : docSnItems) {
				String[] dsns = docitemsn.split(",");
				for(String sn : dsns){
					CInvoiceItem cInvoiceItem = new CInvoiceItem();
					cInvoiceItem.setDocitem_sn(sn);
					cInvoiceItem.setInvoice_code(cInvoice.getInvoice_code());
					cInvoiceItem.setInvoice_id(cInvoice.getInvoice_id());
					invoiceitems.add(cInvoiceItem);
	
					cFeeDao.updateInvoiceByDocItem(sn, cInvoice.getInvoice_code(), cInvoice.getInvoice_book_id(),
							cInvoice.getInvoice_id(),SystemConstants.INVOICE_MODE_AUTO);
				}
			}
			cInvoiceItemDao.save(invoiceitems.toArray(new CInvoiceItem[invoiceitems
			                                           				.size()]));
		}
	}
	/**
	 * 查询发票重打信息
	 * @param invoiceId
	 * @param invoiceCode
	 * @return
	 */
	public CInvoiceDto queryReprintInvoice(String invoiceId, String invoiceCode) throws Exception {
		return cInvoiceDao.queryReprintInvoice(invoiceId,invoiceCode,getOptr().getCounty_id());
	}

	/**
	 * 
	 * @param docSn
	 * @return
	 */
	public void saveConfigPrint(String[] doneCode) throws JDBCException {
		cDocDao.updateLastPrintDate(doneCode);
	}

	/**
	 * @param busiDocTemplatefileDao the tBusiDocTemplatefileDao to set
	 */
	public void setTBusiDocTemplatefileDao(
			TBusiDocTemplatefileDao busiDocTemplatefileDao) {
		tBusiDocTemplatefileDao = busiDocTemplatefileDao;
	}

	/**
	 * @param doneCode
	 * @return
	 */
	public List<DocDto> queryDocInvoice(Integer doneCode) throws Exception {
		List<DocDto> docs = cDocDao.queryDocInvoice(doneCode);
		fillTemplateFile(docs);
		return docs;
	}
	
	public List<DocDto> queryInvoiceType(Integer doneCode,String custId) throws Exception {
		List<DocDto> docs = cDocDao.queryInvoiceType(doneCode,custId,getOptr().getCounty_id());
		fillTemplateFile(docs);
		return docs;
	}
	
	public List<String> queryDocFeeSn(List<String> docSnItems) throws JDBCException{
		return cFeeDao.queryDocFeeSn(docSnItems);
	}

	/**
	 * @param docSnItems
	 * @param invoice_id
	 * @param invoice_code
	 * @param invoice_book_id
	 */
	public void updateDocStatus(List<String> feeSnList, String invoiceId,
			String invoiceCode, String invoiceBookId) throws JDBCException {
		cFeeDao.updateDocStatus(feeSnList, invoiceId, invoiceCode,
				invoiceBookId);
	}
	
	/**
	 * @param printitemId
	 * @return
	 */
	public TPrintitem queryPrintItemById(String printitemId) throws JDBCException {
		return tPrintitemDao.findByKey(printitemId);
	}
	
	/**
	 * @param printitem
	 */
	public void editPrintitem(TPrintitem printitem) throws JDBCException {
		tPrintitemDao.update(printitem);
	}

	/**
	 * @param doneCodeInfoDao the cDoneCodeInfoDao to set
	 */
	public void setCDoneCodeInfoDao(CDoneCodeInfoDao doneCodeInfoDao) {
		cDoneCodeInfoDao = doneCodeInfoDao;
	}

	public void setTPrintitemDao(TPrintitemDao printitemDao) {
		tPrintitemDao = printitemDao;
	}

	/**
	 * @param doneCode
	 * @return
	 */
	public List<PrintItemDto> queryUnPrintItemByDoneCode(String doneCode) throws Exception {
		// TODO Auto-generated method stub
		return cDocDao.queryUnPrintItemByDoneCode(doneCode);
	}

	/**
	 * @param doneCode
	 */
	public void deleteDoc(String doneCode) throws Exception{
		// TODO Auto-generated method stub
		cDocDao.deleteDoc(doneCode);
	}

	/**
	 * @param docItems
	 * @param docSn
	 * @param doneCode
	 */
	public void updateDocItem(String[] docItems, String docSn)  throws Exception{
		cDocDao.updateDocItem( docItems,  docSn);
		
	}

	public void setCUserDao(CUserDao cUserDao) {
		this.cUserDao = cUserDao;
	}

	public void setBBillWriteoffDao(BBillWriteoffDao bBillWriteoffDao) {
		this.bBillWriteoffDao = bBillWriteoffDao;
	}

	/**
	 * @param custComponent the custComponent to set
	 */
	public void setCustComponent(CustComponent custComponent) {
		this.custComponent = custComponent;
	}
}
