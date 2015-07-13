/**
 * 
 */
package com.ycsoft.business.service.externalImpl;

import java.util.List;

import com.ycsoft.beans.core.prod.CProd;
import com.ycsoft.beans.core.prod.CProdPropChange;
import com.ycsoft.business.commons.pojo.BusiParameter;
import com.ycsoft.business.dto.core.prod.PProdDto;
import com.ycsoft.business.dto.core.prod.ProdDictDto;
import com.ycsoft.business.dto.core.prod.ProdResDto;
import com.ycsoft.business.dto.core.user.UserProdRscDto;
import com.ycsoft.business.service.impl.UserProdService;

/**
 * @author liujiaqi
 * 
 */
public class UserProdServiceExternal extends ParentService implements
		IUserProdServiceExternal {

	public void changeTariff(BusiParameter p, String prodSn,
			String newTariffId, String effDate, String expDate)
			throws Exception {
		UserProdService userProdService = (UserProdService) getBean(
				UserProdService.class, p);
		userProdService.changeTariff(prodSn, newTariffId, effDate, expDate,true);
	}

	public void resetUserProdRes(BusiParameter p) throws Exception {
		UserProdService userProdService = (UserProdService) getBean(
				UserProdService.class, p);
		userProdService.resetUserProdRes();
	}

	public void saveTerminate(BusiParameter p, String[] prodSn,
			String banlanceDealType, String transAcctId, String transAcctItemId)
			throws Exception {
		UserProdService userProdService = (UserProdService) getBean(
				UserProdService.class, p);
		userProdService.saveTerminate(prodSn, banlanceDealType, transAcctId,
				transAcctItemId,"");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ycsoft.business.service.externalImpl.IUserProdServiceExternal#queryOrderdProdByUserId(com.ycsoft.business.commons.pojo.BusiParameter,
	 *      java.lang.String, java.lang.String)
	 */
	public CProd queryOrderdProdByUserId(BusiParameter p, String userId,
			String prodId) throws Exception {
		UserProdService userProdService = (UserProdService) getBean(
				UserProdService.class, p);
		return userProdService.queryOrderdProdByUserId(userId, prodId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ycsoft.business.service.externalImpl.IUserProdServiceExternal#saveOrder(com.ycsoft.business.commons.pojo.BusiParameter,
	 *      java.lang.String, java.lang.String, java.lang.String,
	 *      java.lang.Object, java.lang.Object)
	 */
	public void saveOrder(BusiParameter p, String prodId, String tariffId,
			String feeDate, List<UserProdRscDto> dynamicRscList, String expDate)
			throws Exception {
		UserProdService userProdService = (UserProdService) getBean(
				UserProdService.class, p);
		userProdService.saveOrder(prodId, tariffId, feeDate, dynamicRscList,
				expDate);
	}
	
	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.externalImpl.IUserProdServiceExternal#saveTVOrder(com.ycsoft.business.commons.pojo.BusiParameter, java.lang.String, java.lang.String, java.lang.String, java.util.List, java.lang.String)
	 */
	public void saveTVOrder(BusiParameter p, String prodId, String tariffId,
			String feeDate, int fee)
			throws Exception {
		UserProdService userProdService = (UserProdService) getBean(
				UserProdService.class, p);
		userProdService.saveTVOrder(prodId, tariffId, feeDate, fee);
	}

	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.externalImpl.IUserProdServiceExternal#queryCanOrderProd(com.ycsoft.business.commons.pojo.BusiParameter, java.lang.String[], java.lang.String, java.lang.String)
	 */
	public List<ProdDictDto> queryCanOrderProd(BusiParameter p,
			String[] userIds, String userType, String servType)
			throws Exception {
		UserProdService userProdService = (UserProdService) getBean(
				UserProdService.class, p);
		return userProdService.queryCanOrderProd(userIds, userType, servType);
	}

	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.externalImpl.IUserProdServiceExternal#saveEditProd(com.ycsoft.business.commons.pojo.BusiParameter, java.lang.String, java.util.List)
	 */
	public void saveEditProd(BusiParameter p, String prodSn,
			List<CProdPropChange> propChangeList) throws Exception {
		UserProdService userProdService = (UserProdService) getBean(
				UserProdService.class, p);
		userProdService.saveEditProd(prodSn, propChangeList);
	}

	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.externalImpl.IUserProdServiceExternal#changeExpDate(com.ycsoft.business.commons.pojo.BusiParameter, java.lang.String, java.lang.String)
	 */
	public void changeExpDate(BusiParameter p, String prodSn, String expDate)
			throws Exception {
		UserProdService userProdService = (UserProdService) getBean(
				UserProdService.class, p);
		userProdService.changeExpDate(prodSn, expDate);
	}

	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.externalImpl.IUserProdServiceExternal#queryProdRes(com.ycsoft.business.commons.pojo.BusiParameter, java.lang.String)
	 */
	public List<ProdResDto> queryProdRes(BusiParameter p, String prodId) throws Exception {
		UserProdService userProdService = (UserProdService) getBean(
				UserProdService.class, p);
		return userProdService.queryProdRes(prodId);
	}

	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.externalImpl.IUserProdServiceExternal#queryProdByCounty(com.ycsoft.business.commons.pojo.BusiParameter, java.lang.String, java.lang.String)
	 */
	public List<PProdDto> queryProdByCounty(BusiParameter p, String prodId,
			String countyId) throws Exception {
		UserProdService userProdService = (UserProdService) getBean(
				UserProdService.class, p);
		return userProdService.queryProdByCounty(prodId,countyId);
	}

	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.externalImpl.IUserProdServiceExternal#saveEditCustPkg(com.ycsoft.business.commons.pojo.BusiParameter, java.lang.String, java.lang.String)
	 */
	public void saveEditCustPkg(BusiParameter p,String prodSn)
			throws Exception {
		UserProdService userProdService = (UserProdService) getBean(
				UserProdService.class, p);
		userProdService.saveEditCustPkg(prodSn);
	}

	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.externalImpl.IUserProdServiceExternal#queryCanOrderProdToCallCenter(com.ycsoft.business.commons.pojo.BusiParameter, java.lang.String[], java.lang.String, java.lang.String)
	 */
	public List<PProdDto> queryCanOrderProdToCallCenter(BusiParameter p,
			String[] userIds, String userType, String servType)
			throws Exception {
		UserProdService userProdService = (UserProdService) getBean(
				UserProdService.class, p);
		return userProdService.queryCanOrderProdToCallCenter(userIds, userType, servType);
	}
	
	public void pauseProd(BusiParameter p,Integer doneCode, String prodSn, String userId) throws Exception {
		UserProdService userProdService = (UserProdService) getBean(
				UserProdService.class, p);
		userProdService.tempPauseProd(doneCode, prodSn, userId);
	}

}
