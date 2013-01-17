package com.wu.androidfileclient.async;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.wu.androidfileclient.AllActivities;
import com.wu.androidfileclient.models.BaseArrayList;
import com.wu.androidfileclient.models.BaseListItem;
import com.wu.androidfileclient.models.FileItem;
import com.wu.androidfileclient.models.FolderItem;
import com.wu.androidfileclient.utils.HttpHandler;
import com.wu.androidfileclient.utils.ProgressDialogHandler;

public class UpdateListAsyncTask extends CustomAsyncTask<FolderItem, Void, BaseArrayList> {

	public UpdateListAsyncTask(AllActivities activity, long reference, String url) {
		super(activity, reference, url, ProgressDialogHandler.RETRIEVING_DATA, AllActivities.UPDATE_LIST);
    }

	@Override
	protected BaseArrayList doInBackground(FolderItem... params) {
		FolderItem parent       = params[0];
		BaseArrayList fileArray = new BaseArrayList();
		HttpHandler httpHandler = new HttpHandler(url);
		int statusCode          = httpHandler.startGETConnection();
		
		if (statusCode != HttpStatus.SC_OK) cancel(true); 
		
		try {
			String response = httpHandler.retrieveEntireResponse();
			if (response != null) {
				Log.d(getClass().getSimpleName(), response);
				JSONArray files = new JSONArray(response);
				for (int i = 0; i < files.length(); ++i) {
                	if (isCancelled()) break;

	                JSONObject jsonObject = files.getJSONObject(i);
	                BaseListItem listItem = jsonObject.getString("type").equalsIgnoreCase("file") ? new FileItem(jsonObject, parent) : new FolderItem(jsonObject, parent);
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