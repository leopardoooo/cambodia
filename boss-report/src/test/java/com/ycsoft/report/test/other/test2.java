package com.ycsoft.report.test.other;


import java.lang.reflect.Type;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.google.gson.reflect.TypeToken;
import com.ycsoft.commons.helper.JsonHelper;
import com.ycsoft.report.bean.RepCube;
import com.ycsoft.report.dto.RepKeyDto;
import com.ycsoft.report.query.cube.MeasureDataType;


public class test2 {

	public static void main(String[] args){
		
		Number n=new Double(1234566434.656);
		System.out.println(MeasureDataType.integer.fromat(n));
		System.out.println(MeasureDataType.integerbycomma.fromat(n));
		System.out.println(MeasureDataType.decimals.fromat(n));
		System.out.println(MeasureDataType.decimalsbycomma.fromat(n));
		if(true)
			return;
		List<RepKeyDto> aa=new ArrayList<RepKeyDto>();
		
		RepKeyDto b1=new RepKeyDto();
		b1.setKey("aa");b1.setValue("cc");
		aa.add(b1);
		
		
		String json;
		try {
			json = JsonHelper.fromObject(aa);

			System.out.println(json);
			
			Type type = new TypeToken<List<RepKeyDto>>(){}.getType();
			List<RepKeyDto> rs = JsonHelper.toList(json, type);
			ArrayList<RepKeyDto> bb=null;
			List<? extends ArrayList> cc=JsonHelper.toList(json, bb.getClass());
			
			System.out.println(rs.get(0).getKey());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		
		RepCube a=new RepCube();
		a.setColumn_code("");
		a.setColumn_type("");
		a.setColumn_define("");
		a.setColumn_as("");
		
		RepCube b=new RepCube();
		b.setColumn_code("");
		b.setColumn_type("");
		b.setColumn_define("");
		b.setColumn_as("");
		
		RepCube c=new RepCube();
		c.setColumn_code("");
		c.setColumn_type("");
		c.setColumn_define("");
		c.setColumn_as("");
		
		RepCube d=new RepCube();
		d.setColumn_code("");
		d.setColumn_type("");
		d.setColumn_define("");
		d.setColumn_as("");
		
		RepCube e=new RepCube();
		e.setColumn_code("");
		e.setColumn_type("");
		e.setColumn_define("");
		e.setColumn_as("");
		
		RepCube f=new RepCube();
		f.setColumn_code("");
		f.setColumn_type("");
		f.setColumn_define("");
		f.setColumn_as("");
	}
	public static void aaa() {

		try {
			Context ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup("java:/comp/env/boss3");
			/*
			 * DataSource ds =
			 * (DataSource)context.lookup("java:comp/env/jdbc/oracleds");
			 */
			Connection conn = ds.getConnection();
			System.out.println(
			conn.createStatement().executeQuery("select 1,1,1 from dual").getMetaData().getColumnCount()
			);
			conn.close();
			
			ds = (DataSource) ctx.lookup("java:/comp/env/system");
			/*
			 * DataSource ds =
			 * (DataSource)context.lookup("java:comp/env/jdbc/oracleds");
			 */
			 conn = ds.getConnection();
			 System.out.println(
						conn.createStatement().executeQuery("select 1,1,1 from dual").getMetaData().getColumnCount()
						);
			conn.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}