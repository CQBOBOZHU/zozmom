package com.zozmom.ui.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.xutils.common.Callback.CommonCallback;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.SearchViewCompat.OnCloseListenerCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chasedream.zhumeng.R;
import com.zozmom.constants.Constant;
import com.zozmom.model.ComeTrueModel;
import com.zozmom.model.GoodsMaModel;
import com.zozmom.model.UserInfoModel;
import com.zozmom.net.XHttp;
import com.zozmom.ui.BaseTaskActivity;
import com.zozmom.ui.SubmitShowUpdataActivity;
import com.zozmom.ui.fragment.CommTixianFragment;
import com.zozmom.ui.user.AddressManagerActivity;
import com.zozmom.ui.user.UserCtrueActivity;
import com.zozmom.util.JsonUtil;
import com.zozmom.util.RequestErrorUtil;
import com.zozmom.util.ToastUtil;

@SuppressLint("UseSparseArrays")
public class ComeTrueListAdapter extends BaseTaskAdaper<ComeTrueModel> {
	UserInfoModel infoModel;
	Map<Integer, GoodsMaModel> goodMap = new HashMap<Integer, GoodsMaModel>();
	Map<Integer, Boolean> isshow = new HashMap<Integer, Boolean>();// true是开
																	// false是关
	Drawable opendraw;
	Drawable closedraw;
	public ComeTrueListAdapter(Context context, List<ComeTrueModel> mDatas,
			UserInfoModel infoModel) {
		super(context, mDatas);
		this.infoModel = infoModel;
		for (int i = 0; i < mDatas.size(); i++) {
			isshow.put(i, false);
		}
	}

	@Override
	public void setmDatas(List<ComeTrueModel> mDatas) {
		// TODO Auto-generated method stub
		super.setmDatas(mDatas);
		int mapSize = isshow.size();
		int mDataSize = mDatas.size();
		if (mDataSize > mapSize) {
			for (int i = mapSize; i < mDataSize; i++) {
				isshow.put(i, false);
			}
		};
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Viewholder viewholder;
		if (opendraw == null) {
			opendraw = mContext.getResources().getDrawable(R.drawable.take_on);
			opendraw.setBounds(0, 0, opendraw.getMinimumWidth(),
					opendraw.getMinimumHeight());
		}
		if (closedraw == null) {
			closedraw = mContext.getResources()
					.getDrawable(R.drawable.take_off);
			closedraw.setBounds(0, 0, closedraw.getMinimumWidth(),
					closedraw.getMinimumHeight());
		}

		if (convertView == null) {
			viewholder = new Viewholder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.come_true_listitem, null);
			viewholder.imageView = (ImageView) convertView
					.findViewById(R.id.user_getdream_picimageview);
			viewholder.gdNameView = (TextView) convertView
					.findViewById(R.id.user_getdream_name_tv);
			viewholder.gdcountView = (TextView) convertView
					.findViewById(R.id.user_getdream_value_tv);
			viewholder.jointimeView = (TextView) convertView
					.findViewById(R.id.user_getdream_need_textview);
			viewholder.button = (Button) convertView
					.findViewById(R.id.user_getdream_btn);
			viewholder.getuserView = (TextView) convertView
					.findViewById(R.id.user_dream_getuserview);
			viewholder.hintBtn = (Button) convertView
					.findViewById(R.id.user_daibtn);
			viewholder.wuliuBtn = (Button) convertView
					.findViewById(R.id.see_wuliuview);
			viewholder.wuliuTextView = (TextView) convertView
					.findViewById(R.id.wuliu_textview);
			viewholder.disView=(ImageView) convertView.findViewById(R.id.user_getdream_disimageview);
			convertView.setTag(viewholder);
		} else {
			viewholder = (Viewholder) convertView.getTag();
		}
		ComeTrueModel trueModel = mDatas.get(position);
		String title = trueModel.getProductTitle();
		String name=trueModel.getProductName();
		int issuseId = trueModel.getIssueId();
		int buycount = trueModel.getUserBuyCount();
		int statut = trueModel.getStatus();
		int productarea=trueModel.getProductArea();
		if(productarea==10){
			viewholder.disView.setVisibility(View.VISIBLE);
			viewholder.disView.setImageResource(R.drawable.ten_yuan);
		}else if(productarea==100){
			viewholder.disView.setVisibility(View.VISIBLE);
			viewholder.disView.setImageResource(R.drawable.hundred_yuan);
		}else{
			viewholder.disView.setVisibility(View.GONE);
		}
		
		
		String productImg = trueModel.getProductImg();
		loadImage(Constant.PRODUCT_URL + productImg + Constant.PRODUCT_KEY,
				viewholder.imageView);
		String luckNum = trueModel.getLuckyNumber();
		int luck = 10000000 + Integer.parseInt(luckNum);
		// viewholder.imageView
		viewholder.gdNameView.setText(name);
		viewholder.gdcountView.setText(issuseId + "");
		viewholder.jointimeView.setText("已经参与" + buycount + "人次");
		viewholder.getuserView.setText("幸运码逐梦码:    " + luck);
		viewholder.button.setTag(position);
		viewholder.wuliuBtn.setTag(position);
		
		if(statut==10||statut == 2){
			viewholder.button.setVisibility(View.GONE);
		}else{
			viewholder.button.setVisibility(View.VISIBLE);
		}
		if(statut == 1){
			viewholder.hintBtn.setVisibility(View.GONE);
		}else{
			viewholder.hintBtn.setVisibility(View.VISIBLE);
		}
		if(statut == 3){
			viewholder.wuliuBtn.setVisibility(View.VISIBLE);
		}else{
			viewholder.wuliuBtn.setVisibility(View.GONE);
		}
		
		if (statut == 1) {
			viewholder.button.setText("提交收货地址");
		} else if ((statut == 2)) {
			viewholder.hintBtn.setText("待发货");
//			viewholder.hintBtn.setBackgroundResource(R.drawable.btn_ground_co_gray);
//			viewholder.hintBtn.setTextColor(mContext.getResources().getColor(R.color.good_message_textcolors));
//			viewholder.button.setText("待发货");
		} else if ((statut == 3)) {
			viewholder.hintBtn.setText("已发货");
			viewholder.hintBtn.setBackgroundResource(R.drawable.btn_ground_co_green);
			viewholder.hintBtn.setTextColor(mContext.getResources().getColor(R.color.green));
			viewholder.button.setText("确认收货");
		} else if ((statut == 4)) {
			viewholder.hintBtn.setText("已收货");
//			good_message_textcolors
			viewholder.hintBtn.setBackgroundResource(R.drawable.btn_ground_co_green);
			viewholder.hintBtn.setTextColor(mContext.getResources().getColor(R.color.green));
			viewholder.button.setText("晒单");
		} else if ((statut == 10)) {
			viewholder.hintBtn.setText("交易完成");
			viewholder.hintBtn.setBackgroundResource(R.drawable.btn_ground_co_ye);
			viewholder.hintBtn.setTextColor(mContext.getResources().getColor(R.color.background));
		}
		viewholder.wuliuTextView.setTag(position);
		if (isshow.get(position)) {
			viewholder.wuliuTextView.setVisibility(View.VISIBLE);
		} else {
			viewholder.wuliuTextView.setVisibility(View.GONE);
		}
		viewholder.wuliuBtn.setOnClickListener(new MyClickListener(viewholder));
		viewholder.button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int tag = (int) v.getTag();
				ComeTrueModel model = mDatas.get(tag);
				int proudctId = model.getProductId();
				int status = model.getStatus();
				int id = model.getId();
				Intent intent;
				switch (status) {
					case 1 :
						intent = new Intent(mContext,
								AddressManagerActivity.class);
						intent.putExtra("id", id);
						((BaseTaskActivity<?>) mContext)
								.startActivityForResult(intent, 0);
						break;
					case 2 :
						ToastUtil.show(mContext, "点击有多快,你的速递员就有多快");
						break;
					case 3 :
						// requestOrder(proudctId);
						showIsSureGetProductDialog(tag);
						break;
					case 4 :
						intent = new Intent(mContext,
								SubmitShowUpdataActivity.class);
						intent.putExtra("id", id);
						((BaseTaskActivity<?>) mContext)
								.startActivityForResult(intent, 0);
						break;
					case 10 :
						break;
				}
			}
		});
		return convertView;
	}

	class MyClickListener implements OnClickListener {
		Viewholder viewholder;

		MyClickListener(Viewholder viewholde) {
			this.viewholder = viewholde;
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int position = (int) v.getTag();
			if (isshow.get(position)) {
				viewholder.wuliuTextView.setVisibility(View.GONE);
				((Button) v).setCompoundDrawables(null, null, opendraw, null);
				isshow.put(position, false);
			} else {
				GoodsMaModel maModel = goodMap.get(position);
				if (maModel == null) {
					ComeTrueModel model = mDatas.get(position);
					int id = model.getId();
					requestWuliu(id, position);
				} else {
					extracted(position, maModel);
					isshow.put(position, true);
					((Button) v).setCompoundDrawables(null, null, closedraw,
							null);
				}
			}
		}

		/**
		 * 查看物流
		 */
		public void requestWuliu(int id, final int position) {

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("lotteryId", id);
			XHttp.PostByToken(Constant.userLogisticsInfo, infoModel, map,
					new CommonCallback<JSONObject>() {

						@Override
						public void onCancelled(CancelledException arg0) {

						}

						@Override
						public void onError(Throwable arg0, boolean arg1) {
							Log.v("this", "arg0" + arg0);
							RequestErrorUtil.judge(arg0, mContext);
						}

						@Override
						public void onFinished() {

						}

						@Override
						public void onSuccess(JSONObject arg0) {
							Log.v("this", arg0.toString());
							try {
								GoodsMaModel goodsMaModel = (GoodsMaModel) JsonUtil
										.JsonToModel(arg0, GoodsMaModel.class);
								extracted(position, goodsMaModel);
								isshow.put(position, true);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

					});
		}
		private void extracted(final int position, GoodsMaModel goodsMaModel) {
			goodMap.put(position, goodsMaModel);
			String phone = goodsMaModel.getPhone();
			String logisticsCompany = goodsMaModel.getLogisticsCompany();// 物流公司
			String logistiscs = goodsMaModel.getLogistics();// 物流单号
			String consignee = goodsMaModel.getConsignee();// 收货人
			String provide = goodsMaModel.getProvince();
			String city = goodsMaModel.getCity();
			String district = goodsMaModel.getDistrict();
			String address = goodsMaModel.getAddress();
			String ad = provide + city + district + address;
			viewholder.wuliuTextView.setText("物流单号:" + logistiscs + "\n"
					+ "快递公司:" + logisticsCompany + "\n" + "收货人电话:" + phone
					+ "\n" + "收货地址:" + ad);
			viewholder.wuliuTextView.setVisibility(View.VISIBLE);
			viewholder.wuliuBtn.setCompoundDrawables(null, null, closedraw,
					null);
		}

	}

	class Viewholder {
		ImageView imageView;
		TextView gdNameView;
		TextView gdcountView;
		TextView jointimeView;
		TextView getuserView;
		Button button;
		Button hintBtn;
		Button wuliuBtn;
		TextView wuliuTextView;
		ImageView disView;
	}
	/**
	 * 确认收货
	 */
	public void requestOrder(final int position, int prodcutId, int id) {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("lotteryId", id);
		XHttp.PostByToken(Constant.confirmReceipt, infoModel, map,
				new CommonCallback<String>() {

					@Override
					public void onCancelled(CancelledException arg0) {

					}

					@Override
					public void onError(Throwable arg0, boolean arg1) {
						Log.v("this", "arg0" + arg0);
						RequestErrorUtil.judge(arg0, mContext);
					}

					@Override
					public void onFinished() {

					}

					@Override
					public void onSuccess(String arg0) {
						if (arg0.equals("true")) {
							isSureDialog.dismiss();
							mDatas.get(position).setStatus(4);
							ComeTrueListAdapter.this.notifyDataSetChanged();
						}
					}
				});
	}
	AlertDialog isSureDialog;
	public void showIsSureGetProductDialog(int position) {
		if(!((Activity) mContext).isFinishing())
		{
			AlertDialog.Builder builder = new Builder(mContext);
			isSureDialog = builder.create();
			isSureDialog.show();
			Window window = isSureDialog.getWindow();
			window.setContentView(R.layout.exit_activity);
			TextView titleView = (TextView) window
					.findViewById(R.id.exit_dialog_title);
			titleView.setText("请确定是否真的收到商品\n可别人财两空");
			Button sureBtn = (Button) window.findViewById(R.id.exitBtn0);
			Button cancelBtn = (Button) window.findViewById(R.id.exitBtn1);
			sureBtn.setTag(position);
			sureBtn.setOnClickListener(dialogListener);
			cancelBtn.setOnClickListener(dialogListener);
		}else{
			ToastUtil.show(mContext, "请重新进入");
		}
	}

	OnClickListener dialogListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.exitBtn0 :
					int tag = (int) v.getTag();
					// mDatas.get(tag);
					ComeTrueModel model = mDatas.get(tag);
					int proudctId = model.getProductId();
					// int status = model.getStatus();
					int id = model.getId();
					requestOrder(tag, proudctId, id);
					break;
				case R.id.exitBtn1 :
					isSureDialog.dismiss();
					break;

				default :
					break;
			}
		}
	};

}
