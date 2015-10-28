package com.ycsoft.business.component.core;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TAcctFeeType;
import com.ycsoft.beans.config.TRuleDefine;
import com.ycsoft.beans.core.cust.CCust;
import com.ycsoft.beans.core.prod.CProdOrder;
import com.ycsoft.beans.core.prod.CProdOrderDto;
import com.ycsoft.beans.core.prod.CProdOrderFee;
import com.ycsoft.beans.core.prod.CProdOrderFeeOut;
import com.ycsoft.beans.core.prod.CProdOrderFollowPay;
import com.ycsoft.beans.core.prod.CProdOrderHis;
import com.ycsoft.beans.core.prod.CProdPropChange;
import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.beans.prod.PPackageProd;
import com.ycsoft.beans.prod.PProd;
import com.ycsoft.beans.prod.PProdTariff;
import com.ycsoft.beans.prod.PProdTariffDisct;
import com.ycsoft.business.commons.abstracts.BaseBusiComponent;
import com.ycsoft.business.component.config.ExpressionUtil;
import com.ycsoft.business.dao.config.TAcctFeeTypeDao;
import com.ycsoft.business.dao.config.TPayTypeDao;
import com.ycsoft.business.dao.config.TRuleDefineDao;
import com.ycsoft.business.dao.core.cust.CCustDao;
import com.ycsoft.business.dao.core.prod.CProdOrderDao;
import com.ycsoft.business.dao.core.prod.CProdOrderFeeDao;
import com.ycsoft.business.dao.core.prod.CProdOrderFeeOutDao;
import com.ycsoft.business.dao.core.prod.CProdOrderHisDao;
import com.ycsoft.business.dao.core.prod.CProdStatusChangeDao;
import com.ycsoft.business.dao.core.user.CUserDao;
import com.ycsoft.business.dao.prod.PPackageProdDao;
import com.ycsoft.business.dao.prod.PProdDao;
import com.ycsoft.business.dao.prod.PProdTariffDisctDao;
import com.ycsoft.business.dto.core.fee.FeeDto;
import com.ycsoft.business.dto.core.prod.OrderProd;
import com.ycsoft.business.dto.core.prod.OrderProdEdit;
import com.ycsoft.business.dto.core.prod.OrderProdPanel;
import com.ycsoft.business.dto.core.prod.PackageGroupUser;
import com.ycsoft.business.dto.core.prod.ProdTariffDto;
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
	private CProdStatusChangeDao cProdStatusChangeDao;
	@Autowired
	private TRuleDefineDao tRuleDefineDao;
	@Autowired
	private CCustDao cCustDao;
	@Autowired
	private PProdTariffDisctDao pProdTariffDisctDao;
	@Autowired
	private BeanFactory beanFactory;
	@Autowired
	private CProdOrderFeeDao cProdOrderFeeDao;
	@Autowired
	private TAcctFeeTypeDao tAcctFeeTypeDao;
	@Autowired
	private TPayTypeDao tPayTypeDao;
	@Autowired
	private CProdOrderFeeOutDao cProdOrderFeeOutDao;
	/**
	 * 订单编辑的基础数据
	 * @param order
	 * @return
	 * @throws Exception
	 */
	public OrderProdEdit  createOrderEdit(CProdOrderDto order) throws Exception{
		OrderProdEdit edit=new OrderProdEdit();
		edit.setLast_order_sn(order.getOrder_sn());
		edit.setCust_id(order.getCust_id());
		edit.setUser_id(order.getUser_id());
		edit.setExp_date(order.getExp_date());
		edit.setEff_date(order.getEff_date());
		edit.setProd_id(order.getProd_id());
		if(StringHelper.isNotEmpty(order.getDisct_id())){
			edit.setTariff_id(order.getTariff_id()+"_"+order.getDisct_id());
		}else{
			edit.setTariff_id(order.getTariff_id());
		}
		edit.setOrder_months(order.getOrder_months());
		edit.setOld_order_fee(order.getOrder_fee());
		//转移支付费用
		int transFee=0;
		for(CProdOrderFee orderFee:cProdOrderFeeDao.queryByOrderSn(order.getOrder_sn())){
			if(orderFee.getInput_type().equals(SystemConstants.ORDER_FEE_TYPE_TRANSFEE)){
				transFee=transFee+orderFee.getFee();
			}
		}
		edit.setOld_transfer_fee(transFee);
		//套餐子产品的选择情况,在编辑面板异步加载
		if(!order.getProd_type().equals(SystemConstants.PROD_TYPE_BASE)){
			
			Map<String,Set<String>> groupUserMap=new HashMap<>();
			for(CProdOrder detail: cProdOrderDao.queryPakDetailOrder(order.getOrder_sn())){
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
			edit.setGroupSelected(groupSelected);
		}
		return edit;
	}
	/**
	 * 订单编辑可以使用的产品和资费
	 * @param cust
	 * @param filterOrderSn
	 * @param panel
	 * @throws Exception
	 */
	public void queryOrderableEdit(CCust cust,String filterOrderSn,OrderProdEdit panel) throws Exception{
		CProdOrderDto order = cProdOrderDao.queryCProdOrderDtoByKey(filterOrderSn);
		if (order == null)
			return ;
		if(order.getProd_type().equals(SystemConstants.PROD_TYPE_BASE)
				&&!order.getServ_id().equals(SystemConstants.PROD_SERV_ID_BAND)){
			//非宽带单产品处理 (参考 单产品续订)
			CUser user = cUserDao.findByKey(order.getUser_id());
			panel.setUserDesc(getUserDesc(user));
			PProd prod= pProdDao.findByKey(order.getProd_id());
			//单产品的情况
			List<PProdTariffDisct> tariffList = this.queryTariffList(cust,user, prod);
			if (!CollectionHelper.isEmpty(tariffList)){
				panel.getProdList().add(prod);
				panel.getTariffMap().put(prod.getProd_id(), tariffList);
			}
		}else if(order.getProd_type().equals(SystemConstants.PROD_TYPE_BASE)
				&&order.getServ_id().equals(SystemConstants.PROD_SERV_ID_BAND)){
			//宽带单产品处理 (参考 单用户订购宽带)
			CUser user = cUserDao.findByKey(order.getUser_id());
			panel.setUserDesc(getUserDesc(user));
			List<PProd> prodList = pProdDao.queryCanOrderUserProd(user.getUser_type(), user.getCounty_id(),
					user.getCounty_id(), SystemConstants.DEFAULT_DATA_RIGHT);
			for (PProd prod:prodList){
				List<PProdTariffDisct> tariffList = this.queryTariffList(cust,user, prod);
				if (!CollectionHelper.isEmpty(tariffList)){
					panel.getProdList().add(prod);
					panel.getTariffMap().put(prod.getProd_id(), tariffList);
				}
			}
		}else if(!order.getProd_type().equals(SystemConstants.PROD_TYPE_BASE)){
			//套餐处理(参考  套餐订购)
			String custId = cust.getCust_id();
			Map<String,Integer> userCountMap = cUserDao.queryUserCountGroupByType(custId);
			List<PProd> prodList = pProdDao.queryCanOrderPkg(cust.getCounty_id(),  SystemConstants.DEFAULT_DATA_RIGHT);
			
			for (PProd prod:prodList){
				if(!prod.getProd_type().equals(order.getProd_type())){
					continue;
				}
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
					}
				}
			}
		}
	}
	
	/**
	 * 保存订单修改信息
	 */
	public List<CProdOrder> saveOrderEditBean(CProdOrderDto order,Integer done_code,OrderProd orderProd) throws Exception{
		
		List<CProdOrder> changeOrderList=new ArrayList<>();
		cProdOrderDao.updateOrderEdit(order);
		//套餐更新子产品
		if(!order.getProd_type().equals(SystemConstants.PROD_TYPE_BASE)){
			//原来的删除
			List<CProdOrderHis> pakDetailList=new ArrayList<>();
			List<String>  detailSnList=new ArrayList<>();
			for(CProdOrder pakdetail: cProdOrderDao.queryPakDetailOrder(order.getOrder_sn())){
				CProdOrderHis pakdetailhis=new CProdOrderHis();
				BeanHelper.copyProperties(pakdetailhis, pakdetail);
				pakdetailhis.setDelete_done_code(done_code);
				pakdetailhis.setDelete_time(new Date());
				
				pakDetailList.add(pakdetailhis);
				detailSnList.add(pakdetail.getOrder_sn());
				
				changeOrderList.add(pakdetail);
			}
			cProdOrderHisDao.save(pakDetailList.toArray(new CProdOrderHis[pakDetailList.size()]));
			cProdOrderDao.remove(detailSnList.toArray(new String[detailSnList.size()]));
			//插入新的
			if(orderProd.getGroupSelected()!=null&&orderProd.getGroupSelected().size()>0){
				changeOrderList.addAll(savePackageUserProd(order,orderProd));
			}
		}
		return changeOrderList;
	}
	/**
	 * 处理订单修改的费用记录
	 * @param cProdorder
	 * @param payFee
	 * @param feeSn
	 * @param doneCode
	 * @throws Exception
	 */
	public void saveOrderEditFee(CProdOrder cProdorder,Integer payFee,String feeSn,Integer doneCode) throws  Exception{
		if(payFee<0){
			//退款的情况
			//取INPUT_TYPE=CFEE的记录进行扣费
			List<CProdOrderFee> orderFeeOuts=new ArrayList<>();
			int tempFee=payFee*-1;
			for(CProdOrderFee orderFee:cProdOrderFeeDao.queryByOrderSn(cProdorder.getOrder_sn())){
				if(tempFee<=0) break;
				if(orderFee.getInput_type().equals(SystemConstants.ORDER_FEE_TYPE_CFEE)){
					int _kf=0;//扣费金额
					if(tempFee>orderFee.getFee()){
						_kf=orderFee.getFee();
					}else{
						_kf=tempFee;
					}
					tempFee=tempFee-_kf;
					orderFee.setOutput_fee(_kf);
					orderFee.setOutput_type(SystemConstants.ORDER_FEE_TYPE_CFEE);
					orderFee.setOutput_sn(feeSn);
					orderFeeOuts.add(orderFee);
				}
			}
			if(tempFee>0){//金额不足，数据有问题
				throw new ServicesException(ErrorCode.OrderDateFeeError);
			}
			this.saveOrderFeeOut(this.getOrderFeeOutFromOrderFee(orderFeeOuts), doneCode);
		}
		if(payFee>0){
			//补收费用的情况
			//插入CORDERFEE
			CProdOrderFee inputOrderFee=new CProdOrderFee();
			inputOrderFee.setOrder_fee_sn(cProdOrderFeeDao.findSequence().toString());
			inputOrderFee.setOrder_sn(cProdorder.getOrder_sn());
			inputOrderFee.setDone_code(doneCode);
			inputOrderFee.setInput_type(SystemConstants.ORDER_FEE_TYPE_CFEE);
			inputOrderFee.setInput_sn(feeSn);
			inputOrderFee.setInput_fee(payFee);
			inputOrderFee.setFee(payFee);
			inputOrderFee.setFee_type( StatusConstants.UNPAY);
			inputOrderFee.setCounty_id(cProdorder.getCounty_id());
			inputOrderFee.setArea_id(cProdorder.getArea_id());
			inputOrderFee.setCreate_time(new Date());
			cProdOrderFeeDao.save(inputOrderFee);
			
		}
	}
	/**
	 * 记录订单修改异动
	 * @param orderProd
	 * @param order
	 * @throws Exception 
	 */
	public List<CProdPropChange> saveOrderEditChange(CProdOrder order,CProdOrder editOrder,Integer done_code) throws Exception{
		List<CProdPropChange> changeList=new ArrayList<>();
		
		//产品有无变化
		if(!order.getProd_id().equals(editOrder.getProd_id())){
			CProdPropChange change=new CProdPropChange();
			changeList.add(change);
			BeanHelper.copyProperties(change, order);
			change.setDone_code(done_code);
			change.setProd_sn(order.getOrder_sn());
			change.setColumn_name("prod_id");
			change.setOld_value(order.getProd_id());
			change.setNew_value(editOrder.getProd_id());
			
			order.setProd_id(editOrder.getProd_id());
		}
		//资费有无变化
		if(!order.getTariff_id().equals(editOrder.getTariff_id())){
			CProdPropChange change=new CProdPropChange();
			changeList.add(change);
			BeanHelper.copyProperties(change, order);
			change.setDone_code(done_code);
			change.setProd_sn(order.getOrder_sn());
			change.setColumn_name("tariff_id");
			change.setOld_value(order.getTariff_id());
			change.setNew_value(editOrder.getTariff_id());
			
			order.setTariff_id(editOrder.getTariff_id());
		}
		//折扣有无变化
		if((order.getDisct_id()==null&&editOrder.getDisct_id()==null)|| 
				(order.getDisct_id()!=null&&order.getDisct_id().equals(editOrder.getDisct_id()))){
			//相等的情况,不处理
		}else{
			CProdPropChange change=new CProdPropChange();
			changeList.add(change);
			BeanHelper.copyProperties(change, order);
			change.setDone_code(done_code);
			change.setProd_sn(order.getOrder_sn());
			change.setColumn_name("disct_id");
			change.setOld_value(order.getDisct_id());
			change.setNew_value(editOrder.getDisct_id());
			
			order.setDisct_id(editOrder.getDisct_id());
		}
		//失效日期有误变化
		if(!order.getExp_date().equals(editOrder.getExp_date())){
			CProdPropChange change=new CProdPropChange();
			changeList.add(change);
			BeanHelper.copyProperties(change, order);
			change.setDone_code(done_code);
			change.setProd_sn(order.getOrder_sn());
			change.setColumn_name("exp_date");
			change.setOld_value(DateHelper.dateToStr(order.getExp_date()));
			change.setNew_value(DateHelper.dateToStr(editOrder.getExp_date()));
			
			order.setExp_date(editOrder.getExp_date());
		}
		//订购月数有无变化
		if(!order.getOrder_months().equals(editOrder.getOrder_months())){
			CProdPropChange change=new CProdPropChange();
			changeList.add(change);
			BeanHelper.copyProperties(change, order);
			change.setDone_code(done_code);
			change.setProd_sn(order.getOrder_sn());
			change.setColumn_name("order_months");
			change.setOld_value(order.getOrder_months().toString());
			change.setNew_value(editOrder.getOrder_months().toString());
			
			order.setOrder_months(editOrder.getOrder_months());
		}
		//订购金额有无变化
		if(!order.getOrder_fee().equals(editOrder.getOrder_fee())){
			CProdPropChange change=new CProdPropChange();
			changeList.add(change);
			BeanHelper.copyProperties(change, order);
			change.setDone_code(done_code);
			change.setProd_sn(order.getOrder_sn());
			change.setColumn_name("order_fee");
			change.setOld_value(order.getOrder_fee().toString());
			change.setNew_value(editOrder.getOrder_fee().toString());
			
			order.setOrder_fee(editOrder.getOrder_fee());
		}
		cProdPropChangeDao.save(changeList.toArray(new CProdPropChange[changeList.size()]));
		return changeList;
	}
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
	 * 查询一个客户有效的订单和最近的订单
	 * @param custId
	 * @throws Exception 
	 */
	public List<CProdOrderDto> queryCustEFFAndLastOrder(String custId) throws Exception{
		List<CProdOrderDto> orderList=new ArrayList<>();
		List<CProdOrderDto> list=cProdOrderDao.queryCustAllOrderDto(custId);
		Collections.reverse(list);//倒序
		//记录订单是否已存在
		Set<String> existsSet=new HashSet<String>();
		Date today=DateHelper.today();
		for(CProdOrderDto order:list){
			if(order.getStatus().equals(StatusConstants.REQSTOP)
					||order.getStatus().equals(StatusConstants.LINKSTOP)
					||order.getStatus().equals(StatusConstants.INSTALL)){
				//报停和关联停且订单失效日期在状态
				orderList.add(order);
				existsSet.add(getOrderKey(order));
			}else if(!order.getExp_date().before(today)){
				orderList.add(order);
				existsSet.add(getOrderKey(order));
			}else if(!existsSet.contains(getOrderKey(order))){
				orderList.add(order);
				existsSet.add(getOrderKey(order));
			}
		}
		Collections.reverse(orderList);
		return orderList;
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
	
	public List<CProdOrder> queryPakDetailOrder(String package_sn) throws Exception{
		return cProdOrderDao.queryPakDetailOrder(package_sn);
	}
	/**
	 * 销户时订单退款金额计算
	 * @param orderList
	 * @param isHigh
	 * @throws Exception
	 */
	public List<CProdOrderFee> getLogoffOrderFee(List<CProdOrderDto> orderList,boolean isHigh) throws Exception{
	
		List<CProdOrderFee> orderFees=new ArrayList<>();
		for(CProdOrderDto order:orderList){
			if(order.getIs_pay().equals(SystemConstants.BOOLEAN_FALSE)){
				//未支付判断
				throw new ServicesException(ErrorCode.NotCancleHasUnPay);
			}
			List<CProdOrderFee> cancelFeeList=new ArrayList<>();
			if(isHigh){
				//order.setActive_fee(getOrderCancelFee(order,DateHelper.today()));
				cancelFeeList=getOrderCacelFeeDetail(order,DateHelper.today());
			}else if(StringHelper.isNotEmpty(order.getPackage_sn())
					||(order.getBilling_cycle()>1
							&&!DateHelper.isToday(order.getOrder_time())
							&&!order.getStatus().equals(StatusConstants.INSTALL))){
				//套餐子产品和包多月产品，低权限人员退款金额=0
				order.setActive_fee(0);
			}else if(order.getProd_type().equals(SystemConstants.PROD_TYPE_BASE)
					&& order.getIs_base().equals(SystemConstants.BOOLEAN_TRUE)
					&&order.getProtocol_date()!=null
					&&DateHelper.today().before(order.getProtocol_date())){
				//包1月产品在硬件协议期未到时计算退款余额
				//order.setActive_fee(getOrderCancelFee(order,DateHelper.getTruncDate(order.getProtocol_date())));
				cancelFeeList=getOrderCacelFeeDetail(order,DateHelper.getTruncDate(order.getProtocol_date()));				
			}else {
				//order.setActive_fee(getOrderCancelFee(order,DateHelper.today()));
				cancelFeeList=getOrderCacelFeeDetail(order,DateHelper.today());			
			}
			orderFees.addAll(cancelFeeList);
			int outTotalFee=0;
			int balanceCfee=0;
			for(CProdOrderFee orderFee:cancelFeeList){
				outTotalFee=outTotalFee+orderFee.getOutput_fee();
				if(orderFee.getOutput_type().equals(SystemConstants.ORDER_FEE_TYPE_CFEE)){
					balanceCfee=balanceCfee+orderFee.getOutput_fee();
				}
			}
			order.setActive_fee(outTotalFee);
			order.setBalance_cfee(balanceCfee);
			order.setBalance_acct(outTotalFee-balanceCfee);
		}
		return orderFees;
	}
	/**
	 * 是否高级退订或销户功能
	 * @param busi_code
	 * @return
	 */
	public boolean isHighCancel(String busi_code){
		return BusiCodeConstants.PROD_HIGH_TERMINATE.equals(busi_code)
				||BusiCodeConstants.USER_HIGH_WRITE_OFF.equals(busi_code)
				||BusiCodeConstants.PROD_SUPER_TERMINATE.equals(busi_code)
				||BusiCodeConstants.BATCH_USER_WRITE_OFF.equals(busi_code)
				?true:false;
	}
	
	/**
	 * 因为产品退订而重新计算套餐订单的计费时间段（不处理子产品）
	 * @return 返回开始失效时间变化的订单信息
	 * @throws Exception 
	 */
	public List<CProdOrder> movePackageOrderToFollow(String cust_id,Integer done_code) throws Exception{
		return this.changeToFollow(cProdOrderDao.queryNotExpPackageOrderByEff(cust_id), done_code);
	}
	/**
	 * 因为产品退订而重新计算宽带用户订单（含套餐子产品）的计费时间段
	 * @return 返回开始失效时间变化的订单信息
	 * @throws Exception
	 */
	public List<CProdOrder> moveBandOrderToFollow(String user_id,Integer done_code) throws Exception{
		return this.changeToFollow(cProdOrderDao.queryNotExpAllOrderByUserOrderEff(user_id), done_code);
	}
	/**
	 * 因为产品退订而重新计算普通产品订单（含套餐子产品）(非宽带)的计费时间段
	 * @return 返回开始失效时间变化的订单信息
	 * @throws Exception
	 */
	public List<CProdOrder> moveProdOrderToFollow(String user_id,String prod_id,Integer done_code)throws Exception{
		return this.changeToFollow(cProdOrderDao.queryNotExpAllOrderByProdOrderByEff(user_id, prod_id), done_code);
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
			if((start.equals(today)&&!order.getEff_date().after(start))
				||(!start.equals(today)&&start.equals(order.getEff_date()))){
				start=DateHelper.addDate(order.getExp_date(), 1);
			}else{				
				Date eff_date=new Date(start.getTime());
				PProdTariff tariff=pProdTariffDao.findByKey(order.getTariff_id());
				Date exp_date=null;
				if(tariff.getBilling_type().equals(SystemConstants.BILLING_TYPE_MONTH)){
					exp_date=DateHelper.getNextMonthPreviousDay(eff_date, order.getOrder_months().intValue());
				}else if(tariff.getBilling_type().equals(SystemConstants.BILLING_TYPE_DAY)){
					int orderDays= Math.round(order.getOrder_months()*30);
					exp_date=DateHelper.addDate(eff_date, orderDays-1);
				}else{
					continue;
				}
				
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
	
	public List<CProdOrderDto> queryLogoffProdOrderDtoByUserIds(List<String> userIdList) throws Exception {
		return cProdOrderDao.queryProdOrderDtoByUserIdList(userIdList.toArray(new String[userIdList.size()]));
	}
	
	public List<CProdOrder> queryOrderProdByUserId(String user_id) throws Exception {
		return cProdOrderDao.queryOrderProdByUserId(user_id);
	}
	
	public List<CProdOrder> queryNotExpAllOrderByUser(String user_id)throws Exception {
		return cProdOrderDao.queryNotExpAllOrderByUser(user_id);
	}
	
	
	/**
	 * 退出一个所有资金（含已使用部分）明细
	 * @param cancelOrder
	 * @return
	 * @throws Exception
	 */
	public List<CProdOrderFee> getOrderCancelAllFeeDetail(CProdOrderDto cancelOrder) throws Exception{
		List<CProdOrderFee> orderFees=cProdOrderFeeDao.queryByOrderSn(cancelOrder.getOrder_sn());
		
		Map<String,TAcctFeeType> feeTypeMap=CollectionHelper.converToMapSingle( tAcctFeeTypeDao.findAll(), "fee_type");
		int feeTotal=0;
		for(CProdOrderFee orderFee: orderFees){
			feeTotal+=orderFee.getFee();
			orderFee.setRemark(cancelOrder.getProd_name());
			orderFee.setOutput_fee(orderFee.getFee());
			
			if(feeTypeMap.get(orderFee.getFee_type()).getCan_refund().equals(SystemConstants.BOOLEAN_TRUE)){
				orderFee.setOutput_type(SystemConstants.ORDER_FEE_TYPE_CFEE);
			}else{
				orderFee.setOutput_type(SystemConstants.ORDER_FEE_TYPE_ACCT);
			}
		}
		if(feeTotal!=cancelOrder.getOrder_fee().intValue()){
			throw new ComponentException(ErrorCode.OrderFeeDisagree,cancelOrder.getOrder_sn());
		}
		return orderFees;
	}
	/**
	 * 计算一个订单的可退金额的资金明细（产品退订和销户退订）
	 * @param cancelOrder
	 * @param cancelDate
	 * @return
	 * @throws Exception
	 */
	public List<CProdOrderFee> getOrderCacelFeeDetail(CProdOrderDto cancelOrder,Date cancelDate) throws Exception{

		//总退款金额(含可转部分)
		int fee=getMainOrderCancelFee(cancelOrder,cancelDate);
		if(fee>0){
			List<CProdOrderFee> orderFees=cProdOrderFeeDao.queryByOrderSn(cancelOrder.getOrder_sn());
			int totalOrderFee=0;
			for(CProdOrderFee orderFee: orderFees){
				totalOrderFee=totalOrderFee+orderFee.getFee();
				orderFee.setRemark(cancelOrder.getProd_name());
			}
			if(totalOrderFee!=cancelOrder.getOrder_fee().intValue()){
				throw new ComponentException(ErrorCode.OrderFeeDisagree,cancelOrder.getOrder_sn());
			}
			
			Map<String,TAcctFeeType> feeTypeMap=CollectionHelper.converToMapSingle( tAcctFeeTypeDao.findAll(), "fee_type");
			int outTotalFee=fee;
			for(CProdOrderFee orderFee: orderFees){
				int outFee=Math.round(orderFee.getFee()*fee*1.0f/totalOrderFee);
				if(outFee>orderFee.getFee()){
					outFee=orderFee.getFee();
				}
				if(outFee>outTotalFee){
					outFee=outTotalFee;
				}
				outTotalFee=outTotalFee-outFee;
				orderFee.setOutput_fee(outFee);
				
				if(feeTypeMap.get(orderFee.getFee_type()).getCan_refund().equals(SystemConstants.BOOLEAN_TRUE)){
					orderFee.setOutput_type(SystemConstants.ORDER_FEE_TYPE_CFEE);
				}else{
					orderFee.setOutput_type(SystemConstants.ORDER_FEE_TYPE_ACCT);
				}
			}
			if(outTotalFee!=0&&orderFees.size()>0){
				CProdOrderFee orderFee=orderFees.get(orderFees.size()-1);
				orderFee.setOutput_fee(orderFee.getOutput_fee()+outTotalFee);
			}
			return orderFees;
		}else{
			return new ArrayList<>();
		}
	}
	
	/**
	 * 计算一个订单的可退金额（终止退订和销户退订）
	 * 并计算出可退现金部分和可转公用账目部分
	 * @param cancelOrder
	 * @param cancelDate
	 * @return
	 * @throws Exception
	 */
	public Map<String,Integer> getOrderCancelFee(CProdOrderDto cancelOrder,String busi_code,Date cancelDate) throws Exception{
		Map<String,Integer> cancelFeeMap=new HashMap<>();
		cancelFeeMap.put(SystemConstants.ORDER_FEE_TYPE_CFEE, 0);
		cancelFeeMap.put(SystemConstants.ORDER_FEE_TYPE_ACCT, 0);
		List<CProdOrderFee> orderFees=null;
		if(busi_code.equals(BusiCodeConstants.PROD_SUPER_TERMINATE)){
			orderFees=getOrderCancelAllFeeDetail(cancelOrder);
		}else{
			orderFees=getOrderCacelFeeDetail(cancelOrder,cancelDate);
		}
		for(CProdOrderFee orderFee: orderFees){
			cancelFeeMap.put(orderFee.getOutput_type(), orderFee.getOutput_fee()  +cancelFeeMap.get(orderFee.getOutput_type()));
		}
		return cancelFeeMap;
	}
	/**
	 * 计算一个订单的可退金额（终止退订和销户退订）
	 * @param order
	 * @return
	 * @throws Exception 
	 */
	public Integer getMainOrderCancelFee(CProdOrder cancelOrder,Date cancelDate) throws Exception{
		if(StringHelper.isNotEmpty(cancelOrder.getPackage_sn())||cancelOrder.getOrder_fee()==0){
			//套餐子产品可退金额=0;
			return 0;
		}
		//Date cancelDate=DateHelper.today();
		//当天订购或订单状态是施工中
		if(cancelOrder.getStatus().equals(StatusConstants.INSTALL)
				||DateHelper.isToday(cancelOrder.getOrder_time())){
			return cancelOrder.getOrder_fee();
		}
		
		PProdTariff tariff=pProdTariffDao.findByKey(cancelOrder.getTariff_id());
		if(!tariff.getBilling_type().equals(SystemConstants.BILLING_TYPE_MONTH)){
			//非包月计费的订单不退款
			return 0;
		}
		//1.退订日在订购计费完整区间之前
		if(cancelDate.before(cancelOrder.getEff_date())||cancelDate.equals(cancelOrder.getEff_date())){
			//覆盖退订在订购的生效日之前(含)
			return cancelOrder.getOrder_fee();
		}
		//2.退订日在订购计费完整区间之间
		//订购的停止计费日根据订购月数反推开始计费日
		Date expDate=DateHelper.addDate(cancelOrder.getExp_date(), 1);
		Date effDate= DateHelper.getNextMonthByNum(expDate,-1*cancelOrder.getOrder_months().intValue());
		if(cancelDate.equals(effDate)||cancelDate.before(effDate)){
			//退订日包含了整个订单的订购期间
			return cancelOrder.getOrder_fee();
		}
		
		//3.退订日在 订购的计费区间内（按剩余使用天数折算）
		int months=DateHelper.compareToMonthByDate(cancelDate, expDate);
		Date newExpDate=DateHelper.getNextMonthByNum(cancelDate,months);
		if(newExpDate.after(expDate)){
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
	 */
	public List<CProdOrder> recoverTransCancelOrder(Integer recoverDoneCode,String cust_id,Integer doneCode) throws Exception{
		//TODO 查询被覆盖移入历史表的订购记录
		List<CProdOrder> list=cProdOrderHisDao.queryCProdOrderByDelete(recoverDoneCode, cust_id);
		if(list!=null&&list.size()>0){
			//移回正式表
			cProdOrderDao.save(list.toArray(new CProdOrder[list.size()]));
			
			
			for(CProdOrder order:list){
				//订单金额记录  覆盖转出信息恢复
				//cProdOrderFeeDao.clearOutPutInfo(order.getOrder_sn(),SystemConstants.ORDER_FEE_TYPE_TRANSFEE);
				//插入状态异动
				cProdStatusChangeDao.saveStatusChange(doneCode, order.getOrder_sn(), order.getStatus());
			}
			//处理费用转出回退
			List<CProdOrderFeeOut> outList=cProdOrderFeeOutDao.queryByDoneCodeTransFee(recoverDoneCode);
			this.saveOrderFeeOutToBack(outList,doneCode);
			//删除被取消订单的转入记录
			for(CProdOrderFeeOut out:outList){
				cProdOrderFeeDao.remove(out.getOutput_sn());
			}
			String orderSns[]=new String[list.size()];
			for(int i=0;i<list.size();i++){
				orderSns[i]=list.get(i).getOrder_sn();
			}
			//历史记录表移除
			cProdOrderHisDao.remove(orderSns);
			//删除出账状态异动（不能删因为如果非当天取消，可能会造成出账错误）
			//cProdStatusChangeDao.deleteByDoneCode(recoverDoneCode);
		}
		return list;
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
		if(busi_code.equals(BusiCodeConstants.PROD_UPGRADE)){
			if(StringHelper.isEmpty(orderProd.getLast_order_sn())){
				throw new ComponentException(ErrorCode.ParamIsNull);
			}
			//升级的情况
			orderCancelList.addAll(queryTransferFeeByUpProd(orderProd));
		}

		if(orderProd.getGroupSelected()!=null&&orderProd.getGroupSelected().size()>0){
			//套餐订购覆盖普通订购
			orderCancelList.addAll(queryTransferFeeByPackage(orderProd));
		}
		return orderCancelList;
	}
	
	/**
	 * 计算覆盖退订的可退余额(资金明细)
	 * @param cancelDate
	 * @param cancelOrder
	 * @return
	 * @throws Exception 
	 */
	public Integer getTransCancelFee(Date cancelDate,CProdOrder cancelOrder) throws Exception{
		int fee=this.getMainTransCancelFee(cancelDate, cancelOrder);
		//资金明细
		int inputfee=this.getOrderFeeDetailSum(cancelOrder.getOrder_sn());
		if(inputfee!=cancelOrder.getOrder_fee()){
			throw new ComponentException(ErrorCode.OrderFeeDisagree,cancelOrder.getOrder_sn());
		}
		return fee;
	}
	/**
	 * 查询一个订单的来源资金明细总额
	 * @param order_sn
	 * @return
	 * @throws Exception 
	 */
	private Integer getOrderFeeDetailSum(String order_sn) throws Exception{
		int total=0;
		for(CProdOrderFee fee:cProdOrderFeeDao.queryByOrderSn(order_sn)){
			total=fee.getFee()+total;
		}
		return total;
	}
	/**
	 * 计算订购主记录的覆盖退订的可退余额
	 * @param cancelDate
	 * @param cancelOrder
	 * @return
	 * @throws Exception 
	 */
	private Integer getMainTransCancelFee(Date cancelDate,CProdOrder cancelOrder) throws Exception{
		//订单金额为0 或者 订单状态是施工中，直接返回订单金额
		if(cancelOrder.getOrder_fee()==0||cancelOrder.getStatus().equals(StatusConstants.INSTALL)){
			return cancelOrder.getOrder_fee();
		}
		//1.退订日在订购计费完整区间之前
		if(cancelDate.before(cancelOrder.getEff_date())||cancelDate.equals(cancelOrder.getEff_date())){
			//覆盖退订在订购的生效日之前(含)
			return cancelOrder.getOrder_fee();
		}
		//2.退订日在订购计费完整区间之前
		PProdTariff tariff=pProdTariffDao.findByKey(cancelOrder.getTariff_id());
		
		if(tariff.getBilling_type().equals(SystemConstants.BILLING_TYPE_MONTH)){
			//包月的
			//订购的停止计费日根据订购月数反推开始计费日
			Date expDate=DateHelper.addDate(cancelOrder.getExp_date(), 1);
			Date effDate=DateHelper.getNextMonthByNum(expDate,-1*cancelOrder.getOrder_months().intValue());
			if(cancelDate.equals(effDate)||cancelDate.before(effDate)){
				//退订日包含了整个订单的订购期间
				return cancelOrder.getOrder_fee();
			}
			return this.getTransCancelFeeByBillingTypeMonth(cancelDate, cancelOrder.getOrder_months().intValue(), cancelOrder.getOrder_fee(), expDate);
		
		}else if(tariff.getBilling_type().equals(SystemConstants.BILLING_TYPE_DAY)){
			//包天的
			//订购的停止计费日根据订购天数反推开始计费日
			Date expDate=DateHelper.addDate(cancelOrder.getExp_date(), 1);
			Integer orderDays=Math.round(cancelOrder.getOrder_months()*30);
			Date effDate=DateHelper.addDate(expDate,-1*orderDays);
			if(cancelDate.equals(effDate)||cancelDate.before(effDate)){
				//退订日包含了整个订单的订购期间
				return cancelOrder.getOrder_fee();
			}else{
				//提取天数计算金额
				int betweenDays=DateHelper.getDiffDays(cancelDate, expDate);
				return Math.round(betweenDays*cancelOrder.getOrder_fee()*1.0f/orderDays);
			}
		}else{
			//其他计费类型还没考虑怎么处理
			throw new ComponentException(ErrorCode.UNKNOW_EXCEPTION);
		}
	}
	/**
	 * 包月计费的区间内转移支付金额计算
	 * 退订日在 订购的计费区间内（按剩余使用天数折算）
	 * @param cancelDate
	 * @param order_months
	 * @param order_fee
	 * @param exp_date
	 * @return
	 */
	private Integer getTransCancelFeeByBillingTypeMonth(Date cancelDate,Integer order_months,Integer order_fee,Date exp_date){
		int months=DateHelper.compareToMonthByDate(cancelDate, exp_date);
		Date newExpDate=DateHelper.getNextMonthByNum(cancelDate,months);
		if(newExpDate.equals(cancelDate)){
			//剩余整月的情况
			return Math.round(months*1.0f*order_fee/order_months);
		}else if(newExpDate.after(exp_date)){
			//新到期日大于实际到期日，则需要回退一个月再计算
			newExpDate=DateHelper.getNextMonthByNum(cancelDate,months-1);
			float months_fee=(months-1)*1.0f*order_fee/order_months;
			int days=DateHelper.getDiffDays(newExpDate, exp_date);
			float days_fee= days* order_fee/(order_months*30.0f);
			return Math.round(months_fee+days_fee);
		}else{
			float months_fee=months*1.0f*order_fee/order_months;
			int days=DateHelper.getDiffDays(newExpDate, exp_date);
			float days_fee= days* order_fee/(order_months*30.0f);
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
				List<CProdOrder> transferList=cProdOrderDao.queryNotExpAllOrderByUser(prodOrder.getUser_id());
				for(CProdOrder order:transferList){
					if(StringHelper.isNotEmpty(order.getPackage_sn())){
						throw new ServicesException(ErrorCode.OrderDateCanNotUpWhyPak);
					}
				}
				return transferList;
			}else{
				throw new ServicesException(ErrorCode.OrderDateCanNotUp);
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
		prod.setOrder_time(new Date());
		prod.setPublic_acctitem_type(SystemConstants.PUBLIC_ACCTITEM_TYPE_NONE);
		prod.setOrder_type(SystemConstants.PROD_ORDER_TYPE_ORDER);
		if(orderProd.getEff_date().equals(DateHelper.today())){
			prod.setCheck_time(prod.getOrder_time());
		}
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
	 * OTT_MOBILE的升级
	 * @param cProdOrder
	 * @param orderProd
	 * @param cacleOrderList
	 * @return
	 * @throws Exception
	 */
	public String saveCProdOrderByOttMobileUpgrade(CProdOrder cProdOrder,OrderProd orderProd,List<CProdOrderDto> cacleOrderList) throws Exception{
		
		int transfee=saveTransCancelProd(cProdOrder,cacleOrderList,DateHelper.today());
		if(transfee!=orderProd.getTransfer_fee().intValue()){
			throw new ComponentException("转移支付金额不一致，请重新操作!");
		}
		//保存订单
		cProdOrderDao.save(cProdOrder);
		//记录创建订单的原始状态
		cProdStatusChangeDao.saveStatusChange(cProdOrder.getDone_code(), cProdOrder.getOrder_sn(), cProdOrder.getStatus());
		//保存套餐的子订单
		if(orderProd.getGroupSelected()!=null&&orderProd.getGroupSelected().size()>0){
			savePackageUserProd(cProdOrder,orderProd);
		}
		return cProdOrder.getOrder_sn();
	}
	/**
	 * 保存订购记录
	 * @return
	 * @throws Exception 
	 */
	public String saveCProdOrder(CProdOrder cProdOrder,OrderProd orderProd,String busi_code) throws Exception{
		
		//覆盖退订并转移支付余额到新订购记录
		int transfee=0;
		List<CProdOrder> cacleOrderList=this.queryTransCancelOrderList(orderProd, busi_code);
		if(cacleOrderList!=null&&cacleOrderList.size()>0){
			transfee=saveTransCancelProd(cProdOrder,cacleOrderList,orderProd.getEff_date());
		}
		if(transfee!=orderProd.getTransfer_fee().intValue()){
			throw new ComponentException("转移支付金额不一致，请重新操作!");
		}
		
		//保存订单
		cProdOrderDao.save(cProdOrder);
		//记录创建订单的原始状态
		cProdStatusChangeDao.saveStatusChange(cProdOrder.getDone_code(), cProdOrder.getOrder_sn(), cProdOrder.getStatus());
		//保存套餐的子订单
		if(orderProd.getGroupSelected()!=null&&orderProd.getGroupSelected().size()>0){
			savePackageUserProd(cProdOrder,orderProd);
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
	public int saveTransCancelProd(CProdOrder cProdOrder,List<? extends CProdOrder> cancelList,Date cancelDate) throws Exception{
		int transFee=0;

		List<CProdOrderFee> outputList=new ArrayList<>();
		List<CProdOrderFee> inputList=new ArrayList<>();
		for(CProdOrder cancelOrder:cancelList){
			
			if(cancelOrder.getIs_pay().equals(SystemConstants.BOOLEAN_FALSE)){
				throw new ComponentException(ErrorCode.OrderTransUnPayPleaseCancel,cancelOrder.getOrder_sn());
			}
			int fee=this.getTransCancelFee(cancelDate, cancelOrder);
			cancelOrder.setActive_fee(fee);
			transFee=transFee+fee;
			if(fee>0){
				int totalInputFee=0;//被覆盖的订单总的可用余额
				List<CProdOrderFee> outputOrderFees=cProdOrderFeeDao.queryByOrderSn(cancelOrder.getOrder_sn());
				for(CProdOrderFee outputOrderFee:outputOrderFees){
					totalInputFee=totalInputFee+outputOrderFee.getFee();
				}
				int _tmpFee=fee;//被覆盖的订单需要转出的金额
				//转出金额和总余额的比例扣除订单的每条费用记录的余额
				for(CProdOrderFee outputOrderFee:outputOrderFees){
					int outfee= Math.round(outputOrderFee.getFee()*fee*1.0f/totalInputFee);
					if(outfee>outputOrderFee.getFee()){
						outfee=outputOrderFee.getFee();
					}
					if(outfee>_tmpFee){
						outfee=_tmpFee;
					}
					_tmpFee=_tmpFee-outfee;
					outputOrderFee.setOutput_fee(outfee);
					outputOrderFee.setOutput_type(SystemConstants.ORDER_FEE_TYPE_TRANSFEE);	
				}
				if(_tmpFee!=0&&outputOrderFees.size()>0){
					CProdOrderFee outputOrderFee=outputOrderFees.get(outputOrderFees.size()-1);
					outputOrderFee.setOutput_fee(outputOrderFee.getOutput_fee()+_tmpFee);
				}
				
				//新订购的转入记录
				for(CProdOrderFee outputOrderFee:outputOrderFees){
					CProdOrderFee inputOrderFee=new CProdOrderFee();
					inputOrderFee.setOrder_fee_sn(cProdOrderFeeDao.findSequence().toString());
					inputOrderFee.setOrder_sn(cProdOrder.getOrder_sn());
					inputOrderFee.setDone_code(cProdOrder.getDone_code());
					inputOrderFee.setInput_type(SystemConstants.ORDER_FEE_TYPE_TRANSFEE);
					inputOrderFee.setInput_sn(outputOrderFee.getOrder_fee_sn());
					inputOrderFee.setInput_fee(outputOrderFee.getOutput_fee());
					inputOrderFee.setFee(outputOrderFee.getOutput_fee());
					inputOrderFee.setFee_type(outputOrderFee.getFee_type());
					inputOrderFee.setCreate_time(new Date());
					inputOrderFee.setArea_id(outputOrderFee.getArea_id());
					inputOrderFee.setCounty_id(outputOrderFee.getCounty_id());
					inputList.add(inputOrderFee);
					
					outputOrderFee.setOutput_sn(inputOrderFee.getOrder_fee_sn());
				}
				outputList.addAll(outputOrderFees);//装入被覆盖旧订购的转出记录
			}
			
			//退订订购记录
			this.saveCancelProdOrder(cancelOrder,cProdOrder.getDone_code());
		}
		//保存转移支付记录
		cProdOrderFeeDao.save(inputList.toArray(new CProdOrderFee[inputList.size()]));
		//cProdOrderFeeDao.update(outputList.toArray(new CProdOrderFee[outputList.size()]));
		//记录订单费用的覆盖转出记录
		this.saveOrderFeeOut(getOrderFeeOutFromOrderFee(outputList),cProdOrder.getDone_code());
		
		return transFee;
	}
	/**
	 * 处理转出费用回退
	 * @param outList
	 * @param doneCode
	 * @throws JDBCException
	 */
	public void saveOrderFeeOutToBack(List<CProdOrderFeeOut> outList,Integer doneCode) throws JDBCException{
		for(CProdOrderFeeOut out: outList){
			CProdOrderFee orderFee=cProdOrderFeeDao.findByKey(out.getOrder_fee_sn());
			CProdOrderFeeOut in=new CProdOrderFeeOut();
			in.setOrder_fee_sn(out.getOrder_fee_sn());
			in.setDone_code(doneCode);
			in.setOutput_type(out.getOutput_type());
			in.setOutput_sn(out.getOutput_sn());
			in.setRemark(out.getRemark());
			in.setOutput_fee(out.getOutput_fee()*-1);
			in.setPre_fee(orderFee.getFee());
			in.setFee(orderFee.getFee()+out.getOutput_fee());
			cProdOrderFeeDao.updateOrderFee(out.getOrder_fee_sn(), in.getOutput_fee());
			cProdOrderFeeOutDao.save(in);
		}
	}
	/**
	 * 处理费用转出扣费
	 * @param outList
	 * @param doneCode
	 * @throws JDBCException
	 */
	public void saveOrderFeeOut(List<CProdOrderFeeOut> outList,Integer doneCode) throws JDBCException{
		for(CProdOrderFeeOut out: outList){
			CProdOrderFee orderFee=cProdOrderFeeDao.findByKey(out.getOrder_fee_sn());
			out.setDone_code(doneCode);
			out.setPre_fee(orderFee.getFee());
			out.setFee(orderFee.getFee()-out.getOutput_fee());
			out.setCreate_time(new Date());
			cProdOrderFeeDao.updateOrderFee(orderFee.getOrder_fee_sn(), out.getOutput_fee());
		}
		cProdOrderFeeOutDao.save(outList.toArray(new CProdOrderFeeOut[outList.size()]));
	}
	/**
	 * 从订单费用记录中提取费用转出记录并处理转出
	 * @param outputList
	 * @throws JDBCException 
	 */
	public List<CProdOrderFeeOut> getOrderFeeOutFromOrderFee(List<CProdOrderFee> outputList) throws Exception{
		List<CProdOrderFeeOut> outList=new ArrayList<>();
		for(CProdOrderFee orderFee:outputList){
			if(orderFee.getOutput_fee()==null){
				throw new ComponentException(ErrorCode.ParamIsNull);
			}
			if(orderFee.getOutput_fee()==0){
				continue;
			}
			CProdOrderFeeOut out=new CProdOrderFeeOut();
			out.setOrder_sn(orderFee.getOrder_sn());
			out.setOrder_fee_sn(orderFee.getOrder_fee_sn());
			out.setOutput_sn(orderFee.getOutput_sn());
			out.setOutput_type(orderFee.getOutput_type());
			out.setOutput_fee(orderFee.getOutput_fee());
			out.setRemark(orderFee.getRemark());
			out.setFee_type(orderFee.getFee_type());
			outList.add(out);
		}
		return outList;
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
	private List<CProdOrder> savePackageUserProd(CProdOrder cProdOrder,OrderProd orderProd) throws Exception{
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
		return orderList;
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
			queryOrderableUpgrade(cust,filterOrderSn,panel,orderList);
		} 
		return panel;
	}
	
	/**
	 * 升级情况
	 * @throws Exception
	 */
	public void queryOrderableUpgrade(CCust cust,String filterOrderSn,OrderProdPanel panel,List<CProdOrderDto> orderList) throws Exception{
		CProdOrder order = cProdOrderDao.findByKey(filterOrderSn);
		if (order == null)
			return ;
		panel.setUserId(order.getUser_id());
		Map<String,Integer> prodBandWidthMap = pProdDao.queryProdBandWidth();
		PProd prod= pProdDao.findByKey(order.getProd_id());
		if (prod.getProd_type().equals(SystemConstants.PROD_TYPE_BASE) 
				&& prod.getServ_id().equals(SystemConstants.USER_TYPE_BAND)){
			//升级宽带产品
			queryUserOrderableProd(cust,order.getUser_id(),panel,orderList);
			//过滤掉带宽小于等于当前套餐的产品  改成过滤掉 当前产品
			for (Iterator<PProd> it = panel.getProdList().iterator();it.hasNext();){
				PProd selectedProd = it.next();
				if (prodBandWidthMap.get(selectedProd.getProd_id())==null ||
						//prodBandWidthMap.get(selectedProd.getProd_id())<= prodBandWidthMap.get(prod.getProd_id())){
						selectedProd.getProd_id().equals(prod.getProd_id())){
					it.remove();
					panel.getTariffMap().remove(selectedProd.getProd_id());
					panel.getLastOrderMap().remove(prod.getProd_id());
				}
			}
		} else if (prod.getProd_type().equals(SystemConstants.PROD_TYPE_CUSTPKG)
				&& prodBandWidthMap.get(prod.getProd_id()) != null){
			//含宽带的普通套餐
			queryCustOrderablePkg(cust,panel,orderList);
			//过滤掉带宽小于等于当前套餐的产品 改成 过滤掉当前套餐
			for (Iterator<PProd> it = panel.getProdList().iterator();it.hasNext();){
				PProd selectedProd = it.next();
				if (prod.getProd_type().equals(SystemConstants.PROD_TYPE_SPKG) ||
						prodBandWidthMap.get(selectedProd.getProd_id())==null ||
						//prodBandWidthMap.get(selectedProd.getProd_id())<= prodBandWidthMap.get(prod.getProd_id())){
						selectedProd.getProd_id().equals(prod.getProd_id())){
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
	//查找用户能够订购的单产品
	private void queryUserOrderableProd(CCust cust,String userId,OrderProdPanel panel,List<CProdOrderDto> orderList) throws Exception {
		CUser user = cUserDao.findByKey(userId);
		if (user == null)
			return;
		panel.setUserId(userId);
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
		panel.setUserId(order.getUser_id());
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
		}if(prod.getProd_type().equals(SystemConstants.PROD_TYPE_CUSTPKG)&&StringHelper.isNotEmpty(cust.getSpkg_sn())){
			//有协议号的情况，客户套餐不能订购
			ptList.clear();
		} else {
			for (Iterator<ProdTariffDto> tariffIt = ptList.iterator();tariffIt.hasNext();) {
				ProdTariffDto  tariff = tariffIt.next();
				if (!checkRule(cust,user, tariff.getRule_id()))
					tariffIt.remove();
			}
		}
		java.text.DecimalFormat   df=new   java.text.DecimalFormat("#.##");  
		//资费的格式
		Pattern p = Pattern.compile("USD/*M");
		// 如果有适用的资费
		if (CollectionHelper.isNotEmpty(ptList)) {
			for(ProdTariffDto pt:ptList){
				PProdTariffDisct tariff = new PProdTariffDisct();
				tariff.setTariff_id(pt.getTariff_id());
				tariff.setBilling_cycle(pt.getBilling_cycle());
				tariff.setDisct_rent(pt.getRent());
				tariff.setDisct_name(pt.getTariff_name());
				
				Matcher tariffRent = p.matcher(tariff.getDisct_name());
				if(!tariffRent.find()){
					String _v = null;
					if(tariff.getBilling_cycle() != null && tariff.getDisct_rent() !=null ){
						_v = tariff.getBilling_cycle() == 1 ?"":tariff.getBilling_cycle().toString(); 
					}
					if(_v != null){
						tariff.setDisct_name(tariff.getDisct_name()+"("+df.format((double)tariff.getDisct_rent()/100)+"USD/"+_v+"M)");
					}
				}
				tariff.setBilling_type(pt.getBilling_type());
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
							disct.setBilling_type(pt.getBilling_type());
							
							Matcher disctRent = p.matcher(disct.getDisct_name());
							if(!disctRent.find()){
								String _v = null;
								if(disct.getBilling_cycle() != null && disct.getDisct_rent() !=null ){
									_v = disct.getBilling_cycle() == 1 ?"":disct.getBilling_cycle().toString();
								}
								if(_v != null){
									disct.setDisct_name(disct.getDisct_name()+"("+df.format((double)disct.getDisct_rent()/100)+"USD/"+_v+"M)");
								}
							}
							//disct.setDisct_id(disct.getTariff_id() + "-" + disct.getDisct_id());
							tariffList.add(disct);
						}
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
		return getFillUserName(user);
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
	 * 更新订单费用缴费的资金类型
	 * @param upPayDoneCodes
	 * @param feeType
	 * @throws Exception 
	 */
	public void updateOrderFeeTypeByPayType(List<FeeDto> feeList,String payType) throws Exception{
		String feeType=tPayTypeDao.findByKey(payType).getAcct_feetype();
		for(FeeDto fee:feeList){
			if(StringHelper.isNotEmpty(fee.getProd_sn())){
				cProdOrderFeeDao.updateFeeType(fee.getProd_sn(),fee.getCreate_done_code(), feeType);
			}
		}
	}
	
	/**
	 * 获取宽带最后一个订单
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public CProdOrderDto getBandLastOrder(String userId)throws Exception{
		List<CProdOrderDto> orderList = cProdOrderDao.queryProdOrderDtoByUserId(userId);
		CProdOrderDto lastOrder = null;
		if (CollectionHelper.isNotEmpty(orderList)){
			for(CProdOrderDto order:orderList){
				if (order.getProd_type().equals(SystemConstants.PROD_TYPE_BASE) &&order.getServ_id().equals(SystemConstants.PROD_SERV_ID_BAND)){
					if(lastOrder == null){
						lastOrder = order;
					}else if(order.getExp_date().after(lastOrder.getExp_date())){
						lastOrder = order;
					}
				}
				
			}
		}
		return lastOrder;
	}
	
	/**
	 * 整体移动剩下订单的开始和结束计算日期
	 * @param cancelResultList
	 * @param userMap
	 * @param done_code
	 * @throws Exception 
	 */
	public void moveOrderByCancelOrder(List<CProdOrder> cancelResultList,Map<String,CUser> userMap,Integer done_code) throws Exception{
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
			this.movePackageOrderToFollow(cust_id, done_code);
		}
		//宽带订单的接续处理
		for(String user_id:moveBandMap.keySet()){
			this.moveBandOrderToFollow(user_id, done_code);
		}
		//非宽带单产品的接续处理
		for(CProdOrder order:moveProdMap.values()){
			this.moveProdOrderToFollow(order.getUser_id(), order.getProd_id(), done_code);
		}
		
	}
}