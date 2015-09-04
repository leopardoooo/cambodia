package com.ycsoft.sysmanager.web.action.system;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ycsoft.beans.config.TAddress;
import com.ycsoft.beans.core.cust.CCust;
import com.ycsoft.beans.system.SSysChange;
import com.ycsoft.commons.abstracts.BaseAction;
import com.ycsoft.commons.constants.SysChangeType;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ActionException;
import com.ycsoft.commons.exception.ServicesException;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.commons.helper.FileHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.tree.TreeBuilder;
import com.ycsoft.daos.helper.BeanHelper;
import com.ycsoft.sysmanager.component.system.AddressComponent;
import com.ycsoft.sysmanager.web.commons.interceptor.WebOptr;

@SuppressWarnings("serial")
@Controller
public class AddressAction extends BaseAction {

	private AddressComponent addressComponent;

	private String queryText;
	private String addrId;
	private TAddress addr;
	private String addrListStr;
	private String custId;
	private String status;
	
	private File file;

	/**
	 * 返回地区列表
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String queryAddresses() throws Exception{
		if(optr.getCounty_id().equals(SystemConstants.COUNTY_ALL)&&StringHelper.isNotEmpty(queryText)){
			List list = addressComponent.queryAddrByName(queryText,optr);
			getRoot().setRecords(TreeBuilder.createTree(list));
		}
		if(!optr.getCounty_id().equals(SystemConstants.COUNTY_ALL)){
			List list = addressComponent.queryAddrByName(queryText,optr);
			getRoot().setRecords(TreeBuilder.createTree(list));
		}
		return JSON_RECORDS;
	}

	public String queryAddrTree() throws Exception{
		List addrs =  addressComponent.queryAddrByName(queryText,addrId,optr);
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
		getRoot().setSimpleObj(addressComponent.saveAddress(addr));
		TAddress newAdd = addressComponent.queryAddrByaddrId(addr.getAddr_id());
		List<TAddress> newList = new ArrayList<TAddress>();
		newList.add(newAdd);
		saveChanges(oldList, newList);
		return JSON;
	}

	public String queryOptrByCountyId() throws Exception {
		getRoot().setRecords(addressComponent.queryOptrByCountyId(optr.getCounty_id()));
		return JSON_RECORDS;
	}
	
	/*
	 * 批量保存地区
	 */
	public String saveAddrList() throws Exception{
		//TODO 记录异动
		List<TAddress> addrList = null;
		if(StringHelper.isNotEmpty(addrListStr)){
			Type type = new TypeToken<List<TAddress>>(){}.getType();
			Gson gson = new Gson();
			addrList = gson.fromJson(addrListStr,type);
		}
		List<TAddress> oldList = new ArrayList<TAddress>();
		getRoot().setRecords(addressComponent.saveAddrList(addrList, optr));
		List<TAddress> newList = new ArrayList<TAddress>();
		for(TAddress add:addrList){
			newList.add(addressComponent.queryAddrByaddrId(add.getAddr_id()));
		}
		saveChanges(oldList,newList);
		return JSON;
	}

	private void saveChanges(List<TAddress> oldList, List<TAddress> newList) throws ActionException{
		try{
			List<SSysChange> changes = new ArrayList<SSysChange>();
			
			String content;
			String optrId = WebOptr.getOptr().getOptr_id();
			Date createTime = new Date();
			Integer doneCode = addressComponent.getDoneCOde();
			String changeType = SysChangeType.ADDRESS.toString();
			String key ;
			String keyDesc;
			String changeDesc = "地区定义";
			if(CollectionHelper.isEmpty(oldList)){//新增
				for(TAddress add:newList){
					key = add.getAddr_id();
					keyDesc = add.getAddr_name();
					content = BeanHelper.beanchange(null, add);
					SSysChange change = new SSysChange(changeType, doneCode, key, keyDesc, changeDesc, content, optrId, createTime);
					changes.add(change);
				}
			}else{
				TAddress oldAdd = oldList.get(0);
				TAddress newAdd = CollectionHelper.isNotEmpty(newList)?newList.get(0):null;
				
				key = oldAdd.getAddr_id();
				keyDesc = newAdd!=null ? newAdd.getAddr_name():oldAdd.getAddr_name();
				content = BeanHelper.beanchange(oldAdd, newAdd);
				SSysChange change = new SSysChange(changeType, doneCode, key, keyDesc, changeDesc, content, optrId, createTime);
				changes.add(change);
			}
			
			addressComponent.getSSysChangeDao().save(changes.toArray(new SSysChange[changes.size()]));
		}catch (Exception e) {
			throw new ActionException(e.getMessage());
		}
	}



	/**
	 * 修改地区名字
	 * @return
	 * @throws Exception
	 */
	public String editAddress() throws Exception{
		List<TAddress> oldList = new ArrayList<TAddress>();
		oldList.add(addressComponent.queryAddrByaddrId(addr.getAddr_id()));
		addressComponent.editAddress(addr);
		List<TAddress> newList = new ArrayList<TAddress>();
		newList.add(addressComponent.queryAddrByaddrId(addr.getAddr_id()));
		saveChanges(oldList, newList);
		return JSON;
	}

	/**
	 * 删除地区
	 * @return
	 * @throws Exception
	 */
	public String deleteAddress() throws Exception{
		//TODO 记录异动
		List<TAddress> oldList = new ArrayList<TAddress>();
		oldList.add(addressComponent.queryAddrByaddrId(addrId));
		getRoot().setSimpleObj(addressComponent.deleteAddress(addrId));
		List<TAddress> newList = new ArrayList<TAddress>();
		newList.add(addressComponent.queryAddrByaddrId(addrId));
		saveChanges(oldList, newList);
		return JSON;
	}
	
	public String updateAddressStatus() throws Exception {
		addressComponent.updateAddressStatus(addrId, status);
		return JSON_SUCCESS;
	}

	/**
	 * 小区挂载，明细或者文件批量导入
	 * @return
	 * @throws Exception
	 */
	public String changeAddr() throws Exception{
		String newAddrId = request.getParameter("newAddrId");
		String countyId = request.getParameter("countyId");
		List<String> addrIdList = new ArrayList<String>();
		String[] addr = null;
		//文件批量导入
		if(file != null){
			String msg = "";
			String record = "";
			List<String> list = new ArrayList<String>();
			try{
				
				addrIdList = FileHelper.fileToArray(file);
				addr = addrIdList.toArray(new String[addrIdList.size()]);
				if(addrIdList.size() > 1000)
					throw new Exception("请一次性录入小于1000条数据");
				else if(addrIdList.size() == 0){
					throw new Exception("文件数据为空");
				}
				record = addressComponent.changeAddr(newAddrId,addr,countyId,optr);
				if(StringHelper.isNotEmpty(record)){
					list= java.util.Arrays.asList(record.split(","));
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				msg = e.getMessage();
			}
			if(StringHelper.isNotEmpty(msg)){
				return retrunNone(msg);
			}
			return returnList(list);
		}
		//明细导入
		if(StringHelper.isNotEmpty(addrId)){
			addr = addrId.split(",");
			getRoot().setSimpleObj(addressComponent.changeAddr(newAddrId,addr,countyId,optr));
		}
		return JSON;
	}
	
	public String queryCust() throws Exception {
		Integer start = Integer.parseInt(request.getParameter("start"));
		Integer limit = Integer.parseInt(request.getParameter("limit"));
		String countyId = request.getParameter("county_id");
		getRoot().setPage(addressComponent.queryCustAddrByCustIds(custId.split(","),countyId,start,limit));
		return JSON_PAGE;
	}
	
	public String updateAddressList() throws Exception{
		String countyId = request.getParameter("countyId");
		List<CCust> custAddrList = new ArrayList<CCust>();
		if(StringHelper.isNotEmpty(addrListStr)){
			Type type = new TypeToken<List<CCust>>(){}.getType();
			Gson gson = new Gson();
			custAddrList = gson.fromJson(addrListStr, type);
		}
		if(custAddrList == null){
			throw new ServicesException("客户不存在");
		}
		addressComponent.updateAddressList(custAddrList,countyId,optr);
		return JSON;
	}
	
	public String queryFgsByCountyId() throws Exception {
		getRoot().setRecords(addressComponent.queryFgsByCountyId(optr.getCounty_id()));
		return JSON_RECORDS;
	}
	public String queryAddrByCountyId() throws Exception {
		String countyId = request.getParameter("countyId");
		getRoot().setRecords(addressComponent.queryAddrByCountyId(countyId));
		return JSON_RECORDS;
	}
	public String queryAddrByaddrId() throws Exception {
		getRoot().setRecords(addressComponent.queryAddrByaddrPid(addrId));
		return JSON_RECORDS;
	}
	
	public AddressComponent getAddressComponent() {
		return addressComponent;
	}

	public void setAddressComponent(AddressComponent addressComponent) {
		this.addressComponent = addressComponent;
	}

	public String getQueryText() {
		return queryText;
	}

	public void setQueryText(String queryText) {
		this.queryText = queryText;
	}

	public String getAddrId() {
		return addrId;
	}

	public void setAddrId(String addrId) {
		this.addrId = addrId;
	}

	public String getAddrListStr() {
		return addrListStr;
	}

	public void setAddrListStr(String addrListStr) {
		this.addrListStr = addrListStr;
	}

	public TAddress getAddr() {
		return addr;
	}

	public void setAddr(TAddress addr) {
		this.addr = addr;
	}



	public String getCustId() {
		return custId;
	}



	public void setCustId(String custId) {
		this.custId = custId;
	}



	public void setStatus(String status) {
		this.status = status;
	}



	public File getFile() {
		return file;
	}



	public void setFile(File file) {
		this.file = file;
	}

}
