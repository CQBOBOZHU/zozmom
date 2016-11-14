package com.zozmom.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.widget.AdapterView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.chasedream.zhumeng.R;
import com.umeng.socialize.utils.Log;
import com.zozmom.constants.Constant;
import com.zozmom.cuview.CuListView;
import com.zozmom.model.ProductModel;
import com.zozmom.net.XHttp;
import com.zozmom.ui.adapter.BaseTaskAdaper;
import com.zozmom.util.JsonUtil;
import com.zozmom.util.NetWorkUtil;
import com.zozmom.util.RequestErrorUtil;
import com.zozmom.util.ToastUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * 商品分类展示
 * 
 * @author Administrator
 * 
 */
@SuppressLint("InflateParams")
public class CmmCftionActivity extends BaseTaskActivity<Object> {
	String type;
	PullToRefreshScrollView refreshScrollView;
	CuListView cuListView;
	TextView numView;
	int producttype;
	List<ProductModel> listgoods = new ArrayList<ProductModel>();
	ListAdaper listAdaper;
	public static final int maxSize = 10;
	private int pageNum = 0;
	private boolean isFresh = true;// true表示下拉 false表示上啦

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cmmcftion_activity);
//		type = getIntent().getStringExtra("type");
		type = "一元专区";
		producttype = getIntent().getIntExtra("producttype", 0);
		initView();
		requestByType();
	}
	
	
	
	

	public void requestByType() {
		if (!NetWorkUtil.isNetworkAvailable(this)) {
			refreshScrollView.postDelayed(new Runnable() {
				@Override
				public void run() {
					refreshScrollView.onRefreshComplete();
					ToastUtil.show(m_activity, "没有更多数据");
				}
			}, 300);
			return;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		if (!isFresh) {
			int size = listgoods.size();
			if (size == 0) {
				isFresh = true;
			} else {
				if (size < pageNum * maxSize) {
					refreshScrollView.postDelayed(new Runnable() {
						@Override
						public void run() {
							refreshScrollView.onRefreshComplete();
							ToastUtil.show(m_activity, "没有更多数据");
						}
					}, 300);
					return;
				} else
					map.put("productId", listgoods.get(size - 1).getProductId());
			}
		}
		XHttp.Post(Constant.SELECT_PRODUCT_BY_TYPE + producttype, map,
				new CommonCallback<JSONObject>() {

					@Override
					public void onCancelled(CancelledException arg0) {

					}

					@Override
					public void onError(Throwable arg0, boolean arg1) {
						Log.v("this", "onError" + arg0.toString());
						refreshScrollView.onRefreshComplete();
						RequestErrorUtil.judge(arg0, m_activity);
					}

					@Override
					public void onFinished() {

					}

					@Override
					public void onSuccess(JSONObject arg0) {
						Log.v("this", arg0.toString());
						refreshScrollView.onRefreshComplete();
						try {
							int total = arg0.getInt("total");
							JSONArray jsonArray = arg0.getJSONArray("result");
							List<ProductModel> mlist = JsonUtil.JsonToModel(
									jsonArray, ProductModel.class);
							if (mlist != null && mlist.size() > 0) {
								if (isFresh) {
									pageNum = 1;
									listgoods = mlist;
									if (listgoods.size() >= maxSize) {
										refreshScrollView.setMode(Mode.BOTH);
									}
									listAdaper = new ListAdaper(m_activity,
											listgoods);
									cuListView.setAdapter(listAdaper);
									numView.setText("共" + listgoods.size()
											+ "件商品");
								} else {
									pageNum++;
									listgoods.addAll(mlist);
									listAdaper.setmDatas(listgoods);
									listAdaper.notifyDataSetChanged();
									numView.setText("共" + listgoods.size()
											+ "件商品");
								}
							}else{
								ToastUtil.show(m_activity, "没有更多数据");
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}
	public void initView() {
		refreshScrollView = (PullToRefreshScrollView) findViewById(R.id.cmm_scrollview);
		refreshScrollView.getRefreshableView().smoothScrollTo(0, 0);
		refreshScrollView.setMode(Mode.PULL_FROM_START);
		cuListView = (CuListView) findViewById(R.id.cmm_culistview);
		TextView textView = (TextView) findViewById(R.id.status_bar_title);
		textView.setText(type);
		cuListView.setOnItemClickListener(this);
		numView = (TextView) findViewById(R.id.cmm_textview);
		refreshScrollView.setOnRefreshListener(onrefresh);
	}

	OnRefreshListener2<ScrollView> onrefresh = new OnRefreshListener2<ScrollView>() {

		@Override
		public void onPullDownToRefresh(
				PullToRefreshBase<ScrollView> refreshView) {
			isFresh = true;
			requestByType();
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
			isFresh = false;
			requestByType();
		}
	};

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
			case R.id.status_bar_Exit :
				finish();
				break;

			default :
				break;
		}
	}
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(this, GoodMessageActivity.class);
		ProductModel model = listgoods.get(position);
		int issueId = model.getIssueId();
		int productId = model.getProductId();
		intent.putExtra("issueId", issueId);
		intent.putExtra("productId", productId);
		startActivity(intent);
	}

	class ListAdaper extends BaseTaskAdaper<ProductModel> {

		public ListAdaper(Context context, List<ProductModel> mDatas) {
			super(context, mDatas);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.cmm_listview_item,
						null);
				holder.imageView = (ImageView) convertView
						.findViewById(R.id.list_cmm_imageview);
				holder.cmmNameView = (TextView) convertView
						.findViewById(R.id.list_cmm_name);
				holder.priceView = (TextView) convertView
						.findViewById(R.id.list_price_textview);
				holder.countView = (TextView) convertView
						.findViewById(R.id.list_count_textview);
				holder.todreamBtn = (Button) convertView
						.findViewById(R.id.list_cmm_add);
				holder.progressBar = (ProgressBar) convertView
						.findViewById(R.id.list_item_bar);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			ProductModel goodsModel = mDatas.get(position);
			String imageUrl = goodsModel.getShowImage();
			String name = goodsModel.getProductName();
			String title = goodsModel.getProductTitle();
			int price = goodsModel.getProductPrice();
			int BuyCount = goodsModel.getBuyCount();
			loadImage(Constant.PRODUCT_URL + imageUrl + Constant.PRODUCT_KEY,
					holder.imageView);
			holder.cmmNameView.setText(name + title);

			int a = price - BuyCount;
			int b = BuyCount * 100 / price;
			if(b==0&&BuyCount>0){
				b=1;
			}
			holder.priceView.setText("总需:" + price);
			holder.countView.setText("剩余:" + a);
			holder.progressBar.setProgress(b);
			holder.todreamBtn.setTag(position);
			holder.todreamBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext, SubmitActivity.class);
					int productId = mDatas.get((Integer) v.getTag())
							.getProductId();
					intent.putExtra("productId", productId);
					mContext.startActivity(intent);
				}
			});
			return convertView;
		}

		class ViewHolder {
			ImageView imageView;
			TextView cmmNameView;
			TextView priceView;
			TextView countView;
			Button todreamBtn;
			ProgressBar progressBar;
		}
	}
}
