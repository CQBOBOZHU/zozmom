package com.zozmom.ui.pay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback.CancelledException;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chasedream.zhumeng.R;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.zozmom.constants.Constant;
import com.zozmom.cuview.CuGridView;
import com.zozmom.manager.ShareUrlManager;
import com.zozmom.model.ShareUrlModel;
import com.zozmom.model.UserInfoModel;
import com.zozmom.net.XHttp;
import com.zozmom.ui.BaseTaskActivity;
import com.zozmom.ui.GoodMessageActivity;
import com.zozmom.ui.IndexActivity;
import com.zozmom.ui.adapter.BaseTaskAdaper;
import com.zozmom.ui.user.UserDreamActivity;
import com.zozmom.util.NetWorkUtil;
import com.zozmom.util.ToastUtil;

/**
 * 支付成功
 * 
 * @author Administrator
 * 
 */
@ContentView(R.layout.pay_sucess_activity)
public class PaysucessActivity extends BaseTaskActivity<JSONObject> {
	@ViewInject(R.id.status_bar_title)
	TextView titleView;
	@ViewInject(R.id.status_bar_closeBtn)
	Button closeBtn;
	@ViewInject(R.id.status_bar_Exit)
	ImageButton exitBtn;
	@ViewInject(R.id.paysucess_cmm_random_view)
	TextView cmmRandomView;
	@ViewInject(R.id.paysucess_cmm_sharebtn)
	Button shareBtn;
	@ViewInject(R.id.paysucess_cmm_notsharebtn)
	Button notshareBtn;
	@ViewInject(R.id.share_layout)
	View shareview;
	@ViewInject(R.id.pay_resultimageview)
	ImageView imageView;
	@ViewInject(R.id.pay_resultview)
	TextView resultView;
	@ViewInject(R.id.userD_btn)
	Button userdreamlistBtn;
	String result;
	UserInfoModel infoModel;
	// int random;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		x.view().inject(this);
		infoModel = getLogginUserinfo();
		closeBtn.setVisibility(View.VISIBLE);
		exitBtn.setVisibility(View.GONE);
		result = getIntent().getStringExtra("result");
		titleView.setText(getRString(R.string.pay_result));
//		opendraw = getRDrawble(R.drawable.take_on);
//		opendraw.setBounds(0, 0, opendraw.getMinimumWidth(),
//				opendraw.getMinimumHeight());
//		closedraw = getRDrawble(R.drawable.take_off);
//		closedraw.setBounds(0, 0, closedraw.getMinimumWidth(),
//				closedraw.getMinimumHeight());
		init(result);
	}

	public void requsetPuduct() {

	}

//	public void closeOrOpen(Button btn, boolean bl) {
//		if (bl) {
//			btn.setText("展开");
//			btn.setCompoundDrawables(opendraw, null, null, null);
//		} else {
//			btn.setText("收起");
//			btn.setCompoundDrawables(closedraw, null, null, null);
//		}
//	}

	List<String> mlist;

	public void init(String str) {
		if (str.equals("success") || str.equals("\"" + "true" + "\"")) {
			imageView.setImageResource(R.drawable.pay_seccess_2);
			resultView.setText("支付成功");
			userdreamlistBtn.setVisibility(View.VISIBLE);
		} else {
			imageView.setImageResource(R.drawable.pay_fail_1);
			resultView.setText("支付失败");
			userdreamlistBtn.setVisibility(View.GONE);
			shareBtn.setVisibility(View.GONE);
			notshareBtn.setVisibility(View.VISIBLE);
			notshareBtn.setText("返回首页");
		}
	}
	/**
	 * 
	 * @param view
	 */
	public void userDlist(View view){
		 Intent intent=new Intent(this,UserDreamActivity.class);
		startActivity(intent);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(this, IndexActivity.class);
			startActivity(intent);
			return true;
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
			case R.id.status_bar_closeBtn :
				Intent inten = new Intent(this, IndexActivity.class);
				startActivity(inten);
				break;
			case R.id.paysucess_cmm_sharebtn :
				if (shareview.getVisibility() == View.VISIBLE) {
					hideListAnimation(shareview);
					shareview.setVisibility(View.GONE);
				} else {
					shareview.setVisibility(View.VISIBLE);
					showListAnimation(shareview);
				}
				break;
			case R.id.paysucess_cmm_notsharebtn :
				Intent intent = new Intent(this, IndexActivity.class);
				startActivity(intent);
				break;
			case R.id.share_hongbao_exit :
				hideListAnimation(shareview);
				shareview.setVisibility(View.GONE);
				break;
			case R.id.wechat_hongbaobtn :
				requestShareId(SHARE_MEDIA.WEIXIN);
				break;
			case R.id.friends_hongbaobtn :
				requestShareId(SHARE_MEDIA.WEIXIN_CIRCLE);
				break;
			case R.id.qq_hongbaobtn :
				requestShareId(SHARE_MEDIA.QQ);
				break;
			case R.id.qq_kj_hongbaobtn :
				requestShareId(SHARE_MEDIA.QZONE);
				break;
			case R.id.weibo_hongbaobtn :
				ToastUtil.show(this, "微博");
				break;
		}
	}

	AlertDialog shareDialog;
	public void showshareDialog() {
		if (shareDialog != null) {
			shareDialog.show();
			return;
		}
		AlertDialog.Builder builder = new Builder(this);
		shareDialog = builder.create();
		shareDialog.show();
		Window window = shareDialog.getWindow();
		shareDialog.setContentView(R.layout.share_hongbao_lay);
		TextView titleView = (TextView) window
				.findViewById(R.id.hongbao_dialog_title);
		titleView.setText("商品分享");
		ImageView exitview = (ImageView) window
				.findViewById(R.id.share_hongbao_exit);
		ImageButton wechatBtn = (ImageButton) window
				.findViewById(R.id.wechat_hongbaobtn);
		ImageButton wecicleBtn = (ImageButton) window
				.findViewById(R.id.friends_hongbaobtn);
		ImageButton qqBtn = (ImageButton) window
				.findViewById(R.id.qq_hongbaobtn);
		ImageButton kyBtn = (ImageButton) window
				.findViewById(R.id.qq_kj_hongbaobtn);
		exitview.setOnClickListener(shareListener);
		wechatBtn.setOnClickListener(shareListener);
		wecicleBtn.setOnClickListener(shareListener);
		qqBtn.setOnClickListener(shareListener);
		kyBtn.setOnClickListener(shareListener);
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.x = 0;
		lp.y = Mheight;
		// lp.alpha=0.7f;
		lp.width = Mwidth;
		window.setAttributes(lp);
		shareDialog.setCanceledOnTouchOutside(true);
	}

	OnClickListener shareListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			switch (v.getId()) {
				case R.id.share_hongbao_exit :

					break;
				case R.id.wechat_hongbaobtn :
						requestShareId(SHARE_MEDIA.WEIXIN);
					break;
				case R.id.friends_hongbaobtn :
						requestShareId(SHARE_MEDIA.WEIXIN_CIRCLE);
					break;
				case R.id.qq_hongbaobtn :
						requestShareId(SHARE_MEDIA.QQ);
					break;
				case R.id.qq_kj_hongbaobtn :
						requestShareId(SHARE_MEDIA.QZONE);
					break;

				default :
					break;
			}

		}
	};

	public void requestShareId(final SHARE_MEDIA share_MEDIA) {
		// Map<String, Object> map = new HashMap<String, Object>();
		// map.put("type", "paySuccess");
		if(!NetWorkUtil.isNetworkAvailable(this)){
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

					}

					@Override
					public void onFinished() {

					}

					@Override
					public void onSuccess(String arg0) {
						Log.v("this", "arg0 :" + arg0);
						try {
							JSONObject jsonObject = new JSONObject(arg0);
							String id = jsonObject.getString("id");
							String time = jsonObject.getString("endTime");
							shareByT(share_MEDIA, id + "," + time);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});
	}

	public void shareByT(SHARE_MEDIA share_MEDIA, String temp) {
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
						Toast.makeText(PaysucessActivity.this,
								platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onError(SHARE_MEDIA platform, Throwable t) {
						Toast.makeText(PaysucessActivity.this,
								platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onCancel(SHARE_MEDIA platform) {
						Toast.makeText(PaysucessActivity.this,
								platform + " 分享取消了", Toast.LENGTH_SHORT).show();
					}
				}).share();
	}

}
