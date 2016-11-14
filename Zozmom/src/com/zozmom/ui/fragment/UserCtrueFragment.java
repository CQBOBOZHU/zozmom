package com.zozmom.ui.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.chasedream.zhumeng.R;
import com.zozmom.constants.Constant;
import com.zozmom.model.ComeTrueModel;
import com.zozmom.net.XHttp;
import com.zozmom.ui.OpeningGoodMessageActivity;
import com.zozmom.ui.adapter.BaseTaskAdaper;
import com.zozmom.util.JsonUtil;
import com.zozmom.util.NetWorkUtil;
import com.zozmom.util.RequestErrorUtil;
import com.zozmom.util.ToastUtil;
import com.zozmom.util.Util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class UserCtrueFragment extends BaseTaskFragment {
	View view;
	PullToRefreshListView plistview;
	int userId;
	public static final int maxSize=10;
	private int pageNum=0;
	private boolean isFresh=true;//true表示下拉  false表示上啦

	List<ComeTrueModel> mlist = new ArrayList<ComeTrueModel>();
	ListAdapter listAdapter;

	public UserCtrueFragment(int userId) {
		this.userId = userId;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = LayoutInflater.from(getActivity()).inflate(
				R.layout.userdream_fragment, null);
		initView();
		return view;
	}
	
	
	
	public void initView() {
		plistview = (PullToRefreshListView) view
				.findViewById(R.id.userdream_pull_listview);
		plistview.setMode(Mode.PULL_FROM_START);
		plistview.setOnItemClickListener(itemClickListener);
		plistview.setOnRefreshListener(refreshListener);
	}
	
	OnRefreshListener2<ListView> refreshListener=new OnRefreshListener2<ListView>() {

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			isFresh=true;
			requestPost();
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			isFresh=false;
			requestPost();
		}
	};
	
	OnItemClickListener itemClickListener=new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent intent=new Intent(getActivity(),OpeningGoodMessageActivity.class);
			ComeTrueModel model=mlist.get(position-1);
			int productId=model.getProductId();
			int issueId=model.getIssueId();
			int status=model.getStatus();
			intent.putExtra("productId", productId);
			intent.putExtra("issueId", issueId);
			intent.putExtra("status", status);
			getActivity().startActivity(intent);
		}
	};

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			if (mlist == null || mlist.size() == 0) {
				isFresh=true;
				requestPost();
			}
		}
	}

	public void requestPost() {
		if(!NetWorkUtil.isNetworkAvailable(m_activity)){
			plistview.postDelayed(new Runnable() {
	            @Override
	            public void run() {
	            	plistview.onRefreshComplete();
	            }
	        }, 300);
			return;
		}
		Map<String, Object> map=new HashMap<String, Object>();
		if(!isFresh){
			int size=mlist.size();
			if(size==0){
				isFresh=true;
			}else{
				if(size<pageNum*maxSize){
					plistview.postDelayed(new Runnable() {
			            @Override
			            public void run() {
			            	ToastUtil.show(m_activity, "没有更多数据");
			            	plistview.onRefreshComplete();
			            }
			        }, 300);
					return;
				}else{
					map.put("latestId", mlist.get(size - 1).getId());
				}
			}
		}
		
		XHttp.Post(Constant.LUCK_DREAM + userId, map,
				new CommonCallback<JSONArray>() {

					@Override
					public void onCancelled(CancelledException arg0) {

					}

					@Override
					public void onError(Throwable arg0, boolean arg1) {
						plistview.onRefreshComplete();
						RequestErrorUtil.judge(arg0, getActivity());
					}

					@Override
					public void onFinished() {

					}

					@Override
					public void onSuccess(JSONArray arg0) {
						plistview.onRefreshComplete();
						List<ComeTrueModel> mData = JsonUtil.JsonToModel(arg0, ComeTrueModel.class);
						if(mData!=null&&mData.size()>0){
							if(isFresh){
								pageNum=1;
								mlist=mData;
								if(mlist.size()>=maxSize){
									plistview.setMode(Mode.BOTH);
								}
								listAdapter = new ListAdapter(m_activity, mlist);
								plistview.setAdapter(listAdapter);
							}else{
								pageNum++;
								mlist.addAll(mData);
								listAdapter.setmDatas(mlist);
								listAdapter.notifyDataSetChanged();
							}
						}
					}
				});
	}

	class ListAdapter extends BaseTaskAdaper<ComeTrueModel> {

		public ListAdapter(Context context, List<ComeTrueModel> mDatas) {
			super(context, mDatas);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = LayoutInflater.from(getActivity()).inflate(
						R.layout.userctrue_item, null);
				viewHolder.goodimageView = (ImageView) convertView
						.findViewById(R.id.good_imageview);
				viewHolder.goodnameView = (TextView) convertView
						.findViewById(R.id.good_name_tv);
				viewHolder.countView = (TextView) convertView
						.findViewById(R.id.good_num);
				viewHolder.usernameView = (TextView) convertView
						.findViewById(R.id.good_value_tv);
				viewHolder.joinnumberView = (TextView) convertView
						.findViewById(R.id.good_c_tv);
				viewHolder.timeView = (TextView) convertView
						.findViewById(R.id.good_c_time);
				viewHolder.lucknumberView = (TextView) convertView
						.findViewById(R.id.number_textview);
				viewHolder.disImageView=(ImageView) convertView.findViewById(R.id.good_ucdisview);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			ComeTrueModel trueModel = mDatas.get(position);
			String productImage = trueModel.getProductImg();
			String productTitle = trueModel.getProductTitle();
			String productName=trueModel.getProductName();
			int allcount=trueModel.getAllCount();
			int issueId = trueModel.getIssueId();
			int buycount = trueModel.getUserBuyCount();
			long time = trueModel.getAnnouncedTime();
			int productArea=trueModel.getProductArea();
			if(productArea==10){
				viewHolder.disImageView.setVisibility(View.VISIBLE);
				viewHolder.disImageView.setImageResource(R.drawable.ten_yuan);
			}else if(productArea==100){
				viewHolder.disImageView.setVisibility(View.VISIBLE);
				viewHolder.disImageView.setImageResource(R.drawable.hundred_yuan);
			}else{
				viewHolder.disImageView.setVisibility(View.GONE);
			}
			String luckNum = trueModel.getLuckyNumber();
			String data = Util.GetDataTime1(time);
			viewHolder.goodnameView.setText(productName);
			viewHolder.countView.setText(String.valueOf(issueId));
			viewHolder.joinnumberView.setText(String.valueOf(buycount));
			viewHolder.timeView.setText(data);
			viewHolder.usernameView.setText(allcount+"");
			int luntemp = 10000000 + Integer.parseInt(luckNum);
			viewHolder.lucknumberView.setText("幸运逐梦码: " + luntemp);
			loadImage(Constant.PRODUCT_URL + productImage + Constant.PRODUCT_KEY,
					viewHolder.goodimageView);

			return convertView;
		}

		class ViewHolder {
			ImageView goodimageView;
			TextView goodnameView;
			TextView countView;
			TextView usernameView;
			TextView joinnumberView;
			TextView timeView;
			TextView lucknumberView;
			ImageView disImageView;
		}
	}
}
