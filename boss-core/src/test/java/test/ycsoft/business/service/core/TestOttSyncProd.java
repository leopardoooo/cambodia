package test.ycsoft.business.service.core;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.ycsoft.business.service.impl.OttExternalService;

import test.ycsoft.testcomm.JunitSpringBase;

public class TestOttSyncProd extends JunitSpringBase{

	@Autowired
	private OttExternalService ottExternalService;
	
	@Test
	@Rollback(false) //true表示事物最终执行回滚，false表示事物最终执行提交
	public void testSync() throws Exception{
		ottExternalService.saveSyncProd();
	}
}
