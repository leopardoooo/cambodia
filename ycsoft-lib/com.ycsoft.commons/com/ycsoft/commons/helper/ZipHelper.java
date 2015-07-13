package com.ycsoft.commons.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 *
 * 对压缩文件的处理函数。
 * @author hh
 */
public class ZipHelper {

	private ZipHelper(){
	}

	/**
	 * <p> 将给定的srcPath目录，将其在同目录下压缩成一个zip文件，名称为文件夹名称 </p>
	 * @param srcPath
	 * @throws IOException
	 */
	public static void zip(String srcPath)throws IOException{
		int end = srcPath.lastIndexOf(File.separator) ;
		String targetPath = srcPath.substring( 0 , end );
		String zipName = srcPath.substring( end + 1 ) ;
		zip(srcPath, targetPath, zipName) ;
	}

	/**
	 * <p> 将给定的srcPath目录压缩到指定目录的zip文件里，zip名称为文件夹的名称 </p>
	 * @param srcPath
	 * @param targetPath
	 * @throws IOException
	 */
	public static void zip(String srcPath , String targetPath)throws IOException{
		String zipName = srcPath.substring(srcPath.lastIndexOf(File.separator)) ;
		zip(srcPath, targetPath, zipName) ;
	}

	/**
	 * 将指定的srcPath文件夹，将其压缩到指定路径及zip文件名称的文件
	 * @param srcPath
	 * @param targetPath
	 * @throws IOException
	 */
	public static void zip(String srcPath , String targetPath , String zipName) throws IOException{
		ZipOutputStream out=new ZipOutputStream(
					new FileOutputStream( targetPath + File.separator + zipName +".zip" ));
		File f = new File( srcPath );
		recursiveZip( out, f , "" );
		out.close();
	}

	/**
	 * 将指定的多个文件(不支持中文文件名)，将其压缩到指定zip文件
	 * @param fileFullPathList
	 * @param targetPath
	 * @param zipName
	 * @throws IOException
	 */
	public static void zip(String[] fileFullPathList,String targetPath , String zipName) throws IOException{
		ZipOutputStream out=null;
		FileInputStream in=null;
		try {
            byte[] buf = new byte[1024];
            out= new ZipOutputStream(new FileOutputStream(targetPath + File.separator + zipName +".zip" ));
            for (String nowFilePath:fileFullPathList) {
                File nowFile = new File(nowFilePath);
                 in= new FileInputStream(nowFile);
                //out.putNextEntry(new ZipEntry(new String(nowFile.getName().getBytes("gb2312"),"ISO8859-1")));
                out.putNextEntry(new ZipEntry(nowFile.getName()));
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
                in.close();
                in=null;
            }

        } catch (IOException e) {
            throw e;
        }finally{
        	if(out!=null) out.close();
        	if(in!=null) in.close();
        }
    }

	/**
	 * 将指定的多个文件(不支持中文文件名)，将其压缩到指定的输出流中
	 * @param fileFullPathList
	 * @param targetPath
	 * @param zipName
	 * @throws IOException
	 */
	public static void zip(List<String> fileFullPathList,OutputStream outputstream) throws IOException{
		ZipOutputStream out=null;
		FileInputStream in=null;
		try {
            byte[] buf = new byte[1024];
            out= new ZipOutputStream(outputstream);
            for (String nowFilePath:fileFullPathList) {
                File nowFile = new File(nowFilePath);
                 in= new FileInputStream(nowFile);
                //out.putNextEntry(new ZipEntry(new String(nowFile.getName().getBytes("gb2312"),"ISO8859-1")));
                out.putNextEntry(new ZipEntry(nowFile.getName()));
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
                in.close();
                in=null;
            }

        } catch (IOException e) {
            throw e;
        }finally{
        	try{
        	if(out!=null) out.close();
        	}catch(Exception e){}
        	try{
        	if(in!=null) in.close();
        	}catch(Exception e){}
        }
    }

	/**
	 * <p> 递归将子文件写入压缩包 </p>
	 * @param out zip文件流
	 * @param f 当前要写入的文件或文件夹
	 * @param currentPath 当前文件的路径
	 * @throws IOException
	 */
	private static void recursiveZip(ZipOutputStream out , File currentF , String currentPath)throws IOException{
		if (currentF.isDirectory()){
			out.putNextEntry(new ZipEntry( currentPath + "/" ));
			currentPath = currentPath.length() == 0 ? "" : currentPath + "/" ;
			File[] _fs = currentF.listFiles();
			for (File element : _fs) {
				recursiveZip( out , element , currentPath + element.getName());
			}
		}else{
			out.putNextEntry( new ZipEntry( currentPath ));
			FileInputStream in=new FileInputStream( currentF );
			int b;
			while (( b = in.read()) != -1)
				out.write(b);
			in.close();
		}
	}

	/**
	 *  将在zip文件的同一目录下生一个同名称的文件夹，并将所有的文件解压至该文件夹
	 * @param zip 指定的zip文件
	 * @param targetPath 目标文件夹
	 * @throws Exception
	 */
	public static void unzip(String zip)
			throws IOException {
		int last = zip.lastIndexOf(File.separator) + 1  ;
		String targetPath = zip.substring( 0 , last - 1 );// - 1：需要删除分隔符
		String targetName = zip.substring( last , zip.lastIndexOf('.')) ;
		unzip(zip , targetPath , targetName);
	}

	/**
	 * 将在给定的zip文件目录下，生成一个指定名称的文件夹，并将所有的文件解压至该文件夹
	 * @param zip
	 * @param targetName
	 * @throws IOException
	 */
	public static void unzip(String zip , String targetName)throws IOException{
		String targetPath = zip.substring( 0 ,zip.lastIndexOf(File.separator) + 1 );
		unzip(zip , targetPath , targetName);
	}

	/**
	 * <p> 根据指定的zip目录，解压生成指定名称的文件夹并存入指定的硬盘路径， </p>
	 * @param zip文件的完整名称
	 * @param targetPath 目标目录
	 * @param targetName 文件夹名
	 */
	public static void unzip(String zip , String targetPath , String targetName ) throws IOException{
		targetPath = targetPath + File.separator + targetName ;
		ZipInputStream zis = new ZipInputStream(new FileInputStream(zip));
		ZipEntry ze= null ;
		//逐个读取压缩包中的条目
		while ((ze = zis.getNextEntry()) != null) {
			if (ze.isDirectory()) {
				String _n = ze.getName();
				_n = _n.substring(0, _n.length() - 1);
				new File(targetPath + File.separator + _n).mkdir();
			} else {
				File _f = new File(targetPath + File.separator+ ze.getName());
				_f.createNewFile();
				FileOutputStream out = new FileOutputStream(_f);
				int b;
				while ((b =zis.read()) != -1)
					out.write(b);
				out.close();
			}
		}
		zis.close();
	}
}
