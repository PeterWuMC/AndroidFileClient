package com.wu.androidfileclient.async;

import java.util.ArrayList;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.wu.androidfileclient.AllActivities;
import com.wu.androidfileclient.models.BaseListItem;
import com.wu.androidfileclient.models.FileItem;
import com.wu.androidfileclient.models.FolderItem;
import com.wu.androidfileclient.utils.HttpHandler;
import com.wu.androidfileclient.utils.ProgressDialogHandler;

public class UpdateListAsyncTask extends CustomAsyncTask<BaseListItem, Void, ArrayList<BaseListItem>> {

	public UpdateListAsyncTask(AllActivities activity, long reference, String url) {
		super(activity, reference, url, ProgressDialogHandler.RETRIEVING_DATA, AllActivities.UPDATE_LIST);
    }

	@Override
	protected ArrayList<BaseListItem> doInBackground(BaseListItem... params) {
		ArrayList<BaseListItem> fileArray = new ArrayList<BaseListItem>();
		HttpHandler httpHandler 	      = new HttpHandler(url);
		int statusCode                    = httpHandler.startGETConnection();
		
		if (statusCode != HttpStatus.SC_OK) cancel(true); 
		
		try {
			String response = httpHandler.retrieveEntireResponse();
			if (response != null) {
				Log.d(getClass().getSimpleName(), response);
				JSONArray files = new JSONArray(response);
				for (int i = 0; i < files.length(); ++i) {
	                JSONObject jsonObject = files.getJSONObject(i);
	                BaseListItem listItem = jsonObject.getString("type").equalsIgnoreCase("file") ? new FileItem(jsonObject) : new FolderItem(jsonObject);
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