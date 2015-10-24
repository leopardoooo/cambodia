
package com.ycsoft.business.service.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ycsoft.beans.config.TServerRes;
import com.ycsoft.beans.core.common.CDoneCode;
import com.ycsoft.beans.core.cust.CCust;
import com.ycsoft.beans.core.prod.CProd;
import com.ycsoft.beans.core.prod.CProdInvalidTariff;
import com.ycsoft.beans.core.prod.CProdOrderFee;
import com.ycsoft.beans.core.prod.CProdPropChange;
import com.ycsoft.beans.core.promotion.CPromFee;
import com.ycsoft.beans.core.promotion.CPromProdRefund;
import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.beans.core.user.CUserPropChange;
import com.ycsoft.beans.core.user.CUserStb;
import com.ycsoft.beans.prod.PProd;
import com.ycsoft.beans.prod.PProdTariff;
import com.ycsoft.beans.prod.PPromFee;
import com.ycsoft.beans.prod.PPromFeeUser;
import com.ycsoft.beans.prod.PRes;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.business.commons.abstracts.BaseService;
import com.ycsoft.business.component.config.ExpressionUtil;
import com.ycsoft.business.component.core.CustComponent;
import com.ycsoft.business.component.core.UserComponent;
import com.ycsoft.business.component.core.UserProdComponent;
import com.ycsoft.business.component.core.UserPromComponent;
import com.ycsoft.business.component.resource.ProdComponent;
import com.ycsoft.business.dto.core.bill.UserBillDto;
import com.ycsoft.business.dto.core.prod.CProdBacthDto;
import com.ycsoft.business.dto.core.prod.CProdDto;
import com.ycsoft.business.dto.core.prod.CPromotionAcctDto;
import com.ycsoft.business.dto.core.prod.CPromotionDto;
import com.ycsoft.business.dto.core.prod.PProdDto;
import com.ycsoft.business.dto.core.prod.ProdResDto;
import com.ycsoft.business.dto.core.prod.PromFeeProdDto;
import com.ycsoft.business.dto.core.prod.PromTreeDto;
import com.ycsoft.business.dto.core.prod.UserProdDto;
import com.ycsoft.business.dto.core.user.ChangedUser;
import com.ycsoft.business.dto.core.user.UserDto;
import com.ycsoft.business.service.IQueryUserService;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.commons.tree.TreeNode;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;
import com.ycsoft.sysmanager.dto.prod.ResGroupDto;

/**
 * 查询用户相关信息服务实现类
 * @author sheng
 * May 17, 2010 3:22:29 PM
 */
@Service
public class QueryUserService extends BaseService implements IQueryUserService {

	private UserComponent userComponent;
	private UserProdComponent userProdComponent;
	private ProdComponent prodComponent;
	private UserPromComponent userPromComponent;
	private CustComponent custComponent;
	private ExpressionUtil expressionUtil;
	
	public List<PProdDto> queryProdByCountyId(String countyId, String prodStatus,
			String tariffStatus, String ruleId, String tariffType)
			throws Exception{
		return userProdComponent.queryProdByCountyId(countyId, prodStatus,
				tariffStatus, ruleId, tariffType);
	}
	
	
	/**
	 * 查询用户排斥资源
	 * @param userId
	 * @param custId
	 * @return
	 * @throws Exception
	 */
	public List<PRes> queryRejectRes(String userId,String custId) throws Exception{
		return userProdComponent.queryRejectRes(userId, custId);
	}
	
	/**
	 * 查询用户未排斥资源
	 * @param userId
	 * @param custId
	 * @return
	 * @throws Exception
	 */
	public List<PRes> queryUnRejectRes(String userId,String custId) throws Exception{
		return userProdComponent.queryUnRejectRes(userId, custId);
	}
	
	public ProdResDto queryDynResByProdSn(String prodSn) throws Exception {
		return userProdComponent.queryDynResByProdSn(prodSn);
	}

	public List<UserDto> queryUser(String custId) throws Exception {
		List<UserDto> result = userComponent.queryUser(custId);
		return result;
	}

	public Map<String, List<CProdDto>> queryAllProd(String custId)
			throws Exception {
		return CollectionHelper.converToMap(
				userProdComponent.queryProdByCustId(custId), "user_id");
	}
	
	public List<CProdDto> queryProdByCustId(String custId) throws Exception {
		return userProdComponent.queryProdByCustId(custId);
	}
	
	public List<CProdDto> queryProdBalanceByCustId(String custId) throws Exception {
		return userProdComponent.queryProdBalanceByCustId(custId);
	}
	
	public List<UserProdDto> queryUserProdToCallCenter(Map<String,Object> p) throws Exception {
		return userProdComponent.queryUserProdToCallCenter(p);
	}
	
	public List<UserProdDto> queryUserProdHisToCallCenter(Map<String,Object> p) throws Exception {
		return userProdComponent.queryUserProdHisToCallCenter(p);
	}
	
	public List<CProdBacthDto> queryProdByIds(String[] ids,String type,String prodId) throws Exception {
		return userProdComponent.queryProdByIds(ids,type,prodId); 
	}
	
	public List<CProd> queryBaseProdByIds(String[] ids,String type) throws Exception {
		return userProdComponent.queryBaseProdByIds(ids,type);
	}
	
	public Map<String, List<CProdDto>> queryAllProdHis(String custId)
			throws Exception {
		return CollectionHelper.converToMap(
				userProdComponent.queryProdHisByCustId(custId), "user_id");
	}

	public List<UserDto> queryUserHis(String custId) throws Exception {
		return  userComponent.queryUserHis(custId);
	}

	public List<CProdDto> querProdByUserId(String userId) throws Exception{
		return userProdComponent.queryByUserId(userId);
	}

	public List<CDoneCode> queryUserDoneCode(String userId) throws Exception {
		return doneCodeComponent.queryByUserId(userId);
	}

	public List<CUserPropChange> queryUserPropChange(String userId,String userType) throws Exception {
		return userComponent.queryUserPropChange(userId,userType);
	}
	
	public Pager<CProdOrderFee> queryOrderFeeDetail(String orderSn, Integer start, Integer limit) throws Exception {
		return userComponent.queryOrderFeeDetail(orderSn, start, limit);
	}
	
	public List<CPromotionDto> queryUserPromotion(String userId) throws Exception{
		return userComponent.queryUserPromotion(userId);
	}
	
	public List<CPromotionDto> queryPromotionCanCancel(String userId, String prodId) throws Exception{
		return userComponent.queryPromotionCanCancel(userId,prodId);
	}
	
	public List<CPromotionAcctDto> queryPromotionProdBySn(String promotionSn,String promotionId) throws Exception {
		return userPromComponent.queryPromotionProdBySn(promotionSn,promotionId);
	}


	public Pager<CProdInvalidTariff> queryTariffChange(String prodId,Integer start,Integer limit) throws Exception {
		return userProdComponent.queryTariffChange(prodId,start,limit);
	}


	/**
	 * 查询用户产品资源
	 * @param prodSn
	 * @return
	 * @throws Exception
	 */
	public List<PRes> queryUserProdRes(String prodSn) throws Exception{
		return userProdComponent.queryResByProdSn(prodSn);
	}

	/**
	 * 查询当前产品的状态异动信息
	 * @param prodId
	 * @param countyId
	 * @return
	 * @throws Exception
	 */
	public Pager<CProdPropChange> querProdPropChange(String prodId, Integer start, Integer limit) throws Exception{
		Pager<CProdPropChange> prodPropList = userProdComponent.queryChangeByProd(prodId,start,limit);

		PProdTariff ppt = null;
		String paramName = null;
		for(CProdPropChange prodProp : prodPropList.getRecords()){
			//当变动的字段为 资费ID时，通过ID查询资费名称
			if(prodProp.getColumn_name().toUpperCase().equals("TARIFF_ID")){
				ppt = prodComponent.queryTariffById(prodProp.getOld_value());
				prodProp.setOld_value(ppt.getTariff_name());

				ppt = prodComponent.queryTariffById(prodProp.getNew_value());
				prodProp.setNew_value(ppt.getTariff_name());
			}
			prodProp.setColumn_name_text( MemoryDict.getTransData(prodProp.getColumn_name_text()) );

			paramName=prodProp.getParam_name();
			//若数据字典字段不为空，则查询数据字典值
			if (StringHelper.isNotEmpty(prodProp.getParam_name())){
				prodProp.setOld_value_text(MemoryDict.getDictName( paramName, prodProp.getOld_value()));
				prodProp.setNew_value_text(MemoryDict.getDictName( paramName, prodProp.getNew_value()));
			 }else {
				 prodProp.setOld_value_text( MemoryDict.getTransData( prodProp.getOld_value() ));
				 prodProp.setNew_value_text( MemoryDict.getTransData( prodProp.getNew_value() ));
			 }
		}

		return prodPropList;
	}
	
	public List<CUser> queryUserByCustId(String custId) throws Exception {
		return userComponent.queryUserByCustId(custId);
	}
	
	/**
	 * @param deviceId
	 * @param returnTvRecordCount
	 * @param returnVodRecordCount
	 * @return
	 */
	public List<UserBillDto> queryUserBill(String deviceId,
			Integer returnTvRecordCount, Integer returnVodRecordCount) throws Exception {
		return userComponent.queryUserBill(deviceId,returnTvRecordCount,returnVodRecordCount);
	}

	
	public CUser queryUserById(String userId) throws Exception {
		return userComponent.queryUserById(userId);
	}
	
	public List<PProd> queryAtvProds() throws Exception {
		return userProdComponent.queryAllAtvProds();
	}


	public PProdTariff queryProdTariffById(String tariffId) throws Exception {
		return userProdComponent.queryProdTariffById(tariffId);
	}

	public UserDto queryUserByDeviceId(String deviceId) throws Exception {
		return userComponent.queryUserByDeviceId(deviceId);
	}
	
	/**
	 * @return
	 */
	public List<ChangedUser> queryChangedUserInfo(String beginDate,String endDate,String countyId) throws JDBCException {
		return userComponent.queryChangedUserInfo(beginDate,endDate,countyId);
	}
	
	public List<CUser> queryUserByCustNoAndStatus(String custNo,String userStatus) throws Exception {
		List<CUser>	userList = userComponent.queryUserByCustNo(custNo);
		List<CUser> users = new ArrayList<CUser>();
//		if(userStatus.equals(StatusConstants.ATVCLOSE)){
//			for(CUser user : userList){
//				if(user.getUser_type().equals(SystemConstants.USER_TYPE_ATV)){
//					users.add(user);
//				}
//			}
//		}else{
			users = userList;
//		}
		return users;
	}
	

	/* (non-Javadoc)
	 * @see com.ycsoft.business.service.IQueryUserService#querySelectablePromPay()
	 */
	public List<PPromFee> querySelectablePromPay(String custId,SOptr optr) throws Exception {
		CCust cust = custComponent.queryCustById(custId);
		List<CUser> userList = userComponent.queryUserByCustId(custId);
		Map<String, CUserStb> userStbMap = CollectionHelper.converToMapSingle(userComponent.queryUserStbByCustId(custId), "user_id");
		List<PPromFee> promFeeList = userComponent.querySelectablePromPay();
		String[] promFeeIds = CollectionHelper.converValueToArray(promFeeList, "prom_fee_id");
		Map<String,List<CPromFee>> cPrommap = CollectionHelper.converToMap(userComponent.queryPromFee(promFeeIds,custId,optr.getCounty_id()), "prom_fee_id");
		
		Map<String,List<PPromFeeUser>> groupmap = CollectionHelper.converToMap(userComponent.queryPromFeeUser(promFeeIds), "prom_fee_id");
		expressionUtil.setCcust(cust);
		
		//删除规则和客户终端不一致的套餐
		for(int i=promFeeList.size()-1;i>=0;i--){
			int num =0;
			if(groupmap.get(promFeeList.get(i).getProm_fee_id()) == null){
				promFeeList.remove(i);
				continue;
			}
			for(PPromFeeUser promUser:groupmap.get(promFeeList.get(i).getProm_fee_id()) ){
				for(CUser user : userList){
					expressionUtil.setCuser(user);
					expressionUtil.setCuserStb(userStbMap.get(user.getUser_id()));
					expressionUtil.setOrderprods(userProdComponent.queryByUserId(user.getUser_id()));
					if(expressionUtil.parseBoolean(promUser.getRule_id_text())){
						num++;
						break;
					}
				}
			}
			if(num != groupmap.get(promFeeList.get(i).getProm_fee_id()).size() ){
				promFeeList.remove(i);
				continue;
			}
			promFeeList.get(i).setUse_cnt(cPrommap.get(promFeeList.get(i).getProm_fee_id())==null?0:cPrommap.get(promFeeList.get(i).getProm_fee_id()).size());
			
		}
		
		return promFeeList;
	}
	
	public List<CPromProdRefund> querySelectPromFee(String userId,String prodSn) throws Exception {
		
		List<CPromProdRefund> promList = userComponent.querySelectPromFee(userId,prodSn);
		List<CPromProdRefund> list = new ArrayList<CPromProdRefund>();
		for(CPromProdRefund dto:promList){
			boolean ck = false;
			for (int i=list.size()-1;i>=0;i--){
				CPromProdRefund prom = list.get(i);
				if(prom.getProm_fee_id().equals(dto.getProm_fee_id()) && prom.getCreate_time().compareTo(dto.getCreate_time())>=0 ){
					ck = true;
					break;
				}else if(prom.getProm_fee_id().equals(dto.getProm_fee_id()) && dto.getCreate_time().compareTo(prom.getCreate_time())>=0){
					list.remove(i);
				}
			}
			if(!ck){
				list.add(dto);
			}
		}
		return list;
	}

	
	public PromTreeDto querySelectUserProm(String custId,String promFeeId) throws Exception{
		PromTreeDto resultProds = new PromTreeDto();
		List<PromFeeProdDto> promFeeProds = new ArrayList<PromFeeProdDto>();
		
		CCust cust = custComponent.queryCustById(custId);
		List<CUser> userList = userComponent.queryUserByCustId(custId);
		Map<String, CUserStb> userStbMap = CollectionHelper.converToMapSingle(userComponent.queryUserStbByCustId(custId), "user_id");
		List<PPromFeeUser> promUserList = userComponent.queryPromFeeUser(promFeeId);
		
		List<String> selectUser = new ArrayList<String>();
		
		List<CProdDto> prodList = userProdComponent.queryByCustId(cust.getCust_id());
		Map<String,List<CProdDto>> orderprods = CollectionHelper.converToMap(prodList, "user_id");
		//查出可用的终端
		expressionUtil.setCcust(cust);
		for(PPromFeeUser promUser : promUserList){
			String allowUser = "";
			for(CUser user : userList){
				expressionUtil.setCuser(user);
				expressionUtil.setCuserStb(userStbMap.get(user.getUser_id()));
				expressionUtil.setOrderprods(orderprods.get(user.getUser_id()));
				if(expressionUtil.parseBoolean(promUser.getRule_id_text()) && ( user.getStatus().equals(StatusConstants.ACTIVE)||user.getStatus().equals(StatusConstants.OWELONG) )){
					allowUser += user.getUser_id()+","; 
					if(!selectUser.contains(user.getUser_id())){
						selectUser.add(user.getUser_id());	
						PromFeeProdDto dto = new PromFeeProdDto();
						String userName = "";
						String userNameText = "";
						if(StringHelper.isNotEmpty(user.getCard_id())){
							 userName = user.getUser_type_text()+"-"+ user.getUser_name()+"_"+user.getCard_id();
							 userNameText = userName+"->";
						}else{
							userName = user.getUser_type_text()+"-"+user.getUser_name();
							userNameText = userName+"->";
						}
						dto.setCard_id(user.getCard_id());
						dto.setUser_name(userName);
						dto.setUser_name_text(userNameText);
						dto.setUser_id(user.getUser_id());
						dto.setUser_type(user.getUser_type());
						promFeeProds.add(dto);
					}
				}
				
			}
			allowUser = StringHelper.delEndChar( allowUser ,  1 );
			promUser.setAllow_user(allowUser);
		}
		
		//生成套餐树
		List<PromFeeProdDto> promProdList = userComponent.queryPromFeeProds(promFeeId);
		Map<String,List<PromFeeProdDto>> promProdmap = CollectionHelper.converToMap(promProdList, "user_no");
		
		String[] prods = CollectionHelper.converValueToArray(promProdList, "prod_id");
		List<ResGroupDto> groupList = userComponent.queryGroupByProdIds(prods);
		Map<String,List<ResGroupDto>> groupmap = CollectionHelper.converToMap(groupList, "prod_id");
		
		String[] groups = CollectionHelper.converValueToArray(groupList, "group_id");
		List<PRes> resList = userComponent.queryResByGroupId(groups);
		Map<String,List<PRes>> resmap = CollectionHelper.converToMap(resList, "group_id");
		
		List<TreeNode> target = new LinkedList<TreeNode>(); //规则
		List<TreeNode> targep = null; //产品
		List<TreeNode> targeg = null; //产品动态组
		List<TreeNode> targer = null; //资源
		TreeNode _t = null;  
		TreeNode _p = null;
		TreeNode _g = null;
		TreeNode _r = null;
		for (PPromFeeUser v : promUserList) {
			_t = new TreeNode();
			_t.setId(v.getUser_no()+"_"+v.getProm_fee_id());
			_t.setText( v.getRule_name()+ (v.getUser_fee()==null?"":"_实缴:"+v.getUser_fee()/100)+(v.getProd_count()==null?"":"_可选数:"+v.getProd_count()));
			_t.getOthers().put("user_fee",v.getUser_fee()==null?"":Integer.toString(v.getUser_fee()));
			_t.getOthers().put("type","RULE");
			_t.getOthers().put("allow_user",v.getAllow_user());
			_t.getOthers().put("user_no",v.getUser_no());
			_t.getOthers().put("rule_id",v.getRule_id());
			_t.getOthers().put("prod_count",v.getProd_count()==null?"":Integer.toString(v.getProd_count())); //可选数量
			if(promProdmap.get(v.getUser_no())!=null){
				targep = new LinkedList<TreeNode>();
				for(PromFeeProdDto p : promProdmap.get(v.getUser_no()) ){
					_p = new TreeNode();
					_p.setId(p.getProd_id()+"_"+p.getUser_no());
					_p.setText(p.getProd_name()+ (p.getReal_pay()==null?"":"_实缴:"+p.getReal_pay()));
					_p.getOthers().put("type","PROD");
					_p.getOthers().put("is_base",p.getIs_base());
					_p.getOthers().put("prod_id",p.getProd_id());
					_p.getOthers().put("should_pay",p.getShould_pay()==null?"":Integer.toString(p.getShould_pay()*100));
					_p.getOthers().put("real_pay",p.getReal_pay()==null?"":Integer.toString(p.getReal_pay()*100));
					_p.getOthers().put("tariff_id",p.getTariff_id());
					_p.getOthers().put("months",p.getMonths()==null?"":Integer.toString(p.getMonths()));
					_p.getOthers().put("force_select",p.getForce_select());
					_p.getOthers().put("bind_prod_id",p.getBind_prod_id());
					if(groupmap.get(p.getProd_id())!=null){
						targeg = new LinkedList<TreeNode>();
						for(ResGroupDto r: groupmap.get(p.getProd_id())){
							_g = new TreeNode();
							_g.setId(r.getGroup_id()+"_"+p.getProd_id()+"_"+p.getUser_no());
							_g.setText(r.getGroup_name()+(r.getRes_number()==0?"":"_可选数:"+r.getRes_number()));
							_g.getOthers().put("type","GROUP");
							_g.getOthers().put("group_id",r.getGroup_id());
							_g.getOthers().put("res_number",r.getRes_number()==0?"":Integer.toString(r.getRes_number())); //可选数量
							if(resmap.get(r.getGroup_id())!=null){
								targer = new LinkedList<TreeNode>();
								for(PRes res:resmap.get(r.getGroup_id())){
									_r = new TreeNode();
									_r.setId(res.getRes_id()+"_"+res.getGroup_id()+"_"+p.getUser_no());
									_r.setText(res.getRes_name());
									_r.setChecked(false);
									_r.setLeaf(true);
									_r.getOthers().put("type","RES");
									_r.getOthers().put("res_id",res.getRes_id());
									targer.add(_r);
								}
								_g.setChildren(targer);
							}
							targeg.add(_g);
						}
						_p.setChildren(targeg);
					}else{
						_p.setLeaf(true);
					}
					
					if(p.getForce_select().equals("T")){
//						_p.setLeaf(true);
//						_p.setDisabled(true);
//						_p.setChecked(true);
					}else{
						_p.setChecked(false);
					}
					targep.add(_p);
				}
			}
			_t.setChildren(targep);
//			_t.getOthers().put("type", v.getOptr_id());
//			_t.getOthers().put("countyId", v.getCounty_id());
			target.add(_t);
		}
		if(promFeeProds.size()==0){
			PromFeeProdDto dto = new PromFeeProdDto();
			dto.setUser_name_text("用户条件不符合!");
			promFeeProds.add(dto);
		}
		
		//客户宽带产品与套餐中的宽带产品进行比较，如果套餐中的宽带产品客户未订购，不能进行套餐缴费
		Boolean key = false;
		String bandStr = "";
		List<CProdDto> list = userProdComponent.queryAllProdAcct(custId,cust.getCounty_id());
		Map<String,List<CProdDto>> custBandProdmap = CollectionHelper.converToMap(list, "serv_id");
		List<String> cBandProds = CollectionHelper.converValueToList(custBandProdmap.get("BAND"), "prod_id");
		//宽带自动匹配
		cBandProds.add("BAND");
		Map<String,List<PromFeeProdDto>> pBandProdmap = CollectionHelper.converToMap(promProdList, "serv_id");
		if(pBandProdmap.get("BAND") != null){
			for(PromFeeProdDto dto:pBandProdmap.get("BAND")){
				if(!cBandProds.contains(dto.getProd_id())){
					key = true;
					bandStr=dto.getProd_name();
					break;
				}
			}
		}
		if(key){
			promFeeProds.clear();
			PromFeeProdDto dto = new PromFeeProdDto();
			dto.setUser_name_text("该套餐中的宽带产品:"+bandStr+",客户未订购!");
			promFeeProds.add(dto);
			target = new LinkedList<TreeNode>();
		}
		
		resultProds.setUserList(promFeeProds);
		resultProds.setPromTree(target);
		return resultProds;
	}
	
	public Pager<UserDto> queryUserInfoToCallCenter(Map<String ,Object> params, Integer start, Integer limit) throws Exception {
		return userComponent.queryUserInfoToCallCenter(new Pager<Map<String ,Object>>(params , start, limit));
	}
	
	public List<TServerRes> queryZteBandRes() throws Exception {
		return prodComponent.queryZteBandRes();
	}

	
	public void setUserComponent(UserComponent userComponent) {
		this.userComponent = userComponent;
	}

	public void setUserProdComponent(UserProdComponent userProdComponent) {
		this.userProdComponent = userProdComponent;
	}

	public void setProdComponent(ProdComponent prodComponent) {
		this.prodComponent = prodComponent;
	}


	public void setUserPromComponent(UserPromComponent userPromComponent) {
		this.userPromComponent = userPromComponent;
	}


	public void setCustComponent(CustComponent custComponent) {
		this.custComponent = custComponent;
	}

	/**
	 * @param expressionUtil the expressionUtil to set
	 */
	public void setExpressionUtil(ExpressionUtil expressionUtil) {
		this.expressionUtil = expressionUtil;
	}

}
