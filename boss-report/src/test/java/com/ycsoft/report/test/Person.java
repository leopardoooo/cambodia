package com.ycsoft.report.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Person {

	private List<Object> list;
	
	public List<Object> getList() {
		return list;
	}

	public void setList(List<Object> list) {
		this.list = list;
	}

	public static void main(String[] args) {
		
		Map<Person,Integer> map=new HashMap<Person,Integer>();
		Person p1=new Person();
		
		
		System.out.print(p1.hashCode());
		
		
		
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		
		
		
		return true;
	}
}
