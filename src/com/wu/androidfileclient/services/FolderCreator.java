package com.wu.androidfileclient.services;

import com.wu.androidfileclient.AllActivities;
import com.wu.androidfileclient.async.CreateFolderAsyncTask;
import com.wu.androidfileclient.models.Credential;
import com.wu.androidfileclient.models.FolderItem;

public class FolderCreator extends Base {

	protected static final String PROJECT = "cHVibGlj";

	protected static final String OBJECT = "server_folders";
	protected static final String ACTION = "";
	protected static final String FORMAT = ".json";

	public FolderCreator(Credential credential) {
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

	public boolean create_folder(AllActivities activity, long reference, FolderItem folderItem) {
		CreateFolderAsyncTask task = new CreateFolderAsyncTask(activity, reference, constructUrl(folderItem.key, folderItem.projectKey));	

		task.execute(folderItem);
		return true;
	}
}
