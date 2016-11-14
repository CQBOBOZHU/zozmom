package com.zozmom.ui.user;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chasedream.zhumeng.R;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.zozmom.constants.Constant;
import com.zozmom.model.UserInfoModel;
import com.zozmom.net.XHttp;
import com.zozmom.ui.BaseTaskActivity;
import com.zozmom.ui.BaseWebActivity;
import com.zozmom.ui.LoginActivity;
import com.zozmom.ui.fragment.MeFragment;
import com.zozmom.util.FileUtil;
import com.zozmom.util.NetWorkUtil;
import com.zozmom.util.RequestErrorUtil;
import com.zozmom.util.ToastUtil;
import com.zozmom.util.UpdateInfoUtil3;
import com.zozmom.util.UpgradeUtil;

/**
 * 设置页面
 * 
 * @author Administrator
 * 
 */
@ContentView(R.layout.set_up_activity)
public class SetupActivity extends BaseTaskActivity<String> {
	@ViewInject(R.id.setup_logout_btn)
	Button logoutBtn;
	private UserInfoModel model;
//	public static SetupActivity instance = null;
	private AlertDialog alertDialog;
	@ViewInject(R.id.setup_version)
	TextView versionView;
	@ViewInject(R.id.setup_memory)
	TextView memeView;// 内存量

	UpdateInfoUtil3 infoUtil3;

	@SuppressLint("HandlerLeak")
	Handler memhandle = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			long mem = (long) msg.obj;
			// if(mem==null){
			//
			// }
			double mMem = (double) mem / 1024 / 1024;// M
			java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
			if (mMem < 1) {
				double kbMem = (double) mem / 1024;// kb
				// df.format(kbMem);
				if (kbMem == 0) {
					memeView.setText(0 + "KB");
				} else
					memeView.setText(df.format(kbMem) + "KB");
			} else {
				// df.format(mMem);
				memeView.setText(df.format(mMem) + "M");
			}
			// mem/1024/1024;//M
			// memeView.setText(mem+"");
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		x.view().inject(this);
		infoUtil3 = new UpdateInfoUtil3(this);
//		instance = this;
		model = getLogginUserinfo();
		if (model == null) {
			logoutBtn.setVisibility(View.GONE);
		}
		String versionName = UpgradeUtil.getVersionName(this);
		versionView.setText(versionName);
		selectMem();
	}

	public void selectMem() {
		new Thread() {
			public void run() {
				File cacheDir = StorageUtils
						.getCacheDirectory(getApplicationContext());
				long mem = FileUtil.getTotalFile(cacheDir);
				Message message = new Message();
				message.obj = mem;
				memhandle.sendMessage(message);
			}
		}.start();
	}

	Intent intent;

	@Override
	public void onClick(View v) {
		super.onClick(v);
		Intent intent;
		switch (v.getId()) {
			case R.id.status_bar_Exit :
				finish();
				break;
			case R.id.setup_aboutview :
				intent = new Intent(this, BaseWebActivity.class);
				intent.putExtra("title", "关于逐梦");
				intent.putExtra("weburl",
						"http://weixin.zozmom.com/#gyzm?type=app");
				startActivity(intent);
				break;
			case R.id.setup_view :
				GetNewVersion();
				break;
			case R.id.setup_memory :
				break;
			case R.id.setup_userag_view :
				intent = new Intent(this, BaseWebActivity.class);
				intent.putExtra("title", "用户协议");
				intent.putExtra("weburl",
						"http://weixin.zozmom.com/#userAgree?type=app");
				startActivity(intent);
				break;
			case R.id.clean_mem_lay :
				memeView.setText("0KB");
				new Thread() {
					public void run() {
						final File cacheDir = StorageUtils
								.getCacheDirectory(getApplicationContext());
						FileUtil.deleteFile(cacheDir);
					}
				}.start();

				break;
		}
	}
	String oldversionName;
	/**
	 * 判断是否是最新版本号
	 */
	public void GetNewVersion() {
		if (!NetWorkUtil.isNetworkAvailable(this)) {
			return;
		}
		int vcode = UpgradeUtil.getVersionCode(this);
		oldversionName = UpgradeUtil.getVersionName(this);
		XHttp.Get(Constant.UP_VERSION + vcode, null,
				new CommonCallback<JSONObject>() {

					@Override
					public void onCancelled(CancelledException arg0) {

					}

					@Override
					public void onError(Throwable arg0, boolean arg1) {
						Log.v(TAG, "onError" + arg0.toString());
						RequestErrorUtil.judge(arg0, m_activity);
					}

					@Override
					public void onFinished() {

					}

					@Override
					public void onSuccess(JSONObject arg0) {
						Log.v(TAG, "onSuccess" + arg0.toString());
						// if (arg0.toString().contains("code")
						// && arg0.toString().contains("HOM.0001")) {
						// ToastUtil.show(m_activity, "当前为最新版本");
						// return;
						// } else {
						try {
							// {"vid":2,"versionNum":"V1.0.1","desc":"新增商品搜索功能","link":"http://www.hao123.com/"}
							String content = arg0.getString("desc");
							updataUrl = arg0.getString("link");
							String newversion = arg0.getString("versionNum");
							showUpdateDialog(content, newversion);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						// }
					}
				});

	}

	public void lout() {
		intent = new Intent(this, LoginActivity.class);
		logout();
		MeFragment.meFragment.isLogin(false);
		logoutBtn.setVisibility(View.GONE);
		startActivityForResult(intent, 0);
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg0 == 0 && arg1 == LoginActivity.resultCode) {
			logoutBtn.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 退出登录
	 */
	public void showExitDialog(View view) {
		if (alertDialog != null) {
			alertDialog.show();
			return;
		}
		AlertDialog.Builder builder = new Builder(this);
		alertDialog = builder.create();
		alertDialog.show();
		Window window = alertDialog.getWindow();
		window.setContentView(R.layout.exit_activity);
		Button startCa = (Button) window.findViewById(R.id.exitBtn0);
		Button startM = (Button) window.findViewById(R.id.exitBtn1);
		startCa.setOnClickListener(listener);
		startM.setOnClickListener(listener);
		alertDialog.setCanceledOnTouchOutside(true);
	}

	android.view.View.OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.exitBtn0 :
					alertDialog.dismiss();
					lout();
					break;
				case R.id.exitBtn1 :
					alertDialog.dismiss();
					break;

				default :
					break;
			}
		}
	};

	private ProgressDialog progressDialog;

	String description = "";// 更新的内容
	int newversionCode;// 最新版本的版本号
	// String updataUrl;// 更新版本的url;
	String updataUrl = "";// 更新版本的url;
	AlertDialog upDialog;
	Button sureBtn;
	// 显示是否要更新的对话框
	@SuppressLint("InflateParams")
	private void showUpdateDialog(String content, String newversion) {
		if (upDialog != null) {
			upDialog.show();
			return;
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// builder.SETV
		upDialog = builder.create();
		upDialog.show();
		View view = LayoutInflater.from(this)
				.inflate(R.layout.up_version, null);
		upDialog.getWindow().setContentView(view);
		TextView contentview = (TextView) view
				.findViewById(R.id.up_content_view);
		TextView upversion = (TextView) view.findViewById(R.id.up_title_view);
		contentview.setText(content);
		upversion.setText(newversion);
		sureBtn = (Button) view.findViewById(R.id.up_sure_btn);
		sureBtn.setOnClickListener(islistener);
		bar = (ProgressBar) view.findViewById(R.id.download_progressbar);
		barhint = (TextView) view.findViewById(R.id.download_hintview);
		upDialog.setCanceledOnTouchOutside(true);
	}
	ProgressBar bar;
	TextView barhint;

	OnClickListener islistener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.up_sure_btn :
					if (Environment.getExternalStorageState().equals(
							Environment.MEDIA_MOUNTED)) {
						if (!NetWorkUtil.isNetworkAvailable(m_activity)) {
							upDialog.cancel();
							ToastUtil.show(m_activity, "网络错误");
							return;
						}
						sureBtn.setVisibility(View.GONE);
						infoUtil3.downLoadFile(updataUrl, upDialog, handler1,
								bar, barhint);
					} else {
						ToastUtil.show(m_activity, "SD卡不可用，请插入SD卡");
					}
					break;

				default :
					break;
			}
		}
	};

	@SuppressLint("HandlerLeak")
	private Handler handler1 = new Handler() {
		public void handleMessage(Message msg) {
		};
	};
}
