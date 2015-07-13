/**
 *
 */
package com.ycsoft.business.dto.core.prod;

import java.util.List;

import com.ycsoft.beans.prod.PPromotion;
import com.ycsoft.beans.prod.PPromotionAcct;
import com.ycsoft.beans.prod.PPromotionCard;
import com.ycsoft.beans.prod.PPromotionCounty;
import com.ycsoft.beans.prod.PPromotionFee;
import com.ycsoft.beans.prod.PPromotionGift;
import com.ycsoft.business.dto.core.fee.FeeDto;

/**
 * @author YC-SOFT
 *
 */
public class PromotionDto extends PPromotion {

	/**
	 *
	 */
	private static final long serialVersionUID = -2387730970261749570L;
	private String promotion_desc;
	private List<PPromotionAcct> acctList ;
	private List<PPromotionFee> feeList ;
	private List<FeeDto> userFeeList;
	private List<PPromotionCard> cardList ;
	private List<PPromotionGift> giftList ;
	private List<PPromotionCounty> countys;
	private String theme_name;
	private String rule_name;
	private String rule_str;



	/**
	 * @return the theme_name
	 */
	public String getTheme_name() {
		return theme_name;
	}
	/**
	 * @param theme_name the theme_name to set
	 */
	public void setTheme_name(String theme_name) {
		this.theme_name = theme_name;
	}
	/**
	 * @return the rule_name
	 */
	public String getRule_name() {
		return rule_name;
	}
	/**
	 * @param rule_name the rule_name to set
	 */
	public void setRule_name(String rule_name) {
		this.rule_name = rule_name;
	}
	public List<PPromotionAcct> getAcctList() {
		return acctList;
	}
	public void setAcctList(List<PPromotionAcct> acctList) {
		this.acctList = acctList;
	}
	public List<PPromotionFee> getFeeList() {
		return feeList;
	}
	public void setFeeList(List<PPromotionFee> feeList) {
		this.feeList = feeList;
	}
	public List<PPromotionCard> getCardList() {
		return cardList;
	}
	public void setCardList(List<PPromotionCard> cardList) {
		this.cardList = cardList;
	}
	public List<PPromotionGift> getGiftList() {
		return giftList;
	}
	public void setGiftList(List<PPromotionGift> giftList) {
		this.giftList = giftList;
	}
	public List<FeeDto> getUserFeeList() {
		return userFeeList;
	}
	public void setUserFeeList(List<FeeDto> userFeeList) {
		this.userFeeList = userFeeList;
	}
	public String getPromotion_desc() {
		return promotion_desc;
	}
	public void setPromotion_desc(String promotion_desc) {
		this.promotion_desc = promotion_desc;
	}
	/**
	 * @return the rule_str
	 */
	public String getRule_str() {
		return rule_str;
	}
	/**
	 * @param rule_str the rule_str to set
	 */
	public void setRule_str(String rule_str) {
		this.rule_str = rule_str;
	}
	public List<PPromotionCounty> getCountys() {
		return countys;
	}
	public void setCountys(List<PPromotionCounty> countys) {
		this.countys = countys;
	}
}
