package com.wu.androidfileclient.async;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpException;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.wu.androidfileclient.MainActivity;
import com.wu.androidfileclient.listeners.CancelTaskOnCancelListener;
import com.wu.androidfileclient.models.FileItem;
import com.wu.androidfileclient.services.FileLister;
import com.wu.androidfileclient.ui.FileItemsListAdapter;
import com.wu.androidfileclient.utils.Utilities;

public class PerformFileListSearchTask extends AsyncTask<String, Void, ArrayList<FileItem>> {
    private Context context;
	ProgressDialog progressDialog;
	private FileLister fileLister;
	private ArrayList<FileItem> filesList = new ArrayList<FileItem>();
	private FileItem goBack;
	private FileItemsListAdapter filesAdapter;
	private String currentKey;
	
	public PerformFileListSearchTask(Context context, ArrayList<FileItem> fileList, FileItemsListAdapter filesAdapter, FileItem goBack, String currentKey) {
		super();
		this.context = context;
		this.filesList = fileList;
		this.filesAdapter = filesAdapter;
		this.goBack = goBack;
		this.currentKey = currentKey;
		progressDialog = new ProgressDialog(context);
		progressDialog.setTitle("Please wait...");
    	progressDialog.setMessage("Retrieving data...");
    	progressDialog.setCancelable(true);
		progressDialog.show();
		progressDialog.setOnCancelListener(new CancelTaskOnCancelListener(this));
	}
	
	@Override
	protected ArrayList<FileItem> doInBackground(String... params) {

		String key = params[0];
        fileLister = new FileLister();
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
		} else {
			if (result != null) {
				filesList.clear();
				for (int i = 0; i < result.size(); i++) {
					filesList.add(result.get(i));
				}
				if (!currentKey.equals(goBack.key)) filesList.add(0, goBack);
		        filesAdapter.notifyDataSetChanged();
			}
			cancel(true);
		}
	}
}
