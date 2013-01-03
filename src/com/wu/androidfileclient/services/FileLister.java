package com.wu.androidfileclient.services;

import java.util.ArrayList;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import org.apache.http.HttpException;

import com.wu.androidfileclient.models.FileItem;
import com.wu.androidfileclient.utils.HttpRetriever;
import com.wu.androidfileclient.utils.Utilities;


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

	public ArrayList<FileItem> retrieveFilesList(String key) throws HttpException {
		ArrayList<FileItem> fileArray = new ArrayList<FileItem>();
		String url                    = constructUrl(key);
		httpRetriever 	              = new HttpRetriever(url);
		int statusCode                = httpRetriever.startGETConnection();
		
		if (statusCode != HttpStatus.SC_OK) throw new HttpException(""+statusCode);
		
		try {
			String response = httpRetriever.retrieveEntireResponse();
			if (response != null) {
				Log.d(getClass().getSimpleName(), response);
				JSONArray files = new JSONArray(response);
				for (int i = 0; i < files.length(); ++i) {
	                JSONObject rec = files.getJSONObject(i);
	                FileItem file  = new FileItem();
	                
	                file.type = rec.getString("type");
	                file.name = rec.getString("name");
	                file.path = rec.getString("path");
	                file.key  = rec.getString("key");
	                file.ext  = Utilities.fileExt(rec.getString("name"));
	                fileArray.add(file);
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
