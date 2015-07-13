package com.ycsoft.sysmanager.web.action.config;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ycsoft.beans.config.TBusiDoc;
import com.ycsoft.beans.config.TPublicAcctitem;
import com.ycsoft.commons.abstracts.BaseAction;
import com.ycsoft.sysmanager.component.config.PubAcctItemComponent;

public class PubAcctItemAction extends BaseAction {

	/**
	 *
	 */
	private static final long serialVersionUID = -4577919788000131341L;
	private PubAcctItemComponent pubAcctItemComponent;

	private TPublicAcctitem publicAcctitem;
	private String prodIds;
	
	private String printitemName;
	private String templateId;
	private String docType;
	private String templateArr;


	/**
	 * 查询公用账目信息
	 * @return
	 * @throws Exception
	 */
	public String queryAllPubAcctItems() throws Exception{
		getRoot().setRecords(pubAcctItemComponent.queryAllPubAcctItems());
		return JSON_RECORDS;
	}

	/**
	 * 查询所有产品
	 * @return
	 * @throws Exception
	 */
	public String queryAllProds() throws Exception{
		getRoot().setRecords(pubAcctItemComponent.queryAllProds());
		return JSON_RECORDS;
	}

	/**
	 * 查询所有打印项
	 * @return
	 * @throws Exception
	 */
	public String queryAllPrintitems() throws Exception{
		getRoot().setRecords(pubAcctItemComponent.queryAllPrintitems());
		return JSON_RECORDS;
	}

	/**
	 * 保存或修改账目
	 * @param publicAcctitem
	 * @param prodIds
	 * @throws Exception
	 */
	public String savePublicAcctItem() throws Exception{
		pubAcctItemComponent.savePublicAcctItem(publicAcctitem, prodIds.split(","));
		return JSON;
	}
	
	/**
	 * 保存打印项
	 * @return
	 * @throws Exception
	 */
	public String savePrintItem() throws Exception{
		List<TBusiDoc> docList = new ArrayList<TBusiDoc>();
		Type type = new TypeToken<List<TBusiDoc>>(){}.getType();
		Gson gson = new Gson();
		docList = gson.fromJson(templateArr, type);
		pubAcctItemComponent.savePrintItem(printitemName,docList);
		return JSON;
	}

	public PubAcctItemComponent getPubAcctItemComponent() {
		return pubAcctItemComponent;
	}

	public void setPubAcctItemComponent(PubAcctItemComponent pubAcctItemComponent) {
		this.pubAcctItemComponent = pubAcctItemComponent;
	}

	public TPublicAcctitem getPublicAcctitem() {
		return publicAcctitem;
	}

	public void setPublicAcctitem(TPublicAcctitem publicAcctitem) {
		this.publicAcctitem = publicAcctitem;
	}

	public String getProdIds() {
		return prodIds;
	}

	public void setProdIds(String prodIds) {
		this.prodIds = prodIds;
	}

	public String getPrintitemName() {
		return printitemName;
	}

	public void setPrintitemName(String printitemName) {
		this.printitemName = printitemName;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}
	
	public void setTemplateArr(String templateArr) {
		this.templateArr = templateArr;
	}


}
