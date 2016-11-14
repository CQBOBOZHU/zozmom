package com.zozmom.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import com.zozmom.ZozmomContext;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

public final class DeviceInfo {
	private static Context m_context;

	/**
	 * 检测sd卡是否可用
	 * 
	 * @return
	 */
	public static boolean isSDCardExisted() {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public static String getSerial() {
		String res = null;
		try {
			res = Build.SERIAL;
		} catch (Throwable ex) {
			try {
				// 2.2
				Class<?> clazz = Class.forName("android.os.Build");
				Class<?> paraTypes = Class.forName("java.lang.String");
				Method method = clazz.getDeclaredMethod("getString", paraTypes);
				if (!method.isAccessible()) {
					method.setAccessible(true);
				}
				res = (String) method.invoke(new Build(), "ro.serialno");
			} catch (Exception e) {
			}
		}
		return res;
	}

	public static String getROM() {
		String rom = null;
		String str1 = "/proc/version";
		String str2;
		String[] arrayOfString;
		try {
			FileReader localFileReader = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(
					localFileReader, 8192);
			str2 = localBufferedReader.readLine();
			arrayOfString = str2.split("\\s+");
			rom = arrayOfString[2];// KernelVersion
			localBufferedReader.close();
		} catch (IOException e) {
			// Log.e(TAG, "get rom error: " + Log.getStackTraceString(e));
		}

		return rom;
	}

	public static String getModel() {
		return Build.MODEL;
	}

	public static String getNetwork() {
		return DeviceInfo.getMobileNetwork(DeviceInfo
				.getMobileNetwork(getContext()))
				+ ","
				// + DeviceInfo.getMobileNetworkType(DeviceInfo
				// .getCurrentNetworkType(getContext()))
				+ ","
				+ DeviceInfo.getMobileNetworkApn(getContext())
				+ ","
				+ DeviceInfo.getMobileNetworkRoaming(DeviceInfo
						.getMobileNetworkRoaming(getContext()));
	}

	public static String getNetworkOrigin() {
		return DeviceInfo.getMobileNetwork(getContext()) + ","
				+ DeviceInfo.getMobileNetworkType(getContext()) + ","
				+ DeviceInfo.getMobileNetworkApn(getContext()) + ","
				+ DeviceInfo.getMobileNetworkRoaming(getContext());
	}

	public static String getCurrentNetworkType() {
		return DeviceInfo.getCurrentNetworkType(DeviceInfo
				.getCurrentNetworkType(getContext()));
	}

	public static String getCPU() {
		String cpu = "";
		try {
			String[] cpuInfo = DeviceInfo.getCpuInfo();
			cpu = cpuInfo[0] + " "
					+ DeviceInfo.formatCpuSize(DeviceInfo.getMaxCpuFreq());
		} catch (Exception e) {
			// Log.e(TAG, "get cpu error: " + Log.getStackTraceString(e));
		}

		return cpu;
	}

	public static String getMem() {
		String mem = "";
		try {
			mem = DeviceInfo.formatSize(DeviceInfo.getTotalMemory());
		} catch (Exception e) {
			// Log.e(TAG, "get mem error: " + Log.getStackTraceString(e));
		}
		return mem;
	}

	public static String getSystem() {
		return Build.VERSION.RELEASE + "," + Build.DISPLAY;// firmware/os 版本号
															// 显示屏参数
	}

	public static String getProduct() {
		return Build.PRODUCT;// 手机制作商
	}

	public static String getManufacturer() {
		return Build.MANUFACTURER;// 硬件制造商
	}

	public static Context getContext() {
		if (m_context == null) {
			m_context = ZozmomContext.appContext;
		}
		return m_context;
	}

	public static String getPlatform() {
		StringBuffer sb = new StringBuffer();
		sb.append(getDeviceName());
		sb.append(";Android ");
		sb.append(getVersion());
		sb.append(",level ");
		sb.append(getApiLevel());
		return sb.toString();
	}

	public static String getDeviceName() {
		try {
			return Build.MODEL;// 手机版本
		} catch (Throwable thr) {
			thr.printStackTrace();
		}
		return "error";
	}

	public static String getVersion() {
		try {
			return Build.VERSION.RELEASE;
		} catch (Throwable thr) {
			thr.printStackTrace();
		}
		return "error";
	}

	@SuppressWarnings("deprecation")
	public static String getApiLevel() {
		try {
			return Build.VERSION.SDK;
		} catch (Throwable thr) {
			thr.printStackTrace();
		}

		return null;
	}

	public static int getApiLevelInt() {
		try {
			return Integer.parseInt(getApiLevel());
		} catch (Throwable thr) {
			thr.printStackTrace();
		}
		return 5;

	}

	public static String getImei() {
		String imei = "0";
		try {
			TelephonyManager manager = (TelephonyManager) getContext()
					.getSystemService(Context.TELEPHONY_SERVICE);
			imei = manager.getDeviceId();
			if (imei == null) {
				imei = "macAdress:" + getMacAddress();
			}
		} catch (Throwable thr) {
			thr.printStackTrace();
		}
		return imei;
	}

	/**
	 * 获取mac地址
	 * 
	 * @return
	 */
	public static String getMacAddress() {
		try {
			WifiManager wifi = (WifiManager) getContext().getSystemService(
					Context.WIFI_SERVICE);
			WifiInfo info = wifi.getConnectionInfo();
			return "" + info.getMacAddress();
		} catch (Throwable thr) {
			thr.printStackTrace();
		}
		return "error";

	}

	public static boolean checkIsHaveCard() {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			return true;
		}

		return false;
	}

	public static String getScreenInfo() {
		DisplayMetrics dm = getDisplayMetrics();
		return dm.widthPixels + "*" + dm.heightPixels;
	}

	public static DisplayMetrics getDisplayMetrics() {
		try {
			DisplayMetrics metric = new DisplayMetrics();
			WindowManager manager = (WindowManager) getContext()
					.getSystemService(Context.WINDOW_SERVICE);
			manager.getDefaultDisplay().getMetrics(metric);
			return metric;
		} catch (Throwable thr) {
			thr.printStackTrace();
		}
		return null;

	}

	/** ��ȡcpu abi��Ϣ */
	public static String getCPUABI() {
		try {
			return Build.CPU_ABI;// cpu指令集
		} catch (Throwable thr) {
			thr.printStackTrace();
		}
		return null;
	}

	public static byte[] getCPUInfo() {
		FileInputStream in = null;
		try {
			File file = new File("/proc/cpuinfo");
			if (file != null && file.exists() && file.canRead()) {
				in = new FileInputStream(file);
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				byte[] buffer = new byte[1000];
				int len = 0;
				while ((len = in.read(buffer)) > 0) {
					os.write(buffer, 0, len);
					os.flush();
				}
				return os.toByteArray();
			}
		} catch (Throwable thr) {
			thr.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 * 返回当前设备型号
	 * 
	 * 例如： Galaxy Nexus, SGH-I897
	 * 
	 * @return
	 */
	public static String getDeviceModel() {
		return android.os.Build.MODEL;
	}

	/**
	 * 返回当前设备SDK的API版本
	 * 
	 * 例如：如2.3.6是10， 4.0.3是15
	 * 
	 * @return
	 */
	public static int getSdkAPI() {
		return android.os.Build.VERSION.SDK_INT;
	}

	/**
	 * 返回当前设备的SDK的版本号
	 * 
	 * 例如：2.1，2.3.3
	 * 
	 * @return
	 */
	public static String getSdkVersion() {
		return android.os.Build.VERSION.RELEASE;
	}

	// 获取cpu信息
	public static String[] getCpuInfo() {
		String str1 = "/proc/cpuinfo";
		String str2 = "";
		String[] cpuInfo = { "", "" };
		String[] arrayOfString;
		try {
			FileReader fr = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
			str2 = localBufferedReader.readLine();
			arrayOfString = str2.split("\\s+");
			for (int i = 2; i < arrayOfString.length; i++) {
				cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + " ";
			}
			str2 = localBufferedReader.readLine();
			arrayOfString = str2.split("\\s+");
			cpuInfo[1] += arrayOfString[2];
			localBufferedReader.close();
		} catch (IOException e) {
		}
		return cpuInfo;
	}

	public static long getMaxCpuFreq() {
		String result = "";
		ProcessBuilder cmd;
		try {
			String[] args = { "/system/bin/cat",
					"/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq" };
			cmd = new ProcessBuilder(args);
			Process process = cmd.start();
			InputStream in = process.getInputStream();
			byte[] re = new byte[24];
			while (in.read(re) != -1) {
				result = result + new String(re);
			}
			in.close();
		} catch (IOException ex) {
			ex.printStackTrace();
			result = "0";
		}
		return Long.valueOf(result.trim());
	}

	public static long getTotalMemory() {
		String str1 = "/proc/meminfo";
		String str2 = "";
		long totalMen = 0L;
		try {
			FileReader fr = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
			while ((str2 = localBufferedReader.readLine()) != null) {
				if (str2 != null && str2.contains("MemTotal")) {
					String regEx = "[^0-9]";
					Pattern p = Pattern.compile(regEx);
					Matcher m = p.matcher(str2);
					totalMen = Long.parseLong(m.replaceAll("").trim()) * 1024;
					break;
				}
			}
			localBufferedReader.close();
		} catch (IOException e) {
			totalMen = 0L;
		}
		return totalMen;
	}

	// 获取屏幕分辨率
	public static String getDisplayMetrics(Activity activity) {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay()
				.getMetrics(displayMetrics);
		return displayMetrics.widthPixels + "x" + displayMetrics.heightPixels;
	}

	/**
	 * 
	 46000,46002:中国移动 46001:中国联通 46003:中国电信
	 * 
	 * */
	public static String getMobileNetwork(Context context) {
		String net = "0";
		try {
			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			net = tm.getSimOperator();
		} catch (Throwable e) {
			// Log.e(TAG, "getMobileNetwork: " + Log.getStackTraceString(e));
		}
		return net;
	}

	public static String getMobileNetwork(String network) {
		if (network.equals("46000") || network.equals("46002")) {
			return "中国移动";
		} else if (network.equals("46001")) {
			return "中国联通";
		} else if (network.equals("46003")) {
			return "中国电信";
		}
		return "运营商代码:" + network;
	}

	/**
	 * 
	 public static final int NETWORK_TYPE_UNKNOWN = 0; public static final int
	 * NETWORK_TYPE_GPRS = 1; public static final int NETWORK_TYPE_EDGE = 2;
	 * public static final int NETWORK_TYPE_UMTS = 3; public static final int
	 * NETWORK_TYPE_CDMA = 4; public static final int NETWORK_TYPE_EVDO_0 = 5;
	 * public static final int NETWORK_TYPE_EVDO_A = 6; public static final int
	 * NETWORK_TYPE_1xRTT = 7; public static final int NETWORK_TYPE_HSDPA = 8;
	 * public static final int NETWORK_TYPE_HSUPA = 9; public static final int
	 * NETWORK_TYPE_HSPA = 10; public static final int NETWORK_TYPE_IDEN = 11;
	 * public static final int NETWORK_TYPE_EVDO_B = 12; public static final int
	 * NETWORK_TYPE_LTE = 13; public static final int NETWORK_TYPE_EHRPD = 14;
	 * public static final int NETWORK_TYPE_HSPAP = 15;
	 * 
	 * */
	public static int getMobileNetworkType(Context context) {
		int type = 0;
		try {
			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			type = tm.getNetworkType();
		} catch (Throwable e) {
			// Log.e(TAG, "getMobileNetworkType: " +
			// Log.getStackTraceString(e));
		}

		return type;
	}

	public static String getMobileNetworkType(int type) {
		switch (type) {
		case 0:
			return "UNKNOWN";
		case 1:
			return "GPRS";
		case 2:
			return "EDGE";
		case 3:
			return "UMTS";
		case 4:
			return "CDMA";
		case 5:
			return "EVDO_0";
		case 6:
			return "EVDO_A";
		case 7:
			return "1xRTT";
		case 8:
			return "HSDPA";
		case 9:
			return "HSUPA";
		case 10:
			return "HSPA";
		case 11:
			return "IDEN";
		case 12:
			return "EVDO_B";
		case 13:
			return "LTE";
		case 14:
			return "EHRPD";
		case 15:
			return "HSPAP";
		}
		return "无连接";
	}

	/**
	 * 
	 中国移动：cmwap,cmnet 中国联通：3gnet,uninet,3gwap,uniwap 中国电信：#777,ctnet,ctwap
	 * 
	 * */
	@SuppressLint("DefaultLocale")
	public static String getMobileNetworkApn(Context context) {
		String apn = "unknown";
		Uri PREFERRED_APN_URI = Uri
				.parse("content://telephony/carriers/preferapn");
		try {
			Cursor cursor = context.getContentResolver().query(
					PREFERRED_APN_URI, new String[] { "_id", "apn", "type" },
					null, null, null);
			cursor.moveToFirst();
			int counts = cursor.getCount();
			if (counts != 0) {
				if (!cursor.isAfterLast()) {
					apn = cursor.getString(1);
				}
			} else {
				// 适配中国电信定制机
				Cursor c = context.getContentResolver().query(
						PREFERRED_APN_URI, null, null, null, null);
				c.moveToFirst();
				apn = c.getString(c.getColumnIndex("user"));

				c.close();
			}
			cursor.close();
		} catch (Exception e) {
			// Log.e(TAG, "getMobileNetworkApn:" + Log.getStackTraceString(e));
		}
		return apn.toUpperCase();
	}

	public static boolean getMobileNetworkRoaming(Context context) {
		boolean roaming = false;
		try {
			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			roaming = tm.isNetworkRoaming();
		} catch (Throwable e) {
			// Log.e(TAG, "getMobileNetworkRoaming: " +
			// Log.getStackTraceString(e));
		}

		return roaming;
	}

	public static String getMobileNetworkRoaming(boolean roaming) {
		if (roaming) {
			return "漫游中";
		}
		return "无漫游";
	}

	/**
	 * 
	 public static final int TYPE_MOBILE = 0; public static final int
	 * TYPE_WIFI = 1; public static final int TYPE_MOBILE_MMS = 2; public static
	 * final int TYPE_MOBILE_SUPL = 3; public static final int TYPE_MOBILE_DUN =
	 * 4; public static final int TYPE_MOBILE_HIPRI = 5; public static final int
	 * TYPE_WIMAX = 6; public static final int TYPE_BLUETOOTH = 7; public static
	 * final int TYPE_DUMMY = 8; public static final int TYPE_ETHERNET = 9;
	 * 
	 * */
	public static int getCurrentNetworkType(Context context) {
		try {
			ConnectivityManager conMan = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);

			return conMan.getActiveNetworkInfo().getType();
		} catch (Exception e) {
			// Log.e(TAG, "getCurrentNetworkType:" +
			// Log.getStackTraceString(e));
		}
		return -1;
	}

	public static String getCurrentNetworkType(int type) {
		switch (type) {
		case 0:
			return "MOBILE";
		case 1:
			return "WIFI";
		case 2:
			return "MMS";
		case 3:
			return "SUPL";
		case 4:
			return "DUN";
		case 5:
			return "HIPRI";
		case 6:
			return "WIMAX";
		case 7:
			return "BLUETOOTH";
		case 8:
			return "DUMMY";
		case 9:
			return "ETHERNET";
		}
		return "无连接";
	}

	// 格式化大小
	public static String formatSize(long size) {
		String suffix = null;
		float fSize = 0;

		if (size >= 1000) {
			suffix = "KB";
			fSize = size / 1000;
			if (fSize >= 1000) {
				suffix = "MB";
				fSize /= 1000;
			}
			if (fSize >= 1000) {
				suffix = "GB";
				fSize /= 1000;
			}
		} else {
			fSize = size;
		}
		java.text.DecimalFormat df = new java.text.DecimalFormat("#0.0");
		StringBuilder resultBuffer = new StringBuilder(df.format(fSize));
		if (suffix != null)
			resultBuffer.append(suffix);
		return resultBuffer.toString();
	}

	// 格式化大小
	public static String formatCpuSize(long size) {
		String suffix = "KHz";
		float fSize = 0;

		if (size >= 1000) {
			suffix = "MHz";
			fSize = size / 1000;
			if (fSize >= 1000) {
				suffix = "GHz";
				fSize /= 1000;
			}
			if (fSize >= 1000) {
				suffix = "THz";
				fSize /= 1000;
			}
		} else {
			fSize = size;
		}
		java.text.DecimalFormat df = new java.text.DecimalFormat("#0.0");
		StringBuilder resultBuffer = new StringBuilder(df.format(fSize));
		if (suffix != null)
			resultBuffer.append(suffix);
		return resultBuffer.toString();
	}

	public static List<String> getInstalledApks() {
		List<String> res = null;
		if (getContext() != null) {
			res = new ArrayList<String>();
			PackageManager pm = getContext().getPackageManager();
			List<PackageInfo> pkginfolist = pm
					.getInstalledPackages(PackageManager.GET_ACTIVITIES);
			List<ApplicationInfo> appinfoList = pm.getInstalledApplications(0);
			for (int x = 0; x < pkginfolist.size(); x++) {
				res.add(appinfoList.get(x).publicSourceDir);
			}
		}
		return res;
	}

	/**
	 * 获取IP地址
	 * 
	 * @return
	 */
	public static String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()
							&& !inetAddress.isLinkLocalAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			Log.e("WifiPreference IpAddress", ex.toString());
		}
		return null;
	}

	/**
	 * 获取外网IP
	 * 
	 * @return
	 */
	public static String GetNetIp() {
		URL infoUrl = null;
		InputStream inStream = null;
		String ipLine = "";
		HttpURLConnection httpConnection = null;
		try {
			infoUrl = new URL("http://ip168.com/");
			URLConnection connection = infoUrl.openConnection();
			httpConnection = (HttpURLConnection) connection;
			int responseCode = httpConnection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				inStream = httpConnection.getInputStream();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(inStream, "utf-8"));
				StringBuilder strber = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null)
					strber.append(line + "\n");

				Pattern pattern = Pattern
						.compile("((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))");
				Matcher matcher = pattern.matcher(strber.toString());
				if (matcher.find()) {
					ipLine = matcher.group();
				}
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				inStream.close();
				httpConnection.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return ipLine;
	}

//	public static String GetNetIp1() {
//		String IP = "";
//		try {
//			String address = "http://ip.taobao.com/service/getIpInfo2.php?ip=myip";
//			URL url = new URL(address);
//			HttpURLConnection connection = (HttpURLConnection) url
//					.openConnection();
//			connection.setUseCaches(false);
//			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
//				InputStream in = connection.getInputStream(); // 将流转化为字符串
//				BufferedReader reader = new BufferedReader(
//						new InputStreamReader(in));
//				String tmpString = "";
//				StringBuilder retJSON = new StringBuilder();
//				while ((tmpString = reader.readLine()) != null) {
//					retJSON.append(tmpString + "\n");
//				}
//				JSONObject jsonObject = new JSONObject(retJSON.toString());
//				String code = jsonObject.getString("code");
//				if (code.equals("0")) {
//					// http://blog.csdn.net/pishum/article/details/
//					JSONObject data = jsonObject.getJSONObject("data");
//					IP = data.getString("ip") + "(" + data.getString("country")
//							+ data.getString("area") + "区"
//							+ data.getString("region") + data.getString("city")
//							+ data.getString("isp") + ")";
//					Log.e("提示", "您的IP地址是：" + IP);
//				} else {
//					IP = "";
//					Log.e("提示", "IP接口异常，无法获取IP地址！");
//				}
//			} else {
//				IP = "";
//				Log.e("提示", "网络连接异常，无法获取IP地址！");
//			}
//		} catch (Exception e) {
//			IP = "";
//			Log.e("提示", "获取IP地址时出现异常，异常信息是：" + e.toString());
//		}
//		return IP;
//	}
}
