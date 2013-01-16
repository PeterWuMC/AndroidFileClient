package com.wu.androidfileclient.fetchers;

import com.wu.androidfileclient.AllActivities;
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

	public void upload(AllActivities activity, long reference, boolean showDialog, FolderItem folderItem, FileItem fileItem) {
		UploadFileAsyncTask task = new UploadFileAsyncTask(activity, reference, showDialog, constructUrl(folderItem.key, folderItem.projectKey));	
		task.execute(fileItem);
	}
}
