package com.zozmom.ui.fragment;

import java.util.HashMap;
import java.util.Map;

import com.chasedream.zhumeng.R;
import com.zozmom.constants.Constant;
import com.zozmom.model.UserInfoModel;
import com.zozmom.net.XHttp;
import com.zozmom.ui.user.CommissionActivity;
import com.zozmom.util.NetWorkUtil;
import com.zozmom.util.ToastUtil;
import com.zozmom.util.Tools;

import android.annotation.SuppressLint;
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
 * 提现
 * 
 * @author Administrator
 * 
 */
public class CommTixianFragment extends BaseTaskFragment {
	UserInfoModel infoModel;
	float allCommCoin;
	EditText userNameView;
	EditText bankNumView;
	EditText bankNameView;// 银行名称
	EditText bankSnameView;// 支行
	EditText coinView;// 金额
	Button payBtn;
	int lastCoin = 0;
	Button tixianbtn;
	public static final int RATE = 100;
	static  CommTixianFragment commTixianFragment;

	public void setAllCommCoin(float allCommCoin) {
		this.allCommCoin = allCommCoin;
	}

	public CommTixianFragment(UserInfoModel infoModel, float allCommCoin) {
		this.infoModel = infoModel;
		this.allCommCoin = allCommCoin;
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = LayoutInflater.from(getActivity()).inflate(
				R.layout.comm_tixian_fragment, null);
		commTixianFragment=this;
		userNameView = (EditText) view.findViewById(R.id.bankuser_nameView);
		bankNumView = (EditText) view.findViewById(R.id.bank_num_view);
		bankNameView = (EditText) view.findViewById(R.id.bank_name_view);
		bankSnameView = (EditText) view.findViewById(R.id.bank_second_view);
		coinView = (EditText) view.findViewById(R.id.coin_view);
		coinView.addTextChangedListener(watcher);
		tixianbtn=(Button) view.findViewById(R.id.tixian_btn);
		tixianbtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				requesPay();
			}
		});
		return view;
	}
	
	

	TextWatcher watcher = new TextWatcher() {

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
			String coin = s.toString();

			if (coin != null && coin.length() != 0) {
				if (coin.startsWith("0")) {
					coin = coin.substring(1);
					coinView.setText(coin+"");
				} else {
					int wcoin = Integer.parseInt(coin);
					if (wcoin > allCommCoin) {
						coinView.setText(allCommCoin+"");
					}
				}
			}
		}
	};

	private String money;
	private String name;
	private String card;
	private String bankName;
	private String bankBranchName;

	private void init() {
		money = coinView.getText().toString();
		name = userNameView.getText().toString();
		card = bankNumView.getText().toString();
		bankName = bankNameView.getText().toString();
		bankBranchName = bankSnameView.getText().toString();

	}

	public void requesPay() {
		init();
		if (name == null || name.length() == 0) {
			ToastUtil.show(m_activity, "姓名不能为空");
			return;
		}
		if(card==null||card.length()==0){
			ToastUtil.show(m_activity, "银行卡号不能为空");
			return;
		}
		if (bankName == null || bankName.length() == 0) {
			ToastUtil.show(m_activity, "开户行不为空");
			return;
		}
		if (bankSnameView == null || bankBranchName.length() == 0) {
			ToastUtil.show(m_activity, "开发行不能为空");
		}

		if (money == null || money.length() == 0) {
			ToastUtil.show(m_activity, "金额不能为空");
			return;
		}
		if (!Tools.checkBankCard(card)) {
			ToastUtil.show(m_activity, "银行卡号不对");
			return;
		}
		if (Integer.parseInt(money) < 100) {
			ToastUtil.show(m_activity, "至少提现100");
			return;
		}
		if (!NetWorkUtil.isNetworkAvailable(m_activity)) {
			ToastUtil.show(m_activity, "网络错误");
			return;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", name);
		map.put("card", card);
		map.put("bankName", bankName);
		map.put("bankBranchName", bankBranchName);
		lastCoin = Integer.parseInt(money);
		map.put("money", lastCoin*RATE);
		XHttp.PostByToken(Constant.USER_APPLY, infoModel, map,
				new CommonCallback<Boolean>() {

					@Override
					public void onCancelled(CancelledException arg0) {

					}

					@Override
					public void onError(Throwable arg0, boolean arg1) {
						Log.v("this", arg0.toString());
						ToastUtil.show(m_activity, "提现失败");
					}

					@Override
					public void onFinished() {

					}

					@Override
					public void onSuccess(Boolean arg0) {
						if (arg0) {
							ToastUtil.show(m_activity, "提交成功,等待验证");
							CommissionActivity.commissionActivity
									.changeCoin(allCommCoin - lastCoin);
							CommBeDreamCoin.beDreamCoin.setAllCommCoin(allCommCoin - lastCoin);
						} else
							ToastUtil.show(m_activity, "提现失败");
					}
				});
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
//		allCommCoin=CommissionActivity.commissionActivity.
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		commTixianFragment=null;
	}
}
