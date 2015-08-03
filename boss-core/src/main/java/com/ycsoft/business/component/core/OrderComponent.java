package com.ycsoft.business.component.core;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ycsoft.beans.core.prod.CProdOrder;
import com.ycsoft.beans.core.prod.CProdOrderHis;
import com.ycsoft.beans.core.prod.CProdOrderTransfee;
import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.beans.prod.PPackageProd;
import com.ycsoft.beans.prod.PProd;
import com.ycsoft.business.commons.abstracts.BaseBusiComponent;
import com.ycsoft.business.dao.core.prod.CProdOrderDao;
import com.ycsoft.business.dao.core.prod.CProdOrderHisDao;
import com.ycsoft.business.dao.core.prod.CProdOrderTransfeeDao;
import com.ycsoft.business.dao.core.user.CUserDao;
import com.ycsoft.business.dao.prod.PPackageProdDao;
import com.ycsoft.business.dao.prod.PProdDao;
import com.ycsoft.business.dto.core.prod.OrderProd;
import com.ycsoft.business.dto.core.prod.PackageGroupUser;
import com.ycsoft.commons.constants.BusiCodeConstants;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ComponentException;
import com.ycsoft.commons.exception.ServicesException;
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
						orderCancelList.addAll(cProdOrderDao.queryTransOrderByBand(user_id));
					}else{
						orderCancelList.addAll(cProdOrderDao.queryTransOrderByProd(user_id, prod_id));
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
				return cProdOrderDao.queryTransOrderByBand(prodOrder.getUser_id());
			}else{
				throw new ServicesException("非宽带单产品不能升级");
			}
		}else{
			//套餐升级
			return cProdOrderDao.queryTransOrderByPackage(orderProd.getCust_id());
		}
	}
	
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
		prod.setRent_fee(0);
		prod.setEff_date(orderProd.getEff_date());
		prod.setExp_date(orderProd.getExp_date());
		prod.setLast_bill_date(orderProd.getEff_date());
		prod.setNext_bill_date(orderProd.getEff_date());
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
	
	private int saveTransCancelProd(CProdOrder cProdOrder,List<CProdOrder> cancelList,Date cancelDate) throws Exception{
		int transFee=0;
		List<CProdOrderTransfee>  transFeeList=new ArrayList<>();
		for(CProdOrder cancelOrder:cancelList){
			CProdOrderTransfee trans=new CProdOrderTransfee();
			transFeeList.add(trans);
			trans.setDone_code(cProdOrder.getDone_code());
			trans.setCust_id(cProdOrder.getCust_id());
			trans.setOrder_sn(cProdOrder.getOrder_sn());
			trans.setFrom_cust_id(cancelOrder.getCust_id());
			trans.setFrom_order_sn(cancelOrder.getOrder_sn());
			//TODO 有充值卡业务时要修改
			trans.setFee_type(SystemConstants.ACCT_FEETYPE_CASH);
			int fee=this.getTransCancelFee(cancelDate, cancelOrder);
			trans.setBalance(fee);
			transFee=transFee+fee;
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
	public void saveCancelProdOrder(CProdOrder order,Integer done_code) throws Exception{
		PProd prod=pProdDao.findByKey(order.getProd_id());
		CProdOrderHis deleteOrder=new CProdOrderHis();
		BeanHelper.copyProperties(deleteOrder, order);
		deleteOrder.setDelete_done_code(done_code);
		deleteOrder.setDelete_time(new Date());
		cProdOrderHisDao.save(deleteOrder);
		cProdOrderDao.remove(deleteOrder.getOrder_sn());
		
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
			}
			cProdOrderHisDao.save(pakDetailList.toArray(new CProdOrderHis[pakDetailList.size()]));
			cProdOrderDao.remove(detailSnList.toArray());
		}
		
		//TODO 财务账单处理
		
	}
	/**
	 * 保存套餐子产品
	 * @throws InvocationTargetException 
	 * @throws Exception 
	 */
	private void savePackageUserProd(CProdOrder cProdOrder,OrderProd orderProd) throws Exception{
		List<CProdOrder> orderList=new ArrayList<>();
		for(PackageGroupUser pgu: orderProd.getGroupSelected()){
			if(pgu.getUserSelectList()==null){
				continue;
			}
			PPackageProd pakprod= pPackageProdDao.findByKey(pgu.getPackage_group_id());
			for(String prod_id:pakprod.getProd_list().split(",")){
				if(StringHelper.isNotEmpty(prod_id)){
					for(String user_id: pgu.getUserSelectList()){
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
		
}
