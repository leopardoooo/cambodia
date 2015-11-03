package com.ycsoft.business.component.resource;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.reflect.TypeToken;
import com.ycsoft.beans.config.TBusiFeeDevice;
import com.ycsoft.beans.config.TDeviceBuyMode;
import com.ycsoft.beans.core.cust.CCustDevice;
import com.ycsoft.beans.core.fee.CFeeDevice;
import com.ycsoft.beans.core.valuable.CValuableCard;
import com.ycsoft.beans.core.valuable.CValuableCardFee;
import com.ycsoft.beans.core.valuable.CValuableCardHis;
import com.ycsoft.beans.depot.RDepotDefine;
import com.ycsoft.beans.device.RCard;
import com.ycsoft.beans.device.RDevice;
import com.ycsoft.beans.device.RDeviceFee;
import com.ycsoft.beans.device.RDeviceModel;
import com.ycsoft.beans.device.RDeviceReclaim;
import com.ycsoft.beans.device.RDeviceUseRecords;
import com.ycsoft.beans.device.RModem;
import com.ycsoft.beans.device.RStb;
import com.ycsoft.beans.device.RStbModel;
import com.ycsoft.beans.system.SDept;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.business.commons.abstracts.BaseBusiComponent;
import com.ycsoft.business.dao.config.TBusiFeeDeviceDao;
import com.ycsoft.business.dao.config.TDeviceBuyModeDao;
import com.ycsoft.business.dao.core.cust.CCustDeviceDao;
import com.ycsoft.business.dao.core.cust.CCustDeviceHisDao;
import com.ycsoft.business.dao.core.valuable.CValuableCardDao;
import com.ycsoft.business.dao.core.valuable.CValuableCardFeeDao;
import com.ycsoft.business.dao.core.valuable.CValuableCardHisDao;
import com.ycsoft.business.dao.resource.device.RCardDao;
import com.ycsoft.business.dao.resource.device.RCardModelDao;
import com.ycsoft.business.dao.resource.device.RDepotDefineDao;
import com.ycsoft.business.dao.resource.device.RDeviceChangeDao;
import com.ycsoft.business.dao.resource.device.RDeviceDao;
import com.ycsoft.business.dao.resource.device.RDeviceFeeDao;
import com.ycsoft.business.dao.resource.device.RDeviceModelDao;
import com.ycsoft.business.dao.resource.device.RDeviceReclaimDao;
import com.ycsoft.business.dao.resource.device.RDeviceUseRecordsDao;
import com.ycsoft.business.dao.resource.device.RModemDao;
import com.ycsoft.business.dao.resource.device.RModemModelDao;
import com.ycsoft.business.dao.resource.device.RPairCfgDao;
import com.ycsoft.business.dao.resource.device.RStbDao;
import com.ycsoft.business.dao.resource.device.RStbModelDao;
import com.ycsoft.business.dao.system.SDeptDao;
import com.ycsoft.business.dto.config.TemplateConfigDto;
import com.ycsoft.business.dto.device.DeviceDto;
import com.ycsoft.business.dto.device.DeviceSmallDto;
import com.ycsoft.business.dto.device.ValuableCardDto;
import com.ycsoft.commons.constants.DataRight;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ComponentException;
import com.ycsoft.commons.exception.ErrorCode;
import com.ycsoft.commons.exception.ServicesException;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.commons.helper.DateHelper;
import com.ycsoft.commons.helper.JsonHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;
import com.ycsoft.sysmanager.dto.resource.RDeviceModelTotalDto;

/**
 * 设备组件
 *
 * @author YC-SOFT
 *
 */
@Component
public class DeviceComponent extends BaseBusiComponent {
	private RDeviceChangeDao rDeviceChangeDao;
	private RDeviceDao rDeviceDao;
	private RStbDao rStbDao;
	private RCardDao rCardDao;
	private RModemDao rModemDao;
	private RStbModelDao rStbModelDao;
	private RCardModelDao rCardModelDao;
	private RModemModelDao rModemModelDao;
	private RDeviceFeeDao rDeviceFeeDao;
	private RDepotDefineDao rDepotDefineDao;
	private TDeviceBuyModeDao tDeviceBuyModeDao;
	private SDeptDao sDeptDao;
	private CValuableCardDao cValuableCardDao;
	private CValuableCardHisDao cValuableCardHisDao;
	private CValuableCardFeeDao cValuableCardFeeDao;
	private RDeviceReclaimDao rDeviceReclaimDao;
	private RPairCfgDao rPairCfgDao;
	private RDeviceUseRecordsDao rDeviceUseRecordsDao;
	private TBusiFeeDeviceDao tBusiFeeDeviceDao;
	private CCustDeviceDao cCustDeviceDao;
	private RDeviceModelDao rDeviceModelDao;
	@Autowired
	private CCustDeviceHisDao cCustDeviceHisDao;
	/**
	 * 根据设备编号查询设备及客户信息
	 * @param deviceCode
	 * @return
	 * @throws Exception
	 */
	public com.ycsoft.sysmanager.dto.resource.DeviceDto queryDeviceInfoByCode(String deviceCode) 
		throws Exception {
		return rDeviceDao.queryDeviceInfoByCode(deviceCode);
	}
	
	
	public DeviceDto queryDeviceInfoByCodeAndModel(String deviceCode,String models ) throws Exception {
		if(StringHelper.isEmpty(models)){
			throw new ComponentException(ErrorCode.TaskDeviceModelIsNull);
		}
		DeviceDto dto =  rDeviceDao.queryDeviceInfoByCodeAndModel(deviceCode);
		isDeviceSaleable(dto);
//		String[] modesArr = models.split(",");
//		boolean isModel = false;
//		for(int i=0;i<modesArr.length;i++){
//			if(modesArr[i].equals(dto.getDevice_model())){
//				isModel = true;
//				break;
//			}
//		}
//		if(!models.equals(dto.getDevice_model())){
//			throw new ComponentException(ErrorCode.TaskDeviceModelIsWrong,dto.getDevice_model_text());
//		}
		return dto;
	}

	/**
	 * 修改设备的仓库状态
 	 * @param doneCode 业务流水
	 * @param busiCode 业务代码
	 * @param deviceId 设备id
	 * @param status   修改的仓库状态
	 */
	public void updateDeviceDepotStatus(Integer doneCode, String busiCode,
			String deviceId, String oldValue,String newValue,String buyMode,boolean key) throws Exception {
		if (!oldValue.equals(newValue)){
			RDevice rd = new RDevice();
			rd.setDevice_id(deviceId);
			rd.setDepot_status(newValue);
			rDeviceDao.update(rd);
			if (key){	
				saveDeviceChangeAndBuyMode(doneCode, busiCode, deviceId, "depot_status",
						oldValue, newValue,buyMode);
			}
		}
	}

	/**
	 * 保存到待回收设备
	 * @param doneCode
	 * @param busiCode
	 * @param deviceId
	 * @param depotId
	 * @throws JDBCException
	 */
	public void saveDeviceReclaim(Integer doneCode, String busiCode,
			String deviceId, String custId,String reclaimReason) throws Exception {
		
		CCustDevice cDevice = cCustDeviceDao.findByDeviceId(deviceId,getOptr().getCounty_id());
		
		//以前的回收记录转成历史
		rDeviceReclaimDao.updateReclaimHistory(deviceId, getOptr().getDept_id());
		
		RDeviceReclaim rd = new RDeviceReclaim();
		rd.setDone_code(doneCode);
		rd.setDevice_id(deviceId);
		rd.setBusi_code(busiCode);	
		if(cDevice!=null){
			rd.setBuy_mode(cDevice.getBuy_mode());
		}
		rd.setDepot_id(getOptr().getDept_id());
		rd.setOptr_id(getOptr().getOptr_id());
		rd.setDept_id(getOptr().getDept_id());
		rd.setStatus(StatusConstants.UNCONFIRM);
		rd.setCust_id(custId);
		rd.setReclaim_reason(reclaimReason);
		rDeviceReclaimDao.save(rd);
	}
	
	
	/**
	 * 更换设备直接回收
	 * @param doneCode
	 * @param busiCode
	 * @param oldDevice
	 * @param custId
	 * @param reclaimReason
	 * @throws Exception
	 */
	public void saveDeviceToReclaim(Integer doneCode, String busiCode,
			DeviceDto oldDevice, String custId,String reclaimReason) throws Exception {
		
		CCustDevice cDevice = cCustDeviceDao.findByDeviceId(oldDevice.getDevice_id(),getOptr().getCounty_id());
		
		//以前的回收记录转成历史
		rDeviceReclaimDao.updateReclaimHistory(oldDevice.getDevice_id(), getOptr().getDept_id());
		
		
		List<String> deviceIds = new ArrayList<String>();
		deviceIds.add(oldDevice.getDevice_id());
		RCard pairCard = null;
		RModem pairModem = null;
		if(oldDevice != null){
			pairCard = oldDevice.getPairCard();
			if(null !=pairCard && SystemConstants.BOOLEAN_FALSE.equals(pairCard.getIs_virtual()) ){
				deviceIds.add(pairCard.getDevice_id());
			}else{
				pairCard = null;
			}
			
			pairModem = oldDevice.getPairModem();
			if(null !=pairModem && SystemConstants.BOOLEAN_FALSE.equals(pairModem.getIs_virtual())){
				deviceIds.add(pairModem.getDevice_id());
			}else{
				pairModem = null;
			}
		}
		
		for(String devId:deviceIds){
			RDeviceReclaim reclaim = new RDeviceReclaim();
			reclaim.setDone_code(doneCode);
			reclaim.setDevice_id(devId);
			reclaim.setBusi_code(busiCode);	
			if(cDevice!=null){
				reclaim.setBuy_mode(cDevice.getBuy_mode());
			}
			reclaim.setDepot_id(getOptr().getDept_id());
			reclaim.setOptr_id(getOptr().getOptr_id());
			reclaim.setDept_id(getOptr().getDept_id());
			reclaim.setStatus(StatusConstants.CONFIRM);
			reclaim.setCust_id(custId);
			reclaim.setReclaim_reason(reclaimReason);
			reclaim.setConfirm_optr(getOptr().getOptr_id());
			reclaim.setConfirm_time(DateHelper.now());
			rDeviceReclaimDao.save(reclaim);
			
			CCustDevice custDevice = cCustDeviceDao.queryCustDeviceByDeviceId(custId, devId);
			if(custDevice != null){
				cCustDeviceDao.removeDevice(custId, devId, doneCode, SystemConstants.BOOLEAN_TRUE);
			}else{
				cCustDeviceHisDao.updateRecliam(custId, devId);
			}
			
			RDevice device = new RDevice();
			device.setDevice_id(devId);
			device.setDepot_status(StatusConstants.IDLE);
			device.setOwnership(SystemConstants.OWNERSHIP_GD);
			rDeviceDao.update(device);
			
		}
		saveDeviceChange(doneCode, busiCode, oldDevice.getDevice_id(), "depot_status",
				oldDevice.getDepot_status(), StatusConstants.IDLE);
		if(!SystemConstants.OWNERSHIP_GD.equals(oldDevice.getOwnership())){
			saveDeviceChange(doneCode, busiCode, oldDevice.getDevice_id(), "ownership",
					oldDevice.getOwnership(), SystemConstants.OWNERSHIP_GD);
		}
		if(pairCard !=null){
			saveDeviceChange(doneCode, busiCode, pairCard.getDevice_id(), "depot_status",
					pairCard.getDepot_status(), StatusConstants.IDLE);
			saveDeviceChange(doneCode, busiCode, pairCard.getDevice_id(), "ownership",
					pairCard.getOwnership(), SystemConstants.OWNERSHIP_GD);
		}
		if(pairModem !=null){
			saveDeviceChange(doneCode, busiCode, pairModem.getDevice_id(), "depot_status",
					pairModem.getDepot_status(), StatusConstants.IDLE);
			saveDeviceChange(doneCode, busiCode, pairModem.getDevice_id(), "ownership",
					pairModem.getOwnership(), SystemConstants.OWNERSHIP_GD);
		}
		
		
		
	}
	
	
	
	public void saveLossDevice(Integer doneCode, String busiCode, String deviceCode) throws Exception {
		RDevice rd = rDeviceDao.findByDeviceCode(deviceCode);
		if(rd.getIs_loss().equals(SystemConstants.BOOLEAN_FALSE)){
		
			rDeviceDao.updateDeviceLoss(rd.getDevice_id(), SystemConstants.BOOLEAN_TRUE);
			
			saveDeviceChange(doneCode, busiCode, rd.getDevice_id(), "is_loss", rd.getIs_loss(),
					SystemConstants.BOOLEAN_TRUE);
		}
	}
	
	/**
	 * 查询待回收的设备
	 * 
	 * @param deviceId
	 * @return
	 * @throws Exception
	 */
	public RDeviceReclaim queryDeviceReclaim(String deviceId) throws Exception{
		return rDeviceReclaimDao.queryDeviceReclaim(deviceId);
	}
	
	/**
	 * 修改设备状态
 	 * @param doneCode 业务流水
	 * @param busiCode 业务代码
	 * @param deviceId 设备id
	 * @param status   修改的设备状态
	 */
	public void updateDeviceStatus(Integer doneCode, String busiCode,
			String deviceId,  String oldValue,String newValue,boolean key) throws Exception {
		if (!oldValue.equals(newValue)){
			RDevice rd = new RDevice();
			rd.setDevice_id(deviceId);
			rd.setDevice_status(newValue);
			rDeviceDao.update(rd);
			if(key){
				saveDeviceChange(doneCode, busiCode, deviceId, "device_status",
					oldValue, newValue);
			}
		}
	}
	
	/**
	 * 修改设备到当前操作员的部门
	 * @param doneCode
	 * @param busiCode
	 * @param deviceId
	 * @param depot_id
	 */
	public void updateDeviceDepotId(Integer doneCode, String busiCode,
			String deviceId, String oldDepotId, String newDepotId,String buyMode,boolean key) throws Exception {
		if (!oldDepotId.equals(newDepotId)) {
			RDevice rd = new RDevice();
			rd.setDevice_id(deviceId);
			rd.setDepot_id(newDepotId);
			rDeviceDao.update(rd);
			if(key){
				saveDeviceChangeAndBuyMode(doneCode, busiCode, deviceId, "depot_id",
						oldDepotId, newDepotId,buyMode);
			}
		}
	}

	/**
	 * 修改设备产权
 	 * @param doneCode
	 * @param busiCode
	 * @param deviceId
	 * @param ownerShip
	 */
	public void updateDeviceOwnership(Integer doneCode, String busiCode,
			String deviceId, String oldValue,String newValue,String buyMode,boolean key) throws Exception {
		if (!oldValue.equals(newValue)){
			RDevice rd = new RDevice();
			rd.setDevice_id(deviceId);
			rd.setOwnership(newValue);
			rDeviceDao.update(rd);
			if(key){
				saveDeviceChangeAndBuyMode(doneCode, busiCode, deviceId, "ownership",
						oldValue, newValue,buyMode);
			}
		}
	}
	
	/**
	 * 新机变成旧机
	 * @param doneCode
	 * @param busiCode
	 * @param deviceId
	 * @param oldValue
	 * @param newValue
	 * @throws Exception
	 */
	public void updateDeviceIsNewStb(Integer doneCode, String busiCode,
			String deviceId, String oldValue,String newValue,boolean key) throws Exception {
		if (oldValue==null || !oldValue.equals(newValue)){
			RDevice rd = new RDevice();
			rd.setDevice_id(deviceId);
			rd.setIs_new_stb(newValue);
			rDeviceDao.update(rd);
			if(key){
				saveDeviceChange(doneCode, busiCode, deviceId, "is_new_stb",
						oldValue, newValue);
			}
		}
	}
	
	/**
	 * 更新设备新旧属性
	 * @param doneCode
	 * @param busiCode
	 * @param deviceId
	 * @param ownerShip
	 * @throws JDBCException
	 */
	public void updateDeviceUsed(Integer doneCode, String busiCode,
			String deviceId, String oldValue,String newValue,boolean key) throws Exception {
		if (!oldValue.equals(newValue)){
			RDevice rd = new RDevice();
			rd.setDevice_id(deviceId);
			rd.setUsed(newValue);
			rDeviceDao.update(rd);
			if(key){
				saveDeviceChange(doneCode, busiCode, deviceId, "used",
						oldValue, newValue);
			}
		}
	}
	
	/**
	 * 购买方式更新
	 * @param doneCode
	 * @param busiCode
	 * @param deviceId
	 * @param oldBuyMode
	 * @param newBuyMode
	 * @throws Exception
	 */
	public void updateBuyMode(Integer doneCode, String busiCode,String deviceId, String oldBuyMode, String newBuyMode) throws Exception {
		saveDeviceChange(doneCode, busiCode, deviceId, "buy_mode",oldBuyMode, newBuyMode);
	}
	
	
	/**
	 * 记录更换原因
	 * @param doneCode
	 * @param busiCode
	 * @param deviceId
	 * @param changeReason
	 * @throws Exception
	 */
	public void updateChangeReason(Integer doneCode, String busiCode,String deviceId, String changeReason) throws Exception {
		saveDeviceChange(doneCode, busiCode, deviceId, "change_reason","", changeReason);
	}
	
	public void saveDeviceUseRecords(Integer doneCode, String busiCode, String deviceId,
			String deviceType, String deviceCode, String custId, String custNo) throws Exception {
		RDeviceUseRecords records = new RDeviceUseRecords();
		records.setDone_code(doneCode);
		records.setDevice_id(deviceId);
		records.setDevice_type(deviceType);
		records.setDevice_code(deviceCode);
		records.setBusi_code(busiCode);
		records.setCust_id(custId);
		records.setCust_no(custNo);
		records.setDone_date(DateHelper.now());
		setBaseInfo(records);
		rDeviceUseRecordsDao.save(records);
	}
	
	public RDevice findByDeviceCode(String deviceCode) throws JDBCException {
		return rDeviceDao.findByDeviceCode(deviceCode);
	}

	/**
	 * 根据设备编号查找设备信息
	 * 如果设备编号对应的设备为机顶盒，判断pair_card_id是否为空，如果不为空，设置机顶盒的配对卡信息
	 * @param deviceCode
	 * @return
	 * @throws Exception
	 */
	public DeviceDto queryDeviceByDeviceCode(String deviceCode) throws JDBCException{
		if (StringHelper.isEmpty(deviceCode))
			return null;
		RDevice rd = findByDeviceCode(deviceCode.replace(":","").replace("：", ""));
		if (rd == null)
			return null;
		return queryDeviceByDeviceId(rd);
	}

	/**
	 * 修改机顶盒配对的智能卡
	 * @param stbDeviceId
	 * @param cardId
	 */
	public void updatePairCard(Integer doneCode, String busiCode,
			String stbDeviceId,  String oldCardId,String newCardId,boolean key) throws Exception {
		if (!oldCardId.equals(newCardId)){
			rStbDao.updatePairCard(stbDeviceId, newCardId);
			if(key){	
				saveDeviceChange(doneCode, busiCode, stbDeviceId, "pair_card_id",
						oldCardId, newCardId);
			}
		}
	}
	
	public void updatePairModem(Integer doneCode, String busiCode,
			String stbDeviceId,  String oldModemId,String newModemId,boolean key) throws Exception {
		if (!oldModemId.equals(newModemId)){
			rStbDao.updatePairModem(stbDeviceId, newModemId);
			if(key){
				saveDeviceChange(doneCode, busiCode, stbDeviceId, "pair_modem_id",
					oldModemId, newModemId);
			}
		}
	}

	/**
	 * 记录智能卡的原配对盒号和新配对盒号异动
	 * @param doneCode
	 * @param busiCode
	 * @param cardDeviceId
	 * @param oldStbId
	 * @param newStbId
	 * @param key
	 * @throws Exception
	 */
	public void savePairStbChange(Integer doneCode, String busiCode,
			String cardDeviceId,  String oldStbId,String newStbId,boolean key) throws Exception {
		if (!oldStbId.equals(newStbId)){
			if(key){	
				saveDeviceChange(doneCode, busiCode, cardDeviceId, "pair_stb_id",
						oldStbId, newStbId);
			}
		}
	}
	
	/**
	 * 设备产权互换
	 * @param doneCode		业务流水号
	 * @param busiCode		业务编号
	 * @param d1			设备1
	 * @param d2			设备2
	 */
	public void exchangeDeviceOwnership(Integer doneCode,String busiCode,
			DeviceDto d1,DeviceDto d2) throws Exception{
		if (!d1.getOwnership().equals(d2.getOwnership())){
			updateDeviceOwnership(doneCode, busiCode, d1.getDevice_id(), d2.getOwnership(),d1.getOwnership(),null,true);
			updateDeviceOwnership(doneCode, busiCode, d2.getDevice_id(), d1.getOwnership(),d2.getOwnership(),null,true);
		}
	}

	/**
	 * 根据设备类型查找设备型号信息
	 * @param deviceId
	 * @param deviceType
	 * @return
	 * @throws JDBCException
	 */
	public RDeviceModel queryDeviceModelByDeviceType(String deviceId,String deviceType) throws JDBCException {
		RDeviceModel deviceModel = null;
		if(SystemConstants.DEVICE_TYPE_STB.equals(deviceType)){
			deviceModel = rStbModelDao.findByDeviceId(deviceId);
		}else if(SystemConstants.DEVICE_TYPE_CARD.equals(deviceType)){
			deviceModel = rCardModelDao.findByDeviceId(deviceId);
		}else if(SystemConstants.DEVICE_TYPE_MODEM.equals(deviceType)){
			deviceModel = rModemModelDao.findByDeviceId(deviceId);
		}
		return deviceModel;
	}

	/**
	 * 根据设备id查找设备信息
	 * 如果设备编号对应的设备为机顶盒，判断pair_card_id是否为空，如果不为空，设置机顶盒的配对卡信息
	 * @param deviceId
	 * @return
	 * @throws Exception
	 */
	public DeviceDto queryDeviceByDeviceId(String deviceId) throws JDBCException {
		RDevice rd = rDeviceDao.findByDeviceId(deviceId);
		if (rd == null)
			return null;
		return queryDeviceByDeviceId(rd);
	}
	
	public DeviceDto queryDeviceByDeviceId(RDevice rd) throws JDBCException {
		RDeviceModel deviceModel = null;
		String deviceCode = null;
		DeviceDto dto = new DeviceDto();
		BeanUtils.copyProperties(rd, dto);

		if (rd.getDevice_type().equals(SystemConstants.DEVICE_TYPE_STB)) {
			deviceModel = rStbModelDao.findByDeviceId(rd.getDevice_id());
			RStb stb = rStbDao.findByKey(rd.getDevice_id());
			deviceCode = stb.getStb_id();
			// 设置配对信息
			dto.setPairCard(rCardDao.findPairCardByStbDeviceId(rd.getDevice_id()));
			dto.setPairModem(rModemDao.findPairModemByStbDeviceId(rd.getDevice_id()));
			dto.setStbMac(stb.getMac());
		} else if (rd.getDevice_type().equals(SystemConstants.DEVICE_TYPE_CARD)) {
			deviceModel = rCardModelDao.findByDeviceId(rd.getDevice_id());
			deviceCode = rCardDao.findByKey(rd.getDevice_id()).getCard_id();
			
			if(StringHelper.isEmpty(dto.getIs_virtual()))
				dto.setIs_virtual(deviceModel.getIs_virtual());
			if(null != rStbDao.findPairStbByCardDeviceId(rd.getDevice_id())){
				//设置是否配对智能卡的标识位
				dto.setIsPairCard(SystemConstants.BOOLEAN_TRUE);
			}
		} else if (rd.getDevice_type()
				.equals(SystemConstants.DEVICE_TYPE_MODEM)) {
			deviceModel = rModemModelDao.findByDeviceId(rd.getDevice_id());
			deviceCode = rModemDao.findByKey(rd.getDevice_id()).getModem_mac();
			if(StringHelper.isEmpty(dto.getIs_virtual()))
				dto.setIs_virtual(deviceModel.getIs_virtual());
			if(null != rStbDao.findPairStbByModemDeviceId(rd.getDevice_id())){
				//设置是否配对MODEM的标识位
				dto.setIsPairModem(SystemConstants.BOOLEAN_TRUE);
			}
		}

		dto.setDevice_code(deviceCode);
		dto.setDeviceModel(deviceModel);
		return dto;
	}
	
	public RStb findPairStbByModemDeviceId(String deviceId) throws Exception {
		return rStbDao.findPairStbByModemDeviceId(deviceId);
	}
	
	public RStb findPairStbByCardDeviceId(String deviceId) throws Exception {
		return rStbDao.findPairStbByCardDeviceId(deviceId);
	}
	
	public RStb queryStbById(String stbId) throws Exception {
		return rStbDao.queryStbById(stbId);
	}

	/**
	 * 根据设备编号查找可以销售的设备信息
	 * @param deviceCode
	 * @return
	 * @throws Exception
	 */
	public DeviceDto querySaleableDevice(String deviceCode) throws Exception{
		DeviceDto rd = queryDevice(deviceCode);
		if(isDeviceSaleable(rd))
			return rd;
		else
			return null;
	}
	/**
	 * 开户时输入多个设备编号，验证这些设备是否可用
	 * @param deviceCodes
	 * @param userType
	 * @return 最后一个设备的型号
	 * @throws Exception
	 */
	public List<DeviceDto> querySaleableDeviceArea(String deviceCodes,String userType) throws Exception{
		//TODO 
		if(StringHelper.isEmpty(deviceCodes)||StringHelper.isEmpty(userType)){
			throw new ComponentException(ErrorCode.ParamIsNull);
		}
		String[] deviceCodeArrays=deviceCodes.split("\n");
		List<DeviceDto> list=new ArrayList<>();
		for(String deviceCode:deviceCodeArrays){
			DeviceDto rd = queryDevice(deviceCode);
			if(isDeviceSaleable(rd)){
				//判断设备仓库是否可用
				checkDeviceDepotCanUse(rd);
				//判断设备和用户类型是否匹配
				checkDeviceUserType(rd,userType);
			}
			list.add(rd);
		}
		return list;
	}
	
	/**
	 * 判断设备跟用户类型是否匹配
	 * singleStbNotSupDTT: '此设备为单向机顶盒，不支持当前的OTT用户类型!',
	doubleStbNotSupOTT: '此设备为双向机顶盒，不支持当前的DTT用户类型!',
	modemNotSupUserType: '设备为Modem猫，不支持所选[{0}]用户类型!',
	currDeviceNotSupUserType: '此设备不支持当前的用户类型!',
	 * @throws ComponentException 
	 */
	public boolean checkDeviceUserType(DeviceDto rd,String userType) throws ComponentException{
		if(userType.equals(SystemConstants.USER_TYPE_DTT)
				&&SystemConstants.DEVICE_TYPE_STB.equals(rd.getDevice_type())
				&&SystemConstants.DTV_SERV_TYPE_SINGLE.equals(rd.getDeviceModel().getInteractive_type())){
			return true;
		}else if(userType.equals(SystemConstants.USER_TYPE_OTT)
				&&SystemConstants.DEVICE_TYPE_STB.equals(rd.getDevice_type())
				&&SystemConstants.DTV_SERV_TYPE_DOUBLE.equals(rd.getDeviceModel().getInteractive_type())){
			return true;
		}else if(userType.equals(SystemConstants.USER_TYPE_BAND)
				&&SystemConstants.DEVICE_TYPE_MODEM.equals(rd.getDevice_type())){
			return true;
		}else {
			throw new ComponentException(ErrorCode.DeviceNotSupUserType,rd.getDevice_code(),userType);
		}
	}
	/**
	 * 判断设备仓库和操作员部门是否匹配
	 */
	private void checkDeviceDepotCanUse(DeviceDto device) throws Exception{
		String scopeDevice = queryTemplateConfig(TemplateConfigDto.Config.SCOPE_DEVICE.toString());
		if (SystemConstants.SYS_LEVEL_DEPT.equals(scopeDevice)) {
			//按营业厅
			if (!getOptr().getDept_id().equals(device.getDepot_id())) {
				SDept dept = sDeptDao.findByKey(device.getDepot_id());
				if (dept == null) {
					throw new ComponentException("当前设备所在仓库数据有问题");
				}
				throw new ComponentException(ErrorCode.DeviceNotInRightDepot,device.getDevice_code(),dept.getDept_name());
			}
		} else if (SystemConstants.SYS_LEVEL_COUNTY.equals(scopeDevice)) {
			//按县市
			SDept dept = sDeptDao.findByKey(device.getDepot_id());
			if (!getOptr().getCounty_id().equals(dept.getCounty_id())) {
				throw new ComponentException(ErrorCode.DeviceNotInRightDepot,device.getDevice_code(),dept.getDept_name());
			}
		}
	}
	public static void main(String args[]){
		String aaa="aaa\nbbb\nccc\n";
		String[] bbb=aaa.split("\n");
		for(String b:bbb)
		System.out.println("##"+b);
	}
	
	public DeviceDto queryChangeDevice(String userType, String deviceCode) throws Exception {
		DeviceDto rd = queryDevice(deviceCode);
		if(rd.getDevice_type().equals(SystemConstants.DEVICE_TYPE_STB)){
			RStbModel stbModel = rStbModelDao.findByDeviceId(rd.getDevice_id());
			if(userType.equals(SystemConstants.USER_TYPE_OTT) && stbModel.getInteractive_type().equals(SystemConstants.DTV_SERV_TYPE_SINGLE)){
				throw new ComponentException(ErrorCode.OTTIsNotSingle);
			}else if(userType.equals(SystemConstants.USER_TYPE_DTT) && stbModel.getInteractive_type().equals(SystemConstants.DTV_SERV_TYPE_DOUBLE)){
				throw new ComponentException(ErrorCode.DTTIsNotDouble);
			}
		}
		if(isDeviceSaleable(rd))
			return rd;
		else
			return null;
	}
	
	/**
	 * 查询可以单独购买的智能卡
	 * @param deviceCode
	 * @return
	 * @throws ComponentException
	 * @throws JDBCException
	 */
	public DeviceDto querySaleableCard(String deviceCode) throws Exception{
		DeviceDto rd = queryDeviceByDeviceCode(deviceCode);
		if (rd == null)
			throw new ComponentException("设备不存在");

		if(!SystemConstants.DEVICE_TYPE_CARD.equals(rd.getDevice_type())){
			throw new ComponentException("设备不是智能卡");
		}else{
			RStb stb = rStbDao.findPairStbByCardDeviceId(rd.getDevice_id());
			if (stb != null) {
				throw new ComponentException("智能卡有配对的机顶盒");
			}
		}
		if(isDeviceSaleable(rd))
			return rd;
		else
			return null;
	}
	
	/**
	 * 查询可以单独购买的MODEM
	 * @param deviceCode
	 * @return
	 * @throws ComponentException
	 * @throws JDBCException
	 */
	public DeviceDto querySaleableModem(String deviceCode) throws Exception{
		DeviceDto rd = queryDeviceByDeviceCode(deviceCode);
		if (rd == null)
			throw new ComponentException("设备不存在");

		if(!SystemConstants.DEVICE_TYPE_MODEM.equals(rd.getDevice_type())){
			throw new ComponentException("设备不是MODEM");
		}else{
			RStb stb = rStbDao.findPairStbByModemDeviceId(rd.getDevice_id());
			if (stb != null) {
				throw new ComponentException("MODEM有配对的机顶盒");
			}
		}
		if(isDeviceSaleable(rd))
			return rd;
		else
			return null;
	}
	
	public boolean isPair(String stbModel,String cardModel) throws Exception {
		return rPairCfgDao.isPair(stbModel, cardModel);
	}

	/**
	 * 根据设备编号查找可以的设备信息
	 *
	 * 如果设备为配对的卡，返回为配对的机顶盒
	 * @param deviceCode
	 * @return
	 * @throws Exception
	 */
	public DeviceDto queryDevice(String deviceCode) throws JDBCException,
			ComponentException {
		DeviceDto rd = queryDeviceByDeviceCode(deviceCode);
		if (rd == null)
			throw new ComponentException(ErrorCode.DeviceNotExists);

		/*if (rd.getDevice_type().equals(SystemConstants.DEVICE_TYPE_CARD)) {
			RStb stb = rStbDao.findPairStbByCardDeviceId(rd.getDevice_id());
			if (stb != null) {
				rd = queryDeviceByDeviceId(stb.getDevice_id());
			}
		}
		if (rd.getDevice_type().equals(SystemConstants.DEVICE_TYPE_MODEM)) {
			RStb stb = rStbDao.findPairStbByModemDeviceId(rd.getDevice_id());
			if (stb != null) {
				rd = queryDeviceByDeviceId(stb.getDevice_id());
			}
		}*/
		return rd;
	}

	/**
	 * 根据设备型号和销售方式查找销售费用信息
	 * @param deviceModel
	 * @param buyMode
	 * @return
	 */
	public List<RDeviceFee> queryDeviceFee(String deviceType,String deviceModel,String buyMode) throws Exception{
		String templateId = queryTemplateId(SystemConstants.TEMPLATE_TYPE_FEE);
		return rDeviceFeeDao.queryFee(deviceType,deviceModel,buyMode,templateId);
	}
	
	public List<RDeviceFee> queryUpgradeuFee(String deviceType) throws Exception{
		String templateId = queryTemplateId(SystemConstants.TEMPLATE_TYPE_FEE);
		return rDeviceFeeDao.queryUpgradeuFee(deviceType,SystemConstants.BUSI_BUY_MODE_UPGRADE,templateId);
	}

	public TBusiFeeDevice queryFeeByMode(String deviceType) throws Exception{
		return tBusiFeeDeviceDao.queryFeeByMode(deviceType);
	}
	
	
	/**
	 * 判断设备是否可以销售
	 * @param device
	 * @return
	 */
	private boolean isDeviceSaleable(DeviceDto device) throws Exception{
		if (device == null){
			throw new ComponentException(ErrorCode.DeviceNotExists);
		}
		if (device.getDepot_status().equals(StatusConstants.USE)) {
			throw new ComponentException("设备已被使用");
		}
		if(device.getIs_loss().equals(SystemConstants.BOOLEAN_TRUE)){
			throw new ComponentException("设备已被挂失");
		}
		if (device.getOwnership().equals(SystemConstants.OWNERSHIP_CUST)) {
			throw new ComponentException("设备产权不属于广电");
		}
		if(SystemConstants.BOOLEAN_TRUE.equals(device.getFreezed())){
			throw new ComponentException("设备已被冻结");
		}
		if (StatusConstants.UNCONFIRM.equals(device.getTran_status())){
			throw new ComponentException("该设备正处于调拨中");
		}
		if (!SystemConstants.DEVICE_DIFFENCN_TYPE_NODIFF.equals(device.getDiffence_type())){
			throw new ComponentException("该设备实物状态和库存状态不一致");
		}
		if(StatusConstants.CORRUPT.equals(device.getDevice_status())){
			throw new ComponentException("该设备已损坏");
		}
		if(StatusConstants.SCRAP.equals(device.getDevice_status())){
			throw new ComponentException("该设备已报废");
		}
		if(SystemConstants.BOOLEAN_TRUE.equals(device.getIs_virtual())){
			throw new ComponentException("该设备为虚拟"+device.getDevice_type_text());
		}
		
		/*String scopeDevice = queryTemplateConfig(TemplateConfigDto.Config.SCOPE_DEVICE.toString());
		if (SystemConstants.SYS_LEVEL_DEPT.equals(scopeDevice)) {
			//按营业厅
			if (!getOptr().getDept_id().equals(device.getDepot_id())) {
				RDepotDefine depot = rDepotDefineDao.findBydepotId(device
						.getDepot_id());
				if (depot == null) {
					throw new ComponentException("当前设备所在仓库数据有问题");
				}
				throw new ComponentException("设备在" + depot.getDepot_name()
						+ ",不在当前仓库");
			}
		} else if (SystemConstants.SYS_LEVEL_COUNTY.equals(scopeDevice)) {
			//按县市
			SDept dept = sDeptDao.findByKey(device.getDepot_id());
			if (!getOptr().getCounty_id().equals(dept.getCounty_id())) {
				throw new ComponentException("设备在" + dept.getCounty_name()
						+ ",不在当前仓库");
			}
		}*/
		
		
		return true;
	}

	/**
	 * 记录设备的信息异动
	 * @param deviceId
	 * @param columnName
	 * @param oldValue
	 * @param newValue
	 * @throws Exception
	 */
	private void saveDeviceChange(Integer doneCode, String busiCode,
			String deviceId, String columnName, String oldValue, String newValue) throws Exception {
		rDeviceChangeDao.saveDeviceChange(doneCode,busiCode,deviceId,columnName,oldValue,newValue,getOptr().getOptr_id()
				,getOptr().getDept_id(),getOptr().getCounty_id(),getOptr().getArea_id());
	}
	
	private void saveDeviceChangeAndBuyMode(Integer doneCode, String busiCode,
			String deviceId, String columnName, String oldValue, String newValue,String buyMode) throws Exception {
		rDeviceChangeDao.saveDeviceChangeAndBuyMode(doneCode,busiCode,deviceId,columnName,oldValue,newValue,buyMode,getOptr().getOptr_id()
				,getOptr().getDept_id(),getOptr().getCounty_id(),getOptr().getArea_id());
	}
	
	/**
	 * 
	 * @param doneCode
	 * @return
	 */
	public void recover(Integer doneCode) throws Exception{
		rDeviceChangeDao.recover(doneCode) ;
	}

	/**
	 * 根据机顶盒编号查询机顶盒类型信息
	 * @param stbId
	 * @return
	 * @throws Exception
	 */
	public RStbModel queryByStbId(String stbId) throws Exception{
		return rStbModelDao.queryByStbId(stbId);
	}


	/**
	 * 查找客户下 指定购买的设备
	 * @param custId
	 * @param modelName
	 */
	public List<RDevice> queryDeviceByBuyModel(String custId, String modelName)
			throws JDBCException {
		return rDeviceDao.queryDeviceByBuyModel(custId, modelName);
	}

	/**
	 * 查询设备购买方式
	 * @return
	 */
	public List<TDeviceBuyMode> queryDeviceBuyModel() throws Exception {
		String dataRight = this.queryDataRightCon(getOptr(), DataRight.BUY_MODE.toString());
		List<TDeviceBuyMode> list = tDeviceBuyModeDao.queryDeviceBuyModeByParams(
				SystemConstants.BUY_TYPE_BUSI,
				SystemConstants.BUSI_BUY_MODE_BUY, dataRight);
		for(TDeviceBuyMode buyMode : list){
			buyMode.setBuy_mode_name( MemoryDict.getTransData(buyMode.getBuy_mode_name()) );
		}
		return list;
	}
	
	
	/**
	 * 查询设备型号
	 * @return
	 */
	public List<RDeviceModel> queryDeviceModel() throws Exception {
		return rDeviceModelDao.queryDeviceModel();
	}
	
	
	/**
	 * 查询充值卡
	 * @param deviceCode
	 * @return
	 * @throws JDBCException
	 * @throws ComponentException
	 */
	public CValuableCard queryValuableCardById(String deviceCode) throws JDBCException,ComponentException{
		return cValuableCardDao.findByKey(deviceCode);
	}
	public List<ValuableCardDto> queryValuableCardById(String[] valuableId,String countyId) throws JDBCException,ComponentException{
		return cValuableCardDao.queryById(valuableId,countyId);
	}
	
	public List<CValuableCard> queryBydoneCode(Integer doneCode) throws JDBCException,ComponentException{
		return cValuableCardDao.queryBydoneCode(doneCode);
	}
	
	/**
	 * 保存充值卡，并验证是否已经存在
	 * @param doneCode
	 * @param records
	 * @param optr
	 * @throws Exception
	 */
	public void saveValuableCard(Integer doneCode,String records,String feeSn, SOptr optr) throws Exception {
		List<CValuableCard> list = new ArrayList<CValuableCard>();
		if (StringHelper.isNotEmpty(records)) {
			Type type = new TypeToken<List<CValuableCard>>() {
			}.getType();
			list = JsonHelper.gson.fromJson(records, type);
		}
		String[] cards = CollectionHelper.converValueToArray(list, "valuable_id");
		List<CValuableCard> cardList = cValuableCardDao.queryByValuableId(cards);
		if(cardList.size()>0){
			throw new ComponentException("输入的充值卡号:【"+cardList.get(0).getValuable_id()+"】已经存在!");	
		}
		List<CValuableCard> msgList = new ArrayList<CValuableCard>();
		if (list.size() > 0) {
			for (CValuableCard dto : list) {
				CValuableCard msg = new CValuableCard();
				BeanUtils.copyProperties(dto, msg);
				msg.setDept_id(getOptr().getDept_id());
				msg.setCreate_time(DateHelper.now());
				msg.setCounty_id(getOptr().getCounty_id());
				msg.setOptr_id(getOptr().getOptr_id());
				msg.setDone_code(doneCode);
				msgList.add(msg);
			}
			cValuableCardDao.save(msgList.toArray(new CValuableCard[msgList.size()]));
		}
	}
	
	public void editValuableCard(String doneCode,String custName) throws Exception {
		cValuableCardDao.editValuableCard(doneCode,custName);
	}
	
	
	/**
	 * 删除充值卡，并记录历史
	 * @param doneCode
	 * @param deviceCode
	 * @param optr
	 * @throws Exception
	 */
	public void removeValuableCard(Integer doneCode,List<ValuableCardDto> valuableList) throws Exception {
		List<CValuableCardHis> cardHisList = new ArrayList<CValuableCardHis>();
		List<String> list = new ArrayList<String>();
		for(ValuableCardDto dto:valuableList){
			CValuableCardHis cardHis = new CValuableCardHis();
			BeanUtils.copyProperties(dto, cardHis);
			list.add(dto.getValuable_id());
			cardHis.setBusi_done_code(doneCode);
			cardHisList.add(cardHis);
		}
		
		String[] arr = (String[])list.toArray(new String[list.size()]);
		cValuableCardHisDao.save(cardHisList.toArray(new CValuableCardHis[cardHisList.size()]));
		cValuableCardDao.deleteById(arr);
	}
	public void saveValuableCardFee(CValuableCardFee valuableCardFee) throws JDBCException{
		cValuableCardFeeDao.save(valuableCardFee);
	}
	
	public Pager<ValuableCardDto> queryValuableAllCard(Integer start, Integer limit, String query,String queryItem) throws Exception{
		if(queryItem.equals(SystemConstants.BOOLEAN_FALSE)){
			return cValuableCardDao.queryValuableHisAllCard(start,limit,query,getOptr().getCounty_id());
		}else{
			return cValuableCardDao.queryValuableAllCard(start,limit,query,getOptr().getCounty_id());
		}
		
	}
	
	public void checkChangeDevice(DeviceDto oldDevice, DeviceDto device) throws Exception {
		if (!oldDevice.getDevice_type().equals(device.getDevice_type()))
			throw new ServicesException("设备类型不正确!");
		if (device.getDevice_type().equals(SystemConstants.DEVICE_TYPE_STB)){
			//判断设备型号的INTERACTIVE_TYPE是否一致
			RStbModel oldModel = rStbModelDao.findByKey(oldDevice.getDevice_model());
			RStbModel newModel = rStbModelDao.findByKey(device.getDevice_model());
			if (!oldModel.getInteractive_type().equals(newModel.getInteractive_type()))
				throw new ServicesException("设备类型不正确!");
			
		}
	}
	
	/**
	 * 根据doneCode查询本次业务被回收的设备信息.
	 * @param doneCode
	 * @return
	 */
	public DeviceDto queryReclaimedDevice(Integer doneCode) throws Exception{
		RDeviceReclaim rr = rDeviceReclaimDao.queryReclaimDevice(doneCode, null);
		return queryDeviceByDeviceId(rr.getDevice_id());
	}
	
	public boolean getIsCard(String deviceCode) throws Exception{
		RDevice rd  = rDeviceDao.findByDeviceCode(deviceCode);
		if (rd == null){
			throw new ComponentException("设备不存在");
		}
		
		boolean flag = rDeviceDao.isDeviceProcure(rd.getDevice_id());
		if(!flag){
			throw new ComponentException("该智能卡不是一体机领用，不能发预授权!");
		}
		return true;
	}
	
	/**
	 * 器材数量减去购买数量
	 * @param deviceType
	 * @param deviceModel
	 * @param optr
	 * @param buyNum 
	 * @param buyMode 
	 * @throws Exception
	 */
	public void removeTotalNumDevice(Integer doneCode,String busiCode,String deviceId, Integer buyNum,String buyMode, SOptr optr) throws Exception{
		RDevice device = rDeviceDao.findByKey(deviceId);
		
		//总数异动记录
		rDeviceChangeDao.saveMateralTransChange(doneCode,busiCode,device.getDevice_id(),"total_num",device.getTotal_num()
				,device.getTotal_num()-buyNum,optr.getOptr_id(),optr.getDept_id(),optr.getCounty_id(), optr.getArea_id(),buyMode);
		//减去数量
		rDeviceDao.removeMateralDevice(deviceId, buyNum);
		RDevice nextRdevice = rDeviceDao.findByKey(deviceId);
		if(nextRdevice.getTotal_num()<0){
			throw new ComponentException(ErrorCode.DeviceTotalNumIsNull);
		}
	}
	
		
	/**
	 * 原器材加上购买数量
	 * @param deviceId
	 * @param buyNum
	 * @throws Exception
	 */
	public void addTotalNumDevice(String deviceId, Integer buyNum) throws Exception{
		//加上数量购买数量
		rDeviceDao.addMateralDevice(deviceId, buyNum);
	}
	
	public void updateDeviceNum(CFeeDevice feeDevice) throws Exception{
		RDevice r = rDeviceDao.findByKey(feeDevice.getDevice_id());
		if(r == null || !r.getDevice_model().equals(feeDevice.getDevice_model())){
			throw new ComponentException(ErrorCode.DeviceDateException,feeDevice.getDevice_id());
		}
		addTotalNumDevice(feeDevice.getDevice_id(),feeDevice.getBuy_num());
	}
	
	
	
	public RDevice queryTotalNumDevice(String deviceModel,String deptId) throws Exception{
		//原器材
		RDevice device = rDeviceDao.queryIdleMateralDevice(deviceModel, deptId);
		if(device == null){
			throw new ComponentException(ErrorCode.DeviceNotExists);
		}
		
		return device;
	}
	
	public List<RDeviceModelTotalDto> queryMateralTransferDeviceByDepotId(SOptr optr)throws Exception{
		String depotId = optr.getDept_id();
		List<RDevice> list = rDeviceDao.queryMateralDeviceByDepotId(depotId);
		List<RDeviceModel> modelList = queryDeviceModel();
		Map<String, List<RDevice>> deviceMap = CollectionHelper.converToMap(list,new String[]{"device_type","device_model"});
		List<RDeviceModelTotalDto> deviceList = new ArrayList<RDeviceModelTotalDto>();
		for(RDeviceModel _r : modelList){
			RDeviceModelTotalDto dto = new RDeviceModelTotalDto();
			BeanUtils.copyProperties(_r, dto);
			dto.setTotal_num(0);
			String type = StringHelper.join(new String[]{_r.getDevice_type(),_r.getDevice_model()},"_");
			if(deviceMap != null &&  deviceMap.get(type) != null){
				List<RDevice> device = deviceMap.get(type);
				if(device.size()==1){
					dto.setTotal_num(device.get(0).getTotal_num());
				}else{
					throw new ComponentException(ErrorCode.DeviceDateException,type);
				}
			}
			deviceList.add(dto);
		}
		return deviceList;
	}
	
	public List<RDeviceModelTotalDto> queryDeviceCanBuy(String dept_id) throws Exception{		
		List<RDeviceModelTotalDto> list = new ArrayList<RDeviceModelTotalDto>(); 
		
		List<RDevice>  deviceList=  rDeviceDao.queryMateralDeviceByDepotId(dept_id);
		for(RDevice dto : deviceList){
			RDeviceModelTotalDto f = new RDeviceModelTotalDto();
			f.setDevice_model(dto.getDevice_model());
			f.setDevice_type(dto.getDevice_type());
			f.setTotal_num(dto.getTotal_num());
			List<RDeviceFee> fList = queryDeviceFee(dto.getDevice_type(), dto.getDevice_model(), SystemConstants.BUSI_BUY_MODE_BUY);
			if(fList.size()>0){
				f.setFee_id(fList.get(0).getFee_id());
				f.setFee_value(fList.get(0).getFee_value());
				f.setFee_std_id(fList.get(0).getFee_std_id());
			}else{
				//未配置BUY的费用返回-1
				f.setFee_value(-1);
			}
			list.add(f);
		}
		return list;
	}
	
	public List<DeviceSmallDto>  getDeviceCodeByDeviceId(String[] deviceIds) throws Exception{
		if(deviceIds.length>0){
			return rDeviceDao.getDeviceCodeByDeviceId(deviceIds);
		}else{
			return null;
		}
	}
	
	
	public void setCValuableCardHisDao(CValuableCardHisDao valuableCardHisDao) {
		cValuableCardHisDao = valuableCardHisDao;
	}
	
	public void setCValuableCardDao(CValuableCardDao valuableCardDao) {
		cValuableCardDao = valuableCardDao;
	}
	public void setRDeviceChangeDao(RDeviceChangeDao deviceChangeDao) {
		rDeviceChangeDao = deviceChangeDao;
	}
	public void setRDeviceDao(RDeviceDao deviceDao) {
		rDeviceDao = deviceDao;
	}

	/**
	 * @param stbDao the rStbDao to set
	 */
	public void setRStbDao(RStbDao stbDao) {
		rStbDao = stbDao;
	}

	/**
	 * @param stbModelDao the rStbModelDao to set
	 */
	public void setRStbModelDao(RStbModelDao stbModelDao) {
		rStbModelDao = stbModelDao;
	}

	/**
	 * @param cardModelDao the rCardModelDao to set
	 */
	public void setRCardModelDao(RCardModelDao cardModelDao) {
		rCardModelDao = cardModelDao;
	}

	/**
	 * @param modemModelDao the rModemModelDao to set
	 */
	public void setRModemModelDao(RModemModelDao modemModelDao) {
		rModemModelDao = modemModelDao;
	}

	/**
	 * @param cardDao the rCardDao to set
	 */
	public void setRCardDao(RCardDao cardDao) {
		rCardDao = cardDao;
	}

	/**
	 * @param deviceFeeDao the rDeviceFeeDao to set
	 */
	public void setRDeviceFeeDao(RDeviceFeeDao deviceFeeDao) {
		rDeviceFeeDao = deviceFeeDao;
	}



	/**
	 * @param depotDefineDao the rDepotDefineDao to set
	 */
	public void setRDepotDefineDao(RDepotDefineDao depotDefineDao) {
		rDepotDefineDao = depotDefineDao;
	}

	/**
	 * @param modemDao the rModemDao to set
	 */
	public void setRModemDao(RModemDao modemDao) {
		rModemDao = modemDao;
	}

	/**
	 * @param doneCode
	 */
	public void removeChange(Integer doneCode) throws Exception{
		rDeviceChangeDao.removeByDoneCode(doneCode);

	}

	/**
	 * @param deviceBuyModeDao the tDeviceBuyModeDao to set
	 */
	public void setTDeviceBuyModeDao(TDeviceBuyModeDao deviceBuyModeDao) {
		tDeviceBuyModeDao = deviceBuyModeDao;
	}

	/**
	 * @param deptDao the sDeptDao to set
	 */
	public void setSDeptDao(SDeptDao deptDao) {
		sDeptDao = deptDao;
	}

	public void setCValuableCardFeeDao(CValuableCardFeeDao valuableCardFeeDao) {
		cValuableCardFeeDao = valuableCardFeeDao;
	}
	public void setRDeviceReclaimDao(RDeviceReclaimDao deviceReclaimDao) {
		rDeviceReclaimDao = deviceReclaimDao;
	}

	public void setRPairCfgDao(RPairCfgDao pairCfgDao) {
		rPairCfgDao = pairCfgDao;
	}

	public void setRDeviceUseRecordsDao(RDeviceUseRecordsDao deviceUseRecordsDao) {
		rDeviceUseRecordsDao = deviceUseRecordsDao;
	}

	public void setTBusiFeeDeviceDao(TBusiFeeDeviceDao busiFeeDeviceDao) {
		tBusiFeeDeviceDao = busiFeeDeviceDao;
	}

	public void setCCustDeviceDao(CCustDeviceDao custDeviceDao) {
		cCustDeviceDao = custDeviceDao;
	}

	public void setRDeviceModelDao(RDeviceModelDao deviceModelDao) {
		this.rDeviceModelDao = deviceModelDao;
	}


	
}
