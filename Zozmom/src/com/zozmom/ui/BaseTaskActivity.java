package com.zozmom.ui;

import com.zozmom.manager.AccountManager;
import com.zozmom.model.UserInfoModel;
import com.zozmom.pic.PhotoLoader;
import com.zozmom.ui.fragment.MeFragment;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

public class BaseTaskActivity<T> extends BaseActivity<T> {
	PhotoLoader hLoader;
	/**
	 * 加载动画
	 */
	public void showListAnimation(View view) {
		TranslateAnimation ta = new TranslateAnimation(1, 0f, 1, 0f, 1, 1f, 1,
				0f);
		ta.setDuration(200);
		view.startAnimation(ta);
	}

	/**
	 * 隐藏动画，设置透明度
	 */
	public void hideListAnimation(final View view) {
		TranslateAnimation ta = new TranslateAnimation(1, 0f, 1, 0f, 1, 0f, 1,
				1f);
		ta.setDuration(200);
		view.startAnimation(ta);
		ta.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				view.setVisibility(8);
			}
		});
	}

	public void loadimage(ImageView v, String url) {
		if (hLoader == null) {
			hLoader = new PhotoLoader(this);
		}
		hLoader.loadPhoto(url, v);
	}

	Resources m_resource;

	public String getRString(int id) {
		if (m_resource == null) {
			m_resource = getResources();
		}
		return m_resource.getString(id);
	}

	public int getRColor(int id) {
		if (m_resource == null) {
			m_resource = getResources();
		}
		return m_resource.getColor(id);
	}

	public Drawable getRDrawble(int id) {
		if (m_resource == null) {
			m_resource = getResources();
		}
		return m_resource.getDrawable(id);
	}

	public UserInfoModel getLogginUserinfo() {
		return AccountManager.getInstance().getLogginUserInfo();
	}

	public void logout() {
		AccountManager.getInstance().logout();
	}

	public void setLogginUserinfo(UserInfoModel model) {
		AccountManager.getInstance().setLogginUserInfo(model);
	}

	public void updataUserinfo(UserInfoModel model) {
		AccountManager.getInstance().updatUser(model);
	}

	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}

	/**
	 * 点击输入法外,输入框消失
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			View v = getCurrentFocus();
			if (isShouldHideInput(v, ev)) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				if (imm != null) {
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
					outLoactionDo();
				}
			}
			return super.dispatchTouchEvent(ev);
		}
		// 必不可少，否则所有的组件都不会有TouchEvent了
		if (getWindow().superDispatchTouchEvent(ev)) {
			return true;
		}
		return onTouchEvent(ev);
	}

	public void outLoactionDo() {

	}

	public boolean isShouldHideInput(View v, MotionEvent event) {
		if (v != null && (v instanceof EditText)) {
			int[] leftTop = {0, 0};
			// 获取输入框当前的location位置
			v.getLocationInWindow(leftTop);
			int left = leftTop[0];
			int top = leftTop[1];
			int bottom = top + v.getHeight();
			int right = left + v.getWidth();
			if (event.getX() > left && event.getX() < right
					&& event.getY() > top && event.getY() < bottom) {
				// 点击的是输入框区域，保留点击EditText的事件
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
