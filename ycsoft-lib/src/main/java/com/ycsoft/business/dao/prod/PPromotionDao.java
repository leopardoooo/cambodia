/**
 * PPromotionDao.java	2010/07/22
 */

package com.ycsoft.business.dao.prod;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TPublicAcctitem;
import com.ycsoft.beans.prod.PPromotion;
import com.ycsoft.beans.prod.PPromotionAcct;
import com.ycsoft.beans.prod.PPromotionCard;
import com.ycsoft.beans.prod.PPromotionCounty;
import com.ycsoft.beans.prod.PPromotionFee;
import com.ycsoft.beans.prod.PPromotionGift;
import com.ycsoft.business.dto.core.prod.PromotionDto;
import com.ycsoft.commons.constants.SequenceConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.daos.abstracts.BaseEntityDao;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.sysmanager.dto.tree.TreeDto;


/**
 * PPromotionDao -> P_PROMOTION table's operator
 */
@Component
public class PPromotionDao extends BaseEntityDao<PPromotion> {

	/**
	 *
	 */
	private static final long serialVersionUID = 904897447514197370L;

	/**
	 * default empty constructor
	 */
	public PPromotionDao() {}
	
	/**
	 * 获取促销的基本信息,包括主题的名称和描述.
	 * @param promotionId
	 * @return
	 * @throws Exception
	 */
	public PromotionDto queryPromotionSimpleInfoByKey(String promotionId) throws Exception{
		String sql = "select them.theme_name,them.promotion_desc,t.* from p_promotion t,p_promotion_theme them where them.theme_id = t.theme_id and t.promotion_id = ? ";
		return this.createQuery(PromotionDto.class, sql, promotionId).first();
	}

	public PromotionDto findByKey(String promotionId) throws Exception{
		PromotionDto dto = new PromotionDto();
		PPromotion promotion= super.findByKey(promotionId);
		BeanUtils.copyProperties(promotion,dto );
		String sql = "select p.*,t.tariff_name,a.acctitem_name from p_promotion_acct p ,p_prod_tariff t, "
			+ " vew_acctitem a where p.promotion_id=? and p.tariff_id=t.tariff_id(+) "
			+ " and p.acctitem_id=a.acctitem_id"
			+ " union all "
			+ " select t.*,'宽带自动匹配' tariff_name,'宽带自动匹配' acctitem_name"
			+ " from p_promotion_acct t where t.promotion_id=? and t.acctitem_id='BAND'";
		dto.setAcctList(this.createQuery(PPromotionAcct.class, sql, promotionId, promotionId).list());

		sql = "select * from p_promotion_fee where promotion_id=?";
		dto.setFeeList(this.createQuery(PPromotionFee.class, sql, promotionId).list());

		sql = "select * from p_promotion_card where promotion_id=?";
		dto.setCardList(this.createQuery(PPromotionCard.class, sql, promotionId).list());

		sql = "select * from p_promotion_gift where promotion_id=?";
		dto.setGiftList(this.createQuery(PPromotionGift.class, sql, promotionId).list());
		return dto;
	}

	public List<PromotionDto> queryManualPromotion(String userId,String countyId) throws Exception{
		String sql = "select p.*,t.promotion_desc,r.rule_str from p_promotion p,p_promotion_theme t,t_rule_define r,p_promotion_county pc" +
				" where p.promotion_id=pc.promotion_id and pc.county_id=? and r.rule_id(+)=p.rule_id " +
				" and p.eff_date<sysdate and (p.exp_date is null or p.exp_date>sysdate) and p.auto_exec = ? and p.theme_id = t.theme_id " +
				" and p.theme_id not in (select p.theme_id from p_promotion p,c_promotion c where c.user_id = ? and p.promotion_id=c.promotion_id " +
				" and c.create_time >= sysdate-p.days and c.create_time<trunc(sysdate)+1" +
				" group by theme_id,p.times having count(1)>=p.times)";

		return createQuery(PromotionDto.class,sql,countyId,
				 SystemConstants.BOOLEAN_FALSE,userId).list();

	}
	/**
	 * 查询促销详细信息
	 * @param prom
	 * @return
	 * @throws Exception
	 */
	public void findByKey(PromotionDto prom) throws Exception{
		String promotionId = prom.getPromotion_id();
		String sql = "select p.*,t.tariff_name,a.acctitem_name from p_promotion_acct p ,p_prod_tariff t, "
			+ " vew_acctitem a where p.promotion_id=? and p.tariff_id=t.tariff_id(+) "
			+ " and p.acctitem_id=a.acctitem_id"
			+ " union all "
			+ " select t.*,'宽带自动匹配' tariff_name,'宽带自动匹配' acctitem_name"
			+ " from p_promotion_acct t where t.promotion_id=? and t.acctitem_id='BAND'";
		prom.setAcctList(this.createQuery(PPromotionAcct.class, sql, promotionId, promotionId).list());

		sql = "select p.*, m.model_name device_model_name,t.fee_name,t.fee_type from p_promotion_fee p ,t_busi_fee t, "
			+ " vew_device_model m "
			+ " where promotion_id=? and p.fee_id=t.fee_id and p.device_model=m.device_model(+)";
		prom.setFeeList(this.createQuery(PPromotionFee.class, sql, promotionId).list());

		sql = "select * from p_promotion_card where promotion_id=?";
		prom.setCardList(this.createQuery(PPromotionCard.class, sql, promotionId).list());

		sql = "select * from p_promotion_gift where promotion_id=?";
		prom.setGiftList(this.createQuery(PPromotionGift.class, sql, promotionId).list());
		
		sql = "select * from P_PROMOTION_COUNTY where promotion_id=? ";
		prom.setCountys(this.createQuery(PPromotionCounty.class, sql, promotionId).list());
	}

	/**
	 * 根据操作员编号得到所有促销数据
	 * @param areaId
	 * @return
	 * @throws Exception
	 */
	public List<PromotionDto> queryPromotion(String themeId,String dataRight) throws Exception{
		String sql = "select p.*,t.theme_name,r.rule_name from p_promotion p ,p_promotion_theme t, t_rule_define r " +
				" where p.theme_id=? and p.theme_id = t.theme_id and p.rule_id= r.rule_id ";
		//如果不是省公司操作员，只能查看自己的促销配置
		if(!dataRight.equals(SystemConstants.COUNTY_ALL)){
			sql = sql + "  and p.promotion_id in (select pc.promotion_id from p_promotion_county pc where 1=1 and "+dataRight+") ";
		}
		return createQuery(PromotionDto.class,sql,themeId).list();
	}

	/**
	 * 查询所有公用账目
	 * @return
	 * @throws JDBCException
	 */
	public List<TPublicAcctitem> findAllAcctitem() throws JDBCException{
		String sql = "select * from vew_acctitem";
		return createQuery(TPublicAcctitem.class, sql).list();
	}

	/**
	 * 得到序列号
	 * @return
	 * @throws JDBCException
	 */
	public String getPromId() throws JDBCException{
		return this.findSequence(SequenceConstants.SEQ_PROMOTION_SN).toString();
	}

	public List<TreeDto> getPromCountyById(String promotionId) throws JDBCException {
		String sql = " select county_id id from P_PROMOTION_COUNTY where promotion_id = ? ";
		return createQuery(TreeDto.class,sql,promotionId).list();
	}

	public List<TreeDto> getPromThemeCountyById(String ThemeId) throws JDBCException {
		String sql = " select county_id id from P_PROMOTION_THEME_COUNTY where theme_id = ? ";
		return createQuery(TreeDto.class,sql,ThemeId).list();
	}
	
	public List<PPromotionAcct> queryPromotionAcct(String promotionId) throws JDBCException {
		String sql = "select * from p_promotion_acct p where p.promotion_id=?  and p.necessary='T'";
		return createQuery(PPromotionAcct.class, sql, promotionId).list();
	}
	
}
