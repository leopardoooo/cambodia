/**
 *
 */
package com.ycsoft.business.dto.core.print;

import java.util.Calendar;

import com.ycsoft.commons.helper.DateHelper;

/**
 * 封装打印需要的时间类
 */
public class PrintDate{

	private int year;
	private int month;
	private int day;
	private int hour ;
	private int minute ;
	private int second ;

	// HH:mm:ss
	private String timeStr;
	// yyyy-MM-dd
	private String dateStr;
	// yyyy-MM-dd HH:mm:ss
	private String datetimeStr;

	/**
	 * 默认为当前的系统时间
	 */
	public PrintDate(){
		this(Calendar.getInstance());
	}

	/**
	 * 指定需要设置的日期
	 * @param c 日期类型
	 */
	public PrintDate(Calendar c){
		year = c.get(Calendar.YEAR );
		month = c.get(Calendar.MONTH)+1;
		day = c.get(Calendar.DAY_OF_MONTH );
		hour = c.get(Calendar.HOUR );
		minute = c.get(Calendar.MINUTE );
		second = c.get(Calendar.SECOND );

		datetimeStr = DateHelper.format(c.getTime());
		dateStr = DateHelper.format(c.getTime(), "yyyy-MM-dd");
		timeStr = DateHelper.format(c.getTime(), "HH:mm:ss");
	}

	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public int getHour() {
		return hour;
	}
	public void setHour(int hour) {
		this.hour = hour;
	}
	public int getMinute() {
		return minute;
	}
	public void setMinute(int minute) {
		this.minute = minute;
	}
	public int getSecond() {
		return second;
	}
	public void setSecond(int second) {
		this.second = second;
	}

	public String getTimeStr() {
		return timeStr;
	}

	public void setTimeStr(String timeStr) {
		this.timeStr = timeStr;
	}

	public String getDateStr() {
		return dateStr;
	}

	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}

	public String getDatetimeStr() {
		return datetimeStr;
	}

	public void setDatetimeStr(String datetimeStr) {
		this.datetimeStr = datetimeStr;
	}
}
