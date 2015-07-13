package CASche;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigFile {
	private Properties propertie;
	private FileInputStream inputFile;
	private FileOutputStream outputFile;

	public ConfigFile()
	{
		propertie = new Properties();
	}

	public ConfigFile(String filePath)
	{
		propertie = new Properties();
		try{
			inputFile = new FileInputStream(filePath);
			propertie.load(inputFile);
			inputFile.close();
		}catch(FileNotFoundException ex){
			System.out.print("Can't Find the file "+ filePath);
			ex.printStackTrace();
		}catch(IOException ex){
			System.out.println("Can't open the file " + filePath);
			ex.printStackTrace();
		}
	}

	public String getValue(String key)
	{
		if (propertie.containsKey(key)){
			String value = propertie.getProperty(key);
			return value;
		}
		else{
			return "";
		}
	}

	public String getValue(String fileName, String key)
	{
		try{
			String value="";
			inputFile = new FileInputStream(fileName);
			propertie.load(inputFile);
			inputFile.close();
			if (propertie.containsKey(key)){
				value = propertie.getProperty(key);
				return value;
			}else{
				return value;
			}
		}catch(FileNotFoundException e){
			e.printStackTrace();
			return "";
		}catch(IOException e){
			e.printStackTrace();
			return "";
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}
	}
		
	public void clear()
	{
		propertie.clear();
	}

	public void setValue(String key, String value)
	{
		propertie.setProperty(key, value);
	}

	public void saveFile(String fileName, String description)
	{
		try{
			outputFile = new FileOutputStream(fileName);
			propertie.store(outputFile, description);
			outputFile.close();
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
	}

}
