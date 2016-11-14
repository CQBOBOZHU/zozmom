package com.zozmom.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;
import org.xutils.http.RequestParams;

import com.chasedream.zhumeng.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.tencent.open.utils.HttpUtils.MyX509TrustManager;
import com.tencent.open.yyb.ShareModel;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.zozmom.constants.Constant;
import com.zozmom.cuview.CircleImageView;
import com.zozmom.cuview.CuExpandableListview;
import com.zozmom.cuview.CuGridView;
import com.zozmom.manager.ShareUrlManager;
import com.zozmom.model.LuckModel;
import com.zozmom.model.OpenedModel;
import com.zozmom.model.ShareUrlModel;
import com.zozmom.model.UserInfoModel;
import com.zozmom.net.XHttp;
import com.zozmom.ui.adapter.BaseTaskAdaper;
import com.zozmom.ui.adapter.BuyGoodsListAdapter;
import com.zozmom.ui.adapter.NewGoodExAdapter;
import com.zozmom.ui.user.OtherUserCenterActivity;
import com.zozmom.util.JsonUtil;
import com.zozmom.util.NetWorkUtil;
import com.zozmom.util.RequestErrorUtil;
import com.zozmom.util.ToastUtil;
import com.zozmom.util.Tools;
import com.zozmom.util.Util;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 商品详情(揭晓的商品详情)
 * 
 * @author Administrator
 * @param <T>
 * 
 */
public class OpeningGoodMessageActivity extends BaseTaskActivity<Object> {
	PullToRefreshScrollView scrollView;
	ImageView goodImageView;
	TextView goodNameView;
	TextView userNameView;
	TextView useradressView;
	TextView useradressIpView;
	TextView userIdView;
	TextView goodNumbView;// 商品期号
	TextView joinTimeView;// 参与次数
	TextView openTimeView;// 揭晓时间
	TextView luckNumView;// 幸运逐梦码
	TextView issueIdView;// 商品期号
	Button buyBtn;
	ImageView disView;
	TextView titlView;
	CuExpandableListview cuExpandableListview;
	Button openBtn;
	View userlay;
	View showlay;
	View pastlay;
	CuGridView cuGridView;
	View isuserloginView;
	CircleImageView userfaceimageView;

	Button nextBtn;// 下一页
	Button beforeBtn;// 上一页

	private int productId;
	private int issueId;
	private int productArea;
	private String productName;
	private String productTitle;
	private String showImage;
	private int status;

	OpenedModel openedmodel;
	UserInfoModel infoModel;
	ImageButton shareImageBtn;
	public static final int maxSize = 10;
	private int pageNum = 0;// 当前参与的pageNum
	boolean isFresh = true;// 表示下拉表示true 上啦 false

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.opening_goodsmessage_activity);
		Intent intent = getIntent();
		productId = intent.getIntExtra("productId", 1);
		issueId = intent.getIntExtra("issueId", 1);
		status = intent.getIntExtra("status", 0);
		initView();
		requestProduct();
		requestJionUser();
	}

	public void initView() {
		infoModel = getLogginUserinfo();
		titlView = (TextView) findViewById(R.id.status_bar_title);
		titlView.setText("商品详情");
		shareImageBtn = (ImageButton) findViewById(R.id.status_share);
		scrollView = (PullToRefreshScrollView) findViewById(R.id.goodscrollview);
		scrollView.setMode(Mode.BOTH);
		scrollView.getRefreshableView().smoothScrollTo(0, 0);
		scrollView.setOnRefreshListener(refreshListener);
		userfaceimageView = (CircleImageView) findViewById(R.id.user_face_view);
		buyBtn = (Button) findViewById(R.id.opening_goodmessage_todreambtn);
		extracted();
		JudeIsLogin();
		cuExpandableListview = (CuExpandableListview) findViewById(R.id.opengood_listview);
		cuExpandableListview.setGroupIndicator(null);
		nextBtn = (Button) findViewById(R.id.lucknum_after_btn);
		beforeBtn = (Button) findViewById(R.id.lucknum_before_btn);
		if (status == 1) {
			buyBtn.setBackgroundResource(R.drawable.btn_background_gray);
			buyBtn.setClickable(false);
		} else {
			buyBtn.setBackgroundResource(R.drawable.btn_background_yellow);
			buyBtn.setClickable(true);
		}

	}

	OnRefreshListener2<ScrollView> refreshListener = new OnRefreshListener2<ScrollView>() {

		@Override
		public void onPullDownToRefresh(
				PullToRefreshBase<ScrollView> refreshView) {
			isFresh = true;
			requestProduct();
			if (infoModel != null) {
				selectUserjion();
			}
			requestJionUser();
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
			// recordId
			isFresh = false;
			requestJionUser();
		}
	};

	private void JudeIsLogin() {
		if (infoModel == null) {
			openBtn.setVisibility(View.GONE);
			isuserloginView.setVisibility(View.VISIBLE);
		} else {
			openBtn.setVisibility(View.VISIBLE);
			openBtn.setClickable(false);
			isuserloginView.setVisibility(View.GONE);
			selectUserjion();
		}
	}

	private void extracted() {
		disView = (ImageView) findViewById(R.id.opengood_disimageview);
		issueIdView = (TextView) findViewById(R.id.qihao_id_view1);
		issueIdView.setText(String.valueOf(issueId));
		goodImageView = (ImageView) findViewById(R.id.good_imageview);
		goodImageView.setBackgroundResource(R.drawable.pic_loading);
		goodNameView = (TextView) findViewById(R.id.g_name_textview);
		userNameView = (TextView) findViewById(R.id.user_name_view1);
		useradressView = (TextView) findViewById(R.id.location_textview);
		useradressIpView = (TextView) findViewById(R.id.locationip_textview);
		userIdView = (TextView) findViewById(R.id.user_id_view);
		goodNumbView = (TextView) findViewById(R.id.qihao_id_view);
		joinTimeView = (TextView) findViewById(R.id.jion_times_view);
		openTimeView = (TextView) findViewById(R.id.open_time_view);
		luckNumView = (TextView) findViewById(R.id.luck_textview);
		openBtn = (Button) findViewById(R.id.opengoodmessage_user_btn);
		userlay = findViewById(R.id.goodmessage_rlayout);
		cuGridView = (CuGridView) findViewById(R.id.lucknum_gridview);
		isuserloginView = findViewById(R.id.opening_goodmessage_userlogin_layout);
		showlay = findViewById(R.id.open_show_view);
		pastlay = findViewById(R.id.open_past_lay);
		showlay.setOnClickListener(this);
		pastlay.setOnClickListener(this);
	}
	int productPrice;
	public void showView() {
		shareImageBtn.setVisibility(View.VISIBLE);
		showImage = openedmodel.getProductImg();
		productPrice = openedmodel.getProductPrice();
		productTitle = openedmodel.getProductTitle();
		productName = openedmodel.getProductName();
		productArea = openedmodel.getProductArea();
		status = openedmodel.getStatus();
		if (status == 0) {
			buyBtn.setBackgroundResource(R.drawable.btn_background_gray);
			buyBtn.setText("商品已下架");
			buyBtn.setClickable(false);
		} else {
			buyBtn.setBackgroundResource(R.drawable.btn_background_yellow);
			buyBtn.setClickable(true);
		}
		if (productArea == 10) {
			disView.setVisibility(View.VISIBLE);
			disView.setImageResource(R.drawable.ten_yuan);
		} else if ((productArea == 100)) {
			disView.setVisibility(View.VISIBLE);
			disView.setImageResource(R.drawable.hundred_yuan);
		}

		String username = openedmodel.getUserName();
		String loaction = openedmodel.getLocation();
		String adress = loaction.split("IP:")[0];
		String IP = loaction.split("IP:")[1];
		int userid = openedmodel.getUserId();
		int buyCount = openedmodel.getUserBuyCount();
		long announcedTime = openedmodel.getAnnouncedTime();
		String luckNum = openedmodel.getLuckyNumber();
		String userFace = openedmodel.getUserFace();
		int luckn = Integer.parseInt(luckNum);
		int luck = luckn + 10000000;
		loadimage(goodImageView, Constant.PRODUCT_URL + showImage
				+ Constant.PRODUCT_KEY);
		loadimage(userfaceimageView, Constant.FACE_URL + userFace
				+ Constant.FACE_KEY);
		goodNameView.setText(productName + productTitle);
		userNameView.setText(username);
		useradressView.setText(adress);
		useradressIpView.setText(IP);
		userIdView.setText(String.valueOf(userid));
		goodNumbView.setText(String.valueOf(issueId));
		joinTimeView.setText(String.valueOf(buyCount));
		String date = Util.GetDataTime1(announcedTime);
		openTimeView.setText(date);
		dataluckNum = luck;
		luckNumView.setText("幸运逐梦码:" + luck);
	}

	int dataluckNum = 0;

	private void requestProduct() {
		if (!NetWorkUtil.isNetworkAvailable(this)) {
			if (isFresh) {
				scrollView.postDelayed(new Runnable() {
					@Override
					public void run() {
						scrollView.onRefreshComplete();
						ToastUtil.show(m_activity,
								getRString(R.string.network_hint));
					}
				}, 300);
			}
			return;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("productId", productId);
		map.put("issueId", issueId);
		XHttp.Post(Constant.PRODUCT_MESSAGE, map,
				new CommonCallback<JSONObject>() {

					@Override
					public void onCancelled(CancelledException arg0) {

					}

					@Override
					public void onError(Throwable arg0, boolean arg1) {
						RequestErrorUtil.judge(arg0, m_activity);
						scrollView.onRefreshComplete();
					}

					@Override
					public void onFinished() {

					}

					@Override
					public void onSuccess(JSONObject arg0) {
						scrollView.onRefreshComplete();
						Log.v("this", "onSuccess" + arg0.toString());
						try {
							openedmodel = (OpenedModel) JsonUtil.JsonToModel(
									arg0, OpenedModel.class);
							showView();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
	}

	/**
	 * 获取参加的人的次数和幸运码
	 */
	private void requestJionUser() {
		if (!NetWorkUtil.isNetworkAvailable(this)) {
			if (!isFresh) {
				scrollView.postDelayed(new Runnable() {
					@Override
					public void run() {
						scrollView.onRefreshComplete();
						ToastUtil.show(m_activity, "网络错误");
					}
				}, 300);
			}
			return;
		}

		RequestParams params = new RequestParams(Constant.PRODUCT_RECORD);
		if (!isFresh) {
			int size = mluckModels.size();
			if (pageNum != 0) {
				if (size < maxSize * pageNum) {
					scrollView.postDelayed(new Runnable() {
						@Override
						public void run() {
							scrollView.onRefreshComplete();
							ToastUtil.show(m_activity, "没有更多数据");
						}
					}, 300);
					return;
				} else {
					params.addParameter("recordId", mluckModels.get(size - 1)
							.getId());
				}
			}
		}
		params.addParameter("issueId", issueId);
		x.http().post(params, new CommonCallback<JSONArray>() {

			@Override
			public void onCancelled(CancelledException arg0) {

			}

			@Override
			public void onError(Throwable arg0, boolean arg1) {
				scrollView.onRefreshComplete();

				if (arg0.toString().equals("java.io.EOFException")) {
					ToastUtil.show(m_activity, "加载失败");
					return;
				}
				RequestErrorUtil.judge(arg0, OpeningGoodMessageActivity.this);
			}

			@Override
			public void onFinished() {

			}

			@Override
			public void onSuccess(JSONArray arg0) {
				Log.v(TAG, arg0.toString());
				scrollView.onRefreshComplete();
				List<LuckModel> luckModels = JsonUtil.JsonToModel(arg0,
						LuckModel.class);
				Log.v(TAG, "luckModels" + luckModels.size());
				if (isFresh) {
					if (luckModels != null && luckModels.size() > 0) {
						pageNum = 1;
						mluckModels = luckModels;
						parseChildLuckNum(mluckModels);
						exAdapter = new NewGoodExAdapter(luckModels,
								m_activity, childmap, cuExpandableListview);
						cuExpandableListview.setAdapter(exAdapter);
					}
				} else {
					if (luckModels != null && luckModels.size() > 0) {
						pageNum++;
						mluckModels.addAll(luckModels);
						parseChildLuckNum(mluckModels);
						exAdapter.setList(mluckModels);
						exAdapter.setAllchildemap(childmap);
						exAdapter.notifyDataSetChanged();
					}
				}
			}
		});
	}
	NewGoodExAdapter exAdapter;
	List<LuckModel> mluckModels = new ArrayList<LuckModel>();
	@SuppressLint("UseSparseArrays")
	Map<Integer, List<String>> childmap = new HashMap<Integer, List<String>>();

	public void parseChildLuckNum(List<LuckModel> luckModels) {
		if (luckModels != null) {
			List<String> list;
			int index = 0;
			for (LuckModel luckModel : luckModels) {
				list = new ArrayList<String>();
				String luckModelStrin = luckModel.getDreamNum();
				String[] luck = luckModelStrin.split(",");
				for (String string : luck) {
					list.add(string);
				}
				childmap.put(index, list);
				index++;
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.status_bar_Exit :
				finish();
				break;
			case R.id.open_show_view :
				toShow();
				break;
			case R.id.open_past_lay :
				toPast();
				break;

		}
	}

	/**
	 * 查看商品介绍
	 * 
	 * @param view
	 */
	public void openMessage(View view) {
		Intent intent = new Intent(this, BaseWebActivity.class);
		intent.putExtra("type", 1);
		intent.putExtra("weburl", Constant.product_mess + productId
				+ "&type=app");
		intent.putExtra("title", "商品详情");
		startActivity(intent);
	}

	/**
	 * 查看计算
	 * 
	 * @param view
	 */
	public void toReckon(View view) {
		Intent intent = new Intent(this, ReckonActivity.class);
		intent.putExtra("productId", productId);
		intent.putExtra("issueId", issueId);
		intent.putExtra("allcount", productPrice);
		if (dataluckNum != 0) {
			intent.putExtra("luckNum", dataluckNum);
			startActivity(intent);
		}
	}

	/**
	 * 查看他人的个人中心
	 * 
	 * @param view
	 */
	public void toSeeOtherUser(View view) {
		Intent intent = new Intent(this, OtherUserCenterActivity.class);
		if (openedmodel == null) {
			return;
		}
		intent.putExtra("userId", openedmodel.getUserId());
		startActivity(intent);
	}

	/**
	 * 跳转到登录界面
	 */
	public void login(View view) {
		Intent intent = new Intent(this, LoginActivity.class);
		startActivityForResult(intent, 0);
	}

	/**
	 * 立即逐梦
	 * 
	 * @param view
	 */
	public void toDream(View view) {
		Intent intent = new Intent(this, SubmitActivity.class);
		intent.putExtra("productId", productId);
		startActivity(intent);
	}

	/**
	 * 查看逐梦码
	 * 
	 * @param view
	 */
	public void rquestLuck(View view) {
		if (userlay.getVisibility() == View.VISIBLE) {
			userlay.setVisibility(View.GONE);
		} else {
			if (adapter2 != null) {
				userlay.setVisibility(View.VISIBLE);
			} else {
				adapter2 = new gridAdapter(m_activity, Tools.getList(
						userNumlist, 0, 9));
				cuGridView.setAdapter(adapter2);
				openBtn.setClickable(true);
			}
		}

	}

	int page = 1;
	List<String> userNumlist = new ArrayList<String>();

	gridAdapter adapter2;

	public void nextPage(View view) {
		if (userNumlist.size() > page * 9) {
			List<String> li = Tools.getList(userNumlist, page * 9,
					(page + 1) * 9);
			adapter2.setmDatas(li);
			adapter2.notifyDataSetChanged();
			if (userNumlist.size() - page * 9 < 9) {
				changeBtnTextColor(1, false);
			}
			page++;
			if (page > 1) {
				changeBtnTextColor(0, true);
			}
		}
	}

	public void beforePage(View view) {
		if (page > 1) {
			List<String> li = Tools.getList(userNumlist, (page - 1) * 9,
					(page - 2) * 9);
			adapter2.setmDatas(li);
			adapter2.notifyDataSetChanged();
			page--;
			if (page == 1) {
				changeBtnTextColor(0, false);
			}
			if (userNumlist.size() - (page - 1) * 9 > 9) {
				changeBtnTextColor(1, true);
			}
		}
	}

	class gridAdapter extends BaseTaskAdaper<String> {

		public gridAdapter(Context context, List<String> mDatas) {
			super(context, mDatas);
		}

		@SuppressLint({"NewApi", "ViewHolder", "InflateParams"})
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = mInflater.inflate(R.layout.list_item, null);
			convertView.setBackgroundResource(R.drawable.item_selector);
			TextView textView = (TextView) convertView
					.findViewById(R.id.item_id);
			textView.setClickable(false);
			textView.setText(mDatas.get(position).toString() + "");
			Log.v("this", "convertView " + position);
			return convertView;
		}
	}

	/**
	 * id 0 : 上一页 1: 下一页 bl true: 可点击 false: 不可点击
	 * 
	 * @param id
	 * @param bl
	 */
	public void changeBtnTextColor(int id, boolean bl) {
		switch (id) {
			case 0 :
				if (bl) {
					beforeBtn
							.setTextColor(getRColor(R.color.good_message_textcolors));
					beforeBtn.setClickable(true);
				} else {
					beforeBtn.setTextColor(getRColor(R.color.before_btncolor));
					beforeBtn.setClickable(false);
				}
				break;
			case 1 :
				if (bl) {
					nextBtn.setTextColor(getRColor(R.color.good_message_textcolors));
					nextBtn.setClickable(true);
				} else {
					nextBtn.setTextColor(getRColor(R.color.before_btncolor));
					nextBtn.setClickable(false);
				}
				break;

			default :
				break;
		}
	}

	public void share(View view) {
		if (infoModel == null) {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivityForResult(intent, 0);
		} else
			showshareDialog();
	}

	/**
	 * 往期揭晓
	 * 
	 * @param view
	 */
	private void toPast() {
		Intent intent = new Intent(this, PastActivity.class);
		intent.putExtra("productId", productId);
		startActivity(intent);
	}

	/**
	 * 跳转到晒单
	 */
	private void toShow() {
		Intent intent = new Intent(this, ShowProductActivity.class);
		intent.putExtra("productId", productId);
		startActivity(intent);
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg1 == LoginActivity.resultCode) {
			infoModel = getLogginUserinfo();
			JudeIsLogin();
		}
	}
	/**
	 * 获取个人参与的次数
	 */
	private void selectUserjion() {
		if (!NetWorkUtil.isNetworkAvailable(this)) {
			ToastUtil.show(this, "网络错误");
			return;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		int userId = infoModel.getUserId();
		map.put("issueId", issueId);
		map.put("userId", userId);
		XHttp.Post(Constant.SELECT_USER_JION, map,
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
						Log.v("this", "onSuccess USER" + arg0.toString());
						openBtn.setVisibility(View.VISIBLE);
						if (arg0.toString().equals("[]")) {
							openBtn.setClickable(false);
							return;
						}
						try {
							int buyCount = arg0.getInt("buyCount");
							openBtn.setClickable(true);
							openBtn.setText("我参与了" + buyCount + "次");
							String dreamNum = arg0.getString("dreamNum");
							String[] dreamNums = dreamNum.split(",");
							userNumlist = new ArrayList<String>();
							for (String str : dreamNums) {
								int a = Integer.parseInt(str.trim());
								int luck = 10000000 + a;
								userNumlist.add(String.valueOf(luck));
							}
							if (userNumlist.size() > 9) {
								changeBtnTextColor(1, true);
							}
							page = 1;
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
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
					shareByT(SHARE_MEDIA.WEIXIN);
					break;
				case R.id.friends_hongbaobtn :
					shareByT(SHARE_MEDIA.WEIXIN_CIRCLE);
					break;
				case R.id.qq_hongbaobtn :
					shareByT(SHARE_MEDIA.QQ);
					break;
				case R.id.qq_kj_hongbaobtn :
					shareByT(SHARE_MEDIA.QZONE);
					break;

				default :
					break;
			}

		}
	};

	public static String share_murl = "";

	public void shareByT(SHARE_MEDIA share_MEDIA) {
		shareDialog.dismiss();
		UMImage image = new UMImage(this, Constant.PRODUCT_URL + showImage
				+ Constant.PRODUCT_KEY) {
			@Override
			public String asUrlImage() {
				return super.asUrlImage();
			}
		};
		new ShareAction(this).setPlatform(share_MEDIA)

		.withText(productName + productTitle).withTitle("梦想是要有的，万一实现了呢！")
				.withTargetUrl(Constant.share_product + productId)
				.withMedia(image).setListenerList(new UMShareListener() {
					@Override
					public void onResult(SHARE_MEDIA platform) {
						Toast.makeText(OpeningGoodMessageActivity.this,
								platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onError(SHARE_MEDIA platform, Throwable t) {
						Toast.makeText(OpeningGoodMessageActivity.this,
								platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onCancel(SHARE_MEDIA platform) {
						Toast.makeText(OpeningGoodMessageActivity.this,
								platform + " 分享取消了", Toast.LENGTH_SHORT).show();
					}
				}).share();
	}
}
