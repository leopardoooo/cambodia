/*
 * @(#) BOSSBandServiceAdapterTest.java 1.0.0 2015年7月20日 下午1:34:21
 */
package com.ycsoft.boss.remoting.aaa;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Scanner;

/**
 * The TestCase for BOSSBandServiceAdapter
 * 
 * @author Killer
 */
public class BOSSBandServiceAdapterTest {

	private BOSSBandServiceAdapter bandService;

	public void setUp() {
		bandService = new BOSSBandServiceAdapter();
	}

	public static void main(String[] args) {
		BOSSBandServiceAdapterTest o = new BOSSBandServiceAdapterTest();
		o.setUp();
		String line = null;

		System.out
				.println("开户[create],暂停[pause],恢复[resume],重置密码[resetPswd],销户[delete],删除授权[deleteProd],加授权[addProd],查授权信息[queryProd],修改授权[editProd]]");

		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.print("请输入: ");
			line = sc.nextLine();
			if (line.equals("create")) {
				o.testCreate();
			} else if (line.equals("pause")) {
				o.testPause();
			} else if (line.equals("resume")) {
				o.testResume();
			} else if (line.equals("resetPswd")) {
				o.testResetPswd();
			} else if (line.equals("delete")) {
				o.testDelete();
			} else if (line.equals("deleteProd")) {
				o.testDeleteProd();
			} else if (line.equals("addProd")) {
				o.testAddProd();
			} else if (line.equals("queryProd")) {
				o.testQueryProd();
			} else if (line.equals("editProd")) {
				o.testEditProd();
			} else {
				break;
			}
		}
		sc.close();
		System.out.println("Quiting..");
	}

	// 开户
	public void testCreate() {
		try {
			boolean success = bandService
					.create(0l, "0120433901@vip", "123456",null);
			//assertTrue(success);
			System.out.println("开户[create]执行结果：" + success);
		} catch (AAAException e) {
			System.out.println(e.getMessage());
		}
	}

	// 暂停
	public void testPause() {
		try {
			boolean success = bandService.pause(0l, "0120433901@vip");
			//assertTrue(success);
			System.out.println("暂停[pause]执行结果：" + success);
		} catch (AAAException e) {
			System.out.println(e.getMessage());
		}
	}

	// 恢复
	public void testResume() {
		try {
			boolean success = bandService.resume(0l, "0120433901@vip");
			//assertTrue(success);
			System.out.println("恢复[resume]执行结果：" + success);
		} catch (AAAException e) {
			System.out.println(e.getMessage());
		}
	}

	// 重置密码
	public void testResetPswd() {
		try {
			boolean success = bandService
					.resetPswd(0l, "0120433901@vip", "123456");
			//assertTrue(success);
			System.out.println("恢复[resetPswd]执行结果：" + success);
		} catch (AAAException e) {
			System.out.println(e.getMessage());
		}
	}

	// 销户
	public void testDelete() {
		try {
			boolean success = bandService.delete(0l, "0120433901@vip");
			//assertTrue(success);
			System.out.println("销户[delete]执行结果：" + success);
		} catch (AAAException e) {
			System.out.println(e.getMessage());
		}
	}
	
	// 删除授权
	public void testDeleteProd() {
		try {
			boolean success = bandService.cancelOrder(0l, "0120433901@vip");
			//assertTrue(success);
			System.out.println("解除授权：" + success);
		} catch (AAAException e) {
			System.out.println(e.getMessage());
		}
	}
	
	//加授权
	public void testAddProd() {
		try {
			boolean success = bandService.orderService(0l, "0120433901@vip", 637, "20150822000000", "20160825000000");
			//assertTrue(success);
			System.out.println("加授权：" + success);
		} catch (AAAException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void testEditProd() {
		try {
			boolean success = bandService.modifyOrderService(0l, "0120433901@vip", 637, null, "20151005000000");
			//assertTrue(success);
			System.out.println("加授权：" + success);
		} catch (AAAException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void testQueryProd() {
		try {
			List<AAASubscriberServiceInfoWrapper> prodList = bandService.querySubscriberService(0l, "0120433901@vip");
			for (AAASubscriberServiceInfoWrapper prod:prodList){
				System.out.println(prod.getUeid()+"-"+prod.getAccessPolicyID()+"-"+prod.getExpireTime());
			}
		
		} catch (AAAException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
