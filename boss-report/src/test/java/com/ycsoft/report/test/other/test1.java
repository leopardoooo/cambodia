package com.ycsoft.report.test.other;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import com.ycsoft.commons.helper.JsonHelper;
import com.ycsoft.report.query.cube.DimensionType;
import com.ycsoft.report.query.sql.AnalyseSqlFactory2;


public class test1 {
	
	public static void main(String[] args){

		boolean a = false;
		int b = 0;
		System.out.println(a);
		System.out.println(b);
		
		String a1="abc%test1%bb$test2$";
		
		System.out.println(a1.replaceFirst("%test1%", " true "));
		
		//System.out.print( DimensionType.crosswise.name()+" "+ DimensionType.crosswise.getDesc());
	}

}
class FileObjectOutputStream extends ObjectOutputStream {
	
	//输入的文件
	private static File inputFile = null;
	
	/**是否追加文件头标志，默认不追加,
	 该追加标志跟一般的文件追加标志不同，
	 主要是因为 ObjectOutputStream，如果要追加数据的话，
	 需要修改writeStreamHeader()，使其不用重新调用writeStreamHeader，而是直接用reset()
	 */
	private static boolean bHeader = false;
	
	
	private static FileObjectOutputStream instance;
	
	public static FileObjectOutputStream getInstance(String filePath,boolean bApp ){
		bHeader = bApp;
		inputFile = new File(filePath);
		
		//如果文件不存在，则追加标志设置为FALSE
		if(!inputFile.exists()){  
			bHeader = false;
		}
		
		if(instance == null){
			try {
				instance = new FileObjectOutputStream(filePath,bApp);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return instance;
	}
	
	
	
	public static FileObjectOutputStream getInstance(OutputStream out ){
		
		if(instance == null){
			try {
				instance = new FileObjectOutputStream(out);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return instance;
	}
	
	
	
	private FileObjectOutputStream (String filePath,boolean bAppend) throws IOException{
		this(new FileOutputStream(filePath,bAppend));
	}

	private FileObjectOutputStream(OutputStream out) throws IOException {
		super(out);
	}
	
	
	/**
	 * 重写该方法，用于文件追加添加对象数据时，修改其头文件流。。
	 */
	protected void writeStreamHeader() throws IOException{
		
		if(!bHeader){
			super.writeStreamHeader();
		}else{
			super.reset();
		}
		
	}
	
	 
	 
	

}

