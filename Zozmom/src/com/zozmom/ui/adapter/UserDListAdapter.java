//package com.zozmom.ui.adapter;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.drawable.Drawable;
//import android.os.Handler;
//import android.os.Message;
//import android.support.v4.widget.SearchViewCompat.OnCloseListenerCompat;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//
//import com.chasedream.zhumeng.R;
//import com.zozmom.constants.UrlConstants;
//import com.zozmom.cuview.CuGridView;
//import com.zozmom.ui.SubmitActivity;
//import com.zozmom.util.ToastUtil;
//import com.zozmom.util.Tools;
//
///**
// * 新的 不需要异步加载的
// * 
// * @author Administrator
// * 
// */
//public class UserDListAdapter extends BaseTaskAdaper<String> {
//
//	/**
//	 * 用来判断是否打开或关闭号码查看0关闭 1打开
//	 */
//	Map<Integer, Integer> isopenmap = new HashMap<Integer, Integer>();
//	/**
//	 * 用来记录号码的map
//	 */
//	Map<Integer, List<String>> childemap = new HashMap<Integer, List<String>>();
//	/**
//	 * 用来管理gridview换页页数
//	 */
//	Map<Integer, Integer> pagemap = new HashMap<Integer, Integer>();
//
//	public UserDListAdapter(Context context, List<String> mDatas,
//			Map<Integer, List<String>> childemap) {
//		super(context, mDatas);
//		// TODO Auto-generated constructor stub
//		instanceMap(mDatas.size());
//		this.childemap = childemap;
//	}
//
//	/**
//	 * 初始化isopenmap 和 pagemap
//	 */
//	public void instanceMap(int mapsize) {
//		for (int i = 0; i < mapsize; i++) {
//			isopenmap.put(i, 0);
//			pagemap.put(i, 1);
//		}
//	}
//
//	Drawable opendraw;
//	Drawable closedraw;
//
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//		// TODO Auto-generated method stub
//		ViewHolder holder;
//
//		if (opendraw == null) {
//			opendraw = mContext.getResources().getDrawable(R.drawable.take_on);
//			opendraw.setBounds(0, 0, opendraw.getMinimumWidth(),
//					opendraw.getMinimumHeight());
//		}
//		if (closedraw == null) {
//			closedraw = mContext.getResources()
//					.getDrawable(R.drawable.take_off);
//			closedraw.setBounds(0, 0, closedraw.getMinimumWidth(),
//					closedraw.getMinimumHeight());
//		}
//
//		if (convertView == null) {
//			holder = new ViewHolder();
//			convertView = LayoutInflater.from(mContext).inflate(
//					R.layout.userdream_item, null);
//			holder.goodimageView = (ImageView) convertView
//					.findViewById(R.id.sumit_picimageview);
//			holder.goodnameView = (TextView) convertView
//					.findViewById(R.id.sumit_name_tv);
//			holder.countView = (TextView) convertView
//					.findViewById(R.id.userdream_value_tv);
//			holder.joinView = (TextView) convertView
//					.findViewById(R.id.need_textview);
//			holder.openluckNumber = (Button) convertView
//					.findViewById(R.id.user_getdreambtn);
//			holder.bar = (ProgressBar) convertView
//					.findViewById(R.id.userdream_item_bar);
//			holder.getedUserName = (TextView) convertView
//					.findViewById(R.id.userdream_item_getedusernameview);
//			holder.buyagainBtn = (Button) convertView
//					.findViewById(R.id.userdream_item_buyagainbtn);
//			holder.gmBtn = (Button) convertView
//					.findViewById(R.id.userdream_item_gmviewbtn);
//			holder.finishView = convertView.findViewById(R.id.finish_layout);
//			holder.goingView = convertView.findViewById(R.id.going_layout);
//			holder.priceView = (TextView) convertView
//					.findViewById(R.id.userdream_item_priceview);
//			holder.syView = (TextView) convertView
//					.findViewById(R.id.userdream_item_syview);
//			holder.cuGridView = (CuGridView) convertView
//					.findViewById(R.id.userdream_item_gridview);
//			holder.lucklay = convertView.findViewById(R.id.goodmessage_rlayout);
//			holder.fBtn = (Button) convertView
//					.findViewById(R.id.lucknum_before_btn);
//			holder.nextBtn = (Button) convertView
//					.findViewById(R.id.lucknum_after_btn);
//			convertView.setTag(holder);
//		} else {
//			holder = (ViewHolder) convertView.getTag();
//		}
//		holder.fBtn.setTag(position);
//		holder.nextBtn.setTag(position);
//		holder.buyagainBtn.setTag(position);
//		holder.buyagainBtn.setOnClickListener(clickListener);
//		holder.gmBtn.setOnClickListener(clickListener);
//		holder.gmBtn.setTag(position);
//		holder.openluckNumber.setTag(position);
//		holder.cuGridView.setTag(position);
//
//		String url = "/productImg/show/1052/1457934918700.jpg";
//		loadImage(UrlConstants.PIC_URL + url, holder.goodimageView);
//		if (mDatas.get(position).equals("1")) {
//			holder.finishView.setVisibility(View.VISIBLE);
//			holder.goingView.setVisibility(View.GONE);
//		} else {
//			holder.finishView.setVisibility(View.GONE);
//			holder.goingView.setVisibility(View.VISIBLE);
//		}
//
//		GridAdapter adapter = null;
//		List<String> childelist = new ArrayList<String>();
//		childelist = childemap.get(position);
//		if (childelist == null) {
//			adapter = new GridAdapter(mContext, childelist);
//		} else {
//			int page = pagemap.get(position);
//			if (childelist.size() > page * 9) {
//				changeBtnTextColor(holder.nextBtn, true);
//			} else {
//				changeBtnTextColor(holder.nextBtn, false);
//			}
//			if (page > 1) {
//				changeBtnTextColor(holder.fBtn, true);
//			} else {
//				changeBtnTextColor(holder.fBtn, false);
//			}
//			childelist = Tools.getList(childelist, (page - 1) * 9, page * 9);
//			adapter = new GridAdapter(mContext, childelist);
//		}
//		holder.cuGridView.setAdapter(adapter);
//
//		if (isopenmap.get(position) == 1) {
//			holder.openluckNumber.setText("收起号码");
//			holder.openluckNumber.setCompoundDrawables(null, null, closedraw,
//					null);
//			holder.lucklay.setVisibility(View.VISIBLE);
//		} else {
//			holder.openluckNumber.setText("查看号码");
//			holder.openluckNumber.setCompoundDrawables(null, null, opendraw,
//					null);
//			holder.lucklay.setVisibility(View.GONE);
//			isopenmap.put(position, 0);
//		}
//		holder.openluckNumber.setOnClickListener(new MyClickListener(holder,
//				adapter));
//		holder.fBtn.setOnClickListener(new MyClickListener(holder, adapter));
//		holder.nextBtn.setOnClickListener(new MyClickListener(holder, adapter));
//
//		return convertView;
//	}
//
//	class MyClickListener implements OnClickListener {
//		ViewHolder holder;
//		GridAdapter gridAdapter;
//
//		public MyClickListener(ViewHolder viewholder, GridAdapter gridAdapter) {
//			this.holder = viewholder;
//			this.gridAdapter = gridAdapter;
//		}
//
//		@Override
//		public void onClick(View v) {
//			// TODO Auto-generated method stub
//			int position = (int) v.getTag();
//			switch (v.getId()) {
//			case R.id.user_getdreambtn:
//				openLuck(v, holder.openluckNumber);
//				break;
//			case R.id.lucknum_before_btn:
//				before(holder.fBtn, holder.nextBtn, position, gridAdapter);
//				break;
//			case R.id.lucknum_after_btn:
//				next(holder.fBtn, holder.nextBtn, position, gridAdapter);
//				break;
//
//			default:
//				break;
//			}
//		}
//
//		/**
//		 * 处理加载数据.
//		 * 
//		 * @param v
//		 */
//		public void openLuck(View v, Button btn) {
//			final int position = (int) v.getTag();
//			if (isopenmap.get(position) == 1) {
//				btn.setText("查看号码");
//				btn.setCompoundDrawables(null, null, opendraw, null);
//				holder.lucklay.setVisibility(View.GONE);
//				isopenmap.put(position, 0);
//				//
//			} else {
//				isopenmap.put(position, 1);
//				List<String> list = childemap.get(position);
//				holder.lucklay.setVisibility(View.VISIBLE);
//				btn.setText("收起号码");
//				btn.setCompoundDrawables(null, null, closedraw, null);
//				int page = pagemap.get(position);
//				list = Tools.getList(list, (page - 1) * 9, page * 9);
//				gridAdapter.setmDatas(list);
//				gridAdapter.notifyDataSetChanged();
//			}
//		}
//
//		public void next(Button before, Button nextbtn, int groupposition,
//				GridAdapter adapter) {
//			List<String> list = childemap.get(groupposition);
//			int page = pagemap.get(groupposition);
//			if (list.size() > page * 9) {
//				List<String> li = Tools.getList(list, page * 9, (page + 1) * 9);
//				adapter.setmDatas(li);
//				adapter.notifyDataSetChanged();
//				if (list.size() - page * 9 < 9) {
//					changeBtnTextColor(nextbtn, false);
//				} else {
//					changeBtnTextColor(nextbtn, true);
//				}
//				page++;
//				if (page > 1) {
//					changeBtnTextColor(before, true);
//				}
//				pagemap.put(groupposition, page);
//			}
//		}
//
//		public void before(Button before, Button nextbtn, int groupposition,
//				GridAdapter adapter) {
//			List<String> list = childemap.get(groupposition);
//			int page = pagemap.get(groupposition);
//			if (page > 1) {
//				List<String> li = Tools.getList(list, (page - 1) * 9,
//						(page - 2) * 9);
//				adapter.setmDatas(li);
//				adapter.notifyDataSetChanged();
//				page--;
//				pagemap.put(groupposition, page);
//				if (page == 1) {
//					changeBtnTextColor(before, false);
//				}
//				if (list.size() - (page - 1) * 9 > 9) {
//					changeBtnTextColor(nextbtn, true);
//				}
//			}
//		}
//	}
//
//	/**
//	 * id 0 : 上一页 1: 下一页 bl true: 可点击 false: 不可点击
//	 * 
//	 * @param id
//	 * @param bl
//	 */
//	public void changeBtnTextColor(Button button, boolean bl) {
//		if (bl) {
//			button.setTextColor(mContext.getResources().getColor(
//					R.color.good_message_textcolors));
//			button.setClickable(true);
//		} else {
//			button.setTextColor(mContext.getResources().getColor(
//					R.color.before_btncolor));
//			button.setClickable(false);
//		}
//	}
//
//	OnClickListener clickListener = new OnClickListener() {
//
//		@Override
//		public void onClick(View v) {
//			// TODO Auto-generated method stub
//			final int a = (int) v.getTag();
//			switch (v.getId()) {
//			case R.id.userdream_item_buyagainbtn:
//				ToastUtil.show(mContext, "这 是 item" + a);
//				Intent intent = new Intent(mContext, SubmitActivity.class);
//				mContext.startActivity(intent);
//				break;
//			case R.id.userdream_item_gmviewbtn:
//				int a1 = (int) v.getTag();
//				ToastUtil.show(mContext, "这 是 item" + a1);
//				Intent intent1 = new Intent(mContext, SubmitActivity.class);
//				mContext.startActivity(intent1);
//				break;
//			}
//		}
//	};
//
//	class ViewHolder {
//		ImageView goodimageView;
//		TextView goodnameView;
//		TextView countView;
//		TextView joinView;
//		TextView priceView;
//		TextView syView;
//		Button openluckNumber;
//		ProgressBar bar;
//		TextView getedUserName;
//		Button buyagainBtn;
//		Button gmBtn;
//		View finishView;
//		View goingView;
//		CuGridView cuGridView;
//		View lucklay;
//		Button fBtn;
//		Button nextBtn;
//	}
//
//	class CuAdapter extends BaseTaskAdaper<String> {
//
//		public CuAdapter(Context context, List<String> mDatas) {
//			super(context, mDatas);
//			// TODO Auto-generated constructor stub
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			// TODO Auto-generated method stub
//			convertView = LayoutInflater.from(mContext).inflate(
//					R.layout.list_item, null);
//			TextView textView = (TextView) convertView
//					.findViewById(R.id.item_id);
//			textView.setText(mDatas.get(position) + "");
//			Log.v("this", "convertView " + position);
//			return convertView;
//		}
//
//	}
//}
