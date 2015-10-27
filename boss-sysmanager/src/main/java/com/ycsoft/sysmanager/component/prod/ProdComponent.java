package com.ycsoft.sysmanager.component.prod;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.reflect.TypeToken;
import com.ycsoft.beans.config.TAcctitemToProd;
import com.ycsoft.beans.config.TServerRes;
import com.ycsoft.beans.prod.PDictProd;
import com.ycsoft.beans.prod.PPackageProd;
import com.ycsoft.beans.prod.PPackageProdHis;
import com.ycsoft.beans.prod.PProd;
import com.ycsoft.beans.prod.PProdCounty;
import com.ycsoft.beans.prod.PProdCountyPrice;
import com.ycsoft.beans.prod.PProdCountyRes;
import com.ycsoft.beans.prod.PProdDict;
import com.ycsoft.beans.prod.PProdDictCounty;
import com.ycsoft.beans.prod.PProdDynRes;
import com.ycsoft.beans.prod.PProdResChange;
import com.ycsoft.beans.prod.PProdStaticRes;
import com.ycsoft.beans.prod.PProdTariff;
import com.ycsoft.beans.prod.PProdTariffCounty;
import com.ycsoft.beans.prod.PProdTariffDisct;
import com.ycsoft.beans.system.SArea;
import com.ycsoft.beans.system.SCounty;
import com.ycsoft.beans.system.SItemvalue;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.beans.system.SSysChange;
import com.ycsoft.business.dao.config.TAcctitemToProdDao;
import com.ycsoft.business.dao.config.TRuleDefineDao;
import com.ycsoft.business.dao.config.TServerCountyDao;
import com.ycsoft.business.dao.prod.PDictProdDao;
import com.ycsoft.business.dao.prod.PPackageProdDao;
import com.ycsoft.business.dao.prod.PPackageProdHisDao;
import com.ycsoft.business.dao.prod.PProdCountyDao;
import com.ycsoft.business.dao.prod.PProdCountyPriceDao;
import com.ycsoft.business.dao.prod.PProdCountyResDao;
import com.ycsoft.business.dao.prod.PProdDao;
import com.ycsoft.business.dao.prod.PProdDictCountyDao;
import com.ycsoft.business.dao.prod.PProdDictDao;
import com.ycsoft.business.dao.prod.PProdDynResDao;
import com.ycsoft.business.dao.prod.PProdResChangeDao;
import com.ycsoft.business.dao.prod.PProdStaticResDao;
import com.ycsoft.business.dao.prod.PProdTariffCountyDao;
import com.ycsoft.business.dao.prod.PProdTariffDao;
import com.ycsoft.business.dao.prod.PProdTariffDisctCountyDao;
import com.ycsoft.business.dao.prod.PProdTariffDisctDao;
import com.ycsoft.business.dao.prod.PPromFeeCountyDao;
import com.ycsoft.business.dao.prod.PPromotionDao;
import com.ycsoft.business.dao.prod.PResDao;
import com.ycsoft.business.dao.prod.PResgroupDao;
import com.ycsoft.business.dao.prod.PSpkgDao;
import com.ycsoft.business.dao.prod.TServerResDao;
import com.ycsoft.business.dao.system.SAreaDao;
import com.ycsoft.business.dao.system.SCountyDao;
import com.ycsoft.business.dao.system.SRoleCountyDao;
import com.ycsoft.business.dao.system.SSysChangeDao;
import com.ycsoft.business.dto.core.prod.ProdTariffDto;
import com.ycsoft.commons.abstracts.BaseComponent;
import com.ycsoft.commons.constants.BusiCmdConstants;
import com.ycsoft.commons.constants.DataRight;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.constants.FuncCode;
import com.ycsoft.commons.constants.SysChangeType;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ComponentException;
import com.ycsoft.commons.exception.ErrorCode;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.commons.helper.JsonHelper;
import com.ycsoft.commons.helper.NumericHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.commons.tree.TreeBuilder;
import com.ycsoft.commons.tree.TreeNode;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.helper.BeanHelper;
import com.ycsoft.sysmanager.dto.config.VewRuleDto;
import com.ycsoft.sysmanager.dto.prod.PProdDictDto;
import com.ycsoft.sysmanager.dto.prod.ProdCountyResDto;
import com.ycsoft.sysmanager.dto.prod.ProdDto;
import com.ycsoft.sysmanager.dto.prod.ProdResServIdDto;
import com.ycsoft.sysmanager.dto.prod.ResDto;
import com.ycsoft.sysmanager.dto.prod.ResGroupDto;
import com.ycsoft.sysmanager.dto.tree.TreeDto;
import com.ycsoft.sysmanager.web.commons.interceptor.WebOptr;

@Component
public class ProdComponent extends BaseComponent {
	private PProdDao pProdDao;// 产品定义
	private PProdCountyDao pProdCountyDao;
	private PProdTariffDao pProdTariffDao;
	private SRoleCountyDao sRoleCountyDao;
	private PResDao pResDao;
	private PResgroupDao pResgroupDao;
	private SAreaDao sAreaDao;
	private PProdDynResDao pProdDynResDao;
	private PProdStaticResDao pProdStaticResDao;
	private PProdTariffDisctDao pProdTariffDisctDao;
	private PDictProdDao pDictProdDao;
	private PPackageProdDao pPackageProdDao;
	private PPackageProdHisDao pPackageProdHisDao;
	private TRuleDefineDao tRuleDefineDao;
	private SCountyDao sCountyDao;
	private PProdCountyResDao pProdCountyResDao;
	private PProdResChangeDao pProdResChangeDao;
	private PProdTariffCountyDao pProdTariffCountyDao;
	private PProdTariffDisctCountyDao pProdTariffDisctCountyDao;
	private TAcctitemToProdDao tAcctitemToProdDao;
	private TServerResDao tServerResDao;
	
	private PPromotionDao pPromotionDao;
	private PProdDictDao pProdDictDao;
	private PProdDictCountyDao pProdDictCountyDao;
	private PPromFeeCountyDao pPromFeeCountyDao;
	
	private PProdCountyPriceDao pProdCountyPriceDao;
	
	private SSysChangeDao sSysChangeDao;
	private TServerCountyDao tServerCountyDao; 
	@Autowired
	private PSpkgDao pSpkgDao;

	/**
	 * 查询产品树
	 * @param optr
	 * @param showExp 是否查看失效
	 * @return
	 * @throws Exception
	 */
//	public List<TreeNode> getProdTree(SOptr optr,String showAll,String startEffDate,
//			String endEffDate, String startExpDate, String endExpDate) throws Exception {
//		List<SItemvalue> servList = MemoryDict.getDicts(DictKey.SERV_ID);
//		List<SItemvalue> pkgList = MemoryDict.getDicts(DictKey.PROD_TYPE);
//		String countyDataRight = null;
////		if(SystemConstants.COUNTY_ALL.equals(optr.getCounty_id())){
////			countyDataRight = SystemConstants.DEFAULT_DATA_RIGHT;
////		}else{
//		countyDataRight = this.queryDataRightCon(optr, DataRight.CHANGE_COUNTY.toString());
////		}
//		List<PProd> prods = pProdDao.getProdByAreaId(countyDataRight,showAll, startEffDate,
//				endEffDate, startExpDate, endExpDate);
//		List<PProd> baseProds = null;
////		List<PProd> upkgProds = null;
//		List<TreeNode> ct = null;
////		List<TreeNode> et = null;
//		
//		Map<String, List<PProd>> disctmap = CollectionHelper.converToMap(prods,"prod_type");
//
//		baseProds = disctmap.get(SystemConstants.PROD_TYPE_BASE);
////		upkgProds = disctmap.get(SystemConstants.PROD_TYPE_USERPKG);
//		//客户套餐暂时取消
////		List<PProd> cpkgProds = null;  
////		List<TreeNode> dt = null;
////		cpkgProds = disctmap.get(SystemConstants.PROD_TYPE_CUSTPKG);
////		if(cpkgProds!=null){
////			dt = TreeBuilder.convertToProdNodes(cpkgProds, "prod_id","prod_name");
////		}
////		for (int i = pkgList.size()-1; i>=0; i--) {
////			if(pkgList.get(i).getItem_value().equals(SystemConstants.PROD_TYPE_CUSTPKG)){
////				pkgList.remove(i);
////			}
////		}
//		
//		List<TreeNode> at = TreeBuilder.convertToNodes(pkgList, "item_value","item_name");
//		List<TreeNode> bt = TreeBuilder.convertToNodes(servList, "item_value","item_name");
//		bt.addAll(at);
//		
//		if(prods!=null){
//			 ct = TreeBuilder.convertToProdNodes(prods, "prod_id","prod_name");
//		}
////		if(upkgProds!=null){
////			 et = TreeBuilder.convertToProdNodes(upkgProds, "prod_id","prod_name");
////		}
////		for (int i = 0; i < pkgList.size(); i++) {
////			if(pkgList.get(i).getItem_value().equals(SystemConstants.PROD_TYPE_BASE)){
////				for (int j = 0; j < servList.size(); j++) {
////					at.get(i).getChildren().add(bt.get(j));
////				}
////			}
////			
////			if(pkgList.get(i).getItem_value().equals(SystemConstants.PROD_TYPE_CUSTPKG)&&cpkgProds!=null){
////				for (int j = 0; j < cpkgProds.size(); j++) {
////					dt.get(j).setLeaf(true);
////					if (cpkgProds.get(j).getProd_type().equals(SystemConstants.PROD_TYPE_CUSTPKG)) {
////						if(optr.getArea_id().equals(SystemConstants.AREA_ALL)){
////							dt.get(j).getOthers().put("att", "prodId");
////						}else{
////							if(cpkgProds.get(j).getFor_area_id().equals(SystemConstants.AREA_ALL)){
////								dt.get(j).getOthers().put("att", "areaProdId");
////							}else{
////								dt.get(j).getOthers().put("att", "prodId");
////							}
////						} 
////						dt.get(j).getOthers().put("area_id", cpkgProds.get(j).getArea_id());
////						at.get(i).getChildren().add(dt.get(j));
////					}
////				}
////			}
////			if(pkgList.get(i).getItem_value().equals(SystemConstants.PROD_TYPE_USERPKG)&&upkgProds!=null){
////				for (int j = 0; j < upkgProds.size(); j++) {
////					et.get(j).setLeaf(true);
////					if (upkgProds.get(j).getProd_type().equals(SystemConstants.PROD_TYPE_USERPKG)) {
////						if(optr.getArea_id().equals(SystemConstants.AREA_ALL)){
////							et.get(j).getOthers().put("att", "prodId");
////						}else {
////							if(upkgProds.get(j).getFor_area_id().equals(SystemConstants.AREA_ALL)){
////								et.get(j).getOthers().put("att", "areaProdId");
////							}else{
////								et.get(j).getOthers().put("att", "prodId");
////							}
////						}
////						et.get(j).getOthers().put("area_id", upkgProds.get(j).getArea_id());
////						at.get(i).getChildren().add(et.get(j));
////					}
////				}
////			}
////		}
//		for (int i = 0; i < servList.size(); i++) {
//			if(prods!=null){
//				for (int j = 0; j < prods.size(); j++) {
//					ct.get(j).setLeaf(true);
//					if (servList.get(i).getItem_value().equals(prods.get(j).getServ_id())) {
//						if(optr.getArea_id().equals(SystemConstants.AREA_ALL)){
//							ct.get(j).getOthers().put("att", "prodId");
//						}else{
//							if(prods.get(j).getFor_area_id().equals(SystemConstants.AREA_ALL)){
//								ct.get(j).getOthers().put("att", "areaProdId");
//							}else{
//								ct.get(j).getOthers().put("att", "prodId");
//							}
//						}
//						ct.get(j).getOthers().put("area_id", prods.get(j).getArea_id());
//						bt.get(i).getChildren().add(ct.get(j));
//					}
//			
//				}
//			}
//		}
//		return bt;
//	}

	
	public List<TreeNode> getProdTree(SOptr optr,String showAll,String startEffDate,
			String endEffDate, String startExpDate, String endExpDate) throws Exception {
		List<SItemvalue> servList = MemoryDict.getDicts(DictKey.SERV_ID);
		List<SItemvalue> pkgList = MemoryDict.getDicts(DictKey.PROD_TYPE);
		List<SItemvalue> allList = new ArrayList<SItemvalue>();
		allList.addAll(servList);
		allList.addAll(pkgList);
        Iterator<SItemvalue> it = allList.iterator();  
        while(it.hasNext()){  
        	SItemvalue tempobj = it.next();  
            if(tempobj.getItem_value().equals(SystemConstants.PROD_TYPE_BASE)){  
                //移除当前的对象  
                it.remove();  
            }  
        }  
		String countyDataRight = null;
		countyDataRight = this.queryDataRightCon(optr, DataRight.CHANGE_COUNTY.toString());
		List<PProd> prods = pProdDao.getProdByAreaId(countyDataRight,showAll, startEffDate,
				endEffDate, startExpDate, endExpDate);
		
//		Map<String, List<PProd>> prodtmap = CollectionHelper.converToMap(prods,"prod_type");
		List<TreeNode> ct = null;
		List<TreeNode> bt = TreeBuilder.convertToNodes(allList, "item_value","item_name");
		
		if(prods!=null){
			 ct = TreeBuilder.convertToProdNodes(prods, "prod_id","prod_name");
		}

		for (int i = 0; i < allList.size(); i++) {
			if(prods!=null){
				for (int j = 0; j < prods.size(); j++) {
					ct.get(j).setLeaf(true);
					if (allList.get(i).getItem_value().equals(prods.get(j).getServ_id()) || allList.get(i).getItem_value().equals(prods.get(j).getProd_type())) {
						if(optr.getArea_id().equals(SystemConstants.AREA_ALL)){
							ct.get(j).getOthers().put("att", "prodId");
						}else{
							if(prods.get(j).getFor_area_id().equals(SystemConstants.AREA_ALL)){
								ct.get(j).getOthers().put("att", "areaProdId");
							}else{
								ct.get(j).getOthers().put("att", "prodId");
							}
						}
						ct.get(j).getOthers().put("area_id", prods.get(j).getArea_id());
						bt.get(i).getChildren().add(ct.get(j));
					}
			
				}
			}
		}
		return bt;
	}
	
	/**
	 * 地区地市资源关系树 按照TreeNode结构排列
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<TreeNode> getDistrictResTree(String prodId,String forareaId ) throws Exception {
		List<SItemvalue> areas = new ArrayList<SItemvalue>();
		List<SCounty> countys = null;
		/*String areaId = optr.getArea_id();
		String countyId = optr.getCounty_id();
		if (SystemConstants.AREA_ALL.equals(areaId)) {
			//适用地区如果是全省，
			if(SystemConstants.AREA_ALL.equals(forareaId)){
				countys = sCountyDao.findAll();
				areas = MemoryDict.getDicts(DictKey.AREA);
			}else{
				countys = sCountyDao.getCountyByAreaId(forareaId);
				String areaName = MemoryDict.getDictName(DictKey.AREA, forareaId);
				areas.add(new SItemvalue(areaName, forareaId));
			}
		} else {
			String dataRight = this.queryDataRightCon(optr, DataRight.CHANGE_COUNTY.toString());
			countys = sCountyDao.querySwitchCounty(dataRight);
			String areaName = MemoryDict.getDictName(DictKey.AREA, areaId);
			areas.add(new SItemvalue(areaName, areaId));
		}*/
		
		String dataRight = this.queryDataRightCon(WebOptr.getOptr(), DataRight.CHANGE_COUNTY.toString());
		countys = sCountyDao.querySwitchCounty(dataRight);
		List<String> areaIdList = sCountyDao.querySwitchArea(dataRight);
		for(String areaId : areaIdList){
			String areaName = MemoryDict.getDictName(DictKey.AREA, areaId);
			areas.add(new SItemvalue(areaName, areaId));
		}
		
		
		List<TreeNode> at = TreeBuilder.convertToNodes(areas, "item_value","item_name");
		List<TreeNode> bt = TreeBuilder.convertToNodes(countys, "county_id","county_name");
		List<ProdCountyResDto> countyRes = null;
		List<TreeNode> ct = null;
		boolean isDept = false;
		Map<String, String> others = new HashMap<String, String>();
		if (StringHelper.isNotEmpty(prodId)) {
			countyRes = pProdCountyResDao.getProdCountyResByProdId(prodId);
			ct = TreeBuilder.convertToNodes(countyRes, "res_id", "res_name");
			isDept = true;
		}
		if (isDept) {
			setCounty2Res(countys, countyRes, bt, ct);
			setArea2County(areas, countys, at, bt);
		} else {
			setArea2County(areas, countys, at, bt);
			for (int i = 0; i < countys.size(); i++) {
				bt.get(i).setLeaf(false);
				others.put("att", "county");
				bt.get(i).setOthers(others);
			}
		}
		return at;
	}

	private void setCounty2Res(List<SCounty> countys,
			List<ProdCountyResDto> countyRes, List<TreeNode> bt,
			List<TreeNode> ct) {
		Map<String, String> others = new HashMap<String, String>();
		Map<String, String> othersRes = new HashMap<String, String>();
		for (int i = 0; i < countys.size(); i++) {
			for (int j = 0; j < countyRes.size(); j++) {
				if (countys.get(i).getCounty_id().equals(countyRes.get(j).getCounty_id())) {
					ct.get(j).setLeaf(false);
					others.put("att", "county");
					othersRes.put("att", "res");
					ct.get(j).setOthers(othersRes);
					bt.get(i).setOthers(others);
					bt.get(i).getChildren().add(ct.get(j));
				}
			}
			if (bt.get(i).getChildren().size() == 0) {
				bt.get(i).setLeaf(false);
				others.put("att", "county");
				bt.get(i).setOthers(others);
			}
		}
	}

	private void setArea2County(List<SItemvalue> areas, List<SCounty> countys,
			List<TreeNode> at, List<TreeNode> bt) {
		Map<String, String> others = new HashMap<String, String>();
		for (int i = 0; i < areas.size(); i++) {
			for (int j = 0; j < countys.size(); j++) {
				if (areas.get(i).getItem_value().equals(countys.get(j).getArea_id())) {
					at.get(i).getChildren().add(bt.get(j));
					others.put("att", "area");
					at.get(i).setOthers(others);
				}
			}
		}
	}

	/**
	 * 产品目录树
	 */
	public List<PProdDictDto> getPordDictTree(String prodId) throws Exception {
		List<PProdDictDto> nodes = null;
		if(WebOptr.getOptr().getCounty_id().equals(SystemConstants.COUNTY_ALL)){
			nodes = pDictProdDao.getNodeAll();
		}else{
			nodes = pProdDictDao.queryProdDictByCountyId(WebOptr.getOptr().getCounty_id());
		}
		List<PProdDictDto> prods = null;
		if (StringHelper.isNotEmpty(prodId)) {
			prods = pDictProdDao.getProdNodeByProdId(prodId);
		}
		for (int i = 0; i < nodes.size(); i++) {
			nodes.get(i).setChecked(false);
			if (prods != null && !"null".equals(prods)) {
				for (int j = 0; j < prods.size(); j++) {
					if (nodes.get(i).getNode_id().equals(prods.get(j).getNode_id())) {
						nodes.get(i).setChecked(true);
					}
				}
			}
		}
		return nodes;
	}
	
	/**
	 * 根据地市编号查询产品目录
	 * @param countyId
	 * @return
	 * @throws JDBCException
	 */
	public List<PProdDictDto> queryProdDictByCountyId(SOptr optr) throws JDBCException{
		if(optr.getCounty_id().equals(SystemConstants.COUNTY_ALL)){
			return pProdDictDao.queryProdDictAll();
		}else{
			return pProdDictDao.queryProdDictByCountyId(optr.getCounty_id());
		}
	}
	
	/**
	 * 根据目录编号删除没有使用的目录
	 * @param nodeId
	 * @return
	 * @throws Exception
	 */
	public String deleteDict(String nodeId) throws Exception{
		List<PDictProd> dictList = pDictProdDao.queryProdDict(nodeId);
		List<PProdDict> prodList = pProdDictDao.queryDictByPid(nodeId);
		if(dictList.size() > 0){
			return "{'success':false,'msg':'该目录下面已经存在【"+dictList.get(0).getProd_name()+"】等["+dictList.size()+"]个产品，暂不能删除。'}";
		}else if(prodList.size()>0){
			return "{'success':false,'msg':'该目录下面有子目录，先删除子目录。'}";
		}else{
			pProdDictDao.deleteProdDict(nodeId);
			pProdDictCountyDao.deleteById(nodeId);
			return "{'success':true}";
		}
	}
	
	/**
	 * 保存目录信息
	 */
	public void saveProdDict(PProdDict prodDict,String prodCountyIds) throws Exception{
		prodDict.setNode_id(pProdDictDao.findSequence().toString());
		prodDict.setArea_id(WebOptr.getOptr().getArea_id());
		prodDict.setCounty_id(WebOptr.getOptr().getCounty_id());
		prodDict.setOptr_id(WebOptr.getOptr().getOptr_id());
		pProdDictDao.save(prodDict);
		
		saveProdDictCounty(prodDict.getNode_id(),prodCountyIds);
	}
	
	/**
	 * 更新目录信息
	 */
	public void updateProdDict(PProdDict prodDict,String prodCountyIds) throws Exception{
		pProdDictDao.update(prodDict);
		//保存目录适用地区
		saveProdDictCounty(prodDict.getNode_id(),prodCountyIds);
	}
	
	public void saveProdDictCounty(String nodeId,String prodCountyIds) throws JDBCException{
		pProdDictCountyDao.deleteById(nodeId);
		if(StringHelper.isNotEmpty(prodCountyIds)){
			String[] dictCountys = prodCountyIds.split(",");
			for(int i=0;i<dictCountys.length;i++){
				PProdDictCounty entity = new PProdDictCounty();
				entity.setNode_id(nodeId);
				entity.setCounty_id(dictCountys[i]);
				pProdDictCountyDao.save(entity);
			}
		}
	}
	
	public List<TreeDto> checkProdDictCountyTree(String nodeId,String nodepId,String type,List<TreeDto> countyList) throws Exception{
		String value = "";
		//add 为新增目录
		if(type.equals("add")){
			value = nodeId;
		}else{
			value = nodepId;
		}
		if(!nodepId.equals("-1")||(nodepId.equals("-1") && !WebOptr.getOptr().getCounty_id().equals(SystemConstants.COUNTY_ALL))){
			List<PProdDictCounty> dictList = pProdDictCountyDao.queryCountyById(value);
			countyList = chooseDictCounty(countyList,dictList);
		}
//		else if(!optr.getCounty_id().equals(SystemConstants.COUNTY_ALL)){
//			List<PProdDictCounty> dictList = pProdDictCountyDao.queryCountyById(value);
//			countyList = chooseDictCounty(countyList,dictList);
//		}
		return countyList;
	}
	
	public List<TreeDto> chooseDictCounty(List<TreeDto> countyList,List<PProdDictCounty> dictList) throws JDBCException{
		for (int j = countyList.size() - 1; j >= 0; j--) {
			boolean ck = false;
			for(PProdDictCounty pt : dictList){
				if(pt.getCounty_id().equals(countyList.get(j).getId())){
					ck = true;
				}
			}
			if (!ck) {
				countyList.remove(j);
			}
		}
		return countyList;
	}
	
	
	/**
	 *资费、促销应用地区树 
	 *type 格式String[] type = {"AREA","TARIFF"};type[0]有2种值：AREA,COUNTY与传入areaId值对应;
	 */
	public List<TreeDto> getCountyTree(SOptr optr,String[] type,String value)throws Exception{
		List<TreeDto> countys = null;
		List<TreeDto> selectedList = null;
		
		String dataRight = this.queryDataRightCon(optr, DataRight.CHANGE_COUNTY.toString());
		countys = sCountyDao.getCountyTreeByDataRight(dataRight);
		
//		if (areaId.equals(SystemConstants.AREA_ALL)||areaId.equals(SystemConstants.COUNTY_ALL)) {
//			countys = sCountyDao.getCountyTree();
//		}else if(type[0].toString().equals("COUNTY")){
//			countys = sCountyDao.getCountyTreeByCountyId(areaId);
//		}else if(type[0].toString().equals("AREA")){
//			countys = sCountyDao.getCountyTreeByAreaId(areaId);
//		}
		
		if (StringHelper.isNotEmpty(value)) {
			if(type[1].toString().equals("TARIFF")){//资费应用地区
				selectedList = pProdTariffCountyDao.getTariffCountyBytariffId(value);
			}else if(type[1].toString().equals("PROMOTION")){//促销应用地区
				selectedList = pPromotionDao.getPromCountyById(value);
			}else if(type[1].equals("PROD")){
				selectedList = pProdCountyDao.getProdCountyById(value);
			}else if(type[1].equals("ROLE")){
				selectedList = sRoleCountyDao.getRoleCountyById(value);
			}else if(type[1].equals("PROMOTIONTHEME")){
				selectedList = pPromotionDao.getPromThemeCountyById(value);
			}else if(type[1].equals("DICT")){
				selectedList = pProdDictCountyDao.getProdDictCountyById(value);
			}else if(type[1].equals("PROMFEE")){
				selectedList = pPromFeeCountyDao.querybyPromFeeId(value);
			}else if(type[1].equals("DISCT")){
				selectedList = pProdTariffDisctCountyDao.getTariffDisctCountyByDisctId(value);
			}else if(type[1].equals("SERVER")){
				selectedList = tServerCountyDao.getServerCountyById(value);
			}
		}
		
		for (int i = countys.size() - 1; i >= 0; i--) {
			if(type[0].toString().equals("CHOOSE")){//去掉湖北省
				if(countys.get(i).getId().equals(SystemConstants.COUNTY_ALL)
						|| countys.get(i).getId().equals(SystemConstants.AREA_ALL)
						|| countys.get(i).getId().equals(SystemConstants.AREA_ALL+"-1")){
					countys.remove(i);
					continue;
				}
			}
			countys.get(i).setChecked(false);
			if (selectedList != null) {
				for (int j = 0; j < selectedList.size(); j++) {
					if(countys.get(i).getId().equals(selectedList.get(j).getId())){
						countys.get(i).setChecked(true);
					}
				}
			}
		}
		
		return countys;
	}

	// -----产品查询-------------------------------------------------------------------------------------------------

	/**
	 * 根据账目id查询产品信息
	 * 
	 * @param acctItemId
	 * @return
	 * @throws Exception
	 */
	public List<PProd> queryProdByAcctItemId(String acctItemId)
			throws Exception {
		return pProdDao.queryProdByAcctItemId(acctItemId);
	}

	/**
	 * @Description: 查询地区
	 * @param optr
	 * @return
	 * @throws Exception
	 * @return List<SArea>
	 */
	public List<SArea> queryArea(SOptr optr) throws Exception {
		if (optr.getLogin_name().equals(SystemConstants.SUPER_ADMIN)) {
			return sAreaDao.findAll();
		} else {
			return sAreaDao.getAreaById(optr.getArea_id());
		}
	}

	public List<PProd> queryProdAll(SOptr optr) throws Exception {
		String dataRight = this.queryDataRightCon(optr, DataRight.CHANGE_COUNTY.toString());
		return pProdDao.getProdByAreaId(dataRight,null,null,null,null,null);
	}

	public List<PProd> getProdByCounty(String countyId) throws Exception {
		String countyDataRight = "county_id='"+countyId+"' ";
		return pProdDao.getProdByAreaId(countyDataRight,null,null,null,null,null);
	}
	
	/**
	 * @Description: 静态资源
	 * @param prodId
	 * @return
	 * @throws Exception
	 * @return List<PProdStaticRes>
	 */
	public List<ProdCountyResDto> queryStaticAllByProdId(String prodId)
			throws Exception {
		List<ProdCountyResDto> staticList = pProdStaticResDao.queryStaticResByprodId(prodId);
		for(ProdCountyResDto dto:staticList){
			List<TServerRes> ResList =  tServerResDao.getServerRes(dto.getCounty_id());
			Map<String, List<TServerRes>> serverResmap = CollectionHelper.converToMap(ResList, "boss_res_id");
			List<TServerRes> serverList =  serverResmap.get(dto.getRes_id());
			if(serverList ==null) serverList = new ArrayList<TServerRes>();
			String str ="";
			for(TServerRes serRes: serverList){
				str+=serRes.getExternal_res_id()+",";
			}
			str = StringHelper.delEndChar(str,1);
			dto.setServerIds(str);
		}
		return staticList;
	}

	/**
	 * @Description: 动态资源
	 * @param prodId
	 * @return
	 * @throws Exception
	 * @return List<ResGroupDto>
	 */
	public List<ResGroupDto> queryDynAllByProdId(String prodId)
			throws Exception {
		List<ResGroupDto> dto = pProdDynResDao.queryDynByProdId(prodId);
		return dto;
	}
	/**
	 * 根据产品查询资费
	 * @param prodId
	 * @return
	 * @throws Exception
	 */
	public List<ProdTariffDto> queryTariffByProdId(String prodId,String[] prodCountyIds)throws Exception {
		if(prodCountyIds.length == 1 && StringHelper.isEmpty(prodCountyIds[0])){
			throw new ComponentException("请先选择应用地区!");
		}
		List<ProdTariffDto> list = pProdTariffDao.queryTariffByCounty(prodId,prodCountyIds);
		List<ProdTariffDto> tarList = new ArrayList<ProdTariffDto>();
		for(ProdTariffDto dto:list){
			boolean key = true;
			for(ProdTariffDto tar:tarList){
				if(tar.getBilling_cycle().equals(dto.getBilling_cycle()) && tar.getRent().equals(dto.getRent()) 
						&& tar.getBilling_type().equals(dto.getBilling_type())){
					key = false;
					break;
				}
			}
			if(key){
				tarList.add(dto);
			}
		}
		return tarList;
	}
	
	/**
	 * 根据产品获取资费以及该资费的折扣 
	 */
	public List<ProdTariffDto> getTariffDisctByProdId(String prodId,SOptr optr)
			throws Exception {
		String dataRight = this.queryDataRightCon(optr, DataRight.CHANGE_COUNTY.toString());
		if(optr.getCounty_id().equals(SystemConstants.COUNTY_ALL)){
			dataRight = "1=1";
		}
		List<ProdTariffDto> dtoList = pProdTariffDao.queryTariffByProdId(prodId,dataRight);
		String[] tariffs =  CollectionHelper.converValueToArray(dtoList,"tariff_id");
		List<PProdTariffCounty> ptCountyList = new ArrayList<PProdTariffCounty>();
		if(tariffs.length>0){
			ptCountyList = pProdTariffCountyDao.queryTariffCountyById(tariffs);
		}
		Map<String, List<PProdTariffCounty>> tCountyMap = CollectionHelper.converToMap(ptCountyList,"tariff_id");
//		Map<String, List<PProdTariffDisct>> disctmap = CollectionHelper.converToMap(pProdTariffDisctDao.queryDisctByPordId(prodId),
//						"tariff_id");
		Map<String, VewRuleDto> ruleMap = CollectionHelper.converToMapSingle(tRuleDefineDao.findRuleALL(), "rule_id");
		for (ProdTariffDto pt : dtoList) {
			if(StringHelper.isNotEmpty(pt.getRule_id())){
				VewRuleDto rule = ruleMap.get(pt.getRule_id());
				if( rule!= null ){
					pt.setRule_id_text(rule.getRule_name());
				}
			}
			if(StringHelper.isNotEmpty(pt.getBill_rule())){
				VewRuleDto rule = ruleMap.get(pt.getBill_rule());
				if( rule!= null ){
					pt.setBill_rule_text(rule.getRule_name());
				}
			}
			if(StringHelper.isNotEmpty(pt.getDay_rent_cal_type())){
				VewRuleDto rule = ruleMap.get(pt.getDay_rent_cal_type());
				if( rule!= null ){
					pt.setDay_rent_cal_type_text(rule.getRule_name());
				}
			}
			if(StringHelper.isNotEmpty(pt.getMonth_rent_cal_type())){
				VewRuleDto rule = ruleMap.get(pt.getMonth_rent_cal_type());
				if( rule!= null ){
					pt.setMonth_rent_cal_type_text(rule.getRule_name());
				}
			}
			if(StringHelper.isNotEmpty(pt.getUse_fee_rule())){
				VewRuleDto rule = ruleMap.get(pt.getUse_fee_rule());
				if( rule!= null ){
					pt.setUse_fee_rule_text(rule.getRule_name());
				}
			}
//			pt.setDisctList(disctmap.get(pt.getTariff_id()));
//			pt.setCountyList(pProdTariffCountyDao.queryTariffCountyById(pt.getTariff_id()));
			if(tCountyMap.get(pt.getTariff_id()) != null){
				List<String> countyList = CollectionHelper.converValueToList(tCountyMap.get(pt.getTariff_id()),"county_id");
				pt.setCountyList(countyList);
			}
		}
		return dtoList;
	}

	/**
	 * @Description: 查询某个产品的所有信息
	 * @param prodId
	 * @return
	 * @throws Exception
	 * @return ProdDto
	 */
	public ProdDto queryProdById(String prodId,SOptr optr) throws Exception {
		ProdDto dto = pProdDao.queryProdById(prodId);
		
		if (dto != null) {
			dto.setDynamicResList(queryDynAllByProdId(prodId));
			dto.setStaticResList(queryStaticAllByProdId(prodId));
			dto.setPackList(queryPackProdById(prodId));
			dto.setProdTariffList(getTariffDisctByProdId(prodId,optr));
			dto.setCountyList(queryProdCountyIds(prodId));
		}
		return dto;
	}

	public List<String> queryProdCountyIds(String prodId) throws Exception{
		return pProdCountyDao.queryByProdId(prodId);
	}
	
	/**
	 * @Description:获取子产品信息
	 * @param prodId
	 * @return
	 * @throws Exception
	 * @return List<PPackageProd>
	 */
	public List<PPackageProd> queryPackProdById(String prodId) throws Exception {
		List<PPackageProd> pkgProdList = pPackageProdDao.queryPackProdByProdId(prodId);
		return changeProd(pkgProdList);
	}
	
	public List<PPackageProd> queryPackProdById(String pkgId,String pkgTariffId) throws Exception {
		
		List<PPackageProd> pkgProdList = new ArrayList<PPackageProd>();
		if(StringHelper.isNotEmpty(pkgTariffId)){	//修改资费
			List<PPackageProd> list = pPackageProdDao.getPackProdById(pkgId,pkgTariffId);
			Map<String, List<PPackageProd>> map = CollectionHelper.converToMap(
					list, new String[] { "prod_id", "package_tariff_id"});
			
			for(String key : map.keySet()){
				List<PPackageProd> ppList = map.get(key);
				//默认为市场分成子产品,新增 财务分成子产品
				if(ppList.size() == 1){
					PPackageProd pp = ppList.get(0);
					PPackageProd pkgProd = new PPackageProd();
					pkgProd.setPackage_id(pp.getPackage_id());
//					pkgProd.setProd_id(pp.getProd_id());
//					pkgProd.setPackage_tariff_id(pp.getPackage_tariff_id());
//					pkgProd.setType(SystemConstants.PACKAGE_FINANCE_TYPE);
					pkgProdList.add(pkgProd);
				}
				
				pkgProdList.addAll(ppList);
			}
		} else {	//新建资费
			List<PPackageProd> pList = pPackageProdDao.queryPkgProdById(pkgId);
			for(PPackageProd pkg : pList){
				pkg.setPackage_id(pkgId);
		//		pkg.setType(SystemConstants.PACKAGE_MARKET_TYPE);
				pkgProdList.add(pkg);
				
				PPackageProd pp = new PPackageProd();
				pp.setPackage_id(pkgId);
//				pp.setProd_id(pkg.getProd_id());
//				pp.setType(SystemConstants.PACKAGE_FINANCE_TYPE);
				pkgProdList.add(pp);
			}
		}
		
		return  changeProd(pkgProdList);
	}
	
	public List<PPackageProd> changeProd(List<PPackageProd> list) throws Exception {
		for (PPackageProd pkg : list) {
			if(StringHelper.isNotEmpty(pkg.getProd_list())){
				List<PProd> pList = pProdDao.findByProdIds(pkg.getProd_list().split(","));
				String[] prodName = CollectionHelper.converValueToArray(pList, "prod_name");
				pkg.setProd_list_text(StringHelper.join(prodName, ","));
			}
		}
		return list;
	}

	
	/**
	 * 查询套餐分成值操作记录
	 * @param packageId
	 * @return
	 * @throws JDBCException
	 */
	public List<PPackageProdHis> queryByPackId(String packageId) throws JDBCException{
		return pPackageProdHisDao.queryByPackId(packageId);
	}
	
	/**
	 * 根据资费id获取产品对应的有效的折扣
	 * 
	 * @param prodId
	 * @return
	 * @throws Exception
	 */
	public List<PProdTariffDisct> queryDisct(String tariffId, SOptr optr) throws Exception {
		if (StringHelper.isNotEmpty(tariffId)) {
			 List<PProdTariffDisct> dto = pProdTariffDisctDao.queryDisctByTariffId(tariffId, optr.getCounty_id());
			Map<String, VewRuleDto> rulemap = CollectionHelper.converToMapSingle(
					tRuleDefineDao.findRuleALL(), "rule_id");
			for (PProdTariffDisct pt : dto) {
				if(StringHelper.isNotEmpty(pt.getRule_id())){
					VewRuleDto rule = rulemap.get(pt.getRule_id());
					if( rule!= null ){
						pt.setRule_name(rule.getRule_name());
					}
				}
			}
			return dto;
		}
		return null;
	}

	/**
	 * 根据服务类型查询对应的资源，资源组，以及子产品
	 */
	public ProdResServIdDto queryResByServId(String ServId, String prodId,String query) throws Exception {
		String countyId = WebOptr.getOptr().getCounty_id();
		String areaId = WebOptr.getOptr().getArea_id();
		if (StringHelper.isNotEmpty(ServId)) {
			ProdResServIdDto dto = new ProdResServIdDto();
			if(query.equals(SystemConstants.PROD_TYPE_BASE)){
				dto.setDynamicResList(getGroupRes(ServId, prodId,countyId));
				dto.setStaticResList(getStaticByServId(ServId,countyId));
			}else{
				String dataRight = this.queryDataRightCon(WebOptr.getOptr(), DataRight.CHANGE_COUNTY.toString());
				List<ProdDto> prodList = pProdDao.queryProdByServIdArea(ServId, dataRight,query);
				List<PProdCounty> prodCountyList =pProdDao.queryProdCountyByServIdArea(ServId, dataRight,query);
				for(ProdDto prodDto : prodList){
					List<String> countyList = new ArrayList<String>();
					for(PProdCounty prodCounty : prodCountyList){
						if(prodCounty.getProd_id().equals(prodDto.getProd_id())){
							countyList.add(prodCounty.getCounty_id());
						}
					}
					prodDto.setCountyList(countyList);
				}
				dto.setProdList(prodList);
			}
			return dto;
		}
		return null;
	}
	
	
	public List<ResDto> getStaticByServId(String ServId,String countyId) throws Exception{
		List<TServerRes> ResList =  tServerResDao.getServerRes(countyId);
		Map<String, List<TServerRes>> serverResmap = CollectionHelper.converToMap(ResList, "boss_res_id");
		List<ResDto> bossResList = pResDao.queryStaticByServId(ServId,countyId);
			for(ResDto dto:bossResList){
				List<TServerRes> serverList =  serverResmap.get(dto.getRes_id());
				String str ="[";
				for(TServerRes serRes: serverList){
					str+=serRes.getExternal_res_id()+",";
				}
				str = StringHelper.delEndChar(str,1);
				str +="]"; 
				dto.setServerIds(str);
			}
		return bossResList;
	}
	

	/**
	 * @Description:查询资源组,产品编号不为空的时候对应的资源数,为空的时候资源数为0
	 * @param prodId
	 * @return
	 * @throws Exception
	 * @return List<ResGroupDto>
	 */
	public List<ResGroupDto> getGroupRes(String ServId, String prodId,String countyId)
			throws Exception {
		List<ResGroupDto> dto = pResgroupDao.queryGroupResByServId(ServId,countyId);
		if (StringHelper.isNotEmpty(prodId)) {
			List<ResGroupDto> dyndto = pProdDynResDao.queryDynByProdId(prodId);
			if (dyndto.size() > 0) {
				for (int i = 0; i < dto.size(); i++) {
					for (int k = 0; k < dyndto.size(); k++) {
						if (dto.get(i).getGroup_id().equals(dyndto.get(k).getGroup_id())) {
							dto.get(i).setRes_number(dyndto.get(k).getRes_number());
						}
					}
				}
			}
		}
		return dto;
	}

	/**
	 * 修改产品
	 * @param prodCountyIds 
	 * 
	 * @param resource
	 */
	public PProd updateProd(ProdDto prodDto, String[] dictProdIds,
			String dynList, String statList, String[] prodCountyIds) throws Exception {
		if(validName(prodDto.getProd_name(),prodCountyIds,prodDto.getProd_id())){
			throw new ComponentException("产品名称在该应用地区已经存在!");
		};
		List<SSysChange> changes = new ArrayList<SSysChange>();
		Integer doneCode = getDoneCOde();
		Date createTime = new Date();
		
		PProd prod = new PProd();
		BeanUtils.copyProperties(prodDto, prod);
		
		PProd theOldProd = pProdDao.findByKey(prodDto.getProd_id());
		
	    pProdDao.update(prod);
	    prod = pProdDao.findByKey(prodDto.getProd_id());
	    
	    String content = BeanHelper.beanchange(theOldProd, prod);
	    if(StringHelper.isNotEmpty(content)){
	    	SSysChange prodChange = new SSysChange(SysChangeType.PROD.toString(), doneCode,
	    			prod.getProd_id(), prod.getProd_name(), "产品定义修改", content, WebOptr.getOptr().getOptr_id(), createTime);
	    	changes.add(prodChange);
	    }
	    
	    List<TreeDto> oldProdCountList = pProdCountyDao.getProdCountyById(prod.getProd_id());
	    
		if (!"null".equals(dictProdIds) && dictProdIds != null) {
			pDictProdDao.deleteDictProd(prodDto.getProd_id());
			pDictProdDao.addDictProd(dictProdIds, prodDto.getProd_id());
		}
		
		//保存产品适用地区
		saveProdCountyId(prod.getProd_id(), prodCountyIds);
		
		List<TreeDto> newProdCountList = pProdCountyDao.getProdCountyById(prod.getProd_id());
		
		String countyChangeInfo = BeanHelper.listchange(oldProdCountList, newProdCountList, "id", null);
		if(StringHelper.isNotEmpty(countyChangeInfo)){
			SSysChange countyChange = new SSysChange(SysChangeType.PROD.toString(), doneCode,
	    			prod.getProd_id(), prod.getProd_name(), "产品适用地区", countyChangeInfo.replace("id", "county_id"), WebOptr.getOptr().getOptr_id(), createTime);
	    	changes.add(countyChange);
		}
		
		List<ResGroupDto> dynResList = new ArrayList<ResGroupDto>();
		List<ProdCountyResDto> prodCountyResDtoList = new ArrayList<ProdCountyResDto>();
		List<String> aList = new ArrayList<String>();
		if (StringHelper.isNotEmpty(dynList)) {
			Type type = new TypeToken<List<ResGroupDto>>() {}.getType();
			List<ResGroupDto> group = JsonHelper.gson.fromJson(dynList, type);
			for (ResGroupDto dto : group) {
				ResGroupDto groupdto = new ResGroupDto();
				groupdto.setGroup_id(dto.getGroup_id());
				groupdto.setRes_number(dto.getRes_number());
				dynResList.add(groupdto);
			}
		}
		
		if (StringHelper.isNotEmpty(statList)) {
			Type type = new TypeToken<List<ProdCountyResDto>>() {}.getType();
			List<ProdCountyResDto> res = JsonHelper.gson.fromJson(statList,type);
			prodCountyResDtoList = res;
			// 静态资源，原先存在的，修改后取消，进行删除操作，，原先不存在的进行保存
			if (prodCountyResDtoList.size() > 0) {
				if(SystemConstants.USER_TYPE_OTT_MOBILE.equals(theOldProd.getServ_id())){
					Map<String, List<ProdCountyResDto>> map = CollectionHelper.converToMap(prodCountyResDtoList, "county_id");
					Iterator it = map.keySet().iterator();
					while(it.hasNext()){
						String key = (String) it.next();
						List<ProdCountyResDto> value = map.get(key);
						if(value.size()>1){
							throw new ComponentException(ErrorCode.OttMobileNotHaveMoreRes);
						}
					}
				}
				PProdResChange change = new PProdResChange();
				PProdStaticRes sRes = null;
				PProdCountyRes cRes = null;
				List<PProdStaticRes> sResList = new ArrayList<PProdStaticRes>();
				List<PProdCountyRes> cResList = new ArrayList<PProdCountyRes>();
				List<PProdResChange> changeList = new ArrayList<PProdResChange>();
				List<Object[]> countyList = new ArrayList<Object[]>();
				List<String> staticList = new ArrayList<String>();
				List<ProdCountyResDto> oldCountyResList = pProdCountyResDao.getProdCountyResByProdId(prodDto.getProd_id());
				//移出传入与原来相同的资源
				for (int i = prodCountyResDtoList.size() - 1; i >= 0; i--) {
					if (!"null".equals(oldCountyResList)&& oldCountyResList != null) {
						boolean ck = false;
						for (int k = oldCountyResList.size() - 1; k >= 0; k--) {
							if (prodCountyResDtoList.get(i).getRes_id().equals(oldCountyResList.get(k).getRes_id())
									&& prodCountyResDtoList.get(i).getCounty_id().equals(oldCountyResList.get(k).getCounty_id())) {
								oldCountyResList.remove(k);
								ck = true;
							}
						}
						if (ck) {
							prodCountyResDtoList.remove(i);
						}
					}
				}
				//删除不需要的老资源，并记录
				for (int j = 0; j < oldCountyResList.size(); j++) {
					if (oldCountyResList.get(j).getCounty_id().equals(SystemConstants.COUNTY_ALL)) {
						staticList.add(oldCountyResList.get(j).getRes_id());
					} else {
						aList.add(oldCountyResList.get(j).getProd_id());
						aList.add(oldCountyResList.get(j).getCounty_id());
						aList.add(oldCountyResList.get(j).getRes_id());
						countyList.add(aList.toArray(new String[aList.size()]));
						aList.clear();
					}
					change = new PProdResChange(prod.getProd_id(),oldCountyResList.get(j).getCounty_id(),oldCountyResList.get(j).getRes_id(), WebOptr.getOptr().getOptr_id(),SystemConstants.RECORD_CHAGNE_TYPE_DELETE);
					changeList.add(change);
				}
				//添加新资源配置，并记录
				for (int k = 0; k < prodCountyResDtoList.size(); k++) {
					if (StringHelper.isNotEmpty(prodCountyResDtoList.get(k).getRes_id())) {
						if (prodCountyResDtoList.get(k).getCounty_id().equals(SystemConstants.COUNTY_ALL)) {
							sRes = new PProdStaticRes(prodDto.getProd_id(),prodCountyResDtoList.get(k).getRes_id());
							sResList.add(sRes);
						} else {
							cRes = new PProdCountyRes(prodDto.getProd_id(),prodCountyResDtoList.get(k).getCounty_id(),prodCountyResDtoList.get(k).getRes_id());
							cResList.add(cRes);
						}
						change = new PProdResChange(prod.getProd_id(),prodCountyResDtoList.get(k).getCounty_id(),prodCountyResDtoList.get(k).getRes_id(), WebOptr.getOptr().getOptr_id(),SystemConstants.RECORD_CHAGNE_TYPE_ADD);
						changeList.add(change);
					}
				}
				//删除地市资源
				if (countyList.size() > 0) {
					pProdCountyResDao.deleteByAll(countyList);
				}
				// 删除通用资源
				if (!"null".equals(staticList) && staticList != null) {
					pProdStaticResDao.deleteStatic(prodDto.getProd_id(),staticList);
				}
				// 保存县市资源
				pProdCountyResDao.save(cResList.toArray(new PProdCountyRes[cResList.size()]));
				// 保存通用资源
				pProdStaticResDao.save(sResList.toArray(new PProdStaticRes[sResList.size()]));
				// 异动
				pProdResChangeDao.save(changeList.toArray(new PProdResChange[changeList.size()]));

			}
		}
		List<Object[]> resDynList = new ArrayList<Object[]>();
		PProdDynRes dynRes = null;
		List<PProdDynRes> prodDynList = new ArrayList<PProdDynRes>();
		
		if (dynResList.size() > 0) {
			List<ResGroupDto> oldDynResList = pProdDynResDao.queryDynByProdId(prodDto.getProd_id());
			// 动态资源已经存在，更新新数据
			for (int i = dynResList.size() - 1; i >= 0; i--) {
				boolean ck = false;
				for (int k = oldDynResList.size() - 1; k >= 0; k--) {
					if (dynResList.get(i).getGroup_id().equals(oldDynResList.get(k).getGroup_id())&& dynResList.get(i).getRes_number() == oldDynResList.get(k).getRes_number()) {
						oldDynResList.remove(k);
						ck = true;
					}
				}
				if (ck) {
					dynResList.remove(i);
				}
				
			}
			// 动态资源原先未存在，新增新数据
			for (int i = 0; i < dynResList.size(); i++) {
				if (dynResList.get(i).getRes_number() != 0) {
					dynRes = new PProdDynRes(prodDto.getProd_id(),dynResList.get(i).getGroup_id(),dynResList.get(i).getRes_number());
					prodDynList.add(dynRes);
				}
			}
			for (int i = 0; i < oldDynResList.size(); i++) {
					aList.add(oldDynResList.get(i).getGroup_id());
					aList.add(prodDto.getProd_id());
					aList.add(String.valueOf(oldDynResList.get(i).getRes_number()));
					resDynList.add(aList.toArray(new String[aList.size()]));
					aList.clear();
			}
			
			pProdDynResDao.save(prodDynList.toArray(new PProdDynRes[prodDynList.size()]));
			pProdDynResDao.deleteByAll(resDynList);
		}
		
		saveDataSyncJob(BusiCmdConstants.CHANGE_PROD, prod.getProd_id(), "P_Prod");
		
		//保存操作记录
		saveOperateLog(FuncCode.MODIFY_PROD.toString(), prod.getProd_id(),prod.getProd_name() ,WebOptr.getOptr());
		
		sSysChangeDao.save(changes.toArray(new SSysChange[changes.size()]));
		return prod;
	}
	/**
	 * 保存帐目和产品 
	 */
	public void saveAcctitemByProdId(String prodId) throws Exception{
		String[] acctIds = {prodId,SystemConstants.ACCTITEM_PUBLIC_ID};
		TAcctitemToProd acctitem = null;
		List<TAcctitemToProd> acctitemList = new ArrayList<TAcctitemToProd>();
		for(String acctId:acctIds){
			acctitem = new TAcctitemToProd();
			acctitem.setAcctitem_id(acctId);
			acctitem.setProd_id(prodId);
			acctitemList.add(acctitem);
		}
		tAcctitemToProdDao.save(acctitemList.toArray(new TAcctitemToProd[acctitemList.size()]));
	}
	
	
	/**
	 * @Description: 保存产品
	 * @param prodDto
	 * @param prodCountyIds 
	 * @return
	 * @throws Exception
	 * @return boolean
	 */
	public PProd saveProd(ProdDto prodDto, String[] dictProdIds,
			String dynList, String statList, String[] prodCountyIds) throws Exception {
		if(validName(prodDto.getProd_name(),prodCountyIds,prodDto.getProd_id())){
			throw new ComponentException("产品名称在该应用地区已经存在!");
		};
		
		List<SSysChange> changes = new ArrayList<SSysChange>();
		Integer doneCode = getDoneCOde();
		String changeType = SysChangeType.PROD.toString();
		Date createTime = new Date();
		
		PProd prod = new PProd();
		String prodId = pProdDao.findSequence().toString();
		prodDto.setProd_id(prodId);
		BeanUtils.copyProperties(prodDto, prod);
		prod.setFor_area_id(prod.getArea_id());
//		prod.setRefund(SystemConstants.BOOLEAN_TRUE);
//		prod.setTrans(SystemConstants.BOOLEAN_TRUE);
		pProdDao.save(prod);
		
		
		prod = pProdDao.findByKey(prodId);
		String content = BeanHelper.beanchange(null, prod);
		SSysChange prodChange = new SSysChange(changeType, doneCode,
				prodId, prod.getProd_name(), "产品定义", content, WebOptr.getOptr().getOptr_id(), createTime);
		changes.add(prodChange);
		
		// 属于哪个产品目录
		if (!"null".equals(dictProdIds) && dictProdIds != null) {
			pDictProdDao.addDictProd(dictProdIds, prodDto.getProd_id());
		}
		saveAcctitemByProdId(prodDto.getProd_id());
		
		//保存产品适用地区
		saveProdCountyId(prod.getProd_id(), prodCountyIds);
		if(prodCountyIds !=null && prodCountyIds.length>0){//记录适用地区变更的异动
			List<String> cname = new ArrayList<String>();
			for(String cid:prodCountyIds){
				cname.add(MemoryDict.getDictName(DictKey.COUNTY, cid));
			}
			SSysChange prodCountyChange = new SSysChange(SysChangeType.PROD.toString(), doneCode,
					prodId, prod.getProd_name(), "适用地区变更", "county_id"+ BeanHelper.listchange(null, cname, null, null), WebOptr.getOptr().getOptr_id(), createTime);
			changes.add(prodCountyChange);
		}
		
		List<PProdStaticRes> statResList = new ArrayList<PProdStaticRes>();
		List<PProdCountyRes> prodCountyResList = new ArrayList<PProdCountyRes>();
		PProdDynRes dynRes = null;
		List<PProdDynRes> prodDynList = new ArrayList<PProdDynRes>();
		if (StringHelper.isNotEmpty(dynList)) {
			Type type = new TypeToken<List<PProdDynRes>>() {}.getType();
			List<PProdDynRes> group = JsonHelper.gson.fromJson(dynList, type);
			for (PProdDynRes dto : group) {
				if (dto.getRes_number()!= 0) {
					dynRes = new PProdDynRes(prodDto.getProd_id(),dto.getGroup_id(),dto.getRes_number());
					prodDynList.add(dynRes);
				}
			}
		}

		if (StringHelper.isNotEmpty(statList)) {
			Type type = new TypeToken<List<PProdCountyRes>>() {
			}.getType();
			List<PProdCountyRes> res = JsonHelper.gson.fromJson(statList, type);
			if(res.size()>0){
				if(SystemConstants.USER_TYPE_OTT_MOBILE.equals(prod.getServ_id())){
					Map<String, List<PProdCountyRes>> map = CollectionHelper.converToMap(res, "county_id");
					Iterator it = map.keySet().iterator();
					while(it.hasNext()){
						String key = (String) it.next();
						List<PProdCountyRes> value = map.get(key);
						if(value.size()>1){
							throw new ComponentException(ErrorCode.OttMobileNotHaveMoreRes);
						}
					}
				}
			}
			
			for (PProdCountyRes dto : res) {
				PProdCountyRes resdto = new PProdCountyRes();
				PProdStaticRes staticres = new PProdStaticRes();
				if (dto.getCounty_id().equals(SystemConstants.COUNTY_ALL)) {
					staticres = new PProdStaticRes(prodDto.getProd_id(),dto.getRes_id());
					statResList.add(staticres);
				} else {
					resdto = new PProdCountyRes(prodDto.getProd_id(),dto.getCounty_id(),dto.getRes_id());
					prodCountyResList.add(resdto);
				}
			}

		}
		if(prodDynList.size()>0){
			pProdDynResDao.save(prodDynList.toArray(new PProdDynRes[prodDynList.size()]));
		}
		// 保存通用资源
		pProdStaticResDao.save(statResList.toArray(new PProdStaticRes[statResList.size()]));
		// 保存县市资源
		pProdCountyResDao.save(prodCountyResList.toArray(new PProdCountyRes[prodCountyResList.size()]));
		
		saveDataSyncJob(BusiCmdConstants.CREAT_PROD, prod.getProd_id(), "P_Prod");
		
		//保存操作记录
		saveOperateLog(FuncCode.CREATE_PROD.toString(), prod.getProd_id(),prod.getProd_name(), WebOptr.getOptr());
		sSysChangeDao.save(changes.toArray(new SSysChange[changes.size()]));
		return prod;
	}

	private void saveProdCountyId(String prodId, String[] prodCountyIds) throws Exception {
		pProdCountyDao.deletebyProdId(prodId);
		if(null !=prodCountyIds && prodCountyIds.length > 0){
			pProdCountyDao.saveProdCountyId(prodId,prodCountyIds);
		}
	}

	/**
	 * @Description: 保存套餐产品
	 * @param prodDto
	 * @param prodCountyIds 
	 * @return
	 * @throws Exception
	 * @return boolean
	 */
	public PProd saveProdPack(ProdDto prodDto, String[] dictProdIds, String[] prodCountyIds) throws Exception {
		if(validName(prodDto.getProd_name(),prodCountyIds,prodDto.getProd_id())){
			throw new ComponentException("产品名称在该应用地区已经存在!");
		};
		PProd prod = new PProd();
		
		String key = pProdDao.findSequence().toString();
		Date createTime = new Date();
		String changeType= SysChangeType.PROD.toString();
		List<SSysChange> changes = new ArrayList<SSysChange>();
		String optrId = WebOptr.getOptr().getOptr_id();
		Integer doneCode = getDoneCOde();
		
		prodDto.setProd_id(key);
		BeanUtils.copyProperties(prodDto, prod);
		prod.setFor_area_id(prod.getArea_id());
//		prod.setRefund(SystemConstants.BOOLEAN_TRUE);
//		prod.setTrans(SystemConstants.BOOLEAN_TRUE);
		pProdDao.save(prod);
		
		PProd newProd = pProdDao.findByKey(key);
		
		SSysChange prodChange = new SSysChange(changeType, doneCode, key, newProd.getProd_name(), 
				"产品定义", BeanHelper.beanchange(null, newProd), optrId, createTime);
		changes.add(prodChange);
		saveAcctitemByProdId(prodDto.getProd_id());
		// 属于哪个产品目录
		if (!"null".equals(dictProdIds) && dictProdIds != null) {
			pDictProdDao.addDictProd(dictProdIds, prodDto.getProd_id());
		}
		
		//保存产品适用地区
		saveProdCountyId(prod.getProd_id(), prodCountyIds);
		List<String> newProdCounty = pProdCountyDao.queryByProdId(key);
		if(newProdCounty!=null && newProdCounty.size()>0){
			SSysChange countyChange = new SSysChange(changeType, doneCode, key, newProd.getProd_name(),
					"产品使用地区变更", "county_id:"+BeanHelper.listchange(null, newProdCounty, null, null), optrId, createTime);
			changes.add(countyChange);
		}
		
		if (prodDto.getPackList().size() > 0) {
			List<PPackageProd> pkList = new ArrayList<PPackageProd>();
			for(PPackageProd pk :prodDto.getPackList()){
				checkProd(pk);
				pk.setPackage_id(prodDto.getProd_id());
				pk.setPackage_group_id(pPackageProdDao.findSequence().toString());
				pkList.add(pk);
			}
			pPackageProdDao.save(pkList.toArray(new PPackageProd[pkList.size()]));
			
			String[] displayFields = new String[] {"package_group_name","user_type","prod_list","terminal_type","max_user_cnt","precent"};
			String packChangeInfo = BeanHelper.listchange(null, pkList,displayFields );
			if(StringHelper.isNotEmpty(packChangeInfo)){
				String preAppend = "套餐内容组名称_用户类型_产品内容组_终端类型_用户数_权重";
				packChangeInfo = preAppend + ":\n" + packChangeInfo;
				SSysChange countyChange = new SSysChange(SysChangeType.PROD.toString(), doneCode,
		    			prod.getProd_id(), prod.getProd_name(), "套餐内容组", packChangeInfo, WebOptr.getOptr().getOptr_id(), createTime);
		    	changes.add(countyChange);
			}
		}
		
		//保存操作记录
		saveOperateLog(FuncCode.CREATE_PROD.toString(), prod.getProd_id(),prod.getProd_name(), WebOptr.getOptr());
		sSysChangeDao.save(changes.toArray(new SSysChange[changes.size()]));
		return prod;
	}

	private void checkProd(PPackageProd pk) throws Exception{
		if(pk.getMax_user_cnt()<1){
			throw new ComponentException(ErrorCode.ProdPackMaxUserCntIsError);
		}else if(StringHelper.isEmpty(pk.getPackage_group_name())){
			throw new ComponentException(ErrorCode.ProdPackGroupNameIsError);
		}else if(StringHelper.isEmpty(pk.getProd_list())){
			throw new ComponentException(ErrorCode.ProdPackProdListIsError);
		}
		//产品组需要按prodid 顺序排列
		String[] prodIds = pk.getProd_list().split(",");
		List<PProd> list = pProdDao.queryPackProdList(prodIds);
		if(prodIds.length != list.size()){
			throw new ComponentException(ErrorCode.ProdNotExists);
		}
		String[] prodIdList = CollectionHelper.converValueToArray(list, "prod_id");
		if(prodIdList.length>0){
			String prodList = StringHelper.join(prodIdList, ",");
			pk.setProd_list(prodList);
		}
	}

	/** 
	 *  修改产品
	* @param prodDto 
	* @param dictProdIds
	 * @param prodCountyIds 
	* @return
	* @throws Exception    
	* @return boolean
	*/
	public PProd updateProdPack(ProdDto prodDto, String[] dictProdIds, String[] prodCountyIds) throws Exception {
		if(validName(prodDto.getProd_name(),prodCountyIds,prodDto.getProd_id())){
			throw new ComponentException("产品名称在该应用地区已经存在!");
		};
		PProd oldProd = pProdDao.findByKey(prodDto.getProd_id());
		
		PProd prod = new PProd();
		BeanUtils.copyProperties(prodDto, prod);
		pProdDao.update(prod);
		
		PProd newProd = pProdDao.findByKey(prodDto.getProd_id());
		String prodChangeInfo = BeanHelper.beanchange(oldProd, newProd);
		String changeType = SysChangeType.PROD.toString();
		Date createTime = new Date();
		int doneCode = getDoneCOde();
		List<SSysChange> changes = new ArrayList<SSysChange>();
		
		if(StringHelper.isNotEmpty(prodChangeInfo)){
			SSysChange prodChange = new SSysChange(changeType, doneCode, prod.getProd_id(),
					prod.getProd_name(), "产品定义修改", prodChangeInfo, WebOptr.getOptr().getOptr_id(), createTime);
			changes.add(prodChange);
		}
		
	    List<TreeDto> oldProdCountList = pProdCountyDao.getProdCountyById(prod.getProd_id());
	    
		if (!"null".equals(dictProdIds) && dictProdIds != null) {
			pDictProdDao.deleteDictProd(prodDto.getProd_id());
			pDictProdDao.addDictProd(dictProdIds, prodDto.getProd_id());
		}
		
		//保存产品适用地区
		saveProdCountyId(prod.getProd_id(), prodCountyIds);
		
		List<TreeDto> newProdCountList = pProdCountyDao.getProdCountyById(prod.getProd_id());
		
		String countyChangeInfo = BeanHelper.listchange(oldProdCountList, newProdCountList, "id", null);
		if(StringHelper.isNotEmpty(countyChangeInfo)){
			SSysChange countyChange = new SSysChange(SysChangeType.PROD.toString(), doneCode,
	    			prod.getProd_id(), prod.getProd_name(), "产品适用地区", countyChangeInfo.replace("id", "county_id"), WebOptr.getOptr().getOptr_id(), createTime);
	    	changes.add(countyChange);
		}
		
		List<PPackageProd> pkList = new ArrayList<PPackageProd>();
		if (prodDto.getPackList().size() > 0) {
			List<PPackageProd> oldPackList =  pPackageProdDao.queryPackProdByProdId(prod.getProd_id());
			for(PPackageProd pk :prodDto.getPackList()){
				checkProd(pk);
				pk.setPackage_id(prodDto.getProd_id());
				if(StringHelper.isEmpty(pk.getPackage_group_id())){
					pk.setPackage_group_id(pPackageProdDao.findSequence().toString());
				}
				pkList.add(pk);
			}
			pPackageProdDao.deletePackById(prod.getProd_id());
			// 保存新传入的数据
			pPackageProdDao.save(pkList.toArray(new PPackageProd[pkList.size()]));
			String[] displayFields = new String[] {"package_group_name","user_type","prod_list","terminal_type","max_user_cnt","precent"};
			String packChangeInfo = BeanHelper.listchange(oldPackList, pkList,displayFields );
			if(StringHelper.isNotEmpty(packChangeInfo)){
				String preAppend = "套餐内容组名称_用户类型_产品内容组_终端类型_用户数_权重";
				packChangeInfo = preAppend + ":\n" + packChangeInfo;
				SSysChange packChange = new SSysChange(SysChangeType.PROD.toString(), doneCode,
		    			prod.getProd_id(), prod.getProd_name(), "套餐内容组", packChangeInfo, WebOptr.getOptr().getOptr_id(), createTime);
		    	changes.add(packChange);
			}
			
		}

//		if(pkList.size()>0){
//			List<ProdTariffDto> tarifflist = pProdTariffDao.queryTariffByProd(prod.getProd_id());
//			for (ProdTariffDto dto : tarifflist) {
//				deleteTariffById(dto.getTariff_id());
//			}
//		}
		//保存操作记录
		saveOperateLog(FuncCode.MODIFY_PROD.toString(), prod.getProd_id(), prod.getProd_name(), WebOptr.getOptr());
		sSysChangeDao.save(changes.toArray(new SSysChange[changes.size()]));
		return prod;
	}
	
	/**
	 * 修改已生效的产品
	 * @param prodDto
	 * @param prodCountyIds 
	 * @return
	 * @throws Exception
	 */
	public PProd editProd(ProdDto prodDto,String[] dictProdIds, String[] prodCountyIds) throws Exception{
		if(validName(prodDto.getProd_name(),prodCountyIds,prodDto.getProd_id())){
			throw new ComponentException("产品名称在该应用地区已经存在!");
		};
		PProd prod = new PProd();
		PProd oldProd = pProdDao.findByKey(prodDto.getProd_id());
		
		BeanUtils.copyProperties(prodDto, prod);
		pProdDao.update(prod);
		PProd newProd = pProdDao.findByKey(prodDto.getProd_id());
		
		Date createTime = new Date();
		String prodChangeInfo = BeanHelper.beanchange(oldProd, newProd);
		String changeType = SysChangeType.PROD.toString();
		int doneCode = getDoneCOde();
		List<SSysChange> changes = new ArrayList<SSysChange>();
		
		if(StringHelper.isNotEmpty(prodChangeInfo)){
			SSysChange prodChange = new SSysChange(changeType, doneCode, prod.getProd_id(),
					prod.getProd_name(), "产品定义修改", prodChangeInfo, WebOptr.getOptr().getOptr_id(), createTime);
			changes.add(prodChange);
		}
		
		// 属于哪个产品目录
		if (!"null".equals(dictProdIds) && dictProdIds != null) {
			pDictProdDao.deleteDictProd(prodDto.getProd_id());
			pDictProdDao.addDictProd(dictProdIds, prodDto.getProd_id());
		}
		
		List<String> oldProdCounty = pProdCountyDao.queryByProdId(prod.getProd_id());
		List<String> oldCountyName = new ArrayList<String>();
		for(String id:oldProdCounty){
			oldCountyName.add(MemoryDict.getDictName(DictKey.COUNTY, id));
		}
		List<String> newCountyName = new ArrayList<String>();
		
		//保存产品适用地区
		saveProdCountyId(prod.getProd_id(), prodCountyIds);
		List<String> newProdCounty = pProdCountyDao.queryByProdId(prod.getProd_id());
		for(String id:newProdCounty){
			newCountyName.add(MemoryDict.getDictName(DictKey.COUNTY, id));
		}
		String countyChangeInfo = BeanHelper.listchange(oldCountyName, newCountyName, null, null);
		if(StringHelper.isNotEmpty(countyChangeInfo)){
			SSysChange countyChange = new SSysChange(changeType, doneCode, prod.getProd_id(), 
					prod.getProd_name(), "产品适用地区变更","county_id:"+countyChangeInfo, WebOptr.getOptr().getOptr_id(), createTime);
			changes.add(countyChange);
		}
		
		saveDataSyncJob(BusiCmdConstants.CHANGE_PROD, prod.getProd_id(), "P_Prod");
		
		//保存操作记录
		saveOperateLog(FuncCode.MODIFY_PROD.toString(), prod.getProd_id(),prod.getProd_name(), WebOptr.getOptr());
		if(changes.size()>0){
			sSysChangeDao.save(changes.toArray(new SSysChange[changes.size()]));
		}
		
		return prod;
	}

	/**
	 * @Description: 保存资费
	 * @param tariffdto
	 * @param optr
	 * @return
	 * @throws Exception
	 * @return boolean
	 */
	public void saveTariff(ProdTariffDto tariff,String packsList,String[] tariffCountyIds ,SOptr optr)
			throws Exception {
		
		Integer doneCode = getDoneCOde();
		Date createTime = new Date();
		List<SSysChange> changes = new ArrayList<SSysChange>();
		
		PProdTariff oldTariff = null;
		
		List<TreeDto> oldTariffCountyBytariffId = pProdTariffCountyDao.getTariffCountyBytariffId(tariff.getTariff_id());
		PProd prod = pProdDao.findByKey(tariff.getProd_id());
		if(prod.getProd_type().equals(SystemConstants.PROD_TYPE_SPKG) && StringHelper.isNotEmpty(tariff.getSpkg_sn())){
			if(pSpkgDao.querySpkgBySn(tariff.getSpkg_sn()) == null){
				throw new ComponentException("该协议用户配置数据不存在");
			}
		}else{
			tariff.setSpkg_sn("");
		}
		
//		checkPrice(tariff,prod.getProd_type(),tariffCountyIds,optr);
		
		if (StringHelper.isNotEmpty(tariff.getTariff_id())) {
			oldTariff = pProdTariffDao.findByKey(tariff.getTariff_id());
			pProdTariffDao.update(tariff);
			if (!"null".equals(tariffCountyIds) && tariffCountyIds != null) {
				pProdTariffCountyDao.deleteTariffCounty(tariff.getTariff_id());
				pProdTariffCountyDao.addTariffCounty(tariffCountyIds,tariff.getTariff_id());
			}else{
				pProdTariffCountyDao.deleteTariffCounty(tariff.getTariff_id());
			}
			
			//产品数据同步
			saveDataSyncJob(BusiCmdConstants.CHANGE_PROD, tariff.getProd_id(), "P_Prod");
			
			//保存操作记录
			saveOperateLog(FuncCode.MODIFY_TARIFF.toString(),tariff.getTariff_id(), tariff.getTariff_name(), WebOptr.getOptr());
		} else {
			tariff.setArea_id(WebOptr.getOptr().getArea_id());
			tariff.setCounty_id(WebOptr.getOptr().getCounty_id());
			tariff.setOptr_id(WebOptr.getOptr().getOptr_id());
			tariff.setCreate_time(new Date());
			tariff.setTariff_id(pProdTariffDao.findSequence().toString());
			pProdTariffDao.save(tariff);
			if (!"null".equals(tariffCountyIds) && tariffCountyIds != null) {
				pProdTariffCountyDao.addTariffCounty(tariffCountyIds,tariff.getTariff_id());
			}
			//产品数据同步
			if(isSuccess(BusiCmdConstants.CREAT_PROD, tariff.getProd_id(), "P_Prod")){
				saveDataSyncJob(BusiCmdConstants.CHANGE_PROD, tariff.getProd_id(), "P_Prod");
			}else{
				saveDataSyncJob(BusiCmdConstants.CREAT_PROD, tariff.getProd_id(), "P_Prod");
			}
			//保存操作记录
			saveOperateLog(FuncCode.CREATE_TARIFF.toString(),tariff.getTariff_id(), tariff.getTariff_name(), WebOptr.getOptr());
		}
		
		PProdTariff newTariff = pProdTariffDao.findByKey(tariff.getTariff_id());
		
		//处理字段为英文的
		String[] fields = new String[] { /*"tariff_id ", */"tariff_name",
				"tariff_desc", "prod_id", "tariff_type_text", "billing_type_text",
				"rent" ,"month_rent_cal_type_text", "day_rent_cal_type_text",
				"use_fee_rule", "bill_rule", "status_text", "area_name", "county_name",
				"optr_name", "create_time", "rule_id","rule_id_text", "tariff_type_text","eff_date","exp_date","service_channel"};
		
		List<VewRuleDto> rules = tRuleDefineDao.findRuleViewDictByType("BUSI",WebOptr.getOptr().getCounty_id());
		
		if(oldTariff !=null){
			for(VewRuleDto rule:rules){
				if(StringHelper.isNotEmpty(oldTariff.getRule_id())
						&& oldTariff.getRule_id().equals(rule.getRule_id())){
					oldTariff.setRule_id_text(rule.getRule_name());
				}
			}
		}
		
		if(newTariff !=null){
			for(VewRuleDto rule:rules){
				if(StringHelper.isNotEmpty(newTariff.getRule_id())
						&& newTariff.getRule_id().equals(rule.getRule_id())){
					newTariff.setRule_id_text(rule.getRule_name());
				}
			}
		}
//		MemoryDict.getDictName(DictKey.TARIFF_TYPE, oldTariff.getTariff_type());
		String tariffChangeInfo = BeanHelper.beanchange(oldTariff, newTariff,fields);
		
		if(StringHelper.isNotEmpty(tariffChangeInfo)){
			SSysChange tariffChange = new SSysChange(SysChangeType.PROD.toString(), doneCode, prod.getProd_id(), 
					prod.getProd_name(), "产品资费定义:" + tariff.getTariff_name() + "(" + prod.getProd_name() +")", tariffChangeInfo, WebOptr.getOptr().getOptr_id(), createTime);
			changes.add(tariffChange);
		}
		List<TreeDto> newTariffCountyBytariffId = pProdTariffCountyDao.getTariffCountyBytariffId(tariff.getTariff_id());
		List<String> oldCountyList = new ArrayList<String>();
		for(TreeDto tree:oldTariffCountyBytariffId){
			oldCountyList.add(MemoryDict.getDictName(DictKey.COUNTY, tree.getId()));
		}
		List<String> newCountyList = new ArrayList<String>();
		for(TreeDto tree:newTariffCountyBytariffId){
			newCountyList.add(MemoryDict.getDictName(DictKey.COUNTY, tree.getId()));
		}
		
		String countyChangeInfo = BeanHelper.listchange(oldCountyList, newCountyList);
		if(StringHelper.isNotEmpty(countyChangeInfo)){
			SSysChange tariffChange = new SSysChange(SysChangeType.PROD.toString(), doneCode, prod.getProd_id(), 
					prod.getProd_name(), "产品资费定义:" + tariff.getTariff_name() + "(" + prod.getProd_name() +")适用地区变化", countyChangeInfo.replace("id", "county_id"), WebOptr.getOptr().getOptr_id(), createTime);
			changes.add(tariffChange);
		}
		
		if (StringHelper.isNotEmpty(packsList)) {
			List<PPackageProdHis> PacksList = new ArrayList<PPackageProdHis>();
			Type type = new TypeToken<List<PPackageProd>>() {}.getType();
			List<PPackageProd> pack = JsonHelper.gson.fromJson(packsList, type);
			
			Map<String,List<PPackageProd>> map = CollectionHelper.converToMap(pack, "type");
			
			Integer value = 0;
			for(String t : map.keySet()){
				List<PPackageProd> list = map.get(t);
				for(int i=0,len=list.size();i<len;i++){
					PPackageProd pp = list.get(i);
					PPackageProdHis packdto = new PPackageProdHis();
					packdto.setPackage_id(tariff.getProd_id());
//					packdto.setProd_id(pp.getProd_id());
//					packdto.setTariff_id(pp.getTariff_id());
//					packdto.setMax_prod_count(pp.getMax_prod_count());
//					packdto.setPercent_value(pp.getPercent_value());
//					packdto.setPackage_tariff_id(tariff.getTariff_id());
//					packdto.setType(pp.getType());
//					if(i==list.size()-1){
//						if(value > 0){
//							packdto.setPercent(100-value);
//						}else if(value == 0 && pp.getPercent_value() == 0){
//							packdto.setPercent(0);
//						}
//					}else{
//						packdto.setPercent(calculationPercent(pp.getPercent_value(), (float)(tariff.getRent())/100));
//						value +=packdto.getPercent();
//					}
//					
//					packdto.setOptr_id(WebOptr.getOptr().getOptr_id());
//					PacksList.add(packdto);
				}
				value = 0;
			}
			
//			pPackageProdDao.deletePackById(tariff.getProd_id(),tariff.getTariff_id());
//			pPackageProdDao.save(PacksList.toArray(new PPackageProd[PacksList.size()]));
//			pPackageProdHisDao.save(PacksList.toArray(new PPackageProdHis[PacksList.size()]));
		}
		if(changes.size()>0){
			sSysChangeDao.save(changes.toArray(new SSysChange[changes.size()]));
		}
	}
	
	public  Integer calculationPercent(Float num1,Float num2 ) {   
	    // 创建一个数值格式化对象   
	    NumberFormat numberFormat = NumberFormat.getInstance();   
	    // 设置精确到小数点后0位   
	    numberFormat.setMaximumFractionDigits(0);   
	    Integer result = Integer.parseInt(numberFormat.format((num1/num2)*100));   
	    return  result;   
	}
	
	
	/**
	 * @Description: 保存折扣
	 * @param disctlist
	 * @param optr
	 * @return
	 * @throws Exception
	 * @return boolean
	 */
	public void saveDisct(PProdTariffDisct disct, String[] disctCountyIds, SOptr optr)
			throws Exception {		
		Integer doneCode = getDoneCOde();
		Date createTime = new Date();
		PProdTariff tariff = pProdTariffDao.findByKey(disct.getTariff_id());
		PProd prod = pProdDao.findByKey(tariff.getProd_id());
		List<SSysChange> changes = new ArrayList<SSysChange>();
		
		PProdTariffDisct oldDict = pProdTariffDisctDao.findByKey(disct.getDisct_id());
		
		if (StringHelper.isNotEmpty(disct.getDisct_id())) {
			pProdTariffDisctDao.update(disct);
//			//产品数据同步
//			saveDataSyncJob(BusiCmdConstants.CHANGE_PROD, disct.getDisct_id(), "p_prod_tariff_disct");
			
			//保存操作记录
			saveOperateLog(FuncCode.MODIFY_DISCT.toString(),disct.getDisct_id(), disct.getDisct_name(), optr);
		} else {
			disct.setArea_id(optr.getArea_id());
			disct.setCounty_id(optr.getCounty_id());
			disct.setOptr_id(optr.getOptr_id());
			disct.setCreate_time(new Date());
			disct.setDisct_id(pProdTariffDisctDao.findSequence().toString());
			pProdTariffDisctDao.save(disct);
//			//产品数据同步
//			saveDataSyncJob(BusiCmdConstants.CREAT_PROD, tariff.getProd_id(), "P_Prod");
			
			//保存操作记录
			saveOperateLog(FuncCode.CREATE_DISCT.toString(),disct.getDisct_id(), disct.getDisct_name(), optr);
		}
		
		PProdTariffDisct newDict = pProdTariffDisctDao.findByKey(disct.getDisct_id());

		String disctChangeInfo = BeanHelper.beanchange(oldDict, newDict);
		
		if(StringHelper.isNotEmpty(disctChangeInfo)){
			SSysChange disctChange = new SSysChange(SysChangeType.PROD.toString(),
					doneCode, prod.getProd_id(), prod.getProd_name(), 
					"资费_"+tariff.getTariff_name()+ "(" + prod.getProd_name() + "):折扣_" + newDict.getDisct_name() + "(" + newDict.getDisct_id()+")", 
					disctChangeInfo, optr.getOptr_id(), createTime);
			changes.add(disctChange);
		}
		
		List<TreeDto> oldList = pProdTariffDisctCountyDao.getTariffDisctCountyByDisctId(disct.getDisct_id());
		
		pProdTariffDisctCountyDao.deleteDisctCounty(disct.getDisct_id());
		pProdTariffDisctCountyDao.addDisctCounty(disctCountyIds, disct.getDisct_id());
		
		List<TreeDto> newList = pProdTariffDisctCountyDao.getTariffDisctCountyByDisctId(disct.getDisct_id());
		List<SCounty> oldCounty = new ArrayList<SCounty>();
		for(TreeDto tree:oldList){
			SCounty county = new SCounty();
			county.setCounty_id(tree.getId());
			county.setCounty_name(MemoryDict.getDictName(DictKey.COUNTY, tree.getId()));
			oldCounty.add(county);
		}
		List<SCounty> newCounty = new ArrayList<SCounty>();
		for(TreeDto tree:newList){
			SCounty county = new SCounty();
			county.setCounty_id(tree.getId());
			county.setCounty_name(MemoryDict.getDictName(DictKey.COUNTY, tree.getId()));
			newCounty.add(county);
		}
		String countyChangeInfo = BeanHelper.listchange(oldCounty, newCounty, "county_name", null);
		if(StringHelper.isNotEmpty(countyChangeInfo)){
			countyChangeInfo = countyChangeInfo.replace("county_name", "分公司");
			if(StringHelper.isNotEmpty(countyChangeInfo)){
				SSysChange countyChange = new SSysChange(SysChangeType.PROD.toString(), doneCode, prod.getProd_id(), prod.getProd_name(), 
						"资费_"+tariff.getTariff_name() + "(" + prod.getProd_name() +"):折扣_" + disct.getDisct_name() + "(" + disct.getDisct_id() +")" + "适用地区变更", 
								countyChangeInfo, optr.getOptr_id(), createTime);
				changes.add(countyChange);
			}
		}
		if(changes.size()>0){
			sSysChangeDao.save(changes.toArray(new SSysChange[changes.size()]));
		}
	}

	/**
	 * @Description: 删除折扣
	 * @param disctId
	 * @return
	 * @throws Exception
	 * @return boolean
	 */
	public boolean deleteDisct(String disctId ) throws Exception {
		if (StringHelper.isNotEmpty(disctId)) {
			PProdTariffDisct disct = pProdTariffDisctDao.findByKey(disctId);
			pProdTariffDisctDao.deleteDisctByDisctId(disctId);
			//保存操作记录
			saveOperateLog(FuncCode.DELETE_DISCT.toString(),disct.getDisct_id(), disct.getDisct_name(), WebOptr.getOptr());
			return true;
		}
		return false;
	}

	/**
	 * @Description: 删除资费
	 * @param tariffId
	 * @param optr 
	 * @return
	 * @throws Exception
	 * @return boolean
	 */
	public boolean deleteTariff(String tariffId ) throws Exception {
		if (StringHelper.isNotEmpty(tariffId)) {
//			pProdTariffCountyDao.deleteTariffCounty(tariffId);
			PProdTariff oldTariff = pProdTariffDao.findByKey(tariffId);
			PProd prod = pProdDao.findByKey(oldTariff.getProd_id());
			deleteTariffById(tariffId);
			//产品数据同步
			PProdTariff tariff = pProdTariffDao.findByKey(tariffId);
			saveDataSyncJob(BusiCmdConstants.DELETE_PROD, tariff.getProd_id(), "P_Prod");
			
			//保存操作记录
			saveOperateLog(FuncCode.DELETE_TARIFF.toString(),tariff.getTariff_id(), tariff.getTariff_name(), WebOptr.getOptr());
			
			SSysChange tariffChange = new SSysChange(SysChangeType.PROD.toString(),getDoneCOde(), prod.getProd_id(), 
					prod.getProd_name(), "产品资费删除:" + tariff.getTariff_name() + "(" + prod.getProd_name() +")", 
					BeanHelper.beanchange(oldTariff, tariff), WebOptr.getOptr().getOptr_id(), new Date());
			
			sSysChangeDao.save(tariffChange);
			
			return true;
		}
		return false;
	}

	/**
	 * @Description: 删除产品
	 * @param prodId
	 * @return
	 * @throws Exception
	 * @return boolean
	 */
	public ProdDto deletePord(String prodId) throws Exception {
		List<PPackageProd> pkgProdList = queryPackById(prodId);
		if (pkgProdList.size() > 0) {
			ProdDto prod = pProdDao.queryProdById(pkgProdList.get(0).getPackage_id());
			return prod;
		} else {
			deletePordById(prodId);
			return null;
		}
	}
	
	public List<PPackageProd> queryPackById(String pkgId) throws Exception {
		List<PPackageProd> pkgProdList = pPackageProdDao.queryPkgById(pkgId);
		return changeProd(pkgProdList);
	}

	/**
	 * 验证所选的资源和地市是否可用
	 * @param resArray
	 * @param countyId
	 * @return
	 * @throws Exception
	 */
	public ResDto validRes(String resArray,String countyId) throws Exception{
		String[] resList = resArray.split(",");
		ResDto dto = new ResDto();
		if (!"null".equals(resArray) && resArray != null) {
			String src ="";
			String resName = "";
			Map<String, ResDto> resmap = CollectionHelper.converToMapSingle(
					pResDao.getServerRes(), "res_id");
			for(String d:resList){
				ResDto res = resmap.get(d);
				if(res.getCurrency().equals("T")){
					continue;
				}
				if(countyId.equals(SystemConstants.COUNTY_ALL)){
						src += d+",";
						continue;
				}else{
					dto = tServerResDao.validRes(countyId,d);
				}
				int a = Integer.parseInt( dto.getCnt() ); 
				if(a==0){
					src += d+",";
				};
			}
			if(StringHelper.isNotEmpty(src)){
				String[] resArr = src.split(",");
				List<ResDto>  resdto = pResDao.queryResByResIds(resArr);
				for(ResDto res:resdto){
					resName += res.getRes_name()+",";
				}
				resName = StringHelper.delEndChar(resName, 1);
				dto.setResNames(resName);
				return dto;
				
			}else{
				return null;
			}
			
		}
		return null;
	}
	/**
	 * 根据产品查询最低定价（包含未配置的地市）
	 * @param prodId
	 * @param optr
	 * @return
	 * @throws ComponentException
	 * @throws Exception
	 */
	public List<PProdCountyPrice> queryLowestPrice(String prodId,SOptr optr) throws ComponentException, Exception {
		String dataRight = this.queryDataRightCon(optr, DataRight.CHANGE_COUNTY.toString());
		List<PProdCountyPrice> countys = pProdCountyPriceDao.queryCountyByDataRight(optr.getCounty_id(),dataRight);
		List<PProdCountyPrice> prices = pProdCountyPriceDao.getLowestCounty(prodId,optr.getCounty_id(),dataRight);
		for(PProdCountyPrice county:countys){
			for(PProdCountyPrice price:prices){
				if(county.getCounty_id().equals(price.getCounty_id())){
					BeanUtils.copyProperties(price, county);
				}
			}
		}
		return countys;
	}
	
	/**
	 * 验证资费是否符合适用地区的最低定价
	 * @param tariff
	 * @param prodType
	 * @param countys
	 * @param optr
	 * @throws ComponentException
	 * @throws Exception
	 */
	public void  checkPrice(ProdTariffDto tariff,String prodType,String[] countys,SOptr optr) throws ComponentException, Exception {
		List<PProdCountyPrice> priceList = new ArrayList<PProdCountyPrice>();
		String dataRight = this.queryDataRightCon(optr, DataRight.CHANGE_COUNTY.toString());
		if(prodType.equals(SystemConstants.PROD_TYPE_BASE)){
			priceList = pProdCountyPriceDao.getLowestCountyById(tariff.getProd_id(),optr.getCounty_id(),dataRight);
		}else{
			priceList = pProdCountyPriceDao.getLowestCountyByPkId(tariff.getProd_id(),optr.getCounty_id(),dataRight);
		}
		Map<String ,List<PProdCountyPrice>> priceMap = CollectionHelper.converToMap(priceList,new String[] { "prod_id", "prod_name" });
		
		List<String> countyList = Arrays.asList(countys);
		List<String> lowestList = new ArrayList<String>();
		for(String key : priceMap.keySet()){
			List<PProdCountyPrice> list = priceMap.get(key);
			String lowestPrice = "";
			String countyName = "";
			for(PProdCountyPrice price : list){
				if(countyList.contains(price.getCounty_id())){
					if(StringHelper.isEmpty(lowestPrice)){
						if(StringHelper.isNotEmpty(price.getCounty_price())){
							lowestPrice = price.getCounty_price();
						}else if(StringHelper.isNotEmpty(price.getArea_price())){
							lowestPrice = price.getArea_price();
						}else if(StringHelper.isNotEmpty(price.getPrice())){
							lowestPrice = price.getPrice();
						}
						countyName = price.getCounty_name();
					}else{
						String priceValue= "";
						if(StringHelper.isNotEmpty(price.getCounty_price())){
							priceValue = price.getCounty_price();
						}else if(StringHelper.isNotEmpty(price.getArea_price())){
							priceValue = price.getArea_price();
						}else if(StringHelper.isNotEmpty(price.getPrice())){
							priceValue = price.getPrice();
						}
						if(StringHelper.isNotEmpty(priceValue) && Integer.parseInt(priceValue)>Integer.parseInt(lowestPrice)){
							lowestPrice = priceValue;
							countyName = price.getCounty_name();
						}
					}
				}
			}
			if(StringHelper.isNotEmpty(lowestPrice)){
				lowestList.add(key+"_"+lowestPrice+"_"+countyName);
			}
		}
		int realPrice = 0;
		String src = "";
		for(String dto : lowestList){
			realPrice += Integer.parseInt(dto.split("_")[2].toString());
			if(prodType.equals(SystemConstants.PROD_TYPE_BASE)){
				src += "【"+dto.split("_")[3].toString()+"】最低价"+ NumericHelper.changeF2Y(Long.parseLong(dto.split("_")[2].toString()))+"元;";
			}else{
				src += "【"+dto.split("_")[3].toString()+"】"+dto.split("_")[1].toString()+"最低价"+NumericHelper.changeF2Y(Long.parseLong(dto.split("_")[2].toString()))+"元;";
			}
		}
		if(realPrice != 0 && tariff.getBilling_cycle()*realPrice > tariff.getRent()){
			throw new ComponentException("产品定价不能小于每周期"+NumericHelper.changeF2Y((long)realPrice)+"元:(" +src+")!");
		}
	}

	/**
	 * 更新最低定价
	 * @param list
	 * @param prodId
	 * @param operator 
	 * @throws Exception
	 */
	public void saveLowestPrice(List<PProdCountyPrice> list, String prodId)
			throws Exception {
		String[] countys = CollectionHelper.converValueToArray(list, "county_id");
		
		List<PProdCountyPrice> oldLowestCounty = pProdCountyPriceDao.getLowestCounty(prodId, SystemConstants.COUNTY_ALL, " 1=1 ");
		pProdCountyPriceDao.deleteLowestCounty(prodId,countys);
		
		List<PProdCountyPrice> priceList = new ArrayList<PProdCountyPrice>();
		PProdCountyPrice price = null;
		
		for(PProdCountyPrice dto : list){
			if(dto.getPrice() == null && dto.getArea_price() == null && dto.getCounty_price() == null)
				continue;
			price = new PProdCountyPrice(prodId,dto.getCounty_id(),dto.getPrice(),dto.getArea_price(),dto.getCounty_price());
			priceList.add(price);
		}
		pProdCountyPriceDao.save(priceList.toArray(new PProdCountyPrice[priceList.size()]));
		
		List<PProdCountyPrice> newLowestCounty = pProdCountyPriceDao.getLowestCounty(prodId,SystemConstants.COUNTY_ALL, " 1=1 ");
		
//		JsonHelper.fromObject(oldLowestCounty).equals(JsonHelper.fromObject(newLowestCounty))
		List<String> old=new ArrayList<String>();
		List<String> newList=new ArrayList<String>();
		
		for(PProdCountyPrice pcp :oldLowestCounty){
			old.add(JsonHelper.fromObject(pcp));
		}
		
		for(PProdCountyPrice pcp :newLowestCounty){
			newList.add(JsonHelper.fromObject(pcp));
		}
		
		String changeInfo = BeanHelper.listchange(old, newList, null, null);
		
		if(StringHelper.isNotEmpty(changeInfo)){
			PProd prod = pProdDao.findByKey(prodId);
			SSysChange change = new SSysChange(SysChangeType.PROD.toString(), getDoneCOde(), prodId, 
					prod.getProd_name(), "产品最低限价配置", changeInfo, WebOptr.getOptr().getOptr_id(), new Date());
			sSysChangeDao.save(change);
		}
	
	}
	
	/**
	 * @Description: 根据资费id，删除资费，以及对于的折扣
	 * @param tariffId
	 * @throws Exception
	 * @return void
	 */
	public void deleteTariffById(String tariffId) throws Exception {
//		List<PProdTariffDisct> disctlist = queryDisct(tariffId);
//		for (PProdTariffDisct dto : disctlist) {
//			pProdTariffDisctDao.deleteDisctByDisctId(dto.getDisct_id());
//		}
		pProdTariffDao.deleteTariffByTariffId(tariffId);
	}

	/**
	 * @Description: 根据产品id，删除产品，以及该产品对应的资费与折扣
	 * @param prodId
	 * @throws Exception
	 * @return void
	 */
	public void deletePordById(String prodId) throws Exception {
		List<ProdTariffDto> tarifflist = pProdTariffDao.queryTariffByProd(prodId);
		for (ProdTariffDto dto : tarifflist) {
			deleteTariffById(dto.getTariff_id());
		}
		pProdDao.deleteProdByProdId(prodId);
	}

	public boolean checkGroupRes(String groupId, String count) throws Exception {
		if (StringHelper.isNotEmpty(groupId)&& StringHelper.isNotEmpty(count)
				&& pResgroupDao.checkGroupRes(groupId) <= Integer.parseInt(count)) {
			return false;
		} else {
			return true;
		}
	}
	
	public boolean validName(String prodName,String[] prodCountyIds,String prodId) throws Exception {
		return pProdDao.validName(prodName,prodCountyIds,prodId);
	}

	public Map<String,Object> queryProdOrderByServ() throws Exception {
//		List<PProd> list = pProdDao.queryProdOrderByServ();
		List<PProd> list = pProdDao.findAll();
		Map<String, List<PProd>> prodMap = CollectionHelper.converToMap(list, "serv_id");
		Map<String , Object> map = new HashMap<String, Object>();
		List<SItemvalue> allList = new ArrayList<SItemvalue>();
		for (String key : prodMap.keySet()) {
			if(StringHelper.isNotEmpty(key)){
				List<PProd> pList = prodMap.get(key);
				List<SItemvalue> sList = new ArrayList<SItemvalue>();
				for(PProd dto : pList){
					SItemvalue s = new SItemvalue();
					s.setItem_value(dto.getProd_id());
					s.setItem_name(dto.getProd_name());
					sList.add(s);
				}
				allList.addAll(sList);
				map.put(key, sList);	
			}
		}
		if(allList.size()>0){
			map.put("all", allList);	
		}
		return map;
	}
	public List<PPackageProd> queryPackageByProdId(String prod_id)  throws Exception {
		List<PPackageProd> list = pPackageProdDao.queryPackProdByProdId(prod_id);
//		List<PProd> pList = pProdDao.findAll();
//		 Map<String, List<PProd>> map = CollectionHelper.converToMap(pList, "prod_id");
		return list;
	}
	
	public PProdDao getPProdDao() {
		return pProdDao;
	}

	public void setPProdDao(PProdDao prodDao) {
		pProdDao = prodDao;
	}

	public void setPResDao(PResDao resDao) {
		pResDao = resDao;
	}

	public void setPResgroupDao(PResgroupDao resgroupDao) {
		pResgroupDao = resgroupDao;
	}

	public void setSAreaDao(SAreaDao areaDao) {
		sAreaDao = areaDao;
	}

	public void setPProdDynResDao(PProdDynResDao prodDynResDao) {
		pProdDynResDao = prodDynResDao;
	}

	public void setPProdStaticResDao(PProdStaticResDao prodStaticResDao) {
		pProdStaticResDao = prodStaticResDao;
	}

	public void setPProdTariffDisctDao(PProdTariffDisctDao prodTariffDisctDao) {
		pProdTariffDisctDao = prodTariffDisctDao;
	}

	public void setPDictProdDao(PDictProdDao dictProdDao) {
		pDictProdDao = dictProdDao;
	}

	public void setPPackageProdDao(PPackageProdDao packageProdDao) {
		pPackageProdDao = packageProdDao;
	}
	
	public void setPPackageProdHisDao(PPackageProdHisDao packageProdHisDao) {
		pPackageProdHisDao = packageProdHisDao;
	}

	public void setTRuleDefineDao(TRuleDefineDao ruleDefineDao) {
		tRuleDefineDao = ruleDefineDao;
	}

	public void setSCountyDao(SCountyDao countyDao) {
		sCountyDao = countyDao;
	}

	public void setPProdCountyResDao(PProdCountyResDao prodCountyResDao) {
		pProdCountyResDao = prodCountyResDao;
	}

	public void setPProdResChangeDao(PProdResChangeDao prodResChangeDao) {
		pProdResChangeDao = prodResChangeDao;
	}

	public void setPProdTariffCountyDao(PProdTariffCountyDao prodTariffCountyDao) {
		pProdTariffCountyDao = prodTariffCountyDao;
	}

	public void setTAcctitemToProdDao(TAcctitemToProdDao acctitemToProdDao) {
		tAcctitemToProdDao = acctitemToProdDao;
	}

	public void setTServerResDao(TServerResDao tserverResDao) {
		tServerResDao = tserverResDao;
	}

	public PProdTariffDao getPProdTariffDao() {
		return pProdTariffDao;
	}

	public void setPProdTariffDao(PProdTariffDao prodTariffDao) {
		pProdTariffDao = prodTariffDao;
	}

	public void setPPromotionDao(PPromotionDao promotionDao) {
		pPromotionDao = promotionDao;
	}
	
	public void setPProdCountyDao(PProdCountyDao pProdCountyDao){
		this.pProdCountyDao = pProdCountyDao;
	}
	public void setSRoleCountyDao(SRoleCountyDao roleCountyDao) {
		sRoleCountyDao = roleCountyDao;
	}
	public void setPProdDictDao(PProdDictDao prodDictDao) {
		pProdDictDao = prodDictDao;
	}
	public void setPProdDictCountyDao(PProdDictCountyDao prodDictCountyDao) {
		pProdDictCountyDao = prodDictCountyDao;
	}

	public void setPPromFeeCountyDao(PPromFeeCountyDao pPromFeeCountyDao) {
		this.pPromFeeCountyDao = pPromFeeCountyDao;
	}

	public void setPProdTariffDisctCountyDao(
			PProdTariffDisctCountyDao prodTariffDisctCountyDao) {
		pProdTariffDisctCountyDao = prodTariffDisctCountyDao;
	}
	public void setPProdCountyPriceDao(
			PProdCountyPriceDao prodCountyPriceDao) {
		pProdCountyPriceDao = prodCountyPriceDao;
	}
	
	public void setSSysChangeDao(SSysChangeDao sSysChangeDao) {
		this.sSysChangeDao = sSysChangeDao;
	}
	public void setTServerCountyDao(TServerCountyDao tServerCountyDao) {
		this.tServerCountyDao = tServerCountyDao;
	}

	
}
