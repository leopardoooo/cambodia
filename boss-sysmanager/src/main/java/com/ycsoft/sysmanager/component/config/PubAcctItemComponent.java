package com.ycsoft.sysmanager.component.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TAcctitemToProd;
import com.ycsoft.beans.config.TBusiDoc;
import com.ycsoft.beans.config.TInvoicePrintitem;
import com.ycsoft.beans.config.TPrintitem;
import com.ycsoft.beans.config.TPublicAcctitem;
import com.ycsoft.beans.prod.PProd;
import com.ycsoft.business.dao.config.TAcctitemToProdDao;
import com.ycsoft.business.dao.config.TInvoicePrintitemDao;
import com.ycsoft.business.dao.config.TPrintitemDao;
import com.ycsoft.business.dao.config.TPublicAcctitemDao;
import com.ycsoft.business.dao.config.TTemplateDao;
import com.ycsoft.business.dao.prod.PProdDao;
import com.ycsoft.commons.abstracts.BaseComponent;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ComponentException;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.store.TemplateConfig;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.sysmanager.dto.config.PublicAcctItemDto;
import com.ycsoft.sysmanager.web.commons.interceptor.WebOptr;

@Component
public class PubAcctItemComponent extends BaseComponent {

	private TPrintitemDao tPrintitemDao;
	private TPublicAcctitemDao tPublicAcctitemDao;
	private TAcctitemToProdDao tAcctitemToProdDao;
	private PProdDao pProdDao;
	private TInvoicePrintitemDao tInvoicePrintitemDao;
	private TTemplateDao tTemplateDao;

	/**
	 * 查询公用账目信息
	 * @return
	 * @throws Exception
	 */
	public List<PublicAcctItemDto> queryAllPubAcctItems() throws Exception{
		List<TPublicAcctitem> acctItemList = tPublicAcctitemDao.queyrAll();
		List<TPrintitem> printItemList = tPrintitemDao.findAll();
		Map<String,TPrintitem> printMap = CollectionHelper.converToMapSingle(printItemList, "printitem_id");

		List<PublicAcctItemDto> resultList = new ArrayList<PublicAcctItemDto>();
		for(TPublicAcctitem acctItem : acctItemList){
			PublicAcctItemDto pubAcctItemDto = new PublicAcctItemDto();
			BeanUtils.copyProperties(acctItem, pubAcctItemDto);

			//赋值打印名称
			TPrintitem print = printMap.get(acctItem.getPrintitem_id());
			if(print != null){
				pubAcctItemDto.setPrintitem_name(print.getPrintitem_name());
			}

			//赋值产品ID和名称,多个值以逗号隔开
			List<TAcctitemToProd> AcctToPList = tAcctitemToProdDao.queryById(acctItem.getAcctitem_id());
			String prodIds = "";
			String prodNames = "";
			for(int i=0;i<AcctToPList.size();i++){
				if(i != AcctToPList.size() -1){
					prodIds = StringHelper.append(prodIds,AcctToPList.get(i).getProd_id(),",");
					prodNames = StringHelper.append(prodNames,AcctToPList.get(i).getProd_name(),",");
				}else{
					prodIds = StringHelper.append(prodIds,AcctToPList.get(i).getProd_id());
					prodNames = StringHelper.append(prodNames,AcctToPList.get(i).getProd_name());
				}
			}
			pubAcctItemDto.setProdIds(prodIds);
			pubAcctItemDto.setProdNames(prodNames);
			resultList.add(pubAcctItemDto);
		}
		return resultList;
	}

	/**
	 * 查询所有产品
	 * @return
	 * @throws Exception
	 */
	public List<PProd> queryAllProds() throws Exception{
		return pProdDao.findAll();
	}

	/**
	 * 查询所有打印项
	 * @return
	 * @throws Exception
	 */
	public List<TPrintitem> queryAllPrintitems() throws Exception{
		return tPrintitemDao.findAll();
	}

	/**
	 * 保存或修改账目
	 * @param publicAcctitem
	 * @param prodIds
	 * @throws Exception
	 */
	public void savePublicAcctItem(TPublicAcctitem publicAcctitem,String[] prodIds) throws Exception{
		if(StringHelper.isNotEmpty(publicAcctitem.getAcctitem_id())){//修改
			tPublicAcctitemDao.update(publicAcctitem);
		}else{//保存
			publicAcctitem.setAcctitem_id(tPublicAcctitemDao.getAcctItemdId());
			publicAcctitem.setAcctitem_type(SystemConstants.ACCT_TYPE_SPEC);
			tPublicAcctitemDao.save(publicAcctitem);
		}

		String acctItemId = publicAcctitem.getAcctitem_id();
		tAcctitemToProdDao.deleteById(acctItemId);
		for (String element : prodIds) {
			TAcctitemToProd entity = new TAcctitemToProd();
			entity.setAcctitem_id(acctItemId);
			entity.setProd_id(element);
			tAcctitemToProdDao.save(entity);
		}
	}
	
	/**
	 * 保存打印项
	 * @return
	 * @throws Exception
	 */
	public void savePrintItem(String printitemName,List<TBusiDoc> docList) throws Exception{
		
		TPrintitem print = new TPrintitem();
		print.setPrintitem_id(getPrintItemId());
		print.setPrintitem_name(printitemName);
		tPrintitemDao.save(print);
		List<TInvoicePrintitem> list = new ArrayList<TInvoicePrintitem>();
		String id = tTemplateDao.getTemplateId(WebOptr
				.getOptr().getCounty_id(), TemplateConfig.Template.INVOICE
				.toString());
		for(TBusiDoc dto:docList){
			TInvoicePrintitem invoicePrintitem = new TInvoicePrintitem();
			invoicePrintitem.setTemplate_id(id);
			invoicePrintitem.setPrintitem_id(print.getPrintitem_id());
			invoicePrintitem.setDoc_type(dto.getDoc_type());
			list.add(invoicePrintitem);
		}
		tInvoicePrintitemDao.save(list.toArray(new TInvoicePrintitem[list.size()]));
	}
	
	private String getPrintItemId() throws JDBCException{
		return tPrintitemDao.findSequence().toString();
	}


	public TPrintitemDao getTPrintitemDao() {
		return tPrintitemDao;
	}


	public void setTPrintitemDao(TPrintitemDao printitemDao) {
		tPrintitemDao = printitemDao;
	}


	public TPublicAcctitemDao getTPublicAcctitemDao() {
		return tPublicAcctitemDao;
	}
	public void setTPublicAcctitemDao(TPublicAcctitemDao publicAcctitemDao) {
		tPublicAcctitemDao = publicAcctitemDao;
	}

	public TAcctitemToProdDao getTAcctitemToProdDao() {
		return tAcctitemToProdDao;
	}

	public void setTAcctitemToProdDao(TAcctitemToProdDao acctitemToProdDao) {
		tAcctitemToProdDao = acctitemToProdDao;
	}

	public PProdDao getPProdDao() {
		return pProdDao;
	}

	public void setPProdDao(PProdDao prodDao) {
		pProdDao = prodDao;
	}

	public void setTInvoicePrintitemDao(TInvoicePrintitemDao invoicePrintitemDao) {
		tInvoicePrintitemDao = invoicePrintitemDao;
	}

	/**
	 * @param templateDao the tTemplateDao to set
	 */
	public void setTTemplateDao(TTemplateDao templateDao) {
		tTemplateDao = templateDao;
	}
}
