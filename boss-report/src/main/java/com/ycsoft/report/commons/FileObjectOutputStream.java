package com.ycsoft.report.commons;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

import com.ycsoft.commons.exception.ReportException;

/**
 * 对象文件写入
 * @author new
 *
 */
public class FileObjectOutputStream implements CacheOutput {

	//行标号
	private int row_index=0;
	//重置对象输出流，防止内存溢出
	private final int rows_reset=1000;
	
	private ObjectOutputStream oos=null;
	
	private boolean append=false;
	
	/**
	 * 对象追加写入到文件
	 * 只能对已存在的文件追加写入
	 * @author new
	 *
	 */
	class SupplementalObjectOutputStream extends ObjectOutputStream {   
	  
		/**
		 * 覆盖父类方法
		 */
	    @Override  
	    protected void writeStreamHeader() throws IOException {   	         
	            super.reset();	     
	    }   
	  
	    public SupplementalObjectOutputStream(OutputStream out) throws IOException {   
	    	super(out);
	    }
	}  
	
	public FileObjectOutputStream(String file)throws IOException{
		oos=new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
	}
	/**
	 * append=true,表示追加写入文件末尾
	 * @param file
	 * @param append
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public FileObjectOutputStream(String file,boolean append) throws FileNotFoundException, IOException{
		this.append=append;
		if(append&&new File(file).exists())
			oos=new SupplementalObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file,append)));
		else
			oos=new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
	}
    /**
     * 把一个对象写入文件
     * @param object
     * @throws IOException
     */
	public void writeObject(Object object) throws IOException{
		oos.writeObject(object);
		row_index++;
		if(row_index%rows_reset==0){
			oos.flush();
			oos.reset();
		}
	}
		
	public void writeHead(Object obj) throws IOException,ReportException{
		if(obj!=null) this.writeObject(obj);
	}
	
	public void close() throws IOException{
		if(!this.append)
			oos.writeObject(null);
		oos.close();
	}
	
	public int size(){
		return this.row_index;
	}
}


