package com.zozmom.ui.user;

import java.util.HashMap;
import java.util.Map;

import com.chasedream.zhumeng.R;
import com.iapppay.interfaces.callback.IPayResultCallback;
import com.iapppay.sdk.main.IAppPay;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.zozmom.constants.Constant;
import com.zozmom.manager.AccountManager;
import com.zozmom.manager.ShareUrlManager;
import com.zozmom.model.ShareUrlModel;
import com.zozmom.model.UserInfoModel;
import com.zozmom.net.XHttp;
import com.zozmom.ui.BaseActivity;
import com.zozmom.ui.BaseTaskActivity;
import com.zozmom.ui.IndexActivity;
import com.zozmom.ui.pay.RechargeActivity;
import com.zozmom.ui.pay.RechargesucessActivity;
import com.zozmom.util.LogUtil;
import com.zozmom.util.RequestErrorUtil;
import com.zozmom.util.ToastUtil;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class FindWebActivity extends BaseTaskActivity<String> {
	String mUrl;
	WebView webview;
	TextView titleView;
	String mTitle;
	ProgressBar progressBar;
	ImageButton shareBtn;
	UserInfoModel infoModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web_activity);
		infoModel = AccountManager.getInstance().getLogginUserInfo();
		Intent mIntent = getIntent();
		mTitle = mIntent.getStringExtra("title");
		mUrl = mIntent.getStringExtra("weburl");
//		mUrl = "http://10.1.24.132/zhumeng/hd/cashback.html";
//		mUrl = "http://10.1.24.132/zhumeng/hd/friends.html";
		
		shareBtn = (ImageButton) findViewById(R.id.status_bar_express);
		titleView = (TextView) findViewById(R.id.status_bar_title);
		titleView.setText(mTitle);
		webview = (WebView) findViewById(R.id.basewebview);
		progressBar = (ProgressBar) findViewById(R.id.webprogressbar);
		initView();
	}

	@SuppressLint("SetJavaScriptEnabled")
	public void initView() {
		titleView.setText(mTitle);
		WebSettings settings = webview.getSettings();
		settings.setJavaScriptEnabled(true);
		if (mUrl.contains("http://")) {
			webview.loadUrl(mUrl);
		} else
			webview.loadUrl("http://" + mUrl);

		settings.setLoadWithOverviewMode(true);
		settings.setUseWideViewPort(true);
		settings.setAppCacheEnabled(false);
		webview.setWebViewClient(new SampleWebViewClient());
		webview.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				progressBar.setProgress(newProgress);
				if (newProgress == 100) {
					progressBar.setVisibility(View.GONE);
				}
			}
		});
		// webview.setOnKeyListener(this);
	}

	public class SampleWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
			Log.v("this", "shouldOverrideKeyEvent");
			return super.shouldOverrideKeyEvent(view, event);
		}

		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Log.v("this", "url" + url);
			view.loadUrl(url);
			return true;
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			Log.v("this", "onReceivedError" + description);
			super.onReceivedError(view, errorCode, description, failingUrl);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			Log.v("this", "onPageFinished" + url);
			String murl1 = "#cz?";
			String murl2 = "#share?";
			String murl3 = "#money?";
			String murl4 = "#home?";
			String murl5 = "#recharge?";
			String murl6 = "#shareDownload?";
			String murl7 = "#hongbao?";
			if (url.contains(murl1)) {
				// weixin.zozmom.com/#cz?id=1&v=S46tN
				Log.v("this", "onPageFinished" + url);
				int start = url.indexOf("id=") + 3;
				String temp = url.substring(start);
				String activityId;
				if (temp.contains("&")) {
					int end = temp.indexOf("&");
					activityId = temp.substring(0, end);
				} else {
					activityId = temp;
				}
				Log.v("this", "activityId" + activityId);
				Intent intent = new Intent(m_activity, RechargeActivity.class);
				intent.putExtra("activityId", activityId);
				((BaseActivity<?>) m_activity).startActivity(intent);
			} else if (url.contains(murl2)) {
				showshareDialog();
			} else if (url.contains(murl3)) {
				Intent intent = new Intent(m_activity, AccountActivity.class);
				((BaseActivity<?>) m_activity).startActivity(intent);
			} else if (url.contains(murl4)) {
				Intent intent = new Intent(m_activity, IndexActivity.class);
				intent.putExtra("data", 10);
				((BaseActivity<?>) m_activity).startActivity(intent);
			} else if (url.contains(murl5)) {
				extracted(url);

			}else if(url.contains(murl6)){
				showshareDownDialog();
				//分享下载连接出去
			}else if(url.contains(murl7)){
				Intent intent=new Intent(m_activity,UserHongbaoActivity.class);
				((BaseActivity<?>) m_activity).startActivity(intent);
			}
			super.onPageFinished(view, url);
		}

		private void extracted(String url) {
			int start = url.indexOf("id=") + 3;
			String temp = url.substring(start);
			String activityId;
			if (temp.contains("&")) {
				int end = temp.indexOf("&");
				activityId = temp.substring(0, end);
			} else {
				activityId = temp;
			}

			int priceStart = url.indexOf("price=") + "price=".length();
			String price = url.substring(priceStart);
			if (price.contains("&")) {
				price = price.substring(0, price.indexOf("&"));
			}
			LogUtil.v("price" + price);
			LogUtil.v("activityId" + activityId);
			coin=Integer.parseInt(price);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("channel", "wx");
			map.put("price", price);
			map.put("activityId", activityId);
			XHttp.PostByToken(Constant.RECHARGE_DREAM_COIN, infoModel, map,
					new CommonCallback<String>() {

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
						public void onSuccess(String arg0) {
							Log.v("this", "arg0" + arg0);
							// Pingpp.createPayment(this, arg0);
							if (arg0.contains("\"")) {
								arg0 = arg0.replace("\"", "");
							}
							IAppPay.startPay(FindWebActivity.this, "transid="
									+ arg0 + "&appid=3005839314", callback);
						}
					});
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			Log.v("this", "onPageStarted" + url);
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onLoadResource(WebView view, String url) {
			Log.v("this", "onLoadResource" + url);
			super.onLoadResource(view, url);
		}
	}
	
	int coin;

	IPayResultCallback callback = new IPayResultCallback() {

		@Override
		public void onPayResult(int arg0, String arg1, String arg2) {
			if (arg0 == IAppPay.PAY_SUCCESS) {
//				Intent intent = new Intent(FindWebActivity.this,
//						RechargesucessActivity.class);
//				intent.putExtra("result", "success");
//				startActivity(intent);
				webview.loadUrl(mUrl);
				ToastUtil.show(FindWebActivity.this, "充值成功,您有一次抽奖机会");
			} else if (arg0 == IAppPay.PAY_ERROR) {
				Intent intent = new Intent(FindWebActivity.this,
						RechargesucessActivity.class);
				intent.putExtra("result", "false");
				intent.putExtra("coin", coin);
				startActivity(intent);
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.status_bar_Exit :
				finish();
				break;
		}
	}

	

	public void setup(View view) {
		showshareDialog();
	}

	AlertDialog shareDialog;
	ShareUrlModel shareurlModel;
	public static String share_murl = "";

	public void showshareDialog() {
		shareurlModel = ShareUrlManager.getInstance().getSharemodel();
		if (shareurlModel != null) {
			share_murl = shareurlModel.getDetailUrl();
		}
		if (shareDialog != null) {
			shareDialog.show();
			return;
		}
		AlertDialog.Builder builder = new Builder(this);
		shareDialog = builder.create();
		shareDialog.show();
		// shareDialog.
		Window window = shareDialog.getWindow();
		shareDialog.setContentView(R.layout.share_hongbao_lay);
		TextView titleView = (TextView) window
				.findViewById(R.id.hongbao_dialog_title);
		titleView.setText("幸运分享");
		ImageView exitview = (ImageView) window
				.findViewById(R.id.share_hongbao_exit);
		ImageButton wechatBtn = (ImageButton) window
				.findViewById(R.id.wechat_hongbaobtn);
		ImageButton wecicleBtn = (ImageButton) window
				.findViewById(R.id.friends_hongbaobtn);
		ImageButton qqBtn = (ImageButton) window
				.findViewById(R.id.qq_hongbaobtn);
		ImageButton kyBtn = (ImageButton) window
				.findViewById(R.id.qq_kj_hongbaobtn);
		kyBtn.setVisibility(View.GONE);
		exitview.setOnClickListener(shareListener);
		wechatBtn.setOnClickListener(shareListener);
		wecicleBtn.setOnClickListener(shareListener);
		qqBtn.setOnClickListener(shareListener);
		kyBtn.setOnClickListener(shareListener);
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.x = 0;
		lp.y = Mheight;
		// lp.alpha=0.7f;
		lp.width = Mwidth;
		window.setAttributes(lp);
		shareDialog.setCanceledOnTouchOutside(true);
	}
	
	
	
	AlertDialog shareDownDialog;
	ShareUrlModel shareurlDownModel;
	public static String sharedown_murl = "";

	public void showshareDownDialog() {
//		shareurlDownModel = ShareUrlManager.getInstance().getSharemodel();
//		if (shareurlModel != null) {
//			share_murl = shareurlModel.getDetailUrl();
//		}
		if (shareDownDialog != null) {
			shareDownDialog.show();
			return;
		}
		AlertDialog.Builder builder = new Builder(this);
		shareDownDialog = builder.create();
		shareDownDialog.show();
		// shareDialog.
		Window window = shareDownDialog.getWindow();
		shareDownDialog.setContentView(R.layout.share_hongbao_lay);
		TextView titleView = (TextView) window
				.findViewById(R.id.hongbao_dialog_title);
		titleView.setText("分享");
		ImageView exitview = (ImageView) window
				.findViewById(R.id.share_hongbao_exit);
		ImageButton wechatBtn = (ImageButton) window
				.findViewById(R.id.wechat_hongbaobtn);
		ImageButton wecicleBtn = (ImageButton) window
				.findViewById(R.id.friends_hongbaobtn);
		ImageButton qqBtn = (ImageButton) window
				.findViewById(R.id.qq_hongbaobtn);
		ImageButton kyBtn = (ImageButton) window
				.findViewById(R.id.qq_kj_hongbaobtn);
		kyBtn.setVisibility(View.GONE);
		exitview.setOnClickListener(shareListener1);
		wechatBtn.setOnClickListener(shareListener1);
		wecicleBtn.setOnClickListener(shareListener1);
		qqBtn.setOnClickListener(shareListener1);
		kyBtn.setOnClickListener(shareListener1);
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.x = 0;
		lp.y = Mheight;
		// lp.alpha=0.7f;
		lp.width = Mwidth;
		window.setAttributes(lp);
		shareDownDialog.setCanceledOnTouchOutside(true);
	}

	
	OnClickListener shareListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			switch (v.getId()) {
				case R.id.share_hongbao_exit :
					shareDialog.dismiss();
					break;
				case R.id.wechat_hongbaobtn :
					shareByT(SHARE_MEDIA.WEIXIN);
					break;
				case R.id.friends_hongbaobtn :
					shareByT(SHARE_MEDIA.WEIXIN_CIRCLE);
					break;
				case R.id.qq_hongbaobtn :
					shareByT(SHARE_MEDIA.QQ);
					break;
				case R.id.qq_kj_hongbaobtn :
					shareByT(SHARE_MEDIA.QZONE);
					break;

				default :
					break;
			}

		}
	};
	
	OnClickListener shareListener1 = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			switch (v.getId()) {
				case R.id.share_hongbao_exit :
					shareDownDialog.dismiss();
					break;
				case R.id.wechat_hongbaobtn :
					shareByT1(SHARE_MEDIA.WEIXIN);
					break;
				case R.id.friends_hongbaobtn :
					shareByT1(SHARE_MEDIA.WEIXIN_CIRCLE);
					break;
				case R.id.qq_hongbaobtn :
					shareByT1(SHARE_MEDIA.QQ);
					break;
				case R.id.qq_kj_hongbaobtn :
					shareByT1(SHARE_MEDIA.QZONE);
					break;
					
				default :
					break;
			}
			
		}
	};

	public void shareByT1(SHARE_MEDIA share_MEDIA) {
		shareDownDialog.dismiss();
//		UMImage image = new UMImage(this, R.drawable.circle_ic);
		UMImage image = new UMImage(this, "http://weixin.zozmom.com/images/hongbao.png") {
			@Override
			public String asUrlImage() {
				return super.asUrlImage();
			}
		};
		new ShareAction(this).setPlatform(share_MEDIA)
		.setCallback(umShareListener1)
		.withText("关系好不好，邀请来逐梦见分晓").withTitle("邀请好友赢奖励")
		.withTargetUrl("http://www.zozmom.com/dowload")
		.withMedia(image).share();
	}
	public void shareByT(SHARE_MEDIA share_MEDIA) {
		shareDialog.dismiss();
		UMImage image = new UMImage(this, R.drawable.circle_ic);
		new ShareAction(this).setPlatform(share_MEDIA)
				.setCallback(umShareListener)
				.withText("我说YOU，你说要，送给你好运就问你要不要！").withTitle("运气必须要，要的就是这个！")
				.withTargetUrl("http://weixin.zozmom.com/#luckyCircle")
				.withMedia(image).share();
	}
	
	UMShareListener umShareListener1 = new UMShareListener() {
		@Override
		public void onResult(SHARE_MEDIA platform) {
			Log.v("this", " 分享成功啦");

		}

		@Override
		public void onError(SHARE_MEDIA platform, Throwable t) {
			Toast.makeText(m_activity, platform + " 分享失败啦", Toast.LENGTH_SHORT)
					.show();
		}

		@Override
		public void onCancel(SHARE_MEDIA platform) {
			Toast.makeText(m_activity, platform + " 分享取消了", Toast.LENGTH_SHORT)
					.show();
		}
	};

	UMShareListener umShareListener = new UMShareListener() {
		@Override
		public void onResult(SHARE_MEDIA platform) {
			Log.v("this", " 分享成功啦");
			addRanTime();

		}

		@Override
		public void onError(SHARE_MEDIA platform, Throwable t) {
			Toast.makeText(m_activity, platform + " 分享失败啦", Toast.LENGTH_SHORT)
					.show();
		}

		@Override
		public void onCancel(SHARE_MEDIA platform) {
			Toast.makeText(m_activity, platform + " 分享取消了", Toast.LENGTH_SHORT)
					.show();
		}
	};
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.v("this", "requestCode :" + requestCode + "resultCode" + resultCode);
		if (data != null) {
			UMShareAPI.get(this)
					.onActivityResult(requestCode, resultCode, data);
		}
	}

	/**
	 * 分享之后增加测试
	 */
	public void addRanTime() {
		XHttp.PostByToken(Constant.USER_SHARE_ADDTIME, infoModel, null,
				new CommonCallback<String>() {

					@Override
					public void onCancelled(CancelledException arg0) {

					}

					@Override
					public void onError(Throwable arg0, boolean arg1) {
						Log.v("this", arg0.toString());
						RequestErrorUtil.judge(arg0, m_activity);
					}

					@Override
					public void onFinished() {

					}

					@Override
					public void onSuccess(String arg0) {
						Log.v("this", arg0 + "");
						if (arg0.equals("true")) {
							ToastUtil.show(m_activity, "分享成功");
							webview.loadUrl(mUrl);
						} else {
							ToastUtil.show(m_activity, "您已经分享过");
						}
					}
				});
	}
}
