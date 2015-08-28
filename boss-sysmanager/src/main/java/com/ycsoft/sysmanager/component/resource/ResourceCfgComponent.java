package com.ycsoft.sysmanager.component.resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TBusiFee;
import com.ycsoft.beans.config.TDeviceBuyMode;
import com.ycsoft.beans.device.RCardModel;
import com.ycsoft.beans.device.RDeviceFee;
import com.ycsoft.beans.device.RDeviceModel;
import com.ycsoft.beans.device.RDeviceModelCounty;
import com.ycsoft.beans.device.RDeviceSupplier;
import com.ycsoft.beans.device.RDeviceType;
import com.ycsoft.beans.device.RModemModel;
import com.ycsoft.beans.device.RStbModel;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.business.component.config.MemoryComponent;
import com.ycsoft.business.dao.config.TBusiFeeDao;
import com.ycsoft.business.dao.config.TDeviceBuyModeDao;
import com.ycsoft.business.dao.resource.device.RCardModelDao;
import com.ycsoft.business.dao.resource.device.RDepotDefineDao;
import com.ycsoft.business.dao.resource.device.RDeviceFeeDao;
import com.ycsoft.business.dao.resource.device.RDeviceModelCountyDao;
import com.ycsoft.business.dao.resource.device.RDeviceModelDao;
import com.ycsoft.business.dao.resource.device.RDeviceSupplierDao;
import com.ycsoft.business.dao.resource.device.RDeviceTypeDao;
import com.ycsoft.business.dao.resource.device.RModemModelDao;
import com.ycsoft.business.dao.resource.device.RStbModelDao;
import com.ycsoft.commons.abstracts.BaseComponent;
import com.ycsoft.commons.constants.DataRight;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ComponentException;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.sysmanager.dto.resource.CardModelDto;
import com.ycsoft.sysmanager.dto.resource.StbModelDto;
import com.ycsoft.sysmanager.dto.system.RDepotDto;
import com.ycsoft.sysmanager.dto.tree.TreeDto;

@Component
public class ResourceCfgComponent extends BaseComponent {
	private TDeviceBuyModeDao tDeviceBuyModeDao;
	private RDeviceSupplierDao rDeviceSupplierDao;
	private RStbModelDao rStbModelDao;
	private RCardModelDao rCardModelDao;
	private RModemModelDao rModemModelDao;
	private RDeviceFeeDao rDeviceFeeDao;
	private RDepotDefineDao rDepotDefineDao;
	private TBusiFeeDao tBusiFeeDao;
	private RDeviceModelCountyDao rDeviceModelCountyDao;
	private MemoryComponent memoryComponent;
	private RDeviceModelDao rDeviceModelDao;
	private RDeviceTypeDao rDeviceTypeDao;
	
	/**
	 * 查询设备的一次性收费
	 * @return
	 * @throws Exception
	 */
	public List<TBusiFee> queryDeviceBusiFee() throws Exception {
		return tBusiFeeDao.queryBusiFeeByFeeType(SystemConstants.FEE_TYPE_DEVICE);
	}
	
	
	
	public String getDepotId() throws Exception {
		return rDepotDefineDao.findSequence().toString();
	}
	
	/**
	 * 保存或修改仓库
	 * @param depot
	 * @return
	 * @throws Exception
	 */
	public boolean saveDepot(RDepotDto depot) throws Exception {
		String depotId = depot.getDepot_id();
		int num = 0;
		if(StringHelper.isEmpty(depotId)){
			depot.setDepot_id(getDepotId());
			depot.setStatus(StatusConstants.ACTIVE);
			num = rDepotDefineDao.save(depot)[0];
		}else{
			//将null置换为空字符串，以供dao的update
			if(StringHelper.isEmpty(depot.getDepot_id()))
				depot.setDepot_id("");
			num = rDepotDefineDao.update(depot)[0];
		}
		return num>0 || num ==-2;
	}
	

	

	/**
	 * 查询购买方式
	 *
	 * @return
	 * @throws JDBCException
	 */
	public List<TDeviceBuyMode> queryDeviceBuyMode() throws Exception {
		return tDeviceBuyModeDao.queryDeviceBuyMode();
	}

	/**
	 * 保存购买方式
	 *
	 * @param deviceBuyMode
	 * @throws JDBCException
	 */
	public void saveDeviceBuyMode(List<TDeviceBuyMode> deviceBuyMode)
			throws JDBCException {
		for (TDeviceBuyMode m : deviceBuyMode) {
			if (tDeviceBuyModeDao.findByKey(m.getBuy_mode()) == null){
				tDeviceBuyModeDao.save(m);
			}else{
				tDeviceBuyModeDao.update(m);
			}
		}
	}

	/**
	 * 删除购买方式
	 * @param deviceBuyMode
	 * @throws JDBCException
	 * @throws ComponentException
	 */
	public void removeDeviceBuyMode(String buyMode)
			throws Exception {
		List<RDeviceFee> l = rDeviceFeeDao.queryByBuyMode(buyMode);
		if (l.size() > 0)
			throw new ComponentException("不能删除已被使用的购买方式:" + buyMode);
		else{
			tDeviceBuyModeDao.remove(buyMode);
		}
	}

	/**
	 * 查询设备供应商
	 *
	 * @return
	 * @throws JDBCException
	 */
	public List<RDeviceSupplier> queryRDeviceSupplier() throws JDBCException {
		return rDeviceSupplierDao.findAll();
	}

	/**
	 * 保存设备供应商
	 *
	 * @param deviceSupplier
	 * @throws JDBCException
	 */
	public void saveRDeviceSupplier(List<RDeviceSupplier> deviceSupplier)
			throws JDBCException {
		for (RDeviceSupplier d : deviceSupplier) {
			if (StringHelper.isEmpty(d.getSupplier_id()))
				rDeviceSupplierDao.save(d);
			else
				rDeviceSupplierDao.update(d);
		}
	}

	/**
	 * 删除设备供应商
	 *
	 * @param deviceSupplier
	 * @throws JDBCException
	 * @throws ComponentException
	 */
	public void removeRDeviceSupplier(String supplierId,String supplierName)
			throws JDBCException, ComponentException {
		List<RStbModel> allDeviceMdoel = rStbModelDao
				.queryAllDeviceMdoelBySupplier(supplierId);
		if (allDeviceMdoel.size() > 0)
			throw new ComponentException("不能删除已被使用的供应商:"
					+ supplierName);
		else
			rDeviceSupplierDao.remove(supplierId);
	}

	/**
	 * 查询设备费用
	 *
	 * @return
	 * @throws JDBCException
	 */
	public List<RDeviceFee> queryRDeviceFee() throws JDBCException {
		return rDeviceFeeDao.queryAll();
	}

	/**
	 * 保存设备费用
	 *
	 * @param deviceFee
	 * @throws JDBCException
	 */
	public void saveRDeviceFee(List<RDeviceFee> deviceFeeList) throws JDBCException {

		RDeviceFee queryRDeviceFee = null;
		for(RDeviceFee deviceFee : deviceFeeList){
			queryRDeviceFee = new RDeviceFee();
			queryRDeviceFee.setDevice_model(deviceFee.getDevice_model());
			queryRDeviceFee.setBuy_mode(deviceFee.getBuy_mode());
			List<RDeviceFee> list = rDeviceFeeDao.findByEntity(queryRDeviceFee);
			if (list.size() == 0)
				rDeviceFeeDao.insert(deviceFee);
			else
				rDeviceFeeDao.update(deviceFee);
		}
	}

	/**
	 * 查询机顶盒型号
	 *
	 * @return
	 * @throws JDBCException
	 */
	public List<StbModelDto> queryRStbModel() throws JDBCException {
		return rStbModelDao.queryAll();
	}
	
	/**
	 * 加载基本配置数据
	 * @param optr
	 * @return
	 * @throws Exception
	 */
	public Map<String , Object> queryCfgLoad(SOptr optr)throws Exception{
		Map<String , Object> map = new HashMap<String, Object>();
		map.put("stbList", queryRStbModel()); //机顶盒型号
		map.put("supplierList", queryRDeviceSupplier()); //供应商配置
		map.put("buyModeList", queryDeviceBuyMode());//设备购买方式
		map.put("cardList", queryRCardModel());//智能卡型号
		map.put("modemList", queryRModemModel());//modem型号
		map.put("modelList", queryDeviceModel());//器材型号
		map.put("typeList", queryDeviceType());//设备类型
		map.put("countyModelList", queryCountyModel(optr));//设备适用地区
		return map;
	}

	public List<RDeviceType> queryDeviceType() throws Exception {
		return rDeviceTypeDao.queryDeviceType();
	}
	
	/**
	 * 查询设备型号
	 * @return
	 */
	public List<RDeviceModel> queryDeviceModel() throws Exception {
		return rDeviceModelDao.queryDeviceModel();
	}
	
	/**
	 * 保存机顶盒型号
	 *
	 * @param model
	 * @throws JDBCException
	 */
	public void saveRStbModel(List<RStbModel> models) throws JDBCException {
		for(RStbModel m: models){
			if (rStbModelDao.findByKey(m.getDevice_model())==null)
				rStbModelDao.save(m);
			else
				rStbModelDao.update(m);
		}
		memoryComponent.addDictSignal(DictKey.STB_MODEL.toString());
	}
	
	
	/**
	 * 更新器材型号
	 * @param list
	 * @throws Exception
	 */
	public void saveMateralModel(List<RDeviceModel> list) throws Exception{
		for(RDeviceModel m: list){
			RDeviceModel r = rDeviceModelDao.findDevice(m.getDevice_type(),m.getDevice_model());
			if (r==null)
				rDeviceModelDao.saveMateral(m.getDevice_type(),m.getDevice_model(),m.getModel_name());
			else
				rDeviceModelDao.updateMateral(m.getDevice_type(),m.getDevice_model(),m.getModel_name());
		}
		memoryComponent.addDictSignal(DictKey.FITTING_MODEL.toString());
	}
	
	/**
	 * 更新设备类型配置
	 * @param list
	 * @throws Exception
	 */
	public void saveDeviceType(List<RDeviceType> list) throws Exception{
		for(RDeviceType m: list){
			if (rDeviceTypeDao.findByKey(m.getDevice_type())==null){
				if(m.getDevice_type().equals(SystemConstants.DEVICE_TYPE_FITTING)){
					m.setManage_detail(SystemConstants.BOOLEAN_FALSE);
				}else{
					m.setManage_detail(SystemConstants.BOOLEAN_TRUE);
				}
				rDeviceTypeDao.save(m);
			}else{
				rDeviceTypeDao.update(m);
			}
		}
		memoryComponent.addDictSignal(DictKey.ALL_DEVICE_TYPE.toString());
		memoryComponent.addDictSignal(DictKey.DEVICE_TYPE.toString());
		memoryComponent.addDictSignal(DictKey.OTHER_DEVICE_TYPE.toString());
	}


	/**
	 * 查询智能卡型号
	 *
	 * @return
	 * @throws JDBCException
	 */
	public List<CardModelDto> queryRCardModel() throws JDBCException {
		return rCardModelDao.queryAll();
	}

	public List<RDeviceModelCounty> queryCountyModel(SOptr optr) throws ComponentException, Exception {
		String dataRight = this.queryDataRightCon(optr, DataRight.CHANGE_COUNTY.toString());
		List<RDeviceModelCounty> countys = rDeviceModelCountyDao.queryCountyByDataRight(dataRight);
		List<RDeviceModelCounty> models = rDeviceModelCountyDao.getModelCounty(dataRight);
		for(RDeviceModelCounty county:countys){
			String stbSrc ="";
			String cardSrc="";
			String modemSrc="";
			for(RDeviceModelCounty mode:models){
				if(county.getCounty_id().equals(mode.getCounty_id())){
					if(mode.getDevice_type().equals(SystemConstants.DEVICE_TYPE_STB)){
						stbSrc += mode.getDevice_model_text()+",";
					}else if(mode.getDevice_type().equals(SystemConstants.DEVICE_TYPE_CARD)){
						cardSrc += mode.getDevice_model_text()+",";
					}else if(mode.getDevice_type().equals(SystemConstants.DEVICE_TYPE_MODEM)){
						modemSrc += mode.getDevice_model_text()+",";
					}else{
						
					}
				}
			}
			stbSrc = StringHelper.delEndChar(stbSrc,1);
			cardSrc = StringHelper.delEndChar(cardSrc,1);
			modemSrc = StringHelper.delEndChar(modemSrc,1);
			county.setStb_model_src(stbSrc);
			county.setCard_model_src(cardSrc);
			county.setModem_model_src(modemSrc);
		}
		return countys;
	}
	
	public List<TreeDto> getModelCountyTree(String countyId,SOptr optr)throws Exception{
		List<TreeDto> countys = null;
		List<TreeDto> valueList = null;
		countys = rDeviceModelCountyDao.getModelCounty();
		if (StringHelper.isNotEmpty(countyId)) {
			valueList = rDeviceModelCountyDao.getModelCountyById(countyId);
		}
		for (int i = 0; i < countys.size(); i++) {
			countys.get(i).setChecked(false);
			if (valueList != null && !"null".equals(valueList)) {
				for (int j = 0; j < valueList.size(); j++) {
					if(countys.get(i).getId().equals(valueList.get(j).getId())){
						countys.get(i).setChecked(true);
					}
				}
			}
		}
		return countys;
	}
	
	public void saveModelCounty(String countyId,List<RDeviceModelCounty> list) throws Exception{
//		List<RDeviceModelCounty> countyList = new ArrayList<RDeviceModelCounty>();
//		for(RDeviceModelCounty dto:list){
//			RDeviceModelCounty county = new RDeviceModelCounty();
//			BeanUtils.copyProperties(dto, county);
//			countyList.add(county);
//		}
		rDeviceModelCountyDao.deleteById(countyId);
		if(list!=null){
			rDeviceModelCountyDao.save(list.toArray(new RDeviceModelCounty[list.size()]));
		}
	}
	
	/**
	 * 保存智能卡型号
	 *
	 * @param model
	 * @throws JDBCException
	 */
	public void saveRCardModel(List<RCardModel> models) throws JDBCException {
		for(RCardModel m: models){
			if (rCardModelDao.findByKey(m.getDevice_model())==null)
				rCardModelDao.save(m);
			else
				rCardModelDao.update(m);
		}
		memoryComponent.addDictSignal(DictKey.CARD_MODEL.toString());
	}
	
	/**
	 * 根据设备类型查询设备型号
	 * @param deviceType
	 * @return
	 * @throws Exception
	 */
	public List queryDeviceModel(String deviceType) throws Exception {
		if(SystemConstants.DEVICE_TYPE_STB.equals(deviceType)){
			return rStbModelDao.findAll();
		}else if(SystemConstants.DEVICE_TYPE_CARD.equals(deviceType)){
			return rCardModelDao.findAll();
		}else if(SystemConstants.DEVICE_TYPE_MODEM.equals(deviceType)){
			return rModemModelDao.findAll();
		}
		return null;
	}

	/**
	 * 查询modem型号
	 *
	 * @return
	 * @throws JDBCException
	 */
	public List<RModemModel> queryRModemModel() throws JDBCException {
		return rModemModelDao.queryAll();
	}

	/**
	 * 保存modem型号
	 *
	 * @param model
	 * @throws JDBCException
	 */
	public void saveRModemModel(List<RModemModel> models) throws JDBCException {
		for (RModemModel m : models) {
			if (rModemModelDao.findByKey(m.getDevice_model()) == null)
				rModemModelDao.save(m);
			else
				rModemModelDao.update(m);
		}
		memoryComponent.addDictSignal(DictKey.MODEM_MODEL.toString());
	}



	/**
	 * @param deviceFeeDao
	 *            the rDeviceFeeDao to set
	 */
	public void setRDeviceFeeDao(RDeviceFeeDao deviceFeeDao) {
		rDeviceFeeDao = deviceFeeDao;
	}

	/**
	 * @param deviceSupplierDao
	 *            the rDeviceSupplierDao to set
	 */
	public void setRDeviceSupplierDao(RDeviceSupplierDao deviceSupplierDao) {
		rDeviceSupplierDao = deviceSupplierDao;
	}

	/**
	 * @param stbModelDao
	 *            the rStbModelDao to set
	 */
	public void setRStbModelDao(RStbModelDao stbModelDao) {
		rStbModelDao = stbModelDao;
	}

	/**
	 * @param cardModelDao
	 *            the rCardModelDao to set
	 */
	public void setRCardModelDao(RCardModelDao cardModelDao) {
		rCardModelDao = cardModelDao;
	}

	/**
	 * @param modemModelDao
	 *            the rModemModelDao to set
	 */
	public void setRModemModelDao(RModemModelDao modemModelDao) {
		rModemModelDao = modemModelDao;
	}

	/**
	 * @param deviceBuyModeDao
	 *            the tDeviceBuyModeDao to set
	 */
	public void setTDeviceBuyModeDao(TDeviceBuyModeDao deviceBuyModeDao) {
		tDeviceBuyModeDao = deviceBuyModeDao;
	}

	public void setRDepotDefineDao(RDepotDefineDao depotDefineDao) {
		rDepotDefineDao = depotDefineDao;
	}

	public void setTBusiFeeDao(TBusiFeeDao busiFeeDao) {
		tBusiFeeDao = busiFeeDao;
	}
	public void setRDeviceModelCountyDao(RDeviceModelCountyDao deviceModelCountyDao) {
		rDeviceModelCountyDao = deviceModelCountyDao;
	}



	/**
	 * @param memoryComponent the memoryComponent to set
	 */
	public void setMemoryComponent(MemoryComponent memoryComponent) {
		this.memoryComponent = memoryComponent;
	}



	public void setRDeviceModelDao(RDeviceModelDao deviceModelDao) {
		this.rDeviceModelDao = deviceModelDao;
	}



	public void setRDeviceTypeDao(RDeviceTypeDao deviceTypeDao) {
		this.rDeviceTypeDao = deviceTypeDao;
	}





}
