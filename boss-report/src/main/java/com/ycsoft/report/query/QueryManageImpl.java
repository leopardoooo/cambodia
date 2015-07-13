package com.ycsoft.report.query;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gson.reflect.TypeToken;
import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.commons.helper.JsonHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.core.JDBCException;
import com.ycsoft.report.bean.RepMyCube;
import com.ycsoft.report.bean.RepQueryLog;
import com.ycsoft.report.commons.FileObjectOutputStream;
import com.ycsoft.report.commons.ReportConstants;
import com.ycsoft.report.dao.config.RepGroupDao;
import com.ycsoft.report.dao.config.RepHeadDao;
import com.ycsoft.report.dao.config.RepMyCubeDao;
import com.ycsoft.report.dao.config.RepQueryLogDao;
import com.ycsoft.report.dao.config.RepSqlDao;
import com.ycsoft.report.dao.config.RepTotalDao;
import com.ycsoft.report.dao.redis.JedisDao;
import com.ycsoft.report.dto.InitQueryDto;
import com.ycsoft.report.query.cube.CubeExec;
import com.ycsoft.report.query.cube.CubeHeadCell;
import com.ycsoft.report.query.cube.detail.CubeDetail;
import com.ycsoft.report.query.cube.impl.CubeManage;
import com.ycsoft.report.query.cube.impl.MyCube;
import com.ycsoft.report.query.cube.showclass.cellwarn.MeaWarn;
import com.ycsoft.report.query.key.QueryKey;
import com.ycsoft.report.query.key.Impl.ConKeyValue;
import com.ycsoft.report.query.redis.QueryResultOlapRedis;
import com.ycsoft.report.query.sql.AnalyseMemoryKey;
import com.ycsoft.report.query.sql.AnalyseSqlFactory;
import com.ycsoft.report.query.sql.QuerySql;
import com.ycsoft.report.query.treequery.DimKeyContainer;

public class QueryManageImpl implements QueryManage {

	private  CubeManage cubeManage;
	private RepMyCubeDao repMyCubeDao;
	private RepQueryLogDao repQueryLogDao;
	private CubeDetail cubeDetail;
	private QueryKey queryKey;
	private RepHeadDao repHeadDao;
	private RepTotalDao repTotalDao;
	private RepGroupDao repGroupDao;
	private RepSqlDao repSqlDao;
	private QuerySql querySql;
	private JedisDao jedisDao;
	
	/**
	 * 创建cube的查询
	 * @param initQueryDto
	 * @param optr_id
	 * @return
	 * @throws ReportException
	 */
	private QueryResult createQueryResultOlap(InitQueryDto initQueryDto,String optr_id) throws ReportException{
		try {
			if(StringHelper.isNotEmpty(initQueryDto.getCache_query_id())){
				//存在缓存则设置新的查询id
				initQueryDto.setQuery_id(repQueryLogDao.findSequence().toString());
			}
			
			//QueryResultOlap olap=new QueryResultOlap(initQueryDto.getQuery_id(),initQueryDto,initQueryDto.getSql(),initQueryDto.getCache_query_id());
			QueryResultOlap olap=new QueryResultOlapRedis(initQueryDto.getQuery_id(),initQueryDto,initQueryDto.getSql(),initQueryDto.getCache_query_id(),this.jedisDao);
			//查询有效模板
			RepMyCube repmycube=repMyCubeDao.queryDefaultShowCube(initQueryDto.getRep_id(), optr_id);
			if(repmycube==null){
				CubeExec cube=cubeManage.getCube(initQueryDto.getRep_id());
				if(cube==null) throw new ReportException("cube no config!");
				cube.execute(olap);
			}else{
				//使用模板初始化cube
				MyCube mycube=JsonHelper.toObject(repmycube.getCube_json(), MyCube.class);
				CubeExec cube=cubeManage.createCube(initQueryDto.getRep_id(),mycube);
				cube.execute(olap);
				//值警戒
				List<MeaWarn> warnlist=null;
				if(StringHelper.isNotEmpty(repmycube.getWarn_json())){
					Type type = new TypeToken<List<MeaWarn>>(){}.getType();
					warnlist=JsonHelper.toList(repmycube.getWarn_json(), type);
					olap.setMeawarns(warnlist);
				}
			}
			return olap;
		}catch(ReportException e){
			throw e;
		} catch (Exception e) {
			throw new ReportException("error",e);
		}
	}
	/**
	 * 创建cube明细查询
	 * @param initQueryDto
	 * @param optr_id
	 * @return
	 * @throws ReportException
	 */
	private QueryResult createQueryCubeDetail(InitQueryDto initQueryDto) throws ReportException{
		try{
			if(StringHelper.isEmpty(initQueryDto.getHistory_query_id()))
				throw new ReportException("cube_detail:cube_query_id is null");
			RepQueryLog log= repQueryLogDao.getRepQuerLog(initQueryDto.getHistory_query_id());
			if(log==null)
				throw new ReportException("cube_detail:cube_query_id is not right");
			//报表合计项目
			initQueryDto.setTotals(repTotalDao.findTotals(initQueryDto.getRep_id()));
			
			String cube_sql=initQueryDto.getSql();
			//主报表cube的使用的查询条件
			if(StringHelper.isNotEmpty(log.getKeylist())){
				Map<String,String> valuemap= queryKey.toValueMap(log.getKeylist());
				List<ConKeyValue> keylist=new ArrayList<ConKeyValue>();
				Iterator<String> it=valuemap.keySet().iterator();
				while(it.hasNext()){
					ConKeyValue o=new ConKeyValue();
					String key=it.next();
					o.setKey(key);
					o.setValue(valuemap.get(key));
					keylist.add(o);
				}
				cube_sql=new AnalyseSqlFactory(cube_sql).getAnaSql(keylist);
			}
			//headcell处理过程
			List<CubeHeadCell> cells=new ArrayList<CubeHeadCell>();
			cells.addAll(initQueryDto.getHeaddatacells());
			QueryResultOlap olap=(QueryResultOlap) this.get(initQueryDto.getHistory_query_id());
			List<ConKeyValue> keylist =cubeDetail.createCubeDetailKeys(olap.getCube(),cells);
			cube_sql=new AnalyseSqlFactory(cube_sql).getCubeDetail(keylist);
			initQueryDto.setSql(cube_sql);
			return new QueryResultCommon(initQueryDto);
		}catch(JDBCException e){
			throw new ReportException(e,e.getSQL());
		} catch (ReportException e) {
			throw e;
		}
	}
	/**
	 * 通用报表初始化查询处理
	 * @param initQueryDto
	 * @return
	 * @throws ReportException
	 */
	private QueryResult createCommonQuery(InitQueryDto initQueryDto) throws ReportException{
		try{
			initQueryDto.setHeadlist(repHeadDao.getRepHeadList(initQueryDto.getRep_id()));
			//合计项目设置
			initQueryDto.setTotals(repTotalDao.findTotals(initQueryDto.getRep_id()));
			//分组小计设置
			initQueryDto.setGroup(repGroupDao.findColumnName(initQueryDto.getRep_id()));
			return new QueryResultCommon(initQueryDto);
		} catch (JDBCException e) {
			throw new ReportException(e,e.getSQL());
		} catch (ReportException e) {
			throw e;
		}
	}
	/**
	 * sql提取转换组装
	 * @param initQueryDto
	 * @return
	 * @throws ReportException
	 */
	private String translateSql(InitQueryDto initQueryDto) throws ReportException{
		try {
			String sql = repSqlDao.getSql(initQueryDto.getRep_id());
			sql=querySql.translateTemplateKey(sql);
			//内存列转换
			sql=AnalyseMemoryKey.translateColumnToKey(sql);
			//组装sql
			sql=new AnalyseSqlFactory(sql).getAnaSql(initQueryDto.getKeyvaluelist());
			return sql;
		} catch (JDBCException e) {
			throw new ReportException(e,e.getSQL());
		}
	}
	//private Map<String,String> 
	public RepQueryLog initQuery(InitQueryDto initQueryDto,RepQueryLog repQueryLog,String optr_id)
			throws ReportException {
		try{
			QueryResult qr = QueryContainer.getRepQuery(initQueryDto.getQuery_id());
			if (qr == null) {
				initQueryDto.setSql(translateSql(initQueryDto));
				if(initQueryDto.getRep_type().equals(ReportConstants.REP_TYPE_OLAP)){
					qr=createQueryResultOlap(initQueryDto,optr_id);
					//cube查询，不需要12小时持久缓存，*_base 对应历史查询需要缓存，非该文件不需要持久缓存
					QueryContainer.addRepQuery(qr,ReportConstants.VALID_F);
				}else if(initQueryDto.getRep_type().equals(ReportConstants.REP_TYPE_OLAP_DETAIL)){
					//cube明细报表处理
					qr=createQueryCubeDetail(initQueryDto);
					//不缓存查询
					repQueryLog.setIsvalid(ReportConstants.VALID_F);
					QueryContainer.addRepQuery(qr,repQueryLog.getIsvalid());
				}else{
					//老报表处理 含普通老报表和快译报表处理
					qr=createCommonQuery(initQueryDto);
					QueryContainer.addRepQuery(qr,repQueryLog.getIsvalid());
				}
			}
			repQueryLog.setQuerynum((qr.getRowSize() - 1));
			repQueryLog.setQuerytime( (int)(System.currentTimeMillis()- repQueryLog.getCreate_date().getTime()));
			return repQueryLog;
		}catch(ReportException e){
			throw e;
		}
	}

	public QueryResult get(String query_id) throws ReportException {
		QueryResult qr = QueryContainer.getRepQuery(query_id);
		if (qr == null)
			throw new ReportException("report is no_init or is destroyed.");
		qr.updateVisitDate();
		return qr;
	}


	public void delete(String query_id) {
		QueryResult queryResult=QueryContainer.getRepQuery(query_id);
		QueryContainer.deleteRepQuery(queryResult);
	}

	/**
	 * 初始化快逸设计用数据
	 * testdata：true使用测试数据，false不使用测试数据
	 */
	public RepQueryLog initRaqQuery(InitQueryDto initQueryDto,
			RepQueryLog repQueryLog,boolean testdata) throws ReportException {
		FileObjectOutputStream foo=null;
		try {
			if(testdata&&initQueryDto.getKeyvaluelist()!=null){
				for(ConKeyValue keydto:initQueryDto.getKeyvaluelist())
					keydto.setValue("");//keydto.getTestvalue());
			}
			repQueryLog=initQuery(initQueryDto,repQueryLog,repQueryLog.getOptr_id());
			
			foo=new FileObjectOutputStream(ReportConstants.REP_TEMP_TXT+initQueryDto.getQuery_id()+ReportConstants.INDEX);
		    //写入查询结果集
			foo.writeObject(this.get(initQueryDto.getQuery_id()));
		    //写入查询维度容器
		    foo.writeObject(DimKeyContainer.getDimkeymap());
		    
			return repQueryLog;
		} catch (Exception e) {
			throw new ReportException(e);
		}finally{
			try {
				if (foo != null){
					foo.close();
					foo=null;
				}
			} catch (Exception e1) {
			}
		}
		
	}

	public CubeDetail getCubeDetail() {
		return cubeDetail;
	}

	public void setCubeDetail(CubeDetail cubeDetail) {
		this.cubeDetail = cubeDetail;
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

	public RepGroupDao getRepGroupDao() {
		return repGroupDao;
	}

	public void setRepGroupDao(RepGroupDao repGroupDao) {
		this.repGroupDao = repGroupDao;
	}

	public RepSqlDao getRepSqlDao() {
		return repSqlDao;
	}

	public void setRepSqlDao(RepSqlDao repSqlDao) {
		this.repSqlDao = repSqlDao;
	}

	public QuerySql getQuerySql() {
		return querySql;
	}

	public void setQuerySql(QuerySql querySql) {
		this.querySql = querySql;
	}
	public JedisDao getJedisDao() {
		return jedisDao;
	}

	public void setJedisDao(JedisDao jedisDao) {
		this.jedisDao = jedisDao;
	}

	public QueryKey getQueryKey() {
		return queryKey;
	}

	public void setQueryKey(QueryKey queryKey) {
		this.queryKey = queryKey;
	}

	public RepQueryLogDao getRepQueryLogDao() {
		return repQueryLogDao;
	}

	public void setRepQueryLogDao(RepQueryLogDao repQueryLogDao) {
		this.repQueryLogDao = repQueryLogDao;
	}

	public RepMyCubeDao getRepMyCubeDao() {
		return repMyCubeDao;
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
}
