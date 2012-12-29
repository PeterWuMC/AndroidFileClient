package com.wu.androidfileclient.services;

import java.net.URLEncoder;

public abstract class Base {

	protected static final String BASE_URL = "http://peterwumc.asuscomm.com:8087/";
	protected static final String SLASH = "/";

	protected abstract String getObjectUrl();
	protected abstract String getAction();
	protected abstract String getFormat();

	protected String constructSearchUrl(String key) {
		StringBuffer sb = new StringBuffer();
		sb.append(BASE_URL);
		sb.append(getObjectUrl());
		sb.append(SLASH);
		sb.append(key);
		sb.append(SLASH);
		sb.append(getAction());
		sb.append(getFormat());
		return sb.toString();
	}
	
}
