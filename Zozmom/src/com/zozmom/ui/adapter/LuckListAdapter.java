//package com.zozmom.ui.adapter;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Handler;
//import android.os.Message;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.GridView;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.chasedream.zhumeng.R;
//import com.zozmom.cuview.CuGridView;
//import com.zozmom.ui.SubmitShowActivity;
//import com.zozmom.ui.adapter.GoodExAdapter.gridAdapter;
//import com.zozmom.util.Tools;
//
//public class LuckListAdapter extends BaseTaskAdaper<String> {
//	/**
//	 * 用来判断是否打开或关闭号码查看0关闭 1打开
//	 */
//	Map<Integer, Integer> isopenmap = new HashMap<Integer, Integer>();
//	/**
//	 * 用来记录号码的map
//	 */
//	Map<Integer, List<String>> map = new HashMap<Integer, List<String>>();
//	/**
//	 * 用来管理gridview换页页数
//	 */
//	Map<Integer, Integer> pagemap = new HashMap<Integer, Integer>();
//
//	public LuckListAdapter(Context context, List<String> mDatas) {
//		super(context, mDatas);
//		instanceMap(mDatas.size());
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
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//		Viewholder viewholder;
//		if (convertView == null) {
//			viewholder = new Viewholder();
//			convertView = LayoutInflater.from(mContext).inflate(
//					R.layout.user_getdream_item, null);
//			viewholder.imageView = (ImageView) convertView
//					.findViewById(R.id.user_getdream_picimageview);
//			viewholder.gdNameView = (TextView) convertView
//					.findViewById(R.id.user_getdream_name_tv);
//			viewholder.gdcountView = (TextView) convertView
//					.findViewById(R.id.user_getdream_value_tv);
//			viewholder.jointimeView = (TextView) convertView
//					.findViewById(R.id.user_getdream_need_textview);
//			viewholder.gridView = (CuGridView) convertView
//					.findViewById(R.id.user_getdream_CuGridView);
//			viewholder.button = (Button) convertView
//					.findViewById(R.id.user_getdream_btn);
//			viewholder.lucklay = convertView
//					.findViewById(R.id.user_getdream_lucknumberlay);
//			viewholder.openluck = (Button) convertView
//					.findViewById(R.id.user_getdreambtn);
//			viewholder.fBtn = (Button) convertView.findViewById(R.id.f_btn);
//			viewholder.nextBtn = (Button) convertView
//					.findViewById(R.id.next_btn);
//			viewholder.getuserView=(TextView) convertView.findViewById(R.id.user_dream_getuserview);
//			convertView.setTag(viewholder);
//		} else {
//			viewholder = (Viewholder) convertView.getTag();
//		}
//		viewholder.openluck.setTag(position);
//		viewholder.openluck.setVisibility(View.GONE);
//		viewholder.fBtn.setTag(position);
//		viewholder.nextBtn.setTag(position);
//		viewholder.getuserView.setText("幸运码:12345678");
//		viewholder.button.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(mContext, SubmitShowActivity.class);
//				mContext.startActivity(intent);
//			}
//		});
//		GridAdapter adapter = null;
//		List<String> childelist = new ArrayList<String>();
//		childelist = map.get(position);
//		if (childelist == null) {
//			adapter = new GridAdapter(mContext, childelist);
//		} else {
//			int page = pagemap.get(position);
//			if (childelist.size() > page * 9) {
//				changeBtnTextColor(viewholder.nextBtn, true);
//			}else{
//				changeBtnTextColor(viewholder.nextBtn, false);
//			}
//			childelist = Tools.getList(childelist, (page - 1) * 9, page * 9);
//			adapter = new GridAdapter(mContext, childelist);
//			if (page > 1) {
//				changeBtnTextColor(viewholder.fBtn, true);
//			}else{
//				changeBtnTextColor(viewholder.fBtn, false);
//			}
//		}
//		viewholder.gridView.setAdapter(adapter);
//
//		if (isopenmap.get(position) == 1) {
//			viewholder.lucklay.setVisibility(View.VISIBLE);
//		} else {
//			viewholder.lucklay.setVisibility(View.GONE);
//			isopenmap.put(position, 0);
//		}
//		viewholder.openluck.setOnClickListener(new MyClickListener(viewholder,
//				adapter));
//		viewholder.fBtn.setOnClickListener(new MyClickListener(viewholder,
//				adapter));
//		viewholder.nextBtn.setOnClickListener(new MyClickListener(viewholder,
//				adapter));
//		return convertView;
//	}
//
//	class Viewholder {
//		ImageView imageView;
//		TextView gdNameView;
//		TextView gdcountView;
//		TextView jointimeView;
//		TextView getuserView;
//		CuGridView gridView;
//		Button button;
//		View lucklay;
//		Button openluck;
//		Button fBtn;
//		Button nextBtn;
//	}
//
//	class MyClickListener implements OnClickListener {
//		Viewholder holder;
//		GridAdapter gridAdapter;
//		Handler handler = new Handler() {
//			@Override
//			public void handleMessage(Message msg) {
//				super.handleMessage(msg);
//				int position = msg.what;
//				List<String> list = new ArrayList<String>();
//				if (map.get(position).size() > 9) {
//					list = Tools.getList(map.get(position), 0, 9);
//					changeBtnTextColor(holder.nextBtn, true);
//				} else {
//					list = map.get(position);
//					changeBtnTextColor(holder.nextBtn, false);
//				}
//				pagemap.put(position, 1);
//				holder.lucklay.setVisibility(View.VISIBLE);
//				gridAdapter.setmDatas(list);
//				gridAdapter.notifyDataSetChanged();
//			}
//		};
//
//		public MyClickListener(Viewholder viewholder, GridAdapter gridAdapter) {
//			this.holder = viewholder;
//			this.gridAdapter = gridAdapter;
//		}
//
//		@Override
//		public void onClick(View v) {
//			int position = (int) v.getTag();
//			switch (v.getId()) {
//			case R.id.user_getdreambtn:
//				openLuck(v);
//				break;
//			case R.id.f_btn:
//				before(holder.fBtn, holder.nextBtn, position, gridAdapter);
//				break;
//			case R.id.next_btn:
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
//		public void openLuck(View v) {
//			final int position = (int) v.getTag();
//			if (isopenmap.get(position) == 1) {
//				holder.lucklay.setVisibility(View.GONE);
//				isopenmap.put(position, 0);
//			} else {
//				isopenmap.put(position, 1);
//				List<String> list = map.get(position);
//				if (list != null && list.size() > 0) {
//					holder.lucklay.setVisibility(View.VISIBLE);
//					int page = pagemap.get(position);
//					list = Tools.getList(list, (page - 1) * 9, page * 9);
//					// adapter = new GridAdapter(mContext, childelist);
//					gridAdapter.setmDatas(list);
//					gridAdapter.notifyDataSetChanged();
//				} else {
//					new Thread() {
//						public void run() {
//							try {
//								sleep(500);
//								initlist(position);
//								Message msg = new Message();
//								msg.what = position;
//								handler.sendMessage(msg);
//							} catch (InterruptedException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
//						}
//					}.start();
//				}
//			}
//		}
//
//	}
//
//	/**
//	 * 获取数据
//	 * 
//	 * @param grouppositon
//	 */
//	public void initlist(int grouppositon) {
//		List<String> list = new ArrayList<String>();
//		for (int i = 1000000; i < 1000050; i++) {
//			list.add(grouppositon + "" + i);
//		}
//		map.put(grouppositon, list);
//	}
//
//	public void next(Button before, Button nextbtn, int groupposition,
//			GridAdapter adapter) {
//		List<String> list = map.get(groupposition);
//		int page = pagemap.get(groupposition);
//		if (list.size() > page * 9) {
//			List<String> li = Tools.getList(list, page * 9, (page + 1) * 9);
//			adapter.setmDatas(li);
//			adapter.notifyDataSetChanged();
//			if (list.size() - page * 9 < 9) {
//				changeBtnTextColor(nextbtn, false);
//			} else {
//				changeBtnTextColor(nextbtn, true);
//			}
//			page++;
//			if (page > 1) {
//				changeBtnTextColor(before, true);
//			}
//			pagemap.put(groupposition, page);
//		}
//	}
//
//	public void before(Button before, Button nextbtn, int groupposition,
//			GridAdapter adapter) {
//		List<String> list = map.get(groupposition);
//		int page = pagemap.get(groupposition);
//		if (page > 1) {
//			List<String> li = Tools.getList(list, (page - 1) * 9,
//					(page - 2) * 9);
//			adapter.setmDatas(li);
//			adapter.notifyDataSetChanged();
//			page--;
//			pagemap.put(groupposition, page);
//			if (page == 1) {
//				changeBtnTextColor(before, false);
//			}
//			if (list.size() - (page - 1) * 9 > 9) {
//				changeBtnTextColor(nextbtn, true);
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
//}
//
//class GridAdapter extends BaseTaskAdaper<String> {
//
//	public GridAdapter(Context context, List<String> mDatas) {
//		super(context, mDatas);
//		// TODO Auto-generated constructor stub
//	}
//
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//		// TODO Auto-generated method stub
//		convertView = mInflater.inflate(R.layout.list_item, null);
//		TextView textView = (TextView) convertView.findViewById(R.id.item_id);
//		textView.setClickable(false);
//		textView.setText(mDatas.get(position) + "");
//		Log.v("this", "convertView " + position);
//		return convertView;
//	}
//}
