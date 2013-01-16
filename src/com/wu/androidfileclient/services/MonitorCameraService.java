package com.wu.androidfileclient.services;

import java.io.File;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.FileObserver;
import android.os.IBinder;
import android.util.Base64;
import android.util.Log;

import com.wu.androidfileclient.AllActivities;
import com.wu.androidfileclient.fetchers.FileUploader;
import com.wu.androidfileclient.models.Credential;
import com.wu.androidfileclient.models.FileItem;
import com.wu.androidfileclient.models.FolderItem;
import com.wu.androidfileclient.utils.RecursiveFileObserver;
import com.wu.androidfileclient.utils.Utilities;

public class MonitorCameraService extends Service implements AllActivities {

	private FileObserver observer; 
	private Credential credential;
	private FileUploader fileUploader;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		Log.d("PETER", "CREATED");
		credential   = Utilities.getCredential(this);
		fileUploader = new FileUploader(credential);
	}

	@Override
	public void onDestroy() {
		Log.d("PETER", "DEAD");
		observer.stopWatching();
	}

	@Override
	public void onStart(Intent intent, int startid) {
		Log.d("PETER", "ALIVE");

		final String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();

		observer = new RecursiveFileObserver(path) {
			@Override
            public void onEvent(int event, String filePath) {
				if (event == FileObserver.CREATE) {
					File file          = new File(filePath);
					FileItem fileItem  = new FileItem();
					fileItem.localPath = file.getParentFile().getAbsolutePath() + "/";
					fileItem.name      = file.getName();

			        FolderItem folderItem = new FolderItem();
					folderItem.key        = Base64.encodeToString(("/mobile/" + credential.getDeviceName()).getBytes(), Base64.DEFAULT).replaceAll("[\n\r]", "");
					folderItem.projectKey = Base64.encodeToString(credential.getUserName().getBytes(), Base64.DEFAULT).replaceAll("[\n\r]", "");

					Log.d("PETER", fileItem.localPath + fileItem.name);
					fileUploader.upload(MonitorCameraService.this, 1, false, folderItem, fileItem);
				}	
			}
		};
		observer.startWatching();
	}

	public void onTaskCompleted(int task, long reference, Object result) {
		Log.d("PETER", "UPLOADED " + result.toString());
	}
	public void onTaskCancelled(int task, long reference, Object result) {
		Log.d("PETER", "CANCELLED " + result.toString());
	}
	public Context getContext() { return this; }
	public void runOnUiThread(Runnable runnable) {}

}
