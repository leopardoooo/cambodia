package CASche.caschedule;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import CASche.CATools;

public class DESmain {

	public static void main(String args[]){
		String txt="busi";//加密源文件
		 try {
	        	byte[] txtBytes = new byte[48];
	        	System.arraycopy(txt.getBytes(), 0, txtBytes, 0, txt.getBytes().length);
	        	int len = txt.getBytes().length;
	        	int len2 = len % 8;
	        	if (len2 > 0){
	        		for (int i=0; i<8-len2; i++){
	        			txtBytes[len++] = 0x00;
	        		}
	        	}

	        	Cipher cipherEn=null;
				try {
					cipherEn = Cipher.getInstance("DES/ECB/Nopadding");
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchPaddingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	//Cipher cipherEn = Cipher.getInstance("DES/CBC/PKCS5Padding");
	        	Key keyK = new javax.crypto.spec.SecretKeySpec("abcdefgh".getBytes(), "DES");
				cipherEn.init(Cipher.ENCRYPT_MODE, keyK);
				byte[] pnBytes = cipherEn.doFinal(txtBytes, 0, len);
				
				System.out.println(CATools.byte2HexStr(pnBytes));
				System.exit(1);
			} catch (InvalidKeyException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IllegalBlockSizeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BadPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 

	}
}
