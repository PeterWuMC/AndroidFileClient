package com.wu.androidfileclient.fetchers;

import com.wu.androidfileclient.AllActivities;
import com.wu.androidfileclient.async.GetProjectAsyncTask;
import com.wu.androidfileclient.models.Credential;

public class PublicPath extends Base {

	protected static final String OBJECT = "public";
	protected static final String ACTION = "";
	protected static final String FORMAT = "";

	public PublicPath() {
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

	public String generatePublicUrl(String publicPath) {
		return constructUrl() + "/" + publicPath;
	}
}
