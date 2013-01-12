package com.wu.androidfileclient.services;

import android.content.Context;

import com.wu.androidfileclient.async.PerformDownloadFileAsyncTask;
import com.wu.androidfileclient.models.Credential;
import com.wu.androidfileclient.models.FileItem;

public class FileDownloader extends Base {

	protected static final String PROJECT = "/projects/cHVibGlj";

	protected static final String OBJECT = "server_files";
	protected static final String ACTION = "download";
	protected static final String FORMAT = "";


	public FileDownloader(Credential credential) {
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

	public void downloadWithProgressUpdate(Context context, FileItem file) {
		PerformDownloadFileAsyncTask task = new PerformDownloadFileAsyncTask(context, constructUrl(file.key));
		task.execute(file);
	}
}
