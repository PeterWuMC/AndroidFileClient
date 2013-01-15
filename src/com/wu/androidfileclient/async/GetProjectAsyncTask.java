package com.wu.androidfileclient.async;

import java.util.ArrayList;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.wu.androidfileclient.AllActivities;
import com.wu.androidfileclient.models.FolderItem;
import com.wu.androidfileclient.utils.HttpHandler;
import com.wu.androidfileclient.utils.ProgressDialogHandler;

public class GetProjectAsyncTask  extends CustomAsyncTask<Void, Void, ArrayList<FolderItem>> {

	public GetProjectAsyncTask(AllActivities activity, long reference, String url) {
		super(activity, reference, url, ProgressDialogHandler.RETRIEVING_DATA);
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

}