package com.zozmom.ui.user;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.chasedream.zhumeng.R;
import com.zozmom.constants.Constant;
import com.zozmom.ui.fragment.ConsumerFragment;
import com.zozmom.ui.fragment.PayFragment;
import com.zozmom.ui.pay.RechargeActivity;
import com.zozmom.util.NetWorkUtil;
import com.zozmom.util.RequestErrorUtil;

/**
 * 消费明细
 * 
 * @author Administrator
 * 
 */
@ContentView(R.layout.account_activity)
public class AccountActivity extends UserManagerBaseActivity<JSONObject> {
	@ViewInject(R.id.status_bar_Exit)
	ImageButton button;
	@ViewInject(R.id.status_bar_title)
	TextView textView;
	@ViewInject(R.id.pay_btn)
	Button paybtn;
	@ViewInject(R.id.consumer_btn)
	Button consumebtn;
	@ViewInject(R.id.account_viewpager)
	ViewPager viewPager;
	@ViewInject(R.id.usergd_view)
	TextView blanceView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		x.view().inject(this);
		textView.setText("账户明细");
		toDoSomeThing();
		if (NetWorkUtil.isNetworkAvailable(this)) {
			requstPostByToken(Constant.ownPrivateInfo, infoModel, null);
		}
	}

	public void toRecharge(View view) {
		Intent intent = new Intent(this, RechargeActivity.class);
		startActivity(intent);
	}

	/**
	 * 获取逐梦币和佣金
	 */
	@Override
	public void onSuccess(JSONObject arg0) {
		super.onSuccess(arg0);
		Log.v("this", "onSuccess :" + arg0.toString());
		try {
//			int commission = arg0.getInt("commission");
			int balance = arg0.getInt("balance");
			blanceView.setText(balance + "￥");
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onError(Throwable arg0, boolean arg1) {
		super.onError(arg0, arg1);
		Log.v("this", "onError :" + arg0.toString());
		RequestErrorUtil.judge(arg0, m_activity);
	}

	@Override
	public void initView() {
		super.initView();
		PayFragment fragment = new PayFragment(infoModel);
		ConsumerFragment fragment2 = new ConsumerFragment(infoModel);
		List<Fragment> list = new ArrayList<Fragment>();
		list.add(fragment);
		list.add(fragment2);
		viewPager.setAdapter(new ViewpagerAdapter(getSupportFragmentManager(),
				list));
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				changeBtn(arg0);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	@Event(value = {R.id.status_bar_Exit, R.id.pay_btn, R.id.consumer_btn}, type = View.OnClickListener.class)
	private void onViewClick(View view) {
		switch (view.getId()) {
			case R.id.status_bar_Exit :
				finish();
				break;
			case R.id.pay_btn :
				viewPager.setCurrentItem(0);
				break;
			case R.id.consumer_btn :
				viewPager.setCurrentItem(1);
				break;
			default :
				break;
		}
	}

	public void changeBtn(int positon) {
		if (positon == 0) {
			paybtn.setTextColor(getRColor(R.color.background));
			consumebtn.setTextColor(getRColor(R.color.index_btn));
		} else {
			consumebtn.setTextColor(getRColor(R.color.background));
			paybtn.setTextColor(getRColor(R.color.index_btn));
		}
	}

	class ViewpagerAdapter extends FragmentPagerAdapter {
		List<Fragment> list;

		public ViewpagerAdapter(FragmentManager fm, List<Fragment> list) {
			super(fm);
			this.list = list;
		}

		@Override
		public Fragment getItem(int arg0) {
			return list.get(arg0);
		}

		@Override
		public int getCount() {
			return list.size();
		}

	}
	
}
