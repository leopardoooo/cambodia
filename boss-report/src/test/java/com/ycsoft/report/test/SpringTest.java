package com.ycsoft.report.test;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.google.gson.Gson;
import com.ycsoft.beans.system.SOptr;
import com.ycsoft.commons.helper.JsonHelper;
import com.ycsoft.report.bean.RepColumn;
import com.ycsoft.report.bean.RepCube;
import com.ycsoft.report.bean.RepDimKey;
import com.ycsoft.report.bean.RepHead;
import com.ycsoft.report.bean.RepQueryLog;
import com.ycsoft.report.commons.FileObjectInputStream;
import com.ycsoft.report.commons.ReportConstants;
import com.ycsoft.report.commons.SystemConfig;
import com.ycsoft.report.component.query.KeyComponent;
import com.ycsoft.report.component.query.QueryComponent;
import com.ycsoft.report.component.query.RepDesignComponent;
import com.ycsoft.report.dao.config.RepColumnDao;
import com.ycsoft.report.dao.config.RepDatabaseDao;
import com.ycsoft.report.dao.config.RepQueryLogDao;
import com.ycsoft.report.dao.config.RepSqlDao;
import com.ycsoft.report.db.ConnContainer;
import com.ycsoft.report.dto.InitQueryDto;
import com.ycsoft.report.dto.RepKeyDto;
import com.ycsoft.report.pojo.Parameter;
import com.ycsoft.report.query.QueryManage;
import com.ycsoft.report.query.QueryManageImpl;
import com.ycsoft.report.query.QueryResult;
import com.ycsoft.report.query.QueryResultCommon;
import com.ycsoft.report.query.cube.Dimension;
import com.ycsoft.report.query.cube.DimensionType;
import com.ycsoft.report.query.cube.MeasureDataType;
import com.ycsoft.report.query.cube.impl.CubeManage;
import com.ycsoft.report.query.cube.impl.DimensionManage;
import com.ycsoft.report.query.cube.impl.MyCube;
import com.ycsoft.report.query.key.Impl.QueryKeyValue;
import com.ycsoft.report.query.sql.AnalyseMemoryKey;
import com.ycsoft.report.query.treequery.DimKeyContainer;
import com.ycsoft.report.test.other.TestConKill;
import com.ycsoft.report.test.other.ZipUtil;


@ContextConfiguration(locations={"classpath*:app*.xml"})
public class SpringTest extends AbstractTransactionalJUnit4SpringContextTests {
	
	
	private void initSystem()throws Exception{
	    SystemConfig.init(null,this.applicationContext);
	    
	}
		
	private void initDatabase()throws Exception{
		RepDatabaseDao dbdao=this.applicationContext.getBean(RepDatabaseDao.class);
		ConnContainer.init(dbdao);
	}
	

	@Test
	public void testZip()throws Exception{
		
		RepQueryLogDao dao=this.applicationContext.getBean(RepQueryLogDao.class);
		
		RepQueryLog log=dao.getRepQuerLog("386808");
		String a=ZipUtil.compress(log.getKeylist());
		
		System.out.println(a.length()+" "+log.getKeylist().length()+" "+a.length()*100/log.getKeylist().length());
		System.out.println(a);
		System.out.println(ZipUtil.uncompress(a));
		System.out.println(log.getKeylist());
		
		
	}
	@Test
	public void test12231(){
		System.out.print(MeasureDataType.valueOf("ad"));
	}
	
	@Test
	public void testAA()throws Exception{
		initSystem();
		System.out.println(
		org.apache.struts2.json.JSONUtil.serialize(
				DimensionManage.getDimension("month")))
		;
		System.out.println(JsonHelper.
				fromObject(DimensionManage.getDimension("month")));
	}
	/**
	 * 测试表头加载
	 * @throws Exception
	 */
	@Test
	public void queryOlapHeader()throws Exception{
		initSystem();
		
		
	}
	
	@Test
	public void TestKill()throws Exception{
		initDatabase();
		TestConKill ki=new TestConKill("system");
		System.out.println(2);
		ki.start();
		System.out.println(3);
		Thread.sleep(1000*1);
		System.out.println(4);
		ki.ConKill();
		System.out.println(5);
		Thread.sleep(100000*1);
		
	}
	@Test
	public void TestCube()throws Exception{
		
		initSystem();
		RepDesignComponent repDesignComponent=this.applicationContext.getBean(RepDesignComponent.class);
		//
		System.out.println("queryCubeDimensionTypes测试");
		for(QueryKeyValue vo:repDesignComponent.queryCubeDimensionTypes()){
			System.out.println(vo.getId()+" "+vo.getName());
		}
		System.out.println("queryMeasureTypes");
		for(QueryKeyValue vo:repDesignComponent.queryMeasureTypes()){
			System.out.println(vo.getId()+" "+vo.getName());
		}
		System.out.println("queryDimensions测试");
		
		//测试cube保存
		
		List<RepCube> repcubes=new ArrayList<RepCube>();
		String rep_id="10392";
		String optr_id="test1";
		
		RepCube rc=new RepCube();
		rc.setRep_id(rep_id);
		rc.setColumn_code("cust_colony");
		rc.setColumn_type(DimensionType.crosswise.name());
		rc.setColumn_define("custcolony");
		rc.setColumn_as("");
		repcubes.add(rc);
		
		RepCube b=new RepCube();
		b.setRep_id(rep_id);
		b.setColumn_code("客户数");
		b.setColumn_type(DimensionType.measure.name());
		b.setColumn_define("COUNT");
		b.setColumn_as("数量");
		repcubes.add(b);
		System.out.println("saveCuleDefine测试");
		repDesignComponent.saveCuleDefine(rep_id, optr_id, repcubes);
		//
	
	}
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void TestCubeNavigate()throws Exception{
		initSystem();
		QueryComponent queryComponent=this.applicationContext.getBean(QueryComponent.class);
		System.out.println("queryMyCube测试");
		String rep_id="10393";
		String optr_id="aa";
		MyCube mycube= null;//queryComponent.queryMyCube(rep_id, optr_id);
		//System.out.println(mycube.getHeadishidden());
//		for(MyDimension mydim:mycube.getDimensions())
//			System.out.println("ID:"+mydim.getId()+" Name:"+mydim.getName()+" Level:"+mydim.getLevel()+" Slices:"+mydim.getSlices()
//				+" Usesign:"+mydim.isUsesign()+" Verticalsign"+mydim.isVerticalsign());
		System.out.println("queryOlapHeader测试");
		
		
		KeyComponent keyComponent=this.applicationContext.getBean(KeyComponent.class);
		System.out.println("queryLevels测试");
//		for(QueryKeyValue vo: keyComponent.queryLevels("custcolony")){
//			System.out.println(vo.getId()+" "+vo.getName());
//		}
//		for(QueryKeyValue vo: keyComponent.queryLevels("district")){
//			System.out.println(vo.getId()+" "+vo.getName());
//		}
		
		
		System.out.println("queryLevelValues测试");
		for(QueryKeyValue vo: keyComponent.queryLevelValues("district",1)){
			System.out.println(vo.getId()+" "+vo.getName()+" "+vo.getPid());
		}
		for(QueryKeyValue vo: keyComponent.queryLevelValues("district",2)){
			System.out.println(vo.getId()+" "+vo.getName()+" "+vo.getPid());
		}
	}
	
	
	@Test
	public void TestCubeSql()throws Exception{
		CubeManage cubeManage=this.applicationContext.getBean(CubeManage.class);
		
		//Cube<QueryResultQuiee> cube=(Cube<QueryResultQuiee>) cubeManage.getCube("aa");
		
		 // System.out.println(cube.assembleSql(""));
	}
}
