package com.zozmom.ui.pay;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.chasedream.zhumeng.R;
import com.iapppay.interfaces.SDKApiInterface;
import com.iapppay.interfaces.callback.IPayResultCallback;
import com.iapppay.sdk.main.IAppPay;
import com.zozmom.constants.Constant;
import com.zozmom.model.PayModel;
import com.zozmom.model.UserInfoModel;
import com.zozmom.net.XHttp;
import com.zozmom.ui.BaseTaskActivity;
import com.zozmom.util.NetWorkUtil;
import com.zozmom.util.RequestErrorUtil;
import com.zozmom.util.TimeCount;
import com.zozmom.util.ToastUtil;

/**
 * 支付订单
 * 
 * @author Administrator
 * 
 */
@ContentView(R.layout.pay_activity)
public class PayActivity extends BaseTaskActivity<JSONObject>
		implements
			OnCheckedChangeListener {
	@ViewInject(R.id.pay_time1_textview)
	Button timebtn1;
	@ViewInject(R.id.pay_time2_textview)
	Button timebtn2;
	@ViewInject(R.id.pay_time3_textview)
	Button timebtn3;
	@ViewInject(R.id.pay_time4_textview)
	Button timebtn4;
	@ViewInject(R.id.status_bar_title)
	TextView titleView;
	@ViewInject(R.id.pay_numberTextView)
	TextView pay_numberView;// 需要的价钱
	@ViewInject(R.id.paygoods_numbertextview)
	TextView paygoods_numberView;// 订单号
	@ViewInject(R.id.pay_balance_textview)
	TextView paybalanceView;// 余额剩余
	@ViewInject(R.id.pay_balancecheck_btn)
	Button balancecheckbtn;// 是否余额支付按钮
	@ViewInject(R.id.pay_radiogroup)
	RadioGroup radioGroup;
	@ViewInject(R.id.pay_lastmoney_textview)
	TextView lastmoneyView;
	@ViewInject(R.id.pay_type)
	View paytypeView;
	@ViewInject(R.id.recharge_todobtn)
	Button payBtn;

	int random;// 随机数

	private static final String CHANNEL_WECHAT = "wx";// 微信支付渠道
	private static final String DREAM_COIN = "dreamCoin";// 余额支付渠道

	// 最后需要支付的金额
	boolean isbalanselect = true;// true 选中 false 没有选择
	private int lastpayprice = 0;// 最后需要支付的
	TimeCount timeCount;
	// public static PayActivity instance = null;
	PayModel payModel;
	UserInfoModel infoModel;
	private long orderNum;// 支付订单
	int dreamCoin;// 用户余额
	int payprice;// 支付额度
	String clientIp;// ip 外网
	boolean bl = false;// 用来判断IP是否获取到.
	boolean isdreamCoin = true;// 用来判断余额是否充足
	boolean ispayByDreamCoinPay = false;// 判断是否采用的全余额支付

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		x.view().inject(this);
		requesip();
		infoModel = getLogginUserinfo();
		payModel = (PayModel) getIntent().getSerializableExtra("data");
		random = getIntent().getIntExtra("random", 1);
		// instance = this;
		extracted();
		titleView.setText(getRString(R.string.pay_title));
		balancecheckbtn.setSelected(isbalanselect);
		stance();
	}

	/**
	 * 初始化
	 */
	private void stance() {
		orderNum = payModel.getOrderId();
		paygoods_numberView.setText(String.valueOf(orderNum));
		radioGroup.check(R.id.pay_radbtn);
		radioGroup.setOnCheckedChangeListener(this);
		payprice = payModel.getPrice();
		pay_numberView.setText(payprice + "元");
		dreamCoin = payModel.getDreamCoin();
		paybalanceView.setText("(账户余额:" + dreamCoin + "逐梦币)");
		if (dreamCoin >= payprice) {
			isdreamCoin = true;
			paytypeView.setVisibility(View.GONE);
			radioGroup.setVisibility(View.GONE);
		} else {
			isdreamCoin = false;
			paytypeView.setVisibility(View.VISIBLE);
			radioGroup.setVisibility(View.VISIBLE);
			lastpayprice = payprice - dreamCoin;
			lastmoneyView.setText(lastpayprice + "元");
		}
	}

	/**
	 * 计时开始
	 */
	private void extracted() {
		timeCount = new TimeCount(1800000, 1000, timebtn1, timebtn2, timebtn3,
				timebtn4, this);
		timeCount.start();
	}

	@Override
	public void onClick(View v) {
		giveup();
	}

	public void requesip() {
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
					JSONObject jsonObject = arg0.getJSONObject("data");
					clientIp = jsonObject.getString("ip");
					Log.v(TAG, "onSuccess" + clientIp);
					if (bl) {
						pay();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		});
	}

	/**
	 * 是否使用余额支付
	 * 
	 * @param view
	 */
	public void isUsebalance(View view) {
		if (isdreamCoin) {// 余额充足的时候
			if (isbalanselect) {// 选择
				lastpayprice = payprice;
				lastmoneyView.setText(lastpayprice + "元");
				isbalanselect = !isbalanselect;
				balancecheckbtn.setSelected(isbalanselect);
				paytypeView.setVisibility(View.VISIBLE);
				radioGroup.setVisibility(View.VISIBLE);
			} else {
				lastpayprice = 0;
				paytypeView.setVisibility(View.GONE);
				radioGroup.setVisibility(View.GONE);
				isbalanselect = !isbalanselect;
				balancecheckbtn.setSelected(isbalanselect);
			}
		} else {// 余额不足的时候
			if (isbalanselect) {
				lastpayprice = payprice;
			} else
				lastpayprice = payprice - dreamCoin;
			lastmoneyView.setText(lastpayprice + "元");
			isbalanselect = !isbalanselect;
			balancecheckbtn.setSelected(isbalanselect);
		}
	}

	/**
	 * 支付 and 逐梦
	 * 
	 * @param view
	 */
	@SuppressLint("NewApi")
	public void pay(View view) {
		if (!NetWorkUtil.isNetworkAvailable(m_activity)) {
			ToastUtil.show(m_activity, getRString(R.string.network_hint));
			return;
		}
		payBtn.setClickable(false);
		payBtn.setBackgroundResource(R.drawable.btn_background_gray);
		if (clientIp == null) {
			bl = true;
			requesip();
		} else {
			pay();
		}

	}
	/**
	 * 向自己的服务器发送订单请求
	 */
	public void pay() {
		long time = System.currentTimeMillis();
		String url = Constant.PAY_ORDER + orderNum;
		String url1 = Constant.PAY_ORDER_ByCoin + orderNum;
		// Log.v(TAG, "infoModel-token:" + infoModel.getToken());
		// Log.v(TAG, "infoModel-channel:" + CHANNEL_WECHAT);
		// Log.v(TAG, "infoModel-clientIp:" + clientIp);
		RequestParams params;
		if (isdreamCoin) {
			if (!isbalanselect) {
				params = new RequestParams(url);
				params.addHeader(
						"token-param",
						time + "," + infoModel.getToken() + ","
								+ infoModel.getUserId());
				params.addParameter("channel", CHANNEL_WECHAT);
				params.addParameter("clientIp", clientIp);
				// return;
			} else {
				ispayByDreamCoinPay = true;
				params = new RequestParams(url1);
				params.addHeader(
						"token-param",
						time + "," + infoModel.getToken() + ","
								+ infoModel.getUserId());
				params.addParameter("clientIp", clientIp);
				params.addParameter("selected", "selected");// 选择余额支付
				params.addParameter("dreamCoin", DREAM_COIN);
			}
		} else {
			params = new RequestParams(url);
			// channel=…&clientIp=…&dreamCoin=…&selected=
			params.addHeader("token-param", time + "," + infoModel.getToken()
					+ "," + infoModel.getUserId());
			params.addParameter("channel", CHANNEL_WECHAT);
			params.addParameter("clientIp", clientIp);
			if (isbalanselect) {
				params.addParameter("selected", "selected");// 选择余额支付
				params.addParameter("dreamCoin", dreamCoin);
			}
		}
		x.http().post(params, new CommonCallback<String>() {

			@Override
			public void onCancelled(CancelledException arg0) {

			}

			@SuppressLint("NewApi")
			@Override
			public void onError(Throwable arg0, boolean arg1) {
				Log.v(TAG, "onError " + arg0.toString());
				RequestErrorUtil.judge(arg0, m_activity);
				payBtn.setClickable(true);
				payBtn.setBackgroundResource(R.drawable.btn_background_yellow);
			}

			@Override
			public void onFinished() {

			}

			@SuppressLint("NewApi")
			@Override
			public void onSuccess(String arg0) {
				Log.v(TAG, "onSuccess " + arg0.toString());
				// {"orderId":160624374521858,"price":10,"dreamCoin":946586}

				payBtn.setClickable(true);
				payBtn.setBackgroundResource(R.drawable.btn_background_yellow);
				if (ispayByDreamCoinPay) {
					Intent intent = new Intent(m_activity,
							PaysucessActivity.class);
					intent.putExtra("result", arg0.toString());
					m_activity.startActivity(intent);
				} else {
					// 获取服务区返回回来的transid (这个是用来调用爱贝支付)
					// Pingpp.createPayment(m_activity, arg0);(这个是ping++支付)
					// transid=xxxx&appid=xxxx
					// JSONObject jsonObject=new JSONObject(arg0);
					// String transid=jsonObject.getString("transid");
					// Log.v(TAG, "transid " + transid.toString());
					if (arg0.contains("\""))
						arg0 = arg0.replace("\"", "");
					Log.v(TAG, "onSuccess " + arg0.toString());
					IAppPay.startPay(PayActivity.this, "transid=" + arg0
							+ "&appid=3005839314", callback);

				}
			}
		});
	}

	IPayResultCallback callback = new IPayResultCallback() {

		@Override
		public void onPayResult(int arg0, String arg1, String arg2) {
			// TODO Auto-generated method stub
			Log.v("this", "arg0 :" + arg0);
			if (arg0 == IAppPay.PAY_SUCCESS) {
				Intent intent = new Intent(PayActivity.this,
						PaysucessActivity.class);
				intent.putExtra("result", "success");
				startActivity(intent);
			} else if (arg0 == IAppPay.PAY_CANCEL) {
				cancleOrder();
			} else if (arg0 == IAppPay.PAY_ERROR) {
				Intent intent = new Intent(PayActivity.this,
						PaysucessActivity.class);
				intent.putExtra("result", "false");
				startActivity(intent);
			}
		}
	};

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {

	}

	AlertDialog alertDialog;

	/**
	 * 是否放弃逐梦
	 */
	public void giveup() {
		AlertDialog.Builder builder = new Builder(this);
		alertDialog = builder.create();
		alertDialog.show();
		Window window = alertDialog.getWindow();
		window.setContentView(R.layout.give_up_activity);
		Button startCa = (Button) window.findViewById(R.id.pay_btnsure);
		Button startM = (Button) window.findViewById(R.id.pay_btnexit);
		startCa.setOnClickListener(listener);
		startM.setOnClickListener(listener);
		alertDialog.setCanceledOnTouchOutside(true);
	}

	OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.pay_btnsure :
					cancleOrder();
					break;
				case R.id.pay_btnexit :
					alertDialog.dismiss();
					break;
			}
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) { // 监控/拦截/屏蔽返回键
			giveup();
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * onActivityResult 获得支付结果，如果支付成功，服务器会收到ping++ 服务器发送的异步通知。 最终支付成功根据异步通知为准
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		// 支付页面返回处理
		/*
		 * if (requestCode == Pingpp.REQUEST_CODE_PAYMENT) { if (resultCode ==
		 * Activity.RESULT_OK) { String result =
		 * data.getExtras().getString("pay_result"); Log.v(TAG, result); if
		 * (result.equals("cancel")) { cancleOrder(); } else { Intent intent =
		 * new Intent(PayActivity.this, PaysucessActivity.class);
		 * intent.putExtra("result", result.toString()); startActivity(intent);
		 * }
		 */
		// } else if (result.equals("fail")) {
		// Intent intent = new Intent(PayActivity.this,
		// PaysucessActivity.class);
		// intent.putExtra("result", result.toString());
		// startActivity(intent);
		// } else if (result.equals("cancel")) {
		// ToastUtil.show(m_activity, "支付返回");
		// } else if ((result.equals("invalid"))) {
		// ToastUtil.show(m_activity, "支付失效");
		// }
		/*
		 * 处理返回值 "success" - payment succeed "fail" - payment failed "cancel" -
		 * user canceld "invalid" - payment plugin not installed
		 */
		// String errorMsg = data.getExtras().getString("error_msg"); //
		// 错误信息
		// String extraMsg = data.getExtras().getString("extra_msg"); //
		// 错误信息
		// showMsg(result, errorMsg, extraMsg);
		// }
		// }
	}

	public void cancleOrder() {
		if (!NetWorkUtil.isNetworkAvailable(this)) {
			finish();
			return;
		}
		XHttp.PostByToken(Constant.cancelOrder + orderNum, infoModel, null,
				new CommonCallback<Boolean>() {

					@Override
					public void onCancelled(CancelledException arg0) {

					}

					@Override
					public void onError(Throwable arg0, boolean arg1) {
						Log.v("this", "onError " + arg0.toString());
						RequestErrorUtil.judge(arg0, m_activity);
					}

					@Override
					public void onFinished() {

					}

					@Override
					public void onSuccess(Boolean arg0) {
						Log.v("this", "onSuccess" + arg0);
						finish();
					}
				});
	}
}
