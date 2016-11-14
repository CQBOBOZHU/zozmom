//package com.zozmom.ui;
//
//import java.util.List;
//
//import com.zozmom.MainActivity;
//import com.zozmom.util.ToastUtil;
//
//import android.app.ActivityManager;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//
//abstract public class TimeOutActivity extends BaseActivity<Object> {
//	private boolean isLeave = false;
//	SharedPreferences pref;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		pref = getSharedPreferences("timeoutcheck", Context.MODE_PRIVATE);
//	}
//
//	/**
//	 * 回调函数，方便测试
//	 * 
//	 * @return
//	 */
//	abstract protected String getTag();
//
//	// ......省略号......
//
//	/***
//	 * 当用户使程序恢复为前台显示时执行onResume()方法,在其中判断是否超时.
//	 */
//	@Override
//	protected void onResume() {
//		// Log.i("Back",getTag() + "，onResume，是否在前台：" + isOnForeground());
//		super.onResume();
//		if (isLeave) {
//			isLeave = false;
//			timeOutCheck();
//		}
//	}
//
//	@Override
//	protected void onStop() {
//		super.onStop();
//		if (!isOnForeground()) {
//			if (!isLeave) {
//				isLeave = true;
//				saveStartTime();
//			}
//		}
//	}
//
//	int TIMEOUT_ALP = 1;
//	String ALP = "";
//	int REQ_COMPARE_PATTERN_TIMEOUT_CHECK = 0;
//
//	public void timeOutCheck() {
//		long endtime = System.currentTimeMillis();
//		if (endtime - getStartTime() >= 30 * 1000) {
//			ToastUtil.show(this, "超时了,请重新验证");
//			Intent intent = new Intent(this, MainActivity.class);
//			startActivity(intent);
//		}
//	}
//
//	String START_TIME = "stattime";
//
//	public void saveStartTime() {
//		pref.edit().putLong(START_TIME, System.currentTimeMillis()).commit();
//	}
//
//	public long getStartTime() {
//		long startTime = 0;
//		try {
//			startTime = pref.getLong(START_TIME, 0);
//		} catch (Exception e) {
//			startTime = 0;
//		}
//		return startTime;
//	}
//
//	/**
//	 * 程序是否在前端运行,通过枚举运行的app实现。防止重复超时检测多次,保证只有一个activity进入超时检测
//	 * 当用户按home键时，程序进入后端运行，此时会返回false，其他情况引起activity的stop函数的调用，会返回true
//	 * 
//	 * @return
//	 */
//	public boolean isOnForeground() {
//		ActivityManager activityManager = (ActivityManager) getApplicationContext()
//				.getSystemService(Context.ACTIVITY_SERVICE);
//		String packageName = getApplicationContext().getPackageName();
//
//		List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
//				.getRunningAppProcesses();
//		if (appProcesses == null)
//			return false;
//
//		for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
//			if (appProcess.processName.equals(packageName)
//					&& appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
//				return true;
//			}
//		}
//		return false;
//	}
//}