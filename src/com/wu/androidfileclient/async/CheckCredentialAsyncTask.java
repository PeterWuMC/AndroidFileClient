package com.wu.androidfileclient.async;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;

import com.wu.androidfileclient.AllActivities;
import com.wu.androidfileclient.R;
import com.wu.androidfileclient.listeners.CancelTaskOnCancelListener;
import com.wu.androidfileclient.models.Credential;
import com.wu.androidfileclient.utils.HttpHandler;
import com.wu.androidfileclient.utils.ProgressDialogHandler;
import com.wu.androidfileclient.utils.Utilities;

public class CheckCredentialAsyncTask extends AsyncTask<Credential, Void, Boolean>{

	private AllActivities activity;
	private long reference;
	private String url;

	private ProgressDialogHandler progressDialog;

	public CheckCredentialAsyncTask(AllActivities activity, String url) {
		this(activity, 0, url);
	}

	public CheckCredentialAsyncTask(AllActivities activity, long reference, String url) {
		super();

		this.activity   = activity;
		this.url        = url;
		this.reference  = reference;

		progressDialog = new ProgressDialogHandler(activity);
		progressDialog.createProgressDialog(ProgressDialogHandler.CHECKING_CREDENTIAL);
		progressDialog.setOnCancelListener(new CancelTaskOnCancelListener(this));
	}

	@Override
    protected void onPreExecute() {
        super.onPreExecute();
    	progressDialog.show();
    }

	@Override
	protected Boolean doInBackground(Credential... params) {
		Credential credential          = params[0];
		HttpHandler httpHandler 	   = new HttpHandler(url);
		List<NameValuePair> parameters = new ArrayList<NameValuePair>(2);

		parameters.add(new BasicNameValuePair(Credential.USER_NAME_KEY, credential.getUserName()));
		parameters.add(new BasicNameValuePair(Credential.DEVICE_CODE_KEY, credential.getDeviceCode()));

		int statusCode = httpHandler.startPOSTConnection(parameters);

		if (statusCode != HttpStatus.SC_OK) {
			Utilities.longToast(activity, R.string.credential_not_recognised);
			return false;
		}

		return true;
	}

	@Override
	protected void onCancelled() {
		progressDialog.dismiss();
		Utilities.longToast(activity, R.string.connection_error_toast);
	}

	@Override
	protected void onPostExecute(final Boolean result) {
		progressDialog.dismiss();
		activity.afterAsyncTaskFinish(AllActivities.CHECK_CREDENTIAL_COMPLETED, reference, result);
	}
}
