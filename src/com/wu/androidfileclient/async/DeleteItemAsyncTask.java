package com.wu.androidfileclient.async;

import org.apache.http.HttpStatus;

import com.wu.androidfileclient.AllActivities;
import com.wu.androidfileclient.R;
import com.wu.androidfileclient.utils.HttpHandler;
import com.wu.androidfileclient.utils.ProgressDialogHandler;
import com.wu.androidfileclient.utils.Utilities;

public class DeleteItemAsyncTask extends CustomAsyncTask<Void, Void, Boolean> {

	public DeleteItemAsyncTask(AllActivities activity, long reference, String url) {
		super(activity, reference, url, ProgressDialogHandler.DELETING_FILE);
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

}
