package com.ycsoft.report.test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import com.ycsoft.report.commons.ReportConstants;

public class CompilerAPITester {
	private static String JAVA_SOURCE_FILE = "DynamicObject.java";
	private static String JAVA_CLASS_FILE = "DynamicObject.class";
	private static String JAVA_CLASS_NAME = "DynamicObject";

	public static void generateJavaClass() {
		try {
			FileWriter fw = new FileWriter(JAVA_SOURCE_FILE);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("public class " + JAVA_CLASS_NAME + "{");
			bw.newLine();
			bw.write("public "
							+ JAVA_CLASS_NAME
							+ "(){System.out.println(\"In the constructor of DynamicObject\");}}");
			bw.flush();
			bw.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
		//生成java文件
		generateJavaClass();
		try {
			//设置编译路径和文件输出路径
			String cp=ReportConstants.CONTEXT_REAL_PATH+"/classes/";  
			List<String> list=new ArrayList<String>();
			list.add("-d");
			list.add(cp);
			list.add("-cp");
			list.add(cp);
			
             
			Iterable sourcefiles = fileManager.getJavaFileObjects(JAVA_SOURCE_FILE);
			compiler.getTask(null, fileManager, null, list, null, sourcefiles).call();
			fileManager.close();
			// 创建动态编译得到的DynamicObject类的实例
			Class.forName(JAVA_CLASS_NAME).newInstance();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}