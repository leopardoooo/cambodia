package com.ycsoft.sysmanager.component.prod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.prod.PProd;
import com.ycsoft.beans.prod.PProdTariff;
import com.ycsoft.beans.prod.PPromFee;
import com.ycsoft.beans.prod.PPromFeeCounty;
import com.ycsoft.beans.prod.PPromFeeDivision;
import com.ycsoft.beans.prod.PPromFeeProd;
import com.ycsoft.beans.prod.PPromFeeUser;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.business.dao.config.TRuleDefineDao;
import com.ycsoft.business.dao.prod.PPromFeeCountyDao;
import com.ycsoft.business.dao.prod.PPromFeeDao;
import com.ycsoft.business.dao.prod.PPromFeeDivisionDao;
import com.ycsoft.business.dao.prod.PPromFeeProdDao;
import com.ycsoft.business.dao.prod.PPromFeeUserDao;
import com.ycsoft.business.dto.core.prod.PromFeeProdDto;
import com.ycsoft.commons.abstracts.BaseComponent;
import com.ycsoft.commons.constants.SequenceConstants;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.exception.ComponentException;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;
import com.ycsoft.sysmanager.dto.PromFeeDto;
import com.ycsoft.sysmanager.dto.config.VewRuleDto;
import com.ycsoft.sysmanager.dto.tree.TreeDto;

@Component
public class PromFeeComponent extends BaseComponent {
	private PPromFeeDao pPromFeeDao;
	private PPromFeeUserDao pPromFeeUserDao;
	private PPromFeeProdDao pPromFeeProdDao;
	private PPromFeeCountyDao pPromFeeCountyDao;
	private PPromFeeDivisionDao pPromFeeDivisionDao;
	
	private TRuleDefineDao tRuleDefineDao;

	public void setTRuleDefineDao(TRuleDefineDao ruleDefineDao) {
		tRuleDefineDao = ruleDefineDao;
	}

	public List<VewRuleDto> queryPromFeeUserRule() throws Exception{
		return tRuleDefineDao.queryPromFeeUserRule();
		
	}
	public Pager<PPromFee> queryPromFee(Integer start, Integer limit,
			String keyword, SOptr optr) throws Exception {
//		String countyDataRight = this.queryDataRightCon(optr, DataRight.CHANGE_COUNTY.toString());
		return pPromFeeDao.queryPromFee(keyword, optr.getCounty_id(),start, limit);
	}
	public List<PPromFeeUser> queryPromFeeUsers(String promFeeId) throws JDBCException {
		return pPromFeeUserDao.queryPromFeeUser(promFeeId);
	}


	public Map<String,List<PromFeeProdDto>> queryPromFeeProds(String promFeeId) throws Exception {
		List<PromFeeProdDto> prodList = pPromFeeProdDao.queryPromFeeProds(promFeeId);
		Map<String,List<PromFeeProdDto>> map = CollectionHelper.converToMap(prodList, "user_no");
		return map; 
	}
	
	public List<PPromFeeDivision> queryPromFeeDivision(String promFeeId) throws JDBCException {
		return pPromFeeDivisionDao.queryPromFeeDivision(promFeeId);
	}

	
	/**
	 * 保存或者修改
	 * @param promFee
	 * @param optr
	 * @param countyIds
	 * @throws Exception
	 */
	public void savePromFee(PPromFee promFee,SOptr optr, String countyIds) throws Exception {
		//prom_fee_id不为空，修改
		if(null != promFee && StringHelper.isNotEmpty(promFee.getProm_fee_id())){
			pPromFeeDao.update(promFee);
		}else{//保存
			promFee.setProm_fee_id(getPromFeeId());
			promFee.setOptr_id(optr.getOptr_id());
			promFee.setCounty_id(optr.getCounty_id());
			promFee.setArea_id(optr.getArea_id());
			promFee.setStatus(StatusConstants.ACTIVE);
			pPromFeeDao.save(promFee);
		}
		
		//保存适用地区
		pPromFeeCountyDao.deleteById(promFee.getProm_fee_id());
		String[] countyIdArr = countyIds.split(",");
		List<PPromFeeCounty> countyIdList = new ArrayList<PPromFeeCounty>();
		for(int i=0;i<countyIdArr.length;i++){
			PPromFeeCounty promCounty = new PPromFeeCounty();
			promCounty.setProm_fee_id(promFee.getProm_fee_id());
			promCounty.setCounty_id(countyIdArr[i]);
			countyIdList.add(promCounty);
		}
		pPromFeeCountyDao.save(countyIdList.toArray(new PPromFeeCounty[countyIdList.size()]));
	}
	
	/**
	 * 禁用启用套餐
	 * @param promFeeId
	 * @param status
	 * @throws Exception
	 */
	public void updateStatusProm(String promFeeId,String status) throws Exception {
		PPromFee prom = pPromFeeDao.findByKey(promFeeId);
		prom.setStatus(status);
		pPromFeeDao.update(prom);
	}
	
	private String getPromFeeId() throws JDBCException{
		return pPromFeeDao.findSequence(SequenceConstants.SEQ_PROD_ID).toString();
	}
	
	/**
	 * 保存或者修改
	 * @param promFeeUserList
	 * @throws JDBCException 
	 */
	public void savePromFeeUsers(String promFeeId,List<PPromFeeUser> promFeeUserList) throws JDBCException {
		
		List<PPromFeeUser> oldusers=pPromFeeUserDao.queryPromFeeUser(promFeeId);
		Map<String,String> newMap=new HashMap<String,String>();
		pPromFeeUserDao.deleteByFeeId(promFeeId);
		if(null !=promFeeUserList && promFeeUserList.size() > 0){
			pPromFeeUserDao.save(promFeeUserList.toArray(new PPromFeeUser[promFeeUserList.size()]));
			for(PPromFeeUser user: promFeeUserList){
				newMap.put(user.getUser_no(), user.getUser_no());
			}
		}
		//删除不存在规则编号的产品配置信息
		for(PPromFeeUser old:oldusers){
			if(!newMap.containsKey(old.getUser_no()))
				pPromFeeProdDao.deleteByUserNo(promFeeId, old.getUser_no());
		}
		
	}

	/**
	 * 保存或者修改
	 * @param userNo 
	 * @param promFeeProdList
	 * @throws JDBCException 
	 */
	public void savePromFeeProds(String promFeeId,String userNo, List<PPromFeeProd> promFeeProdList) throws JDBCException {
		pPromFeeProdDao.deleteByUserNo(promFeeId,userNo);
		if(null !=promFeeProdList && promFeeProdList.size() > 0){
			pPromFeeProdDao.save(promFeeProdList.toArray(new PPromFeeProd[promFeeProdList.size()]));
		}
		
		//套餐产品发生变化时，根据promFeeId，删除分成不存在的产品和实际缴费金额为0 的分成
		changeDivisionRecrods(promFeeId);
	}
	
	/**
	 * 套餐产品发生变化时，根据promFeeId，删除分成不存在的产品和实际缴费金额为0 的分成
	 * @param promFeeId
	 * @throws JDBCException 
	 */
	private void changeDivisionRecrods(String promFeeId) throws JDBCException{
		pPromFeeDivisionDao.changeDivisionRecrods(promFeeId);
	}
	
	/**
	 * 保存套餐分成
	 * @param divisionList
	 * @throws JDBCException 
	 */
	public void savePromFeeDivision(String promFeeId,List<PPromFeeDivision> divisionList) throws JDBCException{
		//删除原有分成
		pPromFeeDivisionDao.deleteByPromFeeId(promFeeId);
		
		if(null != divisionList && divisionList.size() > 0){
			//保存新的分成
			pPromFeeDivisionDao.save(divisionList.toArray(new PPromFeeDivision[divisionList.size()]));
		}
		
	}

	public List<PProdTariff> queryAllTariff(String promFeeId, String prodId) throws JDBCException {

		if(prodId.equals("BAND")){
			 List<PProdTariff> list=new ArrayList<PProdTariff>();
			 PProdTariff e=new PProdTariff();
			 e.setTariff_id("BAND");
			 e.setTariff_name("宽带自动匹配");
			 e.setRent(0);
			 list.add(e);
			 return list;
		}else
		return pPromFeeProdDao.queryAllTariff(promFeeId,prodId);
	}
	
	public List<PProd> queryProdAll(String promFeeId) throws JDBCException {
		List<PProd> list= pPromFeeProdDao.queryAllTariff(promFeeId);
		PProd pprod=new PProd();
		pprod.setProd_id("BAND");
		pprod.setProd_name("宽带自动匹配");
		list.add(pprod);
		return list;
	}

	
	/**
	 * 查询记录异动需要的数据.
	 * @param promFeeId
	 * @param withCounty 是否查询适用地区
	 * @return
	 * @throws ComponentException
	 */
	public PromFeeDto queryForSysChangeInfo(String promFeeId,boolean withCounty) throws ComponentException{
		PromFeeDto dto = new PromFeeDto();
		if(StringHelper.isEmpty(promFeeId)){
			return dto;
		}
		dto.setPromFeeId(promFeeId);
		
		PPromFee promFee = null;
		try {
			promFee = pPromFeeDao.findByKey(promFeeId);
			dto.setPromFee(promFee);
			dto.setPromFeeName(promFee.getProm_fee_name());
			if(withCounty){
				List<PPromFeeCounty> county = new ArrayList<PPromFeeCounty>();
				List<TreeDto> countyTree = pPromFeeCountyDao.querybyPromFeeId(promFeeId);
				for(TreeDto tree:countyTree){
					PPromFeeCounty pc = new PPromFeeCounty();
					pc.setCounty_id(tree.getId());
					pc.setProm_fee_id(promFeeId);
					county.add(pc);
				}
				dto.setCounty(county);
			}
		} catch (Exception e) {
			throw new ComponentException(e);
		}
		
		return dto;
	}
	public void setPPromFeeDao(PPromFeeDao pPromFeeDao) {
		this.pPromFeeDao = pPromFeeDao;
	}

	public void setPPromFeeUserDao(PPromFeeUserDao pPromFeeUserDao) {
		this.pPromFeeUserDao = pPromFeeUserDao;
	}

	public void setPPromFeeProdDao(PPromFeeProdDao pPromFeeProdDao) {
		this.pPromFeeProdDao = pPromFeeProdDao;
	}

	public void setPPromFeeCountyDao(PPromFeeCountyDao pPromFeeCountyDao) {
		this.pPromFeeCountyDao = pPromFeeCountyDao;
	}

	public void setPPromFeeDivisionDao(PPromFeeDivisionDao pPromFeeDivisionDao) {
		this.pPromFeeDivisionDao = pPromFeeDivisionDao;
	}

	
}
