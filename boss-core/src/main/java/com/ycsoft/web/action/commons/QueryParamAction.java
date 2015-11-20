package com.ycsoft.web.action.commons;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.ycsoft.beans.config.TAddress;
import com.ycsoft.beans.config.TProvince;
import com.ycsoft.business.dto.config.TAddressSysDto;
import com.ycsoft.business.service.IQueryCfgService;
import com.ycsoft.commons.constants.DataRight;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.tree.TreeBuilder;
import com.ycsoft.sysmanager.dto.resource.DeviceDto;
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
	private String editId;
	private IQueryCfgService queryCfgService;
	private String queryText;
	private String districtId;
	private TAddressSysDto addrDto;
	private String status;
	private File files;

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
		List addrs =  queryCfgService.queryAddrByName(comboQueryText,addrId,editId);
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
	
	public String queryAddressTree() throws Exception{
		List addrs =  queryCfgService.queryAddressTree(queryText,addrId,optr);
		getRoot().setRecords(TreeBuilder.createSysAdreeTree(addrs));
		return JSON_RECORDS;
	}
	
	public String queryDistrictTree() throws Exception{
		List addrs =  queryCfgService.queryDistrictByPid(districtId);
		getRoot().setRecords(TreeBuilder.createSysAdreeTree(addrs));
		return JSON_RECORDS;
	}
	
	/**
	 * 增加地区
	 * @return
	 * @throws Exception
	 */
	public String saveAddress() throws Exception{
		//TODO 记录异动
		List<TAddress> oldList = new ArrayList<TAddress>();
		String type = request.getParameter("type");
		TAddress  addr = queryCfgService.saveAddress(addrDto,type);
		getRoot().setSimpleObj(addr);
//		TAddress newAdd = addressComponent.queryAddrByaddrId(addrDto.getAddr_id());
		List<TAddress> newList = new ArrayList<TAddress>();
		newList.add(addr);
//		saveChanges(oldList, newList);
		return JSON;
	}
	
	public String queryCanToSendOsd() throws Exception{
		String begin_date = request.getParameter("begin_date");
		String end_date = request.getParameter("end_date");
		String detail_time = request.getParameter("detail_time");
		String message = request.getParameter("message");
		List<String> list = new ArrayList<String>();
		String msg = "";
		try{
			list = queryCfgService.queryCanToSendOsd(files,begin_date,end_date,detail_time,message);
		}catch(Exception e){
			e.printStackTrace();
			msg = e.getMessage();
		}
		if(StringHelper.isNotEmpty(msg)){
			return retrunNone(msg);
		}
		return returnList(list);
		
	}
	
	public String saveOsd() throws Exception {
		String begin_date = request.getParameter("begin_date");
		String end_date = request.getParameter("end_date");
		String detail_time = request.getParameter("detail_time");
		String send_title = request.getParameter("send_title");
		String send_optr = request.getParameter("send_optr");
		String message = request.getParameter("message");
		String msg = "";
		try{
			queryCfgService.saveOsdByFiles(files,begin_date,end_date,detail_time,send_title,send_optr,message );
		}catch(Exception e){
			e.printStackTrace();
			msg = e.getMessage();
		}
		return retrunNone(msg);
	}
	
	public String queryProvince() throws Exception{
		List<TProvince> list = queryCfgService.queryProvince();
		getRoot().setRecords(list);
		return JSON_RECORDS;
	}
	
	public String queryDistrictByPid() throws Exception{
		List addrs =  queryCfgService.queryDistrictByPid(addrId);
		getRoot().setRecords(TreeBuilder.createAdreeSynchronousTree(addrs));
		return JSON_RECORDS;
	}
	public String updateAddressStatus() throws Exception {
		queryCfgService.updateAddressStatus(addrId, status);
		return JSON_SUCCESS;
	}
	
	/**
	 * 修改地区名字
	 * @return
	 * @throws Exception
	 */
	public String editAddress() throws Exception{
//		List<TAddress> oldList = new ArrayList<TAddress>();
//		oldList.add(addressComponent.queryAddrByaddrId(addrDto.getAddr_id()));
		queryCfgService.editAddress(addrDto);
//		List<TAddress> newList = new ArrayList<TAddress>();
//		newList.add(addressComponent.queryAddrByaddrId(addrDto.getAddr_id()));
//		saveChanges(oldList, newList);
		return JSON;
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

	public String getEditId() {
		return editId;
	}

	public void setEditId(String editId) {
		this.editId = editId;
	}

	public void setQueryText(String queryText) {
		this.queryText = queryText;
	}

	public void setDistrictId(String districtId) {
		this.districtId = districtId;
	}

	
	public TAddressSysDto getAddrDto() {
		return addrDto;
	}

	public void setAddrDto(TAddressSysDto addrDto) {
		this.addrDto = addrDto;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public File getFiles() {
		return files;
	}

	public void setFiles(File files) {
		this.files = files;
	}
	
}
