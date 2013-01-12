package com.wu.androidfileclient.async;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;

import com.wu.androidfileclient.MainActivity;
import com.wu.androidfileclient.models.FolderItem;
import com.wu.androidfileclient.utils.HttpHandler;
import com.wu.androidfileclient.utils.ProgressDialogHandler;

public class PerformCreateFolderAsyncTask extends AsyncTask<FolderItem, Void, Boolean> {
    private MainActivity context;
	private ProgressDialogHandler progressDialog;
	private String url;
	
	public PerformCreateFolderAsyncTask(MainActivity context, String url) {
		super();
		this.context   = context;
		this.url       = url;
		progressDialog = new ProgressDialogHandler(context);
		progressDialog.createProgressDialog(ProgressDialogHandler.CREATING_FOLDER);

		progressDialog.show();
    }

	@Override
	protected Boolean doInBackground(FolderItem... params) {
		FolderItem folderItem = params[0];
		List<NameValuePair> parameters = new ArrayList<NameValuePair>(4);

		parameters.add(new BasicNameValuePair("name", folderItem.name));

		HttpHandler httpHandler = new HttpHandler(url);
		int statusCode = httpHandler.startPOSTConnection(parameters);

		if (statusCode != HttpStatus.SC_OK) return false;
		
		return true;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		progressDialog.dismiss();
		context.refreshList();
	}
}
