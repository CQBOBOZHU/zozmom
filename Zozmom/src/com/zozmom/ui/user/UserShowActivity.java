package com.zozmom.ui.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.xutils.x;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.chasedream.zhumeng.R;
import com.zozmom.constants.Constant;
import com.zozmom.cuview.CuListView;
import com.zozmom.model.ShowModel;
import com.zozmom.ui.ShowActivity;
import com.zozmom.ui.adapter.ShowMineListViewAdapter;
import com.zozmom.util.JsonUtil;
import com.zozmom.util.NetWorkUtil;
import com.zozmom.util.RequestErrorUtil;

/**
 * 我的晒单
 * 
 * @author Administrator
 * 
 */
@ContentView(R.layout.usershow_activity)
public class UserShowActivity extends UserManagerBaseActivity<JSONArray> {
	@ViewInject(R.id.showlist_view)
	PullToRefreshListView listView;
	List<ShowModel> showlist;
	@ViewInject(R.id.status_bar_title)
	TextView titleView;
	private int userId;

	public static final int maxSize = 10;
	private int pageNum = 0;
	private boolean isFresh = true;// true表示下拉 false表示上啦

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		x.view().inject(this);
		titleView.setText(getRString(R.string.my_showlist));
		toDoSomeThing();
	}

	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.status_bar_Exit :
				finish();
				break;
		}
	}

	OnRefreshListener2<ListView> listener = new OnRefreshListener2<ListView>() {

		@Override
		public void onPullDownToRefresh(
				PullToRefreshBase<ListView> refreshView) {
			isFresh = true;
			request();

		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			isFresh = false;
			request();
		}
	};

	public void initView() {
		super.initView();
		listView.setMode(Mode.PULL_FROM_START);
		listView.setOnRefreshListener(listener);
		listView.getRefreshableView().setSelector(new ColorDrawable(Color.TRANSPARENT));
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(m_activity, ShowActivity.class);
				ShowModel model = mData.get(position-1);
				intent.putExtra("data", model);
//				intent.putExtra("status", status);
				startActivity(intent);
			}
		});
		userId = infoModel.getUserId();
		request();
	}

	public void request() {
		if (!NetWorkUtil.isNetworkAvailable(this)) {
			listView.postDelayed(new Runnable() {
	            @Override
	            public void run() {
	            	listView.onRefreshComplete();
	            }
	        }, 300);
			return;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		if (!isFresh) {
			int size = mData.size();
			if (size == 0) {
				isFresh = true;
			} else {
				if (size < pageNum * maxSize) {
					listView.postDelayed(new Runnable() {
			            @Override
			            public void run() {
			            	listView.onRefreshComplete();
			            }
			        }, 300);
					return;
				} else {
					map.put("shareId", mData.get(size - 1).getId());
				}
			}
		}
		RequestParams params = new RequestParams(Constant.ownShareOrderList
				+ userId);
		long time = System.currentTimeMillis();
		params.addHeader("token-param", time + "," + infoModel.getToken() + ","
				+ infoModel.getUserId());
		x.http().post(params, new CommonCallback<JSONArray>() {

			@Override
			public void onCancelled(CancelledException arg0) {

			}

			@Override
			public void onError(Throwable arg0, boolean arg1) {
				listView.onRefreshComplete();
				RequestErrorUtil.judge(arg0, UserShowActivity.this);
			}

			@Override
			public void onFinished() {

			}

			@Override
			public void onSuccess(JSONArray arg0) {
				listView.onRefreshComplete();
				Log.v("this", arg0.toString());
				if (arg0.toString().trim().equals("[]")) {
					Log.v(TAG, "没有数据");
					return;
				}
				List<ShowModel> list = JsonUtil.JsonToModel(arg0,
						ShowModel.class);

				if (list != null && list.size() > 0) {
					if (isFresh) {
						pageNum=1;
						mData = list;
						if(mData.size()>=maxSize){
							listView.setMode(Mode.BOTH);
						}
						adapter = new ShowMineListViewAdapter(m_activity, mData);
						listView.setAdapter(adapter);
					} else {
						pageNum++;
						mData.addAll(list);
						adapter.setmDatas(mData);
						adapter.notifyDataSetChanged();
					}
				}

			}
		});
	}

	List<ShowModel> mData = new ArrayList<ShowModel>();
	ShowMineListViewAdapter adapter;

	public void onSuccess(JSONArray arg0) {

	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg1 == 1) {
			isFresh=true;
			request();
		}
	}

}
