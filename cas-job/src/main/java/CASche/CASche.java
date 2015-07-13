package CASche;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import CASche.CASPrivatePara;
import CASche.CATools;
import CASche.CALogFile;
import CASche.ConfigFile;
import CASche.CASchePara;

public class CASche {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String processName = "";
		boolean test = false;
		boolean encryptPass = false;
		String txt  = "";
		String plain = "";
		args = new String[1];
		args[0]="-f";
		boolean fileFlag = false;
		for (int i=0; i<args.length; i++){
			if (args[i].charAt(0) == '-'){
				if (args[i].charAt(1) == 'p' || args[i].charAt(1)=='P'){
					processName = args[i].substring(2);
				}
				if (args[i].charAt(1) == 't' || args[i].charAt(1)=='T'){
					test = true;
				}
				if (args[i].charAt(1) == 'f' || args[i].charAt(1)=='F'){
					fileFlag = true;
				}
				
				if (args[i].charAt(1) == 'e' || args[i].charAt(1)=='E'){
					encryptPass = true;
					txt = args[i].substring(2);
				}
				
			}
		}

		if (encryptPass == true){
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

		ConfigFile cf = new ConfigFile("./config/casche.properties");
		String ip = cf.getValue("IP");
		String port = cf.getValue("PORT");
		String service = cf.getValue("SERVICE");
		String jdbcurl = cf.getValue("JDBCURL");
		String user = cf.getValue("USER");
		String password = cf.getValue("PASSWORD");
		String logPath = cf.getValue("LOGPATH");
		
		if ("".equals(logPath) == true){
			logPath = "../log";
		}
		if (logPath.charAt(logPath.length()-1) == '/'){
			logPath = logPath.substring(0, logPath.length()-1);
		}
		
		if ("".equals(ip) == true){
			CALogFile.logMsg(logPath,  "main", "IP item don't set value ...!");
			System.out.println("IP item don't set value ...!");
			System.exit(-1);
		}
		
		if ("".equals(port) == true){
			CALogFile.logMsg(logPath,  "main", "PORT item don't set value ...!");
			System.out.println("PORT item don't set value ...!");
			System.exit(-1);
		}
		if ("".equals(service) == true){
			CALogFile.logMsg(logPath,  "main", "SERVICE item don't set value ...!");
			System.out.println("SERVICE item don't set value ...!");
			System.exit(-1);
		}
		if ("".equals(jdbcurl) == true){
			CALogFile.logMsg(logPath,  "main", "SERVICE item don't set value ...!");
			System.out.println("JDBCURL item don't set value ...!");
			System.exit(-1);
		}

		if ("".equals(user) == true){
			CALogFile.logMsg(logPath, "main", "USER item don't set value ...!");
			System.out.println("USER item don't set value ...!");
			System.exit(-1);
		}

		if ("".equals(password) == true){
			CALogFile.logMsg(logPath,  "main", "PASSWORD item don't set value ...!");
			System.out.println("PASSWORD item don't set value ...!");
			System.exit(-1);
		}

		byte[] pnBytes = CATools.hexStr2Bytes(password);
		
    	Cipher cipherEn = null;
		try {
			cipherEn = Cipher.getInstance("DES/ECB/Nopadding");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	Key keyK = new javax.crypto.spec.SecretKeySpec("abcdefgh".getBytes(), "DES");
		try {
			cipherEn.init(Cipher.DECRYPT_MODE, keyK);
		} catch (InvalidKeyException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		try {
			byte[] pn = cipherEn.doFinal(pnBytes, 0, pnBytes.length);
			int len = pn.length;
			for (int i=pn.length-1; i>=0&&pn[i]==0x00; i-- ){
				len--;
			}
			password = new String(pn);
			password = password.substring(0, len);
						
		} catch (IllegalBlockSizeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (BadPaddingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		CASchePara schePara = new CASchePara();
		if (fileFlag==true){
			ParaFromFile pff = new ParaFromFile("./config/casche.properties");
			pff.setSchePara(schePara);
			if (pff.GetPara(processName) < 0){
				CALogFile.logMsg(logPath, "main", "parameter define error in ./config/casche.properties");
				System.exit(-1);
				
			}
		}
		else{
			ParaFromDB pfdb = new ParaFromDB(jdbcurl, user, password);
			pfdb.setSchePara(schePara);
			while(pfdb.GetPara(processName)< 1){
				CALogFile.logMsg(logPath, "main", "parameter define in busi.t_server_parameter and busi.t_ca_interface ");
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		Iterator iter = schePara.getMapCAS().entrySet().iterator(); 
		while (iter.hasNext()) { 
		    Map.Entry entry = (Map.Entry) iter.next(); 
		    String caName = (String)entry.getKey();
		    CASPrivatePara casPPara = (CASPrivatePara)entry.getValue(); 
		    
		    DealThread dt = new DealThread();
		    dt.setCasp(schePara);
		    dt.setCaspp(casPPara);
		    dt.setIp(ip);
		    dt.setPort(port);
		    dt.setService(service);
		    dt.setUser(user);
		    dt.setJdbcurl(jdbcurl);
		    dt.setPassword(password);
		    dt.setLogPath(logPath);
		    dt.setPrefix("main");
		    new Thread(dt).start();
		    CALogFile.logMsg(logPath,  "main", " start " + caName + " thread ...!");
		}

		
	}

}
