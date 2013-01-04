package com.wu.androidfileclient.utils;

import android.app.ProgressDialog;
import android.content.Context;

public final class ProgressDialogHandler {

    public static final int RETRIEVING_DATA     = 1;
    public static final int LOGGING_IN          = 2;
    public static final int CHECKING_CREDENTIAL = 3;
    public static final int DOWNLOADING_FILE    = 4;

    public static final ProgressDialog createProgressDialog(Context context, int dialogType) {
    	ProgressDialog progressDialog = new ProgressDialog(context);
    	progressDialog.setTitle("Please wait...");

    	switch (dialogType) {
	    	case RETRIEVING_DATA:
	        	progressDialog.setMessage("Retrieving data...");
	        	progressDialog.setCancelable(true);
	    		break;
	    	case LOGGING_IN:
	        	progressDialog.setMessage("Logging in...");
	        	progressDialog.setCancelable(true);
	    		break;
	    	case CHECKING_CREDENTIAL:
	        	progressDialog.setMessage("Checking Credential...");
	        	progressDialog.setCancelable(true);
	    		break;
	    	case DOWNLOADING_FILE:
	        	progressDialog.setMessage("Downloading file...");
	        	progressDialog.setIndeterminate(false);
	        	progressDialog.setMax(100);
	        	progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	        	progressDialog.setCancelable(true);
	    		break;
	    	default:
	    		return null;
    	}
    	return progressDialog;
    }
    
    public static final void dismissProgressDialog(ProgressDialog progressDialog) {
    	if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
    }
}
