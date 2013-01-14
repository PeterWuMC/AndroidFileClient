package com.wu.androidfileclient.models;

import java.io.Serializable;
import java.util.HashMap;

import android.os.Build;

public class Credential implements Serializable {

	private static final long serialVersionUID  = -7985043206630478168L;

	public static final String USER_NAME_KEY   = "user_name";
	public static final String PASSWORD_KEY    = "password";
	public static final String DEVICE_CODE_KEY = "device_code";
	public static final String DEVICE_ID_KEY   = "device_id";
	public static final String DEVICE_NAME_KEY = "device_name";

	private HashMap<String, String> credential;
	
	public Credential() {
		credential = new HashMap<String, String>();
	}

	public String getUserName() {
		return credential.get(USER_NAME_KEY) == null ? "" : credential.get(USER_NAME_KEY);
	}

	public String getPassword() {
		return credential.get(PASSWORD_KEY) == null ? "" : credential.get(PASSWORD_KEY);
	}

	public String getDeviceCode() {
		return credential.get(DEVICE_CODE_KEY) == null ? "" : credential.get(DEVICE_CODE_KEY);
	}

	public String getDeviceName() {
		String manufacturer = Build.MANUFACTURER;
		String model = Build.MODEL;
		if (model.startsWith(manufacturer)) {
			return model;
		} else {
			return manufacturer + " " + model;
		}
	}

	public String getDeviceId() {
		return credential.get(DEVICE_ID_KEY) == null ? "" : credential.get(DEVICE_ID_KEY);
	}

	public void set(String key, String value) {
		credential.put(key, value);
	}

	public void setUserName(String userName) {
		credential.put(USER_NAME_KEY, userName);
	}

	public void setPassword(String password) {
		credential.put(PASSWORD_KEY, password);
	}

	public void setDeviceCode(String deviceCode) {
		credential.put(DEVICE_CODE_KEY, deviceCode);
	}

	public void setDeviceId(String deviceId) {
		credential.put(DEVICE_ID_KEY, deviceId);
	}

	@Override
	public String toString() {
		return null;
	}
}
