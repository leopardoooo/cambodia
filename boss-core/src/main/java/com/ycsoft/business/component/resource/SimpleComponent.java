package com.ycsoft.business.component.resource;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.axis2.databinding.types.soapencoding.Array;
import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TAddress;
import com.ycsoft.beans.config.TDistrict;
import com.ycsoft.beans.prod.PPromFeeUser;
import com.ycsoft.beans.prod.PRes;
import com.ycsoft.beans.system.SDeptAddr;
import com.ycsoft.business.commons.abstracts.BaseBusiComponent;
import com.ycsoft.business.dao.config.TAddressDao;
import com.ycsoft.business.dao.config.TDistrictDao;
import com.ycsoft.business.dao.system.SDeptAddrDao;
import com.ycsoft.business.dao.system.SParamDao;
import com.ycsoft.business.dto.config.TAddressDto;
import com.ycsoft.business.dto.core.prod.CProdDto;
import com.ycsoft.business.dto.core.prod.PromFeeProdDto;
import com.ycsoft.commons.constants.DataRight;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ComponentException;
import com.ycsoft.commons.exception.ErrorCode;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.tree.TreeNode;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.sysmanager.dto.prod.ResGroupDto;
/**
 * 简单资源操作：安装地址、ip地址等
 *
 * @author pyb
 *
 * Mar 11, 2010
 *
 */
@Component
public class SimpleComponent extends BaseBusiComponent {
	private TAddressDao tAddressDao;
	private SParamDao sParamDao;
	private TDistrictDao tDistrictDao;
	private SDeptAddrDao sDeptAddrDao;
	

	public List<TAddressDto> queryAddrByName(String q,String pId) throws Exception{
		String dataRight = "";
		try {
			dataRight = this.queryDataRightCon(getOptr(), DataRight.NEW_CUST_ADDRESS.toString());
		} catch (Exception e) {
			dataRight = " 1=1 ";
		}		
		List<TAddressDto> list = tAddressDao.queryActiveAddrByName(q,pId, getOptr().getCounty_id(),null, dataRight);
		return list;
	}
	
	
	public List<TAddressDto> queryAddrByLike(String name,String pId) throws Exception{
		
		List<SDeptAddr> sList = sDeptAddrDao.getAddrByDept(getOptr().getDept_id());
		String[] addrIds = null;
		if(sList.size()>0){
			addrIds = CollectionHelper.converValueToArray(sList, "addr_id");
		}
		if(StringHelper.isEmpty(name)){
			if(StringHelper.isEmpty(pId)){
				return tAddressDao.queryAddrByIds(addrIds);
			}else{
				return tAddressDao.queryAddrById(pId);
			}
		}
		
		
		//2级 允许查询的addr
		List<TAddressDto> twoLevelList = tAddressDao.queryAddrByAllowPids(SystemConstants.ADDR_TREE_LEVEL_TWO,addrIds);
		List<String> twoPidList =  CollectionHelper.converValueToList(twoLevelList, "addr_id");
		
		List<TAddressDto> threeList = tAddressDao.queryAddrByaddrPids(name,twoPidList.toArray(new String[twoPidList.size()]));
		
		List<String> addPids = new ArrayList<String>();
		for(TAddressDto t: threeList){
			if(!addPids.contains(t.getAddr_pid())){
				addPids.add(t.getAddr_pid());
			}
		}
		
		List<String> addTwoQueryPids = new ArrayList<String>();
		for(String t : addPids){
			if(!twoPidList.contains(t)){
				addTwoQueryPids.add(t);
			}
		}
		//2级允许的 并且 过滤 name
		List<TAddressDto> twoAllowList = tAddressDao.queryAddrByaddrIds(name,addPids.toArray(new String[addPids.size()]));
		List<TAddressDto> twoQueryList = null;
		if(addTwoQueryPids.size()>0){
			twoQueryList = tAddressDao.queryAddrByAllowIds(SystemConstants.ADDR_TREE_LEVEL_TWO,addTwoQueryPids.toArray(new String[addTwoQueryPids.size()]));
		}
		List<TAddressDto> oneAllowList = tAddressDao.queryAddrByAllowIds(SystemConstants.ADDR_TREE_LEVEL_ONE,null);
		
		if(twoAllowList.size()>0){
			oneAllowList.addAll(twoAllowList);
		}
		if(twoQueryList != null && twoQueryList.size()>0){
			oneAllowList.addAll(twoQueryList);
		}
		if(threeList.size()>0){
			oneAllowList.addAll(threeList);
		}

		
		return oneAllowList;
		
//		TreeNode _t = null;  
//		for(TAddress addr : oneList){
//			_t = new TreeNode();
//			_t.setId(addr.getAddr_id());
//			_t.setText(addr.getAddr_name());
//			
//			
//			
//		}
//		
//		
//		
//		
//		//生成套餐树
//		List<PromFeeProdDto> promProdList = userComponent.queryPromFeeProds(promFeeId);
//		Map<String,List<PromFeeProdDto>> promProdmap = CollectionHelper.converToMap(promProdList, "user_no");
//		
//		String[] prods = CollectionHelper.converValueToArray(promProdList, "prod_id");
//		List<ResGroupDto> groupList = userComponent.queryGroupByProdIds(prods);
//		Map<String,List<ResGroupDto>> groupmap = CollectionHelper.converToMap(groupList, "prod_id");
//		
//		String[] groups = CollectionHelper.converValueToArray(groupList, "group_id");
//		List<PRes> resList = userComponent.queryResByGroupId(groups);
//		Map<String,List<PRes>> resmap = CollectionHelper.converToMap(resList, "group_id");
//		
//		List<TreeNode> target = new LinkedList<TreeNode>(); //规则
//		List<TreeNode> targep = null; //产品
//		List<TreeNode> targeg = null; //产品动态组
//		List<TreeNode> targer = null; //资源
//		TreeNode _t = null;  
//		TreeNode _p = null;
//		TreeNode _g = null;
//		TreeNode _r = null;
//		for (PPromFeeUser v : promUserList) {
//			_t = new TreeNode();
//			_t.setId(v.getUser_no()+"_"+v.getProm_fee_id());
//			_t.setText( v.getRule_name()+ (v.getUser_fee()==null?"":"_实缴:"+v.getUser_fee()/100)+(v.getProd_count()==null?"":"_可选数:"+v.getProd_count()));
//			_t.getOthers().put("user_fee",v.getUser_fee()==null?"":Integer.toString(v.getUser_fee()));
//			_t.getOthers().put("type","RULE");
//			_t.getOthers().put("allow_user",v.getAllow_user());
//			_t.getOthers().put("user_no",v.getUser_no());
//			_t.getOthers().put("rule_id",v.getRule_id());
//			_t.getOthers().put("prod_count",v.getProd_count()==null?"":Integer.toString(v.getProd_count())); //可选数量
//			if(promProdmap.get(v.getUser_no())!=null){
//				targep = new LinkedList<TreeNode>();
//				for(PromFeeProdDto p : promProdmap.get(v.getUser_no()) ){
//					_p = new TreeNode();
//					_p.setId(p.getProd_id()+"_"+p.getUser_no());
//					_p.setText(p.getProd_name()+ (p.getReal_pay()==null?"":"_实缴:"+p.getReal_pay()));
//					_p.getOthers().put("type","PROD");
//					_p.getOthers().put("is_base",p.getIs_base());
//					_p.getOthers().put("prod_id",p.getProd_id());
//					_p.getOthers().put("should_pay",p.getShould_pay()==null?"":Integer.toString(p.getShould_pay()*100));
//					_p.getOthers().put("real_pay",p.getReal_pay()==null?"":Integer.toString(p.getReal_pay()*100));
//					_p.getOthers().put("tariff_id",p.getTariff_id());
//					_p.getOthers().put("months",p.getMonths()==null?"":Integer.toString(p.getMonths()));
//					_p.getOthers().put("force_select",p.getForce_select());
//					_p.getOthers().put("bind_prod_id",p.getBind_prod_id());
//					if(groupmap.get(p.getProd_id())!=null){
//						targeg = new LinkedList<TreeNode>();
//						for(ResGroupDto r: groupmap.get(p.getProd_id())){
//							_g = new TreeNode();
//							_g.setId(r.getGroup_id()+"_"+p.getProd_id()+"_"+p.getUser_no());
//							_g.setText(r.getGroup_name()+(r.getRes_number()==0?"":"_可选数:"+r.getRes_number()));
//							_g.getOthers().put("type","GROUP");
//							_g.getOthers().put("group_id",r.getGroup_id());
//							_g.getOthers().put("res_number",r.getRes_number()==0?"":Integer.toString(r.getRes_number())); //可选数量
//							if(resmap.get(r.getGroup_id())!=null){
//								targer = new LinkedList<TreeNode>();
//								for(PRes res:resmap.get(r.getGroup_id())){
//									_r = new TreeNode();
//									_r.setId(res.getRes_id()+"_"+res.getGroup_id()+"_"+p.getUser_no());
//									_r.setText(res.getRes_name());
//									_r.setChecked(false);
//									_r.setLeaf(true);
//									_r.getOthers().put("type","RES");
//									_r.getOthers().put("res_id",res.getRes_id());
//									targer.add(_r);
//								}
//								_g.setChildren(targer);
//							}
//							targeg.add(_g);
//						}
//						_p.setChildren(targeg);
//					}else{
//						_p.setLeaf(true);
//					}
//					
//					if(p.getForce_select().equals("T")){
////						_p.setLeaf(true);
////						_p.setDisabled(true);
////						_p.setChecked(true);
//					}else{
//						_p.setChecked(false);
//					}
//					targep.add(_p);
//				}
//			}
//			_t.setChildren(targep);
////			_t.getOthers().put("type", v.getOptr_id());
////			_t.getOthers().put("countyId", v.getCounty_id());
//			target.add(_t);
//		}
//		if(promFeeProds.size()==0){
//			PromFeeProdDto dto = new PromFeeProdDto();
//			dto.setUser_name_text("用户条件不符合!");
//			promFeeProds.add(dto);
//		}
//		
//		//客户宽带产品与套餐中的宽带产品进行比较，如果套餐中的宽带产品客户未订购，不能进行套餐缴费
//		Boolean key = false;
//		String bandStr = "";
//		List<CProdDto> list = userProdComponent.queryAllProdAcct(custId,cust.getCounty_id());
//		Map<String,List<CProdDto>> custBandProdmap = CollectionHelper.converToMap(list, "serv_id");
//		List<String> cBandProds = CollectionHelper.converValueToList(custBandProdmap.get("BAND"), "prod_id");
//		//宽带自动匹配
//		cBandProds.add("BAND");
//		Map<String,List<PromFeeProdDto>> pBandProdmap = CollectionHelper.converToMap(promProdList, "serv_id");
//		if(pBandProdmap.get("BAND") != null){
//			for(PromFeeProdDto dto:pBandProdmap.get("BAND")){
//				if(!cBandProds.contains(dto.getProd_id())){
//					key = true;
//					bandStr=dto.getProd_name();
//					break;
//				}
//			}
//		}
//		if(key){
//			target = new LinkedList<TreeNode>();
//		}
//		
//		return target;
	}
	

	public String queryCustAddrName(String addrId) throws Exception{
		TAddress addr = tAddressDao.findByKey(addrId);
		TAddress paddr = tAddressDao.findByKey(addr.getAddr_pid());
		TDistrict district = tDistrictDao.findByKey(addr.getDistrict_id());
		
		String addrName = "No."+addr.getAddr_name()+",St."+paddr.getAddr_name()+",";
		if(district != null){
			addrName = addrName+district.getDistrict_name();
		}
		return addrName;
	}
	
	
	/**
	 * 查询单个小区信息.
	 * @param addrId
	 * @return
	 */
	public TAddress querySingleAddress(String addrId) throws Exception{
		return tAddressDao.findByKey(addrId);
	}
	
	/**
	 * @return
	 */
	public List<TAddressDto> queryAddrDistrict() throws Exception{
		return tAddressDao.queryAddrDistrict(getOptr().getCounty_id());
	}
	
	/**
	 * 查询区域下的小区
	 * @param addrPid
	 * @return
	 */
	public List<TAddressDto> queryAddrCommunity(String addrPid) throws JDBCException {
		return tAddressDao.queryAddrCommunity(addrPid);
	}

	public String queryParamValue(String name) throws JDBCException{
		return sParamDao.queryValue(name).getParam_value();
	}

	public void saveParamValue(String name, Integer value) throws JDBCException {
		sParamDao.saveParamValue(name,value);
	}

	public void setTAddressDao(TAddressDao addressDao) {
		tAddressDao = addressDao;
	}
	/**
	 * @param paramDao the sParamDao to set
	 */
	public void setSParamDao(SParamDao paramDao) {
		sParamDao = paramDao;
	}


	public void setTDistrictDao(TDistrictDao districtDao) {
		this.tDistrictDao = districtDao;
	}


	public void setSDeptAddrDao(SDeptAddrDao deptAddrDao) {
		this.sDeptAddrDao = deptAddrDao;
	}



}
