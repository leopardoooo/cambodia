package com.ycsoft.report.query.datarole;


public abstract class BaseDataControl implements DataControl,DataRoleInit {
	
	private static ThreadLocal<DataRole> role = new ThreadLocal<DataRole>();
	public static DataRole getRole(){
		return role.get();
	}
	
	public static void setRole(DataRole datarole){
		role.set(datarole);
	}
	
	public void finalize() {
		role.remove();
	}
	
}
