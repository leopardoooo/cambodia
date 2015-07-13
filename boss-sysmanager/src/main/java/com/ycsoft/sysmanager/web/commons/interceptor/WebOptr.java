package com.ycsoft.sysmanager.web.commons.interceptor;

import com.ycsoft.beans.system.SOptr;

public class WebOptr {
	private static ThreadLocal<SOptr> base=new ThreadLocal<SOptr>();
	
	public void finalize() {
		base.remove();
	}
	
	public static void setOptr(SOptr optr){
		base.set(optr);
	}
	
	public static SOptr getOptr(){
		return base.get();
	}
	
}
