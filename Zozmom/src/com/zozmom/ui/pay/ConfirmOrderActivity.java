package com.zozmom.ui.pay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.chasedream.zhumeng.R;
import com.zozmom.constants.Constant;
import com.zozmom.model.HongbaoModel;
import com.zozmom.model.OrderProductModel;
import com.zozmom.model.PayModel;
import com.zozmom.model.ProductModel;
import com.zozmom.model.UserInfoModel;
import com.zozmom.net.XHttp;
import com.zozmom.ui.BaseTaskActivity;
import com.zozmom.ui.LoginActivity;
import com.zozmom.ui.user.ChooseHongbaoActivity;
import com.zozmom.util.JsonUtil;
import com.zozmom.util.RequestErrorUtil;
import com.zozmom.util.ToastUtil;
/**
 * 确认订单
 * 
 * @author Administrator
 * 
 */
@ContentView(R.layout.confirm_orde_activity)
public class ConfirmOrderActivity extends BaseTaskActivity<JSONObject>
		implements
			OnCheckedChangeListener {
	@ViewInject(R.id.status_bar_title)
	TextView titleView;
	@ViewInject(R.id.confirm_order_cmm_image)
	ImageView goodimageView;// 图片
	@ViewInject(R.id.confirm_order_cmm_name)
	TextView goodnameView;// 名字
	@ViewInject(R.id.confirm_order_count_textview)
	TextView goodqihaoView;// 期号
	@ViewInject(R.id.confirm_order_cmm_price_textview)
	TextView goodallcountView;// 总价格
	@ViewInject(R.id.confirm_order_cmm_count_textview)
	TextView sycountView;// 剩余人次
	@ViewInject(R.id.confirm_order_cmm_bar)
	ProgressBar progressBar;
	@ViewInject(R.id.confirm_order_progess_textview)
	TextView progressText;// 进度百分比
	@ViewInject(R.id.confirm_order_maitimestextview)
	TextView jointimesView;// 参与次数
	@ViewInject(R.id.confirm_order_cmm_random_view)
	TextView randomView;// 随机数
	@ViewInject(R.id.confirm_order_allmoney)
	TextView moneyView;// 总价钱
	@ViewInject(R.id.confirm_order_hongbaomoney_textview)
	TextView hongbaoView;// 红包抵扣的额度
	@ViewInject(R.id.confirm_order_sfview)
	TextView lastmoneyView;// 最后需要支付的额度
	@ViewInject(R.id.muchhongbao_view)
	TextView muchView;// 提示用户有几个红包可以使用
	@ViewInject(R.id.chargehongbaoView)
	TextView chargeView;// 红包抵扣额度
	@ViewInject(R.id.confirm_order_cmm_disimage)
	ImageView disView;
	// @ViewInject(R.id.confirm_orde_checkbox1)
	// CheckBox box1;
	// @ViewInject(R.id.confirm_orde_checkbox2)
	// CheckBox box2;
	// @ViewInject(R.id.confirm_orde_checkbox3)
	// CheckBox box3;
	List<CheckBox> list = new ArrayList<CheckBox>();
	CheckBox checkBox;// 被选中的checkBox
	int count = 0;// 参加次数
	int chargehongbao = 0;// 红包抵扣钱
	int paylastmoney = 0;// 最后的钱
	int productArea;
	String redId = "";// 红包id
	String defaultBonusId;
	OrderProductModel ordermodel;
	ProductModel proModel;
	UserInfoModel infoModel;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		x.view().inject(this);
		titleView.setText("确认订单");
		infoModel = getLogginUserinfo();
		ordermodel = (OrderProductModel) getIntent().getSerializableExtra(
				"data");
		initView(ordermodel);
		orderInit();
		loadimage(goodimageView, Constant.PRODUCT_URL + showImage
				+ Constant.PRODUCT_KEY);
		// box1.setOnCheckedChangeListener(this);
		// box2.setOnCheckedChangeListener(this);
		// box3.setOnCheckedChangeListener(this);
		requestProductMessage();
		getHongbaoList();
	}

	private int issueId;
	private int productId;
	private int allCount;
	private int buyCount;
	private int syCount;
	private String productTitle;
	private String productName;
	private String showImage;
	private String jinducount;
	private int buytimes;
	private int progrss;
	private int random;

	public void initView(ProductModel model) {
		productId = model.getProductId();
		issueId = model.getIssueId();
		allCount = model.getAllCount();
		buyCount = model.getBuyCount();
		productName = model.getProductName();
		syCount = allCount - buyCount;
		productArea=model.getProductArea();
		if (allCount != 0) {
			progrss = buyCount * 100 / allCount;
		} else
			progrss = 0;
		if (buyCount != 0 && progrss == 0) {
			progrss = 1;
		}
		jinducount = progrss + "%";
		goodqihaoView.setText(String.valueOf(issueId));
		goodallcountView.setText("总需" + allCount + "人次");
		moneyView.setText(buytimes*productArea + "元逐梦币");
		sycountView.setText(String.valueOf(syCount));
		progressBar.setProgress(progrss);
		progressText.setText(jinducount);
		paylastmoney = buytimes*productArea;
		lastmoneyView.setText(paylastmoney + "元");
		if(productArea==10){
			disView.setVisibility(View.VISIBLE);
			disView.setImageResource(R.drawable.ten_yuan);
		}else if(productArea==100){
			disView.setVisibility(View.VISIBLE);
			disView.setImageResource(R.drawable.hundred_yuan);
		}
	}

	public void orderInit() {
		productTitle = ordermodel.getProductTitle();
		showImage = ordermodel.getShowImage();
		buytimes = ordermodel.getBuytimes();
		random = ordermodel.getRandomCode();
		goodnameView.setText(productName + productTitle);
		jointimesView.setText(String.valueOf(buytimes)+"人次");
		randomView.setText(String.valueOf(random));
//		moneyView.setText(buytimes*productArea + "元逐梦币");
//		paylastmoney = buytimes*productArea;
//		lastmoneyView.setText(paylastmoney + "元");
		count = buytimes;
	}

	public void chooseHongbao() {
		lastmoneyView.setText("");
	}

	public void chooseHongbao(View view) {
		Intent intent = null;
		if (infoModel == null) {
			intent = new Intent(this, LoginActivity.class);
		} else {
			if(productArea==0){
				return;
			}
			intent = new Intent(this, ChooseHongbaoActivity.class);
			intent.putExtra("count", count*productArea);
		}
		startActivityForResult(intent, 1);
	}

	public void requestProductMessage() {
		Map<String, Object> map = new HashMap<String, Object>();
		Log.e(TAG, productId + " ----" + issueId);
		map.put("productId", productId);

		XHttp.Post(Constant.LAST_PRODUCT_DETAIL, map,
				new CommonCallback<JSONObject>() {

					@Override
					public void onCancelled(CancelledException arg0) {

					}

					@Override
					public void onError(Throwable arg0, boolean arg1) {
						Log.e(TAG, arg0.toString());
						RequestErrorUtil.judge(arg0, getApplicationContext());
					}

					@Override
					public void onFinished() {

					}

					@Override
					public void onSuccess(JSONObject arg0) {
						Log.e(TAG, arg0.toString());
						try {
							proModel = (ProductModel) JsonUtil.JsonToModel(
									arg0, ProductModel.class);
							initView(proModel);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
	}

	/**
	 * 获取红包列表
	 * 
	 * @param view
	 */
	public void getHongbaoList() {
		if (infoModel == null) {
			return;
		}
		XHttp.PostByToken(Constant.userBonusList + 0, infoModel, null,
				new CommonCallback<JSONArray>() {

					@Override
					public void onCancelled(CancelledException arg0) {

					}

					@Override
					public void onError(Throwable arg0, boolean arg1) {
						Log.v("this", "onError" + arg0.toString());
						RequestErrorUtil.judge(arg0, m_activity);
					}

					@Override
					public void onFinished() {

					}

					@Override
					public void onSuccess(JSONArray arg0) {
						Log.v("this", "arg0" + arg0.toString());
						if (!arg0.toString().trim().equals("[]")) {
							List<HongbaoModel> mhongbaolist = JsonUtil
									.JsonToModel(arg0, HongbaoModel.class);
							if (mhongbaolist != null && mhongbaolist.size() > 0) {
								judeHongbao(mhongbaolist);
							}
						} else {
							muchView.setText("没有可用红包");
						}
					}
				});
	}

	List<HongbaoModel> hongbaoData = new ArrayList<HongbaoModel>();
	/**
	 * 提取比购买的额度小的红包
	 * 
	 * @param mhongbaolist
	 */
	protected void judeHongbao(List<HongbaoModel> mhongbaolist) {
		for (HongbaoModel hongbaoModel : mhongbaolist) {
			// int discountPrice = hongbaoModel.getDiscountPrice();
			int usedPrice = hongbaoModel.getUsedPrice();
			if (usedPrice <= count*productArea) {
				hongbaoData.add(hongbaoModel);
			}
		}
		extracted();
	}
	/**
	 * 红包 赋值
	 */
	private void extracted() {
		if (hongbaoData.size() > 0) {
			muchView.setText("有" + hongbaoData.size() + "个红包可用");
			HongbaoModel hongbaoModel = hongbaoData.get(0);
			defaultBonusId = hongbaoModel.getBonusId();
			chargehongbao = hongbaoModel.getDiscountPrice();
			redId = hongbaoModel.getBonusId();
			chargeView.setText("抵扣" + chargehongbao + "元");
			hongbaoView.setText(chargehongbao + "元");
			paylastmoney = count*productArea - chargehongbao;
			lastmoneyView.setText(paylastmoney + "元");
		} else {
			muchView.setText("没有可用红包");
		}
	}

	public void topay(View view) {
		if (infoModel == null) {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivityForResult(intent, 0);
			return;
		}
		RequestParams params = new RequestParams(Constant.CREATEORDER);
		long time = System.currentTimeMillis();
		params.addHeader("token-param", time + "," + infoModel.getToken() + ","
				+ infoModel.getUserId());
		Log.e(TAG, "infoModel:" + infoModel.getToken());
		params.addParameter("productId", productId);
		params.addParameter("issueId", issueId);
		params.addParameter("buyCount", count);
		params.addParameter("randomNum", random);
		params.addParameter("redId", redId);
		Log.e(TAG, "productId :" + productId + "issueid" + issueId + "userid:"
				+ infoModel.getUserId() + "buyCount:" + count + "redId" + redId);
		x.http().post(params, new CommonCallback<JSONObject>() {

			@Override
			public void onCancelled(CancelledException arg0) {

			}

			@Override
			public void onError(Throwable arg0, boolean arg1) {
				Log.e(TAG, "onError" + arg0.toString());
				RequestErrorUtil.judge(arg0, m_activity);
			}

			@Override
			public void onFinished() {
			}

			@Override
			public void onSuccess(JSONObject arg0) {
				Log.e(TAG, "onSuccess" + arg0.toString());
				// {"orderId":10000119,"dreamCoin":0,"price":1450}
				try {
					PayModel payModel = (PayModel) JsonUtil.JsonToModel(arg0,
							PayModel.class);
					Intent intent = new Intent(ConfirmOrderActivity.this,
							PayActivity.class);
					intent.putExtra("data", payModel);
					intent.putExtra("random", random);
					startActivity(intent);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
			case R.id.status_bar_Exit :
				finish();
				break;
			default :
				break;
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (checkBox != null) {
			checkBox.setChecked(false);
		}
		if (isChecked) {
			buttonView.setChecked(true);
			checkBox = (CheckBox) buttonView;
		} else {
			checkBox = null;
		}
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg1 == LoginActivity.resultCode) {
			infoModel = getLogginUserinfo();
			getHongbaoList();
		} else if (arg1 == ChooseHongbaoActivity.resultCode) {
			HongbaoModel model = (HongbaoModel) arg2
					.getSerializableExtra("model");
			String bonusId = model.getBonusId();
			if (defaultBonusId != null && bonusId.equals(defaultBonusId)) {

			} else {
				changeText(model);
			}
		} else if (arg1 == ChooseHongbaoActivity.resultCode1) {
			changeText(null);
		}
	}

	private void changeText(HongbaoModel model) {
		if (model == null) {
			redId = "";
			defaultBonusId = redId;
			chargehongbao = 0;
			chargeView.setText("不使用红包");
			hongbaoView.setText(chargehongbao + "元");
			paylastmoney = count - chargehongbao;
			lastmoneyView.setText(paylastmoney + "元");
		} else {
			chargehongbao = model.getDiscountPrice();
			redId = model.getBonusId();
			defaultBonusId = redId;
			chargeView.setText("抵扣" + chargehongbao + "元");
			hongbaoView.setText(chargehongbao + "元");
			paylastmoney = count - chargehongbao;
			lastmoneyView.setText(paylastmoney + "元");
		}
	}

	/**
	 * 次数不足的dialog
	 */
	public void showDialog() {

	}

}
