package com.ycsoft.sysmanager.component.prod;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TBusiFee;
import com.ycsoft.beans.config.TPublicAcctitem;
import com.ycsoft.beans.device.RStbModel;
import com.ycsoft.beans.prod.PProd;
import com.ycsoft.beans.prod.PProdTariff;
import com.ycsoft.beans.prod.PPromotion;
import com.ycsoft.beans.prod.PPromotionAcct;
import com.ycsoft.beans.prod.PPromotionCard;
import com.ycsoft.beans.prod.PPromotionCounty;
import com.ycsoft.beans.prod.PPromotionFee;
import com.ycsoft.beans.prod.PPromotionGift;
import com.ycsoft.beans.prod.PPromotionTheme;
import com.ycsoft.beans.prod.PPromotionThemeCounty;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.business.dao.config.TBusiFeeDao;
import com.ycsoft.business.dao.config.TPublicAcctitemDao;
import com.ycsoft.business.dao.config.TRuleDefineDao;
import com.ycsoft.business.dao.prod.PProdTariffDao;
import com.ycsoft.business.dao.prod.PPromotionAcctDao;
import com.ycsoft.business.dao.prod.PPromotionCardDao;
import com.ycsoft.business.dao.prod.PPromotionCountyDao;
import com.ycsoft.business.dao.prod.PPromotionDao;
import com.ycsoft.business.dao.prod.PPromotionFeeDao;
import com.ycsoft.business.dao.prod.PPromotionGiftDao;
import com.ycsoft.business.dao.prod.PPromotionThemeCountyDao;
import com.ycsoft.business.dao.prod.PPromotionThemeDao;
import com.ycsoft.business.dao.resource.device.RStbModelDao;
import com.ycsoft.business.dto.core.prod.PromotionDto;
import com.ycsoft.business.dto.core.prod.PromotionThemeDto;
import com.ycsoft.commons.abstracts.BaseComponent;
import com.ycsoft.commons.constants.DataRight;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ComponentException;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.sysmanager.dto.tree.TreeDto;

@Component
public class PromotionComponent extends BaseComponent {
	private PPromotionDao pPromotionDao;
	private TRuleDefineDao tRuleDefineDao;
	private TBusiFeeDao tBusiFeeDao;
	private PProdTariffDao pProdTariffDao;
	private TPublicAcctitemDao tPublicAcctitemDao;
	private PPromotionAcctDao pPromotionAcctDao;
	private PPromotionCardDao pPromotionCardDao;
	private PPromotionGiftDao pPromotionGiftDao;
	private PPromotionFeeDao pPromotionFeeDao;
	private RStbModelDao rStbModelDao;
	private PPromotionThemeDao pPromotionThemeDao;
	private PPromotionCountyDao pPromotionCountyDao;
	private PPromotionThemeCountyDao pPromotionThemeCountyDao;


	/**
	 * 查询所有业务费用
	 * @return
	 * @throws Exception
	 */
	public List<TBusiFee> queryAllFee() throws Exception{
		return tBusiFeeDao.findAll();
	}

	/**
	 * 查询产品资费
	 * @return
	 * @throws JDBCException
	 */
	public List<PProdTariff> queryAllTariff(String acctitemId, String promotionId) throws JDBCException{
		List<PProdTariff> list = pProdTariffDao.findProdTariff(acctitemId,promotionId);
		if(acctitemId.equals("BAND")){
			 PProdTariff tariff=new PProdTariff();
			 tariff.setTariff_id("BAND");
			 tariff.setTariff_name("宽带自动匹配");
			 tariff.setRent(0);
			 list.add(tariff);
		}
		return list;
	}

	/**
	 * 查询所有公用账目
	 * @return
	 * @throws JDBCException
	 */
	public List<TPublicAcctitem> queryAllAcct() throws JDBCException{
		List<TPublicAcctitem> list = pPromotionDao.findAllAcctitem();
		TPublicAcctitem acctitem = new TPublicAcctitem();
		acctitem.setAcctitem_id("BAND");
		acctitem.setAcctitem_name("宽带自动匹配");
		acctitem.setAcctitem_type("PROD");
		list.add(acctitem);
		return list;
	}

	/**
	 * 查询所有促销主题
	 * @return
	 * @throws JDBCException
	 */
	public List<PPromotionTheme> queryPromThemes(String query,SOptr optr) throws Exception {
		String dataRight = SystemConstants.COUNTY_ALL;
		if(!optr.getCounty_id().equals(SystemConstants.COUNTY_ALL)){
			dataRight = this.queryDataRightCon(optr, DataRight.CHANGE_COUNTY.toString());
		}
		return pPromotionThemeDao.queryAll(query,dataRight);
	}

	/**
	 * 根据操作员地区Id返回促销数据
	 * @param optr
	 * @return
	 * @throws Exception
	 */
	public List<PromotionDto> queryProm(String themeId,SOptr optr) throws Exception{
		String dataRight = SystemConstants.COUNTY_ALL;
		if(!optr.getCounty_id().equals(SystemConstants.COUNTY_ALL)){
			dataRight = this.queryDataRightCon(optr, DataRight.CHANGE_COUNTY.toString());
		}
		List<PromotionDto> promList = pPromotionDao.queryPromotion(themeId,dataRight);
		for(PromotionDto prom : promList){
			pPromotionDao.findByKey(prom);
		}
		return promList;
	}

	/**
	 * 根据促销主题适用地区，限定促销适用地区
	 * @param promId	      促销编号
	 * @param countyList  促销适用地区
	 * @return
	 * @throws Exception
	 */
	public List<TreeDto> getCountyTree(String themeId,List<TreeDto> countyList) throws Exception{
		List<PPromotionThemeCounty> themeList = pPromotionThemeCountyDao.queryCountyById(themeId);
		
		for (int j = countyList.size() - 1; j >= 0; j--) {
			boolean ck = false;
			for(PPromotionThemeCounty pt : themeList){
				if(pt.getCounty_id().equals(countyList.get(j).getId())){
					ck = true;
				}
			}
			if (!ck) {
				countyList.remove(j);
			}
		}
		return countyList;
	}
	
	/**
	 * 保存一条促销记录
	 * @param promotion
	 * @param promCountyIds 
	 * @throws JDBCException
	 */
	public void savePromotion(PPromotion promotion,SOptr optr, String promCountyIds) throws JDBCException{
		promotion.setPromotion_id(pPromotionDao.getPromId());
		promotion.setArea_id(optr.getArea_id());
		promotion.setCounty_id(optr.getCounty_id());
		promotion.setCreate_time(new Date());
		promotion.setOptr_id(optr.getOptr_id());
		pPromotionDao.save(promotion);
		
		//保存促销适用地区
		savePromotionCounty(promotion.getPromotion_id(),promCountyIds);
	}

	/**
	 * 修改一条促销记录
	 * @param promotion
	 * @param promCountyIds 
	 * @throws JDBCException
	 */
	public void editPromotion(PPromotion promotion, String promCountyIds) throws JDBCException{
		pPromotionDao.update(promotion);
		
		//保存促销适用地区
		savePromotionCounty(promotion.getPromotion_id(),promCountyIds);
	}
	
	/**
	 * 保存促销适用地区
	 * @param promId
	 * @param promCountyIds
	 * @throws JDBCException 
	 */
	public void savePromotionCounty(String promId,String promCountyIds) throws JDBCException{
		pPromotionCountyDao.deleteById(promId);
		if(StringHelper.isNotEmpty(promCountyIds)){
			String[] promCountys = promCountyIds.split(",");
			for(int i=0;i<promCountys.length;i++){
				PPromotionCounty entity = new PPromotionCounty();
				entity.setPromotion_id(promId);
				entity.setCounty_id(promCountys[i]);
				pPromotionCountyDao.save(entity);
			}
		}
	}

	/**
	 * 删除一条促销记录
	 * @param promotion
	 * @throws JDBCException
	 */
	public void removePromotion(String promotionId) throws JDBCException{
		pPromotionDao.remove(promotionId);
		pPromotionAcctDao.deleteByPromId(promotionId);
		pPromotionCardDao.deleteByPromId(promotionId);
		pPromotionFeeDao.deleteByPromId(promotionId);
		pPromotionGiftDao.deleteByPromId(promotionId);
	}

	/**
	 * 保存一条促销记录的详细信息
	 * @param promotionId
	 * @param feeList
	 * @param acctList
	 * @param cardList
	 * @param giftList
	 * @throws JDBCException
	 */
	public void savePromDetail(String promotionId,List<PPromotionFee> feeList,List<PPromotionAcct> acctList,
						List<PPromotionCard> cardList, List<PPromotionGift> giftList) throws JDBCException{
		//先删除原有记录
		pPromotionAcctDao.deleteByPromId(promotionId);
		pPromotionCardDao.deleteByPromId(promotionId);
		pPromotionFeeDao.deleteByPromId(promotionId);
		pPromotionGiftDao.deleteByPromId(promotionId);

		//保存新的数据
		pPromotionAcctDao.save(acctList.toArray(new PPromotionAcct[acctList.size()]));
		pPromotionCardDao.save(cardList.toArray(new PPromotionCard[cardList.size()]));
		pPromotionFeeDao.save(feeList.toArray(new PPromotionFee[feeList.size()]));
		pPromotionGiftDao.save(giftList.toArray(new PPromotionGift[giftList.size()]));
	}

	
	/**
	 * 保存促销主题地市配置
	 * @param themeId
	 * @param promCountyIds
	 * @throws JDBCException
	 */
	public void savePromotionThemeCounty(String themeId,String promCountyIds) throws Exception{
		if(StringHelper.isEmpty(promCountyIds)){
			throw new ComponentException("请先选择应用地区!");
		}
		
		pPromotionThemeCountyDao.deleteById(themeId);
		if(StringHelper.isNotEmpty(promCountyIds)){
			String[] promCountys = promCountyIds.split(",");
			for(int i=0;i<promCountys.length;i++){
				PPromotionThemeCounty entity = new PPromotionThemeCounty();
				entity.setTheme_id(themeId);
				entity.setCounty_id(promCountys[i]);
				pPromotionThemeCountyDao.save(entity);
			}
		}
	}
	
	public void savePromTheme(PPromotionTheme theme ,SOptr optr,String promCountyIds) throws Exception{
		theme.setTheme_id(pPromotionThemeDao.getPromThemeId());
		theme.setArea_id(optr.getArea_id());
		theme.setCounty_id(optr.getCounty_id());
		theme.setCreate_time(new Date());
		theme.setOptr_id(optr.getOptr_id());
		pPromotionThemeDao.save(theme);
		
		//保存促销主题适用地区
		savePromotionThemeCounty(theme.getTheme_id(),promCountyIds);
		
	}

	public void editPromTheme(PPromotionTheme theme, String promCountyIds) throws Exception{
		pPromotionThemeDao.update(theme);
		
		//保存促销主题适用地区
		savePromotionThemeCounty(theme.getTheme_id(),promCountyIds);
		//促销主题适用地区减少后，删除促销的减少的适用地区
		deletePromCounty(theme.getTheme_id(),promCountyIds);
	}

	public void deletePromCounty(String themeId ,String promCountyIds) throws Exception{
		List<PPromotionCounty> promList = pPromotionThemeCountyDao.findByThemeId(themeId);
		String[] promCountys = promCountyIds.split(",");
		for (int i = promList.size() - 1; i >= 0; i--) {
			boolean ck = false;
			for(String pt : promCountys){
				if(pt.equals(promList.get(i).getCounty_id())){
					ck = true;
				}
			}
			if (ck) {
				promList.remove(i);
			}
		}
		List<Object[]> countyList = new ArrayList<Object[]>();
		List<String> aList = new ArrayList<String>();
		for (int j = 0; j < promList.size(); j++) {
			aList.add(promList.get(j).getPromotion_id());
			aList.add(promList.get(j).getCounty_id());
			countyList.add(aList.toArray(new String[aList.size()]));
			aList.clear();
		}
		
		pPromotionCountyDao.deleteByAll(countyList);
	}
	
	public List<RStbModel> queryAllDeviceMdoel() throws JDBCException {
		return rStbModelDao.queryAllDeviceMdoel();
	}

	public PPromotionDao getPPromotionDao() {
		return pPromotionDao;
	}

	public void setPPromotionDao(PPromotionDao promotionDao) {
		pPromotionDao = promotionDao;
	}

	public TRuleDefineDao getTRuleDefineDao() {
		return tRuleDefineDao;
	}

	public void setTRuleDefineDao(TRuleDefineDao ruleDefineDao) {
		tRuleDefineDao = ruleDefineDao;
	}

	public TBusiFeeDao getTBusiFeeDao() {
		return tBusiFeeDao;
	}

	public void setTBusiFeeDao(TBusiFeeDao busiFeeDao) {
		tBusiFeeDao = busiFeeDao;
	}

	public PProdTariffDao getPProdTariffDao() {
		return pProdTariffDao;
	}

	public void setPProdTariffDao(PProdTariffDao prodTariffDao) {
		pProdTariffDao = prodTariffDao;
	}

	public TPublicAcctitemDao getTPublicAcctitemDao() {
		return tPublicAcctitemDao;
	}

	public void setTPublicAcctitemDao(TPublicAcctitemDao publicAcctitemDao) {
		tPublicAcctitemDao = publicAcctitemDao;
	}

	public PPromotionAcctDao getPPromotionAcctDao() {
		return pPromotionAcctDao;
	}

	public void setPPromotionAcctDao(PPromotionAcctDao promotionAcctDao) {
		pPromotionAcctDao = promotionAcctDao;
	}

	public PPromotionCardDao getPPromotionCardDao() {
		return pPromotionCardDao;
	}

	public void setPPromotionCardDao(PPromotionCardDao promotionCardDao) {
		pPromotionCardDao = promotionCardDao;
	}

	public PPromotionGiftDao getPPromotionGiftDao() {
		return pPromotionGiftDao;
	}

	public void setPPromotionGiftDao(PPromotionGiftDao promotionGiftDao) {
		pPromotionGiftDao = promotionGiftDao;
	}

	public PPromotionFeeDao getPPromotionFeeDao() {
		return pPromotionFeeDao;
	}

	public void setPPromotionFeeDao(PPromotionFeeDao promotionFeeDao) {
		pPromotionFeeDao = promotionFeeDao;
	}

	public void setRStbModelDao(RStbModelDao stbModelDao) {
		rStbModelDao = stbModelDao;
	}

	public PPromotionThemeDao getPPromotionThemeDao() {
		return pPromotionThemeDao;
	}

	public void setPPromotionThemeDao(PPromotionThemeDao promotionThemeDao) {
		pPromotionThemeDao = promotionThemeDao;
	}

	public PPromotionCountyDao getPPromotionCountyDao() {
		return pPromotionCountyDao;
	}

	public void setPPromotionCountyDao(PPromotionCountyDao promotionCountyDao) {
		pPromotionCountyDao = promotionCountyDao;
	}
	public PPromotionThemeCountyDao getPPromotionThemeCountyDao() {
		return pPromotionThemeCountyDao;
	}

	public void setPPromotionThemeCountyDao(PPromotionThemeCountyDao promotionThemeCountyDao) {
		pPromotionThemeCountyDao = promotionThemeCountyDao;
	}

	/**
	 * 为记录异动准备数据.
	 * @param themeId
	 * @return
	 * @throws ComponentException
	 */
	public PromotionThemeDto queryForSysChangeInfo(String themeId,SOptr optr) throws ComponentException{
		PromotionThemeDto dto = new PromotionThemeDto();
		if(StringHelper.isEmpty(themeId)){
			return dto;
		}
		
		try {
			PPromotionTheme theme = pPromotionThemeDao.findByKey(themeId);
			dto.setTheme(theme);
			dto.setCountys(pPromotionThemeCountyDao.queryCountyById(themeId));
			List<PromotionDto> proms = queryProm(themeId, optr);
			dto.setProms(proms);
		} catch (Exception e) {
			throw new ComponentException(e);
		}
		
		
		return dto;
	}
	
	/**
	 * 根据promid查询数据为记录异动准备数据.
	 * @param themeId
	 * @return
	 * @throws ComponentException
	 */
	public PromotionThemeDto queryForSysChangeInfoByPromId(String promId,SOptr optr) throws ComponentException{
		PromotionThemeDto dto = new PromotionThemeDto();
		if(StringHelper.isEmpty(promId)){
			return dto;
		}
		PromotionDto prom = new PromotionDto();
		try {
			prom = this.pPromotionDao.findByKey(promId);
		} catch (Exception e) {
			throw new ComponentException(e);
		}
		return queryForSysChangeInfo(prom.getTheme_id(), optr);
	}
}
