package com.ycsoft.commons.helper;

import java.io.IOException;
import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 *
 * 使用DES加密与解密,可对byte[],String类型进行加密与解密 密文可使用String,byte[]存储.
 *
 * 方法: void getKey(String strKey)从strKey的字条生成一个Key
 *
 * String getEncString(String strMing)对strMing进行加密,返回String密文 String
 * getDesString(String strMi)对strMin进行解密,返回String明文
 *
 * byte[] getEncCode(byte[] byteS)byte[]型的加密 byte[] getDesCode(byte[]
 * byteD)byte[]型的解密
 */

public class DES {
	Key key;
	private static final String defaultKey = "2012 YCSHBOSS V4.0";

	public DES(String string) {
		if(string != null && !string.equals("")){
			getKey(string);
		}else{
			getKey(defaultKey);
		}
	}

	public DES() {
		getKey(defaultKey);
	}

	/**
	 * 根据参数生成KEY
	 *
	 * @param strKey
	 */
	public void getKey(String strKey) {
		try {
			KeyGenerator _generator = KeyGenerator.getInstance( "AES" );  
	        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG" );  
	        secureRandom.setSeed(strKey.getBytes());  
	        _generator.init(128,secureRandom);  
	        key = _generator.generateKey();
			_generator = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 加密String明文输入,String密文输出
	 *
	 * @param strMing
	 * @return
	 */
	public String getEncString(String strMing) {
		byte[] byteMi = null;
		byte[] byteMing = null;
		String strMi = "";
		BASE64Encoder base64en = new BASE64Encoder();
		try {
			byteMing = strMing.getBytes("UTF8");
			byteMi = getEncCode(byteMing);
			strMi = base64en.encode(byteMi);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			base64en = null;
			byteMing = null;
			byteMi = null;
		}
		return strMi;
	}

	/**
	 * 解密 以String密文输入,String明文输出
	 *
	 * @param strMi
	 * @return
	 * @throws IOException 
	 */
	public String getDesString(String strMi) throws Exception {
		BASE64Decoder base64De = new BASE64Decoder();
		byte[] byteMing = null;
		byte[] byteMi = null;
		String strMing = "";
		byteMi = base64De.decodeBuffer(strMi);
		byteMing = getDesCode(byteMi);
		strMing = new String(byteMing, "UTF8");
		
		return strMing;
	}

	/**
	 * 加密以byte[]明文输入,byte[]密文输出
	 *
	 * @param byteS
	 * @return
	 */
	private byte[] getEncCode(byte[] byteS) {
		byte[] byteFina = null;
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byteFina = cipher.doFinal(byteS);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cipher = null;
		}
		return byteFina;
	}

	/**
	 * 解密以byte[]密文输入,以byte[]明文输出
	 *
	 * @param byteD
	 * @return
	 */
	private byte[] getDesCode(byte[] byteD) {
		Cipher cipher;
		byte[] byteFina = null;
		try {
			cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, key);
			byteFina = cipher.doFinal(byteD);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cipher = null;
		}
		return byteFina;

	}

	public static void main(String[] args) {

		DES des = new DES();// 实例化一个对像

		try{
			
//			System.out.println(des.getEncString("123busi"));
			
		String strEnc = des.getEncString("busi123");// 加密字符串,返回String的密文
		System.out.println(strEnc);

		String strDes = des.getDesString(strEnc);// 把String 类型的密文解密
		System.out.println(strDes);
		}catch (Exception e){
//			e.printStackTrace();
			System.err.println(1111);
		}
	}

}
