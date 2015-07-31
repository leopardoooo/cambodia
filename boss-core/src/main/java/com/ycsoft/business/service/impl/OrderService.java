package com.ycsoft.business.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ycsoft.beans.core.cust.CCust;
import com.ycsoft.beans.core.prod.CProdOrder;
import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.beans.prod.PPackageProd;
import com.ycsoft.beans.prod.PProd;
import com.ycsoft.beans.prod.PProdTariffDisct;
import com.ycsoft.business.component.core.BillingComponent;
import com.ycsoft.business.component.core.OrderComponent;
import com.ycsoft.business.component.core.UserComponent;
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
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ComponentException;
import com.ycsoft.commons.exception.ServicesException;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.core.JDBCException;

public class OrderService extends BaseBusiService implements IOrderService{
	private PProdDao pProdDao;
	private PProdTariffDao pProdTariffDao;
	private PProdTariffDisctDao pProdTariffDisctDao;
	private PPackageProdDao pPackageProdDao;
	private UserComponent userComponent;
	private CProdOrderDao cProdOrderDao;
	private BillingComponent billingComponent;
	private OrderComponent orderComponent;
	private CCustDao cCustDao;
	private CUserDao cUserDao;

	@Override
	public OrderProdPanel queryOrderableProd(String optr_type, String cust_id, String user_id,
			String filter_order_sn) throws Exception {
		return null;
	}
	
	private OrderProdPanel queryUserOrderableProd(String user_Id) throws Exception{
		OrderProdPanel panel = new OrderProdPanel();
		CUser user = userComponent.queryUserById(user_Id);
		if (user == null)
			return null;
		pProdDao.queryCanOrderUserProd(user.getUser_type(), user.getCounty_id(), user.getCounty_id(), SystemConstants.DEFAULT_DATA_RIGHT);
		return panel;
		
	}
	
	private OrderProdPanel queryOrderableGoon(String filter_order_sn) throws Exception{
		return null;
	}
	
	private  Map<PProd, List<PProdTariffDisct>> queryOrderableUp(String filter_order_sn) throws Exception{
		return null;
	}
	
	private List<PProdTariffDisct> queryTariffList(CUser user,PProd prod) throws Exception{
		List<PProdTariffDisct> tariffList = new ArrayList<>();
		List<ProdTariffDto> ptList = pProdTariffDao.queryProdTariff(prod.getProd_id(),  user.getCounty_id(), SystemConstants.DEFAULT_DATA_RIGHT);
		return tariffList;
		
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
			PackageGroupUser pgu=new PackageGroupUser();
			pgu.setPackage_group_id(pakprod.getPackage_group_id());
			pgu.setpPackageProd(pakprod);
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
				if(pgu.getpPackageProd().getUser_type().equals(user.getUser_type())){
					if(StringHelper.isEmpty(pgu.getpPackageProd().getTerminal_type())
							||pgu.getpPackageProd().getTerminal_type().equals(user.getTerminal_type())){
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
			if(checkMap.get(pgu).size()!=pgu.getpPackageProd().getMax_user_cnt()){
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
	public List<CProdOrder> queryTransferFee(OrderProd orderProd,String busi_code) throws Exception{
		List<CProdOrder> orderCancelList=orderComponent.queryTransCancelOrderList(orderProd, busi_code);
		//按天计算每条被取消订单要转移支付的金额。
		calTransferFee(orderProd.getEff_date(),orderCancelList);
		return orderCancelList;
	}
	/**
	 * 转移支付 计算每条被覆盖退订的订购记录的余额(active_fee)
	 * 如果当期已出账(next_bill_date>cancelDate),则当期金额(rent_fee)按实际使用天数重算。
	 * @param orderProd
	 * @param orderCancelList
	 */
	private void calTransferFee(Date cancelDate,List<CProdOrder> orderCancelList){
		for(CProdOrder order:orderCancelList){
			int rent_fee=billingComponent.recalculateRentFee(order, cancelDate);
			if(order.getActive_fee()>rent_fee){
				order.setActive_fee(order.getActive_fee()-rent_fee);
			}else{
				order.setActive_fee(0);
			}
			order.setRent_fee(0);
			order.setEff_date(cancelDate);
		}
	}
	
	
	/**
	 * 保存订购记录
	 * @return
	 * @throws Exception 
	 */
	public String saveOrderProd(OrderProd orderProd,String busi_code) throws Exception{
		
		Integer doneCode = doneCodeComponent.gDoneCode();
		
		String optr_id=this.getBusiParam().getOptr().getOptr_id();
		CCust cust=cCustDao.findByKey(orderProd.getCust_id());
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
		//主订购记录bean生成
		CProdOrder cProdOrder=orderComponent.createCProdOrder(orderProd, doneCode, optr_id, cust.getArea_id(), cust.getCounty_id());
		//产品状态设置
		cProdOrder.setStatus(orderComponent.getNewOrderProdStatus(lastOrder,orderProd));
		//保存订购记录
		String order_sn=orderComponent.saveCProdOrder(cProdOrder,orderProd);
		
		//覆盖退订处理和转移支付异动
		if(orderProd.getTransfer_fee()>0){
			List<CProdOrder> cancelList=orderComponent.queryTransCancelOrderList(orderProd, busi_code);
			if(cancelList!=null&&cancelList.size()>0){
				//TODO 简单覆盖模式（潘计费模型）
			}
		}
		
		//费用信息
		//打印信息-发票 业务单
		//业务流水
		return null;
	}


	public void setpProdDao(PProdDao pProdDao) {
		this.pProdDao = pProdDao;
	}

	public void setpProdTariffDao(PProdTariffDao pProdTariffDao) {
		this.pProdTariffDao = pProdTariffDao;
	}

	public void setpProdTariffDisctDao(PProdTariffDisctDao pProdTariffDisctDao) {
		this.pProdTariffDisctDao = pProdTariffDisctDao;
	}

	public void setpPackageProdDao(PPackageProdDao pPackageProdDao) {
		this.pPackageProdDao = pPackageProdDao;
	}

	public void setUserComponent(UserComponent userComponent) {
		this.userComponent = userComponent;
	}

	public void setcProdOrderDao(CProdOrderDao cProdOrderDao) {
		this.cProdOrderDao = cProdOrderDao;
	}

	public void setBillingComponent(BillingComponent billingComponent) {
		this.billingComponent = billingComponent;
	}

	public void setOrderComponent(OrderComponent orderComponent) {
		this.orderComponent = orderComponent;
	}
	
}
