package com.wu.androidfileclient.async;

import java.io.File;

import org.apache.http.HttpStatus;
import org.apache.http.entity.mime.content.FileBody;
import org.json.JSONObject;

import com.wu.androidfileclient.AllActivities;
import com.wu.androidfileclient.listeners.ProgressListener;
import com.wu.androidfileclient.models.FileItem;
import com.wu.androidfileclient.utils.CustomMultiPartEntity;
import com.wu.androidfileclient.utils.HttpHandler;
import com.wu.androidfileclient.utils.ProgressDialogHandler;

public class UploadFileAsyncTask extends CustomAsyncTask<FileItem, Integer, FileItem> {

	public UploadFileAsyncTask(AllActivities activity, long reference, String url) {
		super(activity, reference, url, ProgressDialogHandler.UPLOADING_FILE, AllActivities.UPLOAD_FILE);
    }

	@Override
	protected FileItem doInBackground(FileItem... params) {
		CustomMultiPartEntity multipartContent;
		int statusCode;

		FileItem file = params[0];
		
		HttpHandler httpHandler = new HttpHandler(url);
		multipartContent = new CustomMultiPartEntity(new ProgressListener() {
			@Override
			public void transferred(long num) {
				publishProgress((int) num);
			}
			
			@Override
			public boolean isCancelled() {
				return UploadFileAsyncTask.this.isCancelled();
			}
			
		});
		multipartContent.addPart("file", new FileBody(new File(file.localPath + file.name)));
		long totalSize = multipartContent.getContentLength();
		progressDialog.setMax((int) totalSize);

		statusCode = httpHandler.startPOSTConnection(multipartContent);

		if (statusCode != HttpStatus.SC_OK) cancel(true);
		
		String response = httpHandler.retrieveEntireResponse();
		try {
			return new FileItem(new JSONObject(response));
		} catch (Exception e) {
			cancel(true);
		}

        return null;
	}
	
	protected void onProgressUpdate(Integer... params) {
        progressDialog.setProgress((int)params[0]);
    }

}
