package com.wu.androidfileclient.async;

import java.util.ArrayList;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

import com.wu.androidfileclient.AllActivities;
import com.wu.androidfileclient.R;
import com.wu.androidfileclient.listeners.CancelTaskOnCancelListener;
import com.wu.androidfileclient.models.FolderItem;
import com.wu.androidfileclient.utils.HttpHandler;
import com.wu.androidfileclient.utils.ProgressDialogHandler;
import com.wu.androidfileclient.utils.Utilities;

public class GetProjectAsyncTask  extends AsyncTask<Void, Void, ArrayList<FolderItem>> {
	
	private AllActivities activity;
	private long reference;
	private String url;

	private ProgressDialogHandler progressDialog;
	
	public GetProjectAsyncTask(AllActivities activity, long reference, String url) {
		super();

		this.activity  = activity;
		this.reference = reference;
		this.url       = url;
		
		progressDialog = new ProgressDialogHandler(activity);
		progressDialog.createProgressDialog(ProgressDialogHandler.RETRIEVING_DATA);
		progressDialog.setOnCancelListener(new CancelTaskOnCancelListener(this));
    }

	@Override
    protected void onPreExecute() {
        super.onPreExecute();
    	progressDialog.show();
    }

	@Override
	protected ArrayList<FolderItem> doInBackground(Void... params) {
		ArrayList<FolderItem> fileArray = new ArrayList<FolderItem>();
		HttpHandler httpHandler 	    = new HttpHandler(url);
		int statusCode                  = httpHandler.startGETConnection();

		if (statusCode != HttpStatus.SC_OK) cancel(true);

		try {
			String response = httpHandler.retrieveEntireResponse();
			if (response != null) {
				Log.d(getClass().getSimpleName(), response);
				JSONArray folders = new JSONArray(response);
				for (int i = 0; i < folders.length(); ++i) {
	                JSONObject jsonObject = folders.getJSONObject(i);
	                FolderItem listItem = new FolderItem(jsonObject);
	                fileArray.add(listItem);
				}
				return fileArray;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		} finally {
			httpHandler.closeConnect();
		}
		return null;
	}

	@Override
	protected void onCancelled() {
		progressDialog.dismiss();
		Utilities.longToast(activity, R.string.connection_error_toast);
	}

	@Override
	protected void onPostExecute(final ArrayList<FolderItem> result) {
		progressDialog.dismiss();
		activity.afterAsyncTaskFinish(AllActivities.GET_PROJECT_COMPLETED, reference, result);
	}
}