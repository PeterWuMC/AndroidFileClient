package com.wu.androidfileclient;

import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.wu.androidfileclient.async.PerformCheckCredential;
import com.wu.androidfileclient.async.PerformRegisterDevice;
import com.wu.androidfileclient.listeners.CancelTaskOnCancelListener;
import com.wu.androidfileclient.utils.Utilities;

public class LoginActivity extends Activity {
	private String android_id;
	private ProgressDialog progressDialog;
	
	private EditText userNameBox;
	private EditText passwordBox;
	private Button   loginBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        HashMap<String, String> credential = Utilities.getCredential(this);
        android_id  = Secure.getString(this.getContentResolver(), Secure.ANDROID_ID);
        userNameBox = (EditText) findViewById(R.id.user_name);
		passwordBox = (EditText) findViewById(R.id.password);
		loginBtn    = (Button) findViewById(R.id.login);

		if (!credential.isEmpty()) {
			checkCredential(credential.get("user_name"), credential.get("device_code"));
		}
		
		loginBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				registerDevice(userNameBox.getText().toString(), passwordBox.getText().toString());
			}
        });
    }

    public void checkCredential(String userName, String deviceCode) {
		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle("Please wait...");
    	progressDialog.setMessage("Checking Credential...");
    	progressDialog.setCancelable(true);

		progressDialog.show();

		PerformCheckCredential task = new PerformCheckCredential(this, userName, deviceCode);
		task.execute();
		progressDialog.setOnCancelListener(new CancelTaskOnCancelListener(task));
    }

    public void registerDevice(String userName, String password) {
		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle("Please wait...");
    	progressDialog.setMessage("Logging in...");
    	progressDialog.setCancelable(true);

		progressDialog.show();

		PerformRegisterDevice task = new PerformRegisterDevice(this, userName, password, android_id);
		task.execute();
		progressDialog.setOnCancelListener(new CancelTaskOnCancelListener(task));
    }
    
    public void openMainActivity(boolean allowed) {
    	if (allowed) {
    		Intent mainIntent = new Intent(this, MainActivity.class);
    		startActivity(mainIntent);
    	}
    	else {
    		Utilities.longToast(this, "Credential not recognised, please login again");
    	}
		cancelProgressDialog();
    }
    
    public void saveCredential(String userName, String deviceCode) {
    	HashMap<String, String> credential = new HashMap<String, String>();
    	credential.put("user_name", userName);
    	credential.put("device_code", deviceCode);
    	Utilities.setCredential(this, credential);
    	cancelProgressDialog();
    }
    
    public void cancelProgressDialog() {
    	if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
    }
}
