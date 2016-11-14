package com.zozmom;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.chasedream.zhumeng.R;
import com.iapppay.sdk.main.IAppPay;
import com.igexin.sdk.PushManager;
import com.sobot.chat.utils.ScreenUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.MobclickAgent.EScenarioType;
import com.umeng.analytics.MobclickAgent.UMAnalyticsConfig;
import com.zozmom.constants.Constant;
import com.zozmom.model.SplashModel;
import com.zozmom.net.XHttp;
import com.zozmom.ui.BaseTaskActivity;
import com.zozmom.ui.DemoActivity;
import com.zozmom.ui.IndexActivity;
import com.zozmom.ui.adapter.ModelPagerAdapter;
import com.zozmom.util.NetWorkUtil;
import com.zozmom.util.UpgradeUtil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.EdgeEffectCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends BaseTaskActivity<Object> {
	private SharedPreferences preference;
	int i = 5;
	ViewPager viewPager;
	View nonetview;

	protected void onCreate(Bundle savedInstanceState) {
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		// 初始化个推
		PushManager.getInstance().initialize(this.getApplicationContext());
		initUmeng();
		initPreference();
		initAippay();
	}

	private void initUmeng() {
//		String channeId = UpgradeUtil.getChannelId(this);
//		UMAnalyticsConfig config = new UMAnalyticsConfig(this,
//				"5726ddac67e58eafab0020de", channeId);
//		MobclickAgent.startWithConfigure(config);
		MobclickAgent.setScenarioType(this, EScenarioType.E_UM_NORMAL);
	}

	private EdgeEffectCompat leftEdge;
	private EdgeEffectCompat rightEdge;
	@SuppressWarnings("deprecation")
	private void initView() {
		nonetview = findViewById(R.id.no_netlay);
		viewPager = (ViewPager) findViewById(R.id.main_viewpager);

		try {
			Field leftEdgeField = viewPager.getClass().getDeclaredField(
					"mLeftEdge");
			Field rightEdgeField = viewPager.getClass().getDeclaredField(
					"mRightEdge");
			if (leftEdgeField != null && rightEdgeField != null) {
				leftEdgeField.setAccessible(true);
				rightEdgeField.setAccessible(true);
				leftEdge = (EdgeEffectCompat) leftEdgeField.get(viewPager);
				rightEdge = (EdgeEffectCompat) rightEdgeField.get(viewPager);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageScrollStateChanged(int arg0) {
				if (rightEdge != null && !rightEdge.isFinished()) {// 到了最后一张并且还继续拖动，出现蓝色限制边条了
					startActivity(new Intent(MainActivity.this,
							IndexActivity.class));
					MainActivity.this.finish();
				}
			}
			@Override
			public void onPageSelected(int arg0) {
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
		});
	}

	boolean BL = true;
	//
	// public void onClick(View v) {
	// if (BL) {
	// BL = false;
	// ToIndexActivity();
	// }
	// }

	public void requesImage() {
		// if (!NetWorkUtil.isNetworkAvailable(this)) {
		// nonetview.setVisibility(View.VISIBLE);
		// viewPager.setVisibility(View.GONE);
		// } else {
		// nonetview.setVisibility(View.GONE);
		// viewPager.setVisibility(View.VISIBLE);
		// initImage();
		// }
	}

	private void initImage() {
		List<SplashModel> urls = new ArrayList<SplashModel>();
		SplashModel model = new SplashModel();
		model.setHeadurl("http://www.pp3.cn/uploads/201509/2015091507.jpg");
		urls.add(model);
		model.setHeadurl("http://g.hiphotos.baidu.com/image/pic/item/1f178a82b9014a90b04cc438ae773912b21beec1.jpg");
		urls.add(model);
		model.setHeadurl("http://img3.imgtn.bdimg.com/it/u=3510442747,3423177120&fm=21&gp=0.jpg");
		urls.add(model);
		viewPager.setAdapter(new ModelPagerAdapter(getSupportFragmentManager(),
				urls));
	}

	Thread thread;

	private void init(final int count) {

		new Handler().postDelayed(new Runnable() {
			public void run() {
				if (BL)
					ToIndexActivity(count);
				BL = false;
			}
		}, 2000);
	}

	/*
	 * 判断程序与第几次运行，如果是第一次运行则跳转到引导页面的方法
	 */
	public void initPreference() {
		// 读取SharedPreferences中需要的数据
		preference = getSharedPreferences("starttimes", MODE_WORLD_READABLE);
		int count = preference.getInt("starttimes", 0);
		if (count == 0) {
			// 做欢迎页面
			init(count);
			// initImage();
		} else {
			init(count);
		}
		Editor editor = preference.edit();
		editor.putInt("starttimes", ++count);// 存入数据
		editor.commit();// 提交修改
	}

	public void ToIndexActivity(int count) {
		Intent intent = new Intent(this, IndexActivity.class);
		intent.putExtra("starttime", count);
		startActivity(intent);
		finish();
	}

	public void requesScreen() {
		XHttp.Post(Constant.SCREEN, null, new CommonCallback<JSONObject>() {

			@Override
			public void onCancelled(CancelledException arg0) {

			}

			@Override
			public void onError(Throwable arg0, boolean arg1) {
				Log.v("this", arg0.toString());

			}

			@Override
			public void onFinished() {

			}

			@Override
			public void onSuccess(JSONObject arg0) {
				Log.v("this", arg0.toString());
			}
		});
	}

	/**
	 * 初始化爱呗支付
	 */
	public void initAippay() {
		// 3005839314
		IAppPay.init(this, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT,
				"3005839314");
	}
}
