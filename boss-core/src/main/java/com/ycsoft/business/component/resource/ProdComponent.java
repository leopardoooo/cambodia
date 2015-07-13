
package com.ycsoft.business.component.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TServerRes;
import com.ycsoft.beans.prod.PPackageProd;
import com.ycsoft.beans.prod.PProd;
import com.ycsoft.beans.prod.PProdTariff;
import com.ycsoft.beans.prod.PProdTariffDisct;
import com.ycsoft.beans.prod.PProdUserRes;
import com.ycsoft.business.commons.abstracts.BaseBusiComponent;
import com.ycsoft.business.dao.prod.PDictProdDao;
import com.ycsoft.business.dao.prod.PPackageProdDao;
import com.ycsoft.business.dao.prod.PProdTariffDisctDao;
import com.ycsoft.business.dao.prod.PProdUserResDao;
import com.ycsoft.business.dao.prod.TServerResDao;
import com.ycsoft.business.dto.core.prod.PProdDto;
import com.ycsoft.business.dto.core.prod.ProdDictDto;
import com.ycsoft.business.dto.core.prod.ProdResDto;
import com.ycsoft.business.dto.core.prod.ProdTariffDto;
import com.ycsoft.business.dto.core.prod.ResGroupDto;
import com.ycsoft.commons.constants.DataRight;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.core.JDBCException;


/**
 * @author YC-SOFT
 *
 */
@Component
public class ProdComponent extends BaseBusiComponent {
	private PProdTariffDisctDao pProdTariffDisctDao;
	private PPackageProdDao pPackageProdDao;
	private PDictProdDao pDictProdDao;
	private PProdUserResDao pProdUserResDao;
	private TServerResDao tServerResDao;
	
	/**
	 * 判断产品是否按照到期日停机
	 * @param prod
	 * @param tariff
	 * @return
	 */
	public String stopByInvaliddate(PProd prod, PProdTariff tariff) {
		String stopByInvlaidDate = SystemConstants.BOOLEAN_FALSE;
		if ( SystemConstants.BOOLEAN_TRUE.equals(prod.getJust_for_once())
				|| tariff.getBilling_cycle()>1 
				|| (tariff.getBilling_type().equals(SystemConstants.BILLING_TYPE_MONTH) 
						&& tariff.getRent() ==0)){
			stopByInvlaidDate = SystemConstants.BOOLEAN_TRUE;
		}
		return stopByInvlaidDate;
	}

	/**
	 * 创建产品目录树
	 * @param userType
	 * @return
	 * @throws Exception
	 */
	public List<ProdDictDto> queryProdTree(String userType) throws Exception {
		List<ProdDictDto> prodList = pDictProdDao.queryAll(userType, getOptr()
				.getArea_id());
		return prodList;
	}

	/**
	 * 创建产品目录树
	 * @param orderprods
	 * @return
	 */
	public List<ProdDictDto> queryProdTree(List<PProd> orderprods) throws Exception {
		if (orderprods != null && orderprods.size()>0){
			String[] prodIds = new String[orderprods.size()];
			for (int i = 0;i<orderprods.size();i++){
				prodIds[i] = orderprods.get(i).getProd_id();
			}

			Map<String, PProd> prodMap = CollectionHelper.converToMapSingle(orderprods,"prod_id");
			List<ProdDictDto> prodList = pDictProdDao.queryProdDict(prodIds, getOptr()
					.getArea_id(),getOptr().getCounty_id());

			for (ProdDictDto p :prodList){
				p.setPProd(prodMap.get(p.getNode_id()));

				//设置产品的ID：自身ID+父节点ID
				if(p.getPProd() != null){
					p.setNode_id(p.getNode_pid()+","+p.getNode_id());
				}

			}
			return prodList;
		}
		return new ArrayList<ProdDictDto>();
	}

	/**
	 * 根据产品id获取产品基本信息
	 * @param prodId
	 * @return
	 * @throws Exception
	 */
	public PProd queryProdById(String prodId) throws Exception{
		PProd prod = pProdDao.queryProdById(prodId);
		return prod;
	}
	
	
	public PProd queryById(String prodId) throws Exception {
		return pProdDao.findByKey(prodId);
	}


	/**
	 * 根据产品id，获取产品对应的资源信息
	 * @param prodId
	 * @return
	 * @throws Exception
	 */
	public List<ProdResDto> queryProdRes(String prodId) throws Exception{
		List<ProdResDto> prodResList = new ArrayList<ProdResDto>();
		Map<String,ProdResDto> prodResMap = new HashMap<String,ProdResDto>();
		//查找产品的基本信息
		PProd prod = pProdDao.findByKey(prodId);
		//判断产品是否为套餐，如果为套餐，查询子产品，并遍历子产品，查找子产品对应的资源信息
		//如果不是套餐，则直接查找产品对应的资源信息
		if(null != prod){
			if (prod.isPkg()){
				//查找套餐对应的自产品
				List<PPackageProd> pkgProdList = queryPackageProd(prod.getProd_id());
				for(PPackageProd p:pkgProdList){
					ProdResDto prodResDto = this.queryProdRes(p.getProd());
					if(null==prodResMap.get(prodResDto.getProd_id())){
						prodResMap.put(prodResDto.getProd_id(), prodResDto);
					}
 				}
			} else {
				prodResList.add(this.queryProdRes(prod));
			}
		}
		Iterator<String> iterator  = prodResMap.keySet().iterator();
		while(iterator.hasNext()){
			prodResList.add(prodResMap.get(iterator.next()));
		}
		return prodResList;
	}
	/**
	 * 根据产品id获取产品对应的有效资费
	 * 设置每个资费对应的业务规则和折扣信息
	 * @param prodId
	 * @return
	 * @throws Exception
	 */
	public List<ProdTariffDto> queryTariffByProd(String prodId)  throws Exception{
		String dataRight = this.queryDataRightCon(getOptr(), DataRight.TARIFF.toString());
		List<ProdTariffDto> tariffList = pProdTariffDao.queryProdTariff(prodId,getOptr().getCounty_id(),dataRight);
		return tariffList;
	}
	
	/**
	 * 操作员是否有修改资费生效日期权限
	 * @return
	 * @throws Exception
	 */
	public String queryChangeTariffRole() throws Exception {
		return this.queryDataRightCon(getOptr(), DataRight.CHANGE_TARIFF.toString());
	}
	
	/**
	 * 根据产品id获取产品对应的有效资费和折扣
	 * 设置每个资费对应的业务规则和折扣信息
	 * @param prodId
	 * @return
	 * @throws Exception
	 */
	public List<PProdDto> queryTariffByProdDisct(String prodId)  throws Exception{
		String dataRight = this.queryDataRightCon(getOptr(), DataRight.TARIFF.toString());
		return pProdTariffDao.queryProdTariffDisct(prodId,getOptr().getCounty_id(),dataRight);
	}
	
	/**
	 * 根据资费id获取资费的基本信息
	 * @param tariffId
	 * @return
	 * @throws Exception
	 */
	public PProdTariff queryTariffById(String tariffId) throws Exception{
		return pProdTariffDao.findByKey(tariffId);
	}
	
	/**
	 * 查询VOD按次点播资费
	 * @return
	 */
	public PProdTariff queryVodProdTariff() throws Exception{
		return pProdTariffDao.queryVodProdTariff(getOptr().getCounty_id());
	}


	
	/**
	 * 查询产品信息
	 * @param prodSn
	 * @return
	 */
	public PProd queryProdByProdSn(String prodSn) throws JDBCException {
		return pProdDao.queryProdByProdSn(prodSn);
	}
	

	/**
	 * 根据资费id获取资费的基本信息
	 * @param tariffId
	 * @return
	 * @throws Exception
	 */
	public PProdTariffDisct queryDisctById(String disctId) throws Exception{
		return pProdTariffDisctDao.findByKey(disctId);
	}
	/**
	 * 根据产品id，获取基本产品对应的资源信息
	 * @param prodId
	 * @return
	 * @throws Exception
	 */
	private ProdResDto queryProdRes(PProd prod ) throws Exception{
		ProdResDto prodRes = new ProdResDto();
		prodRes.setProd_id(prod.getProd_id());
		prodRes.setProd_name(prod.getProd_name());
		//查找产品对应的静态资源信息
		prodRes.setStaticResList(pProdDao.queryProdStaticRes(getOptr().getCounty_id(),prod.getProd_id()));
		prodRes.setDynamicResList(pProdDao.queryResGroup(prod.getProd_id()));
		if (prodRes.getDynamicResList() != null && prodRes.getDynamicResList().size()>0){
			for (ResGroupDto resGroup:prodRes.getDynamicResList()){
				resGroup.setResList(pProdDao.queryResByGroupId(resGroup.getGroup_id()));
			}
		}

		return prodRes;
	}

	/**
	 * 根据套餐编号获取对应的子产品信息
	 * @param pkgId
	 * @return
	 * @throws Exception
	 */
	public List<PPackageProd> queryPackageProd(String pkgId)  throws Exception{
		PPackageProd pkgProd = new PPackageProd();
		pkgProd.setPackage_id(pkgId);
		pkgProd.setType(SystemConstants.PACKAGE_MARKET_TYPE);
		List<PPackageProd> pkgProdList= pPackageProdDao.findByEntity(pkgProd);
		
		String[] prodIds = CollectionHelper.converValueToArray(pkgProdList, "prod_id");
		Map<String, PProd> prodMap = null;
		if(null!=prodIds && prodIds.length>0){
			prodMap = CollectionHelper.converToMapSingle(
				pProdDao.findByProdIds(prodIds), "prod_id");
		}
		String[] prodTariffIds = CollectionHelper.converValueToArray(pkgProdList, "tariff_id");
		Map<String, PProdTariff> tariffMap = CollectionHelper
				.converToMapSingle(pProdTariffDao
						.queryPTariffByIds(prodTariffIds),
						"tariff_id");
		
		for(PPackageProd pkg:pkgProdList){
			pkg.setProd(prodMap.get(pkg.getProd_id()));
			if (StringHelper.isNotEmpty(pkg.getTariff_id()))
				pkg.setProdTariff(tariffMap.get(pkg.getTariff_id()));
		}

		return pkgProdList;
	}
	/**
	 * 根据套餐编号，资费 获取对应的子产品信息
	 * @param pkgId
	 * @return
	 * @throws Exception
	 */
	public List<PPackageProd> queryPackageProd(String pkgId,String tariffId)  throws Exception{
		List<PPackageProd> pkgProdList= pPackageProdDao.getMarketPackProdById(pkgId, tariffId);
		
		String[] prodIds = CollectionHelper.converValueToArray(pkgProdList, "prod_id");
		Map<String, PProd> prodMap = null;
		if(null!=prodIds && prodIds.length>0){
			prodMap = CollectionHelper.converToMapSingle(
				pProdDao.findByProdIds(prodIds), "prod_id");
		}
		String[] prodTariffIds = CollectionHelper.converValueToArray(pkgProdList, "tariff_id");
		Map<String, PProdTariff> tariffMap = CollectionHelper
				.converToMapSingle(pProdTariffDao
						.queryPTariffByIds(prodTariffIds),
						"tariff_id");
		
		for(PPackageProd pkg:pkgProdList){
			pkg.setProd(prodMap.get(pkg.getProd_id()));
			if (StringHelper.isNotEmpty(pkg.getTariff_id()))
				pkg.setProdTariff(tariffMap.get(pkg.getTariff_id()));
		}

		return pkgProdList;
	}

	public void setPProdTariffDisctDao(PProdTariffDisctDao prodTariffDisctDao) {
		pProdTariffDisctDao = prodTariffDisctDao;
	}

	/**
	 * @param packageProdDao the pPackageProdDao to set
	 */
	public void setPPackageProdDao(PPackageProdDao packageProdDao) {
		pPackageProdDao = packageProdDao;
	}

	/**
	 * @param dictProdDao the pDictProdDao to set
	 */
	public void setPDictProdDao(PDictProdDao dictProdDao) {
		pDictProdDao = dictProdDao;
	}

	/**
	 * 查询产品的子产品
	 * @param prodId
	 * @return
	 */
	public List<PProd> querSubProds(String prodId) throws JDBCException {
		return pProdDao.querySubProds(prodId);
	}

	/**
	 * 获取产品用户特殊资源
	 */
	
	public List<PProdUserRes> queryUserResByCountyId () throws Exception{
		return pProdUserResDao.queryByCountyId(getOptr().getCounty_id());
	}

	public void setPProdUserResDao(PProdUserResDao prodUserResDao) {
		pProdUserResDao = prodUserResDao;
	}

	//中兴宽带资源
	public List<TServerRes> queryZteBandRes() throws Exception {
		return tServerResDao.queryBandRes(SystemConstants.BAND_ZX_SREVER_ID);
	}

	public void setTServerResDao(TServerResDao serverResDao) {
		tServerResDao = serverResDao;
	}

}
