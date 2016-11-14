package com.zozmom.ui.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import com.zozmom.model.ShowPicModel;
import com.zozmom.net.XHttp;
import com.zozmom.ui.ShowActivity;
import com.zozmom.ui.adapter.NewShowListViewAdapter;
import com.zozmom.ui.adapter.ShowListViewAdapter;
import com.zozmom.util.JsonUtil;
import com.zozmom.util.LogUtil;
import com.zozmom.util.NetWorkUtil;
import com.zozmom.util.RequestErrorUtil;
import com.zozmom.util.ToastUtil;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 晒单fragment
 * 
 * @author Administrator
 * 
 */
public class ShowFragment extends BaseTaskFragment {
//	CuListView listView;
//	PullToRefreshScrollView pScrollView;
	PullToRefreshListView listView;
	View rootView;
	List<ShowModel> mData = new ArrayList<ShowModel>();
	NewShowListViewAdapter adapter;
	public static final int maxSize = 10;
	private int pageNum = 0;
	boolean isFresh = true;// 下拉为true 上拉为false

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (rootView == null) {
			LogUtil.v("ShowFragment:  onCreateView");
			rootView = LayoutInflater.from(getActivity()).inflate(
					R.layout.newshowlist_fragment, container, false);
			initView();
		}
		return rootView;
	}

	public void initView() {
		TextView title = (TextView) rootView
				.findViewById(R.id.status_bar_title);
		title.setText(getResources().getString(R.string.openlist));
		ImageButton exitbtn = (ImageButton) rootView
				.findViewById(R.id.status_bar_Exit);
		exitbtn.setVisibility(View.GONE);
		listView = (PullToRefreshListView) rootView
				.findViewById(R.id.showlist_scrollview);
		listView.setMode(Mode.PULL_FROM_START);
		listView.setOnRefreshListener(refreshListener2);
//		listView = (CuListView) rootView.findViewById(R.id.showlist_view);
		listView.getRefreshableView().setSelector(new ColorDrawable(Color.TRANSPARENT));
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getActivity(), ShowActivity.class);
				ShowModel model = mData.get(position-1);
				intent.putExtra("data", model);
				startActivity(intent);
			}
		});
	}

	OnRefreshListener2<ListView> refreshListener2 = new OnRefreshListener2<ListView>() {

		@Override
		public void onPullDownToRefresh(
				PullToRefreshBase<ListView> refreshView) {
			isFresh = true;
			requesShow();
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			isFresh = false;
			requesShow();
		}
	};

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		LogUtil.v("showFragment  setUserVisibleHint" + isVisibleToUser);
		if (isVisibleToUser) {
			if (mData == null || mData.size() == 0) {
				isFresh = true;
				requesShow();
			}
		}
	}

	public void requesShow() {
		if (!NetWorkUtil.isNetworkAvailable(m_activity)) {
			listView.postDelayed(new Runnable() {
				@Override
				public void run() {
					listView.onRefreshComplete();
					ToastUtil.show(m_activity, "网络错误");
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
							ToastUtil.show(m_activity, "无更多数据");
						}
					}, 300);
					return;
				} else {
					map.put("shareId", mData.get(size - 1).getId());
				}
			}
		}

		XHttp.Post(Constant.productShareOrderList, map,
				new CommonCallback<JSONArray>() {

					@Override
					public void onCancelled(CancelledException arg0) {
					}

					@Override
					public void onError(Throwable arg0, boolean arg1) {
						listView.onRefreshComplete();
						RequestErrorUtil.judge(arg0, m_activity);

					}

					@Override
					public void onFinished() {
					}

					@Override
					public void onSuccess(JSONArray arg0) {
						listView.onRefreshComplete();
						Log.v("this", arg0.toString());
						List<ShowModel> list = JsonUtil.JsonToModel(arg0,
								ShowModel.class);
						if (list != null && list.size() > 0) {
							if (isFresh) {
								pageNum = 1;
								mData = list;
								if (mData.size() >= 10) {
									listView.setMode(Mode.BOTH);
								}
								adapter = new NewShowListViewAdapter(m_activity,
										mData,listView,m_loader);
								listView.setAdapter(adapter);

							} else {
								pageNum++;
								mData.addAll(list);
								adapter.setmDatas(mData);
								adapter.notifyDataSetChanged();
							}
						} else {
							ToastUtil.show(m_activity, "没有更多数据");
						}
					}
				});
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
//		LogUtil.v("ShowFragment:  onDestroyView");
//		if (rootView != null) {
//			((ViewGroup) rootView.getParent()).removeView(rootView);
//		}
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		LogUtil.v("ShowFragment:  onDestroy");
		super.onDestroy();
	}
}
