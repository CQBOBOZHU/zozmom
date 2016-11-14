package com.zozmom.pic;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.LruCache;
import android.widget.ImageView;

import com.zozmom.constants.Constant;

public class LoadImageTask extends AsyncTask<String, Void, Bitmap> {

	// private ImageView mImageView;

	private String mUrl;
	private LruCache<String, Bitmap> mLruCache;
	// 获得当前最大可用内存
	int maxMemroy = (int) Runtime.getRuntime().maxMemory();
	// 设置最大缓存大小
	int cacheSize = maxMemroy / 3;
	ImageView imageview;
	@SuppressLint("NewApi")
	public LoadImageTask(String mUrl, ImageView imageview) {
		// this.mImageView = mImageView;
		this.mUrl = mUrl;
		this.imageview = imageview;
		mLruCache = new LruCache<String, Bitmap>(cacheSize) {
			// 在每次加入内存缓存时候调用
			protected int sizeOf(String key, Bitmap value) {
				// 在每次存入缓存的时候给予bitmap准确的大小
				return value.getByteCount();
			};
		};
	}

	@Override
	protected Bitmap doInBackground(String... params) {
		// 从网络获取图片or添加缓存
		String url = params[0];
		// 从网络中获取中取出图片
		Bitmap bitmap = getBitMapFromURL(Constant.PRODUCT_URL + url
				+ Constant.PRODUCT_KEY);
		if (bitmap != null) {
			addBitmapToCache(url, bitmap);
		}

		return bitmap;
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		super.onPostExecute(result);
		if (result != null && imageview != null) {

			imageview.setImageBitmap(result);
		}
	}

	/**
	 * 使用httpUrlConnection下载Bitmap
	 * 
	 * @param urlString
	 * @return
	 */
	public Bitmap getBitMapFromURL(String urlString) {
		Bitmap bitmap;
		InputStream is = null;
		try {
			URL url = new URL(urlString);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			is = new BufferedInputStream(connection.getInputStream());
			bitmap = BitmapFactory.decodeStream(is);
			connection.disconnect();
			return bitmap;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 增加缓存
	 * 
	 * @param url
	 * @param bitmap
	 */
	@SuppressLint("NewApi")
	public void addBitmapToCache(String url, Bitmap bitmap) {
		if (getBitmapToCache(url) == null) {
			mLruCache.put(url, bitmap);
		}
	}

	@SuppressLint("NewApi")
	public Bitmap getBitmapToCache(String url) {
		if (url == null) {
			return null;
		}
		return mLruCache.get(url);
	}

}
