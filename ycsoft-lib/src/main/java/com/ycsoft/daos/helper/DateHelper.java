package com.ycsoft.daos.helper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 包含一系列对日期处理的函数
 *
 * @author hh
 * @date Dec 3, 2009 1:16:21 PM
 */
public class DateHelper {

	private static NewDateFormat df = new NewDateFormat("yyyy-MM-dd HH:mm:ss");
	private static NewDateFormat sdf = new NewDateFormat("yyyy-MM-dd");

	/**
	* 获取指定日期,输出格式化为yyyy-MM-dd HH:mm:ss
	* @return
	*/
	public static String format(Date date) {
		return df.format(date);
	}
	/**
	* 获取当前时刻日期，格式为yyyy-MM-dd HH:mm:ss
	* @return
	*/
	public static String format() {
		Date date = new Date();
		return df.format(date);
	}
	/**
	 * 获取当前日期年份
	 * @return
	 */
	public static int getCurrYear() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		return c.get(Calendar.YEAR);
	}
	/**
	 * 获取当前日期月份
	 * @return
	 */
	public static int getCurrMonth() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		return c.get(Calendar.MONTH);
	}
	/**
	 * 获取当前日期天
	 * @return
	 */
	public static int getCurrDAY() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		return c.get(Calendar.DATE);
	}
	/**
	* 将时间字符串格式化yyyy-MM-dd HH:mm:ss
	* @return
	*/
	public static String format(String datestr) {
		Date date;
		try {
			date = df.parse(datestr);
		} catch (ParseException e) {
			date = new Date();
		}
		return df.format(date);
	}
	/**
	 * 将短时间格式时间转换为字符串 yyyy-MM-dd
	 * @param dateDate
	 * @return
	 */
	public static String dateToStr(java.util.Date dateDate) {
		String dateString = sdf.format(dateDate);
		return dateString;
	}
	/**
	 * 将短时间格式字符串转换为时间 yyyy-MM-dd
	 * @param strDate
	 * @return
	 */
	public static Date strToDate(String strDate) {
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = sdf.parse(strDate, pos);
		return strtodate;
	}
	/**
	* 当前日期按分隔符号"spt"分隔返回显示，不含时分秒
	* @return
	*/
	public static String getDate(String spt) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy" + spt + "MM" + spt+ "dd");
		java.util.Date date = new java.util.Date();
		return sdf.format(date);
	}
	/**
	* 获取给定时间加Ｎ天后的日期,
	* 如果N为负数那么就是给定时间减天
	* @return date
	*/
	public static java.util.Date addDate(java.util.Date date, int day) {
		java.util.Calendar c = java.util.Calendar.getInstance();
			c.setTime(date);
			c.setTimeInMillis(c.getTimeInMillis() + (long) day * 24 * 3600 * 1000);
		return c.getTime();
	}
	/**
	* 获取两个日期之间的间隔天数(1)
	* @return
	*/
	public static int getDiffDays(Date beginDate, Date endDate){
		long lBeginTime = beginDate.getTime();
		long lEndTime = endDate.getTime();
		int iDay = (int) ((lEndTime - lBeginTime) / 86400000);
		return iDay;
	}
	/**
	* 获取两个日期之间的间隔天数(2)
	* @return
	*/
	public static int getDifferDays(String beginDate,String endDate) {
		Date date1 = null, date2 = null;
		int days=0;
		try {
			date1 = sdf.parse(beginDate);
			date2 = sdf.parse(endDate);
			days=(int) ((date2.getTime()-date1.getTime())/86400000);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		   return days;
	}
	/**
	 * 获取本月第一天（根据当前时间）
	 * @return
	 */
	public static String getFirstDateInCurrentMonth() {
	    Calendar calendar = Calendar.getInstance();
	    String year = String.valueOf(calendar.get(Calendar.YEAR));
	    String month = String.valueOf(calendar.get(Calendar.MONTH)+1);
	    String day = "01";
	    return year +"-"+ (month.length() == 1 ? "0" + month : month)+"-" + day;
	  }
	/**
	 * 获取本月最后一天（根据当前时间）
	 */
	public String getLastDateInCurrentMonth(){
	   String str = "";
	   Calendar lastDate = Calendar.getInstance();
	   lastDate.set(Calendar.DATE,1);//设为当前月的1号
	   lastDate.add(Calendar.MONTH,1);//加一个月，变为下月的1号
	   lastDate.add(Calendar.DATE,-1);//减去一天，变为当月最后一天

	   str=sdf.format(lastDate.getTime());
	   return str;
	}
	 /**
	  * 获取本年度第一天（根据当前时间）
	  * @return
	  */
	 public static String getFirstDateInCurrentYear() {
		 Calendar calendar = Calendar.getInstance();
		 String year = String.valueOf(calendar.get(Calendar.YEAR));
		 String month = "01";
		 String day = "01";
		 return year +"-"+ month+"-" + day+" 00:00:00";
	 }
	/**
	 * 获取下个月的第一天，根据输入时间
	 * @return
	 */
	public static Date getNextMonth(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_MONTH,1);//set the first day.
		c.add(Calendar.MONDAY,1); //set next month
		return c.getTime();
	}
	/**
	 * 获取明天日期 格式yyyy-MM-dd
	 * @return
	 */
	public static String getNextDay(){
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH,1);
		return sdf.format(c.getTime());
	}
	/**
	 * 获得一个月共有多少天
	 * @param date
	 * @return
	 */
	public static String  getDateSumDay(String date){
		String day="";
		String month = date.substring(5, 7);
		if(month.equals("02")){
			if(isLeapYear(date)){
			day="29";
			}else{
			day="28";
			}
		}else if(month.equals("01")||month.equals("03")||month.equals("05")||month.equals("07")||month.equals("08")||month.equals("10")||month.equals("12")){
		    day="31";
		}else{
			day="30";
		}
		String dateInfo1 = date.substring(0,8)+""+ day ;
		return  dateInfo1;
	}
	/**
	 * 指定日期(默认当天)的第一个时刻
	 * @param date
	 * @return
	 */
	public final static String firstTimestampMonth(Date date) {
		String firstDate = sdf.format(new Date()) + " 00:00:00";
		if (date != null)
			firstDate = sdf.format(date) + " 00:00:00";
		return firstDate;
	}

	/**
	 * 指定日期(默认当天)的最后时刻
	 * @param date
	 * @return
	 */
	public final static String lastTimestampDate(Date date) {
		String lastTime = sdf.format(new Date()) + " 23:59:59";
		if (date != null)
			lastTime = sdf.format(date) + " 23:59:59";
		return lastTime;
	}
	/**
	 * 比较两个字符型日期是否一致 yyyy-MM-dd
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static int compareDate(String d1,String d2){

		if (d1.trim().length()>10){
			d1 = d1.split(" ")[0];
		}
		if (d2.trim().length()>10){
			d2 = d2.split(" ")[0];
		}
		GregorianCalendar date1 = new GregorianCalendar();
		String[] temp1 = d1.split("-");
		date1.set(Calendar.YEAR, Integer.parseInt(temp1[0]));
		date1.set(Calendar.MONTH, Integer.parseInt(temp1[1].substring(0,1).equals("0")?temp1[1].substring(1):temp1[1])-1);
		date1.set(Calendar.DATE, Integer.parseInt(temp1[2].substring(0,1).equals("0")?temp1[2].substring(1):temp1[2]));

		GregorianCalendar date2 = new GregorianCalendar();
		String[] temp2 = d2.split("-");
		date2.set(Calendar.YEAR, Integer.parseInt(temp2[0]));
		date2.set(Calendar.MONTH, Integer.parseInt(temp2[1].substring(0,1).equals("0")?temp2[1].substring(1):temp2[1])-1);
		date2.set(Calendar.DATE, Integer.parseInt(temp2[2].substring(0,1).equals("0")?temp2[2].substring(1):temp2[2]));

		int result = date1.compareTo(date2);
		return result;
	}
    /**
     * 在日期中取出指定部分的字符串值(年、月、日、时、分、秒）
     * @param date 格式yyyy-MM-dd HH:mm:ss
     * @param part 1年 2月 5日 10时 12分 13秒　Calendar.MONTH;
     * @return
     */
    public static String GetDatePart(String date, int part) {
        try {
        	SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date time =df.parse(date);
        	df.format(time);
            if (Calendar.MONTH == part)
                return String.valueOf(df.getCalendar().get(part) + 1);
            else
                return String.valueOf(df.getCalendar().get(part));
        } catch (Exception ex) {
        	ex.printStackTrace();
            return "";
        }
    }
	/**
	 * 判断date1是否在date2之前
	 * @param date1 date2 df格式
	 * @return
	 */
	public static boolean isDateBefore(String date1,String date2){
		try{
			return df.parse(date1).before(df.parse(date2));
		}catch(ParseException e){
			return false;
		}
	}
	/**
	 * 判断当前时间是否在时间date2之前
	 * @param date2 df格式
	 * @return
	 */
	public static boolean isDateBefore(String date2){
		try{
			Date date1 = new Date();
			return date1.before(df.parse(date2));
		}catch(ParseException e){
			return false;
		}
	}
	/**
	* 给出两个日期，计算他们之间相差的年数|月数|天数
	* @param c1 日期1
	* @param c2 日期2
	* @param what 比较模式，如果是Calendar.YEAR则在年份上比较；
	*             如果是Calendar.MONTH则在月数上比较；
	*             如果是Calendar.DATE则在天数上比较（默认情形）
	* @return 相差的年数或月数或天数
	*/
	public static int compare(Calendar c1,Calendar c2,int what) {
		int number=0;
		switch (what) {
		case Calendar.YEAR:
			number=c1.get(Calendar.YEAR)-c2.get(Calendar.YEAR);
			break;
		case Calendar.MONTH:
			int years=compare(c1,c2,Calendar.YEAR);
			number=12*years+c1.get(Calendar.MONTH)-c2.get(Calendar.MONTH);
			break;
		case Calendar.DATE:
			number=(int) ((c1.getTimeInMillis()-c2.getTimeInMillis())/(1000*60*60*24));
			break;
		default:
			number=(int) ((c1.getTimeInMillis()-c2.getTimeInMillis())/(1000*60*60*24));
		break;
		}
		return number;
	}

	/**
	* 判断是否润年
	* @param ddate
	* @return
	*/
	public static boolean isLeapYear(String ddate) {
		/**
		* 详细设计： 1.被400整除是闰年，否则： 2.不能被4整除则不是闰年 3.能被4整除同时不能被100整除则是闰年
		* 3.能被4整除同时能被100整除则不是闰年
		*/
		Date d =strToDate(ddate);
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(d);
		int year = gc.get(Calendar.YEAR);
		if (year % 400 == 0)
			return true;
		else if (year % 4 == 0) {
			if (year % 100 == 0)
				return false;
			else
				return true;
		} else
			return false;
	}

}
/**
 * 新的时间类型格式工具
 * 解决parse方法在并发下的错误
 * @author new
 *
 */
class NewDateFormat{
	private  String format_str=null;
	private  DateFormat format =null; 
	public NewDateFormat(String format_str){
		this.format_str=format_str;
		this.format=new SimpleDateFormat(format_str);
	}
	public String format(Date date){
		return this.format.format(date);
	}
	public Date parse(String date) throws ParseException{
		return new SimpleDateFormat(format_str).parse(date);
	}
	public Date parse(String date,ParsePosition pos){
		return new SimpleDateFormat(format_str).parse(date,pos);
	}
	
}
