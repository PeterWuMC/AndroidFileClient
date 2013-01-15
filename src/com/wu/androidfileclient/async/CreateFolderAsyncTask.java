package com.wu.androidfileclient.async;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;

import com.wu.androidfileclient.AllActivities;
import com.wu.androidfileclient.R;
import com.wu.androidfileclient.listeners.CancelTaskOnCancelListener;
import com.wu.androidfileclient.models.FolderItem;
import com.wu.androidfileclient.utils.HttpHandler;
import com.wu.androidfileclient.utils.ProgressDialogHandler;
import com.wu.androidfileclient.utils.Utilities;

public class CreateFolderAsyncTask extends AsyncTask<FolderItem, Void, Boolean> {
    private AllActivities activity;
    private long reference;
	private String url;

	private ProgressDialogHandler progressDialog;
	
	public CreateFolderAsyncTask (AllActivities activity, long reference, String url) {
		super();

		this.activity  = activity;
		this.url       = url;
		this.reference = reference;

		progressDialog = new ProgressDialogHandler(activity);
		progressDialog.createProgressDialog(ProgressDialogHandler.CREATING_FOLDER);
		progressDialog.setOnCancelListener(new CancelTaskOnCancelListener(this));
    }

	@Override
    protected void onPreExecute() {
        super.onPreExecute();
    	progressDialog.show();
    }

	@Override
	protected Boolean doInBackground(FolderItem... params) {
		FolderItem folderItem = params[0];
		List<NameValuePair> parameters = new ArrayList<NameValuePair>(4);

		parameters.add(new BasicNameValuePair("name", folderItem.name));

		HttpHandler httpHandler = new HttpHandler(url);
		int statusCode = httpHandler.startPOSTConnection(parameters);

		if (statusCode != HttpStatus.SC_OK) {
			Utilities.longToast(activity, R.string.connection_error_toast);
			return false;
		}
		
		return true;
	}

	@Override
	protected void onCancelled() {
		progressDialog.dismiss();
		Utilities.longToast(activity, R.string.connection_error_toast);
	}

	@Override
	protected void onPostExecute(Boolean result) {
		progressDialog.dismiss();
		activity.afterAsyncTaskFinish(AllActivities.CREATE_FOLDER_COMPLETED, reference, result);
	}
}
