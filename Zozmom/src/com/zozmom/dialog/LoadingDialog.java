package com.zozmom.dialog;


import com.chasedream.zhumeng.R;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LoadingDialog extends Dialog  {
	private Context context = null;
	    private static LoadingDialog customProgressDialog = null;
	    public LoadingDialog(Context context){
	        super(context);
	        this.context = context;
	    }
	
	    public LoadingDialog(Context context, int theme) {
	        super(context, theme);
	    }
	
	     
	
	    public static LoadingDialog createDialog(Context context){
	        customProgressDialog = new LoadingDialog(context,R.style.alertdialogstyle);
	        customProgressDialog.setContentView(R.layout.base_loading);
	        customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
	        return customProgressDialog;
	    }
	

	    public static LoadingDialog createDialog(Context context,String msg){
	        customProgressDialog = new LoadingDialog(context,R.style.alertdialogstyle);
	        customProgressDialog.setContentView(R.layout.base_loading);
	        customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
	        ((TextView)customProgressDialog.findViewById(R.id.txtLoading)).setText(msg);
	        return customProgressDialog;
	    }
	

	    public static LoadingDialog createDialog(Context context,String msg,boolean isShowIco){
	        customProgressDialog = new LoadingDialog(context,R.style.alertdialogstyle);
	        customProgressDialog.setContentView(R.layout.base_loading);
	        customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
	        ((TextView)customProgressDialog.findViewById(R.id.txtLoading)).setText(msg);
	        ProgressBar progressBar = (ProgressBar)customProgressDialog.findViewById(R.id.progressBar1);
	        if(isShowIco){
	        	progressBar.setVisibility(View.VISIBLE);
	        }else{
	        	progressBar.setVisibility(View.GONE);
	        }
	        return customProgressDialog;
	    }
	
	    public void onWindowFocusChanged(boolean hasFocus){
	        if (customProgressDialog == null){
	            return;
	        }
	    }
	
	  
	
	    /**
	     *
	
	     * [Summary]
	
	     *       setTitile ����
	
	     * @param strTitle
	
	     * @return
	
	     *
	
	     */
	
	    public LoadingDialog setTitile(String strTitle){
	        return customProgressDialog;
	    }

	    /**
	     *
	     * [Summary]
	     *       setMessage ��ʾ����
	     * @param strMessage
	     * @return
	     *
	     */
	    public LoadingDialog setMessage(String strMessage){
	        TextView tvMsg = (TextView)customProgressDialog.findViewById(R.id.txtLoading);
	        if (tvMsg != null){
	            tvMsg.setText(strMessage);
	        }
	        return customProgressDialog;
	    }

}
