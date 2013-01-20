package com.wu.androidfileclient.listeners;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;

import com.wu.androidfileclient.async.AsyncTask;

public class CancelTaskOnCancelListener implements OnCancelListener {
	private AsyncTask<?, ?, ?> task;

	public CancelTaskOnCancelListener(AsyncTask<?, ?, ?> task) {
		this.task = task;
	}
	@Override
	public void onCancel(DialogInterface dialog) {
		if (task!=null) {
    		task.cancel(true);
    	}
	}
}
