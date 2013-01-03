package com.wu.androidfileclient;

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
import com.wu.androidfileclient.models.Credential;
import com.wu.androidfileclient.utils.Utilities;

public class LoginActivity extends Activity {
	private ProgressDialog progressDialog;
	private EditText userNameBox;
	private EditText passwordBox;
	private Button   loginBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Credential credential = Utilities.getCredential(this);
        credential.setDeviceId(Secure.getString(this.getContentResolver(), Secure.ANDROID_ID));

        userNameBox = (EditText) findViewById(R.id.user_name);
		passwordBox = (EditText) findViewById(R.id.password);
		loginBtn    = (Button) findViewById(R.id.login);

		if (!credential.getUserName().isEmpty() && !credential.getDeviceCode().isEmpty()) {
			checkCredential(credential);
		}

		loginBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				credential.setUserName(userNameBox.getText().toString());
				credential.setPassword(passwordBox.getText().toString());
				registerDevice(credential);
			}
        });
    }

    public void checkCredential(Credential credential) {
		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle("Please wait...");
    	progressDialog.setMessage("Checking Credential...");
    	progressDialog.setCancelable(true);

		progressDialog.show();

		PerformCheckCredential task = new PerformCheckCredential(this, credential);
		task.execute();
		progressDialog.setOnCancelListener(new CancelTaskOnCancelListener(task));
    }

    public void registerDevice(Credential credential) {
		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle("Please wait...");
    	progressDialog.setMessage("Logging in...");
    	progressDialog.setCancelable(true);

		progressDialog.show();

		PerformRegisterDevice task = new PerformRegisterDevice(this, credential);
		task.execute();
		progressDialog.setOnCancelListener(new CancelTaskOnCancelListener(task));
    }
    
    public void openMainActivity(boolean allowed) {
    	if (allowed) {
    		Intent mainIntent = new Intent(this, MainActivity.class);
    		startActivity(mainIntent);
    		this.finish();
    	}
    	else {
    		Utilities.longToast(this, "Credential not recognised, please login again");
    	}
		cancelProgressDialog();
    }
    
    public void saveCredential(Credential credential) {
    	Utilities.saveCredential(this, credential);
    	cancelProgressDialog();
    }
    
    public void cancelProgressDialog() {
    	if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
    }
}
