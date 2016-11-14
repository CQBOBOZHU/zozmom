package com.zozmom.ui.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.chasedream.zhumeng.R;
import com.zozmom.constants.Constant;
import com.zozmom.cuview.CuExpandableListview;
import com.zozmom.dialog.LoadingDialog;
import com.zozmom.model.LuckModel;
import com.zozmom.pic.PhotoLoader;
import com.zozmom.ui.BaseTaskActivity;
import com.zozmom.ui.adapter.GoodsListAdapter.HolderView;
import com.zozmom.ui.adapter.GoodsListAdapter.listAdapter;
import com.zozmom.ui.user.OtherUserCenterActivity;
import com.zozmom.util.ToastUtil;
import com.zozmom.util.Tools;
import com.zozmom.util.Util;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NewGoodExAdapter extends BaseExpandableListAdapter {
	List<LuckModel> list;
	Context context;
	@SuppressLint("UseSparseArrays")
	Map<Integer, List<String>> allchildemap = new HashMap<Integer, List<String>>();
	ExpandableListView listview;
	@SuppressLint("UseSparseArrays")
	Map<Integer, Integer> pagemap = new HashMap<Integer, Integer>();
	Drawable opendraw;
	Drawable closedraw;
	PhotoLoader loader;
	@SuppressLint("UseSparseArrays")
	Map<Integer, String> dataMap = new HashMap<Integer, String>();

	public NewGoodExAdapter(List<LuckModel> list, Context context,
			Map<Integer, List<String>> allchildemap, ExpandableListView listview) {
		this.list = list;
		this.allchildemap = allchildemap;
		this.context = context;
		this.listview = listview;
		initPagemap(list.size());
		loader = new PhotoLoader(context);
	}

	public List<LuckModel> getList() {
		return list;
	}

	public void setList(List<LuckModel> list) {
		this.list = list;
		int lSize = list.size();
		int mSize = pagemap.size();
		if (lSize > mSize) {
			for (int i = mSize; i < lSize; i++) {
				pagemap.put(i, 1);
			}
		}
	}

	public Map<Integer, List<String>> getAllchildemap() {
		return allchildemap;
	}

	public void setAllchildemap(Map<Integer, List<String>> allchildemap) {
		this.allchildemap = allchildemap;
	}

	private void initPagemap(int size) {
		// TODO Auto-generated method stub
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				pagemap.put(i, 1);
			}
		}
	}

	@Override
	public int getGroupCount() {
		return list.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return 1;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return list.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		if (allchildemap.get(groupPosition) != null) {
			return allchildemap.get(childPosition);
		}
		return null;
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if (opendraw == null) {
			opendraw = context.getResources()
					.getDrawable(R.drawable.take_on_ye);
			opendraw.setBounds(0, 0, opendraw.getMinimumWidth(),
					opendraw.getMinimumHeight());
		}
		if (closedraw == null) {
			closedraw = context.getResources().getDrawable(
					R.drawable.take_off_ye);
			closedraw.setBounds(0, 0, closedraw.getMinimumWidth(),
					closedraw.getMinimumHeight());
		}
		HolderView holderView;
		if (convertView == null) {
			holderView = new HolderView();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.good_listitem, null);
			convertView.setTag(holderView);
			holderView.openBtn = (Button) convertView
					.findViewById(R.id.relayout);
			holderView.listView = (ListView) convertView
					.findViewById(R.id.good_item_listview);
			holderView.userfaceView = (ImageView) convertView
					.findViewById(R.id.good_listitem_imageView1);
			holderView.usernameView = (TextView) convertView
					.findViewById(R.id.good_listitem_textView1);
			holderView.useridView = (TextView) convertView
					.findViewById(R.id.good_listitem_textView2);
			holderView.userjoinView = (TextView) convertView
					.findViewById(R.id.good_listitem_unm_textview);
			holderView.tiemView = (TextView) convertView
					.findViewById(R.id.good_listitem_time_textview);
			holderView.txettimeview = (TextView) convertView
					.findViewById(R.id.good_listtextview);

		} else {
			holderView = (HolderView) convertView.getTag();
		}

		// if (groupPosition % 2 == 0) {
		// holderView.txettimeview.setVisibility(View.VISIBLE);
		// } else {
		// holderView.txettimeview.setVisibility(View.GONE);
		// }

		if (((ExpandableListView) parent).isGroupExpanded(groupPosition)) {// 如果展开
			holderView.openBtn.setText("收起");
			holderView.openBtn
					.setCompoundDrawables(null, null, closedraw, null);
		} else {
			holderView.openBtn.setText("展开");
			holderView.openBtn.setCompoundDrawables(null, null, opendraw, null);
		}
		holderView.openBtn.setTag(groupPosition);
		holderView.listView.setTag(groupPosition);
		holderView.userfaceView.setTag(groupPosition);
		holderView.userfaceView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context,
						OtherUserCenterActivity.class);
				int userId = list.get((int) v.getTag()).getUserId();
				intent.putExtra("userId", userId);
				context.startActivity(intent);
			}
		});
		holderView.openBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Button button = (Button) v;
				onGroupClick(listview, (Integer) v.getTag(), button);
			}
		});
		LuckModel luckModel = list.get(groupPosition);
		String username = luckModel.getUserName();
		int userjoin = luckModel.getBuyCount();
		long data = luckModel.getJoinTime();
		int useid = luckModel.getUserId();
		String headUrl = luckModel.getHeadUrl();

		loader.loadPhoto(Constant.FACE_URL + headUrl + Constant.FACE_KEY,
				holderView.userfaceView);
		holderView.usernameView.setText(username);
		holderView.userjoinView.setText(String.valueOf(userjoin));
		String mdata1 = Util.GetDataTime(data);
		String mdata2 = Util.GetDataTime1(data);

		holderView.useridView.setText("(" + useid + ")");
		holderView.tiemView.setText(mdata2);
		// if(groupPosition==0){
		// dataList.add(mdata1);
		// holderView.txettimeview.setVisibility(View.VISIBLE);
		// holderView.txettimeview.setText(mdata1);
		// }
		if (dataMap.containsValue(mdata1)) {
			for (Map.Entry entry : dataMap.entrySet()) {
				if (mdata1.equals(entry.getValue())) {
					int position = (int) entry.getKey();
					if (position == groupPosition) {
						holderView.txettimeview.setVisibility(View.VISIBLE);
						holderView.txettimeview.setText(mdata1);
					} else {
						holderView.txettimeview.setVisibility(View.GONE);
					}
				}
			}
		} else {
			dataMap.put(groupPosition, mdata1);
			holderView.txettimeview.setVisibility(View.VISIBLE);
			holderView.txettimeview.setText(mdata1);
		}
		return convertView;
	}

	class HolderView {
		ListView listView;
		Button openBtn;
		TextView usernameView;
		TextView useridView;
		TextView userjoinView;
		TextView tiemView;
		ImageView userfaceView;
		TextView txettimeview;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		convertView = LayoutInflater.from(context).inflate(R.layout.list_grid,
				null);
		GridView gridView = (GridView) convertView.findViewById(R.id.list_grid);
		gridView.setTag(groupPosition);
		Button button = (Button) convertView.findViewById(R.id.f_btn);
		button.setTag(groupPosition);
		Button button1 = (Button) convertView.findViewById(R.id.next_btn);
		button1.setTag(groupPosition);
		button.setTag(groupPosition);
		button1.setTag(groupPosition);
		List<String> childlist = allchildemap.get(groupPosition);
		int page = pagemap.get(groupPosition);
		int maxSize = childlist.size();
		int a = maxSize % 9;
		int maxpage;
		if (a == 0) {
			maxpage = maxSize / 9;
		} else {
			maxpage = (maxSize - a) / 9 + 1;
		}
		if (childlist.size() > 9) {
			childlist = Tools.getList(childlist, (page - 1) * 9, page * 9);
			if (maxpage > page) {
				changeBtnTextColor(button1, true);
			} else if (maxpage == page) {
				changeBtnTextColor(button1, false);
			}
		}
		if (page > 1) {
			changeBtnTextColor(button, true);
		} else {
			changeBtnTextColor(button, false);
		}

		gridAdapter adapter = new gridAdapter(context, childlist);
		gridView.setAdapter(adapter);
		button.setOnClickListener(new myclick(adapter, button, button1));
		button1.setOnClickListener(new myclick(adapter, button, button1));
		return convertView;

	}

	public class myclick implements OnClickListener {
		gridAdapter adapter;
		Button beforbtn;
		Button nextbtn;

		public myclick(gridAdapter adapter, Button beforbtn, Button nextbtn) {
			this.adapter = adapter;
			this.beforbtn = beforbtn;
			this.nextbtn = nextbtn;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.f_btn :
					before(beforbtn, nextbtn, (Integer) nextbtn.getTag(),
							adapter);
					break;
				case R.id.next_btn :
					next(beforbtn, nextbtn, (Integer) nextbtn.getTag(), adapter);
					break;

				default :
					break;
			}
		}

	}

	OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			ToastUtil.show(context, "V" + v.getTag());
		}
	};

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	class gridAdapter extends BaseTaskAdaper<String> {

		public gridAdapter(Context context, List<String> mDatas) {
			super(context, mDatas);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.list_item, null);
			TextView textView = (TextView) convertView
					.findViewById(R.id.item_id);
			textView.setClickable(false);
			int a = Integer.parseInt(mDatas.get(position).trim());
			int b = a + 10000000;
			textView.setText(b + "");
			Log.v("this", "convertView " + position);
			return convertView;
		}

	}

	List<String> chlist = new ArrayList<String>();
	int percentID = 0;

	public void onGroupClick(ExpandableListView parent, int groupPosition,
			Button btn) {
		if (parent.isGroupExpanded(groupPosition)) {// 如果展开
			parent.collapseGroup(groupPosition);
			btn.setText("展开");
			btn.setCompoundDrawables(null, null, opendraw, null);
		} else {
			parent.expandGroup(groupPosition);
			parent.setSelectedGroup(groupPosition);
			btn.setText("收起");
			btn.setCompoundDrawables(null, null, closedraw, null);
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
			button.setTextColor(context.getResources().getColor(
					R.color.good_message_textcolors));
			button.setClickable(true);
		} else {
			button.setTextColor(context.getResources().getColor(
					R.color.before_btncolor));
			button.setClickable(false);
		}
	}

	public void next(Button before, Button nextbtn, int groupposition,
			gridAdapter adapter) {
		List<String> list = allchildemap.get(groupposition);
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
			gridAdapter adapter) {
		List<String> list = allchildemap.get(groupposition);
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
