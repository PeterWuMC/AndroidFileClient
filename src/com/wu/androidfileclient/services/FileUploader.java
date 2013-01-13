package com.wu.androidfileclient.services;

import com.wu.androidfileclient.MainActivity;
import com.wu.androidfileclient.async.PerformUploadFileAsyncTask;
import com.wu.androidfileclient.models.Credential;
import com.wu.androidfileclient.models.FileItem;

public class FileUploader extends Base{

	private String project;

	protected static final String PROJECT = "cHVibGlj";

	protected static final String OBJECT = "server_folders";
	protected static final String ACTION = "upload";
	protected static final String FORMAT = "";

	public FileUploader(Credential credential, String project) {
		super(credential);
		this.project = project;
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
		return project;
	}

	public boolean uploadWithProgressUpdate(MainActivity context, String key, FileItem file) {
		PerformUploadFileAsyncTask task = new PerformUploadFileAsyncTask(context, constructUrl(key));	

		task.execute(file);
		return true;
	}
}
