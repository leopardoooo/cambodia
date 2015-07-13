package com.ycsoft.report.commons;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;

import com.ycsoft.beans.system.SItemvalue;
import com.ycsoft.business.dao.system.SItemvalueDao;
import com.ycsoft.commons.helper.CollectionHelper;
import com.ycsoft.commons.helper.LoggerHelper;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.report.bean.RepDatabase;
import com.ycsoft.report.bean.RepKeyCon;
import com.ycsoft.report.bean.RepKeyLevel;
import com.ycsoft.report.bean.RepKeySystem;
import com.ycsoft.report.bean.RepMemoryKey;
import com.ycsoft.report.bean.RepOptrConfigrep;
import com.ycsoft.report.component.query.QueryComponent;
import com.ycsoft.report.dao.config.RepDatabaseDao;
import com.ycsoft.report.dao.config.RepOptrConfigrepDao;
import com.ycsoft.report.dao.keycon.QueryKeyValueDao;
import com.ycsoft.report.dao.keycon.RepKeyConDao;
import com.ycsoft.report.dao.keycon.RepKeyLevelDao;
import com.ycsoft.report.dao.keycon.RepKeySystemDao;
import com.ycsoft.report.dao.keycon.RepMemoryKeyDao;
import com.ycsoft.report.dao.keycon.RepTemplateKeyDao;
import com.ycsoft.report.db.ConnContainer;
import com.ycsoft.report.query.ReloadSystemMemory;
import com.ycsoft.report.query.cube.impl.CubeManage;
import com.ycsoft.report.query.cube.impl.DimensionLevelValueManage;
import com.ycsoft.report.query.cube.impl.DimensionManage;
import com.ycsoft.report.query.datarole.RepLevelManage;
import com.ycsoft.report.query.key.Impl.QueryKeyValue;
import com.ycsoft.report.query.menory.CacheManageClearIndex;
import com.ycsoft.report.query.menory.CacheManageClearRealTime;
import com.ycsoft.report.query.menory.MemoryCacheInit;
import com.ycsoft.report.query.sql.AnalyseTemplateKey;
import com.ycsoft.report.query.task.TaskExec;

public class SystemConfig {
	//运行模式，true 正常模式;false 测试模式
	public static boolean RUN_MODE=true;
	// 报表查询key原始列表
	private static List<RepKeyCon> conList = new ArrayList<RepKeyCon>();
	private static Map<String, RepKeyCon> conMap = new HashMap<String, RepKeyCon>();
	private static List<RepKeyCon> conTree=new ArrayList<RepKeyCon>();
	// keylevel的原始列表
	private static List<RepKeyLevel> levelList = new ArrayList<RepKeyLevel>();
	private static Map<String, RepKeyLevel> levelMap = new HashMap<String, RepKeyLevel>();

	private static List<RepKeySystem> systemList = new ArrayList<RepKeySystem>();
	private static Map<String, RepKeySystem> systemMap = new HashMap<String, RepKeySystem>();

	//报表类型 rep_define.rep_type
	private static List<SItemvalue> repType=new ArrayList<SItemvalue>();
	//报表属性定义  rep_define.rep_info
	private static List<SItemvalue> repInfo=new ArrayList<SItemvalue>();
	
	//数据库配置 rep_database
	private static List<RepDatabase>  databaseList=new ArrayList<RepDatabase>();
	private static Map<String,RepDatabase> databaseMap=new HashMap<String,RepDatabase>();
	//配置报表权限操作员列表(编辑报表)
	private static Map<String,RepOptrConfigrep> optrConfigRepMap=new HashMap<String,RepOptrConfigrep>();
	
	private static Map<String,SItemvalue> repDataLevel=new HashMap<String,SItemvalue>();
	//报表自定义内存值
	private static Map<String,Map<String,String>> my_memory=new HashMap<String,Map<String,String>>();
	//内存关键字MAP，用于sql列ID转换
	private static Map<String,RepMemoryKey> memoryKeyMap=new HashMap<String,RepMemoryKey>();
	private static List<RepMemoryKey> memoryKeyListt=new ArrayList<RepMemoryKey>();
	
	//列模板
	private static Map<String,String> templateKeyMap=new HashMap<String,String>();
	private static List<String> templateKeyList=new ArrayList<String>();
	
	//自定义的数据权限 boss系统定义的数据权限
	private static List<String> dataRightTypeList=new ArrayList<String>();
	private static Map<String,String> dataRightTypeMap=new HashMap<String,String>();
	/**
	 * 系统初始化
	 * @param sc
	 * @param ac
	 * @throws Exception
	 */
	public static void init(ServletContext sc,ApplicationContext ac)
			throws Exception {
		// 更新工作目录
		if (sc != null) {
			ReportConstants.CONTEXT_REAL_PATH = sc
					.getRealPath(ReportConstants.FILE_SEPARATOR);
			ReportConstants.REP_TEMP_TXT = ReportConstants.CONTEXT_REAL_PATH
						+"WEB-INF"+ ReportConstants.FILE_SEPARATOR + "TXT"+ReportConstants.FILE_SEPARATOR;
		}
		LoggerHelper.info(SystemConfig.class,"report work file path init:"+ ReportConstants.REP_TEMP_TXT);
		// 临时目录
		File file = new File(ReportConstants.REP_TEMP_TXT);
		if (!file.exists())
			file.mkdirs();

		//数据库连接池初始化
		RepDatabaseDao dbdao=ac.getBean(RepDatabaseDao.class);
		ConnContainer.init(dbdao);
		/**
		//DimKey容器初始化
		RepDimKeyDao repDimKeyDao=ac.getBean(RepDimKeyDao.class);
		QueryKeyValueDao queryKeyValueDao=ac.getBean(QueryKeyValueDao.class);
		Map<String,DimKey> dimkeymap_init=new HashMap<String,DimKey>();
		StringBuilder sb=new StringBuilder();
		for(RepDimKey key :repDimKeyDao.findAllByTreeOrder()){
			DimKey dimkey=null;
			if(ReportConstants.DIM_KEY_DATATREE.equals(key.getDim_type()))
				dimkey=new DimDataTree(key,queryKeyValueDao.findList(key.getDatabase(),key.getValuedefine()),(DimDataTree) dimkeymap_init.get(key.getFkey()));
			else if(ReportConstants.DIM_KEY_SUBSTR.equals(key.getDim_type()))
				dimkey=new DimSubStr(key,(DimSubStr) dimkeymap_init.get(key.getFkey()));
			else 
				throw new ReportException(key.getDim_type()+" is no define or is null");
			dimkeymap_init.put(key.getKey(), dimkey);
			sb.append(key.getKey()).append(",");
		}
		DimKeyContainer.setDimkeymap(dimkeymap_init);
		LoggerHelper.info(DimKeyContainer.class, "DimKey Init:"+sb.toString());
		**/
		//启动清理系统临时缓存线程
		CacheManageClearIndex clearindexcache=new CacheManageClearIndex();
		clearindexcache.clearCache();
		clearindexcache.clearQueryLog();
		clearindexcache.clearUploadFile();
		clearindexcache.start();
		//实时查询清理
		CacheManageClearRealTime clearrealtimecache=new CacheManageClearRealTime();
		clearrealtimecache.start();
		//定时重载内存数据
		ReloadSystemMemory reloadMemory=new ReloadSystemMemory(ac);
		reloadMemory.start();
		
		
		
		//其他数据初始化
		initMemory(ac);
		
		//所有数据初始化完后，才能执行报表任务调度
		TaskExec taskexec=new TaskExec(ac.getBean(QueryComponent.class));
		taskexec.start();
		LoggerHelper.debug(SystemConfig.class, "task_exec_test#############################################");
		taskexec.exec();
	}
	/**
	 * 内存key列表
	 * @return
	 */
	public static Map<String, RepMemoryKey> getMemoryKeyMap() {
		return memoryKeyMap;
	}

	/**
	 * 初始化内存
	 * @param ac
	 * @throws Exception
	 */
	public static String initMemory(ApplicationContext ac) throws Exception{
		StringBuilder error_buffer=new StringBuilder();
		//报表配置
		RepOptrConfigrepDao configDao=ac.getBean(RepOptrConfigrepDao.class);
		for(RepOptrConfigrep config:configDao.findAll()){
			if(ReportConstants.OPTRCONFIG_OPTR.equals(config.getOptr_type())){
				optrConfigRepMap.put(config.getOptr_id(), null);
			}else if(ReportConstants.OPTRCONFIG_CLEAR_REP_CACHE.equals(config.getOptr_type())){
				int clear_rep_cache=Integer.parseInt(config.getOptr_id());
				if(clear_rep_cache>=ReportConstants.CLEAR_REP_CACHE)
					ReportConstants.CLEAR_REP_CACHE=clear_rep_cache;
				LoggerHelper.info(SystemConfig.class, ReportConstants.OPTRCONFIG_CLEAR_REP_CACHE+":"+ReportConstants.CLEAR_REP_CACHE);
			}else if(ReportConstants.OPTRCONFIG_WHEETMAXROWS.equals(config.getOptr_type())){
				int wheetmaxrows=Integer.parseInt(config.getOptr_id());
				if(wheetmaxrows>=ReportConstants.WHEETMINROWS&&wheetmaxrows<=ReportConstants.WHEETMAXROWS)
					ReportConstants.WHEETMAXROWS=wheetmaxrows;
				LoggerHelper.info(SystemConfig.class, ReportConstants.OPTRCONFIG_WHEETMAXROWS+":"+ReportConstants.WHEETMAXROWS);
			}else if(ReportConstants.OPTRCONFIG_WORKBOOKMAXROWS.equals(config.getOptr_type())){
				int workbookmaxrow=Integer.parseInt(config.getOptr_id());
				if(workbookmaxrow>=ReportConstants.WORKBOOKMINROWS&&workbookmaxrow<=ReportConstants.WORKBOOKMAXROWS)
					ReportConstants.WORKBOOKMAXROWS=workbookmaxrow;
				LoggerHelper.info(SystemConfig.class, ReportConstants.OPTRCONFIG_WORKBOOKMAXROWS+":"+ReportConstants.WORKBOOKMAXROWS);
			}else {
				LoggerHelper.error(SystemConfig.class, config.getOptr_type()+"找不到对应系统配置.");
				error_buffer.append("rep_optr_configrep:"+config.getOptr_type()+"找不到对应系统配置;");
			}
		}
		//内存关键字MAP，用于sql列ID转换
		RepMemoryKeyDao repMemoryKeyDao=ac.getBean(RepMemoryKeyDao.class);
		memoryKeyListt=repMemoryKeyDao.queryAllOrderbtRemark();
		memoryKeyMap=CollectionHelper.converToMapSingle(memoryKeyListt, "memory_key");
		 //初始化自定义内存值
		QueryKeyValueDao queryKeyValueDao=ac.getBean(QueryKeyValueDao.class);
		for(RepMemoryKey mk:memoryKeyListt){
			try{
				if(mk.getMemory_type().equals(ReportConstants.memory_my_define)){
					Map<String,String> valuemap=new HashMap<String,String>();
					for(QueryKeyValue vo: queryKeyValueDao.findList(mk.getDatabase(), mk.getValue_sql()))
							valuemap.put(vo.getId(), vo.getName());
					my_memory.put(mk.getMemory_key(), valuemap);
				}
			}catch(Exception e1){
				LoggerHelper.error(SystemConfig.class, "init_memory_key_error:"+mk.getMemory_key(),e1);
				error_buffer.append("rep_key_memory(").append(mk.getMemory_key()).append(")_init_error:").append(e1.getMessage()+";");
			}
		}
		
		//初始化数据字典
		SItemvalueDao sItemvalueDao = ac.getBean(SItemvalueDao.class);
		MemoryDict.setupData(sItemvalueDao.findAllViewDict());
		
		//初始化报表基础数据
		RepKeySystemDao repKeySystemDao=ac.getBean(RepKeySystemDao.class);
		systemList = repKeySystemDao.findAll();
		systemMap = CollectionHelper.converToMapSingle(systemList, "key");
		
		RepKeyLevelDao repKeyLevelDao=ac.getBean(RepKeyLevelDao.class);
		levelList = repKeyLevelDao.findAll();
		levelMap = CollectionHelper.converToMapSingle(levelList, "key");
		
		RepKeyConDao repKeyConDao=ac.getBean(RepKeyConDao.class);
		conList = repKeyConDao.findAllByOrder();
		conMap = CollectionHelper.converToMapSingle(conList, "key");
		conTree=repKeyConDao.findTree();
		dataRightTypeList=repKeyConDao.findroledatarighttypes();
		for(String key:dataRightTypeList)
			dataRightTypeMap.put(key, key);
		//SItemvalueDao sItemvalueDao=ac.getBean(SItemvalueDao.class);
		repType=sItemvalueDao.findViewDict("DEFINE_TYPE");
		repInfo=sItemvalueDao.findViewDict("DEFINE_INFO");
		repDataLevel=CollectionHelper.converToMapSingle(sItemvalueDao.findViewDict("SYS_LEVEL"),"item_value");
		
		RepDatabaseDao dbdao=ac.getBean(RepDatabaseDao.class);
		databaseList=dbdao.findAll();
		databaseMap=CollectionHelper.converToMapSingle(databaseList, "database");
		
		//列模板
		//templateKeyMap
		RepTemplateKeyDao templatekeydao=ac.getBean(RepTemplateKeyDao.class);
		templateKeyMap=AnalyseTemplateKey.initKeyMap(templatekeydao.findAll(), queryKeyValueDao,error_buffer);
		Iterator<String> it=templateKeyMap.keySet().iterator();
		List<String> temp_templateKeyList=new ArrayList<String>();
		//templateKeyList.clear();
		while(it.hasNext())
			temp_templateKeyList.add(it.next());
		templateKeyList=temp_templateKeyList;
		
		CubeManage cubeManage=ac.getBean(CubeManage.class);
		error_buffer.append(cubeManage.initAll());
		DimensionManage dimensionManage=ac.getBean(DimensionManage.class);
		error_buffer.append(dimensionManage.initAll());
		
		MemoryCacheInit mi=new DimensionLevelValueManage();
		error_buffer.append(mi.initAll());
		RepLevelManage repLevelManage=ac.getBean(RepLevelManage.class);
		error_buffer.append(repLevelManage.initAll());
		
//		Map<String,MemoryCacheInit> initmap=ac.getBeansOfType(MemoryCacheInit.class);
//		for(String o: initmap.keySet()){
//			System.out.println(o+" class="+initmap.get(o).getClass().getName());
//		}
		return error_buffer.toString();
	}
	public static List<RepKeyCon> getConList() {
		return conList;
	}

	public static Map<String, RepKeyCon> getConMap() {
		return conMap;
	}

	public static List<RepKeyLevel> getLevelList() {
		return levelList;
	}

	public static Map<String, RepKeyLevel> getLevelMap() {
		return levelMap;
	}

	public static List<RepKeySystem> getSystemList() {
		return systemList;
	}

	public static Map<String, RepKeySystem> getSystemMap() {
		return systemMap;
	}

	public static List<RepKeyCon> getConTree() {
		return conTree;
	}

	public static List<SItemvalue> getRepType() {
		return repType;
	}

	public static List<RepDatabase> getDatabaseList() {
		return databaseList;
	}

	public static Map<String, RepDatabase> getDatabaseMap() {
		return databaseMap;
	}

	public static List<SItemvalue> getRepInfo() {
		return repInfo;
	}
	
	public static Map<String, RepOptrConfigrep> getOptrConfigRepMap() {
		return optrConfigRepMap;
	}

	public static Map<String, SItemvalue> getRepDataLevel() {
		return repDataLevel;
	}
	public static List<RepMemoryKey> getMemoryKeyListt() {
		return memoryKeyListt;
	}
	public static Map<String, Map<String, String>> getMy_memory() {
		return my_memory;
	}
	public static Map<String, String> getTemplateKeyMap() {
		return templateKeyMap;
	}
	public static List<String> getTemplateKeyList() {
		return templateKeyList;
	}
	public static List<String> getDataRightTypeList() {
		return dataRightTypeList;
	}
	public static Map<String, String> getDataRightTypeMap() {
		return dataRightTypeMap;
	}
}
