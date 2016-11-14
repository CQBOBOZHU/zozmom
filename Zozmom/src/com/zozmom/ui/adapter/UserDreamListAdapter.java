package com.zozmom.ui.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chasedream.zhumeng.R;
import com.zozmom.constants.Constant;
import com.zozmom.cuview.CuGridView;
import com.zozmom.manager.AccountManager;
import com.zozmom.model.ProductModel;
import com.zozmom.ui.SubmitActivity;
import com.zozmom.util.Tools;

public class UserDreamListAdapter extends BaseTaskAdaper<ProductModel> {
	int userId;
	/**
	 * 用来判断是否打开或关闭号码查看0关闭 1打开
	 */
	@SuppressLint("UseSparseArrays")
	Map<Integer, Integer> isopenmap = new HashMap<Integer, Integer>();
	/**
	 * 用来记录号码的map
	 */
	@SuppressLint("UseSparseArrays")
	Map<Integer, List<String>> childemap = new HashMap<Integer, List<String>>();
	/**
	 * 用来管理gridview换页页数
	 */
	@SuppressLint("UseSparseArrays")
	Map<Integer, Integer> pagemap = new HashMap<Integer, Integer>();

	public UserDreamListAdapter(Context context, List<ProductModel> mDatas,
			Map<Integer, List<String>> childemap) {
		super(context, mDatas);
		instanceMap(mDatas.size());
		this.childemap = childemap;
		this.userId=AccountManager.getInstance().getLogginUserInfo().getUserId();
	}

	
	@Override
	public void setmDatas(List<ProductModel> mDatas) {
		super.setmDatas(mDatas);
		int mSize=mDatas.size();
		int mPsize=pagemap.size();
		if(mSize>mPsize){
			for(int a=mPsize;a<mSize;a++){
				pagemap.put(a, 1);
				isopenmap.put(a, 0);
			}
		}
	}
	
	
	public Map<Integer, List<String>> getChildemap() {
		return childemap;
	}




	public void setChildemap(Map<Integer, List<String>> childemap) {
		this.childemap = childemap;
	}




	/**
	 * 初始化isopenmap 和 pagemap
	 */
	public void instanceMap(int mapsize) {
		for (int i = 0; i < mapsize; i++) {
			isopenmap.put(i, 0);
			pagemap.put(i, 1);
		}
	}

	Drawable opendraw;
	Drawable closedraw;

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (opendraw == null) {
			opendraw = mContext.getResources().getDrawable(R.drawable.take_on);
			opendraw.setBounds(0, 0, opendraw.getMinimumWidth(),
					opendraw.getMinimumHeight());
		}
		if (closedraw == null) {
			closedraw = mContext.getResources()
					.getDrawable(R.drawable.take_off);
			closedraw.setBounds(0, 0, closedraw.getMinimumWidth(),
					closedraw.getMinimumHeight());
		}

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.userdream_item, null);
			holder.goodimageView = (ImageView) convertView
					.findViewById(R.id.sumit_picimageview);
			holder.goodnameView = (TextView) convertView
					.findViewById(R.id.sumit_name_tv);
			holder.countView = (TextView) convertView
					.findViewById(R.id.userdream_value_tv);
			holder.joinView = (TextView) convertView
					.findViewById(R.id.need_textview);
			holder.openluckNumber = (Button) convertView
					.findViewById(R.id.user_getdreambtn);
			holder.bar = (ProgressBar) convertView
					.findViewById(R.id.userdream_item_bar);
			holder.getedUserName = (TextView) convertView
					.findViewById(R.id.userdream_item_getedusernameview);
			holder.buyagainBtn = (Button) convertView
					.findViewById(R.id.userdream_item_buyagainbtn);
			holder.gmBtn = (Button) convertView
					.findViewById(R.id.userdream_item_gmviewbtn);
			holder.finishView = convertView.findViewById(R.id.finish_layout);
			holder.goingView = convertView.findViewById(R.id.going_layout);
			holder.priceView = (TextView) convertView
					.findViewById(R.id.userdream_item_priceview);
			holder.syView = (TextView) convertView
					.findViewById(R.id.userdream_item_syview);
			holder.cuGridView = (CuGridView) convertView
					.findViewById(R.id.userdream_item_gridview);
			holder.lucklay = convertView.findViewById(R.id.goodmessage_rlayout);
			holder.fBtn = (Button) convertView
					.findViewById(R.id.lucknum_before_btn);
			holder.nextBtn = (Button) convertView
					.findViewById(R.id.lucknum_after_btn);
			holder.isuserComeTrueImageview=(ImageView) convertView.findViewById(R.id.isuser_cometrue_image);
			holder.disImageView=(ImageView) convertView.findViewById(R.id.user_cometrue_disimage);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.fBtn.setTag(position);
		holder.nextBtn.setTag(position);
		holder.buyagainBtn.setTag(position);
		holder.buyagainBtn.setOnClickListener(clickListener);
		holder.gmBtn.setOnClickListener(clickListener);
		holder.gmBtn.setTag(position);
		holder.openluckNumber.setTag(position);
		holder.cuGridView.setTag(position);
		ProductModel proModel = mDatas.get(position);
		String showImag = proModel.getShowImg();
		String proName = proModel.getProductName();
		String proTitle=proModel.getProductTitle();
		int totalCount = proModel.getTotalCount();
		int allbuyCount = proModel.getAllBuyCount();// 全部购买次数
		int issueId = proModel.getIssueId();
		int buycount = proModel.getBuyCount();// 当前这个人购买的次数
		int luckUserId=proModel.getLuckyUserId();
		int prductArea=proModel.getProductArea();
		int status=proModel.getStatus();
		
		
		if(userId==luckUserId){
			holder.isuserComeTrueImageview.setVisibility(View.VISIBLE);
			holder.disImageView.setVisibility(View.GONE);
		}else{
			holder.isuserComeTrueImageview.setVisibility(View.GONE);
			if(prductArea==10){
				holder.disImageView.setVisibility(View.VISIBLE);
				holder.disImageView.setImageResource(R.drawable.ten_yuan);
			}else if(prductArea==100){
				holder.disImageView.setVisibility(View.VISIBLE);
				holder.disImageView.setImageResource(R.drawable.hundred_yuan);
			}else{
				holder.disImageView.setVisibility(View.GONE);
			}
		}
		
		String luckuserName = proModel.getLuckyUserName();
		loadImage(Constant.PRODUCT_URL+showImag+Constant.PRODUCT_KEY,holder.goodimageView);
		holder.goodnameView.setText(proName+proTitle);
		holder.countView.setText(String.valueOf(issueId));
		holder.joinView.setText("参与" + buycount + "人次");
		if (allbuyCount == totalCount) {// 已经购买完
			holder.finishView.setVisibility(View.VISIBLE);
			holder.goingView.setVisibility(View.GONE);
			holder.getedUserName.setText("获得者: " + luckuserName);
			if(status==0){//下架
				holder.buyagainBtn.setBackgroundResource(R.drawable.btn_background_gray);
				holder.buyagainBtn.setClickable(false);
				holder.buyagainBtn.setText("已下架");
			}else if(status==1){//上架
				holder.buyagainBtn.setBackgroundResource(R.drawable.btn_background_yellow);
				holder.buyagainBtn.setClickable(true);
				holder.buyagainBtn.setText("再次购买");
			}
		} else {
			holder.finishView.setVisibility(View.GONE);
			holder.goingView.setVisibility(View.VISIBLE);
			int progress = allbuyCount * 100 / totalCount;
			if (progress == 0)
				progress = 1;
			int sy = totalCount - allbuyCount;
			holder.bar.setProgress(progress);
			holder.priceView.setText("总需" + totalCount + "人次");
			holder.syView.setText("剩余" + sy + "人次");
			if(status==0){//下架
				holder.gmBtn.setBackgroundResource(R.drawable.btn_background_gray);
				holder.gmBtn.setClickable(false);
				holder.gmBtn.setText("已下架");
			}else if(status==1){//上架
				holder.gmBtn.setBackgroundResource(R.drawable.btn_background_yellow);
				holder.gmBtn.setClickable(true);
				holder.gmBtn.setText("追加购买");
			}
		}

		CuAdapter adapter = null;
		List<String> childelist = new ArrayList<String>();
		childelist = childemap.get(position);
		if (childelist == null) {
			adapter = new CuAdapter(mContext, childelist);
		} else {
			int page = pagemap.get(position);
			if (childelist.size() > page * 9) {
				changeBtnTextColor(holder.nextBtn, true);
			} else {
				changeBtnTextColor(holder.nextBtn, false);
			}
			if (page > 1) {
				changeBtnTextColor(holder.fBtn, true);
			} else {
				changeBtnTextColor(holder.fBtn, false);
			}
			childelist = Tools.getList(childelist, (page - 1) * 9, page * 9);
			adapter = new CuAdapter(mContext, childelist);
		}
		holder.cuGridView.setAdapter(adapter);

		if (isopenmap.get(position) == 1) {
			holder.openluckNumber.setText("收起号码");
			holder.openluckNumber.setCompoundDrawables(null, null, closedraw,
					null);
			holder.lucklay.setVisibility(View.VISIBLE);
		} else {
			holder.openluckNumber.setText("查看号码");
			holder.openluckNumber.setCompoundDrawables(null, null, opendraw,
					null);
			holder.lucklay.setVisibility(View.GONE);
			isopenmap.put(position, 0);
		}
		holder.openluckNumber.setOnClickListener(new MyClickListener(holder,
				adapter));
		holder.fBtn.setOnClickListener(new MyClickListener(holder, adapter));
		holder.nextBtn.setOnClickListener(new MyClickListener(holder, adapter));

		return convertView;
	}

	class MyClickListener implements OnClickListener {
		ViewHolder holder;
		CuAdapter gridAdapter;

		public MyClickListener(ViewHolder viewholder, CuAdapter gridAdapter) {
			this.holder = viewholder;
			this.gridAdapter = gridAdapter;
		}

		@Override
		public void onClick(View v) {
			int position = (int) v.getTag();
			switch (v.getId()) {
			case R.id.user_getdreambtn:
				openLuck(v, holder.openluckNumber);
				break;
			case R.id.lucknum_before_btn:
				before(holder.fBtn, holder.nextBtn, position, gridAdapter);
				break;
			case R.id.lucknum_after_btn:
				next(holder.fBtn, holder.nextBtn, position, gridAdapter);
				break;

			default:
				break;
			}
		}

		/**
		 * 处理加载数据.
		 * 
		 * @param v
		 */
		public void openLuck(View v, Button btn) {
			final int position = (int) v.getTag();
			if (isopenmap.get(position) == 1) {
				btn.setText("查看号码");
				btn.setCompoundDrawables(null, null, opendraw, null);
				holder.lucklay.setVisibility(View.GONE);
				isopenmap.put(position, 0);
				//
			} else {
				isopenmap.put(position, 1);
				List<String> list = childemap.get(position);
				holder.lucklay.setVisibility(View.VISIBLE);
				btn.setText("收起号码");
				btn.setCompoundDrawables(null, null, closedraw, null);
				int page = pagemap.get(position);
				list = Tools.getList(list, (page - 1) * 9, page * 9);
				gridAdapter.setmDatas(list);
				gridAdapter.notifyDataSetChanged();
			}
		}

		public void next(Button before, Button nextbtn, int groupposition,
				CuAdapter adapter) {
			List<String> list = childemap.get(groupposition);
			int page = pagemap.get(groupposition);
			if (list.size() > page * 9) {
				List<String> li = Tools.getList(list, page * 9, (page + 1) * 9);
				adapter.setmDatas(li);
				adapter.notifyDataSetChanged();
				if (list.size() - page * 9 < 9) {
					changeBtnTextColor(nextbtn, false);
				} else {
					changeBtnTextColor(nextbtn, true);
				}
				page++;
				if (page > 1) {
					changeBtnTextColor(before, true);
				}
				pagemap.put(groupposition, page);
			}
		}

		public void before(Button before, Button nextbtn, int groupposition,
				CuAdapter adapter) {
			List<String> list = childemap.get(groupposition);
			int page = pagemap.get(groupposition);
			if (page > 1) {
				List<String> li = Tools.getList(list, (page - 1) * 9,
						(page - 2) * 9);
				adapter.setmDatas(li);
				adapter.notifyDataSetChanged();
				page--;
				pagemap.put(groupposition, page);
				if (page == 1) {
					changeBtnTextColor(before, false);
				}
				if (list.size() - (page - 1) * 9 > 9) {
					changeBtnTextColor(nextbtn, true);
				}
			}
		}
	}

	/**
	 * id 0 : 上一页 1: 下一页 bl true: 可点击 false: 不可点击
	 * 
	 * @param id
	 * @param bl
	 */
	public void changeBtnTextColor(Button button, boolean bl) {
		if (bl) {
			button.setTextColor(mContext.getResources().getColor(
					R.color.good_message_textcolors));
			button.setClickable(true);
		} else {
			button.setTextColor(mContext.getResources().getColor(
					R.color.before_btncolor));
			button.setClickable(false);
		}
	}

	OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			final int a = (int) v.getTag();
			ProductModel promodel = mDatas.get(a);
			int proid = promodel.getProductId();
			Intent intent = null;
			switch (v.getId()) {
			case R.id.userdream_item_buyagainbtn:
				intent = new Intent(mContext, SubmitActivity.class);
				break;
			case R.id.userdream_item_gmviewbtn:
				intent = new Intent(mContext, SubmitActivity.class);
				break;
			}
			intent.putExtra("productId", proid);
			mContext.startActivity(intent);
		}
	};

	class ViewHolder {
		ImageView goodimageView;
		TextView goodnameView;
		TextView countView;
		TextView joinView;
		TextView priceView;
		TextView syView;
		Button openluckNumber;
		ProgressBar bar;
		TextView getedUserName;
		Button buyagainBtn;
		Button gmBtn;
		View finishView;
		View goingView;
		CuGridView cuGridView;
		View lucklay;
		Button fBtn;
		Button nextBtn;
		ImageView isuserComeTrueImageview;
		ImageView disImageView;
	}

	class CuAdapter extends BaseTaskAdaper<String> {

		public CuAdapter(Context context, List<String> mDatas) {
			super(context, mDatas);
		}

		@SuppressLint({ "ViewHolder", "InflateParams" })
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.list_item, null);
			TextView textView = (TextView) convertView
					.findViewById(R.id.item_id);
			String a = mDatas.get(position).trim();
			int b = Integer.parseInt(a) + 10000000;
			textView.setText(b + "");
			Log.v("this", "convertView " + position);
			return convertView;
		}

	}
}
