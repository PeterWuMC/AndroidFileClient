package com.wu.androidfileclient.services;

import java.util.ArrayList;

import org.apache.http.HttpException;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.wu.androidfileclient.models.BaseListItem;
import com.wu.androidfileclient.models.Credential;
import com.wu.androidfileclient.models.FileItem;
import com.wu.androidfileclient.models.FolderItem;
import com.wu.androidfileclient.utils.HttpHandler;


public class FileLister extends Base {

	protected static final String OBJECT = "folder";
	protected static final String ACTION = "list";
	protected static final String FORMAT = ".json";

	public FileLister(Credential credential) {
		super(credential);
	}

	protected String getObjectUrl() {
		return OBJECT;
	}

	protected String getAction() {
		return ACTION;
	}

	protected String getFormat() {
		return FORMAT;
	}

	public ArrayList<BaseListItem> retrieveFilesList(String key) throws HttpException {
		ArrayList<BaseListItem> fileArray = new ArrayList<BaseListItem>();
		String url                        = constructUrl(key);
		httpHandler 	                  = new HttpHandler(url);
		int statusCode                    = httpHandler.startGETConnection();
		
		if (statusCode != HttpStatus.SC_OK) throw new HttpException(""+statusCode);
		
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
