package com.ycsoft.sysmanager.component.resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ycsoft.beans.device.RDevice;
import com.ycsoft.beans.device.RDeviceTransfer;
import com.ycsoft.business.dao.resource.device.RDeviceDao;
import com.ycsoft.business.dao.resource.device.RDeviceTransferDao;
import com.ycsoft.commons.abstracts.BaseComponent;
import com.ycsoft.sysmanager.print.PrintContentConfiguration;

/**
 * @author danjp
 *
 */
@Component
public class DevicePrintComponent extends BaseComponent {
	@Autowired
	private RDeviceDao rDeviceDao;
	@Autowired
	private RDeviceTransferDao rDeviceTransferDao;
	
	//调拨设备信息打印
	public Map queryTransferdevicePrintInfo(Integer deviceDoneCode) throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		Map<String, Object> data = new HashMap<String, Object>();
		//xml文件中数据
		this.putTransferDeviceInfo(data, deviceDoneCode);
		
		
		//xml文件内容
		String content = PrintContentConfiguration.getTemplate("transferDevice.xml");
		map.put("content", content);
		map.put("data", data);
		
		return map;
	}
	
	private void putTransferDeviceInfo(Map<String, Object> map, Integer deviceDoneCode) throws Exception {
		List<RDevice> deviceList = rDeviceDao.queryDeviceInfoByDoneCode(deviceDoneCode);
		map.put("deviceListInfo", deviceList);
		
		RDeviceTransfer transfer = rDeviceTransferDao.findByKey(deviceDoneCode);
		map.put("transferDevice", transfer);
	}
	
	public void downloadTransferDeviceInfo(Integer deviceDoneCode) throws Exception {
		
	}
}