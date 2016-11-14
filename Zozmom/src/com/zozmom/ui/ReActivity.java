//package com.zozmom.ui;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
//import com.chasedream.zhumeng.R;
//import com.zozmom.cuview.CuGridView;
//import com.zozmom.model.GoodsModel;
//import com.zozmom.ui.adapter.GridViewAdapter;
//
//import android.os.Bundle;
//import android.view.View;
//import android.widget.TextView;
//
///**
// * 限购专区
// * 
// * @author Administrator
// * 
// */
//public class ReActivity extends BaseTaskActivity {
//	CuGridView cuGridView;
//	PullToRefreshScrollView scrollView;
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.re_activity);
////		if (listgoods != null || listgoods.size() == 0) {
////			initList();
////		}
////		scrollView=(PullToRefreshScrollView) findViewById(R.id.re_scrollview);
////		scrollView.getRefreshableView().smoothScrollTo(0, 0);
////		cuGridView = (CuGridView) findViewById(R.id.rs_gridView);
////		cuGridView.setAdapter(new GridViewAdapter(this,listgoods));
////		TextView textView = (TextView) findViewById(R.id.status_bar_title);
////		textView.setText(getRString(R.string.good_xiangou));
//
//	}
//
//	public void onClick(View v) {
//
//		switch (v.getId()) {
//		case R.id.status_bar_Exit:
//			finish();
//			break;
//
//		default:
//			break;
//		}
//	}
//
//	List<GoodsModel> listgoods = new ArrayList<GoodsModel>();
//
//	public void initList() {
//		String url = "/productImg/show/1052/1457934918700.jpg";
//		String name = "苹果（Apple）iPhone 6 Plus 64G版 4G手机";
//		String price = "6643";
//		String currentBuyCount = "1240";
//		GoodsModel gModel;
//		for (int i = 0; i < 10; i++) {
//			gModel = new GoodsModel();
//			gModel.setHeadImage(url);
//			gModel.setProductName(name);
//			gModel.setProductPrice(price);
//			gModel.setCurrentBuyCount(currentBuyCount);
//			listgoods.add(gModel);
//		}
//	}
//}
