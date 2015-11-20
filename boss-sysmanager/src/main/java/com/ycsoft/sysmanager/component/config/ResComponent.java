package com.ycsoft.sysmanager.component.config;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.bill.BCreditAddressStop;
import com.ycsoft.beans.config.TServer;
import com.ycsoft.beans.config.TServerCounty;
import com.ycsoft.beans.config.TServerRes;
import com.ycsoft.beans.prod.PRes;
import com.ycsoft.beans.prod.PResgroup;
import com.ycsoft.beans.prod.PResgroupRes;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.business.dao.bill.BCreditAddressStopDao;
import com.ycsoft.business.dao.config.TServerCountyDao;
import com.ycsoft.business.dao.config.TServerDao;
import com.ycsoft.business.dao.prod.PResDao;
import com.ycsoft.business.dao.prod.PResgroupDao;
import com.ycsoft.business.dao.prod.PResgroupResDao;
import com.ycsoft.business.dao.prod.TServerResDao;
import com.ycsoft.commons.abstracts.BaseComponent;
import com.ycsoft.commons.constants.BusiCmdConstants;
import com.ycsoft.commons.constants.FuncCode;
import com.ycsoft.commons.constants.StatusConstants;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ComponentException;
import com.ycsoft.commons.helper.JsonHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;
import com.ycsoft.sysmanager.dto.prod.ResDto;
import com.ycsoft.sysmanager.dto.prod.ResGroupDto;

@Component
public class ResComponent extends BaseComponent {
	private PResgroupDao pResgroupDao;
	private PResDao pResDao;
	private PResgroupResDao pResgroupResDao;
	private TServerResDao tServerResDao;
	private TServerDao tServerDao;
	private TServerCountyDao tServerCountyDao;
	private BCreditAddressStopDao bCreditAddressStopDao;
	
	public List<TServer> queryServerByServType(String servType) throws Exception {
		if(SystemConstants.PROD_SERV_ID_OTTMOBILE.endsWith(servType)){
			servType=SystemConstants.PROD_SERV_ID_OTT;
		}
		return tServerResDao.queryServerByServType(servType);
	}
	
	public List<TServer> queryServerByCountyId(String countyId) throws Exception {
		return tServerResDao.queryServerByCountyId(countyId);
	}
	
	public List<PRes> queryAllRes() throws Exception {
		return pResDao.findAll();
	}
	
	public Pager<TServerRes> queryServerRes(String resId, String countyId,String keyword, Integer start, Integer limit) throws Exception {
		return tServerResDao.queryServerRes(resId, keyword, countyId, start, limit);
	}
	
	public Pager<ResDto> queryRes(String servId,String keyword, String countyId, Integer start, Integer limit) throws Exception {
		return pResDao.queryRes(servId, keyword, countyId, start, limit);
	}
	/**
	 * 
	 * @param res 编辑后的新记录
	 * @param oldRes 编辑前的记录
	 * @throws Exception
	 */
	public void saveServerRes(TServerRes newRes,TServerRes oldRes) throws Exception {
		boolean flag = true;
		if(null != oldRes){
			if(newRes.getServer_id().equals(oldRes.getServer_id())){
				flag = false;
			}
			newRes.setArea_id(oldRes.getArea_id());
			newRes.setCounty_id(oldRes.getCounty_id());
			this.deleteServerRes(oldRes);
		}
		if(flag){
			TServerRes old = tServerResDao.queryServerRes(newRes.getBoss_res_id(), newRes.getServer_id());
			if(old != null){
				throw new ComponentException("同个服务器，不能对应相同的boss资源");
			}
		}
		tServerResDao.save(newRes);
	}
	
	public void deleteServerRes(TServerRes res) throws Exception {
		tServerResDao.deleteServerRes(res);
	}
	
	public void updateRes(PRes pres) throws Exception {
		pResDao.update(pres);
		pres = pResDao.findByKey(pres.getRes_id());
		//serv_is为ITV时，RES_TYPE有对应的点播类型，此时才同步数据
		if(StringHelper.isNotEmpty(pres.getRes_type())){
			saveDataSyncJob(BusiCmdConstants.CHANGE_RES,JsonHelper.fromObject(pres),"P_RES");
		}
	}
	
	public String getResByResName(String resName) throws Exception {
		PRes pres = pResDao.getResByResName(resName);
		if(pres == null){
			return "";
		}
		return pres.getRes_name();
	}
	
	public String saveRes(PRes pres) throws Exception {
		pres.setRes_id(pResDao.findSequence().toString());
		pres.setStatus(StatusConstants.ACTIVE);
		pres.setCreate_time(new Date());
		pResDao.save(pres);
		
		if(StringHelper.isNotEmpty(pres.getRes_type())){
			saveDataSyncJob(BusiCmdConstants.CREAT_RES,JsonHelper.fromObject(pres),"P_RES");
		}
		return pres.getRes_id();
	}
	
	/**
	 * 禁用资源
	 * @param resId
	 * @throws Exception
	 */
	public void deleteRes(String resId) throws Exception {
		PRes res = pResDao.findByKey(resId);
		if(StringHelper.isNotEmpty(res.getRes_type())){
			saveDataSyncJob(BusiCmdConstants.DELETE_RES,JsonHelper.fromObject(res),"P_RES");
		}
		res.setStatus(StatusConstants.INVALID);
		pResDao.update(res);
	}
	
	/**
	 * 启用资源
	 * @param resId
	 * @throws Exception 
	 */
	public void activeRes(String resId) throws Exception {
		PRes res = pResDao.findByKey(resId);
		if(StringHelper.isNotEmpty(res.getRes_type())){
			saveDataSyncJob(BusiCmdConstants.CREAT_RES,JsonHelper.fromObject(res),"P_RES");
		}
		res.setStatus(StatusConstants.ACTIVE);
		pResDao.update(res);
	}

	public Pager<ResGroupDto> queryAllRes(Integer start, Integer limit,
			String keyword, String countyId) throws Exception {
		return pResgroupDao.query(keyword, countyId,start, limit);
	}
	
	public String queryResById(String groupId) throws Exception {
			List<PRes> list = pResDao.queryResById(groupId);
			String str ="";
			if(list.size()>0){
				for(PRes add :list){
					str+=add.getRes_name()+",";
				}
				str = StringHelper.delEndChar(str,1);
		}
		return str; 
	}
	
	/**
	 * 删除资源组
	 * @param groupId
	 * @throws JDBCException 
	 */
	public void delteResGroup(String groupId,SOptr optr) throws Exception {
		ResGroupDto groupDto = pResgroupDao.queryResGroupById(groupId);
		if(null != groupDto){
			throw new ComponentException("该资源组正在被产品"+groupDto.getProd_name()+"使用,不能删除");
		}else{
			PResgroup group = pResgroupDao.findByKey(groupId);
			
			pResgroupDao.remove(groupId);
			
			//保存操作记录
			saveOperateLog(FuncCode.DELETE_RESGROUP.toString(), groupId, group.getGroup_name(), optr);
		}
	}

	public List<PRes> getResByServId(String servId,String countyId) throws Exception {
		return pResDao.queryResByServId(servId,countyId);
	}

	public List<PResgroupRes> queryResByGroupId(String groupId)
			throws Exception {
		return pResgroupResDao.queryResByGroupId(groupId);
	}

	public void saveRes(PResgroup res, String records,SOptr optr) throws Exception {
		String[] resList = records.split(",");
		List<PResgroupRes> groupResList = new ArrayList<PResgroupRes>();
		PResgroupRes groupres = null;
		res.setGroup_id(pResgroupDao.findSequence().toString());
		for (String d : resList) {
			groupres = new PResgroupRes();
			groupres.setRes_id(d);
			groupres.setGroup_id(res.getGroup_id());
			groupResList.add(groupres);
		}
		pResgroupResDao.save(groupResList.toArray(new PResgroupRes[groupResList.size()]));
		pResgroupDao.save(res);
		
		//保存操作记录
		saveOperateLog(FuncCode.CREATE_RESGROUP.toString(), res.getGroup_id(),res.getGroup_name(), optr);
	}

	public void updateRes(PResgroup res, String records,SOptr optr) throws Exception {
		String[] resList = records.split(",");
		List<PResgroupRes> groupResList = new ArrayList<PResgroupRes>();
		PResgroupRes groupres = null;
		for (String d : resList) {
			groupres = new PResgroupRes();
			groupres.setRes_id(d);
			groupres.setGroup_id(res.getGroup_id());
			groupResList.add(groupres);
		}
		pResgroupResDao.deleteRes(res.getGroup_id());
		pResgroupResDao.save(groupResList.toArray(new PResgroupRes[groupResList.size()]));
		pResgroupDao.update(res);
		
		//保存操作记录
		saveOperateLog(FuncCode.MODIFY_RESGROUP.toString(), res.getGroup_id(),res.getGroup_name(), optr);
	}

	/*---------------------------------------------服务器--------------------*/
	/**
	 * 查询服务器
	 * @param countyId
	 * @return
	 * @throws Exception
	 */
	public List<TServer> queryServer(String countyId)throws Exception{
		return tServerDao.query(countyId);
	}
	
	
	/**
	 * 保存修改服务器配置
	 * @param dto
	 * @param optr
	 * @param countyIds
	 * @throws Exception
	 */
	public void saveServer(TServer dto, SOptr optr,String countyIds) throws Exception{
		TServer server = tServerDao.queryById(dto.getServer_id());
		if(server!=null){
			tServerDao.update(dto);
			tServerCountyDao.delCountyById(dto.getServer_id());
		}
		else{
//			TServer server = new TServer();
//			BeanUtils.copyProperties(dto, server);
			tServerDao.save(dto);
		}
		//保存服务器对应的分公司
		if(StringHelper.isNotEmpty(countyIds)){
			String[] countyIdList = countyIds.split(",");
			for(int i=0;i<countyIdList.length;i++){
				TServerCounty  serverCounty = new TServerCounty();
				serverCounty.setServer_id(dto.getServer_id());
				serverCounty.setCounty_id(countyIdList[i]);
				tServerCountyDao.save(serverCounty);
			}
		}
	}
	
	public List<BCreditAddressStop> queryStopCount() throws Exception{
		return bCreditAddressStopDao.queryAll();
	}
	
	public void saveStopCount(List<BCreditAddressStop> changeValueList)throws Exception {
		for (BCreditAddressStop bas : changeValueList) {
			if(null == bCreditAddressStopDao.findByKey(bas.getAddr_id())){
				bCreditAddressStopDao.saveBCreditAddressStop(bas);
			}else{
				bCreditAddressStopDao.updateBCreditAddressStop(bas);
			}
		}
	}
	
	public void setPResgroupDao(PResgroupDao resgroupDao) {
		this.pResgroupDao = resgroupDao;
	}

	public PResgroupDao getPResgroupDao() {
		return pResgroupDao;
	}

	public PResDao getPResDao() {
		return pResDao;
	}

	public void setPResDao(PResDao resDao) {
		this.pResDao = resDao;
	}

	public PResgroupResDao getPResgroupResDao() {
		return pResgroupResDao;
	}

	public void setPResgroupResDao(PResgroupResDao resgroupResDao) {
		this.pResgroupResDao = resgroupResDao;
	}
	public void setTServerResDao(TServerResDao serverResDao) {
		tServerResDao = serverResDao;
	}
	public void setTServerDao(TServerDao serverDao) {
		tServerDao = serverDao;
	}
	public void setTServerCountyDao(TServerCountyDao serverCountyDao) {
		tServerCountyDao = serverCountyDao;
	}

	public void setBCreditAddressStopDao(BCreditAddressStopDao bCreditAddressStopDao) {
		this.bCreditAddressStopDao = bCreditAddressStopDao;
	}

}
