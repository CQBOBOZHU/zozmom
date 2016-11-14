package com.zozmom.ui.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.chasedream.zhumeng.R;
import com.zozmom.constants.Constant;
import com.zozmom.cuview.CuGridView;
import com.zozmom.model.OpenModel;
import com.zozmom.model.ShowModel;
import com.zozmom.net.XHttp;
import com.zozmom.ui.OpeningGoodMessageActivity;
import com.zozmom.ui.adapter.BuyGoodsListAdapter;
import com.zozmom.util.JsonUtil;
import com.zozmom.util.LogUtil;
import com.zozmom.util.NetWorkUtil;
import com.zozmom.util.ToastUtil;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * 最新揭晓
 * 
 * @author Administrator
 * 
 */
public class TheNewFragment extends BaseTaskFragment {

	List<OpenModel> listgoods = new ArrayList<OpenModel>();

	PullToRefreshGridView gridView;
	View view;

	public static final int maxSize = 10;
	private int pageNum = 1;
	private boolean isFresh = true;// true下拉刷新 false 上啦刷新

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (view == null) {
			LogUtil.v("TheNewFragment:  onCreateView");
			view = LayoutInflater.from(getActivity()).inflate(
					R.layout.the_new_fragment, null);
			initView();
		}else{
			if (listgoods == null || listgoods.size() == 0) {
				isFresh = true;
				requestLast();
			}
		}
		return view;
	}
	private void requestLast() {
		Context content=getActivity();
		if(content==null){
			return;
		}
		if (!NetWorkUtil.isNetworkAvailable(content)) {
			gridView.postDelayed(new Runnable() {
				@Override
				public void run() {
					gridView.onRefreshComplete();
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
					gridView.postDelayed(new Runnable() {
						@Override
						public void run() {
							gridView.onRefreshComplete();
							ToastUtil.show(m_activity, "已无更多商品");
						}
					}, 300);
					return;
				} else {
					map.put("lotteryProductId", listgoods.get(size - 1).getId());
				}
			}
		}
		XHttp.Post(Constant.LAST_LOTTERY, map, new CommonCallback<JSONArray>() {

			@Override
			public void onError(Throwable arg0, boolean arg1) {
				gridView.onRefreshComplete();
			}

			@Override
			public void onSuccess(JSONArray arg0) {
				List<OpenModel> models = new ArrayList<OpenModel>();
				models = JsonUtil.JsonToModel(arg0, OpenModel.class);
				gridView.onRefreshComplete();
				if (models != null || models.size() != 0) {
					if (isFresh) {
						pageNum = 1;
						listgoods = models;
						if (listgoods.size() >= 10) {
							gridView.setMode(Mode.BOTH);
						}
						adapter = new BuyGoodsListAdapter(getActivity(),
								listgoods, gridView,m_loader);
						gridView.setAdapter(adapter);
					} else {
						pageNum++;
						listgoods.addAll(models);
						adapter.setmDatas(listgoods);
						adapter.notifyDataSetChanged();
					}
				}
			}

			@Override
			public void onCancelled(CancelledException arg0) {

			}

			@Override
			public void onFinished() {

			}
		});
	}
	BuyGoodsListAdapter adapter;
	Map<Integer, Boolean> loadmap = new HashMap<Integer, Boolean>();
	public void initView() {
		TextView title = (TextView) view.findViewById(R.id.status_bar_title);
		title.setText(getResources().getString(R.string.the_new));
		ImageButton exitbtn = (ImageButton) view
				.findViewById(R.id.status_bar_Exit);
		exitbtn.setVisibility(View.GONE);
		gridView = (PullToRefreshGridView) view
				.findViewById(R.id.the_new_fragment_gridview);
		gridView.setMode(Mode.PULL_FROM_START);
		gridView.getRefreshableView().setSelector(
				new ColorDrawable(Color.TRANSPARENT));
		gridView.setOnRefreshListener(listener2);
		// listView.setOnScrollListener(new PauseOnScrollListener(imageLoader,
		// pauseOnScroll, pauseOnFling));
		// ImageLoader imageLoader=m_loader.imageLoader;
		// if(imageLoader==null){
		// imageLoader=ImageLoader.getInstance();
		// }
		// gridView.setOnScrollListener(new PauseOnScrollListener(imageLoader,
		// true, false));
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getActivity(),
						OpeningGoodMessageActivity.class);
				OpenModel model = listgoods.get(position);
				intent.putExtra("productId", model.getProductId());
				intent.putExtra("issueId", model.getIssueId());
				startActivity(intent);
			}
		});

	}

	OnRefreshListener2<GridView> listener2 = new OnRefreshListener2<GridView>() {

		public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
			isFresh = true;
			requestLast();
		}

		public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
			isFresh = false;
			requestLast();
		}
	};

	public void setUserVisibleHint(boolean isVisibleToUser) {
		LogUtil.v("TheNewFragment:  setUserVisibleHint"+isVisibleToUser);
		if (isVisibleToUser) {
			if (listgoods == null || listgoods.size() == 0) {
				isFresh = true;
				requestLast();
			}
		}
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
//		LogUtil.v("TheNewFragment:  onDestroyView");
//		if (view != null) {
//			((ViewGroup) view.getParent()).removeView(view);
//		}
	}
	
	@Override
	public void onDestroy() {
		LogUtil.v("TheNewFragment:  onDestroy");
		super.onDestroy();
	}
}
