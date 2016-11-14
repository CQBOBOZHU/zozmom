package com.zozmom.net;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public abstract class OkCallback implements Callback{

	
	@Override
	public void onFailure(Call arg0, IOException arg1) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void onResponse(Call arg0, Response arg1) throws IOException {
		// TODO Auto-generated method stub
		
	}
}
