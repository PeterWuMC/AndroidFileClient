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

import com.wu.androidfileclient.utils.HttpRetriever;

public class Registration extends Base {

	protected static final String OBJECT = "";
	protected static final String ACTION = "registration";
	protected static final String FORMAT = "";

	public Registration() {
		super("","");
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
	
	public String register(String userName, String password, String deviceId) throws HttpException{
		String url                     = constructUrl("");
		httpRetriever 	               = new HttpRetriever(url);
		List<NameValuePair> parameters = new ArrayList<NameValuePair>(4);

		parameters.add(new BasicNameValuePair("user_name", userName));
		parameters.add(new BasicNameValuePair("password", password));
		parameters.add(new BasicNameValuePair("device_name", "Android"));
		parameters.add(new BasicNameValuePair("device_code", deviceId));

		int statusCode = httpRetriever.startPOSTConnection(parameters);

		if (statusCode != HttpStatus.SC_ACCEPTED) throw new HttpException(""+statusCode);

		try {
			String response = httpRetriever.retrieveEntireResponse();
			if (response != null) {
				Log.d(getClass().getSimpleName(), response);
				JSONObject json = new JSONObject(response);
				return json.getString("key");
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		} finally {
			httpRetriever.closeConnect();
		}
		return null;
	}
	
	public boolean check(String userName, String deviceCode) {
		String url                     = constructUrl("") + "/check";
		httpRetriever 	               = new HttpRetriever(url);
		List<NameValuePair> parameters = new ArrayList<NameValuePair>(2);

		parameters.add(new BasicNameValuePair("user_name", userName));
		parameters.add(new BasicNameValuePair("code", deviceCode));

		int statusCode = httpRetriever.startPOSTConnection(parameters);
		
		return (statusCode == HttpStatus.SC_OK);
	}

}
