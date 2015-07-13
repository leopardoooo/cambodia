package CASche;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import CASche.CATools;

public class CALogFile {
	String path = null;
	String fileName = null;
	String filePrefix = null;
	FileWriter writer = null;


	public CALogFile()
	{
		path = null;
		fileName = null;
		filePrefix = null;
	}
	
	public CALogFile(String path)
	{
		this.path = path;
	}
	
	public CALogFile(String path, String fileName)
	{
		this.path = path;
		this.fileName = fileName;
	}
	
	public void setPath(String path)
	{
		this.path = path;
	}
	
	public String getPath()
	{
		return path;
	}
	
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}
	
	public String getFileName()
	{
		return this.fileName;
	}

	public void setFilePrefix(String filePrefix)
	{
		this.filePrefix = filePrefix;
	}
	
	public String getFilePrefix()
	{
		return this.filePrefix;
	}

	static public void newFolder(String folderPath)
	{
		try{
			String filePath = folderPath;
			filePath = filePath.toString();
			File myFilePath = new File(filePath);
			if(!myFilePath.exists()){
				boolean succ = myFilePath.mkdirs();
				System.out.println("succ=" + succ);
			}
			System.out.println("新建目录操作 成功执行");
		}
		catch(Exception e)
		{
			System.out.println("新建目录操作出错");
			e.printStackTrace();
		}
	}

	public static void logMsg(String logPath, String fileName, String content){
		String curTime = CATools.GetNowDate();
		String curDate = curTime.substring(0, 8);
		newFolder(logPath);
		String thisFileName =  logPath + "/" + fileName + "_" +  curDate;
		try {
			//打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
			FileWriter writer = new FileWriter(thisFileName, true);
			writer.write(content);
			writer.close();
		}catch (IOException e) {
		    e.printStackTrace();
		}
	}


	public FileWriter logOpen()
	{
	
	   try {
		   writer = new FileWriter(path + "/" + filePrefix + "_" + fileName, true);
	   } catch (IOException e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
	   }
	   return writer;
	}
	 
	public void logMsg(String content)
	{
		String curTime = CATools.GetNowDate();
		String curDate = curTime.substring(0, 8);
		String thisFileName =  curDate;
		if (fileName == null ||thisFileName.equals(fileName)==false){
			if (fileName!=null){
				logClose();
			}
			setFileName(thisFileName);
			logOpen();
		}
		
		try {
			//打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
			writer.write(curTime+": " + content);
//			writer.write(content);
			writer.flush();
			//writer.close();
		}catch (IOException e) {
		    e.printStackTrace();
		}
	}

	public void logClose(){
		try {
			writer.close();
			writer = null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
