package com.ycsoft.boss.remoting.ott;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;

public class OttClientTest {
	OttClient client = new OttClient();

	@Test
	public void testCreateUser() {
		Result result = client.createUser("0120134264", "123456", "pybtest1", "moniwang",
				null, null, "PND031407000245", "88-10-36-2D-E1-AF","ACTIVE");
		assertEquals("0",result.getErr());
	}
	
	//01201342
	
//	@Test
//	public void testDeleteUser() {
//		Result result = client.deleteUser("0120134264");
//		assertEquals("0",result.getErr());
//	}
//	
	
//	BasePO_SuperTV_Basic
//	PO_SuperTV_BASE_Internet_1M
//	@Test
//	public void stopUserProduct() {
//		Result result = client.stopUserProduct("0120134264", "PO_SuperTV_BASE_Internet_1M");
//		assertEquals("0",result.getErr());
//	}
	
	
}
