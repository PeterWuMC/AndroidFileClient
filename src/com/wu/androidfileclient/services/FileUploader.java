package com.wu.androidfileclient.services;

import com.wu.androidfileclient.MainActivity;
import com.wu.androidfileclient.async.PerformUploadFileAsyncTask;
import com.wu.androidfileclient.models.Credential;
import com.wu.androidfileclient.models.FileItem;
import com.wu.androidfileclient.models.FolderItem;

public class FileUploader extends Base{

	protected static final String PROJECT = "cHVibGlj";

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

	public boolean uploadWithProgressUpdate(MainActivity context, FolderItem folderItem, FileItem file) {
		PerformUploadFileAsyncTask task = new PerformUploadFileAsyncTask(context, constructUrl(folderItem.key, folderItem.projectKey));	

		task.execute(file);
		return true;
	}
}
