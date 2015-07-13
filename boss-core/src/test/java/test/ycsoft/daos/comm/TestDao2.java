package test.ycsoft.daos.comm;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.daos.core.impl.AbstractSessionImpl;

@Component
public class TestDao2 extends AbstractSessionImpl{
	
	public List<Test> list( String uid , String pwd )throws Exception{
		String sql="select * from test t where t.uid=? and t.pwd=? ";
		List<Test> lsts = createQuery(Test.class, sql , uid,pwd)
							.list();
		return lsts;
	}
	
	public List<Object[]> array()throws Exception{
		String sql="select t.uid ,t.pwd  from test t ";
		List<Object[]> lsts = createSQLQuery(sql).list();
		return lsts;
	}
	
}
