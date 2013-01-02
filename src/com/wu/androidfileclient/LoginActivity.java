package com.wu.androidfileclient;

import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.util.Log;
import android.widget.Toast;

import com.wu.androidfileclient.async.PerformCheckCredential;
import com.wu.androidfileclient.listeners.CancelTaskOnCancelListener;
import com.wu.androidfileclient.utils.Utilities;

public class LoginActivity extends Activity {
	private String android_id;
	private HashMap<String, String> credential = new HashMap<String, String>();
	private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        android_id = Secure.getString(this.getContentResolver(), Secure.ANDROID_ID);

		credential = Utilities.getCredential(this);

		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle("Please wait...");
    	progressDialog.setMessage("Checking Credential...");
    	progressDialog.setCancelable(true);

		progressDialog.show();
		
		if (!credential.isEmpty()) {
			PerformCheckCredential task = new PerformCheckCredential(this, credential);
			task.execute();
			progressDialog.setOnCancelListener(new CancelTaskOnCancelListener(task));
		}
    }

    public void longToast(CharSequence message) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}
    
    public void openMainActivity(boolean allowed) {
    	if (allowed) {
    		Intent mainIntent = new Intent(this, MainActivity.class);
    		startActivity(mainIntent);
    	}
    	else {
    		longToast("Credential not recognised, please login again");
    	}
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
    }
}
