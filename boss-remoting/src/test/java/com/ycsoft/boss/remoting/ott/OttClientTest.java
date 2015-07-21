package com.ycsoft.boss.remoting.ott;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class OttClientTest {
	OttClient client = new OttClient();

	@Test
	public void testCreateUser() {
		Result result = client.createUser(10000001, "123456", "ptest1", "moniwang", null, null, null, null);
		assertEquals("0",result.getErr());
	}
	
	@Test
	public void testDeleteUser() {
		Result result = client.deleteUser(99032342);
		assertEquals("0", result.getErr());
	}
	
	@Test
	public void addProduct() {
		Result result = client.addOrUpdateProduct("12345321", "好产品啊");
		assertEquals("0", result.getErr());
	}
	
	@Test
	public void testDeleteProduct() {
		Result result = client.deleteProduct("12345321");
		assertEquals("0", result.getErr());
	}
	
	@Test
	public void testOpenUserProduct() {
		Result result = client.openUserProduct(100000, "12345321");
		assertEquals("0", result.getErr());
	}
	
	@Test
	public void testStopUserProduct() {
		Result result = client.openUserProduct(100000, "12345321");
		assertEquals("0", result.getErr());
	}

}
