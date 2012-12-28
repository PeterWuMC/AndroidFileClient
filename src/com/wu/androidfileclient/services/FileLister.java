package com.wu.androidfileclient.services;

import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.wu.androidfileclient.HttpRetriever;
import com.wu.androidfileclient.models.FileItem;


public class FileLister extends Base {
	
	protected static final String OBJECT = "folder";
	protected static final String ACTION = "list";
	
	private FileItem file;
	
	protected HttpRetriever httpRetriever = new HttpRetriever();
	
	protected String getObjectUrl() {
		return OBJECT;
	}
	
	protected String getAction() {
		return ACTION;
	}
	
	
	public ArrayList<FileItem> retrieveFilesList(String key) {
		String url = constructSearchUrl(key);
		String response = httpRetriever.retrieve(url);
		Log.d(getClass().getSimpleName(), response);
		ArrayList<FileItem> fileArray = new ArrayList<FileItem>();
		
		try {
			JSONArray files = new JSONArray(response);
			for (int i = 0; i < files.length(); ++i) {
				file = new FileItem();
                JSONObject rec = files.getJSONObject(i);
                file.type = rec.getString("type");
                file.name = rec.getString("name");
                file.path = rec.getString("path");
                file.key =  rec.getString("key");
                fileArray.add(file);
			}
			return fileArray;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

}
