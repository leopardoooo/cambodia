package com.ycsoft.report.query.daq.translate;

import java.util.ArrayList;
import java.util.List;

import com.ycsoft.commons.exception.ReportException;

/**
 * 缓存计算使用的key
 */
public class CacheKey implements Comparable<CacheKey> {
	

	private List<Object> keys=null;
	private List<Integer> sorts=null;

	public int compareTo(CacheKey c) {
		int compare=0;
		for(int i=0;i<this.sorts.size();i++){
			if(compare!=0) break;
			Integer o1=this.sorts.get(i);
			Integer o2=c.sorts.get(i);
			compare= o1.compareTo(o2);
		}
		return compare;
	}
		
	public CacheKey(List<Object> keys,List<Integer> sorts) throws ReportException{
		if(keys==null) keys=new ArrayList<Object>();
		if(sorts==null) sorts=new ArrayList<Integer>();
		this.keys=keys;
		this.sorts=sorts;
		if(this.keys.size()!=this.sorts.size())
			throw new ReportException("键值和排序值不匹配");
	}
	public List<Integer> getSorts() {
		return sorts;
	}

	public List<Object> getKeys() {
		return keys;
	}
	@Override
	public int hashCode() {
		int result = 1;
		for(Object o:keys)
			result=result+o.toString().hashCode();
		return result;
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj==null) return false;
		CacheKey cachekey=(CacheKey)obj;
		if(cachekey.keys.size()!=this.keys.size())
			return false;
		for(int i=0;i<keys.size();i++)
			if(!this.keys.get(i).toString().equals(cachekey.keys.get(i).toString()))
				return false;
		return true;
	}
	
}
