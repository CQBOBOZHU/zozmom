package com.zozmom.ui.fragment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.handmark.pulltorefresh.library.ScrollViewListener;
import com.chasedream.zhumeng.R;
import com.zozmom.constants.Constant;
import com.zozmom.cuview.CuGridView;
//import com.zozmom.cuview.CuGridView;
//import com.zozmom.cuview.CuScrollView;
//import com.zozmom.cuview.ScrollViewListener;
import com.zozmom.model.ProductModel;
import com.zozmom.net.XHttp;
import com.zozmom.ui.GoodMessageActivity;
import com.zozmom.ui.IndexActivity;
import com.zozmom.ui.adapter.GridViewAdapter;
import com.zozmom.ui.adapter.TitleImageAdapter;
import com.zozmom.util.JsonUtil;
import com.zozmom.util.LogUtil;
import com.zozmom.util.NetWorkUtil;
import com.zozmom.util.RequestErrorUtil;
import com.zozmom.util.ToastUtil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

@SuppressLint("NewApi")
public class IndexFragment extends BaseTaskFragment {

	List<String> list = new ArrayList<String>();
	View view;
	LinearLayout grlayout;
	List<ImageView> imageviews = new ArrayList<ImageView>();
	String tid = "";
	String id = "";
	String actionUrl = "";
	IndexActivity activity;
	PullToRefreshScrollView scrollView;
	CuGridView cuGridView;
	GridViewAdapter galleradapter;
	TitleImageAdapter viewpagerAdapter;
	int checkindex = 1;// 1 :人气 2: 最新 3:进度 4:总需
	int hotpager = 1;
	int thenewpager = 1;
	int progrsspager = 1;
	int allcountpager = 1;
	public static final int MaxSize = 10;

	List<ProductModel> hotlistgoods = new ArrayList<ProductModel>();
	List<ProductModel> newlistgoods = new ArrayList<ProductModel>();
	List<ProductModel> progresslistgoods = new ArrayList<ProductModel>();
	List<ProductModel> totallistgoods = new ArrayList<ProductModel>();
	ViewPager viewPager;

	@SuppressWarnings("deprecation")
	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (view == null) {
			LogUtil.v("IndexFragment:  onCreateView");
			view = inflater.inflate(R.layout.index_fragment, null);
			ImageButton exitbtn = (ImageButton) view
					.findViewById(R.id.status_bar_Exit);
			exitbtn.setVisibility(View.GONE);
			ImageView imageView = (ImageView) view
					.findViewById(R.id.status_bar_imageview);
			imageView.setVisibility(View.VISIBLE);
			activity = (IndexActivity) getActivity();
			initView();
			if (hotlistgoods == null || hotlistgoods.size() == 0) {
				chooserequest(1);
			}
			
		} else {
			viewpagerAdapter = new TitleImageAdapter(getChildFragmentManager(),
					image);
			viewPager.setOffscreenPageLimit(2);
			viewPager.setAdapter(viewpagerAdapter);
			viewPager.setOnPageChangeListener(changeListener);
			viewPager.postDelayed(new Runnable() {
				@Override
				public void run() {
					viewpagerAdapter.notifyDataSetChanged();
				}
			}, 100);
		}

		return view;
	}

	int[] image = {R.drawable.banner2, R.drawable.banner_box,
			R.drawable.banner_dream};

	@SuppressWarnings("deprecation")
	@SuppressLint({"NewApi", "CutPasteId"})
	public void initView() {
		initBitMap();
		viewPager = (ViewPager) view.findViewById(R.id.index_viewpager);
		rd1 = (ImageView) view.findViewById(R.id.rdo1);
		rd2 = (ImageView) view.findViewById(R.id.rdo2);
		rd3 = (ImageView) view.findViewById(R.id.rdo3);
		viewpagerAdapter = new TitleImageAdapter(getChildFragmentManager(),
				image);
		viewPager.setOffscreenPageLimit(2);
		viewPager.setAdapter(viewpagerAdapter);
		viewPager.setOnPageChangeListener(changeListener);
		ToStartThread();
		layoutpop1 = (View) view.findViewById(R.id.popuar_lay1);
		layoutpop = (View) view.findViewById(R.id.popuar_lay);
		initScrollView();
		LinearLayout poluarLayout = (LinearLayout) view
				.findViewById(R.id.popuar_lay);
		poluarLayout.setOnClickListener(listener);
		initGridView();
		initBtn();
	}

	OnPageChangeListener changeListener = new OnPageChangeListener() {

		@Override
		public void onPageSelected(int position) {
			index = position;
			imageC(position);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}
	};
	Thread thread;
	int start = 0;// 定义线程的状态
	int index = 0;// 定义滚动图的下标
	/**
	 * viewpager 滚动
	 */
	public void ToStartThread() {
		if (thread == null || !thread.isAlive()) {
			thread = new Thread() {
				public void run() {
					// 如果状态是0就无限滚动图片
					while (true) {
						try {
							// 设置延时为3秒
							sleep(3000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if (start == 0) {
							// 如果是最后一张图片就重新赋值为0
							if (index == image.length - 1) {
								index = 0;
							} else {
								// 否则+1
								index++;
							}
							// 通过hander改变滚动图
							handler.obtainMessage(0).sendToTarget();
						}
					}
				};
			};
			thread.start();
		}
	}
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {// 定义handler用来改变滚动图
		@Override
		public void handleMessage(Message msg) {
			LogUtil.v("handler :" + index);
			if (msg.what == 3) {
				if (viewpagerAdapter != null) {
					viewpagerAdapter.notifyDataSetChanged();
				}
			} else {
				if (viewPager != null) {
					viewPager.setCurrentItem(index);// 设置滚动图的图片
					imageC(index);
				}
			}
		}
	};
	Bitmap  focusedBitMap;
	Bitmap  notfocusedBitMap;
	public void imageChang(ImageView im1, ImageView im2, ImageView im3) {
		im1.setImageBitmap(focusedBitMap);
		im2.setImageBitmap(notfocusedBitMap);
		im3.setImageBitmap(notfocusedBitMap);
	}
	
	ImageView rd1, rd2, rd3;
	
	public void initBitMap(){
		focusedBitMap=BitmapFactory.decodeStream(getResources().openRawResource(R.drawable.cicle_focused));
		notfocusedBitMap=BitmapFactory.decodeStream(getResources().openRawResource(R.drawable.cicle));
	}
	
	/**
	 * 更改小点
	 * 
	 * @param position
	 */
	public void imageC(int position) {
		switch (position) {
			case 0 :
				imageChang(rd1, rd2, rd3);
				break;
			case 1 :
				imageChang(rd2, rd1, rd3);
				break;
			case 2 :
				imageChang(rd3, rd1, rd2);
				break;
		}
	}

	/**
	 * 初始化scrollview
	 */
	@SuppressLint("NewApi")
	private void initScrollView() {
		scrollView = (PullToRefreshScrollView) view
				.findViewById(R.id.index_scrollview);
		scrollView.setMode(Mode.BOTH);
		scrollView.setOnRefreshListener(refreshListener);
	}
	boolean isFresh = true;// true 表示下拉刷新 和 进入activity属性 false表示上啦刷新
	OnRefreshListener2<ScrollView> refreshListener = new OnRefreshListener2<ScrollView>() {

		@Override
		public void onPullDownToRefresh(
				PullToRefreshBase<ScrollView> refreshView) {
			// viewpagerAdapter.setImage(image);
			viewpagerAdapter.notifyDataSetChanged();
			isFresh = true;
			if (checkindex == 1) {
				hotpager = 1;
			} else if (checkindex == 2) {
				thenewpager = 1;
			} else if (checkindex == 3) {
				progrsspager = 1;
			} else if (checkindex == 4) {
				allcountpager = 1;
			}
			chooserequestFresh(checkindex);

		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
			isFresh = false;
			chooserequestFresh(checkindex);
		}
	};

	/**
	 * 初始化gridview
	 */
	private void initGridView() {
		cuGridView = (CuGridView) view.findViewById(R.id.index_gridView);
		cuGridView.setFocusable(false);
		galleradapter = new GridViewAdapter(activity, hotlistgoods, m_loader);
		cuGridView.setAdapter(galleradapter);
		scrollView.setOnScrollViewListener(new ScrollViewListener() {

			@SuppressLint("NewApi")
			@Override
			public void onScrollChanged(ScrollView scrollView, int x, int y,
					int oldx, int oldy) {
				if (y >= layoutpop.getY()) {
					layoutpop1.setVisibility(View.VISIBLE);
					start = 1;
				} else {
					layoutpop1.setVisibility(View.GONE);
					start = 0;
				}
			}
		});
		cuGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		cuGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				extracted(position);
			}

			private void extracted(int position) {
				Intent intent = new Intent(activity, GoodMessageActivity.class);
				ProductModel model = null;
				switch (checkindex) {
					case 1 :
						model = hotlistgoods.get(position);
						break;
					case 2 :
						model = newlistgoods.get(position);
						break;
					case 3 :
						model = progresslistgoods.get(position);
						break;
					case 4 :
						model = totallistgoods.get(position);
						break;
				}
				intent.putExtra("issueId", model.getIssueId());
				intent.putExtra("productId", model.getProductId());
				startActivity(intent);
			}
		});
	}

	View layoutpop;
	View layoutpop1;

	public void startActivity(Class<?> s) {
		Intent intent = new Intent(activity, s);
		startActivity(intent);
	}

	OnClickListener listener = new OnClickListener() {
		//
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.popu_btn1 :
					check(1);
					break;
				case R.id.popu_btn2 :
					check(1);
					break;
				case R.id.cmm_btn1 :
					check(2);
					break;
				case R.id.cmm_btn2 :
					check(2);
					break;
				case R.id.cum_btn1 :
					check(3);
					break;
				case R.id.cum_btn2 :
					check(3);
					break;
				case R.id.need_btn1 :
					check(4);
					break;
				case R.id.need_btn2 :
					check(4);
					break;
			}
		}
	};

	@SuppressLint("NewApi")
	public void check(int index) {
		checkindex = index;
		switch (index) {
			case 1 :
				chooserequest(1);
				BtnTextChange(popuBtn1, cmmbtn1, cumbtn1, needbtn1);
				BtnTextChange(popuBtn2, cmmbtn2, cumbtn2, needbtn2);
				break;
			case 2 :
				chooserequest(2);
				BtnTextChange(cmmbtn2, popuBtn2, cumbtn2, needbtn2);
				BtnTextChange(cmmbtn1, popuBtn1, cumbtn1, needbtn1);
				break;
			case 3 :
				chooserequest(3);
				BtnTextChange(cumbtn1, cmmbtn1, popuBtn1, needbtn1);
				BtnTextChange(cumbtn2, cmmbtn2, popuBtn2, needbtn2);
				break;
			case 4 :
				chooserequest(4);
				BtnTextChange(needbtn1, cumbtn1, cmmbtn1, popuBtn1);
				BtnTextChange(needbtn2, cumbtn2, cmmbtn2, popuBtn2);
				break;
		}
	}

	Button mPreSelectedBt;

	Button popuBtn1;
	Button popuBtn2;
	Button cmmbtn1;
	Button cmmbtn2;
	Button cumbtn1;
	Button cumbtn2;
	Button needbtn1;
	Button needbtn2;

	public void initBtn() {
		popuBtn1 = (Button) view.findViewById(R.id.popu_btn1);
		popuBtn2 = (Button) view.findViewById(R.id.popu_btn2);
		cmmbtn1 = (Button) view.findViewById(R.id.cmm_btn1);
		cmmbtn2 = (Button) view.findViewById(R.id.cmm_btn2);
		cumbtn1 = (Button) view.findViewById(R.id.cum_btn1);
		cumbtn2 = (Button) view.findViewById(R.id.cum_btn2);
		needbtn1 = (Button) view.findViewById(R.id.need_btn1);
		needbtn2 = (Button) view.findViewById(R.id.need_btn2);
		popuBtn1.setOnClickListener(listener);
		popuBtn2.setOnClickListener(listener);
		cmmbtn1.setOnClickListener(listener);
		cmmbtn2.setOnClickListener(listener);
		cumbtn1.setOnClickListener(listener);
		cumbtn2.setOnClickListener(listener);
		needbtn1.setOnClickListener(listener);
		needbtn2.setOnClickListener(listener);
	}
	Resources resources;
	@SuppressLint("ResourceAsColor")
	public void BtnTextChange(Button btn1, Button btn2, Button btn3, Button btn4) {
		if (null == resources) {
			resources = getResources();
		}
		btn1.setTextColor(resources.getColor(R.color.index_btn_chang));
		btn2.setTextColor(resources.getColor(R.color.index_btn));
		btn3.setTextColor(resources.getColor(R.color.index_btn));
		btn4.setTextColor(resources.getColor(R.color.index_btn));
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		// if (view!=null&&scrollView != null && isVisibleToUser == true) {
		// Log.v("this", "scrollView.getScrollY()" + scrollView.getScrollY());
		// scrollView.getRefreshableView().smoothScrollTo(0,
		// scrollView.getRefreshableView().getScrollY());
		// if (viewpagerAdapter != null) {
		// viewpagerAdapter.notifyDataSetChanged();
		// }
		if (isVisibleToUser) {
			start = 0;
		} else {
			start = 1;
		}
		// }

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onResume() {
		start = 0;
		super.onResume();
	}

	@Override
	public void onPause() {
		
		super.onPause();
		start = 1;
	}

	@Override
	public void onDestroyView() {
		LogUtil.v("IndexFragment:  onDestroyView");
		super.onDestroyView();
		// if (view != null) {
		// ((ViewGroup) view.getParent()).removeView(view);
		// }
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		try {
			Field childFragmentManager = Fragment.class
					.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);

		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		LogUtil.v("IndexFragment:  onDestroy");
	}

	/**
	 * 查询数据
	 * 
	 * @param type
	 */
	public void chooserequest(int type) {
		if (!NetWorkUtil.isNetworkAvailable(activity)) {
			ToastUtil.show(activity,
					getResources().getString(R.string.network_hint));
			return;
		}
		isFresh = true;
		switch (type) {
			case 1 :
				if (hotlistgoods != null && hotlistgoods.size() > 0) {
					notiAdapter(hotlistgoods);
					extracted();
					return;
				} else {
					setV();
					notiAdapter(hotlistgoods);
				}
				requestMessHot();
				break;
			case 2 :
				if (newlistgoods != null && newlistgoods.size() > 0) {
					notiAdapter(newlistgoods);
					extracted();
					return;
				} else {
					setV();
					notiAdapter(newlistgoods);
				}
				requestMessnew();
				break;
			case 3 :
				if (progresslistgoods != null && progresslistgoods.size() > 0) {
					notiAdapter(progresslistgoods);
					extracted();
					return;
				} else {
					setV();
					notiAdapter(progresslistgoods);
				}
				requestMessprogress();
				break;
			case 4 :
				if (totallistgoods != null && totallistgoods.size() > 0) {
					notiAdapter(totallistgoods);
					extracted();
					return;
				} else {
					setV();
					notiAdapter(totallistgoods);
				}
				requestMesstotal();
				break;

			default :
				break;
		}
	}

	/**
	 * 隐藏标题栏
	 */
	public void setV() {
		if (layoutpop1.getVisibility() == View.VISIBLE) {
			layoutpop1.setVisibility(View.GONE);
			start = 0;
		}
	}

	/**
	 * 查询数据
	 * 
	 * @param type
	 */
	public void chooserequestFresh(int type) {
		if (!NetWorkUtil.isNetworkAvailable(activity)) {
			ToastUtil.show(activity,
					getResources().getString(R.string.network_hint));
			// scrollView.onRefreshComplete();
			scrollView.postDelayed(new Runnable() {
				@Override
				public void run() {
					scrollView.onRefreshComplete();
					ToastUtil.show(m_activity, "没有更多商品");
				}
			}, 300);
			return;
		}
		switch (type) {
			case 1 :
				requestMessHot();
				break;
			case 2 :
				requestMessnew();
				break;
			case 3 :
				requestMessprogress();
				break;
			case 4 :
				requestMesstotal();
				break;
		}
	}

	public void requestMessHot() {
		Map<String, String> map = new HashMap<String, String>();
		if (!NetWorkUtil.isNetworkAvailable(m_activity)) {
			if (isFresh) {
				notiAdapter(hotlistgoods);
			} else {
				scrollView.postDelayed(new Runnable() {
					@Override
					public void run() {
						scrollView.onRefreshComplete();
						ToastUtil.show(m_activity, "没有更多商品");
					}
				}, 300);
			}
			return;
		}
		if (!isFresh) {
			int size = hotlistgoods.size();
			if (size == 0) {
				isFresh = true;
				map.put("currentPage", 1 + "");
			} else {
				if (hotlistgoods.size() < 10 * hotpager) {
					scrollView.postDelayed(new Runnable() {
						@Override
						public void run() {
							scrollView.onRefreshComplete();
						}
					}, 300);
					return;
				} else {
					map.put("currentPage", hotpager + 1 + "");
				}
			}
		} else {
			map.put("currentPage", 1 + "");
		}
		XHttp.Get(Constant.SELECTE_PRODUCT_BYTYPE_HOT, map,
				new CommonCallback<JSONArray>() {

					@Override
					public void onCancelled(CancelledException arg0) {

					}

					@Override
					public void onError(Throwable arg0, boolean arg1) {
						scrollView.onRefreshComplete();
						RequestErrorUtil.judge(arg0, activity);
						if (isFresh) {
							notiAdapter(hotlistgoods);
						}
					}

					@Override
					public void onFinished() {
					}

					@Override
					public void onSuccess(JSONArray arg0) {
						scrollView.onRefreshComplete();
						List<ProductModel> mlist = JsonUtil.JsonToModel(arg0,
								ProductModel.class);
						if (mlist != null && mlist.size() > 0) {
							if (isFresh) {
								hotpager = 1;
								hotlistgoods = mlist;
								galleradapter = new GridViewAdapter(activity,
										hotlistgoods, m_loader);
								cuGridView.setAdapter(galleradapter);
								extracted();
							} else {
								hotpager++;
								hotlistgoods.addAll(mlist);
								notiAdapter(hotlistgoods);
							}
						} else {
							ToastUtil.show(m_activity, "没有更多商品");
						}
					}

				});
	}

	private void extracted() {
		if (scrollView.getRefreshableView().getScrollY() > layoutpop.getY()) {
			scrollView.getRefreshableView().smoothScrollTo(0,
					(int) layoutpop.getY());
		}
	}
	@SuppressLint("NewApi")
	public void notiAdapter(List<ProductModel> list) {
		if (galleradapter == null) {
			galleradapter = new GridViewAdapter(activity, list, m_loader);
			cuGridView.setAdapter(galleradapter);
		} else {
			galleradapter.setmDatas(list);
			galleradapter.notifyDataSetChanged();
		}
	}

	public void requestMessnew() {
		Map<String, String> map = new HashMap<String, String>();
		if (!NetWorkUtil.isNetworkAvailable(m_activity)) {
			if (isFresh) {
				notiAdapter(newlistgoods);
			} else {
				scrollView.postDelayed(new Runnable() {
					@Override
					public void run() {
						scrollView.onRefreshComplete();
					}
				}, 300);
			}
			return;
		}

		if (!isFresh) {
			int size = newlistgoods.size();
			if (size == 0) {
				isFresh = true;
				map.put("currentPage", 1 + "");
			} else {
				if (size < MaxSize * thenewpager) {
					scrollView.postDelayed(new Runnable() {
						@Override
						public void run() {
							scrollView.onRefreshComplete();
							ToastUtil.show(m_activity, "没有更多商品");
						}
					}, 300);
					return;
				} else {
					map.put("currentPage", thenewpager + 1 + "");
				}
			}
		} else {
			map.put("currentPage", 1 + "");
		}

		XHttp.Get(Constant.SELECTE_PRODUCT_BYTYPE_NEW, map,
				new CommonCallback<JSONArray>() {

					@Override
					public void onCancelled(CancelledException arg0) {

					}

					@Override
					public void onError(Throwable arg0, boolean arg1) {

						scrollView.onRefreshComplete();
						RequestErrorUtil.judge(arg0, activity);
						if (isFresh) {
							notiAdapter(newlistgoods);
						}
					}

					@Override
					public void onFinished() {

					}

					@Override
					public void onSuccess(JSONArray arg0) {

						scrollView.onRefreshComplete();
						List<ProductModel> mlist = JsonUtil.JsonToModel(arg0,
								ProductModel.class);
						if (isFresh) {
							if (mlist != null && mlist.size() > 0) {
								newlistgoods = mlist;
								thenewpager = 1;
								notiAdapter(newlistgoods);
							} else {
								notiAdapter(newlistgoods);
							}
							extracted();
						} else {
							if (mlist != null && mlist.size() > 0) {
								thenewpager++;
								newlistgoods.addAll(mlist);
								notiAdapter(newlistgoods);
							} else {
								ToastUtil.show(m_activity, "没有更多商品");
							}
						}
					}
				});

	}

	public void requestMessprogress() {
		if (!NetWorkUtil.isNetworkAvailable(m_activity)) {
			if (isFresh) {
				notiAdapter(progresslistgoods);
			} else {
				scrollView.postDelayed(new Runnable() {
					@Override
					public void run() {
						scrollView.onRefreshComplete();
					}
				}, 300);
			}
			return;
		}
		Map<String, String> map = new HashMap<String, String>();
		if (!isFresh) {
			int size = progresslistgoods.size();
			if (size == 0) {
				isFresh = true;
				map.put("currentPage", 1 + "");
			} else if (size < MaxSize * progrsspager) {
				scrollView.postDelayed(new Runnable() {
					@Override
					public void run() {
						scrollView.onRefreshComplete();
						ToastUtil.show(m_activity, "没有更多商品");
					}
				}, 300);
				return;
			} else {
				map.put("currentPage", progrsspager + 1 + "");
			}
		} else {
			map.put("currentPage", 1 + "");
		}
		XHttp.Get(Constant.SELECTE_PRODUCT_BYTYPE_PROGRESS, map,
				new CommonCallback<JSONArray>() {

					@Override
					public void onCancelled(CancelledException arg0) {

					}

					@Override
					public void onError(Throwable arg0, boolean arg1) {

						scrollView.onRefreshComplete();
						RequestErrorUtil.judge(arg0, activity);
					}

					@Override
					public void onFinished() {

					}

					@Override
					public void onSuccess(JSONArray arg0) {

						scrollView.onRefreshComplete();

						List<ProductModel> mlist = JsonUtil.JsonToModel(arg0,
								ProductModel.class);
						if (isFresh) {
							if (mlist != null && mlist.size() > 0) {
								progrsspager = 1;
								progresslistgoods = mlist;
								notiAdapter(progresslistgoods);
							} else {
								notiAdapter(progresslistgoods);
							}
							extracted();
						} else {
							if (mlist != null && mlist.size() > 0) {
								progrsspager++;
								progresslistgoods.addAll(mlist);
								notiAdapter(progresslistgoods);
							} else {
								ToastUtil.show(m_activity, "没有更多商品");
							}
						}

					}
				});

	}

	public void requestMesstotal() {
		if (!NetWorkUtil.isNetworkAvailable(m_activity)) {
			if (isFresh) {
				notiAdapter(totallistgoods);
			} else {
				scrollView.postDelayed(new Runnable() {
					@Override
					public void run() {
						scrollView.onRefreshComplete();
					}
				}, 300);
			}
			return;
		}
		Map<String, String> map = new HashMap<String, String>();
		if (!isFresh) {
			int size = totallistgoods.size();
			if (size == 0) {
				map.put("currentPage", 1 + "");
			} else {
				if (size < MaxSize * allcountpager) {
					scrollView.postDelayed(new Runnable() {
						@Override
						public void run() {
							ToastUtil.show(m_activity, "没有更多商品");
							scrollView.onRefreshComplete();
						}
					}, 300);
					return;
				} else {
					map.put("currentPage", allcountpager + 1 + "");
				}
			}
		} else {
			map.put("currentPage", 1 + "");
		}

		XHttp.Get(Constant.SELECTE_PRODUCT_BYTYPE_TOTAL, map,
				new CommonCallback<JSONArray>() {

					@Override
					public void onCancelled(CancelledException arg0) {

					}

					@Override
					public void onError(Throwable arg0, boolean arg1) {

						scrollView.onRefreshComplete();
						RequestErrorUtil.judge(arg0, activity);
						if (isFresh) {
							notiAdapter(totallistgoods);
						}
					}

					@Override
					public void onFinished() {

					}

					@SuppressLint("NewApi")
					@Override
					public void onSuccess(JSONArray arg0) {
						scrollView.onRefreshComplete();
						List<ProductModel> mlist = JsonUtil.JsonToModel(arg0,
								ProductModel.class);
						if (isFresh) {
							if (mlist != null && mlist.size() > 0) {
								allcountpager = 1;
								totallistgoods = mlist;
								notiAdapter(mlist);
							} else {
								notiAdapter(totallistgoods);
							}
							extracted();
						} else {
							if (mlist != null && mlist.size() > 0) {
								allcountpager++;
								totallistgoods.addAll(mlist);
								notiAdapter(totallistgoods);
							} else {
								ToastUtil.show(m_activity, "没有更多商品");
							}
						}
					}
				});
	}

	/**
	 * 获取banner
	 */
	public void getBaner() {

	}

}
