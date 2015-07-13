package com.ycsoft.commons.helper;
import java.security.*;  
import javax.crypto.Cipher;  
import javax.crypto.SecretKey;  
import javax.crypto.SecretKeyFactory;  
import javax.crypto.spec.DESKeySpec;  
  
public class DESUtil{  
    private static final String PASSWORD_CRYPT_KEY = "ychbWSZF88";  
  
    private final static String DES = "DES";  
  
    public static byte[] encrypt(byte[] src, byte[] key) throws Exception {  
        SecureRandom sr = new SecureRandom();  
        DESKeySpec dks = new DESKeySpec(key);  
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);  
        SecretKey securekey = keyFactory.generateSecret(dks);  
        Cipher cipher = Cipher.getInstance(DES);  
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);  
        return cipher.doFinal(src);  
    }  
  
    public static byte[] decrypt(byte[] src, byte[] key) throws Exception {  
        SecureRandom sr = new SecureRandom();  
        DESKeySpec dks = new DESKeySpec(key);  
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);  
        SecretKey securekey = keyFactory.generateSecret(dks);  
        Cipher cipher = Cipher.getInstance(DES);  
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);  
        return cipher.doFinal(src);  
    }  
  
    /** 
     * ������� 
     *  
     */  
    public final static String decrypt(String data) {  
        try {  
            return new String(decrypt(hex2byte(data.getBytes()),  
                    PASSWORD_CRYPT_KEY.getBytes()));  
        } catch (Exception e) {  
        }  
        return null;  
    }  
  
    /** 
     * ������� 
     *  
     */  
    public final static String encrypt(String password) {  
        try {  
            return byte2hex(encrypt(password.getBytes(), PASSWORD_CRYPT_KEY  
                    .getBytes()));  
        } catch (Exception e) {  
        }  
        return null;  
    }  
  
    public static String byte2hex(byte[] b) {  
        String hs = "";  
        String stmp = "";  
        for (int n = 0; n < b.length; n++) {  
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));  
            if (stmp.length() == 1)  
                hs = hs + "0" + stmp;  
            else  
                hs = hs + stmp;  
        }  
        return hs.toUpperCase();  
    }  
  
    public static byte[] hex2byte(byte[] b) {  
        if ((b.length % 2) != 0)  
            throw new IllegalArgumentException("���Ȳ���ż��");  
        byte[] b2 = new byte[b.length / 2];  
        for (int n = 0; n < b.length; n += 2) {  
            String item = new String(b, n, 2);  
            b2[n / 2] = (byte) Integer.parseInt(item, 16);  
        }  
        return b2;  
    }  
      
    public static void main(String[] args) {  
    	long t1=System.currentTimeMillis();
		
		
        try {  
            String test = "8270002335887164";  
            System.out.println("����ǰ���ַ�" + test + "\t" + test.length());  
            System.out.println("���ܺ���ַ�" + DESUtil.encrypt(test) + "\t" + DESUtil.encrypt(test).length());  
            System.out.println("���ܺ���ַ�" + DESUtil.decrypt(DESUtil.encrypt(test)) + "\t" + DESUtil.decrypt(DESUtil.encrypt(test)).length());  
        } catch (Exception e) {   
            e.printStackTrace();  
        } 
        
        long t2=System.currentTimeMillis();
		System.out.println("t="+(t2-t1));
    }  
}  