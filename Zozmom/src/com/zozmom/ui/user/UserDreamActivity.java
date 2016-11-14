package com.zozmom.ui.user;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chasedream.zhumeng.R;
import com.zozmom.cuview.CuViewPager;
import com.zozmom.ui.adapter.ImagePagerAdapter;
import com.zozmom.ui.fragment.BaseFragment;
import com.zozmom.ui.fragment.MyFinishDreamFragment;
import com.zozmom.ui.fragment.MyIngDreamFragment;
import com.zozmom.ui.fragment.MyallDreamFragment;

/**
 * 逐梦记录
 * 
 * @author Administrator
 * 
 */
@ContentView(R.layout.userdream_activity)
public class UserDreamActivity extends UserManagerBaseActivity<JSONObject> {
	@ViewInject(R.id.status_bar_title)
	TextView titleView;
	@ViewInject(R.id.user_all_btn)
	Button aBtn;
	@ViewInject(R.id.user_going_btn)
	Button ingBtn;
	@ViewInject(R.id.user_finish_btn)
	Button finishBtn;
	@ViewInject(R.id.status_bar_title)
	TextView titleTextView;
	@ViewInject(R.id.userdream_viewpager)
	CuViewPager viewPager;
	int userId;
//	UserInfoModel infoModel;
	// @ViewInject(R.id.notloginview)
	// View notloginView;
	private List<Button> buttons;
	private Button checkBtn;
//	private static int requestCode = 7;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		x.view().inject(this);
		titleTextView.setText(getRString(R.string.dream_list));
		toDoSomeThing();
	}


	public void initView() {
		super.initView();
		userId = infoModel.getUserId();
		buttons = new ArrayList<Button>();
		buttons.add(aBtn);
		buttons.add(ingBtn);
		buttons.add(finishBtn);
		checkBtn = buttons.get(0);
		viewPager.setOffscreenPageLimit(3);
		List<BaseFragment> fragments = new ArrayList<BaseFragment>();
		fragments.add(new MyallDreamFragment(infoModel));
		fragments.add(new MyIngDreamFragment(infoModel));
		fragments.add(new MyFinishDreamFragment(infoModel));
		ImagePagerAdapter adapter = new ImagePagerAdapter(
				getSupportFragmentManager(), fragments);
		viewPager.setAdapter(adapter);
		viewPager.setOnPageChangeListener(pageChangeListener);
	}

	OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

		@Override
		public void onPageSelected(int arg0) {
			viewPager.setCurrentItem(arg0);
			changeBtn(arg0);
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
		case R.id.status_bar_Exit:
			finish();
			break;
		case R.id.user_all_btn:
			viewPager.setCurrentItem(0);
			break;
		case R.id.user_going_btn:
			viewPager.setCurrentItem(1);
			break;
		case R.id.user_finish_btn:
			viewPager.setCurrentItem(2);
			break;
		default:
			break;
		}
	}

	public void changeBtn(int position) {
		if (checkBtn != null) {
			checkBtn.setTextColor(getResources().getColor(R.color.index_btn));
		}
		Button btn = buttons.get(position);
		btn.setTextColor(getResources().getColor(R.color.background));
		checkBtn = btn;
	}
	
	
}
