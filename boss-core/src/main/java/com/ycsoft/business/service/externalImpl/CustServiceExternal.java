/**
 *
 */
package com.ycsoft.business.service.externalImpl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ycsoft.beans.core.cust.CCust;
import com.ycsoft.beans.core.cust.CCustPropChange;
import com.ycsoft.business.commons.pojo.BusiParameter;
import com.ycsoft.business.service.impl.CustService;
import com.ycsoft.business.service.impl.UserProdService;

/**
 * @author liujiaqi
 */
@Service
public class CustServiceExternal extends ParentService implements
		ICustServiceExternal {

	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.externalImpl.ICustServiceExternal#saveCancelDevice(com.ycsoft.business.commons.pojo.BusiParameter, java.lang.String, java.lang.String)
	 */
	public void saveCancelDevice(BusiParameter p, String deviceId,
			String deviceStatus) throws Exception {
		CustService custService = (CustService) getBean(CustService.class, p);
		custService.saveCancelDevice(deviceId, deviceStatus);
	}

	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.externalImpl.ICustServiceExternal#updateAddressList(com.ycsoft.business.commons.pojo.BusiParameter, java.util.List, java.util.List, java.lang.String)
	 */
	public void updateAddressList(BusiParameter p, List<CCust> custAddrList,
			List<CCust> custLinkmanList, String busiCode) throws Exception {
		CustService custService = (CustService) getBean(CustService.class, p);
		custService.updateAddressList(custAddrList, custLinkmanList, busiCode);	
	}

	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.externalImpl.ICustServiceExternal#saveChangeOwnership(com.ycsoft.business.commons.pojo.BusiParameter, java.lang.String)
	 */
	public void saveChangeOwnership(BusiParameter p, String deviceId)
			throws Exception {
		// TODO Auto-generated method stub
		CustService custService = (CustService) getBean(CustService.class, p);
		custService.saveChangeOwnership(deviceId);
	}

	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.externalImpl.ICustServiceExternal#editCust(java.util.List)
	 */
	public void editCust(BusiParameter p, List<CCustPropChange> propChangeList) throws Exception {
		// TODO Auto-generated method stub
		CustService custService = (CustService) getBean(CustService.class, p);
		custService.editCust(propChangeList);
	}

	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.externalImpl.ICustServiceExternal#taskFinish(com.ycsoft.business.commons.pojo.BusiParameter, java.lang.String, boolean, java.lang.String, java.util.Date)
	 */
	public void taskFinish(BusiParameter bp, String taskId, int success,
			String failureCause, Date finishTime) throws Exception {
		CustService custService = (CustService) getBean(CustService.class, bp);
		custService.savetaskFinish(taskId, success, failureCause, finishTime);
	}
	
	public void resumeCustClass(BusiParameter p)throws Exception{
		UserProdService prodService = (UserProdService) getBean(UserProdService.class, p);
		prodService.resumeCustClass();
	}
}
