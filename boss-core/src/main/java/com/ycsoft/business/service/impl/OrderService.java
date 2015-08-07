package com.ycsoft.business.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ycsoft.beans.config.TRuleDefine;
import com.ycsoft.beans.core.common.CDoneCodeUnpay;
import com.ycsoft.beans.core.cust.CCust;
import com.ycsoft.beans.core.fee.CFee;
import com.ycsoft.beans.core.prod.CProdOrder;
import com.ycsoft.beans.core.prod.CProdOrderDto;
import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.beans.prod.PPackageProd;
import com.ycsoft.beans.prod.PProd;
import com.ycsoft.beans.prod.PProdTariffDisct;
import com.ycsoft.business.component.config.ExpressionUtil;
import com.ycsoft.business.component.core.OrderComponent;
import com.ycsoft.business.dao.config.TRuleDefineDao;
import com.ycsoft.business.dao.core.cust.CCustDao;
import com.ycsoft.business.dao.core.fee.CFeeDao;
import com.ycsoft.business.dao.core.prod.CProdOrderDao;
import com.ycsoft.business.dao.core.user.CUserDao;
import com.ycsoft.business.dao.prod.PPackageProdDao;
import com.ycsoft.business.dao.prod.PProdDao;
import com.ycsoft.business.dao.prod.PProdTariffDao;
import com.ycsoft.business.dao.prod.PProdTariffDisctDao;
import com.ycsoft.business.dto.core.acct.PayDto;
import com.ycsoft.business.dto.core.prod.OrderProd;
import com.ycsoft.business.dto.core.prod.OrderProdPanel;
import com.ycsoft.business.dto.core.prod.PackageGroupPanel;
import com.ycsoft.business.dto.core.prod.PackageGroupUser;
import com.ycsoft.business.dto.core.prod.ProdTariffDto;
import com.ycsoft.business.service.IOrderService;
import com.ycsoft.commons.constants.BusiCmdConstants;
import com.ycsoft.commons.constants.BusiCodeConstants;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ComponentException;
import com.ycsoft.commons.exception.ErrorCode;
import com.ycsoft.commons.exception.ServicesException;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.commons.helper.DateHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.store.MemoryDict;
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
	private TRuleDefineDao tRuleDefineDao;
	@Autowired
	private BeanFactory beanFactory;
	@Autowired
	private CFeeDao cFeeDao;

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

		List<CProdOrderDto> orderList=orderComponent.queryOrderByCancelOrder(cancelOrder);
		//是否高级权限
		boolean isHigh=isHighCancel(busi_code);
		//参数检查		
		for(CProdOrderDto order:orderList){
			//检查能否退订
			this.checkOrderCanCancel(cust_id, isHigh, order);
			//费用计算
			order.setActive_fee(orderComponent.getOrderCancelFee(order));
		}
		
		return orderList;
	}
	/**
	 * 是否高级退订或销户功能
	 * @param busi_code
	 * @return
	 */
	private boolean isHighCancel(String busi_code){
		return BusiCodeConstants.PROD_HIGH_TERMINATE.equals(busi_code)||BusiCodeConstants.USER_HIGH_WRITE_OFF.equals(busi_code)
				?true:false;
	}
	/**
	 * 退订产品(高级和普通退订)
	 */
	public void saveCancelProd(String[] orderSns,Integer cancelFee) throws Exception{
		this.saveCancelProdOrder(isHighCancel(this.getBusiParam().getBusiCode()), cancelFee,orderSns);
	}
	/**
	 * 退订当天订单
	 */
	public void saveCancelTodayOrder(String orderSn,Integer cancelFee) throws Exception{
		checkTodayCancelOrder(orderSn);
		this.saveCancelProdOrder(true, cancelFee,orderSn);
	}
	private void checkTodayCancelOrder(String orderSn)throws Exception{
		CProdOrder order=cProdOrderDao.findByKey(orderSn);
		if(!DateHelper.isToday(order.getOrder_time())||!order.getOptr_id().equals(this.getOptr().getOptr_id())){
			throw new ServicesException(ErrorCode.NotCancelOnlyTodayIsYou);
		}
	}
	
	/**
	 * 终止产品
	 * @param isHigh 是否高级权限 orderSns 退订的订单 cancel_fee 退订的总金额
	 * @throws Exception 
	 */
	public void saveCancelProdOrder(boolean  isHigh,Integer cancel_fee,String... orderSns) throws Exception{
		
		String cust_id=this.getBusiParam().getCust().getCust_id();
		doneCodeComponent.lockCust(cust_id);
		
		//参数检查，返回退订订单详细信息列表
		List<CProdOrderDto> cancelList=checkCancelProdOrderParm(isHigh, cust_id, orderSns, cancel_fee);
		
		Integer done_code=doneCodeComponent.gDoneCode();
		if(cancel_fee<0){
			//记录未支付业务
			doneCodeComponent.saveDoneCodeUnPay(cust_id, done_code, this.getOptr().getOptr_id());
			//保存缴费信息
			this.saveCancelFee(cancelList, this.getBusiParam().getCust(), done_code, this.getBusiParam().getBusiCode());
		}
		
		List<CProdOrder> cancelResultList=new ArrayList<>();
		
		for(CProdOrderDto dto:cancelList){
			//执行退订 返回被退订的用户订单清单
			cancelResultList.addAll(orderComponent.saveCancelProdOrder(dto, done_code));
		}

		//退订直接解除授权，不等支付（因为不能取消）
		Map<String,CUser> userMap=new  HashMap<>();
	    pstProdNoPackage(cancelResultList, userMap, done_code);
	    
		//整体移动剩下订单的开始和结束计算日期
	    moveOrderByCancelOrder(cancelResultList, userMap, done_code);
		
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
	private List<CProdOrderDto> checkCancelProdOrderParm(boolean isHigh,String cust_id,String[] orderSns,Integer cancel_fee) throws Exception{
		
		if(StringHelper.isEmpty(cust_id)||cancel_fee==null||orderSns==null||orderSns.length==0){
			throw new ServicesException(ErrorCode.ParamIsNull);
		}

		List<CProdOrderDto> cancelList=new ArrayList<>();
		//参数检查
		//退款总额核对
		int fee=0;
		//相关订单的未支付核对
		Map<String,CProdOrderDto> unPayCheckMap=new HashMap<String,CProdOrderDto>();
		for(String order_sn:orderSns){
			CProdOrderDto order= cProdOrderDao.queryCProdOrderDtoByKey(order_sn);
			cancelList.add(order);
			this.checkOrderCanCancel(cust_id, isHigh, order);
			//可退费用计算
			order.setActive_fee(orderComponent.getOrderCancelFee(order));
			fee=fee+order.getActive_fee();
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
	private void checkOrderCanCancel(String cust_id,boolean isHigh,CProdOrderDto order) throws Exception{
		
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
		if(!isHigh&&order.getBilling_cycle()>1){
			//包多月判断
			throw new ServicesException(ErrorCode.NotCancelHasMoreBillingCycle);
		}
		if(!isHigh&&order.getProd_type().equals(SystemConstants.PROD_TYPE_BASE)
				&& order.getIs_base().equals(SystemConstants.BOOLEAN_TRUE)
				&&DateHelper.today().before(order.getProtocol_date())){
			//基础单产品协议期未到不能终止
			throw new ServicesException(ErrorCode.NotCancelUserProtocol);
		}
		if(!isHigh&&!order.getProd_type().equals(SystemConstants.PROD_TYPE_BASE)){
			//套餐子产品的硬件协议期判断
			for(CProdOrderDto son:  cProdOrderDao.queryProdOrderDtoByPackageSn(order.getOrder_sn())){
				if(son.getIs_base().equals(SystemConstants.BOOLEAN_TRUE)
						&&DateHelper.today().before(son.getProtocol_date())){
					throw new ServicesException(ErrorCode.NotCancelUserProtocol);
				}
			}
		}
	}
	
	/**
	 * 取消未支付订单
	 * @param order_sn
	 * @throws Exception
	 */
	public void saveUnPayOrderCancel(String  cust_id,String order_sn,String fee_sn) throws Exception{
		//锁定未支付业务，防止客户被多个营业员同时操作
	    doneCodeComponent.lockCust(cust_id);
	    
		CProdOrder order=cProdOrderDao.findByKey(order_sn);
		//检查套餐类要按订购顺序取消，同一个用户的宽带类单产品要按订购顺序取消，用户一个用户的非宽带单产品按相同产品订购顺序取消。
		//退订不能取消
		this.checkUnPayOrderCancel(order,cust_id,fee_sn);
		
		//恢复被覆盖转移的订单
		orderComponent.recoverTransCancelOrder(order.getDone_code(),order.getCust_id());
		
		//移除订单到历史表
		Integer doneCode=doneCodeComponent.gDoneCode();
		orderComponent.saveCancelProdOrder(order, doneCode);
		
		//删除缴费信息
		feeComponent.deleteUnPayCFee(fee_sn);
		
		this.saveAllPublic(doneCode, this.getBusiParam());
	}
	/**
	 * 检查是否按顺序取消
	 * 套餐类要按订购顺序取消，同一个用户的宽带类单产品要按订购顺序取消，用户一个用户的非宽带单产品按相同产品订购顺序取消。
	 * @param order
	 * @throws Exception 
	 */
	private void checkUnPayOrderCancel(CProdOrder order,String cust_id,String fee_sn) throws Exception{
		if(StringHelper.isEmpty(cust_id)||StringHelper.isEmpty(fee_sn)||order==null){
			throw new ServicesException(ErrorCode.ParamIsNull);
		}
		if(!cust_id.equals(order.getCust_id())){
			throw new ServicesException(ErrorCode.CustDataException);
		}
		CFee cfee=cFeeDao.findByKey(fee_sn);
		if(!cust_id.equals(cfee.getCust_id())){
			throw new ServicesException(ErrorCode.CustDataException);
		}
		if(cfee.getReal_pay()<0){
			throw new ServicesException(ErrorCode.UnPayOrderCancelUnsubscribe);
		}
		
		if(!order.getDone_code().equals(cfee.getCreate_done_code())||!order.getProd_id().equals(cfee.getAcctitem_id())){
			throw new ServicesException(ErrorCode.CFeeAndProdOrderIsNotOne);
		}
		
		PProd prod=pProdDao.findByKey(order.getProd_id());
		//碰撞检测
		if(!prod.getProd_type().equals(SystemConstants.PROD_TYPE_BASE)){	
			//套餐处理
			for(CProdOrder checkOrder: cProdOrderDao.queryNotExpPackageOrder(cust_id)){
				if(order.getExp_date().before(checkOrder.getExp_date())){
					throw new ServicesException(ErrorCode.UnPayOrderCancelBefor,checkOrder.getOrder_sn());
				}
			}
			//跟单产品在套餐后续订碰撞
			List<CProdOrder>  orderAfterPakList=cProdOrderDao.querySingleProdOrderAfterPak(order.getOrder_sn());
			if(orderAfterPakList!=null&&orderAfterPakList.size()>0){
				throw new ServicesException(ErrorCode.UnPayOrderCancelBefor,orderAfterPakList.get(0).getOrder_sn());
			}
			
		}else{
			//单产品直接碰撞处理
			for(CProdOrder checkOrder: cProdOrderDao.queryProdOrderDtoByUserId(order.getUser_id())){
				if(prod.getServ_id().equals(SystemConstants.PROD_SERV_ID_BAND)||order.getProd_id().equals(checkOrder.getProd_id())){
					if(order.getExp_date().before(checkOrder.getExp_date())){
						throw new ServicesException(ErrorCode.UnPayOrderCancelBefor,checkOrder.getOrder_sn());
					}
				}
			}
		}
	}
	
	@Override
	public OrderProdPanel queryOrderableProd(String busiCode,String custId,String userId, String filterOrderSn)
			throws Exception {
		OrderProdPanel panel =new OrderProdPanel();
		CCust cust = cCustDao.findByKey(custId);
		List<CProdOrderDto> orderList = cProdOrderDao.queryCustEffOrderDto(custId);
		
		if (busiCode.equals(BusiCodeConstants.PROD_SINGLE_ORDER)){
			queryUserOrderableProd(cust,userId,panel,orderList);
		} else if (busiCode.equals(BusiCodeConstants.PROD_PACKAGE_ORDER)){
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
						disct.setTariff_id(disct.getTariff_id() + "_" + disct.getDisct_id());
						//disct.setDisct_id(disct.getTariff_id() + "-" + disct.getDisct_id());
						tariffList.add(disct);
					}
				}
			}
		}

		return tariffList;
	}
	
	

	@Override
	public Map<String,List<CProdOrderDto>> queryCustEffOrder(String custId) throws Exception {
		
		List<CProdOrderDto> orderList = cProdOrderDao.queryCustEffOrderDto(custId);
		for (CProdOrderDto order :orderList){
			if (StringHelper.isEmpty(order.getUser_id()))
				order.setUser_id("CUST");
		}
		
		return CollectionHelper.converToMap(orderList, "user_id");
	}

	private boolean checkRule(CCust cust,CUser user, String ruleId) throws Exception{
		if (StringHelper.isEmpty(ruleId))
			return true;
		TRuleDefine rule = tRuleDefineDao.findByKey(ruleId);
		if (rule == null)
			return true;
		
		ExpressionUtil expressionUtil=new ExpressionUtil(beanFactory);
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
		//要装入施工中和正常的状态终端用户
		panel.setUserList(userComponent.queryCanSelectByCustId(cust_id));
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
			}
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
		//锁定未支付业务,防止同一个客户被多个操作员操作订购产品
		doneCodeComponent.lockCust(orderProd.getCust_id());
		
		
		Integer doneCode = doneCodeComponent.gDoneCode();	
		
		
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
		cProdOrder.setIs_pay(this.saveDoneCodeUnPay(orderProd, doneCode,optr_id)?SystemConstants.BOOLEAN_TRUE:SystemConstants.BOOLEAN_FALSE);
		//保存订购记录
		orderComponent.saveCProdOrder(cProdOrder,orderProd,busi_code);
		
		//已支付且订单状态是正常的要发加授权指令
		this.atvProd(cProdOrder, prodConfig,doneCode);
		
		//费用信息
		if(orderProd.getPay_fee()>0){
			this.saveCFee(cProdOrder,orderProd.getPay_fee(),cust,doneCode,busi_code);
		}
		
		//业务流水
		this.saveAllPublic(doneCode, getBusiParam());
		return cProdOrder.getOrder_sn();
	}
	/**
	 * 激活产品
	 * 已支付且订单状态是正常的要发加授权指令
	 * @param cProdOrder
	 * @param prodConfig
	 * @throws JDBCException
	 */
	private void atvProd(CProdOrder cProdOrder,PProd prodConfig,Integer doneCode) throws Exception{
		if(cProdOrder.getIs_pay().equals(SystemConstants.BOOLEAN_TRUE)
				&&cProdOrder.getStatus().equals(StatusConstants.ACTIVE)){
			
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
	 * 钝化产品（不处理套餐，但是一个产品如果套餐子产品则会被处理）
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
		
		feeComponent.saveAcctFee(cust.getCust_id(), cust.getAddr_id(), pay, doneCode, busi_code, StatusConstants.UNPAY);
	}
	/**
	 * 保存订单退款费用信息
	 * @param cancelList
	 * @param cust
	 * @param doneCode
	 * @param busi_code
	 * @throws InvocationTargetException 
	 * @throws Exception 
	 */
	private void saveCancelFee(List<CProdOrderDto> cancelList,CCust cust,Integer doneCode,String busi_code) throws Exception{
		//按订单退款
		for(CProdOrderDto order:cancelList){
			if(order.getActive_fee()>0){
				PayDto pay=new PayDto();
				BeanHelper.copyProperties(pay, order);
				pay.setProd_sn(order.getOrder_sn());
				pay.setAcctitem_id(order.getProd_id());
				pay.setBegin_date(DateHelper.dateToStr(DateHelper.today().before(order.getEff_date())? order.getEff_date():DateHelper.today()));
				pay.setInvalid_date(DateHelper.dateToStr(order.getExp_date()));
				pay.setPresent_fee(0);
				pay.setFee(order.getActive_fee()*-1);
				feeComponent.saveAcctFee(cust.getCust_id(), cust.getAddr_id(), pay, doneCode, busi_code, StatusConstants.UNPAY);
			}
		}
	}
	
	/**
	 * 保存业务的未支付信息
	 * @param orderProd
	 * @return
	 * @throws JDBCException 
	 * @throws Exception 
	 */
	private boolean saveDoneCodeUnPay(OrderProd orderProd,Integer done_code,String optr_id) throws Exception{
		List<CDoneCodeUnpay> unPayList =doneCodeComponent.queryUnPayList(orderProd.getCust_id());
		if(unPayList.size()==0&&orderProd.getPay_fee()==0){
			//没有未支付的业务，且当前新订单不需要支付，则该笔订单业务设置为已支付
			return true;
		}else{
			doneCodeComponent.saveDoneCodeUnPay(orderProd.getCust_id(), done_code,optr_id);
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
				throw new ServicesException("订购套餐时，不能填user_id！");
			}
		}
		CProdOrder lastOrder=null;
		//user_id数据校验
		if(StringHelper.isNotEmpty(orderProd.getLast_order_sn())){
			lastOrder=cProdOrderDao.findByKey(orderProd.getLast_order_sn());
			if(lastOrder==null){
				throw new ServicesException("上期订购记录不存在！");
			}
			if(!lastOrder.getCust_id().equals(orderProd.getCust_id())){
				throw new ServicesException("上期订购记录和本期客户不一致！");
			}
			if(StringHelper.isNotEmpty(lastOrder.getUser_id())){
				if(StringHelper.isEmpty(orderProd.getUser_id())){
					orderProd.setUser_id(lastOrder.getUser_id());
				}
				if(!lastOrder.getUser_id().equals(orderProd.getUser_id())){
					throw new ServicesException("上期订购记录和本期用户不一致！");
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
				throw new ServicesException("上期订购记录已过期，请重新打开订购界面！");
			}
			for(CProdOrder tmpOrder:lastOrderList){
				if(tmpOrder.getExp_date().after(lastOrder.getExp_date())){
					throw new ServicesException("上期订购记录已过期，请重新打开订购界面！");
				}
			}
		}
		//开始计费日校检
		if(StringHelper.isNotEmpty(orderProd.getLast_order_sn())
				&&!busi_code.equals(BusiCodeConstants.PROD_UPGRADE)){
			//有上期订购记录且非升级的情况，开始计费日是上期计费日+1天
			if(!DateHelper.addDate(lastOrder.getExp_date(), 1).equals(orderProd.getEff_date())){
				throw new ServicesException("开始计费日错误！");
			}
		}else{
			//没有上期订购记录 或者 升级的情况，开始计费日=今天
			if(!DateHelper.isToday(orderProd.getEff_date())){
				throw new ServicesException("开始计费日错误！");
			}
		}
		
		//订购月数校检
		String[] tmpTariff=orderProd.getTariff_id().split("_");
		int billing_cycle=0;
		if(tmpTariff.length==2){
			billing_cycle=pProdTariffDisctDao.findByKey(tmpTariff[1]).getBilling_cycle();
		}else{
			billing_cycle=pProdTariffDao.findByKey(tmpTariff[0]).getBilling_cycle();
		}
		if(orderProd.getOrder_months()==0&&!busi_code.equals(BusiCodeConstants.PROD_UPGRADE)){
			throw new  ComponentException("订购月数的不能=0");
		}
		if(orderProd.getOrder_months()%billing_cycle!=0){
			throw new  ComponentException("订购月数非资费周期月数的倍数");
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
	
}
