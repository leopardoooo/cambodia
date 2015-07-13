package com.ycsoft.report.test;

public class Outer {
	private int a = 10;
	public void doSomething() {
		Inner b = new Inner();
		a++;
		Inner in = new Inner();
		in.seeOuter();
		b.seeOuter();
	}
	
	class Inner {
		public void seeOuter() {
			System.out.println(a);
		}
	}

	public static void main(String[] args) {
		Outer out = new Outer();
		out.doSomething();
	}
}