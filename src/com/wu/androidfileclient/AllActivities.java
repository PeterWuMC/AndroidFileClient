package com.wu.androidfileclient;

import android.app.Activity;

public abstract class AllActivities extends Activity {

	public static final int FINISHED_REGISTER_DEVICE  = 1;
	public static final int FINISHED_CHECK_CREDENTIAL = 2;

	public abstract void afterAsyncTaskFinish(int task, Object object);

	public void afterAsyncTaskFinish(int task) {
		afterAsyncTaskFinish(task, null);
	}


}
