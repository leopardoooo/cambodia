/**
 *
 */
package com.ycsoft.web.action.commons;

import java.util.Map;

import org.springframework.stereotype.Controller;

import com.ycsoft.business.dto.device.DeviceDto;
import com.ycsoft.business.service.ICustService;
import com.ycsoft.business.service.IValuableCardService;
import com.ycsoft.web.commons.abstracts.BaseBusiAction;

/**
 * @author YC-SOFT
 *
 */
@Controller
public class QueryDeviceAction extends BaseBusiAction {
	/**
	 *
	 */
	private static final long serialVersionUID = -8090856074116233534L;
	private ICustService custService;
	private IValuableCardService valuableCardService;
	private String deviceCode ;
	private String buyMode ;
	private String deviceModel;
	private String custId;
	private String deviceId;
	private String deviceType;
	private String stbId;
	private String records;
	private Integer amount;	
	private String userType;
	
	//搜索关键字
	private String query;
	private String queryItem;
	private String newModel;
	private String oldModel;
	
	
	/**
	 * 查询产权变更的设备销售方式
	 * @return
	 * @throws Exception
	 */
	public String queryDeviceBuyModeByOwnership() throws Exception {
		getRoot().setRecords(custService.queryDeviceBuyModeByOwnership());
		return JSON_RECORDS;
	}
	
	
	/**
	 * 查询产权变更的设备销售方式
	 * @return
	 * @throws Exception
	 */
	public String queryDeviceModel() throws Exception {
		getRoot().setRecords(custService.queryDeviceModel());
		return JSON_RECORDS;
	}
	
	
	public String queryDeviceCanFee() throws Exception {
		getRoot().setRecords(custService.queryDeviceCanFee());
		return JSON_RECORDS;
	}
	
	/**
	 * 根据设备编号查询设备信息及客户信息
	 * @return
	 * @throws Exception
	 */
	public String queryDeviceInfoByCode() throws Exception {
		getRoot().setSimpleObj(custService.queryDeviceInfoByCode(deviceCode));
		return JSON_SIMPLEOBJ;
	}

	/**
	 * 根据设备类型查找设备定义
	 * @return
	 * @throws Exception
	 */
	public String queryDeviceModelByDeviceType() throws Exception {
		getRoot().setSimpleObj(custService.queryDeviceModelByDeviceType(deviceId, deviceType));
		return JSON_SIMPLEOBJ;
	}

	/**
	 * 根据设备编号查找可以购买的设备信息
	 * @return
	 * @throws Exception
	 */
	public String queryDevice() throws Exception{

		DeviceDto device =null;
		try {
			device = custService.querySaleableDevice(deviceCode);
			getRoot().setSimpleObj(device);
		} catch (Exception e){
			getRoot().setSuccess(false);
			getRoot().setSimpleObj(e.getMessage());
		}
		return JSON;
	}
	
	/**
	 * 设备更换
	 * @return
	 * @throws Exception
	 */
	public String queryDeviceForExchange() throws Exception{
		DeviceDto device =null;
		try {
			device = custService.querySaleableDevice(deviceCode);
			getRoot().setSimpleObj(device);
		} catch (Exception e){
			getRoot().setSimpleObj(custService.queryUseableDevice(custId,deviceType,deviceCode,userType));
		}
		return JSON_SIMPLEOBJ;
	}
	
	/**
	 * 查询可以用于机卡互换的设备.
	 * @return
	 * @throws Exception
	 */
	public String queryDeviceForSwitch() throws Exception{
		Map<String, Object> device = custService.queryDeviceForSwitch(deviceCode,deviceType,custId );
		getRoot().setSimpleObj(device);
		return JSON_SIMPLEOBJ;
	}

	/**
	 * 查询可以单独购买的智能卡
	 * @return
	 * @throws Exception
	 */
	public String querySingleCard() throws Exception{
		DeviceDto device =null;
		try {
			device = custService.querySaleableCard(deviceCode);
			getRoot().setSimpleObj(device);
		} catch (Exception e){
			getRoot().setSuccess(false);
			getRoot().setSimpleObj(e.getMessage());
		}
		return JSON;
	}
	
	/**
	 * 查询可以单独购买的智能卡
	 * @return
	 * @throws Exception
	 */
	public String querySingleModem() throws Exception{
		DeviceDto device =null;
		try {
			device = custService.querySaleableModem(deviceCode);
			getRoot().setSimpleObj(device);
		} catch (Exception e){
			getRoot().setSuccess(false);
			getRoot().setSimpleObj(e.getMessage());
		}
		return JSON;
	}
	
	/**
	 * 根据设备号查询出该设备的信息以及对应的配对设备信息
	 * @return
	 * @throws Exception
	 */
	public String queryReclaimDevice() throws Exception{
		DeviceDto device =null;
		device = custService.queryDevice(deviceCode);
		getRoot().setSimpleObj(device);
		return JSON;
	}
	/**
	 * 查询购买方式
	 * @return
	 * @throws Exception
	 */
	public String queryDeviceBuyMode() throws Exception{
		getRoot().setRecords(custService.queryDeviceBuyMode());
		return JSON_RECORDS;
	}
	
	/**
	 * 可以购买的设备
	 * @return
	 * @throws Exception
	 */
	public String queryDeviceCanBuy() throws Exception{
		getRoot().setRecords(custService.queryDeviceCanBuy(optr));
		return JSON_RECORDS;
	}
	
	
	public String queryDeviceFee() throws Exception{
		getRoot().setRecords(custService.queryDeviceFee(deviceType,deviceModel,buyMode));
		return JSON_RECORDS;
	}

	public String queryFeeByModel() throws Exception{
		getRoot().setSimpleObj(custService.queryFeeByModel(deviceType,oldModel,newModel));
		return JSON_SIMPLEOBJ;
	}
	
	/**
	 * 查询机顶盒设备类型,验证是否双向
	 * @return
	 * @throws Exception
	 */
	public String queryStbModel() throws Exception{
		getRoot().setSimpleObj(custService.queryStbModel(stbId));
		return JSON_SIMPLEOBJ;
	}

	/**
	 * 查询充值卡
	 * @return
	 * @throws Exception
	 */
	public String queryValuableCard()throws Exception{
		getRoot().setSimpleObj(valuableCardService.queryValuableCard(deviceCode));
		return JSON_SIMPLEOBJ;
	}
	public String queryValuableAllCard() throws Exception{
		String query = request.getParameter("query");
		getRoot().setPage(valuableCardService.queryValuableAllCard(start,limit,query,queryItem));
		return JSON_PAGE;
	}
	
	/**
	 * 保存充值卡
	 * @return
	 * @throws Exception
	 */
	public String saveValuableCard() throws Exception {
		getRoot().setSimpleObj(valuableCardService.saveValuableCard(amount,records,optr));
		return JSON_SIMPLEOBJ;
	}
	
	public String editValuableCard() throws Exception {
		String custName = request.getParameter("cust_name");
		String doneCode = request.getParameter("done_code");
		valuableCardService.editValuableCard(doneCode,custName);
		return JSON;
	}
	
	/**
	 * 删除充值卡
	 * @return
	 * @throws Exception
	 */
	public String removeValuableCard() throws Exception {
		String valuableId = request.getParameter("valuable_id");
		getRoot().setSimpleObj(valuableCardService.removeValuableCard(valuableId.split(","),optr));
		return JSON;
	}
	
	
	public String getDeviceCode() {
		return deviceCode;
	}
	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	public void setCustService(ICustService custService) {
		this.custService = custService;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public void setBuyMode(String buyMode) {
		this.buyMode = buyMode;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public void setStbId(String stbId) {
		this.stbId = stbId;
	}

	public void setRecords(String records) {
		this.records = records;
	}

	public String getRecords() {
		return records;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public void setValuableCardService(IValuableCardService valuableCardService) {
		this.valuableCardService = valuableCardService;
	}

	public String getQueryItem() {
		return queryItem;
	}

	public void setQueryItem(String queryItem) {
		this.queryItem = queryItem;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public void setNewModel(String newModel) {
		this.newModel = newModel;
	}

	public void setOldModel(String oldModel) {
		this.oldModel = oldModel;
	}

	

}
