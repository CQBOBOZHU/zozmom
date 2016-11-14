package com.zozmom.ui.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.chasedream.zhumeng.R;
import com.zozmom.cuview.CuExpandableListview;
import com.zozmom.dialog.LoadingDialog;
import com.zozmom.ui.adapter.GoodsListAdapter.HolderView;
import com.zozmom.ui.adapter.GoodsListAdapter.listAdapter;
import com.zozmom.ui.user.OtherUserCenterActivity;
import com.zozmom.util.ToastUtil;
import com.zozmom.util.Tools;

import android.R.integer;
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

public class GoodExAdapter extends BaseExpandableListAdapter {
	List<String> list;
	Context context;
	Map<Integer, List<String>> map;
	Map<Integer, List<String>> allchildemap = new HashMap<Integer, List<String>>();
	CuExpandableListview listview;
	LoadingDialog dialog;
	Map<Integer, Integer> pagemap = new HashMap<Integer, Integer>();
	int openEx = -1;// 展开的item
	Button openedBtn = null;
	Drawable opendraw;
	Drawable closedraw;

	public GoodExAdapter(List<String> list, Map<Integer, List<String>> map,
			Context context, CuExpandableListview listview) {
		this.list = list;
		this.map = map;
		this.context = context;
		this.listview = listview;
		dialog = LoadingDialog.createDialog(context);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return list.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		if (map.get(groupPosition) != null) {
			return map.get(groupPosition).get(childPosition);
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
		// String name = "参与了";
		// String newMessageInfo = "<font color='#999999'><b>" + name
		// + "</b></font><font color='#FF7E00'><b>" + "26" + "</b></font>"
		// + "<font color='#999999'><b>" + "人次" + "</b></font>";
		// goodNameView.setText(Html.fromHtml(newMessageInfo));
		// holderView.userjoinView.setText(Html.fromHtml(newMessageInfo));
		if (groupPosition % 2 == 0) {
			holderView.txettimeview.setVisibility(View.VISIBLE);
		} else {
			holderView.txettimeview.setVisibility(View.GONE);
		}
		holderView.openBtn.setTag(groupPosition);
		holderView.listView.setTag(groupPosition);
		holderView.userfaceView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context,
						OtherUserCenterActivity.class);
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
		// TODO Auto-generated method stub
		convertView = LayoutInflater.from(context).inflate(R.layout.list_grid,
				null);
		GridView gridView = (GridView) convertView.findViewById(R.id.list_grid);
		gridAdapter adapter = new gridAdapter(context, map.get(groupPosition));
		gridView.setAdapter(adapter);
		gridView.setTag(groupPosition);
		Button button = (Button) convertView.findViewById(R.id.f_btn);
		button.setTag(groupPosition);
		Button button1 = (Button) convertView.findViewById(R.id.next_btn);
		button1.setTag(groupPosition);
		button.setTag(groupPosition);
		button1.setTag(groupPosition);
		button.setOnClickListener(new myclick(adapter, button, button1));
		button1.setOnClickListener(new myclick(adapter, button, button1));
		return convertView;

	}

	public class myclick implements OnClickListener {
		gridAdapter adapter;
		Button beforbtn;
		Button nextbtn;

		public myclick(gridAdapter adapter, Button beforbtn, Button nextbtn) {
			// TODO Auto-generated constructor stub
			this.adapter = adapter;
			this.beforbtn = beforbtn;
			this.nextbtn = nextbtn;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.f_btn:
				before(beforbtn, nextbtn, (Integer) nextbtn.getTag(), adapter);
				break;
			case R.id.next_btn:
				next(beforbtn, nextbtn, (Integer) nextbtn.getTag(), adapter);
				break;

			default:
				break;
			}
		}

	}

	OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			ToastUtil.show(context, "V" + v.getTag());
		}
	};

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

	class gridAdapter extends BaseTaskAdaper<String> {

		public gridAdapter(Context context, List<String> mDatas) {
			super(context, mDatas);
			// TODO Auto-generated constructor stub
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			convertView = LayoutInflater.from(context).inflate(
					R.layout.list_item, null);
			TextView textView = (TextView) convertView
					.findViewById(R.id.item_id);
			textView.setClickable(false);
			textView.setText(mDatas.get(position) + "");
			Log.v("this", "convertView " + position);
			return convertView;
		}

	}

	List<String> chlist = new ArrayList<String>();
	int percentID = 0;

	public void initchlist(int po) {
		chlist = new ArrayList<String>();
		for (int i = 1000001; i < 1000101; i++) {
			chlist.add(po + "" + i);
		}
		allchildemap.put(po, chlist);
		if (chlist.size() > 9) {
			List<String> li = Tools.getList(chlist, 0, 9);
			map.put(po, li);
		} else {
			map.put(po, chlist);
		}

		dialog.dismiss();
		// listview.get
		listview.expandGroup(po);
		openEx = po;
		pagemap.put(po, 1);
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			initchlist(msg.arg1);
		}
	};

	public void onGroupClick(ExpandableListView parent, int groupPosition,
			Button btn) {
		// TODO Auto-generated method stub
		if (openEx != -1 && openEx != groupPosition && openedBtn != null) {
			parent.collapseGroup(openEx);
			// btn.setText("展开");
			openedBtn.setText("展开");
			openedBtn.setCompoundDrawables(null, null, opendraw, null);
		}
		final Button tt = btn;
		if (map.get(groupPosition) == null) {
			dialog.show();
			percentID = groupPosition;
			boolean bl = parent.isGroupExpanded(groupPosition);
			if (!bl) {
				new Thread() {
					public void run() {
						try {
							sleep(1000);
							Message msg = new Message();
							msg.what = 1;
							msg.arg1 = percentID;
							openedBtn = tt;
							handler.sendMessage(msg);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}.start();
			}
			btn.setText("收起");
		} else {
			pagemap.put(groupPosition, 1);
			if (parent.isGroupExpanded(groupPosition)) {// 如果展开
				parent.collapseGroup(groupPosition);
				btn.setText("展开");
				btn.setCompoundDrawables(null, null, opendraw, null);
				openEx = -1;
				openedBtn = null;
			} else {
				parent.expandGroup(groupPosition);
				parent.setSelectedGroup(groupPosition);
				btn.setText("收起");
				btn.setCompoundDrawables(null, null, closedraw, null);
				openEx = groupPosition;
				openedBtn = btn;
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
