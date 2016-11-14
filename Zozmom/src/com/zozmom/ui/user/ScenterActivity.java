package com.zozmom.ui.user;

import org.json.JSONObject;
import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.chasedream.zhumeng.R;
import com.zozmom.ui.BaseTaskActivity;
import com.zozmom.ui.BaseWebActivity.SampleWebViewClient;

/**
 * 客服中心
 * 
 * @author Administrator
 * 
 */
@ContentView(R.layout.scenter_activity)
public class ScenterActivity extends BaseTaskActivity<JSONObject> implements OnKeyListener {
	@ViewInject(R.id.status_bar_title)
	TextView titlView;
	@ViewInject(R.id.scenter_activity_webview)
	WebView webView;
	String url = "http://weixin.zozmom.com/#serviceCenter?type=app";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		x.view().inject(this);
		titlView.setText("帮助中心");
		webView.loadUrl(url);
		WebSettings wset = webView.getSettings();
		wset.setJavaScriptEnabled(true);
		wset.setLoadWithOverviewMode(true);
		wset.setUseWideViewPort(true);
		wset.setAppCacheEnabled(false);
		webView.setWebViewClient(new SampleWebViewClient());
		wset.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		webView.setOnKeyListener(this);
	}
	
	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
				webView.goBack(); // 后退
				return true; // 已处理
			}
		}
		return false;
	}
	
	public class SampleWebViewClient extends WebViewClient {
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Log.v(TAG, "url "+url);
			view.loadUrl(url);
			return true;
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.status_bar_Exit:
			finish();
			break;

		default:
			break;
		}
	}
}
