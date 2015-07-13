package CASche;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CATools {

	public static String GetNowDate(){   
	    String temp_str="";   
	    Date dt = new Date();   
	    //最后的aa表示“上午”或“下午”    HH表示24小时制    如果换成hh表示12小时制   
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");   
	    temp_str=sdf.format(dt);   
	    return temp_str;   
	}  


	/* date = 'yyyymmdd' */
	public static boolean isDate(String date)
	{
//		String dd = date.substring(0, 4) + "."+ date.substring(4, 6) + "." + date.substring(6, 8);
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
		String dd = date.substring(0, 4) + date.substring(4, 6) + date.substring(6, 8);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");		
		sdf.setLenient(false);
		try{
			java.util.Date d=sdf.parse(dd); 
		}catch(java.text.ParseException ex){
			return false;
		}
		return true;
	}
		
	/* time : 'HHmmss' */
	public static boolean isTime(String time)
	{
		SimpleDateFormat   sdf=new   SimpleDateFormat( "HHmmss"); 
		sdf.setLenient(false);
		try{ 
			java.util.Date   d = sdf.parse(time); 
		}catch(Exception   ex){ 
			return false; 
		} 
		return true;
	}
		
	/* datetime : 'yyyyMMddHHmmss' */
	public static boolean isDateTime(String dateTime)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		sdf.setLenient(false);
		try{
			java.util.Date d = sdf.parse(dateTime);
		}catch(Exception ex){
			return false;
		}
		return true;	
	}

	// dateTime: yyyyMMddHHmmss
	public static byte[] StringDate2ByteDate(String dateTime)
	{
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			sdf.setLenient(false);
			java.util.Date d = sdf.parse(dateTime);
			long l = d.getTime()/1000;
//			byte[] b = CATools.intToByteArray((int)l);
//			System.out.println(CATools.byte2HexStr(b));
			return CATools.intToByteArray((int)l); 
		
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}

	public static boolean CheckDate(String date)
	{
		if (date.length()>14){
			String date2 = date.substring(0, 14);
			return CATools.isDateTime(date2);
		}
		else if (date.length()==14){
			return CATools.isDateTime(date);
		}
		else if (date.length() == 12){
			String date2 = date + "01";
			return CATools.isDateTime(date2);
		}
		else if (date.length() == 10){
			String date2 = date + "0001";
			return CATools.isDateTime(date2);
		}
		else if (date.length()==8){
			return CATools.isDate(date);
		}
		else if (date.length() == 6){
			return CATools.isDate(date + "01");
		}
		
		return false;
	}

	public static String date2Str(Date date)
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyyDDddHHmmss");
		df.setLenient(false);
		String sDate = df.format(date);
		return sDate;
	}
	
	/*  */
	public static String getValue(String str, String name)
	{
		String value = "";
		int index = str.indexOf(name);
		if (index != -1 ){
			index += name.length();
			try{
				while(str.charAt(index) != '\''){
					value += str.charAt(index);
					index++;
				}
			}catch(java.lang.IndexOutOfBoundsException e){
				;
			}
		}
		return value;
	}
	
    public static String byte2HexStr(byte[] b) {
        String hs="";
        String stmp="";
        for (int n=0;n<b.length;n++) {
            stmp=(Integer.toHexString(b[n] & 0XFF));
            if (stmp.length()==1) hs=hs+"0"+stmp;
            else hs=hs+stmp;
            if (n<b.length-1)  hs=hs+" ";
        }
        return hs.toUpperCase();
    }

    public  static byte uniteBytes(String src0, String src1)
    {
        byte b0 = Byte.decode("0x" + src0).byteValue();
        b0 = (byte) (b0 << 4);
        byte b1 = Byte.decode("0x" + src1).byteValue();
        byte ret = (byte) (b0 | b1);
        return ret;
    }

    public static String str2HexStr(String str) 
    {
        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;
        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
        }
        return sb.toString();
    }
    
    public static String hexStr2Str(String hexStr) 
    {
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;
        for (int i = 0; i < bytes.length; i++) {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes);
    }

    public static byte[] hexStr2Bytes(String src) 
    {
        int m=0,n=0;
        int l=src.length()/2;
        byte[] ret = new byte[l];
        for (int i = 0; i < l; i++) {
            m=i*2+1;
            n=m+1;
            ret[i] = uniteBytes(src.substring(i*2, m),src.substring(m,n));
        }
        return ret;
    }

    public static String stringToUnicode(String strText) throws Exception {
        char c;
        String strRet = "";
        int intAsc;
        String strHex;
        for (int i = 0; i < strText.length(); i++) {
            c = strText.charAt(i);
            intAsc = (int) c;
            strHex = Integer.toHexString(intAsc);
            if (intAsc > 128) {
                strRet += "\\u" + strHex;
            } else {
                // 低位在前面补00
                strRet += "\\u00" + strHex;
            }
        }
        return strRet;
    }

    public static String unicodeToString(String hex) {
        int t = hex.length() / 6;
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < t; i++) {
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
    
    public static byte[] shortToByteArray(short s) 
    {
    	byte[] shortBuf = new byte[2];
    	for(int i=0;i<2;i++) {
    	   int offset = (shortBuf.length - 1 -i)*8;
    	   shortBuf[i] = (byte)((s>>>offset)&0xff);
    	}
    	return shortBuf;
	}

	public static int byteArrayToShort(byte [] b) 
	{
		return (b[0] << 8) + (b[1] & 0xFF);
	}

	public static int byteArrayToShort(byte [] b, int startPos) 
	{
		return (b[startPos] << 8) + (b[startPos+1] & 0xFF);
	}

	public static byte[] intToByteArray(int value)
	{
		byte[] b = new byte[4];
		for (int i = 0; i < 4; i++) {
			int offset = (b.length - 1 - i) * 8;
			b[i] = (byte) ((value >>> offset) & 0xFF);
		}
		return b;
	}

	public static int byteArrayToInt(byte [] b) 
	{
		return (b[0] << 24)
				+ ((b[1] & 0xFF) << 16)
				+ ((b[2] & 0xFF) << 8)
				+ (b[3] & 0xFF);
	} 
	public static int byteArrayToInt(byte [] b, int startPos) 
	{
		return (b[startPos] << 24)
				+ ((b[startPos+1] & 0xFF) << 16)
				+ ((b[startPos+2] & 0xFF) << 8)
				+ (b[startPos+3] & 0xFF);
	} 

	public static byte char2BCD(byte ch)
	{
	        byte ch2;
	        if (ch >= '0' && ch <= '9'){
	                ch2 = (byte)(ch-'0');
	                return ch2;
	        }
	        if (ch >= 'A' && ch <= 'Z'){
	                ch2 = (byte)(ch - 'A'+10);
	                return ch2;
	        }
	        if (ch >= 'a' && ch <= 'z'){
	                ch2 = (byte)(ch - 'z' + 10);
	                return ch2;
	        }
	        return 0;
	}

	 public static void sleeping(int seconds)
	{
		try {
			Thread.sleep(seconds*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}


