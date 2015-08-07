package com.ycsoft.business.component.core;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLEngineResult.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.prod.CProdOrder;
import com.ycsoft.beans.core.prod.CProdOrderDto;
import com.ycsoft.beans.core.prod.CProdOrderHis;
import com.ycsoft.beans.core.prod.CProdOrderTransfee;
import com.ycsoft.beans.core.prod.CProdPropChange;
import com.ycsoft.beans.core.prod.CProdStatusChange;
import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.beans.prod.PPackageProd;
import com.ycsoft.beans.prod.PProd;
import com.ycsoft.business.commons.abstracts.BaseBusiComponent;
import com.ycsoft.business.dao.core.prod.CProdOrderDao;
import com.ycsoft.business.dao.core.prod.CProdOrderHisDao;
import com.ycsoft.business.dao.core.prod.CProdOrderTransfeeDao;
import com.ycsoft.business.dao.core.prod.CProdStatusChangeDao;
import com.ycsoft.business.dao.core.user.CUserDao;
import com.ycsoft.business.dao.prod.PPackageProdDao;
import com.ycsoft.business.dao.prod.PProdDao;
import com.ycsoft.business.dto.core.prod.OrderProd;
import com.ycsoft.business.dto.core.prod.PackageGroupUser;
import com.ycsoft.commons.constants.BusiCodeConstants;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ComponentException;
import com.ycsoft.commons.exception.ErrorCode;
import com.ycsoft.commons.exception.ServicesException;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.commons.helper.DateHelper;
import com.ycsoft.commons.helper.StringHelper;
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
	 * 计算一个订单的可退金额（终止退订和销户退订）
	 * @param order
	 * @return
	 */
	public Integer getOrderCancelFee(CProdOrder cancelOrder){
		if(StringHelper.isNotEmpty(cancelOrder.getPackage_sn())){
			//套餐子产品可退金额=0;
			return 0;
		}
		Date cancelDate=DateHelper.today();
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
		if(busi_code.equals(BusiCodeConstants.PROD_PACKAGE_ORDER)&&StringHelper.isEmpty(orderProd.getLast_order_sn())
				&&orderProd.getGroupSelected()!=null&&orderProd.getGroupSelected().size()>0){
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
			if(cProdOrder.getIs_pay().equals(SystemConstants.BOOLEAN_TRUE)){
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
								&&pakprod.getTerminal_type().equals(user.getTerminal_type())){
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
		
}
