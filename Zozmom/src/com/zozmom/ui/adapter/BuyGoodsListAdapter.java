package com.zozmom.ui.adapter;

import java.util.List;
import java.util.Map;

import com.chasedream.zhumeng.R;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.zozmom.constants.Constant;
import com.zozmom.model.OpenModel;
import com.zozmom.pic.PhotoLoader;
import com.zozmom.util.Util;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 最新揭晓的adapter
 * 
 * @author Administrator
 * 
 */
public class BuyGoodsListAdapter extends BaseTaskAdaper<OpenModel> {
	PullToRefreshGridView gridView;
	public BuyGoodsListAdapter(Context context, List<OpenModel> mDatas,
			PullToRefreshGridView gridView, PhotoLoader loader) {
		super(context, mDatas);
		this.gridView = gridView;
		this.loader=loader;
		gridView.getRefreshableView().setOnScrollListener(
				new PauseOnScrollListener(loader.imageLoader, true, true));
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		HolderView holderView;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.the_new_fragment_listitem, null, true);
			holderView = new HolderView();
			holderView.imageView = (ImageView) convertView
					.findViewById(R.id.gooditem_imageview);
			holderView.tileView = (TextView) convertView
					.findViewById(R.id.good_name_tv);
			holderView.tView = (TextView) convertView
					.findViewById(R.id.good_c_tv);// 参与人次
			holderView.tnnView = (TextView) convertView
					.findViewById(R.id.good_num);// 期数
			holderView.tmmView = (TextView) convertView
					.findViewById(R.id.good_value_tv);// 获奖者
			holderView.timeView = (TextView) convertView
					.findViewById(R.id.good_c_time);// 获奖时间
			holderView.numberView = (TextView) convertView
					.findViewById(R.id.number_textview);// 获奖码
			holderView.disView = (ImageView) convertView
					.findViewById(R.id.gooditem_disimageview);
			convertView.setTag(holderView);
		} else
			holderView = (HolderView) convertView.getTag();

		OpenModel model = mDatas.get(position);
		String titl = model.getProductName();
		// String tv="1280";//model.getBuyNumberCount();//购买的次数
		String showimage = model.getProductImg();
		String userName = model.getUserName();// 获得者名字
		long time = model.getAnnouncedTime();// model.getAnnouncedTime();
		int price = model.getUserBuyCount();// model.getProductPrice();
		String luckNub = model.getLuckyNumber();// model.getRandomNumber();
		int luck = 10000000 + Integer.parseInt(luckNub);
		int isueid = model.getIssueId();
		int productArea = model.getProductArea();
		String url = Constant.PRODUCT_URL + showimage + Constant.PRODUCT_KEY;
		holderView.imageView.setTag(url);
		loadImage(url, holderView.imageView);
		if (productArea == 10) {
			holderView.disView.setVisibility(View.VISIBLE);
			holderView.disView.setImageResource(R.drawable.ten_yuan);
		} else if (productArea == 100) {
			holderView.disView.setVisibility(View.VISIBLE);
			holderView.disView.setImageResource(R.drawable.hundred_yuan);
		} else {
			holderView.disView.setVisibility(View.GONE);
		}
		holderView.tileView.setText(titl);
		holderView.tView.setText("参与人次:" + String.valueOf(price));
		holderView.tnnView.setText("期号:" + String.valueOf(isueid));
		holderView.tmmView.setText("获得者:" + userName);
		holderView.timeView.setText(Util.GetDataTime(time));
		holderView.numberView.setText("幸运逐梦码:" + luck);

		return convertView;
	}

	class HolderView {
		ImageView imageView;
		TextView tileView;
		TextView tView;
		TextView tnnView;
		TextView tmmView;
		TextView timeView;
		TextView numberView;
		ImageView disView;
	}
}
