package com.ycsoft.sysmanager.web.action.resource;

import java.lang.reflect.Type;
import java.util.List;

import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ycsoft.beans.config.TDeviceBuyMode;
import com.ycsoft.beans.device.RCardModel;
import com.ycsoft.beans.device.RDeviceFee;
import com.ycsoft.beans.device.RDeviceModelCounty;
import com.ycsoft.beans.device.RDeviceSupplier;
import com.ycsoft.beans.device.RModemModel;
import com.ycsoft.beans.device.RStbModel;
import com.ycsoft.commons.abstracts.BaseAction;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.tree.TreeBuilder;
import com.ycsoft.commons.tree.TreeNode;
import com.ycsoft.sysmanager.component.config.BusiFeeComponent;
import com.ycsoft.sysmanager.component.resource.ResourceCfgComponent;
import com.ycsoft.sysmanager.dto.system.RDepotDto;


@Controller
public class ResourceCfgAction extends BaseAction {

	/**
	 *
	 */
	private static final long serialVersionUID = -5104832611105180481L;

	private ResourceCfgComponent resourceCfgComponent;
	private BusiFeeComponent busiFeeComponent;

	private String deviceBuyModeList;
	private String deviceFeeList;
	private String deviceSupplierList;
	private String stbModelList;
	private String modemModelList;
	private String cardModelList;

	private String buyMode;
	private String supplierId;
	private String supplierName;
	
	private String query;
	private String pid;
	
	private RDepotDto depotDto;
	private String depotId;
	private String deviceType;
	
	private String county_id;
	private String modelCountyList;
	
	public void setCounty_id(String countyId) {
		county_id = countyId;
	}

	public void setDepotId(String depotId) {
		this.depotId = depotId;
	}
	
	/**
	 * 查询设备的一次性收费
	 * @return
	 * @throws Exception
	 */
	public String queryDeviceBusiFee() throws Exception {
		getRoot().setRecords(resourceCfgComponent.queryDeviceBusiFee());
		return JSON_RECORDS;
	}

	/**
	 * 保存修改仓库
	 * @return
	 * @throws Exception
	 */
	public String saveDepot() throws Exception {
		String dis = depotDto.getDistrict();
		if(StringHelper.isNotEmpty(dis)){
			String[] districts = dis.split(",");
			depotDto.setArea_id(districts[0]);
			depotDto.setCounty_id(districts[1]);
		}
		getRoot().setSuccess(resourceCfgComponent.saveDepot(depotDto));
		return JSON;
	}
	

	/**
	 * 查询所有的设备费用
	 */
	public String queryDeviceFee() throws Exception{
		getRoot().setRecords(busiFeeComponent.queryDeviceFee());
		return JSON_RECORDS;
	}

	/**
	 * 删除购买方式
	 * @return
	 * @throws Exception
	 */
	public String removeDeviceBuyMode() throws Exception {
		resourceCfgComponent.removeDeviceBuyMode(buyMode);
		return JSON_SUCCESS;
	}

	/**
	 * 删除设备供应商
	 * @return
	 * @throws Exception
	 */
	public String removeRDeviceSupplier() throws Exception {
		resourceCfgComponent.removeRDeviceSupplier(supplierId, supplierName);
		return JSON;
	}

	/**
	 * 保存购买方式
	 * @return
	 * @throws Exception
	 */
	public String saveDeviceBuyMode() throws Exception {
		Type type = new TypeToken<List<TDeviceBuyMode>>(){}.getType();
		List<TDeviceBuyMode> list = new Gson().fromJson(deviceBuyModeList, type);
		resourceCfgComponent.saveDeviceBuyMode(list);
		return JSON;
	}

	/**
	 * 保存智能卡型号
	 * @return
	 * @throws Exception
	 */
	public String saveRCardModel() throws Exception {
		Type type = new TypeToken<List<RCardModel>>(){}.getType();
		List<RCardModel> list = new Gson().fromJson(cardModelList, type);
		resourceCfgComponent.saveRCardModel(list);
		return JSON;
	}

	/**
	 * 保存设备费用
	 * @return
	 * @throws Exception
	 */
	public String saveRDeviceFee() throws Exception {
		Type type = new TypeToken<List<RDeviceFee>>(){}.getType();
		List<RDeviceFee> list = new Gson().fromJson(deviceFeeList, type);
		resourceCfgComponent.saveRDeviceFee(list);
		return JSON;
	}

	/**
	 * 保存设备供应商
	 * @return
	 * @throws Exception
	 */
	public String saveRDeviceSupplier() throws Exception {
		Type type = new TypeToken<List<RDeviceSupplier>>(){}.getType();
		List<RDeviceSupplier> list = new Gson().fromJson(deviceSupplierList, type);
		resourceCfgComponent.saveRDeviceSupplier(list);
		return JSON_SUCCESS;
	}

	/**
	 * 保存modem型号
	 * @return
	 * @throws Exception
	 */
	public String saveRModemModel() throws Exception {
		Type type = new TypeToken<List<RModemModel>>(){}.getType();
		List<RModemModel> list = new Gson().fromJson(modemModelList, type);
		resourceCfgComponent.saveRModemModel(list);
		return JSON_SUCCESS;
	}

	/**
	 * 保存机顶盒型号
	 * @return
	 * @throws Exception
	 */
	public String saveRStbModel() throws Exception {
		Type type = new TypeToken<List<RStbModel>>(){}.getType();
		List<RStbModel> list = new Gson().fromJson(stbModelList, type);
		resourceCfgComponent.saveRStbModel(list);
		return JSON_SUCCESS;
	}

	/**
	 * 查询购买方式
	 * @return
	 * @throws Exception
	 */
	public String queryDeviceBuyMode() throws Exception {
		getRoot().setRecords(resourceCfgComponent.queryDeviceBuyMode());
		return JSON_RECORDS;
	}

	/**
	 * 查询设备供应商
	 * @return
	 * @throws Exception
	 */
	public String queryRDeviceSupplier() throws Exception {
		getRoot().setRecords(resourceCfgComponent.queryRDeviceSupplier());
		return JSON_RECORDS;
	}

	/**
	 * 查询设备费用
	 * @return
	 * @throws Exception
	 */
	public String queryRDeviceFee() throws Exception {
		getRoot().setRecords(resourceCfgComponent.queryRDeviceFee());
		return JSON_RECORDS;
	}
	
	/**
	 * 根据设备类型查询设备型号信息
	 * @return
	 * @throws Exception
	 */
	public String queryDeviceModel() throws Exception {
		getRoot().setRecords(resourceCfgComponent.queryDeviceModel(deviceType));
		return JSON_RECORDS;
	}
	
	/**
	 * 查询机顶盒型号
	 * @return
	 * @throws Exception
	 */
	public String queryRStbModel() throws Exception {
		getRoot().setRecords(resourceCfgComponent.queryRStbModel());
		return JSON_RECORDS;
	}

	/**
	 * 查询智能卡型号
	 * @return
	 * @throws Exception
	 */
	public String queryRCardModel() throws Exception {
		getRoot().setRecords(resourceCfgComponent.queryRCardModel());
		return JSON_RECORDS;
	}

	public String queryCountyModel() throws Exception {
		getRoot().setRecords(resourceCfgComponent.queryCountyModel(optr));
		return JSON_RECORDS;
	}
	
	
	@SuppressWarnings("unchecked")
	public String getModelCountyTree() throws Exception{
		List<TreeNode> prodtree = TreeBuilder.createTreeCheck((List)resourceCfgComponent.getModelCountyTree(county_id,optr));
		getRoot().setRecords(prodtree);
		return JSON_RECORDS;
	}
	
	public String saveModelCounty() throws Exception{
		List<RDeviceModelCounty> mCountyList = null;
		if(StringHelper.isNotEmpty(modelCountyList)){
			Type type = new TypeToken<List<RDeviceModelCounty>>(){}.getType();
			mCountyList = new Gson().fromJson(modelCountyList, type);
		}
		resourceCfgComponent.saveModelCounty(county_id,mCountyList);
		return JSON;
	}
	
	/**
	 * 查询modem型号
	 * @return
	 * @throws Exception
	 */
	public String queryRModemModel() throws Exception {
		getRoot().setRecords(resourceCfgComponent.queryRModemModel());
		return JSON_RECORDS;
	}

	public void setResourceCfgComponent(ResourceCfgComponent resourceCfgComponent) {
		this.resourceCfgComponent = resourceCfgComponent;
	}

	public void setDeviceBuyModeList(String deviceBuyModeList) {
		this.deviceBuyModeList = deviceBuyModeList;
	}

	public void setDeviceFeeList(String deviceFeeList) {
		this.deviceFeeList = deviceFeeList;
	}

	public void setDeviceSupplierList(String deviceSupplierList) {
		this.deviceSupplierList = deviceSupplierList;
	}

	public void setStbModelList(String stbModelList) {
		this.stbModelList = stbModelList;
	}

	public void setModemModelList(String modemModelList) {
		this.modemModelList = modemModelList;
	}

	public void setCardModelList(String cardModelList) {
		this.cardModelList = cardModelList;
	}

	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public void setBuyMode(String buyMode) {
		this.buyMode = buyMode;
	}

	public void setBusiFeeComponent(BusiFeeComponent busiFeeComponent) {
		this.busiFeeComponent = busiFeeComponent;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public RDepotDto getDepotDto() {
		return depotDto;
	}

	public void setDepotDto(RDepotDto depotDto) {
		this.depotDto = depotDto;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public void setModelCountyList(String modelCountyList) {
		this.modelCountyList = modelCountyList;
	}

}
