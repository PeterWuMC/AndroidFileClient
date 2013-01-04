package com.wu.androidfileclient.async;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.wu.androidfileclient.MainActivity;
import com.wu.androidfileclient.models.Credential;
import com.wu.androidfileclient.models.BaseListItem;
import com.wu.androidfileclient.services.FileRemover;
import com.wu.androidfileclient.utils.ProgressDialogHandler;

public class PerformDeleteFileAsyncTask extends AsyncTask<BaseListItem, Void, Boolean> {
    private MainActivity context;
	private ProgressDialog progressDialog;
	private Credential credential;
	
	public PerformDeleteFileAsyncTask(MainActivity context, Credential credential) {
		super();
		this.context    = context;
		this.credential = credential;
		progressDialog  = ProgressDialogHandler.createProgressDialog(this.context, ProgressDialogHandler.DELETING_FILE);

		progressDialog.show();
    }

	@Override
	protected Boolean doInBackground(BaseListItem... params) {
		BaseListItem listItem = params[0];
        FileRemover fileRemover = new FileRemover(credential);

    	return fileRemover.delete(listItem.key);
	}

	@Override
	protected void onPostExecute(Boolean result) {
		ProgressDialogHandler.dismissProgressDialog(progressDialog);
		context.refreshList();
	}

}
