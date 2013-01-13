package com.wu.androidfileclient.async;

import java.util.ArrayList;

import org.apache.http.HttpException;

import android.os.AsyncTask;

import com.wu.androidfileclient.MainActivity;
import com.wu.androidfileclient.listeners.CancelTaskOnCancelListener;
import com.wu.androidfileclient.models.BaseListItem;
import com.wu.androidfileclient.models.Credential;
import com.wu.androidfileclient.services.FolderLister;
import com.wu.androidfileclient.utils.ProgressDialogHandler;
import com.wu.androidfileclient.utils.Utilities;

public class PerformUpdateListAsyncTask extends AsyncTask<BaseListItem, Void, ArrayList<BaseListItem>> {
    private MainActivity context;
	private ProgressDialogHandler progressDialog;
	private FolderLister fileLister;
	private Credential credential;
	
	public PerformUpdateListAsyncTask(MainActivity context, Credential credential) {
		super();
		this.context    = context;
		this.credential = credential;
		progressDialog = new ProgressDialogHandler(context);
		progressDialog.createProgressDialog(ProgressDialogHandler.RETRIEVING_DATA);
		progressDialog.setOnCancelListener(new CancelTaskOnCancelListener(this));

		progressDialog.show();
    }

	@Override
	protected ArrayList<BaseListItem> doInBackground(BaseListItem... params) {
		BaseListItem baseListItem = params[0];
        fileLister = new FolderLister(credential, "cHVibGlj");
        
        try {
        	return fileLister.retrieveList(baseListItem);
        } catch (HttpException e) {
        	cancel(true);
        }
        return null;
	}

	@Override
	protected void onCancelled() {
		progressDialog.dismiss();
		Utilities.longToast(context, "Something wrong with your connection...");
	}

	@Override
	protected void onPostExecute(final ArrayList<BaseListItem> result) {
		progressDialog.dismiss();
		context.updateList(result);
	}
}
