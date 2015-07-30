/**
 * 
 */
package com.ycsoft.business.service.externalImpl;

import java.util.List;
import java.util.Map;

import com.ycsoft.beans.core.common.CDoneCode;
import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.business.commons.pojo.BusiParameter;
import com.ycsoft.business.dto.core.bill.UserBillDto;
import com.ycsoft.business.dto.core.prod.CProdDto;
import com.ycsoft.business.dto.core.prod.PProdDto;
import com.ycsoft.business.dto.core.prod.UserProdDto;
import com.ycsoft.business.dto.core.user.ChangedUser;
import com.ycsoft.business.dto.core.user.UserDto;
import com.ycsoft.business.service.impl.QueryUserService;
import com.ycsoft.daos.core.Pager;

/**
 * @author liujiaqi
 *
 */
public class QueryUserServiceExternal extends ParentService implements IQueryUserServiceExternal {

	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.externalImpl.IQueryUserServiceExternal#queryUser(com.ycsoft.business.commons.pojo.BusiParameter, java.lang.String)
	 */
	public List<UserDto> queryUser(BusiParameter p, String custId) throws Exception {
		QueryUserService queryUserService = (QueryUserService) getBean(
				QueryUserService.class, p);
		return queryUserService.queryUser(custId);
	}

	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.externalImpl.IQueryUserServiceExternal#queryUserByCustId(com.ycsoft.business.commons.pojo.BusiParameter, java.lang.String)
	 */
	public List<CUser> queryUserByCustId(BusiParameter p, String custId) throws Exception {
		QueryUserService queryUserService = (QueryUserService) getBean(
				QueryUserService.class, p);
		return queryUserService.queryUserByCustId(custId);
	}

	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.externalImpl.IQueryUserServiceExternal#queryUserByDeviceId(com.ycsoft.business.commons.pojo.BusiParameter, java.lang.String)
	 */
	public UserDto queryUserByDeviceId(BusiParameter p, String deviceId) throws Exception {
		QueryUserService queryUserService = (QueryUserService) getBean(
				QueryUserService.class, p);
		return queryUserService.queryUserByDeviceId(deviceId);
	}

	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.externalImpl.IQueryUserServiceExternal#queryUserById(com.ycsoft.business.commons.pojo.BusiParameter, java.lang.String)
	 */
	public CUser queryUserById(BusiParameter p, String userId)
			throws Exception {
		QueryUserService queryUserService = (QueryUserService) getBean(
				QueryUserService.class, p);
		return queryUserService.queryUserById(userId);
	}

	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.externalImpl.IQueryUserServiceExternal#querProdByUserId(com.ycsoft.business.commons.pojo.BusiParameter, java.lang.String)
	 */
	public List<CProdDto> querProdByUserId(BusiParameter p, String userId)
			throws Exception {
		QueryUserService queryUserService = (QueryUserService) getBean(
				QueryUserService.class, p);
		return queryUserService.querProdByUserId(userId);
	}

	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.externalImpl.IQueryUserServiceExternal#queryUserDoneCode(com.ycsoft.business.commons.pojo.BusiParameter, java.lang.String)
	 */
	public List<CDoneCode> queryUserDoneCode(BusiParameter p, String userId) throws Exception {
		QueryUserService queryUserService = (QueryUserService) getBean(
				QueryUserService.class, p);
		return queryUserService.queryUserDoneCode(userId);
	}

	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.externalImpl.IQueryUserServiceExternal#queryProdByCountyId(com.ycsoft.business.commons.pojo.BusiParameter, java.lang.String, java.lang.String, java.lang.String, java.lang.Object, java.lang.String)
	 */
	public List<PProdDto> queryProdByCountyId(BusiParameter p, String countyId,
			String prodStatus, String tariffStatus, String ruleId,
			String tariffType) throws Exception {
		QueryUserService queryUserService = (QueryUserService) getBean(
				QueryUserService.class, p);
		return queryUserService.queryProdByCountyId(countyId, prodStatus,
				tariffStatus, ruleId, tariffType);
	}

	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.externalImpl.IQueryUserServiceExternal#queryChangedUserInfo(com.ycsoft.business.commons.pojo.BusiParameter, java.lang.String)
	 */
	public List<ChangedUser> queryChangedUserInfo(BusiParameter p,String beginDate,String endDate,
			String countyId) throws Exception {
		QueryUserService queryUserService = (QueryUserService) getBean(
				QueryUserService.class, p);
		return queryUserService.queryChangedUserInfo(beginDate,endDate,countyId);
	}

	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.externalImpl.IQueryUserServiceExternal#queryUserBill(com.ycsoft.business.commons.pojo.BusiParameter, java.lang.String, java.lang.Integer, java.lang.Integer)
	 */
	public List<UserBillDto> queryUserBill(BusiParameter p, String deviceId,
			Integer returnTvRecordCount, Integer returnVodRecordCount)
			throws Exception {
		// TODO Auto-generated method stub
		QueryUserService queryUserService = (QueryUserService) getBean(
				QueryUserService.class, p);
		return queryUserService.queryUserBill(deviceId,returnTvRecordCount,returnVodRecordCount);
	}
	
	public List<CProdDto> queryProdByCustId(BusiParameter p, String custId) throws Exception{
		QueryUserService queryUserService = (QueryUserService) getBean(
				QueryUserService.class, p);
		return queryUserService.queryProdByCustId(custId);
	}
	
	public List<CProdDto> queryProdBalanceByCustId(BusiParameter p, String custId) throws Exception{
		QueryUserService queryUserService = (QueryUserService) getBean(
				QueryUserService.class, p);
		return queryUserService.queryProdBalanceByCustId(custId);
	}
	
	public Pager<UserDto> queryUserInfoToCallCenter(BusiParameter p, Map<String ,Object> params, 
			Integer start, Integer limit) throws Exception {
		QueryUserService queryUserService = (QueryUserService) getBean(
				QueryUserService.class, p);
		return queryUserService.queryUserInfoToCallCenter(params, start, limit);
	}
	
	public List<UserProdDto> queryUserProdToCallCenter(BusiParameter p, Map<String,Object> params) throws Exception {
		QueryUserService queryUserService = (QueryUserService) getBean(
				QueryUserService.class, p);
		return queryUserService.queryUserProdToCallCenter(params);
	}
	
	public List<UserProdDto> queryUserProdHisToCallCenter(BusiParameter p, Map<String,Object> params) throws Exception {
		QueryUserService queryUserService = (QueryUserService) getBean(
				QueryUserService.class, p);
		return queryUserService.queryUserProdHisToCallCenter(params);
	}

}
