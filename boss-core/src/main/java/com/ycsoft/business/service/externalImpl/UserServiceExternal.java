/**
 * 
 */
package com.ycsoft.business.service.externalImpl;

import java.util.List;

import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.beans.core.user.CUserPropChange;
import com.ycsoft.beans.prod.PPromotionAcct;
import com.ycsoft.business.commons.pojo.BusiParameter;
import com.ycsoft.business.dto.core.prod.DisctFeeDto;
import com.ycsoft.business.service.impl.QueryUserService;
import com.ycsoft.business.service.impl.UserService;

/**
 * @author liujiaqi
 * 
 */
public class UserServiceExternal extends ParentService implements
		IUserServiceExternal {

	public void saveStop(BusiParameter p, String effectiveDate, int tjFee)
			throws Exception {
		UserService userService = (UserService) getBean(UserService.class, p);
		userService.saveStop(effectiveDate, tjFee);
	}

	public void saveCancelPromotion(BusiParameter p, String promotionSn)
			throws Exception {
		UserService userService = (UserService) getBean(UserService.class, p);
		userService.saveCancelPromotion(promotionSn);
	}

	public void savePromotion(BusiParameter p, int times, String promotionId,
			List<DisctFeeDto> feeList, List<PPromotionAcct> acctList)
			throws Exception {
		UserService userService = (UserService) getBean(UserService.class, p);
		userService.savePromotion(times, promotionId, feeList, acctList);
	}

	public void createUser(BusiParameter p, CUser u) throws Exception {
		UserService userService = (UserService) getBean(UserService.class, p);
		userService.createUser(u);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ycsoft.business.service.externalImpl.IUserServiceExternal#editUser(com.ycsoft.business.commons.pojo.BusiParameter,
	 *      java.util.List)
	 */
	public void editUserStatus(BusiParameter p, List<CUserPropChange> propChangeList)
			throws Exception {
		UserService userService = (UserService) getBean(UserService.class, p);
		userService.editUserStatus(propChangeList);

	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ycsoft.business.service.externalImpl.IUserServiceExternal#editUser(com.ycsoft.business.commons.pojo.BusiParameter,
	 *      java.util.List)
	 */
	public void editUser(BusiParameter p, List<CUserPropChange> propChangeList)
			throws Exception {
		UserService userService = (UserService) getBean(UserService.class, p);
		userService.editUser(propChangeList);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ycsoft.business.service.externalImpl.IUserServiceExternal#saveOpenTemp(com.ycsoft.business.commons.pojo.BusiParameter)
	 */
	public void saveOpenTemp(BusiParameter p) throws Exception {
		UserService userService = (UserService) getBean(UserService.class, p);
		userService.saveOpenTemp();
	}

	
	public void saveBatchOpenTemp(BusiParameter p, String[] userIds) throws Exception {
		String custId = p.getCust().getCust_id();
		QueryUserService queryUserService = (QueryUserService) getBean(QueryUserService.class, p);
		List<CUser> userList = queryUserService.queryUserByCustId(custId);
		
		for (String userId : userIds) {
			for (CUser user : userList) {
				if(userId.equals(user.getUser_id())){
					p.addUser(user); // add to parameter
				}
			}
		}
		
		// execute business
		UserService userService = (UserService) getBean(UserService.class, p);
		userService.saveOpenTempBatch();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ycsoft.business.service.externalImpl.IUserServiceExternal#saveResendCa(com.ycsoft.business.commons.pojo.BusiParameter)
	 */
	public void saveResendCa(BusiParameter p, String[] userIds) throws Exception {
		String custId = p.getCust().getCust_id();
		QueryUserService queryUserService = (QueryUserService) getBean(QueryUserService.class, p);
		List<CUser> userList = queryUserService.queryUserByCustId(custId);
		
		for (String userId : userIds) {
			for (CUser user : userList) {
				if(userId.equals(user.getUser_id())){
					p.addUser(user); // add to parameter
				}
			}
		}
		
		// execute business
		UserService userService = (UserService) getBean(UserService.class, p);
		userService.saveResendCa();

	}
}
