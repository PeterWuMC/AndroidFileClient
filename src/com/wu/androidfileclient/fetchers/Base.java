package com.wu.androidfileclient.fetchers;

import com.wu.androidfileclient.models.Credential;
import com.wu.androidfileclient.utils.HttpHandler;

public abstract class Base {

	protected static final String BASE_URL = "http://peterwumc.asuscomm.com:8087";
//	protected static final String BASE_URL = "http://192.168.1.11:8087";
//	protected static final String BASE_URL = "http://172.16.10.3:8087";
	protected static final String SLASH = "/";
	protected static final String START_OF_PARAMETERS = "?";
	protected static final String PARAMETERS_SEPARATOR = "&";
	protected static final String EQUAL = "=";

	protected abstract String getObjectUrl();
	protected abstract String getAction();
	protected abstract String getFormat();
	protected Credential credential;

	protected HttpHandler httpHandler;
	
	public Base(Credential credential) {
		this.credential = credential;
	}
	
	public String getCredentialParameters() {
		StringBuffer sb = new StringBuffer();
		sb.append(START_OF_PARAMETERS);
		sb.append(Credential.USER_NAME_KEY);
		sb.append(EQUAL);
		sb.append(credential.getUserName());
		sb.append(PARAMETERS_SEPARATOR);
		sb.append(Credential.DEVICE_CODE_KEY);
		sb.append(EQUAL);
		sb.append(credential.getDeviceCode());
		
		if (!credential.getUserName().isEmpty() && !credential.getDeviceCode().isEmpty()) {
			return  sb.toString();
		} else {
			return "";
		}
	}

	public String constructUrl() {
		return constructUrl("", "");
	}

	public String constructUrl(String key, String projectKey) {
		StringBuffer sb = new StringBuffer();
		sb.append(BASE_URL);
		if (!projectKey.isEmpty()) sb.append(SLASH + "projects" + SLASH);
		sb.append(projectKey);
		if (!getObjectUrl().isEmpty()) sb.append(SLASH);
		sb.append(getObjectUrl());
		if (!key.isEmpty()) sb.append(SLASH);
		sb.append(key);
		if (!getAction().isEmpty()) sb.append(SLASH);
		sb.append(getAction());
		sb.append(getFormat());
		sb.append(getCredentialParameters());
		
		return sb.toString();
	}

}
