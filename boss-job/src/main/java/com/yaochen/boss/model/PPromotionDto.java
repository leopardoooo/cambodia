package com.yaochen.boss.model;

import java.util.List;

import com.ycsoft.beans.prod.PPromotion;
import com.ycsoft.beans.prod.PPromotionAcct;

public class PPromotionDto extends PPromotion{
	List<PPromotionAcct> acctList ;

	public List<PPromotionAcct> getAcctList() {
		return acctList;
	}

	public void setAcctList(List<PPromotionAcct> acctList) {
		this.acctList = acctList;
	}
	
}
