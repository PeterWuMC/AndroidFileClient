package com.wu.androidfileclient.async;

import java.util.ArrayList;

import org.apache.http.HttpException;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.wu.androidfileclient.MainActivity;
import com.wu.androidfileclient.listeners.CancelTaskOnCancelListener;
import com.wu.androidfileclient.models.Credential;
import com.wu.androidfileclient.models.ListItem;
import com.wu.androidfileclient.services.FileLister;
import com.wu.androidfileclient.utils.ProgressDialogHandler;
import com.wu.androidfileclient.utils.Utilities;

public class PerformUpdateListAsyncTask extends AsyncTask<String, Void, ArrayList<ListItem>> {
    private MainActivity context;
	private ProgressDialog progressDialog;
	private FileLister fileLister;
	private Credential credential;
	
	public PerformUpdateListAsyncTask(MainActivity context, Credential credential) {
		super();
		this.context = context;
		this.credential = credential;
		progressDialog = ProgressDialogHandler.createProgressDialog(context, ProgressDialogHandler.RETRIEVING_DATA);
		progressDialog.setOnCancelListener(new CancelTaskOnCancelListener(this));

		progressDialog.show();
    }

	@Override
	protected ArrayList<ListItem> doInBackground(String... params) {
		String key = params[0];
        fileLister = new FileLister(credential);
        
        try {
        	return fileLister.retrieveFilesList(key);
        } catch (HttpException e) {
        	cancel(true);
        }
        return null;
	}

	@Override
	protected void onCancelled() {
		ProgressDialogHandler.dismissProgressDialog(progressDialog);
		Utilities.longToast(context, "Something wrong with your connection...");
	}

	@Override
	protected void onPostExecute(final ArrayList<ListItem> result) {
		ProgressDialogHandler.dismissProgressDialog(progressDialog);
		context.updateList(result);
	}
}
