package com.ycsoft.commons.helper;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * XML 辅助类。包含解析XML文件等常用的功能。
 * @author hh
 * @date 2009/12/3 pm
 */
public class XMLHelper {

	private XMLHelper(){
	}

	/**
	 * 将Object序列化到XML文件
	 * @param obj 目标对象
	 * @param fileName 完整路径的文件名
	 * @throws IOException
	 */
	public static void objectXmlEncoder(Object obj , String fileName) throws IOException{
		 File fo = new File(fileName);
		 //文件不存在,就创建该文件
		 if(!fo.exists()){
			 String path = fileName.substring(0,fileName.lastIndexOf('.'));
			 File pFile = new File(path);
			 pFile.mkdirs();
		 }
		 FileOutputStream fos = new FileOutputStream(fo);
		 //创建XML文件对象输出类实例
		 XMLEncoder encoder = new XMLEncoder(fos);
		 //对象序列化输出到XML文件
		 encoder.writeObject(obj);
		 encoder.flush();
		 encoder.close();
		 fos.close();
	}

	/**
	 * 将序列化的文件解码到List
	 * @param source 完整路径的文件名
	 */
	public static List<Object> objectXmlDecoder(String source) throws IOException{
		List<Object> objList = new ArrayList<Object>();
		File fin = new File( source );
		FileInputStream fis = new FileInputStream(fin);
	  	XMLDecoder decoder = new XMLDecoder(fis);
	  	Object obj = null;
	  	try{
	  		while( (obj = decoder.readObject()) != null){
	  			objList.add(obj);
	  		}
		}catch (Exception e){
		}
		fis.close();
		decoder.close();
		return objList;
	}

}
