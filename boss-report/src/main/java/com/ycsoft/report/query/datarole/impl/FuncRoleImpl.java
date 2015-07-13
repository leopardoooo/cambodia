package com.ycsoft.report.query.datarole.impl;

import com.ycsoft.report.query.datarole.FuncRole;
import com.ycsoft.report.query.datarole.FuncType;

public class FuncRoleImpl implements FuncRole {

	private FuncType[]  functypes=null;
	public boolean hasFunc(FuncType func) {
		if(func==null) return false;		
		if(functypes==null||functypes.length==0)
			return false;
		for(FuncType o:functypes)
			if(o.equals(func)) return true;
		return false;
	}
	
	public void setFuncs(FuncType[] funcs){
		this.functypes=funcs;
	}

}
