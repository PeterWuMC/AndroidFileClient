package com.wu.androidfileclient;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class HttpRetriever {
	private DefaultHttpClient client;
	private HttpGet getRequest;
	private HttpEntity getResponseEntity;
	String url;
	
	public HttpRetriever(String url) {
		this.url = url;
		try {
			startConnection(url);
		} catch (IOException e) {
			getRequest.abort();
			Log.w(getClass().getSimpleName(), "Error for URL " + url, e);
		}
	}
	
	public void startConnection(String url) throws IOException {
		client = new DefaultHttpClient();
		getRequest = new HttpGet(url);
		
		HttpResponse getResponse = client.execute(getRequest);
		final int statusCode = getResponse.getStatusLine().getStatusCode();

		if (statusCode != HttpStatus.SC_OK) {
			Log.w(getClass().getSimpleName(), "Error " + statusCode + " for URL " + url);
			getResponseEntity = null;
		}
		else {
			getResponseEntity =  getResponse.getEntity();
		}
	}

	public String retrieveEntireResponse() {
		try {
			if (getResponseEntity != null) {
				return EntityUtils.toString(getResponseEntity);
			}
		} catch(IOException e) {
			getRequest.abort();
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
			getRequest.abort();
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
//
//	public Bitmap retrieveBitmap(String url) throws Exception {
//		InputStream inputStream = null;
//		try {
//			inputStream = this.retrieveStream(url);
//			final Bitmap bitmap = BitmapFactory.decodeStream(new FlushedInputStream(inputStream));
//			return bitmap;
//		}
//		finally {
//			Utils.closeStreamQuietly(inputStream);
//		}
//	}
}
