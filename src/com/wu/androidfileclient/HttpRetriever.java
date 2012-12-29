package com.wu.androidfileclient;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class HttpRetriever {
	private DefaultHttpClient client;

	public String retrieve(String url) {
		client = new DefaultHttpClient();
		HttpGet getRequest = new HttpGet(url);

		try {
			HttpResponse getResponse = client.execute(getRequest);
			final int statusCode = getResponse.getStatusLine().getStatusCode();

			if (statusCode != HttpStatus.SC_OK) {
				Log.w(getClass().getSimpleName(), "Error " + statusCode + " for URL " + url);
				return null;
			}

			HttpEntity getResponseEntity = getResponse.getEntity();

			if (getResponseEntity != null) {
				return EntityUtils.toString(getResponseEntity);
			}
		} catch (IOException e) {
			getRequest.abort();
			Log.w(getClass().getSimpleName(), "Error for URL " + url, e);

		} finally {
			client.getConnectionManager().shutdown();
		}
		return null;
	}

//	public InputStream retrieveStream(String url) {
//		client = new DefaultHttpClient();
//		HttpGet getRequest = new HttpGet(url);
//
//		try {
//			HttpResponse getResponse = client.execute(getRequest);
//			final int statusCode = getResponse.getStatusLine().getStatusCode();
//
//			if (statusCode != HttpStatus.SC_OK) {
//				Log.w(getClass().getSimpleName(), "Error " + statusCode + " for URL " + url);
//				return null;
//			}
//
//			HttpEntity getResponseEntity = getResponse.getEntity();
//			return getResponseEntity.getContent();
//		} catch(IOException e) {
//			getRequest.abort();
//			Log.w(getClass().getSimpleName(), "Error for URL " + url, e);
//		} finally {
//			client.getConnectionManager().shutdown();
//		}
//
//		return null;
//	}
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
