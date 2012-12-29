package com.wu.androidfileclient.services;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.wu.androidfileclient.models.FileItem;


public class FileLister extends Base {

	protected static final String OBJECT = "folder";
	protected static final String ACTION = "list";
	protected static final String FORMAT = ".json";


	protected String getObjectUrl() {
		return OBJECT;
	}

	protected String getAction() {
		return ACTION;
	}

	protected String getFormat() {
		return FORMAT;
	}

	public ArrayList<FileItem> retrieveFilesList(String key) {
		ArrayList<FileItem> fileArray = new ArrayList<FileItem>();
		String url                    = constructSearchUrl(key);
		String response               = httpRetriever.retrieve(url);

		Log.d(getClass().getSimpleName(), response);

		try {
			JSONArray files = new JSONArray(response);
			for (int i = 0; i < files.length(); ++i) {
                JSONObject rec = files.getJSONObject(i);
                FileItem file  = new FileItem();
                
                file.type = rec.getString("type");
                file.name = rec.getString("name");
                file.path = rec.getString("path");
                file.key  = rec.getString("key");
                fileArray.add(file);
			}
			return fileArray;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

}
