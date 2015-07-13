/**
 * 
 */
package com.ycsoft.business.service.externalImpl;

import java.util.Date;
import java.util.List;

import com.ycsoft.beans.core.cust.CCust;
import com.ycsoft.beans.core.cust.CCustPropChange;
import com.ycsoft.business.commons.pojo.BusiParameter;

/**
 * @author liujiaqi
 * 
 */
public interface ICustServiceExternal {

	void updateAddressList(BusiParameter p, List<CCust> custAddrList,
			List<CCust> custLinkmanList, String busiCode) throws Exception;

	void saveCancelDevice(BusiParameter p, String deviceId, String deviceStatus)
			throws Exception;
	
	/**
	 * 修改设备产权
	 * @param deviceId
	 */
	void saveChangeOwnership(BusiParameter p, String deviceId)throws Exception;
	
	void editCust(BusiParameter p, List<CCustPropChange> propChangeList) throws Exception;
	

	void taskFinish(BusiParameter bp, String taskId, int success,
			String failureCause, Date finishTime)throws Exception;

	void resumeCustClass(BusiParameter p)throws Exception;

}
