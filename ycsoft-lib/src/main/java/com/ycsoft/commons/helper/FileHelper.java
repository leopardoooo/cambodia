package com.ycsoft.commons.helper;

import java.beans.IntrospectionException;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.BeanUtils;

import com.sun.rowset.CachedRowSetImpl;
import com.ycsoft.commons.exception.ComponentException;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.Sheet;
import jxl.SheetSettings;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.NumberFormat;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

/**
 * 基本类型的文件操作功能
 *
 * @author hh
 * @date Dec 3, 2009 1:09:25 PM
 */
public class FileHelper {

	private static BufferedReader bufferedReader;

	private FileHelper() {
	}

	/**
	 * 指定一个路径创建文件夹
	 *
	 * @param folderPath
	 */
	public static void mkFolder(String folderPath) {

		String filePath = folderPath;
		filePath = filePath.toString();
		java.io.File myFilePath = new java.io.File(filePath);
		if (!myFilePath.exists()) {
			myFilePath.mkdir();
		}

	}

	/**
	 * <p>
	 * 指定文件位置filePathAndName，新建新文件，并保存内容fileContent.
	 * </p>
	 *
	 * @param filePathAndName
	 * @param fileContent
	 * @throws IOException
	 */
	public static void newFile(String filePathAndName, String fileContent)
			throws IOException {

		String filePath = filePathAndName;
		filePath = filePath.toString();
		File myFilePath = new File(filePath);
		if (!myFilePath.exists()) {
			myFilePath.createNewFile();
		}
		FileWriter resultFile = new FileWriter(myFilePath);
		PrintWriter myFile = new PrintWriter(resultFile);
		String strContent = fileContent;
		myFile.println(strContent);
		resultFile.close();

	}

	/**
	 * 追加写入文件
	 *
	 * @param filePathAndName
	 * @param fileContent
	 * @throws IOException
	 */
	public static void addFile(String filePathAndName, String fileContent)
			throws IOException {

		String filePath = filePathAndName;
		filePath = filePath.toString();
		File myFilePath = new File(filePath);
		if (!myFilePath.exists()) {
			throw new IOException(filePathAndName + " is not exist.");
		}
		FileWriter resultFile = new FileWriter(myFilePath, true);
		PrintWriter myFile = new PrintWriter(resultFile);
		String strContent = fileContent;
		myFile.println(strContent);
		resultFile.close();
	}

	/**
	 * 删除文件
	 *
	 * @param filePathAndName
	 */
	public static void delFile(String filePathAndName) {
		java.io.File myDelFile = new java.io.File(filePathAndName);
		if (myDelFile.exists()) {
			myDelFile.delete();
		}

	}

	/**
	 * 删除文体夹
	 *
	 * @param folderPath
	 */
	public static void delFolder(String folderPath) {

		delAllFile(folderPath); // 删除完里面所有内容
		String filePath = folderPath;
		filePath = filePath.toString();
		java.io.File myFilePath = new java.io.File(filePath);
		if (myFilePath.exists())
			myFilePath.delete(); // 删除空文件夹
	}

	/**
	 * 删除文件夹里面的所有文件和子文件夹
	 *
	 * @param path
	 */
	public static void delAllFile(String filepath) {
		File file = new File(filepath);
		if (!file.exists()) {
			return;
		}
		// 判断 是否目录
		if (!file.isDirectory()) {
			return;
		}
		String[] tempList = file.list();
		File temp = null;
		for (String element : tempList) {
			if (filepath.endsWith(File.separator)) {
				temp = new File(filepath + element);
			} else {
				temp = new File(filepath + File.separator + element);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(filepath + File.separator + element);// 先删除文件夹里面的文件
				delFolder(filepath + File.separator + element);// 再删除空文件夹
			}
		}
	}

	/**
	 * 清除文件夹里的所有文件，不删子文件夹
	 *
	 * @param path
	 */
	public static void cleanAllFile(String path) {

		File file = new File(path);
		if (!file.exists()) {
			return;
		}
		// 判断 是否目录
		if (!file.isDirectory()) {
			return;
		}
		String[] tempList = file.list();
		File temp = null;
		for (String element : tempList) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + element);
			} else {
				temp = new File(path + File.separator + element);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				cleanAllFile(path + File.separator + element);// 先删除文件夹里面的文件
			}
		}
	}

	/**
	 * 复制一个文件到指定位置
	 * @param oldfile
	 * @param newFilePath
	 * @throws Exception
	 */
	public static void copyFile(File oldfile, String newFilePath)
			throws Exception {
		FileInputStream is = null;
		FileOutputStream fs = null;
		try {
			if (oldfile.exists()) { // 文件存在时
				is = new FileInputStream(oldfile);
				// 读入原文件
				fs = new FileOutputStream(newFilePath);
				byte[] buffer = new byte[1444];
				int byteread;
				while ((byteread = is.read(buffer)) != -1)
					fs.write(buffer, 0, byteread);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				is.close();
			} catch (Exception e) {
			}
			try {
				fs.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 复制单个文件
	 *
	 * @param oldPath
	 * @param newPath
	 * @throws IOException
	 */
	public static void copyFile(String oldFilePath, String newFilePath)
			throws IOException {

		int bytesum = 0;
		int byteread = 0;
		File oldfile = new File(oldFilePath);
		if (oldfile.exists()) { // 文件存在时
			InputStream inStream = new FileInputStream(oldFilePath); // 读入原文件
			FileOutputStream fs = new FileOutputStream(newFilePath);
			byte[] buffer = new byte[1444];

			while ((byteread = inStream.read(buffer)) != -1) {
				bytesum += byteread; // 字节数 文件大小
				fs.write(buffer, 0, byteread);
			}
			inStream.close();
		} else {
			throw new IOException(oldFilePath + " is not exist.");
		}
	}

	/**
	 * 复制整个文件夹内容
	 *
	 * @param oldPath
	 * @param newPath
	 * @throws IOException
	 */
	public static void copyFolder(String oldPath, String newPath)
			throws IOException {
		(new File(newPath)).mkdirs(); // 如果文件夹不存在 则建立新文件夹
		File a = new File(oldPath);
		if (!a.exists())
			throw new IOException(oldPath + "is not exist.");
		String[] file = a.list();
		File temp = null;
		for (String element : file) {
			if (oldPath.endsWith(File.separator)) {
				temp = new File(oldPath + element);
			} else {
				temp = new File(oldPath + File.separator + element);
			}

			if (temp.isFile()) {
				FileInputStream input = new FileInputStream(temp);
				FileOutputStream output = new FileOutputStream(newPath
						+ File.separator + temp.getName().toString());
				byte[] b = new byte[1024 * 5];
				int len;
				while ((len = input.read(b)) != -1) {
					output.write(b, 0, len);
				}
				output.flush();
				output.close();
				input.close();
			}
			if (temp.isDirectory()) {// 如果是子文件夹
				copyFolder(oldPath + File.separator + element, newPath
						+ File.separator + element);
			}
		}

	}

	/**
	 * 移动文件到指定目录
	 *
	 * @param oldPath
	 * @param newPath
	 * @throws IOException
	 */
	public static void moveFile(String oldPathFile, String newPathFile)
			throws IOException {
		copyFile(oldPathFile, newPathFile);
		delFile(oldPathFile);

	}

	/**
	 * 移动目录到指定目录
	 *
	 * @param oldPath
	 * @param newPath
	 * @throws IOException
	 */
	public static void moveFolder(String oldPath, String newPath)
			throws IOException {
		copyFolder(oldPath, newPath);
		delFolder(oldPath);
	}

	/**
	 * 解析csv文件 到一个list中 每个单元个为一个String类型记录，每一行为一个list。 再将所有的行放到一个总list中
	 *
	 * @param filePathAndName
	 * @return
	 * @throws IOException
	 */
	public static List<List<String>> readCSVFile(String filePathAndName)
			throws IOException {

		InputStreamReader fr = null;
		BufferedReader br = null;
		fr = new InputStreamReader(new FileInputStream(filePathAndName));

		br = new BufferedReader(fr);
		String rec = null;// 一行
		String str;// 一个单元格
		List<List<String>> listFile = new ArrayList<List<String>>();
		try {
			// 读取一行
			while ((rec = br.readLine()) != null) {
				Pattern pCells = Pattern
						.compile("(\"[^\"]*(\"{2})*[^\"]*\")*[^,]*,");
				if (rec.charAt(rec.length() - 1) != ',')
					rec = rec + ",";
				Matcher mCells = pCells.matcher(rec);
				List<String> cells = new ArrayList<String>();// 每行记录一个list
				// 读取每个单元格
				while (mCells.find()) {
					str = mCells.group();
					str = str.replaceAll(
							"(?sm)\"?([^\"]*(\"{2})*[^\"]*)\"?.*,", "$1");
					str = str.replaceAll("(?sm)(\"(\"))", "$2");
					cells.add(str);
				}
				listFile.add(cells);
			}
		} finally {
			if (fr != null) {
				fr.close();
			}
			if (br != null) {
				br.close();
			}
		}
		return listFile;
	}

	/**
	 * <p>
	 * 使用jdbc方式读取csv文件,CachedRowSetImpl是一个离线的ResultSet
	 * </p>
	 *
	 * @param filepathandname
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 */
	public static CachedRowSetImpl readCSVFileByJdbc(String filepathandname)
			throws ClassNotFoundException, SQLException, IOException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		CachedRowSetImpl crs = null;
		try {

			File myfile = new File(filepathandname);
			if (!myfile.exists())
				throw new IOException(filepathandname + " is not exist.");
			Class.forName("org.relique.jdbc.csv.CsvDriver");
			conn = DriverManager.getConnection("jdbc:relique:csv:"
					+ myfile.getParent() + File.separator);

			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * from "
					+ myfile.getName().substring(0,
							myfile.getName().indexOf(".")));
			crs = new CachedRowSetImpl();
			crs.populate(rs);
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			try {
				if (stmt != null)
					stmt.close();
			} catch (Exception e) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
			}
		}
		return crs;

	}

	/**
	 * 把文本文件转换成String
	 *
	 * @param fullPath
	 * @return
	 * @throws IOException
	 */
	public static String readFile(String fullPath) throws IOException {
		return readFile(new File(fullPath));
	}

	/**
	 * 指定文件，读取文件内容。
	 *
	 * @param file
	 *            文件对象
	 * @throws IOException
	 */
	public static String readFile(File file) throws IOException {
		BufferedReader reader = null;
		FileReader fr = null;
		StringBuilder builder = new StringBuilder("");
		try {
			fr = new FileReader(file);
			reader = new BufferedReader(fr);
			if (reader == null)
				return null;

			String line = null;
			while ((line = reader.readLine()) != null) {
				builder.append(line + "\n");
			}
		} finally {
			if (fr != null)
				fr.close();
			if (reader != null)
				reader.close();
		}
		return builder.toString();
	}

	/**
	 * 把属性文件转换成Map
	 *
	 * @param propertiesFile
	 * @return
	 * @throws IOException
	 */
	public static final Map<String, String> getPropertiesMap(
			String propertiesFile) throws IOException {
		Properties properties = new Properties();
		FileInputStream inputStream = null;
		Map<String, String> map = new HashMap<String, String>();
		try {
			inputStream = new FileInputStream(propertiesFile);
			properties.load(inputStream);
			Set<Object> keySet = properties.keySet();
			for (Object key : keySet) {
				map.put((String) key, properties.getProperty((String) key));
			}
		} finally {
			if (inputStream != null)
				inputStream.close();
		}
		return map;
	}

	public static final <T> List<T> fileToBean(File f, String[] colName,
			Class<T> t) throws Exception {
		List<T> list = execlToBean(f, colName, t);
		//第一行 删除
		list.remove(0);
		return list;
	}
	
	/**
	 * 只有一列数据
	 * @param f
	 * @return 返回字符串集合
	 * @throws Exception
	 */
	public static final List<String> fileToArray(File f) throws Exception {
		List<String> list = new ArrayList<String>();
		Workbook workbook = null;
		try {
			workbook = Workbook.getWorkbook(f);
			Sheet sheet = workbook.getSheet(0);

			int rowCount = sheet.getRows();
			for (int i = 0; i < rowCount; i++) {
				Cell cell = sheet.getRow(i)[0];
				list.add(cell.getContents());
			}
		} catch (Exception e) {
			throw new Exception("文件转换异常", e);
		} finally {
			if (workbook != null)
				workbook.close();
		}
		return list;
	}

    public static final List<String> readTxtFile(File file) throws Exception { 
    	List<String> list = new ArrayList<String>();
    	InputStreamReader read = null;
		BufferedReader br = null;
        try {  
            String encoding="GBK";  
            if(file.isFile() && file.exists()){ //判断文件是否存在  
                read = new InputStreamReader(new FileInputStream(file),encoding);//考虑到编码格式  
                br = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = br.readLine()) != null){  
                	lineTxt = lineTxt.replaceAll("，", ",");  
                    list.add(lineTxt);
                }  
	        }
        } catch (Exception e) {  
        	throw new Exception("读取文件内容出错", e);
        } finally {
			if (read != null)
				read.close();
			if (br != null)
				br.close();
		}
        return list;
    } 
	
	
	public static final <T> List<T> txtToBean(File f, String[] colName,Class<T> t) throws Exception {
		List<T> list = new ArrayList<T>();
		List<String> txtList = readTxtFile(f);
		if(txtList.size() == 0){
			throw new ComponentException("文件内容不存在!");
		}
		if(StringHelper.isEmpty(txtList.get(0))){
			txtList.remove(0);//去掉第一行
		}else{
			if(StringHelper.isEmpty(txtList.get(1))){
				throw new ComponentException("第2行不能为空!");
			}
			String fristRow = txtList.get(0).split(",")[0];
			Pattern p = Pattern.compile("[^a-zA-Z0-9]"); 
			Matcher m = p.matcher(fristRow); 
			String newFrist = m.replaceAll("");
			String twoRow = txtList.get(1).split(",")[0];
			//判断第一行和第二行长度
			if(newFrist.length() != twoRow.length()){
				txtList.remove(0);//去掉第一行
			}else{
				throw new ComponentException("第一行需要留空!");
			}
		}
//		txtList.remove(0);//去掉第一行
		for (int i = 0; i < txtList.size(); i++) {
			T bean = t.newInstance();
			String txt = txtList.get(i);
			//去除空的行
			if(StringHelper.isEmpty(txt)){
				continue;
			}
			String[] row =  txt.split(",");
			int rowNum = row.length;//文件数据
			int colNum = colName.length;//系统需要的字段
//			if(colNum -rowNum != 1 && colNum != rowNum){
//				throw new ComponentException("文件格式错误");
//			}
			for (int j = 0;  j < rowNum && j < colNum; j++) {
//				if(colNum -rowNum == 1 && j == colNum-1){//文件只有2列的情况下，默认系统字段有3列，第三列为文件第二列的值，主要是modem入库mac和modem_id一样的
//					BeanHelper.setPropertyString(bean, colName[j],row[j-1] );
//				}else{
					BeanHelper.setPropertyString(bean, colName[j],row[j] );
//				}
			}
			list.add(bean);
		}
		return list;
	}

	public static final <T> List<T> execlToBean(File f, String[] colName,
			Class<T> t) throws Exception {
		List<T> list = new ArrayList<T>();

		Workbook workbook = null;
		try {
			workbook = Workbook.getWorkbook(f);
			Sheet sheet = workbook.getSheet(0);
			Cell cell = null;

			int columnCount = colName.length;
			int rowCount = sheet.getRows();
			
			Pattern p = Pattern.compile("\\s");      
			
			for (int i = 0; i < rowCount; i++) {
				T bean = t.newInstance();
				Cell[] row = sheet.getRow(i);
				//空行的数据跳出继续下循环
				if(row.length==0 && i != 0)
					continue;
//				//第一个为空就跳出继续下循环
//				if (StringHelper.isEmpty(row[0].getContents())) 
//					continue;
//				if (StringHelper.isEmpty(row[0].getContents()))
//					break;
				for (int j = 0; j < row.length && j < columnCount; j++) {
					try {
						
						// 注意，这里的两个参数，第一个是表示列的，第二才表示行
						cell = row[j];

//						if (cell.getType() == CellType.NUMBER) {
//							BeanHelper.setPropertyString(bean, colName[j],
//									((NumberCell) cell).getValue()+"");
//							System.out.print(((NumberCell) cell).getValue());
//						} else if (cell.getType() == CellType.DATE) {
//							BeanHelper.setPropertyString(bean, colName[j],
//									((DateCell) cell).getDate());
//							System.out.print(((DateCell) cell).getDate());
//						} else {
						if(cell.getType() == CellType.DATE){
							BeanHelper.setPropertyString(bean, colName[j],
									DateHelper.dateToStr(((DateCell) cell).getDate()));
						}else{
							String contents = cell.getContents();
							contents = StringHelper.isEmpty(contents) ? "" : p.matcher(contents.trim()).replaceAll("").trim();
							BeanHelper.setPropertyString(bean, colName[j], contents);
						}
							
							//LoggerHelper.debug(FileHelper.class, cell.getContents());
//						}
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
				}
				list.add(bean);
			}
			
		} catch (Exception e) {
			throw new Exception("文件转换异常", e);
		} finally {
			if (workbook != null)
				workbook.close();
		}
		return list;
	}

	public static final <T> String writeExecel(List<T> list,
			Map<String, String> map, String path,String sheetName) throws FileNotFoundException, IntrospectionException{
		String fileName = path + File.separator
				+ (new Date().getTime() + RandomHelper.getNumInner(1000000000))
				+ ".xls";
		fileName = fileName.replaceAll("%20", " ");
		File file = new File(fileName);
		if(file.exists()){
			file.delete();
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(file));
		writeExecel(os, list, new ArrayList<Map.Entry<String,String>>(map.entrySet()), path, sheetName);
		return fileName;
	}
	
	public static final <T> void writeExecel(OutputStream os,List<T> list,
			List<Map.Entry<String, String>> resultList, String path,String sheetName){
		WritableWorkbook wwb = null;
		try {
			wwb = Workbook.createWorkbook(os);
			WritableSheet sheet = wwb.createSheet(sheetName, 0);//生成第一页工作表
			SheetSettings ss = sheet.getSettings();
			ss.setVerticalCentre(true);
			ss.setHorizontalCentre(true);
			ss.setDefaultColumnWidth(20);//默认列宽
			
			//表头的样式
			WritableFont font = new WritableFont(WritableFont.createFont("宋体"),10,WritableFont.BOLD);
			WritableCellFormat lcf = new WritableCellFormat(font);
			
			//T对象里面的属性集合
			List<String> propertyList = new ArrayList<String>();
			int num = 0;
			//添加表头
			for(Map.Entry<String, String> entry : resultList){
				sheet.addCell(new Label(num, 0, entry.getValue(), lcf));
				propertyList.add(entry.getKey());
				num++;
			}
			//添加正文单元格数据
			for(int i=1,len = list.size();i <= len;i++){
				T obj = list.get(i-1);
				for(int j=0,jlen=propertyList.size();j<jlen;j++){
					String propertyName = propertyList.get(j);
					//属性的类型，例：java.lang.String
					String fieldType = BeanHelper.getFieldType(obj.getClass(), propertyName);
					//属性的值
					String value =BeanUtils.getProperty(obj, propertyName);
					if(value != null){
						WritableCell cell = null;
						if("java.lang.String".equals(fieldType)){
							cell = new Label(j,i,value);
						}else if("int".equals(fieldType) || "java.lang.Integer".equals(fieldType)){
							cell = new Number(j,i,new Double(value),
									new WritableCellFormat(new NumberFormat("#.##")));
						}else if("java.util.Date".equals(fieldType)){
							cell = new Label(j,i,DateHelper.format(value));
						}
						if(cell != null){
							sheet.addCell(cell);
						}
					}
					
				}
			}
			
			int rows = sheet.getRows();
			int columns = sheet.getColumns();
			for(int i=0;i<rows;i++){
				boolean flag = true;
				for(int j=0;j<columns;j++){
					String content = sheet.getCell(j, i).getContents();
					if(StringHelper.isNotEmpty(content)){
						flag = false;
						break;
					}
				}
				if(flag){
					sheet.removeRow(i);
					if(i != 0) i = i - 1;
					rows = rows - 1;
				}
			}
			
			wwb.write();
		} catch (RowsExceededException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}/* catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/ catch (IntrospectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				if(null!=wwb)
					wwb.close();
				if(os!=null)
					os.close();
			} catch(Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
