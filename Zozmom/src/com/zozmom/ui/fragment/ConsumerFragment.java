package com.zozmom.ui.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.xutils.common.Callback.CancelledException;
import org.xutils.common.Callback.CommonCallback;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.chasedream.zhumeng.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.zozmom.constants.Constant;
import com.zozmom.model.ConsumeModel;
import com.zozmom.model.UserInfoModel;
import com.zozmom.net.XHttp;
import com.zozmom.ui.adapter.BaseTaskAdaper;
import com.zozmom.ui.user.AccountActivity;
import com.zozmom.util.JsonUtil;
import com.zozmom.util.NetWorkUtil;
import com.zozmom.util.RequestErrorUtil;
import com.zozmom.util.ToastUtil;
import com.zozmom.util.Util;

/**
 * 消费记录
 * @author Administrator
 *
 */
public	class ConsumerFragment extends BaseFragment {
		PullToRefreshListView MlistView;
		boolean isFresh = true;// true 下拉 false 上啦
		private static final int maxSize = 10;
		int pageNum = 1;
		UserInfoModel infoModel;
		public ConsumerFragment(UserInfoModel infoModel){
			this.infoModel=infoModel;
		}
		

		@SuppressLint("InflateParams")
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View view = LayoutInflater.from(getActivity()).inflate(
					R.layout.list_fragment, null);
			MlistView = (PullToRefreshListView) view
					.findViewById(R.id.list_listview);
			MlistView.setMode(Mode.PULL_FROM_START);
			MlistView.setOnRefreshListener(onRefreshListener2);
			return view;
		}
		
		OnRefreshListener2<ListView> onRefreshListener2=new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				isFresh=true;
				getList();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				isFresh=false;
				getList();
			}
		};
		
		
		public void getList() {
			if (!NetWorkUtil.isNetworkAvailable(getActivity())) {
//				MlistView.onRefreshComplete();
				MlistView.postDelayed(new Runnable() {
		            @Override
		            public void run() {
		            	ToastUtil.show(m_activity, "没有更多数据");
		            	MlistView.onRefreshComplete();
		            }
		        }, 200);
				ToastUtil.show(m_activity, "网络错误");
				return;
			}
			Map<String, Object> map = new HashMap<String, Object>();
			if (!isFresh) {
				int size = mlist.size();
				if (size < pageNum * maxSize) {
					ToastUtil.show(m_activity, "没有更多数据");
					MlistView.postDelayed(new Runnable() {
			            @Override
			            public void run() {
			            	ToastUtil.show(m_activity, "没有更多数据");
			            	MlistView.onRefreshComplete();
			            }
			        }, 200);
//					MlistView.onRefreshComplete();
					return;
				} else {
					map.put("recordId", mlist.get(size - 1).getId());
				}
			}
			XHttp.PostByToken(Constant.GET_CONSUNME_RECORD, infoModel, map,
					new CommonCallback<JSONArray>() {

						@Override
						public void onCancelled(CancelledException arg0) {

						}

						@Override
						public void onError(Throwable arg0, boolean arg1) {
							MlistView.onRefreshComplete();
							RequestErrorUtil.judge(arg0, m_activity);
							
						}

						@Override
						public void onFinished() {

						}

						@Override
						public void onSuccess(JSONArray arg0) {
							MlistView.onRefreshComplete();
							Log.v("this", "arg" + arg0);
							List<ConsumeModel> li = JsonUtil.JsonToModel(arg0,
									ConsumeModel.class);
							if (li != null && li.size() > 0) {
								if (isFresh) {
									mlist = li;
									pageNum = 1;
									if (mlist.size() >= maxSize) {
										MlistView.setMode(Mode.BOTH);
									}
									listAdapter = new RechargeListAdapter(
											m_activity, mlist);
									MlistView.setAdapter(listAdapter);
								} else {
									pageNum++;
									mlist.addAll(li);
									listAdapter.setmDatas(mlist);
									listAdapter.notifyDataSetChanged();
								}
							}
						}
					});
		}
		List<ConsumeModel> mlist = new ArrayList<ConsumeModel>();
		RechargeListAdapter listAdapter;

		@Override
		public void setUserVisibleHint(boolean isVisibleToUser) {
			// TODO Auto-generated method stub
			super.setUserVisibleHint(isVisibleToUser);
			if (isVisibleToUser) {
				if (mlist == null || mlist.size() == 0) {
					isFresh = true;
					getList();
				}
			}
		}
		
		
		/**
		 * 消费记录adapter
		 * 
		 * @author Administrator
		 * 
		 */
		class RechargeListAdapter extends BaseTaskAdaper<ConsumeModel> {

			public RechargeListAdapter(Context context, List<ConsumeModel> mDatas) {
				super(context, mDatas);
			}

			@SuppressLint({"ViewHolder", "InflateParams"})
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				convertView = mInflater.inflate(R.layout.account_rechargelist_item,
						null);
				TextView timeView = (TextView) convertView
						.findViewById(R.id.account_recharge_timeview);
				TextView priceView = (TextView) convertView
						.findViewById(R.id.account_recharge_priceView);
				ConsumeModel model = mDatas.get(position);
				long payTime = model.getPayTime();
				int coin = model.getDreamCoin();
				timeView.setText(Util.GetDataTime(payTime));
				priceView.setText(coin + "个逐梦币");
				return convertView;
			}

		}
	}