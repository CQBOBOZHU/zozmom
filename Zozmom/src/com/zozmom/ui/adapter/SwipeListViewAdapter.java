//package com.zozmom.ui.adapter;
//
//import java.util.List;
//
//import com.chasedream.zhumeng.R;
//import com.zozmom.constants.UrlConstants;
//import com.zozmom.cuview.swipelistview.SwipeListView;
//import com.zozmom.pic.PhotoLoader;
//import com.zozmom.ui.fragment.ShowFragment;
//
//import android.R.integer;
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.View.OnFocusChangeListener;
//import android.view.View.OnTouchListener;
//import android.view.ViewGroup;
//import android.view.View.OnClickListener;
//import android.widget.BaseAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//public class SwipeListViewAdapter extends BaseAdapter {
//
//	@Override
//	public int getCount() {
//		// TODO Auto-generated method stub
//		return objects.size();
//	}
//
//	@Override
//	public Object getItem(int arg0) {
//		// TODO Auto-generated method stub
//		return objects.get(arg0);
//	}
//
//	@Override
//	public long getItemId(int arg0) {
//		// TODO Auto-generated method stub
//		return arg0;
//	}
//
//	private LayoutInflater mInflater;
//	private List<GoodsModel> objects;
//	private SwipeListView mSwipeListView;
//	private Context context;
//	private int index;
//
//	public SwipeListViewAdapter(Context context, int textViewResourceId,
//			List<GoodsModel> objects, SwipeListView mSwipeListView) {
//		this.context = context;
//		this.objects = objects;
//		this.mSwipeListView = mSwipeListView;
//		mInflater = (LayoutInflater) context
//				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//	}
//
//	@Override
//	public View getView(final int position, View convertView, ViewGroup parent) {
//		final ViewHolder holder;
//		if (convertView == null) {
//			convertView = mInflater.inflate(R.layout.news_row, parent, false);
//			holder = new ViewHolder();
//			holder.imageView = (ImageView) convertView
//					.findViewById(R.id.left_imageview);
//			holder.titleTText = (TextView) convertView
//					.findViewById(R.id.left_title_text);
//			holder.othText = (TextView) convertView
//					.findViewById(R.id.left_oth_text);
//			convertView.setTag(holder);
//			holder.deletebutton = (Button) convertView
//					.findViewById(R.id.example_row_b_action_dd);
//			holder.addbtn = (Button) convertView.findViewById(R.id.left_add);
//			holder.removebtn = (Button) convertView
//					.findViewById(R.id.left_removebtn);
//			holder.editText = (EditText) convertView
//					.findViewById(R.id.but_times_edit);
//		} else {
//			holder = (ViewHolder) convertView.getTag();
//		}
//		GoodsModel goodsModel = objects.get(position);
//		String url = goodsModel.getHeadImage();
//		String title = goodsModel.getProductName();
//		String ti = goodsModel.getCurrentBuyCount();
//		String pi = goodsModel.getProductPrice();
//		int times = Integer.parseInt(pi) - Integer.parseInt(ti);
//		PhotoLoader loader = new PhotoLoader(context);
//		loader.loadPhoto(UrlConstants.PIC_URL+url, holder.imageView);
//		holder.titleTText.setText(title);
//		holder.othText.setText("剩余次数:"+times + "");
//		holder.editText.setTag(position);
//		editText = holder.editText;
//		holder.editText.setOnTouchListener(new OnTouchListener() {
//			
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				// TODO Auto-generated method stub
//				if (event.getAction() == MotionEvent.ACTION_UP) {
//					index = (Integer) v.getTag();
//				}
//				return false;
//			}
//		});
//		class MyTextWatcher implements TextWatcher {
//			
//			public MyTextWatcher(ViewHolder holder) {
//				mHolder = holder;
//			}
//			
//			private ViewHolder mHolder;
//			
//			public void beforeTextChanged(CharSequence s, int start,
//					int count, int after) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			public void onTextChanged(CharSequence s, int start,
//					int before, int count) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			public void afterTextChanged(Editable s) {
//				// TODO Auto-generated method stub
//				if (s != null && !"".equals(s.toString())) {
////					int position = (Integer) mHolder.numEdit.getTag();
////					mData.get(position).put("list_item_inputvalue",
////							s.toString());
//				}
//			}
//		}
//		holder.editText.addTextChangedListener(new MyTextWatcher(holder));
//		holder.addbtn.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				int position=(Integer) holder.editText.getTag();
//				Log.d("zhang", "clickposition = " + position);
//				String edittextStr = holder.editText.getText().toString();
//				int num = Integer.parseInt(edittextStr);
//				num++;
//				holder.editText.setText(num+"");
//			}
//		});
//		
//	holder.removebtn.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				int position=(Integer) holder.editText.getTag();
//				Log.d("zhang", "clickposition = " + position);
//				String edittextStr = holder.editText.getText().toString();
//				int num = Integer.parseInt(edittextStr);
//				num--;
//				holder.editText.setText(num+"");
//			}
//		});
//		
//		holder.removebtn.setOnClickListener(clickListener);
////		holder.deletebutton.setOnClickListener(clickListener);
////		holder.editText.addTextChangedListener(watcher);
//		return convertView;
//	}
//	EditText editText;
//	
//	public void changEdit(){
//		
//	}
//	// EditText editText;
//	// int buylis;
//	OnClickListener clickListener = new OnClickListener() {
//
//		@Override
//		public void onClick(View v) {
//			// TODO Auto-generated method stub
//			switch (v.getId()) {
//			case R.id.left_add:
//				break;
//			case R.id.left_removebtn:
//
//				break;
//			case R.id.example_row_b_action_dd:
//
//				break;
//
//			default:
//				break;
//			}
//		}
//	};
//
//	TextWatcher watcher = new TextWatcher() {
//		int editstart = 0;
//		int editend = 0;
//		CharSequence temp;
//
//		@Override
//		public void onTextChanged(CharSequence s, int start, int before,
//				int count) {
//			// TODO Auto-generated method stub
//			if (Integer.parseInt(s.toString()) != 0) {
//				temp = s;
//			}
//		}
//
//		@Override
//		public void beforeTextChanged(CharSequence s, int start, int count,
//				int after) {
//			// TODO Auto-generated method stub
//		}
//
//		@Override
//		public void afterTextChanged(Editable s) {
//			// TODO Auto-generated method stub
//		}
//	};
//
//	class ViewHolder {
//		ImageView imageView;
//		TextView titleTText;
//		TextView othText;
//		Button deletebutton;
//		Button addbtn;
//		Button removebtn;
//		EditText editText;
//	}
//
//}
