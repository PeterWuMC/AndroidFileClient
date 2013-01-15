package com.wu.androidfileclient;

import android.content.Context;
import android.content.res.Resources;

public interface AllActivities {

	public static final int REGISTER_DEVICE  = 1;
	public static final int CHECK_CREDENTIAL = 2;
	public static final int CREATE_FOLDER    = 3;
	public static final int DELETE_ITEM      = 4;
	public static final int DOWNLOAD_FILE    = 5;
	public static final int UPDATE_LIST      = 6;
	public static final int UPLOAD_FILE      = 7;
	public static final int GET_PROJECT      = 8;
	public static final int PROJECT_SWITCH   = 9;

	public abstract void onTaskCompleted(int task, long reference, Object result);
	public abstract void onTaskCancelled(int task, long reference, Object result);
	public abstract Resources getResources();
//	public abstract Context getApplicationContext();
	public abstract Context getContext();
	public abstract void runOnUiThread(Runnable runnable);

}
