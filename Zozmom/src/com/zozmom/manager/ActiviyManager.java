package com.zozmom.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.zozmom.ui.BaseActivity;
import com.zozmom.util.LogUtil;

import android.app.Activity;

public class ActiviyManager {
	static ActiviyManager manager;
	Stack<Activity> mtask;

	public static ActiviyManager instance() {
		if (manager == null) {
			manager = new ActiviyManager();
		}
		return manager;
	}
	
	public Activity getTopActivity(){
		if(mtask!=null){
			return mtask.firstElement();
		}else
			return null;
	}
	
	public void addActivity(Activity activity) {
		if (mtask == null) {
			mtask = new Stack<Activity>();
		}
		mtask.add(activity);
	}

	public void removeActivity(Activity activity) {
		if (mtask != null && mtask.size() > 0) {
			LogUtil.v( mtask.size() +"activity");
			if (activity != null) {
				activity.finish();
				mtask.remove(activity);
				activity = null;
			}
		}
	}

	public void removeAll() {
		if (mtask != null) {
			while (mtask.size() > 0) {
				Activity activity = mtask.lastElement();
				if (activity == null)
					break;
				removeActivity(activity);
			}

		}
	}
}
