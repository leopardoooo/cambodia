package com.ycsoft.sysmanager.web.action.system;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ycsoft.beans.prod.PPackageProd;
import com.ycsoft.beans.prod.PProdCountyPrice;
import com.ycsoft.beans.prod.PProdDict;
import com.ycsoft.beans.prod.PProdTariffDisct;
import com.ycsoft.business.dto.core.prod.ProdTariffDto;
import com.ycsoft.commons.abstracts.BaseAction;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.DateHelper;
import com.ycsoft.commons.helper.JsonHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.commons.tree.TreeBuilder;
import com.ycsoft.commons.tree.TreeNode;
import com.ycsoft.sysmanager.component.prod.ProdComponent;
import com.ycsoft.sysmanager.dto.prod.ProdDto;
	/**
	 * 产品信息查询
	 */
	@Controller
	public class ProdAction extends BaseAction{
		/**
		 * @Description:
		 * @date Aug 3, 2010 10:48:38 AM
		 */
		private static final long serialVersionUID = 3107387446745748960L;
		private ProdComponent prodComponent;
		private ProdDto prodDto;
		private ProdTariffDto prodTariffDto;
		private String doneId;
		private String ServId;
		private String query;
		private String dynamicResList;
		private String staticResList;
		private String packList;
		private String records;
		private String [] dictProdIds;
		private String[] prodCountyIds;
		private String[] disctCountyIds;
		private PProdTariffDisct PProdTariffDisct;

		private String areaId;
		private String acctItemId;
		
		private String showAll;
		private String countyId;
		private PProdDict prodDict;
		
		/**
		 *产品树
		 */
		public String getProdTree() throws Exception{
			String startEffDate = request.getParameter("start_eff_date");
			String endEffDate = request.getParameter("end_eff_date");
			String startExpDate = request.getParameter("start_exp_date");
			String endExpDate = request.getParameter("end_exp_date");
			
			getRoot().setRecords(
				prodComponent.getProdTree(optr, showAll, startEffDate,
						endEffDate, startExpDate, endExpDate));
			return JSON_RECORDS;
		}
		/**
		 *地市资源树
		 */
		public String getDistrictResTree()throws Exception{
			getRoot().setRecords( prodComponent.getDistrictResTree(ServId,areaId) );
			return JSON_RECORDS;
		}
		/**
		 *产品目录树
		 */
		@SuppressWarnings("unchecked")
		public String getPordDictTree() throws Exception{
			List<TreeNode> prodtree = TreeBuilder.createTreeCheck((List)prodComponent.getPordDictTree(doneId));
			getRoot().setRecords(prodtree);
			return JSON_RECORDS;
		}

		@SuppressWarnings("unchecked")
		public String getCountyTree() throws Exception{
			String[] type = {"COUNTY","TARIFF"};
			List<TreeNode> prodtree = TreeBuilder.createTreeCheck((List)prodComponent.getCountyTree(optr,type,query));  
			getRoot().setRecords(prodtree);
			return JSON_RECORDS;
		}
		
		@SuppressWarnings("unchecked")
		public String getProdCountyTree() throws Exception{
			String[] type = {"COUNTY","PROD"};
			List<TreeNode> prodtree = TreeBuilder.createTreeCheck((List)prodComponent.getCountyTree(optr,type,query));  
			getRoot().setRecords(prodtree);
			return JSON_RECORDS;
		}
		
		public String getTariffDisctCountyTree() throws Exception{
			String[] type = {"COUNTY","DISCT"};
			List<TreeNode> prodtree = TreeBuilder.createTreeCheck((List)prodComponent.getCountyTree(optr,type,query));  
			getRoot().setRecords(prodtree);
			return JSON_RECORDS;
		}
		
		@SuppressWarnings("unchecked")
		public String getRoleCountyTree() throws Exception{
			String[] type = {"COUNTY","ROLE"};
			List<TreeNode> prodtree = TreeBuilder.createTreeCheck((List)prodComponent.getCountyTree(optr,type,query));  
			getRoot().setRecords(prodtree);
			return JSON_RECORDS;
		}
		@SuppressWarnings("unchecked")
		public String getCustCountyTree() throws Exception{
			String[] type = {"CHOOSE","COLONY"};
			List<TreeNode> prodtree = TreeBuilder.createTreeCheck((List)prodComponent.getCountyTree(optr,type,query));  
			getRoot().setRecords(prodtree);
			return JSON_RECORDS;
		}
		
		/**
		 * 产品目录树
		 * @return
		 * @throws Exception
		 */
		@SuppressWarnings("unchecked")
		public String getDictCountyTree() throws Exception{
			String[] type = {"CHOOSE","DICT"};
			List<TreeNode> countys = TreeBuilder.createTreeCheck((List)prodComponent.getCountyTree(optr,type,query));  
			getRoot().setRecords(countys);
			return JSON_RECORDS;
		}
		
		@SuppressWarnings("unchecked")
		public String queryProdDictByCountyId() throws Exception{
			List list = prodComponent.queryProdDictByCountyId(optr);
			getRoot().setRecords(TreeBuilder.createTree(list));
			return JSON_RECORDS;
		}
		
		/**
		 * 删除产品目录信息
		 */
		public String removeDict() throws Exception{
			getRoot().setSimpleObj(prodComponent.deleteDict(doneId));
			return JSON;
		}
		
		/**
		 * 添加修改产品目录信息
		 */
		public String saveProdDict() throws Exception{
			if(StringHelper.isNotEmpty(prodDict.getNode_id())){
				prodComponent.updateProdDict(prodDict,countyId);
			}else{
				prodComponent.saveProdDict(prodDict,countyId);
			}
			getRoot().setSimpleObj(prodDict);
			return JSON;
		}
		/**
		 * 根据账目id查询产品信息
		 * @param acctItemId
		 * @return
		 * @throws Exception
		 */
		public String queryProdByAcctItemId() throws Exception{
			getRoot().setRecords(prodComponent.queryProdByAcctItemId(acctItemId));
			return JSON_RECORDS;
		}

		/**
		 * 初始化状态
		 * @return
		 */
		public String queryIsstatus(){
			getRoot().setRecords(MemoryDict.getDicts(DictKey.STATUS_P_PROD));
			return JSON_RECORDS;
		}
	/**
	 * 根据地区查询产品信息
	 */
	public String queryProdAll() throws Exception{
		getRoot().setRecords(prodComponent.queryProdAll(optr));
		return JSON_RECORDS;
	}

	public String getProdByCounty() throws Exception{
		if(StringHelper.isEmpty(countyId)){
			countyId = optr.getCounty_id();
		}
		getRoot().setRecords(prodComponent.getProdByCounty(countyId));
		return JSON_RECORDS;
	}
	
	/**
	 * 根据产品编号和地区编号查询产品信息
	 */
	public String queryProdById() throws Exception{
		getRoot().setSimpleObj(prodComponent.queryProdById(doneId,optr));
		return JSON;
	}

	/**
	 * 根据套餐id和产品资费查询子产品
	 */
	public String queryPackProdById() throws Exception{
		getRoot().setRecords(prodComponent.queryPackProdById(doneId,query));
		return JSON_RECORDS;
	}
	
	/**
	 * 根据套餐ID查询子产品
	 * @return
	 * @throws Exception
	 */
	public String queryPackById() throws Exception{
		getRoot().setRecords(prodComponent.queryPackById(doneId));
		return JSON_RECORDS;
	}
	
	
	
	/**
	 * 查询套餐分成值操作记录
	 */
	public String queryProdPackageHis() throws Exception{
		getRoot().setRecords(prodComponent.queryByPackId(doneId));
		return JSON_RECORDS;
	}

	/**
	* @Description:查询操作员可以操作的地区
	* @return
	* @throws Exception
	* @return String
	 */
	public String queryArea() throws Exception{
		getRoot().setRecords(prodComponent.queryArea(optr));
		return JSON_RECORDS;
	}

	/**
	* @Description:根据产品编号查询资费信息
	 */
	public String queryTariffByProdId() throws Exception{
		getRoot().setRecords(prodComponent.queryTariffByProdId(doneId,prodCountyIds));
		return JSON_RECORDS;
	}
	
	/**
	* @Description:根据资费编号查询折扣信息
	 */
	public String queryDisct() throws Exception{
		getRoot().setRecords(prodComponent.queryDisct(doneId, optr));
		return JSON_RECORDS;
	}
	public String queryResByServId() throws Exception{
		getRoot().setSimpleObj(prodComponent.queryResByServId(ServId,doneId,query));
		return JSON;
	}
	
	public String queryResByCountyId() throws Exception{
		getRoot().setRecords(prodComponent.getStaticByServId(SystemConstants.USER_TYPE_DTV+","+SystemConstants.USER_TYPE_ITV,countyId));
		return JSON_RECORDS;
	} 
	/**
	 * 保存产品信息
	 */
	public String save() throws Exception {
		ProdDto proddto = new ProdDto();
		BeanUtils.copyProperties(prodDto, proddto);
		proddto.setStatus(StatusConstants.ACTIVE);
		if(StringHelper.isNotEmpty(prodDto.getProd_id())){
			getRoot().setSimpleObj(prodComponent.updateProd(proddto,dictProdIds,dynamicResList,staticResList,prodCountyIds));
		}else{
			proddto.setArea_id(optr.getArea_id());
			proddto.setCounty_id(optr.getCounty_id());
			proddto.setOptr_id(optr.getOptr_id());
			proddto.setCreate_time( new Date() );
			getRoot().setSimpleObj(prodComponent.saveProd(proddto,dictProdIds,dynamicResList,staticResList,prodCountyIds));
		}
		return JSON;
	}

	/**
	 * 保存套餐产品信息
	 */
	public String savePack() throws Exception {
		ProdDto proddto = new ProdDto();
		List<PPackageProd> PacksList = new ArrayList<PPackageProd>();
		BeanUtils.copyProperties(prodDto, proddto);
		if(StringHelper.isNotEmpty(packList)){
			Type type = new TypeToken<List<PPackageProd>>(){}.getType();
			List<PPackageProd> pack = JsonHelper.gson.fromJson(packList, type);
				for (PPackageProd dto:pack){
					PPackageProd packdto = new PPackageProd();
//					packdto.setProd_id(dto.getProd_id());
					PacksList.add(packdto);
				}
			proddto.setPackList(PacksList);
		}
		
		proddto.setStatus(StatusConstants.ACTIVE);
		if(StringHelper.isNotEmpty(prodDto.getProd_id())){
			getRoot().setSimpleObj(prodComponent.updateProdPack(proddto,dictProdIds,prodCountyIds)); 
		}else{
			proddto.setArea_id(optr.getArea_id());
			proddto.setCounty_id(optr.getCounty_id());
			proddto.setOptr_id(optr.getOptr_id());
			proddto.setCreate_time( DateHelper.now());
			getRoot().setSimpleObj(prodComponent.saveProdPack(proddto,dictProdIds,prodCountyIds));
		}
		return JSON;
	}
	
	/**
	 * 修改已生效的产品
	 * @return
	 * @throws Exception
	 */
	public String editProd() throws Exception{
		ProdDto proddto = new ProdDto();
		BeanUtils.copyProperties(prodDto, proddto);
		getRoot().setSimpleObj(prodComponent.editProd(proddto,dictProdIds,prodCountyIds));
		return JSON;
	}

	/**
	* @Description:保存资费
	* @return
	* @throws Exception
	* @return String
	 */
	public String saveTariff() throws Exception {
		ProdTariffDto dto = new ProdTariffDto();
		BeanUtils.copyProperties(prodTariffDto, dto);
		dto.setStatus(StatusConstants.ACTIVE);

		prodComponent.saveTariff(dto,packList,dictProdIds,optr);
		return JSON;
	}
	/**
	* @Description:保存折扣
	 */
	public String saveDisct() throws Exception {
		PProdTariffDisct dto = new PProdTariffDisct();
		BeanUtils.copyProperties(PProdTariffDisct, dto);
		dto.setTrans(dto.getRefund());
		dto.setStatus(StatusConstants.ACTIVE);
		prodComponent.saveDisct(dto, disctCountyIds, optr);
		return JSON;
	}

	/**
	* @Description:根据折扣编号删除折扣
	 */
	public String deleteDisct() throws Exception {
			getRoot().setSuccess(prodComponent.deleteDisct(doneId));
		return JSON;

	}

	public String deleteTariff() throws Exception {
		getRoot().setSuccess(prodComponent.deleteTariff(doneId));
		return JSON;

	}

	public String deletePord() throws Exception {
		getRoot().setSimpleObj(prodComponent.deletePord(doneId));
		return JSON;

	}

	public String checkGroupRes() throws Exception {
		getRoot().setSuccess(prodComponent.checkGroupRes(doneId,query));
		return JSON;

	}
	/**
	 * 验证资源组是否适合该地市
	 * @return
	 * @throws Exception
	 */
	public String validRes() throws Exception {
		getRoot().setSimpleObj(prodComponent.validRes(records,doneId));
		return JSON;
	}
	
	/**
	 * 查询可用地区的最低定价grid（含有未配置的地市）
	 * @return
	 * @throws Exception
	 */
	public String queryLowestPrice() throws Exception {
		getRoot().setRecords(prodComponent.queryLowestPrice(doneId,getOptr()));
		return JSON_RECORDS;
	}
	
	public String saveLowestPrice() throws Exception{
		List<PProdCountyPrice> valueList = null;
		if(StringHelper.isNotEmpty(records)){
			Type type = new TypeToken<List<PProdCountyPrice>>(){}.getType();
			Gson gson = new Gson();
			valueList = gson.fromJson(records, type);
		}
		prodComponent.saveLowestPrice(valueList,doneId);
		return JSON;
	}
	

	public String getStaticResList() {
			return staticResList;
		}

		public void setStaticResList(String staticResList) {
			this.staticResList = staticResList;
		}
	public String getDynamicResList() {
		return dynamicResList;
	}

	public void setDynamicResList(String dynamicResList) {
		this.dynamicResList = dynamicResList;
	}

	public ProdComponent getProdComponent() {
		return prodComponent;
	}

	public void setProdComponent(ProdComponent prodComponent) {
		this.prodComponent = prodComponent;
	}

	public ProdDto getProdDto() {
		return prodDto;
	}

	public void setProdDto(ProdDto prodDto) {
		this.prodDto = prodDto;
	}

	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}

	public String getRecords() {
		return records;
	}

	public void setRecords(String records) {
		this.records = records;
	}

	public String getDoneId() {
		return doneId;
	}

	public void setDoneId(String doneId) {
		this.doneId = doneId;
	}

	public String getPackList() {
		return packList;
	}

	public void setPackList(String packList) {
		this.packList = packList;
	}

	public void setAcctItemId(String acctItemId) {
		this.acctItemId = acctItemId;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}


	public ProdTariffDto getProdTariffDto() {
		return prodTariffDto;
	}


	public void setProdTariffDto(ProdTariffDto prodTariffDto) {
		this.prodTariffDto = prodTariffDto;
	}
	public String[] getDictProdIds() {
		return dictProdIds;
	}
	public void setDictProdIds(String[] dictProdIds) {
		this.dictProdIds = dictProdIds;
	}
	public String getServId() {
		return ServId;
	}
	public void setServId(String servId) {
		ServId = servId;
	}
	public void setShowAll(String showAll) {
		this.showAll = showAll;
	}
	public void setCountyId(String countyId) {
		this.countyId = countyId;
	}
	public PProdTariffDisct getPProdTariffDisct() {
		return PProdTariffDisct;
	}
	public void setPProdTariffDisct(PProdTariffDisct pProdTariffDisct) {
		PProdTariffDisct = pProdTariffDisct;
	}
	/**
	 * @param prodCountyIds the prodCountyIds to set
	 */
	public void setProdCountyIds(String[] prodCountyIds) {
		this.prodCountyIds = prodCountyIds;
	}
	public PProdDict getProdDict() {
		return prodDict;
	}

	public void setProdDict(PProdDict prodDict) {
		this.prodDict = prodDict;
	}
	public void setDisctCountyIds(String[] disctCountyIds) {
		this.disctCountyIds = disctCountyIds;
	}
}
