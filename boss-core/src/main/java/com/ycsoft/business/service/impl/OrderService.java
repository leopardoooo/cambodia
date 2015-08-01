package com.ycsoft.business.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.ycsoft.beans.config.TRuleDefine;
import com.ycsoft.beans.core.cust.CCust;
import com.ycsoft.beans.core.prod.CProdOrder;
import com.ycsoft.beans.core.prod.CProdOrderDto;
import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.beans.prod.PPackageProd;
import com.ycsoft.beans.prod.PProd;
import com.ycsoft.beans.prod.PProdTariffDisct;
import com.ycsoft.business.component.config.ExpressionUtil;
import com.ycsoft.business.component.core.OrderComponent;
import com.ycsoft.business.component.core.UserComponent;
import com.ycsoft.business.dao.config.TRuleDefineDao;
import com.ycsoft.business.dao.core.cust.CCustDao;
import com.ycsoft.business.dao.core.prod.CProdOrderDao;
import com.ycsoft.business.dao.core.user.CUserDao;
import com.ycsoft.business.dao.prod.PPackageProdDao;
import com.ycsoft.business.dao.prod.PProdDao;
import com.ycsoft.business.dao.prod.PProdTariffDao;
import com.ycsoft.business.dao.prod.PProdTariffDisctDao;
import com.ycsoft.business.dto.core.prod.OrderProd;
import com.ycsoft.business.dto.core.prod.OrderProdPanel;
import com.ycsoft.business.dto.core.prod.PackageGroupPanel;
import com.ycsoft.business.dto.core.prod.PackageGroupUser;
import com.ycsoft.business.dto.core.prod.ProdTariffDto;
import com.ycsoft.business.service.IOrderService;
import com.ycsoft.commons.constants.BusiCodeConstants;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ComponentException;
import com.ycsoft.commons.exception.ServicesException;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.commons.helper.DateHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.core.JDBCException;
@Service
public class OrderService extends BaseBusiService implements IOrderService{
	private PProdDao pProdDao;
	private PProdTariffDao pProdTariffDao;
	private PProdTariffDisctDao pProdTariffDisctDao;
	private PPackageProdDao pPackageProdDao;
	private UserComponent userComponent;
	private CProdOrderDao cProdOrderDao;
	private OrderComponent orderComponent;
	private CCustDao cCustDao;
	private CUserDao cUserDao;
	private ExpressionUtil expressionUtil;
	private TRuleDefineDao tRuleDefineDao;

	@Override
	public OrderProdPanel queryOrderableProd(String busiCode,String custId,String userId, String filterOrderSn)
			throws Exception {
		OrderProdPanel panel =new OrderProdPanel();
		CCust cust = cCustDao.findByKey(custId);
		List<CProdOrderDto> orderList = cProdOrderDao.queryCustEffOrderDto(custId);
		
		if (busiCode.equals(BusiCodeConstants.PROD_SINGLE_ORDER)){
			queryUserOrderableProd(cust,userId,panel,orderList);
		} else if (busiCode.equals(BusiCodeConstants.PROD_ORDER)){
			queryCustOrderablePkg(cust,panel,orderList);
		} else if (busiCode.equals(BusiCodeConstants.PROD_CONTINUE)){
			queryOrderableGoon(cust,filterOrderSn,panel,orderList);
		} else if (busiCode.equals(BusiCodeConstants.PROD_UPGRADE)){
			CProdOrder order = cProdOrderDao.findByKey(filterOrderSn);
			if (order == null)
				return panel;
			Map<String,Integer> prodBandWidthMap = pProdDao.queryProdBandWidth();
			PProd prod= pProdDao.findByKey(order.getProd_id());
			if (prod.getProd_type().equals(SystemConstants.PROD_TYPE_BASE) 
					&& prod.getServ_id().equals(SystemConstants.USER_TYPE_BAND)){
				//升级宽带产品
				queryUserOrderableProd(cust,order.getUser_id(),panel,orderList);
				//过滤掉带宽小于等于当前套餐的产品
				for (Iterator<PProd> it = panel.getProdList().iterator();it.hasNext();){
					PProd selectedProd = it.next();
					if (prodBandWidthMap.get(selectedProd.getProd_id())==null ||
							prodBandWidthMap.get(selectedProd.getProd_id())<= prodBandWidthMap.get(prod.getProd_id())){
						it.remove();
						panel.getTariffMap().remove(selectedProd.getProd_id());
						panel.getLastOrderMap().remove(prod.getProd_id());
					}
				}
			} else if (prod.getProd_type().equals(SystemConstants.PROD_TYPE_CUSTPKG)
					&& prodBandWidthMap.get(prod.getProd_id()) != null){
				//含宽带的普通套餐
				queryCustOrderablePkg(cust,panel,orderList);
				//过滤掉带宽小于等于当前套餐的产品
				for (Iterator<PProd> it = panel.getProdList().iterator();it.hasNext();){
					PProd selectedProd = it.next();
					if (prod.getProd_type().equals(SystemConstants.PROD_TYPE_SPKG) ||
							prodBandWidthMap.get(selectedProd.getProd_id())==null ||
							prodBandWidthMap.get(selectedProd.getProd_id())<= prodBandWidthMap.get(prod.getProd_id())){
						it.remove();
						panel.getTariffMap().remove(selectedProd.getProd_id());
						panel.getLastOrderMap().remove(prod.getProd_id());
					}
				}
			} else if (prod.getProd_type().equals(SystemConstants.PROD_TYPE_SPKG)){
				//协议套餐
				queryCustOrderablePkg(cust,panel,orderList);
				//过滤掉普通套餐
				for (Iterator<PProd> it = panel.getProdList().iterator();it.hasNext();){
					PProd selectedProd = it.next();
					if (prod.getProd_type().equals(SystemConstants.PROD_TYPE_CUSTPKG)){
						it.remove();
						panel.getTariffMap().remove(selectedProd.getProd_id());
						panel.getLastOrderMap().remove(prod.getProd_id());
					}
				}
			} else {
				throw new ServicesException("该产品不能升级");
			}
		} 
		
		
		return panel;
	}

	//查找用户能够订购的单产品
	private void queryUserOrderableProd(CCust cust,String userId,OrderProdPanel panel,List<CProdOrderDto> orderList) throws Exception {
		CUser user = userComponent.queryUserById(userId);
		if (user == null)
			return;
		panel.setUserDesc(getUserDesc(user));
		List<PProd> prodList = pProdDao.queryCanOrderUserProd(user.getUser_type(), user.getCounty_id(),
				user.getCounty_id(), SystemConstants.DEFAULT_DATA_RIGHT);
		for (PProd prod:prodList){
			List<PProdTariffDisct> tariffList = this.queryTariffList(cust,user, prod);
			if (!CollectionHelper.isEmpty(tariffList)){
				panel.getProdList().add(prod);
				panel.getTariffMap().put(prod.getProd_id(), tariffList);
				CProdOrder order = getUserLastOrder(userId, prod, orderList);
				if (order != null){
					panel.getLastOrderMap().put(prod.getProd_id(), order);
				}
			}
		}

	}
	//查找客户能够订购的套餐
	private void queryCustOrderablePkg(CCust cust,OrderProdPanel panel,List<CProdOrderDto> orderList) throws Exception {
		String custId = cust.getCust_id();
		Map<String,Integer> userCountMap = cUserDao.queryUserCountGroupByType(custId);
		List<PProd> prodList = pProdDao.queryCanOrderPkg(cust.getCounty_id(),  SystemConstants.DEFAULT_DATA_RIGHT);
		for (PProd prod:prodList){
			List<PProdTariffDisct> tariffList = this.queryTariffList(cust,null, prod);
			if (!CollectionHelper.isEmpty(tariffList)){
				boolean flag = true;
				if (prod.getProd_type().equals(SystemConstants.PROD_TYPE_CUSTPKG)){
					//验证客户名下终端是否满足要求
					Map<String,Integer> pkgUserCountMap = pProdDao.queryUserCountGroupByType(prod.getProd_id());
					for (Entry<String,Integer> entry:pkgUserCountMap.entrySet()){
						if (entry.getValue()>(userCountMap.get(entry.getKey())==null?0:userCountMap.get(entry.getKey()))){
							flag = false;
							break;
						}
					}
					
				}
				if (flag){
					panel.getProdList().add(prod);
					panel.getTariffMap().put(prod.getProd_id(), tariffList);
					CProdOrder order = getCustLastOrder(orderList);
					if (order != null){
						panel.getLastOrderMap().put(prod.getProd_id(), order);
					}
				}
			}
		}

	}

	private void queryOrderableGoon(CCust cust,String filterOrderSn,OrderProdPanel panel,List<CProdOrderDto> orderList) throws Exception {
		CProdOrder order = cProdOrderDao.findByKey(filterOrderSn);
		if (order == null)
			return;
		PProd prod= pProdDao.findByKey(order.getProd_id());
		if (prod.getExp_date() != null && prod.getEff_date().before(new Date())){
			throw new ServicesException("产品已失效");
		}
		CUser user = null;
		CProdOrder lastOrder = null;
		if (StringHelper.isNotEmpty(order.getUser_id())){
			user = userComponent.queryUserById(order.getUser_id());
			panel.setUserDesc(getUserDesc(user));
			lastOrder = getUserLastOrder(user.getUser_id(), prod, orderList);
		} else {
			lastOrder = getCustLastOrder(orderList);
		}
		List<PProdTariffDisct> tariffList = this.queryTariffList(cust,user, prod);
		if (!CollectionHelper.isEmpty(tariffList)){
			panel.getProdList().add(prod);
			panel.getTariffMap().put(prod.getProd_id(), tariffList);
			panel.getLastOrderMap().put(prod.getProd_id(), lastOrder);
		}
	}

	private List<PProdTariffDisct> queryTariffList(CCust cust,CUser user, PProd prod) throws Exception {
		List<PProdTariffDisct> tariffList = new ArrayList<>();
		List<ProdTariffDto> ptList = pProdTariffDao.queryProdTariff(prod.getProd_id(), cust.getCounty_id(),
				SystemConstants.DEFAULT_DATA_RIGHT);
		if (prod.getProd_type().equals(SystemConstants.PROD_TYPE_SPKG)){//协议套餐，验证协议号
			for (Iterator<ProdTariffDto> tariffIt = ptList.iterator();tariffIt.hasNext();) {
				ProdTariffDto  tariff = tariffIt.next();
				if (!tariff.getSpkg_sn().equals(cust.getSpkg_sn()))
					tariffIt.remove();
			}
		} else {
			for (Iterator<ProdTariffDto> tariffIt = ptList.iterator();tariffIt.hasNext();) {
				ProdTariffDto  tariff = tariffIt.next();
				if (!checkRule(cust,user, tariff.getBill_rule()))
					tariffIt.remove();
			}
		}
		
		// 如果有适用的资费
		if (CollectionHelper.isNotEmpty(ptList)) {
			ProdTariffDto pt = ptList.get(0);
			PProdTariffDisct tariff = new PProdTariffDisct();
			tariff.setTariff_id(pt.getTariff_id());
			tariff.setBilling_cycle(pt.getBilling_cycle());
			tariff.setDisct_rent(pt.getRent());
			tariff.setDisct_name(pt.getTariff_name());
			tariffList.add(tariff);
			// 查找资费所有的优惠
			List<PProdTariffDisct> disctList = pProdTariffDisctDao.queryDisctByTariffId(pt.getTariff_id(),
					cust.getCounty_id());
			if (CollectionHelper.isNotEmpty(disctList)) {
				for (PProdTariffDisct disct : disctList) {
					boolean flag = true;
					if (StringHelper.isNotEmpty(disct.getRule_id())) {
						if (!checkRule(cust,user, tRuleDefineDao.findByKey(disct.getRule_id()).getRule_str()))
							flag = false;
					}
					if (flag) {
						disct.setDisct_id(disct.getTariff_id() + "-" + disct.getDisct_id());
						tariffList.add(disct);
					}
				}
			}
		}

		return tariffList;
	}
	
	

	@Override
	public List<CProdOrderDto> queryCustEffOrder(String custId) throws Exception {
		return cProdOrderDao.queryCustEffOrderDto(custId);
	}

	private boolean checkRule(CCust cust,CUser user, String ruleId) throws Exception{
		if (StringHelper.isEmpty(ruleId))
			return true;
		TRuleDefine rule = tRuleDefineDao.findByKey(ruleId);
		if (rule == null)
			return true;
		expressionUtil.setCcust(cust);
		expressionUtil.setCuser(user);
		return expressionUtil.parseBoolean(rule.getRule_str());
	}

	private String getUserDesc(CUser user) {
		String desc = user.getUser_type();
		if (SystemConstants.USER_TYPE_BAND.equals(user.getUser_type())
				|| SystemConstants.USER_TYPE_OTT_MOBILE.equals(user.getUser_type())) {
			desc = desc + "-" + user.getLogin_name();
		} else {
			desc = desc + "-" + MemoryDict.getDictName(DictKey.TERMINAL_TYPE, user.getTerminal_type());
			if (StringHelper.isNotEmpty(user.getStb_id())) {
				desc = desc + "-" + user.getStb_id();
			}
		}

		return desc;

	}
	
	private CProdOrder getUserLastOrder(String userId,PProd prod,List<CProdOrderDto> orderList){
		CProdOrder lastOrder = null;
		Date maxExpDate = new Date();
		if (CollectionHelper.isNotEmpty(orderList)){
			for(CProdOrder order:orderList){
				if (order.getExp_date().after(maxExpDate)){
					if (userId.equals(order.getUser_id()) && (order.getProd_id().equals(prod.getProd_id()) || 
							prod.getServ_id().equals(SystemConstants.USER_TYPE_BAND))){
						lastOrder = order;
						maxExpDate = order.getExp_date();
					}
				}
				
			}
		}
		return lastOrder;
	}
	
	private CProdOrder getCustLastOrder(List<CProdOrderDto> orderList){
		CProdOrderDto lastOrder = null;
		Date maxExpDate = new Date();
		if (CollectionHelper.isNotEmpty(orderList)){
			for(CProdOrderDto order:orderList){
				if (!order.getProd_type().equals(SystemConstants.PROD_TYPE_BASE) && order.getExp_date().after(maxExpDate)){
					lastOrder = order;
					maxExpDate = order.getExp_date();
				}
				
			}
		}
		return lastOrder;
	}

	/**
	 * 套餐的用户选择界面加载初始化数据
	 * @throws Exception 
	 */
	@Override
	public PackageGroupPanel queryPackageGroupPanel(String cust_id,
			String prod_id, String last_order_sn) throws Exception {
		PackageGroupPanel panel=new PackageGroupPanel();
		panel.setNeedShow(true);
		//装入用户清单
		//TODO 要装入施工中和正常的状态终端用户
		panel.setUserList(userComponent.queryUserByCustId(cust_id));
		//装入内容配置信息
		fillPackageProdConfig(panel,pPackageProdDao.queryPackProdByProdId(prod_id));
		
		//自动适配选定用户数量
		if(!autoSelectUser(panel)){
			//自动适配失败，则根据内容组根据上期订购记录选定适配用户
			lastOrderSelectUser(prod_id,panel,last_order_sn);
		}
		return panel;
	}
	/**
	 * 补全套餐内容组配置信息
	 * @param panel
	 * @param pakprodList
	 * @throws JDBCException 
	 */
	private void fillPackageProdConfig(PackageGroupPanel panel,List<PPackageProd> pakprodList) throws Exception{
		List<PackageGroupUser> groupList=new ArrayList<PackageGroupUser>();
		panel.setGroupList(groupList);
		for(PPackageProd pakprod: pakprodList){
			PackageGroupUser pgu=new PackageGroupUser(pakprod);
			pgu.setProdList(new ArrayList<PProd>());
			for(String prod_id: pakprod.getProd_list().split(",")){
				if(StringHelper.isNotEmpty(prod_id)){
					PProd prodconfig=pProdDao.findByKey(prod_id);
					if(prodconfig==null){
						throw new ComponentException("套餐配置有错误，请联系管理员!");
					}
					pgu.getProdList().add(pProdDao.findByKey(prod_id));
				}
			}
		}
	}
	/**
	 * 套餐内容组自动适用终端用户
	 * 如果每个内容组的要求的最大终端用户数量=客户下符合该用户组的用户终端数，则自动匹配成功。
	 * 
	 * @param panel
	 */
	private boolean autoSelectUser(PackageGroupPanel panel){
		//客户用户组临时存放set
		Set<CUser> userSets=new HashSet<CUser>();
		userSets.addAll(panel.getUserList());
		//生成检查Map
		Map<PackageGroupUser,List<String>> checkMap=new HashMap<PackageGroupUser,List<String>>();
		for(PackageGroupUser pgu:  panel.getGroupList()){
			Iterator<CUser> it= userSets.iterator();
			while(it.hasNext()){
				CUser user=it.next();
				if(pgu.getUser_type().equals(user.getUser_type())){
					if(StringHelper.isEmpty(pgu.getTerminal_type())
							||pgu.getTerminal_type().equals(user.getTerminal_type())){
						if(checkMap.get(pgu)==null){
							checkMap.put(pgu, new ArrayList<String>());
						}
						checkMap.get(pgu).add(user.getUser_id());
						it.remove();
					}
				}
			}
		}
		//判断用户数和套餐内容组要求数量是否一致
		for(PackageGroupUser pgu:  panel.getGroupList()){
			//内容组无适用用户，则不能自动适配
			if(checkMap.get(pgu)==null){
				return false;
			}
			//内容组最大用户数量和适配用户数量不一致，则不能自动适配
			if(checkMap.get(pgu).size()!=pgu.getMax_user_cnt()){
				return false;
			}
		}
		//设置自动选中
		for(PackageGroupUser pgu:  panel.getGroupList()){
			pgu.setUserSelectList(checkMap.get(pgu));
		}
		panel.setNeedShow(false);
		return true;
	}
	/**
	 * 根据上次订购记录选中适配用户
	 * @param panel
	 * @param last_order_sn
	 * @throws Exception 
	 */
	private void lastOrderSelectUser(String prod_id,PackageGroupPanel panel,String last_order_sn) throws Exception{
		//上次订购不存在
		if(StringHelper.isEmpty(last_order_sn)) return;
		CProdOrder mainorder=cProdOrderDao.findByKey(last_order_sn);
		//上次订购不存在或产品不一致
		if(mainorder==null||!mainorder.getProd_id().equals(prod_id)){
			return;
		}
		
		//提取原订购记录的套餐组用户选择情况
		Map<String,List<String>> selectUsers=new HashMap<String,List<String>>();
		for(CProdOrder order:cProdOrderDao.queryPakDetailOrder(last_order_sn)){
			if(StringHelper.isNotEmpty(order.getPackage_group_id())
					&&StringHelper.isNotEmpty(order.getUser_id())){
				if(selectUsers.get(order.getPackage_group_id())==null){
					selectUsers.put(order.getPackage_group_id(), new ArrayList<String>());
				}
				selectUsers.get(order.getPackage_group_id()).add(order.getUser_id());
			}
		}
		//状态到新订购的套餐内容组用户选择中
		for(PackageGroupUser pgu: panel.getGroupList()){
			pgu.setUserSelectList(selectUsers.get(pgu.getPackage_group_id()));
		}
	}
	/**
	 * 查询转移支付的被退的订单清单
	 * 
	 * @throws Exception 
	 */
	@Override
	public List<CProdOrder> queryTransferFee(OrderProd orderProd,String busi_code) throws Exception{
		 List<CProdOrder> list= orderComponent.queryTransCancelOrderList(orderProd, busi_code);
		 //计算可退余额
		 for(CProdOrder order:list){
			 order.setActive_fee(orderComponent.getTransCancelFee(orderProd.getEff_date(), order));
		 }
		 return list;
	}

	
	
	/**
	 * 保存订购记录
	 * @return
	 * @throws Exception 
	 */
	@Override
	public String saveOrderProd(OrderProd orderProd,String busi_code) throws Exception{
		
		//参数检查
		CProdOrder lastOrder=checkOrderProdParam(orderProd,busi_code);
				
		Integer doneCode = doneCodeComponent.gDoneCode();
		
		String optr_id=this.getBusiParam().getOptr().getOptr_id();
		CCust cust=cCustDao.findByKey(orderProd.getCust_id());
		
		
		//主订购记录bean生成
		CProdOrder cProdOrder=orderComponent.createCProdOrder(orderProd, doneCode, optr_id, cust.getArea_id(), cust.getCounty_id());
		//产品状态设置
		cProdOrder.setStatus(orderComponent.getNewOrderProdStatus(lastOrder,orderProd));		
		//保存订购记录
		orderComponent.saveCProdOrder(cProdOrder,orderProd,busi_code);
		
		//费用信息
		
		
		//打印信息-发票 业务单
		//业务流水
		return cProdOrder.getOrder_sn();
	}
	
	private  CProdOrder checkOrderProdParam(OrderProd orderProd,String busi_code) throws Exception{
		
		PProd prod=pProdDao.findByKey(orderProd.getProd_id());
		if(!prod.getProd_type().equals(SystemConstants.PROD_TYPE_BASE)){
			if(StringHelper.isNotEmpty(orderProd.getUser_id())){
				throw new ServicesException("订购套餐时，不能填user_id！");
			}
		}
		CProdOrder lastOrder=null;
		//user_id数据校验
		if(StringHelper.isNotEmpty(orderProd.getLast_order_sn())){
			lastOrder=cProdOrderDao.findByKey(orderProd.getLast_order_sn());
			if(StringHelper.isNotEmpty(lastOrder.getUser_id())){
				if(StringHelper.isEmpty(orderProd.getUser_id())){
					orderProd.setUser_id(lastOrder.getUser_id());
				}
				if(!lastOrder.getUser_id().equals(orderProd.getUser_id())){
					throw new ServicesException("上期订购记录和本期用户不一致！");
				}
			}
		}
		//上期订购记录校检
		
		
		
		//开始计费日校检
		if(StringHelper.isNotEmpty(orderProd.getLast_order_sn())
				&&!busi_code.equals(BusiCodeConstants.PROD_UPGRADE)){
			//有上期订购记录且非升级的情况，开始计费日是上期计费日+1天
			if(DateHelper.addDate(lastOrder.getExp_date(), 1).equals(orderProd.getEff_date())){
				throw new ServicesException("开始计费日错误！");
			}
		}else{
			//没有上期订购记录 或者 升级的情况，开始计费日=今天
			if(!DateHelper.isToday(orderProd.getEff_date())){
				throw new ServicesException("开始计费日错误！");
			}
		}
		//结束计费日校检
		if(busi_code.equals(BusiCodeConstants.PROD_UPGRADE)&&orderProd.getOrder_months()==0){
			//升级且0订购月数的情况，结束日期=上期订购结束日
			if(!orderProd.getExp_date().equals(lastOrder.getExp_date())){
				throw new ServicesException("结束计费日错误！");
			}
		}else{
			//其他情况应该是 结束计费日=开始计费日+订购月数。
			if(!DateHelper.getNextMonthByNum(orderProd.getEff_date(), orderProd.getOrder_months())
					.equals(orderProd.getExp_date())){
				throw new ServicesException("结束计费日错误！");
			}
		}
		
		return lastOrder;
	}


	public void setPProdDao(PProdDao pProdDao) {
		this.pProdDao = pProdDao;
	}

	public void setPProdTariffDao(PProdTariffDao pProdTariffDao) {
		this.pProdTariffDao = pProdTariffDao;
	}

	public void setPProdTariffDisctDao(PProdTariffDisctDao pProdTariffDisctDao) {
		this.pProdTariffDisctDao = pProdTariffDisctDao;
	}

	public void setPPackageProdDao(PPackageProdDao pPackageProdDao) {
		this.pPackageProdDao = pPackageProdDao;
	}

	public void setUserComponent(UserComponent userComponent) {
		this.userComponent = userComponent;
	}

	public void setCProdOrderDao(CProdOrderDao cProdOrderDao) {
		this.cProdOrderDao = cProdOrderDao;
	}

	public void setOrderComponent(OrderComponent orderComponent) {
		this.orderComponent = orderComponent;
	}

	public void setCCustDao(CCustDao cCustDao) {
		this.cCustDao = cCustDao;
	}

	public void setCUserDao(CUserDao cUserDao) {
		this.cUserDao = cUserDao;
	}

	public void setExpressionUtil(ExpressionUtil expressionUtil) {
		this.expressionUtil = expressionUtil;
	}

	public void setTRuleDefineDao(TRuleDefineDao tRuleDefineDao) {
		this.tRuleDefineDao = tRuleDefineDao;
	}
	
}
