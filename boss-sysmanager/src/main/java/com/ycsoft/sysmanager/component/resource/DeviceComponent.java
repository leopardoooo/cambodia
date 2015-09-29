package com.ycsoft.sysmanager.component.resource;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TBusiCmdSupplier;
import com.ycsoft.beans.config.TDeviceBuyMode;
import com.ycsoft.beans.core.common.CDoneCode;
import com.ycsoft.beans.core.common.CDoneCodeDetail;
import com.ycsoft.beans.core.cust.CCust;
import com.ycsoft.beans.core.cust.CCustDevice;
import com.ycsoft.beans.core.job.JBusiCmd;
import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.beans.depot.RDepotDefine;
import com.ycsoft.beans.device.RCard;
import com.ycsoft.beans.device.RCardModel;
import com.ycsoft.beans.device.RDevice;
import com.ycsoft.beans.device.RDeviceChange;
import com.ycsoft.beans.device.RDeviceDoneDetail;
import com.ycsoft.beans.device.RDeviceDoneDeviceid;
import com.ycsoft.beans.device.RDeviceEdit;
import com.ycsoft.beans.device.RDeviceInput;
import com.ycsoft.beans.device.RDeviceModel;
import com.ycsoft.beans.device.RDeviceOrder;
import com.ycsoft.beans.device.RDeviceOrderDetail;
import com.ycsoft.beans.device.RDeviceOutput;
import com.ycsoft.beans.device.RDeviceProcure;
import com.ycsoft.beans.device.RDeviceReclaim;
import com.ycsoft.beans.device.RDeviceSupplier;
import com.ycsoft.beans.device.RDeviceTransfer;
import com.ycsoft.beans.device.RModem;
import com.ycsoft.beans.device.RModemModel;
import com.ycsoft.beans.device.RPairCfg;
import com.ycsoft.beans.device.RStb;
import com.ycsoft.beans.device.RStbModel;
import com.ycsoft.beans.system.SCounty;
import com.ycsoft.beans.system.SDataRightType;
import com.ycsoft.beans.system.SDept;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.beans.system.SRole;
import com.ycsoft.business.commons.pojo.BusiParameter;
import com.ycsoft.business.dao.config.TBusiCmdSupplierDao;
import com.ycsoft.business.dao.config.TDeviceBuyModeDao;
import com.ycsoft.business.dao.core.common.CDoneCodeDetailDao;
import com.ycsoft.business.dao.core.job.JBusiCmdDao;
import com.ycsoft.business.dao.core.user.CUserDao;
import com.ycsoft.business.dao.resource.device.RCardDao;
import com.ycsoft.business.dao.resource.device.RDepotDefineDao;
import com.ycsoft.business.dao.resource.device.RDeviceChangeDao;
import com.ycsoft.business.dao.resource.device.RDeviceDifeenceDao;
import com.ycsoft.business.dao.resource.device.RDeviceDoneDetailDao;
import com.ycsoft.business.dao.resource.device.RDeviceDoneDeviceidDao;
import com.ycsoft.business.dao.resource.device.RDeviceEditDao;
import com.ycsoft.business.dao.resource.device.RDeviceHisDao;
import com.ycsoft.business.dao.resource.device.RDeviceInputDao;
import com.ycsoft.business.dao.resource.device.RDeviceModelDao;
import com.ycsoft.business.dao.resource.device.RDeviceOrderDao;
import com.ycsoft.business.dao.resource.device.RDeviceOrderDetailDao;
import com.ycsoft.business.dao.resource.device.RDeviceOutputDao;
import com.ycsoft.business.dao.resource.device.RDeviceProcureDao;
import com.ycsoft.business.dao.resource.device.RDeviceReclaimDao;
import com.ycsoft.business.dao.resource.device.RDeviceSupplierDao;
import com.ycsoft.business.dao.resource.device.RDeviceTransferDao;
import com.ycsoft.business.dao.resource.device.RDeviceTypeDao;
import com.ycsoft.business.dao.resource.device.RDeviceUseRecordsDao;
import com.ycsoft.business.dao.resource.device.RModemDao;
import com.ycsoft.business.dao.resource.device.RStbDao;
import com.ycsoft.business.dao.system.SCountyDao;
import com.ycsoft.business.dao.system.SDeptDao;
import com.ycsoft.business.dto.core.cust.CustFullInfoDto;
import com.ycsoft.business.service.externalImpl.ICustServiceExternal;
import com.ycsoft.commons.constants.BusiCmdConstants;
import com.ycsoft.commons.constants.BusiCodeConstants;
import com.ycsoft.commons.constants.DataRight;
import com.ycsoft.commons.constants.DataRightLevel;
import com.ycsoft.commons.constants.SequenceConstants;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ComponentException;
import com.ycsoft.commons.exception.ErrorCode;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.commons.helper.DateHelper;
import com.ycsoft.commons.helper.LoggerHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;
import com.ycsoft.sysmanager.dto.depot.RDeviceTransferDto;
import com.ycsoft.sysmanager.dto.resource.DeviceDto;
import com.ycsoft.sysmanager.dto.resource.DeviceInputDetailDto;
import com.ycsoft.sysmanager.dto.resource.DeviceProcureDto;
import com.ycsoft.sysmanager.dto.resource.DeviceReclaimDto;
import com.ycsoft.sysmanager.dto.system.RDepotDto;
import com.ycsoft.sysmanager.dto.system.SDeptDto;
import com.ycsoft.sysmanager.web.commons.interceptor.WebOptr;

/**
 * @author liujiaqi
 *
 */
@Component
public class DeviceComponent extends BaseDeviceComponent {
	private RDeviceTransferDao rDeviceTransferDao;
	private RDepotDefineDao rDepotDefineDao;
	private RDeviceSupplierDao rDeviceSupplierDao;
	private RDeviceInputDao rDeviceInputDao;
	private RDeviceDoneDetailDao rDeviceDoneDetailDao;
	private RDeviceOrderDao rDeviceOrderDao;
	private RDeviceDoneDeviceidDao rDeviceDoneDeviceidDao;
	private RDeviceHisDao rDeviceHisDao;
	private RStbDao rStbDao;
	private RCardDao rCardDao;
	private RModemDao rModemDao;
	private RDeviceOrderDetailDao rDeviceOrderDetailDao;
	private RDeviceProcureDao rDeviceProcureDao;
	private RDeviceOutputDao rDeviceOutputDao;
	private RDeviceEditDao rDeviceEditDao;
	private RDeviceReclaimDao rDeviceReclaimDao;
	private RDeviceChangeDao rDeviceChangeDao;
	private ICustServiceExternal custService;
	private TBusiCmdSupplierDao tBusiCmdSupplierDao;
	private CUserDao cUserDao;
	private RDeviceDifeenceDao rDeviceDifeenceDao;
	private CDoneCodeDetailDao cDoneCodeDetailDao;
	private RDeviceUseRecordsDao rDeviceUseRecordsDao;
	private JBusiCmdDao jBusiCmdDao;
	private SDeptDao sDeptDao;
	private TDeviceBuyModeDao tDeviceBuyModeDao;
	private SCountyDao sCountyDao;
	private RDeviceModelDao rDeviceModelDao;
	private RDeviceTypeDao rDeviceTypeDao;

	public void setRDeviceDifeenceDao(RDeviceDifeenceDao deviceDifeenceDao) {
		rDeviceDifeenceDao = deviceDifeenceDao;
	}

	public void updateDeviceLoss(String deviceId,String isLossed,SOptr optr) throws Exception {
		RDevice rd = rDeviceDao.findByKey(deviceId);
		rDeviceDao.updateDeviceLoss(deviceId, isLossed);
		saveDeviceChange(gDoneCode(), BusiCodeConstants.DEVICE_LOSS, rd
				.getDevice_id(), "is_loss", rd.getIs_loss(), isLossed, optr);
	}
	
	//查询空闲的设备
	public Pager<DeviceDto> queryIdleDevice(String deviceCode, SOptr optr,
			Integer start, Integer limit) throws Exception {
		String depotId = findDepot(optr);
		return rDeviceDao.queryIdleDevice(deviceCode, depotId, start, limit);
	}
	
	public String saveLossDevice(String deviceCode, SOptr optr) throws Exception {
		RDevice rd = rDeviceDao.findByDeviceCode(deviceCode);
		
		String depotId = findDepot(optr);
		RDevice device = rDeviceDao.queryByDeviceCode(deviceCode);
		checkDevice(depotId, device,deviceCode,false);
		
		rDeviceDao.updateDeviceLoss(rd.getDevice_id(), SystemConstants.BOOLEAN_TRUE);
		
		saveDeviceChange(gDoneCode(), BusiCodeConstants.DEVICE_LOSS, rd
				.getDevice_id(), "is_loss", rd.getIs_loss(),
				SystemConstants.BOOLEAN_TRUE, optr);
		return rd.getDevice_type();
	}
	
	private void saveDeviceChange(Integer doneCode, String busiCode,
			String deviceId, String columnName, String oldValue,
			String newValue, SOptr optr) throws Exception {
		RDeviceChange chg = new RDeviceChange();
		chg.setDone_code(doneCode);
		chg.setBusi_code(busiCode);
		chg.setDevice_id(deviceId);
		chg.setColumn_name(columnName);
		chg.setOld_value(oldValue);
		chg.setNew_value(newValue);
		chg.setChange_date(DateHelper.now());
		chg.setArea_id(optr.getArea_id());
		chg.setCounty_id(optr.getCounty_id());
		chg.setOptr_id(optr.getOptr_id());
		chg.setDept_id(optr.getDept_id());
		
		rDeviceChangeDao.save(chg);
	}
	
	/**
	 * 修改退库单号
	 * @param deviceDoneCode
	 * @param remark 
	 * @param procureNo
	 * @throws Exception
	 */
	public void editOutputNo(Integer deviceDoneCode,String outputNo, String remark) throws Exception {
		rDeviceOutputDao.editOutputNo(deviceDoneCode, outputNo,remark);
	}
	
	/**
	 * 修改领用单号
	 * @param deviceDoneCode
	 * @param procureNo
	 * @throws Exception
	 */
	public void editProcureNo(Integer deviceDoneCode,String procureNo) throws Exception {
		rDeviceProcureDao.editProcureNo(deviceDoneCode, procureNo);
	}
	
	/**
	 * 修改待回收设备状态
	 * @param doneCode
	 * @param status
	 * @throws Exception
	 */
	public void updateDeviceReclaimStatus(Integer doneCode, String deviceId,String status) throws Exception {
		rDeviceReclaimDao.updateStatus(doneCode, deviceId, status);
	}
	
	public void saveDoneCode(Integer doneCode, String busiCode,String custId,SOptr optr) throws Exception{
		CDoneCode cDoneCode = new CDoneCode();
		cDoneCode.setDone_code(doneCode);
		cDoneCode.setBusi_code(busiCode);
		cDoneCode.setStatus(StatusConstants.ACTIVE);
		cDoneCode.setArea_id(optr.getArea_id());
		cDoneCode.setCounty_id(optr.getCounty_id());
		cDoneCode.setDept_id(optr.getDept_id());
		cDoneCode.setOptr_id(optr.getOptr_id());
		cDoneCodeDao.save(cDoneCode);
		
		if (StringHelper.isNotEmpty(custId)){
			CDoneCodeDetail detail = new CDoneCodeDetail();
			detail.setDone_code(doneCode);
			detail.setCust_id(custId);
			detail.setArea_id(optr.getArea_id());
			detail.setCounty_id(optr.getCounty_id());
			cDoneCodeDetailDao.save(detail);
		}
	}
	
	/**
	 * 取消确认
	 * @param doneCode
	 * @param deviceId
	 * @throws Exception
	 */
	public void cancelReclaimDevice(Integer doneCode, String deviceId,SOptr optr) throws Exception {
		RDevice stbDevice = rDeviceDao.findByKey(deviceId);
		stbDevice.setDepot_status(StatusConstants.USE);
		rDeviceDao.update(stbDevice);
		rDeviceReclaimDao.updateReclaimDevice(doneCode, deviceId, StatusConstants.UNCONFIRM, null, null);
		saveDeviceChange(doneCode, BusiCodeConstants.CANCEL_RECLAIM_DEVICE,deviceId , "depot_status", StatusConstants.IDLE,StatusConstants.USE,optr);	
		RStb stb = rStbDao.findByKey(deviceId);
		if(stb != null){
			if(stb.getPair_card_id()!=null){
				RDevice card = rDeviceDao.findByKey(stb.getPair_card_id());
				card.setDepot_status(StatusConstants.USE);
				rDeviceDao.update(card);
				rDeviceReclaimDao.updateReclaimDevice(doneCode, stb.getPair_card_id(), StatusConstants.UNCONFIRM, null, null);
				saveDeviceChange(doneCode, BusiCodeConstants.CANCEL_RECLAIM_DEVICE,stb.getPair_card_id() , "depot_status", StatusConstants.IDLE,StatusConstants.USE,optr);
			}
			if(stb.getPair_modem_id()!=null){
				RDevice modem = rDeviceDao.findByKey(stb.getPair_modem_id());
				modem.setDepot_status(StatusConstants.USE);
				rDeviceDao.update(modem);
				rDeviceReclaimDao.updateReclaimDevice(doneCode, stb.getPair_modem_id(), StatusConstants.UNCONFIRM, null, null);
				saveDeviceChange(doneCode, BusiCodeConstants.CANCEL_RECLAIM_DEVICE,stb.getPair_modem_id() , "depot_status", StatusConstants.IDLE,StatusConstants.USE,optr);
			}
		}else{
			RStb cardStb =  rStbDao.findPairStbByCardDeviceId(deviceId);
			if(cardStb != null){
				cardStb.setDepot_status(StatusConstants.USE);
				rDeviceDao.update(cardStb);
				if(cardStb.getPair_modem_id()!=null){
					RDevice modem = rDeviceDao.findByKey(cardStb.getPair_modem_id());
					modem.setDepot_status(StatusConstants.USE);
					rDeviceDao.update(modem);
					rDeviceReclaimDao.updateReclaimDevice(doneCode, cardStb.getPair_modem_id(), StatusConstants.UNCONFIRM, null, null);
					saveDeviceChange(doneCode, BusiCodeConstants.CANCEL_RECLAIM_DEVICE,cardStb.getPair_modem_id() , "depot_status", StatusConstants.IDLE,StatusConstants.USE,optr);
				}
			}
			RStb modemStb =  rStbDao.findPairStbByModemDeviceId(deviceId);
			if(modemStb != null){
				modemStb.setDepot_status(StatusConstants.USE);
				rDeviceDao.update(modemStb);
				if(modemStb.getPair_card_id()!=null){
					RDevice card = rDeviceDao.findByKey(modemStb.getPair_card_id());
					card.setDepot_status(StatusConstants.USE);
					rDeviceDao.update(card);
					rDeviceReclaimDao.updateReclaimDevice(doneCode, modemStb.getPair_card_id(), StatusConstants.UNCONFIRM, null, null);
					saveDeviceChange(doneCode, BusiCodeConstants.CANCEL_RECLAIM_DEVICE,modemStb.getPair_card_id() , "depot_status", StatusConstants.IDLE,StatusConstants.USE,optr);
				}
			}
		}
		CCustDevice custDevice = rDeviceDao.findCustDeviceByDeviceId(deviceId);
		if(custDevice != null){
			saveDoneCode(gDoneCode(), BusiCodeConstants.CANCEL_RECLAIM_DEVICE, custDevice.getCust_id(), optr);
		}
	}
	
	/**
	 * 配对的设备,如果配对的不是虚拟的,一并处理了.
	 * 确认是否回收
	 * @param doneCode
	 * @param deviceId
	 * @param deviceStatus 设备状态 正常、损坏
	 * @param flag Boolean 是否回收
	 */
	public void reclaimDevice(SOptr optr, Integer doneCode, String deviceId,
			String deviceStatus, String flag,String isNewStb) throws Exception {
		
		List<String> deviceIds = new ArrayList<String>();
		RDevice rd = rDeviceDao.findByDeviceId(deviceId);
		com.ycsoft.business.dto.device.DeviceDto deviceDto = null;
		if (rd.getDevice_type().equals(SystemConstants.DEVICE_TYPE_CARD)) {
			RStb stb = rStbDao.findPairStbByCardDeviceId(rd.getDevice_id());
			if (stb != null) {
				deviceDto = queryDeviceByDeviceId(stb.getDevice_id());
			}
		}else if (rd.getDevice_type().equals(SystemConstants.DEVICE_TYPE_MODEM)) {
			RStb stb = rStbDao.findPairStbByModemDeviceId(rd.getDevice_id());
			if (stb != null) {
				deviceDto = queryDeviceByDeviceId(stb.getDevice_id());
			}
		}else{
			deviceDto = queryDeviceByDeviceId(rd);
		}
		deviceIds.add(rd.getDevice_id());
		if(deviceDto != null){
			RCard pairCard = deviceDto.getPairCard();
			if(null !=pairCard && SystemConstants.BOOLEAN_FALSE.equals(pairCard.getIs_virtual()) ){
				deviceIds.add(pairCard.getDevice_id());
			}
			
			RModem pairModem = deviceDto.getPairModem();
			if(null !=pairModem && SystemConstants.BOOLEAN_FALSE.equals(pairModem.getIs_virtual())){
				deviceIds.add(pairModem.getDevice_id());
			}
		}
				
		if(flag.equals(SystemConstants.BOOLEAN_TRUE)){
			RDeviceReclaim dr = rDeviceReclaimDao.queryReclaimDevice(doneCode, deviceId);
			CCust cust = new CCust();
			cust.setCust_id(dr.getCust_id());
			CustFullInfoDto custFull = new CustFullInfoDto();
			custFull.setCust(cust);
			
			BusiParameter p = new BusiParameter();
			p.setBusiCode(BusiCodeConstants.CONFIRM_RECLAIM_DEVICE);
			p.setCustFullInfo(custFull);
			p.setOptr(optr);
			
			for(String devId:deviceIds){			
				rDeviceReclaimDao.updateReclaimConfirm(doneCode, devId, optr.getOptr_id());
			}
			custService.saveCancelDevice(p,deviceId, deviceStatus);
			
			rDeviceDao.updateIsNewStb(deviceIds.toArray(new String[deviceIds.size()]),isNewStb);
			//变更新机的记录
			rDeviceChangeDao.saveDeviceChangeIsNewStb(doneCode,BusiCodeConstants.CONFIRM_RECLAIM_DEVICE,deviceIds.toArray(new String[deviceIds.size()]),isNewStb,WebOptr.getOptr().getOptr_id()
					,WebOptr.getOptr().getDept_id(),WebOptr.getOptr().getCounty_id(),WebOptr.getOptr().getArea_id());

			if(cust != null){
				saveDoneCode(gDoneCode(), BusiCodeConstants.CONFIRM_RECLAIM_DEVICE, cust.getCust_id(), optr);
			}
		}else if(flag.equals(SystemConstants.BOOLEAN_FALSE)){
			for(String devId:deviceIds){
				updateDeviceReclaimStatus(doneCode, devId, StatusConstants.NOTCONFIRM);
			}
		}
	}
	
	public com.ycsoft.business.dto.device.DeviceDto queryDeviceByDeviceId(String deviceId) throws JDBCException {
		RDevice rd = rDeviceDao.findByDeviceId(deviceId);
		if (rd == null)
			return null;
		return queryDeviceByDeviceId(rd);
	}
	
	public com.ycsoft.business.dto.device.DeviceDto queryDeviceByDeviceId(RDevice rd) throws JDBCException {
		RDeviceModel deviceModel = null;
		String deviceCode = null;
		com.ycsoft.business.dto.device.DeviceDto dto = new com.ycsoft.business.dto.device.DeviceDto();
		BeanUtils.copyProperties(rd, dto);

		if (rd.getDevice_type().equals(SystemConstants.DEVICE_TYPE_STB)) {
			deviceModel = rStbModelDao.findByDeviceId(rd.getDevice_id());
			deviceCode = rStbDao.findByKey(rd.getDevice_id()).getStb_id();
			// 设置配对信息
			dto.setPairCard(rCardDao.findPairCardByStbDeviceId(rd.getDevice_id()));
			dto.setPairModem(rModemDao.findPairModemByStbDeviceId(rd.getDevice_id()));
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
	
	/**
	 * 查询待回收设备信息，支持设备编号模糊查询
	 * @param optrId
	 * @param query
	 * @param startDate
	 * @param endDate
	 * @param start
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public Pager<DeviceReclaimDto> queryDeviceReclaim(SOptr optr, String query,
			String startDate, String endDate, String deviceType,String confirmType,String isHistory,Integer start, Integer limit)
			throws Exception {
		String depotId = findDepot(optr);
		return rDeviceReclaimDao.queryDeviceReclaim(depotId, query, startDate, endDate,deviceType,confirmType,isHistory, start, limit);
	}
	
	public void editInputNo(Integer deviceDoneCode,String inputNo, String remark) throws Exception {
		rDeviceInputDao.editInputNo(deviceDoneCode, inputNo,remark);
	}
	
	public void editTransferNo(Integer deviceDoneCode,String transferNo, String remark) throws Exception {
		rDeviceTransferDao.editTransferNo(deviceDoneCode, transferNo,remark);
	}
	
	public void editDeviceOrder(Integer deviceDoneCode,String isHistory) throws Exception {
		rDeviceOrderDao.editDeviceOrder(deviceDoneCode, isHistory);
	}
	
	public void editDeviceTransferHistory(Integer deviceDoneCode,String isHistory) throws Exception {
		rDeviceTransferDao.editDeviceTransferHistory(deviceDoneCode, isHistory);
	}
	
	public List<DeviceDto> queryDeviceInfo(List<DeviceDto> devices,
			String deviceType) throws Exception {
		String[] deviceCodes = CollectionHelper.converValueToArray(devices, "device_code");
		List<DeviceDto> deviceList = rDeviceDao.queryByDeviceCodescount(deviceCodes,deviceType);
		
		//文件设备编号数量 和 通过设备编号查询出的设备数量不相等时
		if(deviceList != null && devices.size() > deviceList.size()){
			//文件中device_code集合
			List<String> fileDevices = CollectionHelper.converValueToList(devices, "device_code");
			//通过文件中device_code查询出来的device_code集合
			List<String> queryDevices = CollectionHelper.converValueToList(deviceList, "device_code");
			
			//删除在库的设备编号，保留不在库的设备编号
			fileDevices.removeAll(queryDevices);
			
			
			if(fileDevices.size()>0){
				int size = deviceList.size();//在库的设备集合
				int len = fileDevices.size();//不在库的设备集合
				
				if(size >= len ){
					for(int i=0;i<size;i++){
						if(i <= len-1){
							deviceList.get(i).setWrong_device_code(fileDevices.get(i));
						}
					}
				}else{
					for(int i=0;i<len;i++){
						if(i <= size-1){
							deviceList.get(i).setWrong_device_code(fileDevices.get(i));
						}else{
							DeviceDto dto = new DeviceDto();
							dto.setWrong_device_code(fileDevices.get(i));
							deviceList.add(dto);
						}
					}
				}
			}
		}else if(deviceList != null && devices.size() < deviceList.size()){
			deviceList = new ArrayList<DeviceDto>();
			List<String> list = CollectionHelper.converValueToList(devices, "device_code");
			for(int i=0;i<list.size();i++){
				DeviceDto dto = new DeviceDto();
				dto.setWrong_device_code(list.get(i));
				deviceList.add(dto);
			}
		}
		
		return deviceList;
	}
	
	/**
	 * 所有调拨明细，供下载excel
	 * @param deviceDoneCode
	 * @param deviceType
	 * @param deviceModel
	 * @return
	 * @throws JDBCException
	 */
	public List<RDeviceTransferDto> queryAllTransferDeviceDtail(
			int deviceDoneCode, String deviceType, String deviceModel)
			throws JDBCException {
		return rDeviceTransferDao.queryAllTransferDeviceDtail(deviceDoneCode,
				deviceType, deviceModel);
	}
	
	/**
	 * 查询调拨详细信息
	 * @param deviceDoneCode
	 * @param deviceType
	 * @param start
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public Pager<DeviceDto> queryTransferDeviceDetail(
			int deviceDoneCode, String deviceType,Integer start, Integer limit) throws Exception {
		return rDeviceDao.queryTransferDeviceDetail(deviceDoneCode,deviceType,start, limit);
	}
	
	/**
	 * 根据设备类型查询型号
	 * @param deviceType
	 * @param modemType 
	 * @return
	 * @throws Exception
	 */
	public List<RDeviceModel> queryDeviceModelByType(String deviceType, String modemType) throws Exception {
		return  rDeviceDao.queryDeviceModelByType(deviceType,modemType);
	}
	
	/**
	 * 根据设备编号查询设备及客户信息
	 * @param deviceCode
	 * @return
	 * @throws Exception
	 */
	public DeviceDto queryDeviceInfoByCode(String deviceCode) throws Exception {
		DeviceDto dev = rDeviceDao.queryDeviceInfoByCode(deviceCode);
		
		if(null == dev){
			throw new ComponentException("查询设备不存在!");
		}
		String deviceId = dev.getDevice_id();
		String devId = dev.getDevice_id();
		if(dev.getDevice_type().equals(SystemConstants.DEVICE_TYPE_CARD)){
			RStb stb = rStbDao.findPairStbByCardDeviceId(dev.getDevice_id());
			if(null != stb){
				devId = stb.getDevice_id();
				dev.setPair_device_stb_code(stb.getStb_id());
				dev.setPair_device_stb_model(stb.getDevice_model());
			}
		}else if(dev.getDevice_type().equals(SystemConstants.DEVICE_TYPE_MODEM)){
			RStb stb = rStbDao.findPairStbByCardDeviceId(dev.getDevice_id());
			if(null != stb){
				devId = stb.getDevice_id();
				dev.setPair_device_stb_code(stb.getStb_id());
				dev.setPair_device_stb_model(stb.getDevice_model());
			}
		}
		
		
		//设备入库信息
		dev.setDeviceInput(rDeviceInputDao.queryByDeviceId(deviceId));
		
		//设备调拨信息
		dev.setDeviceTransferList(rDeviceTransferDao.queryByDeviceId(devId));
		
		dev.setDeviceUseRecordsList(rDeviceUseRecordsDao.queryUseRecordByDeviceCode(deviceCode));
		return dev;
	}
	
	/**
	 * 查询可以进行机卡配对配置.
	 * @param stb_model
	 * @return
	 * @throws Exception
	 */
	public List<RCardModel> queryIdelCardModel(String stb_model) throws Exception{
		return rPairCfgDao.queryIdelCardModel(stb_model );
	}
	
	/**
	 * 查询已配置过的机卡配对配置.
	 * @param stb_model
	 * @return
	 * @throws Exception
	 */
	public List<RCardModel> queryStbCardPaired(String stb_model) throws Exception{
		return rPairCfgDao.queryStbCardPaired(stb_model );
	}
	
	/**
	 * 保存机卡配对配置.
	 * @param list
	 */
	public void saveStbCardPairCfg(String stb_model,String[] cardModes) throws Exception{
		rPairCfgDao.removeOld(stb_model);//旧的删除
		if(cardModes != null){
			RPairCfg [] cfgs = new RPairCfg[cardModes.length];
			for(int index = 0;index<cardModes.length;index++){
				RPairCfg cfg = new RPairCfg();
				String card_model = cardModes[index];
				cfg.setCard_model(card_model );
				cfg.setStb_model(stb_model);
				cfgs[index] = cfg;
			}
			rPairCfgDao.save(cfgs);
		}
	}
	
	/**
	 * 多条件查询
	 * @param stbModel
	 * @param cardModel
	 * @param modemModel
	 * @param depotId
	 * @param status
	 * @param mode
	 * @param modemType 
	 * @return
	 * @throws Exception
	 */
	public Pager<DeviceDto> queryDeviceByMultiCriteria(String deviceModel,String depotId,String status,
			String mode,String depotStatus,String modemType, String backup, String batch_num,String start_input_time,
			String end_input_time, Integer start,Integer limit) 
		throws Exception {
		return rDeviceDao.queryDeviceByMultiCriteria(deviceModel, depotId,
				status, mode, depotStatus, modemType, backup,batch_num,start_input_time,end_input_time,start, limit);
	}
	
	/**
	 * 多条件查询
	 */
	public List<DeviceDto> queryDeviceByBatch(String depotId, String batch_num)throws Exception{
		return rDeviceDao.queryDeviceByBatch(depotId, batch_num);
	}
	
	public List<DeviceDto> queryIDLEDeviceByMultiCriteria(String deviceModel,String depotId,String status,
			String mode,String depotStatus,String backup) throws Exception {
		return rDeviceDao.queryIDLEDeviceByMultiCriteria(deviceModel, depotId,
				status, mode, depotStatus,backup);
	}
	
	
	public List<DeviceDto> queryDeviceDetailByMultiCriteria(String deviceModel, String depotId, String status,
			String mode, String depotStatus, String modemType, String backup, String batch_num, String start_input_time,
			String end_input_time) throws Exception {
		return rDeviceDao.queryDeviceByMultiCriteria(deviceModel, depotId, status, mode, depotStatus, modemType, backup, batch_num, start_input_time, end_input_time);
	}
	/**
	 * 检查盒号是否存在
	 * @param cardId
	 * @return
	 * @throws Exception
	 */
	public boolean isExistsStb(String stbId) throws Exception {
		return rStbDao.isExistsStb(stbId);
	}

	/**
	 * 检查卡号是否存在
	 * @param cardId
	 * @return
	 * @throws Exception
	 */
	public boolean isExistsCard(String cardId) throws Exception {
		return rCardDao.isExistsCard(cardId);
	}
	
	
	public boolean isExistsStbCard(String cardId,String stbId) throws Exception {
		String depotId = findDepot(WebOptr.getOptr());
		RDevice device = rDeviceDao.queryByDeviceCode(cardId);
		checkDevice(depotId, device,cardId,false);
		
		RDevice stbDevice = rDeviceDao.queryByDeviceCode(stbId);
		checkDevice(depotId, stbDevice,stbId,false);
		return true;
	}
	
	
	public boolean isExistsModem(String modemMac) throws Exception {
		return rModemDao.isExistsModem(modemMac);
	}

	/**
	 * 查询往日入库未确认
	 *
	 * @param optrId
	 * @return
	 * @throws Exception
	 * @throws ComponentException
	 */
	public List<RDeviceTransfer> queryUnCheckInput(SOptr optr)
			throws Exception {
		return rDeviceTransferDao.queryInput( findDepot(optr),
				StatusConstants.UNCONFIRM, false);
	}

	/**
	 * 查询往日出库未确认
	 *
	 * @param optrId
	 * @return
	 * @throws Exception
	 * @throws Exception
	 * @throws ComponentException
	 */
	public List<RDeviceTransfer> queryUnCheckOutput(SOptr optr)
			throws Exception {
		return rDeviceTransferDao.queryOutput( findDepot(optr),
				StatusConstants.UNCONFIRM, false);
	}

	/**
	 * 查询当天入库库确认
	 *
	 * @param optrId
	 * @return
	 * @throws Exception
	 * @throws ComponentException
	 */
	public List<RDeviceTransfer> queryTodayCheckInput(SOptr optr)
			throws Exception {
		return rDeviceTransferDao.queryInput( findDepot(optr),
				StatusConstants.ACTIVE, true);
	}

	/**
	 * 查询当天出库确认
	 *
	 * @param optrId
	 * @return
	 * @throws Exception
	 * @throws Exception
	 * @throws ComponentException
	 */
	public List<RDeviceTransfer> queryTodayCheckOutput(SOptr optr)
			throws Exception {
		return rDeviceTransferDao.queryOutput(findDepot(optr),
				StatusConstants.ACTIVE, true);
	}

	/**
	 * 获取所有的供应商
	 *
	 * @return
	 * @throws Exception
	 */
	public List<RDeviceSupplier> queryDeviceSupplier() throws Exception {
		return rDeviceSupplierDao.findAll();
	}

	/**
	 * 查找设备入库信息
	 *
	 * @param optrId
	 * @return
	 * @throws Exception
	 * @throws ComponentException
	 */
	public Pager<RDeviceInput> queryDeviceInput(SOptr optr,String query,Integer start,Integer limit)
			throws Exception {
		String depotId = findDepot(optr);
		return rDeviceInputDao.queryByDepotId(depotId,query,start,limit);
	}
	
	/**
	 * 查询设备修改信息(设备状态修改信息)
	 * @param optrId
	 * @return
	 * @throws Exception
	 */
	public Pager<RDeviceEdit> queryDeviceStatusEdit(SOptr optr,
			String query, Integer start, Integer limit) throws Exception {
		String depotId = findDepot(optr);
		return rDeviceEditDao.queryByDepotIdAndEditType(depotId,
				SystemConstants.DEVICE_EDIT_TYPE_STATUS, query, start, limit);
	}
	
	public Pager<DeviceDto> queryEditDeviceDetail(Integer deviceDoneCode, String deviceType,
			Integer start, Integer limit, String optrId) throws Exception {
		return rDeviceEditDao.queryDeviceDetail(deviceDoneCode, deviceType, start, limit);
	}
	
	public Pager<DeviceDto> queryOutputDeviceDetail(Integer deviceDoneCode, String deviceType,String deviceModel,
			Integer start, Integer limit, String optrId) throws Exception {
		return rDeviceOutputDao.queryDeviceDetail(deviceDoneCode, deviceType,deviceModel, start, limit);
	}

	/**
	 * 查询设备操作明细
	 *
	 * @param deviceDoneCode
	 * @return
	 * @throws Exception
	 */
	public List<RDeviceDoneDetail> queryDeviceDoneDetail(int deviceDoneCode)
			throws Exception {
		return rDeviceDoneDetailDao.queryByDeviceDoneCode(deviceDoneCode);
	}

	/**
	 * 查询待收货设备订单
	 *
	 * @param optrId
	 * @return
	 * @throws Exception
	 * @throws ComponentException
	 */
	public List<RDeviceOrder> queryUnSupplierDeviceOrder(SOptr optr)
			throws Exception {
		String depotId = findDepot(optr);
		return rDeviceOrderDao.queryUnInputByDepot(depotId);
	}

	private RDeviceChange saveDeviceChange(Integer doneCode, String busiCode,String deviceId, String columnName, String oldValue,
			String newValue,String pairCard,String pairModem, SOptr optr) throws Exception {
		RDeviceChange chg = new RDeviceChange();
		chg.setDone_code(doneCode);
		chg.setBusi_code(busiCode);
		chg.setDevice_id(deviceId);
		chg.setColumn_name(columnName);
		chg.setOld_value(oldValue);
		chg.setNew_value(newValue);
		chg.setPair_card_id(pairCard);
		chg.setPair_modem_id(pairModem);
		chg.setChange_date(DateHelper.now());
		chg.setArea_id(optr.getArea_id());
		chg.setCounty_id(optr.getCounty_id());
		chg.setOptr_id(optr.getOptr_id());
		chg.setDept_id(optr.getDept_id());
		return 	chg;
	}
	
	
	/**
	 * 保存入库信息 (文件)
	 * @param optrId
	 * @param input
	 * @param deviceModel 
	 * @param batchNum 
	 * @param devices
	 * @throws Exception
	 */
	public void saveDeviceInputFile(SOptr optr, RDeviceInput input,
			List<DeviceDto> deviceDtoList,String deviceType, String deviceModel, String batchNum) throws Exception, ComponentException{
		List<DeviceDto> devices = new ArrayList<DeviceDto>();
		for(DeviceDto d : deviceDtoList){
			d.setDevice_type(deviceType);
			d.setDevice_model(deviceModel);
			d.setBatch_num(batchNum);
			devices.add(d);
		}
		String[] deviceCodes = null;
		if(deviceType.equals(SystemConstants.DEVICE_TYPE_MODEM)){
			deviceCodes = CollectionHelper.converValueToArray(devices, "modem_mac");
		}else{
			deviceCodes = CollectionHelper.converValueToArray(devices, "device_code");
		}
		if(deviceCodes.length==0){
			throw new ComponentException("文件中设备编号都是空的!");
		}
		List<DeviceDto> deviceList = rDeviceDao.queryByDeviceCodescountIncludeWrongData(deviceCodes,deviceType);
		if (deviceList.size()>0){
			String str = "";
			int cout = 1;
			if(deviceList.size()>2){
				cout = 2 ;
			}
			for(int i=0;i<cout;i++){
				str += deviceList.get(i).getDevice_code()+",";
			}
			str = StringHelper.delEndChar( str ,  1 );
			throw new ComponentException("库里已存在的设备共:"+deviceList.size()+"个,明细:"+str+",...");
		}
		saveDeviceInput(optr, input, devices,SystemConstants.DEVICE_CFG_TYPE_FILE);
	}

	
	
	
	/**
	 * 保存入库信息
	 * @param optrId
	 * @param input
	 * @param devices
	 * @throws Exception
	 */
	public void saveDeviceInput(SOptr optr, RDeviceInput input,
			List<DeviceDto> devices,String deviceType) throws Exception ,ComponentException{
		if(devices.size()==0){
			throw new ComponentException("无可入库的设备");
		}
		
		checkDeviceOperRight(optr,devices);
		//验证设备是否重复
		checkDeviceIsNull(CollectionHelper.converValueToArray(devices, "device_code"),"设备号");
		//验证卡是否重复
		checkDeviceIsNull(CollectionHelper.converValueToArray(devices, "pair_device_code"),"智能卡号");
		//验证猫是否重复
		checkDeviceIsNull(CollectionHelper.converValueToArray(devices, "modem_mac"),"modem_mac");
		
		String depotId = findDepot(optr);
		Integer orderDoneCode = input.getOrder_done_code();
		if(orderDoneCode != null){
			List<RDeviceOrderDetail> orderList = rDeviceOrderDetailDao.queryByDoneCode(orderDoneCode);
			int orderNum = 0;//订单数
			
			for(RDeviceOrderDetail orderDetail : orderList){
				orderNum += orderDetail.getOrder_num();
			}
			
			int inputNum = devices.size();//入库数
			//到货数应当小于等于订货数
			if(inputNum > orderNum){
				throw new ComponentException("入库数应当小于等于订货数！订货数："
						+ orderNum + "，入库数：" + inputNum);
			}
		}
		Integer doneCode = gDoneCode();
		input.setDevice_done_code(doneCode);
		input.setOptr_id(optr.getOptr_id());
		input.setDepot_id(depotId);
		input.setCreate_time(DateHelper.now());
		input.setBatch_num(devices.get(0).getBatch_num());

		List<RDeviceDoneDeviceid> doneDeviceidList = new ArrayList<RDeviceDoneDeviceid>();
		List<RDevice> deviceList = new ArrayList<RDevice>();
		List<RStb> stbList = new ArrayList<RStb>();
		List<RCard> cardList = new ArrayList<RCard>();
		List<RModem> modemList = new ArrayList<RModem>();

		Map<String ,RStbModel> stbModelList = CollectionHelper.converToMapSingle(rStbModelDao.findAll(), "device_model");
		Map<String, RCardModel> cardModelList = CollectionHelper.converToMapSingle(rCardModelDao.findAll(), "device_model");
		Map<String ,RModemModel> modemModelList = CollectionHelper.converToMapSingle(rModemModelDao.findAll(), "device_model");
		
//		List<RPairCfg> pairs = rPairCfgDao.findAll();
		for (DeviceDto d : devices) {
			String deivceId = gDeviceId();
			if(d.getDevice_type().equals(SystemConstants.DEVICE_TYPE_STB) ||
					d.getDevice_type().equals(SystemConstants.DEVICE_TYPE_CARD) ||
					d.getDevice_type().equals(SystemConstants.DEVICE_TYPE_MODEM)){
				checkDeviceCodeNum(d);
			}
			
			if (d.getDevice_type().equals(SystemConstants.DEVICE_TYPE_STB)) {
				//fillDeviceModel(stbModelList,cardModelList,modemModelList,pairs,d);
				if(StringHelper.isEmpty(d.getDevice_code()))
					throw new ComponentException("设备编号存在空值!");
				RStb stb = new RStb();
				stb.setDevice_id(deivceId);
				stb.setStb_id(d.getDevice_code());
				stb.setDevice_model(d.getDevice_model());
				
				RStbModel stbCfg = stbModelList.get(d.getDevice_model());
				
				//根据配置信息取已经配置了的配置型号,
				String pairCardModel = stbCfg.getVirtual_card_model();//与当前机顶盒型号配对的虚拟智能卡类型 
				//jpz 机顶盒管理mac号的
				stb.setMac(d.getModem_mac());
				
//				String pairModemModel = stbCfg.getVirtual_modem_model();//与当前机顶盒型号配对的虚拟猫类型
//				
//				if(StringHelper.isNotEmpty(d.getModem_mac())  ){
//					if(modemModelList.get(pairModemModel) == null){
//						throw new ComponentException("虚拟猫的设备类型错误：" + MemoryDict.getDictName(DictKey.MODEM_MODEL, pairModemModel) );
//					}
//					String modeMeivceId = gDeviceId();
//					stb.setPair_modem_id(modeMeivceId);
//					// 猫设备信息
//					RDevice modemDevice = new RDevice(
//							SystemConstants.DEVICE_TYPE_MODEM,
//							StatusConstants.ACTIVE, StatusConstants.IDLE);
//					modemDevice.setDevice_id(modeMeivceId);
//					modemDevice.setDevice_model(pairModemModel);
//					modemDevice.setDepot_id(depotId);
//					modemDevice.setOwnership(input.getOwnership());
//					modemDevice.setOwnership_depot(depotId);
//					modemDevice.setIs_virtual(modemModelList.get(pairModemModel).getIs_virtual());
//					deviceList.add(modemDevice);
//					// 猫信息
//					RModem modem = new RModem();
//					modem.setDevice_id(modeMeivceId);
//					modem.setModem_mac(d.getModem_mac().replace(":","").replace("：", ""));
//					modem.setDevice_model(pairModemModel);
//					modem.setModem_type(modemModelList.get(pairModemModel).getModem_type());
//					modemList.add(modem);
//					
//				}
				
				if (StringHelper.isNotEmpty(d.getPair_device_code())) {
//					if (cardModelList.get(d.getPair_device_model())==null)
//						throw new ComponentException("错误的设备类型：" + d.getPair_device_model_text()+",请检查该型号是否适用当前地区");
					if(StringHelper.isEmpty(pairCardModel)){
						throw new ComponentException(ErrorCode.DevicePairModelNotExists,d.getDevice_model());
					}
					// 有配对卡，保存配对卡
					String carDeivceId = gDeviceId();
					stb.setPair_card_id(carDeivceId);
					// 卡设备信息
					RDevice cardDevice = new RDevice(
							SystemConstants.DEVICE_TYPE_CARD,
							StatusConstants.ACTIVE, StatusConstants.IDLE);
					cardDevice.setDevice_id(carDeivceId);
					cardDevice.setDevice_model(pairCardModel);
					cardDevice.setDepot_id(depotId);
					cardDevice.setOwnership(input.getOwnership());
					cardDevice.setOwnership_depot(depotId);
					cardDevice.setIs_virtual(cardModelList.get(pairCardModel)!=null?cardModelList.get(pairCardModel).getIs_virtual():SystemConstants.BOOLEAN_FALSE);
					deviceList.add(cardDevice);
					// 卡信息
					RCard card = new RCard();
					card.setDevice_id(carDeivceId);
					card.setCard_id(d.getPair_device_code());
					card.setDevice_model(pairCardModel);
					cardList.add(card);
					
					//对机卡发送灌装指令
					JBusiCmd cmd = new JBusiCmd();

					cmd.setJob_id(Integer.parseInt(jBusiCmdDao.findSequence(SequenceConstants.SEQ_JOB_ID).toString()));
					cmd.setDone_code(doneCode);
					cmd.setCard_id(d.getPair_device_code());
					cmd.setStb_id(d.getDevice_code());
					cmd.setOptr_id(optr.getOptr_id());
					cmd.setDept_id(optr.getDept_id());
					cmd.setCounty_id(optr.getCounty_id());
					cmd.setArea_id(optr.getArea_id());
					cmd.setBusi_cmd_type(BusiCmdConstants.ACCTIVATE_TERMINAL);
					cmd.setCreate_time(DateHelper.now());

					jBusiCmdDao.save(cmd);

					cmd.setJob_id(Integer.parseInt(jBusiCmdDao.findSequence(SequenceConstants.SEQ_JOB_ID).toString()));
					cmd.setBusi_cmd_type(BusiCmdConstants.STB_FILLED);
					cmd.setCreate_time(DateHelper.now());

					jBusiCmdDao.save(cmd);
					
				}
				
				stbList.add(stb);
			} else if (d.getDevice_type().equals(SystemConstants.DEVICE_TYPE_CARD)) {
				RCardModel model = cardModelList.get(d.getDevice_model());
				if(StringHelper.isEmpty(d.getDevice_code()))
					throw new ComponentException("设备编号存在空值!");
				if (model == null)
					throw new ComponentException("错误的设备类型：" + d.getDevice_model()+",请检查该型号是否适用当前地区");
				RCard card = new RCard();
				card.setDevice_id(deivceId);
				card.setCard_id(d.getDevice_code());
				card.setDevice_model(d.getDevice_model());
				cardList.add(card);
			} else if (d.getDevice_type().equals(SystemConstants.DEVICE_TYPE_MODEM)) {
				RModemModel model = modemModelList.get(d.getDevice_model());
				if (model == null)
					throw new ComponentException("错误的设备类型：" + d.getDevice_model());
				if(StringHelper.isEmpty(d.getModem_mac()))
					throw new ComponentException("Modem_mac编号存在空值!");
				RModem modem = new RModem();
				modem.setDevice_id(deivceId);
				modem.setDepot_id(depotId);
				modem.setModem_id(d.getDevice_code());
				modem.setModem_mac(d.getModem_mac().replace(":","").replace("：", ""));
				modem.setDevice_model(d.getDevice_model());
				modem.setModem_type(model.getModem_type());
				modemList.add(modem);
			}
			RDevice device = new RDevice(d.getDevice_type(), StatusConstants.ACTIVE,
					StatusConstants.IDLE);
			device.setDevice_id(deivceId);
			device.setDevice_model(d.getDevice_model());
			device.setDepot_id(depotId);
			device.setBatch_num(d.getBatch_num());//批号
			device.setBox_no(d.getBox_no());//箱号
			device.setOwnership(input.getOwnership());
			device.setBackup(input.getBackup());
			device.setOwnership_depot(depotId);
			//0001407: r_device.is_new_stb属性单卡 单猫都使用 
			device.setIs_new_stb(input.getIs_new_stb());
			// device.setWarranty_date();
			device.setTotal_num(1);
			deviceList.add(device);

			RDeviceDoneDeviceid doneDeviceid = new RDeviceDoneDeviceid();
			doneDeviceid.setDevice_id(deivceId);
			doneDeviceid.setDevice_done_code(doneCode);
			doneDeviceidList.add(doneDeviceid);
		}
		
		String[] stbCodes = null;
		/*String[] cardCodes = null;
		String[] modemCodes = null;*/
		if(stbList.size()>0){
			stbCodes = CollectionHelper.converValueToArray(stbList, "stb_id");
		}
		/*if(cardList.size()>0){
			cardCodes = CollectionHelper.converValueToArray(cardList, "card_id");
		}
		if(modemList.size()>0){
			modemCodes = CollectionHelper.converValueToArray(modemList, "modem_mac");
		}
		//手工入库判断设备是否已经入库
		if(deviceType.equals(SystemConstants.DEVICE_CFG_TYPE_HAND)){
			if(stbCodes != null){
				checkDeviceIsNull(stbCodes,SystemConstants.DEVICE_TYPE_STB);
			}
			if(cardCodes != null){
				checkDeviceIsNull(cardCodes,SystemConstants.DEVICE_TYPE_CARD);
			}
			if(modemCodes != null){
				checkDeviceIsNull(modemCodes,SystemConstants.DEVICE_TYPE_MODEM);
			}
		}*/
		//退库后又入库的机顶盒，默认是旧机
		if(stbCodes != null){
			List<String> deviceHisStr = rDeviceHisDao.queryByStbId(stbCodes);
			Map<String,RDevice> deviceMap = CollectionHelper.converToMapSingle(deviceList, "device_id");
			String[] deviceArr = deviceHisStr.toArray(new String[deviceHisStr.size()]);
			String stbStrs = "";
			for(int i=0;i<deviceArr.length;i++){
				stbStrs = StringHelper.append(stbStrs,",",deviceArr[i]);
			}
			
			if(deviceHisStr.size()>0){
				for(RStb stb:stbList){
					if(stbStrs.indexOf(stb.getStb_id()) > -1){
						if(null != deviceMap.get(stb.getDevice_id()))
							deviceMap.get(stb.getDevice_id()).setIs_new_stb(SystemConstants.BOOLEAN_FALSE);
					}
				}
			}
			
		}
		
		
		rStbDao.save(stbList.toArray(new RStb[stbList.size()]));
		rCardDao.save(cardList.toArray(new RCard[cardList.size()]));
		rModemDao.save(modemList.toArray(new RModem[modemList.size()]));
		rDeviceDao.save(deviceList.toArray(new RDevice[deviceList.size()]));
		rDeviceInputDao.save(input);
		rDeviceDoneDeviceidDao.save(doneDeviceidList
				.toArray(new RDeviceDoneDeviceid[doneDeviceidList.size()]));
		rDeviceDoneDetailDao.updateByDoneDeviceid(doneCode);
		if(input.getOrder_done_code()!=null && input.getOrder_done_code()>0 )
			rDeviceOrderDetailDao.updateSupplyNum(input.getOrder_done_code(),doneCode);
		
		//记录异动
		rDeviceChangeDao.saveInputChange(doneCode, BusiCodeConstants.DEVICE_INPUT, optr.getCounty_id(), optr.getArea_id());
		
	}

	public void saveMateralDeviceInput(SOptr optr, RDeviceInput input,
			String batchNum, String deviceType, String deviceModel,String totalNum) throws Exception{
		
		List<DeviceDto> devices = new ArrayList<>();
		DeviceDto deviceDto = new DeviceDto();
		deviceDto.setDevice_type(deviceType);
		devices.add(deviceDto);
		checkDeviceOperRight(optr,devices);
		
		String depotId = findDepot(optr);
		Integer orderDoneCode = input.getOrder_done_code();
		Integer num = Integer.parseInt(totalNum);
		if(orderDoneCode != null){
			List<RDeviceOrderDetail> orderList = rDeviceOrderDetailDao.queryByDoneCode(orderDoneCode);
			int orderNum = 0;//剩余可入库数
			
			for(RDeviceOrderDetail r : orderList){
				if(r.getDevice_type().equals(deviceType) && r.getDevice_model().equals(deviceModel)){
					orderNum = r.getOrder_num()- (r.getSupply_num() !=null? r.getSupply_num():0);
					//到货数应当小于等于订货数
					if(num> orderNum){
						throw new ComponentException("入库数不能大于剩余的订货数！订单订货数："
								+ r.getOrder_num() + ";到货数：" + r.getSupply_num()+ ";入库数：" + num);
					}
				}
			}
		}
		Integer doneCode = gDoneCode();
		input.setDevice_done_code(doneCode);
		input.setOptr_id(optr.getOptr_id());
		input.setDepot_id(depotId);
		input.setCreate_time(DateHelper.now());
		input.setIs_new_stb(SystemConstants.BOOLEAN_TRUE);
		input.setOwnership(SystemConstants.OWNERSHIP_GD);
		input.setBackup(SystemConstants.BOOLEAN_FALSE);
		input.setBatch_num(batchNum);

		String deviceId = null;
		RDevice device = rDeviceDao.queryIdleMateralDevice(deviceModel,depotId);
		if(device == null){
			RDeviceModel model = rDeviceModelDao.lockModel(deviceModel);
			device = rDeviceDao.queryIdleMateralDevice(model.getDevice_model(),depotId);
			if(device != null){
				deviceId = device.getDevice_id();
			}
		}else{
			deviceId = device.getDevice_id();
		}
		if(deviceId == null){
			device = new RDevice(deviceType, StatusConstants.ACTIVE,StatusConstants.IDLE);
			deviceId = gDeviceId();
			device.setDevice_id(deviceId);
			device.setDevice_model(deviceModel);
			device.setDepot_id(depotId);
//			device.setBatch_num(batchNum);
			device.setOwnership(input.getOwnership());
			device.setBackup(input.getBackup());
			device.setOwnership_depot(depotId);
			device.setIs_new_stb(input.getIs_new_stb());
			device.setTotal_num(num);
			rDeviceDao.save(device);
		}else{
			//更新器材原总数
			rDeviceDao.addMateralDevice(deviceId,num);
			checkDeviceNum(deviceId);
		}

		RDeviceDoneDeviceid doneDeviceid = new RDeviceDoneDeviceid();
		doneDeviceid.setDevice_id(deviceId);
		doneDeviceid.setDevice_done_code(doneCode);
		
		RDeviceDoneDetail deviceDetail = new RDeviceDoneDetail();
		deviceDetail.setCount(num);
		deviceDetail.setDevice_done_code(doneCode);
		deviceDetail.setDevice_model(deviceModel);
		deviceDetail.setDevice_type(deviceType);
		
		
		
		rDeviceInputDao.save(input);
		rDeviceDoneDeviceidDao.save(doneDeviceid);
		rDeviceDoneDetailDao.save(deviceDetail);
		//更新到货数量
		if(orderDoneCode != null){
			rDeviceOrderDetailDao.updateMateralNum(orderDoneCode,deviceType,deviceModel,num);
			RDeviceOrderDetail rDetail = rDeviceOrderDetailDao.queryMateralDeviceDetail(orderDoneCode,deviceType,deviceModel);
			if(rDetail.getOrder_num()<rDetail.getSupply_num()){
				throw new ComponentException(ErrorCode.DeviceTotalNumIsTooBig);
			}
		}
		
		//记录异动
		rDeviceChangeDao.saveInputChange(doneCode, BusiCodeConstants.DEVICE_INPUT, optr.getCounty_id(), optr.getArea_id());
		
	}
	
	
	
	
	
	public List<RDevice> queryMateralTransferDeviceByDepotId(SOptr optr)throws Exception{
		String depotId = optr.getDept_id();
		List<RDevice> list = rDeviceDao.queryMateralDeviceByDepotId(depotId);
		return list;
	}

	/**
	 * 查询当前仓库的设备订单
	 * @param optrId
	 * @return
	 * @throws Exception
	 * @throws ComponentException
	 */
	public Pager<RDeviceOrder> queryDeviceOrder(SOptr optr, Integer start,
			Integer limit, String query, String isHistory) throws Exception {
		String depotId = findDepot(optr);
		return rDeviceOrderDao.queryDeviceOrder(depotId, start, limit,query,isHistory);
	}

	/**
	 * 查询设备订单明细
	 * @param deviceDoneCode
	 * @return
	 * @throws Exception
	 * @throws ComponentException
	 */
	public List<RDeviceOrderDetail> queryDeviceOrderDetail(int deviceDoneCode) throws Exception {
		return rDeviceOrderDetailDao.queryByDoneCode(deviceDoneCode);
	}

	/**
	 * 查询设备到货明细
	 * @param deviceDoneCode
	 * @return
	 * @throws Exception
	 * @throws ComponentException
	 */
	public List<DeviceInputDetailDto> queryDeviceOrderInputDetail(int deviceDoneCode) throws Exception {
		return rDeviceDoneDetailDao.queryInputByDoneCode(deviceDoneCode);
	}


	/**
	 * 保存或修改产品订单，
	 *
	 * @param deviceOrder
	 *            订单信息
	 *            Device_done_code为空保存
	 *            Device_done_code不为空修改原有订单
	 * @param details
	 *            订单明细
	 * @throws Exception
	 * @throws ComponentException
	 */
	public void saveDeviceOrder(SOptr optr, RDeviceOrder deviceOrder,
			List<RDeviceOrderDetail> details) throws Exception,
			ComponentException {
		String depotId = findDepot(optr);
		deviceOrder.setDepot_id(depotId);
		deviceOrder.setOptr_id(optr.getOptr_id());
		Integer doneCode = deviceOrder.getDevice_done_code();
		if (doneCode==null) {
			doneCode = gDoneCode();
			deviceOrder.setDevice_done_code(doneCode);
			deviceOrder.setCreate_time(new Date());
			rDeviceOrderDao.save(deviceOrder);
		}else{
			rDeviceOrderDao.update(deviceOrder);
		}
		for (RDeviceOrderDetail d : details) {
			d.setDevice_done_code(doneCode);
		}
		rDeviceOrderDetailDao.removeByDoneCode(doneCode);
		rDeviceOrderDetailDao.save(details
				.toArray(new RDeviceOrderDetail[details.size()]));
	}

	/**
	 * 查询设备调拨
	 * @param optrId
	 * @return
	 * @throws Exception
	 * @throws ComponentException
	 */
	public Pager<RDeviceTransferDto> queryDeviceTransfer(SOptr optr,
			String query, String startDate, String endDate, Integer start,
			Integer limit ,String isHistory,String deviceModel) throws Exception {
		String depotId = findDepot(optr);
		return rDeviceTransferDao.queryByDepot(depotId,query,startDate,endDate,start,limit,isHistory,deviceModel);
	}
	
	/**
	 * 查询当前仓库及以下子仓库
	 * @param depotId
	 * @return
	 * @throws JDBCException
	 */
	public List<RDepotDto> queryChildDepot(SOptr optr) throws Exception{
		String[] depotIds=null;
		String dataRight = this.queryDataRightCon(optr, DataRight.DEVICE_MNG.toString());
		if (dataRight.equals(DataRightLevel.AREA.toString())
				&& optr.getArea_id().equals(SystemConstants.WH_AREA_ID)) {
			depotIds = new String[2];
			depotIds[0] = SystemConstants.WH_COUNTY_ID;
			depotIds[1] = SystemConstants.ZS_COUNTY_ID;
		} else {
			String depotId = findDepot(optr);
			depotIds = new String[1];
			depotIds[0] = depotId;
		}
		
		return rDepotDefineDao.queryChildDepot(depotIds);
	}


	/**
	 * 查询当前仓库可以流转的上下级仓库
	 * @param optrId
	 * @return
	 * @throws ComponentException
	 * @throws Exception
	 */
	public List<RDepotDefine> queryTransferDepot(SOptr optr) throws Exception {
		SDept dept = sDeptDao.findByKey(optr.getDept_id());
		List<RDepotDefine> list = rDepotDefineDao.queryDepotForTransById(dept.getDept_id());
//		if(dept.getDept_type().equals(SystemConstants.DEPT_TYPE_YYT)){
//			list = rDepotDefineDao.queryYytDepotById(optr.getDept_id(),optr.getCounty_id());
//		}else if(dept.getDept_type().equals(SystemConstants.DEPT_TYPE_CK) || dept.getDept_type().equals(SystemConstants.DEPT_TYPE_FGS)){
//			list = rDepotDefineDao.queryCkDepotById(optr.getDept_id(),optr.getCounty_id());
//		}
		return list;
	}

	/**
	 * 根据设备编号查询信息,并判断设备是否可以操作
	 * @param optrId
	 * @param deviceCode
	 * @return
	 * @throws Exception
	 * @throws ComponentException
	 * @throws BeansException
	 */
	public DeviceDto queryCanOptrDevice(SOptr optr,String deviceCode) throws Exception, BeansException, ComponentException {
		DeviceDto dto = new DeviceDto();
		BeanUtils.copyProperties(queryDevice(optr,deviceCode), dto);
		return dto;
	}
	
	public DeviceDto queryCanProcureDevice(SOptr optr,String deviceCode) throws Exception, BeansException, ComponentException {
		List<DeviceDto> dList = rDeviceDao.queryCardModemByDeviceCodes(deviceCode.split(","));
		if(dList.size()>0){
			throw new ComponentException("设备存在配对的机顶盒号:["+dList.get(0).getDevice_code()+"],请使用该盒号进行领用!");
		}
		DeviceDto dto = new DeviceDto();
		BeanUtils.copyProperties(queryDevice(optr,deviceCode), dto);
		return dto;
	}
	

	
	public List<DeviceDto> queryByBatchNum(SOptr optr,String batchNum) throws Exception{
		return rDeviceDao.queryByBatchNum(batchNum, optr.getDept_id());
	}
	
	public List<RStbModel> queryAllDeviceMdoel() throws JDBCException {
		List<RStbModel> rStbModelList  = rStbModelDao.queryAllDeviceMdoel();
		for(RStbModel dto:rStbModelList){
			dto.setModel_type_name(dto.getDevice_type_text()+"-"+dto.getModel_name()+"-"+dto.getDevice_model());
		}
		return rStbModelList;
	}
	
	/**
	 * 要更改的设备不能和选择的设备状态相同
	 * @param optrId
	 * @param deviceCode
	 * @param deviceStatus
	 * @return
	 * @throws Exception
	 */
	public DeviceDto queryCanUpdateDeviceStatus(SOptr optr,RDevice device) throws Exception {
		String deviceCode = device.getDevice_id();
		String deviceStatus = device.getDevice_status();
		String newStb = device.getIs_new_stb();
		DeviceDto dto = queryCanOptrDevice(optr, deviceCode);

		if(StringHelper.isNotEmpty(deviceStatus ) && deviceStatus.equals(dto.getDevice_status()) && StringHelper.isNotEmpty(newStb) && device.getIs_new_stb().equals(dto.getIs_new_stb())){
			throw new ComponentException("输入的设备状态和新旧与选择的一样！");
		}else if(StringHelper.isEmpty(deviceStatus ) && StringHelper.isNotEmpty(newStb) && device.getIs_new_stb().equals(dto.getIs_new_stb())){
			String neededInfo = newStb.equals(SystemConstants.BOOLEAN_TRUE) ? " 旧 " : " 新 ";
			throw new ComponentException("需要 【"+neededInfo+"】 的设备,当前输入的是 【 " + neededInfo + " 】 的！");
		}else if(StringHelper.isEmpty(newStb) && StringHelper.isNotEmpty(deviceStatus ) && deviceStatus.equals(dto.getDevice_status())){
			throw new ComponentException("设备状态为【"+dto.getDevice_status_text()+"】,请选择其他状态的设备！");
		}
		
		return dto;
	}

	/**
	 * 保存调拨(文件)
	 * 验证设备是否存在
	 * @param optrId
	 * @param transfer
	 * @param devices
	 * @throws Exception
	 */
	public void saveTransferFile(SOptr optr, RDeviceTransfer transfer,
			List<DeviceDto> devices,String deviceType) throws Exception {
		devices = removeRepeatDeviceCodes(devices);
		checkDeviceOperRight(optr,devices);
		String depotId = findDepot(optr);
		
		String[] deviceCodes = CollectionHelper.converValueToArray(devices, "device_code");
		List<DeviceDto> deviceList = rDeviceDao.queryByDeviceCodescount(deviceCodes,deviceType);
		this.compareDevice(devices, deviceList);
		for (DeviceDto d : deviceList) {
			checkDevice(depotId, d,d.getDevice_code(),false);
		}
		saveTransfer(optr, transfer, deviceList);
	}
	
	/**
	 * 保存调拨
	 * @param optrId
	 * @param transfer
	 * @param devices
	 * @throws Exception
	 * @throws ComponentException
	 */
	public void saveTransfer(SOptr optr, RDeviceTransfer transfer,
			List<DeviceDto> devices) throws Exception {
		checkDeviceOperRight(optr,devices);
		String depotId = findDepot(optr);
		Integer doneCode = gDoneCode();
		transfer.setDevice_done_code(doneCode);
		transfer.setDepot_source(depotId);
		transfer.setOptr_id(optr.getOptr_id());
		transfer.setStatus(StatusConstants.UNCONFIRM);
		transfer.setCreate_time(DateHelper.now());

		String[] deviceCodes = CollectionHelper.converValueToArray(devices, "device_code");
		//验证设备是否重复
		checkDeviceIsNull(deviceCodes,"设备号");
		
		List<DeviceDto> dList = rDeviceDao.queryCardModemByDeviceCodes(deviceCodes);
		if(dList.size()>0){
			throw new ComponentException("设备:["+dList.get(0).getPair_device_code()+"]存在配对机顶盒号["+dList.get(0).getDevice_code()+"],使用该盒号调拨!");
		}
		
		List<RDeviceDoneDeviceid> deviceList = new ArrayList<RDeviceDoneDeviceid>();
		for (DeviceDto d : devices) {
			RDeviceDoneDeviceid device = new RDeviceDoneDeviceid();
			device.setDevice_done_code(doneCode);
			device.setDevice_id(d.getDevice_id());
			deviceList.add(device);
		}
		
		LoggerHelper.debug(DeviceComponent.class, "***check后device个数***       "+deviceList.size());
		
		//如果是stb，获取配对卡device_id
		List<String> stbIds = CollectionHelper.converValueToList(deviceList, "device_id");
		List<RStb>  stbList = rStbDao.findPairCardByDeviceId(stbIds.toArray(new String[stbIds.size()]));
		if(stbList.size()>0){
			List<String> cardIds = CollectionHelper.converValueToList(stbList, "pair_card_id");
			stbIds.addAll(cardIds);
		}
		
		rDeviceTransferDao.save(transfer);
		rDeviceDoneDeviceidDao.save(deviceList.toArray(new RDeviceDoneDeviceid[deviceList.size()]));
		rDeviceDoneDetailDao.updateByDoneDeviceid(doneCode);
		rDeviceDao.updateTranOut(doneCode);
		
		//记录异动
		rDeviceChangeDao.saveTransChange(doneCode, BusiCodeConstants.DEVICE_TRANSFER, optr.getCounty_id(), optr.getArea_id());
	}
	
	
	/**
	 * 器材调拨
	 * @param optr
	 * @param transfer
	 * @param deviceList
	 * @throws Exception
	 */
	public void saveMateralTransfer(SOptr optr, RDeviceTransfer transfer,
			List<RDevice> deviceList) throws Exception {
	
		if(deviceList.size()== 0){
			throw new ComponentException(ErrorCode.ParamIsNull);
		}
		
		String[] deviceIds = CollectionHelper.converValueToArray(deviceList, "device_id");
		if(deviceList.size() != deviceIds.length){
			throw new ComponentException(ErrorCode.DeviceRepeat);
		}
		
		
		String depotId = findDepot(optr);
		Integer doneCode = gDoneCode();
		transfer.setDevice_done_code(doneCode);
		transfer.setDepot_source(depotId);
		transfer.setOptr_id(optr.getOptr_id());
		transfer.setStatus(StatusConstants.UNCONFIRM);
		transfer.setCreate_time(DateHelper.now());


		List<RDevice> rList = rDeviceDao.queryDeviceByIds(deviceIds);
		Map<String, RDevice> map = CollectionHelper.converToMapSingle(rList, "device_id");
		
		List<RDeviceDoneDeviceid> deviceDoneList = new ArrayList<RDeviceDoneDeviceid>();
		
		List<RDeviceDoneDetail> deviceDetailList = new ArrayList<RDeviceDoneDetail>();
		
		String deviceType = SystemConstants.DEVICE_TYPE_FITTING;
		List<DeviceDto> devices = new ArrayList<>();
		for (RDevice d : deviceList) {
			//检查权限
			DeviceDto deviceDto = new DeviceDto();
			deviceDto.setDevice_type(deviceType);
			devices.add(deviceDto);
			
			String deviceModel = d.getDevice_model();
			
			String deviceId = null;
			RDevice deviceChick = rDeviceDao.queryIdleMateralDevice(deviceModel,depotId);
			if(deviceChick == null){
				rDeviceModelDao.lockModel(deviceModel);
				deviceChick = rDeviceDao.queryIdleMateralDevice(deviceModel,depotId);
				if(deviceChick != null){
					deviceId = deviceChick.getDevice_id();
				}
			}else{
				deviceId = deviceChick.getDevice_id();
			}
			
			//新增器材数据
			RDevice newDevice = new RDevice(deviceType, StatusConstants.ACTIVE,StatusConstants.IDLE);
			String newDeviceId = gDeviceId();
			newDevice.setDevice_id(newDeviceId);
			newDevice.setDevice_model(deviceModel);
			newDevice.setDepot_id(depotId);
			newDevice.setOwnership(SystemConstants.OWNERSHIP_GD);
			newDevice.setOwnership_depot(depotId);
			newDevice.setTran_status(StatusConstants.UNCONFIRM);
			newDevice.setIs_new_stb(SystemConstants.BOOLEAN_TRUE);
			newDevice.setTotal_num(d.getTotal_num());
			rDeviceDao.save(newDevice);
			
			
			RDeviceDoneDeviceid device = new RDeviceDoneDeviceid();
			device.setDevice_done_code(doneCode);
			device.setDevice_id(newDeviceId);
			deviceDoneList.add(device);
			
			RDeviceDoneDetail detail = new RDeviceDoneDetail();
			detail.setDevice_done_code(doneCode);
			detail.setDevice_type(deviceType);
			detail.setDevice_model(deviceModel);
			detail.setCount(d.getTotal_num());
			deviceDetailList.add(detail);
			
			//变更原有的设备数量
			RDevice _r = map.get(deviceId);
			Integer oldValue = _r.getTotal_num();
			Integer newValue = _r.getTotal_num()-d.getTotal_num();
			//总数异动记录
			rDeviceChangeDao.saveMateralTransChange(doneCode,BusiCodeConstants.DEVICE_TRANSFER,deviceId,"total_num",oldValue
					,newValue,transfer.getOptr_id(),transfer.getDepot_source(),optr.getCounty_id(), optr.getArea_id());
	
			rDeviceDao.removeMateralDevice(deviceId, d.getTotal_num());
			checkDeviceNum(deviceId);
		}
		
		checkDeviceOperRight(optr,devices);
		
		rDeviceTransferDao.save(transfer);
		rDeviceDoneDeviceidDao.save(deviceDoneList.toArray(new RDeviceDoneDeviceid[deviceDoneList.size()]));
		rDeviceDoneDetailDao.save(deviceDetailList.toArray(new RDeviceDoneDetail[deviceDetailList.size()]));
		
	}
	
	public void checkDeviceNum(String deviceId) throws Exception{
		RDevice nextRdevice = rDeviceDao.findByKey(deviceId);
		if(nextRdevice == null){
			throw new ComponentException(ErrorCode.DeviceNotExists);
		}
		if(nextRdevice.getTotal_num()<0){
			throw new ComponentException(ErrorCode.DeviceTotalNumIsNull);
		}
	}
	
	
	/**
	 * 查询可以用来转发的设备.
	 * @return
	 * @throws Exception
	 */
	public Map<String, Map> queryTransInfoByDoneCode(Integer doneCode) throws Exception{
		return rDeviceTransferDao.queryTransInfoByDoneCode(doneCode);
	}
	
	public void saveReTransDevices(SOptr optr, RDeviceTransfer transfer,Integer doneCode) throws Exception{
		List<DeviceDto> devs = rDeviceTransferDao.queryReTransDevices(doneCode);
		saveTransfer(optr, transfer, devs);
	}
	
	/**
	 * 确认调拨
	 * @param optrId
	 * @param transferNo
	 * @param confirmInfo
	 * @throws Exception
	 */
	public void checkTransfer(SOptr optr, int deviceDoneCode,String status,
			String confirmInfo) throws Exception {
		RDeviceTransfer transfer = rDeviceTransferDao.findByKey(deviceDoneCode);
		if(null == transfer)
			throw new ComponentException("调拨单号不存在！");
		transfer.setConfirm_optr_id(optr.getOptr_id());
		transfer.setStatus(status);
		transfer.setConfirm_date(DateHelper.now());
		transfer.setConfirm_info(confirmInfo);
		rDeviceTransferDao.update(transfer);
		
		List<RDevice> rList = rDeviceDao.queryDeviceByDoneCode(deviceDoneCode);
		Boolean isMateral = true;
		for(RDevice r : rList){
			//如果是器材调拨，所有的都只能是器材，判断1次就可以了
			if(!r.getDevice_type().equals(SystemConstants.DEVICE_TYPE_FITTING) ){
				isMateral = false;
				break;
			}
		}
		
		Integer doneCode = gDoneCode();
		if (status.equals(StatusConstants.INVALID)){
			if(isMateral){
				for(RDevice r : rList){
					//原器材
					RDevice device = rDeviceDao.queryIdleMateralDevice(r.getDevice_model(), r.getDepot_id());
					Integer oldValue = device.getTotal_num();
					Integer newValue = oldValue + r.getTotal_num();
					//总数异动记录
					rDeviceChangeDao.saveMateralTransChange(doneCode,BusiCodeConstants.DEVICE_CANCEL_CONFIRM,device.getDevice_id(),"total_num",oldValue
							,newValue,transfer.getConfirm_optr_id(),transfer.getDepot_source(),optr.getCounty_id(), optr.getArea_id());
					//减去调拨数量
					rDeviceDao.removeMateralDevice(r.getDevice_id(), r.getTotal_num());
					checkDeviceNum(r.getDevice_id());
					//原器材总数+取消调拨的数量
					rDeviceDao.addMateralDevice(device.getDevice_id(),r.getTotal_num());
				}
		
			}else{
				rDeviceDao.updateTranIdel(deviceDoneCode);
				//调拨取消记录异动
				rDeviceChangeDao.saveTransCancelChange(doneCode, BusiCodeConstants.DEVICE_CANCEL_CONFIRM,deviceDoneCode, optr.getCounty_id(), optr.getArea_id());
			}
		}else if(status.equals(StatusConstants.CONFIRM)){
			if(isMateral){
				for(RDevice r : rList){
					String deviceId = r.getDevice_id();
					String deviceModel = r.getDevice_model();
					//查询 是否已经存在该型号
					RDevice deviceChick = rDeviceDao.queryIdleMateralDevice(deviceModel,transfer.getDepot_order());
					boolean key = false;
					if(deviceChick == null){
						rDeviceModelDao.lockModel(deviceModel);
						deviceChick = rDeviceDao.queryIdleMateralDevice(deviceModel,transfer.getDepot_order());
						if(deviceChick == null){
							//调拨成功 ，改成目标仓库，改状态
							rDeviceDao.updateMateralTransferDepot(deviceId, transfer.getDepot_order());
						}else{
							key = true;
						}
					}else{
						key = true;
					}
					//已经存在了状态为正常的该型号设备，就加总数
					if(key){
						//原器材总数+调拨的数量
						rDeviceDao.addMateralDevice(deviceChick.getDevice_id(),r.getTotal_num());
						//删除这条调拨的数据
						rDeviceDao.removeDeviceToHis(deviceId);
					}
				}
				
				rDeviceChangeDao.saveMateralTransArrirmChange(doneCode, BusiCodeConstants.DEVICE_CONFIRM, deviceDoneCode,optr.getCounty_id(), optr.getArea_id());
			}else{
				rDeviceDao.updateTranIdelDepot(deviceDoneCode, transfer.getDepot_order());
				//调拨确认记录异动
				rDeviceChangeDao.saveTransArrirmChange(doneCode, BusiCodeConstants.DEVICE_CONFIRM, deviceDoneCode,optr.getCounty_id(), optr.getArea_id());
			}
		}
	}

	/**
	 * 查询当前仓库的差异
	 * @param optrId
	 * @return
	 * @throws Exception
	 * @throws ComponentException
	 */
	public Pager<DeviceDto> queryDeviceDiffence(String deviceCode, SOptr optr,
			String depotId, Integer start, Integer limit) throws Exception {
		if(StringHelper.isEmpty(depotId))
			depotId = findDepot(optr);
		return rDeviceDao.queryDiffence(deviceCode,depotId,start,limit);
	}

	/**
	 * 根据设备编号查询差异确认信息
	 *
	 * @param deviceCode
	 * @return
	 * @throws Exception
	 * @throws ComponentException
	 * @throws BeansException
	 */
	public DeviceDto queryDeviceDiffecnceInfo(SOptr optr,
			String deviceCode,String depotId) throws Exception, BeansException,
			ComponentException {
		RDevice device = queryDiffDevice(optr, deviceCode,depotId);
		if (!SystemConstants.DEVICE_DIFFENCN_TYPE_NODIFF.equals(device.getDiffence_type()))
			throw new ComponentException("该设备有差异或差异待确认");
		DeviceDto dto = new DeviceDto();
		BeanUtils.copyProperties(device, dto);
		return dto;
	}

	/**
	 * 添加差异
	 * @param remark 
	 * @param devices
	 * @throws Exception
	 */
	public void addDeviceDiffence(SOptr optr ,String deviceIds, String remark) throws Exception {
		String [] deviceId = deviceIds.split(",");
		Integer doneCode = gDoneCode();
		rDeviceEditDao.saveDeviceEdit(doneCode,optr.getOptr_id(),deviceIds,remark);
		List<RDeviceDoneDeviceid> deviceList = new ArrayList<RDeviceDoneDeviceid>();
		for (String d : deviceId) {
			RDeviceDoneDeviceid device = new RDeviceDoneDeviceid();
			device.setDevice_done_code(doneCode);
			device.setDevice_id(d);
			deviceList.add(device);
		}
		rDeviceDoneDeviceidDao.save(deviceList
				.toArray(new RDeviceDoneDeviceid[deviceList.size()]));
		rDeviceDoneDetailDao.updateByDoneDeviceid(doneCode);
		rDeviceDifeenceDao.saveDiffence(doneCode,optr,deviceIds,remark);
		rDeviceDao.updateDiffenceType(deviceId,
				SystemConstants.DEVICE_DIFFENCN_TYPE_DIFF);
	}
	
	/**
	 * 文件添加差异
	 * @param devices
	 * @throws Exception
	 */
	public void addFileDeviceDiffence(List<DeviceDto> devices,SOptr optr,String depotId,String remark) throws Exception {
		String deviceIds="";
		devices = removeRepeatDeviceCodes(devices);
 		if(null!=devices && devices.size()>0){
			for(DeviceDto dd : devices){
				if(StringHelper.isNotEmpty(dd.getDevice_code())){
					RDevice device = queryDiffDevice(optr, dd.getDevice_code(),depotId);
					if (!SystemConstants.DEVICE_DIFFENCN_TYPE_NODIFF.equals(device.getDiffence_type())){
						throw new ComponentException("设备【"+dd.getDevice_code()+"】有差异或差异待确认");
					}
					deviceIds+=device.getDevice_id()+",";
				}
			}
			Integer doneCode = gDoneCode();
			deviceIds = deviceIds.substring(0,deviceIds.lastIndexOf(","));
			rDeviceEditDao.saveDeviceEdit(doneCode,optr.getOptr_id(),deviceIds,remark);
			List<RDeviceDoneDeviceid> deviceList = new ArrayList<RDeviceDoneDeviceid>();
			for (String d : deviceIds.split(",")) {
				RDeviceDoneDeviceid device = new RDeviceDoneDeviceid();
				device.setDevice_done_code(doneCode);
				device.setDevice_id(d);
				deviceList.add(device);
			}
			rDeviceDoneDeviceidDao.save(deviceList
					.toArray(new RDeviceDoneDeviceid[deviceList.size()]));
			rDeviceDoneDetailDao.updateByDoneDeviceid(doneCode);
			rDeviceDifeenceDao.saveDiffence(doneCode,optr,deviceIds,remark);
			rDeviceDao.updateDiffenceType(deviceIds.split(","),
					SystemConstants.DEVICE_DIFFENCN_TYPE_DIFF);
		}
		 
		
	}

	/**
	 * 取消差异
	 * @param devices
	 * @throws Exception
	 */
	public void cancelDeviceDiffence(String[] deviceIds) throws Exception {
		rDeviceDao.updateDiffenceType(deviceIds,
				SystemConstants.DEVICE_DIFFENCN_TYPE_NODIFF);
		//删除差异记录表记录
		rDeviceDifeenceDao.removeDiffence(deviceIds);
	}


	/**
	 * 确认差异
	 * @param devices
	 * @throws Exception
	 */
	public void checkDeviceDiffence(String[] deviceIds) throws Exception {
		rDeviceDao.updateDiffenceType(deviceIds,
				SystemConstants.DEVICE_DIFFENCN_TYPE_DIFF);
	}



	/**
	 * 查询领用清单
	 * @param optrId
	 * @return
	 * @throws Exception
	 * @throws ComponentException
	 */
	public Pager<DeviceProcureDto> queryDeviceProcure(SOptr optr, String query,
			Integer start, Integer limit) throws Exception {
		String depotId = findDepot(optr);
		return rDeviceProcureDao.queryBydepot(depotId,query,start,limit);
	}
	
	public List<DeviceProcureDto> queryProcureDeviceDetail(
			Integer deviceDoneCode, String deviceType) throws Exception {
		return rDeviceProcureDao.queryProcureDeviceDetail(deviceDoneCode, deviceType);
	}
	
	public void saveCancelProcure(Integer deviceDoneCode,String deviceId,SOptr optr) throws Exception {
		DeviceProcureDto procure = rDeviceProcureDao.queryProcure(deviceDoneCode);
		TDeviceBuyMode buy = tDeviceBuyModeDao.findByKey(procure.getProcure_type());
		if(StringHelper.isNotEmpty(deviceId)){
			//单个设备取消领用
			RDevice device = rDeviceDao.findByDeviceId(deviceId);
			if (device == null)
				throw new ComponentException("设备不存在");
			if (device.getDepot_status().equals(StatusConstants.IDLE))
				throw new ComponentException("设备库存状态已经是空闲");
			String depotId = findDepot(optr);
			if(!depotId.equals(device.getDepot_id())){
				throw new ComponentException("设备在" + device.getDepot_id_text()
						+ ",不在当前仓库");
			}
			//取消领用记录异动
			rDeviceChangeDao.saveProcureChange(deviceDoneCode, BusiCodeConstants.DEVICE_PROCURE,deviceId,buy.getChange_ownship(),StatusConstants.IDLE,optr.getCounty_id(), optr.getArea_id());
			
			rDeviceDao.updateDepotStatus(deviceDoneCode,deviceId,BusiCodeConstants.CANCEL_PROCURE_DEVICE,buy.getChange_ownship(), StatusConstants.IDLE,optr);

			//删除设备领用明细
			rDeviceDoneDeviceidDao.removeByDeviceDoneCodeAndId(deviceDoneCode, deviceId);
			
			RDeviceDoneDetail deviceDetail = rDeviceDoneDetailDao
					.findByDoneCode(deviceDoneCode, device.getDevice_model());
			
			if(deviceDetail.getCount().intValue() == 1){
				//数量为1，则删除该记录
				rDeviceDoneDetailDao.removeByDeviceDoneCode(deviceDoneCode);
				rDeviceProcureDao.removeByDeviceDoneCode(deviceDoneCode);
			}else{
				//领用数量减1
				rDeviceDoneDetailDao.updateCountByDoneCode(deviceDoneCode, deviceDetail.getCount().intValue()-1);
			}
			
		}else{
			//整个领用单取消
//			List<RDeviceDoneDeviceid> list = rDeviceDoneDeviceidDao.queryByDeviceDoneCode(deviceDoneCode);
//			for(RDeviceDoneDeviceid rid : list){
////				RDevice d = new RDevice();
////				d.setDevice_id(rid.getDevice_id());
////				d.setDepot_status(StatusConstants.IDLE);
////				rDeviceDao.update(d);
//
//			}
			
			//取消领用记录异动
			rDeviceChangeDao.saveProcureChange(deviceDoneCode, BusiCodeConstants.DEVICE_PROCURE,null,buy.getChange_ownship(),StatusConstants.IDLE,optr.getCounty_id(), optr.getArea_id());
			
			rDeviceDao.updateDepotStatus(deviceDoneCode,null, BusiCodeConstants.CANCEL_PROCURE_DEVICE,buy.getChange_ownship(),StatusConstants.IDLE,optr);
			
			rDeviceDoneDetailDao.removeByDeviceDoneCode(deviceDoneCode);
			rDeviceDoneDeviceidDao.removeByDeviceDoneCode(deviceDoneCode);
			rDeviceProcureDao.removeByDeviceDoneCode(deviceDoneCode);
		}
	}

	/**
	 * 保存领用设备
	 * @param optrId
	 * @param procure
	 * @param devices
	 * @throws Exception
	 * @throws ComponentException
	 */
	public void saveDeviceProcure(SOptr optr,RDeviceProcure procure,List<DeviceDto> devices) throws Exception{
		checkDeviceOperRight(optr,devices);
		String depotId = findDepot(optr);
		procure.setDepot_id(depotId);
		Integer doneCode = gDoneCode();
		procure.setDevice_done_code(doneCode);
		procure.setOptr_id(optr.getOptr_id());
		procure.setCreate_time(DateHelper.now());

		//验证设备是否重复
		checkDeviceIsNull(CollectionHelper.converValueToArray(devices, "device_code"),"设备号");
		//验证卡是否重复
		checkDeviceIsNull(CollectionHelper.converValueToArray(devices, "pair_device_code"),"智能卡号");
		//验证猫是否重复
		checkDeviceIsNull(CollectionHelper.converValueToArray(devices, "modem_mac"),"modem_mac");
		
		List<RDeviceDoneDeviceid> deviceList = new ArrayList<RDeviceDoneDeviceid>();
		for (DeviceDto d : devices) {
			RDeviceDoneDeviceid device = new RDeviceDoneDeviceid();
			device.setDevice_done_code(doneCode);
			device.setDevice_id(d.getDevice_id());
			deviceList.add(device);
		}
		
		
		rDeviceProcureDao.save(procure);
		rDeviceDoneDeviceidDao.save(deviceList.toArray(new RDeviceDoneDeviceid[deviceList.size()]));
		rDeviceDoneDetailDao.updateByDoneDeviceid(doneCode);
		TDeviceBuyMode buy = tDeviceBuyModeDao.findByKey(procure.getProcure_type());
		//领用记录异动
		rDeviceChangeDao.saveProcureChange(doneCode, BusiCodeConstants.DEVICE_PROCURE,null,buy.getChange_ownship(),StatusConstants.USE,optr.getCounty_id(), optr.getArea_id());
		rDeviceDao.updateDepotStatus(doneCode,null,BusiCodeConstants.DEVICE_PROCURE,buy.getChange_ownship(),StatusConstants.USE,optr);
		
	}

	/**
	 * 查询退库清单
	 * @param optrId
	 * @return
	 * @throws Exception
	 * @throws ComponentException
	 */
	public Pager<RDeviceOutput> queryDeviceOutput(SOptr optr, String query,
			Integer start, Integer limit) throws Exception {
		String depotId = findDepot(optr);
		return rDeviceOutputDao.queryByDepot(depotId,query,start,limit);
	}

	/**
	 * 查询退库设备信息
	 * @param optrId
	 * @param deviceCode
	 * @return
	 * @throws Exception
	 * @throws BeansException
	 * @throws ComponentException
	 */
	public DeviceDto queryDeviceOutputInfo(SOptr optr, String deviceCode)
			throws Exception, BeansException, ComponentException {
		RDevice device = queryDevice(optr, deviceCode);
		if (!StatusConstants.IDLE.equals(device.getDepot_status())) {
			throw new ComponentException("设备正在使用 ，不能被退库");
		}
		if (!SystemConstants.OWNERSHIP_GD.equals(device.getOwnership())) {
			throw new ComponentException("设备产权不是广电 ，不能被退库");
		}
		
		List<DeviceDto> dList = rDeviceDao.queryCardModemByDeviceCodes(deviceCode.split(","));
		if(dList.size()>0){
			throw new ComponentException("设备:["+dList.get(0).getPair_device_code()+"]存在配对机顶盒号["+dList.get(0).getDevice_code()+"],使用该盒号退库!");
		}
		
		DeviceDto dto = new DeviceDto();
		BeanUtils.copyProperties(device, dto);
		return dto;
	}

	/**
	 * 退库（文件）
	 * @param optrId
	 * @param procure
	 * @param devices
	 * @throws Exception
	 */
	public void saveDeviceOutputFile(SOptr optr, RDeviceOutput output,
			List<DeviceDto> devices,String deviceType) throws Exception {
		devices = removeRepeatDeviceCodes(devices);
		checkDeviceOperRight(optr,devices);
		String depotId = findDepot(optr);
		String[] deviceCodes = CollectionHelper.converValueToArray(devices, "device_code");
		List<DeviceDto> deviceList = rDeviceDao.queryByDeviceCodescount(deviceCodes,deviceType);
		Map<String, DeviceDto> map = CollectionHelper.converToMapSingle(
				deviceList, "device_code");
		for (DeviceDto d : devices) {
			if(StringHelper.isNotEmpty(d.getDevice_code())){
				DeviceDto e = map.get(d.getDevice_code());
				checkDevice(depotId, e,d.getDevice_code(),false);
			}
		}
		saveDeviceOutput(optr, output, deviceList);
	}

	/**
	 * 退库
	 * @param optrId
	 * @param procure
	 * @param devices
	 * @throws Exception
	 * @throws ComponentException
	 */
	public void saveDeviceOutput(SOptr optr,RDeviceOutput output,List<DeviceDto> devices) throws Exception{
		
		for (DeviceDto d : devices) {
			RDevice device = rDeviceDao.findByKey(d.getDevice_id());
			if(!StatusConstants.IDLE.equals(device.getTran_status())){
				throw new ComponentException("设备正在调拨中，不允许退库");
			}
		}
		String[] deviceCodes = CollectionHelper.converValueToArray(devices, "device_code");
		List<DeviceDto> dList = rDeviceDao.queryCardModemByDeviceCodes(deviceCodes);
		if(dList.size()>0){
			throw new ComponentException("设备:["+dList.get(0).getPair_device_code()+"]存在配对机顶盒号["+dList.get(0).getDevice_code()+"],使用该盒号退库!");
		}
		
		//验证设备是否重复
		checkDeviceIsNull(deviceCodes,"设备号");
		
		checkDeviceOperRight(optr,devices);
		String depotId = findDepot(optr);
		output.setDepot_id(depotId);
		Integer doneCode = gDoneCode();
		output.setDevice_done_code(doneCode);
		output.setOptr_id(optr.getOptr_id());
		output.setCreate_time(DateHelper.now());

		
		List<RDeviceDoneDeviceid> deviceList = new ArrayList<RDeviceDoneDeviceid>();
		for (DeviceDto d : devices) {
			RDeviceDoneDeviceid device = new RDeviceDoneDeviceid();
			device.setDevice_done_code(doneCode);
			device.setDevice_id(d.getDevice_id());
			deviceList.add(device);
		}

		rDeviceOutputDao.save(output);
		rDeviceDoneDeviceidDao.save(deviceList
				.toArray(new RDeviceDoneDeviceid[deviceList.size()]));
		rDeviceDoneDetailDao.updateByDoneDeviceid(doneCode);
		
		//退库记录异动
		rDeviceChangeDao.saveOutChange(doneCode, BusiCodeConstants.DEVICE_OUT,optr.getCounty_id(), optr.getArea_id());
		
		rDeviceDao.removeToHis(doneCode);
		
	}

	/**
	 * 器材退库
	 * @param optr
	 * @param output
	 * @param deviceList
	 * @throws Exception
	 */
	public void saveMateralOutput(SOptr optr, RDeviceOutput output,
			List<RDevice> deviceList) throws Exception{
		if(deviceList.size()== 0){
			throw new ComponentException(ErrorCode.ParamIsNull);
		}
		
		String[] deviceIds = CollectionHelper.converValueToArray(deviceList, "device_id");
		if(deviceList.size() != deviceIds.length){
			throw new ComponentException(ErrorCode.DeviceRepeat);
		}
		
		List<RDevice> rList = rDeviceDao.queryDeviceByIds(deviceIds);
		for (RDevice d : rList) {
			if(!StatusConstants.IDLE.equals(d.getTran_status())){
				throw new ComponentException("设备正在调拨中，不允许退库");
			}
		}
		
		String depotId = findDepot(optr);
		output.setDepot_id(depotId);
		Integer doneCode = gDoneCode();
		output.setDevice_done_code(doneCode);
		output.setOptr_id(optr.getOptr_id());
		output.setCreate_time(DateHelper.now());

		Map<String, RDevice> map = CollectionHelper.converToMapSingle(rList, "device_id");
		
		List<RDeviceDoneDeviceid> deviceDoneList = new ArrayList<RDeviceDoneDeviceid>();
		
		List<RDeviceDoneDetail> deviceDetailList = new ArrayList<RDeviceDoneDetail>();
		
		List<DeviceDto> devices = new ArrayList<>();
		for (RDevice d : deviceList) {
			//检查权限
			DeviceDto deviceDto = new DeviceDto();
			deviceDto.setDevice_type(d.getDevice_type());
			devices.add(deviceDto);
			
			RDeviceDoneDeviceid device = new RDeviceDoneDeviceid();
			device.setDevice_done_code(doneCode);
			device.setDevice_id(d.getDevice_id());
			deviceDoneList.add(device);
			
			RDeviceDoneDetail detail = new RDeviceDoneDetail();
			detail.setDevice_done_code(doneCode);
			detail.setDevice_type(d.getDevice_type());
			detail.setDevice_model(d.getDevice_model());
			detail.setCount(d.getTotal_num());
			deviceDetailList.add(detail);
			
			//变更原有的设备数量
			RDevice _r = map.get(d.getDevice_id());
			Integer oldValue = _r.getTotal_num();
			Integer newValue = _r.getTotal_num()-d.getTotal_num();
			//总数异动记录
			rDeviceChangeDao.saveMateralTransChange(doneCode,BusiCodeConstants.DEVICE_OUT,device.getDevice_id(),"total_num",oldValue
					,newValue,output.getOptr_id(),output.getDepot_id(),optr.getCounty_id(), optr.getArea_id());
	
			rDeviceDao.removeMateralDevice(d.getDevice_id(), d.getTotal_num());
			checkDeviceNum(d.getDevice_id());
		}
		
		checkDeviceOperRight(optr,devices);
		
		rDeviceOutputDao.save(output);
		rDeviceDoneDeviceidDao.save(deviceDoneList.toArray(new RDeviceDoneDeviceid[deviceDoneList.size()]));
		rDeviceDoneDetailDao.save(deviceDetailList.toArray(new RDeviceDoneDetail[deviceDetailList.size()]));
		
		//删除设备
//		rDeviceDao.removeDeviceToHis(doneCode);
		
	}



	/**
	 *  批量修改设备状态 ，必须在当前库(文件)
	 * @param optrId
	 * @param deviceStatus
	 * @param deviceCodes 设备号
	 * @throws Exception
	 */
	public String changeDeviceStatusFile(SOptr optr,String deviceStatus,String isNewStb,
			List<DeviceDto> devices,String remark,String deviceType) throws Exception {
		String msg = "";
		String depotId = findDepot(optr);
		devices = removeRepeatDeviceCodes(devices);
		String[] deviceCodes = CollectionHelper.converValueToArray(devices, "device_code");
		List<DeviceDto> deviceList = rDeviceDao.queryByDeviceCodescount(deviceCodes,deviceType);
		Map<String, DeviceDto> map = CollectionHelper.converToMapSingle(
				deviceList, "device_code");
		List<String> deviceIdList = new ArrayList<String>();
		List<String> ignored = new ArrayList<String>();
		for (String d : deviceCodes) {
			DeviceDto e = map.get(d);
			checkDevice(depotId, e,d,true);
			if(!SystemConstants.INVOICE_STATUS_IDLE.equals(e.getDepot_status()) || !depotId.equals(e.getDepot_id()) ){
				ignored.add(d);
			}else{
				deviceIdList.add(e.getDevice_id());
			}
		}
		changeDeviceStatus(optr, deviceStatus, isNewStb,
				deviceIdList.toArray(new String[deviceIdList.size()]),remark);
		msg = "共执行 " + devices.size() + " 条记录,成功 " + (devices.size() - ignored.size() )
				+ " 条,因部分设备不在当前仓库或者状态不是空闲,忽略 " +ignored.size() + " 条!";  
		return msg;
	}

	/**
	 * 批量修改设备状态 ，必须在当前库
	 * @param optrId
	 * @param deviceStatus
	 * @param deviceIds
	 * @throws Exception
	 * @throws ComponentException
	 */
	public void changeDeviceStatus(SOptr optr,String deviceStatus,String isNewStb,String[] deviceIds,String remark) throws Exception {
		String depotId = findDepot(optr);
		Integer doneCode = gDoneCode();
		
		RDeviceEdit deviceEdit = new RDeviceEdit();
		deviceEdit.setDevice_done_code(doneCode);
		deviceEdit.setDepot_id(depotId);
		deviceEdit.setCreate_time(DateHelper.now());
		deviceEdit.setEdit_type("DEVICE_STATUS_R_DEVICE");
		deviceEdit.setOld_value("");
		deviceEdit.setNew_value(deviceStatus);
		deviceEdit.setOptr_id(optr.getOptr_id());
		deviceEdit.setRemark(remark);
		
		RDeviceDoneDeviceid doneDetailId = null;
		List<RDeviceDoneDeviceid> deviceIdList = new ArrayList<RDeviceDoneDeviceid>();
		for(String deviceId : deviceIds){
			doneDetailId = new RDeviceDoneDeviceid();
			doneDetailId.setDevice_done_code(doneCode);
			doneDetailId.setDevice_id(deviceId);
			deviceIdList.add(doneDetailId);
		}
		rDeviceEditDao.save(deviceEdit);
		rDeviceDoneDeviceidDao.save(deviceIdList.toArray(new RDeviceDoneDeviceid[deviceIdList.size()]));
		rDeviceDoneDetailDao.updateByDoneDeviceid(doneCode);
		rDeviceDao.updateDeviceStatus(depotId,deviceIds, deviceStatus,isNewStb);
	}

	protected String canCaSend(SOptr optr,String dataRightType) throws Exception,ComponentException {
		SDataRightType dataRight = sDataRightTypeDao.findByKey(dataRightType);
		if (dataRight == null || SystemConstants.BOOLEAN_TRUE.equals(dataRight.getNull_is_all()))
			return SystemConstants.DEFAULT_DATA_RIGHT;
		
		//取操作员原来信息，参数optr有可能是切换后的
		SOptr sOptr = sOptrDao.findByKey(optr.getOptr_id());
		List<SRole> roleList = sRoleDao.queryByOptrId(sOptr.getOptr_id(),dataRightType,sOptr.getCounty_id());
		for (SRole role : roleList) {
			if (StringHelper.isNotEmpty(role.getData_right_level()))
				return role.getData_right_level();
			else if (StringHelper.isNotEmpty(role.getRule_str())) {
				return role.getRule_str().replaceAll("\"", "'");
			}
		}
		return SystemConstants.DEFAULT_DATA_RIGHT;
	}
	
	public DeviceDto queryDeviceById(String cardId) throws Exception{
		String dataRight = canCaSend(WebOptr.getOptr(), DataRight.CHANGE_COUNTY.toString());
		DeviceDto device = rDeviceDao.queryDeviceByCard(cardId);
		if (device == null) {
			throw new ComponentException("设备【"+cardId+"】不存在");
		}
		
		if(dataRight.equals(SystemConstants.DEFAULT_DATA_RIGHT)){
			if(!device.getDepot_id().equals(WebOptr.getOptr().getDept_id())){
				throw new ComponentException("设备【"+cardId+"】不在当前仓库!");
			}
		}else{
			List<SCounty> countys = sCountyDao.querySwitchCounty(dataRight);
			String countyIds = "";
			for(SCounty county : countys){
				String countyId = county.getCounty_id();
				if(SystemConstants.COUNTY_ALL.equals(countyId)){
					countyIds = SystemConstants.COUNTY_ALL;
					break;
				}else{
					countyIds = StringHelper.append(countyIds,countyId,",");
				}
			}
			List<SDeptDto> depts = sDeptDao.queryAllYYT(countyIds.split(","));
			List<String> list = CollectionHelper.converValueToList(depts, "dept_id");  
			if(!list.contains(device.getCounty_id())){
				throw new ComponentException("设备【"+cardId+"】不在切换部门下的仓库中!");
			}		
		}

		RStb stb = rStbDao.findPairStbByCardDeviceId(device.getDevice_id());
		if(stb != null){
			device.setPair_device_code(stb.getStb_id());
		}else{
			List<CUser> user = cUserDao.queryUserByCardId(cardId);
			if(user.size()>0){
				device.setPair_device_code(user.get(0).getStb_id());
			}
		}
		return device;
	}
	
	public DeviceDto queryDeviceByStbId(String deviceCode,SOptr optr) throws Exception{
		DeviceDto device = rDeviceDao.queryByDeviceCode(deviceCode);
		checkDevice(optr.getDept_id(), device,deviceCode,false);
		return device;
	}
	
	/**
	 * 根据设备类型,设备编号查询配对的设备
	 * @param deviceCode
	 * @param deviceType
	 * @param optr
	 * @return
	 * @throws Exception
	 */
	public DeviceDto getStbCardById(String deviceCode,String deviceType,SOptr optr) throws Exception{
		DeviceDto device = rDeviceDao.getStbCardById(deviceCode,deviceType);
		checkDevice(optr.getDept_id(), device,deviceCode,false);
		return device;
	}
	
	
	public DeviceDto queryStbById(String deviceCode) throws Exception{
		DeviceDto device = rDeviceDao.queryByDeviceCode(deviceCode);
		return device;
	}
	
	/**
	 * 根据ca查询类型
	 * @param Type
	 * @return
	 * @throws Exception
	 */
	public List<TBusiCmdSupplier> querySendTypeByType(String type,String countyId) throws Exception {
		List<TBusiCmdSupplier> list = tBusiCmdSupplierDao.querySendTypeByType(type,countyId);
		return list;
	}
	
	private RDevice queryDiffDevice(SOptr optr,String deviceCode,String depotId) throws Exception{
		if(StringHelper.isEmpty(depotId))
			depotId = findDepot(optr);
		RDevice device = rDeviceDao.queryByDeviceCode(deviceCode);
		checkDevice(depotId, device,deviceCode,false);
		return device;
	}
	
	public List<SDept> queryDeptByOptr(SOptr optr) throws Exception{
		List<SDept> list = sDeptDao.queryAllDept();
		for (int i = list.size() - 1; i >= 0; i--){
			if(list.get(i).getDept_id().equals(optr.getDept_id())){
				list.remove(i);
			}
		}
		return list;
	}
	
	public List<SDept> queryAllDept() throws Exception{
		List<SDept> list = sDeptDao.queryAllDept();
		return list;
	}
	
	public Map<String , Object> queryDeviceStbModem() throws Exception {
		List<RDeviceModel> list = rDeviceDao.queryDeviceStbModem();
		Map<String, List<RDeviceModel>> deviceMap = CollectionHelper.converToMap(list, "device_type");
		Map<String , Object> map = new HashMap<String, Object>();
		map.put("STB", deviceMap.get("STB")); //机顶盒型号
		map.put("MODEM", deviceMap.get("MODEM")); //modem型号
		return map;
	}
	
	
	public String[] getColumnName(String deviceType, String deviceModel) throws Exception{
		if(deviceType.equals(SystemConstants.DEVICE_TYPE_MODEM)){
			return new String[]{"box_no","device_code"};
		}else if(deviceType.equals(SystemConstants.DEVICE_TYPE_STB)){
			RStbModel model =  rStbModelDao.findByKey(deviceModel);
			if(model == null){
				throw new ComponentException(deviceModel+"设备型号不存在");	
			}
			if(SystemConstants.DTV_SERV_TYPE_SINGLE.equals(model.getInteractive_type())){
				return new String[]{"box_no","device_code","pair_device_code"};
			}else if(SystemConstants.DTV_SERV_TYPE_DOUBLE.equals(model.getInteractive_type())){
				return new String[]{"box_no","device_code","modem_mac"};
			}else{
				throw new ComponentException("暂时不支持"+deviceModel+"设备型号的入库");	
			}
		}else{
			throw new ComponentException("暂时不支持"+deviceType+"设备类型的入库");
		}
		
	}
	
	/**
	 * @param deviceTransferDao
	 *            the rDeviceTransferDao to set
	 */
	public void setRDeviceTransferDao(RDeviceTransferDao deviceTransferDao) {
		rDeviceTransferDao = deviceTransferDao;
	}

	/**
	 * @param depotDefineDao
	 *            the rDepotDefineDao to set
	 */
	public void setRDepotDefineDao(RDepotDefineDao depotDefineDao) {
		rDepotDefineDao = depotDefineDao;
	}

	/**
	 * @param deviceSupplierDao
	 *            the rDeviceSupplierDao to set
	 */
	public void setRDeviceSupplierDao(RDeviceSupplierDao deviceSupplierDao) {
		rDeviceSupplierDao = deviceSupplierDao;
	}

	/**
	 * @param deviceInputDao
	 *            the rDeviceInputDao to set
	 */
	public void setRDeviceInputDao(RDeviceInputDao deviceInputDao) {
		rDeviceInputDao = deviceInputDao;
	}

	/**
	 * @param deviceDoneDetailDao
	 *            the rDeviceDoneDetailDao to set
	 */
	public void setRDeviceDoneDetailDao(RDeviceDoneDetailDao deviceDoneDetailDao) {
		rDeviceDoneDetailDao = deviceDoneDetailDao;
	}

	/**
	 * @param deviceOrderDao
	 *            the rDeviceOrderDao to set
	 */
	public void setRDeviceOrderDao(RDeviceOrderDao deviceOrderDao) {
		rDeviceOrderDao = deviceOrderDao;
	}

	/**
	 * @param deviceDoneDeviceidDao
	 *            the rDeviceDoneDeviceidDao to set
	 */
	public void setRDeviceDoneDeviceidDao(
			RDeviceDoneDeviceidDao deviceDoneDeviceidDao) {
		rDeviceDoneDeviceidDao = deviceDoneDeviceidDao;
	}

	public void setRDeviceHisDao(RDeviceHisDao deviceHisDao) {
		rDeviceHisDao = deviceHisDao;
	}
	
	/**
	 * @param stbDao
	 *            the rStbDao to set
	 */
	public void setRStbDao(RStbDao stbDao) {
		rStbDao = stbDao;
	}

	/**
	 * @param cardDao
	 *            the rCardDao to set
	 */
	public void setRCardDao(RCardDao cardDao) {
		rCardDao = cardDao;
	}

	/**
	 * @param modemDao
	 *            the rModemDao to set
	 */
	public void setRModemDao(RModemDao modemDao) {
		rModemDao = modemDao;
	}

	/**
	 * @param deviceOrderDetailDao
	 *            the rDeviceOrderDetailDao to set
	 */
	public void setRDeviceOrderDetailDao(
			RDeviceOrderDetailDao deviceOrderDetailDao) {
		rDeviceOrderDetailDao = deviceOrderDetailDao;
	}

	/**
	 * @param deviceProcureDao the rDeviceProcureDao to set
	 */
	public void setRDeviceProcureDao(RDeviceProcureDao deviceProcureDao) {
		rDeviceProcureDao = deviceProcureDao;
	}

	/**
	 * @param deviceOutputDao the rDeviceOutputDao to set
	 */
	public void setRDeviceOutputDao(RDeviceOutputDao deviceOutputDao) {
		rDeviceOutputDao = deviceOutputDao;
	}

	public void setRDeviceEditDao(RDeviceEditDao deviceEditDao) {
		rDeviceEditDao = deviceEditDao;
	}

	public void setRDeviceReclaimDao(RDeviceReclaimDao deviceReclaimDao) {
		rDeviceReclaimDao = deviceReclaimDao;
	}

	public void setRDeviceChangeDao(RDeviceChangeDao deviceChangeDao) {
		rDeviceChangeDao = deviceChangeDao;
	}

	public void setCustService(ICustServiceExternal custService) {
		this.custService = custService;
	}
	public void setTBusiCmdSupplierDao(TBusiCmdSupplierDao busiCmdSupplierDao) {
		tBusiCmdSupplierDao = busiCmdSupplierDao;
	}
	public void setCUserDao(CUserDao userDao) {
		cUserDao = userDao;
	}

	public void setCDoneCodeDetailDao(CDoneCodeDetailDao doneCodeDetailDao) {
		cDoneCodeDetailDao = doneCodeDetailDao;
	}

	public void setRDeviceUseRecordsDao(RDeviceUseRecordsDao deviceUseRecordsDao) {
		rDeviceUseRecordsDao = deviceUseRecordsDao;
	}
	
	public void setJBusiCmdDao(JBusiCmdDao jBusiCmdDao) {
		this.jBusiCmdDao = jBusiCmdDao;
	}

	public void setSDeptDao(SDeptDao deptDao) {
		sDeptDao = deptDao;
	}

	public void setTDeviceBuyModeDao(TDeviceBuyModeDao deviceBuyModeDao) {
		tDeviceBuyModeDao = deviceBuyModeDao;
	}

	public void setSCountyDao(SCountyDao countyDao) {
		sCountyDao = countyDao;
	}

	public void setRDeviceModelDao(RDeviceModelDao deviceModelDao) {
		this.rDeviceModelDao = deviceModelDao;
	}

	public void setRDeviceTypeDao(RDeviceTypeDao deviceTypeDao) {
		this.rDeviceTypeDao = deviceTypeDao;
	}

	

	
}
