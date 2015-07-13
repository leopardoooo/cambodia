package com.ycsoft.report.test.other;

public class TargetFactory {

	private TargetFactory(){
	}
	
	public static Target newTarget(String pkg){
//		try {
//			return newTarget(Class.forName(pkg));
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//			return null;
//		}
		
		return null;
	}
	
	
	public static <T extends Target> Target newTarget(Class<T> clazz){
		try {
			Target target = clazz.newInstance();
			//System.out.println("create a target instance and init.");
			target.init();
			
			return target;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
