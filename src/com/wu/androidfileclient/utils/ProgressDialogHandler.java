package com.wu.androidfileclient.utils;

import android.app.ProgressDialog;
import android.content.Context;

public final class ProgressDialogHandler extends ProgressDialog {

    public static final int RETRIEVING_DATA     = 1;
    public static final int LOGGING_IN          = 2;
    public static final int CHECKING_CREDENTIAL = 3;
    public static final int DOWNLOADING_FILE    = 4;
    public static final int DELETING_FILE       = 5;
    
    public ProgressDialogHandler(Context context) {
    	super(context);
    }

    public void createProgressDialog(int dialogType) {
    	setTitle("Please wait...");

    	switch (dialogType) {
    	case RETRIEVING_DATA:
        	setMessage("Retrieving list...");
        	setCancelable(true);
    		break;
    	case LOGGING_IN:
        	setMessage("Logging in...");
        	setCancelable(true);
    		break;
    	case CHECKING_CREDENTIAL:
        	setMessage("Checking Credential...");
        	setCancelable(true);
    		break;
    	case DELETING_FILE:
        	setMessage("Deleting...");
        	setCancelable(false);
        	break;
    	case DOWNLOADING_FILE:
        	setMessage("Downloading file...");
        	setIndeterminate(false);
//        	setMax(100);
        	setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        	setCancelable(true);
    		break;
    	}
    }
}
