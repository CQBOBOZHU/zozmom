package com.zozmom.ui.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.chasedream.zhumeng.R;
import com.zozmom.constants.Constant;
import com.zozmom.model.ProductModel;
import com.zozmom.net.XHttp;
import com.zozmom.ui.GoodMessageActivity;
import com.zozmom.ui.OpeningGoodMessageActivity;
import com.zozmom.ui.adapter.OtherUserDreamListAdapter;
import com.zozmom.ui.adapter.UserDreamListAdapter;
import com.zozmom.util.JsonUtil;
import com.zozmom.util.NetWorkUtil;
import com.zozmom.util.ToastUtil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * 逐梦记录
 * 
 * @author Administrator
 * 
 */
public class UserDreamFragment extends BaseTaskFragment {
	PullToRefreshListView plistview;
	View view;
	int userId;

	public static final int maxSize = 10;
	private int pageNum = 0;
	private boolean isFresh = true;// true下拉刷新 false 上拉刷新
	List<ProductModel> proList = new ArrayList<ProductModel>();
	OtherUserDreamListAdapter dreamListAdapter;

	@SuppressLint("UseSparseArrays")
	Map<Integer, List<String>> childmap = new HashMap<Integer, List<String>>();

	public UserDreamFragment(int userId) {
		this.userId = userId;
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = LayoutInflater.from(getActivity()).inflate(
				R.layout.userdream_fragment, null);
		initView();
		requesUser();
		return view;
	}

	public void initView() {
		plistview = (PullToRefreshListView) view
				.findViewById(R.id.userdream_pull_listview);
		plistview.setOnItemClickListener(itemClickListener);
		plistview.setMode(Mode.PULL_FROM_START);
		plistview.setOnRefreshListener(onrefreshListener);

	}

	OnRefreshListener2<ListView> onrefreshListener = new OnRefreshListener2<ListView>() {

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			isFresh = true;
			requesUser();
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			isFresh = false;
			requesUser();
		}
	};

	OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			ProductModel model=proList.get(position - 1);
			int productId = model.getProductId();
			int totalCount = model.getTotalCount();
			int allbuyCount = model.getAllBuyCount();// 全部购买次数
			int issueId=model.getIssueId();
			int status=model.getStatus();
			Intent intent ;
			if (totalCount == allbuyCount) {
				intent = new Intent(getActivity(),
						OpeningGoodMessageActivity.class);
			} else {
				intent = new Intent(getActivity(), GoodMessageActivity.class);
			}
			intent.putExtra("productId", productId);
			intent.putExtra("issueId", issueId);
			intent.putExtra("status", status);
			startActivity(intent);
		}
	};

	public void requesUser() {
		if (!NetWorkUtil.isNetworkAvailable(m_activity)) {
			plistview.postDelayed(new Runnable() {
	            @Override
	            public void run() {
	            	plistview.onRefreshComplete();
	            }
	        }, 300);
			return;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		if (!isFresh) {
			int size = proList.size();
			if (size == 0) {
				isFresh = true;
			} else {
				if (size < pageNum * maxSize) {
					plistview.postDelayed(new Runnable() {
			            @Override
			            public void run() {
			            	ToastUtil.show(m_activity, "没有更多数据");
			            	plistview.onRefreshComplete();
			            }
			        }, 300);
					return;
				} else {
					map.put("latestId", proList.get(size - 1).getId());
				}
			}
		}
		String url = Constant.USER_DREAM_RECORD_LIST + userId;
		map.put("type", "all");
		XHttp.Post(url, map, new CommonCallback<JSONArray>() {

			@Override
			public void onCancelled(CancelledException arg0) {

			}

			@Override
			public void onError(Throwable arg0, boolean arg1) {
				plistview.onRefreshComplete();
				Log.v("this", "onError" + arg0.toString());
			}

			@Override
			public void onFinished() {

			}

			@Override
			public void onSuccess(JSONArray arg0) {
				Log.v("this", "onSuccess" + arg0.toString());
				plistview.onRefreshComplete();
				List<ProductModel> mList = JsonUtil.JsonToModel(arg0,
						ProductModel.class);
				if (mList != null && mList.size() > 0) {
					if (isFresh) {
						pageNum = 1;
						proList = mList;
						if (proList.size() >= 10) {
							plistview.setMode(Mode.BOTH);
						}
						parseChildLuckNum(proList, 0);
						dreamListAdapter = new OtherUserDreamListAdapter(
								getActivity(), proList, childmap,userId);
						plistview.setAdapter(dreamListAdapter);
					} else {
						pageNum++;
						parseChildLuckNum(proList, mList.size());
						proList.addAll(mList);
						dreamListAdapter.setmDatas(proList);
						dreamListAdapter.setChildemap(childmap);
						dreamListAdapter.notifyDataSetChanged();
					}
				}
			}
		});
	}

	public void parseChildLuckNum(List<ProductModel> luckModels, int index) {
		if (luckModels != null) {
			List<String> list;
			for (ProductModel luckModel : luckModels) {
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
}
