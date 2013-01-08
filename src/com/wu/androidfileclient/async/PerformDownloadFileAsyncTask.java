package com.wu.androidfileclient.async;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.HttpStatus;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.wu.androidfileclient.listeners.CancelTaskOnCancelListener;
import com.wu.androidfileclient.models.FileItem;
import com.wu.androidfileclient.utils.HttpHandler;
import com.wu.androidfileclient.utils.ProgressDialogHandler;
import com.wu.androidfileclient.utils.Utilities;

public class PerformDownloadFileAsyncTask extends AsyncTask<FileItem, Integer, FileItem> {
    private Context context;
    private ProgressDialogHandler progressDialog;
	private String url;
	
	public PerformDownloadFileAsyncTask(Context context, String url) {
		super();
		this.context = context;
		this.url = url;
		progressDialog = new ProgressDialogHandler(context);
		progressDialog.createProgressDialog(ProgressDialogHandler.DOWNLOADING_FILE);
//		progressDialog.setProgressNumberFormat("%1d / %2d bytes");
    	
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
		fileItem.localPath  = Environment.getExternalStorageDirectory().getPath() + "/wu_files/" + fileItem.path + "/";

		HttpHandler httphandler = new HttpHandler(url);
		int statusCode = httphandler.startGETConnection();
		InputStream tempStrem        = httphandler.retrieveStream();
		InputStream inputStream      = new BufferedInputStream(tempStrem, 8192);

		if (statusCode != HttpStatus.SC_OK) cancel(true);

		if (tempStrem != null) {
    		try {
    			File folder = new File(fileItem.localPath);
    			byte data[] = new byte[1024];
                long total  = 0;

    			if (!folder.exists()) folder.mkdirs();

    			lenghtOfFile = (Long) httphandler.retrieveContentSize();
    			outputStream = new BufferedOutputStream(new FileOutputStream(fileItem.localPath + fileItem.name));

    	    	progressDialog.setMax((int) (long)lenghtOfFile);

                while ((count = inputStream.read(data)) != -1) {
                    total += count;
                    publishProgress((int)total);

                    outputStream.write(data, 0, count);
                }

                publishProgress((int) (long)lenghtOfFile);

                outputStream.flush();
                outputStream.close();
    		
    	    } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
    	    } finally {
    	    	try{
                    inputStream.close();
    	    		inputStream.close();
        	    	httphandler.closeConnect();
        	    }
    	    	catch (Exception e) { Log.e("Error: ", e.getMessage()); }
    	    }
    		return fileItem;
		} else {
			return null;
		}
	}
	
	protected void onProgressUpdate(Integer... params) {
        progressDialog.setProgress((int)params[0]);
    }

	@Override
	protected void onCancelled() {
		progressDialog.dismiss();
		Utilities.longToast(context, "Something wrong with your connection...");
	}

	@Override
	protected void onPostExecute(FileItem fileItem) {
		progressDialog.dismiss();

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

