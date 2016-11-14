package com.zozmom.manager;

import com.zozmom.model.ShareUrlModel;

public class ShareUrlManager {
	static ShareUrlManager shareUrlManager;
	ShareUrlModel sharemodel;

	public static ShareUrlManager getInstance() {
		if (shareUrlManager == null) {
			shareUrlManager = new ShareUrlManager();
		}
		return shareUrlManager;
	}

	public ShareUrlModel getSharemodel() {
		return sharemodel;
	}

	public void setSharemodel(ShareUrlModel sharemodel) {
		this.sharemodel = sharemodel;
	}
	
	public void upShareModel(ShareUrlModel sharemodel){
		this.sharemodel = sharemodel;
	}
}
