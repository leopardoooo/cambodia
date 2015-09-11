package test.ycsoft.testcomm;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.ycsoft.business.dao.system.SItemvalueDao;
import com.ycsoft.commons.store.MemoryDict;

@ContextConfiguration(locations={"classpath*:spring/*.xml"})
@TransactionConfiguration(transactionManager="transactionManager",defaultRollback=true)
public abstract class JunitSpringBase extends AbstractTransactionalJUnit4SpringContextTests  {

	@Autowired
	private SItemvalueDao sItemvalueDao;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
//		MemoryDict.setupData(sItemvalueDao.findAll());
	}

	@After
	public void tearDown() throws Exception {
	}
}
