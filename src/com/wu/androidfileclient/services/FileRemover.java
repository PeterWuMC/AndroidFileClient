package com.wu.androidfileclient.services;

import org.apache.http.client.methods.HttpDelete;

import com.wu.androidfileclient.models.Credential;

public class FileRemover extends Base{
	protected static final String OBJECT = "server_files";
	protected static final String ACTION = "";
	protected static final String FORMAT = ".json";
	protected HttpDelete a;

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

}
