package com.ycsoft.commons.helper;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 包含一系列对日期处理的函数
 *
 * @author hh
 * @date Dec 3, 2009 1:16:21 PM
 */
public class DateHelper {
	
	public final static String FORMAT_YM="yyyyMM";
	public final static String FORMAT_YMD_STR="yyyyMMdd";
	public final static String FORMAT_YMD="yyyy-MM-dd";
	public final static String FORMAT_TIME="yyyy-MM-dd HH:mm:ss";
	public final static String FORMAT_TIME_START="yyyy-MM-dd 00:00:00";
	public final static String FORMAT_TIME_END="yyyy-MM-dd 23:59:59";
	public final static String FORMAT_TIME_VOD="yyyyMMdd000000";
	
	public final static String FORMAT_TIME_VOD_END="yyyyMMdd235959";
	
	
	private final static NewDateFormat df = new NewDateFormat(FORMAT_TIME);
	
	private final static NewDateFormat sdf = new NewDateFormat(FORMAT_YMD);
	private final static NewDateFormat ym = new NewDateFormat(FORMAT_YM);	
	private final static NewDateFormat ymvod = new NewDateFormat(FORMAT_TIME_VOD);
	private final static NewDateFormat ymd = new NewDateFormat(FORMAT_YMD_STR);

	public static final String SECOND = "second";
	public static final String MINUTE = "minute";
	public static final String HOUR = "hour";
	public static final String DAY = "day";	
	/**
	* 将指定日期按指定的format格式化。
	* @return
	*/
	public static String format(Date date,String format) {
		if(null == date) {
			return "";
		}else{
			return new SimpleDateFormat( format ).format(date);
		}
	}


	/**
	* 获取指定日期,输出格式化为yyyy-MM-dd HH:mm:ss
	* @return
	*/
	public static String format(Date date) {
		if(date == null)
			return "";
		return df.format(date);
	}
	/**
	* 获取当前时刻日期，格式为yyyy-MM-dd HH:mm:ss
	* @return
	*/
	public static String formatNowTime() {
		Date date = new Date();
		return df.format(date);
	}

	/**
	* 获取当前时刻日期，格式为yyyy-MM-dd
	* @return
	*/
	public static String formatNow() {
		Date date = new Date();
		return sdf.format(date);
	}
	
	public static String nowYearMonth() {
		Date date = new Date();
		return ym.format(date);
	}

	/**
	* 获取当前时刻日期
	* @return
	*/
	public static Date now() {
		return new Date();
	}

	/**
	* 获取当前日期0点的日期时间
	* @return
	*/
	public static Date today() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	/**
	 * 日期去时分秒
	 * @param date
	 * @return
	 */
	public static Date getTruncDate(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	public static int getCurrYear(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.YEAR);
	}
	
	
	/**
	 * 获取当前日期年份
	 *
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
	 * 获取当前小时
	 * @return
	 */
	public static int getCurrHour() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		return c.get(Calendar.HOUR_OF_DAY);
	}
	
	public static int getCurrMinute() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		return c.get(Calendar.MINUTE);
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
		if (dateDate == null)
			return "";
			return new SimpleDateFormat(FORMAT_YMD).format(dateDate);
	}
	
	public static String dateToStrYmvod(java.util.Date dateDate) {
		if (dateDate == null)
			return "";
		return ymvod.format(dateDate);
	}
	
	public static String dateToStrYMD(java.util.Date dateDate) {
		if (dateDate == null)
			return "";
		String dateString = ymd.format(dateDate);
		return dateString;
	}
	
	/**
	 * 将短时间格式时间转换为字符串 yyyyMM
	 * @param dateDate
	 * @return
	 */
	public static String formatYM(Date dateDate){
		if (dateDate == null)
			return "";
		return ym.format(dateDate);
	}
	
	public static Date strToDateYM(String strDate) {
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = ym.parse(strDate, pos);
		return strtodate;
	}
	
	public static String formatAddMonthYM(Date dateDate){
		if (dateDate == null)
			return "";
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateDate);
		cal.add(Calendar.MONTH, 1);
		return ym.format(cal.getTime());
	}
	
	/**
	 * 将短时间格式字符串转换为时间 yyyy-MM-dd
	 * @param strDate
	 * @return
	 */
	public static Date strToDate(String strDate) {
		ParsePosition pos = new ParsePosition(0);
			return new SimpleDateFormat(FORMAT_YMD).parse(strDate, pos);
	}
	
	public static Date strYMDToDate(String strDate){
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = ymd.parse(strDate, pos);
		return strtodate;
	}
	
	/**
	 * 将短时间格式字符串转换为时间 yyyyMMddHHmmss
	 * @param strDate
	 * @return
	 */
	public static Date vodStrToDate(String strDate){
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = ymvod.parse(strDate, pos);
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
		c.add(Calendar.DATE, day);
		return c.getTime();
	}
	
	public static java.util.Date addTypeDate(java.util.Date date, String type, int num){
		java.util.Calendar c = java.util.Calendar.getInstance();
		c.setTime(date);
		if(type.equals("DATE")){
			c.add(Calendar.DATE, num);
		}else if(type.equals("MONTH")){
			c.add(Calendar.MONTH, num);
		}else if(type.equals("DAY")){
			c.add(Calendar.DAY_OF_MONTH, num);
		}
		return c.getTime();
	}
	/**
	* 获取两个日期之间的间隔天数(1)
	* @return
	*/
	public static int getDiffDays(Date beginDate, Date endDate){
		//舍去时分秒
		try {
			beginDate = DateHelper.parseDate(DateHelper.dateToStr(beginDate), DateHelper.FORMAT_YMD);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	
	public static int getDiffMonth(Date begin, Date end){
		Calendar bc = Calendar.getInstance();
		bc.setTimeInMillis(begin.getTime());
		//bc.setTime(begin.getTime());
		Calendar be = Calendar.getInstance();
		be.setTime(end);
		int beginYear = bc.get(Calendar.YEAR);
		int beginMonth = bc.get(Calendar.MONTH);
	
		int endYear = be.get(Calendar.YEAR);
		int endMonth = be.get(Calendar.MONTH);
	
		int difMonth = (endYear-beginYear)*12+(endMonth-beginMonth);
	
		return difMonth; 
	}
	/**
	 * 获取本月第一天（根据当前时间）
	 * @return
	 */
	public static String getFirstDateInCurrentMonth() {
	    return getFirstDateInCurrentMonth(new Date());
	  }
	
	public static String getFirstDateInCurrentMonth(Date date) {
	    Calendar calendar =  Calendar.getInstance();
	    calendar.setTime(date);
	    String year = String.valueOf(calendar.get(Calendar.YEAR));
	    String month = String.valueOf(calendar.get(Calendar.MONTH)+1);
	    String day = "01";
	    return year +"-"+ (month.length() == 1 ? "0" + month : month)+"-" + day;
	  }
	/**
	 * 获取本月最后一天（根据当前时间）
	 */
	public static String getLastDateInCurrentMonth(Date date){
	   String str = "";
	   Calendar lastDate = Calendar.getInstance();
	   lastDate.setTime(date);
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
	
	public static Date getNextMonthByNum(Date date,int num){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH,num); //set next month
		return c.getTime();
	}
	/**
	 * 指定日期增加指定月数并减一天
	 * @param date
	 * @param month_num
	 * @return
	 */
	public static Date getNextMonthPreviousDay(Date date,int month_num){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH,month_num); //set next month
		c.add(Calendar.DAY_OF_MONTH, -1);
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
	 * 日期加上num秒 如DateHelper.addNumDate(date, 100, "second");
	 * @param date
	 * @param num
	 * @param type
	 * @return
	 */
	public static Date addNumDate(Date date, int num, String type){
		  long result = 0;
		  if(type == null || type.equals(SECOND)) {
		   result = date.getTime() + num*1000;
		  } else if(type.equals(MINUTE)) {
		   result = date.getTime() + num*60*1000;
		  } else if(type.equals(HOUR)) {
		   result = date.getTime() + num*60*60*1000;
		  } else if(type.equals(DAY)) {
		   result = date.getTime() + num*24*60*60*1000;
		  }
		  ParsePosition pos = new ParsePosition(0);
		  Date strtodate = df.parse(df.format(new Date(result)), pos);
		  return strtodate;
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
     *  date 格式yyyy-MM-dd HH:mm:ss
     * @param date
     * @param part 1年 2月 5日 10时 12分 13秒　Calendar.MONTH
     * @return
     */
    public static String GetDatePart(String date, int part) {
        try {
        	SimpleDateFormat df=new SimpleDateFormat(FORMAT_TIME);
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
	 * @param date1  df格式
	 * @param date2
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

	public static boolean isDateBefore(Date date){
		Date date1 = new Date();
		return date1.before(date);
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
	 * 2个日期相差多少月
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int compareToMonthByDate(Date startDate,Date endDate) {
		Calendar startCal = new GregorianCalendar();
		Calendar endCal = new GregorianCalendar();
		startCal.setTime(startDate);
		endCal.setTime(endDate);
		int a = compare(endCal,startCal,2);
		return a;
	}
	
	/**
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int compareToMonthByString(String startDate,String endDate) {
			int number=0;
			try {
				Date d1 = sdf.parse(startDate);
				Date d2=sdf.parse(endDate);
				Calendar startCal = new GregorianCalendar();
				Calendar endCal = new GregorianCalendar();
				startCal.setTime(d1);
				endCal.setTime(d2);
				number = compare(endCal,startCal,2);
			} catch (ParseException e) {
				e.printStackTrace();
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

	/**
	 * 判断日期是否为当天
	 * @param date
	 * @return
	 */
	public static boolean isToday(Date date){

		return dateToStr(date).equals(dateToStr(now()));
	}


	/**
	 * 按照指定格式将给出的字符串转化成Date对象
	 * @param strDate
	 * @param format
	 * @return
	 */
	public static Date parseDate(String strDate,String format) throws Exception {
		ParsePosition pos = new ParsePosition(0);
		DateFormat df = new SimpleDateFormat(format);
		Date strtodate = df.parse(strDate, pos);
		return strtodate;
	}
	
	public static Date getNexYearByNum(Date date,int num){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.YEAR,num); //set next month
		return c.getTime();
	}
	
	public static void main(String[] args){
		
		System.out.println(DateHelper.FORMAT_TIME+DateHelper.format(new Date(), DateHelper.FORMAT_TIME));
		System.out.println(DateHelper.FORMAT_TIME_START+DateHelper.format(new Date(), DateHelper.FORMAT_TIME_START));
		System.out.println(DateHelper.FORMAT_TIME_END+DateHelper.format(new Date(), DateHelper.FORMAT_TIME_END));
		System.out.println(DateHelper.FORMAT_TIME_VOD+DateHelper.format(new Date(), DateHelper.FORMAT_TIME_VOD));
		System.out.println(DateHelper.FORMAT_TIME_VOD_END+DateHelper.format(new Date(), DateHelper.FORMAT_TIME_VOD_END));
//		System.out.println(DateHelper.dateToStr(DateHelper.addDate(new Date(), 2)));
//		
//		System.out.println(DateHelper.getDiffDays(new Date(),DateHelper.addDate(new Date(), 3)));
//		
//		
//		System.out.println(DateHelper.format(new Date(), DateHelper.FORMAT_TIME_VOD));
		
		List<Map.Entry<String,String>> mappingList = null; 
		  Map<String,String> map = new HashMap<String,String>(); 
		  map.put("aaaa", "month"); 
		  map.put("bbbb", "bread"); 
		  map.put("ccccc", "attack"); 
		  
		  //通过ArrayList构造函数把map.entrySet()转换成list 
		  mappingList = new ArrayList<Map.Entry<String,String>>(map.entrySet()); 
		  //通过比较器实现比较排序 
		  Collections.sort(mappingList, new Comparator<Map.Entry<String,String>>(){ 
		   public int compare(Map.Entry<String,String> mapping1,Map.Entry<String,String> mapping2){ 
		    return mapping1.getValue().compareTo(mapping2.getValue()); 
		   } 
		  }); 
		  
		  for(Map.Entry<String,String> mapping:mappingList){ 
		   System.out.println(mapping.getKey()+":"+mapping.getValue()); 
		  } 
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
