package com.wu.androidfileclient.async;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.wu.androidfileclient.AllActivities;
import com.wu.androidfileclient.R;
import com.wu.androidfileclient.models.Credential;
import com.wu.androidfileclient.utils.HttpHandler;
import com.wu.androidfileclient.utils.ProgressDialogHandler;
import com.wu.androidfileclient.utils.Utilities;

public class CheckCredentialAsyncTask extends CustomAsyncTask<Credential, Void, Boolean>{

	public CheckCredentialAsyncTask(AllActivities activity, long reference, String url) {
		super(activity, reference, url, ProgressDialogHandler.CHECKING_CREDENTIAL, AllActivities.CHECK_CREDENTIAL);
	}

	@Override
	protected Boolean doInBackground(Credential... params) {
		Credential credential          = params[0];
		HttpHandler httpHandler 	   = new HttpHandler(url);
		List<NameValuePair> parameters = new ArrayList<NameValuePair>(2);

		parameters.add(new BasicNameValuePair(Credential.USER_NAME_KEY, credential.getUserName()));
		parameters.add(new BasicNameValuePair(Credential.DEVICE_CODE_KEY, credential.getDeviceCode()));

		int statusCode = httpHandler.startPOSTConnection(parameters);

		httpHandler.closeConnect();

		if (statusCode != HttpStatus.SC_OK) {
			Utilities.longToast(activity, R.string.credential_not_recognised);
			return false;
		}

		return isCancelled() ? false : true;
	}

}
