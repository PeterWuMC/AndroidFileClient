package com.wu.androidfileclient.async;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.wu.androidfileclient.AllActivities;
import com.wu.androidfileclient.models.FolderArrayList;
import com.wu.androidfileclient.models.FolderItem;
import com.wu.androidfileclient.utils.HttpHandler;
import com.wu.androidfileclient.utils.ProgressDialogHandler;

public class GetProjectAsyncTask  extends CustomAsyncTask<Void, Void, FolderArrayList> {

	public GetProjectAsyncTask(AllActivities activity, long reference, String url) {
		super(activity, reference, url, ProgressDialogHandler.RETRIEVING_DATA, AllActivities.GET_PROJECT);
    }

	@Override
	protected FolderArrayList doInBackground(Void... params) {
		FolderArrayList fileArray = new FolderArrayList();
		HttpHandler httpHandler   = new HttpHandler(url);
		int statusCode            = httpHandler.startGETConnection();

		if (statusCode != HttpStatus.SC_OK) cancel(true);

		try {
			String response = httpHandler.retrieveEntireResponse();
			if (response != null) {
				Log.d(getClass().getSimpleName(), response);
				JSONArray folders = new JSONArray(response);
				for (int i = 0; i < folders.length(); ++i) {
                	if (isCancelled()) break;

	                JSONObject jsonObject = folders.getJSONObject(i);
	                FolderItem listItem = new FolderItem(jsonObject);
	                fileArray.add(listItem);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			fileArray = null;
		} finally {
			httpHandler.closeConnect();
		}
		return isCancelled() ? null : fileArray;
	}

}