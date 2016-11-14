package com.zozmom.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.json.JSONObject;
import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import com.chasedream.zhumeng.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.zozmom.constants.Constant;
import com.zozmom.model.OrderProductModel;
import com.zozmom.model.ProductModel;
import com.zozmom.model.UserInfoModel;
import com.zozmom.net.XHttp;
import com.zozmom.ui.pay.ConfirmOrderActivity;
import com.zozmom.util.JsonUtil;
import com.zozmom.util.NetWorkUtil;
import com.zozmom.util.RequestErrorUtil;
import com.zozmom.util.ToastUtil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * 提交订单
 * 
 * @author Administrator
 * 
 */
@ContentView(R.layout.sumit_activity)
public class SubmitActivity extends BaseTaskActivity<Object>
		implements
			TextWatcher {
	@ViewInject(R.id.submit_add)
	Button addbtn;
	@ViewInject(R.id.submit_subtract)
	Button subtractbtn;
	@ViewInject(R.id.sumit_scrollview)
	PullToRefreshScrollView refreshScrollView;

	@ViewInject(R.id.times_one_textview)
	Button tenbtn;
	@ViewInject(R.id.times_two_textview)
	Button fiftybtn;
	@ViewInject(R.id.times_three_textview)
	Button hundredbtn;
	@ViewInject(R.id.times_four_textview)
	Button allbtn;// baowei
	@ViewInject(R.id.submit_random)
	Button randombtn;
	@ViewInject(R.id.status_bar_title)
	TextView titleView;
	@ViewInject(R.id.submit_edit)
	EditText submitTimeView;// 购买次数
	@ViewInject(R.id.cancel_btn)
	Button cancelbtn;
	@ViewInject(R.id.submit_btn)
	Button submitbtn;
	@ViewInject(R.id.show_luckname_btn)
	Button showbtn;// 随机数
	@ViewInject(R.id.sumit_disimageview)
	ImageView disView;

	@ViewInject(R.id.submit_luck_btn1)
	Button luckbtn1;
	@ViewInject(R.id.submit_luck_btn2)
	Button luckbtn2;
	@ViewInject(R.id.submit_luck_btn3)
	Button luckbtn3;
	@ViewInject(R.id.submit_luck_btn4)
	Button luckbtn4;
	@ViewInject(R.id.submit_luck_btn5)
	Button luckbtn5;
	@ViewInject(R.id.submit_luck_btn6)
	Button luckbtn6;
	@ViewInject(R.id.submit_luck_btn7)
	Button luckbtn7;
	@ViewInject(R.id.submit_luck_btn8)
	Button luckbtn8;
	@ViewInject(R.id.submit_luck_btn9)
	Button luckbtn9;

	@ViewInject(R.id.need_textview)
	TextView allCountView;
	@ViewInject(R.id.gallery_value_tv)
	TextView issueIdView;
	@ViewInject(R.id.gallery_c_tv)
	TextView syCountView;
	@ViewInject(R.id.sumit_name_tv)
	TextView productTitleView;
	@ViewInject(R.id.sumit_bar)
	ProgressBar progressBar;
	@ViewInject(R.id.sumit_jindu_textview)
	TextView jdCountView;
	@ViewInject(R.id.sumit_picimageview)
	ImageView productImageView;

	List<Button> buttons;
	List<Button> timebtns;

	Button btnfocuse;
	Button btnfocuse1;

	private int issueId;
	private int productId;
	private int allCount;
	private int buyCount;
	private int syCount = 1;
	private String productTitle;
	private String productName;
	private String showImage;
	private String jinducount;
	private int productArea;
	private int progrss;
	private ProductModel proModel;

	OrderProductModel ordermodel;
	UserInfoModel infoModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		x.view().inject(this);
		titleView.setText("提交订单");
		refreshScrollView.setMode(Mode.PULL_FROM_START);
		// refreshScrollView.setOnRefreshListener(listener);
		initBtns();
		alterBackground(getRandomNumber());
		infoModel = getLogginUserinfo();
		productId = getIntent().getIntExtra("productId", 1);
		submitTimeView.addTextChangedListener(this);
		requestLastPro();
	}

	OnRefreshListener2<ScrollView> listener = new OnRefreshListener2<ScrollView>() {

		@Override
		public void onPullDownToRefresh(
				PullToRefreshBase<ScrollView> refreshView) {
			// refreshPost();
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {

		}
	};

	private void extracted() {
		productTitle = proModel.getProductTitle();
		issueId = proModel.getIssueId();
		// productId = proModel.getProductId();
		allCount = proModel.getAllCount();
		buyCount = proModel.getBuyCount();
		productName = proModel.getProductName();
		showImage = proModel.getShowImage();
		syCount = allCount - buyCount;
		progrss = buyCount * 100 / allCount;
		productArea = proModel.getProductArea();
		if (buyCount != 0 && progrss == 0) {
			progrss = 1;
		}
		jinducount = progrss + "%";
		stanceView();
		loadimage(productImageView, Constant.PRODUCT_URL + showImage
				+ Constant.PRODUCT_KEY);
		if (syCount == 0) {
			clickableBtn(addbtn, false);
			clickableBtn(submitbtn, false);
		}
	}

	private void stanceView() {
		allCountView.setText(String.valueOf(allCount));
		issueIdView.setText(String.valueOf(issueId));
		syCountView.setText(String.valueOf(syCount));
		if (productArea == 10) {
			disView.setVisibility(View.VISIBLE);
			disView.setImageResource(R.drawable.ten_yuan);
		} else if (productArea == 100) {
			disView.setVisibility(View.VISIBLE);
			disView.setImageResource(R.drawable.hundred_yuan);
		}
		productTitleView.setText(productName + productTitle);
		jdCountView.setText(jinducount);
		progressBar.setProgress(progrss);
	}
	/**
	 * 刷新
	 */
	public void refreshPost() {
		refreshPost();
	}

	public void requestLastPro() {
		if (!NetWorkUtil.isNetworkAvailable(this)) {
			ToastUtil.show(this, getRString(R.string.network_er));
			refreshScrollView.onRefreshComplete();
			return;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("productId", productId);
		XHttp.Post(Constant.LAST_PRODUCT_DETAIL, map,
				new CommonCallback<JSONObject>() {

					@Override
					public void onCancelled(CancelledException arg0) {

					}

					@Override
					public void onError(Throwable arg0, boolean arg1) {
						refreshScrollView.onRefreshComplete();
						RequestErrorUtil.judge(arg0, getApplicationContext());
					}

					@Override
					public void onFinished() {

					}

					@Override
					public void onSuccess(JSONObject arg0) {
						refreshScrollView.onRefreshComplete();
						try {
							proModel = (ProductModel) JsonUtil.JsonToModel(
									arg0, ProductModel.class);
							extracted();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
	}

	@Override
	public void outLoactionDo() {
		super.outLoactionDo();
		submitTimeView.clearFocus();
	}

	/**
	 * 把商品次数Btn 和 randombtn 各放list;
	 */
	public void initBtns() {
		buttons = new ArrayList<Button>();
		buttons.add(luckbtn1);
		buttons.add(luckbtn2);
		buttons.add(luckbtn3);
		buttons.add(luckbtn4);
		buttons.add(luckbtn5);
		buttons.add(luckbtn6);
		buttons.add(luckbtn7);
		buttons.add(luckbtn8);
		buttons.add(luckbtn9);
		timebtns = new ArrayList<Button>();
		timebtns.add(tenbtn);
		timebtns.add(fiftybtn);
		timebtns.add(hundredbtn);
		timebtns.add(allbtn);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
			case R.id.status_bar_Exit :
				finish();
				break;
			case R.id.submit_add :
				addorsubtract(true);
				break;
			case R.id.submit_subtract :
				addorsubtract(false);
				break;
			case R.id.times_one_textview :
				alterBackground1(0);
				break;
			case R.id.times_two_textview :
				alterBackground1(1);
				break;
			case R.id.times_three_textview :
				alterBackground1(2);
				break;
			case R.id.times_four_textview :
				alterBackground1(3);
				break;
			case R.id.submit_random :
				alterBackground(getRandomNumber());
				break;
			case R.id.submit_luck_btn1 :
				alterBackground(1);
				break;
			case R.id.submit_luck_btn2 :
				alterBackground(2);
				break;
			case R.id.submit_luck_btn3 :
				alterBackground(3);
				break;
			case R.id.submit_luck_btn4 :
				alterBackground(4);
				break;
			case R.id.submit_luck_btn5 :
				alterBackground(5);
				break;
			case R.id.submit_luck_btn6 :
				alterBackground(6);
				break;
			case R.id.submit_luck_btn7 :
				alterBackground(7);
				break;
			case R.id.submit_luck_btn8 :
				alterBackground(8);
			case R.id.submit_luck_btn9 :
				alterBackground(9);
				break;
			default :
				break;
		}
	}

	/**
	 * 提交订单
	 * 
	 * @param view
	 */
	public void submitOrder(View view) {
		String submittimes = submitTimeView.getText().toString();
		if (submittimes == null) {
			submittimes = 1 + "";
		}
		ordermodel = new OrderProductModel();
		ordermodel.setAllCount(allCount);
		ordermodel.setBuyCount(buyCount);
		ordermodel.setShowImage(showImage);
		ordermodel.setProductId(productId);
		ordermodel.setIssueId(issueId);
		ordermodel.setProductTitle(productTitle);
		ordermodel.setProductName(productName);
		ordermodel.setBuytimes(Integer.parseInt(submittimes));
		int random = Integer.parseInt(showbtn.getText().toString());
		ordermodel.setRandomCode(random);
		Intent intent = new Intent(this, ConfirmOrderActivity.class);
		intent.putExtra("data", ordermodel);
		startActivity(intent);
	}

	/**
	 * 取消订单
	 * 
	 * @param view
	 */
	public void cancelOrder(View view) {
		finish();
	}

	@SuppressLint("NewApi")
	public void clickableBtn(Button btn, boolean bl) {
		if (bl) {
			if (!btn.isClickable()) {
				btn.setClickable(true);
			}
			btn.setBackgroundResource(R.drawable.btn_background_yellow);
		} else {
			if (btn.isClickable()) {
				btn.setClickable(false);
			}
			btn.setBackgroundResource(R.drawable.btn_background_lightgray);
		}
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
		}
	};

	public void addorsubtract(boolean bl) {
		setFocus();
		String times = submitTimeView.getText().toString();
		int ts = Integer.parseInt(times);
		if (bl) {
			ts++;
			if (ts == syCount) {
				clickableBtn(addbtn, false);
			} else {
				clickableBtn(addbtn, true);
			}
			if (ts > 1) {
				clickableBtn(subtractbtn, true);
			}
		} else {
			ts--;
			if (ts == 1) {
				clickableBtn(subtractbtn, false);
			} else {
				clickableBtn(subtractbtn, true);
			}
			if (ts < syCount) {
				clickableBtn(addbtn, true);
			}
		}
		alterBackground1(4);
		buyTimesViewChange(ts + "");
	}

	public void buyTimesViewChange(String times) {
		int ti = Integer.parseInt(times);
		if (ti < syCount) {
			if (ti > 1) {
				clickableBtn(subtractbtn, true);
			}
			clickableBtn(addbtn, true);
			submitTimeView.setText(times);
		}
		if (ti >= syCount) {
			clickableBtn(addbtn, false);
			clickableBtn(subtractbtn, false);
			if (syCount > 1) {
				clickableBtn(subtractbtn, true);
				submitTimeView.setText(syCount + "");
			} else {
				submitTimeView.setText(1 + "");
			}
		}
		setFocus();

	}

	public void luckNumViewChange(Object num) {
		showbtn.setText(num.toString());
	}

	Random random;

	public int getRandomNumber() {
		if (random == null) {
			random = new Random();
		}
		return random.nextInt(9) + 1;
	}

	@SuppressLint("NewApi")
	public void alterBackground1(int position) {
		switch (position) {
			case 0 :
				buyTimesViewChange("10");
				break;
			case 1 :
				buyTimesViewChange("50");
				break;
			case 2 :
				buyTimesViewChange("100");
				break;
			case 3 :
				buyTimesViewChange(String.valueOf(syCount));
				break;
		}
		if (position == 4) {
			if (btnfocuse1 != null) {
				btnfocuse1.setBackgroundResource(R.drawable.btn_ground_co);
				btnfocuse1.setTextColor(getResources().getColor(
						R.color.good_message_textcolors));
				btnfocuse1 = null;
			}
			return;
		}
		if (btnfocuse1 != null) {
			btnfocuse1.setBackgroundResource(R.drawable.btn_groundlow_co);
			btnfocuse1.setTextColor(getResources().getColor(
					R.color.good_message_textcolors));
		}
		Button btn = timebtns.get(position);
		btn.setBackgroundResource(R.drawable.btn_groundlow);
		btn.setTextColor(getResources().getColor(R.color.white));
		btnfocuse1 = btn;
	}

	@SuppressLint("NewApi")
	public void alterBackground(int position) {
		luckNumViewChange(position);
		if (btnfocuse != null) {
			btnfocuse.setBackgroundResource(R.drawable.circle_btn);
			btnfocuse.setTextColor(getResources().getColor(
					R.color.good_message_textcolors));
		}
		Button btn = buttons.get(position - 1);
		btn.setBackgroundResource(R.drawable.circle_btn_focused);
		btn.setTextColor(getResources().getColor(R.color.white));
		btnfocuse = btn;
	}

	public void togax(Editable s) {
		alterBackground1(4);
		String editend = s.toString();
		if (editend.startsWith("0")) {
			String eid = editend.substring(1);
			submitTimeView.setText(eid);
		}
		if (editend.trim().length() == 0 || editend.trim().equals(0)) {
			clickableBtn(subtractbtn, false);
			if (syCount > 1) {
				clickableBtn(addbtn, true);
			}
			submitTimeView.setText(1 + "");
			submitTimeView.setSelection(1);
		} else {
			int ap = Integer.parseInt(editend);
			if (ap > syCount) {
				clickableBtn(addbtn, false);
				if (syCount > 1) {
					clickableBtn(subtractbtn, true);
				}
				submitTimeView.setText(syCount + "");
				submitTimeView.setSelection(String.valueOf(syCount).length());
			} else if (ap < syCount) {
				clickableBtn(addbtn, true);
				if (ap > 1) {
					clickableBtn(subtractbtn, true);
				}
				if (ap == 1) {
					clickableBtn(subtractbtn, false);
				}
			}
		}
	}

	public void setFocus() {
		if (submitTimeView.hasFocus()) {
			submitTimeView.clearFocus();
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}

	@Override
	public void afterTextChanged(Editable s) {
		togax(s);
	}
	
	/**
	 * 随机数的介绍
	 * @param view
	 */
	public void whatRandom(View view) {
		Intent intent=new Intent(this,RandomActivity.class);
		startActivity(intent);
	}
}
