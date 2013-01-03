package com.wu.androidfileclient.async;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpException;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.wu.androidfileclient.MainActivity;
import com.wu.androidfileclient.listeners.CancelTaskOnCancelListener;
import com.wu.androidfileclient.models.FileItem;
import com.wu.androidfileclient.services.FileLister;
import com.wu.androidfileclient.utils.Utilities;

public class PerformFileListSearchTask extends AsyncTask<String, Void, ArrayList<FileItem>> {
    private MainActivity context;
	private ProgressDialog progressDialog;
	private FileLister fileLister;
	private HashMap<String, String> credential;
	
	public PerformFileListSearchTask(MainActivity context, HashMap<String, String> credential) {
		super();
		this.context = context;
		this.credential = credential;
		progressDialog = new ProgressDialog(context);
		progressDialog.setTitle("Please wait...");
    	progressDialog.setMessage("Retrieving data...");
    	progressDialog.setCancelable(true);
		progressDialog.setOnCancelListener(new CancelTaskOnCancelListener(this));

		progressDialog.show();
    }

	@Override
	protected ArrayList<FileItem> doInBackground(String... params) {
		String key = params[0];
        fileLister = new FileLister(credential.get("user_name"), credential.get("device_code"));
        
        try {
        	return fileLister.retrieveFilesList(key);
        } catch (HttpException e) {
        	cancel(true);
        }
        return null;
	}

	@Override
	protected void onCancelled() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
		Utilities.longToast(context, "Something wrong with your connection...");
	}

	@Override
	protected void onPostExecute(final ArrayList<FileItem> result) {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
		context.updateFilesList(result);
	}
}
