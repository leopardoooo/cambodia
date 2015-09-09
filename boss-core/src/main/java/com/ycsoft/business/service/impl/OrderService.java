package com.ycsoft.business.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ycsoft.beans.config.TPayType;
import com.ycsoft.beans.core.acct.CAcct;
import com.ycsoft.beans.core.acct.CAcctAcctitemActive;
import com.ycsoft.beans.core.acct.CAcctAcctitemChange;
import com.ycsoft.beans.core.common.CDoneCodeUnpay;
import com.ycsoft.beans.core.cust.CCust;
import com.ycsoft.beans.core.fee.CFeeAcct;
import com.ycsoft.beans.core.prod.CProdOrder;
import com.ycsoft.beans.core.prod.CProdOrderDto;
import com.ycsoft.beans.core.prod.CProdOrderFee;
import com.ycsoft.beans.core.prod.CProdOrderFollowPay;
import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.beans.prod.PPackageProd;
import com.ycsoft.beans.prod.PProd;
import com.ycsoft.beans.prod.PProdTariff;
import com.ycsoft.beans.prod.PProdTariffDisct;
import com.ycsoft.boss.remoting.ott.OttClient;
import com.ycsoft.boss.remoting.ott.Result;
import com.ycsoft.business.component.core.OrderComponent;
import com.ycsoft.business.dao.config.TPayTypeDao;
import com.ycsoft.business.dao.core.cust.CCustDao;
import com.ycsoft.business.dao.core.prod.CProdOrderDao;
import com.ycsoft.business.dao.core.prod.CProdOrderFeeDao;
import com.ycsoft.business.dao.core.user.CUserDao;
import com.ycsoft.business.dao.prod.PPackageProdDao;
import com.ycsoft.business.dao.prod.PProdDao;
import com.ycsoft.business.dao.prod.PProdTariffDao;
import com.ycsoft.business.dao.prod.PProdTariffDisctDao;
import com.ycsoft.business.dto.core.acct.PayDto;
import com.ycsoft.business.dto.core.fee.BusiFeeDto;
import com.ycsoft.business.dto.core.fee.FeeBusiFormDto;
import com.ycsoft.business.dto.core.prod.OrderProd;
import com.ycsoft.business.dto.core.prod.OrderProdPanel;
import com.ycsoft.business.dto.core.prod.PackageGroupPanel;
import com.ycsoft.business.dto.core.prod.PackageGroupUser;
import com.ycsoft.business.service.IOrderService;
import com.ycsoft.commons.constants.BusiCmdConstants;
import com.ycsoft.commons.constants.BusiCodeConstants;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ComponentException;
import com.ycsoft.commons.exception.ErrorCode;
import com.ycsoft.commons.exception.ServicesException;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.commons.helper.DateHelper;
import com.ycsoft.commons.helper.JsonHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.helper.BeanHelper;
@Service
public class OrderService extends BaseBusiService implements IOrderService{
	@Autowired
	private PProdDao pProdDao;
	@Autowired
	private PProdTariffDao pProdTariffDao;
	@Autowired
	private PProdTariffDisctDao pProdTariffDisctDao;
	@Autowired
	private PPackageProdDao pPackageProdDao;
	@Autowired
	private CProdOrderDao cProdOrderDao;
	@Autowired
	private OrderComponent orderComponent;
	@Autowired
	private CCustDao cCustDao;
	@Autowired
	private CUserDao cUserDao;
	@Autowired
	private CProdOrderFeeDao cProdOrderFeeDao;
	@Autowired
	private TPayTypeDao tPayTypeDao;

	/**
	 * 产品终止退订
	 * 查询退订\销户可退的订单金额
	 * 当天订购的订单退全部金额（可能误操作的情况）
	 * a.普通退订和普通销户：1.包多月产品不可退。
	 *         2.单月基础产品未过协议期，则协议期外可退，退订业务时重发加授权至协议期。
	 *         3.单月基础产品已过协议期(或无协议)，则可退并终止产品
	 *         4.单月非基础产品可退并终止产品
	 * b.高级退订和高级销户可以退钱并终止产品
	 * c.退款金额按剩余使用月整数计算
	 * @return
	 */
	public List<CProdOrderDto> queryCancelFeeByCancelOrder(String busi_code,String cust_id,String order_sn)throws Exception{
		//检查是否未支付项目
		//List<CDoneCodeUnpay> unPays=doneCodeComponent.queryUnPayList(cust_id);
		
		CProdOrderDto cancelOrder=cProdOrderDao.queryCProdOrderDtoByKey(order_sn);
		if(cancelOrder==null||StringHelper.isEmpty(busi_code)){
			throw new ServicesException(ErrorCode.ParamIsNull);
		}
		List<CProdOrderDto> orderList=null;
		if(busi_code.equals(BusiCodeConstants.PROD_TERMINATE)){
			orderList=new ArrayList<>();
			orderList.add(cancelOrder);
		}else{
			orderList=orderComponent.queryOrderByCancelOrder(cancelOrder);
		}
	
		//参数检查		
		for(CProdOrderDto order:orderList){
			//检查能否退订
			this.checkOrderCanCancel(cust_id, busi_code, order);
			//费用计算
			Map<String,Integer> cancelFeeMap=orderComponent.getOrderCancelFee(order,busi_code,DateHelper.today());
			order.setBalance_cfee(cancelFeeMap.get(SystemConstants.ORDER_FEE_TYPE_CFEE));
			order.setBalance_acct(cancelFeeMap.get(SystemConstants.ORDER_FEE_TYPE_ACCT));
			order.setActive_fee(order.getBalance_acct()+order.getBalance_cfee());
		}
		
		return orderList;
	}
	
	
	/** 
	 * 用户销户查询该用户的所有产品信息包含退款金额（高级销户，普通销户）
	 */
	public List<CProdOrderDto> queryLogoffUserProd(String busi_code,String user_id) throws Exception{
		
		List<CProdOrderDto> orderList=orderComponent.queryLogoffProdOrderDtoByUserId(user_id);
		//是否高级权限
		boolean isHigh=orderComponent.isHighCancel(busi_code);
		//费用
		orderComponent.getLogoffOrderFee(orderList, isHigh);
		return orderList;
	}
	

		
	/**
	 * 退订产品
	 * @param 
	 * @throws Exception 
	 */
	public void saveCancelProd(String[] orderSns,Integer cancelFee,Integer refundFee) throws Exception{
		
		String cust_id=this.getBusiParam().getCust().getCust_id();
		doneCodeComponent.lockCust(cust_id);
		
		//退订的费用明细
		List<CProdOrderFee> orderFees=new ArrayList<>();
		//参数检查，返回退订订单详细信息列表
		List<CProdOrderDto> cancelList=checkCancelProdOrderParm(cust_id, orderSns, cancelFee,refundFee,orderFees);
		
		Integer done_code=doneCodeComponent.gDoneCode();
		if(refundFee<0){
			//记录未支付业务
			doneCodeComponent.saveDoneCodeUnPay(cust_id, done_code, this.getOptr().getOptr_id());
			//保存缴费信息
			feeComponent.saveCancelFee(cancelList,orderFees, this.getBusiParam().getCust(), done_code, this.getBusiParam().getBusiCode());
		}
		if(cancelFee-refundFee!=0){
			//余额转回公用账目
			acctComponent.saveCancelFeeToAcct(orderFees, cust_id, done_code, this.getBusiParam().getBusiCode());
		}
		
		//更新订单费用明细的转出信息
		cProdOrderFeeDao.update(orderFees.toArray(new CProdOrderFee[orderFees.size()]));
		
		List<CProdOrder> cancelResultList=new ArrayList<>();
		
		for(CProdOrderDto dto:cancelList){
			//执行退订 返回被退订的用户订单清单
			cancelResultList.addAll(orderComponent.saveCancelProdOrder(dto, done_code));
		}
		
		Map<String,CUser> userMap=new  HashMap<>();
		//整体移动剩下订单的开始和结束计算日期
	    moveOrderByCancelOrder(cancelResultList, userMap, done_code); 
	    //退订直接解除授权，不等支付（因为不能取消）	
	  	pstProdNoPackage(cancelResultList, userMap, done_code);
		
		this.saveAllPublic(done_code, this.getBusiParam());
	}
	
	/**
	 * 整体移动剩下订单的开始和结束计算日期
	 * @param cancelResultList
	 * @param userMap
	 * @param done_code
	 * @throws Exception 
	 */
	private void moveOrderByCancelOrder(List<CProdOrder> cancelResultList,Map<String,CUser> userMap,Integer done_code) throws Exception{
		//key=cust_id+'_'+user_id+"_"
		Map<String,CProdOrder> movePackageMap=new HashMap<>();
		Map<String,CProdOrder> moveBandMap=new HashMap<>();
		Map<String,CProdOrder> moveProdMap=new HashMap<>();
		
		for(CProdOrder order:cancelResultList){
			if(StringHelper.isEmpty(order.getUser_id())){
				//套餐订单
				movePackageMap.put(order.getCust_id(), order);
			}else{
				CUser user=userMap.get(order.getUser_id());
			    if(user==null){
			    	user=cUserDao.findByKey(order.getUser_id());
			    	userMap.put(order.getUser_id(), user);
			    	if(user==null){
			    		throw new ServicesException(ErrorCode.OrderDateException,order.getOrder_sn());
			    	}
			    }
			    if(user.getUser_type().equals(SystemConstants.USER_TYPE_BAND)){
			    	//宽带订单
			    	moveBandMap.put(order.getUser_id(), order);
			    }else{
			    	//非宽带单产品订单
			    	String key=StringHelper.append(order.getUser_id()+"_"+order.getProd_id());
			    	moveProdMap.put(key, order);
			    }
			}
		}
		//套餐订单的接续处理
		for(String cust_id: movePackageMap.keySet()){
			orderComponent.movePackageOrderToFollow(cust_id, done_code);
		}
		//宽带订单的接续处理
		for(String user_id:moveBandMap.keySet()){
			orderComponent.moveBandOrderToFollow(user_id, done_code);
		}
		//非宽带单产品的接续处理
		for(CProdOrder order:moveProdMap.values()){
			orderComponent.moveProdOrderToFollow(order.getUser_id(), order.getProd_id(), done_code);
		}
		
	}
	/**
	 * 退订产品参数检查
	 * @param busi_code
	 * @param cust_id
	 * @param isHigh
	 * @param orderSns
	 * @param cancel_fee
	 * @return
	 * @throws Exception
	 */
	private List<CProdOrderDto> checkCancelProdOrderParm(String cust_id,String[] orderSns
			,Integer cancel_fee,Integer refund_fee,List<CProdOrderFee> orderFees) throws Exception{
		
		if(StringHelper.isEmpty(cust_id)||cancel_fee==null||orderSns==null||orderSns.length==0){
			throw new ServicesException(ErrorCode.ParamIsNull);
		}
		String busi_code=this.getBusiParam().getBusiCode();
		List<CProdOrderDto> cancelList=new ArrayList<>();
		//参数检查
		//退款总额核对
		int fee=0;
		//退现金总金额
		int cfeeTotal=0;
		//相关订单的未支付核对
		Map<String,CProdOrderDto> unPayCheckMap=new HashMap<String,CProdOrderDto>();
		for(String order_sn:orderSns){
			CProdOrderDto order= cProdOrderDao.queryCProdOrderDtoByKey(order_sn);
			if(order==null){
				throw new ServicesException(ErrorCode.OrderTodayHasCancel);
			}
			cancelList.add(order);
			this.checkOrderCanCancel(cust_id, busi_code, order);
			//可退费用计算
			List<CProdOrderFee> cancelFeeList=null;
			if(busi_code.equals(BusiCodeConstants.PROD_SUPER_TERMINATE)){
				//超级退订，退所有钱
				cancelFeeList=orderComponent.getOrderCancelAllFeeDetail(order);
			}else{
				cancelFeeList=orderComponent.getOrderCacelFeeDetail(order,DateHelper.today());
			}
			
			orderFees.addAll(cancelFeeList);
			int outTotalFee=0;
			int balanceCfee=0;
			for(CProdOrderFee orderFee:cancelFeeList){
				outTotalFee=outTotalFee+orderFee.getOutput_fee();
				if(orderFee.getOutput_type().equals(SystemConstants.ORDER_FEE_TYPE_CFEE)){
					if(refund_fee<0){
						balanceCfee=balanceCfee+orderFee.getOutput_fee();
					}else{
						orderFee.setOutput_type(SystemConstants.ORDER_FEE_TYPE_ACCT);
					}
				}
				//后面账户扣款异动要使用
				orderFee.setProd_name(order.getProd_name());
			}
			order.setActive_fee(outTotalFee);
			order.setBalance_cfee(balanceCfee);
			order.setBalance_acct(outTotalFee-balanceCfee);
			
			fee=fee+order.getActive_fee();
			cfeeTotal=cfeeTotal+order.getBalance_cfee();
			
			if(!order.getProd_type().equals(SystemConstants.PROD_TYPE_BASE)){
				unPayCheckMap.put("PACKAGE", order);
			}else if(order.getServ_id().equals(SystemConstants.PROD_SERV_ID_BAND)){
				unPayCheckMap.put("BAND", order);
			}else {
				unPayCheckMap.put("NOBAND", order);
			}
		}
		//金额核对
		if(cancel_fee!=fee*-1){
			throw new ServicesException(ErrorCode.FeeDateException);
		}
		if(refund_fee!=cfeeTotal*-1){
			throw new ServicesException(ErrorCode.FeeDateException);
		}
		//订单相关的所有订单的未支付判断,判断各类订单的相关所有订单的未支付状态
		for(CProdOrderDto checkOrder: unPayCheckMap.values()){
			for(CProdOrderDto unPayOrder: orderComponent.queryOrderByCancelOrder(checkOrder)){
				if(unPayOrder.getIs_pay().equals(SystemConstants.BOOLEAN_FALSE)){
					throw new ServicesException(ErrorCode.NotCancleHasUnPay);
				}
			}
		}
		return cancelList;
	}
	
	/**
	 * 判断订单能否退订
	 * @param cust_id
	 * @param isHigh
	 * @param order
	 * @param prodConfig
	 * @throws Exception
	 */
	private void checkOrderCanCancel(String cust_id,String busi_code,CProdOrderDto order) throws Exception{
		
		if(order==null){
			throw new ServicesException(ErrorCode.OrderNotExists);
		}
		if(!cust_id.equals(order.getCust_id())){
			throw new ServicesException(ErrorCode.CustDataException);
		}
		if(StringHelper.isNotEmpty(order.getPackage_sn())){
			throw new ServicesException(ErrorCode.NotCancelIsPackageDetail);
		}
		if(order.getIs_pay().equals(SystemConstants.BOOLEAN_FALSE)){
			//未支付判断
			throw new ServicesException(ErrorCode.NotCancleHasUnPay);
		}
		//状态判断
		if(!order.getStatus().equals(StatusConstants.ACTIVE)
				&&!order.getStatus().equals(StatusConstants.INSTALL)){
			throw new ServicesException(ErrorCode.NotCancelStatusException);
		}
		//时间判断，exp_date 小于今天不能退订
		if(order.getExp_date().before(DateHelper.today())){
			throw new ServicesException(ErrorCode.NotCancelStatusException);
		}
		//订购时间=今天或状态是施工中的或高级权限，都可以退订
		if(DateHelper.isToday(order.getCreate_time())
				||order.getStatus().equals(StatusConstants.INSTALL)
				||orderComponent.isHighCancel(busi_code)){
			return;
		}
		
	    //普通退订限制判断
		//包多月判断
		if(order.getBilling_cycle()>1){
			throw new ServicesException(ErrorCode.NotCancelHasMoreBillingCycle);
		}
		//基础单产品的硬件协议期未到不能终止
		if(order.getProd_type().equals(SystemConstants.PROD_TYPE_BASE)
				&& order.getIs_base().equals(SystemConstants.BOOLEAN_TRUE)
				&&order.getProtocol_date()!=null
				&&DateHelper.today().before(order.getProtocol_date())){
			throw new ServicesException(ErrorCode.NotCancelUserProtocol);
		}
		//套餐的情况,判断子产品的硬件协议期
		if(!order.getProd_type().equals(SystemConstants.PROD_TYPE_BASE)){
			for(CProdOrderDto son:  cProdOrderDao.queryProdOrderDtoByPackageSn(order.getOrder_sn())){
				if(son.getIs_base().equals(SystemConstants.BOOLEAN_TRUE)
						&&son.getProtocol_date()!=null
						&&DateHelper.today().before(son.getProtocol_date())){
					throw new ServicesException(ErrorCode.NotCancelUserProtocol);
				}
			}
		}
	}
	
	
	
	
	@Override
	public OrderProdPanel queryOrderableProd(String busiCode,String custId,String userId, String filterOrderSn)
			throws Exception{
		OrderProdPanel orderProdPanel=orderComponent.queryOrderableProd(busiCode, custId, userId, filterOrderSn);
		//IP收费方案处理
		if(orderProdPanel.getProdList()!=null&&orderProdPanel.getProdList().size()>0){
			complateOrderableProdIpFee(orderProdPanel,custId);
		}
		
		return orderProdPanel;
	}
	/**
	 * 订购界面的IP收费方案
	 * @param orderProdPanel
	 * @throws Exception 
	 */
	private void complateOrderableProdIpFee(OrderProdPanel orderProdPanel,String custId) throws Exception{
		Map<String,BusiFeeDto> ipUserFeeMap=feeComponent.queryUserIpAddresFee(custId);
		if(ipUserFeeMap.size()==0) return;
		if(StringHelper.isNotEmpty(orderProdPanel.getUserId())&&ipUserFeeMap.containsKey(orderProdPanel.getUserId())){
			//单用户情况
			orderProdPanel.setBusiFee(ipUserFeeMap.get(orderProdPanel.getUserId()));
		}else if(StringHelper.isEmpty(orderProdPanel.getUserId())
				&&orderProdPanel.getProdList()!=null&&orderProdPanel.getProdList().size()>0){
			//套餐情况
			List<PPackageProd> pakList=pPackageProdDao.queryPackProdById(orderProdPanel.getProdList().get(0).getProd_id());
			if(pakList==null||pakList.size()==0) return;
			//随便取一个用户获得IPFee配置
			for(BusiFeeDto busiFee: ipUserFeeMap.values()){
				orderProdPanel.setBusiFee(busiFee);
				break;
			}
		}
	}
	
	@Override
	public Map<String,List<CProdOrderDto>> queryCustEffOrder(String custId,String loadType) throws Exception {
		List<CProdOrderDto> orderList=null;

	    if("EFF".equals(loadType)){
	    	//提取有效的订购记录
			orderList = cProdOrderDao.queryCustEffOrderDto(custId);
		}else if("ALL".equals(loadType)){
			//提取未退订的订购记录
			orderList=cProdOrderDao.queryCustAllOrderDto(custId);
		}else if("HIS".equals(loadType)) {
			//TODO 已退订的订购记录
			
		}else{
			//提取有效的订购记录,如果不存在有效的订购记录 则提取最近一条订购记录
			orderList=orderComponent.queryCustEFFAndLastOrder(custId);
		}
		
		for (CProdOrderDto order :orderList){
			if (StringHelper.isEmpty(order.getUser_id()))
				order.setUser_id("CUST");
		}
		
		return CollectionHelper.converToMap(orderList, "user_id");
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
		//要装入施工中和正常的状态终端用户
		panel.setUserList(userComponent.queryCanSelectByCustId(cust_id));
		//有ip费用的用户产品到期日处理
		Map<String,BusiFeeDto> ipFeeMap=feeComponent.queryUserIpAddresFee(cust_id);
		for(CUser user:panel.getUserList()){
			if(ipFeeMap.containsKey(user.getUser_id())){
				user.setProd_exp_date(ipFeeMap.get(user.getUser_id()).getLast_prod_exp());
			}
		}
		//装入内容配置信息
		fillPackageProdConfig(panel,pPackageProdDao.queryPackProdById(prod_id));
		
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
			groupList.add(pgu);
			pgu.setProdList(new ArrayList<PProd>());
			for(String prod_id: pakprod.getProd_list().split(",")){
				if(StringHelper.isNotEmpty(prod_id)){
					PProd prodconfig=pProdDao.findByKey(prod_id);
					if(prodconfig==null){
						throw new ComponentException(ErrorCode.OrderDatePackageConfig);
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
		if(mainorder==null){
			return;
		}else if(prod_id.equals(mainorder.getProd_id())){
			this.lastOrderSelectUserByPak(panel, last_order_sn);
		}else{
			this.lastOrderSelectUserSameProdList( panel, last_order_sn,mainorder.getProd_id());
		}
		
	}
	
	/**
	 * 相同套餐匹配
	 * @throws JDBCException 
	 */
	private void lastOrderSelectUserByPak(PackageGroupPanel panel,String last_order_sn) throws Exception{
		//提取原订购记录的套餐组用户选择情况,且适用现在的套餐产品限制条件
		Map<String,Set<String>> selectUsers=new HashMap<String,Set<String>>();
		for(CProdOrder order:cProdOrderDao.queryPakDetailOrder(last_order_sn)){
			if(StringHelper.isNotEmpty(order.getPackage_group_id())
					&&StringHelper.isNotEmpty(order.getUser_id())){
				if(selectUsers.get(order.getPackage_group_id())==null){
					selectUsers.put(order.getPackage_group_id(), new HashSet<String>());
				}
				selectUsers.get(order.getPackage_group_id()).add(order.getUser_id());
			}
		}
		//装到新订购的套餐内容组用户选择中
		Map<String,CUser> userMap=CollectionHelper.converToMapSingle(panel.getUserList(), "user_id"); 
		for(PackageGroupUser pgu: panel.getGroupList()){
			Set<String> userSet=selectUsers.get(pgu.getPackage_group_id());
			if(userSet!=null){
				pgu.setUserSelectList(new ArrayList<String>());
				for(String user_id:userSet){
					if(pgu.getUserSelectList().size()>=pgu.getMax_user_cnt()){
						break;
					}
					CUser user=userMap.get(user_id);
					if(user==null){
						continue;
					}
					if(!user.getUser_type().equals(pgu.getUser_type())){
						continue;
					}
					if(StringHelper.isNotEmpty(pgu.getTerminal_type())
							&&!pgu.getTerminal_type().equals(user.getTerminal_type())){
						continue;
					}
					pgu.getUserSelectList().add(user_id);
				}
			}
		}
	}
	/**
	 * 不同套餐，使用相同内容匹配
	 * @throws Exception 
	 */
	private void lastOrderSelectUserSameProdList(PackageGroupPanel panel,String last_order_sn,String last_pak_id) throws Exception{
		
		Map<String,PPackageProd> lastPakProdMap=CollectionHelper.converToMapSingle(
				pPackageProdDao.queryPackProdById(last_pak_id), "package_group_id"); 
		//提取原订购记录的套餐组用户选择情况,且适用现在的套餐产品限制条件
		//Map<prod_list,Set<user_id>> selectUsers 宽带内容组填BAND
		Map<String,Set<String>> selectUsers=new HashMap<String,Set<String>>();
		for(CProdOrder order:cProdOrderDao.queryPakDetailOrder(last_order_sn)){
			if(StringHelper.isNotEmpty(order.getPackage_group_id())
					&&StringHelper.isNotEmpty(order.getUser_id())
					&&lastPakProdMap.containsKey(order.getPackage_group_id())){
				PPackageProd pakProd=lastPakProdMap.get(order.getPackage_group_id());
				String key=pakProd.getProd_list();
				if(pakProd.getUser_type().equals(SystemConstants.USER_TYPE_BAND)){
					key=SystemConstants.USER_TYPE_BAND;
				}
				if(selectUsers.get(key)==null){
					selectUsers.put(key, new HashSet<String>());
				}
				selectUsers.get(key).add(order.getUser_id());
			}
		}
		//装到新订购的套餐内容组用户选择中
		Map<String,CUser> userMap=CollectionHelper.converToMapSingle(panel.getUserList(), "user_id"); 
		for(PackageGroupUser pgu: panel.getGroupList()){
			String key=pgu.getProd_list();
			if(pgu.getUser_type().equals(SystemConstants.USER_TYPE_BAND)){
				key=SystemConstants.USER_TYPE_BAND;
			}
			Set<String> userSet=selectUsers.get(key);
			if(userSet!=null&&userSet.size()>0){
				pgu.setUserSelectList(new ArrayList<String>());
				for(String user_id:userSet){
					if(pgu.getUserSelectList().size()>=pgu.getMax_user_cnt()){
						break;
					}
					CUser user=userMap.get(user_id);
					if(user==null){
						continue;
					}
					if(!user.getUser_type().equals(pgu.getUser_type())){
						continue;
					}
					if(StringHelper.isNotEmpty(pgu.getTerminal_type())
							&&!pgu.getTerminal_type().equals(user.getTerminal_type())){
						continue;
					}
					pgu.getUserSelectList().add(user_id);
				}
				userSet.removeAll(pgu.getUserSelectList());
			}
		}
	}
	
	/**
	 * 查询转移支付的被退的订单清单
	 * 要排除未支付的订单
	 * @throws Exception 
	 */
	@Override
	public List<CProdOrderDto> queryTransferFee(OrderProd orderProd,String busi_code) throws Exception{
		
		List<CProdOrderDto> list=new ArrayList<>();
		
		for(CProdOrder order:  orderComponent.queryTransCancelOrderList(orderProd, busi_code)){
			if(order.getIs_pay().equals(SystemConstants.BOOLEAN_TRUE)){
				CProdOrderDto dto=cProdOrderDao.queryCProdOrderDtoByKey(order.getOrder_sn());
				//计算可退余额
				dto.setActive_fee(orderComponent.getTransCancelFee(orderProd.getEff_date(), order));
				list.add(dto);
			}else{
				throw new ServicesException(ErrorCode.OrderTransUnPayPleaseCancel,order.getOrder_sn());
			}
		}
		return list;
	}
	
	/**
	 * 订单的业务参数
	 */
	public static  String getOrderProdRemark(OrderProd orderProd,String busi_code) throws Exception{
		Map<String,Object> busiMap=new HashMap<>();
		busiMap.put("busi_code", busi_code);
		busiMap.put("cust_id", orderProd.getCust_id());
		busiMap.put("user_id", orderProd.getUser_id());
		busiMap.put("prod_id", orderProd.getProd_id());
		busiMap.put("tariff_id", orderProd.getTariff_id());
		busiMap.put("last_order_sn", orderProd.getLast_order_sn());
		busiMap.put("order_months", orderProd.getOrder_months());
		busiMap.put("pay_fee", orderProd.getPay_fee());
		busiMap.put("transfer_fee", orderProd.getTransfer_fee());
		busiMap.put("eff_date", orderProd.getEff_date());
		busiMap.put("exp_date", orderProd.getExp_date());
		busiMap.put("order_fee_type", orderProd.getOrder_fee_type());
		
	    if(orderProd.getGroupSelected()!=null&&orderProd.getGroupSelected().size()>0){
	    	
	    	for(PackageGroupUser pgu: orderProd.getGroupSelected()){
	    		//package_group_id  userSelectList
	    		Map<String,Object> groupDetailMap=new HashMap<>();
	    		busiMap.put(StringHelper.append("package_group_id_",pgu.getPackage_group_id()),groupDetailMap);
	    		
	    		if(pgu.getUserSelectList()!=null){
	    			groupDetailMap.put("cnt", pgu.getUserSelectList().size());
	    			if(pgu.getUserSelectList().size()<=5){
	    				groupDetailMap.put("userIds", pgu.getUserSelectList());
	    			}else{
	    				groupDetailMap.put("userIds", StringHelper.append("[",
	    						"pgu.getUserSelectList().get(0)",
	    						",...,",
	    						pgu.getUserSelectList().get(pgu.getUserSelectList().size()-1),
	    						"]"));
	    			}
	    		}else{
	    			groupDetailMap.put("cnt", "null");
	    		}
	    	}
	    }
		return JsonHelper.fromObject(busiMap);
	}
	
	public List<String> saveOrderProdList(String busi_code,OrderProd...orderProds) throws Exception{
		//锁定未支付业务,防止同一个客户被多个操作员操作订购产品
		if(orderProds==null||orderProds.length==0){
			throw new ServicesException(ErrorCode.OrderNotExists);
		}
		String cust_id=null;
		for(OrderProd orderProd:orderProds){
			if(cust_id!=null&&!cust_id.equals(orderProd.getCust_id())){
				throw new ServicesException(ErrorCode.CustDataException);
			}else if(cust_id==null){
				cust_id=orderProd.getCust_id();
			}
		}
		if(!this.getBusiParam().getCust().getCust_id().equals(cust_id)){
			throw new ServicesException(ErrorCode.CustDataException);
		}
		
		doneCodeComponent.lockCust(cust_id);
		Integer doneCode = doneCodeComponent.gDoneCode();	
		List<String> prodSns=new ArrayList<>();
		//保存未支付业务信息
		for(OrderProd orderProd:orderProds){
			if(orderProd.getPay_fee()>0&&!SystemConstants.ORDER_FEE_TYPE_ACCT.equals(orderProd.getOrder_fee_type())){
				doneCodeComponent.saveDoneCodeUnPay(cust_id, doneCode,getOptr().getOptr_id());
				break;
			}
		}
		
		for(OrderProd orderProd:orderProds){
			prodSns.add(this.saveOrderProd(orderProd,busi_code,doneCode));
		}
		
		//业务流水
		this.saveAllPublic(doneCode, getBusiParam());
		return prodSns;
	}
	/**
	 * 保存订购记录
	 * @return
	 * @throws Exception 
	 */
	protected String saveOrderProd(OrderProd orderProd,String busi_code,Integer doneCode) throws Exception{

		//订单的业务参数
		String remark=getOrderProdRemark(orderProd,busi_code);
		
		PProd prodConfig=pProdDao.findByKey(orderProd.getProd_id());
		//参数检查
		CProdOrder lastOrder=checkOrderProdParam(orderProd,prodConfig,busi_code);
				
			
		String optr_id=this.getBusiParam().getOptr().getOptr_id();
		CCust cust=cCustDao.findByKey(orderProd.getCust_id());
		
		
		//主订购记录bean生成
		CProdOrder cProdOrder=orderComponent.createCProdOrder(orderProd, doneCode, optr_id, cust.getArea_id(), cust.getCounty_id());
		//产品状态设置
		cProdOrder.setStatus(orderComponent.getNewOrderProdStatus(lastOrder,orderProd));
		//业务是否需要支付判断                     
		cProdOrder.setIs_pay(orderProd.getPay_fee()>0&&!SystemConstants.ORDER_FEE_TYPE_ACCT.equals(orderProd.getOrder_fee_type())
				?SystemConstants.BOOLEAN_FALSE:SystemConstants.BOOLEAN_TRUE);
		cProdOrder.setRemark(remark);
		//保存订购记录
		orderComponent.saveCProdOrder(cProdOrder,orderProd,busi_code);
		
		//已支付且订单状态是正常的要发加授权指令
		this.atvProd(cProdOrder, prodConfig,doneCode);
		
		//费用信息
		if(orderProd.getPay_fee()>0){
			if(!SystemConstants.ORDER_FEE_TYPE_ACCT.equals(orderProd.getOrder_fee_type())){
				//缴费支付
				this.saveCFee(cProdOrder,orderProd.getPay_fee(),cust,doneCode,busi_code);
			}else{
				//账户支付
				this.savePayOrderByAcct(cProdOrder,prodConfig,orderProd.getPay_fee(),doneCode,busi_code);
			}
		}
	
		return cProdOrder.getOrder_sn();
	}
	/**
	 * 使用账户支付订单金额
	 * @throws Exception 
	 */
	private void savePayOrderByAcct(CProdOrder cProdorder,PProd prodConfig,Integer payFee,Integer doneCode,String busiCode) throws Exception{
		CAcct acct=acctComponent.queryCustAcctByCustId(cProdorder.getCust_id());
		//扣款
		List<CAcctAcctitemChange> changeList=
				acctComponent.saveAcctDebitFee(acct.getCust_id(), acct.getAcct_id(), SystemConstants.ACCTITEM_PUBLIC_ID,
						SystemConstants.ACCT_CHANGE_PAY, payFee*-1, busiCode, doneCode, false,prodConfig.getProd_name());
		//订单资金明细
		List<CProdOrderFee> orderFees=new ArrayList<>();

		for(CAcctAcctitemChange change:changeList){
			CProdOrderFee orderFee=new CProdOrderFee();
			orderFees.add(orderFee);
			orderFee.setOrder_fee_sn(cProdOrderFeeDao.findSequence().toString());
			orderFee.setDone_code(doneCode);
			orderFee.setOrder_sn(cProdorder.getOrder_sn());
			orderFee.setInput_type(SystemConstants.ORDER_FEE_TYPE_ACCT);
			orderFee.setInput_sn(change.getAcct_change_sn());
			orderFee.setInput_fee(change.getChange_fee()*-1);
			orderFee.setFee_type(change.getFee_type());
			orderFee.setCreate_time(new Date());
			orderFee.setCounty_id(cProdorder.getCounty_id());
			orderFee.setArea_id(cProdorder.getArea_id());
		}

		cProdOrderFeeDao.save(orderFees.toArray(new CProdOrderFee[orderFees.size()]));
	}
	/**
	 * 激活产品
	 * 已支付且订单状态是正常的要发加授权指令，且服务渠道非移动端的(移动端是直接授权)
	 * @param cProdOrder
	 * @param prodConfig
	 * @throws JDBCException
	 */
	private void atvProd(CProdOrder cProdOrder,PProd prodConfig,Integer doneCode) throws Exception{
		if(cProdOrder.getIs_pay().equals(SystemConstants.BOOLEAN_TRUE)
				&&cProdOrder.getStatus().equals(StatusConstants.ACTIVE)
				&&!SystemConstants.SERVICE_CHANNEL_MOBILE.equals(this.getBusiParam().getService_channel())){
			
			if(prodConfig.getProd_type().equals(SystemConstants.PROD_TYPE_BASE)){
				List<CProdOrder> list=new ArrayList<>();
				list.add(cProdOrder);
				authComponent.sendAuth(cUserDao.findByKey(cProdOrder.getUser_id()), list, BusiCmdConstants.ACCTIVATE_PROD, doneCode);
			}else{
				//套餐的授权
				Map<String,CUser> userMap=CollectionHelper.converToMapSingle(cUserDao.queryUserByCustId(cProdOrder.getCust_id()), "user_id");
				Map<CUser,List<CProdOrder>> atvMap=new HashMap<>();
				for(CProdOrder order: cProdOrderDao.queryPakDetailOrder(cProdOrder.getOrder_sn())){
					CUser user=userMap.get(order.getUser_id());
					if(user==null){
						throw new ServicesException(ErrorCode.OrderDateException,order.getOrder_sn());
					}
					List<CProdOrder> list=atvMap.get(user);
					if(list==null){
						list=new ArrayList<>();
						atvMap.put(user, list);
					}
					list.add(order);
				}
				for(CUser user:atvMap.keySet()){
					authComponent.sendAuth(user, atvMap.get(user),  BusiCmdConstants.ACCTIVATE_PROD, doneCode);
				}
			}
		}
	}
	/**
	 * 钝化产品（不处理套餐，但是一个产品如果是套餐子产品则会被处理）
	 * @param cancelList
	 * @throws Exception 
	 */
	private void pstProdNoPackage(List<CProdOrder> cancelResultList,Map<String,CUser> userMap,Integer done_code) throws Exception{
	
		Map<CUser,List<CProdOrder>> pstProdMap=new HashMap<>();
		for(CProdOrder pstorder:cancelResultList){
			if(StringHelper.isNotEmpty(pstorder.getUser_id())){
				CUser user=userMap.get(pstorder.getUser_id());
			    if(user==null){
			    	user=cUserDao.findByKey(pstorder.getUser_id());
			    	userMap.put(pstorder.getUser_id(), user);
			    	if(user==null){
			    		throw new ServicesException(ErrorCode.OrderDateException,pstorder.getOrder_sn());
			    	}
			    }
			    List<CProdOrder> pstlist=pstProdMap.get(user);
			    if(pstlist==null){
			    	pstlist=new ArrayList<>();
			    	pstProdMap.put(user, pstlist);
			    }
			    pstlist.add(pstorder);
			}
		}
		for(CUser user:  pstProdMap.keySet()){
			authComponent.sendAuth(user, pstProdMap.get(user), BusiCmdConstants.PASSVATE_PROD, done_code);
		}
	}
	/**
	 * 保存订单收费费用信息
	 * @param orderProd
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	private void saveCFee(CProdOrder cProdorder,Integer payFee,CCust cust,Integer doneCode,String busi_code) throws Exception{
		PayDto pay=new PayDto();
		BeanHelper.copyProperties(pay, cProdorder);
		pay.setProd_sn(cProdorder.getOrder_sn());
		pay.setAcctitem_id(cProdorder.getProd_id());
		pay.setBegin_date(DateHelper.dateToStr(cProdorder.getEff_date()));
		pay.setInvalid_date(DateHelper.dateToStr(cProdorder.getExp_date()));
		pay.setPresent_fee(0);
		pay.setFee(payFee);
		
		CFeeAcct feeAcct=feeComponent.saveAcctFee(cust.getCust_id(), cust.getAddr_id(), pay, doneCode, busi_code, StatusConstants.UNPAY);
		//插入订单费用明细
		CProdOrderFee inputOrderFee=new CProdOrderFee();
		inputOrderFee.setOrder_fee_sn(cProdOrderFeeDao.findSequence().toString());
		inputOrderFee.setOrder_sn(cProdorder.getOrder_sn());
		inputOrderFee.setDone_code(doneCode);
		inputOrderFee.setInput_type(SystemConstants.ORDER_FEE_TYPE_CFEE);
		inputOrderFee.setInput_sn(feeAcct.getFee_sn());
		inputOrderFee.setInput_fee(payFee);
		inputOrderFee.setFee_type( StatusConstants.UNPAY);
		inputOrderFee.setCounty_id(cProdorder.getCounty_id());
		inputOrderFee.setArea_id(cProdorder.getArea_id());
		inputOrderFee.setCreate_time(new Date());
		cProdOrderFeeDao.save(inputOrderFee);
	}
	
	/**
	 * 保存业务的未支付信息
	 * @param orderProd
	 * @return
	 * @throws JDBCException 
	 * @throws Exception 
	 */
	private boolean saveDoneCodeUnPay(String custId,Integer payFee,Integer done_code,String optr_id) throws Exception{
		List<CDoneCodeUnpay> unPayList =doneCodeComponent.queryUnPayList(custId);
		if(unPayList.size()==0&&payFee==0){
			//没有未支付的业务，且当前新订单不需要支付，则该笔订单业务设置为已支付
			return true;
		}else{
			doneCodeComponent.saveDoneCodeUnPay(custId, done_code,optr_id);
			return false;
		}
	}
	
	/**
	 * 检查保存产品订购的参数
	 * @param orderProd
	 * @param busi_code
	 * @return
	 * @throws Exception
	 */
	private  CProdOrder checkOrderProdParam(OrderProd orderProd,PProd prodConfig,String busi_code) throws Exception{
		
		PProd prod=prodConfig;
		if(!prod.getProd_type().equals(SystemConstants.PROD_TYPE_BASE)){
			if(StringHelper.isNotEmpty(orderProd.getUser_id())){
				throw new ServicesException(ErrorCode.OrderPackageHasSingleUserParam);
			}
		}
		CProdOrder lastOrder=null;
		//user_id数据校验
		if(StringHelper.isNotEmpty(orderProd.getLast_order_sn())){
			lastOrder=cProdOrderDao.findByKey(orderProd.getLast_order_sn());
			if(lastOrder==null){
				throw new ServicesException(ErrorCode.OrderDateLastOrderIsLost);
			}
			if(!lastOrder.getCust_id().equals(orderProd.getCust_id())){
				throw new ServicesException(ErrorCode.OrderDateLastOrderNotCust);
			}
			if(StringHelper.isNotEmpty(lastOrder.getUser_id())){
				if(StringHelper.isEmpty(orderProd.getUser_id())){
					orderProd.setUser_id(lastOrder.getUser_id());
				}
				if(!lastOrder.getUser_id().equals(orderProd.getUser_id())){
					throw new ServicesException(ErrorCode.OrderDateLastOrderNotUser);
				}
				CUser user=cUserDao.findByKey(orderProd.getUser_id());
				if(user==null){
					throw new ServicesException(ErrorCode.OrderDateUserNotCust,orderProd.getUser_id());
				}
				if(!user.getCust_id().equals(orderProd.getCust_id())){
					throw new ServicesException(ErrorCode.OrderDateUserNotCust,orderProd.getUser_id());
				}
			}
		}
		//上期订购记录校检，是否最近一条订购记录
		if(lastOrder!=null){
			List<CProdOrder> lastOrderList=new ArrayList<>();
			if(!prod.getProd_type().equals(SystemConstants.PROD_TYPE_BASE)){
				//套餐的情况
				lastOrderList=cProdOrderDao.queryNotExpPackageOrder(orderProd.getCust_id());
			}else if(prod.getServ_id().equals(SystemConstants.PROD_SERV_ID_BAND)){
				//单产品宽带的情况
				for(CProdOrder order: cProdOrderDao.queryProdOrderDtoByUserId(orderProd.getUser_id())){
					lastOrderList.add(order);
				}
			}else{
				//单产品非宽带的情况
				for(CProdOrder order: cProdOrderDao.queryProdOrderDtoByUserId(orderProd.getUser_id())){
					if(order.getProd_id().equals(orderProd.getProd_id())){
						lastOrderList.add(order);
					}
				}
			}
			
			if(lastOrderList==null||lastOrderList.size()==0){
				throw new ServicesException(ErrorCode.OrderDateLastOrderIsLost);
			}
			for(CProdOrder tmpOrder:lastOrderList){
				if(tmpOrder.getExp_date().after(lastOrder.getExp_date())){
					throw new ServicesException(ErrorCode.OrderDateLastOrderIsLost);
				}
			}
		}
		//开始计费日校检
		if(StringHelper.isNotEmpty(orderProd.getLast_order_sn())
				&&!busi_code.equals(BusiCodeConstants.PROD_UPGRADE)){
			//有上期订购记录且非升级的情况，开始计费日是上期计费日+1天
			if(!DateHelper.addDate(lastOrder.getExp_date(), 1).equals(orderProd.getEff_date())){
				throw new ServicesException(ErrorCode.OrderDateEffDateError);
			}
		}else{
			//没有上期订购记录 或者 升级的情况，开始计费日=今天
			if(!DateHelper.isToday(orderProd.getEff_date())){
				throw new ServicesException(ErrorCode.OrderDateEffDateError);
			}
		}
		
		//订购月数校检
		String[] tmpTariff=orderProd.getTariff_id().split("_");
		int billing_cycle=0;
		int rent=0;
		PProdTariff tariff=pProdTariffDao.findByKey(tmpTariff[0]);
		
		if(tmpTariff.length==2){
			
			PProdTariffDisct disct= pProdTariffDisctDao.findByKey(tmpTariff[1]);
			billing_cycle=disct.getBilling_cycle();
			rent=disct.getDisct_rent();
		}else{
			billing_cycle=tariff.getBilling_cycle();
			rent=tariff.getRent();
		}
		//订购期数(包天=round(order_months*30))
		int order_cycles=tariff.getBilling_type().equals(SystemConstants.BILLING_TYPE_DAY)?
				Math.round(orderProd.getOrder_months()*30):orderProd.getOrder_months().intValue();
		//记录资费计费类型和订购周期数（后面要使用）
		orderProd.setBilling_type(tariff.getBilling_type());
		orderProd.setOrder_cycle(order_cycles);
		
		if(orderProd.getOrder_months()==0&&!busi_code.equals(BusiCodeConstants.PROD_UPGRADE)){
			throw new  ComponentException(ErrorCode.OrderDateOrderMonthError);
		}
		if(order_cycles%billing_cycle!=0){
			throw new  ComponentException(ErrorCode.OrderDateOrderMonthError);
		}
		
		//订购支付金额验证
		if( (rent*order_cycles/billing_cycle) !=(orderProd.getPay_fee()+orderProd.getTransfer_fee())){
			throw new ComponentException(ErrorCode.OrderDateFeeError);
		}
		
		//结束计费日校检
		if(busi_code.equals(BusiCodeConstants.PROD_UPGRADE)&&orderProd.getOrder_months()==0){
			//升级且0订购月数的情况，结束日期=上期订购结束日
			if(!orderProd.getExp_date().equals(lastOrder.getExp_date())){
				throw new ServicesException(ErrorCode.OrderDateExpDateError);
			}
		}else{
			//包月 结束计费日=开始计费日+订购月数 -1天。
			if(tariff.getBilling_type().equals(SystemConstants.BILLING_TYPE_MONTH)
					&& !DateHelper.getNextMonthPreviousDay(orderProd.getEff_date(), order_cycles)
						.equals(orderProd.getExp_date())){
				throw new ServicesException(ErrorCode.OrderDateExpDateError);
			}
			//包天 结束计费日=开始计费日+订购天数-1天
			if(tariff.getBilling_type().equals(SystemConstants.BILLING_TYPE_DAY)
				&& !DateHelper.addDate(orderProd.getEff_date(), order_cycles-1)
				.equals(orderProd.getExp_date())){
					throw new ServicesException(ErrorCode.OrderDateExpDateError);
				}
		}		
		//支付类型判断
		if(orderProd.getOrder_fee_type()!=null){
			if(!orderProd.getOrder_fee_type().equals(SystemConstants.ORDER_FEE_TYPE_ACCT)
					&&!orderProd.getOrder_fee_type().equals(SystemConstants.ORDER_FEE_TYPE_CFEE)){
				throw new ServicesException(ErrorCode.OrderDateException);
			}
		}
		
		return lastOrder;
	}
	/**
	 * 缴费界面数据初始化
	 * 过期产品的问题？怎么搞好？
	 */
	public List<CProdOrderFollowPay> queryFollowPayOrderDto(String custId) throws Exception{
		doneCodeComponent.checkUnPayOtherLock(custId, this.getOptr().getOptr_id());
		List<CProdOrderFollowPay> orderList  = orderComponent.queryFollowPayOrderDto(custId);
		if(orderList.size()>0){
			//ip收费
			complateFollowPayIpAddressFee(orderList,custId);
		}
		return orderList;
		
	}


	/**
	 * 补全缴费节目的IP收费方案
	 * @param orderList
	 * @param custId
	 * @throws Exception
	 */
	private void complateFollowPayIpAddressFee(List<CProdOrderFollowPay> orderList,String custId) throws Exception{
		//IP加挂收费处理
		Map<String,BusiFeeDto> ipUserFeeMap=feeComponent.queryUserIpAddresFee(custId);
		if(ipUserFeeMap.size()==0) return;
		
		for(CProdOrderFollowPay fp:orderList){
			if(StringHelper.isNotEmpty(fp.getUser_id())){
				//单产品
				if(ipUserFeeMap.containsKey(fp.getUser_id())){
					fp.setBusiFee(ipUserFeeMap.get(fp.getUser_id()));
				}
			}else if(fp.getGroupSelected()!=null&&fp.getGroupSelected().size()>0){
				//套餐判断用户选择有
				BusiFeeDto busiFee=null;
				for(PackageGroupUser pgu: fp.getGroupSelected()){
					if(pgu.getUserSelectList()!=null&&pgu.getUserSelectList().size()>0){
						for(String userId:pgu.getUserSelectList()){
							if(ipUserFeeMap.containsKey(userId)){
								BusiFeeDto _selectBusiFee=ipUserFeeMap.get(userId);
								if(busiFee==null){
									busiFee=_selectBusiFee;
								}else{
									if(!busiFee.getFee_id().equals(_selectBusiFee.getFee_id())){
										throw new ServicesException(ErrorCode.CustUserIpAddressFeeCoinfigError);
									}
									busiFee.setFee_count(busiFee.getFee_count()+_selectBusiFee.getFee_count());
									if(_selectBusiFee.getLast_prod_exp().after(busiFee.getLast_prod_exp())){
										busiFee.setLast_prod_exp(_selectBusiFee.getLast_prod_exp());
									}
									
								}
							}
						}
					}
				}
				fp.setBusiFee(busiFee);
			}
		}
	}


	public void savePublicRecharge(String pay_type, Integer fee, String receipt_id) throws Exception {
		if(fee == null || fee == 0){
			throw new ServicesException(ErrorCode.ParamIsNull);
		}
		CCust cust=this.getBusiParam().getCust();
		String custId = cust.getCust_id();
		doneCodeComponent.lockCust(custId);		
		Integer doneCode=doneCodeComponent.gDoneCode();
		String busiCode = this.getBusiParam().getBusiCode();
		if(fee >0){
			if(pay_type.equals(SystemConstants.PAY_TYPE_CASH)){
				//记录未支付业务
				doneCodeComponent.saveDoneCodeUnPay(custId, doneCode, this.getOptr().getOptr_id());
			}
			//查询公用账户
			CAcct acct=acctComponent.queryCustAcctByCustId(cust.getCust_id());
			String acctItemId=SystemConstants.ACCTITEM_PUBLIC_ID;
			//保存退款记录
			PayDto pay = new PayDto();
			pay.setCust_id(custId);
			pay.setAcct_id(acct.getAcct_id());
			pay.setAcctitem_id(SystemConstants.ACCTITEM_PUBLIC_ID);
			pay.setFee(fee);
			feeComponent.saveAcctFee(custId, cust.getAddr_id(), pay, doneCode, busiCode, StatusConstants.UNPAY);
			//保存缴费信息
			TPayType type = tPayTypeDao.findByKey(pay_type);
			acctComponent.saveAcctAddFee(custId, acct.getAcct_id(), acctItemId, SystemConstants.ACCT_CHANGE_CFEE, fee, type.getAcct_feetype(), busiCode, doneCode,null);
		}
		this.saveAllPublic(doneCode, this.getBusiParam());
	}

	public void savePublicRefund(Integer fee) throws Exception {
		if(fee == null || fee == 0){
			throw new ServicesException(ErrorCode.ParamIsNull);
		}
		
		String custId=this.getBusiParam().getCust().getCust_id();
		doneCodeComponent.lockCust(custId);		
		Integer doneCode=doneCodeComponent.gDoneCode();
		//记录未支付业务
		doneCodeComponent.saveDoneCodeUnPay(custId, doneCode, this.getOptr().getOptr_id());
		
		
		CAcct acct = acctComponent.queryCustAcctByCustId(custId);
		//保存退款记录
		PayDto pay = new PayDto();
		pay.setCust_id(custId);
		pay.setAcct_id(acct.getAcct_id());
		pay.setAcctitem_id(SystemConstants.ACCTITEM_PUBLIC_ID);
		pay.setFee(fee*-1);
		feeComponent.saveAcctFee(custId,this.getBusiParam().getCust().getAddr_id(), pay, doneCode, this.getBusiParam().getBusiCode(), SystemConstants.PAY_TYPE_UNPAY);
		acctComponent.saveAcctDebitFee(custId, acct.getAcct_id(), SystemConstants.ACCTITEM_PUBLIC_ID, SystemConstants.ACCT_CHANGE_REFUND, fee*-1, this.getBusiParam().getBusiCode(), doneCode, true,null);
		
		//检查金额
		List<CAcctAcctitemActive> list = acctComponent.queryActiveMinusByCustId(custId);
		if(list.size()>0){
			throw new ComponentException(ErrorCode.AcctBalanceError);
		}
		this.saveAllPublic(doneCode, this.getBusiParam());
	}
	
}
