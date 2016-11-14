package com.zozmom.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

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
import android.widget.ProgressBar;
import android.widget.TextView;

public class UpdateInfoUtil3 {
	// static ProgressDialog progressDialog;
	static Handler handler;
	static Context context;
	UpdateInfo updateInfo;

	public UpdateInfoUtil3(Context context) {
		this.context = context;
	}

	/**
	 * 判断当前版本是否需要更新
	 * 
	 * @return
	 */
	public boolean isNeedUpdate() {
		String new_version = updateInfo.getVersion(); // 最新版本的版本号
		// 获取当前版本号
		String now_version = "";
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			now_version = packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		if (new_version.equals(now_version)) {
			return false;
		} else {
			return true;
		}
	}
	AlertDialog upDialog;
	ProgressBar progressBar;
	TextView hintview;
	public void downLoadFile(final String url, AlertDialog Dialog, Handler h,
			ProgressBar bar, TextView textview) {
		// progressDialog=pDialog;
		upDialog = Dialog;
		handler = h;
		progressBar = bar;
		hintview = textview;
		new Thread() {
			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(url);
				HttpResponse response;
				try {
					response = client.execute(get);
					HttpEntity entity = response.getEntity();
					final int length = (int) entity.getContentLength(); // 获取文件大小
					progressBar.setMax(length);
					handler.post(new Runnable() {
						public void run() {
							progressBar.setMax(length);
							hintview.setText("0%");// 设置进度条的总长度
						}
					});
					InputStream is = entity.getContent();
					FileOutputStream fileOutputStream = null;
					if (is != null) {
						File file = new File(
								Environment.getExternalStorageDirectory(),
								"zozmom.apk");
						fileOutputStream = new FileOutputStream(file);
						// 这个是缓冲区，即一次读取10个比特，我弄的小了点，因为在本地，所以数值太大一下就下载完了,
						// 看不出progressbar的效果。
						byte[] buf = new byte[1024];
						int ch = -1;
						int process = 0;
						while ((ch = is.read(buf)) != -1) {
							fileOutputStream.write(buf, 0, ch);
							process += ch;
							final int a = process;
							handler.post(new Runnable() {
								public void run() {
									progressBar.setProgress(a); // 这里就是关键的实时更新进度了！
									int c = a * 100 / length;
									hintview.setText(c + "%");// 设置进度条的总长度
								}
							});
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
				// progressDialog.cancel();
				if (upDialog != null) {
					upDialog.cancel();
					upDialog = null;
				}
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
