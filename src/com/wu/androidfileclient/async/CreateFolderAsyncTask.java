package com.wu.androidfileclient.async;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.wu.androidfileclient.AllActivities;
import com.wu.androidfileclient.R;
import com.wu.androidfileclient.models.FolderItem;
import com.wu.androidfileclient.utils.HttpHandler;
import com.wu.androidfileclient.utils.ProgressDialogHandler;
import com.wu.androidfileclient.utils.Utilities;

public class CreateFolderAsyncTask extends CustomAsyncTask<FolderItem, Void, Boolean> {

	public CreateFolderAsyncTask (AllActivities activity, long reference, String url) {
		super(activity, reference, url, ProgressDialogHandler.CREATING_FOLDER, AllActivities.CREATE_FOLDER);
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

}
