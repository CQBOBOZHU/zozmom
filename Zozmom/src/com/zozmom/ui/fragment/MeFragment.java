package com.zozmom.ui.fragment;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.chasedream.zhumeng.R;
import com.zozmom.constants.Constant;
import com.zozmom.manager.AccountManager;
import com.zozmom.model.UserInfoModel;
import com.zozmom.net.XHttp;
import com.zozmom.util.JsonUtil;
import com.zozmom.util.LogUtil;
import com.zozmom.util.NetWorkUtil;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 我Fragment
 * 
 * @author Administrator
 * 
 */
public class MeFragment extends BaseTaskFragment {
	Button payBtn;
	View rootView;
	ImageView imageView;
	ImageView imageView1;
	UserInfoModel userinfo;
	TextView nameView;
	TextView idView;
	TextView codeView;
	View loginlay;
	View notloginlay;
	public static MeFragment meFragment;
	Button yongjinBtn;
	Button blanceBtn;
	public static final int RATE = 100;

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (rootView == null) {
			LogUtil.v("MeFragment  onCreateView");
			userinfo = AccountManager.getInstance().getCurrentUserInfo();
			rootView = LayoutInflater.from(getActivity()).inflate(
					R.layout.my_dream_fragment, null);
			meFragment = this;
			initView();
		}
		return rootView;
	}

	public void initView() {
		TextView title = (TextView) rootView
				.findViewById(R.id.status_bar_title);
		title.setText(getResources().getString(R.string.my_dream));
		
		
		payBtn = (Button) rootView.findViewById(R.id.mydream_paybtn);
		imageView = (ImageView) rootView
				.findViewById(R.id.userdream_face_imageview);
		imageView1 = (ImageView) rootView
				.findViewById(R.id.user_face_imageview1);
		nameView = (TextView) rootView.findViewById(R.id.user_name_textivew);
		idView = (TextView) rootView.findViewById(R.id.user_id_textiview);
		codeView = (TextView) rootView.findViewById(R.id.user_invite_textview);
		loginlay = rootView.findViewById(R.id.login_lay);
		notloginlay = rootView.findViewById(R.id.not_login_lay);
		yongjinBtn = (Button) rootView.findViewById(R.id.yongjinBtn);
		blanceBtn = (Button) rootView.findViewById(R.id.my_dreamCoinBtn);
		if (userinfo == null) {
			isLogin(false);
		} else{
			isLogin(true);
			init();
		}
	}

	public void init() {
		if (nameView != null && idView != null & userinfo != null) {
			nameView.setText("昵    称:  " + userinfo.getUserName());
			idView.setText("用户ID:  " + userinfo.getUserId());
			codeView.setText("邀请码:  " + userinfo.getInviteCode());
			String url = userinfo.getHeadUrl();
			loadImage(Constant.FACE_URL + url + Constant.FACE_KEY, imageView);
		}
	}
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		LogUtil.v("MeFragment  setUserVisibleHint" + isVisibleToUser);
		userinfo = AccountManager.getInstance().getLogginUserInfo();
		if (isVisibleToUser) {
			init();
		}
	}

	public void request() {
		if (userinfo == null) {
			return;
		}
		if (!NetWorkUtil.isNetworkAvailable(m_activity)) {
			return;
		}
		XHttp.PostByToken(Constant.ownPrivateInfo, userinfo, null,
				new CommonCallback<JSONObject>() {

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
					public void onSuccess(JSONObject arg0) {
						Log.v("this", arg0.toString());
						try {
							UserInfoModel userinfo = (UserInfoModel) JsonUtil
									.JsonToModel(arg0, UserInfoModel.class);
							int balance = userinfo.getBalance();
							int commission = userinfo.getCommission();
							int inviteId = userinfo.getInviteId();
							MeFragment.this.userinfo.setInviteId(inviteId);
							AccountManager.getInstance().setLogginUserInfo(
									MeFragment.this.userinfo);
							blanceBtn.setText("逐梦币:" + balance + "个");
							DecimalFormat df = new DecimalFormat("0.00");
							yongjinBtn.setText("佣金:"
									+ df.format((float) commission / 100) + "元");

							// {"balance":946686,"inviteId":"519","commission":0}

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
	}

	@Override
	public void onResume() {
		super.onResume();
		request();
	}

	public void setViewText(float coin, int dreamcoin) {
		yongjinBtn.setText("佣金:" + coin + "元");
		String blance = blanceBtn.getText().toString().replace("逐梦币:", "")
				.replace("个", "");
		int dreamCoin = Integer.parseInt(blance.trim()) + dreamcoin;
		blanceBtn.setText("逐梦币:" + dreamCoin);
	}

	public void setCoinView(int coin) {
		String dcoin = blanceBtn.getText().toString().replace("逐梦币:", "")
				.replace("个", "");
		int olddcoin = Integer.parseInt(dcoin);
		blanceBtn.setText("逐梦币:" + olddcoin + coin);
	}

	public void changgeView(UserInfoModel model, boolean bl) {
		userinfo = model;
		if (rootView != null) {
			isLogin(bl);
			nameView.setText("昵    称:  " + userinfo.getUserName());
			idView.setText("用户ID:  " + userinfo.getUserId());
			codeView.setText("邀请码:  " + userinfo.getInviteCode());
			String url = userinfo.getHeadUrl();
			request();
			if (url == null || url.length() == 0) {
				imageView.setImageResource(R.drawable.deimage);
			} else {
				loadImage(Constant.FACE_URL + userinfo.getHeadUrl()
						+ Constant.FACE_KEY, imageView);
			}
		}
		request();
	}

	public void isLogin(boolean bl) {
		if (bl) {
			loginlay.setVisibility(View.VISIBLE);
			notloginlay.setVisibility(View.GONE);
		} else {
			userinfo = null;
			loginlay.setVisibility(View.GONE);
			notloginlay.setVisibility(View.VISIBLE);
			yongjinBtn.setText("佣金:");
			blanceBtn.setText("逐梦币:");
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
//		LogUtil.v("MeFragment  onDestroyView");
//		if (rootView != null) {
//			((ViewGroup) rootView.getParent()).removeView(rootView);
//		}
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		LogUtil.v("MeFragment  onDestroy");
		meFragment=null;
	}

}
