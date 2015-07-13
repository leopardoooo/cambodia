package com.ycsoft.report.test;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import sun.java2d.pipe.BufferedBufImgOps;

import com.ycsoft.commons.helper.DES;
import com.ycsoft.commons.helper.MD5;


/**
 * 16进制值与String/Byte之间的转换
 * @author JerryLi
 * @email lijian@dzs.mobi
 * @data 2011-10-16
 * */
public class CHexConver
{
	/** 
	 * 字符串转换成十六进制字符串
	 * @param String str 待转换的ASCII字符串
	 * @return String 每个Byte之间空格分隔，如: [61 6C 6B]
	 */  
    public static String str2HexStr(String str)
    {  

        char[] chars = "0123456789ABCDEF".toCharArray();  
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();  
        int bit;  
        
        for (int i = 0; i < bs.length; i++)
        {  
            bit = (bs[i] & 0x0f0) >> 4;  
            sb.append(chars[bit]);  
            bit = bs[i] & 0x0f;  
            sb.append(chars[bit]);
            sb.append(' ');
        }  
        return sb.toString().trim();  
    }
    
    /** 
     * 十六进制转换字符串
	 * @param String str Byte字符串(Byte之间无分隔符 如:[616C6B])
	 * @return String 对应的字符串
     */  
    public static String hexStr2Str(String hexStr)
    {  
        String str = "0123456789ABCDEF";  
        char[] hexs = hexStr.toCharArray();  
        byte[] bytes = new byte[hexStr.length() / 2];  
        int n;  

        for (int i = 0; i < bytes.length; i++)
        {  
            n = str.indexOf(hexs[2 * i]) * 16;  
            n += str.indexOf(hexs[2 * i + 1]);  
            bytes[i] = (byte) (n & 0xff);  
        }  
        return new String(bytes);  
    }
    
    /**
     * bytes转换成十六进制字符串
     * @param byte[] b byte数组
     * @return String 每个Byte值之间空格分隔
     */
    public static String byte2HexStr(byte[] b)
    {
        String stmp="";
        StringBuilder sb = new StringBuilder("");
        for (int n=0;n<b.length;n++)
        {
            stmp = Integer.toHexString(b[n] & 0xFF);
            sb.append((stmp.length()==1)? "0"+stmp : stmp);
            sb.append(" ");
        }
        return sb.toString().toUpperCase().trim();
    }
    
    /**
     * bytes字符串转换为Byte值
     * @param String src Byte字符串，每个Byte之间没有分隔符
     * @return byte[]
     */
    public static byte[] hexStr2Bytes(String src)
    {
        int m=0,n=0;
        int l=src.length()/2;
        System.out.println(l);
        byte[] ret = new byte[l];
        for (int i = 0; i < l; i++)
        {
            m=i*2+1;
            n=m+1;
            ret[i] = Byte.decode("0x" + src.substring(i*2, m) + src.substring(m,n));
        }
        return ret;
    }

    /**
     * String的字符串转换成unicode的String
     * @param String strText 全角字符串
     * @return String 每个unicode之间无分隔符
     * @throws Exception
     */
    public static String strToUnicode(String strText)
    	throws Exception
    {
        char c;
        StringBuilder str = new StringBuilder();
        int intAsc;
        String strHex;
        for (int i = 0; i < strText.length(); i++)
        {
            c = strText.charAt(i);
            intAsc = (int) c;
            strHex = Integer.toHexString(intAsc);
            if (intAsc > 128)
            	str.append("\\u" + strHex);
            else // 低位在前面补00
            	str.append("\\u00" + strHex);
        }
        return str.toString();
    }
    
    /**
     * unicode的String转换成String的字符串
     * @param String hex 16进制值字符串 （一个unicode为2byte）
     * @return String 全角字符串
     */
    public static String unicodeToString(String hex)
    {
        int t = hex.length() / 6;
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < t; i++)
        {
            String s = hex.substring(i * 6, (i + 1) * 6);
            // 高位需要补上00再转
            String s1 = s.substring(2, 4) + "00";
            // 低位直接转
            String s2 = s.substring(4);
            // 将16进制的string转为int
            int n = Integer.valueOf(s1, 16) + Integer.valueOf(s2, 16);
            // 将int转换为字符
            char[] chars = Character.toChars(n);
            str.append(new String(chars));
        }
        return str.toString();
    }
    
    public static String md5s(MessageDigest md,StringBuilder buf,String...plainText) {
    	 // try {
    	   md.reset();
    	   buf.delete(0, buf.length());
    	   for(String bb:plainText)
    		   md.update(bb.getBytes());
    	   byte b[] = md.digest();
    	   
    	   
    	  
   		String stmp = null;
   		for (byte element : b) {
   			stmp = Integer.toHexString(element & 0XFF);
   			if (stmp.length() == 1) {
   				buf.append("0").append(stmp);
   			} else {
   				buf.append(stmp);
   			}
   		}
   		/**
    	   int i;
    	   for (int offset = 0; offset < b.length; offset++) {
    	    i = b[offset];
    	    if (i < 0){
    	     i += 256;
    	    }
    	    if (i < 16) {
    	    	buf.append("0");
    	    }
    	    buf.append(Integer.toHexString(i));
    	   }
    	   **/
    	   return buf.toString();
    }
    
    public static String md5s(MessageDigest md,String...plainText) throws Exception {
   	 // try {
    	MessageDigest 
			messageDigest = MessageDigest.getInstance("SHA-1");

		messageDigest.update(plainText[0].getBytes());
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
		
   }
    static final char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
    		'e', 'f' };

	public static String md5(MessageDigest messageDigest,String string) 
	{
		
		try {

			messageDigest=MessageDigest.getInstance("SHA-1");
		
			//messageDigest.reset();
			messageDigest.update(string.getBytes());
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
		return null;
		}
	}
	
	public static String md5(String string) 
	{
		
		try {

			MessageDigest messageDigest=MessageDigest.getInstance("SHA-1");
		
			messageDigest.update(string.getBytes());
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
		return null;
		}
	}
	
    public static void main(String[] args) {
    	String a1=new String("abc");
    	String S[]={"a","b","c"};
    	
    	long a=System.currentTimeMillis();
    	int i=0;
    	MessageDigest alga=null;
    	String ca2="rZLCcSfJ3ZowYcG你好爱上 地方你的UDnUTEbeqFgYTo/9en3mJYFsbGh90WXhqzDiH2vmQ6qlffTLxTMTNcY5Fq8zyQBOHII1aFOZTUfgqjUlnu3SZ+PC24acTXSUvJbbGvtp9+0OTBcoaRKvw+qdQABA56o1vixTLNg==";
    	
    	long b=0;
    	try {
			alga =  MessageDigest.getInstance("MD5");
			StringBuilder test1=new StringBuilder();
			System.out.println(md5s(alga,test1,a1));
			System.out.println(md5s(alga,S)
		    		);
			System.out.println(//md5s(alga,ca2);
		    		md5(alga,ca2));
			a=System.currentTimeMillis();
   	    	while(i<1000000){
	    		i++;
	    		//StringBuilder test1=new StringBuilder();
	    		md5(ca2);
	    		//System.out.println();
	    		//System.out.println(md5s(alga,test1,"abc"));
	    		//System.out.println(md5s(alga,test1,"a"));
	    		//System.out.println();
	    	}
   	    	b=System.currentTimeMillis();
   	    	System.out.println(System.currentTimeMillis()-a);
   	    	i=0;
   	    	a=System.currentTimeMillis();
   	    	while(i<1000000){
	    		i++;
	    		//StringBuilder test1=new StringBuilder();
	    		//md5s(alga,ca2);
	    		md5(alga,ca2);
	    		//System.out.println();
	    		//System.out.println(md5s(alga,test1,"abc"));
	    		//System.out.println(md5s(alga,test1,"a"));
	    		//System.out.println();
	    	}
   	    	b=System.currentTimeMillis();
   	    	System.out.println("N"+(System.currentTimeMillis()-a));
   	    	i=0;
   	    	a=System.currentTimeMillis();
   	    	while(i<1000000){
	    		i++;
	    		//StringBuilder test1=new StringBuilder();
	    		md5(ca2);
	    		//System.out.println();
	    		//System.out.println(md5s(alga,test1,"abc"));
	    		//System.out.println(md5s(alga,test1,"a"));
	    		//System.out.println();
	    	}
   	    	b=System.currentTimeMillis();
   	    	System.out.println(System.currentTimeMillis()-a);
   	    	i=0;
   	    	a=System.currentTimeMillis();
   	    	while(i<1000000){
	    		i++;
	    		//StringBuilder test1=new StringBuilder();
	    		//md5s(alga,ca2);
	    		md5(alga,ca2);
	    		//System.out.println();
	    		//System.out.println(md5s(alga,test1,"abc"));
	    		//System.out.println(md5s(alga,test1,"a"));
	    		//System.out.println();
	    	}
   	    	b=System.currentTimeMillis();
   	    	System.out.println("N"+(System.currentTimeMillis()-a));
   	    	a=System.currentTimeMillis();
   	    	i=0;
   	    	while(i<1000000){
	    		i++;
	    		//StringBuilder test1=new StringBuilder();
	    		md5(ca2);
	    		//System.out.println();
	    		//System.out.println(md5s(alga,test1,"abc"));
	    		//System.out.println(md5s(alga,test1,"a"));
	    		//System.out.println();
	    	}
   	    	b=System.currentTimeMillis();
   	    	System.out.println(System.currentTimeMillis()-a);
   	    	i=0;
   	    	a=System.currentTimeMillis();
   	    	while(i<1000000){
	    		i++;
	    		//StringBuilder test1=new StringBuilder();
	    		//md5s(alga,ca2);
	    		md5(alga,ca2);
	    		//System.out.println();
	    		//System.out.println(md5s(alga,test1,"abc"));
	    		//System.out.println(md5s(alga,test1,"a"));
	    		//System.out.println();
	    	}
   	    	b=System.currentTimeMillis();
   	    	System.out.println("N"+(System.currentTimeMillis()-a));
   	    	a=System.currentTimeMillis();
   	    	i=0;
   	    	while(i<1000000){
	    		i++;
	    		//StringBuilder test1=new StringBuilder();
	    		md5(ca2);
	    		//System.out.println();
	    		//System.out.println(md5s(alga,test1,"abc"));
	    		//System.out.println(md5s(alga,test1,"a"));
	    		//System.out.println();
	    	}
   	    	b=System.currentTimeMillis();
   	    	System.out.println(System.currentTimeMillis()-a);
   	    	i=0;
   	    	a=System.currentTimeMillis();
   	    	while(i<1000000){
	    		i++;
	    		//StringBuilder test1=new StringBuilder();
	    		//md5s(alga,ca2);
	    		md5(alga,ca2);
	    		//System.out.println();
	    		//System.out.println(md5s(alga,test1,"abc"));
	    		//System.out.println(md5s(alga,test1,"a"));
	    		//System.out.println();
	    	}
   	    	b=System.currentTimeMillis();
   	    	System.out.println("N"+(System.currentTimeMillis()-a));
   	    	a=System.currentTimeMillis();
   	    	i=0;
   	    	while(i<1000000){
	    		i++;
	    		//StringBuilder test1=new StringBuilder();
	    		md5(ca2);
	    		//System.out.println();
	    		//System.out.println(md5s(alga,test1,"abc"));
	    		//System.out.println(md5s(alga,test1,"a"));
	    		//System.out.println();
	    	}
   	    	b=System.currentTimeMillis();
   	    	System.out.println(System.currentTimeMillis()-a);
   	    	i=0;
   	    	a=System.currentTimeMillis();
   	    	while(i<1000000){
	    		i++;
	    		//StringBuilder test1=new StringBuilder();
	    		//md5s(alga,ca2);
	    		md5(alga,ca2);
	    		//System.out.println();
	    		//System.out.println(md5s(alga,test1,"abc"));
	    		//System.out.println(md5s(alga,test1,"a"));
	    		//System.out.println();
	    	}
   	    	b=System.currentTimeMillis();
   	    	System.out.println("N"+(System.currentTimeMillis()-a));
   	    	a=System.currentTimeMillis();
   	    	i=0;
   	    	while(i<1000000){
	    		i++;
	    		//StringBuilder test1=new StringBuilder();
	    		md5(ca2);
	    		//System.out.println();
	    		//System.out.println(md5s(alga,test1,"abc"));
	    		//System.out.println(md5s(alga,test1,"a"));
	    		//System.out.println();
	    	}
   	    	b=System.currentTimeMillis();
   	    	System.out.println(System.currentTimeMillis()-a);
   	    	i=0;
   	    	a=System.currentTimeMillis();
   	    	while(i<1000000){
	    		i++;
	    		//StringBuilder test1=new StringBuilder();
	    		//md5s(alga,ca2);
	    		md5(alga,ca2);
	    		//System.out.println();
	    		//System.out.println(md5s(alga,test1,"abc"));
	    		//System.out.println(md5s(alga,test1,"a"));
	    		//System.out.println();
	    	}
   	    	b=System.currentTimeMillis();
   	    	System.out.println("N"+(System.currentTimeMillis()-a));
   	    	a=System.currentTimeMillis();
   	    	i=0;
   	    	while(i<1000000){
	    		i++;
	    		//StringBuilder test1=new StringBuilder();
	    		md5(ca2);
	    		//System.out.println();
	    		//System.out.println(md5s(alga,test1,"abc"));
	    		//System.out.println(md5s(alga,test1,"a"));
	    		//System.out.println();
	    	}
   	    	b=System.currentTimeMillis();
   	    	System.out.println(System.currentTimeMillis()-a);
   	    	i=0;
   	    	a=System.currentTimeMillis();
   	    	while(i<1000000){
	    		i++;
	    		//StringBuilder test1=new StringBuilder();
	    		//md5s(alga,ca2);
	    		md5(alga,ca2);
	    		//System.out.println();
	    		//System.out.println(md5s(alga,test1,"abc"));
	    		//System.out.println(md5s(alga,test1,"a"));
	    		//System.out.println();
	    	}
   	    	b=System.currentTimeMillis();
   	    	System.out.println("N"+(System.currentTimeMillis()-a));
    	} catch (Exception e) {
			
			e.printStackTrace();
			/**
			 * 32
3448
N3230
3152
N3215
3164
N3200
3199
N3215
3199
N3214
3160
N3199
3199
N3199
			 */
		}
   	}
}