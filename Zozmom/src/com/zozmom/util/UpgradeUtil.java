package com.zozmom.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.chasedream.zhumeng.R;
import com.zozmom.model.UpdateInfo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

public class UpgradeUtil {

	public UpgradeUtil(Context context) {
		this.context = context;
	}

	public static String getChannelId(Context paramContext) {
		try {
			Object localObject = paramContext.getPackageManager()
					.getApplicationInfo(paramContext.getPackageName(), 128).metaData
					.get("UMENG_CHANNEL");
			if (localObject != null) {
				String str = localObject.toString();
				return str;
			}
		} catch (PackageManager.NameNotFoundException localNameNotFoundException) {
			localNameNotFoundException.printStackTrace();
		}
		return String.valueOf("zozmom");
	}
	/**
	 * 获取versioncode
	 * 
	 * @param paramContext
	 * @return
	 */
	public static int getVersionCode(Context paramContext) {
		try {
			int i = paramContext.getPackageManager().getPackageInfo(
					paramContext.getPackageName(), 0).versionCode;
			return i;
		} catch (Exception localException) {
		}
		return 0;
	}
	/**
	 * 获取versionname
	 * 
	 * @param paramContext
	 * @return
	 */
	public static String getVersionName(Context paramContext) {
		try {
			String str = paramContext.getPackageManager().getPackageInfo(
					paramContext.getPackageName(), 0).versionName;
			return str;
		} catch (Exception localException) {
		}
		return null;
	}

	ProgressDialog progressDialog;
	Handler handler;
	Context context;

	/**
	 * 判断当前版本是否需要更新
	 * 
	 * @return
	 */
	public boolean isNeedUpdate(int newversionCode) {
		// 获取当前版本号
		int oldversionCode = getVersionCode(context);
		if (newversionCode == oldversionCode) {
			return false;
		} else {
			return true;
		}
	}
	public void downLoadFile(final String url, final ProgressDialog pDialog,
			Handler h) {
		progressDialog = pDialog;
		handler = h;
		new Thread() {
			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(url);
				HttpResponse response;
				try {
					response = client.execute(get);
					HttpEntity entity = response.getEntity();
					int length = (int) entity.getContentLength(); // 获取文件大小
					int maxlne = length / 1024;
					progressDialog.setMax(maxlne); // 设置进度条的总长度
					// mbar.setMax(length);
					InputStream is = entity.getContent();
					FileOutputStream fileOutputStream = null;
					if (is != null) {
						File file = new File(
								Environment.getExternalStorageDirectory(),
								"zozmom.apk");
						fileOutputStream = new FileOutputStream(file);
						byte[] buf = new byte[1024];
						int ch = -1;
						int process = 0;
						while ((ch = is.read(buf)) != -1) {
							fileOutputStream.write(buf, 0, ch);
							process += ch;
							progressDialog.setProgress(process / 1024); // 这里就是关键的实时更新进度了！
						}
					}
					fileOutputStream.flush();
					if (fileOutputStream != null) {
						fileOutputStream.close();
					}
					down();
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}.start();
	}

	void down() {
		handler.post(new Runnable() {
			public void run() {
				progressDialog.dismiss();
				update();
			}
		});
	}

	void update() {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(Environment
				.getExternalStorageDirectory(), "zozmom.apk")),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
	}
}
