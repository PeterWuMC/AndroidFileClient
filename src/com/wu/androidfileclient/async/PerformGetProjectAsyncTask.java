package com.wu.androidfileclient.async;

import java.util.ArrayList;

import org.apache.http.HttpException;

import android.content.Context;
import android.os.AsyncTask;

import com.wu.androidfileclient.models.Credential;
import com.wu.androidfileclient.models.FolderItem;
import com.wu.androidfileclient.services.ProjectLister;

public class PerformGetProjectAsyncTask  extends AsyncTask<Void, Void, ArrayList<FolderItem>> {
	private ProjectLister projectLister;
//	private ProgressDialogHandler progressDialog;
	private Credential credential;
	
	public PerformGetProjectAsyncTask(Context context, Credential credential) {
		super();
		this.credential = credential;
//		progressDialog = new ProgressDialogHandler(context);
//		progressDialog.createProgressDialog(ProgressDialogHandler.RETRIEVING_DATA);
//		progressDialog.setOnCancelListener(new CancelTaskOnCancelListener(this));
//
//		progressDialog.show();
    }

	@Override
	protected ArrayList<FolderItem> doInBackground(Void... params) {
		projectLister = new ProjectLister(credential);
        try {
        	return projectLister.retrieveList();
        } catch (HttpException e) {
        	cancel(true);
        }
        return null;
	}

	@Override
	protected void onCancelled() {
//		progressDialog.dismiss();
	}

	@Override
	protected void onPostExecute(final ArrayList<FolderItem> result) {
//		progressDialog.dismiss();
	}
}