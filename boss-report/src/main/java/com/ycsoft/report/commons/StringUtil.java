package com.ycsoft.report.commons;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.ycsoft.commons.exception.ReportException;

// 将一个字符串按照zip方式压缩和解压缩
public class StringUtil {

	private static final char hexDigits[] = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	/**
	 * 获得一个SHA-1计算对象
	 * @return
	 * @throws ReportException
	 */
	public static MessageDigest getInstanceSHA1() throws Exception{
		try {
			return MessageDigest.getInstance("SHA-1");
		} catch (Exception e) {
			throw e;
		}
	}
	/**
	 * 传入一个messageDigest算法对象
	 * string 计算文本
	 * 返回 SHA-1散列码
	 * @param messageDigest
	 * @param string
	 * @return
	 * @throws Exception 
	 */
	public static String SHA1(MessageDigest messageDigest, String... texts) throws Exception {
		try {
			if(messageDigest==null) {
				throw new Exception("messageDigest is null or text is null");
			}
			if(texts==null||texts.length==0)
				return null;
			if(texts[0]==null) return null;
			messageDigest.reset();
			for(String tt:texts){
				if(tt!=null) messageDigest.update(tt.getBytes());
			}
			
			byte[] updateBytes = messageDigest.digest();
			int len = updateBytes.length;
			char myChar[] = new char[len * 2];
			int k = 0;
			for (int i = 0; i < len; i++) {
				byte byte0 = updateBytes[i];
				myChar[k++] = hexDigits[byte0 >>> 4 & 0x0f];
				myChar[k++] = hexDigits[byte0 & 0x0f];
			}
			return new String(myChar);
		} catch (Exception e) {
			throw e;
		}
	}
	/**
	 * SHA-1散列计算
	 * @param texts
	 * @return
	 * @throws Exception
	 */
	public static String SHA1( String... texts) throws Exception {
		try {
			
			if(texts==null||texts.length==0)
				return null;
			if(texts[0]==null) return null;
			
			MessageDigest messageDigest=MessageDigest.getInstance("SHA-1");
			for(String tt:texts){
				if(tt!=null)  messageDigest.update(tt.getBytes());
			}
			byte[] updateBytes = messageDigest.digest();
			int len = updateBytes.length;
			char myChar[] = new char[len * 2];
			int k = 0;
			for (int i = 0; i < len; i++) {
				byte byte0 = updateBytes[i];
				myChar[k++] = hexDigits[byte0 >>> 4 & 0x0f];
				myChar[k++] = hexDigits[byte0 & 0x0f];
			}
			return new String(myChar);
		} catch (Exception e) {
			throw e;
		}
	}
	
	public static String MD5( String... texts) throws Exception {
		try {
			
			if(texts==null||texts.length==0)
				return null;
			MessageDigest messageDigest=MessageDigest.getInstance("MD5");
			for(String tt:texts)
			messageDigest.update(tt.getBytes());
			byte[] updateBytes = messageDigest.digest();
			int len = updateBytes.length;
			char myChar[] = new char[len * 2];
			int k = 0;
			for (int i = 0; i < len; i++) {
				byte byte0 = updateBytes[i];
				myChar[k++] = hexDigits[byte0 >>> 4 & 0x0f];
				myChar[k++] = hexDigits[byte0 & 0x0f];
			}
			return new String(myChar);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Zip压缩
	 */ 
	public static String compressZip(String str) throws IOException {
		if (str == null || str.length() == 0) {
			return str;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzip = new GZIPOutputStream(out);
		gzip.write(str.getBytes());
		gzip.close();
		return out.toString("ISO-8859-1");
	}

	/**
	 * Zip解压缩
	 */ 
	public static String uncompressZip(String str) throws IOException {
		if (str == null || str.length() == 0) {
			return str;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in = new ByteArrayInputStream(str
				.getBytes("ISO-8859-1"));
		GZIPInputStream gunzip = new GZIPInputStream(in);
		byte[] buffer = new byte[256];
		int n;
		while ((n = gunzip.read(buffer)) >= 0) {
			out.write(buffer, 0, n);
		}
		// toString()使用平台默认编码，也可以显式的指定如toString("GBK")
		return out.toString();
	}
	
	/**
	 * Zip压缩
	 */ 
	public static byte[] compressZip(byte[] str) throws ReportException {
		
		try{
			if (str == null || str.length == 0) {
				return null;
			}
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			GZIPOutputStream gzip = new GZIPOutputStream(out);
			gzip.write(str);
			gzip.close();
			return out.toByteArray();
		}catch(Exception e){
			throw new ReportException("字节压缩错误",e);
		}
	}

	/**
	 * Zip解压缩
	 */ 
	public static byte[] uncompressZip(byte[] str) throws ReportException {
		try{
			if (str == null || str.length == 0) {
				return null;
			}
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ByteArrayInputStream in = new ByteArrayInputStream(str);
			GZIPInputStream gunzip = new GZIPInputStream(in);
			byte[] buffer = new byte[1024];
			int n;
			while ((n = gunzip.read(buffer)) >= 0) {
				out.write(buffer, 0, n);
			}
			gunzip.close();
			// toString()使用平台默认编码，也可以显式的指定如toString("GBK")
			return out.toByteArray();
		}catch(Exception e){
			throw new ReportException("字节解压缩错误",e);
		}
	}
	

	// 测试方法
	public static void main(String[] args) throws IOException {

		String sql = "";

		System.out.println(StringUtil.compressZip("中国China"));

		System.out.println(StringUtil.uncompressZip(StringUtil
				.compressZip("中国China")));
	}

}
