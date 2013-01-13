package com.wu.androidfileclient.async;

import android.os.AsyncTask;

import com.wu.androidfileclient.MainActivity;
import com.wu.androidfileclient.models.BaseListItem;
import com.wu.androidfileclient.models.Credential;
import com.wu.androidfileclient.services.FileRemover;
import com.wu.androidfileclient.utils.ProgressDialogHandler;

public class PerformDeleteFileAsyncTask extends AsyncTask<BaseListItem, Void, Boolean> {
    private MainActivity context;
	private ProgressDialogHandler progressDialog;
	private Credential credential;
	
	public PerformDeleteFileAsyncTask(MainActivity context, Credential credential) {
		super();
		this.context    = context;
		this.credential = credential;
		progressDialog  = new ProgressDialogHandler(context);
		progressDialog.createProgressDialog(ProgressDialogHandler.DELETING_FILE);

		progressDialog.show();
    }

	@Override
	protected Boolean doInBackground(BaseListItem... params) {
		BaseListItem listItem = params[0];
        FileRemover fileRemover = new FileRemover(credential, "cHVibGlj");

    	return fileRemover.delete(listItem.key);
	}

	@Override
	protected void onPostExecute(Boolean result) {
		progressDialog.dismiss();
		context.refreshList();
	}
}
