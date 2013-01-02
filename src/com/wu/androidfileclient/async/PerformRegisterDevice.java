package com.wu.androidfileclient.async;

import org.apache.http.HttpException;

import android.os.AsyncTask;

import com.wu.androidfileclient.LoginActivity;
import com.wu.androidfileclient.services.Registration;
import com.wu.androidfileclient.utils.Utilities;

public class PerformRegisterDevice extends AsyncTask<Void, Void, String>{
	private LoginActivity context;
	private Registration registration = new Registration();
	private String userName;
	private String password;
	private String deviceId;
	
	public PerformRegisterDevice(LoginActivity context, String userName, String password, String deviceId) {
		super();
		this.context = context;
		this.userName = userName;
		this.password = password;
		this.deviceId = deviceId;
	}

	@Override
	protected String doInBackground(Void... params) {
		try {
			return registration.register(userName, password, deviceId);
		} catch (HttpException e) {
        	cancel(true);
		}
        return null;
	}

	@Override
	protected void onCancelled() {
		Utilities.longToast(context, "Something wrong with your connection...");
		context.cancelProgressDialog();
	}

	@Override
	protected void onPostExecute(final String result) {
		context.saveCredential(userName, result);
		context.checkCredential(userName, result);
	}	
}
