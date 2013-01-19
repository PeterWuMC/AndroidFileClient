package com.wu.androidfileclient.fetchers;

import com.wu.androidfileclient.models.Credential;

public class ThumbnailStreamer extends Base {

	protected static final String OBJECT = "server_files";
	protected static final String ACTION = "thumbnail";
	protected static final String FORMAT = "";

	public ThumbnailStreamer(Credential credential) {
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

}
