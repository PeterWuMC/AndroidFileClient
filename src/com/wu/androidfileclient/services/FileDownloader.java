package com.wu.androidfileclient.services;

import android.content.Context;

import com.wu.androidfileclient.async.PerformDownloadFileAsyncTask;
import com.wu.androidfileclient.models.Credential;
import com.wu.androidfileclient.models.FileItem;

public class FileDownloader extends Base {

	private String project;

	protected static final String OBJECT = "server_files";
	protected static final String ACTION = "download";
	protected static final String FORMAT = "";


	public FileDownloader(Credential credential, String project) {
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

	public void downloadWithProgressUpdate(Context context, FileItem file) {
		PerformDownloadFileAsyncTask task = new PerformDownloadFileAsyncTask(context, constructUrl(file.key));
		task.execute(file);
	}
}
