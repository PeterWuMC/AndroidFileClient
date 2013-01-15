package com.wu.androidfileclient.services;

import com.wu.androidfileclient.AllActivities;
import com.wu.androidfileclient.async.CheckCredentialAsyncTask;
import com.wu.androidfileclient.async.RegisterDeviceAsyncTask;
import com.wu.androidfileclient.models.Credential;

public class Registration extends Base {


	protected static final String OBJECT = "";
	protected static final String ACTION = "registration";
	protected static final String FORMAT = "";

	public Registration() {
		super(new Credential());
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
	
	public void register(AllActivities activity, long reference, Credential credential) {
		RegisterDeviceAsyncTask task = new RegisterDeviceAsyncTask(activity, reference, constructUrl());
		task.execute(credential);
	}
	
	public void check(AllActivities activity, long reference, Credential credential) {
		CheckCredentialAsyncTask task = new CheckCredentialAsyncTask(activity, reference, constructUrl() + "/check");
		task.execute(credential);
	}

}
