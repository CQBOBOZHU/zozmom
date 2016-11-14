//package com.zozmom.ui.adapter;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import com.chasedream.zhumeng.R;
//import com.zozmom.constants.UrlConstants;
//import com.zozmom.model.GoodsModel;
//import com.zozmom.pic.PhotoLoader;
//import com.zozmom.ui.IndexActivity;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.IntentSender.SendIntentException;
//import android.text.Html;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.view.WindowManager;
//import android.widget.BaseAdapter;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//
///**
// * 首页商品展示的listView的适配
// * 
// */
//public class AllGoodsListViewAdapter extends BaseAdapter {
//	List<GoodsModel> list = new ArrayList<GoodsModel>();
//	Context context;
//
//	public AllGoodsListViewAdapter(List<GoodsModel> list, Context context) {
//		this.list = list;
//		this.context = context;
//	}
//
//	@Override
//	public int getCount() {
//		// TODO Auto-generated method stub
//		return list.size();
//	}
//
//	@Override
//	public Object getItem(int position) {
//		// TODO Auto-generated method stub
//		return list.get(position);
//	}
//
//	@Override
//	public long getItemId(int position) {
//		// TODO Auto-generated method stub
//		return position;
//	}
//
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//
//		GoodsModel goodsModel = list.get(position);
//		ViewHolder holder;
//		if (convertView == null) {
//			convertView = LayoutInflater.from(context).inflate(
//					R.layout.list_fragment_listitem, null);
//			holder = new ViewHolder();
//			holder.imageView = (ImageView) convertView
//					.findViewById(R.id.list_fragment_image);
//			holder.textView = (TextView) convertView
//					.findViewById(R.id.list_fragment_title);
//			holder.textView2 = (TextView) convertView
//					.findViewById(R.id.goods_value_text);
//			holder.textView3 = (TextView) convertView
//					.findViewById(R.id.gallery_item_cun);
//			holder.textView4 = (TextView) convertView
//					.findViewById(R.id.gallery_item_allvalue);
//			holder.textView5 = (TextView) convertView
//					.findViewById(R.id.gallery_item_oth);
//			holder.bar = (ProgressBar) convertView
//					.findViewById(R.id.list_item_bar);
//			holder.imageButton = (ImageButton) convertView
//					.findViewById(R.id.list_fragment_add);
//			convertView.setTag(holder);
//		} else
//			holder = (ViewHolder) convertView.getTag();
//		PhotoLoader loader = new PhotoLoader(context);
//		String title = goodsModel.getProductName();
//		String price=goodsModel.getProductPrice();//总的价格
//		String count = goodsModel.getCurrentBuyCount();//已经参与的
//		int ls=Integer.parseInt(price)-Integer.parseInt(count);//剩余的
//		int a=Integer.parseInt(count)*100/Integer.parseInt(price);
//		if(a==0&&Integer.parseInt(count)>0){
//			a=1;
//		}
//		loader.loadPhoto(UrlConstants.PIC_URL + goodsModel.getHeadImage(),
//				holder.imageView);
//		holder.textView.setText(title);
//		holder.textView2.setText(price);
//		holder.textView3.setText(count);
//		holder.textView4.setText(price);
//		holder.textView5.setText(ls+"");
////		String newMessageInfo = "<font ><b>" + "开奖进度"
////				+ "</b></font><font color='red'><b>" + a + "%"
////				+ "</b></font>";
////		holder.textView2.setText(Html.fromHtml(newMessageInfo));
//		holder.bar.setProgress(a);
//		// TODO Auto-generated method stub
//		holder.imageButton.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				IndexActivity activity=(IndexActivity) context;
////				activity.add();
//			}
//		});
//		return convertView;
//	}
//
//
//	class ViewHolder {
//		ImageView imageView;//图片
//		TextView textView;//名称
//		TextView textView2;//总价格
//		TextView textView3;//已参与
//		TextView textView4;//总需
//		TextView textView5;//剩余
//		ProgressBar bar;
//		ImageButton imageButton;
//	}
//
//}
