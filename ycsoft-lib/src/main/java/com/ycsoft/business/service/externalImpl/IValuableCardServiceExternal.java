/**
 * 
 */
package com.ycsoft.business.service.externalImpl;

import java.util.List;

import com.ycsoft.beans.core.acct.CAcctAcctitemInactive;
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

/**
 * @author liujiaqi
 * 
 */
public interface IValuableCardServiceExternal {

	void saveCvaluableCardFee(BusiParameter p, CValuableCardFee valuableCardFee)
			throws Exception;

	List<PayDto> savePayAcct(BusiParameter p, String acctId, String acctItemId,
			Integer fee) throws Exception;

	List<BillDto> queryBillByCustId(BusiParameter p, String custId, String month)
			throws Exception;

	List<AcctitemDto> queryAcctItemByUserId(BusiParameter p, String userId)
			throws Exception;

	CUser queryUserByCard(BusiParameter p, String cardId) throws Exception;

	List<SCounty> queryAllCounty(BusiParameter p) throws Exception;

	List<PProd> queryAllDtvProds(BusiParameter p,String servId,
			String areaId, String countyId) throws Exception;

	List<ProdTariffDto> queryProdTariffByProdId(BusiParameter p, String prod_id)
			throws Exception;

	SOptr queryOptrByLoginName(BusiParameter p, String loginName,
			String password) throws Exception;

}
