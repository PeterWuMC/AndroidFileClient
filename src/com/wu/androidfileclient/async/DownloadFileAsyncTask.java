package com.wu.androidfileclient.async;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.HttpStatus;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.wu.androidfileclient.AllActivities;
import com.wu.androidfileclient.listeners.CancelTaskOnCancelListener;
import com.wu.androidfileclient.models.FileItem;
import com.wu.androidfileclient.utils.HttpHandler;
import com.wu.androidfileclient.utils.ProgressDialogHandler;
import com.wu.androidfileclient.utils.Utilities;

public class DownloadFileAsyncTask extends AsyncTask<FileItem, Integer, FileItem> {

    private AllActivities activity;
    private long reference;
	private String url;

    private ProgressDialogHandler progressDialog;
	
	public DownloadFileAsyncTask(AllActivities activity, long reference, String url) {
		super();

		this.activity  = activity;
		this.reference = reference;
		this.url       = url;

		progressDialog = new ProgressDialogHandler(activity);
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
		Utilities.longToast(activity, "Something wrong with your connection...");
	}

	@Override
	protected void onPostExecute(FileItem result) {
		progressDialog.dismiss();
		activity.afterAsyncTaskFinish(AllActivities.DOWNLOAD_FILE_COMPLETED, reference, result);
	}
}
