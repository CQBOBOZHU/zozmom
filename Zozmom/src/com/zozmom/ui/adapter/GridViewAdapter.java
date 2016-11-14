package com.zozmom.ui.adapter;

import java.util.List;

import com.chasedream.zhumeng.R;
import com.zozmom.constants.Constant;
import com.zozmom.model.ProductModel;
import com.zozmom.pic.PhotoLoader;
import com.zozmom.ui.SubmitActivity;
import com.zozmom.util.LogUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class GridViewAdapter extends BaseTaskAdaper<ProductModel> {
	Bitmap tenbitmap;
	Bitmap hunbitmap;
	PhotoLoader loader;
	public GridViewAdapter(Context context, List<ProductModel> mDatas,PhotoLoader  loader) {
		super(context, mDatas);
		// TODO Auto-generated constructor stub
		this.loader=loader;
		tenbitmap=BitmapFactory.decodeStream(context.getResources().openRawResource(R.drawable.ten_yuan));
		hunbitmap=BitmapFactory.decodeStream(context.getResources().openRawResource(R.drawable.hundred_yuan));
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		HolderView holderView;
		if (convertView == null) {
			holderView = new HolderView();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.gallery_item, null);
			holderView.imageView = (ImageView) convertView
					.findViewById(R.id.gallery_imageview);

			holderView.aText = (TextView) convertView
					.findViewById(R.id.gallery_name_tv);
			holderView.allT = (TextView) convertView
					.findViewById(R.id.gallery_value_tv);
			holderView.otT = (TextView) convertView
					.findViewById(R.id.gallery_c_tv);
			holderView.progressBar = (ProgressBar) convertView
					.findViewById(R.id.gallery_item_bar);
			holderView.button1 = (Button) convertView
					.findViewById(R.id.button1);
			holderView.disView = (ImageView) convertView
					.findViewById(R.id.gallery_disimageview);
			convertView.setTag(holderView);
		} else {
			holderView = (HolderView) convertView.getTag();
		}
		final ProductModel buyGoodsModel = mDatas.get(position);
		String picurl = buyGoodsModel.getShowImage();
		int price = buyGoodsModel.getProductPrice();
		int curr = buyGoodsModel.getBuyCount();
		int productArea = buyGoodsModel.getProductArea();
		String productTitle = buyGoodsModel.getProductTitle();
		String productName = buyGoodsModel.getProductName();
		String url = Constant.PRODUCT_URL + picurl + Constant.PRODUCT_KEY;
		holderView.imageView.setTag(url);
		loadImage1(url, holderView.imageView);
		holderView.aText.setText(productName);
		if (productArea == 10) {
			holderView.disView.setVisibility(View.VISIBLE);
			holderView.disView.setImageBitmap(tenbitmap);
		} else if (productArea == 100) {
			holderView.disView.setVisibility(View.VISIBLE);
			holderView.disView.setImageBitmap(hunbitmap);
		} else {
			holderView.disView.setVisibility(View.GONE);
		}

		int a = price - curr;
		holderView.allT.setText(price + "");
		holderView.otT.setText(a + "");
		int b = curr * 100 / price;
		if (b == 0 && curr > 0) {
			b = 1;
		}
		holderView.progressBar.setProgress(b);
		holderView.button1.setTag(position);
		holderView.button1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(mContext, SubmitActivity.class);
				intent.putExtra("issueId", buyGoodsModel.getIssueId());
				intent.putExtra("productId", buyGoodsModel.getProductId());
				mContext.startActivity(intent);
			}
		});
		return convertView;
	}

	class HolderView {
		ImageView imageView;
		ProgressBar progressBar;
		TextView aText;// 已经参与
		TextView allT;
		TextView otT;
		Button button1;
		ImageView disView;

	}
}
