package com.wu.androidfileclient.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

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
import com.wu.androidfileclient.utils.HttpRetriever;


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
		httpRetriever 	                  = new HttpRetriever(url);
		int statusCode                    = httpRetriever.startGETConnection();
		
		if (statusCode != HttpStatus.SC_OK) throw new HttpException(""+statusCode);
		
		try {
			String response = httpRetriever.retrieveEntireResponse();
			if (response != null) {
				Log.d(getClass().getSimpleName(), response);
				JSONArray files = new JSONArray(response);
				for (int i = 0; i < files.length(); ++i) {
	                JSONObject rec = files.getJSONObject(i);
	                BaseListItem listItem = rec.getString("type").equalsIgnoreCase("file") ? new FileItem() : new FolderItem();
	                
	                listItem.name = rec.getString("name");
	                listItem.path = rec.getString("path");
	                listItem.key  = rec.getString("key");
	                if (listItem instanceof FileItem) {
	                	((FileItem) listItem).size = rec.getLong("size");
	                	try {
//	                		this is very temporary until a better solution is found
	                		((FileItem) listItem).last_modified = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'", Locale.ENGLISH).parse(rec.getString("last_update"));
	                	} catch (Exception e) {
	                		
	                	}
	                }
	                fileArray.add(listItem);
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
