package com.zozmom.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;

@SuppressLint("SimpleDateFormat")
public class Util {
	public static boolean isMobile(String mobiles) {
		boolean res = false;
		if (mobiles != null) {
			Pattern p = Pattern.compile("^(1[3-9])\\d{9}$");
			Matcher m = p.matcher(mobiles);
			res = m.matches();
		}
		return res;
	}

	public static boolean StringName(String temp) {
		String regEx = "[/\\:*?<>|\"\n\t]";
		boolean res = false;
		if (temp != null) {
			Pattern p = Pattern.compile(regEx);
			Matcher m = p.matcher(temp);
			res = m.matches();
		}
		return res;
	}

	public static boolean isPwd(String pwd) {
		boolean res = false;
		if (pwd != null) {
			Pattern p = Pattern.compile("^[\\d\\|\\w]{6,17}$");
			Matcher m = p.matcher(pwd);
			res = m.matches();
		}
		return res;
	}

	private static SimpleDateFormat timeFormat = new SimpleDateFormat(
			"yyyyMMddHHmmssSSS", Locale.getDefault());
	private static SimpleDateFormat timeFormat1 = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss", Locale.getDefault());

	public static String getTime() {
		return timeFormat.format(new Date());
	}

	public static String getStandardTime() {
		return timeFormat1.format(new Date());
	}

	public static String getformerTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR, -12);
		Date date = calendar.getTime();
		return timeFormat1.format(date);
	}

	public static String genUUID() {
		return java.util.UUID.randomUUID().toString();
	}

	public static Calendar parseDate(String timestr) {
		Calendar date = new GregorianCalendar();
		if (timestr != null) {
			if (timestr.length() >= 12) {
				int year = Integer.parseInt(timestr.substring(0, 4));
				int month = Integer.parseInt(timestr.substring(4, 6)) - 1;
				int day = Integer.parseInt(timestr.substring(6, 8));
				int hour = Integer.parseInt(timestr.substring(8, 10));
				int min = Integer.parseInt(timestr.substring(10, 12));
				date = new GregorianCalendar(year, month, day, hour, min, 0);
			} else {
				String[] times = timestr.split(" ");
				if (times.length == 2) {
					String[] md = times[0].split("-");
					String[] hm = times[1].split(":");
					int year = date.get(Calendar.YEAR);
					int month = Integer.parseInt(md[0]) - 1;
					int day = Integer.parseInt(md[1]);
					int hour = Integer.parseInt(hm[0]);
					int min = Integer.parseInt(hm[1]);
					date = new GregorianCalendar(year, month, day, hour, min, 0);
				}
			}
		}
		return date;
	}

	public static int getRandomNumber(int min, int max) {
		Random random = new Random();
		int res = random.nextInt(max) % (max - min + 1) + min;
		return res;
	}

	public static boolean isSameDay(Date day1, Date day2) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String ds1 = sdf.format(day1);
		String ds2 = sdf.format(day2);
		if (ds1.equals(ds2)) {
			return true;
		} else {
			return false;
		}
	}

	public static String GetWeekDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 EEEE");
		return sdf.format(new Date());
	}

	public static boolean isEmail(String email) {
		String ste = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		Pattern p = Pattern.compile(ste);
		Matcher m = p.matcher(email);
		return m.matches();
	}

	public static long GetMillionSeconds() {
		String time = Util.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		long millionSeconds = 0;
		try {
			millionSeconds = sdf.parse(time).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return millionSeconds;
	}

	@SuppressLint("SimpleDateFormat")
	public static long GetMillionSeconds(String time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		long millionSeconds = 0;
		try {
			millionSeconds = sdf.parse(time).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return millionSeconds;
	}

	/**
	 * 把毫秒转换成日期
	 * 
	 * @param time
	 * @return
	 */
	public static String GetDataTime(long time) {
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		return formatter.format(calendar.getTime());
	}
	
	/**
	 * 把毫秒转换成日期
	 * 
	 * @param time
	 * @return
	 */
	public static String GetDataTime3(long time) {
		DateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		return formatter.format(calendar.getTime());
	}
	
	/**
	 * 把毫秒转换成日期
	 * 
	 * @param time
	 * @return
	 */
	public static String GetDataTime(String time) {
		long t=Long.parseLong(time);
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(t);
		return formatter.format(calendar.getTime());
	}
	
	
	
	/**
	 * 把毫秒转换成日期
	 * 
	 * @param time
	 * @return
	 */
	public static String GetDataTime2(long time) {
		DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss SSS");
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		return formatter.format(calendar.getTime());
	}
	/**
	 * 把毫秒转换成日期
	 * 
	 * @param time
	 * @return
	 */
	public static String GetDataTime4(long time) {
		DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		return formatter.format(calendar.getTime());
	}
	
	/**
	 * 把毫秒转换成日期
	 * 
	 * @param time
	 * @return
	 */
	public static String GetDataTime1(long time) {
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		return formatter.format(calendar.getTime());
	}

	/**
	 * xml转义
	 * 
	 */
	public static String Escape(String text) {
		if (text.contains("&")) {
			text = text.replace("&", "&amp;");
		}
		if (text.contains("<")) {
			text = text.replace("<", "&lt;");
		}
		if (text.contains(">")) {
			text = text.replace(">", "&gt;");
		}

		return text;
	}

	/**
	 * 处理字符
	 * 
	 * @param filed
	 * @param character
	 * @return
	 */
	public static List<String> split(String filed, String character) {
		List<String> list = new ArrayList<String>();
		if (filed == null || filed.length() == 0) {
			return null;
		} else if (!filed.contains(";")) {
			list.add(filed);
		} else {
			String[] a = filed.split(character);
			for (int i = 0; i < a.length; i++) {
				list.add(a[i]);
			}
		}
		return list;
	}

	/**
	 * 处理字符（）
	 * 
	 * @param filed
	 * @param character
	 * @return int
	 */
	public static int splitNum(String filed, String character) {
		List<String> list = new ArrayList<String>();
		if (filed == null || filed.length() == 0) {
			return 0;
		} else if (!filed.contains(";")) {
			list.add(filed);
		} else {
			String[] a = filed.split(character);
			for (int i = 0; i < a.length; i++) {
				list.add(a[i]);
			}
		}
		return list.size();
	}

	/**
	 * 处理List<String> 用特殊符号character分隔
	 * 
	 * @param uploadedPics
	 * @param character
	 * @return path
	 */

	public static String GetPath(List<String> uploadedPics, String character) {
		String path = "";
		if (uploadedPics == null) {
			return null;
		}
		for (int i = 0; i < uploadedPics.size(); i++) {
			if (i == uploadedPics.size() - 1) {
				path += uploadedPics.get(i);
			} else {
				path += uploadedPics.get(i) + character;
			}
		}
		return path;
	}

	/**
	 * 计算出指定时间与当前时间的时间差
	 * 
	 * 1000 1秒
	 * 
	 * 1000*60 1分钟
	 * 
	 * 1000*60*60 1小时
	 * 
	 * 1000*60*60*24 1天
	 * 
	 * @param date
	 * @return
	 */
	public static String timeDifference(long date) {
		long currentTime = System.currentTimeMillis();
		long timrDiff = currentTime - date;
		if (timrDiff >= 0) {
			if (timrDiff < 1000 * 60 * 2L) {
				return "1分钟前";
			} else if (timrDiff < 1000 * 60 * 60L) {
				return timrDiff / (1000 * 60L) + "分钟前";
			} else if (timrDiff < 1000 * 60 * 60 * 24L) {
				return timrDiff / (1000 * 60 * 60L) + "小时前";
			} else if (timrDiff < 1000 * 60 * 60 * 24 * 365L) {
				return timrDiff / (1000 * 60 * 60 * 24L) + "天前";
			} else {
				return timrDiff / (1000 * 60 * 60 * 24 * 365L) + "年前";
			}
		} else {
			return "指定时间不能大于当前时间！";
		}
	}

	/**
	 * 获取到当天的00:00:00的毫秒表示
	 * 
	 * @return
	 */
	public static long getDayTime() {
		long cTime = System.currentTimeMillis();
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
			Date date = new Date(cTime);
			String sTime = simpleDateFormat.format(date);
			cTime = simpleDateFormat.parse(sTime).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return cTime;
	}

	public static boolean isNumeric(String str) {
		if (str == null || str.trim().length() == 0) {
			return false;
		}
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	public static String parse(String data) {
		if (data == null) {
			return null;
		}
//		if (!data.contains("USR.") && !data.contains("PRD.")) {
//			return null;
//		}
		data = data.replace("{", "").replace("}", "");
		String[] str = data.split(",");
		String keystring = null;
		if (str.length != 2) {
			return keystring;
		} else {
			keystring = str[0];
			String[] key = keystring.split(":");
			if (key.length != 2) {
				return null;
			}
			keystring = key[1];
			keystring = keystring.replace("\"", "");
		}
		return keystring;
	}

}
