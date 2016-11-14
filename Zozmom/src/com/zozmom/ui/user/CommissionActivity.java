package com.zozmom.ui.user;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chasedream.zhumeng.R;
import com.zozmom.constants.Constant;
import com.zozmom.model.UserInfoModel;
import com.zozmom.net.XHttp;
import com.zozmom.ui.BaseTaskActivity;
import com.zozmom.ui.fragment.CommBeDreamCoin;
import com.zozmom.ui.fragment.CommTixianFragment;
import com.zozmom.util.JsonUtil;
import com.zozmom.util.NetWorkUtil;
import com.zozmom.util.RequestErrorUtil;
/**
 * 佣金
 * 
 * @author Administrator
 * 
 */
public class CommissionActivity extends BaseTaskActivity<String> {
	ViewPager viewPager;
	Button mingxiBtn;
	List<Fragment> mlist;
	Button bedreamBtn;
	Button tixianBtn;
	UserInfoModel userinfo;
	String commission;
	TextView commissonView;
	float allCommCoin;
	public static final int RATE=100;
	public static CommissionActivity commissionActivity;
	
	


	public void setAllCommCoin(int allCommCoin) {
		this.allCommCoin = allCommCoin;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.commission_activity);
		commissionActivity=this;
		userinfo=getLogginUserinfo();
		commission=getIntent().getStringExtra("commcoin");
		if(commission!=null&&commission.trim().length()>0){
			allCommCoin=Float.parseFloat(commission);
		}else{
			allCommCoin=0;
		}
		request();
		TextView titleview = (TextView) findViewById(R.id.status_bar_title);
		titleview.setText("我的佣金");
		mingxiBtn = (Button) findViewById(R.id.status_bar_addbtn);
		mingxiBtn.setText("明细");
		mingxiBtn.setVisibility(View.VISIBLE);
		commissonView=(TextView) findViewById(R.id.commission_view);
		commissonView.setText("￥"+commission);
		mingxiBtn.setTextColor(getRColor(R.color.good_message_textcolors));
		viewPager = (ViewPager) findViewById(R.id.commossion_viewpager);
		bedreamBtn = (Button) findViewById(R.id.bedream_btn);
		tixianBtn = (Button) findViewById(R.id.tixian_btn);
		CommBeDreamCoin beDreamCoin = new CommBeDreamCoin(userinfo,allCommCoin);
		CommTixianFragment fragment = new CommTixianFragment(userinfo,allCommCoin);
		mlist = new ArrayList<Fragment>();
		mlist.add(beDreamCoin);
		mlist.add(fragment);
		viewPager.setAdapter(new FragmentPagerAdapter(
				getSupportFragmentManager()) {

			@Override
			public int getCount() {
				return mlist.size();
			}

			@Override
			public Fragment getItem(int arg0) {
				return mlist.get(arg0);
			}
		});
		viewPager.setOnPageChangeListener(listener);
	}

	private void changBtn(int arg0) {
		if (arg0 == 0) {
			bedreamBtn.setTextColor(getRColor(R.color.background));
			tixianBtn.setTextColor(getRColor(R.color.index_btn));
		} else {
			tixianBtn.setTextColor(getRColor(R.color.background));
			bedreamBtn.setTextColor(getRColor(R.color.index_btn));
		}
	}
	
	public void changeCoin(float coin){
		allCommCoin=coin;
		String cmd=coin+"";
		if(cmd.endsWith(".0")){
			cmd=cmd+"0";
		}
		commissonView.setText("￥"+cmd);
	}
	
	OnPageChangeListener listener = new OnPageChangeListener() {

		@Override
		public void onPageSelected(int arg0) {
			changBtn(arg0);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}
	};

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
			case R.id.status_bar_Exit :
				finish();
				break;
			case R.id.status_bar_addbtn :
				Intent intent = new Intent(this, CommRecordActivity.class);
				startActivity(intent);
				break;
		}
	}

	public void viewpage1(View view) {
		viewPager.setCurrentItem(0);
	}
	public void viewpage2(View view) {
		viewPager.setCurrentItem(1);
	}

	@Override
	public void outLoactionDo() {
		super.outLoactionDo();
	}
	
	public void request() {
		if (!NetWorkUtil.isNetworkAvailable(this)) {
			return;
		}
		if(userinfo==null){
			return;
		}
		XHttp.PostByToken(Constant.ownPrivateInfo, userinfo, null,
				new CommonCallback<JSONObject>() {

					@Override
					public void onCancelled(CancelledException arg0) {

					}

					@Override
					public void onError(Throwable arg0, boolean arg1) {
						RequestErrorUtil.judge(arg0, m_activity);
					}

					@Override
					public void onFinished() {

					}

					@Override
					public void onSuccess(JSONObject arg0) {
						try {
							UserInfoModel userinfo = (UserInfoModel) JsonUtil
									.JsonToModel(arg0, UserInfoModel.class);
							int commi = userinfo.getCommission();
							DecimalFormat df = new DecimalFormat("0.00");//格式化小数，不足的补0
							allCommCoin=Float.parseFloat(df.format((float)commi/100));
//						       String a= df.format((float)amount/100);
//							allCommCoin=commi/RATE;
							commissonView.setText("￥"+df.format((float)commi/100));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		commissionActivity=null;
	}
}
