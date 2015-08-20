package com.ycsoft.business.component.core;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TRuleDefine;
import com.ycsoft.beans.core.cust.CCust;
import com.ycsoft.beans.core.prod.CProdOrder;
import com.ycsoft.beans.core.prod.CProdOrderDto;
import com.ycsoft.beans.core.prod.CProdOrderFollowPay;
import com.ycsoft.beans.core.prod.CProdOrderHis;
import com.ycsoft.beans.core.prod.CProdOrderTransfee;
import com.ycsoft.beans.core.prod.CProdPropChange;
import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.beans.prod.PPackageProd;
import com.ycsoft.beans.prod.PProd;
import com.ycsoft.beans.prod.PProdTariffDisct;
import com.ycsoft.business.commons.abstracts.BaseBusiComponent;
import com.ycsoft.business.component.config.ExpressionUtil;
import com.ycsoft.business.dao.config.TRuleDefineDao;
import com.ycsoft.business.dao.core.cust.CCustDao;
import com.ycsoft.business.dao.core.prod.CProdOrderDao;
import com.ycsoft.business.dao.core.prod.CProdOrderHisDao;
import com.ycsoft.business.dao.core.prod.CProdOrderTransfeeDao;
import com.ycsoft.business.dao.core.prod.CProdStatusChangeDao;
import com.ycsoft.business.dao.core.user.CUserDao;
import com.ycsoft.business.dao.prod.PPackageProdDao;
import com.ycsoft.business.dao.prod.PProdDao;
import com.ycsoft.business.dao.prod.PProdTariffDisctDao;
import com.ycsoft.business.dto.core.prod.OrderProd;
import com.ycsoft.business.dto.core.prod.OrderProdPanel;
import com.ycsoft.business.dto.core.prod.PackageGroupUser;
import com.ycsoft.business.dto.core.prod.ProdTariffDto;
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
/**
 * 订单组件
 * @author new
 *
 */
@Component
public class OrderComponent extends BaseBusiComponent {
	@Autowired
	private PProdDao pProdDao;
	@Autowired
	private PPackageProdDao pPackageProdDao;
	@Autowired
	private CProdOrderDao cProdOrderDao;
	@Autowired
	private CUserDao cUserDao;
	@Autowired
	private CProdOrderHisDao cProdOrderHisDao;
	@Autowired
	private CProdOrderTransfeeDao cProdOrderTransfeeDao;
	@Autowired
	private CProdStatusChangeDao cProdStatusChangeDao;
	@Autowired
	private TRuleDefineDao tRuleDefineDao;
	@Autowired
	private CCustDao cCustDao;
	@Autowired
	private PProdTariffDisctDao pProdTariffDisctDao;
	@Autowired
	private BeanFactory beanFactory;
	/**
	 * cust-user-prod
	 * 客户套餐key=cust--
	 * 宽带单产品key=cust-user-
	 * 普通单产品key=cust-user-prod
	 */
	private String getOrderKey(CProdOrderDto dto){
		String key=StringHelper.append(dto.getCust_id(),"-",
				dto.getProd_type().equals(SystemConstants.PROD_TYPE_BASE)?dto.getUser_id():"","-",
				dto.getProd_type().equals(SystemConstants.PROD_TYPE_BASE)
				&&!dto.getServ_id().equals(SystemConstants.PROD_SERV_ID_BAND)?dto.getProd_id():"");
		return key;
	}
	/**
	 * 查询前 要做锁定判断doneCodeCom*t.checkUnPayOtherLock
	 * 查询可以缴费的产品信息
	 * @throws Exception 
	 */
	public List<CProdOrderFollowPay> queryFollowPayOrderDto(String cust_id) throws Exception{
		
		//有效的订购记录
		List<CProdOrderFollowPay> list=cProdOrderDao.queryFollowPayOrderDto(cust_id);
		//Map<cust_user_prod,不能续费的原因> 
		Map<String,String> hasPakDetailMap=new HashMap<String,String>();
		Map<String,CProdOrderFollowPay> maxFPMap=new HashMap<String,CProdOrderFollowPay>();
		
		for(CProdOrderFollowPay dto:list){
			String key=this.getOrderKey(dto);
			
			if(StringHelper.isNotEmpty(dto.getPackage_sn())){
				//记录是否套餐子产品
				hasPakDetailMap.put(key, "");
			}else{
				//非套餐,且截止日期大 则装入
				CProdOrderDto order=maxFPMap.get(key);
				if(order==null||dto.getExp_date().after(order.getExp_date())){
					maxFPMap.put(key, dto);
				}
			}
		}
		//能否缴费判断和提取可选资费
		Map<String,CProdOrderFollowPay> canFollowMap=new HashMap<>();
		CCust cust=cCustDao.findByKey(cust_id);
		for(Map.Entry<String, CProdOrderFollowPay>  entry: maxFPMap.entrySet()){
			CProdOrderFollowPay dto= entry.getValue();
			canFollowMap.put(dto.getOrder_sn(), dto);
			//资费重新组装
			if(StringHelper.isNotEmpty(dto.getDisct_id())){
				dto.setTariff_id(StringHelper.append(dto.getTariff_id(),"_",dto.getDisct_id()));
			}
			dto.setCanFollowPay(true);
			dto.setRemark("");
			if(StringHelper.isEmpty(dto.getUser_id())){
				dto.setUser_name("客户套餐");
			}
			if(hasPakDetailMap.containsKey(entry.getKey())){
				//存在套餐子产品订单，能不能缴费
				dto.setCanFollowPay(false);
			}else{
				//判断资费是否还适用判断
				CUser user=cUserDao.findByKey(dto.getUser_id());
				PProd prod=pProdDao.findByKey(dto.getProd_id());
				dto.setTariffList(this.queryTariffList(cust,user,prod));
				for(PProdTariffDisct disct: dto.getTariffList()){
					if(disct.getTariff_id().equals(dto.getTariff_id())){
						//资费有效
						dto.setCurrentTariffStatus(true);
					}
				}
			}		
			//套餐的子产品选择情况
			if(!dto.getProd_type().equals(SystemConstants.PROD_TYPE_BASE)){
				
				Map<String,Set<String>> groupUserMap=new HashMap<>();
				for(CProdOrder detail: cProdOrderDao.queryPakDetailOrder(dto.getOrder_sn())){
					Set<String> set=groupUserMap.get(detail.getPackage_group_id());
					if(set==null){
						set=new HashSet<String>();
						groupUserMap.put(detail.getPackage_group_id(), set);
					}
					set.add(detail.getUser_id());
				}
				List<PackageGroupUser> groupSelected=new ArrayList<>();
				for(String group_id:   groupUserMap.keySet()){
					PackageGroupUser pgu=new PackageGroupUser();
					groupSelected.add(pgu);
					pgu.setPackage_group_id(group_id);
					pgu.setUserSelectList(Arrays.asList(groupUserMap.get(group_id).toArray(new String[groupUserMap.get(group_id).size()])));
				}
				dto.setGroupSelected(groupSelected);
			}
		}	
		List<CProdOrderFollowPay> followPayList=new ArrayList<CProdOrderFollowPay>();
		//排序处理
		for(CProdOrderFollowPay dto:list){
			if(canFollowMap.containsKey(dto.getOrder_sn())){
				followPayList.add(canFollowMap.get(dto.getOrder_sn()));
			}
		}
		return followPayList;
	}
	
	/**
	 * 销户时订单退款金额计算
	 * @param orderList
	 * @param isHigh
	 * @throws Exception
	 */
	public Integer getLogoffOrderFee(List<CProdOrderDto> orderList,boolean isHigh) throws Exception{
		int cancelFee=0;
		for(CProdOrderDto order:orderList){
			if(order.getIs_pay().equals(SystemConstants.BOOLEAN_FALSE)){
				//未支付判断
				throw new ServicesException(ErrorCode.NotCancleHasUnPay);
			}
			if(isHigh){
				order.setActive_fee(getOrderCancelFee(order,DateHelper.today()));
			}else if(StringHelper.isNotEmpty(order.getPackage_sn())
					||order.getBilling_cycle()>1){
				//套餐子产品和包多月产品，低权限人员退款金额=0
				order.setActive_fee(0);
			}else if(order.getProd_type().equals(SystemConstants.PROD_TYPE_BASE)
					&& order.getIs_base().equals(SystemConstants.BOOLEAN_TRUE)
					&&order.getProtocol_date()!=null
					&&DateHelper.today().before(order.getProtocol_date())){
				//包1月产品在硬件协议期未到时计算退款余额
				order.setActive_fee(getOrderCancelFee(order,DateHelper.getTruncDate(order.getProtocol_date())));
			}else {
				order.setActive_fee(getOrderCancelFee(order,DateHelper.today()));
			}
			cancelFee=cancelFee+order.getActive_fee();
		}
		return cancelFee;
	}
	/**
	 * 是否高级退订或销户功能
	 * @param busi_code
	 * @return
	 */
	public boolean isHighCancel(String busi_code){
		return BusiCodeConstants.PROD_HIGH_TERMINATE.equals(busi_code)||BusiCodeConstants.USER_HIGH_WRITE_OFF.equals(busi_code)
				?true:false;
	}
	
	/**
	 * 因为产品退订而重新计算套餐订单的计费时间段（不处理子产品）
	 * @return 返回开始失效时间变化的订单信息
	 * @throws Exception 
	 */
	public List<CProdOrder> movePackageOrderToFollow(String cust_id,Integer done_code) throws Exception{
		return this.changeToFollow(cProdOrderDao.queryNotExpPackageOrder(cust_id), done_code);
	}
	/**
	 * 因为产品退订而重新计算宽带用户订单（含套餐子产品）的计费时间段
	 * @return 返回开始失效时间变化的订单信息
	 * @throws Exception
	 */
	public List<CProdOrder> moveBandOrderToFollow(String user_id,Integer done_code) throws Exception{
		return this.changeToFollow(cProdOrderDao.queryNotExpAllOrderByUser(user_id), done_code);
	}
	/**
	 * 因为产品退订而重新计算普通产品订单（含套餐子产品）(非宽带)的计费时间段
	 * @return 返回开始失效时间变化的订单信息
	 * @throws Exception
	 */
	public List<CProdOrder> moveProdOrderToFollow(String user_id,String prod_id,Integer done_code)throws Exception{
		return this.changeToFollow(cProdOrderDao.queryNotExpAllOrderByProd(user_id, prod_id), done_code);
	}
	
	/**
	 * 移动订单接续，并记录开始和结束计费日的异动
	 * 传入的参数moveList是按exp_date排序的
	 * @param moveList
	 * @throws InvocationTargetException 
	 * @throws Exception 
	 */
	private List<CProdOrder> changeToFollow(List<CProdOrder> moveList,Integer done_code) throws Exception{
		Date start=DateHelper.today();
		Date today=start;
		List<CProdPropChange> changeList=new ArrayList<CProdPropChange>();
		List<CProdOrder> updateList=new ArrayList<>();
		List<CProdOrder> moveResult=new ArrayList<>();
		for(CProdOrder order:moveList){
			//第一个订购记录必须是今天之前(含今天)开始，后面的订购是接续在上一条订购之后
			if((start.equals(today)&&order.getEff_date().after(start))
				||(!start.equals(today)&&!start.equals(order.getEff_date()))){
				
				Date eff_date=new Date(start.getTime());
				Date exp_date=DateHelper.getNextMonthByNum(eff_date, order.getOrder_months());
				start=DateHelper.addDate(exp_date, 1);
				
				//记录异动
				CProdPropChange change_effdate=new CProdPropChange();
				changeList.add(change_effdate);
				BeanHelper.copyProperties(change_effdate, order);
				change_effdate.setDone_code(done_code);
				change_effdate.setProd_sn(order.getOrder_sn());
				change_effdate.setColumn_name("eff_date");
				change_effdate.setOld_value(DateHelper.dateToStr(order.getEff_date()));
				change_effdate.setNew_value(DateHelper.dateToStr(eff_date));
				
				CProdPropChange change_expdate=new CProdPropChange();
				changeList.add(change_expdate);
				BeanHelper.copyProperties(change_expdate, order);
				change_expdate.setDone_code(done_code);
				change_expdate.setProd_sn(order.getOrder_sn());
				change_expdate.setColumn_name("exp_date");
				change_expdate.setOld_value(DateHelper.dateToStr(order.getExp_date()));
				change_expdate.setNew_value(DateHelper.dateToStr(exp_date));
				
				//更新开始计费日和结束计费日
				order.setEff_date(eff_date);//外部要使用所以更新
				order.setExp_date(exp_date);//外部要使用所以更新
				moveResult.add(order);
				
				CProdOrder update=new CProdOrder();
				update.setOrder_sn(order.getOrder_sn());
				update.setEff_date(eff_date);
				update.setExp_date(exp_date);
				updateList.add(update);	
			}
		}
		if(changeList.size()>0){
			cProdPropChangeDao.save(changeList.toArray(new CProdPropChange[changeList.size()]));
		}
		if(updateList.size()>0){
			cProdOrderDao.update(updateList.toArray(new CProdOrder[updateList.size()]));
		}
		return moveResult;
	}
	/**
	 * 单产品退订
	 * 查询一个订单相关的退订清单和可退费用(active_fee)
	 * 套餐=客户所有未失效套餐
	 * 宽带=相同用户所有宽带产品（含套餐子宽带产品）
	 * 非宽带单产品=相同用户相同单产品（含套餐子产品)
	 * @param cancelOrder
	 * @param prodConfig
	 * @return
	 * @throws Exception 
	 */
	public List<CProdOrderDto> queryOrderByCancelOrder(CProdOrderDto cancelOrder) throws Exception{
		List<CProdOrderDto> list=new ArrayList<>();
		Date today=DateHelper.today();
		if(!cancelOrder.getProd_type().equals(SystemConstants.PROD_TYPE_BASE)){
			//套餐的情况
			for(CProdOrderDto order: cProdOrderDao.queryPackageOrderDtoByCustId(cancelOrder.getCust_id())){
				if(order.getExp_date().after(today)||order.getExp_date().equals(today)){
					//结束日>=今天
					list.add(order);
				}
			}
		}else {
			//单产品,非套餐子产品
			for(CProdOrderDto order: cProdOrderDao.queryProdOrderDtoByUserId(cancelOrder.getUser_id())){
				if(StringHelper.isEmpty(order.getPackage_sn())
						&&(order.getExp_date().after(today)||order.getExp_date().equals(today))
						&&(cancelOrder.getServ_id().equals(SystemConstants.PROD_SERV_ID_BAND)||cancelOrder.getProd_id().equals(order.getProd_id()))){
					list.add(order);
				}
			}
		}	
		return list;
	}
	/**
	 * 提取未过期的订单记录
	 * @param user_id
	 * @return
	 * @throws Exception
	 */
	public List<CProdOrderDto> queryLogoffProdOrderDtoByUserId(String user_id) throws Exception{
		List<CProdOrderDto> list=new ArrayList<>();
		Date today=DateHelper.today();
		for(CProdOrderDto order:cProdOrderDao.queryProdOrderDtoByUserId(user_id)){
			if(order.getExp_date().equals(today)||order.getExp_date().after(today)){
				list.add(order);
			}
		}
		return list;
	}
	
	/**
	 * 计算一个订单的可退金额（终止退订和销户退订）
	 * @param order
	 * @return
	 */
	public Integer getOrderCancelFee(CProdOrder cancelOrder,Date cancelDate){
		if(StringHelper.isNotEmpty(cancelOrder.getPackage_sn())){
			//套餐子产品可退金额=0;
			return 0;
		}
		//Date cancelDate=DateHelper.today();
		//1.退订日在订购计费完整区间之前
		if(cancelDate.before(cancelOrder.getEff_date())||cancelDate.equals(cancelOrder.getEff_date())){
			//覆盖退订在订购的生效日之前(含)
			return cancelOrder.getOrder_fee();
		}
		//2.退订日在订购计费完整区间之间
		//订购的停止计费日根据订购月数反推开始计费日
		Date effDate= DateHelper.getNextMonthByNum(cancelOrder.getExp_date(),cancelOrder.getOrder_months()*-1);
		if(cancelDate.equals(effDate)||cancelDate.before(effDate)){
			//退订日包含了整个订单的订购期间
			return cancelOrder.getOrder_fee();
		}
		
		//3.退订日在 订购的计费区间内（按剩余使用天数折算）
		int months=DateHelper.compareToMonthByDate(cancelDate, cancelOrder.getExp_date());
		Date newExpDate=DateHelper.getNextMonthByNum(cancelDate,months);
		if(newExpDate.after(cancelOrder.getExp_date())){
			//新到期日大于实际到期日，则需要回退一个月再计算
			months=months-1;
		}
		if(months<0){
			months=0;
		}
		return Math.round(months*1.0f*cancelOrder.getOrder_fee()/cancelOrder.getOrder_months());
	}
	/**
	 * 恢复被覆盖的订单
	 * @param recoverDoneCode
	 * @throws JDBCException 
	 */
	public void recoverTransCancelOrder(Integer recoverDoneCode,String cust_id) throws JDBCException{
		//查询被覆盖移入历史表的订购记录
		List<CProdOrder> list=cProdOrderHisDao.queryCProdOrderByDelete(recoverDoneCode, cust_id);
		if(list!=null&&list.size()>0){
			//移回正式表
			cProdOrderDao.save(list.toArray(new CProdOrder[list.size()]));
			String orderSns[]=new String[list.size()];
			for(int i=0;i<list.size();i++){
				orderSns[i]=list.get(i).getOrder_sn();
			}
			//历史记录表移除
			cProdOrderHisDao.remove(orderSns);
			//删除转移支付异动
			cProdOrderTransfeeDao.deleteTransfeeChange(recoverDoneCode, cust_id);
			//删除出账状态异动
			cProdStatusChangeDao.deleteByDoneCode(recoverDoneCode);
		}
		
	}
	/**
	 * 查询被覆盖取消的订单(套餐订购和升级的情况)
	 * @param orderProd
	 * @param busi_code
	 * @return
	 * @throws Exception 
	 */
	public List<CProdOrder> queryTransCancelOrderList(OrderProd orderProd,String busi_code) throws Exception{
		List<CProdOrder> orderCancelList=new ArrayList<>(); 
		//提取被取消的订购记录
		if(orderProd.getGroupSelected()!=null&&orderProd.getGroupSelected().size()>0){
			//套餐订购覆盖普通订购
			orderCancelList= queryTransferFeeByPackage(orderProd);
		}else if(busi_code.equals(BusiCodeConstants.PROD_UPGRADE)&&StringHelper.isNotEmpty(orderProd.getLast_order_sn())){
			//升级的情况
			orderCancelList=queryTransferFeeByUpProd(orderProd);
		}
		return orderCancelList;
	}
	
	/**
	 * 计算覆盖退订的可退余额
	 * @param cancelDate
	 * @param cancelOrder
	 * @return
	 */
	public Integer getTransCancelFee(Date cancelDate,CProdOrder cancelOrder){
		
		//1.退订日在订购计费完整区间之前
		if(cancelDate.before(cancelOrder.getEff_date())||cancelDate.equals(cancelOrder.getEff_date())){
			//覆盖退订在订购的生效日之前(含)
			return cancelOrder.getOrder_fee();
		}
		//2.退订日在订购计费完整区间之前
		//订购的停止计费日根据订购月数反推开始计费日
		Date effDate= DateHelper.getNextMonthByNum(cancelOrder.getExp_date(),cancelOrder.getOrder_months()*-1);
		if(cancelDate.equals(effDate)||cancelDate.before(effDate)){
			//退订日包含了整个订单的订购期间
			return cancelOrder.getOrder_fee();
		}
		
		//3.退订日在 订购的计费区间内（按剩余使用天数折算）
		int months=DateHelper.compareToMonthByDate(cancelDate, cancelOrder.getExp_date());
		Date newExpDate=DateHelper.getNextMonthByNum(cancelDate,months);
		if(newExpDate.equals(cancelDate)){
			//剩余整月的情况
			return Math.round(months*1.0f*cancelOrder.getOrder_fee()/cancelOrder.getOrder_months());
		}else if(newExpDate.after(cancelOrder.getExp_date())){
			//新到期日大于实际到期日，则需要回退一个月再计算
			newExpDate=DateHelper.getNextMonthByNum(cancelDate,months-1);
			float months_fee=(months-1)*1.0f*cancelOrder.getOrder_fee()/cancelOrder.getOrder_months();
			int days=DateHelper.getDiffDays(newExpDate, cancelOrder.getExp_date());
			float days_fee= days* cancelOrder.getOrder_fee()/(cancelOrder.getOrder_months()*30.0f);
			return Math.round(months_fee+days_fee);
		}else{
			float months_fee=months*1.0f*cancelOrder.getOrder_fee()/cancelOrder.getOrder_months();
			int days=DateHelper.getDiffDays(newExpDate, cancelOrder.getExp_date());
			float days_fee= days* cancelOrder.getOrder_fee()/(cancelOrder.getOrder_months()*30.0f);
			return Math.round(months_fee+days_fee);
		}
	
	}
	
	
	/**
	 * 套餐订购覆盖普通订购的情况提取被退的订单，并结算可退余额
	 * @throws JDBCException 
	 */
	private List<CProdOrder> queryTransferFeeByPackage(OrderProd orderProd) throws JDBCException{
		//加载被覆盖的普通产品订购
		List<CProdOrder> orderCancelList=new ArrayList<>(); 
		for(PackageGroupUser pgu: orderProd.getGroupSelected()){
			if(pgu.getUserSelectList()==null||pgu.getUserSelectList().size()==0){
				continue;
			}
			PPackageProd pakprod= pPackageProdDao.findByKey(pgu.getPackage_group_id());
			for(String prod_id: pakprod.getProd_list().split(",")){
				if(StringHelper.isEmpty(prod_id)){continue;}
				PProd prod=pProdDao.findByKey(prod_id);
				for(String user_id: pgu.getUserSelectList()){
					if(SystemConstants.PROD_SERV_ID_BAND.equals(prod.getServ_id())){
						orderCancelList.addAll(cProdOrderDao.queryNotExpOrderByBand(user_id));
					}else{
						orderCancelList.addAll(cProdOrderDao.queryNotExpOrderByProd(user_id, prod_id));
					}
				}
			
			}
		}
		return orderCancelList;
	}
	/**
	 * 升级订购覆盖的情况 提取被退订的订单，并结算可退余额
	 */
	private List<CProdOrder> queryTransferFeeByUpProd(OrderProd orderProd)throws Exception{
		PProd prod=pProdDao.findByKey(orderProd.getProd_id());
		if(prod.getProd_type().equals(SystemConstants.PROD_TYPE_BASE)){
			//宽带升级
			if(SystemConstants.PROD_SERV_ID_BAND.equals(prod.getServ_id())){
				CProdOrder prodOrder=cProdOrderDao.findByKey(orderProd.getLast_order_sn());
				return cProdOrderDao.queryNotExpOrderByBand(prodOrder.getUser_id());
			}else{
				throw new ServicesException("非宽带单产品不能升级");
			}
		}else{
			//套餐升级
			return cProdOrderDao.queryNotExpPackageOrder(orderProd.getCust_id());
		}
	}
	/**
	 * 生成订单信息
	 */
	public CProdOrder createCProdOrder(OrderProd orderProd,
			Integer done_code,String optr_id,String area_id,String county_id) throws Exception{
		CProdOrder prod=new CProdOrder();
		prod.setOrder_sn(cProdOrderDao.findSequence().toString());
		prod.setCust_id(orderProd.getCust_id());
		prod.setUser_id(orderProd.getUser_id());
		prod.setProd_id(orderProd.getProd_id());
		//资费处理
		String[] tariffSplit=orderProd.getTariff_id().split("_");
		if(tariffSplit.length>2){
			throw new ComponentException("OrderProd的资费格式错误");
		}
		prod.setTariff_id(tariffSplit[0]);
		if(tariffSplit.length==2){
			prod.setDisct_id(tariffSplit[1]);
		}
		prod.setOrder_months(orderProd.getOrder_months());
		prod.setOrder_fee(orderProd.getPay_fee()+orderProd.getTransfer_fee());
		//状态要特殊处理
		prod.setStatus(StatusConstants.ACTIVE);
		prod.setStatus_date(new Date());
		prod.setBill_fee(0);
		prod.setActive_fee(orderProd.getPay_fee()+orderProd.getTransfer_fee());
		prod.setEff_date(orderProd.getEff_date());
		prod.setExp_date(orderProd.getExp_date());
		prod.setDone_code(done_code);
		prod.setOptr_id(optr_id);
		prod.setArea_id(area_id);
		prod.setCounty_id(county_id);
		prod.setCreate_time(new Date());
		prod.setPublic_acctitem_type(SystemConstants.PUBLIC_ACCTITEM_TYPE_NONE);
		prod.setOrder_type(SystemConstants.PROD_ORDER_TYPE_ORDER);
		return prod;
	}
	
	
	
	/**
	 * 创建新订购记录产品状态判断
	 * @param orderProd
	 * @return
	 * @throws Exception 
	 */
	public String getNewOrderProdStatus(CProdOrder lastOrder,OrderProd orderProd) throws Exception{
		if(lastOrder!=null){
			//有上期订单的情况，返回上期订单状态
			return lastOrder.getStatus();
		}else if(StringHelper.isNotEmpty(orderProd.getUser_id())){
			//单产品的情况：状态跟用户一致
			return cUserDao.findByKey(orderProd.getUser_id()).getStatus();
		}else{
			if(orderProd.getGroupSelected()!=null&&orderProd.getGroupSelected().size()>0){
				//套餐的情况: 根据用户选择的情况判断,选中的用户有非正常状态，则返回用户的非正常状态作为产品状态
				Map<String,CUser> userMap=new HashMap<String,CUser>();
				for(CUser user: cUserDao.queryUserByCustId(orderProd.getCust_id())){
					userMap.put(user.getUser_id(), user);
				}
				for(PackageGroupUser pgu: orderProd.getGroupSelected()){
					if(pgu.getUserSelectList()!=null){
						for(String user_id:pgu.getUserSelectList()){
							if(!userMap.get(user_id).getStatus().equals(StatusConstants.ACTIVE)){
								return userMap.get(user_id).getStatus();
							}
						}
					}
				}
			}
		}
		return StatusConstants.ACTIVE;
	}
	
	/**
	 * 保存订购记录
	 * @return
	 * @throws Exception 
	 */
	public String saveCProdOrder(CProdOrder cProdOrder,OrderProd orderProd,String busi_code) throws Exception{
		
		//保存订单
		cProdOrderDao.save(cProdOrder);
		
		//记录创建订单的原始状态
		cProdStatusChangeDao.saveStatusChange(cProdOrder.getDone_code(), cProdOrder.getOrder_sn(), cProdOrder.getStatus());
		//保存套餐的子订单
		if(orderProd.getGroupSelected()!=null&&orderProd.getGroupSelected().size()>0){
			savePackageUserProd(cProdOrder,orderProd);
		}
		
		//覆盖退订并转移支付余额到新订购记录
		int transfee=0;
		List<CProdOrder> cacleOrderList=this.queryTransCancelOrderList(orderProd, busi_code);
		if(cacleOrderList!=null&&cacleOrderList.size()>0){
			transfee=saveTransCancelProd(cProdOrder,cacleOrderList,orderProd.getEff_date());
		}
		if(transfee!=orderProd.getTransfer_fee().intValue()){
			throw new ComponentException("转移支付金额不一致，请重新操作!");
		}
		
		return cProdOrder.getOrder_sn();
	}
	
	/**
	 * 覆盖退订产品
	 * 未支付的产品不算转移支付余额
	 * @param cProdOrder
	 * @param cancelList
	 * @param cancelDate
	 * @return
	 * @throws Exception
	 */
	private int saveTransCancelProd(CProdOrder cProdOrder,List<CProdOrder> cancelList,Date cancelDate) throws Exception{
		int transFee=0;
		List<CProdOrderTransfee>  transFeeList=new ArrayList<>();
		for(CProdOrder cancelOrder:cancelList){
			CProdOrderTransfee trans=new CProdOrderTransfee();
			
			trans.setDone_code(cProdOrder.getDone_code());
			trans.setCust_id(cProdOrder.getCust_id());
			trans.setOrder_sn(cProdOrder.getOrder_sn());
			trans.setFrom_cust_id(cancelOrder.getCust_id());
			trans.setFrom_order_sn(cancelOrder.getOrder_sn());
			//TODO 有充值卡业务时要修改
			trans.setFee_type(SystemConstants.ACCT_FEETYPE_CASH);
			if(cancelOrder.getIs_pay().equals(SystemConstants.BOOLEAN_TRUE)){
				int fee=this.getTransCancelFee(cancelDate, cancelOrder);
				trans.setBalance(fee);
				transFee=transFee+fee;
				transFeeList.add(trans);
				
				cancelOrder.setActive_fee(fee);
			}
			//退订订购记录
			this.saveCancelProdOrder(cancelOrder,cProdOrder.getDone_code());
		}
		//保存转移支付记录
		cProdOrderTransfeeDao.save(transFeeList.toArray(new CProdOrderTransfee[transFeeList.size()]));
		
		return transFee;
	}
	
	/**
	 * 取消订购记录
	 * 财务账单计费相关
	 * @param order
	 * @throws Exception 
	 */
	public List<CProdOrder> saveCancelProdOrder(CProdOrder order,Integer done_code) throws Exception{
		//map<
		List<CProdOrder> cancelResultList=new ArrayList<>();
		
		PProd prod=pProdDao.findByKey(order.getProd_id());
		CProdOrderHis deleteOrder=new CProdOrderHis();
		BeanHelper.copyProperties(deleteOrder, order);
		deleteOrder.setDelete_done_code(done_code);
		deleteOrder.setDelete_time(new Date());
		cProdOrderHisDao.save(deleteOrder);
		cProdOrderDao.remove(deleteOrder.getOrder_sn());
		
		//财务出账相关退订状态设置为失效
		cProdStatusChangeDao.saveStatusChange(done_code, order.getOrder_sn(), StatusConstants.INVALID);
		
		cancelResultList.add(order);
		
		if(!prod.getProd_type().equals(SystemConstants.PROD_TYPE_BASE)){
			//套餐的情况，要退子产品
			List<CProdOrderHis> pakDetailList=new ArrayList<>();
			List<String>  detailSnList=new ArrayList<>();
			for(CProdOrder pakdetail: cProdOrderDao.queryPakDetailOrder(order.getOrder_sn())){
				CProdOrderHis pakdetailhis=new CProdOrderHis();
				BeanHelper.copyProperties(pakdetailhis, pakdetail);
				pakdetailhis.setDelete_done_code(done_code);
				pakdetailhis.setDelete_time(new Date());
				
				pakDetailList.add(pakdetailhis);
				detailSnList.add(pakdetail.getOrder_sn());
				
				cancelResultList.add(pakdetail);
			}
			cProdOrderHisDao.save(pakDetailList.toArray(new CProdOrderHis[pakDetailList.size()]));
			cProdOrderDao.remove(detailSnList.toArray(new String[detailSnList.size()]));
		}
		
		return cancelResultList;
	}
	/**
	 * 保存套餐子产品
	 * @throws InvocationTargetException 
	 * @throws Exception 
	 */
	private void savePackageUserProd(CProdOrder cProdOrder,OrderProd orderProd) throws Exception{
		List<CProdOrder> orderList=new ArrayList<>();
		Map<String,CUser> userMap=CollectionHelper.converToMapSingle(cUserDao.queryUserByCustId(cProdOrder.getCust_id()),"user_id");
		
		for(PackageGroupUser pgu: orderProd.getGroupSelected()){
			if(pgu.getUserSelectList()==null){
				continue;
			}
			PPackageProd pakprod= pPackageProdDao.findByKey(pgu.getPackage_group_id());
			if(!pakprod.getPackage_id().equals(cProdOrder.getProd_id())){
				throw new ServicesException(ErrorCode.OrderDatePackageConfig);
			}
			if(pgu.getUserSelectList().size()>pakprod.getMax_user_cnt()){
				throw new ServicesException(ErrorCode.OrderDatePackageUserLimit);
			}
			for(String prod_id:pakprod.getProd_list().split(",")){
				if(StringHelper.isNotEmpty(prod_id)){
					for(String user_id: pgu.getUserSelectList()){
						CUser user=userMap.get(user_id);
						if(user==null){
							//用户存在检查
							throw new ServicesException(ErrorCode.OrderDateUserNotCust,user_id);
						}
						if(!user.getUser_type().equals(pakprod.getUser_type())){
							//用户适用检查
							throw new ServicesException(ErrorCode.OrderDateUserNotCust,user_id);
						}
						if(StringHelper.isNotEmpty(pakprod.getTerminal_type())
								&&!pakprod.getTerminal_type().equals(user.getTerminal_type())){
							//用户适用检查
							throw new ServicesException(ErrorCode.OrderDateUserNotCust,user_id);
						}
						CProdOrder order=new CProdOrder();
						//copy
						BeanHelper.copyProperties(order, cProdOrder);
						order.setOrder_sn(cProdOrderDao.findSequence().toString());
						order.setProd_id(prod_id);
						order.setUser_id(user_id);
						order.setPackage_id(cProdOrder.getProd_id());
						order.setPackage_sn(cProdOrder.getOrder_sn());
						order.setPackage_group_id(pgu.getPackage_group_id());
						order.setOrder_fee(0);
						order.setActive_fee(0);
						orderList.add(order);
					}
				}
			}
		}
		if(orderList.size()>0){
			cProdOrderDao.save(orderList.toArray(new CProdOrder[orderList.size()]));
		}
	}
	
	/**
	 * 修改产品信息
	 * @param doneCode
	 * @param orderSn
	 * @param propChangeList
	 * @throws Exception
	 */
	public void editOrder(Integer doneCode,String orderSn,List<CProdPropChange> propChangeList) throws Exception{
		if(propChangeList == null || propChangeList.size() == 0) return ;
		CProdOrder order = new CProdOrder();
		order.setOrder_sn(orderSn);
		for (CProdPropChange change:propChangeList){
			if (change.getColumn_name().indexOf("date")>-1){
				if (change.getNew_value().length() ==10)
					BeanHelper.setProperty(order, change.getColumn_name(), 
							DateHelper.parseDate(change.getNew_value(), DateHelper.FORMAT_YMD));
				else 
					BeanHelper.setProperty(order, change.getColumn_name(), 
							DateHelper.parseDate(change.getNew_value(), DateHelper.FORMAT_TIME));
			} else {
				BeanHelper.setProperty(order, change.getColumn_name(), change
						.getNew_value());
			}
			

			change.setProd_sn(orderSn);
			change.setDone_code(doneCode);
			change.setArea_id(getOptr().getArea_id());
			change.setCounty_id(getOptr().getCounty_id());
		}
		//修改产品信息
		cProdOrderDao.update(order);
		
		
		//保存产品异动信息
		cProdPropChangeDao.save(propChangeList.toArray(new CProdPropChange[propChangeList.size()]));
	}

	
	/**
	 * 产品订购面板产品数据初始化加载
	 * @param busiCode
	 * @param custId
	 * @param userId
	 * @param filterOrderSn
	 * @return
	 * @throws Exception
	 */
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
				throw new ServicesException(ErrorCode.OrderDateCanNotUp);
			}
		} 
		
		
		return panel;
	}

	//查找用户能够订购的单产品
	private void queryUserOrderableProd(CCust cust,String userId,OrderProdPanel panel,List<CProdOrderDto> orderList) throws Exception {
		CUser user = cUserDao.findByKey(userId);
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
			throw new ServicesException(ErrorCode.ProdIsInvalid);
		}
		CUser user = null;
		CProdOrder lastOrder = null;
		if (StringHelper.isNotEmpty(order.getUser_id())){
			user = cUserDao.findByKey(order.getUser_id());
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
	/**
	 * 查找客户用户适用的资费和优惠
	 * @throws Exception
	 */
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
		
}
