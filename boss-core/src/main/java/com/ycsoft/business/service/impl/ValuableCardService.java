/**
 * 
 */
package com.ycsoft.business.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ycsoft.beans.config.TBusiFee;
import com.ycsoft.beans.core.bill.BillDto;
import com.ycsoft.beans.core.cust.CCust;
import com.ycsoft.beans.core.fee.CFee;
import com.ycsoft.beans.core.prod.CProd;
import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.beans.core.valuable.CValuableCard;
import com.ycsoft.beans.core.valuable.CValuableCardFee;
import com.ycsoft.beans.prod.PProd;
import com.ycsoft.beans.system.SCounty;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.business.component.config.BusiConfigComponent;
import com.ycsoft.business.component.core.PrintComponent;
import com.ycsoft.business.component.system.IndexComponent;
import com.ycsoft.business.dto.core.acct.AcctitemDto;
import com.ycsoft.business.dto.core.acct.PayDto;
import com.ycsoft.business.dto.core.fee.CFeePayDto;
import com.ycsoft.business.dto.core.prod.ProdTariffDto;
import com.ycsoft.business.dto.device.ValuableCardDto;
import com.ycsoft.business.service.IValuableCardService;
import com.ycsoft.commons.constants.BusiCodeConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ServicesException;
import com.ycsoft.commons.helper.DateHelper;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;


/**
 * @author eaglesnail
 *
 */
@Service
public class ValuableCardService extends BaseBusiService implements IValuableCardService{
	private BusiConfigComponent busiConfigComponent;
	private IndexComponent indexComponent;
	private PrintComponent printComponent;
	/**
	 * 根据客户密码验证客户
	 * @param custId
	 * @param password
	 * @return
	 */
	public CCust validCustByPassword(String custId,String password) throws Exception{
		return custComponent.validCustByPassword(custId, password);
	}

	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.IValuableCardService#queryBillByCustId(java.lang.String, int)
	 */
	public List<BillDto> queryBillByCustId(String custId, String month) throws Exception {
		return billComponent.queryBillByCustId(custId, month);
	}

	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.IValuableCardService#queryUserByCard(java.lang.String)
	 */
	public CUser queryUserByCard(String cardNo) throws Exception {
		List<CUser> userList = userComponent.queryUserByDevice(SystemConstants.DEVICE_TYPE_CARD, cardNo);
		if(userList != null && userList.size() > 0){
			return userList.get(0);
		}else{
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.IValuableCardService#savePayAcct(java.lang.String, java.lang.String, int)
	 */
	public List<PayDto> savePayAcct(String acctId, String acctItemId, int fee)
			throws Exception {
		PayDto pay = new PayDto();
		pay.setAcct_id(acctId);
		pay.setAcctitem_id(acctItemId);
		pay.setFee(fee);
		
		CProd prod = userProdComponent.queryByAcctItem(acctId, acctItemId);
		if(prod != null){
			pay.setProd_sn(prod.getProd_sn());
			pay.setTariff_id(prod.getTariff_id());
			pay.setInvalid_date(DateHelper.dateToStr(prod.getInvalid_date()));
			pay.setUser_id(prod.getUser_id());
		}
		
		List<PayDto> payList = new ArrayList<PayDto>();
		payList.add(pay);
		return payList;
	}

	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.IValuableCardService#queryAcctItemByUserId(java.lang.String)
	 */
	public List<AcctitemDto> queryAcctItemByUserId(String userId)
			throws Exception {
		return acctComponent.queryAcctItemByUserId(userId);
	}

	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.IValuableCardService#queryAllDtvProds(java.lang.String, java.lang.String)
	 */
	public List<PProd> queryAllDtvProds(String servId,String areaId, String countyId)
			throws Exception {
		return userProdComponent.queryAllDtvProds(servId,areaId, countyId);
	}

	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.IValuableCardService#queryAllCounty()
	 */
	public List<SCounty> queryAllCounty() throws JDBCException {
		return busiConfigComponent.queryAllCounty();
	}

	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.IValuableCardService#queryProdTariffByProdId(java.lang.String, java.lang.String, java.lang.String)
	 */
	public List<ProdTariffDto>  queryProdTariffByProdId(String prod_id) throws Exception {
		return prodComponent.queryTariffByProd(prod_id);
	}

	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.IValuableCardService#queryOptrByLoginName(java.lang.String, java.lang.String)
	 */
	public SOptr queryOptrByLoginName(String loginName, String pwd) throws Exception {
		return indexComponent.checkOptrExists(loginName, pwd);
	}

	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.IValuableCardService#saveCvaluableCardFee(com.ycsoft.beans.core.valuable.CValuableCardFee)
	 */
	public void saveCvaluableCardFee(CValuableCardFee valuableCardFee) throws JDBCException {
		deviceComponent.saveValuableCardFee(valuableCardFee);
	}

	/**
	 * @param busiConfigComponent the busiConfigComponent to set
	 */
	public void setBusiConfigComponent(BusiConfigComponent busiConfigComponent) {
		this.busiConfigComponent = busiConfigComponent;
	}

	/**
	 * @param indexComponent the indexComponent to set
	 */
	public void setIndexComponent(IndexComponent indexComponent) {
		this.indexComponent = indexComponent;
	}
	
	public Pager<ValuableCardDto> queryValuableAllCard(Integer start, Integer limit, String query,String queryItem) throws Exception {
		return deviceComponent.queryValuableAllCard(start,limit,query,queryItem);
	}
	
	public String saveValuableCard(Integer amount,String records, SOptr optr) throws Exception {
		Integer doneCode = doneCodeComponent.gDoneCode();
		getBusiParam().setBusiCode(BusiCodeConstants.VALUABLE_CARD_ADD);
		List<TBusiFee> list= feeComponent.queryBusiFeeByType(SystemConstants.FEE_TYPE_VALUABLE);
		if(list.size()>1||list.size()<1){
			throw new ServicesException("该费用类型不能用于多个费用名称，请联系管理员");
		}
		
		CFee fee = feeComponent.saveFeeUnitpre(SystemConstants.PAY_TYPE_CASH,
				list.get(0).getFee_id(), SystemConstants.FEE_TYPE_VALUABLE,
				amount, doneCode, doneCode, getBusiParam().getBusiCode(),
				SystemConstants.BOOLEAN_FALSE,null);
		String[] feeSns = {fee.getFee_sn()};
		printComponent.saveDoc(feeComponent.queryAutoMergeFees(feeSns), null,
				doneCode, getBusiParam().getBusiCode());
		deviceComponent.saveValuableCard(doneCode,records,fee.getFee_sn(),optr);
		CFeePayDto pay = getBusiParam().getPay();
//		if(pay == null){
//			CFeePayDto dto = new CFeePayDto();
//			dto.setAcct_date(DateHelper.strToDate(newAcctDate));
//			dto.setPay_type(SystemConstants.PAY_TYPE_CASH);
//			getBusiParam().setPay(dto);
//		}
		
		saveAllPublic(doneCode,getBusiParam());
		return fee.getFee_sn();
	}
	
	public String removeValuableCard(String[] valuableId, SOptr optr) throws Exception {
		Integer doneCode = doneCodeComponent.gDoneCode();
		getBusiParam().setBusiCode(BusiCodeConstants.VALUABLE_CARD_REMOVE);
		//查询要退的充值卡信息
		List<ValuableCardDto> list =deviceComponent.queryValuableCardById(valuableId,optr.getCounty_id());
		Integer amount = 0;
		Integer done_code = 0;
		for(ValuableCardDto dto:list){
			amount += dto.getAmount();
			if(done_code ==0){
				done_code = dto.getDone_code();
			}else if(!done_code.equals(dto.getDone_code())){
				throw new ServicesException("退卡不能是多个流水!");
			}
			
		}
		
		List<TBusiFee> listFee= feeComponent.queryBusiFeeByType(SystemConstants.FEE_TYPE_VALUABLE);
		CFee newfee = feeComponent.saveFeeUnitpre(
				SystemConstants.PAY_TYPE_CASH, listFee.get(0).getFee_id(),
				SystemConstants.FEE_TYPE_VALUABLE, -amount, doneCode,
				done_code, getBusiParam().getBusiCode(),
				SystemConstants.BOOLEAN_FALSE,null);
		String[] feeSns = {newfee.getFee_sn()};
		printComponent.saveDoc(feeComponent.queryAutoMergeFees(feeSns), null,doneCode, getBusiParam().getBusiCode());
		deviceComponent.removeValuableCard(doneCode,list);
		
		saveAllPublic(doneCode,getBusiParam());
		
		return newfee.getFee_sn();
	}
	
	
	public CValuableCard queryValuableCard(String deviceCode) throws Exception {
		return deviceComponent.queryValuableCardById(deviceCode);
	}
	
	public void editValuableCard(String doneCode,String custName) throws Exception {
		deviceComponent.editValuableCard(doneCode,custName);
	}
	
	
	
	public void setPrintComponent(PrintComponent printComponent) {
		this.printComponent = printComponent;
	}
	
	
}
