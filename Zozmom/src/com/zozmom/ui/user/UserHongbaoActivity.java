package com.zozmom.ui.user;

import java.util.ArrayList;
import java.util.List;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chasedream.zhumeng.R;
import com.zozmom.ui.adapter.ImagePagerAdapter;
import com.zozmom.ui.fragment.BaseFragment;
import com.zozmom.ui.fragment.HongbaoFragment;
import com.zozmom.ui.fragment.UsedHongbaoFragment;

/**
 * 我的红包
 * 
 * @author Administrator
 * 
 */
@ContentView(R.layout.hongbao_activity)
public class UserHongbaoActivity extends UserManagerBaseActivity<Object> {
	@ViewInject(R.id.hongbao_viewpager)
	ViewPager pager;
	@ViewInject(R.id.hongbao_canuserbtn)
	Button canuserBtn;
	@ViewInject(R.id.hongbao_usedbtn)
	Button usedBtn;
	@ViewInject(R.id.status_bar_title)
	TextView titleView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		x.view().inject(this);
		titleView.setText(getRString(R.string.my_hongbao));
		toDoSomeThing();
	}

	public void initView() {
		super.initView();
		List<BaseFragment> mlist = new ArrayList<BaseFragment>();
		mlist.add(new HongbaoFragment());
		mlist.add(new UsedHongbaoFragment());
		pager.setAdapter(new ImagePagerAdapter(getSupportFragmentManager(),
				mlist));
		pager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				pager.setCurrentItem(arg0);
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

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.status_bar_Exit:
			finish();
			break;
		case R.id.hongbao_canuserbtn:
			pager.setCurrentItem(0);
			break;
		case R.id.hongbao_usedbtn:
			pager.setCurrentItem(1);
			break;
		default:
			break;
		}
	}

	public void changeBtn(int bl) {
		if (bl == 1) {
			canuserBtn.setTextColor(getRColor(R.color.index_btn));
			usedBtn.setTextColor(getRColor(R.color.background));
		} else {
			usedBtn.setTextColor(getRColor(R.color.index_btn));
			canuserBtn.setTextColor(getRColor(R.color.background));
		}
	}

}
