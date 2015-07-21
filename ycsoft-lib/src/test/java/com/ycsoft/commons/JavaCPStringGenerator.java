/*
 * @(#) JavaCPStringGenerator.java 1.0.0 2015年7月20日 下午6:58:50
 */
package com.ycsoft.commons;

import java.io.File;
import java.io.FilenameFilter;

/**
 * 
 * @author Killer
 */
public class JavaCPStringGenerator {

	
	public static void main(String[] args) {
		File file = new File("/Users/killer/Documents/works/GitHub/cambodia/boss-remoting/target/dependency");
		File [] files = file.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith("jar");
			}
		});
		
		System.out.print("java -cp .;");
		
		for (File f : files) {
			System.out.print("./lib/" + f.getName() + ";");
		}
		//System.out.println(" " + BOSSBandServiceAdapterTest.class.getName());
		
	}
	
}
