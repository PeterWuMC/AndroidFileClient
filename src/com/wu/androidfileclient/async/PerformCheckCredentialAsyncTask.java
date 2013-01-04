package com.wu.androidfileclient.async;

import android.os.AsyncTask;

import com.wu.androidfileclient.LoginActivity;
import com.wu.androidfileclient.models.Credential;
import com.wu.androidfileclient.services.Registration;
import com.wu.androidfileclient.utils.Utilities;

public class PerformCheckCredentialAsyncTask extends AsyncTask<Void, Void, Boolean>{
	private LoginActivity context;
	private Registration registration = new Registration();
	private Credential credential;
	
	public PerformCheckCredentialAsyncTask(LoginActivity context, Credential credential) {
		super();
		this.context    = context;
		this.credential = credential;
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		if (registration.check(credential)) return true;
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
