package com.zozmom.ui.user;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chasedream.zhumeng.R;
import com.zozmom.constants.Constant;
import com.zozmom.cuview.CircleImageView;
import com.zozmom.model.UserInfoModel;
import com.zozmom.net.XHttp;
import com.zozmom.ui.BaseTaskActivity;
import com.zozmom.ui.adapter.ImagePagerAdapter;
import com.zozmom.ui.fragment.BaseFragment;
import com.zozmom.ui.fragment.UserCtrueFragment;
import com.zozmom.ui.fragment.UserDreamFragment;
import com.zozmom.ui.fragment.UserShowFragment;
import com.zozmom.util.JsonUtil;

/**
 * TA人的个人中心
 * 
 * @author Administrator
 * 
 */
@ContentView(R.layout.otheruser_center_activity)
public class OtherUserCenterActivity extends BaseTaskActivity<Object> {
	@ViewInject(R.id.dreamlist_btn)
	Button dreamlistBtn;
	@ViewInject(R.id.cometrue_btn)
	Button cometrueBtn;
	@ViewInject(R.id.myshowlist_btn)
	Button myshowlistBtn;
	@ViewInject(R.id.user_viewpager)
	ViewPager viewPager;
	@ViewInject(R.id.status_bar_title)
	TextView titleTextView;
	@ViewInject(R.id.user_face_imageview)
	CircleImageView imageView;
	@ViewInject(R.id.other_useridview)
	TextView uIdView;
	@ViewInject(R.id.other_usernameview)
	TextView uNameView;
	int index = 0;
	Button checkBtn;
	private int userId;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		x.view().inject(this);
		userId = getIntent().getIntExtra("userId", 0);
		initView();
		requesUser();
	}

	List<Button> btns;

	public void initView() {
		titleTextView.setText("个人中心");
		List<BaseFragment> mlist = new ArrayList<BaseFragment>();
		btns = new ArrayList<Button>();
		btns.add(dreamlistBtn);
		btns.add(cometrueBtn);
		btns.add(myshowlistBtn);
		checkBtn = btns.get(index);
		mlist.add(new UserDreamFragment(userId));
		mlist.add(new UserCtrueFragment(userId));
		mlist.add(new UserShowFragment(userId));
		ImagePagerAdapter adapter = new ImagePagerAdapter(
				getSupportFragmentManager(), mlist);
		viewPager.setAdapter(adapter);
		viewPager.setOnPageChangeListener(changeListener);
		viewPager.setCurrentItem(index);
		viewPager.setOffscreenPageLimit(2);// 设置viewpager缓存页数
	}

	public void requesUser() {
		String url = Constant.USER_DREAM_LIST + userId;
		XHttp.Post(url, null, new CommonCallback<JSONObject>() {

			@Override
			public void onCancelled(CancelledException arg0) {

			}

			@Override
			public void onError(Throwable arg0, boolean arg1) {
				Log.v(TAG, "onError" + arg0.toString());
			}

			@Override
			public void onFinished() {

			}

			@Override
			public void onSuccess(JSONObject arg0) {
				Log.v(TAG, "onSuccess" + arg0.toString());
				try {
					UserInfoModel infoModel = (UserInfoModel) JsonUtil
							.JsonToModel(arg0, UserInfoModel.class);
					int userId = infoModel.getUserId();
					String userName = infoModel.getUserName();
					uIdView.setText("用户ID: " + userId);
					uNameView.setText("用户昵称: " + userName);
					String url = infoModel.getHeadUrl();
					loadimage(imageView, Constant.FACE_URL + url
							+ Constant.FACE_KEY);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void checkBtn(int position) {
		if (checkBtn != null) {
			checkBtn.setTextColor(getRColor(R.color.index_btn));
		}
		Button btn = btns.get(position);
		btn.setTextColor(getRColor(R.color.background));
		checkBtn = btn;
	}

	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.status_bar_Exit :
				finish();
				break;
		}
	}

	public void onViewClick(View view) {
		switch (view.getId()) {
			case R.id.status_bar_Exit :
				finish();
				break;
			case R.id.dreamlist_btn :
				viewPager.setCurrentItem(0);
				checkBtn(0);
				break;
			case R.id.cometrue_btn :
				viewPager.setCurrentItem(1);
				checkBtn(1);
				break;
			case R.id.myshowlist_btn :
				viewPager.setCurrentItem(2);
				checkBtn(2);
				break;
			default :
				break;
		}
	}

	OnPageChangeListener changeListener = new OnPageChangeListener() {

		@Override
		public void onPageSelected(int arg0) {
			viewPager.setCurrentItem(arg0);
			checkBtn(arg0);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}
	};
}
