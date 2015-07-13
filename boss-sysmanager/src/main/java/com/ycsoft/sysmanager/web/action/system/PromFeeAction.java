package com.ycsoft.sysmanager.web.action.system;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ycsoft.beans.prod.PPromFee;
import com.ycsoft.beans.prod.PPromFeeCounty;
import com.ycsoft.beans.prod.PPromFeeDivision;
import com.ycsoft.beans.prod.PPromFeeProd;
import com.ycsoft.beans.prod.PPromFeeUser;
import com.ycsoft.beans.system.SSysChange;
import com.ycsoft.business.dto.core.prod.PromFeeProdDto;
import com.ycsoft.commons.abstracts.BaseAction;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SysChangeType;
import com.ycsoft.commons.exception.ActionException;
import com.ycsoft.commons.exception.ComponentException;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.tree.TreeBuilder;
import com.ycsoft.commons.tree.TreeNode;
import com.ycsoft.daos.helper.BeanHelper;
import com.ycsoft.sysmanager.component.prod.ProdComponent;
import com.ycsoft.sysmanager.component.prod.PromFeeComponent;
import com.ycsoft.sysmanager.dto.PromFeeDto;

@Controller
public class PromFeeAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3623319464696427973L;
	
	private ProdComponent prodComponent;
	
	private String promFeeId;
	private String userNo;
	private PPromFee promFee;
	private String countyIds;
	
	private String promFeeUserListStr;
	private String promFeeProdListStr;
	private String promFeeDivisonListStr;
	
	private String prodId;
	private String query;
	private PromFeeComponent promFeeComponent;
	
	public String queryPromFeeUserRule() throws Exception{
		getRoot().setRecords(promFeeComponent.queryPromFeeUserRule());
		return JSON_RECORDS;
	}
	public String queryPromFee() throws Exception{
		getRoot().setPage(promFeeComponent.queryPromFee(start, limit,query,optr));
		return JSON_PAGE;
	}
	
	public String queryPromFeeUsers() throws Exception{
		getRoot().setRecords(promFeeComponent.queryPromFeeUsers(promFeeId));
		return JSON_RECORDS;
	}
	
	public String queryPromFeeProds() throws Exception{
		getRoot().setSimpleObj(promFeeComponent.queryPromFeeProds(promFeeId));
		return JSON_SIMPLEOBJ;
	}
	
	public String queryPromFeeDivision() throws Exception{
		getRoot().setRecords(promFeeComponent.queryPromFeeDivision(promFeeId));
		return JSON_RECORDS;
	}
	
	/**
	 * 促销主题适用地区树
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String getCountyTree() throws Exception{
		String[] type = {"COUNTY","PROMFEE"};
		List<TreeNode> prodtree = TreeBuilder.createTreeCheck((List)prodComponent.getCountyTree(optr,type,promFeeId));  
		getRoot().setRecords(prodtree);
		return JSON_RECORDS;
	}
	
	public String savePromFee() throws Exception{
		String id = promFee.getProm_fee_id();
		PromFeeDto oldPromFee = promFeeComponent.queryForSysChangeInfo(id,true);
		promFeeComponent.savePromFee(promFee,optr,countyIds);
		PromFeeDto newPromFee = promFeeComponent.queryForSysChangeInfo(promFee.getProm_fee_id(),true);
		saveChanges(oldPromFee,newPromFee, 1);
		return JSON;
	}
	/**
	 * 禁用套餐
	 * @return
	 * @throws Exception
	 */
	public String stopProm() throws Exception{
		PromFeeDto dto = promFeeComponent.queryForSysChangeInfo(promFeeId, false);
		promFeeComponent.updateStatusProm(promFeeId,StatusConstants.INVALID);
		
		PromFeeDto dtoNew = new PromFeeDto();
		BeanUtils.copyProperties(dto, dtoNew);
		PPromFee newPromFee = new PPromFee();
		newPromFee.setStatus(StatusConstants.INVALID);
		dtoNew.setPromFee(newPromFee);
		
		saveChanges(dto, dtoNew, 5);
		
		return JSON;
	}
	/**
	 * 启用套餐
	 * @return
	 * @throws Exception
	 */
	public String userProm() throws Exception{
		
		PromFeeDto dto = promFeeComponent.queryForSysChangeInfo(promFeeId, false);
		promFeeComponent.updateStatusProm(promFeeId,StatusConstants.ACTIVE);
		
		PromFeeDto dtoNew = new PromFeeDto();
		BeanUtils.copyProperties(dto, dtoNew);
		PPromFee newPromFee = new PPromFee();
		newPromFee.setStatus(StatusConstants.ACTIVE);
		dtoNew.setPromFee(newPromFee);
		
		saveChanges(dto, dtoNew, 5);
		
		return JSON;
	}
	
	public String savePromFeeUsers() throws Exception{
		List<PPromFeeUser> promFeeUserList = new ArrayList<PPromFeeUser>();
		Type type = new TypeToken<List<PPromFeeUser>>(){}.getType();
		Gson gson = new Gson();
		promFeeUserList = gson.fromJson(promFeeUserListStr, type);
		
		String promFeeId = (promFeeUserList !=null && !promFeeUserList.isEmpty()) ? promFeeUserList.get(0).getProm_fee_id():null;
		if(StringHelper.isEmpty(promFeeId)){
			throw new ActionException("参数有错误,未能获取prom_fee_id");
		}
		List<PPromFeeUser> oldList = promFeeComponent.queryPromFeeUsers(promFeeId);
		PromFeeDto oldPromFee = promFeeComponent.queryForSysChangeInfo(promFeeId, false);
		oldPromFee.setUsers(oldList);
		
		promFeeComponent.savePromFeeUsers(promFeeId,promFeeUserList);
		
		List<PPromFeeUser> newList = promFeeComponent.queryPromFeeUsers(promFeeId);
		PromFeeDto newPromFee = promFeeComponent.queryForSysChangeInfo(promFeeId, false);
		newPromFee.setUsers(newList);
		saveChanges(oldPromFee, newPromFee, 2);
		
		return JSON;
	}
	/**
	 * 查询不含客户套餐的产品
	 * @return
	 * @throws Exception
	 */
	public String queryProdAll() throws Exception{
		getRoot().setRecords(promFeeComponent.queryProdAll(promFeeId));
		return JSON_RECORDS;
	}
	
	public String queryAllTariff() throws Exception{
		getRoot().setRecords(promFeeComponent.queryAllTariff(promFeeId,prodId));
		return JSON_RECORDS;
	}

	public String savePromFeeProds() throws Exception{
		List<PPromFeeProd> promFeeProdList = new ArrayList<PPromFeeProd>();
		Type type = new TypeToken<List<PPromFeeProd>>(){}.getType();
		Gson gson = new Gson();
		promFeeProdList = gson.fromJson(promFeeProdListStr, type);
		
		
		PromFeeDto oldPromFee = promFeeComponent.queryForSysChangeInfo(promFeeId, false);
		List<PPromFeeProd> oldList = new ArrayList<PPromFeeProd>();
		List<PromFeeProdDto> oldUserProds = promFeeComponent.queryPromFeeProds(promFeeId).get(userNo);
		if(CollectionHelper.isNotEmpty(oldUserProds)){
			oldList.addAll(oldUserProds);
		}
		oldPromFee.setProds( oldList );
		
		promFeeComponent.savePromFeeProds(promFeeId,userNo,promFeeProdList);
		
		PromFeeDto newPromFee = promFeeComponent.queryForSysChangeInfo(promFeeId, false);
		List<PPromFeeProd> newList = new ArrayList<PPromFeeProd>();
		List<PromFeeProdDto> newUserProds = promFeeComponent.queryPromFeeProds(promFeeId).get(userNo);
		if(CollectionHelper.isNotEmpty(newUserProds)){
			newList.addAll(newUserProds);
		}
		newPromFee.setProds(newList);
		saveChanges(oldPromFee, newPromFee, 3);
		
		return JSON;
	}
	
	public String savePromFeeDivision() throws Exception{
		List<PPromFeeDivision> divisionList = new ArrayList<PPromFeeDivision>();
		Type type = new TypeToken<List<PPromFeeDivision>>(){}.getType();
		Gson gson = new Gson();
		divisionList = gson.fromJson(promFeeDivisonListStr, type);
		
		PromFeeDto oldPromFee = promFeeComponent.queryForSysChangeInfo(promFeeId,false);
		List<PPromFeeDivision> oldDivisions = promFeeComponent.queryPromFeeDivision(promFeeId);
		oldPromFee.setDivisions(oldDivisions);
		
		promFeeComponent.savePromFeeDivision(promFeeId, divisionList);
		
		PromFeeDto newPromFee = promFeeComponent.queryForSysChangeInfo(promFeeId,false);
		List<PPromFeeDivision> newDivisions =  promFeeComponent.queryPromFeeDivision(promFeeId);
		newPromFee.setDivisions(newDivisions);
		
		saveChanges(oldPromFee, newPromFee, 4);
		return JSON;
	}
	
	/**
	 * @param oldPromFee
	 * @param newPromFee 
	 * @param type
	 *            <ul>
	 *            <li>1:基本信息(包括适用地区)</li>
	 *            <li>2:套餐用户、</li>
	 *            <li>3:套餐产品、</li>
	 *            <li>4：分成配置</li>
	 *            <li>5：状态变更,启用/禁用</li>
	 *            </ul>
	 * @throws ComponentException
	 */
	private void saveChanges(PromFeeDto oldPromFee,PromFeeDto newPromFee, int type) throws ActionException{
		if(StringHelper.isEmpty(oldPromFee.getPromFeeId()) && StringHelper.isEmpty(newPromFee.getPromFeeId())){
			throw new ActionException("记录异动出现异常,比较的两个对象都为空!");
		}
		try{
			List<SSysChange> changes = new ArrayList<SSysChange>();
			Integer doneCode = promFeeComponent.getDoneCOde();
			String changeType = SysChangeType.PROMFEE.toString();
			String key = StringHelper.isEmpty(oldPromFee.getPromFeeId()) ? newPromFee.getPromFeeId() : oldPromFee.getPromFeeId();
			String promFeeName = StringHelper.isEmpty(oldPromFee.getPromFeeId()) ? newPromFee.getPromFeeName() : oldPromFee.getPromFeeName();
			Date createTime = new Date();
			String optrId = getOptr().getOptr_id();
			
			switch (type) {
			case 1:// 基本信息和适用地区
				String baseChangeContent = BeanHelper.beanchange(oldPromFee.getPromFee(), newPromFee.getPromFee());
				if(StringHelper.isNotEmpty(baseChangeContent)){
					SSysChange baseChange = new SSysChange(changeType, doneCode, key, promFeeName, "套餐配置定义", baseChangeContent, optrId, createTime);
					changes.add(baseChange);
				}
				
				List<PPromFeeCounty> oldCounty = oldPromFee.getCounty();
				List<PPromFeeCounty> newCounty = newPromFee.getCounty();
				
				if(CollectionHelper.isNotEmpty(oldCounty) || CollectionHelper.isNotEmpty(newCounty)){
					String countyChange = BeanHelper.listchange(oldCounty, newCounty, "county_name");
					if(StringHelper.isNotEmpty(countyChange)){
						SSysChange change = new SSysChange(changeType, doneCode, key, promFeeName, "套餐配置适用分公司", countyChange, optrId, createTime);
						changes.add(change);
					}
				}
				
				break;

			case 2:// 套餐用户,所有前台表格可见字段
				String [] userProperties = new String[] {"user_no","rule_name","user_fee","prod_count"};
				List<PPromFeeUser> oldUsers = oldPromFee.getUsers();
				List<PPromFeeUser> newUsers = newPromFee.getUsers();
				
				if(CollectionHelper.isNotEmpty(oldUsers) || CollectionHelper.isNotEmpty(newUsers)){
					String userChangeInfo = BeanHelper.listchange(oldUsers, newUsers, userProperties);
					if(StringHelper.isNotEmpty(userChangeInfo)){
						SSysChange change = new SSysChange(changeType, doneCode, key, promFeeName, promFeeName+"_套餐用户配置", userChangeInfo, optrId, createTime);
						changes.add(change);
					}
				}
				
				break;

			case 3:// 套餐产品,所有前台表格可见字段
				String [] prodProperties = new String[] {"prod_name","tariff_name","months","real_pay","force_select_text","bind_prod_name"};
				
				List<PPromFeeProd> oldProds = oldPromFee.getProds();
				List<PPromFeeProd> newProds = newPromFee.getProds();
				
				if(CollectionHelper.isNotEmpty(oldProds) || CollectionHelper.isNotEmpty(newProds)){
					String prodChangeInfo = BeanHelper.listchange(oldProds, newProds, prodProperties);
					if(StringHelper.isNotEmpty(prodChangeInfo)){
						String userno = CollectionHelper.isEmpty(oldProds)? newProds.get(0).getUser_no():oldProds.get(0).getUser_no();
						SSysChange change = new SSysChange(changeType, doneCode, key, promFeeName, promFeeName + "_" + userno +"_套餐产品配置", prodChangeInfo, optrId, createTime);
						changes.add(change);
					}
				}
				
				break;

			case 4:// 分成配置,所有前台表格可见字段
				String [] divProperties = new String[] {"user_no","prod_name","tariff_name","real_pay","percent_value"};
				
				List<PPromFeeDivision> oldDivisions = oldPromFee.getDivisions();
				List<PPromFeeDivision> newDivisions = newPromFee.getDivisions();
				
				if(CollectionHelper.isNotEmpty(oldDivisions) || CollectionHelper.isNotEmpty(newDivisions)){
					String divChangeInfo = BeanHelper.listchange(oldDivisions, newDivisions, divProperties);
					if(StringHelper.isNotEmpty(divChangeInfo)){
						SSysChange change = new SSysChange(changeType, doneCode, key, promFeeName, promFeeName + "_分成配置", divChangeInfo, optrId, createTime);
						changes.add(change);
					}
				}
				
				break;
			case 5://状态变更
				String content = BeanHelper.beanchange(oldPromFee.getPromFee(), newPromFee.getPromFee(),"status_text");
				if(StringHelper.isNotEmpty(content)){
					SSysChange baseChange = new SSysChange(changeType, doneCode, key, promFeeName, "禁用/启用套餐收费配置", content, optrId, createTime);
					changes.add(baseChange);
				}
				break;
			}

			if (!changes.isEmpty()) {
				promFeeComponent.getSSysChangeDao().save(changes.toArray(new SSysChange[changes.size()]));
			}
		} catch (Exception e) {
			throw new ActionException(e.getMessage());
		}
		
	}

	public void setPromFeeId(String promFeeId) {
		this.promFeeId = promFeeId;
	}

	public PPromFee getPromFee() {
		return promFee;
	}

	public void setPromFee(PPromFee promFee) {
		this.promFee = promFee;
	}

	public void setPromFeeComponent(PromFeeComponent promFeeComponent) {
		this.promFeeComponent = promFeeComponent;
	}

	public void setPromFeeUserListStr(String promFeeUserListStr) {
		this.promFeeUserListStr = promFeeUserListStr;
	}

	public void setPromFeeProdListStr(String promFeeProdListStr) {
		this.promFeeProdListStr = promFeeProdListStr;
	}

	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}

	public void setCountyIds(String countyIds) {
		this.countyIds = countyIds;
	}

	public void setProdComponent(ProdComponent prodComponent) {
		this.prodComponent = prodComponent;
	}

	public void setProdId(String prodId) {
		this.prodId = prodId;
	}

	public void setPromFeeDivisonListStr(String promFeeDivisonListStr) {
		this.promFeeDivisonListStr = promFeeDivisonListStr;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
}
