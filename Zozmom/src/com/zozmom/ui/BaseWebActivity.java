package com.zozmom.ui;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback.CancelledException;
import org.xutils.common.Callback.CommonCallback;

import com.chasedream.zhumeng.R;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.zozmom.constants.Constant;
import com.zozmom.manager.AccountManager;
import com.zozmom.manager.ShareUrlManager;
import com.zozmom.model.ShareUrlModel;
import com.zozmom.model.UserInfoModel;
import com.zozmom.net.XHttp;
import com.zozmom.util.FileUtil;
import com.zozmom.util.NetWorkUtil;
import com.zozmom.util.ToastUtil;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class BaseWebActivity extends BaseActivity<Object> implements OnKeyListener {
	String mUrl;
	WebView webview;
	TextView titleView;
	String mTitle;
	ProgressBar progressBar;
	ImageButton shareBtn;
	UserInfoModel infoModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web_activity);
		infoModel=AccountManager.getInstance().getLogginUserInfo();
		Intent mIntent = getIntent();
		mTitle = mIntent.getStringExtra("title");
		mUrl = mIntent.getStringExtra("weburl");
		shareBtn=(ImageButton) findViewById(R.id.status_bar_express);
		if(mTitle.equals("赚红包")){
			shareBtn.setVisibility(View.VISIBLE);
			shareBtn.setImageResource(R.drawable.share_msg);
		}
		titleView = (TextView) findViewById(R.id.status_bar_title);
		titleView.setText(mTitle);
		webview = (WebView) findViewById(R.id.basewebview);
		progressBar = (ProgressBar) findViewById(R.id.webprogressbar);
		initView();
	}


	@SuppressLint("SetJavaScriptEnabled")
	public void initView() {
		titleView.setText(mTitle);
		WebSettings settings =	webview.getSettings();
		
		settings.setJavaScriptEnabled(true);
		if (mUrl.contains("http://")) {
			webview.loadUrl(mUrl);
		} else
			webview.loadUrl("http://" + mUrl);
		Log.v("this", "mUrl"+mUrl);
		settings.setLoadWithOverviewMode(true);
		settings.setAppCacheEnabled(false);
		settings.setUseWideViewPort(true);
		webview.setWebViewClient(new SampleWebViewClient());
		webview.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				// TODO Auto-generated method stub
				super.onProgressChanged(view, newProgress);
				progressBar.setProgress(newProgress);
				if (newProgress == 100) {
					progressBar.setVisibility(View.GONE);
				}
			}
		});
		webview.setOnKeyListener(this);
	}

	public class SampleWebViewClient extends WebViewClient {
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			progressBar.setVisibility(View.VISIBLE);
			Log.v(TAG, "url "+url);
			view.loadUrl(url);
			return true;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.status_bar_Exit :
				finish();
				break;
		}
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()) {
				webview.goBack(); // 后退
				return true; // 已处理
			}
		}
		return false;
	}

	
	public void setup(View view){
		showshareDialog();
	}
	
	
	AlertDialog shareDialog;
	ShareUrlModel shareurlModel;
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
		titleView.setText("红包分享");
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

	OnClickListener shareListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			switch (v.getId()) {
				case R.id.share_hongbao_exit :
					shareDialog.dismiss();
					break;
				case R.id.wechat_hongbaobtn :
					requestShareId(SHARE_MEDIA.WEIXIN);
					break;
				case R.id.friends_hongbaobtn :
					requestShareId(SHARE_MEDIA.WEIXIN_CIRCLE);
					break;
				case R.id.qq_hongbaobtn :
					requestShareId(SHARE_MEDIA.QQ);
					break;
				case R.id.qq_kj_hongbaobtn :
					requestShareId(SHARE_MEDIA.QZONE);
					break;

				default :
					break;
			}

		}
	};
	
	public void requestShareId(final SHARE_MEDIA share_MEDIA) {
		// Map<String, Object> map = new HashMap<String, Object>();
		// map.put("type", "paySuccess");
		if(!NetWorkUtil.isNetworkAvailable(this)){
			ToastUtil.show(this, "网络错误");
			return;
		}
		XHttp.PostByToken(Constant.zm_shareBonus + "hongbao", infoModel,
				null, new CommonCallback<String>() {

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
						Log.v("this", "arg0 :" + arg0);
						try {
							JSONObject jsonObject = new JSONObject(arg0);
							String id = jsonObject.getString("id");
							String time = jsonObject.getString("endTime");
							shareByT(share_MEDIA, id + "," + time);
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				});
	}

	
	public static String share_murl = "";

	public void shareByT(SHARE_MEDIA share_MEDIA,String temp) {
		shareDialog.dismiss();
		UMImage image = new UMImage(this,R.drawable.share_hongbao);
			
//		UMImage image = new UMImage(m_activity,R.drawable.app_icon);
		new ShareAction(this).setPlatform(share_MEDIA)
		.withText("我正在逐梦追逐我的梦想！送你梦想的种子可不要浪费哦！").withTitle("Hello,送你一颗梦想的种子，领上红包去逐梦吧！")
				.withTargetUrl(Constant.weixin+"#shareLuckyDraw?key="+temp+"&type=app")
				.withMedia(image).setListenerList(new UMShareListener() {
					@Override
					public void onResult(SHARE_MEDIA platform) {
						Toast.makeText(BaseWebActivity.this,
								platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onError(SHARE_MEDIA platform, Throwable t) {
						Toast.makeText(BaseWebActivity.this,
								platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onCancel(SHARE_MEDIA platform) {
						Toast.makeText(BaseWebActivity.this,
								platform + " 分享取消了", Toast.LENGTH_SHORT).show();
					}
				}).share();
	}
	 private static final String APP_CACAHE_DIRNAME = "/webcache";  
	/** 
     * 清除WebView缓存 
     */  
    public void clearWebViewCache(){  
          
        //清理Webview缓存数据库  
        try {  
            deleteDatabase("webview.db");   
            deleteDatabase("webviewCache.db");  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        //WebView 缓存文件  
        File appCacheDir = new File(getFilesDir().getAbsolutePath()+APP_CACAHE_DIRNAME);  
        Log.e(TAG, "appCacheDir path="+appCacheDir.getAbsolutePath());  
          
        File webviewCacheDir = new File(getCacheDir().getAbsolutePath()+"/webviewCache");  
        Log.e(TAG, "webviewCacheDir path="+webviewCacheDir.getAbsolutePath());  
          
        //删除webview 缓存目录  
        if(webviewCacheDir.exists()){  
           FileUtil.deleteFile(webviewCacheDir);  
        }  
        //删除webview 缓存 缓存目录  
        if(appCacheDir.exists()){  
        	 FileUtil.deleteFile(appCacheDir);  
        }  
    } 
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	clearWebViewCache();
    }
}
