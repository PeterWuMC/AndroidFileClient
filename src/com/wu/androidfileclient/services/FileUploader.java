package com.wu.androidfileclient.services;

import com.wu.androidfileclient.MainActivity;
import com.wu.androidfileclient.async.UploadFileAsyncTask;
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

	public void uploadWithProgressUpdate(MainActivity context, long reference, FolderItem folderItem, FileItem file) {
		UploadFileAsyncTask task = new UploadFileAsyncTask(context, reference, constructUrl(folderItem.key, folderItem.projectKey));	
		task.execute(file);
	}
}
