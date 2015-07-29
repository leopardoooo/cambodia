/**
 *
 */
package com.ycsoft.business.component.config;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TAddress;
import com.ycsoft.beans.config.TPublicAcctitem;
import com.ycsoft.beans.config.TRuleDefine;
import com.ycsoft.beans.core.acct.CAcctBank;
import com.ycsoft.beans.core.cust.CCust;
import com.ycsoft.beans.core.fee.CFee;
import com.ycsoft.beans.core.fee.CFeeDevice;
import com.ycsoft.beans.core.prod.CProd;
import com.ycsoft.beans.core.user.CUser;
import com.ycsoft.beans.core.user.CUserAtv;
import com.ycsoft.beans.core.user.CUserBroadband;
import com.ycsoft.beans.core.user.CUserDtv;
import com.ycsoft.beans.core.user.CUserStb;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.business.commons.pojo.BusiParameter;
import com.ycsoft.business.dao.config.TAddressDao;
import com.ycsoft.business.dao.config.TRuleDefineDao;
import com.ycsoft.business.dao.config.TRuleEditDao;
import com.ycsoft.business.dao.core.acct.CAcctBankDao;
import com.ycsoft.business.dao.core.cust.CCustDao;
import com.ycsoft.business.dao.core.fee.CFeeDao;
import com.ycsoft.business.dao.core.fee.CFeeDeviceDao;
import com.ycsoft.business.dao.core.prod.CProdDao;
import com.ycsoft.business.dao.core.user.CUserAtvDao;
import com.ycsoft.business.dao.core.user.CUserDao;
import com.ycsoft.business.dao.core.user.CUserDtvDao;
import com.ycsoft.business.dao.prod.PPromotionDao;
import com.ycsoft.business.dto.core.acct.AcctitemDto;
import com.ycsoft.business.dto.core.prod.CProdDto;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ComponentException;
import com.ycsoft.commons.helper.BeanHelper;
import com.ycsoft.commons.helper.DateHelper;
import com.ycsoft.commons.helper.LoggerHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.core.JDBCException;

/**
 * @author liujiaqi
 *
 */
@Component
public class ExpressionUtil {

	private static final String [] replaceParmer={"ACCTITEM","times","BALANCE"};
	private static String[] datefunname= {"TODAY","THISYEAR","THISMONTH"};

	public ExpressionUtil() {
	}

	private TAddressDao tAddressDao;
	private CCustDao cCustDao;
	private CFeeDeviceDao cFeeDeviceDao;
	private CUserDao cUserDao;
	private CUserDtvDao cUserDtvDao;
	private CUserAtvDao cUserAtvDao;
	private TRuleEditDao tRuleEditDao;
	private TRuleDefineDao tRuleDefineDao;
	@SuppressWarnings("unused")
	private PPromotionDao pPromotionDao;
	private CFeeDao cFeeDao;
	private CProdDao cProdDao;
	private CAcctBankDao cAcctBankDao;
	
	List<TPublicAcctitem> allAcctitem = null;

	private CCust ccust;
	private CUser cuser;
	private CUserDtv cuserdtv;
	private CUserAtv cuseratv;
	private CUserBroadband cuserBroadband;
	private CUserStb cuserStb;
	private CFeeDevice stb;
	private List<CFee> payacctitem;//当日支付的费用
	private List<AcctitemDto> balanceacctitem;//账户余额
	private List<CProdDto> orderprods;
	private Map<String,Integer> acctitemSumMap;
	private Date TODAY = DateHelper.today();
	private SOptr optr ;

	public void setAllValue(CCust cust, CUser user, CUserDtv userdtv,
			CUserAtv useratv, CUserBroadband userBroadband, List<CFee> feeList,List<AcctitemDto> balanceList,List<CProdDto> prodList) {
		ccust = cust;
		setCuser(user);
		cuserdtv = userdtv;
		cuseratv = useratv;
		cuserBroadband = userBroadband;
		payacctitem = feeList;
		balanceacctitem = balanceList;
		orderprods = prodList;
		converNull();
	}

	public void setAllValue(BusiParameter busiParam, List<CFee> feeList, List<AcctitemDto> balanceList,List<CProdDto> prodList) {
		ccust = busiParam.getCust();
		cuser = busiParam.getSelectedUsers().size() > 0 ? busiParam
				.getSelectedUsers().get(0) : null;
//		cuserdtv = busiParam.getSelectedDtvs().size() > 0 ? busiParam
//				.getSelectedDtvs().get(0) : null;
//		cuseratv = busiParam.getSelectedAtvs().size() > 0 ? busiParam
//				.getSelectedAtvs().get(0) : null;
//		cuserBroadband = busiParam.getSelectedBands().size() > 0 ? busiParam
//				.getSelectedBands().get(0) : null;
		payacctitem = feeList;
		balanceacctitem = balanceList;
		orderprods = prodList;
		optr = busiParam.getOptr();
		converNull();
	}

	private void converNull(){
		BeanHelper.setNullPropertyEmptyString(ccust);
		BeanHelper.setNullPropertyEmptyString(cuser);
		BeanHelper.setNullPropertyEmptyString(cuserdtv);
		BeanHelper.setNullPropertyEmptyString(cuseratv);
		BeanHelper.setNullPropertyEmptyString(cuserBroadband);
		BeanHelper.setNullPropertyEmptyString(optr);
	}
	
	public String afterDate(int day){
		return DateHelper.format(DateHelper.addDate(new Date(),day));
	}
	
	public String dateToString(Date d){
		return DateHelper.format(d);
	}

	public boolean parseBoolean(String ruleStr,String... params) {
		if (StringHelper.isEmpty(ruleStr))
			return true;
		try {
			context.setRootObject(this);
			for (int i = 0; i < params.length; i++) {
				getVariable("#param" + i + "=" + params[i]);
			}
			ruleStr = ruleParamerReplace(ruleStr);
			setupPayacctitem();
			setupBalanceacctitem();
			loadData(ruleStr);
			return getVariable(ruleStr, Boolean.class);
		} catch (Exception e) {
			LoggerHelper.error(getClass(), "表达式解析错误："+ruleStr+e.getMessage());
			return false;
		}
	}

	//装填缴费数据
	private void setupPayacctitem() throws JDBCException {
		if (payacctitem != null){
			for (CFee f : payacctitem) {
				getVariable("#ACCTITEM_" + f.getAcctitem_id() + "=" + f.getReal_pay());
			}
		}
	}
	
	//装填账户余额数据
	private void setupBalanceacctitem() throws JDBCException {
		if (balanceacctitem != null){
			for (AcctitemDto f : balanceacctitem) {
				getVariable("#BALANCE_" + f.getAcctitem_id() + "=" + (f.getActive_balance()-f.getReal_fee()));
			}
		}
	}
	
	public CProdDto prods(String prodid) throws JDBCException {
		if (orderprods != null){
			for (CProdDto f : orderprods) {
				if(f.getProd_id().equals(prodid))
					return f;
			}
		}
		return new CProdDto();
	}	

	/**
	 * 	ruleStr 有包含条件变量 times，计算满足times的最大正整数值，最大值不超过repetitionTimes
	 * @param ruleStr	表达式
	 * @param repetitionTimes 最大值
	 * @return 满足的最大正整数
	 * @throws Exception 
	 */
	public Integer parsePromotion(String ruleStr, Integer repetitionTimes) throws Exception {
		if (StringHelper.isEmpty(ruleStr))
			return 0;

		context.setRootObject(this);
		ruleStr = ruleParamerReplace(ruleStr);
		setupPayacctitem();
		setupBalanceacctitem();
		loadData(ruleStr);
		acctitemSumMap = new HashMap<String ,Integer>();
		
		double times=repetitionTimes;
		while (times > 0) {
			getVariable("#times=" + times);
			try {
				if (getVariable(ruleStr, Boolean.class)) {
					break;
				}
			} catch (Exception e) {
				LoggerHelper.error(getClass(), "表达式解析错误：" + ruleStr
						+ e.getMessage());
				return 0;
			}
			times -= 1;
		}
//		if (times < repetitionTimes){
//			for (int i=1;i<10;i++){
//				times=times+0.1;
//				getVariable("#times=" + times);
//				try {
//					if (!getVariable(ruleStr, Boolean.class)) {
//						break;
//					}
//				} catch (Exception e) {
//				}
//			}
//			times = times -0.1;
//		}
		
		
		return (int)(times*10);
	}

	private void loadData(String ruleStr) throws Exception {
		if (cuser != null && StringHelper.isNotEmpty(cuser.getStb_id())&&ruleStr.indexOf("stb.")>=0)
			stb = cFeeDeviceDao.queryByStbId(cuser.getStb_id());
		if (cuser != null &&ruleStr.indexOf(".unit_id")>=0){
			 List<CCust> units = cCustDao.queryUnitByResident(ccust.getCust_id(),ccust.getCounty_id());
			 if (units!=null && units.size()>0)
				 ccust.setUnit_id(units.get(0).getCust_id());
		}
		if (ccust != null && ruleStr.indexOf(".user_count") >= 0) {
			ccust.setUser_count(cUserDao.queryUserCount(ccust.getCust_id()));
		}
		if (ccust != null && ruleStr.indexOf(".user_count_dtv") >= 0) {
			//ccust.setUser_count_dtv(cUserDao.queryUserByCustId(ccust.getCust_id(),SystemConstants.USER_TYPE_DTV).size());
		}
		if (ccust != null && ruleStr.indexOf(".is_bank") >= 0) {
			CAcctBank acctBank = cAcctBankDao.findByCustId(ccust.getCust_id());
			if (acctBank != null && acctBank.getStatus().equals(StatusConstants.ACTIVE))
				ccust.setIs_bank("T");
		}
		if(cuser != null && ruleStr.indexOf(".seq")>=0){
			cuser.setSeq(cUserDao.queryUserSequence(cuser.getCust_id(),cuser.getUser_id()));
		}
//		System.out.println(ruleStr);
	}

	public boolean parseBoolean(String ruleId, Map<String, Object> params) {
		if (StringHelper.isEmpty(ruleId))
			return true;
		try {
			List<String> modelNames = tRuleEditDao.queryModelByRule(ruleId);
			TRuleDefine rule = tRuleDefineDao.findByKey(ruleId);
			String names = "";
			for (String n : modelNames)
				names += n + ",";

			setupDateByModelNames(params, names);
			if (params.get("prod_id")!=null)
				getVariable("#prod_id='" + params.get("prod_id")+"'");
			
			return parseBoolean(rule.getRule_str());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 加载数据
	 *
	 * @param data
	 * @param modelName
	 */
	private void setupDateByModelNames(Map<String, Object> params,
			String modelName) throws JDBCException, ComponentException {
		if (modelName.indexOf("CUSERDTV,") > -1 ) {
			String userId = (String) params.get("userId");
			if (StringHelper.isEmpty(userId)) {
				throw new ComponentException("表达式缺少必要的参数");
			}
			CUserDtv user = cUserDtvDao.queryDtvById(userId);
			if (user != null){
				params.put("custId", user.getCust_id());
				setCuserdtv(user);
			}
		}
		if (modelName.indexOf("CUSERATV,") > -1) {
			String userId = (String) params.get("userId");
			if (StringHelper.isEmpty(userId)) {
				throw new ComponentException("表达式缺少必要的参数");
			}
			CUserAtv user = cUserAtvDao.queryAtvById(userId);
			if (user != null){
				params.put("custId", user.getCust_id());
				setCuseratv(user);
			}
		}
		if (modelName.indexOf("CUSER,") > -1) {
			String userId = (String) params.get("userId");
			if (StringHelper.isEmpty(userId)) {
				throw new ComponentException("表达式缺少必要的参数");
			}
			CUser user = cUserDao.findByKey(userId);
			if (user != null){
				params.put("custId", user.getCust_id());
				setCuser(user);
			}
		}
		if (modelName.indexOf("CCUST,") > -1) {
			String custId = (String) params.get("custId");
			if (StringHelper.isEmpty(custId)) {
				throw new ComponentException("表达式缺少必要的参数");
			}
			CCust cust = cCustDao.findByKey(custId);
			setCcust(cust);
		}
	}
	
	/**
	 * 一.将replaceParmer 定义参数开头的参数，如果参数为空，设置为0
	 * 原因：SPEL对于空的参数进行逻辑操作时返回结果都是为ture的
	 * 	如：表达式为ACCTITEM_100000>1000,当ACCTITEM_100000为空时，表达式返回的是true，应该返回为false
	 * 
	 * 二.将表达式中，以replaceParmer 定义参数开头的参数，都加前缀 #
	 * 原因：SPEL动态定义的变量都需要在参数前加上 #号
	 * @param ruleStr
	 * @return
	 */
	private String ruleParamerReplace(String ruleStr) {
		String comStr = "";
		for (String p:replaceParmer){
			comStr += p + "_[0-9]+|";
			
			ruleStr = ruleStr.replace(p, "#"+p);
		}
		comStr = comStr.substring(0, comStr.length() - 1);
		
		Pattern pattern = Pattern.compile(comStr);
		Matcher matcher = pattern.matcher(ruleStr);
		while (matcher.find()) {
			getVariable("#" + matcher.group() + "=0");
		}	
		
		ruleStr = converDateFunction(ruleStr);
		return ruleStr;
	}

	private static String converDateFunction(String ruleStr) {
		ruleStr = " " +ruleStr+"  ";
		for (String s : datefunname) {
			int end = ruleStr.indexOf("=="+s) ;
			while (end > -1) {
				String tt = ruleStr.substring(ruleStr.substring(0, end).lastIndexOf(" ")+1,end+s.length()+2);
				ruleStr = ruleStr.replace(tt, s+"("+tt.replaceAll("=="+s, ")"));
				end = ruleStr.indexOf("=="+s) ;
			}
		}
		return ruleStr;
	}

	public boolean inAddr(String... addrid) throws JDBCException {
		if (ccust == null)
			return false;
		if (ccust.getAddr_id().equals(addrid))
			return true;
		List<TAddress> addrs = tAddressDao.queryAddrByPid(ccust.getAddr_id(),addrid);
		return addrs.size() == 0 ? false : true;
	}
	
	public boolean prodTariff(String prodId,String tariffId){
		for(CProd prod : orderprods){
			if(prod.getProd_id().equals(prodId) && prod.getTariff_id().equals(tariffId)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * 统计指定时间段，指定账目的有效缴费金额（分）
	 * @param acctItemId
	 * @param bDate
	 * @param eDate
	 * @return
	 * @throws JDBCException
	 */
	public Integer sumPayFee(String acctItemId,String bDate,String eDate) throws JDBCException{
		if (cuser != null && StringHelper.isNotEmpty(cuser.getUser_id())) 
			return cFeeDao.queryFeeByDate(cuser.getUser_id(),acctItemId,bDate,eDate);
		return 0;
	}
	
	public int acctitem(String acctItemId, Integer days) throws JDBCException {
		if (cuser != null && StringHelper.isNotEmpty(cuser.getUser_id())) {
			Integer sum = acctitemSumMap.get(acctItemId + days);
			if (sum == null) {
				sum = cFeeDao.queryBeforeAllFees(cuser.getCust_id(),cuser.getUser_id(),
						acctItemId, days);
				acctitemSumMap.put(acctItemId + days, sum);
			}
			return sum;
		}
		return 0;
	}
	
	/**
	 * 查找用户下的订购的指定产品
	 * @param acctItemId
	 * @return
	 * @throws JDBCException
	 */
	public CProd orderPord(String acctItemId) throws JDBCException{
		if (cuser != null ){
			return cProdDao.queryProdsByUserProd(cuser.getUser_id(),acctItemId);
		}
		return null;
	}
	
	/**
	 * 查找客户户下的订购的指定产品
	 * @param acctItemId
	 * @return
	 * @throws JDBCException
	 */
	public List<CProd> orderPordByCust(String prodId) throws JDBCException{
		if (ccust != null ){
			return cProdDao.queryProdsByCustProd(ccust.getCust_id(),prodId);
		}
		return null;
	}
	
	/**
	 * 通过规则Id查询
	 * @param ruleId
	 * @return
	 * @throws JDBCException
	 */
	public TRuleDefine queryByRuleId(String ruleId) throws JDBCException{
		return tRuleDefineDao.findByKey(ruleId);
	}
	
	/**
	 * 查找用户下的订购的指定产品类型的最晚日期
	 * @param prodType
	 * @return
	 * @throws JDBCException
	 */
	public Date laterOrderByType(String prodType) throws JDBCException {
		if (cuser != null) {
			CProd cprod = cProdDao.queryLaterOrderDateByProdType(cuser
					.getUser_id(), prodType);
			if (cprod != null)
				return cprod.getOrder_date();
		}
		return null;
	}
	
	/**
	 * 统计指定产品缴费月份数，当天
	 * @return
	 * @throws Exception 
	 */
	public int payMonthsOfToday(String acctitemId) throws Exception {
		int months = 0;
		if (cuser != null) {
			months = cProdDao.payMonthsOfToday(cuser.getUser_id(), acctitemId);
		}
		return months;
	}

	public Boolean TODAY(Date d){
		return DateHelper.isToday(d);
	}

	public Boolean THISMONTH(Date d){
		return DateHelper.dateToStr(d).substring(0, 7).equals(DateHelper.formatNow().substring(0, 7));
	}

	public Boolean THISYEAR(Date d){
		return DateHelper.dateToStr(d).substring(0, 4).equals(DateHelper.formatNow().substring(0, 4));
	}

	/**
	 * @return the ccust
	 */
	public CCust getCcust() {
		return ccust;
	}

	/**
	 * @param ccust
	 *            the ccust to set
	 */
	public void setCcust(CCust ccust) {
		this.ccust = ccust;
	}

	/**
	 * @return the cuser
	 */
	public CUser getCuser() {
		return cuser;
	}

	/**
	 * @param cuser
	 *            the cuser to set
	 */
	public void setCuser(CUser cuser) {
		if (cuser instanceof CUserAtv) {
			setCuseratv((CUserAtv) cuser);
			setCuserdtv(null);
			setCuserBroadband(null);
		} else if (cuser instanceof CUserDtv) {
			setCuserdtv((CUserDtv) cuser);
			setCuserBroadband(null);
			setCuseratv(null);
		} else if (cuser instanceof CUserBroadband) {
			setCuserBroadband((CUserBroadband) cuser);
			setCuserdtv(null);
			setCuseratv(null);
		}
		this.cuser = cuser;
	}

	/**
	 * @return the cuserdtv
	 */
	public CUserDtv getCuserdtv() {
		return cuserdtv;
	}

	/**
	 * @param cuserdtv
	 *            the cuserdtv to set
	 */
	public void setCuserdtv(CUserDtv cuserdtv) {
		this.cuserdtv = cuserdtv;
	}

	/**
	 * @return the cuseratv
	 */
	public CUserAtv getCuseratv() {
		return cuseratv;
	}

	/**
	 * @param cuseratv
	 *            the cuseratv to set
	 */
	public void setCuseratv(CUserAtv cuseratv) {
		this.cuseratv = cuseratv;
	}

	/**
	 * @return the tODAY
	 */
	public Date getTODAY() {
		return TODAY;
	}

	/**
	 * @param today the tODAY to set
	 */
	public void setTODAY(Date today) {
		TODAY = today;
	}

	private StandardEvaluationContext context = new StandardEvaluationContext();
	private ExpressionParser parser = new SpelExpressionParser();


	public ExpressionParser getParser() {
		return parser;
	}

	/**
	 * 返回指定类型的表达式结果
	 *
	 * @param <T>
	 *            返回类型
	 * @param exp
	 *            表达式
	 * @param resultType
	 * @return
	 */
	public <T> T getVariable(String exp, Class<T> resultType) {
		return parser.parseExpression(exp).getValue(context, resultType);
	}

	public String getVariable(String exp) {
		 return getVariable(exp, String.class);
	}

	/**
	 * 将变量加入表达式容器 以#varname的方式调用
	 *
	 * @param varname
	 * @param value
	 */
	public void setVariable(String varname, Object value) {
		context.setVariable(varname, value);
	}

	/**
	 * @return the cuserBroadband
	 */
	public CUserBroadband getCuserBroadband() {
		return cuserBroadband;
	}

	/**
	 * @param cuserBroadband the cuserBroadband to set
	 */
	public void setCuserBroadband(CUserBroadband cuserBroadband) {
		this.cuserBroadband = cuserBroadband;
	}

	/**
	 * @param addressDao the tAddressDao to set
	 */
	public void setTAddressDao(TAddressDao addressDao) {
		tAddressDao = addressDao;
	}

	/**
	 * @param custDao the cCustDao to set
	 */
	public void setCCustDao(CCustDao custDao) {
		cCustDao = custDao;
	}

	public CAcctBankDao getCAcctBankDao() {
		return cAcctBankDao;
	}

	public void setCAcctBankDao(CAcctBankDao cAcctBankDao) {
		this.cAcctBankDao = cAcctBankDao;
	}

	/**
	 * @param userDao the cUserDao to set
	 */
	public void setCUserDao(CUserDao userDao) {
		cUserDao = userDao;
	}

	/**
	 * @param userDtvDao the cUserDtvDao to set
	 */
	public void setCUserDtvDao(CUserDtvDao userDtvDao) {
		cUserDtvDao = userDtvDao;
	}

	/**
	 * @param userAtvDao the cUserAtvDao to set
	 */
	public void setCUserAtvDao(CUserAtvDao userAtvDao) {
		cUserAtvDao = userAtvDao;
	}

	/**
	 * @param ruleEditDao the tRuleEditDao to set
	 */
	public void setTRuleEditDao(TRuleEditDao ruleEditDao) {
		tRuleEditDao = ruleEditDao;
	}

	/**
	 * @param ruleDefineDao the tRuleDefineDao to set
	 */
	public void setTRuleDefineDao(TRuleDefineDao ruleDefineDao) {
		tRuleDefineDao = ruleDefineDao;
	}

	/**
	 * @param promotionDao the pPromotionDao to set
	 */
	public void setPPromotionDao(PPromotionDao promotionDao) {
		pPromotionDao = promotionDao;
	}




	/**
	 * @param feeDao the cFeeDao to set
	 */
	public void setCFeeDao(CFeeDao feeDao) {
		cFeeDao = feeDao;
	}

	/**
	 * @return the stb
	 */
	public CFeeDevice getStb() {
		return stb;
	}

	/**
	 * @param stb the stb to set
	 */
	public void setStb(CFeeDevice stb) {
		this.stb = stb;
	}

	/**
	 * @param feeDeviceDao the cFeeDeviceDao to set
	 */
	public void setCFeeDeviceDao(CFeeDeviceDao feeDeviceDao) {
		cFeeDeviceDao = feeDeviceDao;
	}

	/**
	 * @return the orderprods
	 */
	public List<CProdDto> getOrderprods() {
		return orderprods;
	}

	/**
	 * @param orderprods the orderprods to set
	 */
	public void setOrderprods(List<CProdDto> orderprods) {
		this.orderprods = orderprods;
	}

	/**
	 * @param prodDao the cProdDao to set
	 */
	public void setCProdDao(CProdDao prodDao) {
		cProdDao = prodDao;
	}

	public SOptr getOptr() {
		return optr;
	}

	public void setOptr(SOptr optr) {
		this.optr = optr;
	}

	public ExpressionUtil(TAddressDao tAddressDao) {
		this.tAddressDao = tAddressDao;
	}

	/**
	 * @return the cuserStb
	 */
	public CUserStb getCuserStb() {
		return cuserStb;
	}

	/**
	 * @param cuserStb the cuserStb to set
	 */
	public void setCuserStb(CUserStb cuserStb) {
		this.cuserStb = cuserStb;
	}

}
