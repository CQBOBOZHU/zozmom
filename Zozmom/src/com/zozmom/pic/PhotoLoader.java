package com.zozmom.pic;

import java.io.File;

import org.xutils.x;
import org.xutils.common.util.DensityUtil;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.image.ImageOptions.ParamsBuilder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;

import com.chasedream.zhumeng.R;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class PhotoLoader {
//	private static ImageOptions xutilOptions;
//
//	public static void initXutilImagloader() {
//		xutilOptions = new ImageOptions.Builder()
//				.setLoadingDrawableId(R.drawable.pic_loading)
//				// 设置图片在下载期间显示的图片
//				.setFailureDrawableId(R.drawable.pic_loading)
//				.setRadius(DensityUtil.dip2px(5)).setIgnoreGif(false).build();
//	}
//
//	public static void loadPhotoxUtil(String url, ImageView v) {
//		if (v != null && url != null && url.length() > 4
//				&& url.indexOf(".") > 0) {
//			if (url.trim().toLowerCase().endsWith(".gif")) {
//				return;
//			}
//			File f = new File(url);
//			if (f.exists()) {
//				x.image().bind(v, url, xutilOptions);
//			} else {
//				x.image().bind(v, url, xutilOptions);
//			}
//		}
//	}

	// private static BitmapUtils bitmapUtils = null;
	public static ImageLoader imageLoader = null;
	private static DisplayImageOptions roundOptions;
	private static DisplayImageOptions roundOptions1;
	private static DisplayImageOptions options;

	public PhotoLoader(Context context) {

		if (imageLoader == null)
			imageLoader = ImageLoader.getInstance();
			roundOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.pic_loading) // 下载中展示的图片
				.showImageForEmptyUri(R.drawable.pic_loading) // uri为空或者错误的时候展示的图片
				.showImageOnFail(R.drawable.pic_loading) // 下载失败展示图片
				.resetViewBeforeLoading(false) // default)//设置图片在下载前是否重置，复位
				.cacheInMemory(true)// 设置下载的图片是否缓存在内存中
				.cacheOnDisk(true) // 是否缓存到sd卡
				.considerExifParams(false) // //是否考虑JPEG图像EXIF参数（旋转，翻转）
				.imageScaleType(ImageScaleType.EXACTLY) // //设置图片以如何的编码方式显示//
														// EXACTLY 节省内存
				.bitmapConfig(Bitmap.Config.RGB_565) // /设置图片的解码类型// default
				.delayBeforeLoading(0)// int delayInMillis为你设置的下载前的延迟时间
				.displayer(new SimpleBitmapDisplayer()) // default 正常显示一张图片
				.build();

	}

	public void clearMemoryCache() {
		if (imageLoader != null) {
			imageLoader.clearMemoryCache();
		}
	}

	public void clearDiskCache() {
		if (imageLoader != null) {
			imageLoader.clearDiskCache();
		}
	}

	public void loadPhoto(String url, View v) {
		loadPhoto(url, v, false);
	}

	public void loadPhoto(String url, View v, boolean round) {
		if (v != null && url != null && url.length() > 4
				&& url.indexOf(".") > 0) {
			File f = new File(url);
			if (v instanceof ImageView) {
				imageLoader.displayImage(url, (ImageView) v, roundOptions);
			}
		}
	}

	public static void init() {
		if (imageLoader == null) {
			imageLoader = ImageLoader.getInstance();
			roundOptions1 = new DisplayImageOptions.Builder()
			// .showImageOnLoading(R.drawable.pic_loading) // 下载中展示的图片
					.showImageForEmptyUri(R.drawable.pic_loading) // uri为空或者错误的时候展示的图片
					.showImageOnFail(R.drawable.pic_loading) // 下载失败展示图片
					.resetViewBeforeLoading(false) // default)//设置图片在下载前是否重置，复位
					.cacheInMemory(true)// 设置下载的图片是否缓存在内存中
					.cacheOnDisk(true) // 是否缓存到sd卡
					.considerExifParams(false) // //是否考虑JPEG图像EXIF参数（旋转，翻转）
					.imageScaleType(ImageScaleType.EXACTLY) // //设置图片以如何的编码方式显示
					// default
					// 推荐.imageScaleType(ImageScaleType.EXACTLY)
					// 节省内存
					.bitmapConfig(Bitmap.Config.RGB_565) // /设置图片的解码类型// default
					.delayBeforeLoading(0)// int delayInMillis为你设置的下载前的延迟时间
					.displayer(new SimpleBitmapDisplayer()) // default
					.build();
		}
	}

	public static void loadPhoto1(String url, View v) {

		if (v != null && url != null && url.length() > 4
				&& url.indexOf(".") > 0) {
			File f = new File(url);
			if (v instanceof ImageView) {
				init();
				imageLoader.displayImage(url, (ImageView) v, roundOptions1);
			}
		}
	}

}