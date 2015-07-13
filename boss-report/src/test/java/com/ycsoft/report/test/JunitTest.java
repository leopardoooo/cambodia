package com.ycsoft.report.test;


import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi2.hssf.usermodel.HSSFCell;
import org.apache.poi2.hssf.usermodel.HSSFRow;
import org.apache.poi2.hssf.usermodel.HSSFSheet;
import org.apache.poi2.hssf.usermodel.HSSFWorkbook;
import org.apache.poi2.hssf.util.Region;
import org.junit.Test;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.commons.helper.JsonHelper;
import com.ycsoft.report.commons.ReportConstants;
import com.ycsoft.report.commons.SerializeUtil;


public class JunitTest {
	
	
	@Test
	public void testpoi2()throws Exception{
	    HSSFWorkbook wb = new HSSFWorkbook();
	    HSSFSheet sheet = wb.createSheet("new sheet");
	    HSSFRow row = sheet.createRow((short) 1);
	    HSSFCell cell = row.createCell((short) 1);
	    cell.setCellValue("This is a test of merging");
	    sheet.addMergedRegion(new Region(1,(short)1,1,(short)2));
	    // Write the output to a file
	    FileOutputStream fileOut = new FileOutputStream(ReportConstants.REP_TEMP_TXT+"workbook.xls");
	    wb.write(fileOut);
	    fileOut.close();
	}

	@Test
	public void testNum()throws Exception{
		Object a=new String("aaa");
		Object b=new Integer(12);
		
		String aa="bb";
		aa.toString();
		Number cc=new Integer(23);
		
		System.out.println(Integer.toHexString(23));
		System.out.println(aa.getBytes());
		
		Byte dd='d';
		Integer.toHexString(dd.intValue());
	}
	
	@Test
	public void testDd()throws Exception{
//		Double d=new Double(null);
//		System.out.print(d);
		List a=new ArrayList();
		a.add(null);
		a.add(null);
		a.add("");
		a.add("");
		a.add(Double.class);
		System.out.print(JsonHelper.fromObject(a));
	}
	/**
	 * 测试序列化再反序列化回来是否同一个对象
	 */
	@Test
	public void TestSe(){
		ct t1=new ct();
		try {
			System.out.println("t1==="+t1.getB());
			ct t2= (ct) SerializeUtil.unserialize(SerializeUtil.serialize(t1));
			t1.setB(4);
			System.out.println("t1==="+t1.getB());
			System.out.println("t2==="+t2.getB());
			
		} catch (ReportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void TestSe1(){
		
		try {
			byte[] bb=new byte[2];
			bb[0]=',';
			bb[1]=',';
			char a='1';
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
