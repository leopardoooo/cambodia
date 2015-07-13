package com.ycsoft.report.test.other;

import java.util.ArrayList;
import java.util.List;

public class test22 implements test111 {

	public <T> List<T[]> getHead(Class<T> t) {
		// TODO Auto-generated method stub
		Integer[] a=new Integer[2];
		a[0]=1;
		a[1]=2;
		List<T[]> list=new ArrayList<T[]>();
		list.add((T[])a);
		return list;
	}

	public static void main(String[] args){
		test22 a=new test22();
		for(Integer[] b:  a.getHead(Integer.class)){
			System.out.println(b[0]+"  "+b[1]);
		}
	}
}
