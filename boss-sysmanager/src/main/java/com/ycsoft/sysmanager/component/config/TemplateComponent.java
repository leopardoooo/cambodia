package com.ycsoft.sysmanager.component.config;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TBusiCode;
import com.ycsoft.beans.config.TBusiCodeDoc;
import com.ycsoft.beans.config.TBusiDoc;
import com.ycsoft.beans.config.TBusiFee;
import com.ycsoft.beans.config.TBusiFeeDevice;
import com.ycsoft.beans.config.TBusiFeeStd;
import com.ycsoft.beans.config.TConfigTemplate;
import com.ycsoft.beans.config.TInvoicePrintitem;
import com.ycsoft.beans.config.TOpenTemp;
import com.ycsoft.beans.config.TPrintitem;
import com.ycsoft.beans.config.TProdStatusRent;
import com.ycsoft.beans.config.TStbFilled;
import com.ycsoft.beans.config.TTemplate;
import com.ycsoft.beans.config.TTemplateColumn;
import com.ycsoft.beans.config.TTemplateColumnOptr;
import com.ycsoft.beans.config.TTemplateCounty;
import com.ycsoft.beans.config.TTemplateExcludeCounty;
import com.ycsoft.beans.config.TTemplateFeeStd;
import com.ycsoft.beans.config.TUpdateCfg;
import com.ycsoft.beans.prod.PRes;
import com.ycsoft.beans.system.SItemvalue;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.beans.system.SSysChange;
import com.ycsoft.beans.task.TBusiCodeTask;
import com.ycsoft.beans.task.TTaskDetailType;
import com.ycsoft.beans.task.TTaskTemplatefile;
import com.ycsoft.business.dao.config.TBusiCodeDao;
import com.ycsoft.business.dao.config.TBusiCodeDocDao;
import com.ycsoft.business.dao.config.TBusiCodeFeeDao;
import com.ycsoft.business.dao.config.TBusiCodeTaskDao;
import com.ycsoft.business.dao.config.TBusiDocDao;
import com.ycsoft.business.dao.config.TBusiFeeDao;
import com.ycsoft.business.dao.config.TBusiFeeDeviceDao;
import com.ycsoft.business.dao.config.TBusiFeeStdDao;
import com.ycsoft.business.dao.config.TConfigDao;
import com.ycsoft.business.dao.config.TConfigTemplateDao;
import com.ycsoft.business.dao.config.TInvoicePrintitemDao;
import com.ycsoft.business.dao.config.TOpenTempDao;
import com.ycsoft.business.dao.config.TPrintitemDao;
import com.ycsoft.business.dao.config.TProdStatusRentDao;
import com.ycsoft.business.dao.config.TStbFilledDao;
import com.ycsoft.business.dao.config.TTemplateColumnDao;
import com.ycsoft.business.dao.config.TTemplateColumnOptrDao;
import com.ycsoft.business.dao.config.TTemplateCountyDao;
import com.ycsoft.business.dao.config.TTemplateDao;
import com.ycsoft.business.dao.config.TTemplateExcludeCountyDao;
import com.ycsoft.business.dao.config.TTemplateFeeStdDao;
import com.ycsoft.business.dao.config.TUpdateCfgDao;
import com.ycsoft.business.dao.prod.PResDao;
import com.ycsoft.business.dao.system.SCountyDao;
import com.ycsoft.business.dao.system.SSysChangeDao;
import com.ycsoft.business.dao.task.TTaskDetailTypeDao;
import com.ycsoft.business.dao.task.TTaskTemplatefileDao;
import com.ycsoft.business.dto.config.TemplateConfigDto;
import com.ycsoft.business.dto.config.TemplateFeeDto;
import com.ycsoft.business.dto.config.TemplateUseDto;
import com.ycsoft.commons.abstracts.BaseComponent;
import com.ycsoft.commons.constants.DataRight;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.constants.FuncCode;
import com.ycsoft.commons.constants.SysChangeType;
import com.ycsoft.commons.constants.SystemConstants;
import com.ycsoft.commons.exception.ComponentException;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.helper.BeanHelper;
import com.ycsoft.sysmanager.dto.tree.TreeDto;
import com.ycsoft.sysmanager.web.commons.interceptor.WebOptr;
/**
 * @Description: 模板组件
 * @author  wqy
 * @date Apr 1, 2010 10:07:14 AM
 */

@Component
public class TemplateComponent  extends  BaseComponent {
	private TTemplateDao tTemplateDao;
	private TConfigDao tConfigDao;
	private TTemplateCountyDao tTemplateCountyDao;
	private TBusiCodeDocDao tBusiCodeDocDao;
	private TBusiCodeFeeDao tBusiCodeFeeDao;
	private TBusiCodeTaskDao tBusiCodeTaskDao;
	private TBusiCodeDao tBusiCodeDao;
	private TBusiDocDao tBusiDocDao;
	private TBusiFeeDao	tBusiFeeDao;
	private TTaskDetailTypeDao tTaskDetailTypeDao;
	private TPrintitemDao tPrintitemDao;
	private TInvoicePrintitemDao tInvoicePrintitemDao;
	private SCountyDao sCountyDao ;
	private TProdStatusRentDao tProdStatusRentDao;
	private TUpdateCfgDao tUpdateCfgDao;
	private TConfigTemplateDao tConfigTemplateDao;
	private TOpenTempDao tOpenTempDao;
	private PResDao pResDao;
	private TStbFilledDao tStbFilledDao;
	private TBusiFeeStdDao tBusiFeeStdDao;
	private TBusiFeeDeviceDao tBusiFeeDeviceDao;
	private SSysChangeDao sSysChangeDao;
	private TTemplateColumnDao tTemplateColumnDao;
	private TTemplateColumnOptrDao tTemplateColumnOptrDao;
	private TTemplateFeeStdDao tTemplateFeeStdDao;
	private TTemplateExcludeCountyDao tTemplateExcludeCountyDao;
	private TTaskTemplatefileDao tTaskTemplatefileDao;
	
	public String queryChangeCounty() throws Exception {
		return this.queryDataRightCon(WebOptr.getOptr(), DataRight.CHANGE_COUNTY.toString());
	}
	/**
	 * 返回模板菜单树
	 * @param optr 
	 * @return
	 * @throws Exception 
	 * @throws ComponentException 
	 */
	public List<TreeDto> queryTemplateTree() throws Exception{
		String dataRight = queryChangeCounty();
		if(WebOptr.getOptr().getCounty_id().equals(SystemConstants.COUNTY_ALL)){
			dataRight = " 1=1 ";
		}
		return tTemplateDao.queryTemplateTree(dataRight);
	}
	
	public List<TreeDto> queryFeeTemplateTree(SOptr optr) throws Exception{
		String dataRight = this.queryDataRightCon(optr, DataRight.CHANGE_COUNTY.toString());
		if(!optr.getCounty_id().equals(optr.getOld_county_id())){
			dataRight = " county_id='"+optr.getCounty_id()+"'";
		}
		return tTemplateDao.queryFeeTemplateTree(optr.getCounty_id(),dataRight,optr.getOptr_id());
	}

	/**
	* @Description:初始化业务
	* @return
	* @throws Exception
	* @return List<TBusiCode>
	 */
	public List<TBusiCode> querybusi()throws Exception {
		return tBusiCodeDao.findAll();
	}

/*****************************业务费用****************************/
	/**
	 * 查询费用标准模板数据
	 * @param templateId
	 * @return
	 * @throws JDBCException
	 */
	public TemplateUseDto queryByTemplateId(String templateId) throws Exception{
		List<TBusiFeeStd> feeStdList = this.tBusiFeeStdDao.queryByTemplateId(templateId);
		
		Map<String, List<TBusiFeeDevice>> map = CollectionHelper.converToMap(tBusiFeeDeviceDao.queryModelByStdId(), "fee_std_id");
		
		for (TBusiFeeStd std :feeStdList){
			if (StringHelper.isNotEmpty(std.getDevice_type())){
				List<TBusiFeeDevice> list = map.get(std.getFee_std_id());
				std.setDeviceModelList(CollectionHelper.converValueToList(list, "device_model"));
				String itemKey="";
				String deviceModels="";
				String deviceModelTexts = "";
				if (std.getDevice_type().equals(SystemConstants.DEVICE_TYPE_STB))
					itemKey=DictKey.STB_MODEL.toString();
				else if (std.getDevice_type().equals(SystemConstants.DEVICE_TYPE_CARD))
					itemKey=DictKey.CARD_MODEL.toString();
				else if (std.getDevice_type().equals(SystemConstants.DEVICE_TYPE_MODEM))
					itemKey=DictKey.MODEM_MODEL.toString();
				else{ 
					
				}
				
				for (TBusiFeeDevice model:list){
					deviceModels += model.getDevice_model() + ",";
					if(StringHelper.isNotEmpty(itemKey)){
						deviceModelTexts += MemoryDict.getDictName(itemKey, model.getDevice_model())+",";
					}else{
						deviceModelTexts += model.getModel_name()+",";
					}
				}
				if (deviceModels.length()>1)
					deviceModels.substring(0,deviceModels.length()-1);
				std.setDevice_model_text(deviceModelTexts);
				std.setDevice_model(deviceModels);
				
			}
		}
		TemplateUseDto useDto = new TemplateUseDto();
		useDto.setFeeStbList(feeStdList);
		
		TTemplateExcludeCounty exculde = tTemplateExcludeCountyDao.query(templateId, WebOptr.getOptr().getCounty_id());
		if(exculde != null){
			useDto.setIs_include(exculde.getIs_include());
		}else{
			useDto.setIs_include(SystemConstants.BOOLEAN_TRUE);
		}
		
		List<TTemplateColumn> columnList = tTemplateColumnDao.queryColumnByOptrId(
				WebOptr.getOptr().getOptr_id(), templateId, "FEE");
		
		if(useDto.getIs_include().equals(SystemConstants.BOOLEAN_FALSE)){
			for(TTemplateColumn column : columnList){
				if(column.getType().equals("number")){
					column.setMin_value(Integer.MIN_VALUE);
					column.setMax_value(Integer.MAX_VALUE);
				}
			}
		}
		useDto.setColumnList(columnList);
		
		return useDto;
	}
	
	public List<TemplateFeeDto> queryFeeTemplateColumn(String templateId, String key) throws Exception {
		 return tTemplateColumnDao.queryFeeTemplateColumn(templateId, key);
	}
	
	public void updateColumn(List<TTemplateColumn> columnList) throws Exception {
		tTemplateColumnDao.update(columnList.toArray(new TTemplateColumn[columnList.size()]));
	}
	
	/**
	 * 查找可以配置费用标准的费用，过滤掉已经配置了费用标准的业务费用
	 * @param templateId
	 * @return
	 * @throws Exception
	 */
	public List<TBusiFee> queryBusiFeeForStdCfg(String templateId, String countyIds)throws Exception{
		return this.tBusiFeeStdDao.queryBusiFeeForStdCfg(templateId, countyIds);
	}
	
	/**
	 * 查找费用标准可以选择的设备型号
	 * @param templateId
	 * @param deviceBuyMode
	 * @param deviceType
	 * @return
	 * @throws Exception
	 */
	public List<SItemvalue> qeuryDeviceModelForStdCfg(String templateId,String feeStdId,String deviceBuyMode,String deviceType,String feeId,String[] countyId)throws Exception{
		return this.tBusiFeeStdDao.qeuryDeviceModelForStdCfg(templateId, feeStdId,deviceBuyMode, deviceType,feeId,countyId);
	}

	/**
	 * 保存或修改一条收费标准
	 * @param entity
	 * @throws Exception
	 */
	public void saveFeeConfig(TBusiFeeStd busiFeeStd,String deviceModelListStr) throws Exception{
		//金额以分为单位
		if(StringHelper.isNotEmpty(busiFeeStd.getFee_std_id())){//修改
			tBusiFeeStdDao.update(busiFeeStd);
			if(StringHelper.isNotEmpty(deviceModelListStr)){
				tBusiFeeDeviceDao.deleteFeeById(busiFeeStd.getFee_std_id());
			}
			
			tTemplateColumnDao.updateColumn(busiFeeStd, WebOptr.getOptr().getOptr_id());
		}else{//保存
			busiFeeStd.setFee_std_id(tBusiFeeStdDao.getBusiFeeStdID());
			busiFeeStd.setOptr_id(WebOptr.getOptr().getOptr_id());
			tBusiFeeStdDao.save(busiFeeStd);
			
			this.saveFeeColumn(busiFeeStd, deviceModelListStr);
		}
		
		//设备购买方式、型号对应费用
		if(StringHelper.isNotEmpty(deviceModelListStr)){
			String[] deviceModelList = deviceModelListStr.split(",");
			List<TBusiFeeDevice> busiFeeList = new ArrayList<TBusiFeeDevice>();
			for(int i=0;i<deviceModelList.length;i++){
				TBusiFeeDevice entity = new TBusiFeeDevice();
				entity.setFee_std_id(busiFeeStd.getFee_std_id());
				entity.setDevice_buy_mode(busiFeeStd.getDevice_buy_mode());
				entity.setDevice_type(busiFeeStd.getDevice_type());
				entity.setDevice_model(deviceModelList[i]);
				busiFeeList.add(entity);
			}
			tBusiFeeDeviceDao.save(busiFeeList.toArray(new TBusiFeeDevice[busiFeeList.size()]));
		}
		
	}
	
	private void saveFeeColumn(TBusiFeeStd busiFeeStd, String deviceModelListStr) throws Exception {
		List<TTemplateColumn> columnList = new ArrayList<TTemplateColumn>();
		List<TTemplateFeeStd> stdList = new ArrayList<TTemplateFeeStd>();
		
		TTemplateColumn column = new TTemplateColumn();
		column.setColumn_id(Integer.parseInt(tTemplateColumnDao.findSequence().toString()));
		column.setTemplate_id(busiFeeStd.getTemplate_id());
		column.setColumn_name("fee_id");
		column.setColumn_text("费用名");
		column.setIs_editable(SystemConstants.BOOLEAN_FALSE);
		column.setType("string");
		column.setOptr_id(WebOptr.getOptr().getOptr_id());
		columnList.add(column);
		
		TTemplateFeeStd std = new TTemplateFeeStd();
		std.setColumn_id(column.getColumn_id());
		std.setFee_std_id(busiFeeStd.getFee_std_id());
		stdList.add(std);
		
		column = new TTemplateColumn();
		column.setColumn_id(Integer.parseInt(tTemplateColumnDao.findSequence().toString()));
		column.setTemplate_id(busiFeeStd.getTemplate_id());
		column.setColumn_name("min_value");
		column.setColumn_text("最小值");
		column.setIs_editable(SystemConstants.BOOLEAN_TRUE);
		column.setType("number");
		column.setMin_value(busiFeeStd.getMin_value());
		column.setMax_value(busiFeeStd.getMax_value());
		column.setDefault_value(busiFeeStd.getMin_value());
		column.setOptr_id(WebOptr.getOptr().getOptr_id());
		columnList.add(column);
		
		std = new TTemplateFeeStd();
		std.setColumn_id(column.getColumn_id());
		std.setFee_std_id(busiFeeStd.getFee_std_id());
		stdList.add(std);
		
		column = new TTemplateColumn();
		column.setColumn_id(Integer.parseInt(tTemplateColumnDao.findSequence().toString()));
		column.setTemplate_id(busiFeeStd.getTemplate_id());
		column.setColumn_name("default_value");
		column.setColumn_text("默认值");
		column.setIs_editable(SystemConstants.BOOLEAN_TRUE);
		column.setType("number");
		column.setDefault_value(busiFeeStd.getDefault_value());
		column.setOptr_id(WebOptr.getOptr().getOptr_id());
		columnList.add(column);
		
		std = new TTemplateFeeStd();
		std.setColumn_id(column.getColumn_id());
		std.setFee_std_id(busiFeeStd.getFee_std_id());
		stdList.add(std);
		
		column = new TTemplateColumn();
		column.setColumn_id(Integer.parseInt(tTemplateColumnDao.findSequence().toString()));
		column.setTemplate_id(busiFeeStd.getTemplate_id());
		column.setColumn_name("max_value");
		column.setColumn_text("最大值");
		column.setIs_editable(SystemConstants.BOOLEAN_TRUE);
		column.setType("number");
		column.setMin_value(busiFeeStd.getMin_value());
		column.setMax_value(busiFeeStd.getMax_value());
		column.setDefault_value(busiFeeStd.getMax_value());
		column.setOptr_id(WebOptr.getOptr().getOptr_id());
		columnList.add(column);
		
		std = new TTemplateFeeStd();
		std.setColumn_id(column.getColumn_id());
		std.setFee_std_id(busiFeeStd.getFee_std_id());
		stdList.add(std);
		
		if(StringHelper.isNotEmpty(busiFeeStd.getDevice_buy_mode())){
			column = new TTemplateColumn();
			column.setColumn_id(Integer.parseInt(tTemplateColumnDao.findSequence().toString()));
			column.setTemplate_id(busiFeeStd.getTemplate_id());
			column.setColumn_name("device_buy_mode");
			column.setColumn_text("购买方式");
			column.setIs_editable(SystemConstants.BOOLEAN_FALSE);
			column.setType("string");
			column.setItem_key(DictKey.BUSI_BUY_MODE.toString());
			column.setItem_key_text("购买方式");
			column.setSelect_value("ALL");
			column.setOptr_id(WebOptr.getOptr().getOptr_id());
			columnList.add(column);
			
			std = new TTemplateFeeStd();
			std.setColumn_id(column.getColumn_id());
			std.setFee_std_id(busiFeeStd.getFee_std_id());
			stdList.add(std);
		}
		
		if(StringHelper.isNotEmpty(busiFeeStd.getDevice_type())){
			column = new TTemplateColumn();
			column.setColumn_id(Integer.parseInt(tTemplateColumnDao.findSequence().toString()));
			column.setTemplate_id(busiFeeStd.getTemplate_id());
			column.setColumn_name("device_type");
			column.setColumn_text("设备类型");
			column.setIs_editable(SystemConstants.BOOLEAN_FALSE);
			column.setType("string");
			column.setItem_key(DictKey.DEVICE_TYPE.toString());
			column.setItem_key_text("设备类型");
			column.setSelect_value("ALL");
			column.setOptr_id(WebOptr.getOptr().getOptr_id());
			columnList.add(column);
			
			std = new TTemplateFeeStd();
			std.setColumn_id(column.getColumn_id());
			std.setFee_std_id(busiFeeStd.getFee_std_id());
			stdList.add(std);
		}
		
		if(StringHelper.isNotEmpty(deviceModelListStr)){
			String deviceType = busiFeeStd.getDevice_type();
			column = new TTemplateColumn();
			column.setColumn_id(Integer.parseInt(tTemplateColumnDao.findSequence().toString()));
			column.setTemplate_id(busiFeeStd.getTemplate_id());
			column.setColumn_name("device_model");
			column.setColumn_text("设备型号");
			column.setIs_editable(SystemConstants.BOOLEAN_FALSE);
			column.setType("string");
			
			if(deviceType.equals(SystemConstants.DEVICE_TYPE_STB)){
				column.setItem_key("STB_MODEL");
				column.setItem_key_text("机顶盒型号");
			}else if(deviceType.equals(SystemConstants.DEVICE_TYPE_CARD)){
				column.setItem_key("CARD_MODEL");
				column.setItem_key_text("机顶盒型号");
			}else if(deviceType.equals(SystemConstants.DEVICE_TYPE_MODEM)){
				column.setItem_key("MODEM_MODEL");
				column.setItem_key_text("MODEM型号");
			}else if(deviceType.equals(SystemConstants.DEVICE_TYPE_CTL)){
				column.setItem_key("TV_MODEL");
				column.setItem_key_text("遥控器型号");
			}
			column.setSelect_value("ALL");
			columnList.add(column);
			
			std = new TTemplateFeeStd();
			std.setColumn_id(column.getColumn_id());
			std.setFee_std_id(busiFeeStd.getFee_std_id());
			stdList.add(std);
		}
		
		tTemplateColumnDao.save(columnList.toArray(new TTemplateColumn[columnList.size()]));
		
		tTemplateFeeStdDao.save(stdList.toArray(new TTemplateFeeStd[stdList.size()]));
	}
	
	public List<TreeDto> queryTemplateOptr(String[] columnIds, String type) throws Exception {
		String countyId = WebOptr.getOptr().getCounty_id();
		String dataRight = this.queryDataRightCon(WebOptr.getOptr(), DataRight.CHANGE_COUNTY.toString());
		
		List<TreeDto> treeList = new ArrayList<TreeDto>();
		//查询需要新增模板权限操作员
		if(type.equals("add")){
			treeList = sCountyDao.queryAddTemplateOptr(columnIds, countyId, dataRight);
			for(TreeDto county : treeList){
				county.setChecked(false);
			}
		}else if(type.equals("edit")){	//修改
			treeList = sCountyDao.queryEditTemplateOptr(columnIds, countyId, dataRight);
			List<TreeDto> coList = tTemplateColumnOptrDao.queryByColumnIds(columnIds);
			for(TreeDto county : treeList){
				county.setChecked(false);
				for(TreeDto column : coList){
					if(county.getId().equals(column.getId())){
						county.setChecked(true);
					}
				}
			}
		}
		return treeList;
	}
	
	public void saveTemplateToOptrs(String[] columnIds, String[] optrIds) throws Exception {
		tTemplateColumnOptrDao.deleteByOptrIds(optrIds, columnIds);
		if(optrIds != null && optrIds.length > 0){
			List<TTemplateColumnOptr> list = new ArrayList<TTemplateColumnOptr>();
			for(int i=0,len=columnIds.length;i<len;i++){
				for(int j=0,len2=optrIds.length;j<len2;j++){
					TTemplateColumnOptr co = new TTemplateColumnOptr();
					co.setColumn_id(columnIds[i]);
					co.setOptr_id(optrIds[j]);
					list.add(co);
				}
			}
			if(list.size() > 0){
				tTemplateColumnOptrDao.save(list.toArray(new TTemplateColumnOptr[list.size()]));
			}
		}
	}
	
	/**
	 * 查询单个费用
	 * @param template_id
	 * @param fee_std_id
	 * @return
	 * @throws Exception
	 */
	public TBusiFeeStd queryTBusiFeeStd(String template_id,String fee_std_id) throws Exception{
		if(StringHelper.isEmpty(fee_std_id)){
			return null;
		}else{
			Map<String,TBusiFeeStd> map=CollectionHelper.converToMapSingle(this.queryByTemplateId(template_id).getFeeStbList(), "fee_std_id");
			return map.get(fee_std_id);
		}
	}
	public TBusiFeeStd queryTBusiFeeStd(String fee_std_id) throws Exception{
		return this.tBusiFeeStdDao.findByKey(fee_std_id);
	}
	/**
	 * 保存费用模板异动
	 * @param oldbean
	 * @param newbean
	 * @throws Exception
	 */
	public void saveTemplateBsuiFeeChange(String template_id,TBusiFeeStd oldbean,TBusiFeeStd newbean) throws Exception{
		String change_text=BeanHelper.beanchange(oldbean, newbean);
		if(StringHelper.isNotEmpty(change_text)){
			SSysChange change=new SSysChange();
			change.setContent(change_text);
			change.setChange_type(SysChangeType.TEMPLATE.name());
			change.setOptr_id(WebOptr.getOptr().getOptr_id());
			change.setKey(template_id);
			TTemplate tp=tTemplateDao.findByKey(template_id);
			change.setKey_desc(tp.getTemplate_type_text()+"_"+tp.getTemplate_name());
			change.setCreate_time(new Date());
			change.setChange_desc(oldbean==null?
					newbean.getFee_name()+"("+newbean.getFee_std_id()+")"
					:oldbean.getFee_name()+"("+oldbean.getFee_std_id()+")");
			sSysChangeDao.save(change);
		}
	}
	/**
	 * 查询模板适用地区
	 * @param template_id
	 * @return
	 * @throws Exception
	 */
	public List<TTemplateCounty> queryTemplateCountys(String template_id)throws Exception{
		return tTemplateCountyDao.queryTemplateCounty(template_id);
	}
	/**
	 * 保存使用地区异动
	 * @throws Exception 
	 */
	public void saveTemplateCountyChange(String templateId, List<TTemplateCounty> oldcountys, List<TTemplateCounty> newcountys) throws Exception{
		String change_text=BeanHelper.listchange(oldcountys, newcountys, "county_id");
		if(StringHelper.isNotEmpty(change_text)){
			SSysChange change=new SSysChange();
			change.setContent(change_text);
			change.setChange_type(SysChangeType.TEMPLATE.name());
			change.setOptr_id(WebOptr.getOptr().getOptr_id());
			change.setKey(templateId);
			TTemplate tp=tTemplateDao.findByKey(templateId);
			change.setKey_desc(tp.getTemplate_type_text()+"_"+tp.getTemplate_name());
			change.setCreate_time(new Date());
			change.setChange_desc("适用地区异动");
			sSysChangeDao.save(change);
		}
	}
	/**
	 * 非费用模板记录异动
	 * @param <T>
	 * @param template_id
	 * @param oldList
	 * @param newList
	 * @param proptyname
	 * @throws Exception
	 */
	public <T> void saveTemplateContentChange(String templateId,List<T> oldList,List<T> newList,String...proptyname) throws Exception{
		String change_text=BeanHelper.listchange(oldList, newList, proptyname);
		if(StringHelper.isNotEmpty(change_text)){
			SSysChange change=new SSysChange();
			change.setContent(change_text);
			change.setChange_type(SysChangeType.TEMPLATE.name());
			change.setOptr_id(WebOptr.getOptr().getOptr_id());
			change.setKey(templateId);
			TTemplate tp=tTemplateDao.findByKey(templateId);
			change.setKey_desc(tp.getTemplate_type_text()+"_"+tp.getTemplate_name());
			change.setCreate_time(new Date());
			change.setChange_desc("模板定义异动");
			sSysChangeDao.save(change);
		}
	}
	
	/**
	 * 根据费用标准ID删除记录
	 * @param feeStdId
	 * @throws JDBCException
	 */
	public void deleteFeeConfig(String feeStdId) throws Exception{
		tBusiFeeStdDao.remove(feeStdId);
		tTemplateColumnDao.deleteColumn(feeStdId, WebOptr.getOptr().getOptr_id());
	}
	
	/**
	 * 保存业务费用的地区配置
	 * @param templateId
	 * @param templateType
	 * @param docList
	 * @param countyIds
	 * @throws JDBCException 
	 * @throws JDBCException
	 */
	public void saveFeeTpls(String templateId, String templateType,String[] countyIds) throws JDBCException {
		//保存模板地区配置
		saveTemplateCountyConfigs(templateId, templateType, countyIds);
	}

/*****************************业务单据****************************/
	/**
	* @Description:查询单据类型
	* @return
	* @throws Exception
	* @return List<TBusiDoc>
	 */
	public List<TBusiDoc> querydoc()throws Exception {
		return tBusiDocDao.querydoc();
	}

	/**
	 * 根据模板ID查询业务单据配置
	 * @author eagle
	 * @param templateId
	 * @return
	 * @throws JDBCException
	 */
	public List<TBusiCodeDoc> queryDocTpls(String templateId) throws JDBCException{
		return tBusiCodeDocDao.queryDocTpls(templateId);
	}
	
	/**
	 * 保存业务单据配置
	 * @param templateId
	 * @param templateType
	 * @param docList
	 * @param countyIds
	 * @throws JDBCException
	 */
	public void saveDocTpls(String templateId,String templateType,List<TBusiCodeDoc> docList,String[] countyIds) throws JDBCException{
		//保存模板地区配置
		saveTemplateCountyConfigs(templateId, templateType, countyIds);
		
		tBusiCodeDocDao.deleteByTplId(templateId);
		tBusiCodeDocDao.save(docList.toArray(new TBusiCodeDoc[docList.size()]));
	}

/****************************业务工单****************************/
	/**
	 * 根据模板ID查询业务工单配置
	 * @param templateId
	 * @return
	 * @throws JDBCException
	 */
	public List<TBusiCodeTask> queryTaskTpls(String templateId) throws JDBCException{
		return tBusiCodeTaskDao.queryTaskTpls(templateId);
	}

	/**
	* @Description:查询工单类型
	* @return
	* @throws Exception
	* @return List<TTaskDetailType>
	 */
	public List<TTaskDetailType> querytask()throws Exception {
		return tTaskDetailTypeDao.findAll();
	}
	
	/**
	 * 保存业务工单配置
	 * @param templateId
	 * @param templateType
	 * @param taskList
	 * @param countyIds
	 * @throws Exception 
	 */
	public void saveTaskTpls(String templateId,String templateType,List<TBusiCodeTask> taskList,String[] countyIds) throws Exception{
		//保存模板地区配置
		saveTemplateCountyConfigs(templateId, templateType, countyIds);
		List<TTaskTemplatefile> tFileList = new ArrayList<TTaskTemplatefile>();
		List<String> list = CollectionHelper.converValueToList(tTaskTemplatefileDao.queryTaskFileById(templateId), "task_detail_type");
		for(TBusiCodeTask dto:taskList){
			if(list.contains(dto.getDetail_type_id())){
				continue;
			}
			TTaskTemplatefile file = new TTaskTemplatefile();
			file.setTemlate_id(templateId);
			file.setTask_detail_type(dto.getDetail_type_id());
			file.setTemplate_filename("task.xml");
			tFileList.add(file);
		}
		
		tTaskTemplatefileDao.save(tFileList.toArray(new TTaskTemplatefile[tFileList.size()]));
		tBusiCodeTaskDao.deleteByTplId(templateId);
		tBusiCodeTaskDao.save(taskList.toArray(new TBusiCodeTask[taskList.size()]));
	}

/****************************信息修改****************************/

	/**
	 * 查询跟信息修改相关的业务
	 * @return
	 * @throws Exception
	 */
	public List<TBusiCode> queryBusiForUpdate() throws Exception{
		return tBusiCodeDao.queryBusiForUpdate();
	}
	
	/**
	 * 查询信息修改字段名
	 * @return
	 * @throws JDBCException
	 */
	public List<TUpdateCfg> queryFields() throws JDBCException{
		return tUpdateCfgDao.queryFields();
	}
	/**
	 * 查询信息修改模板数据
	 * @param templateId
	 * @return
	 * @throws JDBCException
	 */
	public List<TUpdateCfg> queryUpdCfgTpls(String templateId) throws JDBCException{
		return tUpdateCfgDao.queryUpdCfgTpls(templateId);
	}
	
	/**
	 * 保存信息修改模板数据
	 * @param templateId
	 * @param templateType
	 * @param invoiceList
	 * @param countyIds
	 * @throws JDBCException
	 */
	public void saveUpdCfgTpls(String templateId,String templateType,List<TUpdateCfg> updList,String[] countyIds) throws JDBCException{
		//保存模板地区配置
		saveTemplateCountyConfigs(templateId, templateType, countyIds);
		
		tUpdateCfgDao.deleteByTplId(templateId);
		tUpdateCfgDao.save(updList.toArray(new TUpdateCfg[updList.size()]));
	}

/****************************发票打印****************************/
	/**
	 * 查询发票打印项
	 */
	public List<TPrintitem> queryPrintItem() throws JDBCException{
		return tPrintitemDao.findAll();
	}

	/**
	 * 查询发票打印种类
	 * @return
	 * @throws JDBCException
	 */
	public List<TBusiDoc> queryInvoiceType() throws JDBCException{
		String countyId = WebOptr.getOptr().getCounty_id();
		return tBusiDocDao.queryInvoiceType(countyId);
	}

	/**
	 * 查询发票打印模板数据
	 * @return
	 * @throws JDBCException
	 */
	public List<TInvoicePrintitem> queryInvoiceTpls(String templateId) throws JDBCException{
		return tInvoicePrintitemDao.queryInvoiceTpls(templateId);
	}
	
	/**
	 * 保存发票打印模板数据
	 * @param templateId
	 * @param templateType
	 * @param invoiceList
	 * @param countyIds
	 * @throws JDBCException
	 */
	public void saveInvoiceTpls(String templateId,String templateType,List<TInvoicePrintitem> invoiceList,String[] countyIds) throws JDBCException{
		//保存模板地区配置
		saveTemplateCountyConfigs(templateId, templateType, countyIds);
		
		tInvoicePrintitemDao.deleteByTplId(templateId);
		tInvoicePrintitemDao.save(invoiceList.toArray(new TInvoicePrintitem[invoiceList.size()]));
	}

/****************************计费****************************/
	/**
	 * 查询产品状态
	 * @return
	 * @throws JDBCException
	 */
	public List<TProdStatusRent> queryProdStatus() throws JDBCException{
		return tProdStatusRentDao.queryProdStatus();
	}

	/**
	 * 查询计费模板数据
	 * @param templateId
	 * @return
	 * @throws JDBCException
	 */
	public List<TProdStatusRent> queryBillTpls(String templateId) throws JDBCException{
		return tTemplateDao.queryBillTpls(templateId);
	}
	
	/**
	 * 保存计费模板数据
	 * @param templateId
	 * @param templateType
	 * @param billList
	 * @param countyIds
	 * @throws JDBCException
	 */
	public void saveBillTpls(String templateId,String templateType,List<TProdStatusRent> billList,String[] countyIds) throws JDBCException{
		//保存模板地区配置
		saveTemplateCountyConfigs(templateId, templateType, countyIds);
		
		tProdStatusRentDao.deleteByTplId(templateId);
		tProdStatusRentDao.save(billList.toArray(new TProdStatusRent[billList.size()]));
	}

/****************************配置****************************/
//	/**
//	 * 查询配置种类
//	 * @return
//	 * @throws JDBCException
//	 */
//	public List<TTemplateDto> queryConfigs() throws JDBCException{
//		return tTemplateDao.queryConfigs();
//	}

	/**
	 * 查询配置模板数据
	 * @param templateId
	 * @return
	 * @throws JDBCException
	 */
	public List<TConfigTemplate> queryConfigTpls(String templateId) throws JDBCException{
		return tConfigTemplateDao.queryConfigTpls(templateId);
	}
	
	/**
	 * 保存配置模板数据
	 * @param templateId
	 * @param templateType
	 * @param configList
	 * @param countyIds
	 * @throws JDBCException
	 */
	public void saveConfigTpls(String templateId, String templateType,
			List<TConfigTemplate> configList, String[] countyIds, SOptr optr)
			throws Exception {
		String oweFeeNumber = "";	//修改后值
		String preTemplateId = "";
		for(TConfigTemplate ct : configList){
			if(ct.getConfig_name().equals(TemplateConfigDto.Config.OWE_FEE_NUMBER.toString())){
				oweFeeNumber = ct.getConfig_value();
				preTemplateId = ct.getTemplate_id();
				break;
			}
		}
		String remark = tConfigDao.findByKey(TemplateConfigDto.Config.OWE_FEE_NUMBER.toString()).getRemark();
		for(String countyId : countyIds){
			TConfigTemplate ct = tConfigTemplateDao.queryConfigByConfigName(
					TemplateConfigDto.Config.OWE_FEE_NUMBER.toString(), countyId);	//当前值
			if(null==ct){
				saveOperateLog(FuncCode.OWE_FEE_NUMBER.toString(), preTemplateId, remark + " 修改前天数:  ; 修改后天数：" + oweFeeNumber+" 模板ID："+preTemplateId,
						optr);
				
			}else if(!oweFeeNumber.equals(ct.getConfig_value())){
				saveOperateLog(FuncCode.OWE_FEE_NUMBER.toString(), ct
						.getTemplate_id(), remark + " 修改前天数：" + ct.getConfig_value()+"; 修改后天数：" + oweFeeNumber+"  模板ID："+preTemplateId,
						optr);
			}
		}
		//保存模板地区配置
		saveTemplateCountyConfigs(templateId, templateType, countyIds);
		
		tConfigTemplateDao.deleteByTplId(templateId);
		tConfigTemplateDao.save(configList.toArray(new TConfigTemplate[configList.size()]));
		
	}

/****************************临时授权***************************/
	/**
	 * 查询临时授权配置
	 */
	public List<TOpenTemp> queryOpenTemps(String templateId) throws JDBCException {
		return tOpenTempDao.queryOpenTemps(templateId);
	}
	
	/**
	 * 保存临时授权配置
	 * @param templateId
	 * @param templateType
	 * @param openList
	 * @param countyIds
	 * @throws JDBCException
	 */
	public void saveOpenTemps(String templateId,String templateType,List<TOpenTemp> openList,String[] countyIds) throws JDBCException{
		//保存模板地区配置
		saveTemplateCountyConfigs(templateId, templateType, countyIds);
		
		tOpenTempDao.deleteByTplId(templateId);
		tOpenTempDao.save(openList.toArray(new TOpenTemp[openList.size()]));
	}

/****************************机顶盒灌装***************************/

	/**
	 * 查询服务类型为DTV的资源
	 * @return
	 * @throws JDBCException
	 */
	public List<PRes> queryDtvRes() throws Exception {
		return pResDao.queryResByServId(SystemConstants.PROD_SERV_ID_DTV,null);
	}

	/**
	 * 根据模板ID查询机顶盒灌装数据
	 * @param templateId
	 * @return
	 * @throws JDBCException
	 */
	public List<TStbFilled> queryStbFilleds(String templateId) throws JDBCException {
		return tStbFilledDao.queryStbFilleds(templateId);
	}
	
	/**
	 * 保存机顶盒灌装配置
	 * @param templateId
	 * @param templateType
	 * @param stbList
	 * @param countyIds
	 * @throws JDBCException
	 */
	public void saveStbFilled(String templateId,String templateType,List<TStbFilled> stbList,String[] countyIds) throws JDBCException{
		//保存模板地区配置
		saveTemplateCountyConfigs(templateId, templateType, countyIds);
		
		tStbFilledDao.deleteByTplId(templateId);
		tStbFilledDao.save(stbList.toArray(new TStbFilled[stbList.size()]));
	}
	
	/**
	 * 保存模板地区配置
	 * @param templateId
	 * @param templateType
	 * @param countyIds
	 * @throws JDBCException
	 */
	private void saveTemplateCountyConfigs(String templateId,String templateType,String[] countyIds) throws JDBCException{
		//根据传过来的countyIds,删除模板类型相同的相关数据和本身数据
		sCountyDao.deleteByTplId(templateId);
		sCountyDao.deleteByTplType(templateType, countyIds);
		
		List<TTemplateCounty> tplCountyList = new ArrayList<TTemplateCounty>();
		for (String element : countyIds) {
			if(element.length() > 0){
				TTemplateCounty tt = new TTemplateCounty();
				tt.setCounty_id(element);
				tt.setTemplate_type(templateType);
				tt.setTemplate_id(templateId);
				tplCountyList.add(tt);
			}
		}
		//保存模板地区选择数据
		if(tplCountyList.size() > 0){
			tTemplateCountyDao.save(tplCountyList.toArray(new TTemplateCounty[tplCountyList.size()]));
		}
	}

	/**
	 * 创建新的模板
	 * @param templateType
	 * @param templateName
	 * @param copyTemplateId 用于拷贝模板id
	 * @param string 
	 * @throws Exception
	 */
	public TTemplate createTemplate(String templateType,String templateName,String copyTemplateId, String optrId) throws Exception{
		TTemplate template = new TTemplate();
		template.setTemplate_id(tTemplateDao.getTemplateID());
		template.setTemplate_type(templateType);
		template.setTemplate_name(templateName);
		template.setOptr_id(optrId);
		tTemplateDao.save(template);

		if(StringHelper.isNotEmpty(copyTemplateId)){
			saveCopyTemplateConfig(template.getTemplate_id(),copyTemplateId,templateType);
		}

		return template;
	}

	/**
	 * 保存复制模板配置数据
	 * @param templateId
	 * @param templateType
	 * @param templateList
	 * @throws JDBCException
	 */
	private void saveCopyTemplateConfig(String templateId,String copyTemplateId,String templateType) throws Exception{
		if(templateType.equals(SystemConstants.TEMPLATE_TYPE_FEE)){//FEE
			List<TBusiFeeStd> feeList = queryByTemplateId(copyTemplateId).getFeeStbList();
			for(TBusiFeeStd tt : feeList){
				List<String> deviceModels = null;
				if(StringHelper.isNotEmpty(tt.getDevice_type())){
					deviceModels = tt.getDeviceModelList();
				}
				tt.setTemplate_id(templateId);
				tt.setFee_std_id(tBusiFeeStdDao.findSequence().toString());
				if(null != deviceModels){
					for(String model : deviceModels){
						TBusiFeeDevice entity = new TBusiFeeDevice();
						entity.setFee_std_id(tt.getFee_std_id());
						entity.setDevice_buy_mode(tt.getDevice_buy_mode());
						entity.setDevice_model(model);
						entity.setDevice_type(tt.getDevice_type());
						tBusiFeeDeviceDao.save(entity);
					}
				}
			}
			tBusiFeeStdDao.save(feeList.toArray(new TBusiFeeStd[feeList.size()]));
		}else if(templateType.equals(SystemConstants.TEMPLATE_TYPE_DOC)){//DOC
			List<TBusiCodeDoc> docList = queryDocTpls(copyTemplateId);
			for(TBusiCodeDoc tt : docList){
				tt.setTemplate_id(templateId);
			}
			tBusiCodeDocDao.save(docList.toArray(new TBusiCodeDoc[docList.size()]));
		}else if(templateType.equals(SystemConstants.TEMPLATE_TYPE_TASK)){//TASK
			List<TBusiCodeTask> taskList = queryTaskTpls(copyTemplateId);
			for(TBusiCodeTask tt : taskList){
				tt.setTemplate_id(templateId);
			}
			tBusiCodeTaskDao.save(taskList.toArray(new TBusiCodeTask[taskList.size()]));
		}else if(templateType.equals(SystemConstants.TEMPLATE_TYPE_UPDPROP)){//UPDPROP
			List<TUpdateCfg> updList = queryUpdCfgTpls(copyTemplateId);
			for(TUpdateCfg tt : updList){
				tt.setTemplate_id(templateId);
			}
			tUpdateCfgDao.save(updList.toArray(new TUpdateCfg[updList.size()]));
		}else if(templateType.equals(SystemConstants.TEMPLATE_TYPE_BILLING)){//BILLING
			List<TProdStatusRent> billList = queryBillTpls(copyTemplateId);
			for(TProdStatusRent tt : billList){
				tt.setTemplate_id(templateId);
			}
			tProdStatusRentDao.save(billList.toArray(new TProdStatusRent[billList.size()]));
		}else if(templateType.equals(SystemConstants.TEMPLATE_TYPE_INVOICE)){//INVOICE
			List<TInvoicePrintitem> invoiceList = queryInvoiceTpls(copyTemplateId);
			for(TInvoicePrintitem tt : invoiceList){
				tt.setTemplate_id(templateId);
			}
			tInvoicePrintitemDao.save(invoiceList.toArray(new TInvoicePrintitem[invoiceList.size()]));
		}else if(templateType.equals(SystemConstants.TEMPLATE_TYPE_CONFIG)){//CONFIG
			List<TConfigTemplate> configList = queryConfigTpls(copyTemplateId);
			for(TConfigTemplate tt : configList){
				tt.setTemplate_id(templateId);
			}
			tConfigTemplateDao.save(configList.toArray(new TConfigTemplate[configList.size()]));
		}else if(templateType.equals(SystemConstants.TEMPLATE_TYPE_OPEN_TEMP)){
			TOpenTemp entity = queryOpenTemps(copyTemplateId).get(0);
			entity.setTemplate_id(templateId);
			tOpenTempDao.save(entity);
		}else if(templateType.equals(SystemConstants.TEMPLATE_TYPE_STB_FILLED)){
			List<TStbFilled> stbList = queryStbFilleds(copyTemplateId);
			for(TStbFilled tt : stbList){
				tt.setTemplate_id(templateId);
			}
			tStbFilledDao.save(stbList.toArray(new TStbFilled[stbList.size()]));
		}
	}

	/**
	 * 修改模板名字
	 * @param tt
	 * @throws JDBCException
	 */
	public void editTemplate(String templateId,String templateName) throws JDBCException{
		TTemplate template = tTemplateDao.findByKey(templateId);
		template.setTemplate_name(templateName);
		tTemplateDao.update(template);
	}

	public void deleteTemplate(String templateId) throws JDBCException{
		tTemplateDao.remove(templateId);
	}

	/**
	 * 根据模板类型查找模板
	 * @param templateType
	 * @param optr 
	 * @return
	 * @throws Exception
	 */
	public List<TTemplate> queryTplsByType(String templateType, SOptr optr) throws Exception{
		return tTemplateDao.queryTplsByType(templateType,optr.getCounty_id());
	}
	
	/**
	 * 根据配置名称和地区查配置
	 * @param configName
	 * @param countyId
	 * @return
	 * @throws JDBCException
	 */
	public TConfigTemplate queryConfigByConfigName(String configName,String countyId) throws Exception{
		return tConfigTemplateDao.queryConfigByConfigName(configName,countyId);
	}

	public TTemplateDao getTTemplateDao() {
		return tTemplateDao;
	}
	public void setTTemplateDao(TTemplateDao templateDao) {
		tTemplateDao = templateDao;
	}
	public TTemplateCountyDao getTTemplateCountyDao() {
		return tTemplateCountyDao;
	}
	public void setTTemplateCountyDao(TTemplateCountyDao templateCountyDao) {
		tTemplateCountyDao = templateCountyDao;
	}
	public TBusiCodeDocDao getTBusiCodeDocDao() {
		return tBusiCodeDocDao;
	}
	public void setTBusiCodeDocDao(TBusiCodeDocDao busiCodeDocDao) {
		tBusiCodeDocDao = busiCodeDocDao;
	}
	public TBusiCodeFeeDao getTBusiCodeFeeDao() {
		return tBusiCodeFeeDao;
	}
	public void setTBusiCodeFeeDao(TBusiCodeFeeDao busiCodeFeeDao) {
		tBusiCodeFeeDao = busiCodeFeeDao;
	}
	public TBusiCodeTaskDao getTBusiCodeTaskDao() {
		return tBusiCodeTaskDao;
	}
	public void setTBusiCodeTaskDao(TBusiCodeTaskDao busiCodeTaskDao) {
		tBusiCodeTaskDao = busiCodeTaskDao;
	}
	public TBusiCodeDao getTBusiCodeDao() {
		return tBusiCodeDao;
	}
	public void setTBusiCodeDao(TBusiCodeDao busiCodeDao) {
		tBusiCodeDao = busiCodeDao;
	}
	public TBusiDocDao getTBusiDocDao() {
		return tBusiDocDao;
	}
	public void setTBusiDocDao(TBusiDocDao busiDocDao) {
		tBusiDocDao = busiDocDao;
	}
	public TBusiFeeDao getTBusiFeeDao() {
		return tBusiFeeDao;
	}
	public void setTBusiFeeDao(TBusiFeeDao busiFeeDao) {
		tBusiFeeDao = busiFeeDao;
	}
	public TTaskDetailTypeDao getTTaskDetailTypeDao() {
		return tTaskDetailTypeDao;
	}
	public void setTTaskDetailTypeDao(TTaskDetailTypeDao taskDetailTypeDao) {
		tTaskDetailTypeDao = taskDetailTypeDao;
	}

	public TPrintitemDao getTPrintitemDao() {
		return tPrintitemDao;
	}

	public void setTPrintitemDao(TPrintitemDao printitemDao) {
		tPrintitemDao = printitemDao;
	}

	public TInvoicePrintitemDao getTInvoicePrintitemDao() {
		return tInvoicePrintitemDao;
	}

	public void setTInvoicePrintitemDao(TInvoicePrintitemDao invoicePrintitemDao) {
		tInvoicePrintitemDao = invoicePrintitemDao;
	}

	public SCountyDao getSCountyDao() {
		return sCountyDao;
	}

	public void setSCountyDao(SCountyDao countyDao) {
		sCountyDao = countyDao;
	}

	public TProdStatusRentDao getTProdStatusRentDao() {
		return tProdStatusRentDao;
	}

	public void setTProdStatusRentDao(TProdStatusRentDao prodStatusRentDao) {
		tProdStatusRentDao = prodStatusRentDao;
	}

	public TUpdateCfgDao getTUpdateCfgDao() {
		return tUpdateCfgDao;
	}

	public void setTUpdateCfgDao(TUpdateCfgDao updateCfgDao) {
		tUpdateCfgDao = updateCfgDao;
	}

	public TConfigTemplateDao getTConfigTemplateDao() {
		return tConfigTemplateDao;
	}

	public void setTConfigTemplateDao(TConfigTemplateDao configTemplateDao) {
		tConfigTemplateDao = configTemplateDao;
	}

	public TOpenTempDao getTOpenTempDao() {
		return tOpenTempDao;
	}

	public void setTOpenTempDao(TOpenTempDao openTempDao) {
		tOpenTempDao = openTempDao;
	}

	public PResDao getPResDao() {
		return pResDao;
	}

	public void setPResDao(PResDao resDao) {
		pResDao = resDao;
	}

	public TStbFilledDao getTStbFilledDao() {
		return tStbFilledDao;
	}

	public void setTStbFilledDao(TStbFilledDao stbFilledDao) {
		tStbFilledDao = stbFilledDao;
	}

	public TBusiFeeStdDao getTBusiFeeStdDao() {
		return tBusiFeeStdDao;
	}

	public void setTBusiFeeStdDao(TBusiFeeStdDao busiFeeStdDao) {
		tBusiFeeStdDao = busiFeeStdDao;
	}

	public TBusiFeeDeviceDao getTBusiFeeDeviceDao() {
		return tBusiFeeDeviceDao;
	}

	public void setTBusiFeeDeviceDao(TBusiFeeDeviceDao busiFeeDeviceDao) {
		tBusiFeeDeviceDao = busiFeeDeviceDao;
	}

	public void setTConfigDao(TConfigDao configDao) {
		tConfigDao = configDao;
	}
	public SSysChangeDao getSSysChangeDao() {
		return sSysChangeDao;
	}

	public void setSSysChangeDao(SSysChangeDao sysChangeDao) {
		sSysChangeDao = sysChangeDao;
	}

	public TConfigDao getTConfigDao() {
		return tConfigDao;
	}

	public void setTTemplateColumnDao(TTemplateColumnDao templateColumnDao) {
		tTemplateColumnDao = templateColumnDao;
	}

	public void setTTemplateColumnOptrDao(
			TTemplateColumnOptrDao templateColumnOptrDao) {
		tTemplateColumnOptrDao = templateColumnOptrDao;
	}

	public void setTTemplateFeeStdDao(TTemplateFeeStdDao templateFeeStdDao) {
		tTemplateFeeStdDao = templateFeeStdDao;
	}

	public void setTTemplateExcludeCountyDao(
			TTemplateExcludeCountyDao templateExcludeCountyDao) {
		tTemplateExcludeCountyDao = templateExcludeCountyDao;
	}
	public void setTTaskTemplatefileDao(TTaskTemplatefileDao taskTemplatefileDao) {
		tTaskTemplatefileDao = taskTemplatefileDao;
	}
	
}
