package com.wu.androidfileclient.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MonitorCameraService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		
	}

	@Override
	public void onDestroy() {
		
	}

	@Override
	public void onStart(Intent intent, int startid) {
	}

}
