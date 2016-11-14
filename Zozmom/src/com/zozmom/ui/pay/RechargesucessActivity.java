package com.zozmom.ui.pay;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.chasedream.zhumeng.R;
import com.tencent.connect.UserInfo;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.utils.Log;
import com.zozmom.constants.Constant;
import com.zozmom.model.UserInfoModel;
import com.zozmom.net.XHttp;
import com.zozmom.ui.BaseTaskActivity;
import com.zozmom.ui.IndexActivity;
import com.zozmom.ui.OpeningGoodMessageActivity;
import com.zozmom.ui.fragment.MeFragment;
import com.zozmom.util.NetWorkUtil;
import com.zozmom.util.ToastUtil;

/**
 * 充值成功页面
 * 
 * @author Administrator
 * 
 */
@ContentView(R.layout.recharge_sucess_activity)
public class RechargesucessActivity extends BaseTaskActivity<JSONObject> {
	@ViewInject(R.id.status_bar_title)
	TextView titleView;
	@ViewInject(R.id.status_bar_closeBtn)
	Button closeBtn;
	@ViewInject(R.id.status_bar_Exit)
	ImageButton exitBtn;
	@ViewInject(R.id.recharge_sucess_textview)
	TextView sucessView;
	@ViewInject(R.id.recharge_sucess_ingbutton)
	Button ingBtn;
	@ViewInject(R.id.recharge_sucess_sharehongbao_button)
	Button shareBtn;
	@ViewInject(R.id.recharge_sucess_notsharehongbao_button)
	Button notshareBtn;
	@ViewInject(R.id.share_layout)
	View shareView;
	UserInfoModel infoModel;
	String result;
	int coin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		x.view().inject(this);
		infoModel = getLogginUserinfo();
		result = getIntent().getStringExtra("result");
		coin = getIntent().getIntExtra("coin", 0);
		exitBtn.setVisibility(View.GONE);
		closeBtn.setVisibility(View.VISIBLE);
		titleView.setText(getRString(R.string.recharge_resulte));
		init(result);
	}

	public void init(String str) {
		if (str.equals("success")) {
			sucessView.setText("您已成功兑换" + coin + "个逐梦币!!");
			if(MeFragment.meFragment!=null){
				MeFragment.meFragment.setCoinView(coin);
			}
		} else {
			sucessView.setText("充值失败");
			shareBtn.setVisibility(View.GONE);
			notshareBtn.setVisibility(View.GONE);
		}
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		Intent intent;
		switch (v.getId()) {
			case R.id.status_bar_closeBtn :
				intent = new Intent(this, IndexActivity.class);
				intent.putExtra("data", 10);
				startActivity(intent);
				break;
			case R.id.recharge_sucess_ingbutton :
				intent = new Intent(this, IndexActivity.class);
				intent.putExtra("data", 10);
				startActivity(intent);
				break;
			case R.id.recharge_sucess_sharehongbao_button :
				if (shareView.getVisibility() == View.VISIBLE) {
					hideListAnimation(shareView);
					shareView.setVisibility(View.GONE);
				} else {
					shareView.setVisibility(View.VISIBLE);
					showListAnimation(shareView);
				}
				break;
			case R.id.recharge_sucess_notsharehongbao_button :
				break;
			case R.id.share_hongbao_exit :
				hideListAnimation(shareView);
				shareView.setVisibility(View.GONE);
				break;
			case R.id.wechat_hongbaobtn :
				requesShare(SHARE_MEDIA.WEIXIN);
				break;
			case R.id.friends_hongbaobtn :
				requesShare(SHARE_MEDIA.WEIXIN_CIRCLE);
				break;
			case R.id.qq_hongbaobtn :
				requesShare(SHARE_MEDIA.QQ);
				break;
			case R.id.qq_kj_hongbaobtn :
				requesShare(SHARE_MEDIA.QZONE);
				break;
		}
	}

	public void requesShare(final SHARE_MEDIA share_MEDIA) {
		if (!NetWorkUtil.isNetworkAvailable(this)) {
			ToastUtil.show(this, "网络错误");
			return;
		}
		XHttp.PostByToken(Constant.zm_shareBonus + "paySuccess", infoModel,
				null, new CommonCallback<String>() {

					@Override
					public void onCancelled(CancelledException arg0) {

					}

					@Override
					public void onError(Throwable arg0, boolean arg1) {
						Log.v("this", "arg0 " + arg0.toString());
					}

					@Override
					public void onFinished() {

					}

					@Override
					public void onSuccess(String arg0) {
						Log.v("this", "arg0 " + arg0);
						try {
							JSONObject jsonObject = new JSONObject(arg0);
							String id = jsonObject.getString("id");
							String time = jsonObject.getString("endTime");
							share(share_MEDIA, id + "," + time);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
	}

	public void share(SHARE_MEDIA share_MEDIA, String temp) {
		UMImage image = new UMImage(this,R.drawable.share_hongbao);

		new ShareAction(this)
				.setPlatform(share_MEDIA)

				.withText("谁说梦想遥不可及，谁说实现遥遥无期，只要这里有的都可以让你实现。送你一颗梦想的种子，立即开始追逐梦想吧！")
				.withTitle("送你一个梦想红包！赶紧领红包去实现你的梦想！")
				.withTargetUrl(
						Constant.SHARES_LUCKYDRRAW + "?key=" + temp
								+ "&type=app").withMedia(image)
				.setListenerList(new UMShareListener() {
					@Override
					public void onResult(SHARE_MEDIA platform) {
						Toast.makeText(RechargesucessActivity.this,
								platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onError(SHARE_MEDIA platform, Throwable t) {
						Toast.makeText(RechargesucessActivity.this,
								platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onCancel(SHARE_MEDIA platform) {
						Toast.makeText(RechargesucessActivity.this,
								platform + " 分享取消了", Toast.LENGTH_SHORT).show();
					}
				}).share();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(this, IndexActivity.class);
			intent.putExtra("data", 10);
			startActivity(intent);
			return true;
		}
		return false;
	}
}
