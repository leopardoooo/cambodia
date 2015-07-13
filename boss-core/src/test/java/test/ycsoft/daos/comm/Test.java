package test.ycsoft.daos.comm;

import com.ycsoft.daos.config.POJO;


@POJO(pk="uid",tn="test")
public class Test {
	private String uid ;
	private String pwd ;
	private String cdate ;
	
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getCdate() {
		return cdate;
	}
	public void setCdate(String cdate) {
		this.cdate = cdate;
	}
	@Override
	public String toString() {
		
		return "uid:"+this.uid + ",pwd:" +this.pwd +",date:"+this.cdate ;
	}
	
	
}
