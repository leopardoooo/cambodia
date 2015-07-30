/**
 * 
 */
package com.ycsoft.business.service.externalImpl;

import java.util.List;
import java.util.Map;

import com.ycsoft.beans.core.acct.CAcctAcctitemInactive;
import com.ycsoft.beans.core.bill.BillDto;
import com.ycsoft.beans.core.common.CDoneCode;
import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.business.commons.pojo.BusiParameter;
import com.ycsoft.business.dto.core.bill.UserBillDto;
import com.ycsoft.business.dto.core.prod.CProdDto;
import com.ycsoft.business.dto.core.prod.PProdDto;
import com.ycsoft.business.dto.core.prod.UserProdDto;
import com.ycsoft.business.dto.core.user.ChangedUser;
import com.ycsoft.business.dto.core.user.UserDto;
import com.ycsoft.daos.core.Pager;

/**
 * @author liujiaqi
 * 
 */
public interface IQueryUserServiceExternal {

	public List<UserDto> queryUser(BusiParameter p, String custId)
			throws Exception;

	public UserDto queryUserByDeviceId(BusiParameter p, String deviceId)
			throws Exception;

	public CUser queryUserById(BusiParameter p, String userId)
			throws Exception;

	public List<CUser> queryUserByCustId(BusiParameter p, String custId)
			throws Exception;

	public List<CProdDto> querProdByUserId(BusiParameter p, String userId)
			throws Exception;

	public List<CDoneCode> queryUserDoneCode(BusiParameter p, String userId)
			throws Exception;

	public List<PProdDto> queryProdByCountyId(BusiParameter p, String countyId,
			String prodStatus, String tariffStatus, String ruleId,
			String tariffType) throws Exception;

	public List<ChangedUser> queryChangedUserInfo(BusiParameter p,String beginDate, String endDate,
			String countyId)throws Exception;

	public List<UserBillDto> queryUserBill(BusiParameter p, String deviceId,
			Integer returnTvRecordCount, Integer returnVodRecordCount) throws Exception;
	
	public List<CProdDto> queryProdByCustId(BusiParameter p, String custId) throws Exception;
	
	public List<CProdDto> queryProdBalanceByCustId(BusiParameter p, String custId) throws Exception;
	
	public Pager<UserDto> queryUserInfoToCallCenter(BusiParameter p, Map<String ,Object> params,
			Integer start, Integer limit) throws Exception;
	
	public List<UserProdDto> queryUserProdToCallCenter(BusiParameter p, Map<String,Object> params) throws Exception;
	public List<UserProdDto> queryUserProdHisToCallCenter(BusiParameter p, Map<String,Object> params) throws Exception;

}
