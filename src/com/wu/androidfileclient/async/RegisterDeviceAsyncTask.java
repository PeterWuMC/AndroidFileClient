package com.wu.androidfileclient.async;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.wu.androidfileclient.AllActivities;
import com.wu.androidfileclient.R;
import com.wu.androidfileclient.models.Credential;
import com.wu.androidfileclient.utils.HttpHandler;
import com.wu.androidfileclient.utils.ProgressDialogHandler;
import com.wu.androidfileclient.utils.Utilities;

public class RegisterDeviceAsyncTask extends CustomAsyncTask<Credential, Void, Credential>{

	public RegisterDeviceAsyncTask(AllActivities activity, long reference, String url) {
		super(activity, reference, url, ProgressDialogHandler.LOGGING_IN, AllActivities.REGISTER_DEVICE);
    }

	@Override
	protected Credential doInBackground(Credential... params) {
		Credential credential          = params[0];
		HttpHandler httpHandler 	   = new HttpHandler(url);
		List<NameValuePair> parameters = new ArrayList<NameValuePair>(4);
		Credential new_credential      = new Credential();

		parameters.add(new BasicNameValuePair(Credential.USER_NAME_KEY, credential.getUserName()));
		parameters.add(new BasicNameValuePair(Credential.PASSWORD_KEY, credential.getPassword()));
		parameters.add(new BasicNameValuePair(Credential.DEVICE_NAME_KEY, credential.getDeviceName()));
		parameters.add(new BasicNameValuePair(Credential.DEVICE_ID_KEY, credential.getDeviceId()));

		int statusCode = httpHandler.startPOSTConnection(parameters);

		if (statusCode != HttpStatus.SC_ACCEPTED) Utilities.longToast(activity, R.string.credential_not_recognised);
		
		try {
			String response = httpHandler.retrieveEntireResponse();
			if (response != null) {
				Log.d(getClass().getSimpleName(), response);
				JSONObject json = new JSONObject(response);
				
				new_credential.setUserName(credential.getUserName());
				new_credential.setDeviceCode(json.getString(Credential.DEVICE_CODE_KEY));
				return isCancelled() ? null : new_credential;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} finally {
			httpHandler.closeConnect();
		}
        return null;
	}
	
}
