package com.wu.androidfileclient.async;

import org.apache.http.HttpStatus;

import android.os.AsyncTask;

import com.wu.androidfileclient.AllActivities;
import com.wu.androidfileclient.R;
import com.wu.androidfileclient.utils.HttpHandler;
import com.wu.androidfileclient.utils.ProgressDialogHandler;
import com.wu.androidfileclient.utils.Utilities;

public class DeleteItemAsyncTask extends AsyncTask<Void, Void, Boolean> {
    private AllActivities activity;
    private long reference;
    private String url;

	private ProgressDialogHandler progressDialog;
	
	public DeleteItemAsyncTask(AllActivities activity, long reference, String url) {
		super();

		this.activity  = activity;
		this.reference = reference;
		this.url       = url;

		progressDialog = new ProgressDialogHandler(activity);
		progressDialog.createProgressDialog(ProgressDialogHandler.DELETING_FILE);
    }

	@Override
    protected void onPreExecute() {
        super.onPreExecute();
    	progressDialog.show();
    }

	@Override
	protected Boolean doInBackground(Void... params) {
		HttpHandler httpHandler = new HttpHandler(url);
		int statusCode = httpHandler.startDELETEConnection();

		if (statusCode != HttpStatus.SC_OK) {
			Utilities.longToast(activity, R.string.connection_error_toast);
			return false;
		}

		return true;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		progressDialog.dismiss();
		activity.afterAsyncTaskFinish(AllActivities.DELETE_ITEM_COMPLETED, reference, result);
	}
}
