package com.wu.androidfileclient;

import android.app.Activity;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.widget.Toast;

public class LoginActivity extends Activity {
	private String android_id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        android_id = Secure.getString(this.getContentResolver(), Secure.ANDROID_ID);

        longToast(android_id);
    }

    public void longToast(CharSequence message) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}

}
