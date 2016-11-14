package com.zozmom.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback.CancelledException;
import org.xutils.common.Callback.CommonCallback;

import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.chasedream.zhumeng.R;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.zozmom.constants.Constant;
import com.zozmom.cuview.CuGridView;
import com.zozmom.cuview.CuListView;
import com.zozmom.manager.ShareUrlManager;
import com.zozmom.model.ShareUrlModel;
import com.zozmom.model.ShowModel;
import com.zozmom.model.UserInfoModel;
import com.zozmom.net.XHttp;
import com.zozmom.ui.adapter.BaseTaskAdaper;
import com.zozmom.util.JsonUtil;
import com.zozmom.util.RequestErrorUtil;
import com.zozmom.util.Util;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 晒单详情
 * 
 * @author Administrator
 * 
 */
public class ShowActivity extends BaseTaskActivity<JSONObject> {
	CuListView listView;
	PullToRefreshScrollView pullToRefreshScrollView;
	LayoutInflater minflater;
	ScrollView scrollView;
	int shareId;
	TextView showTime, showMessage, showGoodname, showGoodprice,
			showLucknumber, showGettime;
	ShowModel showModel;
	UserInfoModel infoModel;

	private long time;
	private String comment;
	private String productTitle;
	private String productName;
	private int buycount;
	private String lucknum;
	private long announcedTime;
	private String images;
	private int productId;
	private int issueId;
	private int productStatus;
	ImageButton button;
	View togoBtn;
	ImageView showimage1, showimage2, showimage3, showimage4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.showmessage_activity);
		infoModel = getLogginUserinfo();
		showModel = (ShowModel) getIntent().getSerializableExtra("data");
		shareId = showModel.getId();
		productStatus = showModel.getProductStatus();
		minflater = LayoutInflater.from(this);
		initView();
		button = (ImageButton) findViewById(R.id.status_share);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("shareId", shareId);
		requstPost(Constant.SHOW_INFO, map);
	}

	public void initView() {
		togoBtn = findViewById(R.id.showbulay);
		if (productStatus == 0) {
			togoBtn.setVisibility(View.GONE);
		}
		showTime = (TextView) findViewById(R.id.message_timetextview1);
		showMessage = (TextView) findViewById(R.id.show_message_tview1);
		showGoodname = (TextView) findViewById(R.id.show_goodname_textview_title);
		showGoodprice = (TextView) findViewById(R.id.show_addtimes_textview);
		showLucknumber = (TextView) findViewById(R.id.show_number_textview);
		showGettime = (TextView) findViewById(R.id.show_get_time_textview);
		scrollView = (ScrollView) findViewById(R.id.showmessage_scrollview);
		showimage1 = (ImageView) findViewById(R.id.show_image1);
		showimage2 = (ImageView) findViewById(R.id.show_image2);
		showimage3 = (ImageView) findViewById(R.id.show_image3);
		showimage4 = (ImageView) findViewById(R.id.show_image4);
		scrollView.smoothScrollTo(0, 0);
		setText();
	}

	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.status_bar_Exit :
				finish();
				break;
		}
	}

	@Override
	public void onSuccess(JSONObject arg0) {
		super.onSuccess(arg0);
		try {
			showModel = (ShowModel) JsonUtil.JsonToModel(arg0, ShowModel.class);
			again();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 我也试试
	 * 
	 * @param view
	 */
	public void toBuy(View view) {
		Intent intent = new Intent(this, GoodMessageActivity.class);
		intent.putExtra("productId", productId);
		intent.putExtra("issueId", issueId);
		startActivity(intent);
	}

	public void again() {
		buycount = showModel.getBuyCount();
		lucknum = showModel.getLuckyNumber();
		announcedTime = showModel.getAnnouncedTime();
		productName = showModel.getProductName();
		String opentime = Util.GetDataTime1(announcedTime);
		int lnum = 10000000 + Integer.parseInt(lucknum);
		showGettime.setText("揭晓时间: " + opentime);
		showLucknumber.setText("幸运号码: " + lnum);
		showGoodprice.setText("本期参与: " + buycount);
	}

	public void setText() {
		productId = Integer.parseInt(showModel.getProductId());
		time = showModel.getCreateTime();
		comment = showModel.getComment();
		images = showModel.getShareImg();
		productTitle = showModel.getProductName();
		issueId = Integer.parseInt(showModel.getIssueId());
		String data = Util.GetDataTime1(time);
		showGoodname.setText("获得商品:  " + productTitle);
		showTime.setText(data);
		showMessage.setText("  "
				+ comment.replace("<p>", "").replace("</p>", ""));
		String[] image = images.split(",");
		// Constant.SHARE_URL + path + Constant.SHARE_KEY);
		if (image.length == 1) {
			loadimage(showimage1, Constant.SHARE_URL + image[0]
					+ Constant.SHARE_KEY);
		} else if (image.length == 2) {
			showimage2.setVisibility(View.VISIBLE);
			loadimage(showimage1, Constant.SHARE_URL + image[0]
					+ Constant.SHARE_KEY);
			loadimage(showimage2, Constant.SHARE_URL + image[1]
					+ Constant.SHARE_KEY);
		} else if (image.length == 3) {
			showimage2.setVisibility(View.VISIBLE);
			showimage3.setVisibility(View.VISIBLE);
			loadimage(showimage1, Constant.SHARE_URL + image[0]
					+ Constant.SHARE_KEY);
			loadimage(showimage2, Constant.SHARE_URL + image[1]
					+ Constant.SHARE_KEY);
			loadimage(showimage3, Constant.SHARE_URL + image[2]
					+ Constant.SHARE_KEY);
		} else if (image.length == 4 || image.length > 4) {
			showimage2.setVisibility(View.VISIBLE);
			showimage3.setVisibility(View.VISIBLE);
			showimage4.setVisibility(View.VISIBLE);
			loadimage(showimage1, Constant.SHARE_URL + image[0]
					+ Constant.SHARE_KEY);
			loadimage(showimage2, Constant.SHARE_URL + image[1]
					+ Constant.SHARE_KEY);
			loadimage(showimage3, Constant.SHARE_URL + image[2]
					+ Constant.SHARE_KEY);
			loadimage(showimage4, Constant.SHARE_URL + image[3]
					+ Constant.SHARE_KEY);
		}
		// 查看大图
		// showimage.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> parent, View view,
		// int position, long id) {
		// Intent intent = new Intent(ShowActivity.this,
		// ImagePagerActivity.class);
		// intent.putStringArrayListExtra(
		// ImagePagerActivity.EXTRA_IMAGE_URLS,
		// (ArrayList<String>) piclist);
		// intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		// startActivity(intent);
		// }
		// });
	}

	int index = 0;

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		if (arg1 == LoginActivity.resultCode) {
			infoModel = getLogginUserinfo();
		}
	}

	public void share(View view) {
		if (infoModel == null) {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivityForResult(intent, 0);
		} else
			showshareDialog();
	}

	AlertDialog shareDialog;
	ShareUrlModel shareurlModel;
	public void showshareDialog() {
		shareurlModel = ShareUrlManager.getInstance().getSharemodel();
		if (shareurlModel != null) {
			share_murl = shareurlModel.getDetailUrl();
		}
		if (shareDialog != null) {
			shareDialog.show();
			return;
		}
		AlertDialog.Builder builder = new Builder(this);
		shareDialog = builder.create();
		shareDialog.show();
		// shareDialog.
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
					shareDialog.dismiss();
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

	public static String share_murl = "";
	public void requestShareId(final SHARE_MEDIA share_MEDIA) {
		// Map<String, Object> map = new HashMap<String, Object>();
		// map.put("type", "shareProduct");
		XHttp.PostByToken(Constant.zm_shareBonus + "shareDetail", infoModel,
				null, new CommonCallback<String>() {

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
		shareDialog.dismiss();
		UMImage image = new UMImage(this, R.drawable.share_hongbao);
		new ShareAction(this).setPlatform(share_MEDIA)
				.withText("我已经在逐梦上实现梦想。以后请叫我梦想家！")
				.withTitle("一个开始和收获梦想的地方，从这里开始追梦实现愿望，速来围观！")
				.withTargetUrl(Constant.SHARES_LUCKYDRRAW + "?key=" + temp)
				.withMedia(image).setListenerList(new UMShareListener() {
					@Override
					public void onResult(SHARE_MEDIA platform) {
						Toast.makeText(ShowActivity.this, platform + " 分享成功啦",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onError(SHARE_MEDIA platform, Throwable t) {
						Toast.makeText(ShowActivity.this, platform + " 分享失败啦",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onCancel(SHARE_MEDIA platform) {
						Toast.makeText(ShowActivity.this, platform + " 分享取消了",
								Toast.LENGTH_SHORT).show();
					}
				}).share();
	}

}
