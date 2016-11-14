package com.zozmom.ui.fragment;

import com.chasedream.zhumeng.R;
import com.zozmom.pic.PhotoLoader;
import com.zozmom.ui.BaseActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;

public class ImageDetailFragment extends Fragment {
	private String mImageUrl;
	private ImageView mImageView;
	private ProgressBar progressBar;

	public static ImageDetailFragment newInstance(String imageUrl) {
		final ImageDetailFragment f = new ImageDetailFragment();

		final Bundle args = new Bundle();
		args.putString("url", imageUrl);
		f.setArguments(args);

		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mImageUrl = getArguments() != null ? getArguments().getString("url")
				: null;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.image_detail_fragment, container,
				false);
		mImageView = (ImageView) v.findViewById(R.id.image);
		@SuppressWarnings("rawtypes")
		PhotoLoader syncImageLoader = new PhotoLoader(
				(BaseActivity) getActivity());
		mImageView.setScaleType(ScaleType.CENTER_INSIDE);
		syncImageLoader.loadPhoto(mImageUrl, mImageView);
		progressBar = (ProgressBar) v.findViewById(R.id.loading);
		return v;
	}

}
