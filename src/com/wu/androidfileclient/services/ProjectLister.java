package com.wu.androidfileclient.services;

import com.wu.androidfileclient.AllActivities;
import com.wu.androidfileclient.async.GetProjectAsyncTask;
import com.wu.androidfileclient.models.Credential;

public class ProjectLister extends Base {

	protected static final String OBJECT = "projects";
	protected static final String ACTION = "list";
	protected static final String FORMAT = ".json";

	public ProjectLister(Credential credential) {
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

	public void retrieveList(AllActivities activity, long reference) {
		GetProjectAsyncTask task = new GetProjectAsyncTask(activity, reference, constructUrl());
		task.execute();
	}

}