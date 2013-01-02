package com.wu.androidfileclient.services;

import com.wu.androidfileclient.utils.HttpRetriever;

public abstract class Base {

	protected static final String BASE_URL = "http://peterwumc.asuscomm.com:8087/";
	protected static final String SLASH = "/";
	protected static final String START_OF_PARAMETERS = "?";
	protected static final String PARAMETERS_SEPARATORS = "&";

	protected abstract String getObjectUrl();
	protected abstract String getAction();
	protected abstract String getFormat();
	protected String userName;
	protected String deviceCode;

	protected HttpRetriever httpRetriever;
	
	public Base(String userName, String deviceCode) {
		this.userName = userName;
		this.deviceCode = deviceCode;
	}
	
	public String getCredentialParameters() {
		if (!userName.isEmpty() && !deviceCode.isEmpty()) {
			return START_OF_PARAMETERS + "user_name=" + userName + PARAMETERS_SEPARATORS + "code=" + deviceCode;
		} else {
			return "";
		}
	}

	public String constructUrl(String key) {
		StringBuffer sb = new StringBuffer();
		sb.append(BASE_URL);
		sb.append(getObjectUrl());
		if (!getObjectUrl().isEmpty()) sb.append(SLASH);
		sb.append(key);
		if (!key.isEmpty()) sb.append(SLASH);
		sb.append(getAction());
		sb.append(getFormat());
		sb.append(getCredentialParameters());
		
		return sb.toString();
	}

}
