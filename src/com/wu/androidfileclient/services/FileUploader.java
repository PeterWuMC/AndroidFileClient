package com.wu.androidfileclient.services;

import android.content.Context;

import com.wu.androidfileclient.async.PerformUploadFileAsyncTask;
import com.wu.androidfileclient.models.Credential;
import com.wu.androidfileclient.models.FileItem;

public class FileUploader extends Base{

	protected static final String PROJECT = "/projects/cHVibGlj";

	protected static final String OBJECT = "server_folders";
	protected static final String ACTION = "upload";
	protected static final String FORMAT = "";

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

	protected String getProject() {
		return PROJECT;
	}

	public boolean uploadWithProgressUpdate(Context context, String key, FileItem file) {
		PerformUploadFileAsyncTask task = new PerformUploadFileAsyncTask(context, constructUrl(key));	

		task.execute(file);
		return true;
	}
}
