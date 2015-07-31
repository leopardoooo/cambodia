package com.ycsoft.business.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.ycsoft.beans.core.prod.CProdOrder;
import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.beans.prod.PProd;
import com.ycsoft.beans.prod.PProdTariffDisct;
import com.ycsoft.business.component.config.ExpressionUtil;
import com.ycsoft.business.dao.config.TRuleDefineDao;
import com.ycsoft.business.dao.core.prod.CProdOrderDao;
import com.ycsoft.business.dao.core.user.CUserDao;
import com.ycsoft.business.dao.prod.PProdDao;
import com.ycsoft.business.dao.prod.PProdTariffDao;
import com.ycsoft.business.dao.prod.PProdTariffDisctDao;
import com.ycsoft.business.dto.core.prod.OrderProdPanel;
import com.ycsoft.business.dto.core.prod.ProdTariffDto;
import com.ycsoft.business.service.IOrderService;
import com.ycsoft.commons.constants.BusiCodeConstants;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ServicesException;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.store.MemoryDict;

public class OrderService extends BaseBusiService implements IOrderService {
	private PProdDao pProdDao;
	private PProdTariffDao pProdTariffDao;
	private PProdTariffDisctDao pProdTariffDisctDao;
	private ExpressionUtil expressionUtil;
	private TRuleDefineDao tRuleDefineDao;
	private CProdOrderDao cProdOrderDao;
	private CUserDao cUserDao;

	@Override
	public OrderProdPanel queryOrderableProd(String userId, String filterOrderSn)
			throws Exception {
		OrderProdPanel panel =new OrderProdPanel();
		String custId = getBusiParam().getCust().getCust_id();
		String busiCode = getBusiParam().getBusiCode();
		List<CProdOrder> orderList = cProdOrderDao.queryCustEffOrder(custId);
		
		if (busiCode.equals(BusiCodeConstants.PROD_SINGLE_ORDER)){
			queryUserOrderableProd(userId,panel,orderList);
		} else if (busiCode.equals(BusiCodeConstants.PROD_ORDER)){
			queryCustOrderablePkg(panel,orderList);
		} else if (busiCode.equals(BusiCodeConstants.PROD_CONTINUE)){
			queryOrderableGoon(filterOrderSn,panel,orderList);
		} else if (busiCode.equals(BusiCodeConstants.PROD_UPGRADE)){
			CProdOrder order = cProdOrderDao.findByKey(filterOrderSn);
			if (order == null)
				return panel;
			Map<String,Integer> prodBandWidthMap = pProdDao.queryProdBandWidth();
			PProd prod= pProdDao.findByKey(order.getProd_id());
			if (prod.getProd_type().equals(SystemConstants.PROD_TYPE_BASE) 
					&& prod.getServ_id().equals(SystemConstants.USER_TYPE_BAND)){
				//升级宽带产品
				queryUserOrderableProd(order.getUser_id(),panel,orderList);
				//过滤掉带宽小于等于当前套餐的产品
				for (PProd selectedProd:panel.getProdList()){
					if (prodBandWidthMap.get(selectedProd.getProd_id())==null ||
							prodBandWidthMap.get(selectedProd.getProd_id())<= prodBandWidthMap.get(prod.getProd_id())){
						panel.getProdList().remove(selectedProd);
						panel.getTariffMap().remove(selectedProd.getProd_id());
						panel.getLastOrderMap().remove(prod.getProd_id());
					}
				}
			} else if (prod.getProd_type().equals(SystemConstants.PROD_TYPE_CUSTPKG)
					&& prodBandWidthMap.get(prod.getProd_id()) != null){
				//含宽带的普通套餐
				queryCustOrderablePkg(panel,orderList);
				//过滤掉带宽小于等于当前套餐的产品
				for (PProd selectedProd:panel.getProdList()){
					if (prod.getProd_type().equals(SystemConstants.PROD_TYPE_SPKG) ||
							prodBandWidthMap.get(selectedProd.getProd_id())==null ||
							prodBandWidthMap.get(selectedProd.getProd_id())<= prodBandWidthMap.get(prod.getProd_id())){
						panel.getProdList().remove(selectedProd);
						panel.getTariffMap().remove(selectedProd.getProd_id());
						panel.getLastOrderMap().remove(prod.getProd_id());
					}
				}
			} else if (prod.getProd_type().equals(SystemConstants.PROD_TYPE_SPKG)){
				//协议套餐
				queryCustOrderablePkg(panel,orderList);
				//过滤掉普通套餐
				for (PProd selectedProd:panel.getProdList()){
					if (prod.getProd_type().equals(SystemConstants.PROD_TYPE_CUSTPKG)){
						panel.getProdList().remove(selectedProd);
						panel.getTariffMap().remove(selectedProd.getProd_id());
						panel.getLastOrderMap().remove(prod.getProd_id());
					}
				}
			}
		} 
		
		
		return panel;
	}

	//查找用户能够订购的单产品
	private void queryUserOrderableProd(String userId,OrderProdPanel panel,List<CProdOrder> orderList) throws Exception {
		CUser user = userComponent.queryUserById(userId);
		if (user == null)
			return;
		panel.setUserDesc(getUserDesc(user));
		List<PProd> prodList = pProdDao.queryCanOrderUserProd(user.getUser_type(), user.getCounty_id(),
				user.getCounty_id(), SystemConstants.DEFAULT_DATA_RIGHT);
		for (PProd prod:prodList){
			List<PProdTariffDisct> tariffList = this.queryTariffList(user, prod);
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
	private void queryCustOrderablePkg(OrderProdPanel panel,List<CProdOrder> orderList) throws Exception {
		String custId = getBusiParam().getCust().getCust_id();
		Map<String,Integer> userCountMap = cUserDao.queryUserCountGroupByType(custId);
		List<PProd> prodList = pProdDao.queryCanOrderPkg(getBusiParam().getCust().getCounty_id(),  SystemConstants.DEFAULT_DATA_RIGHT);
		for (PProd prod:prodList){
			List<PProdTariffDisct> tariffList = this.queryTariffList(null, prod);
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

	private void queryOrderableGoon(String filterOrderSn,OrderProdPanel panel,List<CProdOrder> orderList) throws Exception {
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
		List<PProdTariffDisct> tariffList = this.queryTariffList(user, prod);
		if (!CollectionHelper.isEmpty(tariffList)){
			panel.getProdList().add(prod);
			panel.getTariffMap().put(prod.getProd_id(), tariffList);
			panel.getLastOrderMap().put(prod.getProd_id(), lastOrder);
		}
	}

	/**
	 * 升级的产品必须是
	 * 1、协议套餐：查找匹配的其它套餐，不包含当前套餐
	 * 2、宽带产品或者是包含宽带的普通套餐
	 * @param filterOrderSn
	 * @param panel
	 * @param orderList
	 * @throws Exception
	 */
	private void queryOrderableUpgrade(String filterOrderSn,OrderProdPanel panel,List<CProdOrder> orderList) throws Exception {
		
	}

	private List<PProdTariffDisct> queryTariffList(CUser user, PProd prod) throws Exception {
		List<PProdTariffDisct> tariffList = new ArrayList<>();
		List<ProdTariffDto> ptList = pProdTariffDao.queryProdTariff(prod.getProd_id(), user.getCounty_id(),
				SystemConstants.DEFAULT_DATA_RIGHT);
		if (prod.getProd_type().equals(SystemConstants.PROD_TYPE_SPKG)){//协议套餐，验证协议号
			for (ProdTariffDto tariff : ptList) {
				if (!tariff.getSpkg_sn().equals(getBusiParam().getCust().getSpkg_sn()))
					ptList.remove(tariff);
			}
		} else {
			for (ProdTariffDto tariff : ptList) {
				if (!checkRule(user, tariff.getBill_rule_text()))
					ptList.remove(tariff);
			}
		}
		
		// 如果有适用的资费
		if (!CollectionHelper.isNotEmpty(ptList)) {
			ProdTariffDto pt = ptList.get(0);
			PProdTariffDisct tariff = new PProdTariffDisct();
			tariff.setTariff_id(pt.getTariff_id());
			tariff.setBilling_cycle(pt.getBilling_cycle());
			tariff.setDisct_rent(pt.getRent());
			tariff.setDisct_name(pt.getTariff_desc());
			tariffList.add(tariff);
			// 查找资费所有的优惠
			List<PProdTariffDisct> disctList = pProdTariffDisctDao.queryDisctByTariffId(pt.getTariff_id(),
					user.getCounty_id());
			if (!CollectionHelper.isNotEmpty(disctList)) {
				for (PProdTariffDisct disct : disctList) {
					boolean flag = true;
					if (StringHelper.isNotEmpty(disct.getRule_id())) {
						if (!checkRule(user, tRuleDefineDao.findByKey(disct.getRule_id()).getRule_str()))
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

	private boolean checkRule(CUser user, String ruleText) {
		if (StringHelper.isEmpty(ruleText))
			return true;
		expressionUtil.setCcust(getBusiParam().getCust());
		expressionUtil.setCuser(user);
		return expressionUtil.parseBoolean(ruleText);
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
	
	private CProdOrder getUserLastOrder(String userId,PProd prod,List<CProdOrder> orderList){
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
	
	private CProdOrder getCustLastOrder(List<CProdOrder> orderList){
		CProdOrder lastOrder = null;
		Date maxExpDate = new Date();
		if (CollectionHelper.isNotEmpty(orderList)){
			for(CProdOrder order:orderList){
				if (StringHelper.isEmpty(order.getPackage_sn()) && order.getExp_date().after(maxExpDate)){
					lastOrder = order;
					maxExpDate = order.getExp_date();
				}
				
			}
		}
		return lastOrder;
	}

}
