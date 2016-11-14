package com.zozmom.ui;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;
import org.xutils.http.RequestParams;

import com.chasedream.zhumeng.R;
import com.umeng.analytics.MobclickAgent;
import com.zozmom.constants.Constant;
import com.zozmom.dialog.CustomProgressDialog;
import com.zozmom.gee.Geetest;
import com.zozmom.gee.GtDialog;
import com.zozmom.gee.GtDialog.GtListener;
import com.zozmom.manager.AccountManager;
import com.zozmom.model.UserInfoModel;
import com.zozmom.net.XHttp;
import com.zozmom.ui.DemoActivity.GtAppDlgTask;
import com.zozmom.ui.fragment.MeFragment;
import com.zozmom.ui.user.AlterPwdActivity;
import com.zozmom.util.DeviceInfo;
import com.zozmom.util.JsonUtil;
import com.zozmom.util.LogUtil;
import com.zozmom.util.MD5Utils;
import com.zozmom.util.NetWorkUtil;
import com.zozmom.util.RequestErrorUtil;
import com.zozmom.util.TimeCount;
import com.zozmom.util.ToastUtil;
import com.zozmom.util.UpgradeUtil;
import com.zozmom.util.Util;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LoginActivity extends BaseTaskActivity<Object> {
	TextView titleview;
	Button loginbyAccountView;
	EditText phoneEdit;
	EditText keyEdit;
	EditText passwordEdit;
	RelativeLayout layout1;
	RelativeLayout layout2;
	RelativeLayout layout3;
	Button button;
	Button checkbtn;
	Button requestKeyBtn;
	View kuaijieView;
	Button qqBtn;
	Button weixinBtn;
	int MaxTime = 10;
	TimeCount time;
	int resultcode;
	public static int resultCode = 8;
	String ip;
	String getfrom;
	String inputPhone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_activity);
		resultcode = getIntent().getIntExtra("code", 2);
		getfrom = getIntent().getStringExtra("from");
		inputPhone = getIntent().getStringExtra("phone");
		initView();
	}

	public void requestId() {
		if (!NetWorkUtil.isNetworkAvailable(this)) {
			ToastUtil.show(this, "网络错误");
			return;
		}
		XHttp.Post(Constant.GET_IP_URL, null, new CommonCallback<JSONObject>() {

			@Override
			public void onCancelled(CancelledException arg0) {

			}

			@Override
			public void onError(Throwable arg0, boolean arg1) {

			}

			@Override
			public void onFinished() {

			}

			@Override
			public void onSuccess(JSONObject arg0) {
				try {
					JSONObject object = (JSONObject) arg0.get("data");
					ip = object.getString("ip");
					Log.v("this", "ip" + ip);
					// extracted();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void initView() {
		titleview = (TextView) findViewById(R.id.title_textview);
		passwordEdit = (EditText) findViewById(R.id.editPwd1);
		phoneEdit = (EditText) findViewById(R.id.editName);
		if (inputPhone != null) {
			phoneEdit.setText(inputPhone);
		}
		keyEdit = (EditText) findViewById(R.id.keyedit);
		loginbyAccountView = (Button) findViewById(R.id.loginbyAccountView);
		requestKeyBtn = (Button) findViewById(R.id.send_code_btn);
		time = new TimeCount(60000, 1000, requestKeyBtn);
		layout1 = (RelativeLayout) findViewById(R.id.relativeLayout2);
		layout2 = (RelativeLayout) findViewById(R.id.relativeLayout3);
		layout3 = (RelativeLayout) findViewById(R.id.relativeLayout4);
		kuaijieView = findViewById(R.id.kuaij_per);
		qqBtn = (Button) findViewById(R.id.login_qqImageBtn);
		weixinBtn = (Button) findViewById(R.id.login_weixinImageBtn);
		checkbtn = (Button) findViewById(R.id.check);
		button = (Button) findViewById(R.id.btnLogin);
		// if (resultcode == 1) {// 注册
		// titleview.setText(getRString(R.string.register_title));
		// passwordEdit.setHint(getRString(R.string.input_password));
		// button.setText(getRString(R.string.register));
		// layout3.setVisibility(View.VISIBLE);
		// checkbtn.setSelected(check);
		// } else
		//
		if (resultcode == 2) {// 手机登录
			checkbtn.setSelected(check);
			titleview.setText(getRString(R.string.login));
			layout2.setVisibility(View.GONE);
			button.setText(getRString(R.string.login));
			layout3.setVisibility(View.VISIBLE);
			loginbyAccountView.setVisibility(View.VISIBLE);
		} else {// 忘掉密码了
			if (getfrom != null && getfrom.equals("user")) {
				titleview.setText("修改密码");
			} else
				titleview.setText(getRString(R.string.forget_title));
			passwordEdit.setHint(getRString(R.string.newpassword));
			layout2.setVisibility(View.VISIBLE);
			button.setText(getRString(R.string.sure));
			kuaijieView.setVisibility(View.GONE);
			qqBtn.setVisibility(View.GONE);
			weixinBtn.setVisibility(View.GONE);
		}
	}

	boolean check = true;

	/**
	 * 逐梦协议
	 * 
	 * @param view
	 */
	public void checkBtn(View view) {
		if (check) {
			check = false;
		} else
			check = true;
		checkbtn.setSelected(check);
	}
	/**
	 * 跳转到账户登录界面
	 * 
	 * @param view
	 */
	public void loginByname(View view) {
		Intent intent = new Intent(this, LoginByAccountActivity.class);
		String msg = phoneEdit.getText().toString();
		if (msg != null) {
			intent.putExtra("phone", msg);
		}
		startActivityForResult(intent, 0);
	}

	/**
	 * 退出
	 * 
	 * @param view
	 */
	public void exit(View view) {
		finish();
	}

	/**
	 * 查看逐梦协议
	 * 
	 * @param view
	 */
	public void openUserWarn(View view) {

		Intent intent = new Intent(this, BaseWebActivity.class);
		intent.putExtra("title", "用户协议");
		intent.putExtra("weburl",
				"http://weixin.zozmom.com/#userAgree?type=app");
		startActivity(intent);
	}

	/**
	 * 获取验证码
	 * 
	 * @param view
	 */
	public void requesCode(View view) {
		sendKey();
	}

	/**
	 * 登录 或者注册
	 * 
	 * @param view
	 */
	public void registerBtn(View view) {
		if (resultcode == 2) {
			phoneLogin();
		} else {
			alterPwd();
		}
	}

	String phone;
	String key;
	String password;

	public void init() {
		phone = phoneEdit.getText().toString();
		key = keyEdit.getText().toString();
		password = passwordEdit.getText().toString();
	}
	private GtAppDlgTask mGtAppDlgTask;
	private Context context = LoginActivity.this;
	private Geetest captcha = new Geetest(

	// 设置获取id，challenge，success的URL，需替换成自己的服务器URL
			Constant.gee_check1,

			// 设置二次验证的URL，需替换成自己的服务器URL
			Constant.gee_check);

	public void sendKey() {
		init();
		if (phone == null || !Util.isMobile(phone)) {
			ToastUtil.show(this, "请输入正确手机号");
			return;
		}
		if (!NetWorkUtil.isNetworkAvailable(this)) {
			ToastUtil.show(this, "网络错误");
			return;
		}
		requestKeyBtn.setClickable(false);
		// RequestParams params = new RequestParams(Constant.REGISTER_CHECK);
		// params.addParameter("phone", phone);
		// if (resultcode == 1) {
		// params.addParameter("type", "1");
		// post0(params);
		// } else {
		// params.addParameter("type", "0");
		// post0(params);
		// }

		GtAppDlgTask gtAppDlgTask = new GtAppDlgTask();
		mGtAppDlgTask = gtAppDlgTask;
		mGtAppDlgTask.execute();
		showProgressDialog();
		m_progressDialog
				.setOnCancelListener(new CustomProgressDialog.OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						LogUtil.v("user cancel progress dialog");
						mGtAppDlgTask.cancel(true);
						captcha.cancelReadConnection();
						stopProgressDialog();
						ToastUtil.show(context, "你已经取消");
						requestKeyBtn.setClickable(true);
					}
				});
	}

	class GtAppDlgTask extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {

			return captcha.checkServer();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				// 根据captcha.getSuccess()的返回值 自动推送正常或者离线验证
				openGtTest(context, captcha.getGt(), captcha.getChallenge(),
						captcha.getSuccess());
			} else {
				// TODO 从API_1获得极验服务宕机或不可用通知, 使用备用验证或静态验证
				// 静态验证依旧调用上面的openGtTest(_, _, _), 服务器会根据getSuccess()的返回值, 自动切换
				ToastUtil.show(context, "获取失败,请重新获取");
				LogUtil.v("Geetest Server is Down.");
				requestKeyBtn.setClickable(true);
				// 执行此处网站主的备用验证码方案
			}
		}

		public void openGtTest(Context ctx, String id, String challenge,
				boolean success) {

			GtDialog dialog = new GtDialog(ctx, id, challenge, success);
			dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					// TODO 取消验证
					LogUtil.v("user close the geetest.");
					ToastUtil.show(context, "您已取消验证");
					requestKeyBtn.setClickable(true);
				}
			});

			dialog.setGtListener(new GtListener() {

				@Override
				public void gtResult(boolean success, String result) {

					if (success) {
						LogUtil.v(result);
						try {
							JSONObject res_json = new JSONObject(result);
							Map<String, String> params = new HashMap<String, String>();
							params.put("geetest_challenge",
									res_json.getString("geetest_challenge"));
							params.put("geetest_validate",
									res_json.getString("geetest_validate"));
							params.put("geetest_seccode",
									res_json.getString("geetest_seccode"));
							params.put("phone", phone);

							String response = captcha.submitPostData(params,
									"utf-8");

							// TODO 验证通过, 获取二次验证响应, 根据响应判断验证是否通过完整验证
							LogUtil.v("server captcha :" + response);
							if (response.equals("1")) {
								time.start();
							} else {
								ToastUtil.show(context, "验证失败,请重新验证");
								requestKeyBtn.setClickable(true);
							}

						} catch (Exception e) {

							e.printStackTrace();
						}

					} else {
						// TODO 验证失败
						ToastUtil.show(context, "验证失败,请重新验证");
						LogUtil.v("client captcha failed:" + result);
						requestKeyBtn.setClickable(true);
					}
				}

				@Override
				public void gtCallClose() {

					LogUtil.v("close geetest windows");
					requestKeyBtn.setClickable(true);
				}

				@Override
				public void gtCallReady(Boolean status) {

					// progressDialog.dismiss();
					stopProgressDialog();

					if (status) {
						// TODO 验证加载完成
						LogUtil.v("geetest finish load");
					} else {
						// TODO 验证加载超时,未准备完成
						ToastUtil.show(context, "验证超时");
						LogUtil.v("there's a network jam");
						requestKeyBtn.setClickable(true);
					}
				}

			});
		}
	}

	public void post0(RequestParams params) {
		x.http().post(params, new CommonCallback<String>() {

			@Override
			public void onCancelled(CancelledException arg0) {
				Log.v("this", "onCancelled" + arg0.getMessage());
			}

			@Override
			public void onError(Throwable arg0, boolean arg1) {
				ToastUtil.show(LoginActivity.this, "获取失败");
				requestKeyBtn.setClickable(true);
			}

			@Override
			public void onFinished() {
				Log.v("this", "onFinished");
			}

			@Override
			public void onSuccess(String arg0) {
				Log.v("this", "onSuccess" + arg0.toString());
				time.start();
			}
		});
	}

	/**
	 * 通过手机修改密码
	 */
	public void alterPwd() {
		init();
		if (phone == null || !Util.isMobile(phone)) {
			ToastUtil.show(this, "请输入正确手机号");
			return;
		}
		if (key == null || key.trim().length() != 4) {
			ToastUtil.show(this, "验证码输入错误");
			return;
		}
		if (!Util.isPwd(password)) {
			ToastUtil.show(this, "密码格式错误");
			return;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("phone", phone);
		map.put("vcode", key);
		map.put("newPassword", MD5Utils.epPwd(password));
		Log.v("this", MD5Utils.epPwd(password));
		XHttp.Post(Constant.ALTER_PWD_BYPHONE, map,
				new CommonCallback<String>() {

					@Override
					public void onCancelled(CancelledException arg0) {

					}

					@Override
					public void onError(Throwable arg0, boolean arg1) {
						RequestErrorUtil.judge(arg0, LoginActivity.this);
					}

					@Override
					public void onFinished() {

					}

					@Override
					public void onSuccess(String arg0) {
						Log.v("this", "onSuccess" + arg0.toString());
						if (arg0.equals("true")) {
							showSucessDialog();
						} else {
							ToastUtil.show(LoginActivity.this, "修改失败");
						}
					}
				});
	}
	/**
	 * 手机登录
	 */
	public void phoneLogin() {
		init();
		if (phone == null || !Util.isMobile(phone)) {
			ToastUtil.show(this, "输入正确的手机号");
			return;
		}
		if (key == null || key.trim().length() != 4) {
			ToastUtil.show(this, "验证码输入错误");
			return;
		}
		if (!check) {
			ToastUtil.show(this, "请选择用户协议");
			return;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("phone", phone);
		map.put("vcode", key);
		// String mac = DeviceInfo.getMacAddress();
		String mac = UpgradeUtil.getChannelId(this);
		Log.v(TAG, mac);
		map.put("equipmentId", mac);
		XHttp.Post(Constant.LOGIN + "code", map,
				new CommonCallback<JSONObject>() {

					@Override
					public void onCancelled(CancelledException arg0) {

					}

					@Override
					public void onError(Throwable arg0, boolean arg1) {
						RequestErrorUtil.judge(arg0, LoginActivity.this);
					}

					@Override
					public void onFinished() {

					}

					@Override
					public void onSuccess(JSONObject arg0) {
						Log.v("this", "onSuccess" + arg0.toString());
						try {
							UserInfoModel model = (UserInfoModel) JsonUtil
									.JsonToModel(arg0, UserInfoModel.class);
							AccountManager.getInstance().setLogginUserInfo(
									model);
							 MobclickAgent.onProfileSignIn(phone);
//							TalkingDataAppCpa.onRegister(phone);
							if (MeFragment.meFragment != null) {
								MeFragment.meFragment.changgeView(model, true);
							}
							LoginActivity.this.setResult(resultCode);
							finish();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});

	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		if (arg1 == LoginByAccountActivity.resultCode) {
			setResult(resultCode);
			finish();
		}
	}

	/**
	 * QQ登录
	 * 
	 * @param view
	 */
	public void qqlogin(View view) {

	}
	/**
	 * 微信登录
	 * 
	 * @param view
	 */
	public void weixinlogin(View view) {

	}

	AlertDialog alertDialog;

	public void showSucessDialog() {
		AlertDialog.Builder builder = new Builder(this);
		alertDialog = builder.create();
		alertDialog.show();
		Window window = alertDialog.getWindow();
		window.setContentView(R.layout.alter_pwd_susucess);
		Button startCa = (Button) window.findViewById(R.id.alter_pwd_sucessbtn);
		startCa.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
				Intent data = new Intent();
				data.putExtra("phone", phone);
				LoginActivity.this.setResult(resultcode, data);
				finish();
			}
		});
		alertDialog.setCanceledOnTouchOutside(true);
	}

}