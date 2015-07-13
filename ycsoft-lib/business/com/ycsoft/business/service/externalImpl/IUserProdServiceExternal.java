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

/**
 * @author liujiaqi
 * 
 */
public interface IUserProdServiceExternal {
	public void changeTariff(BusiParameter p, String prodSn,
			String newTariffId, String effDate, String expDate)
			throws Exception;

	public void resetUserProdRes(BusiParameter p) throws Exception;

	public void saveEditCustPkg(BusiParameter p,String prodSn)throws Exception;
	
	public void saveTerminate(BusiParameter p, String[] prodSn,
			String banlanceDealType, String transAcctId, String transAcctItemId)
			throws Exception;

	public void saveOrder(BusiParameter p, String prodId, String tariffId,
			String feeDate, List<UserProdRscDto> dynamicRscList, String expDate)
			throws Exception;
	
	/**
	 * 电视营业厅订购
	 * @param p
	 * @param prodId
	 * @param tariffId
	 * @param feeDate
	 * @param dynamicRscList
	 * @param expDate
	 * @throws Exception
	 */
	public void saveTVOrder(BusiParameter p, String prodId, String tariffId,
			String feeDate, int fee)
			throws Exception;

	public CProd queryOrderdProdByUserId(BusiParameter p, String userId,
			String prodId) throws Exception;

	public List<ProdDictDto> queryCanOrderProd(BusiParameter p,
			String[] userIds, String userType, String servType)
			throws Exception;
	
	/**
	 * 呼叫中心 查询用户可订购产品
	 * @param userIds
	 * @param userType
	 * @param servType
	 * @return
	 * @throws Exception
	 */
	public List<PProdDto> queryCanOrderProdToCallCenter(BusiParameter p, String[] userIds, String userType,String servType) throws Exception;

	public void saveEditProd(BusiParameter p, String prodSn,
			List<CProdPropChange> propChangeList) throws Exception;

	public List<ProdResDto> queryProdRes(BusiParameter p, String prodId) throws Exception;

	public void changeExpDate(BusiParameter p, String prodSn, String expDate)
			throws Exception;

	public List<PProdDto> queryProdByCounty(BusiParameter p, String prodId,
			String countyId) throws Exception;
	
	public void pauseProd(BusiParameter p,Integer doneCode, String prodSn, String userId) throws Exception;
	

}
