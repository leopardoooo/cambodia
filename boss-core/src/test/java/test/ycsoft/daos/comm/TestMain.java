package test.ycsoft.daos.comm;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class TestMain {
	static ApplicationContext ac ; 

	/**
	 * @return
	 */
	static ApplicationContext getFactory(){
		if(ac == null ){
			ac = new ClassPathXmlApplicationContext(new String[]{
					"spring/applicationContext-test.xml",
					"spring/applicationContext-commons.xml",
					"spring/applicationContext-datasouce.xml"
					
			});
		}
		return ac ;
		
	}
	
	public static void main(String[] args)throws Exception {
		
		TestDao testDao = (TestDao)getFactory().getBean("testDao");
		//System.out.println(testDa);
		//System.out.println(testDao);
	//	TestDao2 testDao2 = (TestDao2)getFactory().getBean("testDao2");
		com.ycsoft.business.dao.system.SResourceDao sysResourceDao = (com.ycsoft.business.dao.system.SResourceDao)getFactory().getBean("sysResourceDao");
		System.out.println(sysResourceDao.findAll());
		
		//testDao.findAll();
		//List<Test> lst = testDao.findAll();
		//System.out.println(lst.size());
		
//		Test test = testDao.findByKey("huanghui");
//		System.out.println(test);
		
//		List<Test> test = testDao.find("select * from test t where t.uid=? and t.pwd=?" , "huanghui","huanghui");
//		System.out.println(test);
		
//		Test test= new Test();
	//	test.setUid(null);
	//	test.setPwd("huanghui");
//		List<Test> lst = testDao.findByEntity(test);
//		System.out.println(lst);
		
//		Map<String ,Serializable > map = new HashMap<String,Serializable>();
//		map.put("uid", "huanghui");
//		List<Test> lst = testDao.findByMap( map );
//		System.out.println(lst);
		
//		Test test= new Test();
//		test.setUid("leirenzhen");
//		test.setPwd("123");
//		int [] bools = testDao.save( test );
//		System.out.println(bools.length);
		
		//testDao.remove("huanghui");
		
//		List<Test> test = testDao2.list( "huanghui", "huanghui") ; 
//		System.out.println(test);
		
//		Object o = testDao2.findUnique("select pwd from test t where t.uid=?", "huanghui");
//		System.out.println(o);
		
//		List<Object> lst = testDao2.findUniques("select t.uid from test t ");
//		System.out.println(lst);
		
//		List<Object[]> arrs = testDao2.array();
//		for (Object[] os : arrs) {
//			System.out.println(os[0].toString() + " , " + os[1]);
//		}
		
//		Test test = new Test();
//		test.setUid("huanghui");
//		test.setPwd("123S");
//		List<Test> lst = testDao2.createNameQuery("select * from test t where t.uid=:uid and t.pwd=:pwd", test)
//							.list();
//		System.out.println(lst);
		
//		Map<String  , Object > params = new HashMap<String , Object>();
//		params.put("uid", "huanghui");
//		
//		List<Test> lst = testDao2.createNameQuery("select * from test t where t.uid=:uid",
//								params, 
//								Test.class).list();
//		System.out.println(lst);
		
		
//		List<Test> lst = testDao.createQuery("select * from test t where t.uid=?", "huanghui").list();
//		System.out.println(lst);
		
//		Map<String ,String > maps = new HashMap<String ,String>();
//		maps.put("uid", "huanghui");
//		List<Test> lsts = testDao.createNameQuery("select * from test t where t.uid=:uid", maps ).list();
//		System.out.println(lsts);
		
//		Pager<SysResource> ps = sysResourceDao.findAll(5 ,  2);
//		System.out.println(ps.getRecords());
	}
}
