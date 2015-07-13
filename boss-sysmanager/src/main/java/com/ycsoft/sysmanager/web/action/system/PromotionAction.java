package com.ycsoft.sysmanager.web.action.system;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.WebUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ycsoft.beans.prod.PPromotion;
import com.ycsoft.beans.prod.PPromotionAcct;
import com.ycsoft.beans.prod.PPromotionCard;
import com.ycsoft.beans.prod.PPromotionCounty;
import com.ycsoft.beans.prod.PPromotionFee;
import com.ycsoft.beans.prod.PPromotionGift;
import com.ycsoft.beans.prod.PPromotionTheme;
import com.ycsoft.beans.prod.PPromotionThemeCounty;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.beans.system.SSysChange;
import com.ycsoft.business.dto.core.fee.FeeDto;
import com.ycsoft.business.dto.core.prod.PromotionDto;
import com.ycsoft.business.dto.core.prod.PromotionThemeDto;
import com.ycsoft.commons.abstracts.BaseAction;
import com.ycsoft.commons.constants.Environment;
import com.ycsoft.commons.constants.SysChangeType;
import com.ycsoft.commons.exception.ActionException;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.commons.helper.JsonHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.tree.TreeBuilder;
import com.ycsoft.commons.tree.TreeNode;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.helper.BeanHelper;
import com.ycsoft.sysmanager.component.config.RuleComponent;
import com.ycsoft.sysmanager.component.prod.ProdComponent;
import com.ycsoft.sysmanager.component.prod.PromotionComponent;
import com.ycsoft.sysmanager.dto.tree.TreeDto;
import com.ycsoft.sysmanager.web.commons.interceptor.WebOptr;

@Controller
public class PromotionAction extends BaseAction {

	/**
	 *
	 */
	private static final long serialVersionUID = -7842039594907909339L;
	private PromotionComponent promotionComponent;
	private RuleComponent ruleComponent;
	private ProdComponent prodComponent;
	
	private PPromotion promotion;
	private String promCountyIds;
	private String promotionId;
	private String feeListStr;
	private String cardListStr;
	private String acctListStr;
	private String giftListStr;
	//搜索关键字
	private String query;
	private String themeId;
	
	private String acctitemId;
	
	private PPromotionTheme promotionTheme;

	
	/**
	 * 根据促销主题适用地区，限定促销适用地区树
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String getCountyTree() throws Exception{
		String[] type = {"COUNTY","PROMOTION"};
		List<TreeDto> countys = prodComponent.getCountyTree(optr,type,query);
		List<TreeNode> prodtree = TreeBuilder.createTreeCheck((List)promotionComponent.getCountyTree(themeId,countys));  
		getRoot().setRecords(prodtree);
		return JSON_RECORDS;
	}
	
	/**
	 * 促销主题适用地区树
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String getThemeCountyTree() throws Exception{
		String[] type = {"COUNTY","PROMOTIONTHEME"};
		List<TreeNode> prodtree = TreeBuilder.createTreeCheck((List)prodComponent.getCountyTree(optr,type,query));  
		getRoot().setRecords(prodtree);
		return JSON_RECORDS;
	}
	/**
	 * 根据操作员地区Id返回促销数据
	 * @param optr
	 * @return
	 * @throws Exception
	 */
	public String queryProms() throws Exception{
		getRoot().setRecords(promotionComponent.queryProm(themeId,optr));
		return JSON_RECORDS;
	}

	public String queryPromThemes() throws Exception{
		getRoot().setRecords(promotionComponent.queryPromThemes(query,optr));
		return JSON_RECORDS;
	}
	/**
	 * 保存一条促销记录
	 * @return
	 * @throws Exception
	 */
	public String savePromotion() throws Exception{
		PromotionThemeDto oldDto = promotionComponent.queryForSysChangeInfo(promotion.getTheme_id(),getOptr());
		promotionComponent.savePromotion(promotion,optr,promCountyIds);
		PromotionThemeDto newDto = promotionComponent.queryForSysChangeInfo(promotion.getTheme_id(),getOptr());
		saveChanges(oldDto,newDto,2,promotion.getPromotion_id());
		return JSON;
	}

	/**
	 * 保存一条促销记录的详细信息
	 * @return
	 * @throws Exception
	 */
	public String savePromDetail() throws Exception{
		List<PPromotionAcct> acctList = null;
		List<PPromotionGift> giftList = null;
		List<PPromotionFee> feeList = null;
		List<PPromotionCard> cardList = null;
		if(StringHelper.isNotEmpty(acctListStr)){
			Type type = new TypeToken<List<PPromotionAcct>>(){}.getType();
			Gson gson = new Gson();
			acctList = gson.fromJson(acctListStr,type);
		}
		if(StringHelper.isNotEmpty(giftListStr)){
			Type type = new TypeToken<List<PPromotionGift>>(){}.getType();
			Gson gson = new Gson();
			giftList = gson.fromJson(giftListStr,type);
		}
		if(StringHelper.isNotEmpty(feeListStr)){
			Type type = new TypeToken<List<PPromotionFee>>(){}.getType();
			Gson gson = new Gson();
			feeList = gson.fromJson(feeListStr,type);
		}
		if(StringHelper.isNotEmpty(cardListStr)){
			Type type = new TypeToken<List<PPromotionCard>>(){}.getType();
			Gson gson = new Gson();
			cardList = gson.fromJson(cardListStr,type);
		}
		
		PromotionThemeDto oldDto = promotionComponent.queryForSysChangeInfoByPromId(promotionId,getOptr());
		promotionComponent.savePromDetail(promotionId, feeList, acctList, cardList, giftList);
		PromotionThemeDto newDto = promotionComponent.queryForSysChangeInfoByPromId(promotionId,getOptr());
		saveChanges(oldDto,newDto,3,promotionId);
		
		return JSON;
	}
	/**
	 * 修改一条促销记录
	 * @param promotion
	 * @throws JDBCException
	 */
	public String editPromotion() throws Exception{
		PromotionThemeDto oldDto = promotionComponent.queryForSysChangeInfoByPromId(promotion.getPromotion_id(),getOptr());
		promotionComponent.editPromotion(promotion,promCountyIds);
		PromotionThemeDto newDto = promotionComponent.queryForSysChangeInfoByPromId(promotion.getPromotion_id(),getOptr());
		saveChanges(oldDto,newDto,2,promotion.getPromotion_id());
		
		return JSON;
	}

	/**
	 * 删除一条促销记录
	 * @return
	 * @throws Exception
	 */
	public String removePromotion() throws Exception{
		//TODO 没要求,这个方法也没有地方调用
//		PromotionThemeDto oldDto = promotionComponent.queryForSysChangeInfoByPromId(promotionId,getOptr());
		promotionComponent.removePromotion(promotionId);
//		PromotionThemeDto newDto = promotionComponent.queryForSysChangeInfoByPromId(promotionId,getOptr());
//		saveChanges(oldDto,newDto,3,promotionId);//TODO 待定
		return JSON;
	}

	/**
	 * 保存促销主题
	 * @return
	 * @throws Exception
	 */
	public String savePromTheme() throws Exception{
		PromotionThemeDto oldDto = promotionComponent.queryForSysChangeInfo(promotionTheme.getTheme_id(),getOptr());
		promotionComponent.savePromTheme(promotionTheme,optr,promCountyIds);
		PromotionThemeDto newDto = promotionComponent.queryForSysChangeInfo(promotionTheme.getTheme_id(),getOptr());
		saveChanges(oldDto,newDto,1,null);
		return JSON;
	}

	/**
	 * 修改促销主题
	 * @return
	 * @throws Exception
	 */
	public String editPromTheme() throws Exception{
		PromotionThemeDto oldDto = promotionComponent.queryForSysChangeInfo(promotionTheme.getTheme_id(),getOptr());
		promotionComponent.editPromTheme(promotionTheme,promCountyIds);
		PromotionThemeDto newDto = promotionComponent.queryForSysChangeInfo(promotionTheme.getTheme_id(),getOptr());
		saveChanges(oldDto,newDto,1,null);
		
		return JSON;
	}

	/**
	 * @param oldDto
	 * @param newDto
	 * @param type 1：主题基本信息和使用地区.2,促销和适用地区
	 * @param promotionId 
	 */
	private void saveChanges(PromotionThemeDto oldDto,PromotionThemeDto newDto, int type, String promotionId) throws ActionException{
		List<SSysChange> changes = new ArrayList<SSysChange>();
		
		PPromotionTheme oldTheme = oldDto.getTheme();
		List<PPromotionThemeCounty> oldThemecountys = oldDto.getCountys();
		PPromotionTheme newTheme = newDto.getTheme();
		List<PPromotionThemeCounty> newThemeCountys = newDto.getCountys();
		List<PromotionDto> oldProms = oldDto.getProms();
		List<PromotionDto> newProms = newDto.getProms();
		//确定list里只有一个
		PromotionDto oldProm = null;
		PPromotion oldPromBean = new PPromotion();
		PPromotion newPromBean = new PPromotion();
		String promName = null;
		if(CollectionHelper.isNotEmpty(oldProms)){
			for(PromotionDto dto:oldProms){
				if(dto.getPromotion_id().equals(promotionId)){
					oldProm = dto;
					promName = oldProm.getPromotion_name();
					BeanUtils.copyProperties(oldProm, oldPromBean);
				}
			}
		}
		PromotionDto newProm = null; 
		if(CollectionHelper.isNotEmpty(newProms)){
			for(PromotionDto dto:newProms){
				if(dto.getPromotion_id().equals(promotionId)){
					newProm = dto;
					promName = newProm.getPromotion_name();
					BeanUtils.copyProperties(newProm, newPromBean);
				}
			}
		}
		
		
		if(type ==2 && StringHelper.isEmpty(promotionId)){
			throw new ActionException("未能正确获取参数 'promotionId'");
		}
		
		if(oldTheme ==null && newTheme ==null){
			throw new ActionException("操作和保存异动信息出错!未能获取促销主题ID导致查询出错!");
		}
		
		String key = oldTheme !=null && StringHelper.isNotEmpty(oldTheme.getTheme_id()) ? oldTheme.getTheme_id():newTheme.getTheme_id();
		String keyDesc = oldTheme !=null &&  StringHelper.isNotEmpty(oldTheme.getTheme_name()) ? oldTheme.getTheme_name():newTheme.getTheme_name();;
		String changeDesc = null;
		Integer doneCode = null;
		Date createTime = new Date();
		String optrId = WebOptr.getOptr().getOptr_id();
		String changeType = SysChangeType.PROMOTION.toString();
	
		try{
			doneCode = promotionComponent.getDoneCOde();
			
			String themeNameWithPromNameAndPromId = keyDesc + "_" + promName + "（" + promotionId + "）";
			switch (type) {
			case 1:// 主题基本信息和适用地区
				
				String themeChange = BeanHelper.beanchange(oldTheme, newTheme);
				changeDesc = "促销主题定义";
				if(StringHelper.isNotEmpty(themeChange)){
					SSysChange basicChange = new SSysChange(changeType, doneCode, key, keyDesc, changeDesc, themeChange, optrId, createTime);
					changes.add(basicChange);
				}
				
				changeDesc = "促销主题适用分公司";
				String countyChange = BeanHelper.listchange(oldThemecountys, newThemeCountys, "county_name");
				if(StringHelper.isNotEmpty(countyChange)){
					SSysChange countysChange = new SSysChange(changeType, doneCode, key, keyDesc, changeDesc, countyChange, optrId, createTime);
					changes.add(countysChange);
				}
				
				break;
			case 2:// 促销基本信息和适用地区
				if(oldProm == null && newProm ==null ){
					throw new ActionException("保存异动信息的时候出错!");
				}
				
				
				String proChange = BeanHelper.beanchange(oldPromBean, newPromBean);
				
				changeDesc = themeNameWithPromNameAndPromId + " 定义";
				if(StringHelper.isNotEmpty(proChange)){
					SSysChange basicChange = new SSysChange(changeType, doneCode, key, keyDesc, changeDesc, proChange, optrId, createTime);
					changes.add(basicChange);
				}
				
				changeDesc = themeNameWithPromNameAndPromId + " 适用分公司";
				List<PPromotionCounty> oldPromCountys = oldProm!=null ? oldProm.getCountys():null;
				List<PPromotionCounty> newPromCountys = newProm!=null ? newProm.getCountys():null;
				String proCountyChange = BeanHelper.listchange(oldPromCountys, newPromCountys, "county_name");
				if(StringHelper.isNotEmpty(proCountyChange)){
					SSysChange countysChange = new SSysChange(changeType, doneCode, key, keyDesc, changeDesc, proCountyChange, optrId, createTime);
					changes.add(countysChange);
				}
				
				break;
			case 3:// 促销具体优惠信息,账目,充值卡,礼品,费用优惠
				
				String [] acctFields = new String []{"acctitem_name","present_type_text","present_month","active_amount","cycle","fee","repetition_times","tariff_name","necessary_text"};
				String [] cardFields = new String []{"card_type","card_value"};
				String [] giftFields = new String []{"gift_type","money","amount"};
				String [] feeFields = new String []{"fee_name","device_model_name","disct_value"};
				
				List<PPromotionAcct> oldacctList = oldProm.getAcctList();//账目优惠
				List<PPromotionCard> oldcardList = oldProm.getCardList();//充值卡优惠
				List<PPromotionGift> oldgiftList = oldProm.getGiftList();//礼品优惠
				List<PPromotionFee> oldfeeList = oldProm.getFeeList();//费用优惠

				List<PPromotionAcct> newacctList = newProm.getAcctList();//账目优惠
				List<PPromotionCard> newcardList = newProm.getCardList();//充值卡优惠
				List<PPromotionGift> newgiftList = newProm.getGiftList();//礼品优惠
				List<PPromotionFee> newfeeList = newProm.getFeeList();//费用优惠
				
				String acctChange = BeanHelper.listchange(oldacctList, newacctList, acctFields);
				String cardChange = BeanHelper.listchange(oldcardList, newcardList, cardFields);
				String giftChange = BeanHelper.listchange(oldgiftList, newgiftList, giftFields);
				String feeChange = BeanHelper.listchange(oldfeeList, newfeeList, feeFields);
				
				if(StringHelper.isNotEmpty(acctChange)){
					changeDesc = themeNameWithPromNameAndPromId + "账目优惠";
					SSysChange change = new SSysChange(changeType, doneCode, key, keyDesc, changeDesc, acctChange, optrId, createTime);
					changes.add(change);
				}
				
				if(StringHelper.isNotEmpty(cardChange)){
					changeDesc = themeNameWithPromNameAndPromId + "充值卡优惠";
					SSysChange change = new SSysChange(changeType, doneCode, key, keyDesc, changeDesc, cardChange, optrId, createTime);
					changes.add(change);
				}
				
				if(StringHelper.isNotEmpty(giftChange)){
					changeDesc = themeNameWithPromNameAndPromId + "礼品优惠";
					SSysChange change = new SSysChange(changeType, doneCode, key, keyDesc, changeDesc, giftChange, optrId, createTime);
					changes.add(change);
				}
				
				if(StringHelper.isNotEmpty(feeChange)){
					changeDesc = themeNameWithPromNameAndPromId + "资费优惠";
					SSysChange change = new SSysChange(changeType, doneCode, key, keyDesc, changeDesc, feeChange, optrId, createTime);
					changes.add(change);
				}
				
				break;

			default:
				break;
			}
			
			
			if(changes.size() >0){
				promotionComponent.getSSysChangeDao().save(changes.toArray(new SSysChange[changes.size()]));
			}
			
//			throw new Exception("测试");
		}catch (Exception e) {
			throw new ActionException(e.getMessage());
		}
	}
	
	/**
	 * 查询规则ID
	 * @return
	 * @throws Exception
	 */
	public String queryRules() throws Exception{
		getRoot().setRecords(ruleComponent.queryPromotionRuleByCountyId(optr.getCounty_id()));
		return JSON_RECORDS;
	}

	/**
	 * 查询所有业务费用
	 * @return
	 * @throws Exception
	 */
	public String queryAllFee() throws Exception{
		getRoot().setRecords(promotionComponent.queryAllFee());
		return JSON_RECORDS;
	}

	/**
	 * 查询所有设备型号
	 * @return
	 * @throws JDBCException
	 */
	public String queryAllModel() throws Exception{
		getRoot().setRecords(promotionComponent.queryAllDeviceMdoel());
		return JSON_RECORDS;
	}

	/**
	 * 查询所有公用账目
	 * @return
	 * @throws JDBCException
	 */
	public String queryAllAcct() throws Exception{
		getRoot().setRecords(promotionComponent.queryAllAcct());
		return JSON_RECORDS;
	}

	/**
	 * 查询所有资费
	 * @return
	 * @throws JDBCException
	 */
	public String queryAllTariff() throws Exception{
		getRoot().setRecords(promotionComponent.queryAllTariff(acctitemId,promotionId));
		return JSON_RECORDS;
	}

	/**
	 * 查询操作员信息
	 * @return
	 * @throws Exception
	 */
	public String queryOptr() throws Exception{
		HttpSession session = getSession();
		getRoot().setSimpleObj(JsonHelper.toObject(session.getAttribute(
				Environment.USER_IN_SESSION_NAME).toString(), SOptr.class));
		return JSON_SIMPLEOBJ;
	}

	public String getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(String promotionId) {
		this.promotionId = promotionId;
	}


	public PPromotion getPromotion() {
		return promotion;
	}

	public void setPromotion(PPromotion promotion) {
		this.promotion = promotion;
	}

	public PromotionComponent getPromotionComponent() {
		return promotionComponent;
	}

	public void setPromotionComponent(PromotionComponent promotionComponent) {
		this.promotionComponent = promotionComponent;
	}

	

	public void setRuleComponent(RuleComponent ruleComponent) {
		this.ruleComponent = ruleComponent;
	}

	public String getFeeListStr() {
		return feeListStr;
	}

	public void setFeeListStr(String feeListStr) {
		this.feeListStr = feeListStr;
	}

	public String getCardListStr() {
		return cardListStr;
	}

	public void setCardListStr(String cardListStr) {
		this.cardListStr = cardListStr;
	}

	public String getAcctListStr() {
		return acctListStr;
	}

	public void setAcctListStr(String acctListStr) {
		this.acctListStr = acctListStr;
	}

	public String getGiftListStr() {
		return giftListStr;
	}

	public void setGiftListStr(String giftListStr) {
		this.giftListStr = giftListStr;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public PPromotionTheme getPromotionTheme() {
		return promotionTheme;
	}

	public void setPromotionTheme(PPromotionTheme promotionTheme) {
		this.promotionTheme = promotionTheme;
	}

	public void setThemeId(String themeId) {
		this.themeId = themeId;
	}

	public void setAcctitemId(String acctitemId) {
		this.acctitemId = acctitemId;
	}
	public void setProdComponent(ProdComponent prodComponent) {
		this.prodComponent = prodComponent;
	}
	public void setPromCountyIds(String promCountyIds) {
		this.promCountyIds = promCountyIds;
	}
}
