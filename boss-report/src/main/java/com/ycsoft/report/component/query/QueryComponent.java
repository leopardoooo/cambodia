package com.ycsoft.report.component.query;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.stereotype.Component;

import com.google.gson.reflect.TypeToken;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.commons.abstracts.BaseComponent;
import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.commons.helper.DateHelper;
import com.ycsoft.commons.helper.JsonHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.daos.core.Pager;
import com.ycsoft.report.bean.RepDefine;
import com.ycsoft.report.bean.RepDetailData;
import com.ycsoft.report.bean.RepDetailDim;
import com.ycsoft.report.bean.RepDetailRow;
import com.ycsoft.report.bean.RepHead;
import com.ycsoft.report.bean.RepMyCube;
import com.ycsoft.report.bean.RepQueryLog;
import com.ycsoft.report.bean.RepTask;
import com.ycsoft.report.bean.RepTaskHis;
import com.ycsoft.report.commons.ReportConstants;
import com.ycsoft.report.commons.SystemConfig;
import com.ycsoft.report.dao.config.RepColumnDao;
import com.ycsoft.report.dao.config.RepDefineDao;
import com.ycsoft.report.dao.config.RepDetailDataDao;
import com.ycsoft.report.dao.config.RepDetailDimDao;
import com.ycsoft.report.dao.config.RepDetailRowDao;
import com.ycsoft.report.dao.config.RepGroupDao;
import com.ycsoft.report.dao.config.RepHeadDao;
import com.ycsoft.report.dao.config.RepMyCubeDao;
import com.ycsoft.report.dao.config.RepQueryLogDao;
import com.ycsoft.report.dao.config.RepSqlDao;
import com.ycsoft.report.dao.config.RepTaskDao;
import com.ycsoft.report.dao.config.RepTaskHisDao;
import com.ycsoft.report.dao.config.RepTotalDao;
import com.ycsoft.report.dao.keycon.RepDimKeyDao;
import com.ycsoft.report.dto.InitQueryDto;
import com.ycsoft.report.dto.ShowQueryDto;
import com.ycsoft.report.pojo.Parameter;
import com.ycsoft.report.query.QueryManage;
import com.ycsoft.report.query.QueryResultOlap;
import com.ycsoft.report.query.cube.CubeCell;
import com.ycsoft.report.query.cube.CubeExec;
import com.ycsoft.report.query.cube.CubeHeadCell;
import com.ycsoft.report.query.cube.Dimension;
import com.ycsoft.report.query.cube.DimensionRolap;
import com.ycsoft.report.query.cube.Measure;
import com.ycsoft.report.query.cube.detail.CubeDetail;
import com.ycsoft.report.query.cube.graph.CubeGraph;
import com.ycsoft.report.query.cube.graph.CubeGraphImpl;
import com.ycsoft.report.query.cube.graph.CubeGraphType;
import com.ycsoft.report.query.cube.graph.GraphData;
import com.ycsoft.report.query.cube.impl.CubeManage;
import com.ycsoft.report.query.cube.impl.DimensionManage;
import com.ycsoft.report.query.cube.impl.MyCube;
import com.ycsoft.report.query.cube.showclass.cellwarn.MeaWarn;
import com.ycsoft.report.query.datarole.BaseDataControl;
import com.ycsoft.report.query.datarole.FuncType;
import com.ycsoft.report.query.key.QueryKey;
import com.ycsoft.report.query.key.Impl.ConKeyValue;
import com.ycsoft.report.query.sql.QuerySql;

/**
 * 查询管理组件
 * 
 */
@Component
public class QueryComponent extends BaseComponent {

	private RepDefineDao repDefineDao;
	private RepQueryLogDao repQueryLogDao;
	private RepSqlDao repSqlDao;
	private RepHeadDao repHeadDao;
	/**
	 * @deprecated
	 */
	private RepColumnDao repColumnDao;
	private RepDimKeyDao repDimKeyDao;
	private RepTotalDao repTotalDao;
	private RepGroupDao repGroupDao;
	private QueryManage queryManage;
	private RepMyCubeDao repMyCubeDao;
	private CubeManage cubeManage;
	private QueryKey queryKey;
	private QuerySql querySql;
	private RepDetailDataDao repDetailDataDao;
	private CubeDetail cubeDetail;
	private RepDetailDimDao repDetailDimDao;
	private RepDetailRowDao repDetailRowDao;
	private RepTaskDao repTaskDao;
	private RepTaskHisDao repTaskHisDao;

	/**
	 * 刷新cube,维度度量拖拉后的刷新cube
	 * @param query_id
	 * @param dimlist
	 * @param vertdim
	 * @param meas
	 * @throws ReportException
	 */
	public void cubeRefush(String query_id,List<String> dimlist,String vertdim,List<String> meas) throws ReportException{
		QueryResultOlap olap= (QueryResultOlap)queryManage.get(query_id);
		CubeExec cube=olap.getCube();
		List<Dimension> list=new ArrayList<Dimension>();
		for(String o:dimlist){
			Dimension dim=DimensionManage.getDimension(o);
			if(dim!=null)
				list.add(dim);
			else
				throw new ReportException(dim+":dim is null or undefine");;
		}
		cube.selectDimension(DimensionManage.getDimension(vertdim), list.toArray(new Dimension[list.size()]));
		if(meas!=null&&meas.size()>0)
			cube.selectMeasure(meas.toArray(new String[meas.size()]));
		cube.execute(olap);
	}
	/**
	 * 进入cube的明细编辑模式
	 * 把所有的维度加入到cube中展现
	 * 把具有编辑模式指标的加入到cube中
	 * @throws ReportException 
	 */
	public void cubeEdit(String query_id) throws ReportException{
		QueryResultOlap olap= (QueryResultOlap)queryManage.get(query_id);
		CubeExec cube=olap.getCube();
		//判断指标是否有编辑模式，并把该指标加入到cube中
		Measure editmea=null;
		for(Measure mea: cube.getDefaultMeasures()){
			if(mea.getMeaCustom()==true)
				editmea=mea;
		}
		if(editmea==null)
			throw new ReportException("cube is not exist edit_mea.");
		boolean iseditmea=false;
		for(Measure mea:cube.getMeasures()){
			if(mea==editmea)
				iseditmea=true;
		}
		if(!iseditmea){
			String[] meaids=new String[cube.getMeasures().size()+1];
			for(int i=0;i<cube.getMeasures().size();i++)
				meaids[i]=cube.getMeasures().get(i).getColumnCode();
			meaids[cube.getMeasures().size()]=editmea.getColumnCode();
			cube.selectMeasure(meaids);
		}
		//把所有的维度加入到cube，
		List<DimensionRolap> rolaps=cube.getDimensionRolaps();
		Dimension[] dims=new Dimension[rolaps.size()];
		Dimension vertdim=null;
		for(int i=0;i<rolaps.size();i++){
			dims[i]=rolaps.get(i).getDim();
			if(rolaps.get(i).isUsesign()&&rolaps.get(i).isVerticalsign()){
				vertdim=dims[i];
			}
			//取消所有的合计项目
			cube.configDimensionTotal(dims[i]);
			//所有维度展开到最底层
			for(int j=rolaps.get(i).getLevel();j<=dims[i].getLevelNum();j++)
				cube.expandDimension(dims[i]);
		}
		cube.selectDimension(vertdim, dims);	
		//重新计算cube
		cube.execute(olap);
		
	}
	/**
	 * 更新一行可编辑报表记录
	 * @param rep_id
	 * @param optr_id
	 * @param context_id
	 * @param context_json
	 * @param headdatacells
	 * @return
	 * @throws ReportException
	 */
	public String updateOneCustom(String rep_id,String optr_id,String context_id,String context_json,List<CubeHeadCell> headdatacells ) throws ReportException{
		try {
			if(StringHelper.isNotEmpty(context_id)){
				repDetailDataDao.updateOneData(context_id, context_json);
				return context_id;
			}else{
				//生成可编辑明细报表的维度位置配置信息map
				List<RepDetailDim> dtaildims=repDetailDimDao.queryByRepId(rep_id);
				
				Map<Dimension,Integer> detaildimmap=new HashMap<Dimension,Integer>();
				for(RepDetailDim o:dtaildims){
					detaildimmap.put(DimensionManage.getDimension(o.getDim()), o.getIdx());
				}
				
				RepDetailData data= cubeDetail.customDimData(headdatacells, detaildimmap);
				repDetailDataDao.insertOneData(rep_id,optr_id, context_json, data);
				
				return data.getContext_id();
			}
		} catch (ReportException e) {
			throw e;
		}
	}
	/**
	 * 可编辑明细报表的列配置
	 * @param rep_id
	 * @return
	 * @throws ReportException
	 */
	public List<RepDetailRow> queryCustomRows(String rep_id) throws ReportException{
		return repDetailRowDao.queryRowsByRepid(rep_id);
	}

	/**
	 * 可编辑明细报表的内容根据页面坐标提取
	 * @param rep_id
	 * @param headdatacells
	 * @return
	 * @throws ReportException 
	 */
	public List<RepDetailData> cubeShowCustomDetail(String rep_id,List<CubeHeadCell> headdatacells) throws ReportException{
		try {
			//生成可编辑明细报表的维度位置配置信息map
			List<RepDetailDim> dtaildims=repDetailDimDao.queryByRepId(rep_id);
			
			Map<Dimension,Integer> detaildimmap=new HashMap<Dimension,Integer>();
			for(RepDetailDim o:dtaildims){
				detaildimmap.put(DimensionManage.getDimension(o.getDim()), o.getIdx());
			}
			//可编辑报表的明细中维度坐标查询语句
			String querysql=cubeDetail.customQuerySql(headdatacells, detaildimmap);
			return repDetailDataDao.queryDataByHeadCells(rep_id, querysql);
		} catch (ReportException e) {
			throw e;
		}
	}
	/**
	 * cube图形计算
	 * @param query_id
	 * @param graphtype
	 * @param dims
	 * @param meas
	 * @return
	 * @throws ReportException
	 */
	public GraphData cubeGraph(String query_id,String graphtype,String[] dims,String[] meas) throws ReportException{
		 try {
			if(StringHelper.isEmpty(query_id))
				throw new ReportException("query_id is null");
			if(meas==null||meas.length==0||StringHelper.isEmpty(meas[0]))
				throw new ReportException("mea is null");
			CubeGraphType type= CubeGraphType.valueOf(graphtype);
		    QueryResultOlap olap= (QueryResultOlap) queryManage.get(query_id);
		  
		    String title=repDefineDao.getRepDefine(olap.getRepId()).getRep_name();
		    
			CubeGraph graph=new CubeGraphImpl(type,title,olap.getDatabase(),meas[0],dims);
			return olap.getCube().executeGraph(olap, graph).getGraphData();
		} catch (JDBCException e) {
			throw new ReportException(e,e.getSQL());
		}		
	}
	/**
	 * 模板切换展现
	 * @param query_id
	 * @param cube_json
	 * @throws ReportException 
	 */
	public void cubeChangeMyCube(String query_id,String template_id) throws ReportException{
		try {
			if(StringHelper.isEmpty(query_id))
				throw new ReportException("query_id is null");
			if(StringHelper.isEmpty(template_id))
				throw new ReportException("template_id is null");
			RepMyCube repmycube= repMyCubeDao.queryMyCubeByTemplateId(template_id);
			String mycube_json=repmycube.getCube_json();
			MyCube mycube=JsonHelper.toObject(mycube_json, MyCube.class);
			
			
			
			//cube运算
			QueryResultOlap olap=(QueryResultOlap)queryManage.get(query_id);
			CubeExec cube=olap.getCube();
			cubeManage.setCube(cube, mycube);
			cube.execute(olap);
			//值警戒
			List<MeaWarn> warnlist=null;
			if(StringHelper.isNotEmpty(repmycube.getWarn_json())){
				Type type = new TypeToken<List<MeaWarn>>(){}.getType();
				warnlist=JsonHelper.toList(repmycube.getWarn_json(), type);
				olap.setMeawarns(warnlist);
			}
		} catch (Exception e) {
			throw new ReportException(e);
		}
	}
	/**
	 * 指标选择
	 * @param query_id
	 * @param meas
	 * @throws ReportException
	 */
	public void cubeMeaSelect(String query_id,String...meas) throws ReportException{
		QueryResultOlap olap= (QueryResultOlap)queryManage.get(query_id);
		CubeExec cube=olap.getCube();
		cube.selectMeasure(meas);
		cube.execute(olap);
	}
	/**
	 * 维选择
	 * @param query_id
	 * @param dimlist
	 * @param vertdim
	 * @throws ReportException
	 */
	public void cubeDimSelect(String query_id,List<String> dimlist,String vertdim) throws ReportException{
		QueryResultOlap olap= (QueryResultOlap)queryManage.get(query_id);
		CubeExec cube=olap.getCube();
		
		List<Dimension> list=new ArrayList<Dimension>();
		for(String o:dimlist){
			Dimension dim=DimensionManage.getDimension(o);
			if(dim!=null)
				list.add(dim);
			else
				throw new ReportException(dim+":dim is null or undefine");;
		}
		cube.selectDimension(DimensionManage.getDimension(vertdim), list.toArray(new Dimension[list.size()]));
		cube.execute(olap);
	}
	/**
	 * 维合计项目设置
	 * @param query_id
	 * @param dimtotalmap
	 * @throws ReportException
	 */
	public void cubeDimTotal(String query_id,Map<String,Integer[]> dimtotalmap) throws ReportException{
		QueryResultOlap olap= (QueryResultOlap)queryManage.get(query_id);
		CubeExec cube=olap.getCube();
		if(dimtotalmap==null) dimtotalmap=new HashMap<String,Integer[]>();
		for(DimensionRolap rolap:cube.getDimensionRolaps()){
			cube.configDimensionTotal(rolap.getDim(), dimtotalmap.get(rolap.getId()));
		}
		cube.execute(olap);
	}
	/**
	 * 维展开
	 * @param query_id
	 * @param dim
	 * @throws ReportException 
	 */
	public void cubeExpend(String query_id,String dim) throws ReportException{
		if(StringHelper.isEmpty(dim))
			throw new ReportException("dim is null");
		if(StringHelper.isEmpty(query_id))
			throw new ReportException("query_id is null");
		QueryResultOlap olap= (QueryResultOlap)queryManage.get(query_id);
		CubeExec cube=olap.getCube();
		cube.expandDimension(DimensionManage.getDimension(dim));
		cube.execute(olap);
	}
	/**
	 * 维收缩
	 * @param query_id
	 * @param dim
	 * @throws ReportException
	 */
	public void cubeShrink(String query_id,String dim) throws ReportException{
		if(StringHelper.isEmpty(dim))
			throw new ReportException("dim is null");
		if(StringHelper.isEmpty(query_id))
			throw new ReportException("query_id is null");
		QueryResultOlap olap= (QueryResultOlap)queryManage.get(query_id);
		CubeExec cube=olap.getCube();
		cube.shrinkDimension(DimensionManage.getDimension(dim));
		cube.execute(olap);
	}
	/**
	 * 维过滤切片
	 * @param query_id
	 * @param dim
	 * @param levelValueMap
	 * @throws ReportException 
	 */
	public void cubeSlices(String query_id,String dim,Integer level,String[] values) throws ReportException{
		if(StringHelper.isEmpty(query_id))
			throw new ReportException("query_id is null");
		if(StringHelper.isEmpty(dim))
			throw new ReportException("dim is null");
		if(level==null)
			throw new ReportException("level is null");
		
		if(values!=null&&values.length>0){
			for(int i=0;i<values.length;i++)
				if(StringHelper.isEmpty(values[i]))
				throw new ReportException("values["+i+"] is null");
		}
		QueryResultOlap olap= (QueryResultOlap)queryManage.get(query_id);
		CubeExec cube=olap.getCube();
		cube.slicesDimension(DimensionManage.getDimension(dim),level,values);
		cube.execute(olap);
	}
	/**
	 * 维自定义排序
	 * @param query_id
	 * @param dim
	 * @param levelValueMap
	 * @throws ReportException 
	 */
	public void cubeSort(String query_id,String dim,Map<Integer,String[]> sort_map) throws ReportException{
		if(StringHelper.isEmpty(query_id))
			throw new ReportException("query_id is null");
		if(StringHelper.isEmpty(dim))
			throw new ReportException("dim is null");
		
		QueryResultOlap olap= (QueryResultOlap)queryManage.get(query_id);
		CubeExec cube=olap.getCube();
		cube.sortDimension(DimensionManage.getDimension(dim), sort_map);
		cube.execute(olap);
	}
	/**
	 * 查询cube的olap表头
	 * @param query_id
	 * @param optr_id
	 * @return 
	 * @throws ReportException 
	 */
	public List<CubeHeadCell[]> queryOlapHeader(String query_id) throws ReportException{
		try {
			QueryResultOlap olap= (QueryResultOlap) queryManage.get(query_id);
			
			return olap.getHead();
		} catch (ReportException e) {
			throw e;
		}
	}
	
	/**
	 * 表头 名称查询
	 */
	public List<Object[]> queryHeader(String query_id) throws Exception {
		List<Object[]> list = new ArrayList<Object[]>();
		for (List<RepHead> replist : (List<List<RepHead>>)queryManage.get(query_id).getHead()) {
			Object[] objects = replist.toArray();
			list.add(objects);
		}
		return list;
	}

	/**
	 * 查询报表信息
	 */
	public Pager<List<String>> queryReport(String query_id, Integer start,
			Integer limit) throws Exception {
		return new Pager<List<String>>((List<List<String>>)queryManage.get(query_id)
				.getPage(start, limit), queryManage.get(query_id).getRowSize());
	}

	/**
	 * 查询报表信息
	 */
	public Pager<CubeCell[]> queryOlapReport(String query_id, Integer start,
			Integer limit) throws Exception {
		QueryResultOlap olap= (QueryResultOlap) queryManage.get(query_id);
		//olap.getPage(start, limit), olap.getRowSize()
		return new Pager<CubeCell[]>(olap.getPage(start, limit),olap.getRowSize());
	}
	
	/**
	 * 查询所有任务配置
	 * @param optr
	 * @return 
	 */
	public Pager<RepTask> queryRepTask(String query, Integer start, Integer limit,SOptr optr)throws ReportException{
		Pager<RepTask> page=repTaskDao.queryAllTask(query, start, limit);
		boolean editrep=BaseDataControl.getRole().hasFunc(FuncType.EDITREP);
		
		for(RepTask task: page.getRecords()){
			//能否删除和下载判断
			if(editrep||optr.getOptr_id().equals(task.getOptr_id())){
				task.setIs_delete(ReportConstants.VALID_T);
				if(task.getStatus().equals(ReportConstants.TASK_STATUS_EXECSUCESS)&&DateHelper.isToday(task.getExec_end_time())){
					task.setIs_dowload(ReportConstants.VALID_T);
				}
			}
			//星期转换
			if(task.getTask_type().equals(ReportConstants.TASK_TYPE_WEEK)){
				int execday=Integer.parseInt(task.getTask_execday())-1;
				task.setTask_execday(execday==0?"星期日":"星期"+execday);
			}
		}
		return page;
	}
	/**
	 * 查询几天前已执行的一次性任务
	 * @param day
	 * @return
	 * @throws ReportException
	 */
	public List<RepTask> queryTaskTypeIsOne(Integer day) throws ReportException{
		return repTaskDao.queryTaskTypeIsOne(day);
		
	}
	
	public List<RepTask> queryAllRepTask() throws ReportException{
		try {
			return repTaskDao.findAll();
		} catch (JDBCException e) {
			throw new ReportException(e);
		}
	}
	
	/**
	 * 删除一个任务配置
	 * @param task_id
	 * @throws ReportException
	 */
	public void deleteRepTask(Integer task_id)throws ReportException{
		try {
			RepTask task=repTaskDao.findByKey(task_id);
			if(task==null) return;
			if(task.getStatus().equals(ReportConstants.TASK_STATUS_EXEC)){
				throw new ReportException("正在执行的任务不能删除!");
			}
			RepTaskHis his=new RepTaskHis();
			BeanUtils.copyProperties(task,his);
			his.setKeylist("");
			repTaskHisDao.save(his);
			repTaskHisDao.updateKeylist(task.getTask_id(), task.getKeylist());
			
			repTaskDao.remove(task_id);
		} catch (JDBCException e) {
			throw new ReportException(e);
		}
	}
	/**
	 * 保存一个任务配置
	 * @param task
	 * @param para
	 * @param optr
	 * @throws ReportException
	 */
	public void saveTask(RepTask task,Parameter para,SOptr optr) throws ReportException{
		try {
			//数据验证(未实现)
			RepDefine rd=repDefineDao.getRepDefine(task.getRep_id());
			if(!rd.getRep_type().equals(ReportConstants.REP_TYPE_COMMON)
					||!SystemConfig.getDatabaseMap().get(rd.getDatabase()).getType().equals(ReportConstants.DATABASETYPE_HISTROY)){
				throw new ReportException("只有备份库普通报表支持任务配置！");
			}
			//页面条件取值，权限验证
			queryKey.checkKeyValueList(para.getRepkeys());
			task.setOptr_id(optr.getOptr_id());
			task.setStatus(ReportConstants.TASK_STATUS_WAITEXEC);
			task.setTask_id(Integer.parseInt(repTaskDao.findSequence().toString()));
			repTaskDao.save(task);
			repTaskDao.updateKeylist(task.getTask_id(), queryKey.toString(para.getRepkeys()));
		} catch (JDBCException e) {
			throw new ReportException(e);
		}
	}
	/**
	 * 查询可执行的任务列表
	 * @return
	 * @throws ReportException
	 */
	public RepTask queryCanExecTask() throws ReportException{
		try {
			return repTaskDao.queryExecTask();
		} catch (JDBCException e) {
			throw new ReportException(e);
		}
	}
	/**
	 * 执行任务
	 * @param task
	 * @throws ReportException
	 */
	public void execTask(RepTask task) throws ReportException{
		try{
			//更新任务状态
			task.setStatus(ReportConstants.TASK_STATUS_EXEC);
			task.setExec_start_time(new Date());
			repTaskDao.updateResult(task);
			//主报表cube的使用的查询条件
			Parameter para=new Parameter();
			if(StringHelper.isNotEmpty(task.getKeylist())){
				Map<String,String> valuemap= queryKey.toValueMap(task.getKeylist());
				List<ConKeyValue> keylist=new ArrayList<ConKeyValue>();
				Iterator<String> it=valuemap.keySet().iterator();
				while(it.hasNext()){
					ConKeyValue o=new ConKeyValue();
					String key=it.next();
					o.setKey(key);
					o.setValue(valuemap.get(key));
					keylist.add(o);
				}
				para.setRepkeys(keylist);
			}
			SOptr optr=new SOptr();
			optr.setOptr_id("admin");
			ShowQueryDto show=this.initQuery(task.getRep_id(), para, optr, "localhost");
			task.setExec_query_id(show.getQuery_id());
			task.setExec_result("成功");
			task.setExec_end_time(new Date());
			task.setStatus(ReportConstants.TASK_STATUS_EXECSUCESS);
			repTaskDao.updateResult(task);
		}catch(ReportException e){
			String error=e.getMessage();
			task.setStatus(ReportConstants.TASK_STATUS_EXECFFAILED);
			task.setExec_end_time(new Date());
			task.setExec_result(error!=null&&error.length()>1000?error.substring(0, 1000):error);
			repTaskDao.updateResult(task);
		}catch(Exception e){
			String error=e.getMessage();
			task.setStatus(ReportConstants.TASK_STATUS_EXECFFAILED);
			task.setExec_end_time(new Date());
			task.setExec_result(error!=null&&error.length()>1000?error.substring(0, 1000):error);
			repTaskDao.updateResult(task);
		}
	}
	/**
	 * 初始化查询
	 * @param repQueryDto
	 * @return query_id
	 * @throws ReportException
	 * @throws JDBCException
	 * @throws BeansException
	 */
	public ShowQueryDto initQuery(String rep_id, Parameter para,SOptr optr,String client_ip)
			throws ReportException {
		try{
			if (rep_id == null || rep_id.equals(""))
				throw new ReportException("rep_id is null");
			//页面条件取值，权限验证
			queryKey.checkKeyValueList(para.getRepkeys());
			//查询初始定义bean
			InitQueryDto initQueryDto = new InitQueryDto(repDefineDao.getRepDefine(rep_id),para);
			// 报表查询历史检测
			RepQueryLog repQueryLog = repQueryLogDao.getRepQueryLog(initQueryDto,optr.getOptr_id(),client_ip);
			// 创建查询
			queryManage.initQuery(initQueryDto, repQueryLog,optr.getOptr_id());
			repQueryLogDao.saveRepQuerLog(repQueryLog);
			return new ShowQueryDto(initQueryDto);
		}catch(ReportException e){
			throw e;
		} catch (JDBCException e) {
			throw new ReportException(e,e.getSQL());
		}
	}
	
	public RepDefineDao getRepDefineDao() {
		return repDefineDao;
	}

	public void setRepDefineDao(RepDefineDao repDefineDao) {
		this.repDefineDao = repDefineDao;
	}

	public RepQueryLogDao getRepQueryLogDao() {
		return repQueryLogDao;
	}

	public void setRepQueryLogDao(RepQueryLogDao repQueryLogDao) {
		this.repQueryLogDao = repQueryLogDao;
	}

	public RepSqlDao getRepSqlDao() {
		return repSqlDao;
	}

	public void setRepSqlDao(RepSqlDao repSqlDao) {
		this.repSqlDao = repSqlDao;
	}

	public RepHeadDao getRepHeadDao() {
		return repHeadDao;
	}

	public void setRepHeadDao(RepHeadDao repHeadDao) {
		this.repHeadDao = repHeadDao;
	}

	public RepTotalDao getRepTotalDao() {
		return repTotalDao;
	}

	public void setRepTotalDao(RepTotalDao repTotalDao) {
		this.repTotalDao = repTotalDao;
	}

	public RepDimKeyDao getRepDimKeyDao() {
		return repDimKeyDao;
	}

	public void setRepDimKeyDao(RepDimKeyDao repDimKeyDao) {
		this.repDimKeyDao = repDimKeyDao;
	}

	public RepColumnDao getRepColumnDao() {
		return repColumnDao;
	}

	public void setRepColumnDao(RepColumnDao repColumnDao) {
		this.repColumnDao = repColumnDao;
	}
	public QueryManage getQueryManage() {
		return queryManage;
	}

	public void setQueryManage(QueryManage queryManage) {
		this.queryManage = queryManage;
	}

	public RepGroupDao getRepGroupDao() {
		return repGroupDao;
	}

	public void setRepGroupDao(RepGroupDao repGroupDao) {
		this.repGroupDao = repGroupDao;
	}
	public void setRepMyCubeDao(RepMyCubeDao repMyCubeDao) {
		this.repMyCubeDao = repMyCubeDao;
	}
	public CubeManage getCubeManage() {
		return cubeManage;
	}
	public void setCubeManage(CubeManage cubeManage) {
		this.cubeManage = cubeManage;
	}
	public RepMyCubeDao getRepMyCubeDao() {
		return repMyCubeDao;
	}
	public QueryKey getQueryKey() {
		return queryKey;
	}
	public void setQueryKey(QueryKey queryKey) {
		this.queryKey = queryKey;
	}
	public QuerySql getQuerySql() {
		return querySql;
	}
	public void setQuerySql(QuerySql querySql) {
		this.querySql = querySql;
	}
	public CubeDetail getCubeDetail() {
		return cubeDetail;
	}
	public void setCubeDetail(CubeDetail cubeDetail) {
		this.cubeDetail = cubeDetail;
	}
	public RepDetailDataDao getRepDetailDataDao() {
		return repDetailDataDao;
	}
	public void setRepDetailDataDao(RepDetailDataDao repDetailDataDao) {
		this.repDetailDataDao = repDetailDataDao;
	}
	public RepDetailDimDao getRepDetailDimDao() {
		return repDetailDimDao;
	}
	public void setRepDetailDimDao(RepDetailDimDao repDetailDimDao) {
		this.repDetailDimDao = repDetailDimDao;
	}
	public RepDetailRowDao getRepDetailRowDao() {
		return repDetailRowDao;
	}
	public void setRepDetailRowDao(RepDetailRowDao repDetailRowDao) {
		this.repDetailRowDao = repDetailRowDao;
	}
	
	public RepTaskDao getRepTaskDao() {
		return repTaskDao;
	}
	public void setRepTaskDao(RepTaskDao repTaskDao) {
		this.repTaskDao = repTaskDao;
	}
	public RepTaskHisDao getRepTaskHisDao() {
		return repTaskHisDao;
	}
	public void setRepTaskHisDao(RepTaskHisDao repTaskHisDao) {
		this.repTaskHisDao = repTaskHisDao;
	}
}
