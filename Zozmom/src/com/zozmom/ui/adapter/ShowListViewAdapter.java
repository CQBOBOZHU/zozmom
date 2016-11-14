package com.zozmom.ui.adapter;

import java.util.List;

import com.chasedream.zhumeng.R;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.zozmom.constants.Constant;
import com.zozmom.cuview.CuGridView;
import com.zozmom.cuview.CuListView;
import com.zozmom.model.ShowModel;
import com.zozmom.pic.PhotoLoader;
import com.zozmom.ui.GoodMessageActivity;
import com.zozmom.ui.user.OtherUserCenterActivity;
import com.zozmom.util.Util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 单个商品晒单 他人晒单 晒单
 * 
 * @author Administrator
 * 
 */
public class ShowListViewAdapter extends BaseTaskAdaper<ShowModel> {

	public ShowListViewAdapter(Context context, List<ShowModel> mDatas,CuListView listview) {
		super(context, mDatas);
		listview.setOnScrollListener(new PauseOnScrollListener(loader.imageLoader, true, true));
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.open_list_item, null);
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.openlist_userface_image);
			holder.userNameText = (TextView) convertView
					.findViewById(R.id.openlist_userName_text);
			holder.showTimeText = (TextView) convertView
					.findViewById(R.id.openlist_time_text);
			holder.goodNameView = (TextView) convertView
					.findViewById(R.id.good_name);
			holder.issuedIdView = (TextView) convertView
					.findViewById(R.id.good_number);
			holder.showContentText = (TextView) convertView
					.findViewById(R.id.openlist_content_text);
			holder.iwantBtn = (Button) convertView
					.findViewById(R.id.show_iwantbtn);
			holder.showImage1 = (ImageView) convertView
					.findViewById(R.id.openlist_image1);
			holder.showImage2 = (ImageView) convertView
					.findViewById(R.id.openlist_image2);
			holder.showImage3 = (ImageView) convertView
					.findViewById(R.id.openlist_image3);
			holder.showImage4 = (ImageView) convertView
					.findViewById(R.id.openlist_image4);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.imageView.setTag(position);
		holder.imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int tag = (int) v.getTag();
				ShowModel showModel = mDatas.get(tag);
				int userId = showModel.getUserId();
				Intent intent = new Intent(mContext,
						OtherUserCenterActivity.class);
				intent.putExtra("userId", userId);
				mContext.startActivity(intent);
			}
		});
		ShowModel model = mDatas.get(position);
		int productStatus = model.getProductStatus();
		String userface = model.getHeadUrl();
		Log.v("this", "this" + Constant.FACE_URL + userface + Constant.FACE_KEY);
		String url = Constant.FACE_URL + userface + Constant.FACE_KEY;
		loadImage(url, holder.imageView);
		holder.userNameText.setText(model.getUserName());
		String content = model.getComment();
		content = content.replace("<p>", "").replace("</p>", "");
		holder.showContentText.setText(content);
		long time = model.getCreateTime();
		String goodname = model.getProductName();
		String issueId = model.getIssueId();
		holder.goodNameView.setText(goodname);
		holder.issuedIdView.setText("期号: " + issueId);
		String data = Util.GetDataTime1(time);
		holder.showTimeText.setText(data);
		String shareimageString = model.getShareImg();
		String[] images = shareimageString.split(",");
		// Constant.SHARE_URL + image + Constant.SHARE_KEY
		if (images.length == 1) {
			String image = images[0];
			String url1 = Constant.SHARE_URL + image + Constant.SHARE_KEY;
			loadImage(url1, holder.showImage1);
			holder.showImage2.setVisibility(View.GONE);
			holder.showImage3.setVisibility(View.GONE);
			holder.showImage4.setVisibility(View.GONE);
		} else if (images.length == 2) {
			String image = images[0];
			String image1 = images[1];
			String url1 = Constant.SHARE_URL + image + Constant.SHARE_KEY;
			String url2 = Constant.SHARE_URL + image1 + Constant.SHARE_KEY;
			holder.showImage2.setVisibility(View.VISIBLE);
			holder.showImage3.setVisibility(View.GONE);
			holder.showImage4.setVisibility(View.GONE);
			loadImage(url1, holder.showImage1);
			loadImage(url2, holder.showImage2);
		} else if (images.length == 3) {
			String image = images[0];
			String image1 = images[1];
			String image2 = images[2];
			String url1 = Constant.SHARE_URL + image + Constant.SHARE_KEY;
			String url2 = Constant.SHARE_URL + image1 + Constant.SHARE_KEY;
			String url3 = Constant.SHARE_URL + image2 + Constant.SHARE_KEY;
			loadImage(url1, holder.showImage1);
			loadImage(url2, holder.showImage2);
			loadImage(url3, holder.showImage3);
			holder.showImage2.setVisibility(View.VISIBLE);
			holder.showImage3.setVisibility(View.VISIBLE);
			holder.showImage4.setVisibility(View.GONE);
		} else if (images.length > 4 || images.length == 4) {
			String image = images[0];
			String image1 = images[1];
			String image2 = images[2];
			String image3 = images[3];
			String url1 = Constant.SHARE_URL + image + Constant.SHARE_KEY;
			String url2 = Constant.SHARE_URL + image1 + Constant.SHARE_KEY;
			String url3 = Constant.SHARE_URL + image2 + Constant.SHARE_KEY;
			String url4 = Constant.SHARE_URL + image3 + Constant.SHARE_KEY;
			loadImage(url1, holder.showImage1);
			loadImage(url2, holder.showImage2);
			loadImage(url3, holder.showImage3);
			loadImage(url4, holder.showImage4);
			holder.showImage2.setVisibility(View.VISIBLE);
			holder.showImage3.setVisibility(View.VISIBLE);
			holder.showImage4.setVisibility(View.VISIBLE);
		}

		if (productStatus == 0) {
			holder.iwantBtn.setVisibility(View.GONE);
		} else {
			holder.iwantBtn.setVisibility(View.VISIBLE);
		}
		holder.iwantBtn.setTag(position);
		holder.iwantBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int tag = (int) v.getTag();
				ShowModel showModel = mDatas.get(tag);
				int productId = Integer.parseInt(showModel.getProductId());
				Intent intent = new Intent(mContext, GoodMessageActivity.class);
				intent.putExtra("productId", productId);
				mContext.startActivity(intent);
			}
		});
		return convertView;
	}

	class ViewHolder {
		TextView goodNameView, issuedIdView, userNameText, showTimeText,
				showContentText;
		Button iwantBtn;
		ImageView imageView, showImage1, showImage2, showImage3, showImage4;
	}


}
