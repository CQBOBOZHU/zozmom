package com.zozmom.ui;

import java.util.List;
import java.util.Map;

import org.xutils.common.Callback.CommonCallback;

import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.utils.Log;
import com.zozmom.MainActivity;
import com.zozmom.dialog.CustomProgressDialog;
import com.zozmom.manager.ActiviyManager;
import com.zozmom.model.UserInfoModel;
import com.zozmom.net.XHttp;
import com.zozmom.pic.PhotoSelector;
import com.zozmom.util.RequestErrorUtil;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class BaseActivity<T> extends FragmentActivity
		implements
			OnClickListener,
			OnItemClickListener,
			OnItemSelectedListener,
			CommonCallback<T> {
	public static final String TAG = "zozmom";
	// private UploadPhotoTask m_picUploader;
	public PhotoSelector m_photoSelector = null;
	public static final String Exit_Action = "com.chasedream.zhumeng.action.exit";
	protected boolean m_loginIfAccountNull = false; // account为空就跳转登陆页
	protected boolean m_pressBackToExist = false; // 两次back退出
	static boolean m_showProgressDialog = true;
	static CustomProgressDialog m_progressDialog;
	static int m_showDialogCount = 0;
	public  BaseActivity<?> m_activity;
	public static int Mwidth;
	public static int Mheight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		ActiviyManager.instance().addActivity(this);
		IntentFilter filter = new IntentFilter();
		filter.addAction(Exit_Action);
		m_activity = this;
		WindowManager wm = this.getWindowManager();

		Mwidth = wm.getDefaultDisplay().getWidth();
		Mheight = wm.getDefaultDisplay().getHeight();

		// MobclickAgent.setDebugMode(true);
		// MobclickAgent.openActivityDurationTrack(false);
		// MobclickAgent.setScenarioType(m_activity, EScenarioType.E_UM_NORMAL);

		this.registerReceiver(this.finishAppReceiver, filter);// 注册广播
	}

	protected BroadcastReceiver finishAppReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			finish();
		}
	};

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		if (m_progressDialog != null) {
			m_progressDialog.dismiss();
			m_progressDialog = null;
			m_showDialogCount = 0;
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActiviyManager.instance().removeActivity(this);
		this.m_activity=null;
		this.unregisterReceiver(this.finishAppReceiver);
	}

	@Override
	public void onClick(View v) {
	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
	}

	@Override
	public void onCancelled(CancelledException arg0) {
		// stopProgressDialog();
	}

	@Override
	public void onError(Throwable arg0, boolean arg1) {
		// stopProgressDialog();
		RequestErrorUtil.judge(arg0, this);
	}

	@Override
	public void onFinished() {
		// stopProgressDialog();
	}

	public void requstPost(String url, Map<String, Object> map) {
		// showProgressDialog();
		XHttp.Post(url, map, this);
	}

	public void requstGet(String url, Map<String, String> map) {
		// showProgressDialog();
		XHttp.Get(url, map, this);
	}

	public void requstPostByToken(String url, UserInfoModel infoModel,
			Map<String, Object> map) {
		XHttp.PostByToken(url, infoModel, map, this);
	}

	@Override
	public void onSuccess(T arg0) {
		// stopProgressDialog();
	}

	public void onPhotoSelected(String photo, boolean isHead) {

	}

	public void redirect(Class<?> clz) {
		Intent t = new Intent();
		t.setClass(this, clz);
		this.startActivity(t);
	}

	public void onPicUploaded(List<String> uploadedPics) {
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}

	/**
	 * 选择添加图片所用的方法(0：相机,1：相册) dialog
	 */
	public void showAlerDialog() {
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setPositiveButton("相机", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				startCamera(true);
			}
		});
		builder.setNegativeButton("相册", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				startLocalPhoto(true);
			}
		});
		AlertDialog alert = builder.create();
		builder.setTitle("更换头像");
		WindowManager wm = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);
		// @SuppressWarnings("deprecation")
		// int width = wm.getDefaultDisplay().getWidth();
		@SuppressWarnings("deprecation")
		int height = wm.getDefaultDisplay().getHeight();
		Window w1 = alert.getWindow();
		WindowManager.LayoutParams lp1 = w1.getAttributes();
		lp1.x = 0;
		lp1.y = height / 2;
		lp1.alpha = 0.7f;
		w1.setAttributes(lp1);
		alert.show();
	}

	public void startCamera(boolean isHead) {
		if (m_photoSelector == null) {
			m_photoSelector = new PhotoSelector(this);
		}
		m_photoSelector.startCamera(isHead);
	}

	public void startLocalPhoto(boolean isHead) {
		if (m_photoSelector == null) {
			m_photoSelector = new PhotoSelector(this);
		}
		m_photoSelector.startLocalPhoto(isHead);
	}

	private static Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				if (m_progressDialog != null) {
					try {
						m_progressDialog.dismiss();
					} catch (Exception ex) {
						// ex.printStackTrace();
					}
				}
			} else if (msg.what == 1) {
				if (m_progressDialog != null) {
					try {
						m_progressDialog.show();
					} catch (Exception ex) {
						// ex.printStackTrace();
					}
				}
			}
		}
	};
	/**
	 * 停止加载动画
	 */
	public void stopProgressDialog() {
		stopProgressDialog(false);
	}

	public void stopProgressDialog(boolean force) {
		if (m_showProgressDialog) {
			if (force) {
				Message msg = mHandler.obtainMessage(0);
				mHandler.sendMessage(msg);
			} else {
				m_showDialogCount--;
				if (m_showDialogCount <= 0) {
					Message msg = mHandler.obtainMessage(0);
					mHandler.sendMessage(msg);
				}
			}
		}
	}
	/**
	 * 加载动画
	 */
	public void showProgressDialog() {
		showProgressDialog(false);
	}

	public void showProgressDialog(boolean force) {
		if (m_showProgressDialog) {
			if (force) {
				Message msg = mHandler.obtainMessage(1);
				mHandler.sendMessage(msg);
			} else {
				if (m_showDialogCount <= 0) {
					Message msg = mHandler.obtainMessage(1);
					mHandler.sendMessage(msg);
				}
				m_showDialogCount++;
			}
		}
	}

	long oldtime = 0l;
	long newtime = 0l;
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		if (m_progressDialog == null) {
			m_showDialogCount = 0;
			m_progressDialog = CustomProgressDialog.createDialog(this);
		}
		/**
		 * 在这里本来可以使用if (!isAppOnForeground()) {//to do
		 * sth}，但为了避免再次调用isAppOnForeground()而造成费时且增大系统的开销，故在这里我应用了一个标志位来判断
		 */
		// if (isForeground == false) {
		// Log.v("app", isForeground + "");
		// newtime = System.currentTimeMillis();
		// isForeground = true;
		// if ((newtime - oldtime) > 50000) {
		// Intent intent = new Intent(this, MainActivity.class);
		// startActivity(intent);
		// }
		// }
	}

	public boolean isForeground = false;

	@Override
	protected void onStop() {
		super.onStop();
		if (!isAppOnForeground()) {
			Log.v("app", "isForeground :" + isForeground);
			oldtime = System.currentTimeMillis();
			isForeground = false;
		}
	}

	/**
	 * 程序是否在前台运行
	 * 
	 * @return
	 */
	public boolean isAppOnForeground() {

		ActivityManager activityManager = (ActivityManager) getApplicationContext()
				.getSystemService(Context.ACTIVITY_SERVICE);
		String packageName = getApplicationContext().getPackageName();
		/**
		 * 获取Android设备中所有正在运行的App
		 */
		List<RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		if (appProcesses == null)
			return false;

		for (RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(packageName)
					&& appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}

		return false;
	}
	
	
}