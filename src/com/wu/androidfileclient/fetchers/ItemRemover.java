package com.wu.androidfileclient.fetchers;

import com.wu.androidfileclient.AllActivities;
import com.wu.androidfileclient.async.DeleteItemAsyncTask;
import com.wu.androidfileclient.models.BaseListItem;
import com.wu.androidfileclient.models.Credential;

public class ItemRemover extends Base{

//	protected static final String PROJECT = "cHVibGlj";

	protected static final String OBJECT = "server_files";
	protected static final String ACTION = "";
	protected static final String FORMAT = ".json";

	public ItemRemover(Credential credential) {
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
	
	public String constructUrl() {
		return null;
	}

	public void delete(AllActivities activity, long reference, BaseListItem baseListItem) {
		DeleteItemAsyncTask task = new DeleteItemAsyncTask(activity, reference, constructUrl(baseListItem.key, baseListItem.projectKey));
		task.execute();
	}

}
