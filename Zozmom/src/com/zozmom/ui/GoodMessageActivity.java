package com.zozmom.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback.CancelledException;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;

import com.chasedream.zhumeng.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.zozmom.constants.Constant;
import com.zozmom.cuview.CuExpandableListview;
import com.zozmom.cuview.CuGridView;
import com.zozmom.manager.ShareUrlManager;
import com.zozmom.model.LuckModel;
import com.zozmom.model.ProductModel;
import com.zozmom.model.ShareUrlModel;
import com.zozmom.model.UserInfoModel;
import com.zozmom.net.XHttp;
import com.zozmom.ui.adapter.BaseTaskAdaper;
import com.zozmom.ui.adapter.NewGoodExAdapter;
import com.zozmom.ui.user.McActivity;
import com.zozmom.util.JsonUtil;
import com.zozmom.util.NetWorkUtil;
import com.zozmom.util.RequestErrorUtil;
import com.zozmom.util.ToastUtil;
import com.zozmom.util.Tools;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 商品详情(未卖出的)
 * 
 * @author Administrator
 * @param <T>
 * 
 */
public class GoodMessageActivity extends BaseTaskActivity<Object> {
	PullToRefreshScrollView scrollView;
	CuExpandableListview listView;
	RelativeLayout layout;// 个人的逐梦码
	boolean blend = true;
	Button openuserBtn;
	CuGridView cuGridView;
	Button nextBtn;
	Button beforeBtn;
	UserInfoModel userModel;
	View warnView;// 提示用户登录
	View pastview;
	View showview;
	Intent intent;
	// -----商品的信息------

	private int issueId;
	private int productId;
	private int allCount;
	private int buyCount;
	private int syCount;
	private String productTitle;
	private String productName;
	private String showImage;
	private String jinducount;
	private int productArea;
	private int progrss;
	private ProductModel proModel;
	private int status;

	TextView allCountView;
	TextView issueIdView;
	TextView syCountView;
	TextView productTitleView;
	ImageView disView;//专区图片
	ProgressBar progressBar;
	TextView jdCountView;
	ImageView productImageView;

	ImageButton shareImageBtn;

	public static final int maxSize = 10;
	public int pageNum = 0;
	boolean isFresh = true;// true表示下拉刷新, false 表示上拉刷新

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.goodmessage_activity);
		Intent intent = getIntent();
		issueId = intent.getIntExtra("issueId", 1);
		productId = intent.getIntExtra("productId", 1);
		status=intent.getIntExtra("status", 0);
		userModel = getLogginUserinfo();
		initView();
		requestProductMessage();
		requestJionUser();
	}

	private void extracted() {
		productTitle = proModel.getProductTitle();
		issueId = proModel.getIssueId();
		allCount = proModel.getAllCount();
		buyCount = proModel.getBuyCount();
		showImage = proModel.getShowImage();
		productName = proModel.getProductName();
		syCount = allCount - buyCount;
		issueId = proModel.getIssueId();
		status=proModel.getStatus();
		productArea=proModel.getProductArea();
		progrss = buyCount * 100 / allCount;
		if (buyCount != 0 && progrss == 0) {
			progrss = 1;
		}
		jinducount = progrss + "%";
		loadimage(productImageView, Constant.PRODUCT_URL + showImage
				+ Constant.PRODUCT_KEY);
		if(status==0){
			buyBtn.setBackgroundResource(R.drawable.btn_background_gray);
			buyBtn.setText("商品已下架");
			buyBtn.setClickable(false);
		}else{
			buyBtn.setBackgroundResource(R.drawable.btn_background_yellow);
			buyBtn.setClickable(true);
		}
		requestJionUser();

	}

	OnRefreshListener2<ScrollView> refreshListener = new OnRefreshListener2<ScrollView>() {

		@Override
		public void onPullDownToRefresh(
				PullToRefreshBase<ScrollView> refreshView) {
			isFresh = true;
			requestProductMessage();
			if (userModel != null) {
				selectUserjion();
			}
			requestJionUser();
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
			isFresh = false;
			requestJionUser();
		}
	};
	
	Button buyBtn;
	private void initView() {
		scrollView = (PullToRefreshScrollView) findViewById(R.id.goodscrollview);
		scrollView.setMode(Mode.BOTH);
		scrollView.setOnRefreshListener(refreshListener);
		buyBtn=(Button) findViewById(R.id.go_shopping_btn);
		if(status==0){
			buyBtn.setBackgroundResource(R.drawable.btn_background_gray);
			buyBtn.setClickable(false);
		}else{
			buyBtn.setBackgroundResource(R.drawable.btn_background_yellow);
			buyBtn.setClickable(true);
		}
		TextView titleView = (TextView) findViewById(R.id.status_bar_title);
		titleView.setText("商品详情");
		shareImageBtn = (ImageButton) findViewById(R.id.status_share);
		allCountView = (TextView) findViewById(R.id.goodmessage_allcountView);

		issueIdView = (TextView) findViewById(R.id.goodmessage_isueidview);
		issueIdView.setText(String.valueOf(issueId));
		
		disView=(ImageView) findViewById(R.id.good_disimageview);
		syCountView = (TextView) findViewById(R.id.shengyu_textview);
		jdCountView = (TextView) findViewById(R.id.jindu_textview);
		productTitleView = (TextView) findViewById(R.id.g_name_textview);
		progressBar = (ProgressBar) findViewById(R.id.progress_bar);
		productImageView = (ImageView) findViewById(R.id.good_imageview);
		productImageView.setFocusable(true);
		pastview = findViewById(R.id.past_lay);
		showview = findViewById(R.id.show_lay);
		pastview.setOnClickListener(this);
		showview.setOnClickListener(this);
		layout = (RelativeLayout) findViewById(R.id.goodmessage_rlayout);
		openuserBtn = (Button) findViewById(R.id.goodmessage_userhint_btn);
		cuGridView = (CuGridView) findViewById(R.id.lucknum_gridview);
		nextBtn = (Button) findViewById(R.id.lucknum_after_btn);
		beforeBtn = (Button) findViewById(R.id.lucknum_before_btn);
		warnView = findViewById(R.id.warn_userloginlay);
		judgeIsLogin();

		listView = (CuExpandableListview) findViewById(R.id.good_listview);
		listView.setFocusable(false);
		listView.setGroupIndicator(null);
	}

	private void judgeIsLogin() {
		if (userModel == null) {
			openuserBtn.setVisibility(View.GONE);
			warnView.setVisibility(View.VISIBLE);
		} else {
			openuserBtn.setVisibility(View.VISIBLE);
			openuserBtn.setClickable(false);
			warnView.setVisibility(View.GONE);
			selectUserjion();
		}
	}

	public void requestProductMessage() {
		if (!NetWorkUtil.isNetworkAvailable(this)) {
			if (isFresh) {
				ToastUtil.show(this, getRString(R.string.network_hint));
				scrollView.onRefreshComplete();
			}
			return;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		Log.e(TAG, productId + " ----" + issueId);
		map.put("productId", productId);
		XHttp.Post(Constant.LAST_PRODUCT_DETAIL, map,
				new CommonCallback<JSONObject>() {

					@Override
					public void onCancelled(CancelledException arg0) {

					}

					@Override
					public void onError(Throwable arg0, boolean arg1) {
						Log.e(TAG, arg0.toString());
						scrollView.onRefreshComplete();
						if (arg0.toString().equals("java.io.EOFException")) {
							ToastUtil.show(m_activity, "加载失败");
							return;
						}
						RequestErrorUtil.judge(arg0, getApplicationContext());
					}

					@Override
					public void onFinished() {

					}

					@Override
					public void onSuccess(JSONObject arg0) {
						Log.e(TAG, arg0.toString());
						scrollView.onRefreshComplete();
						try {
							proModel = (ProductModel) JsonUtil.JsonToModel(
									arg0, ProductModel.class);
							extracted();
							stanceView();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
	}

	private void stanceView() {
		shareImageBtn.setVisibility(View.VISIBLE);
		allCountView.setText(String.valueOf(allCount));
		issueIdView.setText(String.valueOf(issueId));
		syCountView.setText(String.valueOf(syCount));
		if(productArea==10){
			disView.setVisibility(View.VISIBLE);
			disView.setImageResource(R.drawable.ten_yuan);
		}else if(productArea==100){
			disView.setVisibility(View.VISIBLE);
			disView.setImageResource(R.drawable.hundred_yuan);
		}
		productTitleView.setText(productName + productTitle);
		jdCountView.setText(jinducount);
		progressBar.setProgress(progrss);
	}

	/**
	 * 跳转到登录界面
	 * 
	 * @param view
	 */
	public void startLogin(View view) {
		intent = new Intent(this, LoginActivity.class);
		startActivityForResult(intent, 0);
	}

	/**
	 * 跳转到提交订单界面
	 */
	public void gosubmit(View view) {
		intent = new Intent(this, SubmitActivity.class);
		intent.putExtra("productId", productId);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
			case R.id.status_bar_Exit :
				finish();
				break;
			case R.id.goodmessage_userhint_btn :
				rquestLuck();
				break;
			case R.id.past_lay :
				toPast();
				break;
			case R.id.show_lay :
				toShow();
				break;
		}
	}

	/**
	 * 下一页
	 */
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

	/**
	 * 上一页
	 * 
	 * @param view
	 */
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

	public void rquestLuck() {
		if (layout.getVisibility() == View.VISIBLE) {
			layout.setVisibility(View.GONE);
		} else {
			if (adapter2 != null) {
				layout.setVisibility(View.VISIBLE);
			} else {
				adapter2 = new gridAdapter(m_activity, Tools.getList(
						userNumlist, 0, 9));
				cuGridView.setAdapter(adapter2);
				openuserBtn.setClickable(true);
			}
		}

	}

	List<String> userNumlist = new ArrayList<String>();
	int page = 1;

	gridAdapter adapter2;

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

	@SuppressLint({"ViewHolder", "InflateParams"})
	class gridAdapter extends BaseTaskAdaper<String> {

		public gridAdapter(Context context, List<String> mDatas) {
			super(context, mDatas);
		}

		@SuppressLint("NewApi")
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
	 * 分享
	 * 
	 * @param view
	 */
	public void share(View view) {
		showshareDialog();
		// final SHARE_MEDIA[] displaylist = new
		// SHARE_MEDIA[]{SHARE_MEDIA.WEIXIN,
		// SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE,};
		// UMImage image = new UMImage(this, BitmapFactory.decodeResource(
		// getResources(), R.drawable.app_icon));
		// new ShareAction(this).setDisplayList(displaylist).withExtra(image)
		// .withText("一元购买iphone 6s").withTitle("逐梦科技圆你梦想")
		// .withTargetUrl("http://www.baidu.com")
		// .setListenerList(new UMShareListener() {
		// @Override
		// public void onResult(SHARE_MEDIA platform) {
		// Toast.makeText(GoodMessageActivity.this,
		// platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
		// }
		//
		// @Override
		// public void onError(SHARE_MEDIA platform, Throwable t) {
		// Toast.makeText(GoodMessageActivity.this,
		// platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
		// }
		//
		// @Override
		// public void onCancel(SHARE_MEDIA platform) {
		// Toast.makeText(GoodMessageActivity.this,
		// platform + " 分享取消了", Toast.LENGTH_SHORT).show();
		// }
		// }).open();
	}

	/**
	 * 获取参加的人的次数和
	 */
	private void requestJionUser() {
		if (!NetWorkUtil.isNetworkAvailable(this)) {
			if (!isFresh) {
				scrollView.postDelayed(new Runnable() {
		            @Override
		            public void run() {
		            	scrollView.onRefreshComplete();
		            	ToastUtil.show(m_activity, getRString(R.string.network_hint));
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
				RequestErrorUtil.judge(arg0, getApplicationContext());
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
					if (luckModels != null) {
						pageNum=1;
						mluckModels = luckModels;
						parseChildLuckNum(mluckModels);
						exAdapter = new NewGoodExAdapter(luckModels,
								m_activity, childmap, listView);
						listView.setAdapter(exAdapter);
					}else{
						ToastUtil.show(m_activity, "没有更多数据");
					}
				} else {
					if (luckModels != null && luckModels.size() > 0) {
						pageNum++;
						mluckModels.addAll(luckModels);
						parseChildLuckNum(mluckModels);
						exAdapter.setList(mluckModels);
						exAdapter.setAllchildemap(childmap);
						exAdapter.notifyDataSetChanged();
					}else{
						ToastUtil.show(m_activity, "没有更多数据");
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
		int index = 0;
		if (luckModels != null) {
			List<String> list;
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

	/**
	 * 往期揭晓
	 * 
	 * @param view
	 */
	private void toPast() {
		intent = new Intent(this, PastActivity.class);
		intent.putExtra("productId", productId);
		startActivity(intent);
	}

	/**
	 * 跳转到晒单
	 */
	private void toShow() {
		intent = new Intent(this, ShowProductActivity.class);
		intent.putExtra("productId", productId);
		startActivity(intent);
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg1 == LoginActivity.resultCode) {
			userModel = getLogginUserinfo();
			judgeIsLogin();
		}
	}
	/**
	 * 获取自己参与的次数和num
	 */
	private void selectUserjion() {
		if (!NetWorkUtil.isNetworkAvailable(this)) {
			return;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		int userId = userModel.getUserId();
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
						openuserBtn.setVisibility(View.VISIBLE);
						if (arg0.toString().equals("[]")) {
							openuserBtn.setClickable(false);
							return;
						}
						try {
							int buyCount = arg0.getInt("buyCount");
							openuserBtn.setClickable(true);
							openuserBtn.setText("我参与了" + buyCount + "次");
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

	public void openDetail(View view) {
		intent = new Intent(this, BaseWebActivity.class);
		intent.putExtra("type", 1);
		intent.putExtra("weburl", Constant.product_mess+productId+"&type=app");
		intent.putExtra("title", "商品详情");
		startActivity(intent);
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
		.withText(productName+productTitle).withTitle("梦想是要有的，万一实现了呢！")
				.withTargetUrl(Constant.share_product + productId)
				.withMedia(image).setListenerList(new UMShareListener() {
					@Override
					public void onResult(SHARE_MEDIA platform) {
						Toast.makeText(GoodMessageActivity.this,
								platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onError(SHARE_MEDIA platform, Throwable t) {
						Toast.makeText(GoodMessageActivity.this,
								platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onCancel(SHARE_MEDIA platform) {
						Toast.makeText(GoodMessageActivity.this,
								platform + " 分享取消了", Toast.LENGTH_SHORT).show();
					}
				}).share();
	}
	
	
}
