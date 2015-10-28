package com.ycsoft.web.action.core;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ycsoft.beans.core.cust.CCust;
import com.ycsoft.beans.core.cust.CCustAddr;
import com.ycsoft.beans.core.cust.CCustLinkman;
import com.ycsoft.beans.core.cust.CCustPropChange;
import com.ycsoft.business.dto.config.ChangeValueDto;
import com.ycsoft.business.dto.core.cust.CustFullInfoDto;
import com.ycsoft.business.dto.core.fee.FeeInfoDto;
import com.ycsoft.business.dto.device.DeviceDto;
import com.ycsoft.business.service.ICustService;
import com.ycsoft.commons.constants.BusiCodeConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ServicesException;
import com.ycsoft.commons.helper.DateHelper;
import com.ycsoft.commons.helper.FileHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.sysmanager.dto.resource.RDeviceModelTotalDto;
import com.ycsoft.web.commons.abstracts.BaseBusiAction;

/**
 * 客户核心控制器
 *
 * @author hh
 * @data Mar 17, 2010 11:24:55 AM
 */
@Controller
public class CustAction extends BaseBusiAction{

	/**
	 *
	 */
	private static final long serialVersionUID = -1981343279974076065L;
	private ICustService custService;
	private CCust cust;
	private CCustLinkman linkman;
	private String buyMode;
	private String deviceId;
	private String device_code;
	private String feeId;
	private int fee;
	private String custId;
	private String custName;
	private String custType;
	private String pkgId;
	private String feeStdId;

	private String custChangeInfo;
	private String banlanceDealType;

	private String cardId;
	private String modemMac;
	
	private String unitId;
	private String mnCustId;
	
	private String addrId;
	private String address;
	
	private int buyFlag;
	
	private String custIds;
	
	private String addrListStr;
	private String new_addr_id;
	private String new_address;
	private String old_addr_text;
	private File file;
	private String custNo;
	private String custStatus;
	
	private String virtualCard;
	private String virtualModem;
	private String custCode;
	
	private FeeInfoDto deviceFee;

	/**
	 * 开户
	 * @return
	 * @throws Exception
	 */
	public String createCust()throws Exception{
		custService.createCust(cust, linkman,custCode);
		getRoot().setSimpleObj(cust);
		return JSON;
	}
	
	/**
	 * 批量开户
	 * @param addrId 小区编号
	 * @param custNum 开户数
	 * @return
	 * @throws Exception
	 */
	public String batchNewCust() throws Exception{
//		custService.createCustBatch(addrId, address,custName,custNum);
		Integer custNum = Integer.parseInt(request.getParameter("custNum"));
		String cAddrId = request.getParameter("addrId");
		String address = request.getParameter("address");
		String custName = request.getParameter("custName");
		String msg = "";
		List<CCustAddr> addrList = new ArrayList<CCustAddr>();
		if(file != null){
			addrList = FileHelper.fileToBean(file, new String[]{"t1","t2","t3","t4","t5","note"}, CCustAddr.class);
		}
		try{
			if(file != null){
//				addrList.remove(0);
				if(addrList.size() != custNum)
					throw new Exception("文件中地址数量必须等于开户数量!");
				if(addrList.size() > 2000)
					throw new Exception("请一次性录入小于2000条数据");
			}
			custService.createCustBatch(cAddrId, address,custName,custNum,addrList);
		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getMessage();
		}
		
		return retrunNone(msg);
		
//		return JSON;
	}

	/**
	 * 客户销户
	 * @return
	 * @throws Exception
	 */
	public String logoffCust() throws Exception{
		custService.saveRemoveCust(banlanceDealType);
		return JSON_SUCCESS;
	}
	
	/**
	 * 恢复销户客户
	 * @return
	 * @throws Exception
	 */
	public String restoeCust() throws Exception{
		CustFullInfoDto custInfo = custService.restoeCust(custId);
		Map<String, Object> others = new HashMap<String, Object>();
		others.put("success", true);
		others.put("custInfo", custInfo);
		getRoot().setOthers(others);
		return JSON_OTHER;
	}

	/**
	 * 调用存储过程，批量销客户
	 * @return
	 * @throws Exception
	 */
	public String batchLogoffCust() throws Exception{
		String remark = request.getParameter("remark");
		String isReclaimDevice = request.getParameter("isReclaimDevice");
		String deviceStatus = request.getParameter("deviceStatus");
		
		String msg = "";
		List<String> custIdList = new ArrayList<String>();
		if(file != null){
			custIdList = FileHelper.fileToArray(file);
		}
		try{
			if(custIdList.size() > 2000)
				throw new Exception("请一次性录入小于2000条数据");
			custService.batchLogoffCust(custIdList,isReclaimDevice,deviceStatus,remark);
		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getMessage();
		}
		
		return retrunNone(msg);
	}
	
	/**
	 * 修改客户资料
	 * @return
	 * @throws Exception
	 */
	public String editCust() throws Exception{
		Type type = new TypeToken<List<ChangeValueDto>>(){}.getType();
		Gson gson = new Gson();
		List<ChangeValueDto> changeValueList = gson.fromJson(custChangeInfo, type);
		List<CCustPropChange> propChangeList = new ArrayList<CCustPropChange>();
		//如果有信息发生了修改
		if (changeValueList != null && changeValueList.size()>0){
			for (ChangeValueDto dto:changeValueList){
				CCustPropChange propChange = new CCustPropChange();
				propChange.setColumn_name(dto.getColumnName().substring(dto.getColumnName().indexOf(".")+1));
				propChange.setOld_value(dto.getOldValue());
				propChange.setNew_value(dto.getNewValue());
				propChangeList.add(propChange);
			}
		}
		custService.editCust(propChangeList);
		return JSON;
	}
	
	/**
	 * 修改优惠类型
	 * @return
	 * @throws Exception
	 */
	public String editCustClass() throws Exception{
		String userId = request.getParameter("user_id");
		String prodSn = request.getParameter("prod_sn");
		String tariffId = request.getParameter("tariff_id");
		String expDateStr = request.getParameter("expDate");
		String tariffStartDate = request.getParameter("tariffStartDate");
		String custClass = request.getParameter("custClass");
		String custClassDate = DateHelper.dateToStr(DateHelper.parseDate(request.getParameter("custClassDate"),"yyyy-MM-dd"));
		custService.editCustClass(userId, prodSn, tariffId, tariffStartDate, expDateStr, custClass, custClassDate);
		return JSON;
	}
	
	/**
	 * 修改优惠类型
	 * @return
	 * @throws Exception
	 */
	public String editCustLevel() throws Exception{
		custService.editCustLevel(request.getParameter("cust_level"));
		return JSON;
	}
	
	public String changeNonresCust() throws Exception {
		custService.changeNonresCust(cust, linkman);
		return JSON;
	}
	
	/**
	 * 客户迁移.
	 * @return
	 * @throws Exception
	 */
	public String transferCust() throws Exception{
		custService.transferCust(cust);
		return JSON;
	}
	
	/**
	 * 客户拆迁
	 * @return
	 * @throws Exception
	 */
	public String relocateCust() throws Exception{
		custService.relocateCust(custId);
		return JSON;
	}
	
	/**
	 * 居民客户加入单位
	 * @return
	 * @throws Exception
	 */
	public String jionUnit()throws Exception{
		custService.saveCustJoinUnit(unitId);
		return JSON;
	}
	
	/**
	 * 批量加入单位
	 * @return
	 * @throws Exception
	 */
	public String batchJoinUnit() throws Exception{
		custService.saveCustJoinUnit(custIds.split(","));
		return JSON;
	}

	/**
	 * 居民客户退出单位
	 * @return
	 * @throws Exception
	 */
	public String quitUnit() throws Exception{
		custService.saveCustQuitUnit(unitId.split(","));
		return JSON;
	}
	
	/**
	 * 客户购买设备终端
	 * @return
	 * @throws Exception
	 */
	public String buyDevice() throws Exception{
		String feeInfo = request.getParameter("feeInfo");
		List<FeeInfoDto> feeInfoList = new ArrayList<FeeInfoDto>();
		if(StringHelper.isNotEmpty(feeInfo)){
			Type type = new TypeToken<List<FeeInfoDto>>(){}.getType();
			Gson gson = new Gson();
			feeInfoList = gson.fromJson(feeInfo, type);
		}
		custService.saveBuyDevice(deviceId,cardId,modemMac, buyMode, feeInfoList,virtualCard,virtualModem);
		return JSON;
	}
	
	public String buyReplacover() throws Exception{
		custService.saveBuyReplacover(deviceId,device_code);
		return JSON_SUCCESS;
	}
	
	/**
	 * 修改设备产权
	 * @return
	 * @throws Exception
	 */
	public String changeOwnership() throws Exception{
		custService.saveChangeOwnership(deviceId);
		return JSON;
	}
	
	/**
	 * 客户购买设备配件
	 * @return
	 * @throws Exception
	 */
	public String buyMaterial() throws Exception {
		
		String deviceType = request.getParameter("deviceType");
		String deviceModel = request.getParameter("deviceModel");
		String feeInfo = request.getParameter("feeInfo");
		int buyNum = Integer.parseInt(request.getParameter("buyNum"));
		List<FeeInfoDto> feeInfoList = new ArrayList<FeeInfoDto>();
		if(StringHelper.isNotEmpty(feeInfo)){
			Type type = new TypeToken<List<FeeInfoDto>>(){}.getType();
			Gson gson = new Gson();
			feeInfoList = gson.fromJson(feeInfo, type);
		}
		
		custService.saveBuyMaterial(deviceType, deviceModel, buyMode, feeInfoList,buyNum);
		return JSON;
	}
	
	
	/**
	 * 批量购买器材
	 * @return
	 * @throws Exception
	 */
	public String bacthBuyMaterial() throws Exception {
		String feeInfo = request.getParameter("feeInfo");
		List<RDeviceModelTotalDto> feeInfoList = new ArrayList<RDeviceModelTotalDto>();
		if(StringHelper.isNotEmpty(feeInfo)){
			Type type = new TypeToken<List<RDeviceModelTotalDto>>(){}.getType();
			Gson gson = new Gson();
			feeInfoList = gson.fromJson(feeInfo, type);
		}
		
		custService.saveBacthBuyMaterial(feeInfoList);
		return JSON;
	}

	/**
	 * 设备挂失
	 * @return
	 * @throws Exception
	 */
	public String saveRegLossDevcie() throws Exception {
		custService.saveRegLossDevcie(deviceId);
		return JSON;
	}

	/**
	 * 取消挂失
	 * @return
	 * @throws Exception
	 */
	public String saveCancelLossDevcie() throws Exception {
		custService.saveCancelLossDevcie(deviceId);
		return JSON;
	}

	/**
	 * 销售设备
	 * @return
	 * @throws Exception
	 */
	public String saveSaleDevice() throws Exception{
		String feeInfo = request.getParameter("feeInfo");
		List<FeeInfoDto> feeInfoList = new ArrayList<FeeInfoDto>();
		if(StringHelper.isNotEmpty(feeInfo)){
			Type type = new TypeToken<List<FeeInfoDto>>(){}.getType();
			Gson gson = new Gson();
			feeInfoList = gson.fromJson(feeInfo, type);
		}
		
		String changeOwnship = request.getParameter("changeOwnship");
		custService.saveSaleDevice(deviceId, feeInfoList,buyMode,changeOwnship);
		return JSON;
	}
	public String saveChangeDeviceType() throws Exception{
		custService.saveChangeDeviceType(deviceId, buyMode);
		return JSON;
	}

	/**
	 * 机顶盒和卡号互换.可以是 客户 之间 也可以客户自己的.
	 * @param reclaimDevice 是否回收设备
	 * @param deviceStatus 回收设备状态 
	 * @param buyFlag 3:机卡都需购买；2：卡需购买；1：机需购买
	 * @param deviceBuyMode 购买方式
	 * @param feeId
	 * @param feeStdId
	 * @return
	 * @throws Exception
	 */
	public String switchDevice() throws Exception{
		String oldStbId = request.getParameter("oldStbCode");
		String oldCardId = request.getParameter("oldCardCode");
		String newStbId = request.getParameter("newStbCode");
		String newCardId = request.getParameter("newCardCode");
		String oldModemId = request.getParameter("oldModemCode");
		String newModemId = request.getParameter("newModemCode");
		
		custService.saveSwitchDevice(oldStbId, oldCardId, oldModemId, newStbId, newCardId, newModemId);
		return JSON;
	}
	
	/**
	 * 更换机顶盒和卡号
	 * @param reclaimDevice 是否回收设备
	 * @param deviceStatus 回收设备状态 
	 * @param buyFlag 3:机卡都需购买；2：卡需购买；1：机需购买
	 * @param deviceBuyMode 购买方式
	 * @param feeId
	 * @param feeStdId
	 * @return
	 * @throws Exception
	 */
	public String changeStbCard() throws Exception{
		String oldStbId = request.getParameter("oldStbCode");
		String oldCardId = request.getParameter("oldCardCode");
		String newStbId = request.getParameter("newStbCode");
		String newCardId = request.getParameter("newCardCode");
		String oldModemId = request.getParameter("oldModemCode");
		String newModemId = request.getParameter("newModemCode");
		String changeReason  = request.getParameter("change_reason");
		String stbZjFee = request.getParameter("stbZjFee");
		if(StringHelper.isEmpty(stbZjFee))
			stbZjFee = "0";
		String cardZjFee = request.getParameter("cardZjFee");
		if(StringHelper.isEmpty(cardZjFee))
			cardZjFee = "0";
		String modemZjFee = request.getParameter("modemZjFee");
		if(StringHelper.isEmpty(modemZjFee))
			modemZjFee = "0";
		boolean singleCard = false;
		String deviceStatus = request.getParameter("deviceStatus");
		
		String feeInfo = request.getParameter("feeInfo");
		List<FeeInfoDto> feeInfoList = new ArrayList<FeeInfoDto>();
		if(StringHelper.isNotEmpty(feeInfo)){
			Type type = new TypeToken<List<FeeInfoDto>>(){}.getType();
			Gson gson = new Gson();
			feeInfoList = gson.fromJson(feeInfo, type);
		}
		
		custService.saveChangeStbCard(oldStbId, oldCardId, oldModemId, newStbId, newCardId, newModemId,
				feeInfoList, Integer.parseInt(stbZjFee),
				Integer.parseInt(cardZjFee),Integer.parseInt(modemZjFee), deviceStatus, buyFlag,
				singleCard,changeReason);
		return JSON;
	}

	/**
	 * 更换猫
	 * @return
	 * @throws Exception
	 */
	public String changeModem() throws Exception{
		
		String oldModemId = request.getParameter("oldModemCode");
		String newModemId = request.getParameter("newModemCode");
		boolean reclaim = SystemConstants.BOOLEAN_TRUE.equals(request.getParameter("reclaimDevice"))?true:false;
		String deviceStatus = request.getParameter("deviceStatus");
		String modemZjFee = request.getParameter("modemZjFee");
		if(StringHelper.isEmpty(modemZjFee))
			modemZjFee = "0";

		String feeInfo = request.getParameter("feeInfo");
		List<FeeInfoDto> feeInfoList = new ArrayList<FeeInfoDto>();
		if(StringHelper.isNotEmpty(feeInfo)){
			Type type = new TypeToken<List<FeeInfoDto>>(){}.getType();
			Gson gson = new Gson();
			feeInfoList = gson.fromJson(feeInfo, type);
		}
		custService.saveChangeModem(oldModemId, newModemId,
				buyMode, feeInfoList, Integer.parseInt(modemZjFee),reclaim,deviceStatus );
		return JSON;
	}
	
	/**
	 * 根据device_code查询设备是否可用
	 * @return
	 * @throws Exception
	 */
	public String queryUseableDevice() throws Exception{
		String deviceCode = request.getParameter("deviceCode");
		String custId = request.getParameter("custId");
		String deviceType = request.getParameter("deviceType");
		String userType = request.getParameter("userType");
		getRoot().setSimpleObj(custService.queryUseableDevice(custId,deviceType,deviceCode,userType));
		return JSON_SIMPLEOBJ;
	}

	/**
	 * 根据机顶盒号查询卡号 或 根据卡号查询机顶盒号
	 * @return
	 * @throws Exception
	 */
	public String queryUserAnotherDevice() throws Exception{
		String deviceType = request.getParameter("deviceType");
		String deviceCode = request.getParameter("deviceCode");
		DeviceDto dto= custService.queryUserAnotherDevice(deviceType,deviceCode);
		getRoot().setSimpleObj(dto);
		return JSON_SIMPLEOBJ;
	}

	/**
	 * 客户回收设备
	 * @return
	 * @throws Exception
	 */
	public String reclaimDevice() throws  Exception{
		String deviceId = request.getParameter("deviceId");
		String deviceStatus = request.getParameter("deviceStatus");
		String deviceFeeValue = request.getParameter("deviceFeeValue");
		String reclaimReason = request.getParameter("reclaimReason");
		if (StringHelper.isEmpty(deviceFeeValue))
			deviceFeeValue="0";
		custService.saveReclaimDevice(deviceId,deviceStatus,reclaimReason,Integer.parseInt(deviceFeeValue));
		return JSON;
	}

	/**
	 * 查找可以加入套餐的客户名下的产品
	 * @param custService
	 */
	public String queryProdForPkg() throws Exception{
		String pkgTarrifId = request.getParameter("pkgTarrifId");
		getRoot().setRecords(custService.queryCustProdForPkg(custId, custType, pkgId,pkgTarrifId));
		return JSON_RECORDS;
	}

	/**
	 * 查询套餐已选中的产品
	 * @return
	 * @throws Exception
	 */
	public String queryProdsOfPkg() throws Exception {
		getRoot().setRecords(custService.queryProdsOfPkg(custId, pkgId));
		return JSON_RECORDS;
	}

	/**
	 * 明细修改地址
	 * @return
	 * @throws Exception
	 */
	public String updateAddressList() throws Exception{
		List<CCust> custAddrList = new ArrayList<CCust>();
		if(StringHelper.isNotEmpty(addrListStr)){
			Type type = new TypeToken<List<CCust>>(){}.getType();
			Gson gson = new Gson();
			custAddrList = gson.fromJson(addrListStr, type);
			if(custAddrList == null){
				throw new ServicesException("客户不存在");
			}
			custService.updateAddressList(custAddrList,null,BusiCodeConstants.ADDRESS_UPDATE_SOME);
		}else{
			custService.updateAddressList(cust, new_addr_id, new_address, old_addr_text,BusiCodeConstants.ADDRESS_UPDATE_SOME);
		}
		return JSON;
	}
	
	/**
	 * 批量更换小区
	 * @return
	 * @throws Exception
	 */
	public String updateCustAddress() throws Exception{
		String oldAddrId = request.getParameter("oldAddrId");
		String newAddrId = request.getParameter("newAddrId");
		getRoot().setSimpleObj(custService.updateCustAddress(oldAddrId,newAddrId,optr));
		return JSON;
	}
	
	/**
	 * 批量修改客户状态
	 * @return
	 * @throws Exception
	 */
	public String updateCustStatus() throws Exception {
		String msg = "";
		List<String> custNos = null;
		try {
			if(file != null){
				custNos = FileHelper.fileToArray(file);
			}
			if(custNos != null){
				if(StringHelper.isNotEmpty(custNo) && !custNos.contains(custNo)){
					//单个需修改的客户编号不为空，加入导入的客户编号集合
					custNos.add(custNo);
				}
			}else{
				//导入文件和单个录入客户编号，二者至少输入其一
				custNos = new ArrayList<String>();
				custNos.add(custNo);
			}
			custService.updateCustStatus(custNos, custStatus);
		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getMessage();
		}
		return retrunNone(msg);
	}
	
	public String renewCust() throws Exception {
		getRoot().setSimpleObj(custService.renewCust());
		return JSON;
	}
	
	public String queryNonresCustApp() throws Exception{
		getRoot().setPage(custService.queryNonresCustApp(start, limit));
		return JSON_PAGE;
	}
	
	public String saveBugTask() throws Exception{
		String bugDetail = request.getParameter("bugDetail");
		String bugPhone = request.getParameter("bugPhone");
		custService.saveBugTask(bugDetail, bugPhone);
		return JSON_SUCCESS;
	}
	
	
	public void setCustService(ICustService custService) {
		this.custService = custService;
	}
	public CCust getCust() {
		return cust;
	}
	public void setCust(CCust cust) {
		this.cust = cust;
	}
	public void setLinkman(CCustLinkman linkman) {
		this.linkman = linkman;
	}
	public CCustLinkman getLinkman() {
		return linkman;
	}

	public void setBuyMode(String buyMode) {
		this.buyMode = buyMode;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public void setFeeId(String feeId) {
		this.feeId = feeId;
	}

	public void setFee(int fee) {
		this.fee = fee;
	}

	public String getBanlanceDealType() {
		return banlanceDealType;
	}

	public void setBanlanceDealType(String banlanceDealType) {
		this.banlanceDealType = banlanceDealType;
	}

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public String getCustType() {
		return custType;
	}

	public void setCustType(String custType) {
		this.custType = custType;
	}

	public String getPkgId() {
		return pkgId;
	}

	public void setPkgId(String pkgId) {
		this.pkgId = pkgId;
	}

	public void setCustChangeInfo(String custChangeInfo) {
		this.custChangeInfo = custChangeInfo;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public void setFeeStdId(String feeStdId) {
		this.feeStdId = feeStdId;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getAddrId() {
		return addrId;
	}

	public void setAddrId(String addrId) {
		this.addrId = addrId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setBuyFlag(int buyFlag) {
		this.buyFlag = buyFlag;
	}

	public void setCustIds(String custIds) {
		this.custIds = custIds;
	}

	/**
	 * @return the custName
	 */
	public String getCustName() {
		return custName;
	}

	/**
	 * @param custName the custName to set
	 */
	public void setCustName(String custName) {
		this.custName = custName;
	}

	public void setAddrListStr(String addrListStr) {
		this.addrListStr = addrListStr;
	}

	public void setMnCustId(String mnCustId) {
		this.mnCustId = mnCustId;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public void setCustNo(String custNo) {
		this.custNo = custNo;
	}

	public void setCustStatus(String custStatus) {
		this.custStatus = custStatus;
	}

	public void setModemMac(String modemMac) {
		this.modemMac = modemMac;
	}

	public void setVirtualCard(String virtualCard) {
		this.virtualCard = virtualCard;
	}

	public void setVirtualModem(String virtualModem) {
		this.virtualModem = virtualModem;
	}

	public void setNew_addr_id(String new_addr_id) {
		this.new_addr_id = new_addr_id;
	}

	public void setOld_addr_text(String old_addr_text) {
		this.old_addr_text = old_addr_text;
	}

	public void setNew_address(String new_address) {
		this.new_address = new_address;
	}

	public String getDevice_code() {
		return device_code;
	}

	public void setDevice_code(String device_code) {
		this.device_code = device_code;
	}

	public void setCustCode(String custCode) {
		this.custCode = custCode;
	}

	public FeeInfoDto getDeviceFee() {
		return deviceFee;
	}

	public void setDeviceFee(FeeInfoDto deviceFee) {
		this.deviceFee = deviceFee;
	}
	
}
