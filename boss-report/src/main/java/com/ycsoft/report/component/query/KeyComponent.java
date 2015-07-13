package com.ycsoft.report.component.query;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ycsoft.business.dao.system.SResourceDao;
import com.ycsoft.business.dao.system.SRoleDao;
import com.ycsoft.commons.abstracts.BaseComponent;
import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.commons.helper.FileHelper;
import com.ycsoft.commons.helper.JsonHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.report.bean.RepDatabase;
import com.ycsoft.report.bean.RepDefine;
import com.ycsoft.report.bean.RepFileKeyValue;
import com.ycsoft.report.bean.RepKeyCon;
import com.ycsoft.report.bean.RepKeySystem;
import com.ycsoft.report.bean.RepMemoryKey;
import com.ycsoft.report.bean.RepMyCube;
import com.ycsoft.report.commons.ReportConstants;
import com.ycsoft.report.commons.SystemConfig;
import com.ycsoft.report.dao.config.RepDefineDao;
import com.ycsoft.report.dao.config.RepDimensionLevelDao;
import com.ycsoft.report.dao.config.RepMyCubeDao;
import com.ycsoft.report.dao.config.RepSqlDao;
import com.ycsoft.report.dao.keycon.QueryKeyValueDao;
import com.ycsoft.report.dto.MyDimTotal;
import com.ycsoft.report.dto.RepDataRight;
import com.ycsoft.report.dto.RepKeyDto;
import com.ycsoft.report.dto.WarnDimLevel;
import com.ycsoft.report.query.QueryManage;
import com.ycsoft.report.query.QueryResultOlap;
import com.ycsoft.report.query.cube.CubeDataSet;
import com.ycsoft.report.query.cube.CubeExec;
import com.ycsoft.report.query.cube.Dimension;
import com.ycsoft.report.query.cube.DimensionLevel;
import com.ycsoft.report.query.cube.DimensionRolap;
import com.ycsoft.report.query.cube.Measure;
import com.ycsoft.report.query.cube.graph.CubeGraphType;
import com.ycsoft.report.query.cube.impl.CubeManage;
import com.ycsoft.report.query.cube.impl.DimensionManage;
import com.ycsoft.report.query.cube.impl.MyCube;
import com.ycsoft.report.query.cube.showclass.cellwarn.WarnRowType;
import com.ycsoft.report.query.datarole.DataControl;
import com.ycsoft.report.query.key.QueryKey;
import com.ycsoft.report.query.key.Impl.ConKeyCheck;
import com.ycsoft.report.query.key.Impl.QueryKeyValue;

/**
 * 查询条件管理组件
 * 
 * @author new
 * 
 */
@Component
public class KeyComponent extends BaseComponent {

	private RepSqlDao repSqlDao;
	private SResourceDao sResourceDao;
	private QueryKeyValueDao queryKeyValueDao;
	private SRoleDao sRoleDao;
	private QueryKey queryKey;
	private RepDefineDao repDefineDao;
	private RepDimensionLevelDao repDimensionLevelDao;
	private QueryManage queryManage;
	private DataControl dataControl;
	private RepMyCubeDao repMyCubeDao;
	private CubeManage cubeManage;

	public CubeManage getCubeManage() {
		return cubeManage;
	}
	public void setCubeManage(CubeManage cubeManage) {
		this.cubeManage = cubeManage;
	}
	/**
	 * 获得警戒类型对应的项目清单
	 * @param warntype
	 * @param template_id
	 * @return
	 * @throws ReportException 
	 */
	public List<WarnDimLevel> queryWarnDimLevels(String warntype,String template_id) throws ReportException{
		try {
			if(StringHelper.isEmpty(warntype))
				throw new ReportException("WarnType is null");
			WarnRowType type=WarnRowType.valueOf(warntype);
			if(type==null)
				throw new ReportException(warntype+" is not a WarnType.");
			RepMyCube repmycube= repMyCubeDao.queryMyCubeByTemplateId(template_id);
			MyCube mycube = JsonHelper.toObject(repmycube.getCube_json(), MyCube.class);
			CubeExec cube=cubeManage.createCube(repmycube.getRep_id(), mycube);
			
			List<WarnDimLevel> list=new ArrayList<WarnDimLevel>();
			for(DimensionRolap rolap: cube.getDimensionRolaps()){
				if(type.equals(WarnRowType.rolsign)){
					//行标签
					if(rolap.isUsesign()&&!rolap.isVerticalsign()){
						for(int i=1;i<=rolap.getLevel();i++){
							WarnDimLevel warnlevel=new WarnDimLevel();
							warnlevel.setDim(rolap.getId());
							warnlevel.setLevel(i);
							warnlevel.setId(JsonHelper.fromObject(warnlevel));
							warnlevel.setName(rolap.getDim().getLevel(i).getColumn_text());
							//warnlevel.setId(warnlevel.getId()+"_"+warnlevel.getLevel());
							list.add(warnlevel);
						}
					}
				}else if(type.equals(WarnRowType.colhead)){
					//列头
					if(rolap.isUsesign()&&rolap.isVerticalsign()){
						WarnDimLevel warnlevel=new WarnDimLevel();
						warnlevel.setDim(rolap.getId());
						warnlevel.setLevel(rolap.getLevel());
						warnlevel.setName(rolap.getName());
						warnlevel.setId(warnlevel.getId()+"_"+warnlevel.getLevel());
						list.add(warnlevel);
					}
				}
			}
			
			return list;
		} catch (ReportException e) {
			throw e;
		}catch (Exception e) {
			throw new ReportException(e);
		}
		
	}
	/**
	 * 图形类型
	 * @return
	 */
	public List<QueryKeyValue> queryGraphTypes(){
		List<QueryKeyValue> list=new ArrayList<QueryKeyValue>();
		for(CubeGraphType type: CubeGraphType.values()){
			QueryKeyValue vo=new QueryKeyValue();
			vo.setId(type.name());
			vo.setName(type.getDesc());
			vo.setPid(String.valueOf(type.getDimnum()));
			list.add(vo);
		}
		return list;
	}
	/**
	 * 获得一个cube的预设的指标清单
	 * 其中check=true表示已选中
	 * @param query_id
	 * @return
	 * @throws ReportException
	 */
	public List<ConKeyCheck> queryMeas(String query_id) throws ReportException{
		QueryResultOlap olap=(QueryResultOlap) queryManage.get(query_id);
		CubeExec cube=olap.getCube();
		List<ConKeyCheck> list=new ArrayList<ConKeyCheck>();
		Map<Measure,Boolean> checkMap=new HashMap<Measure,Boolean>();
		for(Measure mea: cube.getMeasures()){
			checkMap.put(mea, true);
		}
		for(Measure mea: cube.getDefaultMeasures()){
			ConKeyCheck vo=new ConKeyCheck();
			vo.setId(mea.getColumnCode());
			vo.setName(mea.getColumnText()+"_"+mea.getCalculation());
			if(checkMap.containsKey(mea))
				vo.setCheck(true);
			else
				vo.setCheck(false);
			list.add(vo);
		}
		return list;
	}
	
	/**
	 * 获得一个cube的维配置清单
	 * @param query_id
	 * @return
	 * @throws ReportException
	 */
	public List<DimensionRolap> queryDims(String query_id) throws ReportException{
		QueryResultOlap olap=(QueryResultOlap) queryManage.get(query_id);
		CubeExec cube=olap.getCube();
		return cube.getDimensionRolaps();
	}
	/**
	 * 维合计配置列表
	 * @param query_id
	 * @return
	 * @throws ReportException
	 */
	public List<MyDimTotal> queryDimTotal(String query_id) throws ReportException{
		QueryResultOlap olap=(QueryResultOlap) queryManage.get(query_id);
		CubeExec cube=olap.getCube();
		
		List<MyDimTotal> list=new ArrayList<MyDimTotal>();
		
		for(DimensionRolap rolap: cube.getDimensionRolaps()){
			MyDimTotal o=new MyDimTotal();
			o.setDim(rolap.getId());
			o.setDim_name(rolap.getName());
			String total_name="";
			List<ConKeyCheck> checklist=new ArrayList<ConKeyCheck>();
			for(int i=1;i<=rolap.getDim().getLevelNum();i++){
				ConKeyCheck check=new ConKeyCheck();
				check.setId(String.valueOf(i));
				check.setName(rolap.getDim().getLevel(i).getName());
				check.setCheck(rolap.isLevelTotal(i));
				if(check.getCheck()){
					total_name=total_name+check.getName()+",";
				}
				checklist.add(check);
			}
			o.setTotal_name(total_name);
			o.setTotallist(checklist);
			list.add(o);
		}
		return list;
	}
	
	public String queryDimName(String dim) throws ReportException{
		Dimension dimension=DimensionManage.getDimension(dim);
		if(dimension==null) throw new ReportException(dim+": dim is null or undefined.");
		return dimension.getName();
	}
	/**
	 * 查询cube中一个维的切片过滤值
	 * @throws ReportException 
	 */
	public Map<String,String[]> queryCubeSlicesValue(String query_id,String dim) throws ReportException{
		QueryResultOlap olap=(QueryResultOlap) queryManage.get(query_id);
		CubeExec cube=olap.getCube();
		Map<String,String[]> valueMap=new HashMap<String,String[]>();
		Dimension dimension=DimensionManage.getDimension(dim);
		if(dimension==null) throw new ReportException(dim+": dim is null or undefined.");
		DimensionRolap rolap=cube.getDimensionRolap(dimension);
		if(rolap==null) throw new ReportException(dim+" is undefined in cube");
		if(rolap.getSlices_level()!=null)
			valueMap.put(rolap.getSlices_level().toString(), rolap.getSlices_value());
		return valueMap;
	}
	
	/**
	 * 查询cube中一个维的值的排序顺序
	 * @throws ReportException 
	 */
	public Map<String,String[]> queryCubeSortValue(String query_id,String dim) throws ReportException{
		QueryResultOlap olap=(QueryResultOlap) queryManage.get(query_id);
		CubeExec cube=olap.getCube();
		
		Dimension dimension=DimensionManage.getDimension(dim);
		if(dimension==null) throw new ReportException(dim+": dim is null or undefined.");
		DimensionRolap rolap=cube.getDimensionRolap(dimension);
		if(rolap==null) throw new ReportException(dim+" is undefined in cube");

		Map<String,String[]> valueMap=new HashMap<String,String[]>();
		if(rolap.getLevel_sort_map()!=null){
			Iterator<Integer> it= rolap.getLevel_sort_map().keySet().iterator();
			while(it.hasNext()){
				Integer level=it.next();
				valueMap.put(level.toString(), rolap.getLevel_sort_map().get(level));
			}
		}
		return valueMap;
	}
	
	/**
	 * 根据维度id获得维度层级列表
	 * @param dim_id
	 * @return
	 * @throws ReportException 
	 */
	public List<DimensionLevel> queryLevels(String dim_id) throws ReportException{
		
		List<DimensionLevel> dimlevels=new ArrayList<DimensionLevel>();
		Dimension dimension=DimensionManage.getDimension(dim_id);
		if(dimension!=null){
		   for(int i=1;i<=dimension.getLevelNum();i++)
			   dimlevels.add(dimension.getLevel(i));
		}
		return dimlevels;
		
	}
	/**
	 * 获得一个维的一个层级取值
	 * @param dim_id
	 * @param level
	 * @return
	 * @throws ReportException
	 */
	public  List<QueryKeyValue> queryLevelValues(String dim_id,int level) throws ReportException{
		return DimensionManage.getDimension(dim_id).getLevelValues(level);
		
	}
	/**
	 * 保存组件上传的值
	 * @param rep_id
	 * @param key
	 * @param file
	 * @return
	 * @throws ReportException
	 */
	public String saveuploadfilekey(String key,File file) throws ReportException{
		
		try {
			String[] colName={"code1","code2","code3","code4","code5","code6","code7","code8","code9","code10","code11","code12"
					,"code13","code14","code15","code16","code17","code18","code19","code20"};
			List<RepFileKeyValue> list= FileHelper.fileToBean(file, colName, RepFileKeyValue.class);
			RepKeyCon keycon=SystemConfig.getConMap().get(key);
			String file_id=repDefineDao.findSequence("seq_repquery_id").toString();
			queryKeyValueDao.saveuploadqueryfile(file_id, list, keycon.getDatabase());
			return file_id;
		}  catch (JDBCException e) {
			throw new ReportException(e,e.getSQL());
		} catch (ReportException e) {
			throw e;
		}catch (Exception e) {
			throw new ReportException("system_error:",e);
		}
	}
	/**
	 * 获得报表定义信息
	 * @param rep_id
	 * @return
	 * @throws  
	 * @throws ReportException 
	 */
	public RepDefine queryRepDefine(String rep_id) throws ReportException{
		try {
			RepDefine rd=repDefineDao.getRepDefine(rep_id);
			RepDatabase rdb=SystemConfig.getDatabaseMap().get(rd.getDatabase());
			if(rdb!=null)
				rd.setDatabase_text(rdb.getName());
			return rd;
		} catch (JDBCException e) {
			throw new ReportException(e,e.getSQL());
		}
	}
	
	/**
	 * 获得所有列转换关键字清单
	 * @return
	 */
	public List<RepMemoryKey> getAllMemoryKeys(){
		List<RepMemoryKey> list=new ArrayList<RepMemoryKey>();
		for(RepMemoryKey mk:SystemConfig.getMemoryKeyListt()){
			RepMemoryKey o=new RepMemoryKey();
			o.setMemory_key(mk.getMemory_key());
			o.setMemory_desc(mk.getMemory_desc());
			o.setMemory_type(mk.getMemory_type());
			o.setValue_key(mk.getValue_key());
			o.setDatabase(mk.getDatabase());
			o.setRemark(mk.getRemark());
			list.add(o);
		}
		return list;
	}
	/**
	 * 可用查询条件列表
	 * @return
	 */
	public List<RepKeyCon> getAllKeyCon(){
		List<RepKeyCon> list=new ArrayList<RepKeyCon>();
		for(RepKeyCon o:SystemConfig.getConList()){
			RepKeyCon k=new RepKeyCon();
			k.setName(o.getName());
			k.setKey(o.getKey());
			k.setHtmlcode(o.getHtmlcode());
			k.setType(o.getType());
			k.setHtmlorder(o.getHtmlorder());
			k.setDatabase(o.getDatabase());
			list.add(k);
		}
		for( RepKeySystem  o  :SystemConfig.getSystemList()){
			RepKeyCon k=new RepKeyCon();
			k.setKey(o.getKey());
			k.setName(o.getName());
			k.setDatabase("system");
			k.setType("system");
			list.add(k);
		}
		return list;
	}
	/**
	 * 获取报表名称
	 * 
	 * @param rep_id
	 * @return
	 * @throws JDBCException
	 */
	public String getRepName(String rep_id) throws JDBCException {

		return sResourceDao.findByKey(rep_id).getRes_name();
	}

	/**
	 * 获得有效的页面查询条件列表
	 * 
	 * @param rep_id
	 * @return
	 * @throws JDBCException
	 * @throws ReportException
	 */
	public List<RepKeyDto> getRepKeyList(String rep_id) throws JDBCException,
			ReportException {

		String sql = repSqlDao.getSql(rep_id);
		return queryKey.getQueryKeyList(sql);
	}

	/**
	 * 获得有效的页面查询条件列表
	 * 并对的父子关系的datefield,textfiedl组件类型的条件组装到同一行
	 * @param rep_id
	 * @return
	 * @throws JDBCException
	 * @throws ReportException
	 */
	public List<RepKeyDto> getRepKeySameLineList(String rep_id) throws JDBCException,
			ReportException {

		String sql = repSqlDao.getSql(rep_id);
		return queryKey.getQueryKeySameLineList(sql);
	}
	
	/**
	 * 查询条件初始化值
	 * 
	 * @param county_id
	 * @param key
	 * @param fkey_value
	 * @param optr
	 * @return 查询条件值选择范围
	 * @throws ReportException 
	 * @throws JDBCException 
	 */
	public List<QueryKeyValue> getKeyValue(String countyid, String key,String fkey_value) throws JDBCException, ReportException {
		 return queryKey.getKeyValue(key, fkey_value, countyid);
	}

	/**
	 * 根据页面组件的值，返回所有相关子组件的值
	 * @param page
	 * @param rep_role
	 * @param system_optr_map
	 * @return 
	 * @throws ReportException 
	 * @throws JDBCException 
	 */
	public Map<String, List<QueryKeyValue>> getAllKeyValue(String rep_id,String countyid,String key,String key_value) throws ReportException, JDBCException{
		
		Map<String,List<QueryKeyValue>> keyvaluemap=new HashMap<String,List<QueryKeyValue>>();
		
		//把提取报表的查询条件列表并转换成树型结构的顺序
		List<RepKeyDto> keytree=queryKey.translateToTree(this.getRepKeyList(rep_id),key);
		
		String fkey_value="";
		for(RepKeyDto node:keytree){
			//父key的值唯一时
			if(keyvaluemap.containsKey(node.getFkey())&&keyvaluemap.get(node.getFkey()).size()==1){
				fkey_value=keyvaluemap.get(node.getFkey()).get(0).getId();
				//#countyid#为空时，当前key为#countyid#时，设置countyid为当前值
				if((countyid==null||countyid.equals(""))&&node.getFkey().equals(ReportConstants.PARTITION_KEY_CON))
					countyid=fkey_value;					
			}else if(node.getFkey()!=null&&!node.getFkey().equals("")&&node.getFkey().equals(key)){
				fkey_value=key_value;
			}else{
				fkey_value="";
			}
			keyvaluemap.put(node.getKey(), queryKey.getKeyValue(node.getKey(), fkey_value, countyid));
		}
		return keyvaluemap;	
	}
	
	public RepSqlDao getRepSqlDao() {
		return repSqlDao;
	}

	public void setRepSqlDao(RepSqlDao repSqlDao) {
		this.repSqlDao = repSqlDao;
	}

	public SResourceDao getSResourceDao() {
		return sResourceDao;
	}

	public void setSResourceDao(SResourceDao resourceDao) {
		sResourceDao = resourceDao;
	}

	public QueryKeyValueDao getQueryKeyValueDao() {
		return queryKeyValueDao;
	}

	public void setQueryKeyValueDao(QueryKeyValueDao queryKeyValueDao) {
		this.queryKeyValueDao = queryKeyValueDao;
	}

	public SRoleDao getSRoleDao() {
		return sRoleDao;
	}

	public void setSRoleDao(SRoleDao roleDao) {
		sRoleDao = roleDao;
	}

	public QueryKey getQueryKey() {
		return queryKey;
	}

	public void setQueryKey(QueryKey queryKey) {
		this.queryKey = queryKey;
	}
	public RepDefineDao getRepDefineDao() {
		return repDefineDao;
	}
	public void setRepDefineDao(RepDefineDao repDefineDao) {
		this.repDefineDao = repDefineDao;
	}
	public RepDimensionLevelDao getRepDimensionLevelDao() {
		return repDimensionLevelDao;
	}
	public void setRepDimensionLevelDao(RepDimensionLevelDao repDimensionLevelDao) {
		this.repDimensionLevelDao = repDimensionLevelDao;
	}
	public QueryManage getQueryManage() {
		return queryManage;
	}
	public void setQueryManage(QueryManage queryManage) {
		this.queryManage = queryManage;
	}
	public DataControl getDataControl() {
		return dataControl;
	}
	public void setDataControl(DataControl dataControl) {
		this.dataControl = dataControl;
	}
	public RepMyCubeDao getRepMyCubeDao() {
		return repMyCubeDao;
	}
	public void setRepMyCubeDao(RepMyCubeDao repMyCubeDao) {
		this.repMyCubeDao = repMyCubeDao;
	}
}
