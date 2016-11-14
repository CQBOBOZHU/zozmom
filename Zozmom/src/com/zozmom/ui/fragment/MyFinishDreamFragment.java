package com.zozmom.ui.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.xutils.common.Callback.CancelledException;
import org.xutils.common.Callback.CommonCallback;

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

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.chasedream.zhumeng.R;
import com.zozmom.constants.Constant;
import com.zozmom.model.ProductModel;
import com.zozmom.model.UserInfoModel;
import com.zozmom.net.XHttp;
import com.zozmom.ui.GoodMessageActivity;
import com.zozmom.ui.OpeningGoodMessageActivity;
import com.zozmom.ui.adapter.UserDreamListAdapter;
import com.zozmom.util.JsonUtil;
import com.zozmom.util.NetWorkUtil;
import com.zozmom.util.ToastUtil;

/**
 * 我的逐梦-- 已经完成的逐梦记录
 * 
 * @author Administrator
 * 
 */
public class MyFinishDreamFragment extends BaseTaskFragment {
	View view;
	PullToRefreshListView listView;
	int userId;
	UserInfoModel infoModel;
	
	List<ProductModel> proList=new ArrayList<ProductModel>();
	@SuppressLint("UseSparseArrays")
	Map<Integer, List<String>> childmap = new HashMap<Integer, List<String>>();
	UserDreamListAdapter dreamListAdapter;
	public static final int maxSize=10;
	private int pageNum=1;
	boolean isFresh=true;//true表示下拉刷新, false表示上啦刷新
	
	public MyFinishDreamFragment(){
		
	}
	
	public MyFinishDreamFragment(UserInfoModel infoModel) {
		this.infoModel = infoModel;
		userId = infoModel.getUserId();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = LayoutInflater.from(getActivity()).inflate(
				R.layout.userdream_fragment, null);
		listView = (PullToRefreshListView) view
				.findViewById(R.id.userdream_pull_listview);
		listView.setMode(Mode.PULL_FROM_START);
		listView.setOnItemClickListener(itemListener);
		listView.setOnRefreshListener(refreshListener);
		return view;
	}
	
	OnRefreshListener2<ListView> refreshListener=new OnRefreshListener2<ListView>() {

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			isFresh=true;
			requesUser();
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			isFresh=false;
			requesUser();
		}
	};
	
	OnItemClickListener itemListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			ProductModel model = proList.get(position - 1);
			int productId = model.getProductId();
			int issueId = model.getIssueId();
			int status=model.getStatus();
			Intent intent = new Intent(getActivity(),
					OpeningGoodMessageActivity.class);
			intent.putExtra("productId", productId);
			intent.putExtra("issueId", issueId);
			intent.putExtra("status", status);
			getActivity().startActivity(intent);
		}
	};

	public void requesUser() {
		if(!NetWorkUtil.isNetworkAvailable(m_activity)){
			listView.postDelayed(new Runnable() {
	            @Override
	            public void run() {
	            	listView.onRefreshComplete();
	            }
	        }, 300);
			return;
		}
		String url = Constant.USER_DREAM_RECORD_LIST + userId;
		Map<String, Object> map = new HashMap<String, Object>();
		
		if(!isFresh){
			int size=proList.size();
			if(size==0){
				isFresh=true;
			}else{
				if(size<pageNum*maxSize){
					listView.postDelayed(new Runnable() {
			            @Override
			            public void run() {
			            	listView.onRefreshComplete();
			            	ToastUtil.show(m_activity, "无更多商品");
			            }
			        }, 300);
					return;
				}else{
					map.put("latestId", proList.get(size-1).getId());
				}
			}
		}
		map.put("type", "lottery");
		XHttp.Post( url, map, new CommonCallback<JSONArray>() {

			@Override
			public void onCancelled(CancelledException arg0) {

			}

			@Override
			public void onError(Throwable arg0, boolean arg1) {
				Log.v("this", "onError" + arg0.toString());
				listView.onRefreshComplete();
			}

			@Override
			public void onFinished() {

			}

			@Override
			public void onSuccess(JSONArray arg0) {
				Log.v("this", "onSuccess" + arg0.toString());
//				proList = JsonUtil.JsonToModel(arg0, ProductModel.class);
//				parseChildLuckNum(proList);
//				listView.setAdapter(new UserDreamListAdapter(getActivity(),
//						proList, childmap));
//				listView.setMode(Mode.PULL_FROM_END);

				listView.onRefreshComplete();
				List<ProductModel> proModels=JsonUtil.JsonToModel(arg0, ProductModel.class);
				if(isFresh){
					if(proModels!=null&&proModels.size()>0){
						pageNum=1;
						proList = proModels;
						if(proList.size()>=maxSize){
							listView.setMode(Mode.BOTH);
						}
						parseChildLuckNum(proList,0);
						dreamListAdapter=new UserDreamListAdapter(getActivity(),
								proList, childmap);
						listView.setAdapter(dreamListAdapter);
					}
				}else{
					if(proModels!=null&&proModels.size()>0){
						pageNum++;
						parseChildLuckNum(proModels,proList.size());
						proList.addAll(proModels);
						dreamListAdapter.setmDatas(proList);
						dreamListAdapter.setChildemap(childmap);
						dreamListAdapter.notifyDataSetChanged();
					}else{
						ToastUtil.show(m_activity, "没有更多数据");
					}
				}
			
			}
		});
	}


	public void parseChildLuckNum(List<ProductModel> luckModels,int index) {
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

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		// TODO Auto-generated method stub
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			if(proList==null||proList.size()==0){
				requesUser();
			}
		}
	}
}
