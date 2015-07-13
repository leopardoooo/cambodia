package com.ycsoft.report.test.redis;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import redis.clients.jedis.Jedis;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.commons.helper.JsonHelper;
import com.ycsoft.report.commons.SerializeUtil;
import com.ycsoft.report.db.RedisConn;

public class RedisUseTest {

	/**
	 * JSON序列化测试
	 */
	
	
	/**
	 * 对象序列化测试
	 */
	
	
	@Test 
	public void test2(){
		Jedis jedis = RedisConn.getConn();
		
		Integer a=100;
		String b;
		try {
			b = JsonHelper.fromObject(a);
			jedis.set("aaa", b);
			b=jedis.get("aaa");
			jedis.hset("key1", "a", "1");
			jedis.hset("key1".getBytes(), "b".getBytes(), "2".getBytes());
			System.out.println(JsonHelper.toObject(b, Integer.class));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		RedisConn.close(jedis);
	}
	/**
	 * 字符串测试
	 */
	@Test
	public void testEsay(){
		// 从池中获取一个Jedis对象
		Jedis jedis = RedisConn.getConn();
		String keys = "name";
		// 删数据
		jedis.del(keys,"aa");
		
		// 存数据
		jedis.set(keys, "snowolf");
		// 取数据
		String value = jedis.get(keys);
		System.out.println(value);
		
		// 释放对象池
		RedisConn.close(jedis);
	}
	
	/**
	 * 测试批量获取
	 * 当key不存在时，返回的值
	 */
	@Test
	public void testMGet(){
		Jedis jedis =null;
		try{
			jedis= RedisConn.getConn();
			
			jedis.set("t1", "v1");
			jedis.set("t3", "v3");
			jedis.mset("t4","v4","t6","v6");
			
			System.out.println("t1==="+jedis.get("t1"));
			System.out.println("t2==="+jedis.get("t2"));
			
			int i=1;
			for(String t: jedis.mget("t1","t2","t3","t4","t5","t6")){
				System.out.println("t"+i+"==="+t);
				i++;
			}
			
			
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			// 释放对象池
			RedisConn.close(jedis);
		}
		
		
	}
	/**
	 * 测试序列化反序列化计算快，还是对象valueof速度快
	 */
	@Test
	public void testSD(){
		
		Jedis jedis =null;
		try{
			jedis= RedisConn.getConn();
			jedis.flushDB();
			ArrayList<Double> list=new ArrayList<Double>();
			for(int i=0;i<10;i++){
				list.add(i*100.0);
			}
			
			int max=100000;
			long a=0;
			long b=0;
			//jedis.flushAll();
			a=System.currentTimeMillis();
		    this.run_1(max, jedis, list);
		    b=System.currentTimeMillis();
		    System.out.println("run_1="+( b-a));
		//	jedis.flushDB();
			a=System.currentTimeMillis();
			this.run_2(max, jedis, list);
			b=System.currentTimeMillis();
			System.out.println("run_2="+( b-a));
		//	jedis.flushDB();
			a=System.currentTimeMillis();
		    this.run_1(max, jedis, list);
		    b=System.currentTimeMillis();
		    System.out.println("run_1="+( b-a));
		//	jedis.flushDB();
			a=System.currentTimeMillis();
			this.run_2(max, jedis, list);
			b=System.currentTimeMillis();
			System.out.println("run_2="+( b-a));
		//	jedis.flushDB();
			a=System.currentTimeMillis();
		    this.run_1(max, jedis, list);
		    b=System.currentTimeMillis();
		    System.out.println("run_1="+( b-a));
		//	jedis.flushDB();
			a=System.currentTimeMillis();
			this.run_2(max, jedis, list);
			b=System.currentTimeMillis();
			System.out.println("run_2="+( b-a));
		//	jedis.flushDB();
			a=System.currentTimeMillis();
		    this.run_1(max, jedis, list);
		    b=System.currentTimeMillis();
		    System.out.println("run_1="+( b-a));
		//	jedis.flushDB();
			a=System.currentTimeMillis();
			this.run_2(max, jedis, list);
			b=System.currentTimeMillis();
			System.out.println("run_2="+( b-a));
		//	jedis.flushDB();
			a=System.currentTimeMillis();
		    this.run_1(max, jedis, list);
		    b=System.currentTimeMillis();
		    System.out.println("run_1="+( b-a));
		//	jedis.flushDB();
			a=System.currentTimeMillis();
			this.run_2(max, jedis, list);
			b=System.currentTimeMillis();
			System.out.println("run_2="+( b-a));
		//	jedis.flushDB();
			
			a=System.currentTimeMillis();
			this.run_3(max, jedis, list);
			b=System.currentTimeMillis();
			System.out.println("run_3="+( b-a));
			//jedis.flushDB();
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			// 释放对象池
			RedisConn.close(jedis);
		}

	}
	int tta=0;
	private void run_1(int max,Jedis jedis,ArrayList<Double> list) throws ReportException{
		if(true) return;
		tta++;
		ArrayList<byte[]> b=new ArrayList<byte[]>(max*2);
		ArrayList<byte[]> bkey=new ArrayList<byte[]>(max);
		for(int i=0;i<max;i++){
			b.add(("se"+i+" "+tta).getBytes());
			bkey.add(("se"+i+" "+tta).getBytes());
			b.add(SerializeUtil.serialize(list));
		}
		jedis.mset(b.toArray(new byte[max*2][]));
		//组装回对象
		for(byte[] bvalue: jedis.mget(bkey.toArray(new byte[max][]))){
			ArrayList<Double> lv= (ArrayList<Double>) SerializeUtil.unserialize(bvalue);
			//System.out.println(lv.get(0));
		}
	}
	private void run_2(int max,Jedis jedis,ArrayList<Double> list) throws ReportException{
		if(true) return;
		tta++;
		ArrayList<byte[]> b=new ArrayList<byte[]>(max*2*10);
		ArrayList<byte[]> bkey=new ArrayList<byte[]>(max*10);
		for(int i=0;i<max;i++){
			for(int j=0;j<10;j++){
				b.add((j+"se"+i+" "+tta).getBytes());
				bkey.add((j+"se"+i+" "+tta).getBytes());
				b.add(list.get(j).toString().getBytes());
			}
		}
		jedis.mset(b.toArray(new byte[max*2*10][]));
		//组装回对象
		int i=0;
		ArrayList<Double> lv=null;
		for(byte[] bvalue: jedis.mget(bkey.toArray(new byte[max][]))){
			if(i%10==0){
				//if(lv!=null) System.out.println(lv.size());
				lv=new ArrayList<Double>(10);
			}
			lv.add(Double.valueOf(new String(bvalue)));
			i++;
		}
	}
	
	private void run_3(int max,Jedis jedis,ArrayList<Double> list) throws ReportException{
		tta++;
		ArrayList<byte[]> b=new ArrayList<byte[]>(max*2*10);
		ArrayList<byte[]> bkey=new ArrayList<byte[]>(max*10);
		for(int i=0;i<max;i++){
			for(int j=0;j<10;j++){
				//b.add((j+"se"+i).getBytes());
				bkey.add((j+"se"+i+" "+tta).getBytes());
				jedis.set((j+"se"+i+" "+tta).getBytes(), list.get(j).toString().getBytes());
			}
		}
		//jedis.mset(b.toArray(new byte[max*2*10][]));
		//组装回对象
		if(true) return;
		int i=0;
		ArrayList<Double> lv=null;
		for(byte[] bvalue: jedis.mget(bkey.toArray(new byte[max][]))){
			if(i%10==0){
				//if(lv!=null) System.out.println(lv.size());
				lv=new ArrayList<Double>(10);
			}
			lv.add(Double.valueOf(new String(bvalue)));
			i++;
		}
	}
	/**
	 * 把字节数组转换成16进制字符串
	 * 
	 * @param bArray
	 * @return
	 */
	public static String bytesToHexString(byte[] bArray) {
		StringBuilder sb = new StringBuilder(bArray.length);
		String sTemp;
		for (int i = 0; i < bArray.length; i++) {
			sTemp = Integer.toHexString(0xFF & bArray[i]);
			if (sTemp.length() < 2)
				sb.append(0);
			sb.append(sTemp.toUpperCase());
		}
		return sb.toString();
	}
	
	
	@Test
	public void testM(){
		System.out.println();
	}
}
