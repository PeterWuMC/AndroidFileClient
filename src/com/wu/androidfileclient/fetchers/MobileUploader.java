package com.wu.androidfileclient.fetchers;

import com.wu.androidfileclient.AllActivities;
import com.wu.androidfileclient.async.UploadFileAsyncTask;
import com.wu.androidfileclient.models.Credential;
import com.wu.androidfileclient.models.FileItem;

public class MobileUploader extends Base{

	protected static final String OBJECT = "mobile";
	protected static final String ACTION = "upload";
	protected static final String FORMAT = "";

	public MobileUploader(Credential credential) {
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

	public void upload(AllActivities activity, long reference, boolean showDialog, FileItem fileItem) {
		UploadFileAsyncTask task = new UploadFileAsyncTask(activity, reference, showDialog, constructUrl());	
		task.execute(fileItem);
	}

}
