package com.ycsoft.sysmanager.component.system;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TAddress;
import com.ycsoft.beans.config.TCustColonyCfg;
import com.ycsoft.beans.config.TDistrict;
import com.ycsoft.beans.config.TNonresCustApproval;
import com.ycsoft.beans.config.TProvince;
import com.ycsoft.beans.config.TSpell;
import com.ycsoft.beans.core.cust.CCust;
import com.ycsoft.beans.system.SCounty;
import com.ycsoft.beans.system.SDept;
import com.ycsoft.beans.system.SDeptAddr;
import com.ycsoft.beans.system.SItemvalue;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.beans.system.SSysChange;
import com.ycsoft.business.commons.pojo.BusiParameter;
import com.ycsoft.business.dao.config.TAddressDao;
import com.ycsoft.business.dao.config.TCustColonyCfgDao;
import com.ycsoft.business.dao.config.TDistrictDao;
import com.ycsoft.business.dao.config.TNonresCustApprovalDao;
import com.ycsoft.business.dao.config.TProvinceDao;
import com.ycsoft.business.dao.config.TSpellDao;
import com.ycsoft.business.dao.system.SCountyDao;
import com.ycsoft.business.dao.system.SDeptAddrDao;
import com.ycsoft.business.dao.system.SDeptDao;
import com.ycsoft.business.dao.system.SOptrDao;
import com.ycsoft.business.dto.config.DistrictSysDto;
import com.ycsoft.business.dto.config.TAddressDto;
import com.ycsoft.business.dto.config.TAddressSysDto;
import com.ycsoft.business.dto.system.OptrDto;
import com.ycsoft.business.service.externalImpl.ICustServiceExternal;
import com.ycsoft.commons.abstracts.BaseComponent;
import com.ycsoft.commons.constants.BusiCodeConstants;
import com.ycsoft.commons.constants.SequenceConstants;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SysChangeType;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ActionException;
import com.ycsoft.commons.exception.ComponentException;
import com.ycsoft.commons.exception.ErrorCode;
import com.ycsoft.commons.helper.CnToSpell;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.commons.helper.DateHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;
import com.ycsoft.daos.helper.BeanHelper;
import com.ycsoft.sysmanager.web.commons.interceptor.WebOptr;

@Component
public class AddressComponent extends BaseComponent {

	private TAddressDao tAddressDao;
	private TSpellDao tSpellDao;
	private SDeptDao sDeptDao;
	private ICustServiceExternal custService;
	private SCountyDao sCountyDao ;
	private TCustColonyCfgDao tCustColonyCfgDao;
	private SOptrDao sOptrDao;
	private TNonresCustApprovalDao tNonresCustApprovalDao;
	@Autowired
	private SDeptAddrDao sDeptAddrDao;
	@Autowired
	private TProvinceDao tProvinceDao;
	@Autowired
	private TDistrictDao tDistrictDao;

	public void setTNonresCustApprovalDao(
			TNonresCustApprovalDao nonresCustApprovalDao) {
		tNonresCustApprovalDao = nonresCustApprovalDao;
	}

	public List<TAddressDto> queryAddrByName(String q,SOptr optr) throws Exception{
//	public void queryAddrByName(String q,SOptr optr) throws Exception{
//		List<TAddress> list = tAddressDao.findAll();
//		List<TSpell> spell = new ArrayList<TSpell>();
//		
//		for(TAddress addr : list){
//			TSpell sp = new TSpell();
//			sp.setData_id(addr.getAddr_id());
//			sp.setData_type(SystemConstants.DATA_TYPE_ADDRESS);
//			sp.setFull_sepll(CnToSpell.getPinYin(addr.getAddr_name()));
//			sp.setSeq_sepll(CnToSpell.getPinYinHeadChar(addr.getAddr_name()));
//			spell.add(sp);
//		}
//		tSpellDao.save(spell.toArray(new TSpell[spell.size()]));
		List<TAddressDto> addressList = tAddressDao.queryAddrByName(q, optr.getCounty_id(), " 1=1 ");
//		for(TAddressDto addr :addressList ){
//			if(addr.getIs_leaf().equals("T")){
//				if(StringHelper.isNotEmpty(addr.getBusi_optr_name())){
//					addr.setAddr_name(addr.getAddr_name()+"[客户经理："+addr.getBusi_optr_name()+"]");
// 				}
//			}
//		}
		return addressList;
	}

	public Pager<TCustColonyCfg> queryCustColony(Integer start , Integer limit ,String keyword,SOptr optr)throws Exception{
		List<SItemvalue> list = new ArrayList<SItemvalue>();
		if(StringHelper.isNotEmpty(keyword)){
			list = sItemvalueDao.findValueByName(keyword.toUpperCase());
		}
		return tCustColonyCfgDao.query(start, limit,list,keyword,optr.getCounty_id());
	}
	
	public void saveCustColony(String years,String cust_colony,String cust_class,String countys,Integer custNum,Integer userNum,SOptr optr) throws Exception{
		
		String[] custYears = years.split(",");
		String[] custColonys = cust_colony.split(",");
		String[] custClass = cust_class.split(",");
		String[] custCountys = countys.split(",");
		if(custClass.length>1 && custColonys.length>1){
			throw new ComponentException("客户群体和客户优惠类型所选数不能都大于1!");
		}
		if(StringHelper.isEmpty(cust_colony) && StringHelper.isEmpty(cust_class)){
			throw new ComponentException("客户群体和客户优惠类型所选数不能都为空!");
		}
		String type = "";
		String[] value = null ; 
		if(custClass.length>1){
			type = "CLASS";
			value = custClass;
		}else if(custColonys.length>1){
			type = "COLONYS";
			value = custColonys;
		}else if(StringHelper.isEmpty(cust_colony)){
			type = "ALL";
			value = custColonys;
		}else{
			value = custClass;
		}
		
		List<TCustColonyCfg> queryList = tCustColonyCfgDao.queryList(years,cust_colony,cust_class,countys);
		if(queryList.size()>0){
			throw new ComponentException("部分配置已经存在!如:"+queryList.get(0).getYear_date()+"年份"
					+queryList.get(0).getCounty_name_for()+"的"+queryList.get(0).getCust_colony_text()+","+queryList.get(0).getCust_class_text());
		}
		
		List<TCustColonyCfg> dList = new ArrayList<TCustColonyCfg>();
		for(int i=0;i<custYears.length;i++){
			for(int k=0;k<custCountys.length;k++){
				for(int j=0;j<value.length;j++){
					TCustColonyCfg cfg = new TCustColonyCfg();
					cfg.setYear_date(custYears[i]);
					if(type.equals("COLONYS")){
						cfg.setCust_colony(value[j]);
						cfg.setCust_class(custClass[0]);
					}else if(type.equals("CLASS")){
						cfg.setCust_colony(custColonys[0]);
						cfg.setCust_class(value[j]);
					}else {
						cfg.setCust_colony(custColonys[0]);
						cfg.setCust_class(custClass[0]);
					}
					cfg.setCust_num(custNum);
					cfg.setUser_num(userNum);
					cfg.setCounty_id_for(custCountys[k]);
					cfg.setCounty_id(optr.getCounty_id());		
					cfg.setOptr_id(optr.getOptr_id());
					cfg.setStatus(StatusConstants.ACTIVE);
					dList.add(cfg);
					
				}
			}
		}
		tCustColonyCfgDao.save(dList.toArray(new TCustColonyCfg[dList.size()]));
	}
	
	public void updateCustColony(String years,String custColony,String custClass,String countys,Integer custNum,Integer userNum) throws Exception{
		TCustColonyCfg cfg = tCustColonyCfgDao.query(years, custColony,custClass, countys);
		custNum =custNum == null?0:custNum;
		userNum =userNum == null?0:userNum;
		if(cfg.getUse_num() != null && cfg.getUse_num()>custNum){
			throw new ComponentException("新的开户总数小于已开户数!");
		}
		tCustColonyCfgDao.update(years, custColony,custClass, countys, custNum,userNum);
	}
	
	public boolean deleteCustColony(String yearDate,String custColony,String custClass,String countyId) throws Exception {
		tCustColonyCfgDao.delete(yearDate,custColony,custClass,countyId);
		return true;
	}
	
	
	/**
	 * 增加地区
	 * @param treeLevel 
	 * @return
	 * @throws Exception
	 */
	public TAddress saveAddress(TAddressSysDto addr,String type) throws Exception{
		TAddress  newAddr = new TAddress();
		newAddr.setAddr_pid(addr.getAddr_pid());
		newAddr.setArea_id(addr.getArea_id());
		newAddr.setTree_level(addr.getTree_level());
		newAddr.setCounty_id(addr.getCounty_id());
		newAddr.setAddr_name(addr.getAddr_name());
		newAddr.setAddr_id(getNextAddrId());
		newAddr.setNet_type(addr.getNet_type());
		newAddr.setDistrict_id(addr.getDistrict_id());
		newAddr.setIs_leaf(SystemConstants.BOOLEAN_TRUE);
		float  b = (float) 0.00;
		if("leveladd".equals(type)){//新增平级算排序值
			TAddress lastAddr = tAddressDao.findByKey(addr.getAddr_last_id());
			TAddress nextAddr = tAddressDao.querySortNumByNextId(lastAddr.getAddr_pid(), lastAddr.getSort_num());
			if(nextAddr == null){
				b = lastAddr.getSort_num()+1000;
			}else{
				b  = (float)(Math.round((lastAddr.getSort_num()+nextAddr.getSort_num())/2*100))/100;
			}
		}else if("add".equals(type)){//新增下级算排序值，默认最大值+1000
			String maxSortNum =	tAddressDao.queryMaxSortNumByPid(addr.getAddr_pid());
			b = Float.parseFloat(maxSortNum)  + 1000;
		}

		newAddr.setSort_num(b);
		tAddressDao.save(newAddr);
		
		
		//修改父节点is_leaf为F
		updateAddress(addr.getAddr_pid(), SystemConstants.BOOLEAN_FALSE);
		
		return newAddr;
	}

	private String nextAddr(TAddress addr) throws JDBCException {
		String nextAddrId = tAddressDao.getAddrId(addr.getAddr_pid());
		if (addr.getTree_level()==2) {
			nextAddrId = StringHelper.leftWithZero(nextAddrId, 2);
		} else if(addr.getTree_level()>2){
			nextAddrId = StringHelper.leftWithZero(nextAddrId, 5);
		}
		return nextAddrId;
	}
	
	private String getNextAddrId() throws JDBCException{
		return tAddressDao.findSequence(SequenceConstants.SEQ_ADDR_ID).toString();
	}
	
	public List<OptrDto> queryOptrByCountyId(String countyId) throws Exception {
		return sOptrDao.queryOptrByCountyId(countyId);
	}

	/**
	 * 批量保存地址
	 * @param addrList
	 * @param optr
	 * @return
	 * @throws JDBCException
	 */
	public List<TAddress> saveAddrList(List<TAddress> addrList,SOptr optr) throws JDBCException{
		
		for(TAddress addr : addrList){
			addr.setAddr_id(nextAddr(addr));
			addr.setArea_id(optr.getArea_id());
			addr.setCounty_id(optr.getCounty_id());
			addr.setIs_leaf(SystemConstants.BOOLEAN_TRUE);
			tAddressDao.save(addr);
			
			TSpell sp = new TSpell();
			sp.setData_id(addr.getAddr_id());
			sp.setData_type(SystemConstants.DATA_TYPE_ADDRESS);
			sp.setFull_sepll(CnToSpell.getPinYin(addr.getAddr_name()));
			sp.setSeq_sepll(CnToSpell.getPinYinHeadChar(addr.getAddr_name()));
			tSpellDao.save(sp);
		}
		
		//修改父节点is_leaf为F
		if(null !=addrList && addrList.size() > 0){
			updateAddress(addrList.get(0).getAddr_pid(), SystemConstants.BOOLEAN_FALSE);
		}
		
		
		return addrList;
	}

	/**
	 * 修改地区名字
	 * @return
	 * @throws Exception
	 */
	public void editAddress(TAddressSysDto addr) throws JDBCException{
		TAddress  newAddr = new TAddress();
		newAddr.setAddr_id(addr.getAddr_id());
		newAddr.setAddr_name(addr.getAddr_name());
		newAddr.setNet_type(addr.getNet_type());
		newAddr.setSort_num(addr.getSort_num());
		newAddr.setDistrict_id(addr.getDistrict_id());
		tAddressDao.update(newAddr);
		
		//修改地区拼音
//		tSpellDao.updateAddrName(addr.getAddr_id(), CnToSpell.getPinYin(addr.getAddr_name()), CnToSpell.getPinYinHeadChar(addr.getAddr_name()));
	}

	/**
	 * 删除地区
	 * @return
	 * @throws Exception
	 */
	public boolean deleteAddress(String addrId) throws JDBCException{
		List<CCust> custList = tAddressDao.getCustByAddrId(addrId);
		if(custList.size() > 0){
			return false;
		}else{
			TAddress addr = tAddressDao.findByKey(addrId);
			List<TAddress> addrList = tAddressDao.getAddrByPid(addr.getAddr_pid());
			if(addrList.size() == 1){//如果父节点只有当前一个子节点，is_leaf置为T
				updateAddress(addr.getAddr_pid(), SystemConstants.BOOLEAN_TRUE);
			}
			tAddressDao.remove(addrId);
			return true;
		}
	}
	
	public void updateAddressStatus(String addrId, String status) throws Exception{
		TAddress addr = tAddressDao.findByKey(addrId);
		addr.setStatus(status);
		tAddressDao.update(addr);
	}

	/**
	 * 小区挂载
	 * @param newAddrId 新区域编号
	 * @param addrId    需要挂载的小区
	 * @param countyId  对应地市编号
	 * @param optr
	 * @throws Exception
	 */
	public String changeAddr(String newAddrId,String[] addrId ,String countyId,SOptr optr) throws Exception {
			TAddressDto newaddr = tAddressDao.getAddressByAddrId(newAddrId);
			List<CCust> custList = new ArrayList<CCust>();
			List<CCust> custLinkmanList = new ArrayList<CCust>();
			List<CCust> custAllList = tAddressDao.getCustAllByAddrId(addrId,countyId);
			for(String id : addrId){
				TAddressDto oldaddress = tAddressDao.getAddressByAddrId(id);
				if(oldaddress == null)
					throw new ComponentException("小区编号"+id+"不存在!");
				//查询可以修改的c_cust的地址数据
				List<CCust> custAddrList = tAddressDao.getCustByAddrId(newaddr,oldaddress,countyId);
				//查询可以修改的c_cust_linkman的地址数据
				List<CCust> custLinkmanAddrList = tAddressDao.getCustLinkmanByAddrId(newaddr,oldaddress,countyId);
				for(CCust dto:custAddrList){
					CCust custadd = new CCust();
					BeanUtils.copyProperties(dto, custadd);
					custList.add(custadd);
				}
				for(CCust dto:custLinkmanAddrList){
					CCust custadd = new CCust();
					BeanUtils.copyProperties(dto, custadd);
					custLinkmanList.add(custadd);
				}
			}
			for(int i=custAllList.size()-1;i>=0;i--){
				CCust cust = custAllList.get(i);
				boolean flag=false;
				for (CCust dto:custList){
					if (cust.getCust_id().equals(dto.getCust_id())){
						flag = true;
						break;
					}
				}
				if (flag)
					custAllList.remove(i);
			}

			SCounty county = sCountyDao.getCountyById(countyId).get(0);
				BusiParameter p = new BusiParameter();
				optr.setArea_id(county.getArea_id());
				optr.setCounty_id(county.getCounty_id());
				p.setOptr(optr);
				custService.updateAddressList(p,custList,custLinkmanList,BusiCodeConstants.ADDRESS_CHANGE_ADDR);
				//变更小区对应的区域
				tAddressDao.updateAddr(addrId,newAddrId,countyId);
				//修改地址is_leaf属性为F
				updateAddress(newAddrId, SystemConstants.BOOLEAN_FALSE);
				if(custAllList.size()>0){
					String src ="";
					for(CCust dto :custAllList){
						src +=dto.getCust_id()+",";
					}
					src = StringHelper.delEndChar(src, 1);
					return src;
				}else{
					return null;
				}	
	}
	
	public Pager<CCust> queryCustAddrByCustIds(String[] custIds,String countyId,Integer start,Integer limit) throws Exception{
		return tAddressDao.queryCustAddrByCustIds(custIds, countyId,start,limit);
	}
	
	public void updateAddressList(List<CCust> custAddrList,String countyId,SOptr optr) throws Exception{
		SCounty county = sCountyDao.getCountyById(countyId).get(0);
		BusiParameter p = new BusiParameter();
		optr.setArea_id(county.getArea_id());
		optr.setCounty_id(county.getCounty_id());
		p.setOptr(optr);
		custService.updateAddressList(p,custAddrList,null,BusiCodeConstants.ADDRESS_UPDATE_SOME);
	}
	
	/**
	 * 修改地址is_leaf属性
	 * @param addrId
	 * @param isLeaf
	 * @throws JDBCException
	 */
	private void updateAddress(String addrId,String isLeaf) throws JDBCException{
		TAddress addr = tAddressDao.findByKey(addrId);
		addr.setIs_leaf(isLeaf);
		tAddressDao.update(addr);
	}
	
	public List<SDept> queryFgsByCountyId(String countyId) throws Exception {
		return sDeptDao.queryFgsByDeptId(countyId);
	}
	public List<TAddress> queryAddrByCountyId(String countyId) throws Exception {
		return tAddressDao.queryAddrByCountyId(countyId);
	}
	public List<TAddressDto> queryAddrByaddrPid(String addrId) throws Exception {
		return tAddressDao.queryAddrByaddrPid(addrId);
	}
	
	public TAddress queryAddrByaddrId(String addrId) throws Exception {
		return tAddressDao.findByKey(addrId);
	}
	
	public Pager<TNonresCustApproval> queryNonresCustApp(String query,Integer start,Integer limit) throws Exception {
		return tNonresCustApprovalDao.queryNonresCustApp(null,query, start, limit);
	}
	
	public void updateNonresCustApp(TNonresCustApproval nca) throws Exception {
		
		if(StringHelper.isEmpty(nca.getApp_id())){
			TNonresCustApproval nonresCustApp = tNonresCustApprovalDao.queryByAppCode(nca.getApp_code());
			if(nonresCustApp != null)
				throw new ComponentException("该审批单号已存在!");
			nca.setApp_id(tNonresCustApprovalDao.findSequence().toString());
			nca.setStatus(StatusConstants.IDLE);
			tNonresCustApprovalDao.save(nca);
		}else{
			tNonresCustApprovalDao.updateNonresCustApp(nca);
		}
	}
	
	public void deleteNonresCustApp(String appId) throws Exception {
		tNonresCustApprovalDao.deleteByAppId(appId);
	}
	
	public void setTAddressDao(TAddressDao addressDao) {
		tAddressDao = addressDao;
	}

	public TSpellDao getTSpellDao() {
		return tSpellDao;
	}

	public void setTSpellDao(TSpellDao spellDao) {
		tSpellDao = spellDao;
	}
	public void setSDeptDao(SDeptDao deptDao) {
		sDeptDao = deptDao;
	}
	public void setTCustColonyCfgDao(TCustColonyCfgDao custColonyCfgDao) {
		tCustColonyCfgDao = custColonyCfgDao;
	}
	
	public void setCustService(ICustServiceExternal custService) {
		this.custService = custService;
	}
	public void setSCountyDao(SCountyDao countyDao) {
		sCountyDao = countyDao;
	}

	public void setSOptrDao(SOptrDao optrDao) {
		sOptrDao = optrDao;
	}

	public List queryAddrByName(String name, String pId, SOptr optr) throws Exception{
		List<TAddressSysDto> list = new ArrayList<TAddressSysDto>();
		if(StringHelper.isNotEmpty(pId)){
			list = tAddressDao.queryAllAddrById(pId);
		}else{
		
			List<SDeptAddr> sList = sDeptAddrDao.getAddrByDept(optr.getDept_id());
			String[] addrIds = null;
			if(sList.size()>0){
				addrIds = CollectionHelper.converValueToArray(sList, "addr_id");
			}else{
//				SDept dept= sDeptDao.findByKey(optr.getDept_id());
//				if(StringHelper.isNotEmpty(dept.getAgent_id())){
//					throw new ComponentException(ErrorCode.DeptAddrIsNull,dept.getDept_name());
//				}
				//tAddressDao.queryAddrByAllowPids(levelId, addrPid)
				String[] pids={SystemConstants.ADDRESS_ROOT_ID};
				addrIds= CollectionHelper.converValueToArray(tAddressDao.queryAllAddrByPids(SystemConstants.ADDR_TREE_LEVEL_ONE,pids),"addr_id");
			}
			if(StringHelper.isEmpty(name)){
				list = tAddressDao.queryAllAddrByIds(null);
			}else{
				name = name.toLowerCase();
				name = name.replaceAll(" ", "");
				list=tAddressDao.queryAddrSysTreeByLvOneAndName(addrIds,name);
				
			}
		}
		
		List<TDistrict> districtList = tDistrictDao.findAll();
		Map<String,TDistrict> map = CollectionHelper.converToMapSingle(districtList, "district_id");
		for(TAddressSysDto dto:list){
			TDistrict t = map.get(dto.getDistrict_id());
			if(t != null && StringHelper.isNotEmpty(t.getDistrict_name())){
				dto.setDistrict_name(t.getDistrict_name());
			}
		}
		if(list.size()>2000){
			throw new ComponentException(ErrorCode.DataNumTooMuch);
		}
		return list;
	}
	
	public List<TProvince> queryProvince() throws Exception{
		return tProvinceDao.queryProvince();
	}
	
	public List queryDistrictByPid(String pId) throws Exception{
		if(StringHelper.isEmpty(pId)){
			throw new ComponentException(ErrorCode.ParamIsNull);
		}
		return tDistrictDao.queryDistrictListByPid(pId);
	}

	public List queryDistrictTree(String name, SOptr optr) throws Exception{
		List<DistrictSysDto> list = new ArrayList<DistrictSysDto>();
		if(StringHelper.isNotEmpty(name)){
			list = tDistrictDao.queryAllAddrByName(name);
		}else{
			list = tDistrictDao.queryAllDistrictTree();
		}
		if(list.size()>2000){
			throw new ComponentException(ErrorCode.DataNumTooMuch);
		}
		return list;
	}

	public void updateDistruct(TDistrict disDto) throws Exception{
		List<TDistrict> oldList = new ArrayList<TDistrict>();
		List<TDistrict> newList = new ArrayList<TDistrict>();
		if(StringHelper.isNotEmpty(disDto.getDistrict_id())){//修改
			TDistrict _t = tDistrictDao.findByKey(disDto.getDistrict_id());
			if(_t != null)
				oldList.add(_t);
			tDistrictDao.update(disDto);
		}else{//新增
			disDto.setDistrict_id(getNextAddrId());
			if(disDto.getDistrict_level() == 1){//新增的如果是省，就是本身的district_id
				disDto.setParent_id(disDto.getDistrict_id());
			}
			disDto.setStatus(StatusConstants.ACTIVE);
			disDto.setCreate_time(DateHelper.now());
			tDistrictDao.save(disDto);
//			newList.add(addr);
		}
		
		saveDistrictChanges(oldList, newList);
	}
	
	
	private void saveDistrictChanges(List<TDistrict> oldList, List<TDistrict> newList) throws ActionException{
		try{
			List<SSysChange> changes = new ArrayList<SSysChange>();
			
			String content;
			String optrId = WebOptr.getOptr().getOptr_id();
			Date createTime = new Date();
			Integer doneCode = getDoneCOde();
			String changeType = SysChangeType.DISTRICT.toString();
			String key ;
			String keyDesc;
			String changeDesc = "行政区域定义";
			if(CollectionHelper.isEmpty(oldList)){//新增
				for(TDistrict add:newList){
					key = add.getDistrict_id();
					keyDesc = add.getDistrict_name();
					content = BeanHelper.beanchange(null, add);
					SSysChange change = new SSysChange(changeType, doneCode, key, keyDesc, changeDesc, content, optrId, createTime);
					changes.add(change);
				}
			}else{
				TDistrict oldAdd = oldList.get(0);
				TDistrict newAdd = CollectionHelper.isNotEmpty(newList)?newList.get(0):null;
				
				key = oldAdd.getDistrict_id();
				keyDesc = newAdd!=null ? newAdd.getDistrict_name():oldAdd.getDistrict_name();
				content = BeanHelper.beanchange(oldAdd, newAdd);
				SSysChange change = new SSysChange(changeType, doneCode, key, keyDesc, changeDesc, content, optrId, createTime);
				changes.add(change);
			}
			
			getSSysChangeDao().save(changes.toArray(new SSysChange[changes.size()]));
		}catch (Exception e) {
			throw new ActionException(e.getMessage());
		}
	}
}
