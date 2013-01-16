package com.wu.androidfileclient.fetchers;

import com.wu.androidfileclient.AllActivities;
import com.wu.androidfileclient.async.DownloadFileAsyncTask;
import com.wu.androidfileclient.models.Credential;
import com.wu.androidfileclient.models.FileItem;

public class FileDownloader extends Base {

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

	public void download(AllActivities activity, long reference, FileItem file) {
		DownloadFileAsyncTask task = new DownloadFileAsyncTask(activity, reference, constructUrl(file.key, file.projectKey));
		task.execute(file);
	}
}
