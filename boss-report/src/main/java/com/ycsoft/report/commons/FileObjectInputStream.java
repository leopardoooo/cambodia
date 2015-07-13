package com.ycsoft.report.commons;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import com.ycsoft.commons.exception.ReportException;
/**
 * 对象文件读取
 * @author new
 *
 */
public class FileObjectInputStream implements CacheInput{

	private int row_index=0;
	private ObjectInputStream ois=null;
	public FileObjectInputStream(String file) throws IOException {
		ois=new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
	}
	
	public Object readObject() throws IOException, ClassNotFoundException{
		try {
			row_index++;
			return ois.readObject();
		} catch (EOFException e) {
			row_index--;
			return null;
		} 
	}
	/**
	 * 获得当前的行号
	 * @return
	 */
	public int getRowIndex(){
		return row_index;
	}
	
	public void close() throws Exception{
		ois.close();
	}

	public Object readHead() throws ReportException, IOException, ClassNotFoundException {
		return this.readObject();
	}

}
