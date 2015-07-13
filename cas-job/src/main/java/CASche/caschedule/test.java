package CASche.caschedule;

import java.sql.Timestamp;

import CASche.CADB;
import CASche.CASchePara;
import CASche.help.LoggerHelper;

public class test {
	
	public static void main(String[] args){
		
		long cc=System.currentTimeMillis();
		Timestamp a=new Timestamp(cc);
		System.out.println(cc);
		System.out.println(a.getTime());
		java.util.Date b=(java.util.Date)a;
		System.out.println(b.getTime());
		
//	  LoggerHelper.error(test.class, "ÄãºÃ");
//	  CASchePara casp=new CASchePara();
//	  casp.setDstTable("j_ca_command_out");
//	  casp.setDstTableBak("j_ca_command_out_bak");
//	  CADB db=new CADB("jdbc:oracle:thin:@192.168.1.203:1521:boss","busi","busi");
//	  Thread move=new Thread(new MoveCaOutToBak(casp,db));
//	  move.start();
	}

}
