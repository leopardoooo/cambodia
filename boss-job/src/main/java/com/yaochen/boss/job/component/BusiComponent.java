package com.yaochen.boss.job.component;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yaochen.boss.commons.InvalidMath;
import com.yaochen.boss.commons.LoggerUtil;
import com.yaochen.boss.dao.BusiDao;
import com.yaochen.boss.dao.CProdInvalidUpdateDao;
import com.yaochen.boss.dao.JobDao;
import com.yaochen.boss.job.BusiCfgDataJob;
import com.yaochen.boss.model.CProdCycleDto;
import com.yaochen.boss.model.CProdUpdate;
import com.ycsoft.beans.config.TAcctitemToProd;
import com.ycsoft.beans.config.TConfigTemplate;
import com.ycsoft.beans.core.acct.CAcct;
import com.ycsoft.beans.core.acct.CAcctAcctitem;
import com.ycsoft.beans.core.acct.CAcctAcctitemActive;
import com.ycsoft.beans.core.acct.CAcctAcctitemChange;
import com.ycsoft.beans.core.acct.CAcctAcctitemInactive;
import com.ycsoft.beans.core.acct.CAcctPreFee;
import com.ycsoft.beans.core.bank.CBankAgree;
import com.ycsoft.beans.core.bank.CBankGotodisk;
import com.ycsoft.beans.core.bank.CBankRefundtodisk;
import com.ycsoft.beans.core.bank.CBankReturn;
import com.ycsoft.beans.core.bank.CBankReturnPayerror;
import com.ycsoft.beans.core.bill.BTaskInfo;
import com.ycsoft.beans.core.cust.CCust;
import com.ycsoft.beans.core.fee.CFee;
import com.ycsoft.beans.core.job.JBusiCmd;
import com.ycsoft.beans.core.job.JCustAcctmodeCal;
import com.ycsoft.beans.core.job.JCustCreditCal;
import com.ycsoft.beans.core.job.JCustInvalidCal;
import com.ycsoft.beans.core.job.JCustWriteoff;
import com.ycsoft.beans.core.job.JProdNextTariff;
import com.ycsoft.beans.core.job.JProdPreopen;
import com.ycsoft.beans.core.job.JSmsRecord;
import com.ycsoft.beans.core.job.JUserStop;
import com.ycsoft.beans.core.prod.CProd;
import com.ycsoft.beans.core.prod.CProdInclude;
import com.ycsoft.beans.core.prod.CProdPropChange;
import com.ycsoft.beans.core.prod.CProdPropPat;
import com.ycsoft.beans.core.promotion.CPromotion;
import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.beans.core.user.CUserPropChange;
import com.ycsoft.beans.prod.PProdTariff;
import com.ycsoft.beans.system.SCounty;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.business.commons.pojo.BusiParameter;
import com.ycsoft.business.component.config.ExpressionUtil;
import com.ycsoft.business.dao.core.acct.CAcctAcctitemChangeDao;
import com.ycsoft.business.dao.core.acct.CAcctAcctitemDao;
import com.ycsoft.business.dao.core.bank.CBankAgreeDao;
import com.ycsoft.business.dao.core.bank.CBankGotodiskDao;
import com.ycsoft.business.dao.core.bank.CBankRefundtodiskDao;
import com.ycsoft.business.dao.core.bank.CBankReturnDao;
import com.ycsoft.business.dao.core.bank.CBankReturnPayerrorDao;
import com.ycsoft.business.dao.core.bill.BTaskInfoDao;
import com.ycsoft.business.dao.core.cust.CCustDao;
import com.ycsoft.business.dao.core.fee.CFeeDao;
import com.ycsoft.business.dao.core.job.JBusiCmdDao;
import com.ycsoft.business.dao.core.job.JCustCreditCalDao;
import com.ycsoft.business.dao.core.job.JCustInvalidCalDao;
import com.ycsoft.business.dao.core.job.JCustWriteoffDao;
import com.ycsoft.business.dao.core.job.JSmsRecordDao;
import com.ycsoft.business.dao.core.prod.CProdDao;
import com.ycsoft.business.dao.core.prod.CProdPropChangeDao;
import com.ycsoft.business.dao.core.prod.CProdPropPatDao;
import com.ycsoft.business.dao.core.user.CUserAtvDao;
import com.ycsoft.business.dao.core.user.CUserBroadbandDao;
import com.ycsoft.business.dao.core.user.CUserDao;
import com.ycsoft.business.dao.core.user.CUserDtvDao;
import com.ycsoft.business.dao.prod.PProdTariffDao;
import com.ycsoft.business.dao.system.SCountyDao;
import com.ycsoft.business.dto.config.TemplateConfigDto;
import com.ycsoft.business.dto.core.acct.AcctitemDto;
import com.ycsoft.business.dto.core.acct.BankReturnDto;
import com.ycsoft.business.dto.core.cust.CustFullInfoDto;
import com.ycsoft.business.dto.core.prod.CProdDto;
import com.ycsoft.business.dto.core.prod.PPromotionDto;
import com.ycsoft.business.dto.core.prod.ProdRes;
import com.ycsoft.business.service.externalImpl.IAcctServiceExternal;
import com.ycsoft.business.service.externalImpl.ICustServiceExternal;
import com.ycsoft.business.service.externalImpl.IUserProdServiceExternal;
import com.ycsoft.business.service.externalImpl.IUserServiceExternal;
import com.ycsoft.commons.abstracts.BaseComponent;
import com.ycsoft.commons.constants.BusiCmdConstants;
import com.ycsoft.commons.constants.BusiCodeConstants;
import com.ycsoft.commons.constants.SequenceConstants;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ComponentException;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.commons.helper.DateHelper;
import com.ycsoft.commons.helper.LoggerHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.store.AcctItemToProdConfig;
import com.ycsoft.commons.store.TemplateConfig;
import com.ycsoft.commons.store.TemplateConfig.Template;
import com.ycsoft.daos.core.DataHandler;
import com.ycsoft.daos.core.JDBCException;

@Component
public class BusiComponent {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private IUserServiceExternal userService;
	private IAcctServiceExternal acctService;
	private IUserProdServiceExternal userProdService;
	private ICustServiceExternal custService;
	
	private BusiDao busiDao;
	private JobDao jobDao;
	private CCustDao cCustDao;
	private CProdDao cProdDao;
	private CUserDao cUserDao;
	private CFeeDao cFeeDao;
	private CAcctAcctitemDao cAcctAcctitemDao;
	private CAcctAcctitemChangeDao cAcctAcctitemChangeDao;
	private CUserAtvDao cUserAtvDao;
	private CUserDtvDao cUserDtvDao;
	private CUserBroadbandDao cUserBroadbandDao;
	
	private CProdInvalidUpdateDao cProdInvalidUpdateDao;

	private JBusiCmdDao jBusiCmdDao;
	private SCountyDao sCountyDao;
	private PProdTariffDao pProdTariffDao;
	private JCustCreditCalDao jCustCreditCalDao;
	private BTaskInfoDao bTaskInfoDao;
	
	private ExpressionUtil expressionUtil ;
	private CProdPropChangeDao cProdPropChangeDao;
	
	private JSmsRecordDao jSmsRecordDao;
	private CProdPropPatDao cProdPropPatDao;

	private JCustWriteoffDao jCustWriteoffDao;
	private JCustInvalidCalDao jCustInvalidCalDao;
	
	private CBankAgreeDao cBankAgreeDao;
	private CBankGotodiskDao cBankGotodiskDao;
	private CBankReturnDao cBankReturnDao;
	private CBankRefundtodiskDao cBankRefundtodiskDao;
	private CBankReturnPayerrorDao cBankReturnPayerrorDao;

	
	/**
	 * 取一个客户的所有常和预开通产品信息（非子产品）
        0.如果基础且资费是包月的产品信息(无手工变更有效记录)的账目使用级别根据模板配置判断，符合则改成 账目使用级别=NONE 否则改成ALL。
           基础产品中0资费、包多月资费改成NONE
           基础产品且实时点播资费不做任何修改
        1.包月非0资费的正常产品不使用公用或者公用无钱，则 按到期日停 ，否则按账务模式停；
        2.包月非0资费的其他状态产品 则按账务模式停
        3.包月非0资费产品状态是欠费停或欠费未停，停机账务模式、到期日大于当天、余额<=0 则修改到期日为当天。
        
        4.账目使用级别变化和因公用有钱引起的停机模式T=>F，需要重新插入销账重新预约和到期日计算JOB。
        
        5.包月非0资费产品停机模式F=>T,需要按余额计算到期日
	 */
	public void calAcctmode(JCustAcctmodeCal acctmodecal)  throws Exception{
		String custId=acctmodecal.getCust_id();
		String countyId=acctmodecal.getCounty_id();
		//提取一个客户的需要计费模式判断的产品（不含子产品,只提取资费包月且按月计费的产品)
		List<CProdDto> cprods=cProdDao.queryAllAcctModeProd(custId, countyId);
		//产品账目的有效手工变更记录
		Map<String,CProdPropPat> patMap= CollectionHelper.converToMapSingle(cProdPropPatDao.queryPatsByCustId(custId),"prod_sn");
		//一个客户的所有公用账目
		Map<String,CAcctAcctitem> pubAcctItemMap=null;
		List<CProdPropChange> propChangeList = new ArrayList<CProdPropChange>();//异动记录临时保存
		List<CProd> prodUpdateList=new ArrayList<CProd>();//产品更新临时保存
		
		boolean needWrite=false;
		for(CProdDto cprod:cprods){
			
			//0步骤判断,根据模板配置产品的公用账目使用级别
			String public_acctitem_type=cprod.getPublic_acctitem_type();
			
			if(cprod.getBilling_cycle()>1||cprod.getTariff_rent()==0){
				//产品是包多资费月产品和0资费产品，账目使用级别强制为NONE
				public_acctitem_type=SystemConstants.PUBLIC_ACCTITEM_TYPE_NONE;
			}else if(!patMap.containsKey(cprod.getProd_sn())&&cprod.getIs_base().equals(SystemConstants.BOOLEAN_TRUE)){
				//无手工账目使用级别变更记录的基本产品判断
				//提取模板中默认不使用公用账目配置
				String specAcctitemFlag = queryTemplateConfig(TemplateConfigDto.Config.PROD_PUBLIC_TYPE.toString(),countyId); //值：BAND,F;ITV,F;ATV,T
				if(StringHelper.isNotEmpty(specAcctitemFlag) && specAcctitemFlag.indexOf(cprod.getServ_id())>-1){
					public_acctitem_type=SystemConstants.PUBLIC_ACCTITEM_TYPE_NONE;
				}
				//提取模板中的例外产品配置
				String baseprodNoFlag = queryTemplateConfig(TemplateConfigDto.Config.PROD_NONE_PUBLIC_TYPE.toString(),countyId); //值：2728;2729
				if(StringHelper.isNotEmpty(baseprodNoFlag) && baseprodNoFlag.indexOf(cprod.getProd_id())>-1){
					public_acctitem_type=SystemConstants.PUBLIC_ACCTITEM_TYPE_ALL;
				}
				
				//提取模板中的明细产品配置
				String baseprodNoneFlag = queryTemplateConfig(TemplateConfigDto.Config.PROD_PUBLIC_TYPE_DETAIL.toString(),countyId); //值：2728;2729
				if(StringHelper.isNotEmpty(baseprodNoneFlag) && baseprodNoneFlag.indexOf(cprod.getProd_id())>-1){
					public_acctitem_type=SystemConstants.PUBLIC_ACCTITEM_TYPE_NONE;
				}
			}
			
			if(!public_acctitem_type.equals(cprod.getPublic_acctitem_type())){
				needWrite=true;
				//记录异动 更新表数据
				CProd prod=new CProd();
				prod.setProd_sn(cprod.getProd_sn());
				prod.setPublic_acctitem_type(public_acctitem_type);
				prodUpdateList.add(prod);
				
				CProdPropChange prop = new CProdPropChange();
				prop.setProd_sn(cprod.getProd_sn());
				prop.setColumn_name("PUBLIC_ACCTITEM_TYPE");
				prop.setOld_value(cprod.getPublic_acctitem_type());
				prop.setNew_value(public_acctitem_type);
				propChangeList.add(prop);
				
				cprod.setPublic_acctitem_type(public_acctitem_type);
			}
			
            //只处理按月计费产品和非模拟
			//1,2 步骤 包月非0资费正常产品 不使用公用或公用无钱则 按到期日停，其他情况都按账务模式停
			if(cprod.getBilling_type().equals(SystemConstants.BILLING_TYPE_MONTH)
					&&!cprod.getServ_id().equals(SystemConstants.PROD_SERV_ID_ATV)){
				String acctmode=null;
				//1.包月资费正常产品判断停机模式
				if(cprod.getBilling_cycle()==1&&cprod.getTariff_rent()>0){
					//账目使用级别判断
					if(cprod.getStatus().equals(StatusConstants.ACTIVE)&&cprod.getPublic_acctitem_type().equals(SystemConstants.PUBLIC_ACCTITEM_TYPE_NONE)){
						//包月资费状态正常 账目使用NONE的产品
						acctmode=SystemConstants.BOOLEAN_TRUE;
					}else if(cprod.getStatus().equals(StatusConstants.ACTIVE)&&!cprod.getPublic_acctitem_type().equals(SystemConstants.PUBLIC_ACCTITEM_TYPE_NONE)){
						//包月资费状态正常 账目使用非NONE的产品
						acctmode=SystemConstants.BOOLEAN_TRUE;
						if(pubAcctItemMap==null){
							pubAcctItemMap=CollectionHelper.converToMapSingle(cAcctAcctitemDao.queryPubAcctItemsByCustId(custId, countyId),"acctitem_id");
						}
						for(TAcctitemToProd aitp: AcctItemToProdConfig.loadConfig(cprod.getProd_id())){
							//可使用的公用有活动余额
							if(pubAcctItemMap.containsKey(aitp.getAcctitem_id())&&pubAcctItemMap.get(aitp.getAcctitem_id()).getActive_balance()>0){
								if( cprod.getPublic_acctitem_type().equals(SystemConstants.PUBLIC_ACCTITEM_TYPE_PUBLIC_ONLY)
										&&aitp.getAcctitem_id().equals(SystemConstants.ACCTITEM_PUBLIC_ID)){
									//账目使用级别判断 只能使用公用
									acctmode=SystemConstants.BOOLEAN_FALSE;
									break;
								}else if(cprod.getPublic_acctitem_type().equals(SystemConstants.PUBLIC_ACCTITEM_TYPE_SPEC_ONLY)
										&&!aitp.getAcctitem_id().equals(SystemConstants.ACCTITEM_PUBLIC_ID)){
									//账目使用级别判断 只能使用专项公用
									acctmode=SystemConstants.BOOLEAN_FALSE;
									break;
								}else if(cprod.getPublic_acctitem_type().equals(SystemConstants.PUBLIC_ACCTITEM_TYPE_ALL)){
									//账目使用基本 ALL
									acctmode=SystemConstants.BOOLEAN_FALSE;
									break;
								}
							}
						}
						//步骤4：公用账务有余额引起的停机T=>F,需要插入销账和到期日计算
						if(!cprod.getStop_by_invalid_date().equals(acctmode)&&acctmode.equals(SystemConstants.BOOLEAN_FALSE)){
							needWrite=true;
						}
					}else{//包月资费产品其他状态一律按账务模式停机
						acctmode=SystemConstants.BOOLEAN_FALSE;
					}
				}else{//0资费和包多月资费
					acctmode=SystemConstants.BOOLEAN_TRUE;
				}
				//账务模式发生了改变
				if(!cprod.getStop_by_invalid_date().equals(acctmode)){
					
					CProd updateprod=new CProd();
					updateprod.setProd_sn(cprod.getProd_sn());
					updateprod.setStop_by_invalid_date(acctmode);
					prodUpdateList.add(updateprod);
					//步骤5：账务模式F=>T的产品，重算到期日(只有正常的包月资费产品才会F=>T)
					if(SystemConstants.BOOLEAN_TRUE.equals(acctmode)&&cprod.getStatus().equals(StatusConstants.ACTIVE)
							&&cprod.getBilling_cycle()==1&&cprod.getTariff_rent()>0){
						Date invaliddate=DateHelper.today();
						if(cprod.getBillinfo_eff_date()!=null&&cprod.getBillinfo_eff_date().after(invaliddate)){
							//开始计费日期大于当天
							invaliddate=cprod.getBillinfo_eff_date();
						}
						invaliddate=BaseComponent.getInvalidDateByAcctmode(invaliddate, cprod.getAll_balance()-cprod.getOwe_fee()-cprod.getReal_bill(),
										cprod.getTariff_rent(), cprod.getBilling_cycle());
						if(!DateHelper.dateToStr(invaliddate).equals(DateHelper.dateToStr(cprod.getInvalid_date()))){
							//新计算出来的到期日和产品到期日不一致，则更新到期日，并记录异动
							CProdPropChange prop = new CProdPropChange();
							prop.setProd_sn(cprod.getProd_sn());
							prop.setColumn_name("INVALID_DATE");
							prop.setOld_value(DateHelper.dateToStr(cprod.getInvalid_date()));
							prop.setNew_value(DateHelper.dateToStr(invaliddate));
							propChangeList.add(prop);
							
							cprod.setInvalid_date(invaliddate);
							updateprod.setInvalid_date(invaliddate);
						}
					}
					CProdPropChange prop = new CProdPropChange();
					prop.setProd_sn(cprod.getProd_sn());
					prop.setColumn_name("STOP_BY_INVALID_DATE");
					prop.setOld_value(cprod.getStop_by_invalid_date());
					prop.setNew_value(acctmode);
					propChangeList.add(prop);
					
					cprod.setStop_by_invalid_date(acctmode);
					
				}
				//步骤3 包月的欠费停或者欠费未停的包月资费账务停机产品，如果账目无余额到期日大于当天，则修改到期日为当天
				if((cprod.getStatus().equals(StatusConstants.OWESTOP)||cprod.getStatus().equals(StatusConstants.OWENOTSTOP))
						&&cprod.getStop_by_invalid_date().equals(SystemConstants.BOOLEAN_FALSE)&&cprod.getInvalid_date().after(new Date()))
				{
					int now_fee=cprod.getAll_balance()-cprod.getOwe_fee()-cprod.getReal_bill();
					if(now_fee<=0){
						Date invaliddate=DateHelper.today();
						if(!DateHelper.dateToStr(invaliddate).equals(DateHelper.dateToStr(cprod.getInvalid_date()))){
							//新计算出来的到期日和产品到期日不一致，则更新到期日，并记录异动
							CProdPropChange prop1 = new CProdPropChange();
							prop1.setProd_sn(cprod.getProd_sn());
							prop1.setColumn_name("INVALID_DATE");
							prop1.setOld_value(DateHelper.dateToStr(cprod.getInvalid_date()));
							prop1.setNew_value(DateHelper.dateToStr(invaliddate));
							propChangeList.add(prop1);
							cprod.setInvalid_date(invaliddate);
							CProd updateprod=new CProd();
							updateprod.setProd_sn(cprod.getProd_sn());
							prodUpdateList.add(updateprod);					
							updateprod.setInvalid_date(invaliddate);
						}
					}
				}
			}		
		}
		//更新产品保存
		cProdDao.update(prodUpdateList.toArray(new CProd[prodUpdateList.size()]));
		//异动保存
		for(CProdPropChange prop: propChangeList){
			prop.setCounty_id(acctmodecal.getCounty_id());
			prop.setArea_id(acctmodecal.getArea_id());
			prop.setDone_code(acctmodecal.getDone_code());
		}
		cProdPropChangeDao.save(propChangeList.toArray(new CProdPropChange[propChangeList.size()]));
		
		//步骤4执行
		if(needWrite){
			JCustWriteoff creditCal=new JCustWriteoff();
			creditCal.setJob_id(acctmodecal.getJob_id());
			creditCal.setDone_code(acctmodecal.getDone_code());
			creditCal.setCust_id(custId);
			creditCal.setCounty_id(countyId);
			creditCal.setArea_id(acctmodecal.getArea_id());
			//TODO-应该插入特殊销账
			creditCal.setWriteoff(SystemConstants.BOOLEAN_TRUE);
			jCustWriteoffDao.save(creditCal);
			//到期日计算JOB
			JCustInvalidCal invalidCal = new JCustInvalidCal();
			invalidCal.setJob_id(acctmodecal.getJob_id());
			invalidCal.setCust_id(custId);
			invalidCal.setDone_code(acctmodecal.getDone_code());
			invalidCal.setArea_id(acctmodecal.getArea_id());
			invalidCal.setCounty_id(acctmodecal.getCounty_id());
			jCustInvalidCalDao.save(invalidCal);
			
		}
		
	}
	
	public String queryTemplateConfig(String key,String countyId) throws ComponentException{
		TemplateConfigDto t = (TemplateConfigDto) TemplateConfig.loadConfig(
				Template.CONFIG,countyId);
		TConfigTemplate template = t.get(key);
		if (template == null)
			return null;
		return template.getConfig_value();
	}
	
	
	public List<CCust> queryCustWithInvalidCustClass()throws Exception{
		return jobDao.queryCustWithInvalidCustClass();
	}
	
	public void resumeCustClass(CCust cust)throws Exception{
		CustFullInfoDto cfd = new CustFullInfoDto();
		cfd.setCust(cust);
		
		SOptr optr = gOptr(cust.getArea_id(),cust.getCounty_id());
		BusiParameter p = new BusiParameter();
		p.setOptr(optr);
		p.setCustFullInfo(cfd);
		p.setBusiCode(BusiCodeConstants.CUST_CLASS_EDIT);
		
		custService.resumeCustClass(p);
	}
	
	/**
	 * 预开通
	 * @param dto
	 * @param preopen
	 * @throws Exception
	 */
	public void preOpen(CProdDto dto,JProdPreopen preopen) throws Exception{

		if(dto==null)
			throw new Exception("预开通("+preopen.getJob_id()+") 产品不存在.");
		
		if(dto.getStatus().equals(StatusConstants.PREAUTHOR)){
			//变更状态
			/*CProd prod=new CProd();
			prod.setProd_sn(dto.getProd_sn());
			prod.setStatus(StatusConstants.ACTIVE);
			prod.setStatus_date(new Date());
			prod.setPre_open_time(null);
			cProdDao.update(prod);*/
			cProdDao.updatePreOpenTime(dto.getProd_sn());
			
			//插入状态异动
			List<CProdPropChange> propList = new ArrayList<CProdPropChange>();
			CProdPropChange pchange=new CProdPropChange();
			pchange.setArea_id(preopen.getArea_id());
			pchange.setCounty_id(preopen.getCounty_id());
			pchange.setDone_code(Integer.parseInt(preopen.getDone_code()));
			pchange.setChange_time(new Date());
			pchange.setProd_sn(preopen.getProd_sn());
			pchange.setColumn_name("status");
			pchange.setOld_value(StatusConstants.PREAUTHOR);
			pchange.setNew_value(StatusConstants.ACTIVE);
			propList.add(pchange);
			
			pchange=new CProdPropChange();
			pchange.setArea_id(preopen.getArea_id());
			pchange.setCounty_id(preopen.getCounty_id());
			pchange.setDone_code(Integer.parseInt(preopen.getDone_code()));
			pchange.setChange_time(new Date());
			pchange.setProd_sn(preopen.getProd_sn());
			pchange.setColumn_name("pre_open_time");
			pchange.setOld_value(DateHelper.dateToStr(dto.getPre_open_time()));
			pchange.setNew_value(null);
			propList.add(pchange);
			cProdPropChangeDao.save(propList.toArray(new CProdPropChange[propList.size()]));
						
		}else if(!dto.getStatus().equals(StatusConstants.ACTIVE)){
			throw new Exception("预开通("+preopen.getJob_id()+") 用户名下产品状态异常.");
		}
		
		List<CProdDto> childProds = cProdDao.queryChildProdByPkgsn(dto.getProd_sn(), dto.getCounty_id());
		if(childProds.size() > 0){
			//套餐,修改子产品状态，发加授权
			for(CProdDto prod : childProds){
				
				LoggerHelper.info(BusiComponent.class, "原产品prod_sn: "+prod.getProd_sn()+"原产品状态： "+prod.getStatus());
				CProd cp=new CProd();
				cp.setProd_sn(prod.getProd_sn());
				cp.setStatus(StatusConstants.ACTIVE);
				cp.setStatus_date(new Date());
				cp.setPre_open_time(null);
				cProdDao.update(cp);
				
				LoggerHelper.info(BusiComponent.class, "新产品prod_sn: "+cp.getProd_sn()+"新产品状态： "+cp.getStatus());
				
				//插入开通指令
				JBusiCmd busiCmdJob = new JBusiCmd();
				busiCmdJob.setJob_id(getJobId());
				busiCmdJob.setDone_code(Integer.parseInt(preopen.getDone_code()));
				busiCmdJob.setBusi_cmd_type(BusiCmdConstants.ACCTIVATE_PROD);
				busiCmdJob.setCust_id(prod.getCust_id());
				busiCmdJob.setUser_id(prod.getUser_id());
				busiCmdJob.setStb_id(dto.getStb_id());
				busiCmdJob.setCard_id(dto.getCard_id());
				busiCmdJob.setModem_mac(dto.getModem_mac());
				busiCmdJob.setProd_sn(prod.getProd_sn());
				busiCmdJob.setArea_id(prod.getArea_id());
				busiCmdJob.setCounty_id(prod.getCounty_id());
				busiCmdJob.setPriority(SystemConstants.PRIORITY_SSSQ);
				busiCmdJob.setProd_id(prod.getProd_id());
				
				jBusiCmdDao.save(busiCmdJob);
			}
		}else{
			//插入开通指令
			JBusiCmd busiCmdJob = new JBusiCmd();
			busiCmdJob.setJob_id(getJobId());
			busiCmdJob.setDone_code(Integer.parseInt(preopen.getDone_code()));
			busiCmdJob.setBusi_cmd_type(BusiCmdConstants.ACCTIVATE_PROD);
			busiCmdJob.setCust_id(dto.getCust_id());
			busiCmdJob.setUser_id(dto.getUser_id());
			busiCmdJob.setStb_id(dto.getStb_id());
			busiCmdJob.setCard_id(dto.getCard_id());
			busiCmdJob.setModem_mac(dto.getModem_mac());
			busiCmdJob.setProd_sn(dto.getProd_sn());
			busiCmdJob.setArea_id(dto.getArea_id());
			busiCmdJob.setCounty_id(dto.getCounty_id());
			busiCmdJob.setPriority(SystemConstants.PRIORITY_SSSQ);
			busiCmdJob.setProd_id(dto.getProd_id());
			
			jBusiCmdDao.save(busiCmdJob);
		}
	}
	
	public void userStop(JUserStop stopJob) throws Exception{

	}
	
	/**
	 * 更新一个客户的使用公用账目的产品的到期日
	 * @param cust_id
	 * @param doneCode 
	 */
	public void updateCprodInvalid(String cust_id,String county_id, Integer doneCode)throws Exception{
		//按公用账目数取产品集合
		List<CProdUpdate> list= cProdInvalidUpdateDao.queryCProdUpdates(cust_id, county_id);
	
		Map<String,CProdCycleDto> cprodmp=new HashMap<String,CProdCycleDto>();
		for(CProdUpdate cu:list){
			
			//使用其他计算后的到期日值更新当次到期日计算初始值
			for(CProdCycleDto dto:cu.getCprods()){
				if(cprodmp.containsKey(dto.getProd_sn()))
					dto.setInvalid_date_num(cprodmp.get(dto.getProd_sn()).getInvalid_date_num());
			}
			InvalidMath.doMath(cu);
			
			for(CProdCycleDto dto:cu.getCprods()){
				cprodmp.put(dto.getProd_sn(), dto);
			}
			
		}
		//取不存在的最后一个计算过程中的产品集合

		//更新到期日
		cProdInvalidUpdateDao.updateCprod(cprodmp,doneCode);
		
		//处理非公用到期日计算
		List<CProdCycleDto> canprods=cProdInvalidUpdateDao.queryCProdCanManthInvalid(cust_id, county_id);

		for(CProdCycleDto o:canprods){
			//排除公用计算过的产品
			if(!cprodmp.containsKey(o.getProd_sn()))
				cProdInvalidUpdateDao.updateOneProdInavlid(o, doneCode);
		}
		//更新状态=不计费的产品的到期日即被包含的产品=主产品的到期日
		//包月不计费产品到期日=主产品到期日+专用账目可看时间
		for(CProdCycleDto o: cProdInvalidUpdateDao.queryCProdPaushInvalid(cust_id,county_id)){
			cProdInvalidUpdateDao.updateOneProdInavlid(o, doneCode);
		}
	}
	/**
	 * 修改资费
	 * @param tariffJob
	 * @throws Exception
	 */
	public void changeTariff(JProdNextTariff tariffJob) throws Exception{
		SOptr optr = gOptr(tariffJob.getArea_id(),tariffJob.getCounty_id());
		BusiParameter p = new BusiParameter();
		p.setBusiCode(BusiCodeConstants.PROD_CHANGE_TARIFF);
		p.setOptr(optr);
		userProdService.changeTariff(p,tariffJob.getProd_sn(), tariffJob.getTariff_id(),DateHelper.dateToStr(tariffJob.getEff_date()),null);
		LoggerUtil.PrintInfo(this.getClass(),"资费变更","产品【"+tariffJob.getProd_sn()+"】资费变更为"+tariffJob.getTariff_id());
	}
	
	public void changeFzdProdTariff(CProd prod) throws Exception{
		SOptr optr = gOptr(prod.getArea_id(),prod.getCounty_id());
		BusiParameter p = new BusiParameter();
		p.setBusiCode(BusiCodeConstants.PROD_CHANGE_TARIFF);
		p.setOptr(optr);
		//修改资费，变为6元每月
		userProdService.changeTariff(p,prod.getProd_sn(), "4119",DateHelper.formatNow(),null);
		LoggerUtil.PrintInfo(this.getClass(),"资费变更","产品【"+prod.getProd_sn()+"】资费变更为4119");
	}
	
	public void changeDeviceOwnership(String deviceId,String countyId,String areaId) throws Exception{
		SOptr optr = gOptr(areaId,countyId);
		BusiParameter p = new BusiParameter();
		p.setBusiCode(BusiCodeConstants.CHANGE_OWNERSHIP);
		p.setOptr(optr);
		
		//修改设备产权
		custService.saveChangeOwnership(p, deviceId);
		LoggerUtil.PrintInfo(this.getClass(),"产权变更","设备【"+deviceId+"】变更为个人");
	}
	
	/**
	 * 修改产品公用账目使用类型
	 * @param prod
	 * @throws Exception
	 */
	public void changeProdPubAcctType(CProd prod) throws Exception{
		CProdPropChange change = new CProdPropChange("public_acctitem_type",prod.getPublic_acctitem_type(),SystemConstants.PUBLIC_ACCTITEM_TYPE_NONE);
		List<CProdPropChange> changeList = new ArrayList<CProdPropChange>();
		changeList.add(change);
		
		SOptr optr = gOptr(prod.getArea_id(),prod.getCounty_id());
		BusiParameter p = new BusiParameter();
		p.setBusiCode(BusiCodeConstants.PROD_CHANGE_PUBACCTTYPE);
		p.setOptr(optr);
		userProdService.saveEditProd(p,prod.getProd_sn(), changeList);
		LoggerUtil.PrintInfo(this.getClass(),"公用账目使用类型变更","产品【"+prod.getProd_sn()+"】公用账目使用类型变更为"+SystemConstants.PUBLIC_ACCTITEM_TYPE_NONE);
	}

	/**
	 * 处理预报停
	 * @param stopJob
	 * @throws Exception
	 */
	public void userRequireStop(JUserStop stopJob) throws Exception {
		BusiParameter p = new BusiParameter();
		CUser user = cUserDao.findByKey(stopJob.getUser_id());
		if(user==null)
			return ;
		p.addUser(user);
//		if (user.getUser_type().equals(SystemConstants.USER_TYPE_ATV)) {
//			List<CUserAtv> atvs = new ArrayList<CUserAtv>();
//			atvs.add(cUserAtvDao.queryAtvById(user.getUser_id()));
//			p.setSelectedAtvs(atvs);
//		} else if (user.getUser_type().equals(SystemConstants.USER_TYPE_DTV)) {
//			List<CUserDtv> dtvs = new ArrayList<CUserDtv>();
//			dtvs.add(cUserDtvDao.queryDtvById(user.getUser_id()));
//			p.setSelectedDtvs(dtvs);
//		} else if (user.getUser_type().equals(SystemConstants.USER_TYPE_BAND)) {
//			List<CUserBroadband> bands = new ArrayList<CUserBroadband>();
//			bands.add(cUserBroadbandDao.queryBandById(user.getUser_id()));
//			p.setSelectedBands(bands);
//		}
		CustFullInfoDto custFullInfo = new CustFullInfoDto();
		custFullInfo.setCust(cCustDao.findByKey(user.getCust_id()));
		p.setCustFullInfo(custFullInfo);
		p.setBusiCode(BusiCodeConstants.USER_REQUIRE_STOP);
		SOptr optr = gOptr(user.getArea_id(),user.getCounty_id());
		p.setOptr(optr);
		userService.saveStop(p,DateHelper.formatNow(),0);
		LoggerUtil.PrintInfo(this.getClass(),"报停","用户【"+user.getUser_id()+"】报停成功");
	}

	/**
	 * 处理自动加授权
	 * @param prodList
	 * @throws Exception
	 */
	public void saveAutoBusiCmd(List<CProdDto> prodList) throws Exception {
		List<JBusiCmd> busiCmdList = new ArrayList<JBusiCmd>();		
		for(CProdDto dto: prodList){
			JBusiCmd busiCmdJob = new JBusiCmd();
			busiCmdJob.setJob_id(getJobId());
			busiCmdJob.setDone_code(-2);
			busiCmdJob.setBusi_cmd_type(BusiCmdConstants.ACCTIVATE_PROD);
			busiCmdJob.setCust_id(dto.getCust_id());
			busiCmdJob.setUser_id(dto.getUser_id());
			busiCmdJob.setStb_id(dto.getStb_id());
			busiCmdJob.setCard_id(dto.getCard_id());
			busiCmdJob.setModem_mac(dto.getModem_mac());
			busiCmdJob.setProd_sn(dto.getProd_sn());
			busiCmdJob.setArea_id(dto.getArea_id());
			busiCmdJob.setCounty_id(dto.getCounty_id());
			busiCmdJob.setPriority(SystemConstants.PRIORITY_SSSQ);
			
			//是产品指令
			if (dto.getProd_type().equals(SystemConstants.PROD_TYPE_BASE)){
				busiCmdJob.setProd_id(dto.getProd_id());
				busiCmdList.add(busiCmdJob);
			} else {
				//在c_prod 表中去套餐子产品,因为要对子产品的动态资源发送授权，动态资源是根据子产品的prod_sn 取的。
				List<CProdDto> cprodDtoList = cProdDao.queryChildProdByPkgsn(dto.getProd_sn(), dto.getCounty_id());
 				if(null!=cprodDtoList){
					for (CProdDto pp: cprodDtoList){
						busiCmdJob.setProd_id(pp.getProd_id());
						busiCmdJob.setProd_sn(pp.getProd_sn());
						busiCmdList.add(busiCmdJob);
					}
				}
			}
		}
		jBusiCmdDao.save(busiCmdList.toArray(new JBusiCmd[busiCmdList.size()]));
	
	}
	private int getJobId() throws Exception{
		return Integer.parseInt(jBusiCmdDao.findSequence(SequenceConstants.SEQ_JOB_ID).toString());
	}
	/**
	 * 处理自动退订
	 * @param prodSns
	 * @param dto
	 * @throws Exception
	 */
	public void saveProdStop(CProdDto prod) throws Exception {
		BusiParameter p = new BusiParameter();
		CUser user = cUserDao.findByKey(prod.getUser_id());
//		if (user.getUser_type().equals(SystemConstants.USER_TYPE_ATV)) {
//			List<CUserAtv> atvs = new ArrayList<CUserAtv>();
//			atvs.add(cUserAtvDao.queryAtvById(user.getUser_id()));
//			p.setSelectedAtvs(atvs);
//		} else if (user.getUser_type().equals(SystemConstants.USER_TYPE_DTV)) {
//			List<CUserDtv> dtvs = new ArrayList<CUserDtv>();
//			dtvs.add(cUserDtvDao.queryDtvById(user.getUser_id()));
//			p.setSelectedDtvs(dtvs);
//		} else if (user.getUser_type().equals(SystemConstants.USER_TYPE_BAND)) {
//			List<CUserBroadband> bands = new ArrayList<CUserBroadband>();
//			bands.add(cUserBroadbandDao.queryBandById(user.getUser_id()));
//			p.setSelectedBands(bands);
//		}
		
		p.addUser(user);
		CustFullInfoDto custFullInfo = new CustFullInfoDto();
		custFullInfo.setCust(cCustDao.findByKey(user.getCust_id()));
		p.setCustFullInfo(custFullInfo);
		p.setBusiCode(BusiCodeConstants.JOB_PROD_STOP);
		SOptr optr = gOptr(prod.getArea_id(),prod.getCounty_id());
		//判断是否有欠费，如果有欠费修改账单的实际出帐金额
		if (prod.getAll_balance()-prod.getOwe_fee()-prod.getReal_bill()<0){
			if (prod.getOwe_fee()==0){
				//有往月欠费
				//更新本月账单为all_balance
				this.busiDao.updateNotConfirmBill(prod.getProd_sn(), prod.getAll_balance(),prod.getCounty_id());
			} else {
				//删除当月账单
				this.busiDao.delNotConfirmBill(prod.getProd_sn(),prod.getCounty_id());
				//更新历史账单欠费为0
				this.busiDao.updateConfirmedBill(prod.getProd_sn(),prod.getCounty_id());
			}
		}
		//终止产品
		p.setOptr(optr);
		String[] prodSn = new String[1];
		prodSn[0] = prod.getProd_sn();
		userProdService.saveTerminate(p,prodSn, "JOB_PROD_STOP", null, null);
		LoggerUtil.PrintInfo(this.getClass(),"自动退订","客户【"+user.getCust_id()+"】下的产品编号【"+prod.getProd_id()+"】退订成功");
	}
	
	/**
	 * 将套餐子产品从套餐中移走
	 * @param prod
	 * @throws Exception
	 */
	public void saveEditPkg(CProdDto prod) throws Exception{
		SOptr optr = gOptr(prod.getArea_id(),prod.getCounty_id());
		BusiParameter p = new BusiParameter();
		p.setOptr(optr);
		CustFullInfoDto custFullInfo = new CustFullInfoDto();
		custFullInfo.setCust(cCustDao.findByKey(prod.getCust_id()));
		p.setCustFullInfo(custFullInfo);
		p.setBusiCode(BusiCodeConstants.PKG_EDIT);
		
		userProdService.saveEditCustPkg(p, prod.getProd_sn());
		
		LoggerUtil.PrintInfo(this.getClass(),"修改客户套餐","客户【"+prod.getCust_id()+"】下的产品编号【"+prod.getProd_id()+"】从套餐编号"+"prod.getPackage_sn()"+"移走");
	}
	
	
	/**
	 * 账目余额解冻
	 * @param unfreezeJob
	 * @throws Exception
	 */
	public void acctUnfreeze(CAcctAcctitemInactive unfreezeJob)throws Exception{
		SOptr optr = gOptr(unfreezeJob.getArea_id(),unfreezeJob.getCounty_id());
		BusiParameter p = new BusiParameter();
		p.setOptr(optr);
		int fee = acctService.saveAcctUnfreeze(p,unfreezeJob);
		LoggerUtil.PrintInfo(this.getClass(),"资金解冻","账目【"+unfreezeJob.getAcct_id()+"|"+unfreezeJob.getAcctitem_id()+"】解冻了"+fee+"分");

	}
	
	/**
	 * 银行签约
	 * @throws Exception
	 */
	public boolean saveSignBank(CBankAgree bank) throws Exception {
		CCust cust = cCustDao.queryCustByCustNo(bank.getB_asnb());

		if (null == cust) {
			throw new ComponentException("错误的客户编号：" + bank.getB_asnb());
		}

		CustFullInfoDto cfd = new CustFullInfoDto();
		cfd.setCust(cust);

		SOptr optr = gOptr(cust.getArea_id(), cust.getCounty_id());
		BusiParameter p = new BusiParameter();
		p.setOptr(optr);
		p.setCustFullInfo(cfd);
		p.setBusiCode(BusiCodeConstants.ACCT_BANK_AGREE);

		try {
			acctService.saveSignBank(p, bank);
		} catch (Exception e) {
			throw new ComponentException("签约失败，客户编号：" + cust.getCust_no()
					+ e.getMessage());
		}
		return true;
	}
	
	/**
	 * 取消银行签约
	 * @throws Exception
	 */
	public boolean saveRemoveSignBank(CBankAgree bank) throws Exception {
		CCust cust = cCustDao.queryCustByCustNo(bank.getB_asnb());

		if (null == cust) {
			throw new ComponentException("错误的客户编号：" + bank.getB_asnb());
		}

		CustFullInfoDto cfd = new CustFullInfoDto();
		cfd.setCust(cust);

		SOptr optr = gOptr(cust.getArea_id(), cust.getCounty_id());
		BusiParameter p = new BusiParameter();
		p.setOptr(optr);
		p.setCustFullInfo(cfd);
		p.setBusiCode(BusiCodeConstants.ACCT_BANK_CANCEL_AGREE);
		Date time = DateHelper.parseDate(bank.getB_wkdt(),
				DateHelper.FORMAT_YMD);

		try {
			acctService.saveRemoveSignBank(p,
					SystemConstants.PAY_TYPE_BANK_DEDU, time);
		} catch (Exception e) {
			throw new ComponentException("解约失败，客户编号：" + cust.getCust_no()
					+ e.getMessage());
		}
		return true;
	}
	
	/**
	 * 保存银行扣费记录
	 * @throws Exception
	 */
	public void saveBankGotodisk(String fileNo)throws Exception{
		busiDao.saveBankGotodisk(fileNo);
	}
	
	public void updateBankReturn(CBankReturn cbr2)throws Exception {
		cBankReturnDao.update(cbr2);
	}
	
	public void updateBankReturnPayerror(CBankReturnPayerror dest)throws Exception {
		cBankReturnPayerrorDao.update(dest);
	}
	
	public void saveBankReturnPayerror(CBankReturnPayerror dest)throws Exception {
		cBankReturnPayerrorDao.save(dest);
	}
	
	/**
	 * 保存银行回盘记录
	 * @return
	 * @throws Exception
	 */
	public void saveBankReturn(List<CBankReturn> list)throws Exception{
		busiDao.saveBankReturn(list);
	}
	
	public void queryBankReturn(DataHandler<CBankReturn> handler)throws Exception{
		cBankReturnDao.queryBankList(handler);
		
	}
	
	/**
	 * 保存银行缴费
	 * @return
	 * @throws Exception
	 */
	public boolean saveBankPk(CBankReturn bank, CBankGotodisk cbg)throws Exception{
		SOptr optr = gOptr(cbg.getArea_id(), cbg.getCounty_id());
		BusiParameter p = new BusiParameter();
		CustFullInfoDto custFullInfo = new CustFullInfoDto();
		CCust cust = cCustDao.findByKey(cbg.getCust_id());
		custFullInfo.setCust(cust);
		p.setCustFullInfo(custFullInfo);
		p.setOptr(optr);
		p.setBusiCode(BusiCodeConstants.ACCT_BANK_PAY);
		
		try{
			int doneCode = acctService.saveBankPk(p, bank.getBank_trans_sn(), cbg.getAcct_id(),cbg.getCust_id(),
				cbg.getAcctitem_id(), cbg.getStart_date(), cbg.getEnd_date(),
				cbg.getFee(), cbg.getUser_id(),cbg.getProd_sn());
			bank.setPay_done_code(doneCode);
			LoggerUtil.PrintInfo(this.getClass(), "银行缴费",
					"交易流水【" + bank.getBank_trans_sn() + "|客户号" + cbg.getCust_id()
							+ "】金额: " + cbg.getFee());
			return true;
		}catch(Exception e){
			e.printStackTrace();
			bank.setPay_failure_reason(e.getMessage());
		}
		return false;
	}

	public CBankGotodisk findBankGotodisk(String trans_sn)throws Exception{
		return cBankGotodiskDao.findByKey(trans_sn);
	}
	
	public CBankRefundtodisk findBankRefundtodisk(String trans_sn)throws Exception{
		return cBankRefundtodiskDao.findByKey(trans_sn);
	}
	
	/**
	 * 查询银行扣费记录
	 * @throws Exception
	 */
	public void queryBankGotodisk(String fileNo, DataHandler<CBankGotodisk> handler)throws Exception{
		busiDao.queryBankGotoDisk(fileNo, handler);
	}
	
	/**
	 * 保存银行退款记录
	 * @throws Exception
	 */
	public void saveBankRefundtodisk(String fileNo)throws Exception{
		busiDao.saveBankRefundtodisk(fileNo);
	}
	
	public void updateBankReturnPayError(CBankReturnPayerror error)throws Exception{
		this.cBankReturnPayerrorDao.save(error);
	}
	
	/**
	 * 查询银行退款记录
	 * @throws Exception
	 */
	public void queryBankRefundtoDisk(String fileNo, DataHandler<CBankGotodisk> handler)throws Exception{
		busiDao.queryBankRefundtoDisk(fileNo, handler);
	}
	
	
	/**
	 * 取消预扣费
	 * @param acctPreFee
	 * @throws Exception 
	 */
	public void cancelAcctPreFee(CAcctPreFee acctPreFee) throws Exception{
		SOptr optr = gOptr(acctPreFee.getArea_id(), acctPreFee.getCounty_id());
		BusiParameter p = new BusiParameter();
		CustFullInfoDto custFullInfo = new CustFullInfoDto();
		CCust cust = cCustDao.findByKey(acctPreFee.getCust_id());
		custFullInfo.setCust(cust);
		p.setCustFullInfo(custFullInfo);
		p.setOptr(optr);
		
		acctService.cancelVodPreFee(p,acctPreFee);
		
		LoggerUtil.PrintInfo(this.getClass(),"取消预扣费","交易流水【"+acctPreFee.getTrans_id()+"|用户编号"+acctPreFee.getUser_id()+"】取消了"+acctPreFee.getFee()+"分");
	}

	/**
	 * 设置用户产品之间的包含关系
	 * @param custId
	 * @param userId
	 * @param countyId
	 * @param cfgData
	 * @throws Exception
	 */
	public int setProdInclude(Integer doneCode,String custId,String userId,String countyId,String areaId) throws Exception{
		LoggerHelper.debug(getClass(), "开始处理【产品包含】，客户ID:"+custId+",用户ID："+userId+",地区ID："+countyId);
		//查找用户产品
		List<CProd> prodList = busiDao.queryUserProd(userId, countyId);
		//获取产品的res信息
		List<ProdRes> prodResList = new ArrayList<ProdRes>();
		for (CProd prod:prodList){
			ProdRes pr = new ProdRes();
			pr.setProdSn(prod.getProd_sn());
			if (prod.getProd_type().equals(SystemConstants.PROD_TYPE_BASE)){
				//查询基本产品所有资源
				List<String> resList= jBusiCmdDao.queryBaseProdRes(pr.getProdSn());
				pr.getResList().addAll(resList);
				
				
				prodResList.add(pr);
			} else {
				List<String> dynRes= jBusiCmdDao.queryDnyRes(pr.getProdSn());
				pr.getResList().addAll(dynRes);
				
				//查找套餐对应的子产品
				List<CProd> childProdList = cProdDao.queryByPkgSn(pr.getProdSn(), countyId);
				for (CProd cp:childProdList){
					//查询基本产品所有资源
					List<String> resList= jBusiCmdDao.queryBaseProdRes(cp.getProd_sn());
					pr.getResList().addAll(resList);
				}
				
				prodResList.add(pr);
			}
			
			String resArr= null;
			for(String res : pr.getResList()){
				resArr = StringHelper.append(resArr,res,",");
			}
			LoggerHelper.debug(getClass(), "产品prod_sn:"+prod.getProd_sn()+",资源："+resArr);
		}
		//比较资源之间的包含关系
		List<CProdInclude> includeList =new ArrayList<CProdInclude>();
		for (int i=0;i<prodResList.size()-1;i++){
			ProdRes pr = prodResList.get(i);
			for (int j=i+1;j<prodResList.size();j++){
				ProdRes pr1 = prodResList.get(j);
				if (pr1.getResList() == null || pr1.getResList().size()==0)
					continue;
				int k = CollectionHelper.compare(pr.getResList(), pr1.getResList());
				if (k !=0){
					CProdInclude include = new CProdInclude();
					include.setCust_id(custId);
					include.setUser_id(userId);
					if (k==1){
						include.setProd_sn(pr.getProdSn());
						include.setInclude_prod_sn(pr1.getProdSn());
					} else {
						include.setProd_sn(pr1.getProdSn());
						include.setInclude_prod_sn(pr.getProdSn());
					}
					
					LoggerHelper.debug(getClass(), "一条包含数据，prod_sn:"+include.getProd_sn()+",被包含prod_sn："+include.getInclude_prod_sn());
					includeList.add(include);
				}
			}
		}
		//保存比较结果
		busiDao.saveProdInclude(userId, includeList);
		//插入信控
		if(includeList.size() > 0){
			JCustCreditCal creditCal = new JCustCreditCal();
			creditCal.setJob_id(getJobId());
			creditCal.setDone_code(doneCode);
			creditCal.setCust_id(custId);
			creditCal.setCounty_id(countyId);
			creditCal.setArea_id(areaId);
			creditCal.setCredit_exec(SystemConstants.BOOLEAN_TRUE);
			jCustCreditCalDao.save(creditCal);
		}
		
		LoggerHelper.debug(getClass(), "【产品包含】处理结束，用户ID："+userId+",共："+includeList.size()+"条数据");
		
		return includeList.size();
	}
	
	public void saveProdIncludeRecord(Integer doneCode, String custId, String userId,
			String countyId, String isSuccess,String errorInfo) throws JDBCException {
		busiDao.saveProdIncludeRecord(doneCode,custId,userId,countyId,isSuccess,errorInfo);
	}
	
	/**
	 * 重新设置用户产品资源
	 * @param custId 
	 * @param unfreezeJob
	 * @throws Exception
	 */
	public void resetUserProdRes(String custId)throws Exception{
		CCust cust = cCustDao.findByKey(custId);
		List<CCust> unitList = cCustDao.queryUnitByResident(cust.getCust_id(), cust.getCounty_id());
		if (unitList != null && unitList.size()>0)
			cust.setUnit_id(unitList.get(0).getCust_id());
		SOptr optr = gOptr(cust.getArea_id(),cust.getCounty_id());
		BusiParameter p = new BusiParameter();
		CustFullInfoDto custFullInfo = new CustFullInfoDto();
		custFullInfo.setCust(cust);
		p.setCustFullInfo(custFullInfo);
		p.setOptr(optr);
		userProdService.resetUserProdRes(p);
		LoggerUtil.PrintInfo(this.getClass(),"重置产品资源","设置客户【"+cust.getCust_name()+"】的产品资源");

	}

	public void promotion(String userId) throws Exception{
		//查找用户基本信息
		CUser user = busiDao.queryUser(userId);
		if (user == null)
			throw new ComponentException("用户不存在userId="+userId);
		CCust cust = busiDao.queryCust(user.getCust_id());
		if (cust == null)
			throw new ComponentException("客户不存在custId="+user.getCust_id());
//		CUserAtv userAtv = new CUserAtv();
//		CUserDtv userDtv = new CUserDtv();
//		CUserBroadband userBroadband = new CUserBroadband();
//		if (user.getUser_type().equals(SystemConstants.USER_TYPE_ATV))
//			userAtv = cUserAtvDao.queryAtvById(user.getUser_id());
//		else if (user.getUser_type().equals(SystemConstants.USER_TYPE_DTV))
//			userDtv =  cUserDtvDao.queryDtvById(user.getUser_id());
//		else if (user.getUser_type().equals(SystemConstants.USER_TYPE_BAND))
//			userBroadband = cUserBroadbandDao.queryBandById(user.getUser_id());
		//获取用户当日累积缴费
		List<CFee> feeList = cFeeDao.queryUserFee(user.getCust_id(),userId);
		List<AcctitemDto> balanceList = cAcctAcctitemDao.queryAcctAndAcctItemByUserId(cust.getCust_id(),userId,cust.getCounty_id());
		//获取用户当日参加过的促销主题
		List<String> userPromotionList = busiDao.queryUserPromotionTheme(userId);
		//遍历自动促销，判断用户可以参加哪些促销
		
		List<CProdDto> prodList = cProdDao.queryUserProd(userId, user.getCounty_id());
		
		expressionUtil.setAllValue(cust, user, null,null,null, feeList, balanceList,prodList);
		expressionUtil.setCuserStb(cUserDao.queryUserStbByUserId(userId));
		

		
		SOptr optr = gOptr(user.getArea_id(),user.getCounty_id());
		for (PPromotionDto promotion:BusiCfgDataJob.CFG.getPromotionList()){
			if (promotion.getAuto_exec().equals(SystemConstants.BOOLEAN_TRUE) && 
					promotion.getCounty_id().equals(user.getCounty_id())){
				//判断是否参加过同主题的促销
				boolean flag=false;
				for (String themeId:userPromotionList){
					if (promotion.getTheme_id().equals(themeId)){
						flag=true;
						break;
					}
				}
				if (flag)
					continue;
				//计算可以参加促销的次数
				int repetitionTimes = expressionUtil.parsePromotion(BusiCfgDataJob.CFG.getRuleStr(promotion.getRule_id()),
						 promotion.getRepetition_times());
				if (repetitionTimes>0){
					BusiParameter p = new BusiParameter();
					p.setBusiCode(BusiCodeConstants.PROMOTION_AUTO);

//					if (user.getUser_type().equals(SystemConstants.USER_TYPE_ATV)) {
//						List<CUserAtv> atvs = new ArrayList<CUserAtv>();
//						atvs.add(userAtv);
//						p.setSelectedAtvs(atvs);
//					} else if (user.getUser_type().equals(SystemConstants.USER_TYPE_DTV)) {
//						List<CUserDtv> dtvs = new ArrayList<CUserDtv>();
//						dtvs.add(userDtv);
//						p.setSelectedDtvs(dtvs);
//					} else if (user.getUser_type().equals(SystemConstants.USER_TYPE_BAND)) {
//						List<CUserBroadband> bands = new ArrayList<CUserBroadband>();
//						bands.add(userBroadband);
//						p.setSelectedBands(bands);
//					}
					
					p.addUser(user);
					CustFullInfoDto custFullInfo = new CustFullInfoDto();
					custFullInfo.setCust(cust);
					p.setCustFullInfo(custFullInfo);
					p.setOptr(optr);
					userService.savePromotion(p,repetitionTimes,promotion.getPromotion_id(), null, promotion.getAcctList());
					LoggerUtil.PrintInfo(this.getClass(),"自动促销","用户"+user.getUser_id()+
							"参加了【"+promotion.getPromotion_id()+promotion.getPromotion_name()+"】促销");
					userPromotionList.add(promotion.getTheme_id());
				}
			}
		}
	}
	
	public void cancelPromotion(String custId) throws Exception{
		// 查找客户参加过的促销
		List<CPromotion> promotionList = busiDao.queryCustPromotion(custId);
		CCust cust = busiDao.queryCust(custId);
		if (cust==null) return ;
		SOptr optr = gOptr(cust.getArea_id(),cust.getCounty_id());
		for (CPromotion cp :promotionList){
			CUser user = busiDao.queryUser(cp.getUser_id());
			if (user == null)
				continue;
			
//			CUserAtv userAtv = new CUserAtv();
//			CUserDtv userDtv = new CUserDtv();
//			CUserBroadband userBroadband = new CUserBroadband();
//			if (user.getUser_type().equals(SystemConstants.USER_TYPE_ATV))
//				userAtv = cUserAtvDao.queryAtvById(user.getUser_id());
//			else if (user.getUser_type().equals(SystemConstants.USER_TYPE_DTV))
//				userDtv =  cUserDtvDao.queryDtvById(user.getUser_id());
//			else if (user.getUser_type().equals(SystemConstants.USER_TYPE_BAND))
//				userBroadband = cUserBroadbandDao.queryBandById(user.getUser_id());
			//获取用户当日累积缴费
			List<CFee> feeList = cFeeDao.queryUserFee(cp.getCust_id(),cp.getUser_id());
			List<AcctitemDto> balanceList = cAcctAcctitemDao.queryAcctAndAcctItemByUserId(custId,cp.getUser_id(),cust.getCounty_id());
			//设备公式的参数
			
			List<CProdDto> prodList = cProdDao.queryUserProd(user.getUser_id(), user.getCounty_id());
			expressionUtil.setAllValue(cust, user,null,null,null, feeList, balanceList,prodList);
			for (PPromotionDto promotion:BusiCfgDataJob.CFG.getPromotionList()){
				if (promotion.getPromotion_id().equals(cp.getPromotion_id())){
					int repetitionTimes = expressionUtil.parsePromotion(BusiCfgDataJob.CFG.getRuleStr(promotion.getRule_id()),
							 promotion.getRepetition_times());
					if (repetitionTimes<1){
						//取消促销
						BusiParameter p = new BusiParameter();
						p.setBusiCode(BusiCodeConstants.PROMOTION_CANCEL);
						CustFullInfoDto custFullInfo = new CustFullInfoDto();
						custFullInfo.setCust(cust);
						p.setCustFullInfo(custFullInfo);
						p.setOptr(optr);
						userService.saveCancelPromotion(p,cp.getPromotion_sn());
						LoggerUtil.PrintInfo(this.getClass(),"促销回退","用户"+user.getUser_id()+
								"回退了【"+promotion.getPromotion_id()+promotion.getPromotion_name()+"】促销");
					}
					break;
				}
			}
		}
	}
	
	public List<SCounty> queryAllCounty() throws Exception {
		return sCountyDao.findAll();
	}

	public List<CUser> queryOwnFeeUser(String countyId) throws Exception {
		return cUserDao.queryOwnFeeUser(countyId,queryTemplateConfig(TemplateConfigDto.Config.OWN_LONG_DAYS.toString(), countyId));
	}
	
	public void modifyUserStatus(List<CUser> userList) throws Exception {
		CUser cuser = userList.get(0);
		SOptr optr = gOptr(cuser.getArea_id(),cuser.getCounty_id());
		BusiParameter p = new BusiParameter();
		p.setBusiCode(BusiCodeConstants.OWN_LONG);
		p.setOptr(optr);
		
		List<CUserPropChange> propChangeList = null;
		CUserPropChange cpc = null;
		for(CUser user : userList){
			String userId = user.getUser_id();
			propChangeList = new ArrayList<CUserPropChange>();
			cpc = new CUserPropChange();
			cpc.setUser_id(userId);
			cpc.setArea_id(user.getArea_id());
			cpc.setCounty_id(user.getCounty_id());
			cpc.setOld_value(user.getStatus());
			cpc.setColumn_name("status");
			cpc.setNew_value(StatusConstants.OWELONG);
			propChangeList.add(cpc);
			
			cpc = new CUserPropChange();
			cpc.setUser_id(userId);
			cpc.setArea_id(user.getArea_id());
			cpc.setCounty_id(user.getCounty_id());
			cpc.setOld_value(DateHelper.dateToStr(user.getStatus_date()));
			cpc.setColumn_name("status_date");
			cpc.setNew_value(DateHelper.dateToStr(new Date()));
			propChangeList.add(cpc);
			
//			String userType = user.getUser_type();
//			if(userType.equals(SystemConstants.USER_TYPE_ATV)){
//				List<CUserAtv> selectedAtvs = new ArrayList<CUserAtv>();
//				CUserAtv userAtv = new CUserAtv();
//				userAtv.setUser_id(userId);
//				selectedAtvs.add(userAtv);
//				p.setSelectedAtvs(selectedAtvs);
//			}else if(userType.equals(SystemConstants.USER_TYPE_DTV)){
//				List<CUserDtv> selectedDtvs = new ArrayList<CUserDtv>();
//				CUserDtv userDtv = new CUserDtv();
//				userDtv.setUser_id(userId);
//				selectedDtvs.add(userDtv);
//				p.setSelectedDtvs(selectedDtvs);
//			}
			
			p.addUser(user);
			
			CustFullInfoDto dto = new CustFullInfoDto();
			CCust cust = new CCust();
			cust.setCust_id(user.getCust_id());
			dto.setCust(cust);
			p.setCustFullInfo(dto);
			
			userService.editUserStatus(p, propChangeList);
		}
	}
	
	/**
	 * 计算客户到期日
	 * @param custId
	 * @param countyId
	 */
	public void calprodInvalidDate(String custId, String countyId) {
		// TODO Auto-generated method stub
		
	}

	
	public void delAcctItem(int jobId,String acctId, String acctItemId,Integer doneCode) throws Exception{
		CAcct acct = this.busiDao.queryAcct(acctId);
		if (acct==null) return;
		SOptr optr = gOptr(acct.getArea_id(),acct.getCounty_id());
		BusiParameter p = new BusiParameter();
		p.setBusiCode(BusiCodeConstants.PROD_TERMINATE);
		p.setOptr(optr);
		acctService.saveDelAcctItem(p,acctId, acctItemId,doneCode);
	}
	
	public boolean canRemove(int jobId,String acctId, String acctItemId) throws Exception{
		if ( jobDao.qureyJobCount(jobId) ==0)
			return true;
		else 
			return false;
	}
	
	public boolean canCancelPromotion(String custId) throws Exception{
		if ( jobDao.qureyJobCount(custId) ==0)
			return true;
		else 
			return false;
	}
	
	/**
	 * 补入非公用账目充值的账目异动数据
	 * @param doneCode
	 * @throws Exception 
	 */
	public void adjustSpecAcctPay(Integer doneCode) throws Exception {
		List<CAcctAcctitemChange> changeList = cAcctAcctitemChangeDao.querybyDoneCode(doneCode);
		
		if(null != changeList && changeList.size() > 0){
			SOptr optr = gOptr(changeList.get(0).getArea_id(),changeList.get(0).getCounty_id());
			BusiParameter p = new BusiParameter();
			p.setOptr(optr);
			
			acctService.saveAdjustSpecAcctPay(p,changeList,doneCode);
			
			LoggerUtil.PrintInfo(this.getClass(),"补入非公用账目充值的账目异动数据","客户"+changeList.get(0).getCust_id()+
					"流水"+doneCode+"共"+changeList.size()+"条缴费异动");
		}
	}

	/**
	 * 添加解冻对应 赠送 缴费冲正记录
	 * 修改活动余额
	 * 修改账目信息
	 * @param changeAcctitem	账目解冻异动	
	 * @param doneCode
	 * @throws Exception
	 */
	public void dealReversalChange(CAcctAcctitemChange changeAcctitem,Integer doneCode) throws Exception {
		
		//当前解冻为止，赠送金额之和
		int presentBalance = jobDao.queryPresentBalance(changeAcctitem
				.getAcct_id(), changeAcctitem.getAcctitem_id(), DateHelper
				.format(changeAcctitem.getDone_date()));
		
		CAcctAcctitemChange change = new CAcctAcctitemChange();
		int changeFee = changeAcctitem.getChange_fee();				//变更金额
		
		BeanUtils.copyProperties(changeAcctitem, change);
		
		change.setDone_code(doneCode);
		change.setBusi_code(BusiCodeConstants.ACCT_PAY);			//缴费
		change.setChange_type(SystemConstants.ACCT_CHANGE_UNCFEE);	//缴费冲正
		change.setChange_fee(changeFee*-1);
		change.setPre_fee(presentBalance);							//上次余额
		change.setFee(presentBalance - changeFee);					//当前余额为上次余额减去变更金额
		cAcctAcctitemChangeDao.save(change);
		
		String acctId = changeAcctitem.getAcct_id();
		String acctItemId = changeAcctitem.getAcctitem_id();
		String feeType = SystemConstants.ACCT_FEETYPE_PRESENT;	//赠送
		String countyId = changeAcctitem.getCounty_id();
		CAcctAcctitemActive activeItem = jobDao.queryActiveAcctitem(acctId, acctItemId, feeType, countyId);
		//是否有赠送活动余额
		if(activeItem != null){
			int balance = activeItem.getBalance();
			if(balance >= changeFee){
				//活动余额大于解冻金额
				jobDao.updateActiveAcctitemBalance(changeFee*-1, acctId, acctItemId, feeType, countyId);
			}
		}
		
		cAcctAcctitemDao.updateActiveBanlance(acctId, acctItemId, changeFee*-1, changeFee, 0, 0, countyId);
//		LoggerUtil.PrintMessage("冲正后解冻金额未冲掉","账目【"+acctId+"|"+acctItemId+"】异动金额"+changeFee*-1+"分");
	}
	
	private SOptr gOptr(String areaId,String countyId) {
		SOptr optr = new SOptr();
		optr.setCounty_id(StringUtils.isEmpty(countyId) ? "4501" : countyId);
		optr.setArea_id(StringUtils.isEmpty(areaId) ? "4501" : areaId);
		optr.setOptr_id("0");
		optr.setDept_id(StringUtils.isEmpty(countyId) ? "4501" : countyId);
		return optr;
	}
	
	
	public List<CUser> queryProdIncludeUser(String countyId) throws JDBCException {
		return jobDao.queryProdIncludeUser(countyId);
	}
	
	public boolean isExistsUnBillByProdSn(String prodSn) throws Exception {
		return busiDao.isExistsUnBillByProdSn(prodSn);
	}

	public void updateProdStatus(Integer doneCode, String prodSn) throws Exception {
		CProd prod = cProdDao.findByKey(prodSn);
		//停机不改为暂停状态
		if(prod != null && StringHelper.isNotEmpty(prod.getUser_id())){
			SOptr optr = gOptr(prod.getArea_id(),prod.getCounty_id());
			BusiParameter p = new BusiParameter();
			p.setBusiCode(BusiCodeConstants.TEMP_PROD_PAUSE);
			p.setOptr(optr);
			
			CustFullInfoDto custFullDto = new CustFullInfoDto();
			CCust cust = new CCust();
			cust.setCust_id(prod.getCust_id());
			custFullDto.setCust(cust);
			p.setCustFullInfo(custFullDto);
			userProdService.pauseProd(p, doneCode, prod.getProd_sn(), prod.getUser_id());
		}
	}
	
	public void execPause(Integer doneCode, Integer jobId, String prodSn, String oldTariffId, String newTariffId){
		try {
			
			
			if(StringHelper.isNotEmpty(oldTariffId)){
				PProdTariff oldTariff = this.queryTariffById(oldTariffId);
				PProdTariff newTariff = this.queryTariffById(newTariffId);
				//包月资费不暂停
				if(oldTariff.getBilling_type().equals(SystemConstants.BILLING_TYPE_MONTH)
						&& newTariff.getBilling_type().equals(SystemConstants.BILLING_TYPE_MONTH)
						&& oldTariff.getBilling_cycle().intValue() == 1){
				
				//老资费是包年 ， 新资费是包月 或 包年 。
				}else{
					//无销账的账单,产品暂停
					boolean flag = this.isExistsUnBillByProdSn(prodSn);
					if(flag){
						this.updateProdStatus(doneCode, prodSn);
						LoggerUtil.PrintInfo(this.getClass(),"修改产品状态为暂停","产品【"+prodSn+"】");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			LoggerUtil.PrintInfo(this.getClass(),"修改产品状态暂停","产品【"+prodSn+"】, 异常: "+e.getMessage());
		}
	}
	
	public PProdTariff queryTariffById(String tariffId) throws Exception {
		return pProdTariffDao.findByKey(tariffId);
	}
	
	public List<JSmsRecord> queryReminderCustRecord(String countyId) throws Exception {
		return jSmsRecordDao.queryReminderCustRecord(countyId);
	}
	
	public void queryInvalidProd(Date invalidDate, DataHandler<CProdDto> dataHandler) throws Exception {
		busiDao.queryInvalidProd(invalidDate, dataHandler);
	}
	
	public List<CProdDto> queryInvalidProd(Date invalidDate) throws Exception {
		return busiDao.queryInvalidProd(invalidDate);
	}
	
	public void updateStopProd() {
		try {
			List<CProd> prodList = busiDao.queryStopProd();
			LoggerUtil.PrintInfo(this.getClass(),"巡查停机产品","修改记录数："+prodList.size());
			for(CProd prod : prodList){
				busiDao.updateStopProd(prod);
				LoggerUtil.PrintInfo(this.getClass(),"巡查停机产品","产品【"+prod.getProd_sn()+"】");
			}
		} catch (Exception e) {
			e.printStackTrace();
			LoggerUtil.PrintInfo(this.getClass(),"巡查停机产品","异常："+e.getMessage());
		}
	}

	public void dealInvalidProd(CProdDto prod) throws Exception {
		String prodSn = prod.getProd_sn();
		LoggerUtil.PrintInfo(this.getClass(), "按到期日停机", "产品【"+prod.getProd_sn()+"】");
		if( (prod.getAll_balance() - prod.getOwe_fee() - prod.getReal_bill() >= prod.getTariff_rent()/2) || prod.getInactive_balance() > 0 ){
			//实时余额 大于等于 产品半个月资费 或 有冻结金额，不删除余额，记录数据
			busiDao.saveNoDelProd(prod);
			busiDao.updateStopProd(prod);
			LoggerUtil.PrintInfo(this.getClass(), "按到期日停机", "产品【"+prod.getProd_sn()+"】实时余额 大于等于 产品半个月资费 或 有冻结金额，不删除余额，记录数据");
		}else{
			int oweFee = prod.getOwe_fee();
			if(oweFee > 0){
				LoggerUtil.PrintInfo(this.getClass(), "按到期日停机", "产品【"+prod.getProd_sn()+"】有欠费");
				//有欠费账单，插入一条负的总欠费调账账单
				int sumOweFee = busiDao.sumOweBill(prodSn, prod.getCounty_id()) * -1;
				busiDao.cancelOweBill(prodSn, sumOweFee);
				cAcctAcctitemDao.changeOwefee(true, prod.getAcct_id(), prod.getProd_id(), sumOweFee, prod.getCounty_id());
				
				busiDao.cancelRealBill(prodSn);
			}else{
				int balance = prod.getAll_balance();
				if(balance > 0){
					LoggerUtil.PrintInfo(this.getClass(), "按到期日停机", "产品【"+prod.getProd_sn()+"】有余额");
					busiDao.cancelRealBill(prodSn, balance);
					cAcctAcctitemDao.changeOwefee(true, prod.getAcct_id(), prod.getProd_id(), balance, prod.getCounty_id());
				}
			}
			//删除日租
			busiDao.delRentFee(prodSn);
			//当天到期
			if(DateHelper.dateToStr(prod.getInvalid_date()).equals(DateHelper.dateToStr(DateHelper.now()))){
				//包月基本包变更 状态为欠费停机（是否欠费停由后台账务控制,标记为欠费未停,为了变更账务模式程序不会重复判断） 
				//变更为按账务模式计费
				busiDao.saveProdProp(prodSn, SystemConstants.BOOLEAN_FALSE, StatusConstants.OWENOTSTOP);
			}
		}
		
//		jobDao.createCustWriteOffJob(prod.getCust_id(), SystemConstants.BOOLEAN_TRUE, prod.getArea_id(), prod.getCounty_id());
		this.createCustWriteOffJob(-3, prod.getCust_id(), SystemConstants.BOOLEAN_TRUE, prod.getArea_id(), prod.getCounty_id());
		
		//记录每天操作记录
		jobDao.saveInvalidCal(prod.getProd_sn(), prod.getArea_id(), prod.getCounty_id());
	}
	
	public void createCustWriteOffJob(Integer doneCode,String custId,String writeOff, String areaId, String countyId)throws Exception{
		JCustWriteoff writeOffJob = new JCustWriteoff();
		writeOffJob.setJob_id(getJobId());
		writeOffJob.setDone_code(doneCode);
		writeOffJob.setCust_id(custId);
		writeOffJob.setArea_id(areaId);
		writeOffJob.setCounty_id(countyId);
		writeOffJob.setWriteoff(writeOff);

		jCustWriteoffDao.save(writeOffJob);
	}
	
	public void deleteInvaliCal() throws Exception {
		jobDao.deleteInvalidCal();
	}

	public void setBusiDao(BusiDao busiDao) {
		this.busiDao = busiDao;
	}

	/**
	 * @param custDao the cCustDao to set
	 */
	public void setCCustDao(CCustDao custDao) {
		cCustDao = custDao;
	}

	/**
	 * @param busiCmdDao the jBusiCmdDao to set
	 */
	public void setJBusiCmdDao(JBusiCmdDao busiCmdDao) {
		jBusiCmdDao = busiCmdDao;
	}

	/**
	 * @param userDao the cUserDao to set
	 */
	public void setCUserDao(CUserDao userDao) {
		cUserDao = userDao;
	}

	/**
	 * @param userAtvDao the cUserAtvDao to set
	 */
	public void setCUserAtvDao(CUserAtvDao userAtvDao) {
		cUserAtvDao = userAtvDao;
	}

	/**
	 * @param userDtvDao the cUserDtvDao to set
	 */
	public void setCUserDtvDao(CUserDtvDao userDtvDao) {
		cUserDtvDao = userDtvDao;
	}

	/**
	 * @param userBroadbandDao the cUserBroadbandDao to set
	 */
	public void setCUserBroadbandDao(CUserBroadbandDao userBroadbandDao) {
		cUserBroadbandDao = userBroadbandDao;
	}

	/**
	 * @param feeDao the cFeeDao to set
	 */
	public void setCFeeDao(CFeeDao feeDao) {
		cFeeDao = feeDao;
	}

	public void setJobDao(JobDao jobDao) {
		this.jobDao = jobDao;
	}

	public void setCProdDao(CProdDao prodDao) {
		cProdDao = prodDao;
	}

	public void setExpressionUtil(ExpressionUtil expressionUtil) {
		this.expressionUtil = expressionUtil;
	}

	/**
	 * @param acctAcctitemDao the cAcctAcctitemDao to set
	 */
	public void setCAcctAcctitemDao(CAcctAcctitemDao acctAcctitemDao) {
		cAcctAcctitemDao = acctAcctitemDao;
	}
	

	public void setCAcctAcctitemChangeDao(
			CAcctAcctitemChangeDao acctAcctitemChangeDao) {
		this.cAcctAcctitemChangeDao = acctAcctitemChangeDao;
	}


	public void setCProdInvalidUpdateDao(CProdInvalidUpdateDao prodInvalidUpdateDao) {
		cProdInvalidUpdateDao = prodInvalidUpdateDao;
	}

	/**
	 * @param userService the userService to set
	 */
	public void setUserService(IUserServiceExternal userService) {
		this.userService = userService;
	}

	/**
	 * @param custService the custService to set
	 */
	public void setCustService(ICustServiceExternal custService) {
		this.custService = custService;
	}

	/**
	 * @param acctService the acctService to set
	 */
	public void setAcctService(IAcctServiceExternal acctService) {
		this.acctService = acctService;
	}

	/**
	 * @param userProdService the userProdService to set
	 */
	public void setUserProdService(IUserProdServiceExternal userProdService) {
		this.userProdService = userProdService;
	}

	public void setSCountyDao(SCountyDao countyDao) {
		sCountyDao = countyDao;
	}

	public void setPProdTariffDao(PProdTariffDao prodTariffDao) {
		pProdTariffDao = prodTariffDao;
	}

	public void setJCustCreditCalDao(JCustCreditCalDao custCreditCalDao) {
		jCustCreditCalDao = custCreditCalDao;
	}
	/**
	 * @param smsRecordDao the jSmsRecordDao to set
	 */
	public void setJSmsRecordDao(JSmsRecordDao smsRecordDao) {
		jSmsRecordDao = smsRecordDao;
	}
	public void saveJSmsRecord(List<JSmsRecord> recordList) throws JDBCException {
		jSmsRecordDao.save(recordList.toArray(new JSmsRecord[recordList.size()]));
	}
	/**
	 * 查询所有催缴内容
	 * @return
	 * @throws Exception
	 */
	public Map<String, BTaskInfo> queryPromptInfo() throws Exception {
		return bTaskInfoDao.queryTaskInfo();
	}
	/**
	 * @return the bTaskInfoDao
	 */
	public BTaskInfoDao getBTaskInfoDao() {
		return bTaskInfoDao;
	}
	/**
	 * @param taskInfoDao the bTaskInfoDao to set
	 */
	public void setBTaskInfoDao(BTaskInfoDao taskInfoDao) {
		bTaskInfoDao = taskInfoDao;
	}
	public void setCProdPropPatDao(CProdPropPatDao prodPropPatDao) {
		cProdPropPatDao = prodPropPatDao;
	}
	public void setCProdPropChangeDao(CProdPropChangeDao prodPropChangeDao) {
		cProdPropChangeDao = prodPropChangeDao;
	}

	public void setJCustInvalidCalDao(JCustInvalidCalDao custInvalidCalDao) {
		jCustInvalidCalDao = custInvalidCalDao;
	}
	public void setJCustWriteoffDao(JCustWriteoffDao custWriteoffDao) {
		jCustWriteoffDao = custWriteoffDao;
	}
	public void setCBankGotodiskDao(CBankGotodiskDao cBankGotodiskDao) {
		this.cBankGotodiskDao = cBankGotodiskDao;
	}

	public void setCBankReturnDao(CBankReturnDao cBankReturnDao) {
		this.cBankReturnDao = cBankReturnDao;
	}
	public void setCBankRefundtodiskDao(CBankRefundtodiskDao cBankRefundtodiskDao) {
		this.cBankRefundtodiskDao = cBankRefundtodiskDao;
	}

	public void setCBankReturnPayerrorDao(
			CBankReturnPayerrorDao cBankReturnPayerrorDao) {
		this.cBankReturnPayerrorDao = cBankReturnPayerrorDao;
	}

	public void savebankagree(List<CBankAgree> bankList) throws JDBCException {
		cBankAgreeDao.save(bankList.toArray(new CBankAgree[bankList.size()]));
	}

	public List<CBankAgree> quertyUnProcBankAgree() throws JDBCException {
		return cBankAgreeDao.queryUnProc();
	}

	public void successBankAgree(Integer agree_id) throws JDBCException {
		cBankAgreeDao.updateProcStatus(agree_id,SystemConstants.BOOLEAN_TRUE,"");
	}

	public void failBankAgree(Integer agree_id, String message) throws JDBCException {
		cBankAgreeDao.updateProcStatus(agree_id,SystemConstants.BOOLEAN_FALSE,message);
	}

	/**
	 * @param bankAgreeDao the cBankAgreeDao to set
	 */
	public void setCBankAgreeDao(CBankAgreeDao bankAgreeDao) {
		cBankAgreeDao = bankAgreeDao;
	}

	public void runBankReturn() throws Exception {
		SOptr optr = gOptr("5000", "5001");
		BusiParameter p = new BusiParameter();
		p.setOptr(optr);
		p.setBusiCode(BusiCodeConstants.ACCT_BANK_PAY);
		
		cBankReturnDao.updateFailure();
		List<BankReturnDto> unExecBankRetrunList = cBankReturnDao.queryUnExecBankRetrun();
		int threadnum=5;
		Integer num = unExecBankRetrunList.size()/threadnum;
		System.out.println("处理总数"+unExecBankRetrunList.size());
		System.out.println("num"+num);
		int s = 0;
		List<BankReturnDto> tList = new ArrayList<BankReturnDto>();
		for(BankReturnDto d :unExecBankRetrunList){
			tList.add(d);
			if (++s>num){
				new Thread(new BankExec(acctService,tList,p,cCustDao)).start();
				s=0;
				tList = new ArrayList<BankReturnDto>();
			}
		}
		new Thread(new BankExec(acctService,tList,p,cCustDao)).start();
	}

}


class BankExec extends Thread {
	private List<BankReturnDto> execList ;
	private IAcctServiceExternal acctService;
	private BusiParameter p;
	private CCustDao cCustDao;
	/**
	 * @param acctService2
	 * @param list
	 */
	public BankExec(IAcctServiceExternal acctService, List<BankReturnDto> execList,BusiParameter p,CCustDao cCustDao) {
		this.acctService = acctService;
		this.execList = execList;
		this.p = p;
		this.cCustDao = cCustDao;
	}
	public void run() {
		System.out.println("处理数量:"+execList.size());
		for (BankReturnDto r:execList) {
			try {
				CustFullInfoDto custFullInfo = new CustFullInfoDto();
				custFullInfo.setCust(cCustDao.findByKey(r.getCust_id()));
				p.setCustFullInfo(custFullInfo);
				acctService.runBankReturn(p,r);
			} catch (Exception e) {
				e.printStackTrace();
			}	
		}
	}
}