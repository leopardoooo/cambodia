package com.ycsoft.sysmanager.component.resource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.job.JBusiCmd;
import com.ycsoft.beans.device.RCardModel;
import com.ycsoft.beans.device.RDevice;
import com.ycsoft.beans.device.RDeviceModel;
import com.ycsoft.beans.device.RModemModel;
import com.ycsoft.beans.device.RPairCfg;
import com.ycsoft.beans.device.RStbModel;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.business.dao.resource.device.RCardModelDao;
import com.ycsoft.business.dao.resource.device.RDeviceDao;
import com.ycsoft.business.dao.resource.device.RModemModelDao;
import com.ycsoft.business.dao.resource.device.RPairCfgDao;
import com.ycsoft.business.dao.resource.device.RStbModelDao;
import com.ycsoft.commons.abstracts.BaseComponent;
import com.ycsoft.commons.constants.DataRight;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ComponentException;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.sysmanager.dto.resource.CardModelDto;
import com.ycsoft.sysmanager.dto.resource.DeviceDto;
import com.ycsoft.sysmanager.dto.resource.RDeviceModelDto;
import com.ycsoft.sysmanager.dto.resource.StbModelDto;

@Component
public class BaseDeviceComponent extends BaseComponent {

	public RStbModelDao rStbModelDao;
	public RCardModelDao rCardModelDao;
	public RModemModelDao rModemModelDao;
	public RPairCfgDao rPairCfgDao;
	public RDeviceDao rDeviceDao;
	
	protected Integer gDoneCode() throws Exception {
		return Integer.parseInt(cDoneCodeDao.findSequence().toString());
	}

	protected String gDeviceId() throws Exception {
		return rDeviceDao.findSequence().toString();
	}
	
	/**
	 * 查找操作员管理的仓库
	 * @param optrId
	 * @return
	 * @throws Exception
	 * @throws ComponentException
	 */
	protected String findDepot(SOptr optr) throws Exception,
			ComponentException {
		/*String dataRight = this.queryDataRightCon(optr, DataRight.DEVICE_MNG.toString());
		if (DataRightLevel.DEPT.toString().equals(dataRight)){
			return optr.getDept_id();
		} else  {
			return optr.getCounty_id();
		}*/
//		因为有切换仓库功能，所以只取操作员当前操作的仓库
		return optr.getDept_id();
	}
	
	protected RDevice queryDevice(SOptr optr,String deviceCode) throws Exception{
		String depotId = findDepot(optr);
		RDevice device = rDeviceDao.queryByDeviceCode(deviceCode);
		checkDevice(depotId, device,deviceCode,false);
		return device;
	}
	
	/**
	 * 设备入库前验证ID长度是否符合要求.
	 * 返回的Map外层以device_type 为key ,内层以 device_model为key.
	 * @return
	 * @throws Exception
	 */
	public Map<String, Map<String, RDeviceModelDto>> listAllDeviceTypeLengthCfg() throws Exception{
		List<RDeviceModel> list = new ArrayList<RDeviceModel>();
		List<StbModelDto> rstList = rStbModelDao.queryAll();
		CollectionHelper.setValues(rstList, "device_type", "STB");
		list.addAll(rstList);
		List<CardModelDto> cardList = rCardModelDao.queryAll();
		CollectionHelper.setValues(cardList, "device_type", "CARD");
		list.addAll(cardList);
		List<RModemModel> modemList = rModemModelDao.queryAll();
		CollectionHelper.setValues(modemList, "device_type", "MODEM");
		list.addAll(modemList);
		
		List<RPairCfg> allStbCardPariCfgList = rPairCfgDao.findAll();
		
		//机卡配对配置,以stb_model为键值
		Map<String, List<RPairCfg>> allStbCardPariCfgMap = CollectionHelper.converToMap(allStbCardPariCfgList, "stb_model");
		
		
		Map<String, Map<String, RDeviceModelDto>> result = new HashMap<String, Map<String,RDeviceModelDto>>();
		//第一次处理
		Map<String, List<RDeviceModel>> rawMapData = CollectionHelper.converToMap(list, "device_type");
		for(Map.Entry<String, List<RDeviceModel>> entry:rawMapData.entrySet()){
			String key = entry.getKey();
			Map<String, RDeviceModelDto> value = new HashMap<String, RDeviceModelDto>();
			List<RDeviceModel> values =  entry.getValue();
			for(RDeviceModel rdm :values){
				RDeviceModelDto dto = new RDeviceModelDto();
				BeanUtils.copyProperties(rdm, dto);
				if(dto.getDevice_type().equals(SystemConstants.DEVICE_TYPE_STB)){//是机顶盒的时候,填充机卡配对的配置信息
					dto.setStbPairCardCfgs(allStbCardPariCfgMap.get(rdm.getDevice_model()));//设置机卡配对的配置
				}
				value.put(rdm.getDevice_model(), dto );
			}
			
			result.put(key, value);//前面的处理保证了这里不会有已经存在 result.get(key) 的记录
		}
		
		return result;
	}
	
	/**
	 * 验证设备是不是在本地
	 * @param depotId
	 * @param device
	 * @param deviceCode 设备代码.
	 * @param ignoreDepotId 是否忽略是否在当前仓库.
	 * @throws ComponentException
	 */
	protected void checkDevice(String depotId, RDevice device,String deviceCode,boolean ignoreDepotId)
			throws ComponentException {
		if (device == null) {
			throw new ComponentException("设备【"+deviceCode+"】不存在");
		}
		if(!ignoreDepotId){
			if (!depotId.equals(device.getDepot_id())){
				throw new ComponentException("设备【"+deviceCode+"】不在当前仓库，不能操作");
			}
		}
		if (StatusConstants.USE.equals(device.getDepot_status())){
			throw new ComponentException("设备【"+deviceCode+"】已经被使用，不能操作");
		}
		if(SystemConstants.BOOLEAN_TRUE.equals(device.getIs_loss())){
			throw new ComponentException("设备【"+deviceCode+"】已经被挂失，不能操作");
		}
		if (!SystemConstants.OWNERSHIP_GD.equals(device.getOwnership())){
			throw new ComponentException("设备【"+deviceCode+"】产权不是广电，不能操作");
		}
		if (StatusConstants.UNCONFIRM.equals(device.getTran_status())){
			throw new ComponentException("设备【"+deviceCode+"】已被调拨，等待确认中,不能操作");
		}
		if (!SystemConstants.DEVICE_DIFFENCN_TYPE_NODIFF.equals(device.getDiffence_type())){
			throw new ComponentException("设备【"+deviceCode+"】为差异设备,不能操作");
		}
		if (SystemConstants.BOOLEAN_TRUE.equals(device.getIs_virtual())){
			throw new ComponentException("设备【"+deviceCode+"】为虚拟设备,不能操作");
		}
		
	}
	
	/**
	 * 检查设备智能卡是否16位，设备编号是否含有特殊字符
	 * @param d
	 * @return
	 * @throws ComponentException
	 */
	protected boolean checkDeviceCodeNum(DeviceDto d)throws ComponentException {
		String type = d.getDevice_type();
		String deviceCode = d.getDevice_code();
		String pairDeviceCode = d.getPair_device_code();
//		if(type.equals(SystemConstants.DEVICE_TYPE_CARD)&&deviceCode.length()!= 16){
//			 throw new ComponentException("该智能卡号【"+deviceCode+"】不符合标准16位!");
//		}
//		if(type.equals(SystemConstants.DEVICE_TYPE_STB)&& StringHelper.isNotEmpty(pairDeviceCode)&&pairDeviceCode.length()!= 16){
//			 throw new ComponentException("该智能卡号【"+pairDeviceCode+"】不符合标准16位!");
//		}
		
		Pattern p = Pattern.compile("[^a-zA-Z0-9]"); 
		Matcher m = p.matcher(deviceCode); 
		String newDeviceCode = m.replaceAll("");
		if(newDeviceCode.length()!= deviceCode.length()){
			if(type.equals(SystemConstants.DEVICE_TYPE_CARD)){
				throw new ComponentException("该智能卡号【"+deviceCode+"】含有特殊字符!");
			}
			if(type.equals(SystemConstants.DEVICE_TYPE_STB)){
				throw new ComponentException("该机顶盒号【"+deviceCode+"】含有特殊字符!");
			}
		}
		
		if(StringHelper.isNotEmpty(pairDeviceCode)){
			m=p.matcher(pairDeviceCode); 
			newDeviceCode = m.replaceAll("");
			if(newDeviceCode.length()!= pairDeviceCode.length()){
				throw new ComponentException("该智能卡号【"+pairDeviceCode+"】含有特殊字符!");
			}
		}
		return true;
	}
	
	/**
	 * 检查操作员是否有操作某种设备类型的数据权限
	 * @param optr
	 * @param devices
	 * @throws Exception
	 */
	protected void checkDeviceOperRight(SOptr optr,List<DeviceDto> devices) throws Exception {
		String dataRight = null;
		try {
			dataRight = this.queryDataRightCon(optr,DataRight.DEVICE_TYPE_MNG.toString());
		} catch (Exception e) {
			dataRight = SystemConstants.DEFAULT_DATA_RIGHT;
		}
		//若无此数据权限，默认为所有设备都能操作
		if(StringHelper.isNotEmpty(dataRight) && !dataRight.equals(SystemConstants.DEFAULT_DATA_RIGHT)){
			String[] arr = dataRight.substring(dataRight.indexOf("(")+1,
					dataRight.lastIndexOf(")")).replaceAll("'", "").split(",");
			List<String> deviceTypeList = Arrays.asList(arr);
			for(DeviceDto dto : devices){
				if(deviceTypeList.indexOf(dto.getDevice_type()) == -1){
					throw new ComponentException("您没有操作 "
							+ MemoryDict.getDictName(DictKey.DEVICE_TYPE, dto
									.getDevice_type()) + " 类型的数据权限!");
				}
			}
		}
	}
	
	/**
	 * 删除设备编号相同的记录
	 * @param devices
	 * @return
	 * @throws Exception
	 */
	protected List<DeviceDto> removeRepeatDeviceCodes(List<DeviceDto> devices) throws Exception {
		if(devices != null && devices.size()>0){
			for (int i = 0; i < devices.size() - 1; i++) {
				for (int j = devices.size() - 1; j > i; j--) {
					if (devices.get(j).getDevice_code().equals(devices.get(i).getDevice_code())) {
						devices.remove(j);
					}
				}
			}
			return devices;
		}
		return new ArrayList<DeviceDto>();
	}
	
	protected void checkDeviceIsNull(String[] deviceCodes,String name) throws Exception {
		List<String> list = new ArrayList<String>();
		Map<String,String> map = new HashMap<String,String>();
		for(String code : deviceCodes){
			if(map.containsKey(code)){
				list.add(code);
			}else{
				map.put(code, code);
			}
		}
		if(list.size()>0){
			String str = "";
			int t=0;
			int n=0;
			for(String s : list){
				if(t<3){
					str += s+",";
				}
				t++;
				if(t==2){
					str +="<br/> ";
					t=0;
				}
				n++;
				if(n ==20){
					break;
				}
			}
			str ="<br/>"+StringHelper.delEndChar(str,1)+";";
			throw new ComponentException("输入项中重复的"+name+": "+str);
		}
	}
	
	/**
	 * 比较文件解析出设备编号和查询的设备号
	 * @param transferDevices 文件解析出的设备号集合
	 * @param queryDeviceList 在库的设备号集合
	 * @throws Exception
	 */
	protected void compareDevice(List<DeviceDto> transferDevices, List<DeviceDto> queryDeviceList) throws Exception {
		//文件设备编号数量 和 通过设备编号查询出的设备数量不相等时
		if(transferDevices.size() != queryDeviceList.size()){
			//文件中device_code集合
			List<String> fileDevices = CollectionHelper.converValueToList(transferDevices, "device_code");
			//通过文件中device_code查询出来的device_code集合
			List<String> queryDevices = CollectionHelper.converValueToList(queryDeviceList, "device_code");
			int fileCount = fileDevices.size();
			int queryCount = queryDevices.size();
			
			
			fileDevices.removeAll(queryDevices);
			
			if(fileDevices.size() > 0 ){
				throw new ComponentException("设备编号 "+fileDevices+" 错误或者没有入库");
			}
			if(fileCount > queryCount && fileDevices.size() == 0 ){
				throw new ComponentException("设备编号有重复，请确认!");
			}
		}
	}
	
	/**
	 * 根据设备型号 填入设备类型
	 * 是虚拟卡的 填入虚拟信息
	 * 验证配对的设备信号
	 * @param ls
	 * @param deviceModel
	 * @return
	 * 		true 设备了虚拟设备
	 * 		flase 未设置虚拟设备
	 * @throws ComponentException
	 */
	protected boolean fillDeviceModel(Map<String,RStbModel> stbModels,Map<String,RCardModel> cardModels,Map<String,RModemModel> modemModels,List<RPairCfg> pairs, DeviceDto device)
			throws ComponentException {
		
		if(StringHelper.isNotEmpty(device.getPair_device_model())){
			 for (RPairCfg p :pairs){
				 if (p.getStb_model().equals(device.getDevice_model())
						 && p.getCard_model().equals(device.getPair_device_model())){
					 return true;
				 }
			 }
			 throw new ComponentException("设备型号：" + device.getDevice_model_text()
					+ "与" + device.getPair_device_model_text() + "不能配对使用，请检查");
		}else {
			//配对设备号空，设置相应的虚拟卡信息
			RStbModel model = stbModels.get(device.getDevice_model());
			if (model == null)
				throw new ComponentException("错误的设备型号："
						+ device.getDevice_model()+",请检查该型号是否适用当前地区");
			
			if (StringHelper.isNotEmpty(model.getVirtual_card_model())) {
				RCardModel cardM = cardModels.get(model.getVirtual_card_model());
				if(cardM.getIs_virtual().equals(SystemConstants.BOOLEAN_TRUE)){
					if (StringHelper.isNotEmpty(device.getPair_device_code())) {
						throw new ComponentException("设备为机卡一体设备，不需要指定卡号，请确认设备号"
								+ device.getDevice_code());
					}else{ 
						device.setDiffence_type(device.getDiffence_type());
						device.setPair_device_code(device.getDevice_code());
						device.setPair_device_model(model.getVirtual_card_model());
					}
				}
			}else if(StringHelper.isNotEmpty(model.getVirtual_modem_model())){
				RModemModel modemM = modemModels.get(model.getVirtual_modem_model());
				if(modemM.getIs_virtual().equals(SystemConstants.BOOLEAN_TRUE)){
					if (StringHelper.isNotEmpty(device.getPair_device_modem_code())) {
						throw new ComponentException("设备为机卡一体设备，不需要指定卡号，请确认设备号"
								+ device.getDevice_code());
					}else{ 
						device.setDiffence_type(device.getDiffence_type());
						device.setPair_device_modem_code(device.getDevice_code());
						device.setPair_device_model(model.getVirtual_modem_model());
					}
				}
			}
			return true;
		}
	}
	
	public void setRStbModelDao(RStbModelDao stbModelDao) {
		rStbModelDao = stbModelDao;
	}
	public void setRCardModelDao(RCardModelDao cardModelDao) {
		rCardModelDao = cardModelDao;
	}
	public void setRModemModelDao(RModemModelDao modemModelDao) {
		rModemModelDao = modemModelDao;
	}
	public void setRPairCfgDao(RPairCfgDao pairCfgDao) {
		rPairCfgDao = pairCfgDao;
	}
	public void setRDeviceDao(RDeviceDao deviceDao) {
		rDeviceDao = deviceDao;
	}
}
