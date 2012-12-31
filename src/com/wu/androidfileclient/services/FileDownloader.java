package com.wu.androidfileclient.services;

import org.apache.http.HttpException;
import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

import com.wu.androidfileclient.HttpRetriever;
import com.wu.androidfileclient.models.FileItem;

public class FileDownloader extends Base {

	protected static final String OBJECT = "server_files";
	protected static final String ACTION = "download";
	protected static final String FORMAT = "";


	protected String getObjectUrl() {
		return OBJECT;
	}

	protected String getAction() {
		return ACTION;
	}

	protected String getFormat() {
		return FORMAT;
	}

//	public FileItem retrieveFile(String key) throws HttpException {
//		String url      = constructSearchUrl(key);
//		httpRetriever 	= new HttpRetriever(url);
//		int statusCode  = httpRetriever.startConnection();
//
//		if (statusCode != HttpStatus.SC_OK) throw new HttpException(""+statusCode);
//		
//		try {
//			String response = httpRetriever.retrieveEntireResponse();
//			FileItem file   = new FileItem();
//			JSONObject obj  = new JSONObject(response);
//
//            file.path = obj.getString("path");
//            file.key  = obj.getString("key");
//            file.setContent(obj.getString("file_content"));
//
//			return file;
//		} catch (JSONException e) {
//			e.printStackTrace();
//			return null;
//		} finally {
//			httpRetriever.closeConnect();
//		}
//	}
}
