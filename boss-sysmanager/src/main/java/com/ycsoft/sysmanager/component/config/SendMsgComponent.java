package com.ycsoft.sysmanager.component.config;


import static com.ycsoft.daos.helper.StringHelper.format;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ycsoft.beans.config.JSendMsg;
import com.ycsoft.beans.config.TAddress;
import com.ycsoft.beans.core.cust.CCust;
import com.ycsoft.beans.prod.PProd;
import com.ycsoft.beans.system.SBulletin;
import com.ycsoft.beans.system.SBulletinCounty;
import com.ycsoft.beans.system.SItemvalue;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.business.dao.config.JSendMsgDao;
import com.ycsoft.business.dao.config.TAddressDao;
import com.ycsoft.business.dao.core.cust.CCustDao;
import com.ycsoft.business.dao.prod.PProdDao;
import com.ycsoft.business.dao.system.SBulletinCountyDao;
import com.ycsoft.business.dao.system.SBulletinDao;
import com.ycsoft.business.dao.system.SCountyDao;
import com.ycsoft.business.dto.config.TAddressDto;
import com.ycsoft.business.dto.config.TSendMsgDto;
import com.ycsoft.commons.abstracts.BaseComponent;
import com.ycsoft.commons.constants.DataRight;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.constants.SequenceConstants;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.commons.helper.JsonHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;
import com.ycsoft.daos.helper.BeanHelper;
import com.ycsoft.sysmanager.dto.tree.TreeDto;
@Component
public class SendMsgComponent  extends  BaseComponent {
	private JSendMsgDao jSendMsgDao;
	private TAddressDao tAddressDao;
	private SBulletinDao sBulletinDao;
	private SBulletinCountyDao sBulletinCountyDao;
	private CCustDao cCustDao;
	private PProdDao pProdDao;
	private SCountyDao sCountyDao;
	
	public Pager<JSendMsg> queryMsg(Integer start , Integer limit ,String keyword,String countyId,String taskId)throws Exception{
		List<SItemvalue> list = new ArrayList<SItemvalue>();
		if(StringHelper.isNotEmpty(keyword)){
			list = sItemvalueDao.findValueByName(keyword.toUpperCase());
		}
		String county = "";
		if(!countyId.equals(SystemConstants.COUNTY_ALL)){
			county = countyId;
		}
		return jSendMsgDao.query(start, limit, list,county,taskId);
	}
	public List<TAddressDto> getAddrByName(String q,String countyId) throws Exception{
		return tAddressDao.getAddrByName(q, countyId);
	}
	
	public List<CCust> getUnitAll(String countyId) throws Exception {
		return cCustDao.getUnitAll(countyId); 
	}
	/**
	 * 根据任务编号JobId查询任务的详细信息
	 * @param JobId
	 * @return
	 * @throws Exception
	 */
	public TSendMsgDto queryMsgByJob(String JobId) throws Exception {
		TSendMsgDto dto  = jSendMsgDao.queryMsgByJob(JobId);
		if(StringHelper.isNotEmpty(dto.getAddress())){
			List<TAddress> list = tAddressDao.queryAddrByaddr(dto.getAddress().split(","));
			if(list.size()>0){
				String str ="";
				for(TAddress add :list){
					str+=add.getAddr_name()+",";
				}
				str = StringHelper.delEndChar(str,1);
				dto.setAddress_str(str);
			}
		}
		if(StringHelper.isNotEmpty(dto.getUnit())){
			List<CCust> list = cCustDao.queryCustByCustIds(dto.getUnit().split(","));
			if(list.size()>0){
				String str ="";
				for(CCust add :list){
					str+=add.getCust_name()+",";
				}
				str = StringHelper.delEndChar(str,1);
				dto.setUnit_str(str);
			}
		}
		if(StringHelper.isNotEmpty(dto.getProd_id())){
			List<PProd> list = pProdDao.findByProdIds(dto.getProd_id().split(","));
			if(list.size()>0){
				String str ="";
				for(PProd add :list){
					str+=add.getProd_name()+",";
				}
				str = StringHelper.delEndChar(str,1);
				dto.setProd_id_str(str);
			}
		}
		if(StringHelper.isNotEmpty(dto.getTerminal_type())){
			Map<String, SItemvalue> map = CollectionHelper.converToMapSingle(
					MemoryDict.getDicts(DictKey.TERMINAL_TYPE), "item_value");
			String[] Terminal = dto.getTerminal_type().split(",");
			String str ="";
			for(int i=0;i<Terminal.length;i++){
				SItemvalue value = map.get(Terminal[i].toString());
				if(StringHelper.isNotEmpty(value.getItem_name()))
					str+=value.getItem_name()+",";
			}
			str = StringHelper.delEndChar(str,1);
			dto.setTerminal_type_str(str);
		}
		if(StringHelper.isNotEmpty(dto.getHasten_stop_flag())){
			Map<String, SItemvalue> map = CollectionHelper.converToMapSingle(
					MemoryDict.getDicts(DictKey.STOP_TYPE), "item_value");
			String[] Hasten = dto.getHasten_stop_flag().split(",");
			String str ="";
			for(int i=0;i<Hasten.length;i++){
				SItemvalue value = map.get(Hasten[i].toString());
				if(StringHelper.isNotEmpty(value.getItem_name()))
					str+=value.getItem_name()+",";
			}		
			str = StringHelper.delEndChar(str,1);
			dto.setHasten_stop_flag_str(str);
		}
		if(StringHelper.isNotEmpty(dto.getCust_type())){
			Map<String, SItemvalue> map = CollectionHelper.converToMapSingle(
					MemoryDict.getDicts(DictKey.CUST_TYPE), "item_value");
			String[] CustType = dto.getCust_type().split(",");
			String str ="";
			for(int i=0;i<CustType.length;i++){
				SItemvalue value = map.get(CustType[i].toString());
				if(StringHelper.isNotEmpty(value.getItem_name()))
					str+=value.getItem_name()+",";
			}		
			str = StringHelper.delEndChar(str,1);
			dto.setCust_type_str(str);
		}
		if(StringHelper.isNotEmpty(dto.getCust_class())){
			Map<String, SItemvalue> map = CollectionHelper.converToMapSingle(
					MemoryDict.getDicts(DictKey.CUST_CLASS), "item_value");
			String[] CustClass = dto.getCust_class().split(",");
			String str ="";
			for(int i=0;i<CustClass.length;i++){
				SItemvalue value = map.get(CustClass[i].toString());
				if(StringHelper.isNotEmpty(value.getItem_name()))
					str+=value.getItem_name()+",";
			}		
			str = StringHelper.delEndChar(str,1);
			dto.setCust_class_str(str);
		}
		if(StringHelper.isNotEmpty(dto.getCust_colony())){
			Map<String, SItemvalue> map = CollectionHelper.converToMapSingle(
					MemoryDict.getDicts(DictKey.CUST_COLONY), "item_value");
			String[] CustColony = dto.getCust_colony().split(",");
			String str ="";
			for(int i=0;i<CustColony.length;i++){
				SItemvalue value = map.get(CustColony[i].toString());
				if(StringHelper.isNotEmpty(value.getItem_name()))
					str+=value.getItem_name()+",";
			}
			str = StringHelper.delEndChar(str,1);
			dto.setCust_colony_str(str);
		}
		return dto;
	}
	
	public void saveMsg(String addrRecord,String records,String countyId,String areaId, SOptr optr,JSendMsg sendMsg)
			throws Exception {
		List<TAddress> addrList =  new ArrayList<TAddress>();
		if(StringHelper.isNotEmpty(addrRecord)){
			Type type = new TypeToken<List<TAddress>>(){}.getType();
			Gson gson = new Gson();
			addrList = gson.fromJson(addrRecord,type);
		}
		List<JSendMsg> sendMsglist = new ArrayList<JSendMsg>();
		if(StringHelper.isNotEmpty(records)){
			Type type = new TypeToken<List<JSendMsg>>(){}.getType();
			sendMsglist = JsonHelper.gson.fromJson( records , type);
		}
		TAddress addr = null;
		List<TAddress> addrCounty =  new ArrayList<TAddress>();
		List<TAddress> addrDistrict=  new ArrayList<TAddress>();
		List<TAddress> addrSubDistrict =  new ArrayList<TAddress>();

		if(addrList.size()>0){
			for(TAddress dto:addrList){
				addr = new TAddress();
				if(dto.getTree_level().equals(SystemConstants.ADDRESS_LEVEL_COUNTY)){
					addr.setAddr_id(dto.getAddr_id());
					addrCounty.add(addr);
				}
				if(dto.getTree_level().equals(SystemConstants.ADDRESS_LEVEL_DISTRICT)){
					addr.setAddr_id(dto.getAddr_id());
					addrDistrict.add(addr);
				}
				if(dto.getTree_level().equals(SystemConstants.ADDRESS_LEVEL_SUBDISTRICT)){
					addr.setAddr_id(dto.getAddr_id());
					addrSubDistrict.add(addr);
				}
			}
		}
		String[] addrCountyArr = new String[addrCounty.size()];
		String[] addrDistrictArr = new String[addrDistrict.size()];
		String[] addrSubDistrictArr = new String[addrSubDistrict.size()];
		if(addrCounty.size()>0){
			for (int i = 0;i<addrCounty.size();i++){
				addrCountyArr[i] = addrCounty.get(i).getAddr_id();
			}
		}
		if(addrDistrict.size()>0){
			for (int i = 0;i<addrDistrict.size();i++){
				addrDistrictArr[i] = addrDistrict.get(i).getAddr_id();
			}
		}		
		if(addrSubDistrict.size()>0){
			for (int i = 0;i<addrSubDistrict.size();i++){
				addrSubDistrictArr[i] = addrSubDistrict.get(i).getAddr_id();
			}
		}
		
		String sql = " select cc.cust_id cust_id,cu.user_id user_id,cu.acct_id acct_id,cc.cust_name cust_name,cu.stb_id stb_id,cu.card_id card_id," +
				" cu.modem_mac modem_mac,cu.county_id county_id,cu.area_id area_id  " +
				"from  busi.c_user cu, busi.c_cust cc where cc.cust_id = cu.cust_id  and cc.county_id=cc.county_id " +
				" and cu.user_type ='"+SystemConstants.USER_TYPE_DTT+"' and cc.county_id ='"+countyId+"' ";
		
		String addSql = "";
		
		if(StringHelper.isNotEmpty(sendMsg.getUnit())){
			addSql += " and cc.cust_id in (select a.resident_cust_id from busi.c_cust_unit_to_resident a where a.unit_cust_id in ('"+sendMsg.getUnit().replaceAll(",", "','")+"'))";
		}
		if(StringHelper.isNotEmpty(sendMsg.getCust_type())){
			addSql += " and cc.cust_type in ('"+sendMsg.getCust_type().replaceAll(",", "','")+"')";
		}
		if(StringHelper.isNotEmpty(sendMsg.getCust_colony())){
			addSql += " and cc.cust_colony in ('"+sendMsg.getCust_colony().replaceAll(",", "','")+"')";
		}
		if(StringHelper.isNotEmpty(sendMsg.getCust_class())){
			addSql += " and cc.cust_class in ('"+sendMsg.getCust_class().replaceAll(",", "','")+"')";
		}
		if(StringHelper.isNotEmpty(sendMsg.getTerminal_type())){
			addSql += "  and cu.user_id in ( select user_id from busi.c_user_dtv where terminal_type in ('"+sendMsg.getTerminal_type().replaceAll(",", "','")+"'))";
		}
		if(addrCountyArr.length>0){
			addSql += " and cc.address in (select t.addr_id from busi.t_address t where t.addr_pid in (select t1.addr_id from busi.t_address t1 where t1.addr_pid in ("+in(addrCountyArr)+")) ";
		}
		if(addrDistrictArr.length>0){
			addSql += " and cc.address in (select t.addr_id from busi.t_address t where t.addr_pid in  ("+in(addrDistrictArr)+")) ";
		}
		if(addrSubDistrictArr.length>0){
			addSql += " and cc.address in ("+in(addrSubDistrictArr)+")";
		}
		sql += addSql;
		sql = thisReplaceAll(sql, "'", "''");
		
		List<JSendMsg> msgList = new ArrayList<JSendMsg>();
		JSendMsg msg = null;
		sendMsg.setArea_id(areaId);
		sendMsg.setCounty_id(countyId);
		sendMsg.setOptr_id(optr.getOptr_id());
		sendMsg.setCreate_time(new Date());
		sendMsg.setSql(sql);
		sendMsg.setStatus(StatusConstants.ACTIVE);
		sendMsg.setAud_status(SystemConstants.JOB_Y);
		sendMsg.setDept_id(optr.getDept_id());
		if(StringHelper.isEmpty(sendMsg.getProd_id())&&sendMsg.getTask_code().equals(SystemConstants.TASK_CODE_CJ)){
			//TODO 待确认,
//			List<PProd> prodList =  pProdDao.getBaseProd(areaId);原来的写法
			List<PProd> prodList =  pProdDao.getBaseProd(countyId);
			if(prodList.size()>0){
				String[] prodArr = new String[prodList.size()];
				for (int i = 0;i<prodList.size();i++){
					prodArr[i] = prodList.get(i).getProd_id();
					
				}
				sendMsg.setProd_id(StringUtils.join(prodArr,","));
			}
		}
			//停机
		if (sendMsglist.size() > 0) {
			for (JSendMsg dto : sendMsglist) {
				msg =new JSendMsg();
				BeanUtils.copyProperties(sendMsg, msg);
				msg.setExec_time(dto.getExec_time());
				msg.setLimit_user_cnt(dto.getLimit_user_cnt());
				msg.setJob_id(getJobId());
				msg.setDone_code(gDoneCode());
				msgList.add(msg);
			}
			jSendMsgDao.save(msgList.toArray(new JSendMsg[msgList.size()]));
		}else{
			//催缴
			sendMsg.setJob_id(getJobId());
			sendMsg.setDone_code(gDoneCode());
			jSendMsgDao.save(sendMsg);
		}
	}
	
	public String in(Object[] value) {
		String target = StringUtils.EMPTY;
		if(value == null ) return target;
		for (Object o : value) {
			 target += format("{0},", o) ;
		}
		return StringHelper.delEndChar( target ,  1 );
	}
	public static String thisReplaceAll(String source, String sign1, String sign2) {
		String order = "";
		while (source.indexOf(sign1) != -1) {
			order = order + source.substring(0, source.indexOf(sign1)) + sign2;
			source = source.substring(source.indexOf(sign1) + sign1.length());
		}
		order = order + source;
		return order;
	}
	public boolean deleteMsg(String job) throws Exception{
		return jSendMsgDao.invalidMsg(job)>0;
	}
	
	
	private Integer gDoneCode() throws JDBCException {
		return jSendMsgDao.findSequence(SequenceConstants.SEQ_DONE_CODE).intValue();
	}
	private int getJobId() throws Exception{
		return jSendMsgDao.findSequence(SequenceConstants.SEQ_JOB_ID).intValue();
	}
	
	/* ----------------------  公告     ------------------------------------------  */
	
	public Pager<SBulletin> queryBulletin(Integer start , Integer limit ,String keyword,String countyId)throws Exception{
			String county ="";
			if(!countyId.equals(SystemConstants.COUNTY_ALL)){
				county = countyId; 
			}
			return sBulletinDao.query(start, limit, keyword,county);
			
	}
	
	/**
	 * 保存公告.
	 * @param dto
	 * @param optr
	 * @param deptIds 使用部门.
	 * @return
	 * @throws Exception
	 */
	public boolean saveBulletin(SBulletin dto, SOptr optr,String deptIds) throws Exception{
		int sues =0;
		if(StringHelper.isNotEmpty(dto.getBulletin_id())){
			sues = sBulletinDao.doUpdate(dto);
			sBulletinCountyDao.delBullCountyById(dto.getBulletin_id());
		}
		else{
			dto.setBulletin_id(sBulletinDao.findSequence().toString());
			dto.setStatus(StatusConstants.ACTIVE);
			dto.setOptr_id(optr.getOptr_id());
			dto.setCreate_date(new Date());
			sues = sBulletinDao.doSave(dto);
		}
		//保存公告对应的分公司
		if(StringHelper.isNotEmpty(deptIds)){
			String[] deptArray = deptIds.split(",");
			for(int i=0;i<deptArray.length;i++){
				SBulletinCounty  sBulletinCounty = new SBulletinCounty();
				sBulletinCounty.setBulletin_id(dto.getBulletin_id());
//				sBulletinCounty.setCounty_id(deptArray[i]);
				sBulletinCounty.setDept_id(deptArray[i]);
				sBulletinCountyDao.save(sBulletinCounty);
			}
		}
		if(sues >= 0 || sues == -2){
			return true;
		}
		return true;
	}
	
	
	public boolean changeBulletin(String bulletinId,String statusId) throws Exception{
		String status = StatusConstants.ACTIVE;
		if(statusId.equals(StatusConstants.ACTIVE)){
			status = StatusConstants.INVALID;
		}
		if(statusId.equals(StatusConstants.INVALID)){
			status = StatusConstants.ACTIVE;
		}
		return sBulletinDao.updateBulletin(bulletinId,status)>0;
		
	}
	
	/**
	 *公告应用地区树 
	 *type 格式String[] type = {"AREA","TARIFF"};type[0]有2种值：AREA,COUNTY与传入areaId值对应;
	 */
	public List<TreeDto> getCountyTree(SOptr optr,String[] type,String value)throws Exception{
		List<TreeDto> countys = null;
		List<TreeDto> selectedList = null;
		
		String dataRight = this.queryDataRightCon(optr, DataRight.CHANGE_COUNTY.toString());
		countys = sCountyDao.getCountyTreeByDataRight(dataRight);
		
		
		if (StringHelper.isNotEmpty(value)) {
			selectedList = sBulletinCountyDao.getBullCountyById(value);
		}
			for (int i = 0; i < countys.size(); i++) {
				if(type[0].toString().equals("CHOOSE")){//去掉湖北省
					if(countys.get(i).getId().equals(SystemConstants.COUNTY_ALL)||countys.get(i).getId().equals(SystemConstants.AREA_ALL)){
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
	
	/**
	 *公告应用部门树 
	 *type 格式String[] type = {"AREA","TARIFF"};type[0]有2种值：AREA,COUNTY与传入areaId值对应;
	 */
	public List<TreeDto> getDeptTree(SOptr optr,String[] type,String value)throws Exception{
		List<TreeDto> depts = null;
		List<TreeDto> selectedList = null;
		
		String dataRight = this.queryDataRightCon(optr, DataRight.CHANGE_COUNTY.toString());
		depts = sCountyDao.getDeptTreeByDataRight(dataRight);
		List<TreeDto> added = new ArrayList<TreeDto>();
		for(TreeDto tree:depts){
			String id = tree.getId();
			int indexOf = id.indexOf("-1");
			if(!tree.isLeaf() && indexOf > 0 ){
				TreeDto newTree = new TreeDto();
				BeanHelper.copyProperties(newTree, tree);
				newTree.setId(id.substring(0,indexOf));
				newTree.setPid(id);
				added.add(newTree);
			}
		}
		depts.addAll(added);
		
		if (StringHelper.isNotEmpty(value)) {
			selectedList = sBulletinCountyDao.getBullCountyById(value);
		}
			for (int i = 0; i < depts.size(); i++) {
				TreeDto treeDto = depts.get(i);
				if(type[0].toString().equals("CHOOSE")){//去掉湖北省
					if(treeDto.getId().equals(SystemConstants.COUNTY_ALL)||treeDto.getId().equals(SystemConstants.AREA_ALL)){
						depts.remove(i);
						continue;
					}
				}
				treeDto.setChecked(false);
				if (selectedList != null) {
					for (int j = 0; j < selectedList.size(); j++) {
						if(treeDto.getId().equals(selectedList.get(j).getId())){
							treeDto.setChecked(true);
						}
					}
				}
			}
		
		return depts;
	}
	public JSendMsgDao getJSendMsgDao() {
		return jSendMsgDao;
	}
	public void setJSendMsgDao(JSendMsgDao sendMsgDao) {
		jSendMsgDao = sendMsgDao;
	}

	public void setSBulletinDao(SBulletinDao bulletinDao) {
		sBulletinDao = bulletinDao;
	}
	public void setTAddressDao(TAddressDao addressDao) {
		this.tAddressDao = addressDao;
	}
	public void setCCustDao(CCustDao custDao) {
		cCustDao = custDao;
	}
	public PProdDao getPProdDao() {
		return pProdDao;
	}

	public void setPProdDao(PProdDao prodDao) {
		pProdDao = prodDao;
	}
	public void setSCountyDao(SCountyDao countyDao) {
		sCountyDao = countyDao;
	}
	public SBulletinCountyDao getSBulletinCountyDao() {
		return sBulletinCountyDao;
	}
	public void setSBulletinCountyDao(SBulletinCountyDao bulletinCountyDao) {
		sBulletinCountyDao = bulletinCountyDao;
	}
}