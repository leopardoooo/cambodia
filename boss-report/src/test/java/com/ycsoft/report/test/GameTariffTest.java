package com.ycsoft.report.test;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = "classpath:applicationContext-*.xml")
public class GameTariffTest {
//	@Autowired
//	public GameTariffComponent gameTariffComponent;
//	@Autowired
//	public GameTariffDao gameTariffDao;
//	@Autowired
//	public GameDao gameDao;
//
//	Member member = new Member();
//	SOptr optr = new SOptr();
//	GameTariff gameTariff = new GameTariff();
//	
//	@Before
//	public void testWired(){
//		gameTariff.setGame_id(1);
//		gameTariff.setTariff_name("name" + (int)Math.random()*100 + 1);
//		gameTariff.setBegin_time("08:30:00");
//		gameTariff.setEnd_time("10:00:00");
//		gameTariff.setCredit(10);
//		gameTariff.setLess_than_smallest(LessThanSmallest.on1carry.toString());
//		gameTariff.setSmallest_billing_unit(60);
//		gameTariff.setStatus("ACTIVE");
//								
//		optr.setDept_id("11");
//		optr.setOptr_id("12");
//		
//		member.setMember_id(11);
//		
//		Assert.assertNotNull(gameTariffComponent);		
//	}
//
//	/**
//	 * 测试游戏优惠资费名称为空
//	 */
//	@Test(expected=ComponentException.class)
//	public void testTariffName_IsNull() throws Exception{
//		try{
//			gameTariff.setTariff_name("");
//			gameTariffComponent.addGameTariff(gameTariff, optr);
//			}catch(Exception e){
//				if(!"资费名称不能为空".equals(e.getMessage()))
//					throw new Exception("测试资费名称不能为空失败");
//				else
//					throw e;
//			}
//	}
//
//	/**
//	 * 测试游戏编号为空
//	 */
//	@Test(expected=ComponentException.class)
//	public void testGameId_IsNull() throws Exception{
//		try{
//			gameTariff.setGame_id(null);
//			gameTariffComponent.addGameTariff(gameTariff, optr);
//			}catch(Exception e){
//				if(!"游戏编号不能为空".equals(e.getMessage()))
//					throw new Exception("测试游戏编号不能为空失败");
//				else
//					throw e;
//			}
//	}
//
//	/**
//	 * 测试游戏编号对应的游戏不存在
//	 */
//	@Test(expected=ComponentException.class)
//	public void testGameId_NotExist() throws Exception{
//		try{
//			gameTariff.setGame_id(6);
//			gameTariffComponent.addGameTariff(gameTariff, optr);
//			}catch(Exception e){
//				if(!"游戏编号不存在".equals(e.getMessage()))
//					throw new Exception("测试游戏编号不存在失败");
//				else
//					throw e;
//			}
//	}
//
//
//	/**
//	 * 测试开始时间不能为空
//	 */
//	@Test(expected=ComponentException.class)
//	public void testBeginTime_IsNull() throws Exception{
//		try{
//			gameTariff.setBegin_time("");
//			gameTariffComponent.addGameTariff(gameTariff, optr);
//			}catch(Exception e){
//				if(!"开始时间不能为空".equals(e.getMessage()))
//					throw new Exception("测试开始时间不能为空失败");
//				else
//					throw e;
//			}
//	}
//
//	/**
//	 * 测试结束时间不能为空
//	 */
//	@Test(expected=ComponentException.class)
//	public void testEndTime_IsNull() throws Exception{
//		try{
//			gameTariff.setEnd_time("");
//			gameTariffComponent.addGameTariff(gameTariff, optr);
//			}catch(Exception e){
//				if(!"结束时间不能为空".equals(e.getMessage()))
//					throw new Exception("测试结束时间不能为空失败");
//				else
//					throw e;
//			}
//	}
//
//	/**
//	 * 测试小于计费单位处理方式不能为空
//	 */
//	@Test(expected=ComponentException.class)
//	public void testLessThanSmallest_IsNull() throws Exception{
//		try{
//			gameTariff.setLess_than_smallest(null);
//			gameTariffComponent.addGameTariff(gameTariff, optr);
//			}catch(Exception e){
//				if(!"小于计费单位处理方式不能为空".equals(e.getMessage()))
//					throw new Exception("测试小于计费单位处理方式不能为空失败");
//				else
//					throw e;
//			}
//	}
//
//	/**
//	 * 测试最小计算单位不能小于0
//	 */
//	@Test(expected=ComponentException.class)
//	public void testSmallest_billing_unit_IsNull() throws Exception{
//		try{
//			gameTariff.setSmallest_billing_unit(null);
//			gameTariffComponent.addGameTariff(gameTariff, optr);
//			}catch(Exception e){
//				if(!"最小计算单位不能小于0".equals(e.getMessage()))
//					throw new Exception("测试最小计算单位不能小于0失败");
//				else
//					throw e;
//			}
//	}
//
//	/**
//	 * 测试最小计算单位不能小于0
//	 */
//	@Test(expected=ComponentException.class)
//	public void testSmallest_billing_unit_lezero() throws Exception{
//		try{
//			gameTariff.setSmallest_billing_unit(0);
//			gameTariffComponent.addGameTariff(gameTariff, optr);
//			}catch(Exception e){
//				if(!"最小计算单位不能小于0".equals(e.getMessage()))
//					throw new Exception("测试最小计算单位不能小于0失败");
//				else
//					throw e;
//			}
//	}
//
//	
//	/**
//	 * 测试计算单位价格不能小于0
//	 */
//	@Test(expected=ComponentException.class)
//	public void testCredit_IsNull() throws Exception{
//		try{
//			gameTariff.setCredit(null);
//			gameTariffComponent.addGameTariff(gameTariff, optr);
//			}catch(Exception e){
//				if(!"计算单位价格不能小于0".equals(e.getMessage()))
//					throw new Exception("测试计算单位价格不能小于0失败");
//				else
//					throw e;
//			}
//	}
//
//	/**
//	 * 测试计算单位价格不能小于0
//	 */
//	@Test(expected=ComponentException.class)
//	public void testCredit_lezero() throws Exception{
//		try{
//			gameTariff.setCredit(-1);
//			gameTariffComponent.addGameTariff(gameTariff, optr);
//			}catch(Exception e){
//				if(!"计算单位价格不能小于0".equals(e.getMessage()))
//					throw new Exception("测试计算单位价格不能小于0失败");
//				else
//					throw e;
//			}
//	}
//
//	/**
//	 * 测试状态不能为空不能为空
//	 */
//	@Test(expected=ComponentException.class)
//	public void testStatus_IsNull() throws Exception{
//		try{
//			gameTariff.setStatus(null);
//			gameTariffComponent.addGameTariff(gameTariff, optr);
//			}catch(Exception e){
//				if(!"状态不能为空".equals(e.getMessage()))
//					throw new Exception("测试状态不能为空失败");
//				else
//					throw e;
//			}
//	}
//
//	
//	/**
//	 * 测试保存结果是否
//	 */
//	@Test
//	public void testAddGameTariff() throws Exception{
//		gameTariff.setGame_id(1);
//		int result = gameTariffComponent.addGameTariff(gameTariff, optr);
//		Assert.assertTrue(result >0);
//		
//	}
//
//	
//	/**
//	 * 测试修改结果是否
//	 */
//	@Test
//	public void testUpdateGameTariff() throws Exception{
//		gameTariff.setGame_id(1);
//		gameTariff.setTariff_id(11);
//		gameTariff.setTariff_name("name01_a");
//		gameTariff.setStatus("INVALID");
//		int result = gameTariffComponent.updateGameTariff(gameTariff, optr);
//		Assert.assertTrue(result >0);
//		
//	}
//
//	/**
//	 * 测试删除结果是否
//	 */
//	@Test
//	public void testRemoveGameTariff() throws Exception{
//		gameTariff.setGame_id(1);
//		gameTariff.setTariff_id(12);
//		int result = gameTariffComponent.removeGameTariff(gameTariff, optr);
//		Assert.assertTrue(result >0);
//		
//	}
//	
//	/**
//	 * 测试查询所有游戏标准资费
//	 */	
//	@Test(expected=ComponentException.class)
//	public void testQueryAllGameTariff() throws Exception{
//		try{
//			List<GameTariff> lgameTariff =  gameTariffComponent.queryAllGameTariff();
//			int i = lgameTariff.size();
//			}catch(Exception e){
//				throw e;
//			}
//	}
//
//	/**
//	 * 测试查询所有ACTIve游戏标准资费
//	 */	
//	@Test(expected=ComponentException.class)
//	public void testQueryActiveGameTariff() throws Exception{
//		try{
//			List<GameTariff> lgameTariff =  gameTariffComponent.queryActiveGameTariff();
//			int i = lgameTariff.size();
//		}catch(Exception e){
//			throw e;
//		}
//	}
//	
//	/**
//	 * 测试查询Tariff_id对应的标准资费 (查到)
//	 */	
//	@Test(expected=ComponentException.class)
//	public void testQueryGameTariffByTariffId() throws Exception{
//		try{
//			GameTariff gameTariff =  gameTariffComponent.queryGameTariffByTariffId(11);
//			int i = gameTariff.getTariff_id();
//		}catch(Exception e){
//			throw e;
//		}
//	}
//
//	/**
//	 * 测试查询Tariff_id对应的标准资费 (查不到)
//	 */	
//	@Test(expected=ComponentException.class)
//	public void testQueryGameTariffByTariffId_not() throws Exception{
//		try{
//			GameTariff gameTariff =  gameTariffComponent.queryGameTariffByTariffId(1);
//			int i = gameTariff.getTariff_id();
//		}catch(Exception e){
//			throw e;
//		}
//	}
//	
//	/**
//	 * 测试查询game_id对应的ACTIVE标准资费 (查到)
//	 */	
//	@Test(expected=ComponentException.class)
//	public void testQueryGameTariffByGameId() throws Exception{
//		try{
//			List<GameTariff> lgameTariff =  gameTariffComponent.queryAllActiveGameTariffByGameId(1);
//			int i = lgameTariff.size();
//		}catch(Exception e){
//			throw e;
//		}
//	}
//
//	/**
//	 * 测试查询game_id对应的ACTIVE标准资费 (查不到)
//	 */	
//	@Test(expected=ComponentException.class)
//	public void testQueryGameTariffByGameId_not() throws Exception{
//		try{
//			List<GameTariff> lgameTariff =  gameTariffComponent.queryAllActiveGameTariffByGameId(2);
//			int i = lgameTariff.size();
//		}catch(Exception e){
//			throw e;
//		}
//	}
//
//	
//	/**
//	 * 测试查询game_id对应的标准资费 (查到)
//	 */	
//	@Test(expected=ComponentException.class)
//	public void testQueryAllGameTariffByGameId() throws Exception{
//		try{
//			List<GameTariff> lgameTariff =  gameTariffComponent.queryAllGameTariffByGameId(1);
//			int i = lgameTariff.size();
//		}catch(Exception e){
//			throw e;
//		}
//	}
//
//	/**
//	 * 测试查询game_id对应的标准资费 (查不到)
//	 */	
//	@Test(expected=ComponentException.class)
//	public void testQueryAllGameTariffByGameId_not() throws Exception{
//		try{
//			List<GameTariff> lgameTariff =  gameTariffComponent.queryAllGameTariffByGameId(2);
//			int i = lgameTariff.size();
//		}catch(Exception e){
//			throw e;
//		}
//	}
//
//	/**
//	 * 测试分页查询所有游戏标准资费 (查到)
//	 */	
//	@Test(expected=ComponentException.class)
//	public void testQueryAllGameTariffPage() throws Exception{
//		try{
//			//Pager<GameTariff> lgameTariff =  gameTariffComponent.queryAllGameTariffPage(0, 10);
//			//int i = lgameTariff.getRecords().size();
//		}catch(Exception e){
//			throw e;
//		}
//	}
//
//	/**
//	 * 测试分页查询所有游戏标准资费 (查不到)
//	 */	
//	@Test(expected=ComponentException.class)
//	public void testQueryAllGameTariffPage_start2() throws Exception{
//		try{
//			//Pager<GameTariff> lgameTariff =  gameTariffComponent.queryAllGameTariffPage(10, 10);
//			//int i = lgameTariff.getRecords().size();
//		}catch(Exception e){
//			throw e;
//		}
//	}
//
//	/**
//	 * 测试分页查询所有Active游戏标准资费 (查到)
//	 */	
//	@Test(expected=ComponentException.class)
//	public void testQueryAllActiveGameTariffPage() throws Exception{
//		try{
//			Pager<GameTariff> lgameTariff =  gameTariffComponent.queryActiveGameTariffPage(0, 10);
//			int i = lgameTariff.getRecords().size();
//		}catch(Exception e){
//			throw e;
//		}
//	}
//
//	/**
//	 * 测试分页查询所有ACTIVE游戏标准资费 (查不到)
//	 */	
//	@Test(expected=ComponentException.class)
//	public void testQueryAllActiveGameTariffPage_start2() throws Exception{
//		try{
//			Pager<GameTariff> lgameTariff =  gameTariffComponent.queryActiveGameTariffPage(10, 10);
//			int i = lgameTariff.getRecords().size();
//		}catch(Exception e){
//			throw e;
//		}
//	}
//
//	/**
//	 * 测试分页查询game_id对应的所有Active游戏标准资费 (查到)
//	 */	
//	@Test(expected=ComponentException.class)
//	public void testQueryAllActiveGameTariffPageByGameId() throws Exception{
//		try{
//			Pager<GameTariff> lgameTariff =  gameTariffComponent.queryAllActiveGameTariffPageByGameId(1, 0, 10);
//			int i = lgameTariff.getRecords().size();
//		}catch(Exception e){
//			throw e;
//		}
//	}
//
//	/**
//	 * 测试分页查询game_id对应的所有Active游戏标准资费 (game_id不存在 查不到)
//	 */	
//	@Test(expected=ComponentException.class)
//	public void testQueryAllActiveGameTariffPageByGameId_1() throws Exception{
//		try{
//			Pager<GameTariff> lgameTariff =  gameTariffComponent.queryAllActiveGameTariffPageByGameId(3, 0, 10);
//			int i = lgameTariff.getRecords().size();
//		}catch(Exception e){
//			throw e;
//		}
//	}
//	
//	/**
//	 * 测试分页查询game_id对应的所有ACTIVE游戏标准资费 (查不到)
//	 */	
//	@Test(expected=ComponentException.class)
//	public void testQueryAllActiveGameTariffPageByGameId_2() throws Exception{
//		try{
//			Pager<GameTariff> lgameTariff =  gameTariffComponent.queryAllActiveGameTariffPageByGameId(1, 10, 10);
//			int i = lgameTariff.getRecords().size();
//		}catch(Exception e){
//			throw e;
//		}
//	}
//	
}
