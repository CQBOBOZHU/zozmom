package com.zozmom.ui.fragment;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import com.chasedream.zhumeng.R;
import com.zozmom.constants.Constant;
import com.zozmom.model.UserInfoModel;
import com.zozmom.net.XHttp;
import com.zozmom.ui.user.CommRecordActivity;
import com.zozmom.ui.user.CommissionActivity;
import com.zozmom.ui.user.ExchangestatusActivity;
import com.zozmom.util.NetWorkUtil;
import com.zozmom.util.ToastUtil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
/**
 * 兑换逐梦币
 * 
 * @author Administrator
 * 
 */
public class CommBeDreamCoin extends BaseTaskFragment {
	EditText editText;
	Button dBtn;
	Button allBtn;
	Drawable enbleDra;
	Drawable notenbleDra;
	UserInfoModel infoModel;
	float allCommCoin;
	public static final int RATE = 100;
	static CommBeDreamCoin beDreamCoin;

	public void setAllCommCoin(float allCommCoin) {
		this.allCommCoin = allCommCoin;
	}

	public CommBeDreamCoin(UserInfoModel infoModel, float allCommCoin) {
		this.infoModel = infoModel;
		this.allCommCoin = allCommCoin;
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = LayoutInflater.from(getActivity()).inflate(
				R.layout.comm_bedreamcoin_fragment, null);
		beDreamCoin=this;
		enbleDra = getResources().getDrawable(R.drawable.btn_background_yellow);
		notenbleDra = getResources().getDrawable(R.drawable.btn_background_d7);
		editText = (EditText) view.findViewById(R.id.comm_editview);
		dBtn = (Button) view.findViewById(R.id.duihuanBtn);
		allBtn = (Button) view.findViewById(R.id.all_Btn);
		dBtn.setOnClickListener(listener);
		allBtn.setOnClickListener(listener);
		editText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				toagex(s);
			}

		});
		return view;
	}

	OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.duihuanBtn :
					dreamCoin();
					break;
				case R.id.all_Btn :
					if(allCommCoin<1){
						ToastUtil.show(m_activity, "佣金至少大于1");
						return;
					}
					editText.setText("" + (int)allCommCoin);
					break;
				default :
					break;
			}
		}
	};

	@SuppressLint("NewApi")
	public void toagex(Editable s) {
		if (s != null) {
			String endtemp = s.toString();
			if (endtemp.startsWith("0")) {
				endtemp = endtemp.substring(1);
				editText.setText(endtemp);
			}
		}

		if (s != null && !s.toString().trim().equals("")
				&& !s.toString().trim().equals("0")) {
			String coin = s.toString();
			int comcoin = Integer.parseInt(coin);
			if (comcoin > allCommCoin) {
				dBtn.setBackground(notenbleDra);
				dBtn.setEnabled(false);
			} else {
				dBtn.setBackground(enbleDra);
				dBtn.setEnabled(true);
			}
		} else {
			dBtn.setBackground(notenbleDra);
			dBtn.setEnabled(false);
		}
	}

	int dCoin;
	/**
	 * 兑换逐梦币
	 */
	public void dreamCoin() {
		String amount = editText.getText().toString().trim();
		if (amount == null || amount.equals("0")||amount.length()==0) {
			ToastUtil.show(m_activity, "输入错误");
			return;
		}
		dCoin = Integer.parseInt(amount) * RATE;
		if (dCoin == 0) {
			ToastUtil.show(m_activity, "不能为0");
			return;
		}
		if (!NetWorkUtil.isNetworkAvailable(m_activity)) {
			ToastUtil.show(m_activity, "网络错误");
			return;
		}
		if(allCommCoin<dCoin/RATE){
			ToastUtil.show(m_activity, "兑换数不能大于总佣金");
			return;
		}
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("type", "dreamCoin");
		map.put("amount", dCoin);
		XHttp.PostByToken(Constant.D_COMM, infoModel, map,
				new CommonCallback<String>() {

					@Override
					public void onCancelled(CancelledException arg0) {

					}

					@Override
					public void onError(Throwable arg0, boolean arg1) {
						Log.v("this", arg0.toString());
					}

					@Override
					public void onFinished() {

					}

					@Override
					public void onSuccess(String arg0) {
						Intent intent = new Intent(m_activity,
								ExchangestatusActivity.class);
						intent.putExtra("type", 0);
						if (arg0.equals("true")) {
							 ToastUtil.show(getActivity(), "兑换成功");
							Log.v("this", "兑换成功");
							int dcoin = dCoin / RATE;
							
							DecimalFormat df = new DecimalFormat("0.00");//格式化小数，不足的补0
							allCommCoin=Float.parseFloat(df.format((float)(allCommCoin - dcoin)));
//							allCommCoin = allCommCoin - dcoin;
							Log.v("this", "allCommCoin"+allCommCoin);
							CommissionActivity.commissionActivity
									.changeCoin(allCommCoin);
							MeFragment.meFragment.setViewText(allCommCoin,
									dcoin);
							CommTixianFragment.commTixianFragment.setAllCommCoin(allCommCoin);
							intent.putExtra("result", "success");
							intent.putExtra("coin", dcoin);
						} else {
							intent.putExtra("result", "fail");
						}
						m_activity.startActivity(intent);
					}
				});
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		beDreamCoin=null;
	}
}
