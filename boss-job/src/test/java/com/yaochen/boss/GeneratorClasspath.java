package com.yaochen.boss;
/*
 * @(#) GeneratorClasspath.java 1.0.0 Aug 3, 2011 8:57:06 PM
 *
 * Copyright 2011 YaoChen, Ltd. All rights reserved.
 * YaoChen PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

import java.io.File;
import java.io.FileFilter;


/**
 * 
 *
 * @author allex
 * @since 1.0
 */
public class GeneratorClasspath {

	static final String PATH = "D:\\Source\\workspace\\ly\\ly-job\\deploy\\dist\\lib";
	
	public static void main(String[] args) {
		File file = new File(PATH);
		System.out.println(file.listFiles());
		File[] jars = file.listFiles(new FileFilter(){
			public boolean accept(File f) {
				System.out.println(f.getName());
				return f.getName().endsWith(".jar");
			}
		});
		
		String classPath = "";
		for (File f : jars) {
			classPath += "lib/" + f.getName() + " ";
		}
		System.out.println(classPath);
	}
}
