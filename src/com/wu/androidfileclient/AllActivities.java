package com.wu.androidfileclient;

import android.content.Context;
import android.content.res.Resources;

public interface AllActivities {

	public static final int REGISTER_DEVICE_COMPLETED  = 1;
	public static final int CHECK_CREDENTIAL_COMPLETED = 2;
	public static final int CREATE_FOLDER_COMPLETED    = 3;
	public static final int DELETE_ITEM_COMPLETED      = 4;
	public static final int DOWNLOAD_FILE_COMPLETED    = 5;
	public static final int UPDATE_LIST_COMPLETED  	   = 6;
	public static final int UPLOAD_FILE_COMPLETED  	   = 7;
	public static final int GET_PROJECT_COMPLETED = 8;

	public abstract void afterAsyncTaskFinish(int task, long reference, Object result);
	public abstract Resources getResources();
//	public abstract Context getApplicationContext();
	public abstract Context getContext();
	public abstract void runOnUiThread(Runnable runnable);

}
