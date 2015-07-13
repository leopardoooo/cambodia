package com.ycsoft.report.test.other;

public class PrintTarget implements Target{

	public void destory() {
	}

	public void init() {
		System.out.println("连接到打印机!");
	}
	
	public void print(){
		System.out.println("正在打印...");
	}
	
}
