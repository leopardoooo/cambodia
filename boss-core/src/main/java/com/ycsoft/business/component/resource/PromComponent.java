/**
 *
 */
package com.ycsoft.business.component.resource;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.prod.PPromotionAcct;
import com.ycsoft.business.commons.abstracts.BaseBusiComponent;
import com.ycsoft.business.dao.prod.PPromotionDao;
import com.ycsoft.business.dto.core.prod.PromotionDto;

/**
 * @author YC-SOFT
 *
 */
@Component
public class PromComponent extends BaseBusiComponent {
	private PPromotionDao pPromotionDao;
	/**
	 * 根据促销编号获取促销的详细信息
	 * @param promotionId
	 * @return
	 * @throws Exception
	 */
	public PromotionDto queryById(String promotionId) throws Exception{
		return pPromotionDao.findByKey(promotionId);
	}
	
	/**
	 * 获取促销的基本信息,包括主题的名称和描述.
	 * @param promotionId
	 * @return
	 * @throws Exception
	 */
	public PromotionDto queryPromotionSimpleInfoByKey(String promotionId) throws Exception{
		return pPromotionDao.queryPromotionSimpleInfoByKey(promotionId);
	}
	
	/**
	 * 查找参加手动的促销
	 * @param promotionDao
	 */
	public List<PromotionDto> queryManualPromotion(String userId) throws Exception{
		return pPromotionDao.queryManualPromotion(userId,getOptr().getCounty_id());

	}
	
	public List<PPromotionAcct> queryPromotionAcct(String promotionId) throws Exception{
		return pPromotionDao.queryPromotionAcct(promotionId);
	}


	public void setPPromotionDao(PPromotionDao promotionDao) {
		pPromotionDao = promotionDao;
	}

}
