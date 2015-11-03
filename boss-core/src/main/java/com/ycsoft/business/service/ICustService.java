package com.ycsoft.business.service;

import java.util.List;
import java.util.Map;

import com.ycsoft.beans.config.TDeviceBuyMode;
import com.ycsoft.beans.config.TNonresCustApproval;
import com.ycsoft.beans.core.cust.CCust;
import com.ycsoft.beans.core.cust.CCustAddr;
import com.ycsoft.beans.core.cust.CCustLinkman;
import com.ycsoft.beans.core.cust.CCustPropChange;
import com.ycsoft.beans.device.RDeviceFee;
import com.ycsoft.beans.device.RDeviceModel;
import com.ycsoft.beans.device.RStbModel;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.business.commons.abstracts.IBaseService;
import com.ycsoft.business.dto.core.cust.CustFullInfoDto;
import com.ycsoft.business.dto.core.fee.FeeInfoDto;
import com.ycsoft.business.dto.core.prod.CustProdDto;
import com.ycsoft.business.dto.device.DeviceDto;
import com.ycsoft.commons.exception.ComponentException;
import com.ycsoft.commons.exception.ServicesException;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;
import com.ycsoft.sysmanager.dto.resource.RDeviceModelTotalDto;

/**
 * @author YC-SOFT
 *
 */
public interface ICustService extends IBaseService{

	/**
	 * 查询产权变更的设备销售方式
	 * @return
	 * @throws Exception
	 */
	public List<TDeviceBuyMode> queryDeviceBuyModeByOwnership() throws Exception;
	
	public List<RDeviceModelTotalDto> queryDeviceModel()throws Exception;
	
	public List<TDeviceBuyMode> queryDeviceCanFee() throws Exception;

	/**
	 * 生成客户
	 * @param busiCode 业务编号
	 * @param cust     客户基本信息
	 * @param linkman  联系人信息
	 * @param custCode 
	 * @param resident 居民客户信息
	 * @throws Exception
	 *
	 */
	public void createCust(CCust cust, CCustLinkman linkman, String custCode) throws Exception;
	
	/**
	 * 批量预开户
	 * @param addrId	地址编号
	 * @param custName 
	 * @param addrName	地址名称
	 * @param custCount	客户数
	 */
	public void createCustBatch(String addrId,String address,String custName, int custCount,List<CCustAddr> addrList)throws Exception;

	/**
	 * 修改客户信息:移机、过户、修改客户资料等功能调用
	 * @param propChangeList	属性变化列表
	 * @throws Exception
	 */
	public void editCust(List<CCustPropChange> propChangeList) throws Exception;
	
	public void relocateCust(String custId) throws Exception;

	/**
	 * 客户销户
	 * @param banlanceDealType	余额处理方式:退款、不退款
	 * @throws Exception
	 */
	public void saveRemoveCust(String banlanceDealType)throws Exception;

	/**
	 * 购买设备
	 * @param deviceId		设备id
	 * @param cardId        卡号id
	 * @param modemMac      MODEM_MAC
	 * @param buyMode		购买方式
	 * @param feeId			费用id
	 * @param feeStdId 
	 * @param fee 
	 * @param feeValueId	费用值
	 * @param virtualCard	是否是虚拟卡
	 * @param virtualModem	是否是虚拟MODEM
	 * @throws Exception
	 */
	public void saveBuyDevice(String deviceId,String cardId,String modemMac,String buyMode,List<FeeInfoDto> feeInfoList,String virtualCard,String virtualModem) throws Exception;
	
	/**
	 * 给设备购买保修期.
	 * @param deviceId
	 * @throws Exception
	 */
	public void saveBuyReplacover(String deviceId,String deviceCode) throws Exception;
	
	/**
	 * 购买配件
	 * @param deviceType
	 * @param deviceModel
	 * @param buyMode
	 * @param feeId
	 * @param feeStdId
	 * @param fee
	 * @param buyNum	购买数量
	 * @throws Exception
	 */
	public void saveBuyMaterial(String deviceType,String deviceModel,String buyMode,List<FeeInfoDto> feeInfoList,int buyNum) throws Exception;
	/**
	 * 把客户名下产权属于广电的设备销售给客户
	 * @param deviceId
	 * @param buyMode
	 * @param feeId
	 * @param fee
	 * @throws Exception
	 */
	public void saveSaleDevice(String deviceId,List<FeeInfoDto> feeInfoList,String buyMode,String changeOwnship) throws Exception;
	
	
	public void saveChangeDeviceType(String deviceId,String buyMode) throws Exception;

	/**
	 * 设备回收
	 * @param deviceId		设备id
	 * @param deviceStatus	回收后的设备状态
	 * @param fee 
	 * @throws Exception
	 */
	public void saveReclaimDevice(String deviceId,String deviceStatus,String reclaimReason, int fee) throws Exception;

	/**
	 * 换MODEM
	 * @param oldModemId		原modem的设备code
	 * @param newModemId		新modem的设备code
	 * @param fee 
	 * @param modemZjFee		MODEM折旧费 
	 * @param feeStdId 
	 * @param feeId 
	 * @param buyMode 
	 * @param changeOwnership	是否变更产权
	 * @param deviceStatus 
	 * @throws Exception
	 */
	public void saveChangeModem(String oldModemId, String newModemId,
			String buyMode, List<FeeInfoDto> feeInfoList, int modemZjFee,
			boolean reclaim, String deviceStatus) throws Exception;

	/**
	 * 更换机顶盒和卡
	 * @param oldStbId 原stb的设备code
	 * @param oldCardId 原card的设备code
	 * @param oldModemId 原modem的设备code
	 * @param newStbId 新stb的设备code
	 * @param newCardId 新card的设备code
	 * @param newModemId 新modem的设备code
	 * @param buyMode 购买方式
	 * @param feeId 费用编号
	 * @param feeStdId 费用标准id
	 * @param fee 费用
	 * @param stbZjFee 机顶盒折旧费用
	 * @param cardZjFee 智能卡折旧费用
	 * @param modemZjFee MODEM折旧费用
	 * @param reclaim 是否回收
	 * @param deviceStatus 设备状态
	 * @param buyFlag 5:机卡猫全买；4：机卡；3：机猫；6：猫卡；1：单机；2：单卡；7：单猫；0：不用买
	 * @param singleCard 是否一体机
	 * @throws Exception
	 */
	public void saveChangeStbCard(String oldStbId, String oldCardId, String oldModemId,
			String newStbId, String newCardId, String newModemId, List<FeeInfoDto> feeInfoList, 
			int stbZjFee, int cardZjFee, int modemZjFee,String deviceStatus, int buyFlag,
			boolean singleCard,String changeReason) throws Exception;
	
	/**
	 * 机卡互换,客户之间可以互换,必须是有用户的设备(正在使用的)才可以互换。</br>
	 * 同客户互换：交换2个用户的机卡号，重发指令，保修期不变。</br>
	 * 不同客户互换：交换2个用户的机卡号，重发指令，保修期也同时互换</br>
	 * 猫的参数留待以后如果需求有变更使用.
	 * @param oldStbId  	旧机顶盒
	 * @param oldCardId		旧智能卡
	 * @param oldModemId 	旧猫
	 * @param newStbId		新机顶盒
	 * @param newCardId		新智能卡
	 * @param newModemId	新猫
	 * @throws Exception
	 */
	public void saveSwitchDevice(String oldStbId, String oldCardId, String oldModemId,
			String newStbId, String newCardId, String newModemId) throws Exception;
	
	/**
	 * 设备挂失
	 * @param deviceId
	 * @throws Exception
	 */
	public void saveRegLossDevcie(String deviceId) throws Exception;

	/**
	 * 取消挂失
	 * @param deviceId
	 * @throws Exception
	 */
	public void saveCancelLossDevcie(String deviceId) throws Exception;
	/**
	 * 客户加入单位
	 * @param unitId 加入的单位客户id
	 * @throws Exception
	 */
	public void saveCustJoinUnit(String unitId) throws Exception;
	
	
	/**
	 * 暂停卡扣
	 * @throws Exception
	 */
	public void saveBankStop()throws Exception;
	
	
	/**
	 * 恢复卡扣
	 * @throws Exception
	 */
	public void saveBankResume()throws Exception;
	
	/**
	 * 客户加入单位
	 * @param custIds 被加入的客户id
	 * @throws Exception
	 */
	public void saveCustJoinUnit( String[] custIds)
			throws Exception;

	/**
	 * 客户退出单位
	 * @param unitId
	 * @throws Exception
	 */
	public void saveCustQuitUnit(String[] unitId) throws Exception;
	
	/**
	 * 居民转非居民
	 * @param nonresCust	非居民客户(不存在则新建)
	 * @param linkman		居民客户
	 * @throws Exception
	 */
	public void changeNonresCust(CCust nonresCust, CCustLinkman linkman) throws Exception;
	
	/**
	 * 客户迁移.
	 * @param nonresCust
	 * @param linkman
	 * @throws ServicesException
	 */
	public void transferCust(CCust nonresCust) throws ServicesException;
	
	/**
	 * 根据设备编号查询设备信息及客户信息
	 * @param deviceCode
	 * @return
	 * @throws Exception
	 */
	public com.ycsoft.sysmanager.dto.resource.DeviceDto queryDeviceInfoByCode(String deviceCode) 
	throws Exception;
	
	/**
	 * 根据设备类型查找设备型号信息
	 * @param deivceId
	 * @param deviceType
	 * @return
	 * @throws Exception
	 */
	public RDeviceModel queryDeviceModelByDeviceType(String deivceId,String deviceType) throws Exception;

	/**
	 * 根据设备编号查找可以销售的设备信息
	 * 如果设备不存在，或者设备不能用于销售，则抛出异常
	 *
	 * 如果设备为配对的卡，返回为配对的机顶盒
	 * @param deviceCode
	 * @return
	 * @throws Exception
	 */
	public DeviceDto querySaleableDevice(String deviceCode) throws Exception;
	/**
	 * 返回最后一个设备的型号
	 * @param deviceCode
	 * @return
	 * @throws Exception
	 */
	public DeviceDto querySaleableDeviceArea(String deviceCodes,String userType) throws Exception;
	
	public DeviceDto queryChangeDevice(String userType, String deviceCode) throws Exception;
	
	/**
	 * 根据设备编号查可以进行机卡互换的设备,该设备只能是已经在使用的。不能为空闲的.
	 * @param deviceCode
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> queryDeviceForSwitch(String deviceCode,String deviceType, String custId) throws Exception;

	/**
	 * 查询可以单独购买的智能卡
	 * @return
	 * @throws Exception
	 */
	public DeviceDto querySaleableCard(String deviceCode) throws Exception;
	
	/**
	 * 查询可以单独购买的MODEM
	 * @return
	 * @throws Exception
	 */
	public DeviceDto querySaleableModem(String deviceCode) throws Exception;
	
	/**
	 * 根据设备编号查找设备信息
	 * 如果设备不存在，则抛出异常
	 *
	 * 如果设备为配对的卡，返回为配对的机顶盒
	 * @param deviceCode
	 * @return
	 * @throws Exception
	 */
	public DeviceDto queryDevice(String deviceCode) throws Exception;
	/**
	 * 根据设备号,查询用户下另一个设备的编号
	 * deviceType 为STB ,返回用户下的卡
	 * deviceType 为CARD ,返回用户下的机
	 * @param deviceCode
	 * @return
	 * @throws Exception
	 */
	public DeviceDto queryUserAnotherDevice(String deviceType,String deviceCode) throws Exception;

	/**
	 * 根据设备编号查找可以被当前用户使用的设备
	 * 如果设备不存在，或者设备不能用于使用（开户、更换)，则抛出异常
	 * @param custId
	 * @param deviceCode
	 * @return
	 * @throws Exception
	 */
	public DeviceDto queryUseableDevice(String custId,String deviceType, String deviceCode,String userType) throws Exception;

	/**
	 * 根据设备型号和销售方式查找销售费用信息
	 * @param deviceModel
	 * @param buyMode
	 * @return
	 */
	public List<RDeviceFee> queryDeviceFee(String deviceType,String deviceModel,String buyMode)throws Exception;

	/**
	 * 查询指定客户下 设备的购买方式
	 * @param custId
	 * @param deviceCode
	 * @return
	 */
	public List<TDeviceBuyMode> queryDeviceBuyMode()
			throws Exception;

	/**
	 * 根据客户编号、客户类型、和套餐编号查找客户名下可以
	 * @param custId
	 * @param custType
	 * @param pkgId
	 * @return
	 * @throws Exception
	 */
	public List<CustProdDto> queryCustProdForPkg(String custId,String custType,String pkgId, String pkgTarrifId) throws Exception;

	public List<CustProdDto> queryProdsOfPkg(String custId,String pkgId ) throws Exception;
	
	/**
	 * 查询机顶盒设备类型,验证是否双向
	 * @param stbId
	 * @return
	 * @throws Exception
	 */
	public RStbModel queryStbModel(String stbId) throws Exception;
	
	
	/**批量明细修改地址
	 * @param custAddrList
	 * @param busiCode
	 * @throws Exception
	 */
	public void updateAddressList(List<CCust> custAddrList,List<CCust> custLinkmanList,String busiCode) throws Exception;
	
	public void updateAddressList(CCust cust,String newAddrId ,String newAddress, String old_addr_id,String busiCode) throws Exception;
	

	/**A小区名下的客户移入B小区下
	 * @param oldAddrId
	 * @param newAddrId
	 * @param optr
	 * @throws Exception
	 */
	public String updateCustAddress(String oldAddrId,String newAddrId,SOptr optr) throws Exception;
	
	/**
	 * 修改设备产权
	 * @param deviceId
	 */
	public void saveChangeOwnership(String deviceId)throws Exception;
	
	
	/**
	 * 仓库管理调用回收设备
	 * @param deviceId
	 * @param deviceStatus
	 * @throws JDBCException
	 * @throws Exception
	 * @throws ComponentException
	 */
	public void saveCancelDevice(String deviceId, String deviceStatus)
	throws JDBCException, Exception, ComponentException;
	
	/**
	 * 批量修改客户状态
	 * @param custNos
	 * @param custStatus
	 * @throws Exception
	 */
	public void updateCustStatus(List<String> custNos,String custStatus) throws Exception;
	
	/**
	 * 恢复客户状态 从资料隔离到正常
	 * @param custId
	 * @throws Exception
	 */
	public String renewCust() throws Exception;

	/**
	 * 调用存储过程，批量销客户
	 * @param custIdList
	 * @param isReclaimDevice
	 * @param deviceStatus
	 * @param remark
	 */
	public void batchLogoffCust(List<String> custIdList, String isReclaimDevice, String deviceStatus, String remark) throws Exception;
	
	public Pager<TNonresCustApproval> queryNonresCustApp(Integer start,Integer limit) throws Exception;

	/**
	 * 恢复已销户的客户.恢复客户信息,客户联系人,客户地址.
	 * @param custId
	 */
	public CustFullInfoDto restoeCust(String custId) throws ServicesException;

	/**
	 * @param userId
	 * @param prodId
	 * @param tariffId
	 * @param tariffStartDate
	 * @param custClass
	 * @param custClassDate
	 */
	public void editCustClass(String userId, String prodId, String tariffId,
			String tariffStartDate, String expDate, String custClass, String custClassDate)throws Exception;

	/**
	 * @param deviceType
	 * @param oldModel
	 * @param newModel
	 * @returnR
	 */
	public RDeviceFee queryFeeByModel(String deviceType, String oldModel,String newModel) throws Exception;

	public List<RDeviceModelTotalDto> queryDeviceCanBuy(SOptr optr) throws Exception;

	public void saveBacthBuyMaterial(List<RDeviceModelTotalDto> feeInfoList) throws Exception;

	public void editCustLevel(String parameter) throws Exception;

	public void saveBugTask(String bugDetail, String bugPhone)throws Exception;

}
