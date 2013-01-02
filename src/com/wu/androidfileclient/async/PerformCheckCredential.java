package com.wu.androidfileclient.async;

import java.util.HashMap;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.wu.androidfileclient.LoginActivity;
import com.wu.androidfileclient.listeners.CancelTaskOnCancelListener;
import com.wu.androidfileclient.services.Registration;
import com.wu.androidfileclient.utils.Utilities;

public class PerformCheckCredential extends AsyncTask<Void, Void, Boolean>{
	private LoginActivity context;
	private HashMap<String, String> credential;
	private Registration registration = new Registration();
	
	public PerformCheckCredential(LoginActivity context, HashMap<String, String> credential) {
		super();
		this.context = context;
		this.credential = credential;
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		if (registration.check(credential.get("user_name"), credential.get("device_code"))) return true;
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
