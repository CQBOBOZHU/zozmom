package com.zozmom.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.chasedream.zhumeng.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.zozmom.constants.Constant;
import com.zozmom.cuview.CuListView;
import com.zozmom.model.LuckModel;
import com.zozmom.net.XHttp;
import com.zozmom.ui.adapter.AlistViewAdapter;
import com.zozmom.ui.adapter.BlistViewAdapter;
import com.zozmom.util.JsonUtil;
import com.zozmom.util.NetWorkUtil;
import com.zozmom.util.RequestErrorUtil;
import com.zozmom.util.ToastUtil;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * 计算方式
 * 
 * @author Administrator
 * 
 */
public class ReckonActivity extends BaseTaskActivity<JSONObject> {
	CuListView AcuListView;
	CuListView BcuListView;
	AlistViewAdapter alistViewAdapter;
	BlistViewAdapter blistViewAdapter;
	TextView titleView;
	Button a_openBtn;
	Button b_openBtn;
	TextView acodeView;
	TextView bcodeView;
	TextView luckView;

	View reckonView;
	TextView a_textview;
	TextView b_textview;
	TextView all_textview;
	TextView ysview;

	Drawable opendraw;
	Drawable closedraw;
	List<LuckModel> AluckModels = new ArrayList<LuckModel>();
	List<LuckModel> BluckModels = new ArrayList<LuckModel>();

	// private int productId;
	private int issueId;
	int luckNum;
	int productPrice;
	long timeSum;// 时间和
	int random;// 随机数和
	PullToRefreshScrollView refreshScrollView;
	int pageNum = 1;
	private static final int maxSize = 10;
	boolean isBopen = false;// true表示B打开 false 表示b没有打开

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reckon_activity);
		AluckModels.add(new LuckModel());
		BluckModels.add(new LuckModel());
		Intent intent = getIntent();
		// productId = intent.getIntExtra("productId", 1);
		refreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pullto_listview);
		refreshScrollView.setOnRefreshListener(listener);
		refreshScrollView.setMode(Mode.PULL_FROM_END);
		issueId = intent.getIntExtra("issueId", 1);
		luckNum = intent.getIntExtra("luckNum", 10000000);
		productPrice = intent.getIntExtra("allcount", 0);
		AcuListView = (CuListView) findViewById(R.id.A_listview);
		BcuListView = (CuListView) findViewById(R.id.B_listview);
		titleView = (TextView) findViewById(R.id.status_bar_title);
		titleView.setText("计算详情");
		a_openBtn = (Button) findViewById(R.id.Aopen_textview);
		b_openBtn = (Button) findViewById(R.id.Bopen_textview);
		acodeView = (TextView) findViewById(R.id.A_code_view);
		bcodeView = (TextView) findViewById(R.id.B_code_view);
		luckView = (TextView) findViewById(R.id.luckunmView);
		reckonView = findViewById(R.id.reckon_mes);
		a_textview = (TextView) findViewById(R.id.a_textview);
		b_textview = (TextView) findViewById(R.id.b_textview);
		all_textview = (TextView) findViewById(R.id.all_cview1);
		ysview = (TextView) findViewById(R.id.yushuview);

		luckView.setText(String.valueOf(luckNum));
		a_openBtn.setClickable(false);
		b_openBtn.setClickable(false);
		opendraw = getRDrawble(R.drawable.take_on_blue);
		opendraw.setBounds(0, 0, opendraw.getMinimumWidth(),
				opendraw.getMinimumHeight());
		closedraw = getRDrawble(R.drawable.take_off_blue);
		closedraw.setBounds(0, 0, closedraw.getMinimumWidth(),
				closedraw.getMinimumHeight());
		requesReck(0);
	}
	boolean isFresh = true;
	OnRefreshListener<ScrollView> listener = new OnRefreshListener<ScrollView>() {

		@Override
		public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
			isFresh = false;
			requestReckonRandom();
		}
	};

	public void requesReck(int id) {
		if (!NetWorkUtil.isNetworkAvailable(this)) {
			ToastUtil.show(this, getRString(R.string.network_er));
			a_openBtn.setClickable(true);
			b_openBtn.setClickable(true);
			return;
		} else {
			if (id == 0) {
				requestReckonTime();
				requestReckonRandom();
			} else if (id == 1) {
				requestReckonTime();
			} else if (id == 2) {
				requestReckonRandom();
			}
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
			case R.id.status_bar_Exit :
				finish();
				break;
		}
	}

	/**
	 * 查看A值
	 * 
	 * @param view
	 */
	public void openA(View view) {
		if (AluckModels.size() < 2) {
			requesReck(1);
			return;
		}

		a_openBtn.setClickable(false);
		if (AcuListView.getVisibility() == View.GONE) {
			AcuListView.setVisibility(View.VISIBLE);
			if (alistViewAdapter == null) {
				alistViewAdapter = new AlistViewAdapter(this, AluckModels);
				AcuListView.setAdapter(alistViewAdapter);
			} else
				alistViewAdapter.notifyDataSetChanged();
			closeOrOpen(a_openBtn, false);
		} else {
			AcuListView.setVisibility(View.GONE);
			closeOrOpen(a_openBtn, true);
		}
		a_openBtn.setClickable(true);
	}

	/**
	 * 查看B值
	 * 
	 * @param view
	 */
	public void openB(View view) {
		isBopen = !isBopen;
		if (BluckModels.size() < 2) {
			requesReck(2);
			return;
		}
		b_openBtn.setClickable(false);
		if (BcuListView.getVisibility() == View.GONE) {
			BcuListView.setVisibility(View.VISIBLE);
			if (blistViewAdapter == null) {
				blistViewAdapter = new BlistViewAdapter(this, BluckModels);
				BcuListView.setAdapter(blistViewAdapter);
			} else {
				blistViewAdapter.notifyDataSetChanged();
			}
			closeOrOpen(b_openBtn, false);
		} else {
			BcuListView.setVisibility(View.GONE);
			closeOrOpen(b_openBtn, true);
		}
		b_openBtn.setClickable(true);
	}

	public void closeOrOpen(Button btn, boolean bl) {
		if (bl) {
			btn.setText("展开");
			btn.setCompoundDrawables(opendraw, null, null, null);
		} else {
			btn.setText("收起");
			btn.setCompoundDrawables(closedraw, null, null, null);
		}
	}
	/**
	 * 展示计算
	 */
	public void showReckonMes() {
		if (reckonView.getVisibility() == View.VISIBLE) {
			return;
		}
		if (productPrice != 0 && timeSum != 0 && random != 0) {
			long sy = (timeSum + random) % productPrice;
			reckonView.setVisibility(View.VISIBLE);
			a_textview.setText(String.valueOf(timeSum));
			b_textview.setText(String.valueOf(random));
			all_textview.setText(String.valueOf(productPrice));
			ysview.setText(String.valueOf(sy));
		}

	}

	/**
	 * 获取最后50条时间和用户名
	 */
	private void requestReckonTime() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("issueId", issueId);
		XHttp.Post(Constant.PRODUCT_CALCDATA, map,
				new CommonCallback<JSONObject>() {

					@Override
					public void onCancelled(CancelledException arg0) {

					}

					@Override
					public void onError(Throwable arg0, boolean arg1) {
						a_openBtn.setClickable(true);
						RequestErrorUtil.judge(arg0, m_activity);
					}

					@Override
					public void onFinished() {

					}

					@Override
					public void onSuccess(JSONObject arg0) {
						a_openBtn.setClickable(true);
						JSONArray jsonArray;
						try {
							jsonArray = arg0.getJSONArray("latestDreamRecord");
							timeSum = arg0.getLong("buyTimeSum");
							random = arg0.getInt("randomSum");
							acodeView.setText(String.valueOf(timeSum));
							bcodeView.setText(String.valueOf(random));
							List<LuckModel> mlist = new ArrayList<LuckModel>();
							mlist = JsonUtil.JsonToModel(jsonArray,
									LuckModel.class);
							AluckModels.addAll(mlist);
							showReckonMes();
							Log.v("this", "AluckModels:" + AluckModels.size());
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}
	/**
	 * 获取所有随机数和用户名和时间
	 */
	private void requestReckonRandom() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("issueId", issueId);
		// map.put("", value);
		int size = BluckModels.size()-1;
		if (!isFresh) {
			if(size==0){
				refreshScrollView.postDelayed(new Runnable() {
		            @Override
		            public void run() {
		            	refreshScrollView.onRefreshComplete();
		            }
		        }, 300);
			}
			
			if (size >= pageNum * maxSize) {
				map.put("recordId", BluckModels.get(size - 1).getId());
			} else {
				refreshScrollView.postDelayed(new Runnable() {
		            @Override
		            public void run() {
		            	refreshScrollView.onRefreshComplete();
		            	ToastUtil.show(m_activity, "没有更多数据");
		            }
		        }, 300);
				return;
			}
		}
		XHttp.Post(Constant.PRODUCT_RECORD, map,
				new CommonCallback<JSONArray>() {

					@Override
					public void onCancelled(CancelledException arg0) {

					}

					@Override
					public void onError(Throwable arg0, boolean arg1) {
						refreshScrollView.onRefreshComplete();
						b_openBtn.setClickable(true);
						Log.v(TAG, "onError" + arg0.toString());
						RequestErrorUtil.judge(arg0, m_activity);
					}

					@Override
					public void onFinished() {

					}

					@Override
					public void onSuccess(JSONArray arg0) {
						b_openBtn.setClickable(true);
						refreshScrollView.onRefreshComplete();
						Log.v(TAG, "onSuccess" + arg0.toString());
						List<LuckModel> mlist = JsonUtil.JsonToModel(arg0,
								LuckModel.class);
						if (mlist != null && mlist.size() > 0) {
							if (isFresh) {
								pageNum = 1;
								BluckModels.addAll(mlist);
								if (isBopen) {
									blistViewAdapter = new BlistViewAdapter(
											m_activity, BluckModels);
									BcuListView.setAdapter(blistViewAdapter);
								}
							} else {
								pageNum++;
								BluckModels.addAll(mlist);
								if (isBopen) {
									blistViewAdapter.setmDatas(BluckModels);
									blistViewAdapter.notifyDataSetChanged();
								}
							}
						}
						Log.v("this", "BluckModels:" + BluckModels.size());
					}
				});
	}
}
