package com.zozmom.ui.user;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.chasedream.zhumeng.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zozmom.constants.Constant;
import com.zozmom.model.ConsumeModel;
import com.zozmom.model.UserInfoModel;
import com.zozmom.net.XHttp;
import com.zozmom.ui.BaseTaskActivity;
import com.zozmom.ui.adapter.BaseTaskAdaper;
import com.zozmom.util.JsonUtil;
import com.zozmom.util.NetWorkUtil;
import com.zozmom.util.RequestErrorUtil;
import com.zozmom.util.ToastUtil;
import com.zozmom.util.Util;

/**
 * 佣金记录
 * 
 * @author Administrator
 * 
 */
public class CommRecordActivity extends BaseTaskActivity<JSONArray> {
	TextView titleView;
	PullToRefreshListView refreshListView;
	List<ConsumeModel> mlist = new ArrayList<ConsumeModel>();
	UserInfoModel infoModel;
	private static final int maxSize=10;
	private int pageNum=1;
	boolean isFresh=true;//true 下拉 false 上啦
	
	CommListAdapter commListAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comm_record_activity);
		titleView = (TextView) findViewById(R.id.status_bar_title);
		titleView.setText("佣金记录");
		infoModel=getLogginUserinfo();
		refreshListView = (PullToRefreshListView) findViewById(R.id.comm_listview);
		refreshListView.getRefreshableView().setSelector(
				new ColorDrawable(Color.TRANSPARENT));
		refreshListView.setMode(Mode.PULL_FROM_START);
		refreshListView.setOnRefreshListener(listener);
		requesList();
	}
	
	OnRefreshListener2<ListView> listener=new OnRefreshListener2<ListView>() {

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			isFresh=true;
			requesList();
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			isFresh=false;
			requesList();
		}
	};
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.status_bar_Exit :
				finish();
				break;
		}
	}
	/**
	 * 获取明细
	 */
	public void requesList(){
		Map<String,Object> map=new HashMap<String, Object>();
		if(!NetWorkUtil.isNetworkAvailable(this)){
			ToastUtil.show(this, "网络错误");
			return;
		}
		if(!isFresh){
			int size=mlist.size();
			if(size<pageNum*maxSize){
				refreshListView.postDelayed(new Runnable() {
		            @Override
		            public void run() {
		            	refreshListView.onRefreshComplete();
		            	ToastUtil.show(m_activity, "没有更多数据");
		            }
		        }, 300);
				return;
			}else{
				map.put("recordId", mlist.get(size-1).getId());
			}
		}
		XHttp.PostByToken(Constant.GEY_brokerage_RECORD, infoModel, map, new CommonCallback<JSONArray>() {

			@Override
			public void onCancelled(CancelledException arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onError(Throwable arg0, boolean arg1) {
				Log.v("this", "onError"+arg0.toString());
				refreshListView.onRefreshComplete();
				RequestErrorUtil.judge(arg0, m_activity);
			}

			@Override
			public void onFinished() {
				
			}

			@Override
			public void onSuccess(JSONArray arg0) {
				Log.v("this", arg0.toString());
				refreshListView.onRefreshComplete();
				List<ConsumeModel> list=JsonUtil.JsonToModel(arg0, ConsumeModel.class);
				if(list!=null&&list.size()>0){
					if(isFresh){
						mlist=list;
						pageNum=1;
						if(mlist.size()>=maxSize){
							refreshListView.setMode(Mode.BOTH);
						}
						commListAdapter=new CommListAdapter(m_activity, mlist);
						refreshListView.setAdapter(commListAdapter);
					}else{
						mlist.addAll(list);
						pageNum++;
						commListAdapter.setmDatas(mlist);
						commListAdapter.notifyDataSetChanged();
					}
					
				}
				
			}
		});
		
	}
	
	

	class CommListAdapter extends BaseTaskAdaper<ConsumeModel> {

		public CommListAdapter(Context context, List<ConsumeModel> mDatas) {
			super(context, mDatas);
		}

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.comm_listview_item,
						null);
				holder.commTypeView = (TextView) convertView
						.findViewById(R.id.cmm_typeView);
				holder.commPayView = (TextView) convertView
						.findViewById(R.id.cmm_pay_hintview);
				holder.moneyView = (TextView) convertView
						.findViewById(R.id.cmm_money_view);
				holder.timeView = (TextView) convertView
						.findViewById(R.id.cmm_tiem_view);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
//			 0:晒单增加  1:下级用户充值增加   2:转换逐梦币  3: 提现  4 活动赠送
//			long time = System.currentTimeMillis();
			ConsumeModel model=mDatas.get(position);
			int amount=model.getAmount();
			long createTime=model.getCreateTime();
			int source=model.getSource();
			if(source==0){
				holder.commTypeView.setText("晒单");
			}else if(source==1){
				holder.commTypeView.setText("Ta人充值");
			}else if(source==2){
				holder.commTypeView.setText("兑换逐梦币");
			}else if(source==3){
				holder.commTypeView.setText("提现");
			}else if(source==4){
				holder.commTypeView.setText("活动");
			}
			DecimalFormat df = new DecimalFormat("0.00");//格式化小数，不足的补0
//	        System.out.println(df.format((float)amount/100));
//	       String a= df.format((float)amount/100);
			holder.moneyView.setText(df.format((float)amount/100));
			holder.timeView.setText(Util.GetDataTime3(createTime));
			if(amount<0){
				holder.moneyView.setTextColor(getRColor(R.color.money_color));
			}else{
				holder.moneyView.setTextColor(getRColor(R.color.background));
			}

			return convertView;
		}

		class ViewHolder {
			TextView commTypeView;// 类型
			TextView commPayView;// 消费消息
			TextView moneyView;// 钱
			TextView timeView;// 时间
		}
	}
}
