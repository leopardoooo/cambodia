package com.ycsoft.commons.store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ycsoft.beans.core.fee.CFee;

public class MemoryPrintData {
	//操作员对应的未打印费用
	private static Map<String,List<String>> printMap = null;

	/**
	public static void appendPrintData(String optrId,String feeSn){
		List<String> feeSnList = printMap.get(optrId);
		if(null != feeSnList){
			feeSnList.add(feeSn);
		}else{
			feeSnList = new ArrayList<String>();
			feeSnList.add(feeSn);
			printMap.put(optrId, feeSnList);
		}
	}
	
	public static void removePrintData(String optrId,String feeSn){
		List<String> feeSnList = printMap.get(optrId);
		if(null != feeSnList && feeSnList.contains(feeSn)){
			feeSnList.remove(feeSn);
		}
	}**/
	
	public static void reloadOptrFee(String optrId,List<String> feeSnList){
		if(null!=printMap.get(optrId)){
			printMap.get(optrId).clear();
		}
		printMap.put(optrId, feeSnList);
	}
	
	public static String getUnPrintFee(String optrId){
		List<String> feeSnList = printMap.get(optrId);
		if(null != feeSnList && feeSnList.size() > 0){
			return feeSnList.get(0);
		}
		return null;
	}
	
	public static void loadData(List<CFee> feeList){
		Map<String,List<String>> t = new HashMap<String, List<String>>();
		for(CFee fee : feeList){
			List<String> feeSnList = t.get(fee.getOptr_id());
			if(null != feeSnList){  
				feeSnList.add(fee.getFee_sn());
			}else{
				feeSnList = new ArrayList<String>();
				feeSnList.add(fee.getFee_sn());
				t.put(fee.getOptr_id(), feeSnList);
			}
			
		}
		printMap = t;
	}
	
	public static void main(String[] args){
		
	}
}
