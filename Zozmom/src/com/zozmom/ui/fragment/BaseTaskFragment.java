package com.zozmom.ui.fragment;


import com.zozmom.pic.PhotoLoader;

import android.widget.ImageView;

public abstract class BaseTaskFragment extends BaseFragment {
	PhotoLoader m_loader;

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	public void loadImage(String url, ImageView imageView) {
		if (m_loader == null) {
			m_loader = new PhotoLoader(m_activity);
		}
		m_loader.loadPhoto(url, imageView);
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}