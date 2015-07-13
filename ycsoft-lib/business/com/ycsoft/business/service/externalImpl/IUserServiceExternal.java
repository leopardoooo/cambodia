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

/**
 * @author liujiaqi
 * 
 */
public interface IUserServiceExternal {
	public void saveStop(BusiParameter p, String effectiveDate, int tjFee)
			throws Exception;

	public void saveCancelPromotion(BusiParameter p, String promotionSn)
			throws Exception;

	public void savePromotion(BusiParameter p, int times, String promotionId,
			List<DisctFeeDto> feeList, List<PPromotionAcct> acctList)
			throws Exception;

	public void createUser(BusiParameter p, CUser u) throws Exception;

	public void saveResendCa(BusiParameter p, String[] userIds) throws Exception;

	public void saveOpenTemp(BusiParameter p) throws Exception;
	
	public void saveBatchOpenTemp(BusiParameter p, String[] userIds) throws Exception;
	
	public void editUserStatus(BusiParameter p, List<CUserPropChange> propChangeList)
			throws Exception;

	public void editUser(BusiParameter p, List<CUserPropChange> propChangeList) throws Exception;
}
