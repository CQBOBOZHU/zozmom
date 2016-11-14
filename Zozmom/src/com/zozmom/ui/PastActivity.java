package com.zozmom.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import com.chasedream.zhumeng.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.zozmom.constants.Constant;
import com.zozmom.model.OpenModel;
import com.zozmom.ui.adapter.BuyGoodsListAdapter;
import com.zozmom.util.JsonUtil;
import com.zozmom.util.NetWorkUtil;
import com.zozmom.util.ToastUtil;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

/**
 * 往期揭晓
 * 
 * @author Administrator
 * 
 */
public class PastActivity extends BaseTaskActivity<JSONArray> {
	PullToRefreshGridView gridView;
	int productId;
	List<OpenModel> listgoods = new ArrayList<OpenModel>();
	TextView titleView;
	public static final int maxSize = 10;
	private int pageNum = 0;
	private boolean isFresh = true;// true表示下拉 false 表示上拉
	BuyGoodsListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.past_activity);
		productId = getIntent().getIntExtra("productId", 0);
		gridView = (PullToRefreshGridView) findViewById(R.id.past_pullgridview);
		titleView = (TextView) findViewById(R.id.status_bar_title);
		titleView.setText("往期揭晓");
		gridView.getRefreshableView().setSelector(
				new ColorDrawable(Color.TRANSPARENT));
		gridView.setOnRefreshListener(refreshlistener);
		gridView.setOnItemClickListener(this);
		gridView.setMode(Mode.PULL_FROM_START);
		requst();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		super.onItemClick(parent, view, position, id);
		Intent intent = new Intent(this, OpeningGoodMessageActivity.class);
		OpenModel model = listgoods.get(position);
		intent.putExtra("productId", model.getProductId());
		intent.putExtra("issueId", model.getIssueId());
		startActivity(intent);
	}

	public void onClick(android.view.View v) {
		switch (v.getId()) {
			case R.id.status_bar_Exit :
				finish();
				break;
		}
	}

	OnRefreshListener2<GridView> refreshlistener = new OnRefreshListener2<GridView>() {

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
			isFresh = true;
			requst();
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
			isFresh = false;
			requst();

		}
	};
	public void requst() {
		if (!NetWorkUtil.isNetworkAvailable(this)) {
			ToastUtil.show(this, getRString(R.string.network_er));
			gridView.onRefreshComplete();
			return;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		if (!isFresh) {
			int size = listgoods.size();
			if (size == 0) {
				isFresh = true;
			} else {
				if (size < pageNum * maxSize) {
					gridView.onRefreshComplete();
					return;
				} else {
					map.put("lotteryProductId", listgoods.get(size - 1).getId());
				}
			}
		}
		map.put("productId", productId);
		requstPost(Constant.LAST_LOTTERY, map);
	}

	@Override
	public void onError(Throwable arg0, boolean arg1) {
		gridView.onRefreshComplete();
		super.onError(arg0, arg1);
	}
	@Override
	public void onSuccess(JSONArray arg0) {
		gridView.onRefreshComplete();
		Log.v("this", arg0.toString());
		List<OpenModel> models = JsonUtil.JsonToModel(arg0, OpenModel.class);
		if (models != null && models.size() > 0) {
			if (isFresh) {
				pageNum = 1;
				listgoods = models;
				if(listgoods.size()>=10){
					gridView.setMode(Mode.BOTH);
				}
				adapter = new BuyGoodsListAdapter(this, listgoods,gridView,hLoader);
				gridView.setAdapter(adapter);
			} else {
				pageNum++;
				listgoods.addAll(models);
				adapter.setmDatas(listgoods);
				adapter.notifyDataSetChanged();
			}
		}
	}
}
