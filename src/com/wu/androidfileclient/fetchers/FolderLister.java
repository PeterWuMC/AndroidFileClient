package com.wu.androidfileclient.fetchers;

import com.wu.androidfileclient.AllActivities;
import com.wu.androidfileclient.async.UpdateListAsyncTask;
import com.wu.androidfileclient.models.Credential;
import com.wu.androidfileclient.models.FolderItem;


public class FolderLister extends Base {

	protected static final String OBJECT = "server_folders";
	protected static final String ACTION = "list";
	protected static final String FORMAT = ".json";

	public FolderLister(Credential credential) {
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

	public void retrieveList(AllActivities activity, long reference, FolderItem folderItem) {
		UpdateListAsyncTask task = new UpdateListAsyncTask(activity, reference, constructUrl(folderItem.key, folderItem.projectKey));
		task.execute(folderItem);
	}

}
