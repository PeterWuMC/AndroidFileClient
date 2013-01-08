package com.wu.androidfileclient.services;

import com.wu.androidfileclient.models.Credential;

public class FileUploader extends Base{
	protected static final String OBJECT = "folder";
	protected static final String ACTION = "upload";
	protected static final String FORMAT = ".json";

	public FileUploader(Credential credential) {
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
	
	public boolean upload() {
		
		
		
		
		return true;
	}
}
