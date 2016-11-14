package com.zozmom.ui.user;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chasedream.zhumeng.R;
import com.zozmom.constants.Constant;
import com.zozmom.model.UserInfoModel;
import com.zozmom.ui.BaseTaskActivity;
import com.zozmom.ui.fragment.MeFragment;
import com.zozmom.util.NetWorkUtil;
import com.zozmom.util.ToastUtil;
import com.zozmom.util.Util;

@ContentView(R.layout.alter_usename_activity)
public class AlterNameActivity extends BaseTaskActivity<String> {
	UserInfoModel model;
	@ViewInject(R.id.alter_name_editview)
	EditText nameview;
	@ViewInject(R.id.status_bar_title)
	TextView titleView;
	public static int resultCode = 8;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		x.view().inject(this);
		model = getLogginUserinfo();
		nameview.setText(model.getUserName());
//		nameview.addTextChangedListener(watcher);
		titleView.setText(getRString(R.string.alter_username));
		nameview.addTextChangedListener(new MaxLengthWatcher(14, nameview, this));
	}
	
	public static String stringFilter(String str) throws PatternSyntaxException {
		// 只允许字母、数字和汉字
		String regEx = "[^a-zA-Z0-9\u4E00-\u9FA5]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}
	TextWatcher watcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			String editable = nameview.getText().toString();
			String str = stringFilter(editable.toString());
			if (!editable.equals(str)) {
				nameview.setText(str);
				// 设置新的光标所在位置
				nameview.setSelection(str.length());
			}
		}
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
		}
	};
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
			case R.id.status_bar_Exit :
				finish();
				break;
			case R.id.alter_saveBtn :
				toDo();
				break;
			case R.id.alter_delete_btn :
				nameview.setText("");
				break;
		}
	}

	String name;

	public void toDo() {
		name = nameview.getText().toString();
		if (!NetWorkUtil.isNetworkAvailable(this)) {
			ToastUtil.show(this, getRString(R.string.network_hint));
			return;
		}
		if (name == null) {
			ToastUtil.show(this, "名字不能为空");
			return;
		}
		if (Util.StringName(name)) {
			ToastUtil.show(this, "不能有特殊符号");
			return;
		}
		if (name.equals(model.getUserName())) {
			ToastUtil.show(this, "没有改变");
			return;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", model.getUserId());
		map.put("username", name);
		requstPostByToken(Constant.UPDATA_USER, model, map);
	}

	@Override
	public void onSuccess(String arg0) {
		super.onSuccess(arg0);
		model.setUserName(name);
		ToastUtil.show(this, "修改成功");
		updataUserinfo(model);
		setResult(resultCode);
		if(MeFragment.meFragment!=null){
			MeFragment.meFragment.changgeView(model, true);
		}
		finish();
	}

	AlertDialog alertDialog;

	public void showSucessDialog() {
		AlertDialog.Builder builder = new Builder(this);
		alertDialog = builder.create();
		alertDialog.show();
		Window window = alertDialog.getWindow();
		window.setContentView(R.layout.alter_name_susucess);
		Button button = (Button) window.findViewById(R.id.alter_name_sucessbtn);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlterNameActivity.this.finish();
			}
		});
		alertDialog.setCanceledOnTouchOutside(true);
	}

}
