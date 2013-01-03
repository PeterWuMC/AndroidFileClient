package com.wu.androidfileclient.services;

import java.util.ArrayList;

import org.apache.http.HttpException;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.wu.androidfileclient.models.ListItem;
import com.wu.androidfileclient.models.FileItem;
import com.wu.androidfileclient.models.FolderItem;
import com.wu.androidfileclient.utils.HttpRetriever;


public class FileLister extends Base {

	protected static final String OBJECT = "folder";
	protected static final String ACTION = "list";
	protected static final String FORMAT = ".json";

	public FileLister(String userName, String secretCode) {
		super(userName, secretCode);
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

	public ArrayList<ListItem> retrieveFilesList(String key) throws HttpException {
		ArrayList<ListItem> fileArray = new ArrayList<ListItem>();
		String url                     = constructUrl(key);
		httpRetriever 	               = new HttpRetriever(url);
		int statusCode                 = httpRetriever.startGETConnection();
		
		if (statusCode != HttpStatus.SC_OK) throw new HttpException(""+statusCode);
		
		try {
			String response = httpRetriever.retrieveEntireResponse();
			if (response != null) {
				Log.d(getClass().getSimpleName(), response);
				JSONArray files = new JSONArray(response);
				for (int i = 0; i < files.length(); ++i) {
	                JSONObject rec = files.getJSONObject(i);
	                ListItem object = rec.getString("type").equalsIgnoreCase("file") ? new FileItem() : new FolderItem();
	                
	                object.name = rec.getString("name");
	                object.path = rec.getString("path");
	                object.key  = rec.getString("key");
	                fileArray.add(object);
				}
				return fileArray;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		} finally {
			httpRetriever.closeConnect();
		}
		return null;
	}

}
