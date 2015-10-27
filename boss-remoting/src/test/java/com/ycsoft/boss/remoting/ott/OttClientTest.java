package com.ycsoft.boss.remoting.ott;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

public class OttClientTest {
	static OttClient client = new OttClient();
	
	@BeforeClass
	public static void setUp(){
		URLBuilder b = new URLBuilder();
		b.setIp("172.18.21.100");
		b.setPort(80);
		
		client.setBuilder(b);
	}
	
	@Test
	public void testCreateUser() {
		Result result = client.createUser("99000002", "123456", "99000002", "moniwang",
				null, null, "PND031501000065", "8810362E6435","ACTIVE");
		assertEquals("0",result.getErr());
	}
	
	//01201342
	
	@Test
	public void testDeleteUser() {
		Result result = client.deleteUser("0120134264");
		assertEquals("0",result.getErr());
	}
	
	/**
	 * 加授权测试
	 * 产品ID: BasePO_SuperTV_Basic
	 * 产品名称:SuperTV Basic Combo
	 * 资费ID: 9223372028806824119
	 * 
	 */
	@Test 
	public void addUserProduct(){
		
//		Result result=client.openUserProduct("0120134264", 
//				"PO_SuperTV_BASE_Internet_1M", "2015-11-13");
//		System.out.println(result.getStatus()+":"+result.getReason());
//		assertEquals("0",result.getErr());
	}
	/**
	 * BasePO_SuperTV_Basic
	* PO_SuperTV_BASE_Internet_1M
	 */
	@Test
	public void stopUserProduct() {
//		Result result = client.stopUserProduct("0120134264", "BasePO_SuperTV_Basic");
//		System.out.println(result.getStatus()+":"+result.getReason());
//		assertEquals("0",result.getErr());
	}
	
	
}
