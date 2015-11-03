package com.ycsoft.boss.remoting.ott;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import com.ycsoft.beans.ott.TServerOttauthProd;

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
		//{"login_name":"0120777661","login_password":"612598","stb_id":"PND031409000746","stb_mac":"8810362E2F79","user_status":"INSTALL"}
		Result result = client.createUser("0120777661", "612598", "0120777661", "moniwang",
				null, null, "PND031409000746", "8810362E2F79","INSTALL");
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
		
		TServerOttauthProd op=new TServerOttauthProd();
		op.setId("BasePO_SuperTV_Basic_Mobile");
		op.setFee_id("9223372028795493243");
		Map<String,TServerOttauthProd> map=new HashMap<>();
		map.put(op.getId(),op);
		Result result=client.openUserProduct("13882130978@sina.com", 
				"BasePO_SuperTV_Basic_Mobile", "2015-10-30 23:59:59",map);
		System.out.println(result.getStatus()+":"+result.getReason());
		assertEquals("0",result.getErr());
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
