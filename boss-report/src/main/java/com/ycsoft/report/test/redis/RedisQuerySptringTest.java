package com.ycsoft.report.test.redis;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ycsoft.report.commons.ReportConstants;
import com.ycsoft.report.dao.config.RepDatabaseDao;
import com.ycsoft.report.dao.config.RepQueryLogDao;
import com.ycsoft.report.db.ConnContainer;
import com.ycsoft.report.dto.InitQueryDto;
import com.ycsoft.report.query.QueryResultOlap;
import com.ycsoft.report.query.cube.CubeCell;
import com.ycsoft.report.query.cube.CubeExec;
import com.ycsoft.report.query.cube.impl.CubeManage;
import com.ycsoft.report.query.cube.impl.DimensionLevelValueManage;
import com.ycsoft.report.query.cube.impl.DimensionManage;
import com.ycsoft.report.query.datarole.RepLevelManage;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:app*.xml")
public class RedisQuerySptringTest {
	
	@Autowired
	private RepDatabaseDao repDatabaseDao;
	@Autowired
	private RepLevelManage repLevelManage;
	@Autowired
	private DimensionManage dimensionManage;
	@Autowired
	private CubeManage cubeManage;
	@Before
	public void initSystem()throws Exception{
		//数据源
		ConnContainer.init(repDatabaseDao);
		//cube定义缓存
		System.out.println(cubeManage.initAll());
		//维度定义
		System.out.println(dimensionManage.initAll());
		//维度层级定义
		System.out.println(new DimensionLevelValueManage().initAll());
		//数据权限定义
		System.out.println(repLevelManage.initAll());
	}
	@Autowired
	private RepQueryLogDao repQueryLogDao;
	@Test
	/**
	 * 测试cube查询简单用例
	 */
	public void testCubeQuery() throws Exception{
		InitQueryDto initQueryDto=new InitQueryDto();
		initQueryDto.setQuery_id(repQueryLogDao.findSequence().toString());
		initQueryDto.setDatabase(ReportConstants.DATABASE_SYSTEM);
		initQueryDto.setRep_type(ReportConstants.REP_TYPE_OLAP);
		initQueryDto.setRep_id("15112");
		initQueryDto.setSql("select to_char(cf.create_time,'yyyymm') month_id,cf.dept_id,cf.acctitem_id prod_id,sum(cf.real_pay)/100 fee "
				+" from busi.c_fee cf where cf.create_time>sysdate-20 and cf.acctitem_id is not null "
				+" group by to_char(cf.create_time,'yyyymm') ,cf.dept_id,cf.acctitem_id");
		QueryResultOlap olap=new QueryResultOlap(initQueryDto.getQuery_id(),initQueryDto,initQueryDto.getSql(),initQueryDto.getCache_query_id());
		CubeExec cube=cubeManage.getCube(initQueryDto.getRep_id());
		cube.execute(olap);
		
		for(CubeCell[] cells:olap.getPage(0, 100)){
			System.out.println();
			for(CubeCell cell:cells){
				System.out.print(cell.getId());
				System.out.print("-");
				System.out.print(cell.getName());
				System.out.print(" ");
			}
			System.out.println();
		}
	}
	
	
	
	
}
