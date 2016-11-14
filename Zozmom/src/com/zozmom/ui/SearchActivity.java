//package com.zozmom.ui;
//
//import com.chasedream.zhumeng.R;
//import com.zozmom.util.ToastUtil;
//
//import android.os.Bundle;
//import android.view.KeyEvent;
//import android.view.View;
//import android.view.inputmethod.EditorInfo;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.TextView.OnEditorActionListener;
//
//public class SearchActivity extends BaseTaskActivity<Object> {
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.search_activity);
//		EditText editText = (EditText) findViewById(R.id.search_edit);
//		editText.setOnEditorActionListener(new OnEditorActionListener() {
//			public boolean onEditorAction(TextView v, int actionId,
//					KeyEvent event) {
//				if (actionId == EditorInfo.IME_ACTION_SEARCH
//						|| (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
//					if(event.getAction()==KeyEvent.ACTION_UP){
//						ToastUtil.show(getApplication(), "哈哈哈");
//					}
//					return true;
//				}
//				return false;
//			}
//		});
//
//	}
//
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.status_bar_Exit:
//			finish();
//			break;
//
//		default:
//			break;
//		}
//	}
//}
