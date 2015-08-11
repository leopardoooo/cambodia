package com.ycsoft.boss.remoting.ott;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;

public class OttClientTest {
	OttClient client = new OttClient();

	@Test
	public void testCreateUser() {
		Result result = client.createUser("10000005", "123456", "ptest1", "moniwang", null, null, null, null);
		assertEquals("0",result.getErr());
	}
	
}
