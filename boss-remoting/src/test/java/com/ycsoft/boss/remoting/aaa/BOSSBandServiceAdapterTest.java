/*
 * @(#) BOSSBandServiceAdapterTest.java 1.0.0 2015年7月20日 下午1:34:21
 */
package com.ycsoft.boss.remoting.aaa;

import static org.junit.Assert.assertTrue;

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
				.println("开户[create],暂停[pause],恢复[resume],重置密码[resetPswd],销户[delete]]");

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
					.create(0l, "Killer001", "12345678", 536);
			//assertTrue(success);
			System.out.println("开户[create]执行结果：" + success);
		} catch (AAAException e) {
			System.out.println(e.getMessage());
		}
	}

	// 暂停
	public void testPause() {
		try {
			boolean success = bandService.pause(0l, "Killer001");
			//assertTrue(success);
			System.out.println("暂停[pause]执行结果：" + success);
		} catch (AAAException e) {
			System.out.println(e.getMessage());
		}
	}

	// 恢复
	public void testResume() {
		try {
			boolean success = bandService.resume(0l, "Killer001");
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
					.resetPswd(0l, "Killer001", "abcd1234");
			//assertTrue(success);
			System.out.println("恢复[resetPswd]执行结果：" + success);
		} catch (AAAException e) {
			System.out.println(e.getMessage());
		}
	}

	// 销户
	public void testDelete() {
		try {
			boolean success = bandService.delete(0l, "Killer001");
			//assertTrue(success);
			System.out.println("销户[delete]执行结果：" + success);
		} catch (AAAException e) {
			System.out.println(e.getMessage());
		}
	}
}
