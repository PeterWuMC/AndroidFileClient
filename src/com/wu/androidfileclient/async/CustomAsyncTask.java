package com.wu.androidfileclient.async;

import android.os.AsyncTask;

import com.wu.androidfileclient.AllActivities;
import com.wu.androidfileclient.R;
import com.wu.androidfileclient.listeners.CancelTaskOnCancelListener;
import com.wu.androidfileclient.utils.ProgressDialogHandler;
import com.wu.androidfileclient.utils.Utilities;

public abstract class CustomAsyncTask<A,B,C> extends AsyncTask<A, B, C> {

	protected AllActivities activity;
	protected long reference;
	protected String url;
	protected int taskId;

	protected ProgressDialogHandler progressDialog;
	
	protected abstract C doInBackground(A... params);
	
	public CustomAsyncTask(AllActivities activity, long reference, String url, int progressDialogId, int taskId) {
		super();

		this.activity   = activity;
		this.url        = url;
		this.reference  = reference;
		this.taskId     = taskId;

		progressDialog = new ProgressDialogHandler(activity);
		progressDialog.createProgressDialog(progressDialogId);
		progressDialog.setOnCancelListener(new CancelTaskOnCancelListener(this));
//		progressDialog.setProgressNumberFormat("%1d / %2d bytes");
	}

	@Override
	protected void onCancelled(C result) {
		progressDialog.dismiss();
		Utilities.longToast(activity, R.string.connection_error_toast);
		activity.onTaskCancelled(taskId, reference, result);
	}

	@Override
    protected void onPreExecute() {
        super.onPreExecute();
    	progressDialog.show();
    }

	@Override
	protected void onPostExecute(C result) {
		progressDialog.dismiss();
		activity.onTaskCompleted(taskId, reference, result);
	}
}
