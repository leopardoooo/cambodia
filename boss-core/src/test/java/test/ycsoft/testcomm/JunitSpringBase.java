package test.ycsoft.testcomm;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

@ContextConfiguration(locations={"classpath*:spring/*.xml"})
@TransactionConfiguration(transactionManager="transactionManager",defaultRollback=false)
public abstract class JunitSpringBase extends AbstractTransactionalJUnit4SpringContextTests  {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
}
