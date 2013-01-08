package com.wu.androidfileclient.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class HttpHandler {
	private DefaultHttpClient client;
	private HttpRequestBase request;
	private HttpEntity getResponseEntity;
	String url;
	
	public HttpHandler(String url) {
		this.url = url;
	}
	
	public int startGETConnection() {
		request = new HttpGet(url);

		return startConnection();
	}

	public int startDELETEConnection() {
		request = new HttpDelete(url);

		return startConnection();
	}

	public int startPOSTConnection(List<NameValuePair> parameters) {
		HttpPost request = new HttpPost(url); 
		try {
			request.setEntity(new UrlEncodedFormEntity(parameters));
		} catch (Exception e) {}
		this.request = request;
		return startConnection();
	}
	
	private int startConnection() {
		int statusCode;
		
		HttpResponse getResponse;
		client = new DefaultHttpClient();
		
		try {
			getResponse = client.execute(request);
			statusCode = getResponse.getStatusLine().getStatusCode();
			
			if (statusCode != HttpStatus.SC_OK && statusCode != HttpStatus.SC_ACCEPTED) {
				Log.e(getClass().getSimpleName(), "Error " + statusCode + " for URL " + url);
				getResponseEntity = null;
			}
			else {
				getResponseEntity =  getResponse.getEntity();
			}
		} catch (IOException e) {
			statusCode = HttpStatus.SC_BAD_REQUEST;
		}

		return statusCode;
	}

	public String retrieveEntireResponse() {
		try {
			if (getResponseEntity != null) {
				return EntityUtils.toString(getResponseEntity);
			}
		} catch(IOException e) {
			request.abort();
			Log.w(getClass().getSimpleName(), "Error for URL " + url, e);
		}
		return null;
	}

	public InputStream retrieveStream() {
		try {
			if (getResponseEntity != null) {
				return getResponseEntity.getContent();
			}
		} catch(IOException e) {
			request.abort();
			Log.w(getClass().getSimpleName(), "Error for URL " + url, e);
		}
		return null;
	}
	
	public Long retrieveContentSize() {
		if (getResponseEntity != null) {
			return getResponseEntity.getContentLength();
		}
		return null;
	}
	
	public void closeConnect() {
		client.getConnectionManager().shutdown();
	}
}
