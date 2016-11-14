package com.zozmom.ui;

import java.io.ByteArrayOutputStream;
import java.util.List;

import com.chasedream.zhumeng.R;
import com.zozmom.cuview.HackyViewPager;
import com.zozmom.ui.fragment.ImageDetailFragment;
import com.zozmom.util.ImageUtil;
import com.zozmom.util.ToastUtil;
import com.zozmom.util.Util;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class ImagePagerActivity extends BaseActivity<Object> {
	private static final String STATE_POSITION = "STATE_POSITION";
	public static final String EXTRA_IMAGE_INDEX = "image_index";
	public static final String EXTRA_IMAGE_URLS = "image_urls";

	private HackyViewPager mPager;
	private int pagerPosition;
	private TextView indicator;
	private List<String> list;
	int picid = 0;
	View layview;
	ImageButton imageButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_detail_pager);
		Intent intent = this.getIntent();
		list = intent.getStringArrayListExtra(EXTRA_IMAGE_URLS);
		pagerPosition = intent.getIntExtra(EXTRA_IMAGE_INDEX, 0);
		imageButton = (ImageButton) findViewById(R.id.image_detail);
		mPager = (HackyViewPager) findViewById(R.id.pager);
		ImagePagerAdapter mAdapter = new ImagePagerAdapter(
				getSupportFragmentManager(), list);
		mPager.setAdapter(mAdapter);
		indicator = (TextView) findViewById(R.id.indicator);
		layview = findViewById(R.id.tolayout);
		CharSequence text = getString(R.string.viewpager_indicator, 1, mPager
				.getAdapter().getCount());
		indicator.setText(text);
		// 更新下标
		mPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageSelected(int arg0) {
				picid = arg0;
				CharSequence text = getString(R.string.viewpager_indicator,
						arg0 + 1, mPager.getAdapter().getCount());
				indicator.setText(text);
			}

		});
		if (savedInstanceState != null) {
			pagerPosition = savedInstanceState.getInt(STATE_POSITION);
		}

		mPager.setCurrentItem(pagerPosition);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_POSITION, mPager.getCurrentItem());
	}

	private class ImagePagerAdapter extends FragmentStatePagerAdapter {

		public List<String> fileList;

		public ImagePagerAdapter(FragmentManager fm, List<String> fileList) {
			super(fm);
			this.fileList = fileList;
		}

		@Override
		public int getCount() {
			return fileList == null ? 0 : fileList.size();
		}

		@Override
		public Fragment getItem(int position) {
			String url = fileList.get(position);
			return ImageDetailFragment.newInstance(url);
			// return null;
		}

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.popup_btn1:
			new Thread() {
				@Override
				public void run() {
					super.run();
					try {
						ImageUtil
								.downloadImage(list.get(picid), Util.getTime());
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}.start();
			ToShow(isShow, imageButton);
			ToastUtil.show(ImagePagerActivity.this, "保存到SD/zozmom/image/");
			break;
		case R.id.popup_btn2:
			ToShow(isShow, imageButton);
			break;
		case R.id.popup_btn3:
			ToShow(isShow, imageButton);
			break;
		case R.id.image_detail:
			ToShow(isShow, imageButton);
			break;
		case R.id.left:
			finish();
			break;
		default:
			break;
		}
	}

	boolean isShow = false;

	public void ToShow(boolean bl, ImageButton view) {
		if (bl) {
			view.setImageResource(R.drawable.take_off);
			hideListAnimation();
			isShow = false;
		} else {
			view.setImageResource(R.drawable.take_on);
			layview.setVisibility(View.VISIBLE);
			showListAnimation();
			isShow = true;
		}
	}

	/**
	 * listview加载动画
	 */
	public void showListAnimation() {
		TranslateAnimation ta = new TranslateAnimation(1, 0f, 1, 0f, 1, 1f, 1,
				0f);
		ta.setDuration(200);
		layview.startAnimation(ta);
	}

	ListView listview;

	/**
	 * listview隐藏动画，设置透明度
	 */
	public void hideListAnimation() {
		TranslateAnimation ta = new TranslateAnimation(1, 0f, 1, 0f, 1, 0f, 1,
				1f);
		ta.setDuration(200);
		layview.startAnimation(ta);
		ta.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				layview.setVisibility(8);
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			if (layview.getVisibility() == View.GONE) {
				ToShow(isShow, imageButton);
				return true;
			} else {
				ToShow(isShow, imageButton);
				return true;
			}
		}
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (layview.getVisibility() == View.VISIBLE) {
				ToShow(isShow, imageButton);
				return true;
			} else {
				finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	static final int THUMB_SIZE = 150;

	OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int positon,
				long arg3) {
			ToastUtil.show(ImagePagerActivity.this, "点击的数" + positon);
//			final int a = positon;
			new Thread() {
				public void run() {
					try {
//						String path = ImageUtil.downloadImage(list.get(picid),
//								Util.getTime());
					} catch (Exception e) {
						e.printStackTrace();
					}
				};

			}.start();

		}
	};

	public byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
		int i;
		int j;
		if (bmp.getHeight() > bmp.getWidth()) {
			i = bmp.getWidth();
			j = bmp.getWidth();
		} else {
			i = bmp.getHeight();
			j = bmp.getHeight();
		}

		Bitmap localBitmap = Bitmap.createBitmap(i, j, Bitmap.Config.RGB_565);
		Canvas localCanvas = new Canvas(localBitmap);

		while (true) {
			localCanvas.drawBitmap(bmp, new Rect(0, 0, i, j), new Rect(0, 0, i,
					j), null);
			if (needRecycle)
				bmp.recycle();
			ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
			localBitmap.compress(Bitmap.CompressFormat.JPEG, 100,
					localByteArrayOutputStream);
			localBitmap.recycle();
			byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
			try {
				localByteArrayOutputStream.close();
				return arrayOfByte;
			} catch (Exception e) {
				// F.out(e);
			}
			i = bmp.getHeight();
			j = bmp.getHeight();
		}
	}
}