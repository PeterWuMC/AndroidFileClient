package com.wu.androidfileclient.async;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;

import com.wu.androidfileclient.AllActivities;
import com.wu.androidfileclient.listeners.CancelTaskOnCancelListener;
import com.wu.androidfileclient.models.Credential;
import com.wu.androidfileclient.utils.HttpHandler;
import com.wu.androidfileclient.utils.ProgressDialogHandler;
import com.wu.androidfileclient.utils.Utilities;

public class CheckCredentialAsyncTask extends AsyncTask<Void, Void, Boolean>{
	private AllActivities activity;
	private Credential credential;
	private String url;

	private ProgressDialogHandler progressDialog;

	public CheckCredentialAsyncTask(AllActivities activity, Credential credential, String url) {
		super();
		this.activity   = activity;
		this.credential = credential;
		this.url        = url;

		progressDialog = new ProgressDialogHandler(activity);
		progressDialog.createProgressDialog(ProgressDialogHandler.CHECKING_CREDENTIAL);
		progressDialog.setOnCancelListener(new CancelTaskOnCancelListener(this));
		progressDialog.show();
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		HttpHandler httpHandler 	   = new HttpHandler(url);
		List<NameValuePair> parameters = new ArrayList<NameValuePair>(2);

		parameters.add(new BasicNameValuePair(Credential.USER_NAME_KEY, credential.getUserName()));
		parameters.add(new BasicNameValuePair(Credential.DEVICE_CODE_KEY, credential.getDeviceCode()));

		int statusCode = httpHandler.startPOSTConnection(parameters);

		return (statusCode == HttpStatus.SC_OK) ? true : false;
	}

	@Override
	protected void onCancelled() {
		Utilities.longToast(activity, "Something wrong with your connection...");
		progressDialog.dismiss();
	}

	@Override
	protected void onPostExecute(final Boolean result) {
		if (!result) {
			Utilities.longToast(activity, "Credential not recognised, please login again");
		} else {
			activity.afterAsyncTaskFinish(AllActivities.FINISHED_CHECK_CREDENTIAL);
			progressDialog.dismiss();
		}
	}	
}
