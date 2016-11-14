package com.zozmom.ui.adapter;

import java.util.List;
import java.util.Map;

import com.zozmom.pic.PhotoLoader;
import com.zozmom.util.LogUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public abstract class BaseTaskAdaper<T> extends BaseAdapter {

	protected LayoutInflater mInflater;
	protected Context mContext;
	protected List<T> mDatas;
	PhotoLoader loader;
//	public   Map<Integer, String> mMap;
	
	


//	public  Map<Integer, String> getmMap() {
//		return mMap;
//	}
//
//	public  void setmMap(Map<Integer, String> mMap) {
//		this.mMap = mMap;
//	}

	public BaseTaskAdaper(Context context, List<T> mDatas) {
		mInflater = LayoutInflater.from(context);
		this.mContext = context;
		this.mDatas = mDatas;
	}

	public int getCount() {
		if(mDatas==null){
			return 0;
		}
		return mDatas.size();
	}

	public Object getItem(int position) {
		if(mDatas==null){
			return null;
		}
		return mDatas.get(position);
	}

	public long getItemId(int position) {
		if(mDatas==null){
			return 0;
		}
		return position;
	}

	public List<T> getmDatas() {
		return mDatas;
	}

	public void setmDatas(List<T> mDatas) {
		this.mDatas = mDatas;
	}

	public void loadImage(String url, ImageView v) {
		if (loader == null) {
			loader = new PhotoLoader(mContext);
		}
		loader.loadPhoto(url, v);
	}
	
	public void loadImage1(String url, ImageView v){
		PhotoLoader.loadPhoto1(url, v);
	}
}
