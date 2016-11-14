package com.zozmom;

import java.io.File;

import org.xutils.DbManager;
import org.xutils.DbManager.DbUpgradeListener;
import org.xutils.x;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.FIFOLimitedMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.umeng.socialize.PlatformConfig;
import com.zozmom.util.UpgradeUtil;

import android.app.Application;
import android.content.Context;
import android.util.Log;

public class Myapplication extends Application {
	DbManager.DaoConfig daoConfig;

//	RefWatcher mRefWatcher;
	public DbManager.DaoConfig getdaoConfig() {
		return daoConfig;
	}
	@Override
	public void onCreate() {
		super.onCreate();
//		mRefWatcher = LeakCanary.install(this);
		ZozmomContext.getInstance(getApplicationContext());
		x.Ext.init(this);
		x.Ext.setDebug(true);
		daoConfig = new DbManager.DaoConfig().setDbName("zozmom_db")
				.setAllowTransaction(true).setDbVersion(1)
				.setDbUpgradeListener(new DbUpgradeListener() {

					@Override
					public void onUpgrade(DbManager arg0, int arg1, int arg2) {

					}
				});// 更新数据库
					// com.zozmom.manager.AccountManager.getInstance().setlocalUser();
		setSdk();
		initImageLoader(getApplicationContext());
	}

	public void setSdk() {
		PlatformConfig.setWeixin("wxef020bf683e76488",
				"1b1443c19b1c4f9ea8088a4e287caf1e");
		PlatformConfig.setQQZone("1105362336", "DDfRdCJTZGic57QN");
	}

	public static void initImageLoader(Context context) {

		File cacheDir = StorageUtils.getCacheDirectory(context);
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context)
				.memoryCacheExtraOptions(480, 800)
				// default = device screen dimensions
				.threadPoolSize(2)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.denyCacheImageMultipleSizesInMemory()//  当同一个Uri获取不同大小的图片，缓存到内存时，只缓存一个。默认会缓存多个不同的大小的相同图片
//				.memoryCache(new LruMemoryCache(10*1024*1024))
//				.memoryCacheSize(10 * 1024 * 1024)
				.memoryCacheSizePercentage(20)//默认内存分配的1/8
//				.diskCacheSize(120*1024*1024)
//				.diskCacheFileCount(1000)
				.diskCache(new UnlimitedDiskCache(cacheDir))
				.diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
				// // default
				// .imageDecoder(new BaseImageDecoder()) // default
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				// default
				.imageDownloader(
						new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间  
				.writeDebugLogs()
				.build();

		ImageLoader.getInstance().init(config);
	}
}
