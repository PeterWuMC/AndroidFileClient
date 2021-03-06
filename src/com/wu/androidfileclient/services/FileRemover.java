package com.wu.androidfileclient.services;

import org.apache.http.HttpStatus;

import com.wu.androidfileclient.models.BaseListItem;
import com.wu.androidfileclient.models.Credential;
import com.wu.androidfileclient.utils.HttpHandler;

public class FileRemover extends Base{

//	protected static final String PROJECT = "cHVibGlj";

	protected static final String OBJECT = "server_files";
	protected static final String ACTION = "";
	protected static final String FORMAT = ".json";

	public FileRemover(Credential credential) {
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
	
	public String constructUrl() {
		return null;
	}

	public boolean delete(BaseListItem baseListItem) {
		String url     = constructUrl(baseListItem.key, baseListItem.projectKey);
		httpHandler    = new HttpHandler(url);
		int statusCode = httpHandler.startDELETEConnection();

		if (statusCode != HttpStatus.SC_OK) return false;

		return true;
	}

}
