package com.wu.androidfileclient.services;

import java.io.File;
import java.util.HashMap;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.FileObserver;
import android.os.IBinder;
import android.util.Base64;
import android.util.Log;

import com.wu.androidfileclient.AllActivities;
import com.wu.androidfileclient.fetchers.MobileUploader;
import com.wu.androidfileclient.models.Credential;
import com.wu.androidfileclient.models.FileItem;
import com.wu.androidfileclient.models.FolderItem;
import com.wu.androidfileclient.utils.RecursiveFileObserver;
import com.wu.androidfileclient.utils.Utilities;

public class MonitorCameraService extends Service implements AllActivities {

	private FileObserver observer; 
	private Credential credential;
	private MobileUploader mobileUploader;
	private HashMap<String, Integer> filesMap = new HashMap<String, Integer>();

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		credential   = Utilities.getCredential(this);
		mobileUploader = new MobileUploader(credential);
	}

	@Override
	public void onDestroy() {
		observer.stopWatching();
	}

	@Override
	public void onStart(Intent intent, int startid) {
		Log.d("PETER", "STARTED");
		final String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();

		observer = new RecursiveFileObserver(path) {
			@Override
            public void onEvent(int event, String filePath) {
				switch(event) {
				case FileObserver.CREATE:
					filesMap.put(filePath, FileObserver.CREATE);
					break;
				case FileObserver.CLOSE_WRITE:
					if (filesMap.get(filePath) == FileObserver.CREATE) {
						File file = new File(filePath);
						
						FileItem fileItem  = new FileItem();
						fileItem.localPath = file.getParentFile().getAbsolutePath() + "/";
						fileItem.name      = file.getName();

						if (fileItem.ext().equalsIgnoreCase("jpg")) {
							Log.d("PETER", fileItem.localPath + fileItem.name);
							mobileUploader.upload(MonitorCameraService.this, 1, false, fileItem);
						}
					}
					filesMap.put(filePath, FileObserver.CLOSE_WRITE);

					break;
				}
			}
		};
		observer.startWatching();
	}

	public boolean isWifiConnected() {
		ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		return mWifi.isConnected();
	}

	public void onTaskCompleted(int task, long reference, Object result) {Log.d("PETER", "UPLOADED");}
	public void onTaskCancelled(int task, long reference, Object result) {Log.d("PETER", "CANCELLED");}
	public Context getContext() { return this; }
	public void runOnUiThread(Runnable runnable) {}

}
