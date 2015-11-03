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
import com.ycsoft.beans.core.cust.CCust;
import com.ycsoft.beans.core.fee.CFeeAcct;
import com.ycsoft.beans.core.prod.CProdOrder;
import com.ycsoft.beans.core.prod.CProdOrderDto;
import com.ycsoft.beans.core.prod.CProdOrderFee;
import com.ycsoft.beans.core.prod.CProdOrderFeeOut;
import com.ycsoft.beans.core.prod.CProdOrderFollowPay;
import com.ycsoft.beans.core.prod.CancelUserDto;
import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.beans.prod.PPackageProd;
import com.ycsoft.beans.prod.PProd;
import com.ycsoft.beans.prod.PProdTariff;
import com.ycsoft.beans.prod.PProdTariffDisct;
import com.ycsoft.beans.prod.PSpkg;
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
import com.ycsoft.business.dao.prod.PSpkgDao;
import com.ycsoft.business.dto.core.acct.PayDto;
import com.ycsoft.business.dto.core.fee.BusiFeeDto;
import com.ycsoft.business.dto.core.prod.OrderProd;
import com.ycsoft.business.dto.core.prod.OrderProdEdit;
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
import com.ycsoft.commons.helper.LoggerHelper;
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
	@Autowired
	private PSpkgDao pSpkgDao;

	/**
	 * 查询订单修改需要初始化数据
	 * 未支付的，宽带和套餐可以改产品，资费，订购期限，套餐终端选择
	 * 已支付的，只有协议套餐能修改
	 * 先实现未支付的修改
	 * @param orderSn
	 * @throws Exception 
	 */
	public OrderProdEdit queryOrderToEdit(String orderSn) throws Exception{
		if(StringHelper.isEmpty(orderSn)){
			throw new ServicesException(ErrorCode.ParamIsNull);
		}
		CProdOrderDto order=cProdOrderDao.queryCProdOrderDtoByKey(orderSn);
		if(order==null){
			throw new ServicesException(ErrorCode.ParamIsNull);
		}
		if(order.getIs_pay().equals(SystemConstants.BOOLEAN_TRUE)
				&&!order.getProd_type().equals(SystemConstants.PROD_TYPE_SPKG)){
			 //已支付的，只有协议套餐能修改
			throw new ServicesException(ErrorCode.OrderEditNoProd);
		}
		OrderProdEdit edit=orderComponent.createOrderEdit(order);
		//提取可用的产品和资费列表
		orderComponent.queryOrderableEdit(custComponent.queryCustById(order.getCust_id()), order.getOrder_sn(), edit);
		
		//如果当前产品不可用，则装入当前产品和资费
		orderEditSetCurrentProdTariff(edit,order);
		
		return edit;
	}
	/**
	 * 如果当前产品资费、折扣不可用，则装入当前产品和资费、折扣
	 * @param edit
	 * @param order
	 * @throws Exception
	 */
	private void orderEditSetCurrentProdTariff(OrderProdEdit edit,CProdOrderDto order) throws Exception{
		Map<String,PProd> prodMap=CollectionHelper.converToMapSingle(edit.getProdList(), "prod_id");
		if(!prodMap.containsKey(edit.getProd_id())){
			prodMap.put(edit.getProd_id(), pProdDao.findByKey(edit.getProd_id()));
			edit.getTariffMap().put(edit.getProd_id(), new ArrayList<PProdTariffDisct>());
		}
		Map<String,PProdTariffDisct> tariffDisctMap=CollectionHelper.converToMapSingle(edit.getTariffMap().get(edit.getProd_id()), "tariff_id");
		if(!tariffDisctMap.containsKey(edit.getTariff_id())){
			
			PProdTariff pt=pProdTariffDao.findByKey(order.getTariff_id());
			
			PProdTariffDisct tariff=new PProdTariffDisct();
			tariff.setTariff_id(pt.getTariff_id());
			tariff.setBilling_cycle(pt.getBilling_cycle());
			tariff.setDisct_rent(pt.getRent());
			tariff.setDisct_name(pt.getTariff_name());
			tariff.setBilling_type(pt.getBilling_type());
			
			if(StringHelper.isNotEmpty(order.getDisct_id())){
				PProdTariffDisct disct=pProdTariffDisctDao.findByKey(order.getDisct_id());
				tariff.setTariff_id(disct.getTariff_id()+"_"+disct.getDisct_id());
				tariff.setBilling_cycle(disct.getBilling_cycle());
				tariff.setDisct_rent(disct.getDisct_rent());
				tariff.setDisct_name(disct.getDisct_name());
			}
			edit.getTariffMap().get(edit.getProd_id()).add(tariff);
		}
	}
	/**
	 * 订单修改功能
	 * 差额只能使用现金来退款或补收（减少处理难度）
	 * 新的订单金额不能小于转移支付金额
	 * 可以修改差额，修改到期日
	 * 已支付的订单不能退款
	 * 未支付的订单可以退款
	 * 套餐 不搞转移支付了，如果还存在被覆盖的独立子产品（可以使用超级退订更正，超级退订可以修改退任意金额）
	 * @throws Exception 
	 */
	public void saveOrderEdit(OrderProd orderProd) throws Exception{
		
		String cust_id=this.getBusiParam().getCust().getCust_id();
		doneCodeComponent.lockCust(cust_id);
		CProdOrderDto order=checkOrderEditParam(cust_id,orderProd);
		Integer done_code=doneCodeComponent.gDoneCode();
		

		
		// 客户套餐自动选终端处理，因为前台先不提供客户套餐用户变更功能，如果套餐变更了产品，自动更新用户对应的套餐内容
		if(order.getProd_type().equals(SystemConstants.PROD_TYPE_CUSTPKG)
				&&!order.getProd_id().equals(orderProd.getProd_id())){
			this.editCustOrderAutoSelectUser(orderProd);
		}
		
		//记录异动
		CProdOrder editOrder=orderComponent.createCProdOrder(orderProd, null, null, null, null);
		if(StringHelper.isNotEmpty(this.getBusiParam().getRemark())){
			order.setRemark(this.getBusiParam().getRemark());
		}
	    editOrder.setOrder_fee(order.getOrder_fee()+orderProd.getPay_fee());
		orderComponent.saveOrderEditChange(order, editOrder,done_code);
		
		//处理费用
		if(orderProd.getPay_fee()!=0){
			CCust cust=custComponent.queryCustById(cust_id);
			//插入CFEE记录
			String feeSn=feeComponent.saveOrderEdittoCFee(order, orderProd.getPay_fee(), cust, done_code, this.getBusiParam().getBusiCode());
			orderComponent.saveOrderEditFee(order, orderProd.getPay_fee(), feeSn, done_code);
			//记录未支付业务
			doneCodeComponent.saveDoneCodeUnPay(cust_id, done_code, this.getOptr().getOptr_id());
		}
		
		//更新订单，返回有变化的订单信息,套餐会重新处理子产品
		List<CProdOrder> orderChangeList=orderComponent.saveOrderEditBean(order, done_code, orderProd);
		//移动订单接续，套餐子产品先排，然后排非套餐子产品，使用生效时间排
		Map<String,CUser> userMap=userComponent.queryUserMap(cust_id);
		orderComponent.moveOrderByCancelOrder(orderChangeList, userMap, done_code);
		//处理授权
		this.authProdNoPackage(orderChangeList, userMap, done_code);
		getBusiParam().setOperateObj("OrderSn:"+editOrder.getOrder_sn());
		this.saveAllPublic(done_code, this.getBusiParam());
	}
	/**
	 * 订单修改，客户套餐自动选择终端
	 * @throws Exception 
	 */
	private void editCustOrderAutoSelectUser(OrderProd orderProd) throws Exception{
		List<PPackageProd> newPakProds= pPackageProdDao.queryPackProdById(orderProd.getProd_id());
		if(newPakProds.size()!= orderProd.getGroupSelected().size()){
			throw new ServicesException(ErrorCode.OrderDatePackageConfig);
		}
		for(PackageGroupUser pgu: orderProd.getGroupSelected()){
			PPackageProd oldPakProd=pPackageProdDao.findByKey(pgu.getPackage_group_id());
			if(oldPakProd==null){
				throw new ServicesException(ErrorCode.OrderDatePackageConfig);
			}
			PPackageProd selectPakProd=null;//使用套餐终端适用条件去匹配，应该匹配到唯一一条配置
			for(PPackageProd newPakProd:newPakProds){
				if(newPakProd.getUser_type().equals(oldPakProd.getUser_type())){
					if(StringHelper.isEmpty(newPakProd.getTerminal_type())
							&&StringHelper.isEmpty(oldPakProd.getTerminal_type())){
						if(selectPakProd!=null){//找到多个对应配置错误
							throw new ServicesException(ErrorCode.OrderDatePackageConfig);
						}
						selectPakProd=newPakProd;
					}else if(StringHelper.isNotEmpty(newPakProd.getTerminal_type())
							&&newPakProd.getTerminal_type().equals(oldPakProd.getTerminal_type())){
						if(selectPakProd!=null){//找到多个对应配置错误
							throw new ServicesException(ErrorCode.OrderDatePackageConfig);
						}
						selectPakProd=newPakProd;
					}
				}
			}
			if(selectPakProd==null){//找不到对应配置
				throw new ServicesException(ErrorCode.OrderDatePackageConfig);
			}
			pgu.setPackage_group_id(selectPakProd.getPackage_group_id());
		}
	}
	private CProdOrderDto checkOrderEditParam(String cust_id,OrderProd orderProd) throws Exception{
		if(StringHelper.isEmpty(cust_id)||orderProd==null||StringHelper.isEmpty(orderProd.getLast_order_sn())){
			throw new ServicesException(ErrorCode.ParamIsNull);
		}
		CProdOrderDto order=cProdOrderDao.queryCProdOrderDtoByKey(orderProd.getLast_order_sn());
		if(order==null){
			throw new ServicesException(ErrorCode.OrderNotExists);
		}
		if(StringHelper.isNotEmpty(order.getPackage_sn())){
			throw new ServicesException(ErrorCode.OrderEditIsPakDetail);
		}
		if(!cust_id.equals(orderProd.getCust_id())||
				!cust_id.equals(order.getCust_id())){
			throw new ServicesException(ErrorCode.CustDataException);
		}
//		if(!order.getStatus().equals(StatusConstants.ACTIVE)&&!order.getStatus().equals(StatusConstants.INSTALL)){
//			throw new ServicesException(ErrorCode.OrderStatusException);
//		}
		if(order.getStatus().equals(StatusConstants.ACTIVE)&&order.getExp_date().before(DateHelper.today())){
			throw new ServicesException(ErrorCode.ProdIsInvalid);//正常状态的订单 但是已过期
		}
		
		if(!order.getProd_type().equals(SystemConstants.PROD_TYPE_BASE)){//套餐的情况，有用户
			if(StringHelper.isNotEmpty(orderProd.getUser_id())){
				throw new ServicesException(ErrorCode.OrderPackageHasSingleUserParam);
			}
		}else if(!order.getUser_id().equals(orderProd.getUser_id())){//单产品的情况，用户不一致
			throw new ServicesException(ErrorCode.OrderDateLastOrderNotUser);
		}

		//未支付的订单才可以修改
		if(orderProd.getPay_fee()<0&&!order.getIs_pay().equals(SystemConstants.BOOLEAN_FALSE)){
			throw new ServicesException(ErrorCode.OrderEditOnlyUnPay);
		}
		
		//开始计费日校检，开始计费日不能变化
		if(order.getEff_date().getTime() != orderProd.getEff_date().getTime()){
			throw new ServicesException(ErrorCode.OrderDateEffDateError);
		}
		//结束计费日，必须大于等于开始计费日
		if(orderProd.getExp_date().before(orderProd.getEff_date())){
			throw new ServicesException(ErrorCode.OrderDateExpDateError);
		}
		//支付类型判断，退款只能现金方式 
		if(orderProd.getPay_fee()<0&&orderProd.getOrder_fee_type()!=null&&!orderProd.getOrder_fee_type().equals(SystemConstants.ORDER_FEE_TYPE_CFEE)){
			throw new ServicesException(ErrorCode.OrderDateException);
		}
	
		String[] tmpTariff=orderProd.getTariff_id().split("_");
		PProdTariff tariff=pProdTariffDao.findByKey(tmpTariff[0]);		

		if(orderProd.getTransfer_fee()==null){
			orderProd.setTransfer_fee(0);
		}
		//新订单金额
		int new_order_fee=order.getOrder_fee()+orderProd.getPay_fee();
		
		if(new_order_fee<0){//订单费用不能小于0
			throw new ComponentException(ErrorCode.OrderDateFeeError);
		}
		
		int oldTransFee=0;
		for(CProdOrderFee orderFee: cProdOrderFeeDao.queryByOrderSn(order.getOrder_sn())){
			if(orderFee.getInput_type().equals(SystemConstants.ORDER_FEE_TYPE_TRANSFEE)){
				oldTransFee=oldTransFee+orderFee.getFee();
			}
		}
		//新的订单金额不能小于（原始转移支付金额）
		if(new_order_fee<oldTransFee){
			throw new ComponentException(ErrorCode.OrderDateFeeError);
		}
		
		/**订购月数和结束日 校检 结束日是否等于 开始日+订购月数,不再强制验证订单修改的到期日是否准确
		if(tariff.getBilling_type().equals(SystemConstants.BILLING_TYPE_DAY)){
			Date checkExpDate= DateHelper.addDate(orderProd.getEff_date(), Math.round(orderProd.getOrder_months()*30)-1);
			if(!checkExpDate.equals(orderProd.getExp_date())){
				throw new ServicesException(ErrorCode.OrderDateExpDateError);
			}
		}else if(tariff.getBilling_type().equals(SystemConstants.BILLING_TYPE_MONTH)){
			//先加整月，然后加天
			Date checkExpDate=DateHelper.getNextMonthPreviousDay(orderProd.getEff_date(), orderProd.getOrder_months().intValue());
			checkExpDate=DateHelper.addDate(checkExpDate, Math.round((orderProd.getOrder_months().floatValue()-orderProd.getOrder_months().intValue())*30));
			if(!checkExpDate.equals(orderProd.getExp_date())){
				throw new ServicesException(ErrorCode.OrderDateExpDateError);
			}
		}**/
		
		return order;
	}
	
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
		List<CProdOrderDto> orderList= new ArrayList<>();
		orderList.add(cancelOrder);
		
		//碰撞检测
		PProd prod=pProdDao.findByKey(cancelOrder.getProd_id());
		if(!prod.getProd_type().equals(SystemConstants.PROD_TYPE_BASE)){	
			//套餐处理
			for(CProdOrder checkOrder: cProdOrderDao.queryNotExpPackageOrder(cust_id)){
				if(cancelOrder.getExp_date().before(checkOrder.getExp_date())){
					throw new ServicesException(ErrorCode.NotCancleTheOrderBefor,checkOrder.getOrder_sn());
				}
			}
			//跟单产品在套餐后续订碰撞
			List<CProdOrder>  orderAfterPakList=cProdOrderDao.querySingleProdOrderAfterPak(cancelOrder.getOrder_sn());
			if(orderAfterPakList!=null&&orderAfterPakList.size()>0){
				throw new ServicesException(ErrorCode.NotCancleTheOrderBefor,orderAfterPakList.get(0).getOrder_sn());
			}
			
		}else{
			//单产品直接碰撞处理
			for(CProdOrder checkOrder: cProdOrderDao.queryProdOrderDtoByUserId(cancelOrder.getUser_id())){
				if(prod.getServ_id().equals(SystemConstants.PROD_SERV_ID_BAND)||cancelOrder.getProd_id().equals(checkOrder.getProd_id())){
					if(cancelOrder.getExp_date().before(checkOrder.getExp_date())){
						throw new ServicesException(ErrorCode.NotCancleTheOrderBefor,checkOrder.getOrder_sn());
					}
				}
			}
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
		List<CUser> userList = this.getBusiParam().getSelectedUsers();
		List<CProdOrderDto> orderList=orderComponent.queryLogoffProdOrderDtoByUserId(user_id);
		//是否高级权限
		boolean isHigh=orderComponent.isHighCancel(busi_code);
		//费用
		orderComponent.getLogoffOrderFee(orderList, isHigh);
		return orderList;
	}
	
	public Map<String, Object> queryLogoffUserProdList(String custId, List<String> userIdList) throws Exception {
		
		if(null != userIdList && userIdList.size() == 0){
			throw new ServicesException("表格数据为空");
		}else if(userIdList.size() > 1000){
			throw new ServicesException("请一次性录入小于1000条数据");
		}
		//过滤重复用户ID
		List<String> userIds = new ArrayList<String>();
		for(String userId : userIdList){
			if(!userIds.contains(userId)){
				userIds.add(userId);
			}
		}
		List<CUser> userList = userComponent.queryUserByUserIds(userIds);
		if(userList.size() == 0){
			throw new ServicesException("无效用户ID");
		}
		for(CUser user : userList){
			if(!user.getCust_id().equals(custId)){
				throw new ServicesException("用户【"+user.getUser_id()+"】不在当前客户下!");
			}
		}
		
		List<CProdOrderDto> prodOrderList = orderComponent.queryLogoffProdOrderDtoByUserIds(userIds);
		orderComponent.getLogoffOrderFee(prodOrderList, false);
		List<CancelUserDto> cancelList = new ArrayList<CancelUserDto>();
		for(CProdOrderDto order : prodOrderList){
			CancelUserDto cancelUser = new CancelUserDto();
			cancelUser.setUser_id(order.getUser_id());
			cancelUser.setProd_name(order.getProd_name());
			cancelUser.setActive_fee(order.getActive_fee());
			cancelList.add(cancelUser);
		}
		Map<String, List<CancelUserDto>> orderMap = CollectionHelper.converToMap(cancelList, "user_id");
		List<CancelUserDto> returnCancelUserList = new ArrayList<CancelUserDto>();
		for(String key : orderMap.keySet()){
			List<CancelUserDto> list = orderMap.get(key);
			int fee = 0;
			for(CancelUserDto order : list){
				fee += order.getActive_fee();
			}
			CancelUserDto cancelUserDto = new CancelUserDto();
			cancelUserDto.setUser_id(key);
			cancelUserDto.setActive_fee(fee*-1);
			returnCancelUserList.add(cancelUserDto);
		}
		
		Map<String, List<CancelUserDto>> map = CollectionHelper.converToMap(cancelList, "prod_name");
		
		List<CancelUserDto> showCancelUserList = new ArrayList<CancelUserDto>();
		for(String key : map.keySet()){
			List<CancelUserDto> list = map.get(key);
			int fee = 0;
			CancelUserDto cancelUser = new CancelUserDto();
			for(CancelUserDto order : list){
				fee += order.getActive_fee();
			}
			cancelUser.setProd_name(key);
			cancelUser.setActive_fee(fee);
			showCancelUserList.add(cancelUser);
		}
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("showData", showCancelUserList);
		returnMap.put("returnData", returnCancelUserList);
		returnMap.put("userCount", userList.size());
		return returnMap;
	}
	

		
	/**
	 * 退订产品
	 * @param 
	 * @throws Exception 
	 */
	public void saveCancelProd(String[] orderSns,Integer cancelFee,Integer refundFee, String acctBalanceType) throws Exception{
		
		String cust_id=this.getBusiParam().getCust().getCust_id();
		doneCodeComponent.lockCust(cust_id);
		if(StringHelper.isEmpty(acctBalanceType)
				||(!acctBalanceType.equals(SystemConstants.ACCT_BALANCE_REFUND)&&
						!acctBalanceType.equals(SystemConstants.ACCT_BALANCE_TRANS))){
			throw new ServicesException(ErrorCode.ParamIsNull);
		}
		//退订的费用明细
		List<CProdOrderFee> orderFees=new ArrayList<>();
		//参数检查，返回退订订单详细信息列表
		List<CProdOrderDto> cancelList=checkCancelProdOrderParm(cust_id, orderSns, cancelFee,refundFee,orderFees,acctBalanceType);
		
		Integer done_code=doneCodeComponent.gDoneCode();
		List<CProdOrderFeeOut> outList=orderComponent.getOrderFeeOutFromOrderFee(orderFees);
		if(refundFee<0&&acctBalanceType.equals(SystemConstants.ACCT_BALANCE_REFUND)){
			//现金部分退款

			//记录未支付业务
			doneCodeComponent.saveDoneCodeUnPay(cust_id, done_code, this.getOptr().getOptr_id());
			//保存缴费信息
			feeComponent.saveCancelFee(cancelList,outList, this.getBusiParam().getCust(), done_code, this.getBusiParam().getBusiCode());
		}
		if(acctBalanceType.equals(SystemConstants.ACCT_BALANCE_TRANS)){
			//现金部分转账到公用
			for(CProdOrderFeeOut orderFee:outList){
				if(orderFee.getOutput_type().equals(SystemConstants.ORDER_FEE_TYPE_CFEE)){
					orderFee.setOutput_type(SystemConstants.ORDER_FEE_TYPE_ACCT);
				}
			}
		}
		
		if(cancelFee-refundFee!=0||acctBalanceType.equals(SystemConstants.ACCT_BALANCE_TRANS)){
			//余额转回公用账目
			acctComponent.saveCancelFeeToAcct(outList, cust_id, done_code, this.getBusiParam().getBusiCode());
		}
		
		//更新订单费用明细的转出信息
		orderComponent.saveOrderFeeOut(outList, done_code);
		
		List<CProdOrder> cancelResultList=new ArrayList<>();
		
		for(CProdOrderDto dto:cancelList){
			//执行退订 返回被退订的用户订单清单
			cancelResultList.addAll(orderComponent.saveCancelProdOrder(dto, done_code));
		}
		
		Map<String,CUser> userMap=new  HashMap<>();
		//整体移动剩下订单的开始和结束计算日期
	    orderComponent.moveOrderByCancelOrder(cancelResultList, userMap, done_code); 
	    //退订直接解除授权，不等支付（因为不能取消）	
	  	authProdNoPackage(cancelResultList, userMap, done_code);
		
		this.saveAllPublic(done_code, this.getBusiParam());
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
			,Integer cancel_fee,Integer refund_fee,List<CProdOrderFee> orderFees,String acctBalanceType) throws Exception{
		
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
		int needRefundee=refund_fee*-1;//现金退款总额
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
			
			for(CProdOrderFee orderFee: cancelFeeList){
				fee=fee+orderFee.getOutput_fee();//可退总额计算
				if(orderFee.getOutput_type().equals(SystemConstants.ORDER_FEE_TYPE_CFEE)){
					cfeeTotal=cfeeTotal+orderFee.getOutput_fee();//可退现金总额计算
				}
				//计算订单资金的可退现金部分要退多少钱
				if(orderFee.getOutput_type().equals(SystemConstants.ORDER_FEE_TYPE_CFEE)){
					if(needRefundee==0){
						orderFee.setOutput_fee(0);
					}
					if(needRefundee>orderFee.getOutput_fee()){
						needRefundee=needRefundee-orderFee.getOutput_fee();
					}else{
						orderFee.setOutput_fee(needRefundee);
						needRefundee=0;
					}
				}
			}
			
			//设置订单的实际现金退款和 强制转账金额
			int outTotalFee=0;
			int balanceCfee=0;
			for(CProdOrderFee orderFee:cancelFeeList){
				outTotalFee=outTotalFee+orderFee.getOutput_fee();
				if(orderFee.getOutput_type().equals(SystemConstants.ORDER_FEE_TYPE_CFEE)){
					balanceCfee=balanceCfee+orderFee.getOutput_fee();
				}
				//后面账户扣款异动要使用
				orderFee.setRemark(order.getProd_name());
			}
			order.setActive_fee(outTotalFee);
			order.setBalance_cfee(balanceCfee);
			order.setBalance_acct(outTotalFee-balanceCfee);
			
			orderFees.addAll(cancelFeeList);
			
			if(!order.getProd_type().equals(SystemConstants.PROD_TYPE_BASE)){
				unPayCheckMap.put("PACKAGE", order);
			}else if(order.getServ_id().equals(SystemConstants.PROD_SERV_ID_BAND)){
				unPayCheckMap.put("BAND", order);
			}else {
				unPayCheckMap.put("NOBAND", order);
			}
		}
		
		//金额核对
		if(cancel_fee*-1 > fee){
			throw new ServicesException(ErrorCode.FeeDateException);
		}
		if(refund_fee*-1 > cfeeTotal){
			throw new ServicesException(ErrorCode.FeeDateException);
		}
		if(needRefundee!=0){
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
		if(DateHelper.isToday(order.getOrder_time())
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
			orderList=cProdOrderDao.queryCustOrderALLAndHisDto(custId);
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
	 * 查询ott_mobile升级的转移支付金额
	 * @param cancelList
	 * @return
	 * @throws Exception
	 */
	public Integer queryOttMobileUpdateTransferFee(List<CProdOrderDto> cancelList) throws Exception{
		Date eff_date=DateHelper.today();
		int translate_fee=0;
		for(CProdOrderDto order:  cancelList){
			if(order.getIs_pay().equals(SystemConstants.BOOLEAN_TRUE)){
				//计算可退余额
				translate_fee=translate_fee+orderComponent.getTransCancelFee(eff_date, order);
			}else{
				throw new ServicesException(ErrorCode.OrderTransUnPayPleaseCancel,order.getOrder_sn());
			}
		}
		return translate_fee;
	}
	/**
	 * 订单的业务参数
	 */
	public static  String getOrderProdRemark(OrderProd orderProd,String busi_code,String order_sn,Integer done_code) throws Exception{
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
		busiMap.put("order_sn", order_sn);
		busiMap.put("done_code", done_code);
		
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
	 * ott_mobile产品升级
	 * @param orderProd
	 * @param cancleOrderList
	 * @param busi_code
	 * @param doneCode
	 * @throws Exception
	 */
	public void saveOttMobileUpdateProd(OrderProd orderProd,List<CProdOrderDto> cancleOrderList,String busi_code,Integer doneCode)throws Exception{
		PProd prodConfig=pProdDao.findByKey(orderProd.getProd_id());
		//参数检查
		checkOrderProdParam(orderProd,prodConfig,busi_code);
		String optr_id=this.getBusiParam().getOptr().getOptr_id();
		CCust cust=cCustDao.findByKey(orderProd.getCust_id());
		CProdOrder cProdOrder=orderComponent.createCProdOrder(orderProd, doneCode, optr_id, cust.getArea_id(), cust.getCounty_id());
		//产品状态设置
		cProdOrder.setStatus(StatusConstants.ACTIVE);
		//业务是否需要支付判断                     
		cProdOrder.setIs_pay(SystemConstants.BOOLEAN_TRUE);
		//保存订购记录
		orderComponent.saveCProdOrderByOttMobileUpgrade(cProdOrder, orderProd, cancleOrderList);
		
	}
	
	/**
	 * 保存订购记录
	 * @return
	 * @throws Exception 
	 */
	protected String saveOrderProd(OrderProd orderProd,String busi_code,Integer doneCode) throws Exception{

		
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
		cProdOrder.setRemark(this.getBusiParam().getRemark());
		
		if(LoggerHelper.isDebugEnabled(this.getClass())){//日志记录业务参数，可能核对需要使用
			LoggerHelper.debug(this.getClass(), getOrderProdRemark(orderProd,busi_code,cProdOrder.getOrder_sn(),doneCode));
		}
		
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
			orderFee.setFee(change.getChange_fee()*-1);
			orderFee.setFee_type(change.getFee_type());
			orderFee.setCreate_time(new Date());
			orderFee.setCounty_id(cProdorder.getCounty_id());
			orderFee.setArea_id(cProdorder.getArea_id());
		}

		cProdOrderFeeDao.save(orderFees.toArray(new CProdOrderFee[orderFees.size()]));
	}
	/**
	 * 激活产品
	 * 已支付要发加授权指令，且服务渠道非移动端的(移动端是直接授权)
	 * @param cProdOrder
	 * @param prodConfig
	 * @throws JDBCException
	 */
	private void atvProd(CProdOrder cProdOrder,PProd prodConfig,Integer doneCode) throws Exception{
		if(SystemConstants.SERVICE_CHANNEL_MOBILE.equals(this.getBusiParam().getService_channel())){
			//移动渠道走直接授权，不通过异步指令授权
			return;
		}
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
		inputOrderFee.setFee(payFee);
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
	
	private boolean saveDoneCodeUnPay(String custId,Integer payFee,Integer done_code,String optr_id) throws Exception{
		List<CDoneCodeUnpay> unPayList = doneCodeComponent.queryUnPayList(custId);
		if(unPayList.size()==0&&payFee==0){
			//没有未支付的业务，且当前新订单不需要支付，则该笔订单业务设置为已支付
			return true;
		}else{
			doneCodeComponent.saveDoneCodeUnPay(custId, done_code,optr_id);
			return false;
		}
	}
	 */
	
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
		
		//验证协议套餐能否适用
		if(StringHelper.isNotEmpty(tariff.getSpkg_sn())){
			PSpkg spkg = pSpkgDao.querySpkgBySn(tariff.getSpkg_sn());
			if(spkg == null){
				throw new ServicesException(ErrorCode.SpkgIsError);
			}else if(!spkg.getStatus().equals(StatusConstants.CONFIRM)){
				throw new ServicesException(ErrorCode.SpkgHasNotCONFIRM);
			}else if(!tariff.getSpkg_sn().equals(this.getBusiParam().getCust().getSpkg_sn())){
				throw new ServicesException(ErrorCode.SpkgIsNotTrueCust);
			}
			if(lastOrder!=null){
				CProdOrderDto tmp= cProdOrderDao.queryCProdOrderDtoByKey(lastOrder.getOrder_sn());
				if(tmp==null){
					throw new ServicesException(ErrorCode.ParamIsNull);
				}
				if(!SystemConstants.PROD_TYPE_SPKG.equals(tmp.getProd_type())){
					throw new ServicesException(ErrorCode.SpkgIsPaiChuCustKG);
				}
			}
		}
		
		if(tmpTariff.length==2){
			
			PProdTariffDisct disct= pProdTariffDisctDao.findByKey(tmpTariff[1]);
			billing_cycle=disct.getBilling_cycle();
			rent=disct.getDisct_rent();
		}else{
			billing_cycle=tariff.getBilling_cycle();
			rent=tariff.getRent();
		}
		//订购期数(包天=round(order_months*30))
		//TODO 这个不足一个周期需要如何修改
		int order_cycles=tariff.getBilling_type().equals(SystemConstants.BILLING_TYPE_DAY)?
				Math.round(orderProd.getOrder_months()*30):orderProd.getOrder_months().intValue();
		//记录资费计费类型和订购周期数（后面要使用）
		//orderProd.setBilling_type(tariff.getBilling_type());
		//orderProd.setOrder_cycle(order_cycles);
		
		if(orderProd.getOrder_months()==0){
			throw new  ComponentException(ErrorCode.OrderDateOrderMonthError);
		}
	
		//非0资费且普通订购，续订，套餐订购，缴费 验证订购月数，订购金额，截止日期 是否一致
		if(rent>0&&(busi_code.equals(BusiCodeConstants.PROD_SINGLE_ORDER)||
				busi_code.equals(BusiCodeConstants.PROD_CONTINUE)||
				busi_code.equals(BusiCodeConstants.PROD_PACKAGE_ORDER)||
				busi_code.equals(BusiCodeConstants.ACCT_PAY))){
			//订购月数
			if(order_cycles%billing_cycle!=0){
			throw new  ComponentException(ErrorCode.OrderDateOrderMonthError);
			}
			//订购金额
			if(orderProd.getPay_fee()==0&& (rent*order_cycles/billing_cycle) !=(orderProd.getPay_fee()+orderProd.getTransfer_fee())){
				throw new ComponentException(ErrorCode.OrderDateFeeError);
			}
			
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
		//doneCodeComponent.checkUnPayOtherLock(custId, this.getOptr().getOptr_id());
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
	@Override
	public void savePayOtherFee() throws Exception {
		String custId=this.getBusiParam().getCust().getCust_id();
		doneCodeComponent.lockCust(custId);		
		Integer doneCode=doneCodeComponent.gDoneCode();
		this.saveAllPublic(doneCode, this.getBusiParam());
	}
	
}
