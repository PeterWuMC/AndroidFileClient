package com.wu.androidfileclient.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpException;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.wu.androidfileclient.models.Credential;
import com.wu.androidfileclient.utils.HttpHandler;

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
	
	public Credential register(Credential credential) throws HttpException{
		String url                     = constructUrl("");
		httpHandler 	               = new HttpHandler(url);
		List<NameValuePair> parameters = new ArrayList<NameValuePair>(4);
		Credential new_credential      = new Credential();

		parameters.add(new BasicNameValuePair(Credential.USER_NAME_KEY, credential.getUserName()));
		parameters.add(new BasicNameValuePair(Credential.PASSWORD_KEY, credential.getPassword()));
		parameters.add(new BasicNameValuePair(Credential.DEVICE_NAME_KEY, credential.getDeviceName()));
		parameters.add(new BasicNameValuePair(Credential.DEVICE_ID_KEY, credential.getDeviceId()));

		int statusCode = httpHandler.startPOSTConnection(parameters);

		if (statusCode != HttpStatus.SC_ACCEPTED) throw new HttpException(""+statusCode);
		
		try {
			String response = httpHandler.retrieveEntireResponse();
			if (response != null) {
				Log.d(getClass().getSimpleName(), response);
				JSONObject json = new JSONObject(response);
				
				new_credential.setUserName(credential.getUserName());
				new_credential.setDeviceCode(json.getString(Credential.DEVICE_CODE_KEY));
				return new_credential;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return new_credential;
		} finally {
			httpHandler.closeConnect();
		}
		return new_credential;
	}
	
	public boolean check(Credential credential) {
		String url                     = constructUrl("") + "/check";
		httpHandler 	               = new HttpHandler(url);
		List<NameValuePair> parameters = new ArrayList<NameValuePair>(2);

		parameters.add(new BasicNameValuePair(Credential.USER_NAME_KEY, credential.getUserName()));
		parameters.add(new BasicNameValuePair(Credential.DEVICE_CODE_KEY, credential.getDeviceCode()));

		int statusCode = httpHandler.startPOSTConnection(parameters);
		
		return (statusCode == HttpStatus.SC_OK);
	}

}
