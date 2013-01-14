package com.wu.androidfileclient;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.wu.androidfileclient.models.Credential;
import com.wu.androidfileclient.services.Registration;
import com.wu.androidfileclient.utils.Utilities;

public class LoginActivity extends AllActivities {

	private EditText userNameBox;
	private EditText passwordBox;
	private Button   loginBtn;

	private final Registration registration = new Registration();

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
			registration.check(this, credential);
		}

		loginBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				credential.setUserName(userNameBox.getText().toString());
				credential.setPassword(passwordBox.getText().toString());
				registration.register(LoginActivity.this, credential);
			}
        });
    }
    
    public void afterAsyncTaskFinish(int task, Object object) {
    	switch (task) {
    	case FINISHED_REGISTER_DEVICE:
    		if (object instanceof Credential) {
    			Utilities.saveCredential(this, (Credential) object);
    			registration.check(this, (Credential) object);
    		}
    		break;
    	case FINISHED_CHECK_CREDENTIAL:
    		Intent mainIntent = new Intent(this, MainActivity.class);
    		startActivity(mainIntent);
    		this.finish();
    		break;
    	}
    }
}
