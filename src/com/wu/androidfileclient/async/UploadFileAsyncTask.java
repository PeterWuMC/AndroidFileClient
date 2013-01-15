package com.wu.androidfileclient.async;

import java.io.File;

import org.apache.http.HttpStatus;
import org.apache.http.entity.mime.content.FileBody;
import org.json.JSONObject;

import android.os.AsyncTask;

import com.wu.androidfileclient.AllActivities;
import com.wu.androidfileclient.R;
import com.wu.androidfileclient.listeners.CancelTaskOnCancelListener;
import com.wu.androidfileclient.listeners.ProgressListener;
import com.wu.androidfileclient.models.FileItem;
import com.wu.androidfileclient.utils.CustomMultiPartEntity;
import com.wu.androidfileclient.utils.HttpHandler;
import com.wu.androidfileclient.utils.ProgressDialogHandler;
import com.wu.androidfileclient.utils.Utilities;

public class UploadFileAsyncTask extends AsyncTask<FileItem, Integer, FileItem> {

    private AllActivities activity;
    private long reference;
	private String url;

	private ProgressDialogHandler progressDialog;
	
	public UploadFileAsyncTask(AllActivities activity, long reference, String url) {
		super();

		this.activity  = activity;
		this.url       = url;
		this.reference = reference;

		progressDialog = new ProgressDialogHandler(activity);
		progressDialog.createProgressDialog(ProgressDialogHandler.UPLOADING_FILE);
		progressDialog.setOnCancelListener(new CancelTaskOnCancelListener(this));
    }

	@Override
    protected void onPreExecute() {
        super.onPreExecute();
    	progressDialog.show();
    }

	@Override
	protected FileItem doInBackground(FileItem... params) {
		CustomMultiPartEntity multipartContent;
		int statusCode;

		FileItem file = params[0];
		
		HttpHandler httpHandler = new HttpHandler(url);
		multipartContent = new CustomMultiPartEntity(new ProgressListener() {
			@Override
			public void transferred(long num)
			{
					publishProgress((int) num);
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

	@Override
	protected void onCancelled() {
		progressDialog.dismiss();
		Utilities.longToast(activity, R.string.connection_error_toast);
	}
	
	protected void onProgressUpdate(Integer... params) {
        progressDialog.setProgress((int)params[0]);
    }

	@Override
	protected void onPostExecute(final FileItem result) {
		progressDialog.dismiss();
		activity.afterAsyncTaskFinish(AllActivities.UPLOAD_FILE_COMPLETED, reference, result);
	}
}
