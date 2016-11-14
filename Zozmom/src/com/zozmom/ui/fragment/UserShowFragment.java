package com.zozmom.ui.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.chasedream.zhumeng.R;
import com.zozmom.constants.Constant;
import com.zozmom.cuview.CuListView;
import com.zozmom.model.ShowModel;
import com.zozmom.net.XHttp;
import com.zozmom.ui.ShowActivity;
import com.zozmom.ui.adapter.ShowListViewAdapter;
import com.zozmom.util.JsonUtil;
import com.zozmom.util.NetWorkUtil;
import com.zozmom.util.RequestErrorUtil;
import com.zozmom.util.ToastUtil;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ScrollView;
import android.widget.AdapterView.OnItemClickListener;
/**
 * 别人的晒单列表
 * 
 * @author Administrator
 * 
 */
public class UserShowFragment extends BaseTaskFragment {

	CuListView listView;
	PullToRefreshScrollView pullToRefreshScrollView;
	View view;
	boolean isVisible = false;
	int userId;
	List<ShowModel> mData = new ArrayList<ShowModel>();
	ShowListViewAdapter adapter;

	public static final int maxSize = 10;
	private int pageNum = 0;
	private boolean isFresh = true;// 下拉为true 上拉为false

	public UserShowFragment(int userId) {
		this.userId = userId;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.v("this", "onCreateView");
		view = LayoutInflater.from(getActivity()).inflate(
				R.layout.user_show_fragment, container, false);
		initView();
		return view;
	}

	public void initView() {
		pullToRefreshScrollView = (PullToRefreshScrollView) view
				.findViewById(R.id.user_showlist_scrollview);
		listView = (CuListView) view.findViewById(R.id.user_showlist_view);
		pullToRefreshScrollView.setMode(Mode.PULL_FROM_START);
		pullToRefreshScrollView.setOnRefreshListener(listener2);
		listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getActivity(), ShowActivity.class);
				ShowModel model = mData.get(position);
				intent.putExtra("data", model);
				startActivity(intent);
			}
		});
	}

	OnRefreshListener2<ScrollView> listener2 = new OnRefreshListener2<ScrollView>() {

		@Override
		public void onPullDownToRefresh(
				PullToRefreshBase<ScrollView> refreshView) {
			isFresh = true;
			request();
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
			isFresh = false;
			request();
		}
	};

	public void request() {
		Map<String, Object> map = new HashMap<String, Object>();
		if(!NetWorkUtil.isNetworkAvailable(m_activity)){
			pullToRefreshScrollView.postDelayed(new Runnable() {
	            @Override
	            public void run() {
	            	pullToRefreshScrollView.onRefreshComplete();
	            }
	        }, 300);
			return;
		}
		if(!isFresh){
			int size=mData.size();
			if(size==0){
				isFresh=true;
			}else{
				if(size<pageNum*maxSize){
					pullToRefreshScrollView.postDelayed(new Runnable() {
			            @Override
			            public void run() {
			            	ToastUtil.show(m_activity, "没有更多数据");
			            	pullToRefreshScrollView.onRefreshComplete();
			            }
			        }, 300);
					return;
				}else{
					map.put("shareId", mData.get(size-1).getId());
				}
			}
		}
		
		XHttp.Post(Constant.shareOrderList + "/" + userId, map,
				new CommonCallback<JSONArray>() {

					@Override
					public void onCancelled(CancelledException arg0) {

					}

					@Override
					public void onError(Throwable arg0, boolean arg1) {
						pullToRefreshScrollView.onRefreshComplete();
						RequestErrorUtil.judge(arg0, m_activity);
					}

					@Override
					public void onFinished() {

					}

					@Override
					public void onSuccess(JSONArray arg0) {
						pullToRefreshScrollView.onRefreshComplete();
						Log.v("this", arg0.toString());
						List<ShowModel> list = JsonUtil.JsonToModel(arg0,
								ShowModel.class);
						if (list != null && list.size()>0) {
							if(isFresh){
								pageNum=1;
								mData=list;
								if(mData.size()>10){
									pullToRefreshScrollView.setMode(Mode.BOTH);
								}
								adapter = new ShowListViewAdapter(m_activity,
										mData,listView);
								listView.setAdapter(adapter);
							}else{
								pageNum++;
								mData.addAll(list);
								adapter.setmDatas(mData);
								adapter.notifyDataSetChanged();
							}
						}
					}
				});
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		Log.v("this", "UserVisibleHint" + isVisibleToUser);
		if (isVisibleToUser) {
			if (mData == null || mData.size() == 0) {
				isFresh=true;
				request();
			}
		}
	}
}
