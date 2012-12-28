package com.wu.androidfileclient.services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.wu.androidfileclient.HttpRetriever;
import com.wu.androidfileclient.models.FileItem;

public class FileDownloader extends Base {

	protected static final String OBJECT = "server_files";
	protected static final String ACTION = "download";
	
	private FileItem file;
	
	protected HttpRetriever httpRetriever = new HttpRetriever();
	
	protected String getObjectUrl() {
		return OBJECT;
	}
	
	protected String getAction() {
		return ACTION;
	}
	
	public FileItem retrieveFilesList(String key) {
		String url = constructSearchUrl(key);
		String response = httpRetriever.retrieve(url);
		Log.d(getClass().getSimpleName(), response);
		
		try {
			JSONArray files = new JSONArray(response);
			
			file = new FileItem();
            JSONObject rec = files.getJSONObject(0);
            file.path = rec.getString("path");
            file.key =  rec.getString("key");
            file.setContent(rec.getString("file_content"));
            
			return file;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
}
