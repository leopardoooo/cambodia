/**
 * 
 */
package com.ycsoft.business.service.externalImpl;

import java.util.List;

import com.ycsoft.beans.core.bill.BillDto;
import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.beans.core.valuable.CValuableCardFee;
import com.ycsoft.beans.prod.PProd;
import com.ycsoft.beans.system.SCounty;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.business.commons.pojo.BusiParameter;
import com.ycsoft.business.dto.core.acct.AcctitemDto;
import com.ycsoft.business.dto.core.acct.PayDto;
import com.ycsoft.business.dto.core.prod.ProdTariffDto;
import com.ycsoft.business.service.impl.ValuableCardService;

/**
 * @author liujiaqi
 * 
 */
public class ValuableCardServiceExternal extends ParentService implements
		IValuableCardServiceExternal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ycsoft.business.service.externalImpl.IValuableCardServiceExternal#queryAcctItemByUserId(com.ycsoft.business.commons.pojo.BusiParameter,
	 *      java.lang.String)
	 */
	public List<AcctitemDto> queryAcctItemByUserId(BusiParameter p,
			String userId) throws Exception {
		ValuableCardService vluableCardService = (ValuableCardService) getBean(
				ValuableCardService.class, p);
		return vluableCardService.queryAcctItemByUserId(userId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ycsoft.business.service.externalImpl.IValuableCardServiceExternal#queryAllCounty(com.ycsoft.business.commons.pojo.BusiParameter)
	 */
	public List<SCounty> queryAllCounty(BusiParameter p) throws Exception {
		ValuableCardService vluableCardService = (ValuableCardService) getBean(
				ValuableCardService.class, p);
		return vluableCardService.queryAllCounty();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ycsoft.business.service.externalImpl.IValuableCardServiceExternal#queryAllDtvProds(com.ycsoft.business.commons.pojo.BusiParameter,
	 *      java.lang.String, java.lang.String, java.lang.String)
	 */
	public List<PProd> queryAllDtvProds(BusiParameter p, String servId,
			String areaId, String countyId) throws Exception {
		ValuableCardService vluableCardService = (ValuableCardService) getBean(
				ValuableCardService.class, p);
		return vluableCardService.queryAllDtvProds(servId, areaId, countyId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ycsoft.business.service.externalImpl.IValuableCardServiceExternal#queryBillByCustId(com.ycsoft.business.commons.pojo.BusiParameter,
	 *      java.lang.String, java.lang.String)
	 */
	public List<BillDto> queryBillByCustId(BusiParameter p, String custId,
			String month) throws Exception {
		ValuableCardService vluableCardService = (ValuableCardService) getBean(
				ValuableCardService.class, p);
		return vluableCardService.queryBillByCustId(custId, month);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ycsoft.business.service.externalImpl.IValuableCardServiceExternal#queryOptrByLoginName(com.ycsoft.business.commons.pojo.BusiParameter,
	 *      java.lang.String, java.lang.String)
	 */
	public SOptr queryOptrByLoginName(BusiParameter p, String loginName,
			String password) throws Exception {
		ValuableCardService vluableCardService = (ValuableCardService) getBean(
				ValuableCardService.class, p);
		return vluableCardService.queryOptrByLoginName(loginName, password);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ycsoft.business.service.externalImpl.IValuableCardServiceExternal#queryProdTariffByProdId(com.ycsoft.business.commons.pojo.BusiParameter,
	 *      java.lang.String)
	 */
	public List<ProdTariffDto> queryProdTariffByProdId(BusiParameter p,
			String prod_id) throws Exception {
		ValuableCardService vluableCardService = (ValuableCardService) getBean(
				ValuableCardService.class, p);
		return vluableCardService.queryProdTariffByProdId(prod_id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ycsoft.business.service.externalImpl.IValuableCardServiceExternal#queryUserByCard(com.ycsoft.business.commons.pojo.BusiParameter,
	 *      java.lang.String)
	 */
	public CUser queryUserByCard(BusiParameter p, String cardId)
			throws Exception {
		ValuableCardService vluableCardService = (ValuableCardService) getBean(
				ValuableCardService.class, p);
		return vluableCardService.queryUserByCard(cardId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ycsoft.business.service.externalImpl.IValuableCardServiceExternal#saveCvaluableCardFee(com.ycsoft.business.commons.pojo.BusiParameter,
	 *      com.ycsoft.beans.core.valuable.CValuableCardFee)
	 */
	public void saveCvaluableCardFee(BusiParameter p,
			CValuableCardFee valuableCardFee) throws Exception {
		ValuableCardService vluableCardService = (ValuableCardService) getBean(
				ValuableCardService.class, p);
		vluableCardService.saveCvaluableCardFee(valuableCardFee);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ycsoft.business.service.externalImpl.IValuableCardServiceExternal#savePayAcct(com.ycsoft.business.commons.pojo.BusiParameter,
	 *      java.lang.String, java.lang.String, java.lang.Integer)
	 */
	public List<PayDto> savePayAcct(BusiParameter p, String acctId,
			String acctItemId, Integer fee) throws Exception {
		ValuableCardService vluableCardService = (ValuableCardService) getBean(
				ValuableCardService.class, p);
		return vluableCardService.savePayAcct(acctId, acctItemId, fee);
	}

}
