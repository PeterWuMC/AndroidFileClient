package com.wu.androidfileclient.async;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.HttpStatus;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.wu.androidfileclient.listeners.CancelTaskOnCancelListener;
import com.wu.androidfileclient.models.Credential;
import com.wu.androidfileclient.models.FileItem;
import com.wu.androidfileclient.services.FileDownloader;
import com.wu.androidfileclient.utils.HttpRetriever;
import com.wu.androidfileclient.utils.ProgressDialogHandler;
import com.wu.androidfileclient.utils.Utilities;

public class PerformFileDownloadAsyncTask extends AsyncTask<FileItem, String, FileItem> {
    private Context context;
	ProgressDialog progressDialog;
	private Credential credential;
	
	public PerformFileDownloadAsyncTask(Context context, Credential credential) {
		super();
		this.context = context;
		this.credential = credential;
		progressDialog = ProgressDialogHandler.createProgressDialog(context, ProgressDialogHandler.DOWNLOADING_FILE);
    	
		progressDialog.setOnCancelListener(new CancelTaskOnCancelListener(this));
	}

	@Override
    protected void onPreExecute() {
        super.onPreExecute();
    	progressDialog.show();
    }

	@Override
	protected FileItem doInBackground(FileItem... params) {
		int count;
		Long lenghtOfFile;
		OutputStream outputStream;
		
		FileItem fileItem   = params[0];
		String url          = new FileDownloader(credential).constructUrl(fileItem.key);
		fileItem.localPath  = Environment.getExternalStorageDirectory().getPath() + "/wu_files/";

		HttpRetriever httpRetreiever = new HttpRetriever(url);
		int statusCode = httpRetreiever.startGETConnection();
		InputStream tempStrem        = httpRetreiever.retrieveStream();
		InputStream inputStream      = new BufferedInputStream(tempStrem, 8192);

		if (statusCode != HttpStatus.SC_OK) cancel(true);

		if (tempStrem != null) {
    		try {
    			File folder = new File(fileItem.localPath);
    			byte data[] = new byte[1024];
                long total  = 0;

    			if (!folder.exists()) folder.mkdir();

    			lenghtOfFile = (Long) httpRetreiever.retrieveContentSize();
    			outputStream = new BufferedOutputStream(new FileOutputStream(fileItem.localPath + fileItem.name));

                while ((count = inputStream.read(data)) != -1) {
                    total += count;
                    publishProgress(""+(int)((total*100)/lenghtOfFile));

                    outputStream.write(data, 0, count);
                }

                outputStream.flush();
                outputStream.close();
    		
    	    } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
    	    } finally {
    	    	try{
                    inputStream.close();
    	    		inputStream.close();
        	    	httpRetreiever.closeConnect();
        	    }
    	    	catch (Exception e) { Log.e("Error: ", e.getMessage()); }
    	    }
    		return fileItem;
		} else {
			return null;
		}
	}
	
	protected void onProgressUpdate(String... params) {
        progressDialog.setProgress(Integer.parseInt(params[0]));
    }

	@Override
	protected void onCancelled() {
		ProgressDialogHandler.dismissProgressDialog(progressDialog);
		Utilities.longToast(context, "Something wrong with your connection...");
	}

	@Override
	protected void onPostExecute(FileItem fileItem) {
		ProgressDialogHandler.dismissProgressDialog(progressDialog);

		Intent fileViewIntent = new Intent();
		fileViewIntent.setAction(android.content.Intent.ACTION_VIEW);
		if (fileItem != null && !fileItem.localPath.isEmpty()) {
    		File file = new File(fileItem.localPath + fileItem.name);

    		fileViewIntent.setDataAndType(Uri.fromFile(file), MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileItem.ext()));
    		try {
    			context.startActivity(fileViewIntent);
    		} catch (android.content.ActivityNotFoundException e) {
    			Utilities.longToast(context, "No Application found to open this file");
    			Log.e(getClass().getSimpleName(), "Activity Not Found");
    		}
		} else {
			cancel(true);
		}
	}
}

