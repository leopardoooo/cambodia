package com.ycsoft.sysmanager.web.action.resource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ycsoft.beans.device.RDevice;
import com.ycsoft.beans.device.RDeviceInput;
import com.ycsoft.beans.device.RDeviceOrder;
import com.ycsoft.beans.device.RDeviceOrderDetail;
import com.ycsoft.beans.device.RDeviceOutput;
import com.ycsoft.beans.device.RDeviceProcure;
import com.ycsoft.beans.device.RDeviceTransfer;
import com.ycsoft.commons.abstracts.BaseAction;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ActionException;
import com.ycsoft.commons.exception.ComponentException;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.commons.helper.FileHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.tree.TreeBuilder;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.sysmanager.component.resource.DeviceComponent;
import com.ycsoft.sysmanager.component.resource.JobComponent;
import com.ycsoft.sysmanager.dto.depot.RDeviceTransferDto;
import com.ycsoft.sysmanager.dto.resource.DeviceDto;

@Controller
public class DeviceAction extends BaseAction {

	/**
	 *
	 */
	private static final long serialVersionUID = 732231340135621730L;
	private DeviceComponent deviceComponent;
	private JobComponent jobComponent;

	private int deviceDoneCode;
	private String deviceCode;
	private String deviceType;
	private String modemType;
	private String backup;

	private RDeviceInput deviceInput;
	private RDeviceOutput deviceOutput;
	private RDeviceOrder deviceOrder;
	private RDeviceTransfer deviceTransfer;
	private RDeviceProcure deviceProcure;
	private RDevice device;

	private String deviceOrderDetailList;
	private String deviceDtoList;
	private String confirmInfo;
	private String status;
	private String deviceIds;
	private String deviceStatus;
	private File files;
	private String cardId;
	private String remark;
	private String deviceModel;
	
	private String depotId;
	private String mode;
	private String[] card_models;
	private String depotStatus;
	
	private String query;
	private String isHistory;
	private Integer start;
	private Integer limit;
	private String startDate;
	private String endDate;
	private String transferNo;
	private String inputNo;
	private String procureNo;
	private String outputNo;
	
	private Integer doneCode;
	private String deviceId;
	private String flag;
	private String isLossed;
	private String confirmType;
	private String isNewStb;
	
	private final String PROP_FILE_NAME = "deviceProperty.properties";//设备盘点
	private final String TRANSFER_FILE_NAME = "transferDeviceProperty.properties";//调拨明细
	
	public String updateDeviceLoss() throws Exception {
		deviceComponent.updateDeviceLoss(deviceId, isLossed, optr);
		return JSON;
	}
	
	/**
	 * 设备挂失，空闲设备
	 * @param deviceCode
	 * @return
	 * @throws Exception
	 */
	public String queryIdleDevice() throws Exception {
		getRoot().setPage(deviceComponent.queryIdleDevice(query, optr, start, limit));
		return JSON_PAGE;
	}
	
	public String saveLossDevice() throws Exception {
		String deviceType = deviceComponent.saveLossDevice(deviceCode, optr);
		if (SystemConstants.DEVICE_TYPE_CARD.equals(deviceType))
			jobComponent.createCmdCancelStbFilled(deviceCode, optr);
		return JSON;
	}
	
	/**
	 * 设备入库前验证ID长度是否符合要求.
	 * @return
	 * @throws Exception
	 */
	public String listDeviceLenCfg() throws Exception {
		getRoot().setOthers(deviceComponent.listAllDeviceTypeLengthCfg());
		return JSON_OTHER;
	}
	
	/**
	 * 修改退库单号
	 * @return
	 * @throws Exception
	 */
	public String editOutputNo() throws Exception {
		deviceComponent.editOutputNo(deviceDoneCode, outputNo,remark);
		return JSON;
	}
	
	/**
	 * 修改领用单号
	 * @return
	 * @throws Exception
	 */
	public String editProcureNo() throws Exception {
		deviceComponent.editProcureNo(deviceDoneCode, procureNo);
		return JSON;
	}
	
	/**
	 * 取消设备回收
	 * @return
	 * @throws Exception
	 */
	public String cancelReclaimDevice() throws Exception {
		deviceComponent.cancelReclaimDevice(doneCode, deviceId,optr);
		return JSON;
	}
	/**
	 * 回收设备
	 * @return
	 * @throws Exception
	 */
	public String reclaimDevice() throws Exception {
		deviceComponent.reclaimDevice(optr, doneCode, deviceId, deviceStatus, flag,isNewStb);
		return JSON;
	}
	
	public String queryDeviceReclaim() throws Exception {
		getRoot().setPage(
				deviceComponent.queryDeviceReclaim(optr, query, startDate,
						endDate, deviceType,confirmType,isHistory,start, limit));
		return JSON_PAGE;
	}
	
	public String editInputNo() throws Exception {
		deviceComponent.editInputNo(deviceDoneCode, inputNo,remark);
		return JSON_SUCCESS;
	}
	
	public String editTransferNo() throws Exception {
		deviceComponent.editTransferNo(deviceDoneCode, transferNo,remark);
		return JSON_SUCCESS;
	}
	
	public String editDeviceOrder() throws Exception {
		deviceComponent.editDeviceOrder(deviceDoneCode, isHistory);
		return JSON_SUCCESS;
	}
	
	public String editDeviceTransferHistory() throws Exception {
		deviceComponent.editDeviceTransferHistory(deviceDoneCode, isHistory);
		return JSON_SUCCESS;
	}
	
	private List<Map.Entry<String, String>> sortList(Map<String,String> map){
		List<Map.Entry<String, String>> resultList = new ArrayList<Map.Entry<String, String>>(
				map.entrySet());
		Collections.sort(resultList, new Comparator<Map.Entry<String, String>>() {
			public int compare(Entry<String, String> o1,
					Entry<String, String> o2) {
				int num1 = Integer.parseInt(o1.getValue().substring(0, 2));
				int num2 = Integer.parseInt(o2.getValue().substring(0, 2));
				return num1 - num2;
			}
		});
		for(Map.Entry<String, String> entry : resultList){
			entry.setValue(entry.getValue().substring(2, entry.getValue().length()));
		}
		return resultList;
	}
	
	public String downloadDeviceInfo() throws Exception {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try{
			String[] colName = {"device_code"};
			List<DeviceDto> devices = FileHelper.fileToBean(files, colName, DeviceDto.class);
			CollectionHelper.setValues(devices,"device_type",deviceType);
			
			List<DeviceDto> list = deviceComponent.queryDeviceInfo( devices, deviceType);
			
			Map<String,String> map = new HashMap<String,String>();
			String filePath = ServletActionContext.getServletContext().getRealPath("/")
					+ "WEB-INF" + File.separator + "classes" + File.separator + PROP_FILE_NAME;
			map = FileHelper.getPropertiesMap(filePath);
			if(SystemConstants.DEVICE_TYPE_STB.equals(deviceType)){//如果是机顶盒,增加显示配对智能卡号
				map.put("pair_device_code", "00配对智能卡号");
			}
			
			List<Map.Entry<String, String>> resultList = sortList(map);
			FileHelper.writeExecel(os, list, resultList, ServletActionContext.getServletContext().getRealPath("/"), "设备盘点");
			this.excelStream = new ByteArrayInputStream(os.toByteArray());
		} catch(Exception e){
        	e.printStackTrace();
        }finally{
        	if(os != null){
        		os.close();
        	}
        }
        return EXCEL;
			
	}
	
	public String downloadQueryDevice() throws Exception {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try{
			List<DeviceDto> list = deviceComponent.queryIDLEDeviceByMultiCriteria(deviceModel, depotId, status, mode, depotStatus,backup);
			Map<String,String> map = new HashMap<String,String>();
			map.put("device_code", "01设备编号");
			map.put("device_type_text", "02设备类型");
			map.put("device_model_text", "03设备型号");
			map.put("device_status_text", "04设备状态");
			map.put("depot_status_text", "05库存状态");
			map.put("depot_id_text", "06所在仓库");
			
			List<Map.Entry<String, String>> resultList = sortList(map);
			FileHelper.writeExecel(os, list, resultList, ServletActionContext.getServletContext().getRealPath("/"), "设备查询");
			this.excelStream = new ByteArrayInputStream(os.toByteArray());
		} catch(Exception e){
        	e.printStackTrace();
        }finally{
        	if(os != null){
        		os.close();
        	}
        }
        return EXCEL;
	}
	
	
	public String downloadQueryDeviceDetail() throws Exception {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try{
			
			String batch_num = request.getParameter("batch_num");
			String start_input_time = request.getParameter("start_input_time");
			String end_input_time = request.getParameter("end_input_time");
			List<DeviceDto> list = deviceComponent.queryDeviceDetailByMultiCriteria(deviceModel, depotId, status, mode, 
					depotStatus,modemType,backup,batch_num,start_input_time,end_input_time);
			Map<String,String> map = new HashMap<String,String>();
			map.put("device_code", "01设备编号");
			map.put("device_type_text", "02设备类型");
			map.put("device_model_text", "03设备型号");
			map.put("device_status_text", "04设备状态");
			map.put("depot_status_text", "05库存状态");
			map.put("depot_id_text", "06所在仓库");
			map.put("cust_id", "07客户编号");
			map.put("cust_name", "08客户名称");
			
			List<Map.Entry<String, String>> resultList = sortList(map);
			FileHelper.writeExecel(os, list, resultList, ServletActionContext.getServletContext().getRealPath("/"), "设备查询");
			this.excelStream = new ByteArrayInputStream(os.toByteArray());
		} catch(Exception e){
        	e.printStackTrace();
        }finally{
        	if(os != null){
        		os.close();
        	}
        }
        return EXCEL;
	}
	
	private InputStream excelStream;
	/**
	 * 查询调拨详细信息
	 * @return
	 * @throws Exception
	 */
	public String queryAllTransferDeviceDtail() throws Exception {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try{
			List<RDeviceTransferDto> list = deviceComponent.queryAllTransferDeviceDtail(
					deviceDoneCode, deviceType, deviceModel);
	
			Map<String, String> map = new HashMap<String, String>();
			String filePath = ServletActionContext.getServletContext().getRealPath("/")
					+ "WEB-INF" + File.separator + "classes" + File.separator + TRANSFER_FILE_NAME;
			map = FileHelper.getPropertiesMap(filePath);
			List<Map.Entry<String, String>> resultList = sortList(map);
			
			FileHelper.writeExecel(os, list, resultList, ServletActionContext.getServletContext().getRealPath("/"), "调拨明细");
			this.excelStream = new ByteArrayInputStream(os.toByteArray());
		} catch(Exception e){
        	e.printStackTrace();
        }finally{
        	if(os != null){
        		os.close();
        	}
        }
        return EXCEL;
	}
	
	/**
	 * 查询调拨详细信息
	 * @return
	 * @throws Exception
	 */
	public String queryTransferDeviceDetail() throws Exception {
		getRoot().setPage(deviceComponent.queryTransferDeviceDetail(deviceDoneCode, deviceType, start, limit));
		return JSON_PAGE;
	}

	/**
	 * 根据设备类型查询型号
	 * @return
	 * @throws Exception
	 */
	public String queryDeviceModelByType() throws Exception {
		getRoot().setRecords(deviceComponent.queryDeviceModelByType(deviceType,modemType));
		return JSON_RECORDS;
	}
	
	/**
	 * 根据设备编号查询设备及客户信息
	 * @param deviceCode
	 * @return
	 * @throws Exception
	 */
	public String queryDeviceInfoByCode() throws Exception {
		getRoot().setSimpleObj(deviceComponent.queryDeviceInfoByCode(deviceCode));
		return JSON_SIMPLEOBJ;
	}
	
	/**
	 * 查询智能卡机顶盒绑定设置.
	 * @return
	 * @throws Exception
	 */
	public String queryIdelCardModel() throws Exception {
		getRoot().setRecords(deviceComponent.queryIdelCardModel(mode));
		return JSON_RECORDS;
	}
	
	/**
	 * 查询智能卡机顶盒已绑定的信息.
	 * @return
	 * @throws Exception
	 */
	public String queryStbCardPaired() throws Exception {
		getRoot().setRecords(deviceComponent.queryStbCardPaired(mode));
		return JSON_RECORDS;
	}
	
	public String saveStbCardPairCfg() throws Exception {
		deviceComponent.saveStbCardPairCfg(mode,card_models);
		getRoot().setSimpleObj(true);
		return JSON_SIMPLEOBJ;
	}
	
	/**
	 * 设备多条件查询
	 * @return
	 * @throws Exception
	 */
	public String queryDeviceByMultiCriteria() throws Exception {
		String batch_num = request.getParameter("batch_num");
		String start_input_time = request.getParameter("start_input_time");
		String end_input_time = request.getParameter("end_input_time");
		getRoot().setPage(deviceComponent.queryDeviceByMultiCriteria(deviceModel, depotId, status, mode, 
				depotStatus,modemType,backup,batch_num,start_input_time,end_input_time,start,limit));
		return JSON_PAGE;
	}
	
	/**
	 * 批号查询
	 */
	public String queryDeviceByBatch() throws Exception {
		String batch_num = request.getParameter("batch_num");
		getRoot().setRecords(deviceComponent.queryByBatchNum(getOptr(), batch_num));
		return JSON_RECORDS;
	}
	
	public String isExistsStbCard() throws Exception {
		getRoot().setSuccess(deviceComponent.isExistsStbCard(cardId,deviceCode));
		return JSON;
	}
	
	/**
	 * 检查盒号是否存在
	 * @return
	 * @throws Exception
	 */
	public String isExistsStb() throws Exception {
		getRoot().setSuccess(deviceComponent.isExistsStb(deviceCode));
		return JSON;
	}
	
	/**
	 * 检查卡号是否存在
	 * @return
	 * @throws Exception
	 */
	public String isExistsCard() throws Exception {
		getRoot().setSuccess(deviceComponent.isExistsCard(deviceCode));
		return JSON;
	}
	
	/**
	 * 检查ModemMac号是否存在
	 * @return
	 * @throws Exception
	 */
	public String isExistsModem() throws Exception {
		getRoot().setSuccess(deviceComponent.isExistsModem(deviceCode));
		return JSON;
	}

	/**
	 * 退库
	 * @return
	 * @throws Exception
	 */
	public String saveDeviceOutput() throws Exception {
		Type type = new TypeToken<List<DeviceDto>>(){}.getType();
		List<DeviceDto> list = new Gson().fromJson(deviceDtoList, type);
		deviceComponent.saveDeviceOutput(optr, deviceOutput, list);
		return JSON_SUCCESS;
	}

	/**
	 * 器材退库
	 * @return
	 * @throws Exception
	 */
	public String saveMateralOutput() throws Exception {
		Type type = new TypeToken<List<RDevice>>(){}.getType();
		List<RDevice> list = new Gson().fromJson(deviceDtoList, type);
		deviceComponent.saveMateralOutput(optr, deviceOutput, list);
		return JSON_SUCCESS;
	}
	
	/**
	 * 退库（文件）
	 * @return
	 * @throws Exception
	 */
	public String saveDeviceOutputFile() throws Exception {
		String[] colName = {"device_code"};
		List<DeviceDto> devices = FileHelper.fileToBean(files, colName, DeviceDto.class);
		CollectionHelper.setValues(devices,"device_type",deviceType);
		
		String msg = "";
		try{
			deviceComponent.saveDeviceOutputFile(optr, deviceOutput, devices,deviceType);
		}catch(Exception e){
			e.printStackTrace();
			msg = e.getMessage();
		}
		return retrunNone(msg);
	}

	/**
	 * 查询退库设备信息
	 * @return
	 * @throws Exception
	 */
	public String queryDeviceOutputInfo() throws Exception {
		getRoot().setSimpleObj(deviceComponent.queryDeviceOutputInfo(optr, deviceCode));
		return JSON_SIMPLEOBJ;
	}

	/**
	 * 查询退库清单
	 * @return
	 * @throws Exception
	 */
	public String queryDeviceOutput() throws Exception {
		getRoot().setPage(deviceComponent.queryDeviceOutput(optr, query, start, limit));
		return JSON_PAGE;
	}

	/**
	 * 批量修改设备状态 ，必须在当前库(文件)
	 * @return
	 * @throws Exception
	 */
	public String changeDeviceStatusFile() throws Exception {

		String[] colName = {"device_code"};
		List<DeviceDto> devices = FileHelper.fileToBean(files, colName, DeviceDto.class);
		CollectionHelper.setValues(devices,"device_type",deviceType);
		String msg = "";
		try{
			msg = deviceComponent.changeDeviceStatusFile(optr, deviceStatus,isNewStb, devices,remark,deviceType);
		}catch(Exception e){
			e.printStackTrace();
			msg = e.getMessage();
		}
		return retrunNone(msg);
	}

	/**
	 * 批量修改设备状态 ，必须在当前库
	 * @return
	 * @throws Exception
	 */
	public String changeDeviceStatus() throws Exception {
		deviceComponent.changeDeviceStatus(optr, deviceStatus,isNewStb, deviceIds.split(","), remark);
		return JSON_SUCCESS;
	}

	/**
	 * 查询状态修改设备
	 * @return
	 * @throws Exception
	 */
	public String queryDeviceStatusChangeInfo() throws Exception {
		if(device == null || StringHelper.isEmpty(device.getDevice_id())){
			throw new ActionException("参数有误!");
		}
		getRoot().setSimpleObj(deviceComponent.queryCanUpdateDeviceStatus(optr,device));
		return JSON_SIMPLEOBJ;
	}

	/**
	 * 保存领用设备
	 * @return
	 * @throws Exception
	 */
	public String saveDeviceProcure() throws Exception {
		Type type = new TypeToken<List<DeviceDto>>(){}.getType();
		List<DeviceDto> list = new Gson().fromJson(deviceDtoList, type);
		deviceComponent.saveDeviceProcure(optr, deviceProcure, list);
		return JSON_SUCCESS;
	}
	
	public String saveCancelProcure() throws Exception {
		deviceComponent.saveCancelProcure(deviceDoneCode, deviceId, optr);
		return JSON_SUCCESS;
	}

	/**
	 * 查询领用的设备信息
	 * @return
	 * @throws Exception
	 */
	public String queryDeviceProcureInfo() throws Exception {
		getRoot().setSimpleObj(deviceComponent.queryCanProcureDevice(optr, deviceCode));
		return JSON_SIMPLEOBJ;
	}

	/**
	 * 查询领用清单
	 * @return
	 * @throws Exception
	 */
	public String queryDeviceProcure() throws Exception {
		getRoot().setPage(deviceComponent.queryDeviceProcure(optr, query, start, limit));
		return JSON_PAGE;
	}
	
	/**
	 * 查询领用清单对应设备信息
	 * @return
	 * @throws Exception
	 */
	public String queryProcureDeviceDetail() throws Exception {
		getRoot().setRecords(deviceComponent.queryProcureDeviceDetail(deviceDoneCode,deviceType));
		return JSON_RECORDS;
	}

	/**
	 * 查询当前仓库的差异
	 * @return
	 * @throws Exception
	 */
	public String queryDeviceDiffence() throws Exception {
		getRoot().setPage(deviceComponent.queryDeviceDiffence(query,optr,depotId,start,limit));
		return JSON_PAGE;
	}

	/**
	 * 保存差异
	 * @return
	 * @throws Exception
	 */
	public String addDeviceDiffence() throws Exception {
		deviceComponent.addDeviceDiffence(optr,deviceIds);
		return JSON_SUCCESS;
	}
	/**
	 * 保存文件差异
	 * @return
	 * @throws Exception
	 */
	public String addFileDeviceDiffence() throws Exception {
		String msg="";
		try{
			String[] colName = {"device_code"};
			List<DeviceDto> devices = FileHelper.fileToBean(files, colName, DeviceDto.class);
			deviceComponent.addFileDeviceDiffence(devices,optr,depotId,remark);
		}catch(Exception e ){
			msg = e.getMessage();
			e.printStackTrace();
		}
		return retrunNone(msg);
	}

	/**
	 * 取消差异
	 * @return
	 * @throws Exception
	 */
	public String cancelDeviceDiffence() throws Exception {
		deviceComponent.cancelDeviceDiffence(deviceIds.split(","));
		return JSON_SUCCESS;
	}

	/**
	 * 确认差异
	 * @return
	 * @throws Exception
	 */
	public String checkDeviceDiffence() throws Exception {
		deviceComponent.checkDeviceDiffence(deviceIds.split(","));
		return JSON_SUCCESS;
	}

	/**
	 * 根据设备编号查询差异确认信息
	 * @return
	 * @throws Exception
	 */
	public String queryDeviceDiffecnceInfo() throws Exception {
		getRoot().setSimpleObj(deviceComponent.queryDeviceDiffecnceInfo(optr, deviceCode,depotId));
		return JSON_SIMPLEOBJ;
	}

	/**
	 * 确认调拨
	 * @return
	 * @throws Exception
	 */
	public String checkTransfer() throws Exception {
		deviceComponent.checkTransfer(optr, deviceDoneCode, status, confirmInfo);
		return JSON_SUCCESS;
	}

	/**
	 * 保存调拨
	 * @return
	 * @throws Exception
	 */
	public String saveTransfer() throws Exception {
		Type type = new TypeToken<List<DeviceDto>>(){}.getType();
		List<DeviceDto> list = new Gson().fromJson(deviceDtoList, type);
		deviceComponent.saveTransfer(optr, deviceTransfer, list);
		return JSON_SUCCESS;
	}
	
	public String saveMateralTransfer() throws Exception {
		Type type = new TypeToken<List<RDevice>>(){}.getType();
		List<RDevice> list = new Gson().fromJson(deviceDtoList, type);
		deviceComponent.saveMateralTransfer(optr, deviceTransfer, list);
		return JSON_SUCCESS;
	}
	
	
	/**
	 * 查询可以用来转发的设备.
	 * @return
	 * @throws Exception
	 */
	public String queryReTransDevices() throws Exception {
		Map<String, Map> devices = deviceComponent.queryTransInfoByDoneCode(doneCode);
		getRoot().setOthers(devices);
		return JSON_OTHER;
	}
	
	/**
	 * 查询可以用来转发的设备.
	 * @return
	 * @throws Exception
	 */
	public String reTransDevices() throws Exception {
		deviceComponent.saveReTransDevices(optr, deviceTransfer,doneCode);
		return JSON_SUCCESS;
	}
	
	
	
	/**
	 * 查询当前仓库及以下子仓库
	 * @return
	 * @throws Exception
	 */
	public String queryChildDepot() throws Exception {
		List list = deviceComponent.queryChildDepot(optr);
		getRoot().setRecords(TreeBuilder.createTree(list));
		return JSON_RECORDS;
	}

	/**
	 * 查询当前仓库可以流转的上下级仓库
	 * @return
	 * @throws Exception
	 */
	public String queryTransferDepot() throws Exception {
		getRoot().setRecords(deviceComponent.queryTransferDepot(optr));
		return JSON_RECORDS;
	}
	
	/**
	 * 查询排除自己外的部门
	 * @return
	 * @throws Exception
	 */
	public String queryDeptByOptr() throws Exception {
		getRoot().setRecords(deviceComponent.queryDeptByOptr(optr));
		return JSON_RECORDS;
	}
	
	public String queryAllDept() throws Exception {
		getRoot().setRecords(deviceComponent.queryAllDept());
		return JSON_RECORDS;
	}

	/**
	 * 根据设备编号查询信息
	 * @return
	 * @throws Exception
	 */
	public String queryDeviceInfo() throws Exception {
		getRoot().setSimpleObj(deviceComponent.queryCanOptrDevice(optr,deviceCode));
		return JSON_SIMPLEOBJ;
	}

	/**
	 * 查询所有设备型号
	 * @return
	 * @throws JDBCException
	 */
	public String queryAllModel() throws Exception{
		getRoot().setRecords(deviceComponent.queryAllDeviceMdoel());
		return JSON_RECORDS;
	}
	
	/**
	 * 查询设备调拨
	 * @return
	 * @throws Exception
	 */
	public String queryDeviceTransfer() throws Exception {
		if(StringHelper.isEmpty(startDate)){
			startDate = endDate;
		}
		if(StringHelper.isEmpty(endDate)){
			endDate = startDate;
		}
		getRoot().setPage(deviceComponent.queryDeviceTransfer(optr, query,
						startDate, endDate, start, limit, isHistory,deviceModel));
		return JSON_PAGE;
	}

	/**
	 * 保存或修改产品订单
	 * @return
	 * @throws Exception
	 */
	public String saveDeviceOrder() throws Exception {
		Type type = new TypeToken<List<RDeviceOrderDetail>>(){}.getType();
		List<RDeviceOrderDetail> list = new Gson().fromJson(deviceOrderDetailList, type);
		deviceComponent.saveDeviceOrder(optr,deviceOrder, list);
		return JSON_SUCCESS;
	}

	/**
	 * 查询设备到货明细
	 * @return
	 * @throws Exception
	 */
	public String queryDeviceOrderInputDetail() throws Exception {
		getRoot().setRecords(deviceComponent.queryDeviceOrderInputDetail(deviceDoneCode));
		return JSON_RECORDS;
	}

	/**
	 * 查询设备订单明细
	 * @return
	 * @throws Exception
	 */
	public String queryDeviceOrderDetail() throws Exception {
		getRoot().setRecords(deviceComponent.queryDeviceOrderDetail(deviceDoneCode));
		return JSON_RECORDS;
	}

	/**
	 * 保存调拨(文件)
	 * @return
	 * @throws Exception
	 */
	public String saveTransferFile() throws Exception {
		String[] colName = {"device_code"};
		List<DeviceDto> devices = FileHelper.fileToBean(files, colName, DeviceDto.class);
		CollectionHelper.setValues(devices,"device_type",deviceType);
		String msg = "";
		try{
			deviceComponent.saveTransferFile(optr, deviceTransfer, devices,deviceType);
		}catch(Exception e){
			e.printStackTrace();
			msg = e.getMessage();
		}
		return retrunNone(msg);
	}
	
	private List<DeviceDto> check(File files,String deviceType) throws ComponentException,Exception{
		List<DeviceDto> devices = null;
		try {
			String[] colName = getColumnName(deviceType);
			devices = FileHelper.fileToBean(files, colName, DeviceDto.class);
			boolean key = false;
			CollectionHelper.setValues(devices,"device_type",deviceType);
			if(colName.length==3){
				if(StringHelper.isEmpty(devices.get(0).getDevice_model())||StringHelper.isEmpty(devices.get(0).getDevice_code())){
					key = true;
				}
			}
			if(colName.length==4){
				if(StringHelper.isEmpty(devices.get(0).getDevice_model())||StringHelper.isEmpty(devices.get(0).getModem_mac())){
					key = true;
				}
			}
			if(key){
				throw new ComponentException("导入格式不对!【机顶盒】：第一列：机顶盒型号,第二列：机顶盒编号,第三列：配对智能卡编号,第四列：配对MODEM编号" +
				"【智能卡】:第一列：设备型号,第二列：设备编号【modem】：第一列：modem型号,第二列：mac地址, 第三列：modem编号；最后一列为批号");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return devices;
	}

	/**
	 * 保存入库信息 (文件)
	 * @return
	 * @throws Exception
	 */
	public String saveDeviceInputFile() throws Exception,ComponentException{
		String msg = "";
		try{
			List<DeviceDto> devices = check(files, deviceType);
			deviceComponent.saveDeviceInputFile(optr, deviceInput, devices,deviceType);
		}catch(Exception e){
			e.printStackTrace();
			msg = e.getMessage();
		}
		return retrunNone(msg);
	}

	/**
	 * 手动入库
	 * @return
	 * @throws Exception
	 */
	public String saveDeviceInput() throws Exception {
		Type type = new TypeToken<List<DeviceDto>>(){}.getType();
		List<DeviceDto> list = new Gson().fromJson(deviceDtoList, type);
		String batch_num = request.getParameter("batch_num");
		if(StringHelper.isNotEmpty(batch_num)){
			for (DeviceDto deviceDto : list) {
				deviceDto.setBatch_num(batch_num);
			}
		}
		deviceComponent.saveDeviceInput(optr, deviceInput, list,SystemConstants.DEVICE_CFG_TYPE_HAND);
		return JSON_SUCCESS;
	}
	
	/**
	 * 器材入库
	 * @return
	 * @throws Exception
	 */
	public String saveMateralDeviceInput() throws Exception {
		String batchNum = request.getParameter("batch_num");
		String deviceType =  SystemConstants.DEVICE_TYPE_FITTING;
		String deviceModel = request.getParameter("device_model");
		String totalNum = request.getParameter("total_num");
		deviceComponent.saveMateralDeviceInput(optr, deviceInput, batchNum,deviceType,deviceModel,totalNum);
		return JSON_SUCCESS;
	}
	

	/**
	 * 查询待收货设备订单
	 * @return
	 * @throws Exception
	 */
	public String queryDeviceOrder() throws  Exception {
		getRoot().setPage(deviceComponent.queryDeviceOrder(optr, start, limit, query, isHistory));
		return JSON_PAGE;
	}

	/**
	 * 获取所有的供应商
	 * @return
	 * @throws Exception
	 */
	public String queryDeviceSupplier() throws Exception {
		getRoot().setRecords(deviceComponent.queryDeviceSupplier());
		return JSON_RECORDS;
	}

	/**
	 * 查询设备操作明细
	 * @return
	 * @throws Exception
	 */
	public String queryDeviceDoneDetail() throws Exception {
		getRoot().setRecords(deviceComponent.queryDeviceDoneDetail(deviceDoneCode));
		return JSON_RECORDS;
	}

	/**
	 * 查找入库信息
	 * @return
	 * @throws Exception
	 */
	public String queryDeviceInput() throws Exception {
		getRoot().setPage(deviceComponent.queryDeviceInput(optr, query, start, limit));
		return JSON_PAGE;
	}
	
	/**
	 * 查询设备修改信息(设备状态修改信息)
	 * @return
	 * @throws Exception
	 */
	public String queryDeviceStatusEdit() throws Exception {
		getRoot().setPage(deviceComponent.queryDeviceStatusEdit(optr, query, start, limit));
		return JSON_PAGE;
	}
	
	public String queryEditDeviceDetail() throws Exception {
		getRoot().setPage(deviceComponent.queryEditDeviceDetail(deviceDoneCode,deviceType, start, limit, optr.getOptr_id()));
		return JSON_PAGE;
	}
	
	public String queryOutputDeviceDetail() throws Exception {
		getRoot().setPage(deviceComponent.queryOutputDeviceDetail(deviceDoneCode, deviceType,deviceModel, start, limit, optr.getOptr_id()));
		return JSON_PAGE;
	}

	/**
	 * 查询往日入库未确认
	 * @return
	 * @throws Exception
	 */
	public String queryUnCheckInput() throws Exception {
		getRoot().setRecords(deviceComponent.queryUnCheckInput(optr));
		return JSON_RECORDS;
	}

	/**
	 * 查询往日出库未确认
	 * @return
	 * @throws Exception
	 */
	public String queryUnCheckOutput() throws Exception {
		getRoot().setRecords(deviceComponent.queryUnCheckOutput(optr));
		return JSON_RECORDS;
	}

	/**
	 * 查询当天入库库确认
	 * @return
	 * @throws Exception
	 */
	public String queryTodayCheckInput() throws Exception {
		getRoot().setRecords(deviceComponent.queryTodayCheckInput(optr));
		return JSON_RECORDS;
	}

	/**
	 * 查询当天出库确认
	 * @return
	 * @throws Exception
	 */
	public String queryTodayCheckOutput() throws Exception {
		getRoot().setRecords(deviceComponent.queryTodayCheckOutput(optr));
		return JSON_RECORDS;
	}

	/**
	 * 根据设备类型获取表头列
	 * @param deviceType
	 * @return
	 * @throws Exception
	 */
	private String[] getColumnName(String deviceType) throws Exception {
		if (SystemConstants.DEVICE_TYPE_STB.equals(deviceType)){
			return new String[]{"device_model","device_code","pair_device_code","modem_mac","batch_num"};
		}else if(SystemConstants.DEVICE_TYPE_CARD.equals(deviceType)){
			return new String[]{"device_model","device_code","batch_num"};
		}else if (SystemConstants.DEVICE_TYPE_MODEM.equals(deviceType)){
			return new String[]{"device_model","modem_mac","device_code","batch_num"};
		}
		return null;
	}
	public String queryDeviceById() throws Exception {
		getRoot().setSimpleObj(deviceComponent.queryDeviceById(deviceCode));
		return JSON_SIMPLEOBJ;
	}
	
	public String getStbCardById() throws Exception {
		getRoot().setSimpleObj(deviceComponent.getStbCardById(deviceCode,deviceType,optr));
		return JSON_SIMPLEOBJ;
	}
	
	public String queryDeviceByStbId() throws Exception {
		getRoot().setSimpleObj(deviceComponent.queryDeviceByStbId(deviceCode,optr));
		return JSON_SIMPLEOBJ;
	}
	public String queryStbById() throws Exception {
		getRoot().setSimpleObj(deviceComponent.queryStbById(deviceCode));
		return JSON_SIMPLEOBJ;
	}
	
	public String querySendTypeByType() throws Exception {
		String countyId = request.getParameter("county_id");
		getRoot().setRecords(deviceComponent.querySendTypeByType(deviceType, countyId));
		return JSON_RECORDS;
	}
	
	
	public String queryMateralTransferDeviceByDepotId() throws Exception {
		getRoot().setRecords(deviceComponent.queryMateralTransferDeviceByDepotId(getOptr()));
		return JSON_RECORDS;
	}
	
	public void setDeviceDoneCode(int deviceDoneCode) {
		this.deviceDoneCode = deviceDoneCode;
	}

	public RDeviceInput getDeviceInput() {
		return deviceInput;
	}

	public void setDeviceInput(RDeviceInput deviceInput) {
		this.deviceInput = deviceInput;
	}

	public void setDeviceComponent(DeviceComponent deviceComponent) {
		this.deviceComponent = deviceComponent;
	}

	public void setDeviceDtoList(String deviceDtoList) {
		this.deviceDtoList = deviceDtoList;
	}

	public RDeviceOrder getDeviceOrder() {
		return deviceOrder;
	}

	public void setDeviceOrder(RDeviceOrder deviceOrder) {
		this.deviceOrder = deviceOrder;
	}

	public void setDeviceOrderDetailList(String deviceOrderDetailList) {
		this.deviceOrderDetailList = deviceOrderDetailList;
	}

	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
	}

	public RDeviceTransfer getDeviceTransfer() {
		return deviceTransfer;
	}

	public void setDeviceTransfer(RDeviceTransfer deviceTransfer) {
		this.deviceTransfer = deviceTransfer;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setConfirmInfo(String confirmInfo) {
		this.confirmInfo = confirmInfo;
	}

	public File getFiles() {
		return files;
	}

	public void setFiles(File files) {
		this.files = files;
	}

	public void setDeviceIds(String deviceIds) {
		this.deviceIds = deviceIds;
	}

	public RDeviceProcure getDeviceProcure() {
		return deviceProcure;
	}

	public void setDeviceProcure(RDeviceProcure deviceProcure) {
		this.deviceProcure = deviceProcure;
	}

	public void setDeviceStatus(String deviceStatus) {
		this.deviceStatus = deviceStatus;
	}

	public RDevice getDevice() {
		return device;
	}

	public void setDevice(RDevice device) {
		this.device = device;
	}
	/**
	 * @return the deviceType
	 */
	public String getDeviceType() {
		return deviceType;
	}

	/**
	 * @param deviceType the deviceType to set
	 */
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public RDeviceOutput getDeviceOutput() {
		return deviceOutput;
	}

	public void setDeviceOutput(RDeviceOutput deviceOutput) {
		this.deviceOutput = deviceOutput;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public void setIsHistory(String isHistory) {
		this.isHistory = isHistory;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public void setTransferNo(String transferNo) {
		this.transferNo = transferNo;
	}

	public void setInputNo(String inputNo) {
		this.inputNo = inputNo;
	}

	public void setDoneCode(Integer doneCode) {
		this.doneCode = doneCode;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public void setProcureNo(String procureNo) {
		this.procureNo = procureNo;
	}

	public void setOutputNo(String outputNo) {
		this.outputNo = outputNo;
	}

	public void setDepotId(String depotId) {
		this.depotId = depotId;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public void setDepotStatus(String depotStatus) {
		this.depotStatus = depotStatus;
	}

	public InputStream getExcelStream() {
		return excelStream;
	}

	public void setExcelStream(InputStream excelStream) {
		this.excelStream = excelStream;
	}

	public void setIsLossed(String isLossed) {
		this.isLossed = isLossed;
	}

	/**
	 * @param jobComponent the jobComponent to set
	 */
	public void setJobComponent(JobComponent jobComponent) {
		this.jobComponent = jobComponent;
	}

	public void setModemType(String modemType) {
		this.modemType = modemType;
	}

	public void setConfirmType(String confirmType) {
		this.confirmType = confirmType;
	}

	public void setCard_models(String[] card_models) {
		this.card_models = card_models;
	}

	public void setBackup(String backup) {
		this.backup = backup;
	}

	public void setIsNewStb(String isNewStb) {
		this.isNewStb = isNewStb;
	}

}
