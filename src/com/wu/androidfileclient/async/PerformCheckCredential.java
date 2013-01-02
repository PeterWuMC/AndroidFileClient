package com.wu.androidfileclient.async;

import android.os.AsyncTask;

import com.wu.androidfileclient.LoginActivity;
import com.wu.androidfileclient.services.Registration;
import com.wu.androidfileclient.utils.Utilities;

public class PerformCheckCredential extends AsyncTask<Void, Void, Boolean>{
	private LoginActivity context;
	private Registration registration = new Registration();
	private String userName;
	private String deviceCode;
	
	public PerformCheckCredential(LoginActivity context, String userName, String deviceCode) {
		super();
		this.context    = context;
		this.userName   = userName;
		this.deviceCode = deviceCode;
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		if (registration.check(userName, deviceCode)) return true;
        return false;
	}

	@Override
	protected void onCancelled() {
		Utilities.longToast(context, "Something wrong with your connection...");
		context.openMainActivity(false);
	}

	@Override
	protected void onPostExecute(final Boolean result) {
		context.openMainActivity(result);
	}	
}
