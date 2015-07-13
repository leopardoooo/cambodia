/**
 *
 */
package com.ycsoft.business.component.core;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.promotion.CPromotion;
import com.ycsoft.beans.core.promotion.CPromotionAcct;
import com.ycsoft.beans.core.promotion.CPromotionCard;
import com.ycsoft.beans.core.promotion.CPromotionChange;
import com.ycsoft.beans.core.promotion.CPromotionGift;
import com.ycsoft.beans.core.promotion.CPromotionHis;
import com.ycsoft.beans.prod.PPromotionAcct;
import com.ycsoft.beans.prod.PPromotionCard;
import com.ycsoft.beans.prod.PPromotionGift;
import com.ycsoft.business.commons.abstracts.BaseBusiComponent;
import com.ycsoft.business.dao.core.promotion.CPromotionAcctDao;
import com.ycsoft.business.dao.core.promotion.CPromotionCardDao;
import com.ycsoft.business.dao.core.promotion.CPromotionDao;
import com.ycsoft.business.dao.core.promotion.CPromotionGiftDao;
import com.ycsoft.business.dao.core.promotion.CPromotionHisDao;
import com.ycsoft.business.dao.prod.PPromotionDao;
import com.ycsoft.business.dto.core.prod.CProdDto;
import com.ycsoft.business.dto.core.prod.CPromotionAcctDto;
import com.ycsoft.business.dto.core.prod.PromotionDto;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ComponentException;
import com.ycsoft.commons.helper.DateHelper;
import com.ycsoft.commons.helper.StringHelper;

/**
 * @author YC-SOFT
 *
 */
@Component
public class UserPromComponent extends BaseBusiComponent {
	private CPromotionDao cPromotionDao;
	private CPromotionAcctDao cPromotionAcctDao;
	private CPromotionCardDao cPromotionCardDao;
	private CPromotionGiftDao cPromotionGiftDao;
	private PPromotionDao pPromotionDao;
	private CPromotionHisDao cPromotionHisDao;
	/**
	 * 查找变更促销异动
	 * @param promotionSn
	 * @return
	 * @throws Exception
	 */
	public  CPromotionChange queryPromotionChange(String promotionSn) throws Exception{
		return cPromotionDao.queryPromotionChangeHis(promotionSn);
	}
	/**
	 * 查询促销历史
	 * @param promotionSn
	 * @return
	 * @throws Exception
	 */
	public CPromotionHis queryPromotionHis(String promotionSn) throws Exception{
		return cPromotionHisDao.queryBySn(promotionSn);
	}
	/**
	 * 保存变更促销异动
	 * @param change
	 * @throws Exception
	 */
	public void savePromotionChange(CPromotionChange change)throws Exception{
		cPromotionDao.savePromotionChange(change);
	}

	public String addPromotion(String changeCreateSn,String custId,String userId,String acctId,String promotionId,Integer doneCode,int times,List<PPromotionAcct> acctList) throws Exception{
		//获取促销信息
		PromotionDto promotion = pPromotionDao.findByKey(promotionId);
		//保存促销信息
		String sn = gPromotionSn();
		CPromotion cp = new CPromotion();
		cp.setPromotion_sn(sn);
		cp.setPromotion_id(promotion.getPromotion_id());
		cp.setCust_id(custId);
		cp.setUser_id(userId);
		cp.setStatus(StatusConstants.ACTIVE);
		cp.setDone_code(doneCode);
		cp.setCreate_time(DateHelper.now());
		cp.setTimes(times/10);
		cp.setPromotion_create_sn(StringHelper.isNotEmpty(changeCreateSn)?changeCreateSn:sn);
		setBaseInfo(cp);
		cPromotionDao.save(cp);

		if (acctList != null){
			for (PPromotionAcct acct:acctList){
				CPromotionAcct cpAcct = new CPromotionAcct();
				cpAcct.setPromotion_sn(sn);
				//宽带自动匹配的情况
				if(acct.getAcctitem_id().equals(SystemConstants.PROD_SERV_ID_BAND)){
					List<CProdDto> list=cProdDao.queryAllProdAcct(custId,this.getOptr().getCounty_id());
					boolean existsBand=false;
					for(CProdDto cprod:list){
						if(cprod.getServ_id().equals(SystemConstants.PROD_SERV_ID_BAND)){
							
							acct.setAcct_id(cprod.getAcct_id());
							acct.setAcctitem_id(cprod.getProd_id());
							acct.setTariff_id(cprod.getTariff_id());
							
							if(cprod.getTariff_rent()>0){
								acct.setPresent_type(SystemConstants.PRESENT_TYPE_FEE);
								acct.setFee( Math.round(acct.getPresent_month()*cprod.getTariff_rent()*1.0F/cprod.getBilling_cycle()));
								acct.setActive_amount(cprod.getTariff_rent());
								acct.setCycle(cprod.getBilling_cycle());
							}
							existsBand=true;
							break;
						}
					}
					if(!existsBand){
						throw new ComponentException("错误:无宽带用户或者宽带用户未订产品.");
					}
				}else{
					acct.setAcct_id(acctId);
				}
				cpAcct.setAcct_id(acct.getAcct_id());
				cpAcct.setAcctitem_id(acct.getAcctitem_id());
				if(acct.getRepetition_times()*10 > times){
					cpAcct.setFee(acct.getFee()*times/10);
					cpAcct.setMonths(acct.getPresent_month()*times/10);
				}else{
					cpAcct.setFee(acct.getFee()*acct.getRepetition_times());
					cpAcct.setMonths(acct.getPresent_month()*acct.getRepetition_times());
				}
				
				setBaseInfo(cpAcct);
				cPromotionAcctDao.save(cpAcct);
			}
		}

		if (promotion.getCardList() != null){
			for (PPromotionCard card:promotion.getCardList()){
				CPromotionCard cpCard = new CPromotionCard();
				cpCard.setPromotion_sn(sn);
				cpCard.setCard_type(card.getCard_type());
				cpCard.setCard_value(card.getCard_value());
				setBaseInfo(cpCard);
				cPromotionCardDao.save(cpCard);
			}
		}

		if (promotion.getGiftList() != null){
			for (PPromotionGift gift:promotion.getGiftList()){
				CPromotionGift cpGift = new CPromotionGift();
				cpGift.setPromotion_sn(sn);
				cpGift.setGift_type(gift.getGift_type());
				cpGift.setAmount(gift.getAmount());
				cpGift.setMoney(gift.getMoney());
				setBaseInfo(cpGift);
				cPromotionGiftDao.save(cpGift);
			}
		}
		return sn;
	}

	public CPromotion queryBySn(String promotionSn) throws Exception{
		return cPromotionDao.findByKey(promotionSn);
	}
	
	public List<CPromotionAcct> queryAcctBySn(String promotionSn) throws Exception{
		return cPromotionAcctDao.queryBySn(promotionSn);
	}
	
	public List<CPromotionAcctDto> queryPromotionProdBySn(String promotionSn,String promotionId) throws Exception {
		return cPromotionAcctDao.queryPromotionProdBySn(promotionSn,promotionId);
	}
	
	/**
	 * @param promotion_sn
	 */
	public void removeBySn(Integer doneCode,String promotionSn) throws Exception {
		cPromotionDao.removePromotionWithHis(promotionSn,doneCode);
		cPromotionAcctDao.removePromotionAcctWithHis(promotionSn,doneCode);
		cPromotionCardDao.removeBySn(promotionSn);
		cPromotionGiftDao.removeBySn(promotionSn);

	}

	public CPromotion queryByDoneCode(Integer doneCode) throws Exception{
		return cPromotionDao.queryByDoneCode(doneCode);
	}

	private String gPromotionSn() throws Exception{
		return cPromotionDao.findSequence().toString();
	}
	public void setCPromotionDao(CPromotionDao promotionDao) {
		cPromotionDao = promotionDao;
	}
	public void setCPromotionAcctDao(CPromotionAcctDao promotionAcctDao) {
		cPromotionAcctDao = promotionAcctDao;
	}
	public void setCPromotionCardDao(CPromotionCardDao promotionCardDao) {
		cPromotionCardDao = promotionCardDao;
	}
	public void setCPromotionGiftDao(CPromotionGiftDao promotionGiftDao) {
		cPromotionGiftDao = promotionGiftDao;
	}

	public void setPPromotionDao(PPromotionDao promotionDao) {
		pPromotionDao = promotionDao;
	}

	public void setCPromotionHisDao(CPromotionHisDao promotionHisDao) {
		cPromotionHisDao = promotionHisDao;
	}

	
	



}
