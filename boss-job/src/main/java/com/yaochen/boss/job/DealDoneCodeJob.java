package com.yaochen.boss.job;

import java.util.List;

import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.yaochen.boss.job.component.BusiComponent;
import com.yaochen.boss.job.component.JobComponent;
import com.yaochen.myquartz.Job2;
import com.yaochen.myquartz.Job2ExecutionContext;
import com.ycsoft.beans.core.common.CDoneCode;
import com.ycsoft.beans.core.common.CDoneCodeDetail;
import com.ycsoft.beans.prod.PProd;
import com.ycsoft.commons.constants.BusiCodeConstants;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.LoggerHelper;
import com.ycsoft.commons.helper.StringHelper;

/**
 * 流水记录实时处理：
 * 1、包含产品包含计算
 * 2、发送冲正和退款后的自动促销回退
 * 3、无缴费的促销自动执行判断
 * 4、客户基础信息发送变更，修改基本包自动赠送的资源(重置产品资源)
 * 5、9005补入非公用账目充值的账目异动数据
 */
@Service
@Scope("prototype")
public class DealDoneCodeJob implements Job2 {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private BusiComponent busiComponent;
	private JobComponent jobComponent;
	
	@Override
	public void execute(Job2ExecutionContext context)
			throws JobExecutionException {
		try {
			List<CDoneCode> doneCodeList = jobComponent.queryDoneCode();
			if(doneCodeList.size() > 0){
				dealDoneCode(doneCodeList);
			}
		} catch (Exception e){
			LoggerHelper.error(this.getClass(), "系统错误", e);
		}

	}
	
	/**
	 *  * 1、包含产品包含计算
	 * 2、发送冲正和退款后的自动促销回退
	 * 3、无缴费的促销自动执行判断
	 * 4、客户基础信息发送变更，修改基本包自动赠送的资源(重置产品资源)
	 * 5、9005补入非公用账目充值的账目异动数据
	 * @param doneCodeList
	 * @throws Exception
	 */
	public void dealDoneCode(List<CDoneCode> doneCodeList)throws Exception{
		long maxDoneCode =0;
		logger.info("处理业务流水","启动first_donecode: " + doneCodeList.get(0).getDone_code());
		for (CDoneCode cDoneCode:doneCodeList){
			List<CDoneCodeDetail> detailList = jobComponent.queryDoneCodeDetail(cDoneCode.getDone_code());
			String busiCode = cDoneCode.getBusi_code();
			if (busiCode.equals(BusiCodeConstants.PROD_PACKAGE_ORDER) ||
					busiCode.equals(BusiCodeConstants.PROD_TERMINATE) || 
					busiCode.equals(BusiCodeConstants.JOB_PROD_STOP) ||
					busiCode.equals(BusiCodeConstants.PROMOTION_AUTO) ||
					busiCode.equals(BusiCodeConstants.PROMOTION_CANCEL) ||
					busiCode.equals(BusiCodeConstants.USER_PROMOTION) ||
					busiCode.equals(BusiCodeConstants.BATCH_PROD_ORDER) ||
					busiCode.equals(BusiCodeConstants.CHANGE_PROD_DYN_RES) || 
					busiCode.equals(BusiCodeConstants.SYNC_ZZD_PROD) ||
					busiCode.equals(BusiCodeConstants.PROM_ACCT_PAY)){
				//产品订购或者是产品退订,自动退订,产品批量订购,更换产品动态资源,主机产品同步
				//获取流水明细
				
				for (CDoneCodeDetail detail:detailList){
					if (StringHelper.isNotEmpty(detail.getUser_id())){
						//设置用户产品之间的关系
						try{
							int size = busiComponent.setProdInclude(detail.getDone_code(),detail.getCust_id(),detail.getUser_id(), detail.getCounty_id(), detail.getArea_id());
							
							if(size>0)
								busiComponent.saveProdIncludeRecord(detail.getDone_code(),detail.getCust_id(), detail.getUser_id(), detail.getCounty_id(),SystemConstants.BOOLEAN_TRUE,null);
						}catch(Exception e){
							e.printStackTrace();
							busiComponent.saveProdIncludeRecord(detail.getDone_code(),detail.getCust_id(), detail.getUser_id(), detail.getCounty_id(),SystemConstants.BOOLEAN_FALSE,e.getMessage().substring(0, 99));
							logger.error("设置产品包含关系","用户【"+detail.getUser_id()+"】"+e.getMessage());
						}
						
						if (busiCode.equals(BusiCodeConstants.PROD_PACKAGE_ORDER)){
							//判断订购是否是基本产品，如果是，执行自动促销
							PProd prod = jobComponent.queryProdByDoneCode(cDoneCode.getDone_code());
							if (prod != null && SystemConstants.BOOLEAN_TRUE.equals(prod.getIs_base())){
								try{
									busiComponent.promotion(detail.getUser_id());
								} catch(Exception e){
									logger.error("基础产品自动促销", e.getMessage());
								}
							}
						}
					}
				}
			} 
			if (busiCode.equals(BusiCodeConstants.FEE_CANCEL) ||
					busiCode.equals(BusiCodeConstants.ACCT_REFUND) ||
					busiCode.equals(BusiCodeConstants.PROD_TERMINATE) ||
					cDoneCode.getStatus().equals(StatusConstants.INVALID)){
				//如果发生了冲正和退款，判断客户今天参加的促销是否需要回退
				if (detailList !=null && detailList.size()>0){
					int i=0;
					while (i<20){
						if (busiComponent.canCancelPromotion(detailList.get(0).getCust_id())){
							try{
								busiComponent.cancelPromotion(detailList.get(0).getCust_id());
							} catch(Exception e){
								logger.error("促销自动回退", e);
							}
							break;
						}
						else 
							Thread.sleep(1000);
						i++;							
					}
				}
			} 
			if (busiCode.equals(BusiCodeConstants.USER_PROMOTION)){
				if (detailList !=null && detailList.size()>0){
					try{
						busiComponent.promotion(detailList.get(0).getUser_id());
					} catch(Exception e){
						logger.error("自动促销",e.getMessage());
					}
				}
			} 
			if (busiCode.equals(BusiCodeConstants.CUST_JOIN_UNIT) || 
					busiCode.equals(BusiCodeConstants.CUST_QUIT_UNIT)||
					busiCode.equals(BusiCodeConstants.CUST_EDIT)||
					busiCode.equals(BusiCodeConstants.CUST_TRANS)||
					busiCode.equals(BusiCodeConstants.CUST_CHANGE_ADDR)){
				if (detailList !=null && detailList.size()>0){
					for (CDoneCodeDetail detail:detailList){
						try{
							busiComponent.resetUserProdRes(detail.getCust_id());
						} catch(Exception e){
							logger.error("重置产品资源",e.getMessage());
						};
					}
				}
			}
			if(BusiCodeConstants.ACCT_PAY.equals(busiCode) && SystemConstants.COUNTY_9005.equals(cDoneCode.getCounty_id())){//如果是缴费
				//补入非公用账目充值的账目异动数据
				busiComponent.adjustSpecAcctPay(cDoneCode.getDone_code());
			}
					
			if (cDoneCode.getStatus().equals(StatusConstants.ACTIVE)){
				maxDoneCode = cDoneCode.getDone_code();
			} else {
				jobComponent.updateCancelDoneCode(cDoneCode.getDone_code());
			}
		}
		
		if (maxDoneCode>0)
			jobComponent.updateMaxDoneCode(maxDoneCode);
		logger.info("处理业务流水","共处理"+doneCodeList.size()+"条流水,结束流水:"+maxDoneCode);
	}

	public void setBusiComponent(BusiComponent busiComponent) {
		this.busiComponent = busiComponent;
	}

	public void setJobComponent(JobComponent jobComponent) {
		this.jobComponent = jobComponent;
	}
	
}
