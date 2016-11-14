package com.zozmom.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import com.chasedream.zhumeng.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.zozmom.constants.Constant;
import com.zozmom.cuview.CuListView;
import com.zozmom.model.ShowModel;
import com.zozmom.ui.adapter.ShowListViewAdapter;
import com.zozmom.util.JsonUtil;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 单个商品的晒单
 * 
 * @author Administrator
 * 
 */
public class ShowProductActivity extends BaseTaskActivity<JSONArray> {
	PullToRefreshScrollView refreshScrollView;
	CuListView cuListView;
	int productId;

	List<ShowModel> mData = new ArrayList<ShowModel>();
	ShowListViewAdapter adapter;
	int maxSize=10;
	int pageNum=1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.showlist_fragment);
		productId = getIntent().getIntExtra("productId", 0);
		initView();
		requesShow(-1);
	}

	private void requesShow(int shareId) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (shareId != -1) {
			map.put("shareId", shareId);
		}
		map.put("productId", productId);
		requstPost(Constant.productShareOrderList, map);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.status_bar_Exit:
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	public void onSuccess(JSONArray arg0) {
		super.onSuccess(arg0);
		refreshScrollView.onRefreshComplete();
		List<ShowModel> list = JsonUtil.JsonToModel(arg0, ShowModel.class);
		if (list != null && list.size() != 0) {
			mData.addAll(list);
			if (adapter == null) {
				adapter = new ShowListViewAdapter(m_activity, mData,cuListView);
				cuListView.setAdapter(adapter);
			} else {
				adapter.setmDatas(mData);
				adapter.notifyDataSetChanged();
			}

		}

	}

	@Override
	public void onError(Throwable arg0, boolean arg1) {
		super.onError(arg0, arg1);
		refreshScrollView.onRefreshComplete();
	}

	public void initView() {
		refreshScrollView = (PullToRefreshScrollView) findViewById(R.id.showlist_scrollview);
		cuListView = (CuListView) findViewById(R.id.showlist_view);
		TextView title = (TextView) findViewById(R.id.status_bar_title);
		title.setText(getResources().getString(R.string.openlist));
		refreshScrollView.setMode(Mode.PULL_FROM_END);
		refreshScrollView.setOnRefreshListener(listener);
		cuListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		cuListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(m_activity, ShowActivity.class);
				ShowModel model = mData.get(position);
				intent.putExtra("data", model);
				startActivity(intent);
			}
		});
	}

	OnRefreshListener<ScrollView> listener = new OnRefreshListener<ScrollView>() {

		@Override
		public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
			if (mData != null && mData.size() > 0) {
				int shareId = mData.get(mData.size() - 1).getId();
				requesShow(shareId);
			} else {
				refreshScrollView.postDelayed(new Runnable() {
					@Override
					public void run() {
						refreshScrollView.onRefreshComplete();
					}
				}, 300);
			}
		}
	};
}
