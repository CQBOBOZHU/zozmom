package com.zozmom.ui.pay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chasedream.zhumeng.R;
import com.iapppay.interfaces.callback.IPayResultCallback;
import com.iapppay.sdk.main.IAppPay;
import com.zozmom.constants.Constant;
import com.zozmom.model.UserInfoModel;
import com.zozmom.ui.BaseTaskActivity;
import com.zozmom.util.NetWorkUtil;
import com.zozmom.util.ToastUtil;

/**
 * 充值
 * 
 * @author Administrator
 * 
 */
@ContentView(R.layout.recharge_activity)
public class RechargeActivity extends BaseTaskActivity<String> {
	@ViewInject(R.id.status_bar_title)
	TextView titleView;
	@ViewInject(R.id.recharge_onebtn)
	Button rechargeoneBtn;
	@ViewInject(R.id.recharge_twobtn)
	Button rechargetwoBtn;
	@ViewInject(R.id.recharge_threebtn)
	Button rechargethreeBtn;
	@ViewInject(R.id.recharge_fourbtn)
	Button rechargefourBtn;
	@ViewInject(R.id.recharge_fivebtn)
	Button rechargefiveBtn;
	@ViewInject(R.id.recharge_sixbtn)
	Button rechargesxiBtn;
	List<Button> buttons;
	Button checkBtn;
	int rechargenumber = 0;
	@ViewInject(R.id.pay_radbtn)
	Button pingBtn;
	boolean ischeckping = true;// 判断ping++是否选中
	UserInfoModel infoModel;
	private int lastCoin;
	@ViewInject(R.id.input_menoyview)
	EditText inputView;
	String activityId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		x.view().inject(this);
		titleView.setText(getRString(R.string.recharge_title));
		inputView.clearFocus();
		activityId=getIntent().getStringExtra("activityId");
		buttons = new ArrayList<Button>();
		infoModel = getLogginUserinfo();
		checkBtn = rechargeoneBtn;
		rechargenumber = 10;
		buttons.add(rechargeoneBtn);
		buttons.add(rechargetwoBtn);
		buttons.add(rechargethreeBtn);
		buttons.add(rechargefourBtn);
		buttons.add(rechargefiveBtn);
		buttons.add(rechargesxiBtn);
		inputView.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				changView(s);
			}
		});
	}
	
	@SuppressLint("NewApi")
	public void changView(Editable s){
		if (checkBtn != null) {
			checkBtn.setBackgroundResource(R.drawable.recharge_btn_bg);
			checkBtn.setTextColor(getRColor(R.color.good_message_textcolors));
			checkBtn=null;
		}
		String input=s.toString();
		if(input.startsWith("0")){
			input=input.substring(1);
			inputView.setText(input);
		}
	}
	@Override
	public void outLoactionDo() {
		// TODO Auto-generated method stub
		super.outLoactionDo();
		if (inputView.hasFocus()) {
			inputView.clearFocus();
		}
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
			case R.id.status_bar_Exit :
				finish();
				break;
			case R.id.recharge_onebtn :
				rechargenumber = 10;
				changBtn(0);
				break;
			case R.id.recharge_twobtn :
				rechargenumber = 50;
				changBtn(1);
				break;
			case R.id.recharge_threebtn :
				rechargenumber = 100;
				changBtn(2);
				break;
			case R.id.recharge_fourbtn :
				rechargenumber = 200;
				changBtn(3);
				break;
			case R.id.recharge_fivebtn :
				rechargenumber = 500;
				changBtn(4);
				break;
			case R.id.recharge_sixbtn :
				rechargenumber = 1000;
				changBtn(5);
				break;
			case R.id.recharge_todobtn :
				if (!ischeckping) {
					ToastUtil.show(this, "请选择支付方式");
					return;
				}
				instancePay();
				// Intent intent = new Intent(this,
				// RechargesucessActivity.class);
				// startActivity(intent);
				break;
			case R.id.pay_radbtn :
				pingBtnCheck();
				break;
			default :
				break;
		}
	}

	public void instancePay() {
		if(checkBtn==null){
			String str=inputView.getText().toString();
			if(str==null||str.trim().length()==0){
				ToastUtil.show(this, "请输入想要充值的额度");
			}else{
				lastCoin=Integer.parseInt(str);
			}
		}else{
			lastCoin = rechargenumber;
		}
		if (!NetWorkUtil.isNetworkAvailable(this)) {
			ToastUtil.show(this, "网络错误");
			return;
		}
		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("channel", "wx");
		map.put("price", lastCoin);
		if(activityId!=null){
			Log.v("this","activityId:"+ activityId+"");
			map.put("activityId", activityId);
		}
		requstPostByToken(Constant.RECHARGE_DREAM_COIN, infoModel, map);
	}

	public void onSuccess(String transid) {
		Log.v("this", "arg0" + transid);
//		Pingpp.createPayment(this, arg0);
//		String transid;
//		try {
//			JSONObject jsonObject=new JSONObject(arg0);
//			transid = jsonObject.getString("transid");
			if(transid.contains("\"")){
				transid=transid.replace("\"", "");
			}
			IAppPay.startPay(RechargeActivity.this, "transid="+transid+"&appid=3005839314",callback);
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	IPayResultCallback  callback=new IPayResultCallback() {
		
		@Override
		public void onPayResult(int arg0, String arg1, String arg2) {
			// TODO Auto-generated method stub
			if(arg0==IAppPay.PAY_SUCCESS){
					Intent intent = new Intent(RechargeActivity.this,
							RechargesucessActivity.class);
					intent.putExtra("result", "success");
					intent.putExtra("coin", lastCoin);
					startActivity(intent);
			}else if(arg0==IAppPay.PAY_ERROR){
				Intent intent = new Intent(RechargeActivity.this,
						RechargesucessActivity.class);
				intent.putExtra("result", "false");
				startActivity(intent);
			}
		}
	};
	
	public void onError(Throwable arg0, boolean arg1) {
		super.onError(arg0, arg1);
	}

	// Pingpp.createPayment(m_activity, arg0);
	Drawable checkdraw;
	Drawable notcheckdraw;

	/**
	 * 处理ping++是否被选择
	 */
	public void pingBtnCheck() {
		if (checkdraw == null) {
			checkdraw = getRDrawble(R.drawable.ricle_yes);
			checkdraw.setBounds(0, 0, checkdraw.getMinimumWidth(),
					checkdraw.getMinimumHeight());
		}
		if (notcheckdraw == null) {
			notcheckdraw = getRDrawble(R.drawable.ricle_no);
			notcheckdraw.setBounds(0, 0, notcheckdraw.getMinimumWidth(),
					notcheckdraw.getMinimumHeight());
		}
		if (ischeckping) {
			pingBtn.setCompoundDrawables(notcheckdraw, null, null, null);
			ischeckping = false;
		} else {
			pingBtn.setCompoundDrawables(checkdraw, null, null, null);
			ischeckping = true;
		}
	}

	@SuppressLint("NewApi")
	public void changBtn(int position) {
		inputView.setText("");
		if (checkBtn != null) {
			checkBtn.setBackgroundResource(R.drawable.recharge_btn_bg);
			checkBtn.setTextColor(getRColor(R.color.good_message_textcolors));
		}
		Button button = buttons.get(position);
		button.setBackgroundResource(R.drawable.recharge_check_btn_selected);
		button.setTextColor(getRColor(R.color.white));
		checkBtn = button;
	}

	/**
	 * onActivityResult 获得支付结果，如果支付成功，服务器会收到ping++ 服务器发送的异步通知。 最终支付成功根据异步通知为准
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		// 支付页面返回处理
//		if (requestCode == Pingpp.REQUEST_CODE_PAYMENT) {
//			if (resultCode == Activity.RESULT_OK) {
//				String result = data.getExtras().getString("pay_result");
//				Log.v(TAG, result);
//				Intent intent = new Intent(RechargeActivity.this,
//						RechargesucessActivity.class);
//				if(result.equals("cancel")){
//					ToastUtil.show(m_activity, "支付返回");
//				}else{
//					intent.putExtra("coin", lastCoin);
//					intent.putExtra("result", result.toString());
//					startActivity(intent);
//				}
//			}
//		}
	}
}
