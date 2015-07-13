package com.ycsoft.report.commons;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.ycsoft.commons.exception.ReportException;

public class SerializeUtil {
	
	public static byte[] serialize(Serializable object) throws ReportException {
		ObjectOutputStream oos = null;
		ByteArrayOutputStream baos = null;
		try {
			// 序列化
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			byte[] bytes = baos.toByteArray();
			return bytes;
		} catch (Exception e) {
			throw new ReportException("对象序列化错误",e);
		}
	
	}

	public static Serializable unserialize(byte[] bytes) throws ReportException {
		ByteArrayInputStream bais = null;
		try {
			// 反序列化
			bais = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			return (Serializable) ois.readObject();
		} catch (Exception e) {
			throw new ReportException("对象反序列化错误",e);
		}
		
	}
	/**
	 * 序列化对象并压缩
	 * @param obj
	 * @return
	 * @throws IOException
	 */
	public static byte[] writeCompressObject(Serializable obj) throws IOException {
		if(obj==null) return null;
		byte[] byteDate = null;
		ByteArrayOutputStream o = new ByteArrayOutputStream();
		GZIPOutputStream gzout = new GZIPOutputStream(o);
		ObjectOutputStream out = new ObjectOutputStream(gzout);
		out.writeObject(obj);
		out.flush();
		out.close();
		gzout.close();
		byteDate = o.toByteArray();
		o.close();
		return byteDate;
	}
	/**
	 * 解压缩并反序列化对象
	 * @param byteDate
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Serializable readCompressObject(byte[] byteDate)
			throws IOException, ClassNotFoundException {
		if(byteDate==null) return null;
		Serializable obj = null;
		ByteArrayInputStream i = new ByteArrayInputStream(byteDate);
		GZIPInputStream gzin = new GZIPInputStream(i);
		ObjectInputStream in = new ObjectInputStream(gzin);
		obj = (Serializable) in.readObject();
		i.close();
		gzin.close();
		in.close();
		return obj;
	}
	
	
}