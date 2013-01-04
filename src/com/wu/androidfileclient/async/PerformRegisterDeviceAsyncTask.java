package com.wu.androidfileclient.async;

import org.apache.http.HttpException;

import android.os.AsyncTask;

import com.wu.androidfileclient.LoginActivity;
import com.wu.androidfileclient.models.Credential;
import com.wu.androidfileclient.services.Registration;
import com.wu.androidfileclient.utils.Utilities;

public class PerformRegisterDeviceAsyncTask extends AsyncTask<Void, Void, Credential>{
	private LoginActivity context;
	private Registration registration = new Registration();
	private Credential credential;
	
	public PerformRegisterDeviceAsyncTask(LoginActivity context, Credential credential) {
		super();
		this.context = context;
		this.credential = credential;
	}

	@Override
	protected Credential doInBackground(Void... params) {
		try {
			return registration.register(credential);
		} catch (HttpException e) {
        	cancel(true);
		}
        return null;
	}

	@Override
	protected void onCancelled() {
		Utilities.longToast(context, "Something wrong with your connection...");
		context.dismissProgressDialog();
	}

	@Override
	protected void onPostExecute(Credential result) {
		context.saveCredential(result);
		context.checkCredential(result);
	}	
}
