package com.wu.androidfileclient.async;

import java.io.File;

import org.apache.http.HttpStatus;
import org.apache.http.entity.mime.content.FileBody;
import org.json.JSONObject;

import android.os.AsyncTask;

import com.wu.androidfileclient.MainActivity;
import com.wu.androidfileclient.listeners.CancelTaskOnCancelListener;
import com.wu.androidfileclient.listeners.ProgressListener;
import com.wu.androidfileclient.models.FileItem;
import com.wu.androidfileclient.utils.CustomMultiPartEntity;
import com.wu.androidfileclient.utils.HttpHandler;
import com.wu.androidfileclient.utils.ProgressDialogHandler;
import com.wu.androidfileclient.utils.Utilities;

public class PerformUploadFileAsyncTask extends AsyncTask<FileItem, Integer, FileItem> {
    private MainActivity context;
	private ProgressDialogHandler progressDialog;
	private String url;
	private HttpHandler httpHandler;
	private long totalSize;
	
	public PerformUploadFileAsyncTask(MainActivity context, String url) {
		super();
		this.context   = context;
		this.url       = url;
		progressDialog = new ProgressDialogHandler(context);
		progressDialog.createProgressDialog(ProgressDialogHandler.UPLOADING_FILE);
		progressDialog.setOnCancelListener(new CancelTaskOnCancelListener(this));

		progressDialog.show();
    }

	@Override
	protected FileItem doInBackground(FileItem... params) {
		CustomMultiPartEntity multipartContent;
		int statusCode;

		FileItem file = params[0];
		
		httpHandler = new HttpHandler(url);
		multipartContent = new CustomMultiPartEntity(new ProgressListener() {
			@Override
			public void transferred(long num)
			{
					publishProgress((int) num);
			}
		});
		multipartContent.addPart("file", new FileBody(new File(file.localPath + file.name)));
		totalSize = multipartContent.getContentLength();
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

	@Override
	protected void onCancelled() {
		progressDialog.dismiss();
		Utilities.longToast(context, "Something wrong with your connection...");
	}
	
	protected void onProgressUpdate(Integer... params) {
        progressDialog.setProgress((int)params[0]);
    }

	@Override
	protected void onPostExecute(final FileItem result) {
		progressDialog.dismiss();
		context.refreshList();
	}
}
