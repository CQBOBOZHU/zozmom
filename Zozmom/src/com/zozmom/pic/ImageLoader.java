//package com.zozmom.pic;
//
//import java.io.BufferedInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.HashSet;
//import java.util.Set;
//
//import com.chasedream.zhumeng.R;
//import com.zozmom.constants.Constant;
//import com.zozmom.ui.adapter.BaseTaskAdaper;
//
//import android.annotation.SuppressLint;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.AsyncTask;
//import android.os.Handler;
//import android.os.Message;
//import android.util.Log;
//import android.util.LruCache;
//import android.widget.AbsListView;
//import android.widget.ImageView;
//import android.widget.ListView;
//
///**
// * @author Thro 图片异步加载工具类
// */
//public class ImageLoader {
//	private ImageView mImageView;
//	private String murl;
//	private AbsListView mListView;
//	private Set<NewsAsyncTask> mTask;
//	// 创建缓存机制 Cache
//	private LruCache<String, Bitmap> mLruCache;
//
//	@SuppressLint("NewApi")
//	public ImageLoader(AbsListView listView) {
//		mListView = listView;
//		mTask = new HashSet<NewsAsyncTask>();
//		// 获得当前最大可用内存
//		int maxMemroy = (int) Runtime.getRuntime().maxMemory();
//		// 设置最大缓存大小
//		int cacheSize = maxMemroy / 3;
//		mLruCache = new LruCache<String, Bitmap>(cacheSize) {
//			// 在每次加入内存缓存时候调用
//			protected int sizeOf(String key, Bitmap value) {
//				// 在每次存入缓存的时候给予bitmap准确的大小
//				return value.getByteCount();
//			};
//		};
//	}
//
//	/**
//	 * 增加缓存
//	 * 
//	 * @param url
//	 * @param bitmap
//	 */
//	@SuppressLint("NewApi")
//	public void addBitmapToCache(String url, Bitmap bitmap) {
//		if (getBitmapToCache(url) == null) {
//			mLruCache.put(url, bitmap);
//		}
//	}
//
//	@SuppressLint("NewApi")
//	public Bitmap getBitmapToCache(String url) {
//		if (url == null) {
//			return null;
//		}
//		return mLruCache.get(url);
//	}
//
//	private Handler handler = new Handler() {
//		public void handleMessage(android.os.Message msg) {
//			if (mImageView.getTag().equals(murl)) {
//				mImageView.setImageBitmap((Bitmap) msg.obj);
//			}
//		};
//	};
//
//	/**
//	 * 使用多线程加载图片
//	 * 
//	 * @param image
//	 * @param url
//	 */
//	public void ShowImageByThread(ImageView image, final String url) {
//		mImageView = image;
//		murl = url;
//		new Thread() {
//			@Override
//			public void run() {
//				super.run();
//				Bitmap bitmap = getBitMapFromURL(url);
//				Message message = Message.obtain();
//				message.obj = bitmap;
//				handler.sendMessage(message);
//			}
//		}.start();
//
//	}
//
//	/**
//	 * 使用httpUrlConnection下载Bitmap
//	 * 
//	 * @param urlString
//	 * @return
//	 */
//	public Bitmap getBitMapFromURL(String urlString) {
//		Bitmap bitmap;
//		InputStream is = null;
//		try {
//			URL url = new URL(urlString);
//			HttpURLConnection connection = (HttpURLConnection) url
//					.openConnection();
//			is = new BufferedInputStream(connection.getInputStream());
//			bitmap = BitmapFactory.decodeStream(is);
//			connection.disconnect();
//			return bitmap;
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				is.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		return null;
//	}
//
//	/**
//	 * 用来加载start到end的图片
//	 * 
//	 * @param Start
//	 * @param End
//	 */
//	public void loadImages(int Start, int End, BaseTaskAdaper<?> dasTaskAdaper) {
//		for (int i = Start; i < End; i++) {
//			String url = dasTaskAdaper.mMap.get(i);
//			Bitmap bitmap = getBitmapToCache(url);
//			if (bitmap == null) {
//				NewsAsyncTask Task = new NewsAsyncTask(url);
//				Task.execute(url);
//				mTask.add(Task);
//			} else {
//				ImageView imageView = (ImageView) mListView
//						.findViewWithTag(url);
//				imageView.setBackgroundResource(R.drawable.pic_loading);
//			}
//		}
//	};
//
//	/**
//	 * 使用异步任务加载Bitmap
//	 * 
//	 * @param imageView
//	 * @param Url
//	 */
//	public void ShowImageByAsyncTask(ImageView imageView, String Url) {
//		Bitmap bitmap = getBitmapToCache(Url);
//		// 在这里使用loadImages来加载图片，bitmap为空时不再调用AsynTask而是加在一张默认图片
//		if (bitmap == null) {
//			imageView.setImageResource(R.drawable.pic_loading);
//		} else {
//			imageView.setImageBitmap(bitmap);
//		}
//	}
//
//	private class NewsAsyncTask extends AsyncTask<String, Void, Bitmap> {
//		// private ImageView mImageView;
//		private String mUrl;
//
//		public NewsAsyncTask(String mUrl) {
//			// this.mImageView = mImageView;
//			this.mUrl = mUrl;
//		}
//
//		@Override
//		protected Bitmap doInBackground(String... params) {
//			// 从网络获取图片or添加缓存
//			String url = params[0];
//			// 从网络中获取中取出图片
//			Bitmap bitmap = getBitMapFromURL(Constant.PRODUCT_URL + url
//					+ Constant.PRODUCT_KEY);
//			if (bitmap != null) {
//				addBitmapToCache(url, bitmap);
//			}
//
//			return bitmap;
//		}
//
//		@Override
//		protected void onPostExecute(Bitmap result) {
//			super.onPostExecute(result);
//			/*
//			 * if (mImageView.getTag().equals(mUrl)) {
//			 * mImageView.setImageBitmap(result); }
//			 */
//			ImageView imageView = (ImageView) mListView.findViewWithTag(mUrl);
//			if (imageView != null && result != null) {
//				imageView.setImageBitmap(result);
//			}
//		}
//	}
//
//	public void cancelAllTasks() {
//		if (mTask != null) {
//			for (NewsAsyncTask task : mTask) {
//				task.cancel(false);
//			}
//		}
//	}
//}
