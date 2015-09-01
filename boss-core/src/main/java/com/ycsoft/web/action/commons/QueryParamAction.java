package com.ycsoft.web.action.commons;

import java.util.List;

import com.ycsoft.beans.config.TAddress;
import com.ycsoft.business.service.IQueryCfgService;
import com.ycsoft.commons.constants.DataRight;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.tree.TreeBuilder;
import com.ycsoft.web.commons.abstracts.BaseBusiAction;


/**
 *  查询系统参数、等数据的控制器。
 *
 * @author hh
 * @date Feb 8, 2010 7:10:35 PM
 */
public class QueryParamAction extends BaseBusiAction {
	/**
	 *
	 */
	private static final long serialVersionUID = -5406013631195636270L;
	private String node;
	private String comboQueryText;
	private String addrPid;
	private String addrId;
	private IQueryCfgService queryCfgService;


	public String queryProdFreeDay() throws Exception{
		getRoot().setOthers(queryCfgService.queryProdFreeDay());
		return JSON_OTHER;
	}

	/**
	 * 根据地址名称查询地址树信息
	 * @return
	 * @throws Exception
	 */
	public String queryAddrTree() throws Exception{
		List addrs =  queryCfgService.queryAddrByName(comboQueryText,addrId);
		getRoot().setRecords(TreeBuilder.createAdreeTree(addrs,false));
		return JSON_RECORDS;
	}
	
	
	public String queryCustAddrName() throws Exception{
		getRoot().setOthers(queryCfgService.queryCustAddrName(addrId));
		return JSON_OTHER;
	}
	
	public String queryNoteCust() throws Exception{
		getRoot().setPage(queryCfgService.queryNoteCust(addrId,start,limit));
		return JSON_PAGE;
	}
	
	
	public String querySingleAddress() throws Exception {
		TAddress add = queryCfgService.querySingleAddress(addrId);
		getRoot().setSimpleObj(add);
		return JSON_SIMPLEOBJ;
	}
	
	/**
	 * 查询区域
	 * @return
	 * @throws Exception
	 */
	public String queryAddrDistrict() throws Exception{
		getRoot().setRecords(queryCfgService.queryAddrDistrict());
		return JSON_RECORDS;
	}
	
	/**
	 * 查询区域下的小区
	 * @return
	 * @throws Exception
	 */
	public String queryAddrCommunity() throws Exception{
		getRoot().setRecords(queryCfgService.queryAddrCommunity(addrPid));
		return JSON_RECORDS;
	}


	public String queryCanUpdateCustField() throws Exception{
		String busiCode = request.getParameter("busiCode");
		getRoot().setRecords(queryCfgService.queryCanUpdateField(busiCode));
		return JSON_RECORDS;
	}
	
	public String queryPayType()throws Exception{
		getRoot().setRecords(queryCfgService.queryPayType());
		return JSON_RECORDS;
	}

	/**
	 * 数据权限查询下拉数据
	 * @return
	 * @throws Exception
	 */
	public String queryItemValues()throws Exception{
		getRoot().setRecords(queryCfgService.queryItemValues(DataRight.CUST_CLASS_CFG.toString(),DictKey.CUST_CLASS.toString()));
		return JSON_RECORDS;
	}
	
	public String getComboQueryText() {
		return comboQueryText;
	}

	public void setComboQueryText(String comboQueryText) {
		this.comboQueryText = comboQueryText;
	}

	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.node = node;
	}

	public void setQueryCfgService(IQueryCfgService queryCfgService) {
		this.queryCfgService = queryCfgService;
	}

	public void setAddrPid(String addrPid) {
		this.addrPid = addrPid;
	}

	public void setAddrId(String addrId) {
		this.addrId = addrId;
	}
}
